package com.zjc.seckilldemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import com.zjc.seckilldemo.exception.GlobalException;
import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.mapper.OrderMapper;
import com.zjc.seckilldemo.pojo.*;
import com.zjc.seckilldemo.service.IGoodsService;
import com.zjc.seckilldemo.service.IOrderService;
import com.zjc.seckilldemo.service.ISeckillGoodsService;
import com.zjc.seckilldemo.service.ISeckillOrderService;
import com.zjc.seckilldemo.util.JsonUtil;
import com.zjc.seckilldemo.util.SM3Util;
import com.zjc.seckilldemo.util.UUIDUtil;
import com.zjc.seckilldemo.vo.GoodsVo;
import com.zjc.seckilldemo.vo.OrderDetailVo;
import com.zjc.seckilldemo.vo.RespBean;
import com.zjc.seckilldemo.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvaho
 * @since 2021-12-12
 */
@Service
@Transactional
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements IOrderService {
    @Autowired
    private ISeckillGoodsService seckillGoodsService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DepositMapper depositMapper;

    @Override
    public Order seckill(User user, GoodsVo goods) {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //秒杀商品表减库存
        SeckillGoods seckillGoods = seckillGoodsService.getOne(new
                QueryWrapper<SeckillGoods>().eq("goods_id", goods.getId()));
        boolean seckillGoodsResult = seckillGoodsService.update(
                new UpdateWrapper<SeckillGoods>().setSql("stock_count = stock_count-1").eq("goods_id",
                        goods.getId()).gt("stock_count", 0));
        // seckillGoodsService.updateById(seckillGoods);
        if (seckillGoods.getStockCount() < 1) {
            //判断是否还有库存
            valueOperations.set("isStockEmpty:" + goods.getId(), "0");
            return null;
        }
        //生成订单
        Order order = new Order();
        order.setUserId(user.getId());
        order.setGoodsId(goods.getId());
        order.setDeliverAddrId(0);
        order.setGoodsName(goods.getGoodsName());
        order.setGoodsCount(1);
        order.setGoodsPrice(seckillGoods.getSeckillPrice());
        order.setOrderChannel(1);
        order.setStatus(0);
        order.setCreateDate(new Date());
        orderMapper.insert(order);
        //生成秒杀订单
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setOrderId(order.getId());
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goods.getId());
        seckillOrderService.save(seckillOrder);
        redisTemplate.opsForValue().set("order:" + user.getId() + ":" +
                        goods.getId(),
                JsonUtil.object2JsonStr(seckillOrder));
        return order;
    }
    /**
     * 订单详情
     * @param orderId
     * @return
     */
    @Override
    public OrderDetailVo detail(Integer orderId) {
        if (null==orderId){
            throw new GlobalException(RespBeanEnum.ORDER_NOT_EXIST);
        }
        Order order = orderMapper.selectById(orderId);
        GoodsVo goodsVo = goodsService.findGoodsVoByGoodsId(order.getGoodsId());
        OrderDetailVo detail = new OrderDetailVo();
        detail.setGoodsVo(goodsVo);
        detail.setOrder(order);
        return detail;
    }

    /**
     * 验证请求地址
     *
     * @param user
     * @param goodsId
     * @param path
     * @return
     */
    @Override
    public boolean checkPath(User user, Integer goodsId, String path) {
        if (user==null || StringUtils.isEmpty(path)){
            return false;
        }
        String redisPath = (String) redisTemplate.opsForValue().get("seckillPath:" + user.getId() + ":" + goodsId);
        return path.equals(redisPath);
    }

    /**
     * 生成秒杀地址
     *
     * @param user
     * @param goodsId
     * @return
     */
    @Override
    public String createPath(User user, Integer goodsId) {
        String str = SM3Util.SM3Encrypt(UUIDUtil.uuid() + "123456");
        redisTemplate.opsForValue().set("seckillPath:" + user.getId() + ":" + goodsId, str, 60, TimeUnit.SECONDS);
        return str;
    }

    /**
     * 兜底方案，手动支付秒杀订单
     * @param user
     * @param orderid
     * @return
     */
    @Override
    public RespBean payseckillOrder(User user, Integer orderid) {
        //未获取到用户Token
        if (user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        String identity = user.getIdentity();
        Deposit deposit = depositMapper.findDepositByIdentity(identity);
        Order order = orderMapper.selectById(orderid);
        //未查询到订单
        if (order==null){
            return RespBean.error(RespBeanEnum.ORDER_NOT_EXIST);
        }
        //订单已支付
        if (order.getStatus() == 2 ){
            return RespBean.error(RespBeanEnum.ORDER_ALREADY_PAYED);
        }
        BigDecimal depositreduced = deposit.getDeposit().subtract(order.getGoodsPrice());
        //余额不足
        if (depositreduced.compareTo(BigDecimal.ZERO) < 0 ){
            return RespBean.error(RespBeanEnum.DEPOSIT_NOT_ENOUGH_TO_PAY_ORDER);
        }
        //扣减余额
        UpdateWrapper<Deposit> updatedeposit = new UpdateWrapper<>();
        updatedeposit.eq("identity",identity).set("deposit",depositreduced);
        Integer i = depositMapper.update(null,updatedeposit);
        //修改订单状态
        UpdateWrapper<Order> updateOrder = new UpdateWrapper<>();
        updateOrder.eq("id",orderid).set("status",1);
        Integer j = orderMapper.update(null,updateOrder);
        //为银行账户添加对应资金
        //TO DO
        //成功
        if (i == 1 && j ==1){
            return RespBean.success();
        }
        //未知错误
        return RespBean.error(RespBeanEnum.ORDER_PAY_FAIL);
    }


}

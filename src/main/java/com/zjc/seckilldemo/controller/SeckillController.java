package com.zjc.seckilldemo.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.seckilldemo.pojo.Order;
import com.zjc.seckilldemo.pojo.SeckillOrder;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IGoodsService;
import com.zjc.seckilldemo.service.IOrderService;
import com.zjc.seckilldemo.service.ISeckillOrderService;
import com.zjc.seckilldemo.vo.GoodsVo;
import com.zjc.seckilldemo.vo.RespBean;
import com.zjc.seckilldemo.vo.RespBeanEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotNull;


@Controller
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private ISeckillOrderService seckillOrderService;
    @Autowired
    private IOrderService orderService;





    @ApiOperation(value = "秒杀操作网页版 传参为用户和商品ID（还未优化存在超卖问题）")
    @RequestMapping("/doSeckill")
    @ResponseBody
    public RespBean doSeckill(User user, Integer goodsId) {
        if (user == null) {
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            return RespBean.error(RespBeanEnum.EMPTY_STOCK);
        }
        //判断是否重复抢购
        SeckillOrder seckillOrder = seckillOrderService.getOne(new
                QueryWrapper<SeckillOrder>().eq("user_id",
                user.getId()).eq(
                "goods_id",
                goodsId));
        if (seckillOrder != null) {
            return RespBean.error(RespBeanEnum.REPEATE_ERROR);
        }
        Order order = orderService.seckill(user, goods);
        return RespBean.success(order);
    }
}

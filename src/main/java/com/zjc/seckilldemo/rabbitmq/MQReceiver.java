package com.zjc.seckilldemo.rabbitmq;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjc.seckilldemo.pojo.SeckillMessage;
import com.zjc.seckilldemo.pojo.SeckillOrder;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IGoodsService;
import com.zjc.seckilldemo.service.IOrderService;
import com.zjc.seckilldemo.util.JsonUtil;
import com.zjc.seckilldemo.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@Slf4j
public class MQReceiver {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    @RabbitListener(queues = "seckillQueue")
    public void receive(String msg) {
        log.info("QUEUE接受消息：" + msg);
        SeckillMessage message = JsonUtil.jsonStr2Object(msg, SeckillMessage.class);
        Integer goodsId = message.getGoodsId();
        User user = message.getUser();
        GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
        //判断库存
        if (goods.getStockCount() < 1) {
            return;
        }
        //判断是否重复抢购
        // SeckillOrder seckillOrder = seckillOrderService.getOne(newQueryWrapper<SeckillOrder> ().eq("user_id",
                //       user.getId()).eq(
                //       "goods_id",
                //       goodsId));
                String seckillOrderJson = (String) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
        if (!StringUtils.isEmpty(seckillOrderJson)) {
            return ;
        }
        orderService.seckill(user, goods);
    }
}
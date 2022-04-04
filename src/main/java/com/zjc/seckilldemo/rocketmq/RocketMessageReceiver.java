package com.zjc.seckilldemo.rocketmq;


import com.zjc.seckilldemo.pojo.Order;
import com.zjc.seckilldemo.pojo.SeckillMessage;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IGoodsService;
import com.zjc.seckilldemo.service.IOrderService;
import com.zjc.seckilldemo.util.JsonUtil;
import com.zjc.seckilldemo.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Slf4j
@Component
@RocketMQMessageListener(topic="TopicTest",consumerGroup = "defaultconsumer")
public class RocketMessageReceiver implements RocketMQListener<String> {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    @Override
    public void onMessage(String s) {
        SeckillMessage message = JsonUtil.jsonStr2Object(s, SeckillMessage.class);
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
        Order seckill = orderService.seckill(user, goods);
        if (seckill != null){
        orderService.payseckillOrder(user,seckill.getId());
        }
    }
}

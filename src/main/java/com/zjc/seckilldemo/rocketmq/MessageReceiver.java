package com.zjc.seckilldemo.rocketmq;

import com.zjc.seckilldemo.pojo.SeckillMessage;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IGoodsService;
import com.zjc.seckilldemo.service.IOrderService;
import com.zjc.seckilldemo.util.JsonUtil;
import com.zjc.seckilldemo.vo.GoodsVo;
import lombok.val;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
@Service

public class MessageReceiver {
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private IOrderService orderService;
    public void main() throws InterruptedException, MQClientException {
        // Instantiate with specified consumer group name.
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("please_rename_unique_group_name");
        // Specify name server addresses.
        consumer.setNamesrvAddr("192.168.10.8:9876");
        // Subscribe one more more topics to consume.
        //订阅主题，后面的*为二级主题，也称为tag
        consumer.subscribe("TopicTest","*");
        // Register callback to execute on arrival of messages fetched from brokers.
        consumer.setVipChannelEnabled(false);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs,
                                                            ConsumeConcurrentlyContext context) {
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                for (MessageExt msg : msgs) {
                    String str = new String(msg.getBody());
                    SeckillMessage seckillMessage = JsonUtil.jsonStr2Object(str, SeckillMessage.class);
                    Integer goodsId = seckillMessage.getGoodsId();
                    User user = seckillMessage.getUser();
                    GoodsVo goods = goodsService.findGoodsVoByGoodsId(goodsId);
                    String seckillOrderJson = (String) redisTemplate.opsForValue().get("order:" + user.getId() + ":" + goodsId);
                    orderService.seckill(user, goods);

                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        //Launch the consumer instance.
        consumer.start();

        System.out.printf("Consumer Started.%n");
    }


}

package com.zjc.seckilldemo.rocketmq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RocketMessageSender {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    public void SendMessage(String msg){
        rocketMQTemplate.asyncSend("TopicTest", msg, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }
}

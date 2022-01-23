package com.zjc.seckilldemo.rocketmq;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NewMessageSender {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    public void SendMessage(String msg){
        rocketMQTemplate.convertAndSend("TopicTest",msg);
    }
}

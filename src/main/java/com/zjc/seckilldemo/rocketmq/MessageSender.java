package com.zjc.seckilldemo.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageSender {
    public void sendMessage(String message) throws Exception{
    //Instantiate with a producer group name.
    DefaultMQProducer producer = new DefaultMQProducer("producer_group1");
    // Specify name server addresses.
        producer.setNamesrvAddr("192.168.10.8:9876");
    //Launch the instance.
        producer.setSendMsgTimeout(8000);
        producer.setVipChannelEnabled(false);
        producer.start();
        //Create a message instance, specifying topic, tag and message body.
        Message msg = new Message("TopicTest" /* Topic */, "TagA" /* Tag */,
                (message).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */);
        //Call send message to deliver message to one of brokers.
        SendResult sendResult = producer.send(msg);
        System.out.printf("%s%n", sendResult);
    //Shut down once the producer instance is not longer in use.
        producer.shutdown();
}
}

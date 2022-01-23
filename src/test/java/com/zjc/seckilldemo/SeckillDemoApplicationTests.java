package com.zjc.seckilldemo;

import com.zjc.seckilldemo.mapper.GoodsMapper;
import com.zjc.seckilldemo.mapper.OrderMapper;
import com.zjc.seckilldemo.mapper.UserMapper;
import com.zjc.seckilldemo.pojo.Order;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.rocketmq.MessageSender;
import com.zjc.seckilldemo.rocketmq.NewMessageSender;
import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.util.UserUtil;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SeckillDemoApplicationTests {
@Autowired
  private   UserMapper userMapper;
@Autowired
 private IUserService iUserService;
@Autowired
private OrderMapper orderMapper;
@Autowired
  private   GoodsMapper goodsMapper;
@Autowired
    private MessageSender messageSender;
@Autowired
private NewMessageSender newMessageSender;
    private UserUtil userUtil;
    @Test
    void contextLoads() throws Exception {

    }

}

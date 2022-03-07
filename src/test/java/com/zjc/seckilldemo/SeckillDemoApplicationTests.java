package com.zjc.seckilldemo;

import com.zjc.seckilldemo.mapper.*;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.rocketmq.RocketMessageSender;
import com.zjc.seckilldemo.service.IDepositService;
import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.vo.UserCallVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.print.Pageable;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

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
private DepositMapper depositMapper;
@Autowired
    private DepositOrderMapper depositOrderMapper;
@Autowired
private RocketMessageSender rocketMessageSender;
@Autowired
private  MethodnameMapper methodnameMapper;
@Autowired
private IDepositService depositService;

    @Test
    void contextLoads() throws Exception {
        System.out.println(methodnameMapper.findInterfaceControlVobyInderfaceName(1));
    }

}



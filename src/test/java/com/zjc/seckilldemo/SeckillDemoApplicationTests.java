package com.zjc.seckilldemo;

import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.mapper.GoodsMapper;
import com.zjc.seckilldemo.mapper.OrderMapper;
import com.zjc.seckilldemo.mapper.UserMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.rocketmq.RocketMessageSender;
import com.zjc.seckilldemo.service.IDepositService;
import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.util.UserUtil;
import com.zjc.seckilldemo.vo.DepositVo;
import com.zjc.seckilldemo.vo.RechargeOrderVo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

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
private RocketMessageSender rocketMessageSender;
@Autowired
private IDepositService depositService;
    @Test
    void contextLoads() throws Exception {
        System.out.println();
    }

}



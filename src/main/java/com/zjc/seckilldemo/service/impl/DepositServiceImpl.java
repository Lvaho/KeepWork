package com.zjc.seckilldemo.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.service.IDepositService;
import com.zjc.seckilldemo.util.OrderUtil;
import com.zjc.seckilldemo.vo.DepositVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvaho
 * @since 2022-02-07
 */
@Service
public class DepositServiceImpl extends ServiceImpl<DepositMapper, Deposit> implements IDepositService {
    @Autowired
    private DepositMapper  depositMapper;

    @Value("${alipay.returnUrl}")
    private String returnUrl;


    @Override
    public int recharge(DepositVo depositVo) {
        String identity = depositVo.getIdentity();
        BigDecimal total = depositVo.getTotal();
        Deposit depositByIdentity = depositMapper.findDepositByIdentity(identity);
        BigDecimal afterdeposit = new BigDecimal(0);
        afterdeposit  = afterdeposit.add(total);
        afterdeposit = afterdeposit.add(depositByIdentity.getDeposit());
        depositByIdentity.setDeposit(afterdeposit);
        return depositMapper.updateDepositByIdentity(depositByIdentity);
    }

    @Override
    public String SendRequestToAlipay(DepositVo depositVo) throws Exception{
        String orderNo = OrderUtil.getOrderNo();
        String identity = depositVo.getIdentity();
        AlipayTradePagePayResponse response = Factory
                .Payment
                .Page()
                .pay(identity, orderNo,depositVo.getTotal().toString(),returnUrl);
        createOrder(depositVo,orderNo);
        return response.body;
    }

    @Override
    public void createOrder(DepositVo depositVo,String orderNo) {
        depositMapper.createOrder(depositVo,orderNo);
    }
}

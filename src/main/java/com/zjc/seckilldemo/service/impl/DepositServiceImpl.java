package com.zjc.seckilldemo.service.impl;

import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IDepositService;
import com.zjc.seckilldemo.util.OrderUtil;
import com.zjc.seckilldemo.vo.DepositVo;
import com.zjc.seckilldemo.vo.RechargeOrderVo;
import com.zjc.seckilldemo.vo.RespBean;
import com.zjc.seckilldemo.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    private DepositMapper depositMapper;
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
    public RespBean SendRequestToAlipay(DepositVo depositVo, User user) throws Exception{
        if (user == null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        if (depositVo.getTotal().intValue() < 0){
            return RespBean.error(RespBeanEnum.CHARGENUM_SMALLER_THAN_ZERO);
        }
        String orderNo = OrderUtil.getOrderNo();
        String identity = depositVo.getIdentity();
        AlipayTradePagePayResponse response = Factory
                .Payment
                .Page()
                .pay(identity, orderNo,depositVo.getTotal().toString(),returnUrl);
        createOrder(depositVo,orderNo);
        //return response.body;
        return RespBean.success(response.body);
    }

    @Override
    public void createOrder(DepositVo depositVo,String orderNo) {
        depositMapper.createOrder(depositVo,orderNo);
    }

    @Override
    public Deposit findDepositByIdentity(String identity) {
        return depositMapper.findDepositByIdentity(identity);
    }

    @Override
    public String checkOrderAndrecharge(String out_trade_no) throws Exception {
        AlipayTradeQueryResponse query = Factory.Payment.
                Common().
                query(out_trade_no);
        RechargeOrderVo rechargeOrderByOrderNo = depositMapper.findRechargeOrderByOrderNo(out_trade_no);
        String identity = rechargeOrderByOrderNo.getIdentity();
        if (query.tradeStatus.equals("TRADE_SUCCESS") & !rechargeOrderByOrderNo.getStatus().equals("2")){
            rechargeOrderByOrderNo.setStatus("2");
            rechargeOrderByOrderNo.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
            depositMapper.updateRechargeOrderByOrderNo(rechargeOrderByOrderNo);
            Deposit depositByIdentity = depositMapper.findDepositByIdentity(identity);
            String total_amount = rechargeOrderByOrderNo.getTotal_amount();
            BigDecimal bigDecimal = BigDecimal.valueOf(Long.valueOf(total_amount));
            BigDecimal deposit = depositByIdentity.getDeposit();
            bigDecimal = bigDecimal.add(deposit);
            depositByIdentity.setDeposit(bigDecimal);
            depositMapper.updateDepositByIdentity(depositByIdentity);
            return "success";
        }
        return "fail";
    }
}
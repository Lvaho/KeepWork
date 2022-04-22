package com.zjc.seckilldemo.service.impl;


import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.payment.app.models.AlipayTradeAppPayResponse;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.facetoface.models.AlipayTradePayResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.mapper.DepositOrderMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IDepositOrderService;
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
import java.util.Map;

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
    @Autowired
    private DepositOrderMapper depositOrderMapper;
    @Autowired
    private IDepositOrderService depositOrderService;

    @Value("${alipay.returnUrl}")
    private String returnUrl;
    /**
     * 余额充值
     * @param depositVo
     * @return
     */
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

    /**
     * 创建支付宝订单
     * @param depositVo
     * @param user
     * @return
     * @throws Exception
     */
    @Override
    public RespBean SendRequestToAlipay(DepositVo depositVo, User user,String returnurl,String notifyUrl) throws Exception{
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
                //.asyncNotify(notifyUrl)
                .pay(identity, orderNo,depositVo.getTotal().toString(),returnUrl);
        depositOrderService.createOrder(depositVo,orderNo);
        //return response.body;
        return RespBean.success(response.body);
    }



    /**
     * 根据身份证查询余额
     * @param identity
     * @return
     */
    @Override
    public Deposit findDepositByIdentity(String identity) {
        return depositMapper.findDepositByIdentity(identity);
    }

    /**
     * 通过友好回调界面查询订单以及根据主动查询的订单状态进行订单充值
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    @Override
    public String checkOrderAndrecharge(String out_trade_no) throws Exception {
        //EasySDK对支付订单状态进行查询
        AlipayTradeQueryResponse query = Factory.Payment.
                Common().
                query(out_trade_no);
        //查询订单
        RechargeOrderVo rechargeOrderByOrderNo = depositOrderMapper.findRechargeOrderByOrderNo(out_trade_no);
        String identity = rechargeOrderByOrderNo.getIdentity();
        if (query.tradeStatus.equals("TRADE_SUCCESS") & !rechargeOrderByOrderNo.getStatus().equals("2")){
            rechargeOrderByOrderNo.setStatus("2");
            rechargeOrderByOrderNo.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
            depositOrderMapper.updateRechargeOrderByOrderNo(rechargeOrderByOrderNo);
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

    @Override
    public String receiveArsycMsg(Map<String, String> params) throws Exception {
        String out_trade_no = params.get("out_trade_no");
        String tradestatus = params.get("trade_status");
        RechargeOrderVo rechargeOrderByOrderNo = depositOrderMapper.findRechargeOrderByOrderNo(out_trade_no);
        String identity = rechargeOrderByOrderNo.getIdentity();
        Boolean verifyNotify = Factory.Payment.Common().verifyNotify(params);
        if (verifyNotify){
            if (tradestatus.equals("TRADE_SUCCESS") & rechargeOrderByOrderNo.getStatus().equals("1")){
                rechargeOrderByOrderNo.setStatus("2");
                rechargeOrderByOrderNo.setTimestamp(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
                depositOrderMapper.updateRechargeOrderByOrderNo(rechargeOrderByOrderNo);
                Deposit depositByIdentity = depositMapper.findDepositByIdentity(identity);
                String total_amount = rechargeOrderByOrderNo.getTotal_amount();
                BigDecimal bigDecimal = BigDecimal.valueOf(Long.valueOf(total_amount));
                BigDecimal deposit = depositByIdentity.getDeposit();
                bigDecimal = bigDecimal.add(deposit);
                depositByIdentity.setDeposit(bigDecimal);
                depositMapper.updateDepositByIdentity(depositByIdentity);
                return "success";
            }
        }else {
            System.out.println("验签不通过");
            return "fail";
        }
        return "fail";
    }

    @Override
    public RespBean getDeposit(User user) {
        if (user==null){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        if (depositMapper.findDepositByIdentity(user.getIdentity())==null){
            Deposit deposit =new Deposit();
            deposit.setIdentity(user.getIdentity());
            deposit.setDeposit(BigDecimal.ZERO);
            depositMapper.insert(deposit);
        }
        return RespBean.success(depositMapper.findDepositByIdentity(user.getIdentity()));
    }

    @Override
    public RespBean generateOrderInfo(User user, BigDecimal chargenum,String notifyUrl) throws Exception {
        String orderNo = OrderUtil.getOrderNo();
        DepositVo depositVo = new DepositVo();
        depositVo.setIdentity(user.getIdentity());
        depositVo.setTotal(chargenum);
        AlipayTradeAppPayResponse query = Factory.Payment
                .App()
                //.asyncNotify(notifyUrl)
                .pay(user.getIdentity(),orderNo,chargenum.toString());
        depositOrderService.createOrder(depositVo,orderNo);
        return RespBean.success(query);
    }
}
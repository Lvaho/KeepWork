package com.zjc.seckilldemo.service;

import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.vo.DepositVo;
import com.zjc.seckilldemo.vo.RespBean;

import java.math.BigDecimal;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvaho
 * @since 2022-02-07
 */
public interface IDepositService extends IService<Deposit> {
    /**
     * 充值数据库余额
     * @param depositVo
     * @return int（1，成功）
     */
    public int recharge(DepositVo depositVo);
    /**
     * 生成支付页面
     * @param depositVo
     * @return RespBean(code,message,obj==>html)
     */
    public RespBean SendRequestToAlipay(DepositVo depositVo, User user,String returnURL,String notifyUrl) throws Exception;
    /**
     * 通过身份证查询余额
     * @param identity
     * @return
     */
    public Deposit findDepositByIdentity(String identity);
    /**
     * 主动查询订单状态来充值
     * @param
     * @return
     */
    public String checkOrderAndrecharge(String out_trade_no) throws Exception;
    /**
     *
     */
    public String receiveArsycMsg(Map<String, String> params) throws Exception;

    /**
     * 获取余额
     * @return
     */
    public RespBean getDeposit(User user);

    /**
     * 处理移动端的余额充值请求，生成OrderInfo并返回
     * @param user
     * @param chargenum
     * @return
     */
    RespBean generateOrderInfo(User user, BigDecimal chargenum,String notifyUrl) throws Exception;
}

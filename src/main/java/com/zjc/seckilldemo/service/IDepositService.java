package com.zjc.seckilldemo.service;

import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.vo.DepositVo;
import com.zjc.seckilldemo.vo.RespBean;

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
    public RespBean SendRequestToAlipay(DepositVo depositVo, User user) throws Exception;
    /**
     * 创建充值订单
     * @param depositVo
     * @return
     */
    public void createOrder(DepositVo depositVo,String orderNo);
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
}

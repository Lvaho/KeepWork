package com.zjc.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.vo.DepositVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvaho
 * @since 2022-02-07
 */
public interface IDepositService extends IService<Deposit> {
    public int recharge(DepositVo depositVo);
    public String SendRequestToAlipay(DepositVo depositVo) throws Exception;
    public void createOrder(DepositVo depositVo,String orderNo);
}

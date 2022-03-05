package com.zjc.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.DepositOrder;
import com.zjc.seckilldemo.vo.DepositVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvaho
 * @since 2022-03-05
 */
public interface IDepositOrderService extends IService<DepositOrder> {
    /**
     * 创建充值订单
     * @param depositVo
     * @return
     */
    public void createOrder(DepositVo depositVo, String orderNo);


}

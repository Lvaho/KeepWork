package com.zjc.seckilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.mapper.DepositOrderMapper;
import com.zjc.seckilldemo.pojo.DepositOrder;
import com.zjc.seckilldemo.service.IDepositOrderService;
import com.zjc.seckilldemo.vo.DepositVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvaho
 * @since 2022-03-05
 */
@Service
public class DepositOrderServiceImpl extends ServiceImpl<DepositOrderMapper, DepositOrder> implements IDepositOrderService {
    @Autowired
    private DepositOrderMapper depositOrderMapper;
    /**
     * 创建数据库充值订单，订单状态为1（未支付）
     * @param depositVo
     * @param orderNo
     */
    @Override
    public void createOrder(DepositVo depositVo, String orderNo) {
    DepositOrder depositOrder = new DepositOrder();
    depositOrder.setIdentity(depositVo.getIdentity());
    depositOrder.setOutTradeNo(orderNo);
    depositOrder.setStatus("1");
    depositOrder.setTotalAmount(depositVo.getTotal());
    depositOrderMapper.insert(depositOrder);
    }
}

package com.zjc.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.seckilldemo.pojo.DepositOrder;
import com.zjc.seckilldemo.vo.DepositVo;
import com.zjc.seckilldemo.vo.RechargeOrderVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvaho
 * @since 2022-03-05
 */
public interface DepositOrderMapper extends BaseMapper<DepositOrder> {
    RechargeOrderVo findRechargeOrderByOrderNo(String orderNo);
    int updateRechargeOrderByOrderNo(RechargeOrderVo rechargeOrderVo);
}

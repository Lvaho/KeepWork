package com.zjc.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.seckilldemo.pojo.Order;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.vo.GoodsVo;
import com.zjc.seckilldemo.vo.OrderDetailVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvaho
 * @since 2021-12-12
 */
public interface IOrderService extends IService<Order> {
    /**
     * 秒杀
     * @param user
     * @param goods
     * @return
     */
    Order seckill(User user, GoodsVo goods);
    /**
     * 订单详情
     * @param orderId
     * @return
     */
    OrderDetailVo detail(Integer orderId);

}

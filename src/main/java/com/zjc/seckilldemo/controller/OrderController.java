package com.zjc.seckilldemo.controller;


import com.zjc.seckilldemo.aop.ScreenAnnotation;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IOrderService;
import com.zjc.seckilldemo.vo.OrderDetailVo;
import com.zjc.seckilldemo.vo.RespBean;
import com.zjc.seckilldemo.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvaho
 * @since 2021-12-12
 */
@Controller
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private IOrderService orderService;
    /**
     * 订单详情
     * @param user
     * @param orderId
     * @return
     */
    @ScreenAnnotation
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    @ResponseBody
    public RespBean detail(User user, Integer orderId){
        if (null==user){
            return RespBean.error(RespBeanEnum.SESSION_ERROR);
        }
        OrderDetailVo detail = orderService.detail(orderId);
        return RespBean.success(detail);
    }
    /**
     * 支付订单
     * @param user
     * @param orderId
     * @Return RespBean
     */
    @RequestMapping(value = "/payorder",method = RequestMethod.POST)
    @ResponseBody
    public RespBean payorder(User user,Integer orderId){
        return orderService.payseckillOrder(user,orderId);
    }
    /**
     * 结算订单
     */
    @RequestMapping(value = "/settlement",method = RequestMethod.POST)
    @ResponseBody
    public RespBean settlement(){return orderService.settlement();}

}

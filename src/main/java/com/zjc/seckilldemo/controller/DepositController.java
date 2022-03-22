package com.zjc.seckilldemo.controller;


import com.alibaba.fastjson.JSONObject;

import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.mapper.DepositOrderMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IDepositService;
import com.zjc.seckilldemo.util.AlipayHttpUtil;
import com.zjc.seckilldemo.vo.DepositVo;
import com.zjc.seckilldemo.vo.RechargeOrderVo;
import com.zjc.seckilldemo.vo.RespBean;
import com.zjc.seckilldemo.vo.RespBeanEnum;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvaho
 * @since 2022-02-07
 */
@Controller
@RequestMapping("/deposit")
public class DepositController {

    @Autowired
    private DepositMapper depositMapper;
    @Autowired
    private IDepositService depositService;
    @Autowired
    private DepositOrderMapper depositOrderMapper;

    /**
     * 跳转余额界面
     *
     * @return html界面
     */
    @ApiOperation(value = "余额界面")
    @RequestMapping(value = "/toDeposit", method = RequestMethod.GET)
    public String todeposit(Model model, User user) {
        String identity = user.getIdentity();
        String name = user.getNickname();
        Deposit depositByIdentity = depositService.findDepositByIdentity(identity);
        model.addAttribute("deposit", depositByIdentity.getDeposit());
        model.addAttribute("name", name);
        return "deposit";
    }

    /**
     * 跳转余额充值
     *
     * @return html界面
     */
    @ApiOperation(value = "充值界面")
    @RequestMapping(value = "/toRecharge", method = RequestMethod.GET)
    public String toRecharge(User user, Model model) {
        model.addAttribute("name", user.getNickname());
        return "recharge";
    }

    /**
     * 跳转商品详情页
     *
     * @param chargenum
     * @param user
     * @return RespBean
     */
    @ApiOperation(value = "充值接口 返回RespBean")
    @RequestMapping(value = "/doRecharge", method = RequestMethod.POST)
    @ResponseBody
    public RespBean doRecharge(User user, BigDecimal chargenum, HttpServletResponse httpServletResponse,HttpServletRequest request) throws Exception {
        DepositVo depositVo = new DepositVo();
        //if (chargenum.intValue() < 0) {
        //    return RespBean.error(RespBeanEnum.CHARGENUM_SMALLER_THAN_ZERO);
        //}
        String requestURL = request.getRequestURL().toString();
        requestURL=requestURL.substring(0,requestURL.length()-10);
        requestURL=requestURL+"checkOrder";
        System.out.println(requestURL);
        depositVo.setIdentity(user.getIdentity());
        depositVo.setTotal(chargenum);
        return depositService.SendRequestToAlipay(depositVo,user,requestURL);
    }

    /**
     * 兜底方案，如果没有接收到异步返回则直接主动查询
     * @param out_trade_no
     * @return
     * @throws Exception
     */
    @ResponseBody
    @RequestMapping(value = "/checkOrder",method = RequestMethod.GET)
    public String checkOrder(String out_trade_no) throws Exception {
            return depositService.checkOrderAndrecharge(out_trade_no);
    }

    /**
     * 接收支付宝的异步回调并且充值余额
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/receiveArsycMsg",method = RequestMethod.POST)
    @ResponseBody
    public String receiveArsycMsg(HttpServletRequest request) throws Exception {
        System.out.println("接收到异步回调");
        Map<String, String> params = AlipayHttpUtil.convertRequestParamsToMap(request);
        return depositService.receiveArsycMsg(params);
    }
    /**
     * 获取余额
     */
    @RequestMapping(value = "/getDeposit",method = RequestMethod.GET)
    @ResponseBody
    public RespBean getDeposit(User user){
        return depositService.getDeposit(user);
    }
}

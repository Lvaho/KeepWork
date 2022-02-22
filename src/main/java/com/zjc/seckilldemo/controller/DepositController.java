package com.zjc.seckilldemo.controller;


import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IDepositService;
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
    public RespBean doRecharge(User user, BigDecimal chargenum, HttpServletResponse httpServletResponse) throws Exception {
        DepositVo depositVo = new DepositVo();
        //if (chargenum.intValue() < 0) {
        //    return RespBean.error(RespBeanEnum.CHARGENUM_SMALLER_THAN_ZERO);
        //}
        depositVo.setIdentity(user.getIdentity());
        depositVo.setTotal(chargenum);
        return depositService.SendRequestToAlipay(depositVo,user);
    }

    @ResponseBody
    @RequestMapping(value = "/checkOrder",method = RequestMethod.POST)
    public String checkOrder(String out_trade_no) throws Exception {
            return depositService.checkOrderAndrecharge(out_trade_no);
    }

    @RequestMapping(value = "/receiveArsycMsg")
    @ResponseBody
    public String receiveArsycMsg(HttpServletRequest request) {
        Map<String, String> params = convertRequestParamsToMap(request);
        try{
            String outTradeNo = params.get("out_trade_no");
            String totalamount = params.get("total_amount");
            String timestamp = params.get("notify_time");
            RechargeOrderVo rechargeOrderByOrderNo = depositMapper.findRechargeOrderByOrderNo(outTradeNo);
            if (rechargeOrderByOrderNo.getOut_trade_no().equals(outTradeNo) & Integer.parseInt(rechargeOrderByOrderNo.getStatus()) == 1 & rechargeOrderByOrderNo.getTotal_amount().equals(totalamount)){
                rechargeOrderByOrderNo.setStatus("2");
                rechargeOrderByOrderNo.setTimestamp(timestamp);
                DepositVo depositVo = new DepositVo();
                BigDecimal bd=new BigDecimal(totalamount);
                depositVo.setTotal(bd);
                String identity = rechargeOrderByOrderNo.getIdentity();
                depositVo.setIdentity(identity);
                depositService.recharge(depositVo);
                depositMapper.updateRechargeOrderByOrderNo(rechargeOrderByOrderNo);
                System.out.println("成功接收回调");
                return "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    // 将request中的参数转换成Map
    private static Map<String, String> convertRequestParamsToMap(HttpServletRequest request) {
        Map<String, String> retMap = new HashMap<String, String>();

        Set<Map.Entry<String, String[]>> entrySet = request.getParameterMap().entrySet();

        for (Map.Entry<String, String[]> entry : entrySet) {
            String name = entry.getKey();
            String[] values = entry.getValue();
            int valLen = values.length;

            if (valLen == 1) {
                retMap.put(name, values[0]);
            } else if (valLen > 1) {
                StringBuilder sb = new StringBuilder();
                for (String val : values) {
                    sb.append(",").append(val);
                }
                retMap.put(name, sb.toString().substring(1));
            } else {
                retMap.put(name, "");
            }
        }
        return retMap;
    }
}

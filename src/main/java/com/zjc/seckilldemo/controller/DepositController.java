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
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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


    @ApiOperation(value = "余额界面")
    @RequestMapping(value = "/toDeposit", method = RequestMethod.GET)
    public String todeposit(Model model, User user) {
        String identity = user.getIdentity();
        String name = user.getNickname();
        Deposit depositByIdentity = depositMapper.findDepositByIdentity(identity);
        model.addAttribute("deposit", depositByIdentity.getDeposit());
        model.addAttribute("name", name);
        return "deposit";
    }

    @ApiOperation(value = "充值界面")
    @RequestMapping(value = "/toRecharge", method = RequestMethod.GET)
    public String toRecharge(User user, Model model) {
        model.addAttribute("name", user.getNickname());
        return "recharge";
    }

    @ApiOperation(value = "充值接口 返回html片段")
    @RequestMapping(value = "/doRecharge", method = RequestMethod.POST)
    @ResponseBody
    public String doRecharge(User user, BigDecimal chargenum) throws Exception {
        DepositVo depositVo = new DepositVo();
        if (chargenum.intValue() < 0) {
            return "ERROR";
        }
        depositVo.setIdentity(user.getIdentity());
        depositVo.setTotal(chargenum);
        return depositService.SendRequestToAlipay(depositVo);
    }

    @RequestMapping(value = "/receiveArsycMsg", method = RequestMethod.POST)
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

@ResponseBody
@RequestMapping("/checkOrder")
public String checkOrder(String out_trade_no) throws AlipayApiException {
    AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipaydev.com/gateway.do","2021000118652337","MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCQq3Oo4SAnGMtZ5kVjoGQ9KsPeFf4vPnzf7FN7c4A3CFMxSwRkUovab9vCm4Of+2lvjQJmqc0zjig3M8di+ufq92eR82WxA0ELHt7vf45j8ZCe3A6oUuvs5swcSlH748+wuQyZxSWD2VxkuBgt7eOb6eorwRyP4e+Nh7r04GvyODVQnaDn22w1r3h8D3auTJC7g4Lsh4yCMRSbzZ/BhcDkWFcEmcFHccysw8rIt36nBolj+hpAzvdu76gsmdlvo2vxIWrpeTdpWBlhoJD8zQWNPdV2RCEn3RtnqUZNCqAzp5ulKkF6/WWdSYSgBPUp++VymF69xqyQLgDbbzUAUP8VAgMBAAECggEBAICyVXDN3OmK8BPoeqAXJjUX3yipTfc/Up1tQhLXfB744/E8+Ao7OLi4u7oS2HQwluoqehFUSvzrILGC+YP/CuOG9fbrnPJsn6dB7jcGtWgJlIsN3mg1ZDXaV4dvQiGf1roy1JfSvv3dFolvD+uD7fBclfoYj80bxByWu2VuY/kUVCklWwCbPpR0/NopH8c6Ecjx3DIIEHrdIgNy5MWG47vQe8kAxeilaJFhl/sMyFW48i+pfb9m5c8secMvy/sj9uVidA4H43Td2GkDzKUyY0OgXh46WGCEumsOrCu41L6wAeVFtHPmtA4GkMo20rZZvP9W4E6pz+l++9BLgcQl2D0CgYEA6+/ea8qwrzkEtYs63loHk/KP0JHVg/Oe3Swhc3zMPXFhDN8VSgHHicaSxKj+0N6ZvOzbeUabJNSAttsStWxgbEWhgLsBBo8L5+Yq9jiJzV9kgI/kWDY/nrQIGY8Qkz+L0jvhtRozEiuI9V+7lla911Kqqh4Dy6DsKZEyFlRG2C8CgYEAnPjHX8SxBexCfubGcE7aBcKXR1HR8MwpPcdW4Y28QtdUie2b1LF9PUnU3AmmG4rrQFLecZXwlRAg7Y1ukn/esd95dmN6W5vorcCyItSr7yJ5NZJk5vYZEn40LiQezR+lfZKu/MEh+Hohw5oF2+k1aGLJWp6H/0Qs4IBIJlX6R/sCgYAR3Q14F9aczbJflHUWlLZyKzFcV05VOULT+B1oQxCrrkPs148jFVNoSRKftTPl2JTHSx+q5P+mIDjGn3uhJN2AFR2PQ5lmR7pdCn5COjGkzQ4s9bgSPbk/aN4cIUrd+ze46TPDpIItZAJwoZjwXwzsmqO1UaRz7HE1DHtAWY0rnQKBgChuyGQ5tONyrNolNX4eQXh0XCUpxSsCW6knHJDywkhxULxKcPZDVaAg0WxwK9IyYVBrSBL37UVoJyMpaA7iUwl8J7w/+Xd5XGDNHtVr9HGfbptG/yiiwWiLC4PLGAf8G9ORofNHCCoJw4KvvMydcH1X3MezyBIPjIOzZ2JAiTxbAoGAIxUpDrhJqcrLITH4H8jmCYkmuoJJpJHsWBNKqYmfxNWvMGhDR/cMlDg3LOoIzqklU1qfvVNnvw5fJhPrejcgUBsBjUqZhVy/l/N2fjSGEV7R413ZmEG1em5su4888KrGEUOxrt/ygFA0J7nyjJYvWQfd61A3oE5IFbHtD+vYcQU=","json","GBK","MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAm2xEcR/nmQ/G1rkbRoI2C2WI99nijkyEpWOszl16KrC/V8MVWS5AqDfwDVtn6s8MXQu9E3tblr1xctHzlGOdMlmYj7Terp8EMruoml3ZkL/SwHxmtrUnHAQCWcwlzlxiQoiYcMMqezbAg5JUYa7m7EZxYjkhe5Qn6zAdsCYB3cWnXSBzlJ9/t17JXhcpiEi6sYSJJuXZt2yi7mVVx1AGwC848i5XTzNxcIgNlNqCty9228s8IkAeKcY+oY74X0o0GBkm90ZCt5RzR/HJJlvun6JmDIecrIZhjz1xjeegTYVnN6zKhTtPvLAivhQVfXPjoMtoCl7h6d2jM1SRvZJKvwIDAQAB","RSA2");
    AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
    JSONObject bizContent = new JSONObject();
    bizContent.put("out_trade_no", out_trade_no);
//bizContent.put("trade_no", "2014112611001004680073956707");
    request.setBizContent(bizContent.toString());
    AlipayTradeQueryResponse response = alipayClient.execute(request);
    if(response.isSuccess()){
        String tradeStatus = response.getTradeStatus();
        if (tradeStatus.equals("TRADE_SUCCESS")){
            SimpleDateFormat sdf = new SimpleDateFormat();// 格式化时间
            sdf.applyPattern("yyyy-MM-dd HH:mm:ss a");// a为am/pm的标记
            Date date = new Date();// 获取当前时间
            RechargeOrderVo rechargeOrderByOrderNo = depositMapper.findRechargeOrderByOrderNo(out_trade_no);
            rechargeOrderByOrderNo.setStatus("2");
            rechargeOrderByOrderNo.setTimestamp(sdf.format(date));
            DepositVo depositVo = new DepositVo();
            BigDecimal bd=new BigDecimal(response.getTotalAmount());
            depositVo.setTotal(bd);
            String identity = rechargeOrderByOrderNo.getIdentity();
            depositVo.setIdentity(identity);
            depositService.recharge(depositVo);
            depositMapper.updateRechargeOrderByOrderNo(rechargeOrderByOrderNo);
            System.out.println("成功充值");
        }
    } else {
        System.out.println("调用失败");
    }
        return "success";
}

}

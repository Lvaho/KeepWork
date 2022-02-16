package com.zjc.seckilldemo.controller;


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



}

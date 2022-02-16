package com.zjc.seckilldemo.controller;


import com.alipay.api.internal.util.AlipaySignature;
import com.zjc.seckilldemo.init.AlipayInit;
import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IDepositService;
import com.zjc.seckilldemo.vo.DepositVo;
import com.zjc.seckilldemo.vo.RespBean;
import io.swagger.annotations.ApiOperation;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    @RequestMapping(value = "/toDeposit",method = RequestMethod.GET)
    public String todeposit(Model model, User user){
        String identity = user.getIdentity();
        String name = user.getNickname();
        Deposit depositByIdentity = depositMapper.findDepositByIdentity(identity);
        model.addAttribute("deposit",depositByIdentity.getDeposit());
        model.addAttribute("name",name);
        return "deposit";
    }

    @ApiOperation(value = "充值界面")
    @RequestMapping(value = "/toRecharge",method = RequestMethod.GET)
    public String toRecharge(User user,Model model){
    model.addAttribute("name",user.getNickname());
        return "recharge";
    }

    @ApiOperation(value = "充值接口 返回html片段")
    @RequestMapping(value = "/doRecharge",method = RequestMethod.POST)
    @ResponseBody
    public String doRecharge(User user, BigDecimal chargenum) throws Exception {
        DepositVo  depositVo = new DepositVo();
        if (chargenum.intValue() < 0){
            return "ERROR";
        }
        depositVo.setIdentity(user.getIdentity());
        depositVo.setTotal(chargenum);
        return depositService.SendRequestToAlipay(depositVo);
    }

    /**
     * 异步通知支付结果
     *
     * @param request
     * @return String
     * @throws
     * @throws
     */
    @PostMapping("/callBack")
    public String alipayNotify(HttpServletRequest request) throws Exception {
// 获取支付宝的请求信息
        Map<String, String> map = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        if(requestParams.isEmpty()) {
            return "failure";
        }
        // 将 Map<String,String[]> 转为 Map<String,String>
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            map.put(name, valueStr);
        }
        // 验签
        //boolean signVerified = AlipaySignature.rsaCheckV1(map, AlipayInit.publicKey, "UTF-8","RSA2");
        // 验签通过
        
        return "failure";
    }

}

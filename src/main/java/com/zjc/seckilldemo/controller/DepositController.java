package com.zjc.seckilldemo.controller;


import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IDepositService;
import com.zjc.seckilldemo.vo.DepositVo;
import com.zjc.seckilldemo.vo.RespBean;
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

}

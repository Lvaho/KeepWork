package com.zjc.seckilldemo.controller;


import com.zjc.seckilldemo.mapper.DepositMapper;
import com.zjc.seckilldemo.pojo.Deposit;
import com.zjc.seckilldemo.pojo.User;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

}

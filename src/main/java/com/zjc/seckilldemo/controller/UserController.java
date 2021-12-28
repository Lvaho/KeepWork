package com.zjc.seckilldemo.controller;



import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.vo.RespBean;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author lvaho
 * @since 2021-11-24
 */
@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private IUserService iUserService;
    @ApiOperation(value = "注册接口 注意：传进来的密码需要已经MD5加密以及加盐 手机号 身份证号都不能重复")
    @RequestMapping("/doRegister")
    @ResponseBody
    public RespBean doRegister(String nickname, String mobile, String identity, String password) {
        return iUserService.register(nickname,mobile,identity,password);
    }
}

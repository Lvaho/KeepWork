package com.zjc.seckilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.exception.GlobalException;
import com.zjc.seckilldemo.mapper.UserMapper;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.util.*;
import com.zjc.seckilldemo.vo.LoginVo;
import com.zjc.seckilldemo.vo.RespBean;
import com.zjc.seckilldemo.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvaho
 * @since 2021-11-24
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements
        IUserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    /**
     * 登录
     *
     * @param loginVo
     * @return
     */
    @Override
    public RespBean login(HttpServletRequest request, HttpServletResponse
            response, LoginVo loginVo) {
        String mobile = loginVo.getMobile();
        String password = loginVo.getPassword();
        //根据手机号获取用户
        User user = userMapper.selectById(mobile);
        if (null == user) {
            throw new GlobalException(RespBeanEnum.LOGINVO_ERROR);
        }
        //校验密码
        if (!SM3Util.formPassToDBPass(password,
                user.getSalt()).equals(user.getPassword())) {
            System.out.println(SM3Util.formPassToDBPass(password,user.getSalt()));
            System.out.println(user.getPassword());
            throw new GlobalException(RespBeanEnum.LOGINVO_ERROR);
        }
        //生成cookie
        String ticket = UUIDUtil.uuid();
        redisTemplate.opsForValue().set("user:" + ticket, JsonUtil.object2JsonStr(user));
        CookieUtil.setCookie(request, response, "userTicket", ticket);
        return RespBean.success(ticket);
    }

    /**
     * 根据cookie获取用户
     * @param userTicket
     * @param request
     * @param response
     * @return
     */

    @Override
    public User getByUserTicket(String userTicket, HttpServletRequest request,
                                HttpServletResponse response) {
        if (StringUtils.isEmpty(userTicket)) {
            return null;
        }
        String userJson = (String) redisTemplate.opsForValue().get("user:" +
                userTicket);
        User user = JsonUtil.jsonStr2Object(userJson, User.class);
        if (null != user) {
            CookieUtil.setCookie(request,response,"userTicket",userTicket);
        }
        return user;
    }
    /**
     * 用户注册
     * @param nickname
     * @param mobile "ONLY ONE"
     * @param identity "ONLY ONE"
     * @param password "ALREADY MD5 AND SALT"
     * @return
     */
    @Override
    public RespBean register(@NotNull String nickname, @NotNull String mobile, @NotNull String identity, @NotNull String password) {
        User user=new User();
        user.setId(mobile);
        user.setSalt("1a2b3c4d");
        user.setNickname(nickname);
        user.setPassword(MD5Util.formPassToDBPass(password, user.getSalt()));
        user.setIdentity(identity);
        if (userMapper.selectUserByIdentity(identity) != null){
            return RespBean.error(RespBeanEnum.ID_ALREADY_REGISTER);
        }else if(userMapper.selectById(mobile) != null) {
            return RespBean.error(RespBeanEnum.MOBILE_ALREADY_REGISTER);
        }else {userMapper.insert(user);}
        return RespBean.success("注册成功");
    }
}


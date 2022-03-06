package com.zjc.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.vo.LoginVo;
import com.zjc.seckilldemo.vo.RespBean;
import com.zjc.seckilldemo.vo.ViolationRecordVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvaho
 * @since 2021-11-24
 */
public interface IUserService extends IService<User> {
    /**
     * 登录
     * @param loginVo
     * @return
     */
    RespBean login(HttpServletRequest request, HttpServletResponse response,
                   LoginVo loginVo);
    /**
     * 根据cookie获取用户
     * @param userTicket
     * @param request
     * @param response
     * @return
     */
    User getByUserTicket(String userTicket,HttpServletRequest request,HttpServletResponse response);

    /**
     * 注册用户
     * @param nickname
     * @param mobile
     * @param identity
     * @param password
     * @return
     */
    RespBean register(String nickname, String mobile, String identity, String password);

    /**
     * 查询用户不符合条件的记录
     * @param identity
     */
    List<ViolationRecordVo> findViolationRecordVobyIdentity(String identity);
    /**
     * 存储用户的调用接口的记录
     */
    void recordScreenResult(boolean violation, String identity, String method_name, Timestamp time);
}

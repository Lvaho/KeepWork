package com.zjc.seckilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.service.IUserDetailService;
import com.zjc.seckilldemo.pojo.UserDetail;
import com.zjc.seckilldemo.mapper.UserDetailMapper;
import com.zjc.seckilldemo.vo.RespBean;
import com.zjc.seckilldemo.vo.RespBeanEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvaho
 * @since 2022-03-02
 */
@Service
public class UserDetailServiceImpl extends ServiceImpl<UserDetailMapper, UserDetail> implements IUserDetailService {

    @Autowired
    private UserDetailMapper userDetailMapper;

    /**
     * 根据数据库中取到的用户信息进行判断
     * 需要为通过才能正常返回
     * @param identity
     * @return RespBean
     */
    @Override
    public RespBean screen(String identity) {
        UserDetail userDetailbyIdentity = userDetailMapper.findUserDetailbyIdentity(identity);
        if (!userDetailbyIdentity.getDefaulter().equals("通过")){
            return RespBean.error(RespBeanEnum.USER_DEFAULTER);
        }else if (!userDetailbyIdentity.getWorkstate().equals("通过")){
            return RespBean.error(RespBeanEnum.USER_WORKSTATR_BAD);
        }else if (!userDetailbyIdentity.getOverdue().equals("通过")){
            return RespBean.error(RespBeanEnum.USER_OVERDUE);
        }
        return RespBean.success();
    }
}

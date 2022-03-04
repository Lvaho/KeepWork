package com.zjc.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.seckilldemo.pojo.UserDetail;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvaho
 * @since 2022-03-02
 */

public interface UserDetailMapper extends BaseMapper<UserDetail> {
    public UserDetail findUserDetailbyIdentity(String identity);
}

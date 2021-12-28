package com.zjc.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.seckilldemo.pojo.User;


/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvaho
 * @since 2021-11-24
 */
public interface UserMapper extends BaseMapper<User> {
    public User selectUserByIdentity(String identity);
}

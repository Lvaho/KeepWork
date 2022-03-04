package com.zjc.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.seckilldemo.pojo.UserDetail;
import com.zjc.seckilldemo.vo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvaho
 * @since 2022-03-02
 */
public interface IUserDetailService extends IService<UserDetail> {
    RespBean screen(String identity);
}

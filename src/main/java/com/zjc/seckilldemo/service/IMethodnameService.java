package com.zjc.seckilldemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.seckilldemo.pojo.Methodname;
import com.zjc.seckilldemo.vo.InterfaceControlVo;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvaho
 * @since 2022-03-05
 */
public interface IMethodnameService extends IService<Methodname> {
    public List<InterfaceControlVo> findInterfaceControlVobyInterfaceName(String method_name);
    public boolean checkIfInterfaceNeedScreen(String method_name);
}

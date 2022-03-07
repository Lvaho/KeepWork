package com.zjc.seckilldemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.seckilldemo.pojo.Methodname;
import com.zjc.seckilldemo.vo.InterfaceControlVo;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author lvaho
 * @since 2022-03-05
 */
public interface MethodnameMapper extends BaseMapper<Methodname> {
    public List<InterfaceControlVo> findInterfaceControlVobyInderfaceName(Integer method_id);
}

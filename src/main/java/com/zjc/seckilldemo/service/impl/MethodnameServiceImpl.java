package com.zjc.seckilldemo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.seckilldemo.mapper.MethodnameMapper;
import com.zjc.seckilldemo.pojo.Methodname;
import com.zjc.seckilldemo.service.IMethodnameService;
import com.zjc.seckilldemo.vo.InterfaceControlVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author lvaho
 * @since 2022-03-05
 */
@Service
public class MethodnameServiceImpl extends ServiceImpl<MethodnameMapper, Methodname> implements IMethodnameService {

    @Autowired
    private MethodnameMapper methodnameMapper;

    @Override
    public List<InterfaceControlVo> findInterfaceControlVobyInterfaceName(String method_name) {
        return methodnameMapper.findInterfaceControlVobyInderfaceName(method_name);
    }

    public boolean checkIfInterfaceNeedScreen(String method_name){
        Map<String,Object> map = new HashMap<>();
        map.put("method_name",method_name);
        if (methodnameMapper.selectByMap(map) != null) {
            return true;
        }
        return false;
    }
}

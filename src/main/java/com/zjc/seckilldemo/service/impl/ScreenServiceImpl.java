package com.zjc.seckilldemo.service.impl;


import com.zjc.seckilldemo.service.IMethodnameService;
import com.zjc.seckilldemo.service.IScreenService;
import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.util.ListUtil;
import com.zjc.seckilldemo.vo.InterfaceControlVo;
import com.zjc.seckilldemo.vo.RespBean;
import com.zjc.seckilldemo.vo.RespBeanEnum;
import com.zjc.seckilldemo.vo.ViolationRecordVo;
import org.apache.ibatis.annotations.Select;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScreenServiceImpl  implements IScreenService {

    @Autowired
    private IMethodnameService methodnameService;

    @Autowired
    private IUserService userService;

    @Override
    public Object screen(String method_name, String identity, String AOPTargetClassReturnType, ProceedingJoinPoint pjp) throws Throwable {
        if (methodnameService.checkIfInterfaceNeedScreen(method_name)) {
            List<InterfaceControlVo> interfaceControlVobyInterfaceName = methodnameService.findInterfaceControlVobyInterfaceName(method_name);
            List<ViolationRecordVo> violationRecordVobyIdentity = userService.findViolationRecordVobyIdentity(identity);
            Iterator<InterfaceControlVo> controlVoIterator = interfaceControlVobyInterfaceName.iterator();
            Iterator<ViolationRecordVo> recordVoIterator = violationRecordVobyIdentity.iterator();
            StringBuilder controlVo = new StringBuilder();
            StringBuilder recordVo = new StringBuilder();
            boolean violation = false;
            while (controlVoIterator.hasNext()){
                controlVo.append(controlVoIterator.next().getScreen_id());
            }
            while (recordVoIterator.hasNext()){
                recordVo.append(recordVoIterator.next().getScreen_id());
            }
            for (int i =0; i < controlVo.length();i++){
                for (int j = 0; j<recordVo.length(); j++)
                if (controlVo.charAt(i)==recordVo.charAt(j)){
                violation=true;
                    break;
                }
            }
            if (violation){
                recordScreenResult(violation,identity);
                if (AOPTargetClassReturnType.contains("RespBean")){
                    return RespBean.error(RespBeanEnum.USER_ACTION_REFUSE);
                }else if (AOPTargetClassReturnType.contains("String")){
                    return "useractionrefuse";
                }
                return null;
            }
            recordScreenResult(violation,identity);
            return pjp.proceed();
        }
        return pjp.proceed();
    }

    @Override
    public int recordScreenResult(boolean violation, String identity) {
        return 0;
    }

}
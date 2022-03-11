package com.zjc.seckilldemo.service.impl;


import com.zjc.seckilldemo.service.IMethodnameService;
import com.zjc.seckilldemo.service.IScreenService;
import com.zjc.seckilldemo.service.IUserService;
import com.zjc.seckilldemo.vo.*;
import org.apache.ibatis.annotations.Select;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ScreenServiceImpl  implements IScreenService {

    @Autowired
    private IMethodnameService methodnameService;

    @Autowired
    private IUserService userService;


    /**
     * 准入初筛最主要的部分
     * 流程图在文档里面
     * @param method_name
     * @param identity
     * @param AOPTargetClassReturnType
     * @param pjp
     * @return
     * @throws Throwable
     */
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
                recordScreenResult(violation,identity,method_name);
                if (AOPTargetClassReturnType.contains("RespBean")){
                    return RespBean.error(RespBeanEnum.USER_ACTION_REFUSE);
                }else if (AOPTargetClassReturnType.contains("String")){
                    return "useractionrefuse";
                }
                return null;
            }
            recordScreenResult(violation,identity,method_name);
            return pjp.proceed();
        }
        return pjp.proceed();
    }

    /**
     * 记录筛选结果
     * @param violation
     * @param identity
     * @param method_name
     */
    @Override
    public void recordScreenResult(boolean violation, String identity,String method_name) {
        Timestamp time1 = new Timestamp(System.currentTimeMillis());
        userService.recordScreenResult(violation,identity,method_name,time1);

    }

}
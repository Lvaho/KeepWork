package com.zjc.seckilldemo.aop;


import com.zjc.seckilldemo.pojo.User;
import com.zjc.seckilldemo.service.IUserDetailService;
import com.zjc.seckilldemo.vo.RespBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


@Component
@Aspect
public class ScreenAop {
    @Value("${Screenswitch}")
    private String Screenswitch;

    @Autowired
    private IUserDetailService userDetailService;
    //在拥有ScreenAnnotataion的注解的方法上创建切点
    @Pointcut("@annotation(com.zjc.seckilldemo.aop.ScreenAnnotation)")
    public void ScreenPointCut(){
    }

    /**
     * 截取方法上的user参数，取出其中的身份证
     * 并且提交给Service进行筛选
     * 正常继续放行（pjp.proceed();）
     * 不正常直接返回RespBean.error(RespBeanEnum)
     * 设置了开关，开关的值在application.properties中进行配置
     * 如果不是false要执行筛选
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("ScreenPointCut()")
    public Object ScreenAopDealer(ProceedingJoinPoint pjp) throws Throwable {
        if (Screenswitch.equals("false")){
            return pjp.proceed();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Object[] values = pjp.getArgs();
        String[] names = ((CodeSignature) pjp.getSignature()).getParameterNames();
        System.out.println(Arrays.toString(values));
        for (int i = 0; i < names.length; i++) {
            map.put(names[i], values[i]);
        }
        User user = (User) map.get("user");
        String identity = user.getIdentity();
        System.out.println(identity);
        if (!userDetailService.screen(identity).equals(RespBean.success())) {
            return userDetailService.screen(identity);
        }
        return pjp.proceed();
    }
}


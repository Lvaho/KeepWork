package com.zjc.seckilldemo.service;

import org.aspectj.lang.ProceedingJoinPoint;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author lvaho
 * @since 2022-03-05
 */

public interface IScreenService {
    Object screen(String method_name, String identity, String AOPTargetClassReturnType, ProceedingJoinPoint pjp) throws Throwable;
    void recordScreenResult(boolean violation,String identity,String method_name);
}

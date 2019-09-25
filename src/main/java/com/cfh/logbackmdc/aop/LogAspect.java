package com.cfh.logbackmdc.aop;

import com.cfh.logbackmdc.Common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @ClassName LogAspect
 * @Description:
 * @Author: bughua
 * @CreateDate: 2019/9/25 14:22
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    // 切点
    @Around("execution(public * com.cfh.logbackmdc.controller.*.*(..))")
    public void serviceAccess(ProceedingJoinPoint joinPoint){
        //计时
        long startTime = System.currentTimeMillis();
        //获得类名
        String clazzName = joinPoint.getTarget().getClass().getSimpleName();
        //获得方法名
        String methodName = joinPoint.getSignature().getName();
        //获得参数列表
        Object[] args = joinPoint.getArgs();
        log.info("方法[{}]开始执行。。。", methodName);
        try {
            //初始化日志ID
            initMDC(args);
            //业务执行
            joinPoint.proceed();
            log.info("方法[{}]结束执行。。。耗时[{}]ms", methodName, System.currentTimeMillis()-startTime);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * 初始化日志ID
     *
     * @param args 入参
     */
    private String initMDC(Object[] args) {
        // 先不用  根据业务需要再用
        StringBuilder stringBuilder = new StringBuilder("sec_");
        stringBuilder.append(System.currentTimeMillis());
        stringBuilder.append("_");
        Integer random = (int)(100 * Math.random());
        stringBuilder.append(random);
        String traceLogId = stringBuilder.toString();
        MDC.put(Constants.TRACE_LOG_ID, traceLogId);
        return traceLogId;
    }
}

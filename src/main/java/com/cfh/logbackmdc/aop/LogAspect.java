package com.cfh.logbackmdc.aop;

import com.cfh.logbackmdc.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * @ClassName LogAspect
 * @Description: AOP通知类（方法耗时，参数校验，异常捕捉（可自定义异常类型））
 * @Author: bughua
 * @CreateDate: 2019/10/11 9:22
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    // 切点 定义@Around环绕通知注解，表达式为conntroller下面的所有方法（实际写在service）
    @Around("execution(public * com.cfh.logbackmdc.controller.*.*(..))")
    // 参数ProceedingJoinPoint joinPoint用于获取类中属性以及业务执行
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
        if (args != null) {
            Object argObject = args[0];
            try {
                //参数校验
                validate(argObject);
                //初始化日志ID
                initMDC(args);
                //业务执行
                joinPoint.proceed();
                log.info("方法[{}]结束执行。。。耗时[{}]ms", methodName, System.currentTimeMillis()-startTime);
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    /**
     * 参数校验
     * @param object 校验对象
     * @throws
     */
    private <T> void validate(T object, Class<?>... groups) throws Exception {
        Set<ConstraintViolation<T>> constraintViolations = localValidatorFactoryBean.validate(
                object, groups);
        if (constraintViolations != null && !constraintViolations.isEmpty()) {
            ConstraintViolation c = constraintViolations.iterator().next();
            throw new Exception(c.getMessage());
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
        // 使用logback的MDC.put方法植入日志号，在logback中用[%X{TRACE_LOG_ID}]获取该日志号
        MDC.put(Constants.TRACE_LOG_ID, traceLogId);
        return traceLogId;
    }
}

package com.sunchs.lyt.user.aop;


import com.sunchs.lyt.framework.bean.ResultData;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ExceptionInterceptorAspect {

    @Pointcut(value = "execution(* com.sunchs.lyt.user.controller.*.*(..))")
    private void controllerException(){}

    @Around("controllerException()")
    public Object ddd(ProceedingJoinPoint joinPoint) {
        try {
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            return ResultData.getFailure(throwable.getMessage());
        }
    }

}

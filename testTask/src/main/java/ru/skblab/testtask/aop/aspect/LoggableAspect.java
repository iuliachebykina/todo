package ru.skblab.testtask.aop.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Aspect
@Component
@Slf4j
public class LoggableAspect {

    @Before(value = "@annotation(ru.skblab.testtask.aop.annotation.Loggable)")
    public void logBeforeMethodCall(JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        List<String> args = Arrays.stream(joinPoint.getArgs()).map(Object::toString).collect(Collectors.toList());

        log.info("Call {}.{}() with arguments: {}",
                className, methodName, String.join(", ", args));
    }

    @AfterReturning(value = "@annotation(ru.skblab.testtask.aop.annotation.Loggable)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        if(result == null){
            return;
        }
        log.info("{}.{}() return result: {}",
                className, methodName, result);

    }

    @AfterThrowing(value = "@annotation(ru.skblab.testtask.aop.annotation.Loggable)", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();

        log.error("Exception in {}.{}() with cause: {}",
                className, methodName, error.getCause() != null ? error.getCause() : "NULL");
    }
}

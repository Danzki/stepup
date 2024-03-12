package com.danzki.logger;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class LogTransformationProcessor {
    private static Logger logger = LoggerFactory.getLogger(LogTransformationProcessor.class);

    @Around("@annotation(com.danzki.annotation.LogTransformation)")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        Date currentDate = new Date();

        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        logger.debug(">>{}: {}() - {}", currentDate.toString(), methodName, Arrays.toString(args));
        Object result = joinPoint.proceed();
        logger.debug("<< {}() - {}", methodName, result);
        return result;
    }

}

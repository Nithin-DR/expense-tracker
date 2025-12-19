package com.nkoder.expense_tracker.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect     //-- by marking this ANNOTATION , it tells to spring : This class contains cross-cutting logic
@Component   //--- Without @Component, AOP will not work
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

//SYNTAX:   ("execution(returnType package.class.method(arguments))")
    @Around("execution(* com.nkoder.expense_tracker.controller..*(..)) || execution(* com.nkoder.expense_tracker.service..*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
    //Object - by mentioning it as return type : it supports all return types
        long startTime = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().toShortString();

        log.info("Entering {}", methodName);

        try {
            Object result = joinPoint.proceed();

            log.info("Exiting {} successfully", methodName);
            return result;

        } catch (Exception ex) {
            log.error("Exception in {}", methodName, ex);
            throw ex;

        } finally {
            long timeTaken = System.currentTimeMillis() - startTime;
            log.info("{} executed in {} ms", methodName, timeTaken);
        }
    }
}

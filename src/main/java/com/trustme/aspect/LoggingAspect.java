package com.trustme.aspect;

import com.trustme.dto.request.UserRegisterRequest;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    @Before("execution(* com.trustme.service.AuthService.registerUser(..))")
    public void logBeforeRegisterUser(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        UserRegisterRequest registerRequest = (UserRegisterRequest) args[0];

        logger.info("Starting user registration process for username: {} ", registerRequest.getUsername());
    }

    @AfterThrowing(pointcut = "applicationPackagePointcut()", throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Registered failed {}: {}", joinPoint.getSignature(), exception.getMessage(), exception);
    }
}

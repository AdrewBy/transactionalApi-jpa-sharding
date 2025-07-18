package com.ustsinau.transactionapi.service.compensation.aopV2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.ArrayDeque;
import java.util.Deque;

@Aspect
@Component
public class CompensatingTransactionAspect {

    private final ThreadLocal<Deque<CompensationDto>> compensationQueue = new ThreadLocal<>();

    @Around("@annotation(CompensatingTransaction)")
    public Object executeWithCompensation(ProceedingJoinPoint joinPoint) throws Throwable {
        compensationQueue.set(new ArrayDeque<>());
        try {
            return joinPoint.proceed();
        } finally {
            compensationQueue.remove();
        }
    }

    public void registerCompensation(Runnable action, String name) {
        compensationQueue.get().push(new CompensationDto(action, true, name));
    }
}

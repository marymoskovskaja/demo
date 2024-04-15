package com.example.demo.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Аспект для логирования операций изменения содержимого БД.
 */
@Slf4j
@Aspect
@Component
@ConditionalOnExpression("${log.repository.change.enable:true}")
public class ChangeLoggerAspect {

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository+.save*(..))))")
    public void saveMethod() {
    }

    @Pointcut("execution(* org.springframework.data.jpa.repository.JpaRepository+.delete*(..))))")
    public void deleteMethod() {
    }

    @AfterReturning("saveMethod()")
    public void logSaveEntitiesAfterSaving(JoinPoint joinPoint) throws Throwable {
        log.info(String.format("%s.%s(): Сохранение сущности(ей) в БД: %s ",
                Arrays.stream(joinPoint.getTarget().getClass().getGenericInterfaces())
                        .findFirst().orElseThrow().getTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs())));
    }

    @AfterReturning("deleteMethod()")
    public void logDeleteEntitiesAfterSaving(JoinPoint joinPoint) throws Throwable {
        log.info(String.format("%s.%s(): Удаление сущности(ей) из БД: %s ",
                Arrays.stream(joinPoint.getTarget().getClass().getGenericInterfaces())
                        .findFirst().orElseThrow().getTypeName(),
                joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs())));
    }

}

package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Slf4j
@Component
public class AotuFillAspect {

    @Pointcut("@annotation(com.sky.annotation.AutoFill) && execution( * com.sky.mapper.*.*(..))")
    public void autoFillPointCut() {
    }

    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws InvocationTargetException, IllegalAccessException {
        log.info("自动填充");
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.Value();

        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }

        Object entity = args[0];
        log.info("entity:{}",entity);

        LocalDateTime now = LocalDateTime.now();
        Long id = BaseContext.getCurrentId();

        if(operationType == OperationType.INSERT){
            try {
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME,LocalDateTime.class).invoke(entity,now);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class).invoke(entity,now);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER,Long.class).invoke(entity,id);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class).invoke(entity,id);

            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

        }else {
            try {
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME,LocalDateTime.class).invoke(entity,now);
                entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER,Long.class).invoke(entity,id);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        log.info("entity:{}",entity);

    }
}

package com.jinhx.java.aspect;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * TimeConsumingAspect
 *
 * @author jinhx
 * @since 2022-01-13
 */
@Aspect
@Component
@Slf4j
@Order(-1)
public class TimeConsumingAspect {

    @Pointcut("@annotation(com.jinhx.java.aspect.TimeConsuming)")
    public void timeConsumingPointCut() {

    }

    @Around("timeConsumingPointCut()")
    public Object around(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // 耗时计算
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        // 执行方法
        Object result = null;
        try {
            result = proceedingJoinPoint.proceed();
            return result;
        } finally {
            stopWatch.stop();
            Object finalResult = result;
            MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();

            Map<String, Object> map = new HashMap<>();
            Object[] values = proceedingJoinPoint.getArgs();
            String[] names = signature.getParameterNames();
            for (int i = 0; i < names.length; i++) {
                map.put(names[i], values[i]);
            }

            String className = proceedingJoinPoint.getTarget().getClass().getName();
            String methodName = signature.getName();

            Method method = signature.getMethod();
            TimeConsuming timeConsuming = method.getAnnotation(TimeConsuming.class);
            if (timeConsuming.printParams()) {
                log.info("act={} className={} time={} req={} rsp={}", methodName, className, (double) stopWatch.getTime() / 1000 + "s",
                        JSONUtil.parseObj(map), JSONUtil.parseObj(finalResult));
            } else {
                log.info("act={} className={} time={}", methodName, className, (double) stopWatch.getTime() / 1000 + "s");
            }
        }
    }

}

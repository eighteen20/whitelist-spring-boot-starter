package com.middleware.whitelist;

import com.alibaba.fastjson.JSON;
import com.middleware.whitelist.annotation.DoWhiteList;
import org.apache.commons.beanutils.BeanUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;
import java.lang.reflect.Method;

/**
 * 白名单切面拦截逻辑
 *
 * @author liujm
 */
@Aspect
public class DoJoinPoint {
    private final Logger logger = LoggerFactory.getLogger(DoJoinPoint.class);

    @Resource
    private String whiteListConfig;


    /**
     * 定义切点
     */
    @Pointcut("@annotation(com.middleware.whitelist.annotation.DoWhiteList)")
    public void aopPoint() {

    }

    /**
     * 对方法增强的织入动作
     * 实现白名单用户拦截还是放行
     * 拦截方法后，获取方法上的自定义注解
     *
     * @param joinPoint 切入点
     * @return Object
     * @throws Throwable InstantiationException, IllegalAccessException,  NoSuchMethodException
     */
    @Around("aopPoint()")
    public Object doRouter(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取内容
        Method method = getMethod(joinPoint);
        DoWhiteList doWhiteList = method.getAnnotation(DoWhiteList.class);

        // 获取字段值
        String keyValue = getFiledValue(doWhiteList.key(), joinPoint.getArgs());
        logger.info("middleware whitelist handler method：{} value：{}", method.getName(), keyValue);
        if ("".equals(keyValue) || null == keyValue) {
            // 放行
            return joinPoint.proceed();
        }

        String[] split = whiteListConfig.split(",");
        // 白名单过滤
        for (String str : split) {
            if (keyValue.equals(str)) {
                return joinPoint.proceed();
            }
        }

        // 拦截
        return returnObject(doWhiteList, method);
    }

    private Object returnObject(DoWhiteList doWhiteList, Method method)
            throws InstantiationException, IllegalAccessException {
        Class<?> returnType = method.getReturnType();
        String returnJson = doWhiteList.returnJson();
        if ("".equals(returnJson)) {
            return returnType.newInstance();
        }
        return JSON.parseObject(returnJson, returnType);
    }

    private String getFiledValue(String key, Object[] args) {
        String filedValue = null;
        for (Object arg : args) {
            try {
                if (null == filedValue || "".equals(filedValue)) {
                    filedValue = BeanUtils.getProperty(arg, key);
                } else {
                    break;
                }
            } catch (Exception e) {
                if (args.length == 1) {
                    return args[0].toString();
                }
            }
        }
        return filedValue;
    }

    private Method getMethod(ProceedingJoinPoint joinPoint) throws NoSuchMethodException {
        Signature sig = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) sig;
        return joinPoint.getTarget().getClass().getMethod(methodSignature.getName(), methodSignature.getParameterTypes());
    }
}

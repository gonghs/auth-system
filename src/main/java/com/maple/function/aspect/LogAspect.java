package com.maple.function.aspect;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.maple.common.anno.Log;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日志切面
 *
 * @author maple
 * @version 1.0
 * @since 2020-02-24 17:00
 */
@Aspect
@Component
public class LogAspect {
    @Autowired
    private HttpServletRequest request;

    @Around("@annotation(logAnnotation)")
    public Object printLog(ProceedingJoinPoint joinPoint, Log logAnnotation) throws Throwable {
        // 获取类日志对象
        Logger log = LoggerFactory.getLogger(joinPoint.getSignature().getDeclaringType());

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();

        LogPrint logPrint = new LogPrint();
        logPrint.setMethodName(joinPoint.getSignature().getName());

        // 打印入参 过滤文件和字节数组
        if (logAnnotation.enableInputParams()) {
            Object[] args = joinPoint.getArgs();


            Object[] printArgs = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                if ((args[i] instanceof HttpServletRequest) || args[i] instanceof HttpServletResponse) {
                    continue;
                }
                if (args[i] instanceof byte[]) {
                    printArgs[i] = "byte array";
                } else if (args[i] instanceof MultipartFile) {
                    printArgs[i] = "file";
                } else {
                    printArgs[i] = args[i];
                }
            }
            logPrint.setInputParams(printArgs);
        }

        if (logAnnotation.enableOutputParams()) {
            logPrint.setOutputParams(result);
        }

        if (logAnnotation.enableExecuteTime()) {
            logPrint.setExecuteTime(System.currentTimeMillis() - startTime);
        }

        log(log, logPrint);
        return result;
    }

    private void log(Logger logger, LogPrint logPrint) {
        logger.info(" ---> 方法名:{} <---", logPrint.getMethodName());
        logger.info(" ---> 入参:{} <---", JSON.toJSONString(logPrint.getInputParams()));
        logger.info(" ---> 出参:{} <---", JSON.toJSONString(logPrint.getOutputParams()));
    }

    @Getter
    @Setter
    private class LogPrint {
        /**
         * 方法名
         */
        @JSONField(ordinal = 1)
        private String methodName;
        /**
         * 入参
         */
        @JSONField(ordinal = 2)
        private Object[] inputParams;
        /**
         * 出参
         */
        @JSONField(ordinal = 3)
        private Object outputParams;
        /**
         * 执行时间
         */
        @JSONField(ordinal = 4)
        private long executeTime;
    }
}


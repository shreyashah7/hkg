package com.argusoft.hkg.web.config;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingConfigurator {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Around("within(@org.springframework.stereotype.Service *)  &amp;&amp;  !within(@com.argusoft.hkg.web.assets.databeans *) &amp;&amp; !within(@com.argusoft.hkg.web.base *) &amp;&amp; !within(@com.argusoft.hkg.web.common.databeans) &amp;&amp; !within(@com.argusoft.hkg.web.internationalization.databeans) &amp;&amp; !within(@com.argusoft.hkg.web.leavemanagement.databeans) &amp;&amp; !within(@com.argusoft.hkg.web.sync.databeans) &amp;&amp; !within(@com.argusoft.hkg.web.taskmanagement.databeans) &amp;&amp; !within(@com.argusoft.hkg.web.usermanagement.databeans) &amp;&amp; !within(@com.argusoft.hkg.model)")
    public Object logTimeMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            Object retVal = joinPoint.proceed();

            if (log.isTraceEnabled()) {
                StringBuilder logMessage = new StringBuilder();
                logMessage.append(joinPoint.getTarget().getClass().getName());
                logMessage.append(".");
                logMessage.append(joinPoint.getSignature().getName());
                logMessage.append("()\nargs : \n");

                Object[] args = joinPoint.getArgs();
                if (args.length > 0) {

                    for (int i = 0; i < args.length; i++) {
                        logMessage.append(args[i]).append(",");
                    }
                    logMessage.deleteCharAt(logMessage.length() - 1);
                }
                logMessage.append("Return value :\n");
                if(retVal instanceof Object[]) {
                	logMessage.append(Arrays.toString((Object[]) retVal));
                }else {
                	logMessage.append(retVal);
                }                

                log.trace(logMessage.toString());
            } else if (log.isDebugEnabled()) {
                StringBuilder logMessage = new StringBuilder();
                logMessage.append(joinPoint.getTarget().getClass().getName());
                logMessage.append(".");
                logMessage.append(joinPoint.getSignature().getName());
                logMessage.append("()");
                log.debug(logMessage.toString());
            }
            return retVal;
        } catch (Exception ex) {
            log.error("Exception at :" + joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName(), ex);
            throw ex;
        }

    }
}

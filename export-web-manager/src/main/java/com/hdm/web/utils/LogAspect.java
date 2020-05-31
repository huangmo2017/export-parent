package com.hdm.web.utils;

import com.hdm.domain.system.SysLog;
import com.hdm.domain.system.User;
import com.hdm.service.system.SysLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 日志切面类
 */
@Component
@Aspect
public class LogAspect {

    @Autowired
    private SysLogService sysLogService;

    @Autowired
    private HttpSession session;

    @Autowired
    private HttpServletRequest request;


    /**
     * 使用环绕通知对controller方法进行增强：自动记录日志
     */
    @Around(value = "execution(* com.hdm.web.controller.*.*.*(..))")
    public Object log(ProceedingJoinPoint pjp) {
        //1.获取登陆数据
        User user = (User) session.getAttribute("loginUser");

        //2. 获取方法名称、类全名
        String methodName = pjp.getSignature().getName();
        String fullClassName = pjp.getTarget().getClass().getName();

        //3.构造SysLog对象
        SysLog log = new SysLog();
        log.setTime(new Date());
        if (user != null) {
            log.setUserName(user.getUserName());
            log.setCompanyId(user.getCompanyId());
            log.setCompanyName(user.getCompanyName());
        }
        log.setIp(request.getLocalAddr());
        log.setMethod(methodName);
        log.setAction(fullClassName);

        try {
            //4.调用service保存日志
            sysLogService.save(log);
            return pjp.proceed();
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }
}
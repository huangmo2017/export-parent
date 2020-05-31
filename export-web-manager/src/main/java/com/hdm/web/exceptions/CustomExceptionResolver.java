package com.hdm.web.exceptions;

import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一异常处理
 */
public class CustomExceptionResolver implements HandlerExceptionResolver {
    /**
     * 1.跳转到美化了的错误页面
     * 2.携带错误信息
     */
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("error");
        mv.addObject("errorMsg", "对不起，我错了");
        mv.addObject("ex", ex);
        return mv;
    }
}
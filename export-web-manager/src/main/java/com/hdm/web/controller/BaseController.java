package com.hdm.web.controller;

import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class BaseController {

    // 注入request对象
    @Autowired
    protected HttpServletRequest request;
    // 注入response对象
    @Autowired
    protected HttpServletResponse response;
    // 注入session对象
    @Autowired
    protected HttpSession session;

    /**
     * 获取当前登陆用户所属企业id
     */
    public String getLoginCompanyId(){
        return "1";
    }

    /**
     * 获取当前登陆用户所属企业名称
     */
    public String getLoginCompanyName(){
        return "传智播客教育股份有限公司";
    }
}
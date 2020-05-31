package com.hdm.web.controller;

import com.hdm.domain.system.Module;
import com.hdm.domain.system.User;
import com.hdm.service.system.ModuleService;
import com.hdm.service.system.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @ClassName: LoginController
 * @description:
 * @author: huangdaming
 * @Date: 2020-05-28 22:14
 */
@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private ModuleService moduleService;

    @RequestMapping("/loginbak")
    public String loginbak(String email, String password) {
        // 判断
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            return "forward:/login.jsp";
        }

        // 根据邮箱查询
        User user = userService.findByEmail(email);
        // 判断
        if (user != null) {
            // 判断密码
            if (password.equals(user.getPassword())) {
                // 登陆成功
                session.setAttribute("loginUser", user);
                // 根据用户id查询用户的权限（根据用户的degree级别判断）
                List<Module> moduleList = moduleService.findModulesByUserId(user.getId());
                session.setAttribute("modules", moduleList);
                return "home/main";
            } else {
                // 密码错误
                request.setAttribute("error", "密码错误!");
                return "forward:/login.jsp";
            }
        } else {
            // 账号错误
            request.setAttribute("error", "账号错误!");
            return "forward:/login.jsp";
        }
    }

    @RequestMapping("/home")
    public String home() {
        return "home/home";
    }

    /**
     * 通过shiro进行登录认证
     */
    @RequestMapping("/login")
    public String login(String email, String password) {
        try {
            //1.获取subject
            Subject subject = SecurityUtils.getSubject();
            //2.构造用户名和密码
            UsernamePasswordToken upToken = new UsernamePasswordToken(email, password);
            //3.借助subject完成用户登录
            subject.login(upToken);
            //4.通过shiro获取用户对象，保存到session中
            User user = (User) subject.getPrincipal(); //获取安全数据（用户对象）
            session.setAttribute("loginUser", user);
            //5.获取菜单数据
            List<Module> modules = moduleService.findModulesByUserId(user.getId());
            session.setAttribute("modules", modules);
            //登录成功跳转页面
            return "home/main";
        } catch (Exception e) {
            e.printStackTrace();
            //登录失败跳转页面
            request.setAttribute("error", "用户名或者密码错误");
            return "forward:login.jsp";
        }
    }


    @RequestMapping("/logout")
    public String logout() {
        // shiro也提供了退出方法(清除shiro的认证信息)
        SecurityUtils.getSubject().logout();
        // 先清空session中登陆用户
        session.removeAttribute("loginUser");
        // 销毁服务端session
        session.invalidate();
        return "forward:/login.jsp";
    }
}

package com.hdm.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: LoginController
 * @description:
 * @author: huangdaming
 * @Date: 2020-05-28 22:14
 */
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login(String email, String password) {
        return "home/main";
    }

    @RequestMapping("/home")
    public String home() {
        return "home/home";
    }
}

package com.hdm.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.hdm.domain.company.Company;
import com.hdm.service.company.CompanyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ApplyController {

    /**
     * @Reference(retries = 0)
     * retries:配置重试次数。
     */
    @Reference(retries = 0)
    private CompanyService companyService;

    /**
     * 企业入驻申请，保存
     */
    @RequestMapping("/apply")
    @ResponseBody
    public String apply(Company company) {
        try {
            company.setState(0);
            companyService.save(company);
            return "1";
        } catch (Exception e) {
            e.printStackTrace();
            return "2";
        }
    }
}
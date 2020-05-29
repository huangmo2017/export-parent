package com.hdm.service.impl;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.company.Company;
import com.hdm.service.CompanyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * @ClassName: CompanyServiceImplTest
 * @description:
 * @author: huangdaming
 * @Date: 2020-05-28 22:52
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/applicationContext-dao.xml","classpath:spring/applicationContext-tx.xml"})
public class CompanyServiceImplTest {
    // 注入service
    @Autowired
    private CompanyService companyService;

    @Test
    public void findByPage(){
        PageInfo<Company> pageInfo = companyService.findByPage(1, 2);
        System.out.println(pageInfo);
    }
}
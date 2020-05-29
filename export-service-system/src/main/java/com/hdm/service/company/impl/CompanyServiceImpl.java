package com.hdm.service.company.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.company.CompanyDao;
import com.hdm.domain.company.Company;
import com.hdm.service.company.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * @ClassName: CompanyServiceImpl
 * @description:
 * @author: huangdaming
 * @Date: 2020-05-27 23:30
 */
@Service
public class CompanyServiceImpl implements CompanyService {

    // 注入dao
    @Autowired
    private CompanyDao companyDao;

    @Override
    public List<Company> findAll() {
        return companyDao.findAll();
    }

    @Override
    public void save(Company company) {
        // uuid作为主键
        company.setId(UUID.randomUUID().toString());
        companyDao.save(company);
    }

    @Override
    public void update(Company company) {
        companyDao.update(company);
    }

    @Override
    public Company findById(String id) {
        return companyDao.findById(id);
    }

    @Override
    public void delete(String id) {
        companyDao.delete(id);
    }

    @Override
    public PageInfo<Company> findByPage(int pageNum, int pageSize) {
        // 开始分页, PageHelper组件会自动对其后的第一条查询查询分页
        PageHelper.startPage(pageNum,pageSize);
        // 调用dao查询
        List<Company> list = companyDao.findAll();
        // 创建PageInfo对象封装分页结果，传入查询集合。会自动计算分页参数
        PageInfo<Company> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }
}

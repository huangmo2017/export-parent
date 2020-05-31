package com.hdm.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.cargo.FactoryDao;
import com.hdm.domain.cargo.Factory;
import com.hdm.domain.cargo.FactoryExample;
import com.hdm.service.cargo.FactoryService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class FactoryServiceImpl implements FactoryService {

    @Autowired
    private FactoryDao factoryDao;

    @Override
    public PageInfo<Factory> findByPage(
            FactoryExample contractExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Factory> list = factoryDao.selectByExample(contractExample);
        PageInfo<Factory> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<Factory> findAll(FactoryExample factoryExample) {
        return factoryDao.selectByExample(factoryExample);
    }

    @Override
    public Factory findById(String id) {
        return factoryDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Factory factory) {
        factoryDao.insertSelective(factory);
    }

    @Override
    public void update(Factory factory) {
        factoryDao.updateByPrimaryKeySelective(factory);
    }

    @Override
    public void delete(String id) {
        factoryDao.deleteByPrimaryKey(id);
    }
}
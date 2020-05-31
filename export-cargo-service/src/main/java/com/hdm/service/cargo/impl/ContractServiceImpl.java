package com.hdm.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.cargo.ContractDao;
import com.hdm.domain.cargo.Contract;
import com.hdm.domain.cargo.ContractExample;
import com.hdm.service.cargo.ContractService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class ContractServiceImpl implements ContractService {

    @Autowired
    private ContractDao contractDao;

    @Override
    public PageInfo<Contract> findByPage(ContractExample contractExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Contract> list = contractDao.selectByExample(contractExample);
        PageInfo<Contract> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<Contract> findAll(ContractExample contractExample) {
        return contractDao.selectByExample(contractExample);
    }

    @Override
    public Contract findById(String id) {
        return contractDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(Contract contract) {
        contract.setId(UUID.randomUUID().toString());
        contract.setCreateTime(new Date());

        // 设置货物数、附件数
        contract.setProNum(0);
        contract.setExtNum(0);

        // 设置购销合同金额、购销合同状态
        contract.setTotalAmount(0d);
        contract.setState(0); //0:草稿； 1：已提交； 2：已报运

        contractDao.insertSelective(contract);
    }

    @Override
    public void update(Contract contract) {
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        contractDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<Contract> findByDeptId(String deptId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(contractDao.findByDeptId(deptId));
    }
}
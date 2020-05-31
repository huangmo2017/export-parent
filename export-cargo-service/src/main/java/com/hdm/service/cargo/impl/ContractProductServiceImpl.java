package com.hdm.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.cargo.ContractDao;
import com.hdm.dao.cargo.ContractProductDao;
import com.hdm.dao.cargo.ExtCproductDao;
import com.hdm.domain.cargo.*;
import com.hdm.service.cargo.ContractProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@Service
public class ContractProductServiceImpl implements ContractProductService {

    @Autowired
    private ContractProductDao contractProductDao;
    @Autowired
    private ContractDao contractDao;
    @Autowired
    private ExtCproductDao extCproductDao;

    @Override
    public PageInfo<ContractProduct> findByPage(
            ContractProductExample contractExample, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ContractProduct> list = contractProductDao.selectByExample(contractExample);
        PageInfo<ContractProduct> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public List<ContractProduct> findAll(ContractProductExample contractProductExample) {
        return contractProductDao.selectByExample(contractProductExample);
    }

    @Override
    public ContractProduct findById(String id) {
        return contractProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ContractProduct contractProduct) {
        //1.设置基本属性（保存的id）
        contractProduct.setId(UUID.randomUUID().toString());
        //2.根据添加的货物的数量和单价计算总金额
        double amount = 0d;
        if (contractProduct.getCnumber() != null && contractProduct.getPrice() != null) {
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
        }
        //3.添加货物的总金额
        contractProduct.setAmount(amount);
        //4.保存货物
        contractProductDao.insertSelective(contractProduct);
        //5.根据货物所属的购销合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(contractProduct.getContractId());
        //6.设置货物数量
        contract.setProNum(contract.getProNum() + 1);
        //7.设置总金额
        contract.setTotalAmount(contract.getTotalAmount() + amount);
        //8.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void update(ContractProduct contractProduct) {
        //1.获取修改之前的金额
        ContractProduct oldCp = contractProductDao.selectByPrimaryKey(contractProduct.getId());
        //2.计算修改之后的总金额
        double amount = 0d;
        if (contractProduct.getCnumber() != null && contractProduct.getPrice() != null) {
            amount = contractProduct.getCnumber() * contractProduct.getPrice();
        }
        //3.设置货物修改之后的总金额
        contractProduct.setAmount(amount);
        //4.更新货物
        contractProductDao.updateByPrimaryKeySelective(contractProduct);
        //5.根据货物所属的购销合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(oldCp.getContractId());
        //6.计算购销合同的总金额     总金额 - 修改之前的金额  + 修改之后的金额
        contract.setTotalAmount(contract.getTotalAmount() - oldCp.getAmount() + amount);
        //7.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }

    @Override
    public void delete(String id) {
        //1.根据id查询货物
        ContractProduct cp = contractProductDao.selectByPrimaryKey(id);

        //2.根据货物id查询所有此货物的附件
        ExtCproductExample example = new ExtCproductExample();
        ExtCproductExample.Criteria criteria = example.createCriteria();
        criteria.andContractProductIdEqualTo(id);
        List<ExtCproduct> list = extCproductDao.selectByExample(example);

        //3.删除货物
        contractProductDao.deleteByPrimaryKey(id);
        //4.循环附件删除附件
        double amount = cp.getAmount(); //货物的总金额
        for (ExtCproduct extCproduct : list) {
            amount += extCproduct.getAmount();
            extCproductDao.deleteByPrimaryKey(extCproduct.getId());
        }

        //5.根据货物所属的购销合同id查询购销合同
        Contract contract = contractDao.selectByPrimaryKey(cp.getContractId());
        //6.设置购销和同的总金额
        contract.setTotalAmount(contract.getTotalAmount() - amount);
        //7.设置购销合同的附件数量和货物数量
        contract.setProNum(contract.getProNum() - 1);
        contract.setExtNum(contract.getExtNum() - list.size());
        //8.更新购销合同
        contractDao.updateByPrimaryKeySelective(contract);
    }
}
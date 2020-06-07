package com.hdm.service.cargo.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.cargo.ExportProductDao;
import com.hdm.domain.cargo.ExportProduct;
import com.hdm.domain.cargo.ExportProductExample;
import com.hdm.service.cargo.ExportProductService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service
public class ExportProductServiceImpl implements ExportProductService {

    // 注入dao
    @Autowired
    private ExportProductDao exportProductDao;

    @Override
    public ExportProduct findById(String id) {
        return exportProductDao.selectByPrimaryKey(id);
    }

    @Override
    public void save(ExportProduct exportProduct) {
        exportProductDao.insertSelective(exportProduct);
    }

    @Override
    public void update(ExportProduct exportProduct) {
        exportProductDao.updateByPrimaryKeySelective(exportProduct);
    }

    @Override
    public void delete(String id) {
        exportProductDao.deleteByPrimaryKey(id);
    }

    @Override
    public PageInfo<ExportProduct> findByPage(ExportProductExample example, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return new PageInfo<>(exportProductDao.selectByExample(example));
    }

    @Override
    public List<ExportProduct> findAll(ExportProductExample epExample) {
        return exportProductDao.selectByExample(epExample);
    }
}
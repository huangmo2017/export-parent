package com.hdm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.system.DeptDao;
import com.hdm.domain.system.Dept;
import com.hdm.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeptServiceImpl implements DeptService {

   @Autowired
   private DeptDao deptDao;

   @Override
   public PageInfo<Dept> findByPage(String companyId, int pageSize, int pageNum) {
      //1.调用startPage方法
      PageHelper.startPage(pageSize,pageNum);
      //2.查询全部列表
      List<Dept> list = deptDao.findAll(companyId);
      //3.返回分页对象
      return new PageInfo<Dept>(list);
   }

}
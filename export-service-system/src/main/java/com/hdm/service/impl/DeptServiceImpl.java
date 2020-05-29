package com.hdm.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.system.DeptDao;
import com.hdm.domain.system.Dept;
import com.hdm.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DeptServiceImpl implements DeptService {

   @Autowired
   private DeptDao deptDao;

   @Override
   public PageInfo<Dept> findByPage(String companyId, int pageNum, int pageSize) {
      PageHelper.startPage(pageNum,pageSize);
      return new PageInfo<>(deptDao.findAll(companyId));
   }

   @Override
   public Dept findById(String id) {
      return deptDao.findById(id);
   }

   @Override
   public List<Dept> findAll(String companyId) {
      return deptDao.findAll(companyId);
   }

   @Override
   public void save(Dept dept) {
      // 设置主键
      dept.setId(UUID.randomUUID().toString());
      deptDao.save(dept);
   }

   @Override
   public void update(Dept dept) {
      deptDao.update(dept);
   }

   @Override
   public boolean delete(String id) {

      //1) 先根据要删除的部门id查询
      long count = deptDao.findDeptByParentId(id);
      //2) 判断
      if (count == 0){
         // 2) 删除
         deptDao.delete(id);
         return true;
      } else {
         return false;
      }
   }

}
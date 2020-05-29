package com.hdm.dao.system;

import com.hdm.domain.system.Dept;

import java.util.List;

public interface DeptDao {
   /**
    * 查询全部部门
    * @param companyId 根据企业id查询
    * @return
    */
   List<Dept> findAll(String companyId);

   /**
    * 根据id查询部门
    */
   Dept findById(String id);
}
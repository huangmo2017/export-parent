package com.hdm.service.system;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.system.Module;

import java.util.List;

public interface ModuleService {

   //根据id查询
   Module findById(String id);

   //查询全部
   PageInfo<Module> findByPage(int pageNum, int pageSize);

   //根据id删除
   void delete(String id);

   //添加
   void save(Module module);

   //更新
   void update(Module module);

   //查询全部
   List<Module> findAll();

   // 查询角色已经拥有的权限. 查询条件：角色ID
   List<Module> findModulesByRoleId(String roleId);
}
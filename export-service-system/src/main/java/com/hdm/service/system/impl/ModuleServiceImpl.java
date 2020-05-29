package com.hdm.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.system.ModuleDao;
import com.hdm.domain.system.Module;
import com.hdm.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

   @Autowired
   private ModuleDao moduleDao;

   public Module findById(String id) {
      return moduleDao.findById(id);
   }

   public PageInfo<Module> findByPage(int pageNum, int pageSize) {
      PageHelper.startPage(pageNum,pageSize);
      List<Module> list = moduleDao.findAll();
      return new PageInfo<Module>(list);
   }

   public void delete(String id) {
      moduleDao.delete(id);
   }

   public void save(Module module) {
      //指定id属性
      module.setId(UUID.randomUUID().toString());
      moduleDao.save(module);
   }

   public void update(Module module) {
      moduleDao.update(module);
   }

   @Override
   public List<Module> findAll() {
      return moduleDao.findAll();
   }

   @Override
   public List<Module> findModulesByRoleId(String roleId) {
      return moduleDao.findModulesByRoleId(roleId);
   }
}
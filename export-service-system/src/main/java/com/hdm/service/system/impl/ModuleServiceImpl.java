package com.hdm.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.system.ModuleDao;
import com.hdm.dao.system.UserDao;
import com.hdm.domain.system.Module;
import com.hdm.domain.system.User;
import com.hdm.service.system.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ModuleServiceImpl implements ModuleService {

   @Autowired
   private ModuleDao moduleDao;

   @Autowired
   private UserDao userDao;

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

   /**
    * 根据登录的用户id查询用户所具有的所有权限（模块，菜单）
    *   1.根据用户id查询用户
    *   2.根据用户degree级别判断
    *   3.如果degree==0 （内部的sass管理）
    *      根据模块中的belong字段进行查询，belong = "0";
    *   4.如果degree==1 （租用企业的管理员）
    *      根据模块中的belong字段进行查询，belong = "1";
    *   5.其他的用户类型
    *      借助RBAC的数据库模型，多表联合查询出结果
    */
   @Override
   public List<Module> findModulesByUserId(String userId) {
      //1.根据用户id查询用户
      User user = userDao.findById(userId);
      //2.根据用户degree级别判断
      if (user.getDegree() == 0) {
         //3.如果degree==0 （内部的sass管理）
         return moduleDao.findModulesByBelong("0");
      }else if(user.getDegree() == 1) {
         //4.如果degree==1 （租用企业的管理员）
         return moduleDao.findModulesByBelong("1");
      }else{
         //5.其他的用户类型
         return moduleDao.findModulesByUserId(userId);
      }
   }
}
package com.hdm.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.system.RoleDao;
import com.hdm.domain.system.Role;
import com.hdm.service.system.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class RoleServiceImpl implements RoleService {

   @Autowired
   private RoleDao roleDao;

   public Role findById(String id) {
      return roleDao.findById(id);
   }

   //分页
   public PageInfo<Role> findByPage(String companyId, int pageNum, int pageSize) {
      PageHelper.startPage(pageNum,pageSize);
      List<Role> list = roleDao.findAll(companyId);
      return new PageInfo<Role>(list);
   }

   public void delete(String id) {
      roleDao.delete(id);
   }

   public void save(Role role) {
      //指定id属性
      role.setId(UUID.randomUUID().toString());
      roleDao.save(role);
   }

   public void update(Role role) {
      roleDao.update(role);
   }

   @Override
   public void updateRoleModule(String roleId, String moduleIds) {
      //1. 先删除用户角色中间表数据
      roleDao.deleteRoleModule(roleId);

      //2. 判断
      if (moduleIds != null){
         //3. 分割字符串
         String[] array = moduleIds.split(",");
         if (array != null){
            //4. 遍历所有模块，实现角色添加模块
            for (String moduleId : array) {
               roleDao.saveRoleModule(roleId,moduleId);
            }
         }
      }
   }
}
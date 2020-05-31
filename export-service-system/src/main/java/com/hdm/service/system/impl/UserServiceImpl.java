package com.hdm.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.system.UserDao;
import com.hdm.domain.system.User;
import com.hdm.service.system.UserService;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    public PageInfo<User> findByPage(String companyId, int pageNum, int pageSize) {
        //1.调用startPage方法
        PageHelper.startPage(pageNum, pageSize);
        //2.查询全部列表
        List<User> list = userDao.findAll(companyId);
        //构造pagebean
        return new PageInfo<User>(list);
    }

    //查询所有部门
    public List<User> findAll(String companyId) {
        return userDao.findAll(companyId);
    }

    public void save(User user) {
        user.setId(UUID.randomUUID().toString());
        if (user.getPassword() != null) {
            String encodePwd = new Md5Hash(user.getPassword(), user.getEmail()).toString();
            user.setPassword(encodePwd);
        }
        userDao.save(user);
    }

    public void update(User user) {
        if (user.getPassword() != null) {
            String encodePwd = new Md5Hash(user.getPassword(), user.getEmail()).toString();
            user.setPassword(encodePwd);
        }
        userDao.update(user);
    }

    @Override
    public boolean delete(String userId) {
        //1. 根据用户id，查询用户角色中间表
        long count = userDao.findUserRoleByUserId(userId);
        //2. 判断
        if (count == 0) {
            userDao.delete(userId);
            return true;
        }
        return false;
    }

    public User findById(String id) {
        return userDao.findById(id);
    }

    @Override
    public void changeRole(String userId, String[] roleIds) {
        //DELETE FROM pe_role_user WHERE user_id=''
        //INSERT INTO pe_role_user(user_id,role_id)VALUES();

        //1） 先解决用户角色关系
        userDao.deleteUserRoleByUserId(userId);

        //2） 给用户添加角色
        if (roleIds != null && roleIds.length > 0) {
            for (String roleId : roleIds) {
                userDao.saveUserRole(userId, roleId);
            }
        }
    }

    @Override
    public User findByEmail(String email) {
        return userDao.findByEmail(email);
    }
}
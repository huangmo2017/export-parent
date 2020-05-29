package com.hdm.service.system;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.system.User;

import java.util.List;

public interface UserService {

    // 分页查询
    PageInfo<User> findByPage(String companyId, int pageNum, int PageSize);

    //查询所有部门
    List<User> findAll(String companyId);

    //保存
    void save(User user);

    //更新
    void update(User user);

    //删除
    boolean delete(String id);

    //根据id查询
    User findById(String id);

    //给用户分配角色
    void changeRole(String userId, String[] roleIds);

    // 登陆时候根据邮箱查询
    User findByEmail(String email);
}
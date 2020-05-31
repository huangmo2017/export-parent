package com.hdm.service.cargo;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.cargo.Factory;
import com.hdm.domain.cargo.FactoryExample;

import java.util.List;

/**
 * 工厂模块
 */
public interface FactoryService {

    /**
     * 分页查询
     */
    PageInfo<Factory> findByPage(
            FactoryExample FactoryExample, int pageNum, int pageSize);

    /**
     * 查询所有
     */
    List<Factory> findAll(FactoryExample FactoryExample);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Factory findById(String id);

    /**
     * 新增
     */
    void save(Factory Factory);

    /**
     * 修改
     */
    void update(Factory Factory);

    /**
     * 删除部门
     */
    void delete(String id);
}
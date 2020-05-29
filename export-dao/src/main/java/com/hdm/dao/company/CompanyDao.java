package com.hdm.dao.company;

import com.hdm.domain.company.Company;

import java.util.List;

/**
 * @ClassName: CompanyDao
 * @description:
 * @author: huangdaming
 * @Date: 2020-05-27 23:18
 */
public interface CompanyDao {

    /**
     * 查询所有企业
     */
    List<Company> findAll();

    /**
     * 添加
     * @param company
     */
    void save(Company company);

    /**
     * 修改
     * @param company
     */
    void update(Company company);

    /**
     * 主键查询
     * @param id
     * @return
     */
    Company findById(String id);

    /**
     * 删除
     * @param id
     */
    void delete(String id);
}

package com.hdm.service.company;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.company.Company;

import java.util.List;

/**
 * @ClassName: CompanyService
 * @description:
 * @author: huangdaming
 * @Date: 2020-05-27 23:29
 */
public interface CompanyService {

    List<Company> findAll();

    /**
     * 添加
     *
     * @param company
     */
    void save(Company company);

    /**
     * 修改
     *
     * @param company
     */
    void update(Company company);

    /**
     * 主键查询
     *
     * @param id 主键
     * @return 返回用户对象
     */
    Company findById(String id);

    /**
     * 删除
     *
     * @param id
     */
    void delete(String id);

    /**
     * 分页查询
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<Company> findByPage(int pageNum, int pageSize);
}

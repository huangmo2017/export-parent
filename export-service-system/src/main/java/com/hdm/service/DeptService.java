package com.hdm.service;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.system.Dept;

public interface DeptService {

    /**
     * 分页查询
     * @param companyId 公司的id
     * @param pageNum 当前页
     * @param pageSize 页大小
     * @return
     */
    PageInfo<Dept> findByPage(String companyId, int pageNum, int pageSize);
}
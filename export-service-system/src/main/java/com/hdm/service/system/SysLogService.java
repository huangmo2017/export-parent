package com.hdm.service.system;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.system.SysLog;

public interface SysLogService {

    /**
     * 分页
     */
    PageInfo<SysLog> findByPage(String companyId, int pageNum, int pageSize);

    //保存
    void save(SysLog log);
}
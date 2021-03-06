package com.hdm.service.system.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hdm.dao.system.SysLogDao;
import com.hdm.domain.system.SysLog;
import com.hdm.service.system.SysLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SysLogServiceImpl implements SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    public PageInfo<SysLog> findByPage(String companyId, int pageNum, int pageSize) {
        //1.调用startPage方法
        PageHelper.startPage(pageNum, pageSize);
        //2.查询全部列表
        List<SysLog> list = sysLogDao.findAll(companyId);
        //3. 构造pagebean
        return new PageInfo(list);
    }

    public void save(SysLog sysLog) {
        sysLog.setId(UUID.randomUUID().toString());
        sysLogDao.save(sysLog);
    }

}
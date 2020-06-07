package com.hdm.service.cargo;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.cargo.Export;
import com.hdm.domain.cargo.ExportExample;

public interface ExportService {

    Export findById(String id);

    void save(Export export);

    void update(Export export);

    void delete(String id);

    PageInfo<Export> findByPage(ExportExample example,
                                int pageNum, int pageSize);
}
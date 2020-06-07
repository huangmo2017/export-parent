package com.hdm.service.cargo;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.cargo.ExportProduct;
import com.hdm.domain.cargo.ExportProductExample;

import java.util.List;

public interface ExportProductService {

	ExportProduct findById(String id);

	void save(ExportProduct exportProduct);

	void update(ExportProduct exportProduct);

	void delete(String id);

	PageInfo<ExportProduct> findByPage(ExportProductExample exportProductExample,
									   int pageNum, int pageSize);

	List<ExportProduct> findAll(ExportProductExample epExample);
}
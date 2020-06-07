package com.hdm.dao.cargo;

import com.hdm.domain.cargo.ContractProduct;
import com.hdm.domain.cargo.ContractProductExample;
import com.hdm.vo.ContractProductVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ContractProductDao {

    //删除
    int deleteByPrimaryKey(String id);

    //保存
    int insertSelective(ContractProduct record);

    //条件查询
    List<ContractProduct> selectByExample(ContractProductExample example);

    //id查询
    ContractProduct selectByPrimaryKey(String id);

    //更新
    int updateByPrimaryKeySelective(ContractProduct record);

    // 根据船期查询，导出出货表
    List<ContractProductVo> findByShipTime(@Param("shipTime") String shipTime,@Param("companyId")  String companyId);
}
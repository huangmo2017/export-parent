package com.hdm.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdm.domain.cargo.ExtCproduct;
import com.hdm.domain.cargo.ExtCproductExample;
import com.hdm.domain.cargo.Factory;
import com.hdm.domain.cargo.FactoryExample;
import com.hdm.service.cargo.ExtCproductService;
import com.hdm.service.cargo.FactoryService;
import com.hdm.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping(value="/cargo/extCproduct")
public class ExtCproductController extends BaseController {
   @Reference
   private FactoryService factoryService;
   @Reference
   private ExtCproductService extCproductService;

   @RequestMapping(value="/list")
   public String list(
         String contractId,String contractProductId,
         @RequestParam(defaultValue = "1") int pageNum,
         @RequestParam(defaultValue = "5") int pageSize) {
      //1.查询附件的生产厂家
      FactoryExample factoryExample = new FactoryExample();
      FactoryExample.Criteria criteria2 = factoryExample.createCriteria();
      criteria2.andCtypeEqualTo("附件");
      List<Factory> factoryList = factoryService.findAll(factoryExample);
      request.setAttribute("factoryList",factoryList);

      //2.查询当前货物下的所有附件
      ExtCproductExample extCproductExample = new ExtCproductExample();
      ExtCproductExample.Criteria criteria = extCproductExample.createCriteria();
      criteria.andContractProductIdEqualTo(contractProductId);
      PageInfo pageInfo =
            extCproductService.findByPage(extCproductExample, pageNum, pageSize);
      request.setAttribute("pageInfo",pageInfo);

      //3.设置页面的基本参数：id
      request.setAttribute("contractId",contractId);
      request.setAttribute("contractProductId",contractProductId);

      return "cargo/extc/extc-list";
   }

   @RequestMapping(value="/edit")
   public String edit(ExtCproduct extCproduct) {
      if(StringUtils.isEmpty(extCproduct.getId())) {
         extCproductService.save(extCproduct);
      }else{
         extCproductService.update(extCproduct);
      }
      return "redirect:/cargo/extCproduct/list.do?contractId="+extCproduct.getContractId()+"&contractProductId="+extCproduct.getContractProductId();
   }

   @RequestMapping(value="/toUpdate")
   public String toUpdate(String contractId,String contractProductId,String id) {
      //1.查询附件
      ExtCproduct extCproduct = extCproductService.findById(id);
      request.setAttribute("extCproduct",extCproduct);

      //2.查询生产厂家
      FactoryExample example2 = new FactoryExample();
      FactoryExample.Criteria criteria2 = example2.createCriteria();
      criteria2.andCtypeEqualTo("附件");
      List<Factory> factoryList = factoryService.findAll(example2);
      request.setAttribute("factoryList",factoryList);

      //3.保存参数：购销合同id、货物id
      request.setAttribute("contractId",contractId);
      request.setAttribute("contractProductId",contractProductId);

      return "cargo/extc/extc-update";
   }

   @RequestMapping(value="/delete")
   public String delete(String contractId,String contractProductId,String id) {
      extCproductService.delete(id);
      return "redirect:/cargo/extCproduct/list.do?contractId="+contractId+"&contractProductId="+contractProductId;
   }
}
package com.hdm.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdm.domain.cargo.*;
import com.hdm.service.cargo.ContractService;
import com.hdm.service.cargo.ExportProductService;
import com.hdm.service.cargo.ExportService;
import com.hdm.web.controller.BaseController;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cargo/export")
public class ExportController extends BaseController {

   @Reference
   private ContractService contractService;
   @Reference
   private ExportService exportService;
   @Reference
   private ExportProductService exportProductService;

   /**
    * 合同管理 列表： 只显示购销合同状态为1的记录。
    */
   @RequestMapping("/contractList")
   public String contractList(
         @RequestParam(defaultValue = "1") int pageNum,
         @RequestParam(defaultValue = "5") int pageSize) {
      ContractExample example = new ContractExample();
      ContractExample.Criteria criteria = example.createCriteria();
      criteria.andCompanyIdEqualTo(getLoginCompanyId());
      //查询条件：状态为1 (已上报)
      criteria.andStateEqualTo(1);
      PageInfo<Contract> pageInfo = contractService.findByPage(example, pageNum, pageSize);
      request.setAttribute("pageInfo",pageInfo);
      return "cargo/export/export-contractList";
   }
   /**
    * 出口报运单列表
    */
   @RequestMapping("/list")
   public String list(
           @RequestParam(defaultValue = "1") int pageNum,
           @RequestParam(defaultValue = "5") int pageSize) {
      ExportExample example = new ExportExample();
      ExportExample.Criteria criteria = example.createCriteria();
      criteria.andCompanyIdEqualTo(getLoginCompanyId());
      PageInfo<Export> pageInfo =
              exportService.findByPage(example, pageNum, pageSize);
      request.setAttribute("pageInfo",pageInfo);
      return "cargo/export/export-list";
   }

   /**
    * 3. 进入报运单的添加页面
    * 功能入口： 合同管理点击报运
    * 请求地址： http://localhost:8080/cargo/export/toExport.do
    * 请求参数： 多个购销合同的id，  id=1&id=2
    * 响应地址： /WEB-INF/pages/cargo/export/export-toExport.jsp
    */
   @RequestMapping("/toExport")
   public String toExport(String id){
      // 保存购销合同id
      request.setAttribute("id",id);
      return "cargo/export/export-toExport";
   }

   /**
    * 4. 报运单添加或者修改方法
    */
   @RequestMapping("/edit")
   public String edit(Export export){
      // 设置购销合同所属企业信息
      export.setCompanyId(getLoginCompanyId());
      export.setCompanyName(getLoginCompanyName());

      if (StringUtils.isEmpty(export.getId())){
         // 添加
         exportService.save(export);
      }
      else {
         // 修改
         exportService.update(export);
      }
      return "redirect:/cargo/export/list.do";
   }

   /**
    * 4. 报运单修改（1）进入修改页面
    * 请求地址：http://localhost:8080/cargo/export/toUpdate.do
    * 请求参数：报运单id   如：id=1
    * 响应地址：cargo/export/export-update
    */
   @RequestMapping("/toUpdate")
   public String toUpdate(String id){
      //4.1 根据报运单id查询报运单
      Export export = exportService.findById(id);
      request.setAttribute("export",export);

      //4.2 根据报运单id查询商品
      //4.2.1 构造条件： exportId
      ExportProductExample epExample = new ExportProductExample();
      epExample.createCriteria().andExportIdEqualTo(id);
      //4.2.2 根据报运单id查询
      List<ExportProduct> exportProductList =
              exportProductService.findAll(epExample);
      //4.2.3 保存报运商品
      request.setAttribute("eps",exportProductList);
      return "cargo/export/export-update";
   }

}
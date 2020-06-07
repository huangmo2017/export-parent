package com.hdm.web.controller.cargo;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.PageInfo;
import com.hdm.domain.cargo.*;
import com.hdm.service.cargo.ContractService;
import com.hdm.service.cargo.ExportProductService;
import com.hdm.service.cargo.ExportService;
import com.hdm.vo.ExportProductVo;
import com.hdm.vo.ExportResult;
import com.hdm.vo.ExportVo;
import com.hdm.web.controller.BaseController;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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

   /**
    * 出口报运列表，点击取消
    */
   @RequestMapping("/cancel")
   public String cancel(String id) {
      Export export = exportService.findById(id);
      export.setState(0);
      exportService.update(export);
      return "redirect:/cargo/export/list.do";
   }

   /**
    * 出口报运列表，点击提交
    */
   @RequestMapping("/submit")
   public String submit(String id) {
      Export export = exportService.findById(id);
      export.setState(1);
      exportService.update(export);
      return "redirect:/cargo/export/list.do";
   }

   /**
    * 电子报运
    * @param id
    * @return
    */
   @RequestMapping("/exportE")
   public String exportE(String id) {
      //1.根据报运单id查询报运单对象
      Export export = exportService.findById(id);
      //2.根据报运单id查询报运商品列表
      ExportProductExample example = new ExportProductExample();
      ExportProductExample.Criteria criteria = example.createCriteria();
      criteria.andExportIdEqualTo(id);
      List<ExportProduct> eps = exportProductService.findAll(example);

      //3.构造电子报运的VO对象，并赋值
      ExportVo vo = new ExportVo();
      BeanUtils.copyProperties(export,vo);
      vo.setExportId(export.getId());
      //构造报运商品数据
      List<ExportProductVo> products = new ArrayList<ExportProductVo>();
      for (ExportProduct ep : eps) {
         ExportProductVo epv = new ExportProductVo();
         BeanUtils.copyProperties(ep,epv);
         epv.setExportProductId(ep.getId());
         products.add(epv);
      }
      vo.setProducts(products);

      //4.电子报运
      WebClient client = WebClient.create("http://localhost:9091/ws/export/user");
      client.post(vo);
      //5.查询报运结果
      client = WebClient.create("http://localhost:9091/ws/export/user/"+id);
      ExportResult result = client.get(ExportResult.class);

      //6.调用service完成报运结果的入库
      exportService.updateExport(result);
      return "redirect:/cargo/export/list.do";
   }

}
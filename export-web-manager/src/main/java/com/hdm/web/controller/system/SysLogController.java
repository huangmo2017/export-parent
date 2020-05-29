package com.hdm.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.hdm.service.system.SysLogService;
import com.hdm.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/system/log")
public class SysLogController extends BaseController {

   @Autowired
   private SysLogService sysLogService;

   /**
    * 分页查询
    */
   @RequestMapping("/list")
   public String list(
         @RequestParam(defaultValue = "1") int pageNum,
         @RequestParam(defaultValue = "5") int pageSize) {
      //1.调用service查询部门列表
      String companyId = getLoginCompanyId();
      PageInfo pageInfo = sysLogService.findByPage(companyId, pageNum, pageSize);
      //2.将部门列表保存到request域中
      request.setAttribute("pageInfo",pageInfo);
      //3.跳转到对象的页面
      return "system/log/log-list";
   }

}
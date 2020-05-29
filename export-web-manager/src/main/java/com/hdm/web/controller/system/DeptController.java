package com.hdm.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.hdm.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/system/dept")
public class DeptController {

   @Autowired
   private DeptService deptService;

   /**
    * 部门列表分页
    */
   @RequestMapping("/list")
   public ModelAndView list(
         @RequestParam(defaultValue = "1") int pageNum,
         @RequestParam(defaultValue = "5") int pageSize) {

      //模拟获取当前登录用户的数据
      //初始化当前登录用户所属的企业ID为1
      String companyId = "1";

      //1.调用service查询部门列表
      PageInfo pageInfo = deptService.findByPage(companyId, pageNum, pageSize);
      //2.返回
      ModelAndView mv = new ModelAndView();
      mv.addObject("pageInfo",pageInfo);
      mv.setViewName("system/dept/dept-list");
      return mv;
   }
}
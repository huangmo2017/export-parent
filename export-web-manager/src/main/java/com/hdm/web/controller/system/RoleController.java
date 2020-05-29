package com.hdm.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.system.Module;
import com.hdm.domain.system.Role;
import com.hdm.service.system.DeptService;
import com.hdm.service.system.ModuleService;
import com.hdm.service.system.RoleService;
import com.hdm.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/role")
public class RoleController extends BaseController {

   @Autowired
   private RoleService roleService;

   @Autowired
   private DeptService deptService;

   /**
    * 角色列表分页
    */
    @RequestMapping("/list")
    public ModelAndView list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize) {
        //初始化当前登录用户所属的企业ID为1
        String companyId = getLoginCompanyId();

        //1.调用service查询部门列表
        PageInfo pageInfo = roleService.findByPage(companyId, pageNum, pageSize);
        //2.返回
        ModelAndView mv = new ModelAndView();
        mv.addObject("pageInfo",pageInfo);
        mv.setViewName("system/role/role-list");
        return mv;
    }

   /**
    * 进入新增角色页面
    */
   @RequestMapping("/toAdd")
   public String toAdd() {
      return "system/role/role-add";
   }

   /**
    * 新增角色
    */
   @RequestMapping("/edit")
   public String edit(Role role) {
      String company = getLoginCompanyId();
      String companyName = getLoginCompanyName();
      role.setCompanyId(company);
      role.setCompanyName(companyName);
       
      //1.判断是否具有id属性
      if(StringUtils.isEmpty(role.getId())) {
         //2.没有id，保存
         roleService.save(role);
      }else{
         //3.具有id，更新
         roleService.update(role);
      }
      return "redirect:/system/role/list.do";
   }

   /**
    * 进入到修改界面
    *  1.获取到id，根据id进行查询
    *  2.保存查询结果到request域中
    *  3.跳转到修改界面
    */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id){
        Role role = roleService.findById(id);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/role/role-update");
        mv.addObject("role",role);
        return mv;
    }

   /**
    * 删除角色
    */
   @RequestMapping("/delete")
   public String delete(String id) {
      roleService.delete(id);
      //跳转到修改界面
      return "redirect:/system/role/list.do";
   }

    /**
     * 6.角色权限（1）进入角色权限页面
     * 请求地址：http://localhost:8080/system/role/roleModule.do?roleid=1
     * 转发页面：/WEB-INF/pages/system/role/role-module.jsp
     */
    @RequestMapping("/roleModule")
    public String roleModule(String roleid){
        // 根据角色id查询
        Role role = roleService.findById(roleid);
        // 保存
        request.setAttribute("role",role);
        return "system/role/role-module";
    }

    @Autowired
    private ModuleService moduleService;

    /**
     * 角色分配权限（2）role-module.jsp页面发送异步请求
     * A. 返回所有权限的json字符串
     * B. 角色已经具有的权限需要默认选中
     * C. 返回的json格式参考ztree
     *       如：[{id:2, pId:0, name:"随意勾选 2", checked:true, open:true}]
     */
    @RequestMapping("/getZtreeNodes")
    @ResponseBody
    public List<Map<String,Object>> getZtreeNodes(String roleId) {
        //1.查询所有的模块
        List<Module> moduleList = moduleService.findAll();

        //根据角色id查询角色所具有的权限信息
        List<Module> roleModules = moduleService.findModulesByRoleId(roleId);

        //2.构造map集合
        List<Map<String,Object>> list = new ArrayList<>();
        //构造map
        for (Module module : moduleList) {  //循环所有的模块
            //初始化map
            Map<String,Object> map = new HashMap<>();
            //添加map中的数据
            map.put("id",module.getId());   //模块id
            map.put("pId",module.getParentId());  //父模块id
            map.put("name",module.getName()); //模板名称

            if(roleModules.contains(module)) {
                map.put("checked",true); //默认勾选
            }
            //存入list集合
            list.add(map);
        }
        //3.返回
        return list;
    }

    /**
     * 角色分配权限（3）实现分配权限
     */
    @RequestMapping("/updateRoleModule")
    public String updateRoleModule(String roleId,String moduleIds) {
        //1.调用service完成权限分配
        roleService.updateRoleModule(roleId,moduleIds);
        //2.跳转到角色列表
        return "redirect:/system/role/list.do";
    }
}
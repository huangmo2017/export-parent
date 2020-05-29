package com.hdm.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.system.Dept;
import com.hdm.domain.system.Role;
import com.hdm.domain.system.User;
import com.hdm.service.system.DeptService;
import com.hdm.service.system.RoleService;
import com.hdm.service.system.UserService;
import com.hdm.web.controller.BaseController;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/system/user")
public class UserController extends BaseController {

    @Autowired
    private UserService userService;
    @Autowired
    private DeptService deptService;
    @Autowired
    private RoleService roleService;

    //用户列表分页
    @RequestMapping("/list")
    @RequiresPermissions("用户管理")
    public String list(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "5") int PageSize) {
        // 企业id：后期从当前登陆用户中获取用户的企业信息。现在先写死。
        String companyId = getLoginCompanyId();
        //1.调用service查询用户列表
        PageInfo<User> pageInfo =
                userService.findByPage(companyId, pageNum, PageSize);
        //2.将用户列表保存到request域中
        request.setAttribute("pageInfo", pageInfo);
        //3.跳转到对象的页面
        return "system/user/user-list";
    }

    /**
     * 进入新增用户页面
     */
    @RequestMapping("/toAdd")
    public String toAdd() {
        String companyId = getLoginCompanyId();
        //1.查询所有部门
        List<Dept> deptList = deptService.findAll(companyId);
        //2.存入request域
        request.setAttribute("deptList", deptList);
        return "system/user/user-add";
    }

    /**
     * 新增或更新用户
     */
    @RequestMapping("/edit")
    public String edit(User user) {
        String company = getLoginCompanyId();
        String companyName = getLoginCompanyName();
        user.setCompanyId(company);
        user.setCompanyName(companyName);

        //1.判断是否具有id属性
        if (StringUtils.isEmpty(user.getId())) {
            //2.用户id为空，执行保存
            userService.save(user);
        } else {
            //3.用户id不为空，执行更新
            userService.update(user);
        }
        return "redirect:/system/user/list.do";
    }

    /**
     * 进入到修改界面
     */
    @RequestMapping("/toUpdate")
    public ModelAndView toUpdate(String id) {
        User user = userService.findById(id);

        String companyId = getLoginCompanyId();
        List<Dept> deptList = deptService.findAll(companyId);

        ModelAndView mv = new ModelAndView();
        mv.setViewName("system/user/user-update");
        mv.addObject("user", user);
        mv.addObject("deptList", deptList);
        return mv;
    }

    /**
     * 删除，异步请求返回结果给客户端浏览器
     * @param id
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Map<String,Object> delete(String id){
        Map<String,Object> map = new HashMap<>();
        boolean flag = userService.delete(id);
        if (flag){
            map.put("message","删除成功！");
        } else {
            map.put("message","当前删除的记录被外键引用，删除失败！");
        }
        return map;
    }

    /**
     * 从用户列表，进入用户角色页面
     */
    @RequestMapping("/roleList")
    public String roleList(String id) {
        //1.根据id查询用户
        User user = userService.findById(id);

        //2.查询所有角色列表
        List<Role> roleList = roleService.findAll(getLoginCompanyId());

        //3.根据用户id查询用户已经具有的所有的角色集合
        List<Role> userRoles = roleService.findUserRole(id);
        //保存角色字符串，如："1,2,3"
        String userRoleStr = "";
        for (Role userRole : userRoles) {
            userRoleStr += userRole.getId()+",";
        }

        //4. 保存数据
        request.setAttribute("user",user);
        request.setAttribute("roleList",roleList);
        request.setAttribute("userRoleStr",userRoleStr);

        //5.跳转页面
        return "system/user/user-role";
    }

    /**
     * 6. 用户分配角色（2）保存用户角色
     */
    @RequestMapping("/changeRole")
    public String changeRole(String userId,String[] roleIds){
        // 给用户分配角色
        userService.changeRole(userId,roleIds);
        return "redirect:/system/user/list.do";
    }
}
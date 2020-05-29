package com.hdm.web.controller.system;

import com.github.pagehelper.PageInfo;
import com.hdm.domain.system.Dept;
import com.hdm.domain.system.User;
import com.hdm.service.system.DeptService;
import com.hdm.service.system.UserService;
import com.hdm.web.controller.BaseController;
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

    //用户列表分页
    @RequestMapping("/list")
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
}
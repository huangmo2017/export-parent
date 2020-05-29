package com.hdm.web.shiro;

import com.hdm.domain.system.Module;
import com.hdm.domain.system.User;
import com.hdm.service.system.ModuleService;
import com.hdm.service.system.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 自定义reamlm
 */
public class AuthRealm extends AuthorizingRealm {

   @Autowired
   private UserService userService;

   @Autowired
   private ModuleService moduleService;

   //登陆认证
   protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
      //1.获取到用户界面输入的邮箱地址和密码
      UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
      //2.获取用户出入的邮箱和密码
      String email = upToken.getUsername();
      //3.根据邮箱查询用户对象
      User user = userService.findByEmail(email);
      if(user != null) {
         //第一个参数：安全数据（user对象）
         //第二个参数：密码（数据库密码）
         //第三个参数：当前调用realm域的名称（类名即可）
         SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user,user.getPassword(),this.getName());
         return info;
      }
      return null;
   }

   // 授权访问校验
   protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
      /* 查询用户已经具有的权限并返回*/

      //1.获取当前登录的用户对象
      User user = (User) principalCollection.getPrimaryPrincipal();
      //2.获取用户的所有权限
      List<Module> moduleList = moduleService.findModulesByUserId(user.getId());
      //4.构造AuthorizationInfo对象返回
      Set<String> permissions = new HashSet<>();

      for (Module module : moduleList) {
         permissions.add(module.getName());
      }
      SimpleAuthorizationInfo sai = new SimpleAuthorizationInfo();
      //将所有可操作模块的名称存入到授权对象中
      sai.addStringPermissions(permissions);
      return sai;
   }
}
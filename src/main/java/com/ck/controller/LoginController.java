package com.ck.controller;

import com.ck.bean.User;
import com.ck.shiro.ShiroRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口的权限控制
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private ShiroRealm shiroRealm;

    @RequestMapping(value = "")
    public String login(User user) {
        //添加用户认证信息
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(
                user.getUserName(),
                user.getPassword()
        );
        try {
            //进行验证，这里可以捕获异常，然后返回对应信息
            subject.login(usernamePasswordToken);
//          subject.checkRole("admin");
//          subject.checkPermissions("query", "add");
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return "账号或密码错误！";
        } catch (AuthorizationException e) {
            e.printStackTrace();
            return "没有权限";
        }
        return "login success";
    }
    //注解验角色和权限
    @RequestMapping("/add")
    @RequiresPermissions("add")
    public String index() {
        return "有add权限通过";
    }

    //注解验角色和权限
    @RequestMapping("/query")
    @RequiresPermissions("query")
    public String query() {
        return "有query权限通过";
    }


    /**
     * 清空当前登录用户的缓存
     * 一般用户更新用户的角色信息
     * 此操作会触发doGetAuthorizationInfo方法
     * @return
     */
    @RequestMapping("/clear")
    public String clear(){
        shiroRealm.clearCache();
        return "缓存清除成功";
    }

}

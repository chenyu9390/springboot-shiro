package com.ck.controller;

import com.ck.shiro.ShiroRealm;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private ShiroRealm shiroRealm;

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

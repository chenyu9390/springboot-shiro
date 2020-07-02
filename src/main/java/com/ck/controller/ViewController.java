package com.ck.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 基于网页的权限控制
 */
@Controller
public class ViewController {



    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public String login(){
        return "login";
    }

    @RequestMapping(value = "/add",method = RequestMethod.GET)
    @RequiresPermissions("add")
    public String index(){
        return "index";
    }

    @RequestMapping(value = "/query",method = RequestMethod.GET)
    @RequiresPermissions("query")
    public String indexs(){
        return "index";
    }
}

package com.ck.bean.em;

import lombok.Getter;

/**
 * shiro拦截器枚举
 *  参考网址：https://blog.csdn.net/jinnianshilongnian/article/details/84533948
 */
@Getter
public enum  ShiroFilterEnum {


    AUTHC("authc","基于表单的拦截器"),
    AUTHCBASIC("athcBasic","Basic HTTP身份验证拦截器，主要属性： applicationName：弹出登录框显示的信息"),
    LOGOUT("logout","退出拦截器，主要属性：redirectUrl：退出成功后重定向的地址（/）"),
    USER("user","用户拦截器，用户已经身份验证/记住我登录的都可"),
    ANON("anon","匿名拦截器，即不需要登录即可访问"),;

    private String name;
    private String desc;

    ShiroFilterEnum(String name,String desc){
        this.name = name;
        this.desc = desc;
    }
}

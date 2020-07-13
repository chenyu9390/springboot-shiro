package com.ck.bean;

import lombok.Getter;

/**
 * 接口返回调用状态
 * @author CHEN
 */
@Getter
public enum ResponseStatus {

    UN_AUTH("10","没有权限",false),
    NEED_LOGIN("30","登录已失效，请重新登录",false),
    LOGIN_OUT("40","退出成功",true),
    REPEAT_LOGIN("50","重复登录",false),
    UNAUTHORIZED("20","未认证",false);

    private String key;
    private String value;
    private Boolean flag;

    ResponseStatus(String key, String value, boolean flag){
        this.key = key;
        this.value = value;
        this.flag =flag;
    }


    public String getKey() {
        return key;
    }
    public String getValue( )
    {
        return this.value;
    }

}

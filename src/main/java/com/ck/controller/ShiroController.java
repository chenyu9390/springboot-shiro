package com.ck.controller;

import com.ck.bean.ResponseResult;
import com.ck.bean.ResponseStatus;
import com.ck.domain.entity.UserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 接口的权限控制
 */
@RestController
@RequestMapping("/shiro")
public class ShiroController {

    @RequestMapping("/un_auth")
    public ResponseResult unAuth(){
        return new ResponseResult(ResponseStatus.UN_AUTH);
    }

    @RequestMapping("/unauthorized")
    public ResponseResult unauthorized(){
        return new ResponseResult(ResponseStatus.UNAUTHORIZED);
    }


}

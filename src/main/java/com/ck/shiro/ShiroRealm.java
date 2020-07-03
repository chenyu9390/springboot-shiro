package com.ck.shiro;

import com.ck.domain.entity.PermissionEntity;
import com.ck.domain.entity.RoleEntity;
import com.ck.domain.entity.UserEntity;
import com.ck.manager.ShiroManager;
import com.ck.service.LoginService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShiroRealm extends AuthorizingRealm {

    @Autowired
    private LoginService loginService;
    @Autowired
    private ShiroManager shiroManager;

    /**
     * 获取授权信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        String userName = (String) principalCollection.getPrimaryPrincipal();
        //根据用户名去数据库查询用户信息
        UserEntity user = loginService.getUserByName(userName);
        //添加角色和权限
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<RoleEntity> roles = shiroManager.getRoleByUserId(user.getUserId());
        for (RoleEntity role : roles) {
            //添加角色
            simpleAuthorizationInfo.addRole(role.getRoleName());
        }
        List<PermissionEntity> permissionEntityList = shiroManager.getPermissionEntityByUserId(user.getUserId());
        //添加权限
        for(PermissionEntity permissionEntity : permissionEntityList){
            simpleAuthorizationInfo.addStringPermission(permissionEntity.getPerms());
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 获取身份验证相关信息
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        if (authenticationToken.getPrincipal() == null) {
            return null;
        }
        //获取用户信息
        String userName = authenticationToken.getPrincipal().toString();
        UserEntity user = loginService.getUserByName(userName);
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(userName, user.getPassword(), getName());
            return simpleAuthenticationInfo;
        }
    }

    /**
     * 清除当前用户权限缓存
     * 使用方法：在需要清除用户权限的地方注入 ShiroRealm,
     * 然后调用其 clearCache方法。
     */
    public void clearCache() {
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }
}

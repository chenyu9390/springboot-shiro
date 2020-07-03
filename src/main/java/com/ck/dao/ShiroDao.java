package com.ck.dao;

import com.ck.domain.entity.PermissionEntity;
import com.ck.domain.entity.RoleEntity;

import java.util.List;
import java.util.Map;

public interface ShiroDao {

    /**
     * 通过用户名查询用户权限
     * @param userId id
     * @param type 0 : 菜单权限 1：按钮权限 2:所有权限
     * @return
     */
    List<Map<String,Object>> getPermissionByUserId(Long userId,Integer type);

    /**
     * 用户信息
     * @param userId 用户ID
     * @return
     */
    List<RoleEntity> getRoleByUserId(Long userId);

    /**
     * 保存用户角色信息
     * @param userId 用户ID
     * @param roleList  权限列表
     */
    void createUserRole(long userId,List<String> roleList);

    /**
     * 保存角色权限信息
     * @param roleId 角色ID
     * @param permissionIdList 权限列表
     */
    void createRolePermission(long roleId,List<String> permissionIdList);
}

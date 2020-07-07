package com.ck.dao;

import com.ck.domain.entity.MenuEntity;
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
    void createUserRole(long userId,List<Long> roleList);

    /**
     * 保存角色权限信息
     * @param roleId 角色ID
     * @param permissionIdList 权限列表
     */
    void createRolePermission(long roleId,List<Long> permissionIdList);

    /**
     * 新建角色信息
     * @param roleEntities  角色列表
     * @param createBy  创建人
     */
    List<RoleEntity> createRole(List<RoleEntity> roleEntities, String createBy);

    /**
     * 取消角色
     * @param roleIdList 角色列表
     * @param modifyBy 修改人
     */
    void closeRole(List<Long> roleIdList, String modifyBy);

    /**
     * 启用角色
     * @param roleIdList 角色列表
     * @param modifyBy 修改人
     */
    void enableRole(List<Long> roleIdList, String modifyBy);

    /**
     * 创建权限信息
     * @param menuEntityList  权限列表
     * @param createBy  创建人
     */
    List<MenuEntity> createMenu(List<MenuEntity> menuEntityList, String createBy);

    /**
     * 关闭权限
     * @param menuIdList 权限列表
     * @param modifyBy  修改人
     */
    void closeMenu(List<Long> menuIdList, String modifyBy);

    /**
     * 启用权限
     * @param menuIdList 权限列表
     * @param modifyBy  修改人
     */
    void enableMenu(List<Long> menuIdList, String modifyBy);

    /**
     * 删除用户角色
     * @param userId 用户ID
     * @param roleIdList    角色ID集合
     */
    void deleteUserRole(long userId,List<Long> roleIdList);

    /**
     * 删除角色权限ID
     * @param roleId    角色ID
     * @param menuIdList    权限ID集合
     */
    void deleteRoleMenu(long roleId,List<Long> menuIdList);
}

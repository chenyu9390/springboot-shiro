package com.ck.service;

import com.ck.domain.entity.MenuEntity;
import com.ck.domain.entity.RoleEntity;

import java.util.List;

public interface ShiroService {

    /**
     * 保存用户角色信息
     * @param userId 用户ID
     * @param roleIdList 角色ID集合
     */
    void createUserRole(long userId, List<Long> roleIdList);

    /**
     * 删除用户角色
     * @param userId 用户ID
     * @param roleIdList    角色ID集合
     */
    void deleteUserRole(long userId,List<Long> roleIdList);

    /**
     * 保存角色权限信息
     * @param roleId    角色ID
     * @param menuIdList 权限信息
     */
    void createRoleMenu(long roleId, List<Long> menuIdList);

    /**
     * 删除角色权限信息
     * @param roleId    角色ID
     * @param menuIdList    权限信息
     */
    void deleteRoleMenu(long roleId, List<Long> menuIdList);

    /**
     * 新建角色,并返回角色ID
     * @param roleEntities 角色列表
     * @param createBy 创建人
     */
    List<RoleEntity> createRole(List<RoleEntity> roleEntities,String createBy);

    /**
     * 取消角色
     * @param roleIdList 角色列表
     * @param modifyBy 修改人
     */
    void closeRole(List<Long> roleIdList,String modifyBy);

    /**
     * 启用角色
     * @param roleIdList 角色列表
     * @param modifyBy 修改人
     */
    void enableRole(List<Long> roleIdList,String modifyBy);

    /**
     * 创建权限信息,并返回ID
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

}

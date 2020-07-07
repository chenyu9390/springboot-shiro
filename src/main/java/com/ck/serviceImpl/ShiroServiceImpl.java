package com.ck.serviceImpl;

import com.ck.dao.ShiroDao;
import com.ck.domain.entity.MenuEntity;
import com.ck.domain.entity.RoleEntity;
import com.ck.manager.ShiroManager;
import com.ck.service.ShiroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiroServiceImpl implements ShiroService {

    @Autowired
    private ShiroManager shiroManager;

    /**
     * 保存用户角色信息
     * @param userId 用户ID
     * @param roleIdList 角色ID集合
     */
    @Override
    public void createUserRole(long userId, List<Long> roleIdList){
        shiroManager.createUserRole(userId,roleIdList);
    }

    /**
     * 删除用户角色
     * @param userId 用户ID
     * @param roleIdList    角色ID集合
     */
    @Override
    public void deleteUserRole(long userId, List<Long> roleIdList) {
        shiroManager.deleteUserRole(userId, roleIdList);
    }

    /**
     * 删除角色权限信息
     * @param roleId    角色ID
     * @param menuIdList    权限信息
     */
    @Override
    public void deleteRoleMenu(long roleId, List<Long> menuIdList) {
        shiroManager.deleteRoleMenu(roleId, menuIdList);
    }

    /**
     * 保存角色权限信息
     * @param roleId    角色ID
     * @param menuIdList 权限信息
     */
    @Override
    public void createRoleMenu(long roleId, List<Long> menuIdList) {
        shiroManager.createRolePermission(roleId,menuIdList);
    }

    /**
     * 新建角色,并返回角色ID
     * @param roleEntities 角色列表
     * @param createBy 创建人
     */
    @Override
    public List<RoleEntity> createRole(List<RoleEntity> roleEntities, String createBy) {
        return shiroManager.createRole(roleEntities, createBy);
    }

    /**
     * 取消角色
     * @param roleIdList 角色列表
     * @param modifyBy 修改人
     */
    @Override
    public void closeRole(List<Long> roleIdList, String modifyBy) {
        shiroManager.closeRole(roleIdList, modifyBy);
    }

    @Override
    public void enableRole(List<Long> roleIdList, String modifyBy) {
        shiroManager.enableRole(roleIdList, modifyBy);
    }

    /**
     * 创建权限信息
     * @param menuEntityList  权限列表
     * @param createBy  创建人
     */
    @Override
    public List<MenuEntity> createMenu(List<MenuEntity> menuEntityList, String createBy) {
        return shiroManager.createMenu(menuEntityList, createBy);
    }

    /**
     * 关闭权限
     * @param menuIdList 权限列表
     * @param modifyBy  修改人
     */
    @Override
    public void closeMenu(List<Long> menuIdList, String modifyBy) {
        shiroManager.closeMenu(menuIdList, modifyBy);
    }

    /**
     * 启用权限
     * @param menuIdList 权限列表
     * @param modifyBy  修改人
     */
    @Override
    public void enableMenu(List<Long> menuIdList, String modifyBy) {
        shiroManager.enableMenu(menuIdList, modifyBy);
    }
}

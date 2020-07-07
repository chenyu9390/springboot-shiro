package com.ck.dao.impl;

import com.ck.dao.BaseDao;
import com.ck.dao.ShiroDao;
import com.ck.domain.entity.MenuEntity;
import com.ck.domain.entity.RoleEntity;
import com.ck.util.StringValueUtil;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ShiroDaoImpl extends BaseDao implements ShiroDao {

    /**
     * 查询用户菜单栏权限
     * @param userId 用户ID
     * @param type 0 : 菜单权限 1：按钮权限 2:所有权限
     * @return
     */
    @Override
    public List<Map<String, Object>> getPermissionByUserId(Long userId,Integer type) {
        String sql = "SELECT m.MENU_ID,m.PARENT_ID,m.MENU_NAME,m.PERMS,m.ORDER_NUM FROM user_role ur " +
                        " INNER JOIN role r ON ur.ROLE_ID = r.ROLE_ID " +
                        " INNER JOIN role_menu rm ON r.ROLE_ID = rm.ROLE_ID " +
                        " INNER JOIN menu m ON m.MENU_ID = rm.MENU_ID " +
                        " WHERE ur.USER_ID = ? ";
        List<Object> objects = new ArrayList<>();
        objects.add(userId);
        if(type != 2){
            sql += " AND m.TYPE = ? ";
            objects.add(type);
        }
        sql += " ORDER BY m.MENU_ID ASC,m.ORDER_NUM ASC";
        return queryForList(sql,objects.toArray());
    }

    /**
     * 查询用户角色信息
     * @param userId 用户ID
     * @return
     */
    @Override
    public List<RoleEntity> getRoleByUserId(Long userId) {
        String sql = "SELECT r.ROLE_ID,r.ROLE_NAME,r.REMARK,r.IS_ENABLE FROM user_role ur,role r WHERE ur.ROLE_ID = r.ROLE_ID AND r.IS_ENABLE = 1 AND ur.USER_ID = ?";
        Optional<List<Map<String,Object>>> optionalMaps = Optional.ofNullable(queryForList(sql,new Object[]{userId}));
        List<RoleEntity> roles = new ArrayList<>();
        optionalMaps.orElse(Collections.emptyList()).forEach(map -> {
            RoleEntity role = new RoleEntity();
            role.setRoleId(StringValueUtil.getValueByMap(map,"ROLE_ID",0L));
            role.setIsEnable(StringValueUtil.getValueByMap(map,"IS_ENABLE",0L).intValue());
            role.setRemark(StringValueUtil.getValueByMap(map,"REMARK",""));
            role.setRoleName(StringValueUtil.getValueByMap(map,"ROLE_NAME",""));
            roles.add(role);
        });
        return roles;
    }

    /**
     * 保存用户角色信息
     * @param userId 用户ID
     * @param roleList  角色ID
     */
    @Override
    public void createUserRole(long userId,List<Long> roleList) {
        String sql = "INSERT IGNORE INTO `user_role` (`USER_ID`, `ROLE_ID`) VALUES (?,?)";
        List<Object[]> objects = new ArrayList<>(roleList.size());
        for(Long roleId : roleList){
            objects.add(new Object[]{userId,roleId});
        }
        batchCreate(sql,objects);
    }

    /**
     * 保存角色权限信息
     * @param roleId 角色ID
     * @param permissionEntityList 权限列表
     */
    @Override
    public void createRolePermission(long roleId, List<Long> permissionEntityList) {
        String sql = "INSERT IGNORE INTO `role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (?,?)";
        List<Object[]> objects = new ArrayList<>(permissionEntityList.size());
        for(Long permissionId : permissionEntityList){
            objects.add(new Object[]{roleId,permissionId});
        }
        batchCreate(sql,objects);
    }

    /**
     * 新建角色信息
     * @param roleEntities  角色列表
     * @param createBy  创建人
     */
    @Override
    public List<RoleEntity> createRole(List<RoleEntity> roleEntities, String createBy) {

        for(RoleEntity entity : roleEntities){
            String sql = "INSERT INTO `role` (`ROLE_NAME`, `REMARK`, `IS_ENABLE` `CREATE_BY`) VALUES (?,?,?,?)";
            long roleId = create(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(sql,new String[]{"ROLE_ID"});
                    ps.setString(1,entity.getRoleName());
                    ps.setString(2,entity.getRemark());
                    ps.setInt(3,entity.getIsEnable());
                    ps.setString(4,createBy);
                    return ps;
                }
            });
            entity.setRoleId(roleId);
        }
        return roleEntities;
    }
    /**
     * 取消角色
     * @param roleIdList 角色列表
     * @param modifyBy 修改人
     */
    @Override
    public void closeRole(List<Long> roleIdList, String modifyBy) {
        String sql = "UPDATE role SET IS_ENABLE = 0,MODIFY_BY = ? WHERE ROLE_ID = ?";
        List<Object[]> objects = new ArrayList<>();
        for(Long roleId : roleIdList){
            objects.add(new Object[]{modifyBy,roleId});
        }
        batchUpdate(sql,objects);
    }

    /**
     * 启用角色
     * @param roleIdList 角色列表
     * @param modifyBy 修改人
     */
    @Override
    public void enableRole(List<Long> roleIdList, String modifyBy) {
        String sql = "UPDATE role SET IS_ENABLE = 1,MODIFY_BY = ? WHERE ROLE_ID = ?";
        List<Object[]> objects = new ArrayList<>();
        for(Long roleId : roleIdList){
            objects.add(new Object[]{modifyBy,roleId});
        }
        batchUpdate(sql,objects);
    }

    /**
     * 创建权限信息
     * @param menuEntityList  权限列表
     * @param createBy  创建人
     */
    @Override
    public List<MenuEntity> createMenu(List<MenuEntity> menuEntityList, String createBy) {
        for(MenuEntity entity : menuEntityList){
            String sql = "INSERT INTO `menu` (`PARENT_ID`, `MENU_NAME`, `PERMS`, `TYPE`, `IS_ENABLE`, `ORDER_NUM`, `CREATE_BY`) VALUES (?,?,?,?,?,?,?)";
            long menuId = create(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                    PreparedStatement ps = con.prepareStatement(sql,new String[]{"MENU_ID"});
                    ps.setLong(1,entity.getParentId());
                    ps.setString(2,entity.getMenuName());
                    ps.setString(3,entity.getPerms());
                    ps.setInt(4,entity.getType());
                    ps.setInt(5,entity.getIsEnable());
                    ps.setLong(6,entity.getOrderNum());
                    ps.setString(7,createBy);
                    return ps;
                }
            });
            entity.setMenuId(menuId);
        }
        return menuEntityList;
    }

    /**
     * 关闭权限
     * @param menuIdList 权限列表
     * @param modifyBy  修改人
     */
    @Override
    public void enableMenu(List<Long> menuIdList, String modifyBy) {
        String sql = "UPDATE menu SET IS_ENABLE = 1,MODIFY_BY=? WHERE MENU_ID = ?";
        List<Object[]> objects = new ArrayList<>();
        for(Long id : menuIdList){
            objects.add(new Object[]{modifyBy,id});
        }
        batchUpdate(sql,objects);
    }

    /**
     * 启用权限
     * @param menuIdList 权限列表
     * @param modifyBy  修改人
     */
    @Override
    public void closeMenu(List<Long> menuIdList, String modifyBy) {
        String sql = "UPDATE menu SET IS_ENABLE = 0,MODIFY_BY=? WHERE MENU_ID = ?";
        List<Object[]> objects = new ArrayList<>();
        for(Long id : menuIdList){
            objects.add(new Object[]{modifyBy,id});
        }
        batchUpdate(sql,objects);
    }

    /**
     * 删除用户角色
     * @param userId 用户ID
     * @param roleIdList    角色ID集合
     */
    @Override
    public void deleteUserRole(long userId, List<Long> roleIdList) {
        String sql = "DELETE FROM user_role WHERE USER_ID = ? AND ROLE_ID = ?";
        List<Object[]> objects = new ArrayList<>(roleIdList.size());
        for(Long id : roleIdList){
            objects.add(new Object[]{userId,id});
        }
        batchUpdate(sql,objects);
    }

    /**
     * 删除角色权限
     * @param roleId    角色ID
     * @param menuIdList    权限ID集合
     */
    @Override
    public void deleteRoleMenu(long roleId, List<Long> menuIdList) {
        String sql = "DELETE FROM role_menu WHERE MENU_ID = ? AND ROLE_ID = ?";
        List<Object[]> objects = new ArrayList<>(menuIdList.size());
        for(Long id : menuIdList){
            objects.add(new Object[]{id,roleId});
        }
        batchUpdate(sql,objects);
    }
}

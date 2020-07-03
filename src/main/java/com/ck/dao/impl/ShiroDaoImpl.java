package com.ck.dao.impl;

import com.ck.dao.BaseDao;
import com.ck.dao.ShiroDao;
import com.ck.domain.entity.PermissionEntity;
import com.ck.domain.entity.RoleEntity;
import com.ck.util.StringValueUtil;
import org.springframework.stereotype.Repository;

import javax.management.relation.Role;
import java.util.*;

@Repository
public class ShiroDaoImpl extends BaseDao implements ShiroDao {

    /**
     * 查询用户菜单栏权限
     * @param userId 用户ID
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
        if(type == 2){
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
    public void createUserRole(long userId,List<String> roleList) {
        String sql = "INSERT IGNORE INTO `user_role` (`USER_ID`, `ROLE_ID`) VALUES (?,?)";
        List<Object[]> objects = new ArrayList<>(roleList.size());
        for(String roleId : roleList){
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
    public void createRolePermission(long roleId, List<String> permissionEntityList) {
        String sql = "INSERT IGNORE INTO `role_menu` (`ROLE_ID`, `MENU_ID`) VALUES (?,?)";
        List<Object[]> objects = new ArrayList<>(permissionEntityList.size());
        for(String permissionId : permissionEntityList){
            objects.add(new Object[]{roleId,permissionId});
        }
        batchCreate(sql,objects);
    }
}

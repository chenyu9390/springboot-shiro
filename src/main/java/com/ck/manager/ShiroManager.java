package com.ck.manager;

import com.ck.dao.ShiroDao;
import com.ck.domain.MenuTree;
import com.ck.domain.entity.MenuEntity;
import com.ck.domain.entity.RoleEntity;
import com.ck.util.JSONUtil;
import com.ck.util.StringValueUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class ShiroManager {

    @Autowired
    private ShiroDao shiroDao;

    private static final long TOP_NODE_ID = 0;

    /**
     * 查询用户所有权限信息
     * @param userId 用户ID
     * @return
     */
    public List<MenuEntity> getPermissionEntityByUserId(long userId){
        Optional<List<Map<String,Object>>> optionalMaps = Optional.ofNullable(shiroDao.getPermissionByUserId(userId,2));
        List<MenuEntity> menuList = new ArrayList<>(optionalMaps.isPresent()?optionalMaps.get().size():0);
        optionalMaps.orElse(Collections.emptyList()).forEach(map -> {
            String perms = StringValueUtil.getValueByMap(map,"PERMS","");
            if(!StringValueUtil.isEmpty(perms)){
                MenuEntity entity = MenuEntity.builder()
                        .menuId(StringValueUtil.getValueByMap(map,"MENU_ID",0))
                        .parentId(StringValueUtil.getValueByMap(map,"PARENT_ID",0))
                        .menuName(StringValueUtil.getValueByMap(map,"MENU_NAME",""))
                        .perms(perms)
                        .orderNum(StringValueUtil.getValueByMap(map,"ORDER_NUM",0))
                        .build();
                menuList.add(entity);
            }
        });
        return menuList;
    }

    /**
     * 通过用户ID查询按钮权限
     * @param userId 用户信息
     * @return
     */
    public List<MenuTree> getButtonByUserId(long userId){
        Optional<List<Map<String,Object>>> optionalMaps = Optional.ofNullable(shiroDao.getPermissionByUserId(userId,1));
        List<MenuTree> menuList = new ArrayList<>(optionalMaps.isPresent()?optionalMaps.get().size():0);
        optionalMaps.orElse(Collections.emptyList()).forEach(map -> {
            MenuTree entity = MenuTree.builder()
                    .id(StringValueUtil.getValueByMap(map,"MENU_ID",0))
                    .parentId(StringValueUtil.getValueByMap(map,"PARENT_ID",0))
                    .title(StringValueUtil.getValueByMap(map,"MENU_NAME",""))
                    .perms(StringValueUtil.getValueByMap(map,"PERMS",""))
                    .orderNum(StringValueUtil.getValueByMap(map,"ORDER_NUM",0).intValue())
                    .build();
            menuList.add(entity);
        });
        return menuList;
    }

    /**
     * 通过用户ID查询菜单栏权限
     * @param userId 用户信息
     * @return
     */
    public List<MenuTree> getFunctionByUserId(long userId){
        Optional<List<Map<String,Object>>> optionalMaps = Optional.ofNullable(shiroDao.getPermissionByUserId(userId,0));
        List<MenuTree> menuList = new ArrayList<>(optionalMaps.isPresent()?optionalMaps.get().size():0);
        optionalMaps.orElse(Collections.emptyList()).forEach(map -> {
            String perms = StringValueUtil.getValueByMap(map,"PERMS","");
            MenuTree entity = MenuTree.builder()
                    .id(StringValueUtil.getValueByMap(map,"MENU_ID",0))
                    .parentId(StringValueUtil.getValueByMap(map,"PARENT_ID",0))
                    .title(StringValueUtil.getValueByMap(map,"MENU_NAME",""))
                    .perms(perms)
                    .orderNum(StringValueUtil.getValueByMap(map,"ORDER_NUM",0).intValue())
                    .build();
            menuList.add(entity);

        });
        List<MenuTree> parentMenuList = menuList.stream().filter((MenuTree t) -> t.getParentId() == TOP_NODE_ID).collect(Collectors.toList());
        List<MenuTree> chileMenuList = menuList.stream().filter((MenuTree t) -> t.getParentId() != TOP_NODE_ID).collect(Collectors.toList());
        parentMenuList.forEach(menu -> {
            convertMenus(menu,chileMenuList);
        });
        return parentMenuList;
    }

    /**
     * 查询用户角色信息
     * @param userId userId
     * @return
     */
    public List<RoleEntity> getRoleByUserId(long userId){
        return shiroDao.getRoleByUserId(userId);
    }



    /**
     * 数据类型转换
     * @param tree 菜单
     * @param menuList 权限集合
     * @return
     */
    public MenuTree convertMenus(MenuTree tree,List<MenuTree> menuList){
        menuList.forEach(meun -> {
            if(meun.getParentId().longValue() == tree.getId().longValue()){
                tree.setHasChild(true);
                meun.setHasParent(true);
                List<MenuTree> childs = tree.getChilds();
                if(childs == null){
                    childs = new ArrayList<>();
                    tree.setChilds(childs);
                }
                if(!childs.contains(meun)){
                    childs.add(meun);
                }
                convertMenus(meun,menuList);
            }
        });
        return tree;
    }

    /**
     * 保存用户角色信息
     * @param userId 用户ID
     * @param roleIdList  权限列表
     */
    public void createUserRole(long userId,List<Long> roleIdList){
        shiroDao.createUserRole(userId,roleIdList);
    }

    /**
     * 保存角色权限信息
     * @param roleId
     * @param permissionIdList 功能集合
     */
    public void createRolePermission(long roleId,List<Long> permissionIdList){
        shiroDao.createRolePermission(roleId,permissionIdList);
    }

    /**
     * 新建角色
     * @param roleEntities 角色列表
     * @param createBy 创建人
     */
    public List<RoleEntity> createRole(List<RoleEntity> roleEntities, String createBy) {
        return shiroDao.createRole(roleEntities, createBy);
    }

    /**
     * 取消角色
     * @param roleIdList 角色列表
     * @param modifyBy 修改人
     */
    public void closeRole(List<Long> roleIdList, String modifyBy) {
        shiroDao.closeRole(roleIdList, modifyBy);
    }

    public void enableRole(List<Long> roleIdList, String modifyBy){
        shiroDao.enableRole(roleIdList, modifyBy);
    }

    /**
     * 创建权限信息
     * @param menuEntityList  权限列表
     * @param createBy  创建人
     */
    public List<MenuEntity> createMenu(List<MenuEntity> menuEntityList, String createBy) {
        return shiroDao.createMenu(menuEntityList,createBy);
    }

    /**
     * 关闭权限
     * @param menuIdList 权限列表
     * @param modifyBy  修改人
     */
    public void closeMenu(List<Long> menuIdList, String modifyBy) {
        shiroDao.closeMenu(menuIdList, modifyBy);
    }

    /**
     * 启用权限
     * @param menuIdList 权限列表
     * @param modifyBy  修改人
     */
    public void enableMenu(List<Long> menuIdList, String modifyBy) {
        shiroDao.enableMenu(menuIdList, modifyBy);
    }

    /**
     * 删除用户角色
     * @param userId 用户ID
     * @param roleIdList    角色ID集合
     */
    public void deleteUserRole(long userId,List<Long> roleIdList){
        shiroDao.deleteUserRole(userId, roleIdList);
    }

    /**
     * 删除角色权限ID
     * @param roleId    角色ID
     * @param menuIdList    权限ID集合
     */
    public void deleteRoleMenu(long roleId,List<Long> menuIdList){
        shiroDao.deleteRoleMenu(roleId, menuIdList);
    }













    public static void main(String[] args) throws JsonProcessingException {

        MenuTree tree = MenuTree.builder()
                .id(1L)
                .parentId(0L)
                .title("1级菜单")
                .build();

        MenuTree tree1 = MenuTree.builder()
                .id(2L)
                .parentId(1L)
                .title("2级菜单")
                .build();
        MenuTree tree6 = MenuTree.builder()
                .id(7L)
                .parentId(1L)
                .title("2级菜单")
                .build();

        MenuTree tree2 = MenuTree.builder()
                .id(3L)
                .parentId(2L)
                .title("3级菜单")
                .build();

        MenuTree tree5 = MenuTree.builder()
                .id(6L)
                .parentId(2L)
                .title("3级菜单")
                .build();

        MenuTree tree3 = MenuTree.builder()
                .id(4L)
                .parentId(3L)
                .title("4级菜单")
                .build();
        MenuTree tree4 = MenuTree.builder()
                .id(5L)
                .parentId(3L)
                .title("4级菜单")
                .build();

        MenuTree tree7 = MenuTree.builder()
                .id(8L)
                .parentId(7L)
                .title("3级菜单")
                .build();
        List<MenuTree> menuList = Arrays.asList(tree,tree1,tree2,tree3,tree4,tree5,tree6,tree7);
        List<MenuTree> parentMenuList = menuList.stream().filter((MenuTree t) -> t.getParentId() == TOP_NODE_ID).collect(Collectors.toList());
        ShiroManager shiroManager = new ShiroManager();
        parentMenuList.forEach(menu -> {
            try {
                System.out.println(JSONUtil.writeValue(shiroManager.convertMenus(menu,menuList)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });

    }


}

package com.ck.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class RoleEntity implements Serializable {

    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 角色名
     */
    private String roleName;
    /**
     *  描述
     */
    private String remark;
    /**
     *  是否启用，0：停用 1：启用
     */
    private Integer isEnable;

    /**
     * 权限列表
     */
    private List<Menu> menuList;
}

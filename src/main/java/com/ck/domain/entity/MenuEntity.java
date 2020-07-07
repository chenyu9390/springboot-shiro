package com.ck.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class MenuEntity implements Serializable {
    /**
     * 权限ID
     */
    private Long menuId;
    /**
     * 父权限ID
     */
    private Long parentId;
    /**
     *  菜单/按钮名称
     */
    private String menuName;
    /**
     *  权限标识
     */
    private String perms;
    /**
     * 类型 0菜单 1按钮
     */
    private Integer type;
    /**
     * 排序
     */
    private Long orderNum;
    /**
     * 0:停用 1：启用
     */
    private Integer isEnable;
}

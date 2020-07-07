package com.ck.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class UserEntity {

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 状态 0锁定 1有效
     */
    private Integer status;
    /**
     * 性别 0男 1女 2保密
     */
    private Integer sex;

    /**
     * 角色列表
     */
    private List<RoleEntity> roleEntityList;

}

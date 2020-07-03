package com.ck.domain.entity;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserEntity {

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

}

package com.ck.domain.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RoleEntity implements Serializable {

    private Long roleId;
    private String roleName;
    private String remark;
    private String createTime;
    private String modifyTime;
    private Integer isEnable;
}

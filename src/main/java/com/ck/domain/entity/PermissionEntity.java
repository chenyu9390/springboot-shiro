package com.ck.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
public class PermissionEntity implements Serializable {
    private Long menuId;
    private Long parentId;
    private String menuName;
    private String url;
    private String perms;
    private Integer type;
    private Long orderNum;
    private String createTime;
    private String modifyTime;
}

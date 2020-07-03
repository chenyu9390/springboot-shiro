package com.ck.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
public class MenuTree implements Serializable {
    private static final long serialVersionUID = 7681873362531265829L;

    private Long id;
    private Long parentId;
    private String title;
    private List<MenuTree> childs;
    private boolean hasParent;
    private boolean hasChild;
    private String perms;
    private Integer orderNum;

}

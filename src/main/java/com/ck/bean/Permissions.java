package com.ck.bean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Permissions implements Serializable {
    private String id;
    private String permissionsName;
}

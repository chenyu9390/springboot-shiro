package com.ck.service;

import com.ck.bean.User;

public interface LoginService {

    /**
     * 查询用户信息
     * @param name
     * @return
     */
    User getUserByName(String name);
}

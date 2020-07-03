package com.ck.service;

import com.ck.domain.entity.UserEntity;

public interface LoginService {

    /**
     * 查询用户信息
     * @param userId
     * @return
     */
    UserEntity getUserById(long userId);


    /**
     * 查询用户信息
     * @param userName
     * @return
     */
    UserEntity getUserByName(String userName);

}

package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author leiwenfeng
 * Date: 2023/9/9 18:01
 */
public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);
}

package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

/**
 * @author leiwenfeng
 * Date: 2023/9/11 11:57
 */
public interface UserService {
    User wxLogin(UserLoginDTO userLoginDTO);
}

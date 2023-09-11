package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {


    User getByOpenId(String code);

    void insert(User user);
}

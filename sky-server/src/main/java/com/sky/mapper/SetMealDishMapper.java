package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    List<Long> selectSetmealIdsByDishId(List<Long> ids);
}
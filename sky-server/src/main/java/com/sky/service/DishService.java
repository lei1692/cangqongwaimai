package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;

public interface DishService {
    /***
     * 新增菜品 菜品有口味
     * @param dishDTO
     */
     void saveWithFlavor(DishDTO dishDTO);

    /***
     * 分页查询菜品
     * @param dishPageQueryDTO
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);
}

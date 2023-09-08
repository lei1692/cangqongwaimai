package com.sky.service;

import com.sky.dto.DishDTO;

public interface DishService {
    /***
     * 新增菜品 菜品有口味
     * @param dishDTO
     */
     void saveWithFlavor(DishDTO dishDTO);
}

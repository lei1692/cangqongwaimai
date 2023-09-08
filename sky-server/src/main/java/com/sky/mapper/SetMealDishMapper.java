package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {

    List<Long> selectSetmealIdsByDishId(List<Long> ids);


    void insertBatch(List<SetmealDish> setmealDishes);


    List<SetmealDish> getBySetmealId(Long id);


    void deleteBySetmealId(Long[] ids);
}

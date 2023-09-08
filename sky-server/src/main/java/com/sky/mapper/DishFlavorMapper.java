package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    /***
     * 批量新增菜品口味
     * @param flavors
     */
//    @AutoFill(Value = OperationType.INSERT)
    void insertBatch(List<DishFlavor> flavors);

    @Delete("delete from dish_flavor where dish_id = #{id}")
    void deleteByDishId(Long dishId);

    List<DishFlavor> getByDishId(Long id);
}

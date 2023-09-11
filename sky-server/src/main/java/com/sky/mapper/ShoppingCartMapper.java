package com.sky.mapper;

import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {


    ShoppingCart getByDishId(ShoppingCart shoppingCart);


    void insert(ShoppingCart shoppingCart);

    void update(ShoppingCart shoppingCart);

    @Select("select * from shopping_cart where user_id = #{currentId}")
    List<ShoppingCart> getList(Long currentId);
}

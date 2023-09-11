package com.sky.service;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;

import java.util.List;

/**
 * @author leiwenfeng
 * Date: 2023/9/11 15:37
 */
public interface ShoppingCartService {
    void add(ShoppingCartDTO shoppingCartDTO);


    List<ShoppingCart> getList();
}

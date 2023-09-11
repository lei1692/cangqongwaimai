package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author leiwenfeng
 * Date: 2023/9/11 15:37
 */
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetmealMapper setmealMapper;
    @Override
    public void add(ShoppingCartDTO shoppingCartDTO) {
        //1.封装为ShoppingCart对象
        //2.设置ShoppingCart对象的属性值
        ShoppingCart shoppingCart = setNameImageAmount(shoppingCartDTO);
        //3.判断购物车表中是否已经存在该商品 dish_id 或 setmeal_id
        ShoppingCart getShoppingCart = shoppingCartMapper.getByDishId(shoppingCart);
        if (getShoppingCart == null){
            //4.如果不存在，添加该商品到购物车
            shoppingCart.setNumber(1);
            shoppingCartMapper.insert(shoppingCart);
        }else {
            //5.如果存在，更新购物车中该商品的数量和金额
            shoppingCart.setNumber(getShoppingCart.getNumber()+1);
            BigDecimal amount = shoppingCart.getAmount().add(getShoppingCart.getAmount());

            shoppingCart.setAmount(amount);
            shoppingCartMapper.update(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> getList() {
        List<ShoppingCart> shoppingCartList = shoppingCartMapper.getList(BaseContext.getCurrentId());
        return shoppingCartList;
    }

    @Override
    public void cleanAll() {
        setmealMapper.cleanAll(BaseContext.getCurrentId());
    }

    @Override
    public void sub(ShoppingCartDTO shoppingCartDTO) {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .setmealId(shoppingCartDTO.getSetmealId())
                .dishId(shoppingCartDTO.getDishId())
                .dishFlavor(shoppingCartDTO.getDishFlavor())
                .build();
        shoppingCartMapper.sub(shoppingCart);
    }

    private ShoppingCart setNameImageAmount(ShoppingCartDTO shoppingCartDTO){
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .dishFlavor(shoppingCartDTO.getDishFlavor())
                .dishId(shoppingCartDTO.getDishId())
                .setmealId(shoppingCartDTO.getSetmealId())
                .userId(BaseContext.getCurrentId())
                .build();

        if (shoppingCartDTO.getDishId() !=null){
            Dish dish = dishMapper.getById(shoppingCartDTO.getDishId());

            shoppingCart.setName(dish.getName());
            shoppingCart.setImage(dish.getImage());
            shoppingCart.setAmount(dish.getPrice());
        }else {
            Setmeal setmeal = setmealMapper.getById(shoppingCartDTO.getSetmealId());

            shoppingCart.setName(setmeal.getName());
            shoppingCart.setImage(setmeal.getImage());
            shoppingCart.setAmount(setmeal.getPrice());
        }
        return shoppingCart;
    }
}

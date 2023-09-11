package com.sky.controller.user;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingCartService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author leiwenfeng
 * Date: 2023/9/11 15:34
 */
@RestController
@RequestMapping("/user/shoppingCart")
@Slf4j
@Api(tags = "用户端购物车接口")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public Result add(@RequestBody ShoppingCartDTO shoppingCartDTO){
        shoppingCartService.add(shoppingCartDTO);

        return Result.success();
    }
    @GetMapping("/list")
    public Result<List<ShoppingCart>>  list(){
        List<ShoppingCart> shoppingCartList = shoppingCartService.getList();
        return Result.success(shoppingCartList);
    }


}

package com.sky.controller.admin;

import com.sky.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * Author: leiwenfeng
 * Date: 2023/9/9 14:56
 */
@RestController("admineShopController")
@RequestMapping("/admin/shop")
@Slf4j
@Api(tags = "管理端店铺接口")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 店铺状态修改
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    @ApiOperation("店铺状态修改")
    public Result setStatus(@PathVariable Integer status){
        log.info("店铺状态修改");
        redisTemplate.opsForValue().set("shop_status",status);
        log.info("status:{}",status);
        return Result.success();
    }

    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus(){
        log.info("获取店铺状态");
        Integer status = (Integer) redisTemplate.opsForValue().get("shop_status");
        log.info("status:{}",status);
        return Result.success(status);
    }

}

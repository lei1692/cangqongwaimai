package com.sky.controller.user;

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
@RestController("userShopController")
@RequestMapping("/user/shop")
@Slf4j
@Api(tags = "用户端店铺接口")
public class ShopController {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取店铺状态
     * @return
     */
    @GetMapping("/status")
    @ApiOperation("获取店铺状态")
    public Result<Integer> getStatus(){
        log.info("获取店铺状态");
        Integer status = (Integer) redisTemplate.opsForValue().get("shop_status");
        return Result.success(status);
    }

}

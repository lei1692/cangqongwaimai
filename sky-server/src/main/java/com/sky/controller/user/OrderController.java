package com.sky.controller.user;

import com.alibaba.fastjson.JSON;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.OrderService;
import com.sky.vo.OrderPageVo;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import com.sky.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author leiwenfeng
 * Date: 2023/9/11 18:11
 */
@RestController
@RequestMapping("/user/order")
@Slf4j
@Api(tags = "用户端订单接口")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 订单提交
     * @param ordersSubmitDTO
     * @return
     */
    @PostMapping("/submit")
    public Result<OrderSubmitVO> submit(@RequestBody OrdersSubmitDTO ordersSubmitDTO){
        log.info("订单提交：{}",ordersSubmitDTO);
        OrderSubmitVO orderSubmitVO = orderService.submit(ordersSubmitDTO);
        return Result.success(orderSubmitVO);
    }

    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    @PutMapping("/payment")
    public Result<OrderPaymentVO> payment(@RequestBody OrdersPaymentDTO ordersPaymentDTO){
        log.info("订单支付：{}",ordersPaymentDTO);
        OrderPaymentVO orderPaymentVO = new OrderPaymentVO().builder()
                .paySign("paySuccess")
                .timeStamp(String.valueOf(System.currentTimeMillis()))
                .nonceStr("nonceStr")
                .signType("signType")
                .packageStr("packageStr")
                .build();
        orderService.updateOrderStatusByOrderNumber(ordersPaymentDTO.getOrderNumber());

        //消息提醒 type 1来订单 2 客户催单 orderID content
        Map map = new HashMap();
        map.put("type",1);
        map.put("orderId",ordersPaymentDTO.getOrderNumber());
        map.put("content","您有新的订单，请及时处理。订单号："+ordersPaymentDTO.getOrderNumber());

        String jsonString = JSON.toJSONString(map);

        webSocketServer.sendToAllClient(jsonString);

        return Result.success(orderPaymentVO);
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param status
     * @return
     */
    @GetMapping("/historyOrders")
    public Result<PageResult> historyOrders(String page,String pageSize, String status){
        PageResult pageResult = orderService.OrderPage4User(page,pageSize,status);
        return Result.success(pageResult);
    }

    /**
     * 根据订单id查询订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/orderDetail/{orderId}")
    public Result<OrderPageVo> orderDetail(@PathVariable("orderId") Long orderId){
        OrderPageVo orderPageVO = orderService.orderDetailByOrderId(orderId);
        return Result.success(orderPageVO);
    }

    /**
     * 取消订单 6已取消
     * @param orderId
     * @return
     */
    @PutMapping("/cancel/{orderId}")
    public Result cancel(@PathVariable("orderId") Long orderId){
        orderService.cancleByOrderId(orderId);
        return Result.success();
    }


    /**
     * 再来一单
     * @param orderId
     * @return
     */
    @PostMapping("/repetition/{orderId}")
    public Result repetition(@PathVariable("orderId") Long orderId){
        orderService.repetitionByOrderId(orderId);
        return Result.success();
    }

    @GetMapping("/reminder/{id}")
    public Result reminder(@PathVariable("id") Long id){
        orderService.reminder(id);
        return Result.success();
    }
}

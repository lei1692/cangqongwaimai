package com.sky.task;

import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author leiwenfeng
 * Date: 2023/9/12 3:47
 */
@Component
@Slf4j
public class OrderTask {
    @Autowired
    private OrderMapper orderMapper;

    @Scheduled(cron = "0 * * * * ? ")
    //@Scheduled(cron = "2/5 * * * *  ?")
    public void processTimeOutOrder(){
        log.info("定时执行处于超时的订单：{}", System.currentTimeMillis());
        LocalDateTime orderTime = LocalDateTime.now().plusMinutes(-15);

        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.PENDING_PAYMENT,orderTime);

        if (ordersList!=null && ordersList.size()>0){
            ordersList.forEach(order -> {
                order.setStatus(Orders.CANCELLED);
                order.setCancelReason("超时未支付，自动取消");
                orderMapper.update(order);
            });
        }
    }

    @Scheduled(cron = "0 0 1 * * ? ")
    //@Scheduled(cron = "0/5 * * * *  ?")
    public void processDeliveryOrder(){
        log.info("定时执行处于派送中的订单：{}", LocalDateTime.now());
        LocalDateTime time = LocalDateTime.now().plusMinutes(-60);
        List<Orders> ordersList = orderMapper.getByStatusAndOrderTime(Orders.DELIVERY_IN_PROGRESS,time);

        if (ordersList!=null && ordersList.size()>0){
            ordersList.forEach(order -> {
                order.setStatus(Orders.COMPLETED);
                order.setCancelReason("超时未支付，自动取消");
                orderMapper.update(order);
            });
        }

    }
}

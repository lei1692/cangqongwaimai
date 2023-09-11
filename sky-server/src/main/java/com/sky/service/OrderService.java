package com.sky.service;

import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.result.PageResult;
import com.sky.vo.OrderPageVo;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;

/**
 * @author leiwenfeng
 * Date: 2023/9/11 18:13
 */
public interface OrderService {
    OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO);
    /**
     * 订单支付
     * @param ordersPaymentDTO
     * @return
     */
    OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception;

    /**
     * 支付成功，修改订单状态
     * @param outTradeNo
     */
    void paySuccess(String outTradeNo);

    void updateOrderStatusByOrderNumber(String orderNumber);

    PageResult OrderPage4User(String page, String pageSize, String status);

    OrderPageVo orderDetailByOrderId(Long orderId);

    void cancleByOrderId(Long orderId);

    void repetitionByOrderId(Long orderId);
}

package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.entity.Orders;
import com.sky.vo.OrderPageVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface OrderMapper {


    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    void updateOrderStatusByOrderNumber(String orderNumber);

    Page<OrderPageVo> getOrderPage4User(Long userId, String status);

    @Select("select * from orders where id = #{orderId}")
    OrderPageVo getByOrderId(Long orderId);

    @Update("update orders set status = 6 where id = #{orderId}")
    void updateOrderStatusByOrderNumber6(Long orderId);
}

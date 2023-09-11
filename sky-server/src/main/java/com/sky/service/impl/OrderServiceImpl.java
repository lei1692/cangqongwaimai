package com.sky.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPaymentDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.*;
import com.sky.exception.AddressBookBusinessException;
import com.sky.exception.OrderBusinessException;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.result.PageResult;
import com.sky.service.AddressBookService;
import com.sky.service.OrderService;
import com.sky.service.ShoppingCartService;
import com.sky.utils.WeChatPayUtil;
import com.sky.vo.OrderPageVo;
import com.sky.vo.OrderPaymentVO;
import com.sky.vo.OrderSubmitVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author leiwenfeng
 * Date: 2023/9/11 18:13
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;
    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    private ShoppingCartService shoppingCartService;
    @Autowired
    private WeChatPayUtil weChatPayUtil;
    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public OrderSubmitVO submit(OrdersSubmitDTO ordersSubmitDTO) {

        Long userId = BaseContext.getCurrentId();
        //1.处理异常 地址簿为空 购物车为空
        AddressBook addressBook = addressBookService.getById(ordersSubmitDTO.getAddressBookId());
        if (addressBook == null) {
            throw new AddressBookBusinessException(MessageConstant.ADDRESS_BOOK_IS_NULL);
        }
        List<ShoppingCart> shoppingCartList = shoppingCartService.getList();
        if (shoppingCartList == null || shoppingCartList.isEmpty()) {
            throw new AddressBookBusinessException(MessageConstant.SHOPPING_CART_IS_NULL);
        }

        //2.保存订单
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setStatus(Orders.PENDING_PAYMENT);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setPhone(addressBook.getPhone());
        orders.setConsignee(addressBook.getConsignee());
        orders.setUserId(userId);

        orderMapper.insert(orders);
        //3.保存订单详情
        List<OrderDetail>  orderDetailList = new ArrayList<>();
        Long ordersId = orders.getId();
        shoppingCartList.forEach(shoppingCart -> {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(shoppingCart, orderDetail);
            orderDetail.setOrderId(ordersId);

            orderDetailList.add(orderDetail);
        });
        orderDetailMapper.insertBatch(orderDetailList);
        //4.清空购物车
        shoppingCartService.cleanAll();
        //5.返回订单信息 VO对象
        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(ordersId)
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber())
                .orderAmount(orders.getAmount())
                .build();
        return orderSubmitVO;

    }

    /**
     * 订单支付
     *
     * @param ordersPaymentDTO
     * @return
     */

    public OrderPaymentVO payment(OrdersPaymentDTO ordersPaymentDTO) throws Exception {
        // 当前登录用户id
        Long userId = BaseContext.getCurrentId();
        User user = userMapper.getById(userId);

        //调用微信支付接口，生成预支付交易单
        JSONObject jsonObject = weChatPayUtil.pay(
                ordersPaymentDTO.getOrderNumber(), //商户订单号
                new BigDecimal(0.01), //支付金额，单位 元
                "苍穹外卖订单", //商品描述
                user.getOpenid() //微信用户的openid
        );

        if (jsonObject.getString("code") != null && jsonObject.getString("code").equals("ORDERPAID")) {
            throw new OrderBusinessException("该订单已支付");
        }

        OrderPaymentVO vo = jsonObject.toJavaObject(OrderPaymentVO.class);
        vo.setPackageStr(jsonObject.getString("package"));

        return vo;
    }

    /**
     * 支付成功，修改订单状态
     *
     * @param outTradeNo
     */
    public void paySuccess(String outTradeNo) {

        // 根据订单号查询订单
        Orders ordersDB = orderMapper.getByNumber(outTradeNo);

        // 根据订单id更新订单的状态、支付方式、支付状态、结账时间
        Orders orders = Orders.builder()
                .id(ordersDB.getId())
                .status(Orders.TO_BE_CONFIRMED)
                .payStatus(Orders.PAID)
                .checkoutTime(LocalDateTime.now())
                .build();

        orderMapper.update(orders);
    }

    @Override
    public void updateOrderStatusByOrderNumber(String orderNumber) {
        orderMapper.updateOrderStatusByOrderNumber(orderNumber);
    }

    @Override
    public PageResult OrderPage4User(String pageStart, String pageSize, String status) {
        //分页插件
        PageHelper.startPage(Integer.parseInt(pageStart), Integer.parseInt(pageSize));

        Long userId = BaseContext.getCurrentId();
        Page<OrderPageVo> page = orderMapper.getOrderPage4User(userId, status);


        page.forEach(OrderPageVo -> {
            List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(OrderPageVo.getId());
            OrderPageVo.setOrderDetailList(orderDetailList);
        });
        return new PageResult(page.getTotal(), page.getResult());
    }

    @Override
    public OrderPageVo orderDetailByOrderId(Long orderId) {
        OrderPageVo orderPageVO = orderMapper.getByOrderId(orderId);
        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);
        orderPageVO.setOrderDetailList(orderDetailList);

        return orderPageVO;
    }

    @Override
    public void cancleByOrderId(Long orderId) {
        orderMapper.updateOrderStatusByOrderNumber6(orderId);
    }

    @Override
    @Transactional
    public void repetitionByOrderId(Long orderId) {
        OrderPageVo byOrderId = orderMapper.getByOrderId(orderId);
        Orders orders = new Orders();
        BeanUtils.copyProperties(byOrderId, orders);
        orders.setOrderTime(LocalDateTime.now());
        orders.setPayStatus(Orders.UN_PAID);
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        orders.setEstimatedDeliveryTime(LocalDateTime.now().plusHours(1));
        orderMapper.insert(orders);

        List<OrderDetail> orderDetailList = orderDetailMapper.getByOrderId(orderId);
        List<ShoppingCart> shoppingCartList = new ArrayList<>();

        orderDetailList.forEach(orderDetail -> {
            ShoppingCart shoppingCart = new ShoppingCart().builder()
                    .userId(BaseContext.getCurrentId())
                    .createTime(LocalDateTime.now())
                    .name(orderDetail.getName())
                    .dishId(orderDetail.getDishId())
                    .setmealId(orderDetail.getSetmealId())
                    .dishFlavor(orderDetail.getDishFlavor())
                    .number(orderDetail.getNumber())
                    .amount(orderDetail.getAmount())
                    .image(orderDetail.getImage())
                    .build();
            shoppingCartList.add(shoppingCart);
        });
        shoppingCartService.insertBatch(shoppingCartList);
    }

}

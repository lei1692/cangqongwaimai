package com.sky.service.impl;

import com.sky.dto.GoodsSalesDTO;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author leiwenfeng
 * Date: 2023/9/12 6:14
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;
    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate  begin, LocalDate  end) {
        List<LocalDate> list = new ArrayList<>();

        list.add(begin);

        while (!begin.isEqual(end)){
            begin = begin.plusDays(1);
            list.add(begin);
        }

        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate data : list) {
            LocalDateTime beginTime = LocalDateTime.of(data, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(data, LocalTime.MAX);

            Map map = new HashMap();
            map.put("begin",beginTime);
            map.put("end",endTime);
            map.put("status", Orders.COMPLETED);

            Double turnover = orderMapper.getSumByMap(map);
            turnover = turnover == null ? 0.0 : turnover;
            turnoverList.add(turnover);

        }


        return TurnoverReportVO
                .builder()
                .dateList(StringUtils.join(list,","))
                .turnoverList(StringUtils.join(turnoverList,","))
                .build();
    }

    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> list = new ArrayList<>();

        list.add(begin);

        while (!begin.isEqual(end)){
            begin = begin.plusDays(1);
            list.add(begin);
        }
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totleUserList = new ArrayList<>();
        for (LocalDate date : list) {

            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);

            Map map = new HashMap();
            map.put("end",endTime);

            Integer totleUser = orderMapper.countByMap(map);
            map.put("begin",beginTime);

            Integer newUser = orderMapper.countByMap(map);
            newUserList.add(newUser);
            totleUserList.add(totleUser);
        }

        return UserReportVO
                .builder()
                .dateList(StringUtils.join(list,","))
                .newUserList(StringUtils.join(newUserList,","))
                .totalUserList(StringUtils.join(totleUserList,","))
                .build();
    }

    @Override
    public OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end) {
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);

        while (!begin.isEqual(end)){
            begin = begin.plusDays(1);
            dateList.add(begin);
        }
        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate date : dateList) {
            LocalDateTime beginTime = LocalDateTime.of(date, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(date, LocalTime.MAX);
            Integer orderCount = getOrderCount(null, beginTime, endTime);
            orderCountList.add(orderCount);

            Integer validOrderCount = getOrderCount(Orders.COMPLETED, beginTime, endTime);
            validOrderCountList.add(validOrderCount);
        }
        //计算订单总数量和有效订单数量
        Integer totalOrderCount = orderCountList.stream().reduce(Integer::sum).get();
        Integer validOrderCount = validOrderCountList.stream().reduce(Integer::sum).get();

        Double orderCompletionRage = 0.0;
        if (totalOrderCount != 0){
            orderCompletionRage = validOrderCount * 1.0 / totalOrderCount;
        }

        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList,","))//日期
                .orderCountList(StringUtils.join(orderCountList,","))//订单总数量
                .validOrderCountList(StringUtils.join(validOrderCountList,","))//有效订单数量
                .totalOrderCount(totalOrderCount)//订单总数量
                .validOrderCount(validOrderCount)//有效订单数量
                .orderCompletionRate(orderCompletionRage)//订单完成率
                .build();
    }

    @Override
    public SalesTop10ReportVO getTop10Statistics(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);
        List<GoodsSalesDTO> top10 = orderMapper.getTop10(beginTime, endTime);

        List<String> names = top10.stream().map(GoodsSalesDTO::getName).collect(Collectors.toList());
        String nameList = StringUtils.join(names,",");

        List<Integer> numbers = top10.stream().map(GoodsSalesDTO::getNumber).collect(Collectors.toList());
        String numberList = StringUtils.join(numbers,",");



        return SalesTop10ReportVO.builder()
                .nameList(nameList)
                .numberList(numberList)
                .build();
    }

    private Integer getOrderCount(Integer status, LocalDateTime beginTime, LocalDateTime endTime) {
        Map map = new HashMap();
        map.put("status",status);
        map.put("begin",beginTime);
        map.put("end",endTime);
        return orderMapper.countByMap1(map);
    }
}

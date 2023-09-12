package com.sky.service;

import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;

/**
 * @author leiwenfeng
 * Date: 2023/9/12 6:14
 */
public interface ReportService {
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate  end);

    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);


    OrderReportVO getOrdersStatistics(LocalDate begin, LocalDate end);

    SalesTop10ReportVO getTop10Statistics(LocalDate begin, LocalDate end);

    void export(HttpServletResponse response);
}

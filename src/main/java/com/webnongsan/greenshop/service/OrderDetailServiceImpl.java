package com.webnongsan.greenshop.service;

import com.webnongsan.greenshop.model.response.OrderDetailResponse;
import com.webnongsan.greenshop.model.response.StatisticalOrderDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface OrderDetailServiceImpl {
    List<OrderDetailResponse> findOrderDetailByOrderId(Long orderId);
    Page<Object[]> statisticalByYear(PageRequest pageRequest);
    Page<Object[]> statisticalByMonth(PageRequest pageRequest);
    Page<Object[]> statisticalByQuarter(PageRequest pageRequest);
    Page<Object[]> statisticalWhereCategory(PageRequest pageRequest);
    Page<Object[]> statisticalWhereCustomer(PageRequest pageRequest);
    Page<Object[]> statisticalWhereProducts(PageRequest pageRequest);
    Page<Object[]> statisticalProductOfKey(String keyword, PageRequest pageRequest);
    List<StatisticalOrderDetail> mapToStatisticalOrderDetailList(Page<Object[]> objects, String nameField);
}


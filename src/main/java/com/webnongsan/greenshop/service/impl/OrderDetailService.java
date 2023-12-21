package com.webnongsan.greenshop.service.impl;

import com.webnongsan.greenshop.converter.OrderDetailConverter;
import com.webnongsan.greenshop.model.response.OrderDetailResponse;
import com.webnongsan.greenshop.model.response.StatisticalOrderDetail;
import com.webnongsan.greenshop.repository.Entities.OrderDetailEntity;
import com.webnongsan.greenshop.repository.OrderDetailRepository;
import com.webnongsan.greenshop.service.OrderDetailServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailService implements OrderDetailServiceImpl {
    private final OrderDetailRepository orderDetailRepository;
    private final OrderDetailConverter orderDetailConverter;
    @Override
    public List<OrderDetailResponse> findOrderDetailByOrderId(Long orderId) {
        List<OrderDetailEntity> orderDetail = orderDetailRepository.findOrderDetailByOrderId(orderId);
        return orderDetail.stream().map(orderDetailConverter::convertToResponse).collect(Collectors.toList());
    }

    @Override
    public Page<Object[]> statisticalByYear(PageRequest pageRequest) {
        return orderDetailRepository.repoWhereYear(pageRequest);
    }

    @Override
    public Page<Object[]> statisticalByMonth(PageRequest pageRequest) {
        return orderDetailRepository.repoWhereMonth(pageRequest);
    }

    @Override
    public Page<Object[]> statisticalByQuarter(PageRequest pageRequest) {
        return orderDetailRepository.repoWhereQuarter(pageRequest);
    }

    @Override
    public Page<Object[]> statisticalWhereCategory(PageRequest pageRequest) {
        return orderDetailRepository.repoWhereCategory(pageRequest);
    }

    @Override
    public Page<Object[]> statisticalWhereCustomer(PageRequest pageRequest) {
        return orderDetailRepository.repoWhereCustomer(pageRequest);
    }

    public Page<Object[]> statisticalWhereProducts(PageRequest pageRequest) {
        return orderDetailRepository.repoWhereProduct(pageRequest);
    }

    @Override
    public Page<Object[]> statisticalProductOfKey(String keyword, PageRequest pageRequest) {
        return orderDetailRepository.statisticsByProductOfKey(keyword,pageRequest);
    }

    public List<StatisticalOrderDetail> mapToStatisticalOrderDetailList(Page<Object[]> objects, String nameField) {
        List<StatisticalOrderDetail> statisticalOrderDetails = new ArrayList<>();
        for(Object[] object : objects.getContent()){
            StatisticalOrderDetail orderDetailOfProduct = new StatisticalOrderDetail();
            if("day".equals(nameField)){
                orderDetailOfProduct.setDay(Integer.parseInt(object[0].toString()));
            } else {
                orderDetailOfProduct.setName(object[0].toString());
            }
            orderDetailOfProduct.setQuantity(Integer.parseInt(object[1].toString()));
            orderDetailOfProduct.setSumPrice(Double.parseDouble(object[2].toString()));
            orderDetailOfProduct.setAveragePrice(Double.parseDouble(object[3].toString()));
            orderDetailOfProduct.setMinimumPrice(Double.parseDouble(object[4].toString()));
            orderDetailOfProduct.setMaximumPrice(Double.parseDouble(object[5].toString()));
            statisticalOrderDetails.add(orderDetailOfProduct);
        }
        return statisticalOrderDetails;
    }

}

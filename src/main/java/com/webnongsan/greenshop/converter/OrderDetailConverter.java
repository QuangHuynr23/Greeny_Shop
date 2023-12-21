package com.webnongsan.greenshop.converter;

import com.webnongsan.greenshop.model.dto.CategoryDTO;
import com.webnongsan.greenshop.model.response.OrderDetailResponse;
import com.webnongsan.greenshop.repository.Entities.CategoryEntity;
import com.webnongsan.greenshop.repository.Entities.OrderDetailEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OrderDetailConverter {
    private final ModelMapper modelMapper;

    public OrderDetailResponse convertToResponse(OrderDetailEntity orderDetailEntity) {
        return modelMapper.map(orderDetailEntity, OrderDetailResponse.class);
    }
}

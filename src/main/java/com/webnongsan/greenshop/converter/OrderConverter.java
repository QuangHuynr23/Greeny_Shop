package com.webnongsan.greenshop.converter;

import com.webnongsan.greenshop.model.dto.OrderDTO;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.response.OrderResponse;
import com.webnongsan.greenshop.repository.Entities.OrderEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderConverter {
    private final ModelMapper modelMapper;
    public OrderDTO convertToDTO(OrderEntity orderEntity){
        return modelMapper.map(orderEntity,OrderDTO.class);
    }
    public OrderResponse convertToResponse(OrderEntity orderEntity){
        return modelMapper.map(orderEntity,OrderResponse.class);
    }
    public OrderEntity convertToEntity(OrderDTO orderDTO){
        return modelMapper.map(orderDTO,OrderEntity.class);
    }
}

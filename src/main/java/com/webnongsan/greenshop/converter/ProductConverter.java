package com.webnongsan.greenshop.converter;

import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductConverter {
    private final ModelMapper modelMapper;
    public ProductDTO convertToDTO(ProductEntity productEntity){
        return modelMapper.map(productEntity,ProductDTO.class);
    }
    public ProductEntity convertToEntity(ProductDTO productDTO){
        return modelMapper.map(productDTO,ProductEntity.class);
    }
}

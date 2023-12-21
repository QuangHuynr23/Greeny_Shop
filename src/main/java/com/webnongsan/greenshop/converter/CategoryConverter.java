package com.webnongsan.greenshop.converter;

import com.webnongsan.greenshop.model.dto.CategoryDTO;
import com.webnongsan.greenshop.repository.Entities.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CategoryConverter {
    private final ModelMapper modelMapper;

    public CategoryDTO convertToDTO(CategoryEntity categoryEntity){
        return modelMapper.map(categoryEntity,CategoryDTO.class);
    }
    public CategoryEntity convertToEntity(CategoryDTO categoryDTO){
        return modelMapper.map(categoryDTO,CategoryEntity.class);
    }
}

package com.webnongsan.greenshop.converter;

import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import com.webnongsan.greenshop.repository.Entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final ModelMapper modelMapper;
    public UserDTO convertToDTO(UserEntity userEntity){
        return modelMapper.map(userEntity,UserDTO.class);
    }
    public UserEntity convertToEntity(UserDTO userDTO){
        return modelMapper.map(userDTO,UserEntity.class);
    }
}

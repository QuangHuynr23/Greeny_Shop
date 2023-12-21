package com.webnongsan.greenshop.converter;

import com.webnongsan.greenshop.model.dto.FavoriteDTO;
import com.webnongsan.greenshop.repository.Entities.FavoriteEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FavoriteConverter {
    private final ModelMapper modelMapper;
    public FavoriteDTO  convertToDTO(FavoriteEntity favoriteEntity) {
        return modelMapper.map(favoriteEntity,FavoriteDTO.class);
    }
    public FavoriteEntity  convertToEntity(FavoriteDTO favoriteDTO) {
        return modelMapper.map(favoriteDTO,FavoriteEntity.class);
    }
}

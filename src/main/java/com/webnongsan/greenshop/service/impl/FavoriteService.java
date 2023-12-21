package com.webnongsan.greenshop.service.impl;

import com.webnongsan.greenshop.converter.FavoriteConverter;
import com.webnongsan.greenshop.converter.UserConverter;
import com.webnongsan.greenshop.model.dto.FavoriteDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.FavoriteEntity;
import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import com.webnongsan.greenshop.repository.Entities.UserEntity;
import com.webnongsan.greenshop.repository.FavoriteRepository;
import com.webnongsan.greenshop.repository.ProductRepository;
import com.webnongsan.greenshop.service.FavoriteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FavoriteService implements FavoriteServiceImpl {
    private final FavoriteRepository favoriteRepository;
    private final FavoriteConverter favoriteConverter;
    private final ProductRepository productRepository;
    private final UserConverter userConverter;

    @Override
    public FavoriteDTO selectSaves(Long productId, Long userId) {
        FavoriteEntity favoriteEntity = favoriteRepository.selectSaves(productId,userId);
        if(favoriteEntity != null){
            return favoriteConverter.convertToDTO(favoriteEntity);
        }
        return null;
    }

    @Override
    public Integer selectCountFavoriteSave(Long userId) {
        return favoriteRepository.selectCountFavoriteSave(userId);
    }

    @Override
    public List<FavoriteDTO> selectAllSaves(Long userId) {
        List<FavoriteEntity> favoriteEntityList = favoriteRepository.selectAllSaves(userId);
        return favoriteEntityList.stream().map(favoriteConverter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void removeFavorite(Long id, UserDTO userDTO, ProductEntity product) {
        FavoriteDTO favoriteDTO = selectSaves(id,userDTO.getId());
        product = productRepository.findById(id).orElse(null);
        product.setFavorite(false);
        favoriteRepository.delete(favoriteConverter.convertToEntity(favoriteDTO));
    }

    @Override
    public void saveFavorite(Long id,FavoriteEntity favoriteEntity ,UserDTO userDTO) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        UserEntity user = userConverter.convertToEntity(userDTO);
        if(product != null){
            product.setFavorite(true);
            favoriteEntity.setProduct(product);
        }
        favoriteEntity.setUser(user);
        favoriteRepository.save(favoriteEntity);
    }
}

package com.webnongsan.greenshop.service;

import com.webnongsan.greenshop.model.dto.FavoriteDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.FavoriteEntity;
import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface FavoriteServiceImpl {
    FavoriteDTO selectSaves(Long productId, Long userId);
    Integer selectCountFavoriteSave(Long userId);
    List<FavoriteDTO> selectAllSaves(Long userId);
    void removeFavorite(Long id, UserDTO userDTO, ProductEntity product);
    void saveFavorite(Long id, FavoriteEntity favoriteEntity, UserDTO userDTO);
}

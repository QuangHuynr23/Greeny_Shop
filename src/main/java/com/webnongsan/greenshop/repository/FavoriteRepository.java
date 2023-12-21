package com.webnongsan.greenshop.repository;

import com.webnongsan.greenshop.repository.Entities.FavoriteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<FavoriteEntity, Long> {

    @Query("select f from FavoriteEntity f where f.product.id =:productId and f.user.id = :userId")
    FavoriteEntity selectSaves(Long productId, Long userId );

    @Query("SELECT f from FavoriteEntity f where f.user.id =:userId")
    List<FavoriteEntity> selectAllSaves(Long userId);

    @Query("SELECT count(f.id) from FavoriteEntity f where f.user.id =:userId")
    Integer selectCountFavoriteSave(Long userId);
}

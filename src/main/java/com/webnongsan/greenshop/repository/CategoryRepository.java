package com.webnongsan.greenshop.repository;

import com.webnongsan.greenshop.repository.Entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {

    Page<CategoryEntity> findAll(Pageable pageable);
    List<CategoryEntity> findAll();

    @Query("select c from CategoryEntity c where :keyword is null or :keyword = '' or c.categoryName like %:keyword%" )
    Page<CategoryEntity> findByName(@Param("keyword") String keyword, Pageable pageable);
}

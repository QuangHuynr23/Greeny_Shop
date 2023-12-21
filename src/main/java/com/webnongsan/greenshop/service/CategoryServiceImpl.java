package com.webnongsan.greenshop.service;

import com.webnongsan.greenshop.model.dto.CategoryDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryServiceImpl {
    List<CategoryDTO> findAll();
    Page<CategoryDTO> findCategoryOfPageByName(String keyword,PageRequest pageRequest);
    Page<CategoryDTO> findAllCategoryOfPage(PageRequest pageRequest);
    CategoryDTO getByID(Long id);
    CategoryDTO insert (CategoryDTO categoryDTO);
    CategoryDTO delete (CategoryDTO categoryDTO);
}

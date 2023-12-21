package com.webnongsan.greenshop.service.impl;

import com.webnongsan.greenshop.converter.CategoryConverter;
import com.webnongsan.greenshop.model.dto.CategoryDTO;
import com.webnongsan.greenshop.repository.CategoryRepository;
import com.webnongsan.greenshop.repository.Entities.CategoryEntity;
import com.webnongsan.greenshop.service.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryServiceImpl {
    private final CategoryRepository categoryRepository;
    private final CategoryConverter categoryConverter;

    @Override
    public List<CategoryDTO> findAll() {
        List<CategoryEntity> categoryEntities = categoryRepository.findAll();
        return categoryEntities.stream().map(categoryConverter ::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public Page<CategoryDTO> findCategoryOfPageByName(String keyword, PageRequest pageRequest) {
        Page<CategoryEntity> categoryEntityPage = categoryRepository.findByName(keyword,pageRequest);
        return categoryEntityPage.map(categoryConverter::convertToDTO);
    }

    @Override
    public Page<CategoryDTO> findAllCategoryOfPage(PageRequest pageRequest) {
        Page<CategoryEntity> categoryEntityPage = categoryRepository.findAll(pageRequest);
        return categoryEntityPage.map(categoryConverter::convertToDTO);
    }

    @Override
    public CategoryDTO getByID(Long id) {
        Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
        return categoryEntity.map(categoryConverter::convertToDTO).orElse(null);
    }

    @Override
    @Transactional
    public CategoryDTO insert(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryConverter.convertToEntity(categoryDTO);
        categoryEntity.setStatus(true);
        categoryEntity = categoryRepository.save(categoryEntity);
        categoryDTO = categoryConverter.convertToDTO(categoryEntity);
        return categoryDTO;
    }

    @Override
    @Transactional
    public CategoryDTO delete(CategoryDTO categoryDTO) {
        CategoryEntity categoryEntity = categoryConverter.convertToEntity(categoryDTO);
        categoryEntity.setStatus(false);
        categoryEntity = categoryRepository.save(categoryEntity);
        categoryDTO = categoryConverter.convertToDTO(categoryEntity);
        return categoryDTO;
    }

}

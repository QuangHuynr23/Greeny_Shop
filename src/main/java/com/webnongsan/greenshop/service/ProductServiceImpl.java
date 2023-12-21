package com.webnongsan.greenshop.service;

import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface ProductServiceImpl {
    Page<ProductDTO> findAllProductOfPage(PageRequest pageRequest);
    List<ProductDTO> findListProductBestSaler10(UserDTO userDTO);
    List<ProductDTO> findListProductNew10();
    List<CategoryResponse> findListCategoryByProduct();
    Page<ProductDTO> findProductOfName(String name, PageRequest pageRequest);
    List<ProductDTO> findProductByCategoryId(Long categoryId);
    void updateQuantityProduct(Long id);
    ProductDTO findById(Long id);
    ProductDTO insert(ProductDTO productDTO, MultipartFile file);
    void deleteProduct(Long id);
}

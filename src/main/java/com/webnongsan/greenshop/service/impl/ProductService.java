package com.webnongsan.greenshop.service.impl;

import com.webnongsan.greenshop.converter.ProductConverter;
import com.webnongsan.greenshop.customerexception.DataNotFoundException;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.CategoryResponse;
import com.webnongsan.greenshop.repository.CategoryRepository;
import com.webnongsan.greenshop.repository.Entities.CategoryEntity;
import com.webnongsan.greenshop.repository.Entities.FavoriteEntity;
import com.webnongsan.greenshop.repository.Entities.OrderDetailEntity;
import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import com.webnongsan.greenshop.repository.FavoriteRepository;
import com.webnongsan.greenshop.repository.OrderDetailRepository;
import com.webnongsan.greenshop.repository.ProductRepository;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements ProductServiceImpl {
    private final ProductRepository productRepository;
    private final ProductConverter productConverter;
    private final FavoriteRepository favoriteRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final CategoryRepository categoryRepository;

    @Value("${upload.path}")
    private String pathUploadImage;

    @Override
    public Page<ProductDTO> findAllProductOfPage(PageRequest pageRequest) {
        Page<ProductEntity> productEntities = productRepository.findAll(pageRequest);
        return productEntities.map(productConverter :: convertToDTO);
    }


    @Override
    public List<ProductDTO> findListProductBestSaler10(UserDTO userDTO) {
        List<Object[]> objects = productRepository.findListProductBestSaler10();
        List<ProductDTO> productDTOList = new ArrayList<>();
        if(objects != null){
            List<Long> listProductId = new ArrayList<>();
            for (Object[] obj : objects){
                BigInteger productIdBigInteger = (BigInteger) obj[0];
                Long productId = productIdBigInteger.longValue();
                listProductId.add(productId);
            }
            List<ProductEntity> productEntities = productRepository.findProductByIds(listProductId);
            for(ProductEntity product: productEntities){
                ProductDTO productDTO = productConverter.convertToDTO(product);
                FavoriteEntity save = favoriteRepository.selectSaves(product.getId(), userDTO.getId());
                productDTO.setFavorite(save != null);
                productDTOList.add(productDTO);
            }
        }
        return productDTOList;
    }

    @Override
    public List<ProductDTO> findListProductNew10() {
        List<ProductEntity> productEntities = productRepository.findListProductNew();
        return productEntities.stream().map(productConverter :: convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponse> findListCategoryByProduct() {
        List<CategoryResponse> listCategory = new ArrayList<>();
        List<Object[]> objects = productRepository.findListCategoryByProduct();
        if(objects != null){
            for(Object[] object : objects){
                 CategoryResponse categoryResponse = new CategoryResponse();
                 categoryResponse.setId(Long.valueOf(String.valueOf(object[0])));
                 categoryResponse.setCategoryName(String.valueOf(object[1]));
                 categoryResponse.setCountProduct(Integer.valueOf(String.valueOf(object[2])));
                 listCategory.add(categoryResponse);
            }
        }
        return listCategory;
    }

    @Override
    public Page<ProductDTO> findProductOfName(String name, PageRequest pageRequest) {
        Page<ProductEntity> productEntityPage = productRepository.findByName(name,pageRequest);
        return productEntityPage.map(productConverter::convertToDTO);
    }


    @Override
    public List<ProductDTO> findProductByCategoryId(Long categoryId) {
        List<ProductEntity> productEntities = productRepository.findByCategoryId(categoryId);
        return productEntities.stream().map(productConverter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void updateQuantityProduct(Long id) {
        ProductEntity product = null;
        List<OrderDetailEntity> orderDetailEntities = orderDetailRepository.findOrderDetailByOrderId(id);
        for (OrderDetailEntity item : orderDetailEntities) {
            product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);
        }
    }

    @Override
    public ProductDTO findById(Long id) {
        ProductEntity product = productRepository.findById(id)
                                .orElseThrow(()-> new DataNotFoundException("Cannot find product with id "));
        return productConverter.convertToDTO(product);
    }

    @Override
    @Transactional
    public ProductDTO insert(ProductDTO productDTO, MultipartFile file) {
        try {
            File convFile = new File(pathUploadImage + "/" + file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        }catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        ProductEntity product = productConverter.convertToEntity(productDTO);
        CategoryEntity categoryEntity = categoryRepository.getById(productDTO.getCategory().getId());
        product.setProductImage(file.getOriginalFilename());
        product.setCategory(categoryEntity);
        product.setFavorite(false);
        product.setStatus(true);
        product = productRepository.save(product);
        productDTO = productConverter.convertToDTO(product);
        return productDTO;
    }

    @Override
    public void deleteProduct(Long id) {
        ProductDTO productDTO = findById(id);
        ProductEntity product = productConverter.convertToEntity(productDTO);
        productRepository.delete(product);
    }


}

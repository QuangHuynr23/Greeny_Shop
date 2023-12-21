package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.common.CommonDataService;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import com.webnongsan.greenshop.repository.ProductRepository;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductDetailController extends CommonController {
    private final CommonDataService commonDataService;
    private final ProductServiceImpl productService;

    @GetMapping(value = "/productDetail")
    public String getProductDetail(@RequestParam("productId") Long id,
                                   Model model, UserDTO userDTO) {
        ProductDTO productDTO = productService.findById(id);
        model.addAttribute("product", productDTO);
        listProductByCategory10(model,productDTO.getCategory().getId());
        commonDataService.commonData(model, userDTO);
        return "web/productDetail";
    }

    public void listProductByCategory10(Model model, Long categoryId){
        List<ProductDTO> products = productService.findProductByCategoryId(categoryId);
        model.addAttribute("productByCategory",products);
    }

}

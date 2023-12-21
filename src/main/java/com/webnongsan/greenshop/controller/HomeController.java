package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.common.CommonDataService;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.CartItemEntity;
import com.webnongsan.greenshop.service.CategoryServiceImpl;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController extends CommonController {
    private final ProductServiceImpl productService;
    private final CommonDataService commonDataService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Long id){
        ProductDTO productDTO = productService.findById(id);
        return ResponseEntity.ok(productDTO);
    }


    @GetMapping("/")
    public String home(Model model, UserDTO userDTO){
         commonDataService.commonData(model,userDTO);
         bestSaleProduct20(model, userDTO);
         return "web/home";
    }

    public void bestSaleProduct20(Model model, UserDTO userDTO) {
        List<ProductDTO> listProductNew = productService.findListProductBestSaler10(userDTO);
        if (listProductNew != null) {
            model.addAttribute("bestSaleProduct20", listProductNew);
        }
    }

    @ModelAttribute("listProductNew10")
    public List<ProductDTO> listProductNew10(Model model) {
        List<ProductDTO> productList = productService.findListProductNew10();
        model.addAttribute("productList", productList);
        return productList;
    }

}

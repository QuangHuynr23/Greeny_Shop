package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.common.CommonDataService;
import com.webnongsan.greenshop.model.dto.FavoriteDTO;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.CartItemEntity;
import com.webnongsan.greenshop.service.FavoriteServiceImpl;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import com.webnongsan.greenshop.service.ShoppingCartServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ShopController extends CommonController{
    private final ProductServiceImpl productService;
    private final CommonDataService commonDataService;
    private final FavoriteServiceImpl favoriteService;
    private final ShoppingCartServiceImpl shoppingCartService;
    // Hiển thị danh sách sản phẩm có phân trang cho người dùng
    @GetMapping(value = "/products")
    public String shop(Model model, UserDTO userDTO , @RequestParam(defaultValue ="0" )    int page
                                                    , @RequestParam(defaultValue ="12" )   int limit){
        PageRequest pageRequest = PageRequest.of(page,limit,
                Sort.by("id").ascending()
        );
        Page<ProductDTO> productDTOPage = productService.findAllProductOfPage(pageRequest);
        //       Lấy tổng số trang
        int totalPages = productDTOPage.getTotalPages();
        List<ProductDTO> productDTOList = productDTOPage.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages-1).boxed().collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
        commonDataService.commonData(model, userDTO);
        model.addAttribute("products",productDTOPage);
        return "web/shop";
    }


    // Tìm kiếm Product
    @GetMapping("/searchProduct")
    public String searchProduct(Model model, @RequestParam("keyword") String keyword
                                            ,@RequestParam(defaultValue ="0" )    int page
                                            ,@RequestParam(defaultValue ="12" )   int limit
                                            , UserDTO userDTO){
        PageRequest pageRequest = PageRequest.of(page,limit,
                Sort.by("id").ascending()
        );
        Page<ProductDTO> productDTOPage = productService.findProductOfName(keyword, pageRequest);
        for (ProductDTO productDto : productDTOPage) {
            if (userDTO.getId() != null) {
                FavoriteDTO favoriteDTO = favoriteService.selectSaves(productDto.getId(), userDTO.getId());
                if (favoriteDTO != null) {
                    productDto.setFavorite(true);
                }else {
                    productDto.setFavorite(false);
                }
            }else {
                productDto.setFavorite(false);
            }
        }
        //       Lấy tổng số trang
        int totalPages = productDTOPage.getTotalPages();
        List<ProductDTO> productDTOList = productDTOPage.getContent();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", productDTOPage.getNumber()+1);
        List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
        commonDataService.commonData(model, userDTO);
        model.addAttribute("products",productDTOPage);
        return "web/shop";
    }

    // Liệt kê danh sách theo thể loại
    @GetMapping(value = "/productByCategory")
    public String listProductById(Model model, @RequestParam("id") Long categoryId,UserDTO userDTO ){
        List<ProductDTO> productDTOList = productService.findProductByCategoryId(categoryId);
        for(ProductDTO productDTO :productDTOList){
            if(userDTO != null){
                FavoriteDTO favoriteDTO = favoriteService.selectSaves(productDTO.getId(), userDTO.getId());
                if(favoriteDTO != null){
                    productDTO.setFavorite(true);
                }
                else {
                    productDTO.setFavorite(false);
                }
            } else {
                productDTO.setFavorite(false);
            }
        }
        model.addAttribute("products", productDTOList);
        commonDataService.commonData(model, userDTO);
        return "web/shop";
    }

}

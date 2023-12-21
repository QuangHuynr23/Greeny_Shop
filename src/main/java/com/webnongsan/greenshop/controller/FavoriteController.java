package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.common.CommonDataService;
import com.webnongsan.greenshop.model.dto.FavoriteDTO;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.FavoriteEntity;
import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import com.webnongsan.greenshop.service.FavoriteServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FavoriteController extends CommonController{
    private final FavoriteServiceImpl favoriteService;
    private final CommonDataService commonDataService;

    @GetMapping("/favorite")
    public String favorite(Model model, UserDTO userDTO) {
        List<FavoriteDTO> favorites = favoriteService.selectAllSaves(userDTO.getId());
        commonDataService.commonData(model,userDTO);
        model.addAttribute("favorites", favorites);
        return "web/favorite";
    }

    @GetMapping("doUnFavorite")
    public String doUnFavorite(Model model, ProductEntity product, @RequestParam("productId") Long id, UserDTO userDTO){
        favoriteService.removeFavorite(id,userDTO,product);
        commonDataService.commonData(model,userDTO);
        return "redirect:/products";
    }

    @GetMapping("doFavorite")
    public String doFavorite(Model model,@RequestParam("productId") Long id, FavoriteEntity favoriteEntity, UserDTO userDTO){
        favoriteService.saveFavorite(id,favoriteEntity,userDTO);
        commonDataService.commonData(model,userDTO);
        return "redirect:/products";
    }
}

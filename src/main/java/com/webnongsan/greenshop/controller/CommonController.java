package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.model.dto.CategoryDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.CategoryEntity;
import com.webnongsan.greenshop.service.CategoryServiceImpl;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.List;

@Controller
public class CommonController {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    CategoryServiceImpl categoryService;


    @ModelAttribute(value = "user")
    public UserDTO userDTO(Model model, Principal principal, UserDTO userDTO){
        if(principal != null){
                model.addAttribute("userDTO",new UserDTO());
                userDTO=  userService.findByEmail(principal.getName());
                model.addAttribute("userDTO",userDTO);

        }
        return userDTO;
    }

    @ModelAttribute("categoryList")
    public List<CategoryDTO> showCategory(Model model){
        List<CategoryDTO> categoryDTOList = categoryService.findAll();
        model.addAttribute("categoryList", categoryDTOList);
        return categoryDTOList;
    }


}

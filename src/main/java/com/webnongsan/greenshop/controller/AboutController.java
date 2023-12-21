package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.common.CommonDataService;
import com.webnongsan.greenshop.model.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AboutController extends CommonController {
    private final CommonDataService commonDataService;

    @GetMapping(value = "aboutUs")
    public String about(Model model, UserDTO userDTO) {
        commonDataService.commonData(model, userDTO);
        return "web/about";
    }
}

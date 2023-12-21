package com.webnongsan.greenshop.controller.admin;

import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class IndexAdminController{
	
	private final UserServiceImpl userService;
	
	@ModelAttribute(value = "user")
	public UserDTO user(Model model, Principal principal, UserDTO userDTO) {
		if (principal != null) {
			model.addAttribute("userDto", new UserDTO());
			userDTO = userService.findByEmail(principal.getName());
			model.addAttribute("userDto", userDTO);
		}
		return userDTO;
	}

	@GetMapping(value = "/home")
	public String index() {
		return "admin/index";
	}
}

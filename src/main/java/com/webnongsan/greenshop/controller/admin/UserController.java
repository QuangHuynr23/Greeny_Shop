package com.webnongsan.greenshop.controller.admin;

import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class UserController{
	
	private final UserServiceImpl userService;


	@GetMapping(value = "/admin/users")
	public String customer(Model model, Principal principal) {
		UserDTO userDTO = userService.findByEmail(principal.getName());
		model.addAttribute("user", userDTO);
		List<UserDTO> users = userService.findAll();
		model.addAttribute("users", users);
		return "/admin/users";
	}
}

package com.webnongsan.greenshop.controller.admin;

import com.webnongsan.greenshop.model.dto.CategoryDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.PaginateResponse;
import com.webnongsan.greenshop.repository.Entities.CategoryEntity;
import com.webnongsan.greenshop.service.CategoryServiceImpl;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;



@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class CategoryController {
	private final CategoryServiceImpl categoryService;
	private final UserServiceImpl userService;
	
	private String message;

	@ModelAttribute(value = "user")
	public UserDTO userDTO(Model model, Principal principal, UserDTO userDTO){
		if(principal != null){
			model.addAttribute("userDTO",new UserDTO());
			userDTO=  userService.findByEmail(principal.getName());
			model.addAttribute("userDTO",userDTO);
		}
		return userDTO;
	}


	@GetMapping(value = "/categories")
	public String categories(Model model, Principal principal, @RequestParam(defaultValue ="1" )    int page
			              								     , @RequestParam(defaultValue ="5" )   int limit) {
		model.addAttribute("category", new CategoryEntity());
		PageRequest pageRequest = PageRequest.of(page-1,limit,
				Sort.by("id").ascending()
		);
		Page<CategoryDTO> categoryDTOPage = categoryService.findAllCategoryOfPage(pageRequest);
		model.addAttribute("categories", categoryDTOPage);
		int totalPages = categoryDTOPage.getTotalPages();
		PaginateResponse paginateResponse = new PaginateResponse();
		paginateResponse.setTotalPage(totalPages);
		paginateResponse.setPage(page);
		model.addAttribute("paginate", paginateResponse);
		if(message != null && !message.isEmpty()) {
			model.addAttribute("message", message);
			message = null;
		}
		return "admin/categories";
	}

	// add category
	@PostMapping(value = "/addCategory")
	public String addCategory(@Validated @ModelAttribute("category") CategoryDTO categoryDTO, ModelMap model,
							  BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			model.addAttribute("error", "failure");

			return "admin/categories";
		}
		categoryDTO = categoryService.insert(categoryDTO);
		if (categoryDTO != null) {
			message = "successful!";
		}
		return "redirect:/admin/categories";
	}

	// get Edit category
	@GetMapping(value = "/editCategory/{id}")
	public String editCategory(@PathVariable("id") Long id, ModelMap model) {
		CategoryDTO categoryDTO = categoryService.getByID(id);
		model.addAttribute("category", categoryDTO);
		return "admin/editCategory";
	}

	// delete category
	@GetMapping("/delete/{id}")
	public String delCategory(@PathVariable("id") Long id, Model model) {
		CategoryDTO categoryDto = categoryService.getByID(id);
		if (categoryDto != null) {
			categoryDto = categoryService.delete(categoryDto);
		}
		if (null != categoryDto) {
			message = "Delete successful!";
		}
		model.addAttribute("message", message);
		return "redirect:/admin/categories";
	}
}

package com.webnongsan.greenshop.controller.admin;

import com.webnongsan.greenshop.model.dto.CategoryDTO;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.PaginateResponse;
import com.webnongsan.greenshop.service.CategoryServiceImpl;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class ProductController{
	
	@Value("${upload.path}")
	private String pathUploadImage;
	private final ProductServiceImpl productService;
	private final CategoryServiceImpl categoryService;
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


	// show list product - table list
	@GetMapping(value = "/products")
	public String products(Model model, Principal principal , @RequestParam(defaultValue ="1" )    int page
			 												, @RequestParam(defaultValue ="5" )   int limit) {
		model.addAttribute("product", new ProductDTO());
		PageRequest pageRequest = PageRequest.of(page-1,limit,
				Sort.by("id").ascending()
		);
		Page<ProductDTO> productDTOPage = productService.findAllProductOfPage(pageRequest);
		int totalPages = productDTOPage.getTotalPages();
		PaginateResponse paginateResponse = new PaginateResponse();
		paginateResponse.setTotalPage(totalPages);
		paginateResponse.setPage(page);
		model.addAttribute("products", productDTOPage);
		model.addAttribute("paginate", paginateResponse);
		return "admin/products";
	}

	// add product
	@PostMapping(value = "/addProduct")
	public String addProduct(@ModelAttribute("product") ProductDTO productDto , ModelMap model,
							 @RequestParam("file") MultipartFile file, HttpServletRequest httpServletRequest) {
		ProductDTO productDTO = productService.insert(productDto, file);
		if (null != productDTO) {
			model.addAttribute("message", "Update success");
			model.addAttribute("product", productDTO);
		} else {
			model.addAttribute("message", "Update failure");
			model.addAttribute("product", productDTO);
		}
		return "redirect:/admin/products";
	}

	// show select option ở add product
	@ModelAttribute("categoryList")
	public List<CategoryDTO> showCategory(Model model) {
		List<CategoryDTO> categoryList = categoryService.findAll();
		model.addAttribute("categoryList", categoryList);
		return categoryList;
	}
	
	// get Edit brand
	@GetMapping(value = "/editProduct/{id}")
	public String editCategory(@PathVariable("id") Long id, Model model) {
		ProductDTO productDTO = productService.findById(id);
		model.addAttribute("product", productDTO);
		return "admin/editProduct";
	}

	// delete category
	@GetMapping("/deleteProduct/{id}")
	public String delProduct(@PathVariable("id") Long id, Model model) {
		productService.deleteProduct(id);
		model.addAttribute("message", "Delete successful!");
		return "redirect:/admin/products";
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		sdf.setLenient(true);
		binder.registerCustomEditor(Date.class, new CustomDateEditor(sdf, true));
	}
}

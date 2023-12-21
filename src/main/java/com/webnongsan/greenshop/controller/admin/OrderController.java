package com.webnongsan.greenshop.controller.admin;

import com.webnongsan.greenshop.customerexception.DataNotFoundException;
import com.webnongsan.greenshop.model.dto.OrderDTO;
import com.webnongsan.greenshop.service.impl.OrderExcelExporter;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.OrderDetailResponse;
import com.webnongsan.greenshop.service.OrderDetailServiceImpl;
import com.webnongsan.greenshop.service.OrderServiceImpl;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class OrderController {
    private final OrderDetailServiceImpl orderDetailService;
	private final OrderServiceImpl orderService;
	private final UserServiceImpl userService;
	private final ProductServiceImpl productService;

	@ModelAttribute(value = "user")
	public UserDTO user(Model model, Principal principal, UserDTO userDTO) {
		if (principal != null) {
			model.addAttribute("userDto", new UserDTO());
			userDTO = userService.findByEmail(principal.getName());
			model.addAttribute("userDto", userDTO);
		}
		return userDTO;
	}

	// list order
	@GetMapping(value = "/orders")
	public String orders(Model model, Principal principal) {
		List<OrderDTO> orderDetails = orderService.findAll();
		model.addAttribute("orderDetails", orderDetails);
		return "admin/orders";
	}

	@GetMapping("/order/detail/{order_id}")
	public String detail(Model model, @PathVariable("order_id") Long id) {

		List<OrderDetailResponse> orderDetailResponses = orderDetailService.findOrderDetailByOrderId(id);

		model.addAttribute("amount", orderService.findById(id).getAmount());
		model.addAttribute("orderDetail", orderDetailResponses);
		model.addAttribute("orderId", id);
		// set active front-end
		model.addAttribute("menuO", "menu");
		return "admin/editOrder";
	}

	@PutMapping("/order/cancel/{order_id}")
	public String cancel(Model model, @PathVariable("order_id") Long id) {
		try {
			orderService.updateOrderStatus(id,3);
			return "forward:/admin/orders";
		} catch (DataNotFoundException e) {
			return "forward:/admin/orders";
		}
	}

	@PutMapping("/order/confirm/{order_id}")
	public String confirm(Model model, @PathVariable("order_id") Long id) {
		try {
			orderService.updateOrderStatus(id,1);
			return "forward:/admin/orders";
		} catch (DataNotFoundException e) {
			return "forward:/admin/orders";
		}
	}

	@RequestMapping("/order/delivered/{order_id}")
	public String delivered(Model model, @PathVariable("order_id") Long id) {
		try {
 			orderService.updateOrderStatus(id,2);
// 			update product quantity
			productService.updateQuantityProduct(id);
			return "forward:/admin/orders";
		}catch (DataNotFoundException e) {
			return "forward:/admin/orders";
		}
	}

	// to excel
	@GetMapping(value = "/export")
	public void exportToExcel(HttpServletResponse response) throws IOException {
		response.setContentType("application/octet-stream");
		String headerKey = "Content-Disposition";
		String headerValue = "attachement; filename=orders.xlsx";
		response.setHeader(headerKey, headerValue);
		List<OrderDTO> lisOrders = orderService.findAll();
		OrderExcelExporter excelExporter = new OrderExcelExporter(lisOrders);
		excelExporter.export(response);
	}

}

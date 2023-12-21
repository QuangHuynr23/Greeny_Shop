package com.webnongsan.greenshop.controller.admin;

import com.webnongsan.greenshop.Enum.StatisticalType;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.PaginateResponse;
import com.webnongsan.greenshop.model.response.StatisticalOrderDetail;
import com.webnongsan.greenshop.service.OrderDetailServiceImpl;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
public class ReportController {
	private final OrderDetailServiceImpl orderDetailService;
	private final UserServiceImpl userService;

	// Statistics by product sold
	@GetMapping(value = "/admin/reports")
	public String report(Model model, Principal principal, @RequestParam(defaultValue ="1" )    int page
			 											 , @RequestParam(defaultValue ="5" )   int limit){
		UserDTO userDTO = userService.findByEmail(principal.getName());
		model.addAttribute("user",userDTO);
		PageRequest pageRequest = PageRequest.of(page-1,limit,
				Sort.by("id").ascending()
		);
		Page<Object[]> objects = orderDetailService.statisticalWhereProducts(pageRequest);
		int totalPages = objects.getTotalPages();
		List<StatisticalOrderDetail> listReportCommon = orderDetailService.mapToStatisticalOrderDetailList(objects, StatisticalType.Other.getValue());
		model.addAttribute("listReportCommon", listReportCommon);
		PaginateResponse paginateResponse = new PaginateResponse();
		paginateResponse.setTotalPage(totalPages);
		paginateResponse.setPage(page);
		model.addAttribute("paginate", paginateResponse);
		return "admin/statisticalOfProduct";
	}

	// Statistics by category sold
	@RequestMapping(value = "/admin/reportCategory")
	public String reportcategory(Model model, Principal principal,@RequestParam(defaultValue ="1" )    int page
																 , @RequestParam(defaultValue ="5" )   int limit){
		UserDTO userDTO = userService.findByEmail(principal.getName());
		model.addAttribute("user", userDTO);
		PageRequest pageRequest = PageRequest.of(page-1,limit,
				Sort.by("id").ascending()
		);
		Page<Object[]> objects = orderDetailService.statisticalWhereCategory(pageRequest);
		int totalPages = objects.getTotalPages();
		List<StatisticalOrderDetail> listReportCommon = orderDetailService.mapToStatisticalOrderDetailList(objects, StatisticalType.Other.getValue());
		model.addAttribute("listReportCommon", listReportCommon);
		PaginateResponse paginateResponse = new PaginateResponse();
		paginateResponse.setTotalPage(totalPages);
		paginateResponse.setPage(page);
		model.addAttribute("paginate", paginateResponse);
		return "admin/statisticalOfProduct";
	}

	// Statistics of products sold by year
	@RequestMapping(value = "/admin/reportYear")
	public String reportyear(Model model, Principal principal, @RequestParam(defaultValue ="1" )    int page
			                                                 , @RequestParam(defaultValue ="5" )   int limit){
		UserDTO userDTO = userService.findByEmail(principal.getName());
		model.addAttribute("user", userDTO);
		PageRequest pageRequest = PageRequest.of(page-1,limit,
				Sort.by("id").ascending()
		);
		Page<Object[]> objects = orderDetailService.statisticalByYear(pageRequest);
		int totalPages = objects.getTotalPages();
		List<StatisticalOrderDetail> listReportCommon = orderDetailService.mapToStatisticalOrderDetailList(objects, StatisticalType.Day.getValue());
		model.addAttribute("listReportCommon", listReportCommon);
		PaginateResponse paginateResponse = new PaginateResponse();
		paginateResponse.setTotalPage(totalPages);
		paginateResponse.setPage(page);
		model.addAttribute("paginate", paginateResponse);
		return "admin/statisticalOfDay";
	}

	// Statistics of products sold by month
	@RequestMapping(value = "/admin/reportMonth")
	public String reportmonth(Model model, Principal principal, @RequestParam(defaultValue ="1" )    int page
			 												  , @RequestParam(defaultValue ="5" )   int limit){
		UserDTO userDTO = userService.findByEmail(principal.getName());
		model.addAttribute("user", userDTO);
		PageRequest pageRequest = PageRequest.of(page-1,limit,
				Sort.by("id").ascending()
		);
		Page<Object[]> objects = orderDetailService.statisticalByMonth(pageRequest);
		int totalPages = objects.getTotalPages();
		List<StatisticalOrderDetail> listReportCommon = orderDetailService.mapToStatisticalOrderDetailList(objects, StatisticalType.Day.getValue());
		model.addAttribute("listReportCommon", listReportCommon);
		PaginateResponse paginateResponse = new PaginateResponse();
		paginateResponse.setTotalPage(totalPages);
		paginateResponse.setPage(page);
		model.addAttribute("paginate", paginateResponse);
		return "admin/statisticalOfDay";
	}

	// Statistics of products sold by quarter
	@RequestMapping(value = "/admin/reportQuarter")
	public String reportquarter(Model model, Principal principal, @RequestParam(defaultValue ="1" )    int page
			 													, @RequestParam(defaultValue ="5" )   int limit){
		UserDTO userDTO = userService.findByEmail(principal.getName());
		model.addAttribute("user", userDTO);
		PageRequest pageRequest = PageRequest.of(page-1,limit,
				Sort.by("id").ascending()
		);
		Page<Object[]> objects = orderDetailService.statisticalByQuarter(pageRequest);
		int totalPages = objects.getTotalPages();
		List<StatisticalOrderDetail> listReportCommon = orderDetailService.mapToStatisticalOrderDetailList(objects, StatisticalType.Day.getValue());
		model.addAttribute("listReportCommon", listReportCommon);
		PaginateResponse paginateResponse = new PaginateResponse();
		paginateResponse.setTotalPage(totalPages);
		paginateResponse.setPage(page);
		model.addAttribute("paginate", paginateResponse);
		return "admin/statisticalOfDay";
	}

	// Statistics by user
	@RequestMapping(value = "/admin/reportOrderCustomer")
	public String reportordercustomer(Model model, Principal principal, @RequestParam(defaultValue ="1" )    int page
			 													      , @RequestParam(defaultValue ="5" )   int limit){
		UserDTO userDTO = userService.findByEmail(principal.getName());
		model.addAttribute("user", userDTO);
		PageRequest pageRequest = PageRequest.of(page-1,limit,
				Sort.by("id").ascending()
		);
		Page<Object[]> objects = orderDetailService.statisticalWhereCustomer(pageRequest);
		int totalPages = objects.getTotalPages();
		List<StatisticalOrderDetail> listReportCommon = orderDetailService.mapToStatisticalOrderDetailList(objects, StatisticalType.Other.getValue());
		model.addAttribute("listReportCommon", listReportCommon);
		PaginateResponse paginateResponse = new PaginateResponse();
		paginateResponse.setTotalPage(totalPages);
		paginateResponse.setPage(page);
		model.addAttribute("paginate", paginateResponse);
		return "admin/statisticalOfProduct";
	}
}

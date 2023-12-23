package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.common.CommonDataService;
import com.webnongsan.greenshop.converter.OrderConverter;
import com.webnongsan.greenshop.customerexception.DataNotFoundException;
import com.webnongsan.greenshop.model.dto.OrderDTO;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.OrderDetailResponse;
import com.webnongsan.greenshop.model.response.OrderResponse;
import com.webnongsan.greenshop.repository.Entities.OrderEntity;
import com.webnongsan.greenshop.repository.Entities.UserEntity;
import com.webnongsan.greenshop.service.OrderDetailServiceImpl;
import com.webnongsan.greenshop.service.OrderServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class ProfileController extends CommonController{
    private final OrderServiceImpl orderService;
    private final CommonDataService commonDataService;
    private final OrderDetailServiceImpl orderDetailService;
    @GetMapping("profile")
    public String profile(Model model, Principal principal, @RequestParam(defaultValue ="0" )    int page
                                                          , @RequestParam(defaultValue ="5" )   int limit
                                                          , UserDTO userDTO){
        if (principal != null) {

            model.addAttribute("user", new UserEntity());
            userDTO = userService.findByEmail(principal.getName());
            model.addAttribute("user", userDTO);
        }
        PageRequest pageRequest = PageRequest.of(page,limit,
                Sort.by("id").ascending()
        );
        Page<OrderResponse> orderResponsePage = orderService.findOrderByUserId(userDTO.getId(),pageRequest);
        int totalPages = orderResponsePage.getTotalPages();
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        commonDataService.commonData(model, userDTO);
        List<Integer> pageNumbers = IntStream.rangeClosed(0, totalPages-1).boxed().collect(Collectors.toList());
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("orderByUser",orderResponsePage);
        return "web/profile";
    }

    @GetMapping("/order/detail/{id}")
    public String detailOrder(Model model,Principal principal, UserDTO userDTO, @PathVariable("id") Long id){
        if (principal != null) {

            model.addAttribute("user", new UserEntity());
            userDTO = userService.findByEmail(principal.getName());
            model.addAttribute("user", userDTO);
        }
        List<OrderDetailResponse> orderDetailResponses = orderDetailService.findOrderDetailByOrderId(id);
        model.addAttribute("orderDetail",orderDetailResponses);
        commonDataService.commonData(model,userDTO);
        return "web/historyOrderDetail";
    }


    @PutMapping("/order/cancel/{id}")
    public String cancel(Model model,@PathVariable("id") Long id){
        try {
            orderService.updateOrderStatus(id,2);
            return "redirect:/profile";
        } catch (DataNotFoundException e) {
            return "redirect:/profile";
        }
    }

}

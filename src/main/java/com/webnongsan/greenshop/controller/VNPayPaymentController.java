package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.common.CommonDataService;
import com.webnongsan.greenshop.model.dto.OrderDTO;
import com.webnongsan.greenshop.model.dto.TransactionCompleteDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.CartItemEntity;
import com.webnongsan.greenshop.repository.Entities.OrderEntity;
import com.webnongsan.greenshop.service.OrderServiceImpl;
import com.webnongsan.greenshop.service.ShoppingCartServiceImpl;
import com.webnongsan.greenshop.service.UserServiceImpl;
import com.webnongsan.greenshop.service.impl.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller(value ="VNPay")
@RequestMapping("/api")
@RequiredArgsConstructor
public class VNPayPaymentController {
	@Autowired
	HttpSession session;

	private final UserServiceImpl userService;
	private final ShoppingCartServiceImpl shoppingCartService;
	private final OrderServiceImpl orderService;
	private final CommonDataService commonDataService;
	
	@ModelAttribute(value = "user")
	public UserDTO user(Model model, Principal principal, UserDTO userDTO, HttpServletRequest request) {
		if (principal != null) {
			model.addAttribute("userDTO", new UserDTO());
			session = request.getSession();
			String email = session.getAttribute("email").toString();
			String login = session.getAttribute("login").toString();
			if (login.equals("DATABASE")) {
				userDTO = userService.findByEmail(principal.getName());
				if (userDTO != null) {
					model.addAttribute("userDto", userDTO);
				}
			}else {
				if (email != null) {
					userDTO = userService.findByEmail(email);
					model.addAttribute("userDto", userDTO);
				}
			}
		}
		return userDTO;
	}
	
	@GetMapping("/vnpay_result")
	public String completeTransactionVNPayPayment (HttpServletRequest req, HttpServletResponse resp,UserDTO userDto,
			@RequestParam(name = "vnp_OrderInfo", required = false) String vnp_OrderInfo,
			@RequestParam(name = "vnp_Amount", required = false) Integer vnp_Amount,
			@RequestParam(name = "vnp_BankCode", required = false) String vnp_BankCode,
			@RequestParam(name = "vnp_BankTranNo", required = false) String vnp_BankTranNo,
			@RequestParam(name = "vnp_CardType", required = false) String vnp_CardType,
			@RequestParam(name = "vnp_PayDate", required = false) String vnp_PayDate,
			@RequestParam(name = "vnp_ResponseCode", required = false) String vnp_ResponseCode,
			@RequestParam(name = "vnp_TransactionNo", required = false) String vnp_TransactionNo,
			@RequestParam(name = "vnp_TxnRef",required= false) String vnp_TxnRef
			) throws MessagingException{
		// Lấy thời gian khi thanh toán thành công
		String year = vnp_PayDate.substring(0, 4);
		String month = vnp_PayDate.substring(4, 6);
		String date = vnp_PayDate.substring(6, 8);
		String hour = vnp_PayDate.substring(8, 10);
		String minutes = vnp_PayDate.substring(10, 12);
		String second = vnp_PayDate.substring(12, 14);
		String timePay = date + "-" + month + "-" + year + " " + hour + ":" + minutes + ":" + second;
		TransactionCompleteDTO transactionCompleteDto = new TransactionCompleteDTO();
		if (vnp_ResponseCode.equals("00")) {
			//xử lý sự kiện sau khi thanh toán thành công
			transactionCompleteDto.setStatus(true);
			transactionCompleteDto.setAmount(vnp_Amount);
			transactionCompleteDto.setBankName(vnp_BankCode);
			transactionCompleteDto.setMessage("Successfully");
			transactionCompleteDto.setData("");
			Collection<CartItemEntity> cartItems = shoppingCartService.getCartItems();
			double totalPrice = 0;
			for (CartItemEntity cartItem : cartItems) {
				double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
				totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
			}
			if ((totalPrice*100) != Double.valueOf(vnp_Amount)) {
				return null;
			}
			session = req.getSession();
			OrderEntity orderFinal = new OrderEntity();
			orderService.processSuccessfulPayment(userDto,totalPrice,orderFinal,cartItems);
			session.removeAttribute("cartItems");
			return "web/checkout_success";
		}else {
			//xử lý sự kiện sau khi thanh toán không thành công
			transactionCompleteDto.setStatus(false);
			transactionCompleteDto.setAmount(vnp_Amount);
			transactionCompleteDto.setBankName(vnp_BankCode);
			transactionCompleteDto.setMessage("Failed");
			transactionCompleteDto.setData("");
			return null;
		}
	}
}

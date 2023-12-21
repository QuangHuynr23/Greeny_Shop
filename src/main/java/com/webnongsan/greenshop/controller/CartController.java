package com.webnongsan.greenshop.controller;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.webnongsan.greenshop.common.CommonDataService;
import com.webnongsan.greenshop.config.PaypalPaymentIntent;
import com.webnongsan.greenshop.config.PaypalPaymentMethod;
import com.webnongsan.greenshop.constant.SystemConstant;
import com.webnongsan.greenshop.converter.OrderConverter;
import com.webnongsan.greenshop.converter.ProductConverter;
import com.webnongsan.greenshop.model.dto.OrderDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.*;
import com.webnongsan.greenshop.repository.OrderDetailRepository;
import com.webnongsan.greenshop.repository.OrderRepository;
import com.webnongsan.greenshop.repository.ProductRepository;
import com.webnongsan.greenshop.service.OrderServiceImpl;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import com.webnongsan.greenshop.service.ShoppingCartServiceImpl;
import com.webnongsan.greenshop.service.impl.PaypalService;
import com.webnongsan.greenshop.utils.SystemUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Date;

@Controller
@RequiredArgsConstructor
public class CartController extends CommonController{
     private final CommonDataService commonDataService;
     private HttpSession session;
     private final ProductServiceImpl productService;
     private final ShoppingCartServiceImpl shoppingCartService;
     private final ProductConverter productConverter;
     private final ProductRepository productRepository;
     private final PaypalService paypalService;
     private final OrderServiceImpl orderService;
     private final OrderConverter orderConverter;
     private final OrderRepository orderRepository;
     private final OrderDetailRepository orderDetailRepository;
     private OrderEntity orderFinal =new OrderEntity();
     private Logger log = LoggerFactory.getLogger(getClass());

     @GetMapping("/shoppingCart_checkout")
     public String shoppingCart(Model model){
          Collection<CartItemEntity> cartItems = shoppingCartService.getCartItems();
           model.addAttribute("cartItems",cartItems);
          model.addAttribute("total", shoppingCartService.getAmount());
          double totalPrice = 0;
          for (CartItemEntity cartItem : cartItems) {
               double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
               totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
          }

          model.addAttribute("totalPrice", totalPrice);
          model.addAttribute("totalCartItems", shoppingCartService.getCountCart());
          return "web/shoppingCart_checkout";
     }

     @GetMapping("/checkout")
     public String checkout(Model model, UserDTO userDTO){
          OrderEntity order = new OrderEntity();
          model.addAttribute("order",order);
          Collection<CartItemEntity> cartItems = shoppingCartService.getCartItems();
          for (CartItemEntity cartItemEntity : cartItems) {
               cartItemEntity.setProduct(productRepository.getById(cartItemEntity.getProduct().getId()));
          }
          model.addAttribute("cartItems", cartItems);
          model.addAttribute("totalCartItems",shoppingCartService.getCountCart());
          double totalPrice = 0;
          for (CartItemEntity cartItem: cartItems){
               double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
               totalPrice += price -(price * cartItem.getProduct().getDiscount() / 100);
          }
          model.addAttribute("totalPrice", totalPrice);
          commonDataService.commonData(model,userDTO);
          return "web/shoppingCart_checkout";
     }

     // done checkout ship cod
     @GetMapping(value = "/checkout_success")
     public String checkoutSuccess(Model model, UserDTO userDto) {
          commonDataService.commonData(model, userDto);

          return "web/checkout_success";

     }
     // done checkout paypal
     @GetMapping(value = "/checkout_paypal_success")
     public String paypalSuccess(Model model, UserDTO userDto) {
          commonDataService.commonData(model, userDto);

          return "web/checkout_paypal_success";

     }

     @PostMapping("/checkout")
     public String checkOut(Model model,OrderEntity order,UserDTO userDTO, HttpServletRequest request) throws MessagingException {
          String checkOut = request.getParameter("checkOut");
          Collection<CartItemEntity> cartItems = shoppingCartService.getCartItems();
          double totalPrice = getTotalPrice(cartItems);
          if(StringUtils.equals(checkOut,"paypal")){
               String cancelUrl = SystemUtils.getBaseUrl(request) + "/" + SystemConstant.URL_PAYPAL_CANCEL;
               String successUrl = SystemUtils.getBaseUrl(request) + "/" + SystemConstant.URL_PAYPAL_SUCCESS;
               try{
                    totalPrice = totalPrice / 22;
                    Payment payment = paypalService.createPayment(totalPrice, "USD", PaypalPaymentMethod.paypal,
                            PaypalPaymentIntent.sale, "payment description", cancelUrl, successUrl);
                    for (Links links : payment.getLinks()) {
                         if (links.getRel().equals("approval_url")) {
                              return "redirect:" + links.getHref();
                         }
                    }
               }
               catch (PayPalRESTException e) {
                    log.error(e.getMessage());
               }
          }
          session = request.getSession();
          orderService.processOrder(order,userDTO,orderFinal,totalPrice,cartItems);
          session.removeAttribute("cartItems");
          model.addAttribute("orderId", order.getId());
          return "redirect:/checkout_success";
     }
     // paypal
     @GetMapping(SystemConstant.URL_PAYPAL_SUCCESS)
     public String successPay(@RequestParam("" + "" + "") String paymentId, @RequestParam("PayerID") String payerId,
                              HttpServletRequest request, UserDTO userDTO, Model model) throws MessagingException {
          Collection<CartItemEntity> cartItems = shoppingCartService.getCartItems();
          model.addAttribute("cartItems", cartItems);
          model.addAttribute("total", shoppingCartService.getAmount());
          double totalPrice = getTotalPrice(cartItems);
          model.addAttribute("totalPrice", totalPrice);
          model.addAttribute("totalCartItems", shoppingCartService.getCountCart());
          try {
               Payment payment = paypalService.executePayment(paymentId, payerId);
               if (payment.getState().equals("approved")) {
                    session = request.getSession();
                    orderService.processSuccessfulPayment(userDTO,totalPrice,orderFinal,cartItems);
                    session.removeAttribute("cartItems");
                    model.addAttribute("orderId", orderFinal.getId());
                    orderFinal = new OrderEntity();
                    return "redirect:/checkout_paypal_success";
               }
          } catch (PayPalRESTException e) {
               log.error(e.getMessage());
          }
          return "redirect:/";
     }

     // delete cartItem
     @GetMapping("/remove/{id}")
     public String removeItem(@PathVariable("id") Long id, Model model, HttpServletRequest request ){
          orderService.removeOrder(id);
          Collection<CartItemEntity> cartItems = shoppingCartService.getCartItems();
          session = request.getSession();
          cartItems.remove(session);
          model.addAttribute("totalCartItems", shoppingCartService.getCountCart());
          return "redirect:/checkout";
     }


     @GetMapping(value = "addToCart")
     public String addToCart(@RequestParam("productId") Long productId, HttpServletRequest request
                            , Model model){
          shoppingCartService.processAddToCart(productId);
          session  = request.getSession();
          Collection<CartItemEntity> cartItemEntities = shoppingCartService.getCartItems();
          session.setAttribute("cartItems",cartItemEntities);
          model.addAttribute("totalCartItems", shoppingCartService.getCountCart());
          return "redirect:/products";
     }

     @GetMapping("/quantity-inc-dec")
     public String handleQuantityIncDec(HttpServletRequest request, @RequestParam("action") String action, @RequestParam("id") int id) {
          Collection<CartItemEntity> cartItems = (Collection<CartItemEntity>) request.getSession().getAttribute("cartItems");

          if (cartItems != null) {
               shoppingCartService.updateCartItemQuantity(cartItems, action, id);
               request.getSession().setAttribute("cartItems", cartItems);
          }
          return "redirect:/products";
     }

     private Double getTotalPrice(Collection<CartItemEntity> cartItems){
          double totalPrice = 0;
          for (CartItemEntity cartItem : cartItems) {
               double price = cartItem.getQuantity() * cartItem.getProduct().getPrice();
               totalPrice += price - (price * cartItem.getProduct().getDiscount() / 100);
          }
          return totalPrice;
     }

}

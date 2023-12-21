package com.webnongsan.greenshop.service.impl;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.webnongsan.greenshop.common.CommonDataService;
import com.webnongsan.greenshop.config.PaypalPaymentIntent;
import com.webnongsan.greenshop.config.PaypalPaymentMethod;
import com.webnongsan.greenshop.constant.SystemConstant;
import com.webnongsan.greenshop.converter.OrderConverter;
import com.webnongsan.greenshop.converter.UserConverter;
import com.webnongsan.greenshop.customerexception.DataNotFoundException;
import com.webnongsan.greenshop.model.dto.OrderDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.OrderResponse;
import com.webnongsan.greenshop.repository.Entities.*;
import com.webnongsan.greenshop.repository.OrderDetailRepository;
import com.webnongsan.greenshop.repository.OrderRepository;
import com.webnongsan.greenshop.repository.ProductRepository;
import com.webnongsan.greenshop.repository.UserRepository;
import com.webnongsan.greenshop.service.OrderServiceImpl;
import com.webnongsan.greenshop.utils.SystemUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements OrderServiceImpl {
    private final PaypalService paypalService;
    private final UserConverter userConverter;
    private final OrderRepository orderRepository;
    private final ShoppingCartService shoppingCartService;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final CommonDataService commonDataService;
    private final ProductRepository productRepository;
    private final OrderConverter orderConverter;
    private Logger log = LoggerFactory.getLogger(getClass());

    @Override
    @Transactional
    public void processOrder(OrderEntity order, UserDTO userDTO, OrderEntity orderFinal, double totalPrice, Collection<CartItemEntity> cartItems) throws MessagingException {


        BeanUtils.copyProperties(order, orderFinal);
        UserEntity userEntity = userConverter.convertToEntity(userDTO);
        userEntity = userRepository.findById(userEntity.getId()).orElse(null);
        OrderEntity orderEntity = createOrder(userEntity, order, totalPrice);
        orderEntity.setStatus(0);
        orderRepository.save(orderEntity);
        saveOrderDetails(orderEntity, cartItems);
        // sendMail
        sendOrderConfirmationEmail(userDTO.getEmail(), "Greeny-Shop Xác Nhận Đơn hàng", "aaaa", cartItems, totalPrice, order);
    }

    @Override
    @Transactional
    public void processSuccessfulPayment(UserDTO userDTO, double totalPrice,
                                         OrderEntity orderFinal, Collection<CartItemEntity> cartItems) throws MessagingException {

        UserEntity userEntity = userConverter.convertToEntity(userDTO);
        userEntity = userRepository.findById(userEntity.getId()).orElse(null);
        OrderEntity orderEntity = createOrder(userEntity, orderFinal, totalPrice);
        orderEntity.setStatus(2);
        orderRepository.save(orderEntity);
        saveOrderDetails(orderEntity, cartItems);
        sendOrderConfirmationEmail(userDTO.getEmail(), "Greeny-Shop Xác Nhận Đơn hàng", "aaaa", cartItems, totalPrice, orderFinal);
    }

    @Override
    public void removeOrder(Long id) {
        ProductEntity product = productRepository.findById(id).orElse(null);
        Collection<CartItemEntity> cartItems = shoppingCartService.getCartItems();
        if(product != null){
            CartItemEntity cartItem = new CartItemEntity();
            BeanUtils.copyProperties(product,cartItem);
            cartItem.setProduct(product);
            cartItem.setId(id);
            shoppingCartService.remove(cartItem);
        }
    }

    @Override
    public Page<OrderResponse> findOrderByUserId(Long userId, PageRequest request) {
        Page<OrderEntity> orderPage = orderRepository.findOrderByUserId(userId, request);
        return orderPage.map(orderConverter::convertToResponse);
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long id,int status) {
        Optional<OrderEntity> orderEntity = orderRepository.findById(id);
        if (orderEntity.isPresent()) {
            OrderEntity order = orderEntity.get();
            order.setStatus(status);
            orderRepository.save(order);
        }
        else {
            throw new DataNotFoundException("Order not found");
        }
    }

    @Override
    public List<OrderDTO> findAll() {
        List<OrderEntity> orderEntities = orderRepository.findAll();
        return orderEntities.stream().map(orderConverter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO findById(Long id) {
        Optional<OrderEntity> orderEntity =  orderRepository.findById(id);
        return orderEntity.map(orderConverter::convertToDTO).orElse(null);
    }


    private OrderEntity createOrder(UserEntity user, OrderEntity order, double totalPrice) {
        Date date = new Date();
        order.setOrderDate(date);
        order.setUser(user);
        order.setAmount(totalPrice);
        return order;
    }

    private void saveOrderDetails(OrderEntity order, Collection<CartItemEntity> cartItems) {
        for (CartItemEntity cartItem : cartItems) {
            OrderDetailEntity orderDetail = new OrderDetailEntity();
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setOrder(order);
            orderDetail.setProduct(cartItem.getProduct());
            double unitPrice = cartItem.getProduct().getPrice();
            orderDetail.setPrice(unitPrice);
            orderDetailRepository.save(orderDetail);
        }
    }

    private void sendOrderConfirmationEmail(String userEmail, String subject, String message, Collection<CartItemEntity> cartItems,
                                            double totalPrice, OrderEntity order) throws MessagingException {
        commonDataService.sendSimpleEmail(userEmail, subject, message, cartItems, totalPrice, order);
        shoppingCartService.clear();
    }
}

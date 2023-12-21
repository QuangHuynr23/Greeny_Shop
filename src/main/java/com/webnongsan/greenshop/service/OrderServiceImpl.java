package com.webnongsan.greenshop.service;

import com.webnongsan.greenshop.model.dto.OrderDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.model.response.OrderResponse;
import com.webnongsan.greenshop.repository.Entities.CartItemEntity;
import com.webnongsan.greenshop.repository.Entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Collection;
import java.util.List;

public interface OrderServiceImpl {
    void processOrder(OrderEntity orderDTO, UserDTO userDTO,
                      OrderEntity orderFinal,double totalPrice, Collection<CartItemEntity> cartItems) throws MessagingException;
    void processSuccessfulPayment(UserDTO userDTO,double totalPrice,
                                  OrderEntity orderFinal, Collection<CartItemEntity> cartItems) throws MessagingException;
    void removeOrder(Long id);

    Page<OrderResponse> findOrderByUserId(Long userId, PageRequest request);

    void updateOrderStatus(Long id,int status);
    List<OrderDTO> findAll();
    OrderDTO findById(Long id);
}

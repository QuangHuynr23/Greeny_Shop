package com.webnongsan.greenshop.service;

import com.webnongsan.greenshop.model.dto.OrderDTO;
import com.webnongsan.greenshop.repository.Entities.CartItemEntity;

import java.util.Collection;

public interface ShoppingCartServiceImpl {
    void add(CartItemEntity item);
    void remove(CartItemEntity item);
    void clear();
    Collection<CartItemEntity> getCartItems();
    int getCountCart();
    void processAddToCart(Long productId);
    void updateCartItemQuantity(Collection<CartItemEntity> cartItems, String action, int id);
    Double getAmount();

}

package com.webnongsan.greenshop.service.impl;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.webnongsan.greenshop.config.PaypalPaymentIntent;
import com.webnongsan.greenshop.config.PaypalPaymentMethod;
import com.webnongsan.greenshop.constant.SystemConstant;
import com.webnongsan.greenshop.converter.ProductConverter;
import com.webnongsan.greenshop.converter.UserConverter;
import com.webnongsan.greenshop.model.dto.OrderDTO;
import com.webnongsan.greenshop.model.dto.ProductDTO;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.CartItemEntity;
import com.webnongsan.greenshop.repository.Entities.OrderEntity;
import com.webnongsan.greenshop.service.ProductServiceImpl;
import com.webnongsan.greenshop.service.ShoppingCartServiceImpl;
import com.webnongsan.greenshop.utils.SystemUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ShoppingCartService implements ShoppingCartServiceImpl {
    private final ProductServiceImpl productService;
    private final ProductConverter productConverter;

    private Map<Long, CartItemEntity> map = new HashMap<>();
    @Override
    public void add(CartItemEntity item) {
         CartItemEntity existedItem = map.get(item.getId());
//         Kiểm tra mặt hàng có tồn tại trong giỏ không
         if(existedItem != null){
            existedItem.setQuantity(item.getQuantity() + existedItem.getQuantity());
            existedItem.setTotalPrice(item.getTotalPrice()+existedItem.getProduct().getPrice() * existedItem.getQuantity() );
         } else {
            map.put(item.getId(), item );
         }
    }

    @Override
    public void remove(CartItemEntity item) {
        map.remove(item.getId());
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Collection<CartItemEntity> getCartItems() {
        return map.values();
    }

    @Override
    public int getCountCart() {
        if(map.isEmpty()){
            return 0;
        }
        int totalCartItems =0;
        for(Map.Entry<Long,CartItemEntity> itemCart : map.entrySet()){
            totalCartItems += itemCart.getValue().getQuantity();
        }
        return totalCartItems;
    }

    @Override
    public void processAddToCart(Long productId) {
        ProductDTO productDTO = productService.findById(productId);
        if(productDTO != null){
            CartItemEntity item = new CartItemEntity();
            BeanUtils.copyProperties(productDTO, item);
            item.setQuantity(1);
            item.setProduct(productConverter.convertToEntity(productDTO));
            item.setId(productId);
            add(item);
        }
    }

    @Override
    public void updateCartItemQuantity(Collection<CartItemEntity> cartItems, String action, int id) {
        if (action != null && id >= 1) {
            for (CartItemEntity item : cartItems) {
                if (item.getId() == id) {
                    int quantity = item.getQuantity();
                    if (action.equals("inc")) {
                        quantity++;
                    } else if (action.equals("dec") && item.getQuantity() > 1) {
                        quantity--;
                    }
                    item.setQuantity(quantity);
                    break;
                }
            }
        }
    }

    @Override
    public Double getAmount() {
        return map.values().stream().mapToDouble(item -> item.getQuantity() * item.getProduct().getPrice()).sum();
    }


}

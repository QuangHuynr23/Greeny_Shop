package com.webnongsan.greenshop.model.dto;

import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import com.webnongsan.greenshop.repository.Entities.UserEntity;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    private Date orderDate;
    private Double amount;
    private String address;
    private String phone;
    private int status;
    private List<ProductEntity> products;
    private UserEntity user;
}

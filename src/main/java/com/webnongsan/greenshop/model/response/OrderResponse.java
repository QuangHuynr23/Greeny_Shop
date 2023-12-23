package com.webnongsan.greenshop.model.response;

import com.webnongsan.greenshop.repository.Entities.UserEntity;
import lombok.*;

import java.util.Date;
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long id;
    private Date orderDate;
    private Double amount;
    private String address;
    private String phone;
    private int status;
    private UserEntity user;
}

package com.webnongsan.greenshop.model.response;

import com.webnongsan.greenshop.repository.Entities.ProductEntity;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id ;
    private int quantity;
    private Double price;
    private ProductEntity product;
}

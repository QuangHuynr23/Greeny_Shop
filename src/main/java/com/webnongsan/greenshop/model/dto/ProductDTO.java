package com.webnongsan.greenshop.model.dto;

import com.webnongsan.greenshop.repository.Entities.CategoryEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    private String productName;
    private int quantity;
    private double price;
    private int discount;
    private String productImage;
    private String description;
    private Date enteredDate;
    private Boolean status;
    private Boolean favorite;
    private CategoryEntity category;
}

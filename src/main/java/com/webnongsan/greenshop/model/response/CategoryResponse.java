package com.webnongsan.greenshop.model.response;

import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {
    private Long id;
    private String categoryName;
    private int countProduct;
}

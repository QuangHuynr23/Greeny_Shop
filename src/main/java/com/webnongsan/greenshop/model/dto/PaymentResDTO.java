package com.webnongsan.greenshop.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResDTO {
    private String status;
    private String message;
    private String URL;
    private String bankName;
}

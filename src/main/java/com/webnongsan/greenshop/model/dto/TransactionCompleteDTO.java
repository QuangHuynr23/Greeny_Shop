package com.webnongsan.greenshop.model.dto;

import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TransactionCompleteDTO{
	private Boolean status;
	private String message;
	private String data;
	private String bankName;
	private Integer amount;
}

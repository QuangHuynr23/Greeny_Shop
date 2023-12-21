package com.webnongsan.greenshop.model.response;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatisticalOrderDetail {
	private String name;
	private int day;
//	private Boolean status;
	private int quantity;
	private double sumPrice;
	private double averagePrice;
	private double minimumPrice;
	private double maximumPrice;
}

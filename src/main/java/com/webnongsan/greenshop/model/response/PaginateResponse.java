package com.webnongsan.greenshop.model.response;

import lombok.*;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaginateResponse {
	private int page;
	private Integer totalPage;

}

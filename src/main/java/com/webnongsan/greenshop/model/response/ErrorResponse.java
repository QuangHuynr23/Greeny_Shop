package com.webnongsan.greenshop.model.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String error;
    private List<String> details = new ArrayList<>();
}

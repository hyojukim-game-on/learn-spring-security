package com.toy.tuser.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateExpenseRequest {
    private String category;
    private String detail;
    private Integer amount;
    private String spender;
}

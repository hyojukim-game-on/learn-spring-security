package com.toy.tuser.dto;

import com.toy.tuser.domain.Expense;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class ExpenseViewResponse {

    private Long id;
    private String category;
    private String detail;
    private Integer amount;
    private String spender;

    public ExpenseViewResponse(Expense expense) {
        this.id = expense.getId();
        this.category = expense.getCategory();
        this.detail = expense.getDetail();
        this.amount = expense.getAmount();
        this.spender = expense.getSpender();
    }
}

package com.toy.tuser.dto;

import com.toy.tuser.domain.Expense;
import lombok.Getter;

@Getter
public class ExpenseListViewResponse {

    private final Long id;
    private final String category;
    private final String detail;
    private final Integer amount;
    private final String spender;

    public ExpenseListViewResponse(Expense expense) {
        this.id = expense.getId();
        this.category = expense.getCategory();
        this.detail = expense.getDetail();
        this.amount = expense.getAmount();
        this.spender = expense.getSpender();
    }
}

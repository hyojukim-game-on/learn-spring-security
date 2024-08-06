package com.toy.tuser.dto;

import com.toy.tuser.domain.Expense;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddExpenseRequest {
    private String category;
    private String detail;
    private Integer amount;
    private String spender;

    public Expense toEntity(String author) { // 생성자를 사용해 객체 생성
        return Expense.builder()
                .category(category)
                .detail(detail)
                .amount(amount)
                .spender(spender)
                .build();
    }
}

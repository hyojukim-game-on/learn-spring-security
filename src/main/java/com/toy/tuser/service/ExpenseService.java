package com.toy.tuser.service;

import com.toy.tuser.domain.Expense;
import com.toy.tuser.dto.AddExpenseRequest;
import com.toy.tuser.dto.UpdateExpenseRequest;
import com.toy.tuser.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor // final 이 붙거나 @Notnull이 붙은 필드의 생성자 추가
@Service // 빈으로 등록
public class ExpenseService {

    private final ExpenseRepository expenseRepository;


    // 단건 소비 내역 추가
    @Transactional
    public Expense save(AddExpenseRequest request, String userName) {
        return expenseRepository.save(request.toEntity(userName));
    }


    // 소비 내역 전부 조회
    @Transactional(readOnly = true)
    public List<Expense> findAll() {
        return expenseRepository.findAll();
    }


    // 단건 소비 내역 조회
    @Transactional(readOnly = true)
    public Expense findById(long id) {
        return expenseRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found: " + id));
    }


    // 단건 소비 내역 삭제
    @Transactional
    public void delete(long id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found :" + id));
        authorizeExpenseAuthor(expense);
        expenseRepository.delete(expense);
    }


    // 단건 소비 내역 수정
    @Transactional
    public Expense update(long id, UpdateExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("not found: "+id));

        authorizeExpenseAuthor(expense);
        expense.update(
                request.getCategory(),
                request.getDetail(),
                request.getAmount(),
                request.getSpender()
        );

        return expense;
    }


    // 소비 내역을 작성한 유저인지 확인
    private static void authorizeExpenseAuthor(Expense expense) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!expense.getSpender().equals(userName)) {
            throw new IllegalArgumentException("not authorized");
        }
    }

}

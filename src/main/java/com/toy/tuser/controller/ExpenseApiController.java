package com.toy.tuser.controller;

import com.toy.tuser.domain.Expense;
import com.toy.tuser.dto.AddExpenseRequest;
import com.toy.tuser.dto.ExpenseResponse;
import com.toy.tuser.dto.UpdateExpenseRequest;
import com.toy.tuser.service.ExpenseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController // HTTP Response Body 에 객체 데이터를 JSON 형식으로 반환하는 컨트롤러
public class ExpenseApiController {

    private final ExpenseService expenseService;

    // HTTP 메서드가 POST일 때 전달받은 URL과 동일하면 메서드로 매핑
    @PostMapping("/api/expenses")
    //@RequestBody 로 요청 본문 값 매핑
    public ResponseEntity<Expense> addExpense(@RequestBody AddExpenseRequest request,
                                              Principal principal) {
        Expense savedExpense = expenseService.save(request, principal.getName());
        // 요청한 자원이 성공적으로 생성되었으며 저장된 블로그 글 정보를 응답 객체에 담아 전송
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(savedExpense);
    }

    @GetMapping("/api/expenses")
    public ResponseEntity<List<ExpenseResponse>> findAllExpenses() {
        List<ExpenseResponse> articles = expenseService.findAll()
                .stream()
                .map(ExpenseResponse::new)
                .toList();

        return ResponseEntity.ok()
                .body(articles);
    }

    @GetMapping("/api/expense/{id}")
    // URL 경로에서 값 추출
    public ResponseEntity<ExpenseResponse> findExpense(@PathVariable("id") long id) {
        Expense expense = expenseService.findById(id);

        return ResponseEntity.ok()
                .body(new ExpenseResponse(expense));
    }


    @DeleteMapping("/api/expenses/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable("id") long id) {
        expenseService.delete(id);

        return ResponseEntity.ok()
                .build();
    }


    @PutMapping("/api/expenses/{id}")
    public ResponseEntity<Expense> updateExpense(@PathVariable("id") long id,
                                                 @RequestBody UpdateExpenseRequest request) {
        Expense updatedExpense = expenseService.update(id, request);

        return ResponseEntity.ok()
                .body(updatedExpense);
    }

}

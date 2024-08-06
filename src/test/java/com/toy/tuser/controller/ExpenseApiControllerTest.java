package com.toy.tuser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.toy.tuser.domain.Expense;
import com.toy.tuser.domain.User;
import com.toy.tuser.dto.AddExpenseRequest;
import com.toy.tuser.dto.UpdateExpenseRequest;
import com.toy.tuser.repository.ExpenseRepository;
import com.toy.tuser.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest // 테스트용 애플리케이션 컨텍스트
@AutoConfigureMockMvc // MockMvc 생성 및 자동 구성
class ExpenseApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper; // 직렬화, 역직렬화를 위한 클래스

    @Autowired
    private WebApplicationContext context;

    @Autowired
    ExpenseRepository expenseRepository;

    @Autowired
    UserRepository userRepository;

    User user;

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("hyoju@gmail.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user,
                user.getPassword(), user.getAuthorities()));
    }


    @BeforeEach // 테스트 실행 전 실행하는 메서드
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        expenseRepository.deleteAll();
    }

    @DisplayName("addExpense: 단건 소비 내역 추가에 성공한다.")
    @Test
    public void addExpense() throws Exception {
        // given
        final String url = "/api/expenses";
        final String category = "카페";
        final String detail = "이디야 아이스 아메리카노";
        final Integer amount = 2800;
        final String spender = "효주";
        final AddExpenseRequest userRequest = new AddExpenseRequest(category, detail, amount, spender);

        // 객체 JSON 으로 직렬화
        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        // 설정한 내용을 바탕으로 요청 전송
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Expense> expenses = expenseRepository.findAll();

        assertThat(expenses.size()).isEqualTo(1); // 크기가 1인지 검증
        assertThat(expenses.get(0).getCategory()).isEqualTo(category);
        assertThat(expenses.get(0).getDetail()).isEqualTo(detail);
        assertThat(expenses.get(0).getAmount()).isEqualTo(amount);
        assertThat(expenses.get(0).getSpender()).isEqualTo(spender);
    }


    @DisplayName("findAllExpenses: 전체 소비 내역 조회에 성공한다.")
    @Test
    public void findAllExpenses() throws Exception {
        // given
        final String url = "/api/expenses";
        Expense savedExpense = createDefaultExpense();

        // when
        final ResultActions resultActions = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].category").value(savedExpense.getCategory()))
                .andExpect(jsonPath("$[0].detail").value(savedExpense.getDetail()))
                .andExpect(jsonPath("$[0].amount").value(savedExpense.getAmount()))
                .andExpect(jsonPath("$[0].spender").value(savedExpense.getSpender()));
    }

    @DisplayName("findExpense: 단건 소비 내역 조회에 성공한다.")
    @Test
    public void findExpense() throws Exception {
        // given
        final String url = "/api/expense/{id}";
        Expense savedExpense = createDefaultExpense();

        // when
        final ResultActions resultActions = mockMvc.perform(get(url, savedExpense.getId()));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category").value(savedExpense.getCategory()))
                .andExpect(jsonPath("$.detail").value(savedExpense.getDetail()))
                .andExpect(jsonPath("$.amount").value(savedExpense.getAmount()))
                .andExpect(jsonPath("$.spender").value(savedExpense.getSpender()));
    }

    @DisplayName("deleteExpense: 단건 소비 내역 삭제에 성공한다.")
    @Test
    public void deleteExpense() throws Exception {
        // given
        final String url = "/api/expenses/{id}";
        Expense savedExpense = createDefaultExpense();

        // when
        mockMvc.perform(delete(url, savedExpense.getId()))
                .andExpect(status().isOk());

        // then
        List<Expense> expenses = expenseRepository.findAll();

        assertThat(expenses).isEmpty();
    }

    @DisplayName("updateExpense: 단건 소비 내역 수정에 성공한다.")
    @Test
    public void updateExpense() throws Exception {
        // given
        final String url = "/api/expenses/{id}";
        final String category = "카페";
        final String detail = "이디야 아이스 아메리카노";
        final Integer amount = 2800;
        final String spender = "효주";

        Expense savedExpense = createDefaultExpense();

        final String newCategory = "카페";
        final String newDetail = "이디야 카페라떼";
        final Integer newAmount = 3400;
        final String newSpender = "효주";

        UpdateExpenseRequest request = new UpdateExpenseRequest(
                newCategory,
                newDetail,
                newAmount,
                newSpender);

        // when
        ResultActions result = mockMvc.perform(put(url, savedExpense.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk());

        Expense expense = expenseRepository.findById(savedExpense.getId()).get();

        assertThat(expense.getCategory()).isEqualTo(newCategory);
        assertThat(expense.getDetail()).isEqualTo(newDetail);
        assertThat(expense.getAmount()).isEqualTo(newAmount);
        assertThat(expense.getSpender()).isEqualTo(newSpender);
    }

    private Expense createDefaultExpense() {
        return expenseRepository.save(Expense.builder()
                .category("카페")
                .detail("아이스 아메리카노")
                .amount(2800)
                .spender(user.getUsername())
                .build());
    }

    
}
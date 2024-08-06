package com.toy.tuser.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity // 엔티티로 지정
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expense {
    @Id // id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키를 자동으로 1씩 증가
    @Column(name="id",updatable=false)
    private Long id;

    @Column(name="category")
    private String category;

    @Column(name = "detail")
    private String detail;

    @Column(name = "amount")
    private Integer amount;

    @CreatedDate // 엔티티가 생성될 때 생성 시간 저장
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때 수정 시간 저장
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "spender", nullable = false)
    private String spender;

    @Builder // 빌더 패턴으로 객체 생성
    public Expense(String category, String detail, Integer amount, String spender) {
        this.category = category;
        this.detail = detail;
        this.amount = amount;
        this.spender = spender;
    }

    public void update(String category, String detail, Integer amount, String spender) {
        this.category = category;
        this.detail = detail;
        this.amount = amount;
        this.spender = spender;
    }
}

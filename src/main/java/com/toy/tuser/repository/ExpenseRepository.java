package com.toy.tuser.repository;

import com.toy.tuser.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}

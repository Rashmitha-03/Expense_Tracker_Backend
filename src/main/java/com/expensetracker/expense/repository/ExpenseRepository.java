package com.expensetracker.expense.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.expensetracker.expense.dto.CategoryExpenseResponse;
import com.expensetracker.expense.entity.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	List<Expense> findByCategory(String category);

	Page<Expense> findByUserId(Long userId, Pageable pageable);

	List<Expense> findByUserId(Long userId);

	List<Expense> findByUserIdAndCategory(Long userId, String category);

	// Between two dates
	List<Expense> findByUserIdAndExpenseDateBetween(Long userId, LocalDate start, LocalDate end);

	// Today's expenses
	List<Expense> findByUserIdAndExpenseDate(Long userId, LocalDate today);

	@Query("SELECT SUM(e.amount) FROM Expense e WHERE e.user.id = :userId")
	Double getTotalExpenseByUserId(Long userId);
	
	@Query("SELECT new com.expensetracker.expense.dto.CategoryExpenseResponse(e.category, SUM(e.amount)) " +
		       "FROM Expense e WHERE e.user.id = :userId GROUP BY e.category")
		List<CategoryExpenseResponse> getCategoryWiseTotal(Long userId);

}


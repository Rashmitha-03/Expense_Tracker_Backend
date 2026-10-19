package com.expensetracker.expense.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import com.expensetracker.expense.dto.AddExpenseRequest;
import com.expensetracker.expense.dto.CategoryExpenseResponse;
import com.expensetracker.expense.dto.ExpenseResponse;

public interface ExpenseService {

	ExpenseResponse addExpense(AddExpenseRequest request);

	List<ExpenseResponse> getAllExpenses();

	ExpenseResponse getExpenseById(Long id);

	void deleteExpense(Long id);

	ExpenseResponse updateExpense(Long id, AddExpenseRequest request);

	List<ExpenseResponse> getExpensesByCategory(String category);

	Page<ExpenseResponse> getExpenses(int page, int size, String sortBy, String direction);

	List<ExpenseResponse> getExpensesBetweenDates(LocalDate start, LocalDate end);

	List<ExpenseResponse> getWeeklyExpenses();

	List<ExpenseResponse> getMonthlyExpenses(int month, int year);

	List<ExpenseResponse> getTodayExpenses();
	
	Double getTotalExpenseForUser();
	
	List<CategoryExpenseResponse> getCategoryWiseExpenses();

}

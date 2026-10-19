package com.expensetracker.expense.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.expensetracker.expense.dto.AddExpenseRequest;
import com.expensetracker.expense.dto.CategoryExpenseResponse;
import com.expensetracker.expense.dto.ExpenseResponse;
import com.expensetracker.expense.service.ExpenseService;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
	private final ExpenseService expenseService;

	public ExpenseController(ExpenseService expenseService) {
		this.expenseService = expenseService;
	}

	@PostMapping
	public ResponseEntity<ExpenseResponse> addExpense(@RequestBody AddExpenseRequest request) {
		ExpenseResponse response = expenseService.addExpense(request);
		return new ResponseEntity<>(response, HttpStatus.CREATED);
	}

	@GetMapping("/{id}")
	public ResponseEntity<ExpenseResponse> getExpenseById(@PathVariable Long id) {
		ExpenseResponse response = expenseService.getExpenseById(id);
		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteExpense(@PathVariable Long id) {
		expenseService.deleteExpense(id);
		return ResponseEntity.ok("Expense deleted successfully with ID: " + id);
	}

	@PutMapping("/{id}")
	public ResponseEntity<ExpenseResponse> updateExpense(@PathVariable Long id,
			@RequestBody AddExpenseRequest request) {

		ExpenseResponse response = expenseService.updateExpense(id, request);
		return ResponseEntity.ok(response);
	}

	@GetMapping("/filter/category")
	public ResponseEntity<List<ExpenseResponse>> filterExpenses(@RequestParam String category) {
		List<ExpenseResponse> filteredExpenses = expenseService.getExpensesByCategory(category);
		return ResponseEntity.ok(filteredExpenses);
	}

	@GetMapping
	public ResponseEntity<Page<ExpenseResponse>> getExpenses(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(defaultValue = "expenseDate") String sortBy,
			@RequestParam(defaultValue = "desc") String direction) {
		return ResponseEntity.ok(expenseService.getExpenses(page, size, sortBy, direction));
	}

	@GetMapping("/filter/dates")
	public ResponseEntity<List<ExpenseResponse>> filterBetweenDates(@RequestParam LocalDate start,
			@RequestParam LocalDate end) {
		return ResponseEntity.ok(expenseService.getExpensesBetweenDates(start, end));
	}

	@GetMapping("/weekly")
	public ResponseEntity<List<ExpenseResponse>> getWeeklyExpenses() {
		return ResponseEntity.ok(expenseService.getWeeklyExpenses());
	}

	@GetMapping("/monthly")
	public ResponseEntity<List<ExpenseResponse>> getMonthlyExpenses(@RequestParam int month, @RequestParam int year) {
		return ResponseEntity.ok(expenseService.getMonthlyExpenses(month, year));
	}

	@GetMapping("/today")
	public ResponseEntity<List<ExpenseResponse>> getTodayExpenses() {

		return ResponseEntity.ok(expenseService.getTodayExpenses());
	}

	@GetMapping("/category/{category}")
	public ResponseEntity<List<ExpenseResponse>> getByCategory(@PathVariable String category) {
		return ResponseEntity.ok(expenseService.getExpensesByCategory(category));
	}

	@GetMapping("/totalExpensePerUser")
	public ResponseEntity<Double> getTotalExpense() {
	    return ResponseEntity.ok(expenseService.getTotalExpenseForUser());
	}
	
	@GetMapping("/category-summary")
	public List<CategoryExpenseResponse> getCategorySummary() {
	    return expenseService.getCategoryWiseExpenses();
	}
}

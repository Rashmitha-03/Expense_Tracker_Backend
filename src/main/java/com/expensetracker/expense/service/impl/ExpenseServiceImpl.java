package com.expensetracker.expense.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.expensetracker.expense.dto.AddExpenseRequest;
import com.expensetracker.expense.dto.CategoryExpenseResponse;
import com.expensetracker.expense.dto.ExpenseResponse;
import com.expensetracker.expense.entity.Expense;
import com.expensetracker.expense.exception.ExpenseNotFoundException;
import com.expensetracker.expense.repository.ExpenseRepository;
import com.expensetracker.expense.service.ExpenseService;
import com.expensetracker.user.entity.User;
import com.expensetracker.user.exception.UserNotFoundException;
import com.expensetracker.user.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class ExpenseServiceImpl implements ExpenseService {
	private final ExpenseRepository expenseRepository;

	private final UserRepository userRepository;

	public ExpenseServiceImpl(ExpenseRepository expenseRepository, UserRepository userRepository) {

		this.expenseRepository = expenseRepository;
		this.userRepository = userRepository;
	}

	private User getLoggedInUser() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		return userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
	}

	@Override
	@Transactional
	public ExpenseResponse addExpense(AddExpenseRequest request) {

		// Get logged-in email
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

		Expense expense = new Expense();
		expense.setTitle(request.getTitle());
		expense.setAmount(request.getAmount());
		expense.setCategory(request.getCategory());
		expense.setPaymentMethod(request.getPaymentMethod());
		expense.setExpenseDate(request.getExpenseDate());

		// ✅ Attach user
		expense.setUser(user);

		System.out.println("Before save...");
		Expense savedExpense = expenseRepository.save(expense);

		System.out.println("Saved Expense ID: " + savedExpense.getId());

		return mapToResponse(savedExpense);
	}

	// Get All Expenses
	@Override
	public List<ExpenseResponse> getAllExpenses() {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

		List<Expense> expenses = expenseRepository.findByUserId(user.getId());

		return expenses.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public void deleteExpense(Long id) {

		// ✅ Get logged-in user email
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

		// ✅ Find expense
		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ExpenseNotFoundException("Expense not found with ID: " + id));

		// ✅ Ownership check
		if (!expense.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("You are not allowed to delete this expense");
		}

		// ✅ Delete only if owner
		expenseRepository.delete(expense);
	}

	@Override
	public ExpenseResponse updateExpense(Long id, AddExpenseRequest request) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ExpenseNotFoundException("Expense not found with ID: " + id));

		// ✅ Ownership check
		if (!expense.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("You are not allowed to update this expense");
		}

		expense.setTitle(request.getTitle());
		expense.setAmount(request.getAmount());
		expense.setCategory(request.getCategory());
		expense.setPaymentMethod(request.getPaymentMethod() );
		expense.setExpenseDate(request.getExpenseDate());

		Expense updatedExpense = expenseRepository.save(expense);

		return mapToResponse(updatedExpense);
	}

	@Override
	public ExpenseResponse getExpenseById(Long id) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

		Expense expense = expenseRepository.findById(id)
				.orElseThrow(() -> new ExpenseNotFoundException("Expense not found with ID: " + id));

		if (!expense.getUser().getId().equals(user.getId())) {
			throw new AccessDeniedException("You are not allowed to view this expense");
		}

		return mapToResponse(expense);
	}

	@Override
	public Page<ExpenseResponse> getExpenses(int page, int size, String sortBy, String direction) {

		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

		Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

		Pageable pageable = PageRequest.of(page, size, sort);

		return expenseRepository.findByUserId(user.getId(), pageable).map(exp -> new ExpenseResponse(exp.getId(),
				exp.getTitle(), exp.getAmount(), exp.getCategory(),exp.getPaymentMethod(), exp.getExpenseDate()));
	}

	private ExpenseResponse mapToResponse(Expense expense) {

		return new ExpenseResponse(expense.getId(), expense.getTitle(), expense.getAmount(), expense.getCategory(), expense.getPaymentMethod(),
				expense.getExpenseDate());
	}

	@Override
	public List<ExpenseResponse> getExpensesByCategory(String category) {

		// Get logged-in user email from JWT
		String email = SecurityContextHolder.getContext().getAuthentication().getName();

		// Fetch logged-in user
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));

		// Fetch expenses only for that user + category
		List<Expense> expenses = expenseRepository.findByUserIdAndCategory(user.getId(), category);

		// Convert Entity → DTO and return
		return expenses.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public List<ExpenseResponse> getExpensesBetweenDates(LocalDate start, LocalDate end) {
		User user = getLoggedInUser();

		return expenseRepository.findByUserIdAndExpenseDateBetween(user.getId(), start, end).stream()
				.map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public List<ExpenseResponse> getWeeklyExpenses() {
		User user = getLoggedInUser();

		LocalDate today = LocalDate.now();
		LocalDate weekStart = today.minusDays(6);

		return expenseRepository.findByUserIdAndExpenseDateBetween(user.getId(), weekStart, today).stream()
				.map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public List<ExpenseResponse> getMonthlyExpenses(int month, int year) {
		User user = getLoggedInUser();

		LocalDate start = LocalDate.of(year, month, 1);
		LocalDate end = start.withDayOfMonth(start.lengthOfMonth());

		return expenseRepository.findByUserIdAndExpenseDateBetween(user.getId(), start, end).stream()
				.map(this::mapToResponse).collect(Collectors.toList());
	}

	@Override
	public List<ExpenseResponse> getTodayExpenses() {
		User user = getLoggedInUser();

		LocalDate today = LocalDate.now();

		return expenseRepository.findByUserIdAndExpenseDate(user.getId(), today).stream().map(this::mapToResponse)
				.collect(Collectors.toList());
	}

	@Override
	public Double getTotalExpenseForUser() {
		User user = getLoggedInUser();

		Double total = expenseRepository.getTotalExpenseByUserId(user.getId());

		return total != null ? total : 0.0;

	}

	@Override
	public List<CategoryExpenseResponse> getCategoryWiseExpenses() {

		User user = getLoggedInUser();

		return expenseRepository.getCategoryWiseTotal(user.getId());
	}
	
}

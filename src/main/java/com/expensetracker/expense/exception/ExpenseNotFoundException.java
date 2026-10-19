package com.expensetracker.expense.exception;

public class ExpenseNotFoundException extends RuntimeException{

	public ExpenseNotFoundException(String message) {
        super(message);
    }
}

package com.expensetracker.expense.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class AddExpenseRequest {
	 private String title;
	    private Double amount;
	    private String category;
	    private String paymentMethod;
	    private LocalDate expenseDate;
}

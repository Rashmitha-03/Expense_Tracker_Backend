package com.expensetracker.expense.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExpenseResponse {

	private Long id;
	private String title;
	private Double amount;
	private String category;
	private String paymentMethod;
	private LocalDate expenseDate;

}

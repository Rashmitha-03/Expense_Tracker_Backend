package com.expensetracker.expense.dto;

public class CategoryExpenseResponse {
	 private String category;
	    private Double totalAmount;

	    public CategoryExpenseResponse(String category, Double totalAmount) {
	        this.category = category;
	        this.totalAmount = totalAmount;
	    }

	    public String getCategory() {
	        return category;
	    }

	    public Double getTotalAmount() {
	        return totalAmount;
	    }
}

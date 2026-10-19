package com.expensetracker.expense.entity;

import java.time.LocalDate;

import com.expensetracker.user.entity.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "expenses")
@Data
public class Expense {

	 	@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;

	    private String title;

	    private Double amount;

	    private String category;

	    private String paymentMethod;
	    private LocalDate expenseDate;
	    
	    @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

}

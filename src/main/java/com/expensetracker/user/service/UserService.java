package com.expensetracker.user.service;

import com.expensetracker.user.dto.LoginRequest;
import com.expensetracker.user.dto.LoginResponse;
import com.expensetracker.user.dto.RegisterRequest;
import com.expensetracker.user.dto.RegisterResponse;

public interface UserService {
	public RegisterResponse registerUser(RegisterRequest request);

	LoginResponse loginUser(LoginRequest request);
	

}

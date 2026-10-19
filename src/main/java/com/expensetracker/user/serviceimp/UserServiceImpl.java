package com.expensetracker.user.serviceimp;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.expensetracker.user.dto.LoginRequest;
import com.expensetracker.user.dto.LoginResponse;
import com.expensetracker.user.dto.RegisterRequest;
import com.expensetracker.user.dto.RegisterResponse;
import com.expensetracker.user.entity.User;
import com.expensetracker.user.exception.EmailAlreadyExistsException;
import com.expensetracker.user.exception.InvalidCredentialsException;
import com.expensetracker.user.repository.UserRepository;
import com.expensetracker.user.service.UserService;
import com.expensetracker.util.JwtUtil;

@Service
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtUtil jwtUtil;

	public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {

		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public RegisterResponse registerUser(RegisterRequest request) {
		if (userRepository.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("Email already registered!");

		}

		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setAge(request.getAge());
		User savedUser = userRepository.save(user);

		return new RegisterResponse(savedUser.getId(), savedUser.getName(), savedUser.getEmail(),savedUser.getAge());
	}

	@Override
	public LoginResponse loginUser(LoginRequest request) {

		User user = userRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));

		if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new InvalidCredentialsException("Invalid password");
		}

		String token = jwtUtil.generateToken(user.getEmail());

		return new LoginResponse(user.getId(), user.getName(), user.getEmail(), "Login successful!", token

		);

	}

}

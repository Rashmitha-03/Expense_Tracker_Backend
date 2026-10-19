package com.expensetracker.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
	private Long id;
    private String name;
    private String email;
    private String message;
    private String token;
    
    


}

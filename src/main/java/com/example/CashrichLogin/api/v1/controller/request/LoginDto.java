package com.example.CashrichLogin.api.v1.controller.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class LoginDto {

	@NotBlank(message = "Username is required")
	@Pattern(regexp = "^[a-zA-Z0-9]{4,15}$", message = "Username must be between 4 and 15 characters long and can only contain letters and digits")
	private String username;
	
	@NotBlank(message = "Password is required")
	@Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$", message = "Password must be between 8 and 15 characters long and contain at least one letter, one digit, and one special character")
	private String password;
}

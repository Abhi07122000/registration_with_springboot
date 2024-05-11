package com.example.CashrichLogin.api.v1.controller.request;

import com.example.CashrichLogin.service.ValidationGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignupDto {

	@NotBlank(groups = ValidationGroup.class, message = "First name is required")
	@Size(max = 50, message = "First name must be at most 50 characters long")
	private String firstName;

	@NotBlank(groups = ValidationGroup.class, message = "Last name is required")
	@Size(max = 50, groups = ValidationGroup.class, message = "Last name must be at most 50 characters long")
	private String lastName;

	@NotBlank(groups = ValidationGroup.class, message = "Email is required")
	@Size(max = 50, groups = ValidationGroup.class, message = "Email must be at most 50 characters long")
	@Email(groups = ValidationGroup.class, message = "Invalid email format")
	private String email;

	@NotBlank(groups = ValidationGroup.class, message = "Username is required")
	@Pattern(regexp = "^[a-zA-Z0-9]{4,15}$", groups = ValidationGroup.class, message = "Username must be between 4 and 15 characters long and can only contain letters and digits")
	private String username;

	@NotBlank(groups = ValidationGroup.class, message = "Password is required")
	@Pattern(regexp="^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$", groups = ValidationGroup.class, message = "Password must be between 8 and 15 characters long and contain at least one letter, one digit, and one special character")
	private String password;

	@Size(min = 10, max = 10, message = "Mobile number must be 10 digits long", groups = ValidationGroup.class)
	@Pattern(regexp="^[0-9]*$", message = "Mobile number must contain only digits", groups = ValidationGroup.class)
	private String mobileNo;
    
}

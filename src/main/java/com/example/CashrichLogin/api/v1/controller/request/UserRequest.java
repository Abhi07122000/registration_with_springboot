package com.example.CashrichLogin.api.v1.controller.request;

import com.example.CashrichLogin.service.SignUpValidationGroup;
import com.example.CashrichLogin.service.UpdationValidationGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequest {

	@NotBlank(groups = { SignUpValidationGroup.class,
			UpdationValidationGroup.class }, message = "First name is required")
	@Size(max = 50, groups = { SignUpValidationGroup.class,
			UpdationValidationGroup.class }, message = "First name must be at most 50 characters long")
	private String firstName;

	@NotBlank(groups = { SignUpValidationGroup.class,
			UpdationValidationGroup.class }, message = "Last name is required")
	@Size(max = 50, groups = { SignUpValidationGroup.class,
			UpdationValidationGroup.class }, message = "Last name must be at most 50 characters long")
	private String lastName;

	@NotBlank(groups = SignUpValidationGroup.class, message = "Email is required")
	@Size(max = 50, groups = SignUpValidationGroup.class, message = "Email must be at most 50 characters long")
	@Email(groups = SignUpValidationGroup.class, message = "Invalid email format")
	private String email;

	@NotBlank(groups = { SignUpValidationGroup.class, UpdationValidationGroup.class }, message = "Username is required")
	@Pattern(regexp = "^[a-zA-Z0-9]{4,15}$", groups = { SignUpValidationGroup.class,
			UpdationValidationGroup.class }, message = "Username must be between 4 and 15 characters long and can only contain letters and digits")
	private String username;

	@NotBlank(groups = { SignUpValidationGroup.class, UpdationValidationGroup.class }, message = "Password is required")
	@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,15}$", groups = {
			SignUpValidationGroup.class,
			UpdationValidationGroup.class }, message = "Password must be between 8 and 15 characters long and contain at least one letter, one digit, and one special character")
	private String password;

	@NotBlank(groups = { SignUpValidationGroup.class, UpdationValidationGroup.class }, message = "Mobile No. is required")
	@Size(min = 10, max = 10, message = "Mobile number must be 10 digits long", groups = { SignUpValidationGroup.class,
			UpdationValidationGroup.class })
	@Pattern(regexp = "^[0-9]*$", message = "Mobile number must contain only digits", groups = {
			SignUpValidationGroup.class, UpdationValidationGroup.class })
	private String mobileNo;

	@NotBlank(groups = SignUpValidationGroup.class, message = "PAN Number is required")
	@Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]{1}", groups = SignUpValidationGroup.class, message = "Pan Number must be in the format: 5 uppercase letters, followed by 4 digits, and 1 uppercase letter")
	private String panNumber;

}

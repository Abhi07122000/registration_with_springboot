package com.example.CashrichLogin.api.v1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.CashrichLogin.api.v1.controller.request.LoginDto;
import com.example.CashrichLogin.api.v1.controller.request.SignupDto;
import com.example.CashrichLogin.api.v1.controller.request.UpdationDto;
import com.example.CashrichLogin.domain.User;
import com.example.CashrichLogin.service.UsersServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UsersServiceImpl userServiceImpl;

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody @Valid SignupDto signupDto) {
		User user = userServiceImpl.signUp(signupDto);
	    if (user != null) {
	        return ResponseEntity.ok("User signed up successfully");
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to sign up user");
	    }
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginDto loginDto) {
		return ResponseEntity.ok(userServiceImpl.validateUserAndLogin(loginDto));

	}

	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody @Valid UpdationDto updationDto) {
		if (!userServiceImpl.validateToken(updationDto.getToken())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
		}
		return ResponseEntity.ok(userServiceImpl.updateUser(updationDto));
	}
}

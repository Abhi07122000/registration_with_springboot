package com.example.CashrichLogin.api.v1.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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
import com.example.CashrichLogin.api.v1.controller.response.LoginResponse;
import com.example.CashrichLogin.api.v1.controller.response.ResponseEnvelope;
import com.example.CashrichLogin.domain.User;
import com.example.CashrichLogin.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserService userServiceImpl;

	@PostMapping("/signup")
	public ResponseEntity<?> signUp(@RequestBody SignupDto signupDto) {
		Map<String, Object> violations = userServiceImpl.performValidation(signupDto);
		if (violations.isEmpty()) {
			ResponseEnvelope response = userServiceImpl.signUp(signupDto);
			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.ok(new ResponseEnvelope(HttpStatus.BAD_REQUEST.value(),
					HttpStatus.BAD_REQUEST.getReasonPhrase(), violations));
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid LoginDto loginDto, HttpServletResponse response) {
		Optional<User> user = userServiceImpl.validateUserName(loginDto);
		if (!user.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ResponseEnvelope(HttpStatus.NOT_FOUND.value(), "User Not Found", null));
		}
		if (!userServiceImpl.validatePassword(loginDto, user.get())) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ResponseEnvelope(
					HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), "Invalid Password"));
		}
		LoginResponse loginResponse = userServiceImpl.validateUserAndLogin(loginDto, user.get());
		Cookie jwtCookie = new Cookie("token", loginResponse.getToken());
		jwtCookie.setHttpOnly(true);
		jwtCookie.setPath("/");
		response.addCookie(jwtCookie);
		return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
				.body(new ResponseEnvelope(HttpStatus.OK.value(), loginResponse.getMessage(), null));
	}
	
	/*
	 * Add new Field customerPan
	 * /
	 */

	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody @Valid UpdationDto updationDto) {
		return ResponseEntity.ok(userServiceImpl.updateUser(updationDto));
	}
}

package com.example.CashrichLogin.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.CashrichLogin.api.v1.controller.request.LoginDto;
import com.example.CashrichLogin.api.v1.controller.request.UserRequest;
import com.example.CashrichLogin.api.v1.controller.response.LoginResponse;
import com.example.CashrichLogin.api.v1.controller.response.ResponseEnvelope;
import com.example.CashrichLogin.api.v1.controller.response.UserProfileDetails;
import com.example.CashrichLogin.domain.User;
import com.example.CashrichLogin.repository.UserRepository;
import com.example.CashrichLogin.security.EncryptionUtil;
import com.example.CashrichLogin.security.JwtUtils;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@Service
public class UsersServiceImpl implements UserService {

	private final UserRepository userRepository;
	
	private final ModelMapper modelMapper;
	
	private final PasswordEncoder passwordEncoder;
	
	private final JwtUtils jwtUtils;
	
	private final AuthenticationManager authenticationManager;
	
	private final Validator validator;
	
    private EncryptionUtil encryptionUtil;

	public UsersServiceImpl(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder,
			JwtUtils jwtUtils, AuthenticationManager authenticationManager, Validator validator, EncryptionUtil encryptionUtil) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
		this.passwordEncoder = passwordEncoder;
		this.jwtUtils = jwtUtils;
		this.authenticationManager = authenticationManager;
		this.validator = validator;
		this.encryptionUtil = encryptionUtil;
	}

	public ResponseEnvelope signUp(UserRequest signupDto) throws Exception {
		User user = modelMapper.map(signupDto, User.class);
		user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
		user.setActive(true);
        user.setPanNumber(encryptionUtil.encrypt(signupDto.getPanNumber()));
		try {
			userRepository.save(user);
			return new ResponseEnvelope(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
					"User signed up successfully");
		} catch (DataIntegrityViolationException e) {
			return new ResponseEnvelope(HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
					"User with this email and username already exists");
		} catch (Exception e) {
			return new ResponseEnvelope(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), "Failed to sign up user");
		}
	}

	public Optional<User> validateUserName(LoginDto loginDto) {
		return userRepository.findByUsername(loginDto.getUsername());
	}

	public boolean validatePassword(LoginDto loginDto, User user) {
		return passwordEncoder.matches(loginDto.getPassword(), user.getPassword());
	}

	public LoginResponse validateUserAndLogin(LoginDto loginDto, User user) {
		Authentication authentication = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String token = jwtUtils.generateToken(authentication, user.getId());
		return new LoginResponse(token, "Login Successful");
	}

	public boolean validateToken(String token) {
		return jwtUtils.validateToken(token);
	}

	public ResponseEnvelope updateUser(UserRequest updateUserDto) {
		Optional<User> userOptional = userRepository.findByUsername(updateUserDto.getUsername());
		if (userOptional.isPresent()) {
			User user = userOptional.get();
			if (updateUserDto.getFirstName() != null) {
				user.setFirstName(updateUserDto.getFirstName());
			}
			if (updateUserDto.getLastName() != null) {
				user.setLastName(updateUserDto.getLastName());
			}
			if (updateUserDto.getMobileNo() != null) {
				user.setMobileNo(updateUserDto.getMobileNo());
			}
			if (updateUserDto.getPassword() != null) {
				user.setPassword(passwordEncoder.encode(updateUserDto.getPassword()));
			}
			userRepository.save(user);
			return new ResponseEnvelope(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
					"User data updated successfully");
		} else {
			return new ResponseEnvelope(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
					"User not found");
		}
	}

	public Map<String, Object> performSignUpValidation(UserRequest signupDto) {
		Set<ConstraintViolation<UserRequest>> violations = validator.validate(signupDto, SignUpValidationGroup.class);
		if (!violations.isEmpty()) {
			Map<String, Object> errorMap = new HashMap<>();
			for (ConstraintViolation<UserRequest> violation : violations) {
				errorMap.put(violation.getPropertyPath().toString(), violation.getMessage());
			}
			return errorMap;
		}
		return Collections.emptyMap();
	}

	@Override
	public Map<String, Object> performUpdationValidation(UserRequest signupDto) {
		Set<ConstraintViolation<UserRequest>> violations = validator.validate(signupDto, UpdationValidationGroup.class);
		if (!violations.isEmpty()) {
			Map<String, Object> errorMap = new HashMap<>();
			for (ConstraintViolation<UserRequest> violation : violations) {
				errorMap.put(violation.getPropertyPath().toString(), violation.getMessage());
			}
			return errorMap;
		}
		return Collections.emptyMap();
	}

	@Override
	public UserProfileDetails getUserProfile(String token) throws Exception {
		UserProfileDetails response = new UserProfileDetails();
		Optional<User> optionalUser = userRepository.findById(jwtUtils.extractUserId(token));
		if(optionalUser.isPresent()) {
			User user = optionalUser.get();
			response.setFirstName(user.getFirstName());
			response.setLastName(user.getLastName());
			response.setMobileNo(user.getMobileNo());
			response.setEmail(user.getEmail());
			response.setPanNumber(encryptionUtil.decrypt(user.getPanNumber()));
			return response;
		}
		return null;
	}

}

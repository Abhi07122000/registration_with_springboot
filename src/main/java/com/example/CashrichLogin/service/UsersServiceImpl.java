package com.example.CashrichLogin.service;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.CashrichLogin.api.v1.controller.request.LoginDto;
import com.example.CashrichLogin.api.v1.controller.request.SignupDto;
import com.example.CashrichLogin.api.v1.controller.request.UpdationDto;
import com.example.CashrichLogin.api.v1.controller.response.ResponseEnvelope;
import com.example.CashrichLogin.domain.User;
import com.example.CashrichLogin.repository.UserRepository;
import com.example.CashrichLogin.security.JwtUtils;

@Service
public class UsersServiceImpl {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtUtils jwtUtils;

	@Autowired
	private AuthenticationManager authenticationManager;

	public User signUp(SignupDto signupDto) {
		User user = modelMapper.map(signupDto, User.class);
		user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
		user.setActive(true);
		return userRepository.save(user);
	}

	public ResponseEnvelope validateUserAndLogin(LoginDto loginDto) {
		Optional<User> user = userRepository.findByUsername(loginDto.getUsername());
		if (!user.isPresent()) {
			return new ResponseEnvelope(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
					"User Not Found");
		} else {
			User cur = user.get();
			boolean flag = passwordEncoder.matches(loginDto.getPassword(), cur.getPassword());
			if(!flag) {
				return new ResponseEnvelope(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(),
						"Invalid Password");
			}
			Authentication authentication = authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			String token = jwtUtils.generateToken(authentication, cur.getId());
			return new ResponseEnvelope(HttpStatus.OK.value(), "Login Successful",
					token);
		}
	}
	
	public boolean validateToken(String token) {
		return jwtUtils.validateToken(token);
	}

	public ResponseEnvelope updateUser(UpdationDto updateUserDto) {
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
}

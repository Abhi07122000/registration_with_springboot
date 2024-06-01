package com.example.CashrichLogin.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.CashrichLogin.api.v1.controller.request.LoginDto;
import com.example.CashrichLogin.api.v1.controller.request.UserRequest;
import com.example.CashrichLogin.api.v1.controller.response.LoginResponse;
import com.example.CashrichLogin.api.v1.controller.response.ResponseEnvelope;
import com.example.CashrichLogin.api.v1.controller.response.UserProfileDetails;
import com.example.CashrichLogin.domain.User;

@Service
public interface UserService {

	ResponseEnvelope signUp(UserRequest signupDto) throws Exception;

    Optional<User> validateUserName(LoginDto loginDto);

    boolean validatePassword(LoginDto loginDto, User user);

    LoginResponse validateUserAndLogin(LoginDto loginDto, User user);

    boolean validateToken(String token);

    ResponseEnvelope updateUser(UserRequest updateUserDto);

    Map<String, Object> performSignUpValidation(UserRequest signupDto);
    
    Map<String, Object> performUpdationValidation(UserRequest signupDto);

    UserProfileDetails getUserProfile(String string) throws Exception;
    
}

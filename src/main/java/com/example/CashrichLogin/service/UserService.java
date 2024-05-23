package com.example.CashrichLogin.service;

import java.util.Map;
import java.util.Optional;

import com.example.CashrichLogin.api.v1.controller.request.LoginDto;
import com.example.CashrichLogin.api.v1.controller.request.SignupDto;
import com.example.CashrichLogin.api.v1.controller.request.UpdationDto;
import com.example.CashrichLogin.api.v1.controller.response.LoginResponse;
import com.example.CashrichLogin.api.v1.controller.response.ResponseEnvelope;
import com.example.CashrichLogin.domain.User;

public interface UserService {

	ResponseEnvelope signUp(SignupDto signupDto);

    Optional<User> validateUserName(LoginDto loginDto);

    boolean validatePassword(LoginDto loginDto, User user);

    LoginResponse validateUserAndLogin(LoginDto loginDto, User user);

    boolean validateToken(String token);

    ResponseEnvelope updateUser(UpdationDto updateUserDto);

    Map<String, Object> performValidation(SignupDto signupDto);
    
}

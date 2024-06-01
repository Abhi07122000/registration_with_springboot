package com.example.CashrichLogin.api.v1.controller.response;

import lombok.Data;

@Data
public class UserProfileDetails {

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;
    
    private String panNumber;
    
}

package com.example.CashrichLogin.api.v1.controller.response;

import java.time.LocalDateTime;

import lombok.Data;


@Data
public class UserCryptoResponse {

    private Long userId;
    
    private String cryptoDetails;
    
    private LocalDateTime createdDate;	
}

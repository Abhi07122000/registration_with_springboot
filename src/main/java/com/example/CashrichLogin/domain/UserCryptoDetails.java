package com.example.CashrichLogin.domain;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_crypto_details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCryptoDetails {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    
    private String name;
    
    private String symbol;

    private String numMarketPairs;

    private String maxSupply;
    
    private String price;

    private LocalDateTime createdDate=LocalDateTime.now();	
}

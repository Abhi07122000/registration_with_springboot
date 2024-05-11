package com.example.CashrichLogin.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
public class PropertiesConfig {

	@Value("${userCryptoUrl}")
	private String userCryptoUrl;

}

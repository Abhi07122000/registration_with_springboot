package com.example.CashrichLogin.service;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.CashrichLogin.api.v1.controller.response.UserCryptoResponse;
import com.example.CashrichLogin.config.PropertiesConfig;
import com.example.CashrichLogin.domain.UserCryptoDetails;
import com.example.CashrichLogin.exception.BusinessException;
import com.example.CashrichLogin.repository.UserCryptoDetailsRepo;
import com.example.CashrichLogin.security.JwtUtils;

@Service
public class UserCryptoDetailsService {

	private RestTemplate restTemplate;

	private UserCryptoDetailsRepo cryptoDetailsRepo;

	private JwtUtils jwtUtils;

	private ModelMapper modelMapper;
	
	private PropertiesConfig propertiesConfig;

	public UserCryptoDetailsService(RestTemplate restTemplate, UserCryptoDetailsRepo cryptoDetailsRepo,
			JwtUtils jwtUtils, ModelMapper modelMapper,PropertiesConfig propertiesConfig) {
		this.restTemplate = restTemplate;
		this.cryptoDetailsRepo = cryptoDetailsRepo;
		this.jwtUtils = jwtUtils;
		this.modelMapper = modelMapper;
		this.propertiesConfig=propertiesConfig;
	}

	public UserCryptoResponse getLatestCryptoData(String token) {
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-CMC_PRO_API_KEY", "27ab17d1-215f-49e5-9ca4-afd48810c149");

		HttpEntity<String> entity = new HttpEntity<>(headers);
		UserCryptoResponse objCryptoResponse = new UserCryptoResponse();

		try {
			ResponseEntity<String> responseEntity = restTemplate.exchange(
					propertiesConfig.getUserCryptoUrl(),
					HttpMethod.GET, entity, String.class);

			String crypto = responseEntity.getBody();
			UserCryptoDetails response = new UserCryptoDetails();

			response.setCryptoDetails(crypto);
			response.setUserId(jwtUtils.extractUserId(token));
			objCryptoResponse = modelMapper.map(response, UserCryptoResponse.class);
			cryptoDetailsRepo.save(response);

		} catch (Exception e) {

			
			 throw new BusinessException(e.getLocalizedMessage());
		}

		return objCryptoResponse;
	}
}

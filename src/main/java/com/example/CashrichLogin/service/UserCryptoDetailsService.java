package com.example.CashrichLogin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.example.CashrichLogin.api.v1.controller.response.ResponseEnvelope;
import com.example.CashrichLogin.api.v1.controller.response.UserCryptoResponse;
import com.example.CashrichLogin.config.PropertiesConfig;
import com.example.CashrichLogin.domain.UserCryptoDetails;
import com.example.CashrichLogin.exception.BusinessException;
import com.example.CashrichLogin.repository.UserCryptoDetailsRepo;
import com.example.CashrichLogin.security.JwtUtils;
import com.fasterxml.jackson.databind.JsonNode;

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

	public ResponseEnvelope getLatestCryptoData(String token, String symbol) {
		List<UserCryptoResponse> cryptoResultList = new ArrayList<>();
		HttpHeaders headers = new HttpHeaders();
		headers.set("X-CMC_PRO_API_KEY", "27ab17d1-215f-49e5-9ca4-afd48810c149");
		HttpEntity<String> entity = new HttpEntity<>(headers);
		UserCryptoResponse objCryptoResponse = new UserCryptoResponse();
		String apiUrl = propertiesConfig.getUserCryptoUrl();
		String url = UriComponentsBuilder.fromHttpUrl(apiUrl)
				.queryParam("symbol", symbol)
				.encode()
		        .toUriString();
	    String[] symbolsList = symbol.split(",");
		try {
			ResponseEntity<JsonNode> responseEntity = restTemplate.exchange(
					url,
					HttpMethod.GET, entity, JsonNode.class);
			if(responseEntity.getBody() != null) {
				JsonNode cryptoData = responseEntity.getBody().get("data");
				if (cryptoData != null) {
				    for(int i = 0; i < symbolsList.length; i++) {
						JsonNode crypto = cryptoData.get(symbolsList[i]);
					    UserCryptoDetails response = new UserCryptoDetails();
					    JsonNode nameNode = crypto.get("name");
					    response.setName(nameNode != null ? nameNode.asText() : null);
					    JsonNode symbolNode = crypto.get("symbol");
					    response.setSymbol(symbolNode != null ? symbolNode.asText() : null);
					    JsonNode maxSupplyNode = crypto.get("max_supply");
					    response.setMaxSupply(maxSupplyNode != null ? maxSupplyNode.asText() : null);
					    JsonNode numMarketPairsNode = crypto.get("num_market_pairs");
					    response.setNumMarketPairs(numMarketPairsNode != null ? numMarketPairsNode.asText() : null);
					    response.setUserId(jwtUtils.extractUserId(token));
					    JsonNode quoteObjectNode = crypto.get("quote");
					    if(quoteObjectNode.get("USD") != null) {
					    	response.setPrice(quoteObjectNode.get("USD").get("price").asText());
					    }
					    cryptoDetailsRepo.save(response);
					    objCryptoResponse = modelMapper.map(response, UserCryptoResponse.class);
					    cryptoResultList.add(objCryptoResponse);
				    }
				}
				return new ResponseEnvelope(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
						cryptoResultList);
			}
		} catch (Exception e) {
			 throw new BusinessException(e.getLocalizedMessage());
		}
		return new ResponseEnvelope(HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
				"User Not Found");
	}
}

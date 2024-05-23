package com.example.CashrichLogin.api.v1.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.CashrichLogin.service.UserCryptoDetailsService;

@Controller
@RequestMapping("crypto-data")
public class UserCryptoDetailsController {

	@Autowired
	UserCryptoDetailsService cryptoDetailsService;
	
	@GetMapping("/by-user")
	public ResponseEntity<?> getCryptoData(@RequestHeader Map<String, String> headers, @RequestParam(name = "symbol") String symbol) {
	    return ResponseEntity.ok(cryptoDetailsService.getLatestCryptoData(headers.get("authorization"), symbol));
	}
	
}

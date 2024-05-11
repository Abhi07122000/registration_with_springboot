package com.example.CashrichLogin.api.v1.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseEnvelope {

	private Integer statusCode;
	
	private String statusMessage;
	
	private Object data;
}

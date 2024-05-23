package com.example.CashrichLogin.api.v1.advice;

import java.io.IOException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.CashrichLogin.api.v1.controller.response.ResponseEnvelope;
import com.example.CashrichLogin.exception.BusinessException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {

	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private static final int RANDOM_LOWER = 1000;
	private static final int RANDOM_UPPER = 9000;

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ResponseEnvelope> handleValidationExceptions(HttpServletRequest request,
			HttpServletResponse response, MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		String errorId = getErrorId();
		ResponseEnvelope exceptionReponse = new ResponseEnvelope(ex.getStatusCode().value(), errorId, errors);
		response.setStatus(ex.getStatusCode().value());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exceptionReponse);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ResponseEnvelope> handleBusinessExceptionExceptionError(HttpServletRequest request,
			HttpServletResponse response, BusinessException ex) {
		generateLogs(request, response, ex, null);
		ResponseEnvelope exceptionReponse = new ResponseEnvelope(ex.getCode(), ex.getMessage(), null);
		response.setStatus(ex.getCode());
		return new ResponseEntity<ResponseEnvelope>(exceptionReponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ResponseEnvelope> handleGeneralError(HttpServletRequest request, HttpServletResponse response,
			Exception ex) {
		String errorId = getErrorId();
		generateLogs(request, response, ex, errorId);
		ResponseEnvelope exceptionReponse = new ResponseEnvelope(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorId,
				null);
		return new ResponseEntity<ResponseEnvelope>(exceptionReponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	public String getErrorId() {
		return "CashRich " + formatter.format(LocalDateTime.now())
				+ (new SecureRandom().nextInt(RANDOM_UPPER) + RANDOM_LOWER);
	}

	private String generateLogs(HttpServletRequest request, HttpServletResponse response, Exception ex,
			String errorId) {

		StringBuilder result = new StringBuilder("ERROR ID IS: " + errorId + " \n");
		result.append("### URL : (" + request.getMethod() + ") " + request.getRequestURI()
				+ (request.getQueryString() != null ? "?" + request.getQueryString() : "") + "\n");

		if (StringUtils.equalsAnyIgnoreCase(request.getMethod(), "POST", "PUT")) {
			try {
				result.append("### POST Data : "
						+ request.getReader().lines().collect(Collectors.joining(System.lineSeparator())) + "\n");
			} catch (IOException e1) {
			}
		}
		result.append("### Exception error : " + ExceptionUtils.getStackTrace(ex) + "\n");
		log.error(result.toString());
		return errorId;
	}
}

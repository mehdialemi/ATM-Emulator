package com.alemi.bank.common;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BankException.class)
	public ResponseEntity<String> walletException(BankException ex) {
		ErrorResponse errorResponse = ex.getErrorResponse();
		HttpStatus httpStatus = HttpStatus.resolve(errorResponse.getHttpCode());
		return new ResponseEntity <>(errorResponse.getMessage(), httpStatus);
	}
}

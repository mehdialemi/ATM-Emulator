package com.alemi.common.configs;

import com.alemi.common.exceptions.AtmException;
import com.alemi.common.exceptions.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(AtmException.class)
	public ResponseEntity<String> atmException(AtmException ex) {
		ErrorResponse errorResponse = ex.getErrorResponse();
		HttpStatus httpStatus = HttpStatus.resolve(errorResponse.getHttpCode());
		return new ResponseEntity <>(errorResponse.getMessage(), httpStatus);
	}
}

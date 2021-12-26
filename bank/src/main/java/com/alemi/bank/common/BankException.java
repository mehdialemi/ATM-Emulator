package com.alemi.bank.common;

public class BankException extends Exception {

	private ErrorResponse errorResponse;

	public BankException(String message, int code) {
		errorResponse = new ErrorResponse();
		errorResponse.setMessage(message);
		errorResponse.setHttpCode(code);
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
}

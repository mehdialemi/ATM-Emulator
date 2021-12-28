package com.alemi.common.exceptions;

public class AtmException extends Exception {

	private ErrorResponse errorResponse;

	public AtmException(String message, int code) {
		errorResponse = new ErrorResponse();
		errorResponse.setMessage(message);
		errorResponse.setHttpCode(code);
	}

	public ErrorResponse getErrorResponse() {
		return errorResponse;
	}
}

package com.alemi.common.exceptions;

import lombok.Data;

@Data
public class ErrorResponse {
	private String message;
	private int httpCode;
}

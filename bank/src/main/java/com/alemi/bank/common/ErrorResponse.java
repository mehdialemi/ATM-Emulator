package com.alemi.bank.common;

import lombok.Data;

@Data
public class ErrorResponse {
	private String message;
	private int httpCode;
}

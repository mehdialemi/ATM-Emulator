package com.alemi.bank.exceptions.auth;

import com.alemi.common.exceptions.AtmException;

public class AuthOptionException extends AtmException {

	public final static int ErrorCode = 2001;

	public AuthOptionException(String message) {
		super(message, ErrorCode);
	}
}

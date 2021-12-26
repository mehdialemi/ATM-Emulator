package com.alemi.bank.auth.exceptions;

import com.alemi.bank.common.BankException;

public class AuthOptionException extends BankException {

	public final static int ErrorCode = 2001;

	public AuthOptionException(String message) {
		super(message, ErrorCode);
	}
}

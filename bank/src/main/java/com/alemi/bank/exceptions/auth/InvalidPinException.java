package com.alemi.bank.exceptions.auth;

import com.alemi.common.exceptions.AtmException;

public class InvalidPinException extends AtmException {
	public final static int ErrorCode = 2003;

	public InvalidPinException(int pin) {
		super("Invalid pin: " + pin, ErrorCode);
	}
}

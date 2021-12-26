package com.alemi.bank.auth.exceptions;

import com.alemi.bank.common.BankException;

public class InvalidPinException extends BankException {
	public final static int ErrorCode = 2003;

	public InvalidPinException(int pin) {
		super("Invalid pin: " + pin, ErrorCode);
	}
}

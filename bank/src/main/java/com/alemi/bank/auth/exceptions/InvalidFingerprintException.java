package com.alemi.bank.auth.exceptions;

import com.alemi.bank.common.BankException;

public class InvalidFingerprintException extends BankException {
	public final static int ErrorCode = 2004;

	public InvalidFingerprintException(String fingerprint) {
		super("Invalid fingerprint: " + fingerprint, ErrorCode);
	}
}

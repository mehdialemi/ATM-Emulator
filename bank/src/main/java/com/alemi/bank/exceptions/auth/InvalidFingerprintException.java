package com.alemi.bank.exceptions.auth;

import com.alemi.common.exceptions.AtmException;

public class InvalidFingerprintException extends AtmException {
	public final static int ErrorCode = 2004;

	public InvalidFingerprintException(String fingerprint) {
		super("Invalid fingerprint: " + fingerprint, ErrorCode);
	}
}

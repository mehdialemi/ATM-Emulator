package com.alemi.bank.exceptions.card;

import com.alemi.common.exceptions.AtmException;

public class DuplicatedCardException extends AtmException {

	public final static int ErrorCode = 1002;

	public DuplicatedCardException(String cardNumber) {
		super("Already exist card number: " + cardNumber, ErrorCode);
	}
}

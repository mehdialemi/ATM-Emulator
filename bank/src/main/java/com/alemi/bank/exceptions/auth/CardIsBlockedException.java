package com.alemi.bank.exceptions.auth;

import com.alemi.common.exceptions.AtmException;

public class CardIsBlockedException extends AtmException {
	public final static int ErrorCode = 2002;

	public CardIsBlockedException(String cardNumber) {
		super("Card is blocked: " + cardNumber, ErrorCode);
	}
}

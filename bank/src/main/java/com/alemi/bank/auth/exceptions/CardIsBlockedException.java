package com.alemi.bank.auth.exceptions;

import com.alemi.bank.common.BankException;

public class CardIsBlockedException extends BankException {
	public final static int ErrorCode = 2002;

	public CardIsBlockedException(String cardNumber) {
		super("Card is blocked: " + cardNumber, ErrorCode);
	}
}

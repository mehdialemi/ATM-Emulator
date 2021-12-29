package com.alemi.bank.exceptions.card;

import com.alemi.common.exceptions.AtmException;

public class CardNotFountException extends AtmException {

	public final static int ErrorCode = 1001;

	public CardNotFountException(String cardNumber) {
		super("Not found card cardNumber: " + cardNumber, ErrorCode);
	}
}

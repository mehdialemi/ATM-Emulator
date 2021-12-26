package com.alemi.bank.card.exceptions;

import com.alemi.bank.common.BankException;

public class CardNotFountException extends BankException {

	public final static int ErrorCode = 1001;

	public CardNotFountException(String cardNumber) {
		super("Not found card number: " + cardNumber, ErrorCode);
	}
}

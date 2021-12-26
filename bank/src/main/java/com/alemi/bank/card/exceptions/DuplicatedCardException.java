package com.alemi.bank.card.exceptions;

import com.alemi.bank.common.BankException;

public class DuplicatedCardException extends BankException {

	public final static int ErrorCode = 1002;

	public DuplicatedCardException(String cardNumber) {
		super("Already exist card number: " + cardNumber, ErrorCode);
	}
}

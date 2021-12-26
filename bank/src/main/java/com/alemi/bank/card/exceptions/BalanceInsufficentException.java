package com.alemi.bank.card.exceptions;

import com.alemi.bank.common.BankException;

import java.math.BigDecimal;

public class BalanceInsufficentException extends BankException {

	public final static int ErrorCode = 1003;

	public BalanceInsufficentException(String cardNumber, BigDecimal requestAmount) {
		super("Balance insufficient, card number: " + cardNumber + ", requested: " + requestAmount, ErrorCode);
	}
}

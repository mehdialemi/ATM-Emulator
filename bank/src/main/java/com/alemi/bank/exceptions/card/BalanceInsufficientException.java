package com.alemi.bank.exceptions.card;

import com.alemi.common.exceptions.AtmException;

import java.math.BigDecimal;

public class BalanceInsufficientException extends AtmException {

	public final static int ErrorCode = 1003;

	public BalanceInsufficientException(String cardNumber, BigDecimal requestAmount) {
		super("Balance insufficient, card cardNumber: " + cardNumber + ", requested: " + requestAmount, ErrorCode);
	}
}

package com.alemi.bank.card.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardResponse {
	private String cardNumber;
	private BigDecimal amount;
	private OperationType operationType;
}

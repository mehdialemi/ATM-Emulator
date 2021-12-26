package com.alemi.bank.card.models;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CardOperationRequest {
	private String cardNumber;
	private double amount;
}

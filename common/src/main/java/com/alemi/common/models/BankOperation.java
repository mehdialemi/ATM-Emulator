package com.alemi.common.models;

import lombok.Data;

@Data
public class BankOperation {
	private String cardNumber;
	private double amount;
	private OperationType operationType;
}

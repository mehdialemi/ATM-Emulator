package com.alemi.atm.deposit;

import lombok.Data;

@Data
public class DepositRequest {
	private String cardNumber;
	private double amount;
}

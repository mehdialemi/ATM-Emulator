package com.alemi.atm.controller;

import lombok.Data;

@Data
public class AtmRequest {
	private String cardNumber;
	private double amount;
}

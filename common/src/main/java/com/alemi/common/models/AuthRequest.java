package com.alemi.common.models;

import lombok.Data;

@Data
public class AuthRequest {
	private String cardNumber = "";
	private int pinCode;
	private String fingerprint = "";
	private CardAuthOption option = CardAuthOption.PIN;
}

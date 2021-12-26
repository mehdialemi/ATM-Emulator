package com.alemi.bank.auth.models;

import com.alemi.bank.card.models.CardAuthOption;
import lombok.Data;

@Data
public class AuthRequest {
	private String cardNumber = "";
	private int pinCode;
	private String fingerprint = "";
	private CardAuthOption option = CardAuthOption.PIN;
}

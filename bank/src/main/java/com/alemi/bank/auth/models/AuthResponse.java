package com.alemi.bank.auth.models;

import com.alemi.bank.card.models.OperationType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthResponse {
	private String token;
	private List<OperationType> operations = new ArrayList <>();
}

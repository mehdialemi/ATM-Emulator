package com.alemi.common.models;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthResponse {
	private String token;
	private List<OperationType> operations = new ArrayList <>();
}

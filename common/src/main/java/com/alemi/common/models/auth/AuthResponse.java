package com.alemi.common.models.auth;

import com.alemi.common.models.transaction.OperationType;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AuthResponse {
	private String token;
	private List<OperationType> operations = new ArrayList <>();
}

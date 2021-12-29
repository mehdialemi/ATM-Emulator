package com.alemi.common.models.transaction;

import lombok.Data;

@Data
public class BankOperation extends Operation {
	private OperationType operationType;
}

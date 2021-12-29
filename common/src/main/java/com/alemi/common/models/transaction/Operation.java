package com.alemi.common.models.transaction;

import com.alemi.common.models.CardInfo;
import lombok.Data;

@Data
public class Operation extends CardInfo {
	private double amount;
}

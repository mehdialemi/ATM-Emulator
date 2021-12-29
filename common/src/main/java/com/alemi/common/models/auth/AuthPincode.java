package com.alemi.common.models.auth;

import com.alemi.common.models.CardInfo;
import lombok.Data;

@Data
public class AuthPincode extends CardInfo {
	private int code = 0;
}

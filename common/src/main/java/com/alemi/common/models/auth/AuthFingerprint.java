package com.alemi.common.models.auth;

import com.alemi.common.models.CardInfo;
import lombok.Data;

@Data
public class AuthFingerprint extends CardInfo {
	private String pattern = "";
}

package com.alemi.common.models.auth;

import com.alemi.common.models.CardInfo;
import lombok.Data;

@Data
public class AuthRequest extends CardInfo {
	private CardAuthOption option = CardAuthOption.PIN;
	private AuthPincode pin;
	private AuthFingerprint fingerprint;

}

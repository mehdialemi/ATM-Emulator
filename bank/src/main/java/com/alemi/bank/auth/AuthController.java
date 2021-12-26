package com.alemi.bank.auth;

import com.alemi.bank.auth.models.AuthRequest;
import com.alemi.bank.card.exceptions.CardNotFountException;
import com.alemi.bank.card.models.CardAuthOption;
import com.alemi.bank.common.BankException;
import com.alemi.bank.common.BankApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(BankApiPaths.ROOT + BankApiPaths.AUTH)
public class AuthController {

	@Autowired
	private AuthService service;

	@PostMapping(BankApiPaths.LOGIN)
	public void login(@RequestBody AuthRequest request) throws BankException {
		if (request.getOption() == CardAuthOption.PIN) {
			service.loginByPin(request.getCardNumber(), request.getPinCode());
		} else {
			service.loginByFingerprint(request.getCardNumber(), request.getFingerprint());
		}
	}

	@GetMapping(BankApiPaths.AUTH_GET_OPTION)
	public CardAuthOption getAuthOption(@RequestParam("cardNumber") String cardNumber) throws CardNotFountException {
		return service.getAuthOption(cardNumber);
	}

	@PostMapping(BankApiPaths.AUTH_SET_OPTION)
	public void changeOption(@RequestBody AuthRequest request) throws CardNotFountException {
		service.changeAuthOption(request.getCardNumber(), request.getOption());
	}
}

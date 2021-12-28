package com.alemi.bank.controllers;

import com.alemi.bank.services.AuthService;
import com.alemi.common.ApiPaths;
import com.alemi.common.exceptions.AtmException;
import com.alemi.common.models.AuthRequest;
import com.alemi.bank.exceptions.card.CardNotFountException;
import com.alemi.common.models.CardAuthOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * User authentication controller for performing the following operations:
 *  - login
 *  - get login option
 *  - change login option
 */
@RestController
public class AuthController {

	@Autowired
	private AuthService service;

	/**
	 * Login user based on the configured authentication mechanism:
	 *
	 * @param request
	 *  If authentication is configured by pin, the request must contains
	 * 		- card number
	 * 		- pin code
	 * 	If authentication is configured fingerprint, the request must have
	 * 		- card number
	 * 		- fingerprint
	 */
	@PostMapping(ApiPaths.Bank.LOGIN)
	public void login(@RequestBody AuthRequest request) throws AtmException {
		if (request.getOption() == CardAuthOption.PIN) {
			service.loginByPin(request.getCardNumber(), request.getPinCode());
		} else {
			service.loginByFingerprint(request.getCardNumber(), request.getFingerprint());
		}
	}

	/**
	 * Get the configured authentication option for login
	 * @param cardNumber the user card number
	 * @return the option configured for login
	 * @throws CardNotFountException if card number not found
	 */
	@GetMapping(ApiPaths.Bank.AUTH_GET_OPTION)
	public CardAuthOption getAuthOption(@RequestParam("cardNumber") String cardNumber) throws CardNotFountException {
		return service.getAuthOption(cardNumber);
	}

	/**
	 * Change the authentication option such as pin code or fingerprint for login
	 * @param request the desired authentication option declared by the user
	 * @throws CardNotFountException if card number not found
	 */
	@PostMapping(ApiPaths.Bank.AUTH_SET_OPTION)
	public void changeOption(@RequestBody AuthRequest request) throws CardNotFountException {
		service.changeAuthOption(request.getCardNumber(), request.getOption());
	}
}

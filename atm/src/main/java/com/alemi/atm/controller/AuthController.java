package com.alemi.atm.controller;


import com.alemi.atm.communication.BankConnector;
import com.alemi.atm.communication.BankService;
import com.alemi.common.ApiPaths;
import com.alemi.common.models.auth.AuthFingerprint;
import com.alemi.common.models.auth.AuthPincode;
import com.alemi.common.models.auth.AuthRequest;
import com.alemi.common.models.auth.CardAuthOption;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * User authentication controller for performing the following operations:
 * - login by pin or fingerprint
 * - get login option
 * - change login option
 */
@RestController
public class AuthController {
	public final static String AUTHENTICATION = "authentication";

	@Autowired
	private BankConnector connector;
	private BankService service;

	@PostConstruct
	public void init() {
		service = connector.service();
	}

	@PostMapping(ApiPaths.Atm.LOGIN_BY_PIN)
	@CircuitBreaker(name = AUTHENTICATION, fallbackMethod = "fallback")
	public void loginPincode(@RequestBody AuthPincode request) {
		AuthRequest req = new AuthRequest();
		req.setCardNumber(request.getCardNumber());
		req.setOption(CardAuthOption.PIN);
		req.setPin(request);
		connector.execute(service.login(req));
	}

	@PostMapping(ApiPaths.Atm.LOGIN_BY_FINGERPRINT)
	@CircuitBreaker(name = AUTHENTICATION, fallbackMethod = "fallback")
	public void loginPincode(@RequestBody AuthFingerprint request) {
		AuthRequest req = new AuthRequest();
		req.setCardNumber(request.getCardNumber());
		req.setOption(CardAuthOption.FINGERPRINT);
		req.setFingerprint(request);
		connector.execute(service.login(req));
	}

	@GetMapping(ApiPaths.Atm.AUTH)
	@CircuitBreaker(name = AUTHENTICATION, fallbackMethod = "fallback")
	public CardAuthOption getAuthOption(@RequestParam("cardNumber") String cardNumber) {
		return connector.execute(service.getAuthOption(cardNumber));
	}

	@PutMapping(ApiPaths.Atm.AUTH)
	@CircuitBreaker(name = AUTHENTICATION, fallbackMethod = "fallback")
	public void setAuthOption(@RequestBody AuthRequest request) {
		connector.execute(service.changeAuthOption(request));
	}

	public ResponseEntity<String> fallback(Throwable e) {
		return new ResponseEntity <>(e.getMessage(), HttpStatus.BAD_GATEWAY);
	}
}

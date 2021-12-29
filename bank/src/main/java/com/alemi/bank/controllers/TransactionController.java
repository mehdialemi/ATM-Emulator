package com.alemi.bank.controllers;

import com.alemi.bank.services.CardService;
import com.alemi.bank.exceptions.card.BalanceInsufficientException;
import com.alemi.common.ApiPaths;
import com.alemi.bank.exceptions.card.CardNotFountException;
import com.alemi.common.exceptions.AtmException;
import com.alemi.common.exceptions.ErrorResponse;
import com.alemi.common.models.transaction.BankOperation;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.micrometer.core.annotation.Timed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
public class TransactionController {

	private static final String BANK_SERVICE = "bankService";
	@Autowired
	private CardService service;

	@PostMapping(ApiPaths.Bank.DEPOSIT)
	@CircuitBreaker(name = BANK_SERVICE, fallbackMethod = "fallback")
	@Timed(value = "bank_deposit")
	public BankOperation deposit(@RequestBody BankOperation request) throws CardNotFountException {
		return service.deposit(request.getCardNumber(), BigDecimal.valueOf(request.getAmount()));
	}

	@PostMapping(ApiPaths.Bank.WITHDRAW)
	@CircuitBreaker(name = BANK_SERVICE, fallbackMethod = "fallback")
	@Timed(value = "bank_withdraw")
	public BankOperation withdraw(@RequestBody BankOperation request) throws
			CardNotFountException, BalanceInsufficientException {
		return service.withdraw(request.getCardNumber(), BigDecimal.valueOf(request.getAmount()));
	}

	@GetMapping(ApiPaths.Bank.CHECK_BALANCE)
	@CircuitBreaker(name = BANK_SERVICE, fallbackMethod = "fallback")
	@Timed(value = "bank_check_balance")
	public BankOperation checkBalance(@RequestParam("cardNumber") String cardNumber) throws CardNotFountException {
		return service.getBalance(cardNumber);
	}

	@Timed(value = "bank_internal_error")
	public ResponseEntity<String> fallback(AtmException e) {
		ErrorResponse errorResponse = e.getErrorResponse();
		HttpStatus httpStatus = HttpStatus.resolve(errorResponse.getHttpCode());
		return new ResponseEntity <>(errorResponse.getMessage(), httpStatus);
	}
}

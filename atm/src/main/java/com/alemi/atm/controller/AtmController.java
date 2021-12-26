package com.alemi.atm.controller;

import com.alemi.atm.communication.BankConnector;
import com.alemi.atm.communication.BankService;
import com.alemi.atm.config.AtmPaths;
import com.alemi.atm.printer.PrintService;
import com.alemi.bank.card.models.CardOperationRequest;
import com.alemi.bank.card.models.CardResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(AtmPaths.ROOT)
public class AtmController {

	public final static String ATM_SERVICE = "AtmService";

	@Autowired
	private BankConnector bankConnector;
	private BankService bankService;

	@Autowired
	private PrintService printService;

	@PostConstruct
	public void init() {
		bankService = bankConnector.service();
	}

	@PostMapping(AtmPaths.DEPOSIT)
	@CircuitBreaker(name = ATM_SERVICE, fallbackMethod = "bankFallback")
	public void deposit(@RequestBody AtmRequest atmRequest) {
		CardOperationRequest request = new CardOperationRequest();
		request.setCardNumber(atmRequest.getCardNumber());
		request.setAmount(atmRequest.getAmount());
		bankConnector.execute(bankService.deposit(request));
		printService.printDeposit(atmRequest);
	}


	@PostMapping(AtmPaths.WITHDRAW)
	@CircuitBreaker(name = ATM_SERVICE, fallbackMethod = "bankFallback")
	public void withdraw(@RequestBody AtmRequest atmRequest) {
		CardOperationRequest request = new CardOperationRequest();
		request.setCardNumber(atmRequest.getCardNumber());
		request.setAmount(atmRequest.getAmount());
		bankConnector.execute(bankService.withdraw(request));
		printService.printWithdraw(atmRequest);
	}

	@GetMapping(AtmPaths.CHECK_BALANCE)
	@CircuitBreaker(name = ATM_SERVICE, fallbackMethod = "bankFallback")
	public void checkBalance(@RequestParam("cardNumber") String cardNumber) {
		CardResponse response = bankConnector.execute(bankService.checkBalance(cardNumber));
		printService.printBalance(response);
	}

	public ResponseEntity<String> bankFallback(RuntimeException e) {
		return new ResponseEntity <>(e.getMessage(), HttpStatus.BAD_GATEWAY);
	}
}

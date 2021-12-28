package com.alemi.atm.controller;

import com.alemi.atm.communication.BankConnector;
import com.alemi.atm.communication.BankService;
import com.alemi.atm.printer.PrintService;
import com.alemi.common.ApiPaths;
import com.alemi.common.models.BankOperation;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(ApiPaths.ROOT)
public class AtmController {

	public final static String ATM_SERVICE = "atmService";

	@Autowired
	private BankConnector bankConnector;
	private BankService bankService;

	@Autowired
	private PrintService printService;

	@PostConstruct
	public void init() {
		bankService = bankConnector.service();
	}

	@PostMapping(ApiPaths.Atm.DEPOSIT)
	@CircuitBreaker(name = ATM_SERVICE, fallbackMethod = "fallback")
	public void deposit(@RequestBody BankOperation atmRequest) {
		BankOperation request = new BankOperation();
		request.setCardNumber(atmRequest.getCardNumber());
		request.setAmount(atmRequest.getAmount());
		bankConnector.execute(bankService.deposit(request));
		printService.printDeposit(atmRequest);
	}

	@PostMapping(ApiPaths.Atm.WITHDRAW)
	@CircuitBreaker(name = ATM_SERVICE, fallbackMethod = "fallback")
	public void withdraw(@RequestBody BankOperation atmRequest) {
		BankOperation request = new BankOperation();
		request.setCardNumber(atmRequest.getCardNumber());
		request.setAmount(atmRequest.getAmount());
		bankConnector.execute(bankService.withdraw(request));
		printService.printWithdraw(atmRequest);
	}

	@GetMapping(ApiPaths.Atm.CHECK_BALANCE)
	@CircuitBreaker(name = ATM_SERVICE, fallbackMethod = "fallback")
	public void checkBalance(@RequestParam("cardNumber") String cardNumber) {
		BankOperation response = bankConnector.execute(bankService.checkBalance(cardNumber));
		printService.printBalance(response);
	}

	public ResponseEntity<String> fallback(Throwable e) {
		return new ResponseEntity <>(e.getMessage(), HttpStatus.BAD_GATEWAY);
	}
}

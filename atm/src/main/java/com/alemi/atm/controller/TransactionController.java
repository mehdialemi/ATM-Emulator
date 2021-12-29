package com.alemi.atm.controller;

import com.alemi.atm.communication.BankConnector;
import com.alemi.atm.communication.BankService;
import com.alemi.atm.printer.PrintService;
import com.alemi.common.ApiPaths;
import com.alemi.common.models.transaction.BankOperation;
import com.alemi.common.models.transaction.Operation;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * Receive user demands for deposit, withdraw, check balance requests.
 */
@RestController
@RequestMapping(ApiPaths.ROOT)
public class TransactionController {

	public final static String TRANSACTION = "transaction";

	@Autowired
	private BankConnector connector;
	private BankService service;

	@Autowired
	private PrintService printer;

	@PostConstruct
	public void init() {
		service = connector.service();
	}

	/**
	 * Deposit money into the user banking card
	 * @param atmRequest user request including card cardNumber and amount of deposit money
	 */
	@PostMapping(ApiPaths.Atm.DEPOSIT)
	@CircuitBreaker(name = TRANSACTION, fallbackMethod = "fallback")
	public void deposit(@RequestBody Operation atmRequest) {
		BankOperation request = new BankOperation();
		request.setCardNumber(atmRequest.getCardNumber());
		request.setAmount(atmRequest.getAmount());
		connector.execute(service.deposit(request));
		printer.printDeposit(atmRequest);
	}


	/**
	 * Withdraw money from the user banking card
	 * @param atmRequest user request including card cardNumber and amount of withdraw money
	 */
	@PostMapping(ApiPaths.Atm.WITHDRAW)
	@CircuitBreaker(name = TRANSACTION, fallbackMethod = "fallback")
	public void withdraw(@RequestBody Operation atmRequest) {
		BankOperation request = new BankOperation();
		request.setCardNumber(atmRequest.getCardNumber());
		request.setAmount(atmRequest.getAmount());
		connector.execute(service.withdraw(request));
		printer.printWithdraw(atmRequest);
	}

	/**
	 * Request to get balance of user banking card
	 */
	@GetMapping(ApiPaths.Atm.CHECK_BALANCE)
	@CircuitBreaker(name = TRANSACTION, fallbackMethod = "fallback")
	public void checkBalance(@RequestParam("cardNumber") String cardNumber) {
		BankOperation response = connector.execute(service.checkBalance(cardNumber));
		printer.printBalance(response);
	}

	public ResponseEntity<String> fallback(Throwable e) {
		return new ResponseEntity <>(e.getMessage(), HttpStatus.BAD_GATEWAY);
	}
}

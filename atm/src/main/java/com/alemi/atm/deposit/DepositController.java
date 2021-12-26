package com.alemi.atm.deposit;

import com.alemi.atm.communication.BankConnector;
import com.alemi.atm.communication.BankService;
import com.alemi.atm.config.AtmPaths;
import com.alemi.bank.card.models.CardOperationRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;

@RestController
@RequestMapping(AtmPaths.ROOT)
public class DepositController {

	public final static String DEPOSIT_SERVICE = "depositService";

	@Autowired
	private BankConnector bankConnector;
	private BankService bankService;

	@PostConstruct
	public void init() {
		bankService = bankConnector.service();
	}

	@PostMapping(AtmPaths.DEPOSIT)
	@CircuitBreaker(name = DEPOSIT_SERVICE, fallbackMethod = "depositFallback")
	public void deposit(@RequestBody DepositRequest depositRequest) {
		CardOperationRequest request = new CardOperationRequest();
		request.setCardNumber(depositRequest.getCardNumber());
		request.setAmount(depositRequest.getAmount());
		bankConnector.execute(bankService.deposit(request));
	}

	public String depositFallback(RuntimeException e) {
		return e.getMessage();
	}
}

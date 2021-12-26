package com.alemi.atm.printer;

import com.alemi.atm.controller.AtmRequest;
import com.alemi.bank.card.models.CardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrintService {

	public void printDeposit(AtmRequest request) {
		log.info("Successfully controller request: {}", request);
	}

	public void printWithdraw(AtmRequest request) {
		log.info("Successfully withdraw request: {}", request);
	}

	public void printBalance(CardResponse response) {
		log.info("Balance: {}", response);
	}
}

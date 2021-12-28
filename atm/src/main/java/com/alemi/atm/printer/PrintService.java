package com.alemi.atm.printer;

import com.alemi.common.models.BankOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrintService {

	public void printDeposit(BankOperation request) {
		log.info("Successfully controller request: {}", request);
	}

	public void printWithdraw(BankOperation request) {
		log.info("Successfully withdraw request: {}", request);
	}

	public void printBalance(BankOperation response) {
		log.info("Balance: {}", response);
	}
}

package com.alemi.atm.printer;

import com.alemi.common.models.transaction.BankOperation;
import com.alemi.common.models.transaction.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PrintService {

	public void printDeposit(Operation request) {
		log.info("Successfully controller request: {}", request);
	}

	public void printWithdraw(Operation request) {
		log.info("Successfully withdraw request: {}", request);
	}

	public void printBalance(BankOperation response) {
		log.info("Balance: {}", response);
	}


}

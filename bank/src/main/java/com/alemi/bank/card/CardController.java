package com.alemi.bank.card;

import com.alemi.bank.card.exceptions.CardNotFountException;
import com.alemi.bank.card.models.CardOperationRequest;
import com.alemi.bank.card.models.CardResponse;
import com.alemi.bank.common.BankException;
import com.alemi.bank.common.BankApiPaths;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(BankApiPaths.ROOT + BankApiPaths.CARD)
public class CardController {
	
	@Autowired
	private CardService cardService;

	@PostMapping(BankApiPaths.DEPOSIT)
	public CardResponse deposit(@RequestBody CardOperationRequest request) throws CardNotFountException {
		return cardService.deposit(request.getCardNumber(), BigDecimal.valueOf(request.getAmount()));
	}

	@PostMapping(BankApiPaths.WITHDRAW)
	public CardResponse withdraw(@RequestBody CardOperationRequest request) throws BankException  {
		return cardService.withdraw(request.getCardNumber(), BigDecimal.valueOf(request.getAmount()));
	}

	@GetMapping(BankApiPaths.CHECK_BALANCE)
	public CardResponse checkBalance(@RequestParam("cardNumber") String cardNumber) throws CardNotFountException {
		return cardService.getBalance(cardNumber);
	}

}

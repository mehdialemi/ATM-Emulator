package com.alemi.bank.auth;

import com.alemi.bank.auth.exceptions.AuthOptionException;
import com.alemi.bank.auth.exceptions.CardIsBlockedException;
import com.alemi.bank.auth.exceptions.InvalidFingerprintException;
import com.alemi.bank.auth.exceptions.InvalidPinException;
import com.alemi.bank.auth.models.AuthResponse;
import com.alemi.bank.card.CardService;
import com.alemi.bank.card.entities.Card;
import com.alemi.bank.card.exceptions.CardNotFountException;
import com.alemi.bank.card.models.CardAuthOption;
import com.alemi.bank.card.models.OperationType;
import com.alemi.bank.common.BankException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@Slf4j
public class AuthService {

	@Autowired
	private CardService cardService;

	@Value("${bank.auth.attempt.max}")
	private int maxAttempts;

	public AuthResponse loginByPin(String cardNumber, int pinCode) throws BankException {
		log.debug("Login request by pin, cardNumber: {}", cardNumber);

		Card card = cardService.getCard(cardNumber);

		checkBlocked(card);

		if (CardAuthOption.PIN != card.getCardAuth().getOption()) {
			throw new AuthOptionException("Card authentication is not set by pin");
		}

		if (card.getPinCode().equals(pinCode)) {
			cardService.onSuccessAuth(card);
			return createResponse();
		}

		cardService.onFailedAuth(card, maxAttempts);
		throw new InvalidPinException(pinCode);
	}

	public AuthResponse loginByFingerprint(String cardNumber, String fingerprint) throws BankException {
		log.debug("Login request by fingerprint, card number: {}", cardNumber);

		Card card = cardService.getCard(cardNumber);

		checkBlocked(card);

		if (card.getCardAuth().getOption() != CardAuthOption.FINGERPRINT) {
			throw new AuthOptionException("Card authentication is not set by fingerprint");
		}

		if(card.getFingerprint().equals(fingerprint)) {
			cardService.onSuccessAuth(card);
			return createResponse();
		}

		cardService.onFailedAuth(card, maxAttempts);
		throw new InvalidFingerprintException(fingerprint);
	}

	public CardAuthOption getAuthOption(String cardNumber) throws CardNotFountException {
		log.debug("Request to get auth option for card number: {}", cardNumber);

		Card card = cardService.getCard(cardNumber);
		return card.getCardAuth().getOption();
	}

	public void changeAuthOption(String cardNumber, CardAuthOption option) throws CardNotFountException {
		log.debug("Request to change auth option for card number: {}, option: {}", cardNumber, option);

		cardService.changeAuthOption(cardNumber, option);
	}

	private void checkBlocked(Card card) throws CardIsBlockedException {
		if (card.getCardAuth().getBlocked())
			throw new CardIsBlockedException(card.getCardNumber());
	}

	private AuthResponse createResponse() {
		AuthResponse response = new AuthResponse();
		response.setOperations(Arrays.asList(
				OperationType.CHECK_BALANCE,
				OperationType.DEPOSIT,
				OperationType.WITHDRAW)
		);

		return response;
	}
}

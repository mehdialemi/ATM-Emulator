package com.alemi.bank.services;

import com.alemi.bank.exceptions.auth.AuthOptionException;
import com.alemi.bank.exceptions.auth.CardIsBlockedException;
import com.alemi.bank.exceptions.auth.InvalidFingerprintException;
import com.alemi.bank.exceptions.auth.InvalidPinException;
import com.alemi.common.exceptions.AtmException;
import com.alemi.common.models.auth.AuthResponse;
import com.alemi.bank.entities.Card;
import com.alemi.bank.exceptions.card.CardNotFountException;
import com.alemi.common.models.auth.CardAuthOption;
import com.alemi.common.models.transaction.OperationType;
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

	public AuthResponse loginByPin(String cardNumber, int pinCode) throws AtmException {
		log.debug("Login request by pin, cardNumber: {}", cardNumber);

		Card card = cardService.getCard(cardNumber);

		checkBlocked(card);

		if (CardAuthOption.PIN != card.getCardAuth().getCardAuthOption()) {
			throw new AuthOptionException("CardInfo authentication is not set by pin");
		}

		if (card.getPinCode().equals(pinCode)) {
			cardService.onSuccessAuth(card);
			return createResponse();
		}

		cardService.onFailedAuth(card, maxAttempts);
		throw new InvalidPinException(pinCode);
	}

	public AuthResponse loginByFingerprint(String cardNumber, String fingerprint) throws AtmException {
		log.debug("Login request by fingerprint, card cardNumber: {}", cardNumber);

		Card card = cardService.getCard(cardNumber);

		checkBlocked(card);

		if (card.getCardAuth().getCardAuthOption() != CardAuthOption.FINGERPRINT) {
			throw new AuthOptionException("CardInfo authentication is not set by fingerprint");
		}

		if(card.getFingerprint().equals(fingerprint)) {
			cardService.onSuccessAuth(card);
			return createResponse();
		}

		cardService.onFailedAuth(card, maxAttempts);
		throw new InvalidFingerprintException(fingerprint);
	}

	public CardAuthOption getAuthOption(String cardNumber) throws CardNotFountException {
		log.debug("Request to get auth option for card cardNumber: {}", cardNumber);

		Card card = cardService.getCard(cardNumber);
		return card.getCardAuth().getCardAuthOption();
	}

	public void changeAuthOption(String cardNumber, CardAuthOption option) throws CardNotFountException {
		log.debug("Request to change auth option for card cardNumber: {}, option: {}", cardNumber, option);

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

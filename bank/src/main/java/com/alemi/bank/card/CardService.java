package com.alemi.bank.card;

import com.alemi.bank.card.entities.CardAuth;
import com.alemi.bank.card.exceptions.BalanceInsufficentException;
import com.alemi.bank.card.exceptions.CardNotFountException;
import com.alemi.bank.card.entities.Card;
import com.alemi.bank.card.entities.Transaction;
import com.alemi.bank.card.exceptions.DuplicatedCardException;
import com.alemi.bank.card.models.CardAuthOption;
import com.alemi.bank.card.models.CardResponse;
import com.alemi.bank.card.models.OperationType;
import com.alemi.bank.card.repositories.CardRepository;
import com.alemi.bank.card.repositories.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@Slf4j
public class CardService {


	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private CardRepository repository;


	public Card create(String cardNumber, int pin, String fingerprint) throws DuplicatedCardException {
		Optional <Card> optionalCard = repository.findByCardNumber(cardNumber);
		if (optionalCard.isPresent()) {
			throw new DuplicatedCardException(cardNumber);
		}

		log.info("Creating new card with card number: {}", cardNumber);

		Card card = new Card();
		card.setCardNumber(cardNumber);
		card.setPinCode(pin);
		card.setFingerprint(fingerprint);

		CardAuth auth = new CardAuth();
		auth.setBlocked(false);
		auth.setCardAuthOption(CardAuthOption.PIN);
		card.setCardAuth(auth);

		return repository.save(card);
	}

	@Transactional
	public CardResponse getBalance(String cardNumber) throws CardNotFountException {
		Card card = getCard(cardNumber);
		log.info("Get balance for card number: {}", cardNumber);
		return createResponse(cardNumber, card.getBalance(), OperationType.CHECK_BALANCE);
	}

	@Transactional
	public CardResponse deposit(String cardNumber, BigDecimal amount) throws CardNotFountException {
		Card card = getCard(cardNumber);
		log.info("Deposit {} amount for card number {}", amount, cardNumber);

		Transaction transaction = new Transaction();
		transaction.setCard(card);
		transaction.setAmount(amount);
		transaction.setOperationType(OperationType.DEPOSIT);
		BigDecimal added = card.getBalance().add(amount);
		card.setBalance(added);

		repository.save(card);
		transactionRepository.save(transaction);

		return createResponse(cardNumber, amount, OperationType.DEPOSIT);
	}

	@Transactional
	public CardResponse withdraw(String cardNumber, BigDecimal amount) throws CardNotFountException, BalanceInsufficentException {
		Card card = getCard(cardNumber);

		log.info("Withdraw {} amount for card number {}", amount, cardNumber);

		if (card.getBalance().subtract(amount).doubleValue() < 0.0)
			throw new BalanceInsufficentException(cardNumber, amount);

		Transaction transaction = new Transaction();
		transaction.setCard(card);
		transaction.setAmount(amount);
		transaction.setOperationType(OperationType.WITHDRAW);
		BigDecimal subtract = card.getBalance().subtract(amount);
		card.setBalance(subtract);

		repository.save(card);
		transactionRepository.save(transaction);

		return createResponse(cardNumber, amount, OperationType.WITHDRAW);
	}

	public Card getCard(String cardNumber)  throws CardNotFountException {
		Optional<Card> optionalCard = repository.findByCardNumber(cardNumber);
		if (optionalCard.isPresent())
			return optionalCard.get();
		throw new CardNotFountException(cardNumber);
	}

	public void onSuccessAuth(Card card) {
		card.getCardAuth().setUnsuccessfulAttempts(0);
		repository.save(card);
	}

	public void onFailedAuth(Card card, int maxAttempts) {
		CardAuth cardAuth = card.getCardAuth();
		Integer attempts = cardAuth.getUnsuccessfulAttempts();
		cardAuth.setUnsuccessfulAttempts(attempts + 1);
		if (cardAuth.getUnsuccessfulAttempts() >= maxAttempts) {
			cardAuth.setBlocked(true);
		}

		card.setCardAuth(cardAuth);
		repository.save(card);
	}

	public void changeAuthOption(String cardNumber, CardAuthOption option) throws CardNotFountException {
		Card card = getCard(cardNumber);
		CardAuth cardAuth = card.getCardAuth();
		cardAuth.setCardAuthOption(option);
		card.setCardAuth(cardAuth);
		repository.save(card);
	}

	private CardResponse createResponse(String cardNumber, BigDecimal amount, OperationType type) {
		CardResponse response = new CardResponse();
		response.setCardNumber(cardNumber);
		response.setAmount(amount);
		response.setOperationType(type);
		return response;
	}
}

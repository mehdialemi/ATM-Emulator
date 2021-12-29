package com.alemi.bank.services;

import com.alemi.bank.entities.Card;
import com.alemi.bank.entities.CardAuth;
import com.alemi.bank.entities.Transaction;
import com.alemi.bank.exceptions.card.BalanceInsufficientException;
import com.alemi.bank.exceptions.card.CardNotFountException;
import com.alemi.bank.exceptions.card.DuplicatedCardException;
import com.alemi.common.models.auth.CardAuthOption;
import com.alemi.bank.repositories.CardRepository;
import com.alemi.bank.repositories.TransactionRepository;
import com.alemi.common.models.transaction.BankOperation;
import com.alemi.common.models.transaction.OperationType;
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

		log.info("Creating new card with card cardNumber: {}", cardNumber);

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
	public BankOperation getBalance(String cardNumber) throws CardNotFountException {
		Card card = getCard(cardNumber);
		log.info("Get balance for card cardNumber: {}", cardNumber);
		return createResponse(cardNumber, card.getBalance(), OperationType.CHECK_BALANCE);
	}

	@Transactional
	public BankOperation deposit(String cardNumber, BigDecimal amount) throws CardNotFountException {
		Card card = getCard(cardNumber);
		log.info("Deposit {} amount for card cardNumber {}", amount, cardNumber);

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
	public BankOperation withdraw(String cardNumber, BigDecimal amount) throws CardNotFountException, BalanceInsufficientException {
		Card card = getCard(cardNumber);

		log.info("Withdraw {} amount for card cardNumber {}", amount, cardNumber);

		if (card.getBalance().subtract(amount).doubleValue() < 0.0)
			throw new BalanceInsufficientException(cardNumber, amount);

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

	private BankOperation createResponse(String cardNumber, BigDecimal amount, OperationType type) {
		BankOperation response = new BankOperation();
		response.setCardNumber(cardNumber);
		response.setAmount(amount.doubleValue());
		response.setOperationType(type);
		return response;
	}
}

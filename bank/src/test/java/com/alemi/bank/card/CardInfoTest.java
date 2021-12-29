package com.alemi.bank.card;


import com.alemi.bank.entities.Card;
import com.alemi.bank.exceptions.card.BalanceInsufficientException;
import com.alemi.bank.exceptions.card.CardNotFountException;
import com.alemi.bank.exceptions.card.DuplicatedCardException;
import com.alemi.bank.services.CardService;
import com.alemi.common.models.auth.CardAuthOption;
import com.alemi.common.models.transaction.BankOperation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class CardInfoTest {

	@Autowired
	private CardService cardService;

	@Test
	public void create() throws DuplicatedCardException, CardNotFountException {
		cardService.create("12", 1234, "abcd");
		Card card = cardService.getCard("12");
		Assert.assertEquals("12", card.getCardNumber());
		Assert.assertEquals(1234, card.getPinCode().intValue());
		Assert.assertEquals("abcd", card.getFingerprint());
		Assert.assertEquals(CardAuthOption.PIN, card.getCardAuth().getCardAuthOption());
	}

	@Test
	public void deposit() throws DuplicatedCardException, CardNotFountException {
		cardService.create("12", 1234, "abcd");
		BigDecimal amount = BigDecimal.valueOf(1000.00);
		cardService.deposit("12", amount);
		BankOperation response = cardService.getBalance("12");
		Assert.assertEquals(amount.doubleValue(), response.getAmount(), 0.0000001);
	}

	@Test
	public void withdraw() throws DuplicatedCardException, CardNotFountException, BalanceInsufficientException {
		cardService.create("12", 1234, "abcd");
		cardService.deposit("12", BigDecimal.valueOf(1000));
		cardService.withdraw("12", BigDecimal.valueOf(100));
		BankOperation response = cardService.getBalance("12");
		Assert.assertEquals(BigDecimal.valueOf(900).doubleValue(), response.getAmount(), 0.0000001);
	}
}

package com.alemi.bank.auth;

import com.alemi.bank.auth.exceptions.AuthOptionException;
import com.alemi.bank.auth.exceptions.CardIsBlockedException;
import com.alemi.bank.auth.exceptions.InvalidPinException;
import com.alemi.bank.auth.models.AuthRequest;
import com.alemi.bank.auth.models.AuthResponse;
import com.alemi.bank.card.CardService;
import com.alemi.bank.card.entities.Card;
import com.alemi.bank.card.exceptions.CardNotFountException;
import com.alemi.bank.card.exceptions.DuplicatedCardException;
import com.alemi.bank.card.models.CardAuthOption;
import com.alemi.bank.common.BankException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuthTest {

	@Autowired
	private CardService cardService;

	@Autowired
	private AuthService authService;

	@Test
	public void testLoginByPin() throws BankException {
		cardService.create("12", 1234, "abcd");
		AuthResponse authResponse = authService.loginByPin("12", 1234);
		Assert.assertTrue(authResponse.getOperations().size() > 0);
	}

	@Test
	public void testLoginByFingerprint() throws BankException {
		cardService.create("12", 1234, "abcd");
		authService.changeAuthOption("12", CardAuthOption.FINGERPRINT);
		AuthResponse authResponse = authService.loginByFingerprint("12", "abcd");
		Assert.assertTrue(authResponse.getOperations().size() > 0);
	}

	@Test
	public void testFailedLogin() throws DuplicatedCardException, CardNotFountException {
		cardService.create("12", 1234, "abcd");

		try {
			authService.loginByPin("12", 12);
		} catch (BankException e) {
			Assert.assertTrue(e instanceof InvalidPinException);
		}

		Card card = cardService.getCard("12");
		Assert.assertEquals(1, card.getCardAuth().getUnsuccessfulAttempts().intValue());
	}

	@Test
	public void testBlockCard() throws DuplicatedCardException {
		cardService.create("12", 1234, "abcd");
		AuthRequest request = new AuthRequest();
		request.setCardNumber("12");
		request.setPinCode(12);

		try {
			authService.loginByPin("12", 12);
		} catch (BankException e) {
			Assert.assertTrue(e instanceof InvalidPinException);
		}

		try {
			authService.loginByPin("12", 12);
		} catch (BankException e) {
			Assert.assertTrue(e instanceof InvalidPinException);
		}

		try {
			authService.loginByPin("12", 12);
		} catch (BankException e) {
			Assert.assertTrue(e instanceof InvalidPinException);
		}

		try {
			authService.loginByPin("12", 12);
		} catch (BankException e) {
			Assert.assertTrue(e instanceof CardIsBlockedException);
		}
	}

	@Test
	public void testAuthOption() throws BankException {
		cardService.create("12", 1234, "abcd");

		try {
			authService.loginByFingerprint("12", "abcd");
		} catch (BankException e) {
			Assert.assertTrue(e instanceof AuthOptionException);
		}

		authService.changeAuthOption("12", CardAuthOption.FINGERPRINT);
		AuthResponse authResponse = authService.loginByFingerprint("12", "abcd");
		Assert.assertTrue(authResponse.getOperations().size() > 0);
	}
}

package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Hashtable;

public class BankTest {
	Currency SEK, DKK;
	Bank SweBank, Nordea, DanskeBank;
	
	@Before
	public void setUp() throws Exception {
		DKK = new Currency("DKK", 0.20);
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		Nordea = new Bank("Nordea", SEK);
		DanskeBank = new Bank("DanskeBank", DKK);
		SweBank.openAccount("Ulrika");
		SweBank.openAccount("Bob");
		Nordea.openAccount("Bob");
		DanskeBank.openAccount("Gertrud");
	}

	@Test
	public void testGetName() {
		// defining expected name
		String expectedName = "PKO";

		// assert expected name with real
		assertEquals(expectedName, new Bank(expectedName, SEK).getName());
	}

	@Test
	public void testGetCurrency() {
		// asserting expected currency (SEK) with real one
		assertEquals(SEK, new Bank("PKO", SEK).getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException, IllegalAccessException, NoSuchFieldException {
		// open a new account
		String newAccountId = "MaxAccount";
		SweBank.openAccount(newAccountId);

		// accessing private field "accountlist" of the Bank class
		Field accountlist = Bank.class.getDeclaredField("accountlist");
		accountlist.setAccessible(true);

		// checking if the account with appropriate id has been opened
		assertNotNull(((Hashtable<String, Account>)accountlist.get(SweBank)).get(newAccountId));
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {

		// define balance before
		Integer balanceBefore = SweBank.getBalance("Ulrika");

		// performing concrete deposit
		SweBank.deposit("Ulrika", new Money(1000, SEK));

		// checking if balanceBefore plus deposited value equals to the current balance
		assertEquals(balanceBefore + 1000, SweBank.getBalance("Ulrika").intValue());
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		// define balance before
		Integer balanceBefore = SweBank.getBalance("Ulrika");

		// withdraw amount 1000 in SEK
		SweBank.withdraw("Ulrika", new Money(1000, SEK));

		// checking if balance before - 100 is equal to real balance
		assertEquals(balanceBefore - 1000, SweBank.getBalance("Ulrika").intValue());
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException, AccountExistsException {
		// define a new account name
		String newAccountName = "newAccount";

		// define deposit amount in SEK currency
		Money depositAmount = new Money(1000, SEK);

		// open a new account with defined name
		SweBank.openAccount(newAccountName);

		// deposit earlier defined money to newly opened account
		SweBank.deposit(newAccountName, depositAmount);

		// checking if defined amount equals to current balance
		assertEquals(depositAmount.getAmount(), SweBank.getBalance(newAccountName));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		// define expected amount
		Integer amount = 10000;

		// deposit this amount to account Ulrika
		SweBank.deposit("Ulrika", new Money(amount, SEK));

		// transfer defined amount to another account Bob
		SweBank.transfer("Ulrika", Nordea, "Bob", new Money(amount, SEK));

		// checking if expect and real values are the same
		assertEquals(amount, Nordea.getBalance("Bob"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		// adding a new timed payment
		SweBank.addTimedPayment("Ulrika", "UlrikaSWEToBobNORDEA", 1000, 10, new Money(1000, SEK), Nordea, "Bob");

		// removing newly created timed payment.
		SweBank.removeTimedPayment("Ulrika", "UlrikaSWEToBobNORDEA");
	}
}

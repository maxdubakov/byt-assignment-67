package b_Money;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Hashtable;

import static org.junit.Assert.*;

public class AccountTest {
	Currency SEK, DKK;
	Bank Nordea;
	Bank DanskeBank;
	Bank SweBank;
	Account testAccount;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		SweBank = new Bank("SweBank", SEK);
		SweBank.openAccount("Alice");
		testAccount = new Account("Hans", SEK);
		testAccount.deposit(new Money(10000000, SEK));

		SweBank.deposit("Alice", new Money(1000000, SEK));
	}
	
	@Test
	public void testAddRemoveTimedPayment() throws NoSuchFieldException, IllegalAccessException {
		// accessing private field "accountlist" via reflection mechanism
		Field accountlist = Bank.class.getDeclaredField("accountlist");
		accountlist.setAccessible(true);
		Account alice = ((Hashtable<String, Account>)accountlist.get(SweBank)).get("Alice");

		// adding timed payment to the alice back account
		alice.addTimedPayment("AliceToHans", 1000, 10, new Money(1000, SEK), SweBank, "Hans");

		// checking if newly added timed payment exists
		assertTrue(alice.timedPaymentExists("AliceToHans"));

		// removing timed payment
		alice.removeTimedPayment("AliceToHans");

		// checking if removed timed payment does not exist
		assertFalse(alice.timedPaymentExists("AliceToHans"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		// adding timed payment with id UlrikaSWEToBobNORDEA
		testAccount.addTimedPayment( "UlrikaSWEToBobNORDEA", 1000, 10, new Money(1000, SEK), Nordea, "Bob");

		// removing timed payment with id UlrikaSWEToBobNORDEA
		testAccount.removeTimedPayment("UlrikaSWEToBobNORDEA");
	}

	@Test
	public void testAddWithdraw() throws AccountDoesNotExistException {
		// defining expected value after operation
		Integer expected = testAccount.getBalance().getAmount() - 10000;

		// perform withdraw
		testAccount.withdraw(new Money(10000, SEK));

		// asserting expected value with real
		assertEquals(expected, testAccount.getBalance().getAmount());
	}
	
	@Test
	public void testGetBalance() {
		// defining expected amount
		Integer amount = 10000000;

		// asserting expected value with real
		assertEquals(amount, testAccount.getBalance().getAmount());
	}
}

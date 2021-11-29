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
		Field accountlist = Bank.class.getDeclaredField("accountlist");
		accountlist.setAccessible(true);
		Account alice = ((Hashtable<String, Account>)accountlist.get(SweBank)).get("Alice");

		alice.addTimedPayment("AliceToHans", 1000, 10, new Money(1000, SEK), SweBank, "Hans");
		assertTrue(alice.timedPaymentExists("AliceToHans"));

		alice.removeTimedPayment("AliceToHans");
		assertFalse(alice.timedPaymentExists("AliceToHans"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		testAccount.addTimedPayment( "UlrikaSWEToBobNORDEA", 1000, 10, new Money(1000, SEK), Nordea, "Bob");
		testAccount.removeTimedPayment("UlrikaSWEToBobNORDEA");
	}

	@Test
	public void testAddWithdraw() throws AccountDoesNotExistException {
		Integer expected = testAccount.getBalance().getAmount() - 10000;
		testAccount.withdraw(new Money(10000, SEK));

		assertEquals(expected, testAccount.getBalance().getAmount());
	}
	
	@Test
	public void testGetBalance() {
		Integer amount = 10000000;
		assertEquals(amount, testAccount.getBalance().getAmount());
	}
}

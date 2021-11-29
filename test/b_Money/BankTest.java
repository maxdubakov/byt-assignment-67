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
		String expectedName = "PKO";
		assertEquals(expectedName, new Bank(expectedName, SEK).getName());
	}

	@Test
	public void testGetCurrency() {
		assertEquals(SEK, new Bank("PKO", SEK).getCurrency());
	}

	@Test
	public void testOpenAccount() throws AccountExistsException, AccountDoesNotExistException, IllegalAccessException, NoSuchFieldException {
		String newAccountId = "MaxAccount";
		SweBank.openAccount(newAccountId);

		Field accountlist = Bank.class.getDeclaredField("accountlist");
		accountlist.setAccessible(true);

		assertNotNull(((Hashtable<String, Account>)accountlist.get(SweBank)).get(newAccountId));
	}

	@Test
	public void testDeposit() throws AccountDoesNotExistException {
		Integer balanceBefore = SweBank.getBalance("Ulrika");
		SweBank.deposit("Ulrika", new Money(1000, SEK));

		assertEquals(balanceBefore + 1000, SweBank.getBalance("Ulrika").intValue());
	}

	@Test
	public void testWithdraw() throws AccountDoesNotExistException {
		Integer balanceBefore = SweBank.getBalance("Ulrika");
		SweBank.withdraw("Ulrika", new Money(1000, SEK));

		assertEquals(balanceBefore - 1000, SweBank.getBalance("Ulrika").intValue());
	}
	
	@Test
	public void testGetBalance() throws AccountDoesNotExistException, AccountExistsException {
		String newAccountName = "newAccount";
		Money depositAmount = new Money(1000, SEK);
		SweBank.openAccount(newAccountName);
		SweBank.deposit(newAccountName, depositAmount);
		assertEquals(depositAmount.getAmount(), SweBank.getBalance(newAccountName));
	}
	
	@Test
	public void testTransfer() throws AccountDoesNotExistException {
		Integer amount = 10000;
		SweBank.deposit("Ulrika", new Money(amount, SEK));

		SweBank.transfer("Ulrika", Nordea, "Bob", new Money(amount, SEK));
		assertEquals(amount, Nordea.getBalance("Bob"));
	}
	
	@Test
	public void testTimedPayment() throws AccountDoesNotExistException {
		SweBank.addTimedPayment("Ulrika", "UlrikaSWEToBobNORDEA", 1000, 10, new Money(1000, SEK), Nordea, "Bob");
		SweBank.removeTimedPayment("Ulrika", "UlrikaSWEToBobNORDEA");
	}
}

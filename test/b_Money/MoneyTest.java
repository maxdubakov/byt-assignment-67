package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MoneyTest {
	Currency SEK, DKK, NOK, EUR;
	Money SEK100, EUR10, SEK200, EUR20, SEK0, EUR0, SEKn100;
	
	@Before
	public void setUp() throws Exception {
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
		SEK100 = new Money(10000, SEK);
		EUR10 = new Money(1000, EUR);
		SEK200 = new Money(20000, SEK);
		EUR20 = new Money(2000, EUR);
		SEK0 = new Money(0, SEK);
		EUR0 = new Money(0, EUR);
		SEKn100 = new Money(-10000, SEK);
	}

	@Test
	public void testGetAmount() {
		// initialize Money object with [initialAmount] money
		int initialAmount = 1000;
		Money newSEK10 = new Money(initialAmount, SEK);

		// check if defined amount is the same as in newSEK10 object
		assertEquals(newSEK10.getAmount().intValue(), initialAmount);
	}

	@Test
	public void testGetCurrency() {
		// define Money with currency EUR
		Currency initialCurrency = EUR;
		Money newEUR10 = new Money(1000, initialCurrency);

		// check if newly created object has the same currency
		assertEquals(initialCurrency, newEUR10.getCurrency());
	}

	@Test
	public void testToString() {
		// check if Money class implements toString method correctly
		String desiredMessage = "100.00 SEK";
		assertEquals(SEK100.toString(), desiredMessage);
	}

	@Test
	public void testGlobalValue() {
		// define correct formula to convert current amount to a global value
		Integer universalCurrency = (int)(EUR10.getAmount() * EUR10.getCurrency().getRate());

		// check if Money class use the same formula
		assertEquals(universalCurrency, EUR10.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		// define new Money objects
		Money newEUR10 = new Money(1000, EUR);
		Money SEK100 = new Money(10000, SEK);

		// perform a number of checks to make sure that amount as currency are assigned correctly
		assertTrue(newEUR10.equals(EUR10));
		assertNotEquals(newEUR10, EUR10);

		assertTrue(newEUR10.equals(SEK100));
	}

	@Test
	public void testAdd() {
		// define correct formula to add 2 Money objects
		Money result = new Money((int)((EUR10.universalValue() + SEK100.universalValue()) / EUR.getRate()), EUR);

		// check if Money class use the same way of dealing with addition
		assertEquals(result.getAmount(), EUR10.add(SEK100).getAmount());
	}

	@Test
	public void testSub() {
		// define correct formula to subtract 2 Money objects
		Money result = new Money((int)((EUR20.universalValue() - SEK100.universalValue()) / EUR.getRate()), EUR);

		// check if Money class use the same way of dealing with subtraction
		assertEquals(result.getAmount(), EUR20.sub(SEK100).getAmount());
	}

	@Test
	public void testIsZero() {
		assertTrue(EUR0.isZero());
		assertFalse(EUR10.isZero());
	}

	@Test
	public void testNegate() {
		// define a global value to work with
		int result = -10;

		// test if Money class negates amount correctly
		assertEquals(result * 100, EUR10.negate().getAmount().intValue());
		assertNotEquals(result * 100, SEK100.negate().getAmount().intValue());
		assertNotEquals(SEK100, SEK100.negate());
	}

	@Test
	public void testCompareTo() {
		// testing comparing amounts of money in different currencies
		assertEquals(0, EUR10.compareTo(SEK100));
		assertTrue(SEK100.compareTo(EUR20) < 0);
		assertTrue(EUR20.compareTo(SEKn100) > 0);
	}
}

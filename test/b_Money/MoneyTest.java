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

		assertEquals(newSEK10.getAmount().intValue(), initialAmount);
	}

	@Test
	public void testGetCurrency() {
		Currency initialCurrency = EUR;
		Money newEUR10 = new Money(1000, initialCurrency);

		assertEquals(initialCurrency, newEUR10.getCurrency());
	}

	@Test
	public void testToString() {
		String desiredMessage = "100.00 SEK";
		assertEquals(SEK100.toString(), desiredMessage);
	}

	@Test
	public void testGlobalValue() {
		Integer universalCurrency = (int)(EUR10.getAmount() * EUR10.getCurrency().getRate());

		assertEquals(universalCurrency, EUR10.universalValue());
	}

	@Test
	public void testEqualsMoney() {
		Money newEUR10 = new Money(1000, EUR);
		Money SEK100 = new Money(10000, SEK);

		assertTrue(newEUR10.equals(EUR10));
		assertNotEquals(newEUR10, EUR10);

		assertTrue(newEUR10.equals(SEK100));
	}

	@Test
	public void testAdd() {
		Money result = new Money((int)((EUR10.universalValue() + SEK100.universalValue()) / EUR.getRate()), EUR);
		assertEquals(result.getAmount(), EUR10.add(SEK100).getAmount());
	}

	@Test
	public void testSub() {
		Money result = new Money((int)((EUR20.universalValue() - SEK100.universalValue()) / EUR.getRate()), EUR);
		assertEquals(result.getAmount(), EUR20.sub(SEK100).getAmount());
	}

	@Test
	public void testIsZero() {
		assertTrue(EUR0.isZero());
		assertFalse(EUR10.isZero());
	}

	@Test
	public void testNegate() {
		int result = -10;

		assertEquals(result * 100, EUR10.negate().getAmount().intValue());
		assertNotEquals(result * 100, SEK100.negate().getAmount().intValue());
		assertNotEquals(SEK100, SEK100.negate());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, EUR10.compareTo(SEK100));
		assertTrue(SEK100.compareTo(EUR20) < 0);
		assertTrue(EUR20.compareTo(SEKn100) > 0);
	}
}

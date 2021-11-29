package b_Money;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class CurrencyTest {
	Currency SEK, DKK, NOK, EUR;
	
	@Before
	public void setUp() throws Exception {
		/* Setup currencies with exchange rates */
		SEK = new Currency("SEK", 0.15);
		DKK = new Currency("DKK", 0.20);
		EUR = new Currency("EUR", 1.5);
	}

	@Test
	public void testGetName() {
		String name = "USD";

		Currency USD = new Currency("USD", 1.2);

		assertEquals(name, USD.getName());
		assertNotEquals("name", USD.getName());
	}
	
	@Test
	public void testGetRate() {
		Double rate = 1.2;
		Currency USD = new Currency("USD", rate);

		assertEquals(rate, USD.getRate());
	}
	
	@Test
	public void testSetRate() {
		Double rate = 1.2;

		Currency USD = new Currency("USD", rate);
		assertEquals(rate, USD.getRate());
		assertNotEquals(1.5, USD.getRate());

		rate = 1.5;
		USD.setRate(rate);
		assertEquals(rate, USD.getRate());
		assertNotEquals(1.2, USD.getRate());
	}
	
	@Test
	public void testGlobalValue() {
		Integer amount = 100;
		Integer universalCurrency = (int)(100 * 1.5);

		assertEquals(universalCurrency, EUR.universalValue(amount));
	}
	
	@Test
	public void testValueInThisCurrency() {
		Integer expected = (int)(100 * SEK.getRate()/EUR.getRate());
		assertEquals(expected, EUR.valueInThisCurrency(100, SEK));
	}

}

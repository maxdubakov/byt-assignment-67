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
		// define expected name
		String name = "USD";

		// create an object of Currency class with defined name
		Currency USD = new Currency("USD", 1.2);

		// checking if the name is rightly assigned
		assertEquals(name, USD.getName());
		assertNotEquals("name", USD.getName());
	}
	
	@Test
	public void testGetRate() {

		// define expected rate
		Double rate = 1.2;

		// create an object of Currency class with defined rate
		Currency USD = new Currency("USD", rate);

		// checking if the rate is rightly assigned
		assertEquals(rate, USD.getRate());
	}
	
	@Test
	public void testSetRate() {
		// define expected rate
		Double rate = 1.2;

		// create an object of Currency class with defined rate
		Currency USD = new Currency("USD", rate);

		// checking if the rate is rightly assigned
		assertEquals(rate, USD.getRate());
		assertNotEquals(1.5, USD.getRate());

		// define a new rate
		rate = 1.5;

		// set a new rate to USD currency
		USD.setRate(rate);

		// checking if the rate is rightly set
		assertEquals(rate, USD.getRate());
		assertNotEquals(1.2, USD.getRate());
	}
	
	@Test
	public void testGlobalValue() {
		// define and calculate universal currency
		Integer amount = 100;
		Integer universalCurrency = (int)(100 * 1.5);

		// check if expected amount is the same as calculated by the Currency EUR with the same amount (=100)
		assertEquals(universalCurrency, EUR.universalValue(amount));
	}
	
	@Test
	public void testValueInThisCurrency() {
		// define appropriate formula for converting from SEK to EUR
		Integer expected = (int)(100 * SEK.getRate()/EUR.getRate());

		// checking if class Currency is using the same formula
		assertEquals(expected, EUR.valueInThisCurrency(100, SEK));
	}

}

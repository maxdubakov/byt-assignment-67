package b_Money;

import java.util.Hashtable;

public class Account {
	// Bug fix: introduced final field name (since, we pass it to the constructor)
	private final String name;
	private Money content;

	// Bug fix: made timedpayments final
	private final Hashtable<String, TimedPayment> timedpayments = new Hashtable<>();

	Account(String name, Currency currency) {
		this.name = name; // Bug fix: remembering name of the account
		this.content = new Money(0, currency);
	}

	/**
	 * Add a timed payment
	 * @param id Id of timed payment
	 * @param interval Number of ticks between payments
	 * @param next Number of ticks till first payment
	 * @param amount Amount of Money to transfer each payment
	 * @param tobank Bank where receiving account resides
	 * @param toaccount Id of receiving account
	 */
	public void addTimedPayment(String id, Integer interval, Integer next, Money amount, Bank tobank, String toaccount) {
		TimedPayment tp = new TimedPayment(interval, next, amount, this, tobank, toaccount);
		timedpayments.put(id, tp);
	}
	
	/**
	 * Remove a timed payment
	 * @param id Id of timed payment to remove
	 */
	public void removeTimedPayment(String id) {
		timedpayments.remove(id);
	}
	
	/**
	 * Check if a timed payment exists
	 * @param id Id of timed payment to check for
	 */
	public boolean timedPaymentExists(String id) {
		return timedpayments.containsKey(id);
	}

	/**
	 * A time unit passes in the system
	 */
	public void tick() {
		for (TimedPayment tp : timedpayments.values()) {
			tp.tick(); // Bug fix: removed double tick(). Only one tick() should be called
		}
	}
	
	/**
	 * Deposit money to the account
	 * @param money Money to deposit.
	 */
	public void deposit(Money money) {
		content = content.add(money);
	}
	
	/**
	 * Withdraw money from the account
	 * @param money Money to withdraw.
	 */
	public void withdraw(Money money) {
		content = content.sub(money);
	}

	/**
	 * Get balance of account
	 * @return Amount of Money currently on account
	 */
	public Money getBalance() {
		return content;
	}

	// Bug fix: getter for "name" field
	public String getName() {
		return name;
	}

	/* Everything below belongs to the private inner class, TimedPayment */
	private static class TimedPayment {
		// Bug fix: made interval final
		private final int interval;

		private int next;
		// Bug fix: made fromaccount final
		private final Account fromaccount;

		// Bug fix: made account final
		private final Money amount;

		// Bug fix: made tobank final
		private final Bank tobank;

		// Bug fix: made toaccount final
		private final String toaccount;
		
		TimedPayment(Integer interval, Integer next, Money amount, Account fromaccount, Bank tobank, String toaccount) {
			this.interval = interval;
			this.next = next;
			this.amount = amount;
			this.fromaccount = fromaccount;
			this.tobank = tobank;
			this.toaccount = toaccount;
		}

		/* Return value indicates whether or not a transfer was initiated */
		public Boolean tick() {
			if (next == 0) {
				next = interval;

				fromaccount.withdraw(amount);
				try {
					tobank.deposit(toaccount, amount);
				}
				catch (AccountDoesNotExistException e) {
					/* Revert transfer.
					 * In reality, this should probably cause a notification somewhere. */
					fromaccount.deposit(amount);
				}
				return true;
			}
			else {
				next--;
				return false;
			}
		}
	}
}

package com.domainlanguage.money;

import java.math.*;
import java.util.Currency;


public class Money implements Comparable{
	private BigInteger amount;
	private Currency currency;

	public static Money valueOf(double dblAmount, Currency currency) {
		BigInteger amount = BigInteger.valueOf(Math.round (dblAmount * 100));
		return new Money(amount, currency);
	}

	public static Money valueOf (long longAmount, Currency currency) {
		BigInteger amount = BigInteger.valueOf(longAmount * 100);
		return new Money(amount, currency);
	}

	public static Money valueOf (BigInteger amount, Currency currency) {
		return new Money(amount, currency);
	}

	public Money plus (Money arg) {
		if (!isSameCurrencyAs(arg)) throw new IllegalArgumentException("Addition is not defined between different currencies");
		return Money.valueOf(amount.add(arg.amount), currency);
	}
	
	public Money minus (Money arg) {
		return this.plus(arg.negation());
	}
	
	boolean isSameCurrencyAs(Money arg) {
		return currency.equals(arg.currency);
	}

	private Money (BigInteger amountInPennies, Currency currency) {
		assert amountInPennies!=null;
		assert currency!=null;
		this.amount = amountInPennies;
		this.currency = currency;
	}
	
	public Money negation() {
		return new Money (amount.negate(), currency);
	}

	//TODO Shouldn't "amount" return BigInteger?
	public double amount() {
		return amount.doubleValue() / 100;
	}
	
	public Currency currency() {
		return currency;
	}

	public int compareTo (Object arg) {
		Money moneyArg = (Money) arg;
		if (!isSameCurrencyAs(moneyArg)) throw new IllegalArgumentException("Comparison is not defined between different currencies");
		return amount.compareTo(moneyArg.amount);
	}

	public static Money dollars(double amount) {
		return Money.valueOf(amount, Currency.getInstance("USD"));
	}

	public boolean equals(Object arg) {
		if (!(arg instanceof Money)) return false;
		Money other = (Money) arg;
		return (currency.equals(other.currency) && (amount.equals(other.amount)));
	}

	public boolean greaterThan(Money arg) {
		return (this.compareTo(arg) == 1);
	}

	public boolean lessThan(Money arg) {
		return (this.compareTo(arg) == -1);
	}

	public int hashCode() {
		return amount.hashCode();
	}

	public boolean isNegative() {
		return (amount.compareTo(BigInteger.ZERO) == -1);
	}
	
	public boolean isPositive() {
		return (amount.compareTo(BigInteger.ZERO) == 1);
	}
	
	public boolean isZero() {
		return amount.signum() == 0;
	}

//	public String localString() {
//		return currency.getFormat().format(amount());
//	}

	public Money multiply (double arg) {
		return Money.valueOf(amount() * arg, currency);
	}

	public String toString() {
		return currency.toString() + " " + amount();
	}

	public Money[] divide(int denominator) {
		BigInteger bigDenominator = BigInteger.valueOf(denominator);
		Money[] result = new Money[denominator];
		BigInteger simpleResult = amount.divide(bigDenominator);
		for (int i = 0; i < denominator ; i++) {
			result[i] = new Money(simpleResult, currency);
		}
		int remainder = amount.subtract(simpleResult.multiply(bigDenominator)).intValue();
		for (int i=0; i < remainder; i++) {
			result[i] = result[i].plus(new Money(BigInteger.valueOf(1), currency));
		}
		return result;
  	}
}

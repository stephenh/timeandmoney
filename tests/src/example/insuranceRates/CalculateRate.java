/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.insuranceRates;

import com.domainlanguage.basic.*;
import com.domainlanguage.time.*;
import com.domainlanguage.money.Money;
import com.domainlanguage.money.Proration;

import junit.framework.TestCase;

public class CalculateRate extends TestCase {
	
	public void testLookUpRate() {
		CalendarDate policyEffectiveDate = CalendarDate.date(2004, 11, 1);
		CalendarDate birthdate = CalendarDate.date(1962, 3, 5);
		Duration ageOnEffectiveDate = birthdate.through(policyEffectiveDate).lengthInMonths();
		Money monthlyPremium = (Money)insuranceSchedule().get(ageOnEffectiveDate);
		assertEquals(Money.dollars(150.00), monthlyPremium);
	}
	
	public void testProrateFirstMonth() {
		Money monthlyPremium = Money.dollars(150.00);
		CalendarDate effectiveDateOfPolicy = CalendarDate.date(2004, 11, 7);
		CalendarInterval entireMonth = effectiveDateOfPolicy.month();
		CalendarInterval remainderOfMonth = effectiveDateOfPolicy.through(entireMonth.end());
		
		Proration proration = new Proration();
		Money firstPayment = proration.partOfWhole(monthlyPremium, remainderOfMonth.lengthInDaysInt(), entireMonth.lengthInDaysInt());
		assertEquals(Money.dollars(120.00), firstPayment);

		//Alternative, equivalent calculation
		firstPayment = proration.partOfWhole(monthlyPremium, remainderOfMonth.length().dividedBy(entireMonth.length()));
		assertEquals(Money.dollars(120.00), firstPayment);

	}
/*
 * 
 	public void testLookUpMoreComplicated() {
		BusinessCalendar paymentCalendar = null;
		CalendarInterval paymentQuarter = paymentCalendar.currentQuarter();

				CalendarDate birthdate = null;
				Duration age = birtdate.until(paymentQuarter.start()).duration();
				Rate rate = insuranceSchedule.get(age);
		Money quarterlyPayment = rate.times(Duration.quarters(1));
		CalendarDate effectiveDate = null;
		CalendarInterval remainingQuarter = paymentQuarter.cropForwardFrom(effectiveDate);
		BigDecimal ratio = remainingQuarter.duration().dividedBy(paymentQuarter);
		Money firstPayment = quarterlyPayment.prorate(ratio);
	}
*/

	private IntervalMap insuranceSchedule() {
		Interval age25_35 = Interval.over(Duration.years(25), true, Duration.years(35), false);
		Interval age35_45 = Interval.over(Duration.years(35), true, Duration.years(45), false);
		Interval age45_55 = Interval.over(Duration.years(45), true, Duration.years(55), false);
		Interval age55_65 = Interval.over(Duration.years(55), true, Duration.years(65), false);
		
		IntervalMap schedule = new LinearIntervalMap();
		schedule.put(age25_35, Money.dollars(100.00));
		schedule.put(age35_45, Money.dollars(150.00));
		schedule.put(age45_55, Money.dollars(200.00));
		schedule.put(age55_65, Money.dollars(250.00));
		return schedule;
	}
	
}

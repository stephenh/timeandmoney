/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.insuranceRates;

import com.domainlanguage.basic.*;
import com.domainlanguage.time.*;
import com.domainlanguage.money.Money;

import junit.framework.TestCase;

public class CalculateRate extends TestCase {
	
	public void testLookUpRate() {
		CalendarDate policyEffectiveDate = CalendarDate.date(2004, 11, 1);
		CalendarDate birthdate = CalendarDate.date(1962, 3, 5);
		Duration ageOnEffectiveDate = birthdate.through(policyEffectiveDate).lengthInMonths();
		Money monthlyPremium = (Money)insuranceSchedule().get(ageOnEffectiveDate);
		assertEquals(Money.dollars(150.00), monthlyPremium);
	}
/*
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
		ComparableInterval age25_35 = ComparableInterval.over(Duration.years(25), true, Duration.years(35), false);
		ComparableInterval age35_45 = ComparableInterval.over(Duration.years(35), true, Duration.years(45), false);
		ComparableInterval age45_55 = ComparableInterval.over(Duration.years(45), true, Duration.years(55), false);
		ComparableInterval age55_65 = ComparableInterval.over(Duration.years(55), true, Duration.years(65), false);
		
		IntervalMap schedule = new LinearIntervalMap();
		schedule.put(age25_35, Money.dollars(100.00));
		schedule.put(age35_45, Money.dollars(150.00));
		schedule.put(age45_55, Money.dollars(200.00));
		schedule.put(age55_65, Money.dollars(250.00));
		return schedule;
	}
	
}

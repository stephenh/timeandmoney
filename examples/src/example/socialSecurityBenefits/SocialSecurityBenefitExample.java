/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.socialSecurityBenefits;

import java.math.BigDecimal;

import com.domainlanguage.basic.Ratio;
import com.domainlanguage.money.*;
import com.domainlanguage.time.*;

import junit.framework.TestCase;


public class SocialSecurityBenefitExample extends TestCase {
	/*
	 * Money calculations often must follow seemingly arbitrary
	 * rules, and the intricate computations must be exact. Real-world,
	 * public examples are provided courtesy of our Social Security
	 * Agency.
	 * 
	 * The US Social Security regulation 404.430 has the following
	 * example calculations.
	 * (see http://www.ssa.gov/OP_Home/cfr20/404/404-0430.htm)
	 * 
	 * The examples are 25 years old, but the regs are current. 
	 */
	 	
	public void xtestExcessEarnings() {
	/*
	 * Example 1. The self-employed beneficiary attained age 72 
	 * in July 1979. His net earnings for 1979, his taxable year, 
	 * were $12,000. The pro rata share of the net earnings for the 
	 * period prior to July is $6,000. His excess earnings for 1979 
	 * for retirement test purposes are $750. This is computed by 
	 * subtracting $4,500 ($375×12), the exempt amount for 1979, 
	 * from $6,000 and dividing the result by 2.
	*/
		
		//Does beneficiary attain age 72 during the benefit year 1972?
		CalendarInterval y1979 = CalendarInterval.year(1979);
		CalendarDate birthday72 = CalendarDate.date(1979, 7, 15);
		assertTrue(y1979.covers(birthday72));

		//Note that all calculations are based on entire months.
		//The proration is not based on the number of days prior to
		// turning 72 (the exempt age). It is base on the number
		// of months prior to the month in which he turned 72.
		
		CalendarInterval subintervalOfYearSubjectToExcess = CalendarInterval.inclusive(
			y1979.start(),
			birthday72.firstOfMonth().previousDay()
		);
		
		Ratio portionOfYearSubject = 
			subintervalOfYearSubjectToExcess.lengthInMonths().dividedBy(y1979.lengthInMonths());
		
		Money earningsFor1979 = Money.dollars(12000);
		//assertEquals(Money.dollars(6000), netEarningsPriorToMonthOfTurning72);

		Money exemptMonthlyEarnings = Money.dollars(375);
		Money exemptAnnualEarnings = exemptMonthlyEarnings.times(12);
		assertEquals(Money.dollars(4500), exemptAnnualEarnings);
		Money annualExcessEarnings = earningsFor1979.minus(exemptAnnualEarnings);
		
		// Money excessEarnings = earningsFor1979.minus(exemptEarnings);
//		assertEquals(Money.dollars(750), excessEarnings);
	}
	
	/*
	 * 404.439 Partial monthly benefits; excess earnings of the 
	 * individual charged against his benefits and the benefits of 
	 * persons entitled (or deemed entitled) to benefits on his earnings 
	 * record.
	 * (see http://www.ssa.gov/OP_Home/cfr20/404/404-0439.htm)
	 */
	
	public void testDeductionsFromFamilyBenefits() {
		/**
		 * Example: A is entitled to an old-age insurance benefit of 
		 * $165 and his wife is entitled to $82.50 before rounding, 
		 * making a total of $247.50. After A's excess earnings have 
		 * been charged to the appropriate months, there remains a 
		 * partial benefit of $200 payable for October, which is 
		 * apportioned as follows After deductions for excess earnings 
		 * and after rounding per §404.304(f).:
		 * 
		 *    Original benefit   Fraction   Benefit
		 * A      $165               2/3       $133
		 * Spouse   82.50            1/3         66
		 * Total   247.50                       199
		 * 
		 */
		Proration proration = new Proration();
		Money totalBenefit = Money.dollars(247.50);
		Money benefit = Money.dollars(200);
		Ratio workerShare = Ratio.of(2, 3);
		Ratio spouseShare = Ratio.of(1, 3);
		Money workerBenefit = benefit.applying(workerShare, 0, BigDecimal.ROUND_DOWN);
		Money spouseBenefit = benefit.applying(spouseShare, 0, BigDecimal.ROUND_DOWN);

		assertEquals(Money.dollars(133), workerBenefit);
		assertEquals(Money.dollars(66), spouseBenefit);
		
	}
}

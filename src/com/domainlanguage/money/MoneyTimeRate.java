/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.money;

import java.math.*;
import java.util.*;

import com.domainlanguage.time.*;
import com.domainlanguage.util.*;

public class MoneyTimeRate {
    private TimeRate rate;
    private Currency currency;
    
    public MoneyTimeRate(Money money, Duration duration) {
        rate=new TimeRate(money.amount, duration);
        currency=money.currency;
    }

    public Money over(Duration duration) {
        return over(duration, BigDecimal.ROUND_UNNECESSARY);
    }
    public Money over(Duration duration, int roundRule) {
        return over(duration, rate.scale(), roundRule);
    }
    public Money over(Duration duration, int scale, int roundRule) {
        return Money.valueOf(rate.over(duration, scale, roundRule), currency);
    }
    public boolean equals(Object another) {
        if (!TypeCheck.sameClassOrBothNull(this, another))
            return false;
        MoneyTimeRate anotherRate=(MoneyTimeRate)another;
        return this.rate.equals(anotherRate.rate) && this.currency.equals(anotherRate.currency);
    }
    public String toString() {
        return rate.toString();
    }
}

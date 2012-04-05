/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.Iterator;

import com.domainlanguage.util.ImmutableIterator;

public abstract class AnnualDateSpecification extends DateSpecification {

  public abstract CalendarDate ofYear(int year);

  @Override
  public CalendarDate firstOccurrenceIn(CalendarInterval interval) {
    CalendarDate firstTry = ofYear(interval.start().getYear());
    if (interval.includes(firstTry)) {
      return firstTry;
    }
    CalendarDate secondTry = ofYear(interval.start().getYear() + 1);
    if (interval.includes(secondTry)) {
      return secondTry;
    }
    return null;
  }

  @Override
  public Iterator<CalendarDate> iterateOver(final CalendarInterval interval) {
    final AnnualDateSpecification spec = this;
    return new ImmutableIterator<CalendarDate>() {
      CalendarDate next = AnnualDateSpecification.this.firstOccurrenceIn(interval);
      int year = next.getYear();

      public boolean hasNext() {
        return next != null;
      }

      public CalendarDate next() {
        if (next == null) {
          return null;
        }
        CalendarDate current = next;
        year += 1;
        next = spec.ofYear(year);
        if (!interval.includes(next)) {
          next = null;
        }
        return current;
      }
    };
  }
}

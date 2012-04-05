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
    CalendarDate firstTry = this.ofYear(interval.start().getYear());
    if (interval.includes(firstTry)) {
      return firstTry;
    }
    CalendarDate secondTry = this.ofYear(interval.start().getYear() + 1);
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
      int year = this.next.getYear();

      public boolean hasNext() {
        return this.next != null;
      }

      public CalendarDate next() {
        if (this.next == null) {
          return null;
        }
        CalendarDate current = this.next;
        this.year += 1;
        this.next = spec.ofYear(this.year);
        if (!interval.includes(this.next)) {
          this.next = null;
        }
        return current;
      }
    };
  }
}

/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

class FixedDateSpecification extends AnnualDateSpecification {

  private final int month;
  private final int day;

  FixedDateSpecification(int month, int day) {
    this.month = month;
    this.day = day;
  }

  @Override
  public CalendarDate ofYear(int year) {
    return CalendarDate.date(year, this.month, this.day);
  }

  @Override
  public boolean isSatisfiedBy(CalendarDate date) {
    return this.day == date.getDay() && this.month == date.getMonth();
  }

}

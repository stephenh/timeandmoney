/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

class FloatingDateSpecification extends AnnualDateSpecification {

  private final int month;
  private final int dayOfWeek;
  private final int occurrence;

  FloatingDateSpecification(int month, int dayOfWeek, int occurrence) {
    this.month = month;
    this.dayOfWeek = dayOfWeek;
    this.occurrence = occurrence;
  }

  @Override
  public CalendarDate ofYear(int year) {
    CalendarDate firstOfMonth = CalendarDate.date(year, this.month, 1);
    int dayOfWeekOffset = this.dayOfWeek - firstOfMonth.dayOfWeek();
    int dateOfFirstOccurrenceOfDayOfWeek = dayOfWeekOffset + (dayOfWeekOffset < 0 ? 8 : 1);
    int date = ((this.occurrence - 1) * 7) + dateOfFirstOccurrenceOfDayOfWeek;
    return CalendarDate.date(year, this.month, date);
  }

  @Override
  public boolean isSatisfiedBy(CalendarDate date) {
    return this.ofYear(date.getYear()).equals(date);
  }
}

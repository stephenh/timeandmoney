/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

class FixedDateSpecification extends AnnualDateSpecification {
    private int month;
    private int day;

    FixedDateSpecification(int month, int day) {
        this.month = month;
        this.day = day;
    }

    public CalendarDate ofYear(int year) {
        return CalendarDate.date(year, month, day);
    }

    public boolean isSatisfiedBy(CalendarDate date) {
        return day == date.getDay() && month == date.getMonth();
    }

    //for persistent mapping
    FixedDateSpecification() {
    }
    private int getDayForPersistentMapping() {
        return day;
    }

    private void setDayForPersistentMapping(int day) {
        this.day = day;
    }

    private int getMonthForPersistentMapping() {
        return month;
    }

    private void setMonthForPersistentMapping(int month) {
        this.month = month;
    }

}
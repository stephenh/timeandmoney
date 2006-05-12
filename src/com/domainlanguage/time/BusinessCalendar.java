/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

import com.domainlanguage.util.*;

public class BusinessCalendar {
    private Set holidays;

    static BusinessCalendar defaultBusinessCalendar() {
        BusinessCalendar calendar = new BusinessCalendar();
        calendar.addHolidays(defaultHolidays());
        return calendar;
    }

    /** Should be rewritten for each particular organization */
    static Set defaultHolidays() {
        return new HashSet();
    }

    public BusinessCalendar() {
        holidays = new HashSet();
    }
    public void addHolidays(Set days) {
        holidays.addAll(days);
    }

    public int getElapsedBusinessDays(CalendarInterval interval) {
        int tally = 0;
        for (Iterator iterator = interval.daysIterator(); iterator.hasNext();)
            if (isBusinessDay((CalendarDate) iterator.next()))
                tally += 1;
        return tally;
    }

    public CalendarDate nearestBusinessDay(CalendarDate day) {
        CalendarDate current = day;
        while (!isBusinessDay(current))
            current = current.plusDays(1);
        return current;
    }

    public boolean isHoliday(CalendarDate day) {
        return holidays.contains(day);
    }

    public boolean isWeekend(CalendarDate day) {
        Calendar calday = day.asJavaCalendarUniversalZoneMidnight();
        return (calday.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) || (calday.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY);
    }

    public boolean isBusinessDay(CalendarDate day) {
        return !isWeekend(day) && !isHoliday(day);
    }

    public Iterator businessDaysIterator(CalendarInterval anInterval) {
        final CalendarInterval interval = anInterval;
        return new ImmutableIterator() {
            CalendarDate next = interval.start();

            public boolean hasNext() {
                return interval.includes(next);
            }

            public Object next() {
                Object current = next;
                next = nearestBusinessDay(next.nextDay());
                return current;
            }
        };
    }



    /*
     * boolean isBusinessHours(TimePoint now) { Calendar date =
     * now.asJavaCalendar(); int theHour = date.get(Calendar.HOUR_OF_DAY); int
     * theMinute = date.get(Calendar.MINUTE); int timeAsMinutes = (theHour * 60) +
     * theMinute; return timeAsMinutes >= openForBusiness && timeAsMinutes <=
     * closeForBusiness; }
     * 
     * boolean isInBusiness(TimePoint point) { return isBusinessDay(point) &&
     * isBusinessHours(point); }
     * 
     * Returns true if <now> is a holiday. An alternative to using
     * <Holidays.ALL>
     * 
     * It makes no effort to recognize "half-day holidays", such as the
     * Wednesday before Thanksgiving. Currently, it only recognizes these
     * holidays: New Year's Day MLK Day President's Day Memorial Day
     * Independence Day Labor Day Thanksgiving Christmas
     * 
     * 
     * boolean isFederalHoliday(TimePoint point) { Calendar javaCal =
     * point.asJavaCalendar(); int[] month_date = { Calendar.JANUARY, 1,
     * Calendar.JULY, 4, Calendar.DECEMBER, 25, }; int[] month_weekday_monthweek = {
     * Calendar.JANUARY, Calendar.MONDAY, 3, // MLK Day, 3rd monday in Jan
     * Calendar.FEBRUARY, Calendar.MONDAY, 3, // President's day
     * Calendar.SEPTEMBER, Calendar.MONDAY, 1, // Labor day Calendar.NOVEMBER,
     * Calendar.THURSDAY, 4, // Thanksgiving };
     *  // Columbus Day is a federal holiday. // it is the second Monday in
     * October int mm = javaCal.get(Calendar.MONTH); int dd =
     * javaCal.get(Calendar.DAY_OF_MONTH); int dw =
     * javaCal.get(Calendar.DAY_OF_WEEK); int wm =
     * javaCal.get(Calendar.WEEK_OF_MONTH);
     *  // go over the month/day-of-month entries, return true on full match for
     * (int i = 0; i < month_date.length; i += 2) { if ((mm == month_date[i +
     * 0]) && (dd == month_date[i + 1])) return true; }
     *  // go over month/weekday/week-of-month entries, return true on full
     * match for (int i = 0; i < month_weekday_monthweek.length; i += 3) { if
     * ((mm == month_weekday_monthweek[i + 0]) && (dw ==
     * month_weekday_monthweek[i + 1]) && (wm == month_weekday_monthweek[i +
     * 2])) return true; }
     * 
     * if ((mm == Calendar.MAY) && (dw == Calendar.MONDAY) && (wm ==
     * javaCal.getMaximum(Calendar.WEEK_OF_MONTH))) // last week in May return
     * true;
     * 
     * return false; }
     */
    
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private Set getForPersistentMapping_Holidays() {
        return holidays;
    }
    //Only for use by persistence mapping frameworks
    //<rant>These methods break encapsulation and we put them in here begrudgingly</rant>
    private void setForPersistentMapping_Holidays(Set holidays) {
        this.holidays = holidays;
    }
}
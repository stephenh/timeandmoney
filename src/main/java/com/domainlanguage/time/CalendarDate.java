/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.Serializable;
import java.util.Calendar;
import java.util.TimeZone;

public class CalendarDate implements Comparable<CalendarDate>, Serializable {

	private static final int MAX_YEAR = 9999;
	private final int year;
	private final int month; // 1 based: January = 1, February = 2, ...
	private final int day;
	
    public static CalendarDate date(int year, int month, int day) {
        return CalendarDate.from(year, month, day);
    }
    
	public static CalendarDate from(int year, int month, int day) {
		CalendarDate result =  new CalendarDate(year, month, day);
		return result;

	}
	
	public static CalendarDate from(String dateString, String pattern) {
		TimeZone arbitraryZone = TimeZone.getTimeZone("Universal"); 
		//Any timezone works, as long as the same one is used throughout.
		TimePoint point = TimePoint.parseFrom(dateString, pattern, arbitraryZone);
		return CalendarDate.from(point, arbitraryZone);
	}
	
	public static CalendarDate from(TimePoint timePoint, TimeZone zone) {
		Calendar calendar = timePoint.asJavaCalendar();
		calendar.setTimeZone(zone);
		return CalendarDate._from(calendar);
	}

	static CalendarDate _from(Calendar calendar) {
		//Use timezone already set in calendar.
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1; //T&M Lib counts January
                                                      // as 1
		int date = calendar.get(Calendar.DATE);
		return CalendarDate.from(year, month, date);
	}

	public static CalendarDate max(CalendarDate d1, CalendarDate d2) {
		if (d1.isAfter(d2)) {
			return d1;
		} else {
			return d2;
		}
	}

	public static CalendarDate min(CalendarDate d1, CalendarDate d2) {
		if (d1.isBefore(d2)) {
			return d1;
		} else {
			return d2;
		}
	}

	CalendarDate(int year, int month, int day) {
		this.year = year > MAX_YEAR ? MAX_YEAR : year;
		this.month = month;
		this.day = day;
	}

	@Override
	public String toString() {
		return toString("yyyy-M-d"); //default for console
	}

	public String toString(String pattern) {
		TimeZone arbitraryZone = TimeZone.getTimeZone("Universal"); 
		//Any timezone works, as long as the same one is used throughout.
		TimePoint point = startAsTimePoint(arbitraryZone);
		return point.toString(pattern, arbitraryZone);
	}
	
	public boolean isBefore(CalendarDate other) {
        if (other == null)
            return false;
		if (year < other.year) return true;
		if (year > other.year) return false;
		if (month < other.month) return true;
		if (month > other.month) return false;
		return day < other.day;
	}

	public boolean isAfter(CalendarDate other) {
        if (other == null)
            return false;
		return !isBefore(other) && !this.equals(other);
	}

	public boolean isOnOrBefore(CalendarDate other) {
		return !isAfter(other);
	}

	public boolean isOnOrAfter(CalendarDate other) {
		return !isBefore(other);
	}

	public boolean in(CalendarInterval interval) {
		return interval.includes(this);
	}

	public boolean notIn(CalendarInterval interval) {
		return !interval.includes(this);
	}

	@Override
    public int compareTo(CalendarDate other) {
        if (other == null) return -1;
        if (isBefore(other)) return -1;
        if (isAfter(other)) return 1;
        return 0;
    }

	@Override
	public boolean equals(Object object) {
        try {
            return equals((CalendarDate) object);
        } catch(ClassCastException ex) {
            return false;
        }
	}
    public boolean equals(CalendarDate other) {
        return 
            other != null &&
            this.year == other.year &&
            this.month == other.month &&
            this.day == other.day;
    }

	@Override
	public int hashCode() {
		return year * month * day;
	}

	public CalendarDate start() {
		return this;
	}

	public CalendarDate end() {
		return this;
	}

	public CalendarDate nextDay() {
		return this.plusDays(1);
	}
	
	public CalendarDate previousDay() {
		return this.plusDays(-1);
	}
	
    /**
     * @deprecated (2006-03-23)
     * Use calendarDate.month().start() instead.
     */
	public CalendarDate firstOfMonth() {
		return month().start();
	}
	
    /**
     * @deprecated (2006-03-23)
     * Use calendarDate.month().end() instead.
     */
	public CalendarDate lastOfMonth() {
		return month().end();
	}

	public CalendarInterval month() {
		return CalendarInterval.month(year, month);
	}
	
	public CalendarInterval year() {
		return CalendarInterval.year(year);
	}
	
	public CalendarDate plusDays(int increment) {
		Calendar calendar = asJavaCalendarUniversalZoneMidnight();
		calendar.add(Calendar.DATE, increment);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		return CalendarDate.from(year, month, day);
	}

	public CalendarDate plusMonths(int increment) {
		Calendar calendar = asJavaCalendarUniversalZoneMidnight();
		calendar.add(Calendar.MONTH, increment);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DATE);
		return CalendarDate.from(year, month, day);
	}
	
	public CalendarDate plus(Duration length) {
		return length.addedTo(this);
	}
	
	Calendar asJavaCalendarUniversalZoneMidnight() {
		TimeZone zone = TimeZone.getTimeZone("Universal");
		Calendar calendar = Calendar.getInstance(zone);
		calendar.set(Calendar.YEAR, year);
		calendar.set(Calendar.MONTH, month-1);
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar;
	}
	
	public TimeInterval asTimeInterval(TimeZone zone) {
		return TimeInterval.startingFrom(startAsTimePoint(zone), true, Duration.days(1), false);
	}
	
	public TimePoint startAsTimePoint(TimeZone zone) {
        return TimePoint.atMidnight(year, month, day, zone);
	}

	public CalendarInterval through(CalendarDate otherDate) {
		return CalendarInterval.inclusive(this, otherDate);
	}

	public int dayOfWeek() {
		Calendar calendar = asJavaCalendarUniversalZoneMidnight();
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	public int dayOfMonth() {
		Calendar calendar = asJavaCalendarUniversalZoneMidnight();
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

    public int weekOfMonth() {
        Calendar calendar = asJavaCalendarUniversalZoneMidnight();
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    public int weekOfYear() {
        Calendar calendar = asJavaCalendarUniversalZoneMidnight();
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    public int occurrenceOfDayInMonth() {
        Calendar calendar = asJavaCalendarUniversalZoneMidnight();
        return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    public boolean isWeekend() {
        int dayOfWeek = this.dayOfWeek();
        return (dayOfWeek == Calendar.SUNDAY || dayOfWeek == Calendar.SATURDAY);
    }

    public boolean isWeekday() {
        return !this.isWeekend();
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public CalendarMinute at(TimeOfDay timeOfDay) {
        return CalendarMinute.dateAndTimeOfDay(this, timeOfDay);
    }



    
	
}

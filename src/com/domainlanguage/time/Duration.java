/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

public class Duration implements Comparable {
	public static final Duration NONE = milliseconds(0);

	final long quantity;
	final TimeUnit unit;
	
	private static Duration of(long howMany, TimeUnit unit) {
		return new Duration(howMany, unit);
	}

	public static Duration milliseconds(long howMany) {
		return Duration.of(howMany, TimeUnit.millisecond);
	}

	public static Duration seconds(int howMany) {
		return Duration.of(howMany, TimeUnit.second);
	}
	
	public static Duration minutes(int howMany) {
		return Duration.of(howMany, TimeUnit.minute);
	}
	
	public static Duration hours(int howMany) {
		return Duration.of(howMany, TimeUnit.hour);
	}
	
	public static Duration days(int howMany) {
		return Duration.of(howMany, TimeUnit.day);
	}
	
	public static Duration daysHoursMinutesSecondsMillis(int days, int hours, int minutes, int seconds, long milliseconds) {
		Duration result = Duration.days(days);
		if (! (hours == 0)) result = result.plus(Duration.hours(hours));
		if (! (minutes == 0)) result = result.plus(Duration.minutes(minutes));
		if (! (seconds == 0)) result = result.plus(Duration.seconds(seconds));
		if (! (milliseconds == 0)) result = result.plus(Duration.milliseconds(milliseconds));
		return result;
	}
	
	public static Duration weeks(int howMany) {
		return Duration.of(howMany, TimeUnit.week);
	}
	
	public static Duration months(int howMany) {
		return Duration.of(howMany, TimeUnit.month);
	}
	
	public static Duration quarters(int howMany) {
		return Duration.of(howMany, TimeUnit.quarter);
	}
	
	public static Duration years(int howMany) {
		return Duration.of(howMany, TimeUnit.year);
	}
	
	Duration(long howMany, TimeUnit unit) {
//		assert howMany >= 0;
		this.quantity = howMany;
		this.unit = unit;
	}
		
	long inBaseUnits() {
		return quantity * unit.factor;
	}
	
	/**
	 * TODO What SHOULD this do if the guard fails?
	 */
	public Duration plus(Duration other) {
		if (!other.unit.isConvertibleTo(this.unit)) return null;
		long newQuantity = this.inBaseUnits() + other.inBaseUnits();
		return new Duration(newQuantity, unit.baseUnit());
	}

	/**
	 * TODO What SHOULD this do if the guard fails?
	 * TODO What SHOULD happen if assertion fails?
	 */
	public Duration minus(Duration other) {
		if (!other.unit.isConvertibleTo(this.unit)) return null;
		assert this.compareTo(other) >= 0;
		long newQuantity = this.inBaseUnits() - other.inBaseUnits();
		return new Duration(newQuantity, unit.baseUnit());
	}
	
	public TimePoint addedTo(TimePoint point) {
		Calendar calendar = point.asJavaCalendar();
		calendar.add(unit.javaCalendarConstantForBaseType(), (int) inBaseUnits());
		return TimePoint.from(calendar);
	}

	public TimePoint subtractedFrom(TimePoint point) {
		Calendar calendar = point.asJavaCalendar();
		calendar.add(unit.javaCalendarConstantForBaseType(), -1 * (int) inBaseUnits());
		return TimePoint.from(calendar);
	}

	public CalendarDate addedTo(CalendarDate day) {
//		only valid for days and larger units		
		if (unit.compareTo(TimeUnit.day) < 0) return day;
		Calendar calendar = day._asJavaCalendarUniversalZoneMidnight();
		if (unit.equals(TimeUnit.day)) {
			calendar.add(Calendar.DATE, (int)quantity);
		} else {
			calendar.add(unit.javaCalendarConstantForBaseType(), (int) inBaseUnits());
		}
		return CalendarDate._from(calendar);
	}

	public CalendarDate subtractedFrom(CalendarDate day) {
//		only valid for days and larger units
		if (unit.compareTo(TimeUnit.day) < 0) return day;
		Calendar calendar = day._asJavaCalendarUniversalZoneMidnight();
		if (unit.equals(TimeUnit.day)) {
			calendar.add(Calendar.DATE, -1 * (int)quantity);
		} else {
			calendar.add(unit.javaCalendarConstantForBaseType(), -1 * (int) inBaseUnits());
		}
		return CalendarDate._from(calendar);
	}

	public boolean equals(Object arg) {
		if (!(arg instanceof Duration)) return false;
		Duration other = (Duration) arg;
		if (!this.unit.isConvertibleTo(other.unit)) return false;
		return this.inBaseUnits() == other.inBaseUnits();
	}
	
	public String toNormalizedString() {
		StringBuffer buffer = new StringBuffer();
		long remainder = inBaseUnits();
		TimeUnit[] units = unit.descendingUnits();
		boolean first = true;
		
		for (int i = 0; i < units.length; i++) {
			TimeUnit each = units[i];
			long portion = remainder / each.factor;
			
			if (portion > 0) {
				if (!first)
					buffer.append(", ");
				else
					first = false;
				buffer.append(each.toString(portion));
			}
			remainder = remainder % each.factor;
		}
		return buffer.toString();
	}

	public String toString() {
		//return toString(quantity, unit);
		return toNormalizedString();
	}
	
	private String toString(long quantity, TimeUnit unit) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(quantity);
			buffer.append(" ");
			buffer.append(unit);
			buffer.append(quantity == 1 ? "" : "s");
			return buffer.toString();
	}

	public int hashCode() {
		return (int) quantity;
	}
	/**
	 * TODO What should happen if units are not convertible?
	 */
	public int compareTo(Object arg) {
		Duration other = (Duration) arg;
		assert this.unit.isConvertibleTo(other.unit);
		long difference = this.inBaseUnits() - other.inBaseUnits();
		if (difference > 0) return 1;
		if (difference < 0) return -1;
		return 0;
	}
	
	/**
	 * Convenience method
	 */
	public TimeInterval startingFrom(TimePoint start) {
		return TimeInterval.from(start, this);
	}
			
}

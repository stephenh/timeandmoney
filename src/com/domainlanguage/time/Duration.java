/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.*;
import java.util.*;

import com.domainlanguage.basic.*;

public class Duration implements Comparable, Serializable {
	public static final Duration NONE = milliseconds(0);

	private long quantity;
	private TimeUnit unit;
	
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
	
	public static Duration daysHoursMinutesSecondsMilliseconds(int days, int hours, int minutes, int seconds, long milliseconds) {
		Duration result = Duration.days(days);
		if (hours != 0) result = result.plus(Duration.hours(hours));
		if (minutes != 0) result = result.plus(Duration.minutes(minutes));
		if (seconds != 0) result = result.plus(Duration.seconds(seconds));
		if (milliseconds != 0) result = result.plus(Duration.milliseconds(milliseconds));
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
	
	private static Duration of(long howMany, TimeUnit unit) {
		return new Duration(howMany, unit);
	}

	Duration(long quantity, TimeUnit unit) {
		assert quantity >= 0;
		this.quantity = quantity;
		this.unit = unit;
	}
    
	long inBaseUnits() {
		return quantity * unit.factor;
	}
	
	//TODO: What SHOULD happen if assertion fails?
	public Duration plus(Duration other) {
		assert other.unit.isConvertibleTo(this.unit);
		long newQuantity = this.inBaseUnits() + other.inBaseUnits();
		return new Duration(newQuantity, unit.baseUnit());
	}

	//TODO: What SHOULD happen if assertion fails?
	public Duration minus(Duration other) {
		assert other.unit.isConvertibleTo(this.unit);
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
		Calendar calendar = day.asJavaCalendarUniversalZoneMidnight();
		if (unit.equals(TimeUnit.day)) 
			calendar.add(Calendar.DATE, (int) quantity);
		else 
			calendar.add(unit.javaCalendarConstantForBaseType(), (int) inBaseUnits());
		return CalendarDate._from(calendar);
	}

	public CalendarDate subtractedFrom(CalendarDate day) {
//		only valid for days and larger units
		if (unit.compareTo(TimeUnit.day) < 0) return day;
		Calendar calendar = day.asJavaCalendarUniversalZoneMidnight();
		if (unit.equals(TimeUnit.day)) 
			calendar.add(Calendar.DATE, -1 * (int)quantity);
		else 
			calendar.add(unit.javaCalendarConstantForBaseType(), -1 * (int) inBaseUnits());
		return CalendarDate._from(calendar);
	}

	public Ratio dividedBy (Duration divisor) {
		assert unit.isConvertibleTo(divisor.unit);
		return Ratio.of(inBaseUnits(), divisor.inBaseUnits());
	}

	public boolean equals(Object arg) {
		if (!(arg instanceof Duration)) return false;
		Duration other = (Duration) arg;
		if (!this.unit.isConvertibleTo(other.unit)) return false;
		return this.inBaseUnits() == other.inBaseUnits();
	}
	
	public String toString() {
		return toNormalizedString(unit.descendingUnitsForDisplay());
	}

	public String toNormalizedString() {
		return toNormalizedString(unit.descendingUnits());
	}

	private String toNormalizedString(TimeUnit[] units) {
		StringBuffer buffer = new StringBuffer();
		long remainder = inBaseUnits();
		boolean first = true;		
		for (int i = 0; i < units.length; i++) {
			TimeUnit aUnit = units[i];
			long portion = remainder / aUnit.factor;			
			if (portion > 0) {
				if (!first)
					buffer.append(", ");
				else
					first = false;
				buffer.append(aUnit.toString(portion));
			}
			remainder = remainder % aUnit.factor;
		}
		return buffer.toString();
	}
	
	public int hashCode() {
		return (int) quantity;
	}
	
	//TODO: What should happen if units are not convertible?
	public int compareTo(Object arg) {
		Duration other = (Duration) arg;
		assert this.unit.isConvertibleTo(other.unit);
		long difference = this.inBaseUnits() - other.inBaseUnits();
		if (difference > 0) return 1;
		if (difference < 0) return -1;
		return 0;
	}
	
	public TimeInterval startingFrom(TimePoint start) {
		return TimeInterval.startingFrom(start, this);
	}

	public TimeInterval preceding(TimePoint end) {
		return TimeInterval.preceding(end, this);
	}
			
}

/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.Serializable;


class TimeUnit implements Comparable, Serializable {

	//Unit conversion factors
	static final int millisecondsPerSecond = 1000;
	static final int millisecondsPerMinute = 60 * millisecondsPerSecond;
	static final int millisecondsPerHour = 60 * millisecondsPerMinute;
	static final int millisecondsPerDay = 24 * millisecondsPerHour;
	static final int millisecondsPerWeek = 7 * millisecondsPerDay;

	static final int monthsPerQuarter = 3;
	static final int monthsPerYear = 12;

	
	//Defining the units
	static final TimeUnit millisecond = new TimeUnit(Type.millisecond, Type.millisecond, 1);
	static final TimeUnit second = new TimeUnit(Type.second, Type.millisecond, millisecondsPerSecond);
	static final TimeUnit minute = new TimeUnit(Type.minute, Type.millisecond, millisecondsPerMinute);
	static final TimeUnit hour = new TimeUnit(Type.hour, Type.millisecond, millisecondsPerHour);
	static final TimeUnit day = new TimeUnit(Type.day, Type.millisecond, millisecondsPerDay);
	static final TimeUnit week = new TimeUnit(Type.week, Type.millisecond, millisecondsPerWeek);
	static final TimeUnit[] descendingMillisecondBased = {week, day, hour, minute, second, millisecond};
	
	static final TimeUnit month = new TimeUnit(Type.month, Type.month, 1);
	static final TimeUnit quarter = new TimeUnit(Type.quarter, Type.month, monthsPerQuarter);
	static final TimeUnit year = new TimeUnit(Type.year, Type.month, monthsPerYear);
	static final TimeUnit[] descendingMonthBased = {year, quarter, month};

	
	final Type type;
	final Type baseType;
	final int factor;
	
	private TimeUnit(Type type, Type baseType, int factor) {
		this.type = type;
		this.baseType = baseType;
		this.factor = factor;
	}

	TimeUnit baseUnit() {
		if (baseType.equals(Type.millisecond)) return TimeUnit.millisecond;
		else return TimeUnit.month;
	}
	
	public boolean isConvertibleToMilliseconds() {
		return isConvertibleTo(millisecond);
	}
	
	public boolean isConvertibleTo(TimeUnit other) {
		return baseType.equals(other.baseType);
	}
	
	public int compareTo(Object arg) {
		TimeUnit other = (TimeUnit) arg;
		if (other.baseType.equals(baseType)) return factor - other.factor;
		if (baseType.equals(Type.month)) return 1;
		return -1;
	}
	
	int javaCalendarConstantForBaseType() {
		if (baseType.equals(Type.millisecond)) return java.util.Calendar.MILLISECOND;
		if (baseType.equals(Type.month)) return java.util.Calendar.MONTH;
		return 0;
	}
	
	public String toString() {
		return type.name;
	}

	String toString(long quantity) {
		StringBuffer buffer = new StringBuffer();
		buffer.append(quantity);
		buffer.append(" ");
		buffer.append(type.name);
		buffer.append(quantity == 1 ? "" : "s");
		return buffer.toString();
	}
	
	TimeUnit[] descendingUnits() {
		if (isConvertibleToMilliseconds()) return descendingMillisecondBased;
			else return descendingMonthBased;		
	}
	
	TimeUnit nextFinerUnit() {
		TimeUnit[] descending = descendingUnits();
		int index = -1;
		for (int i = 0; i < descending.length; i++) {
			if (descending[i].equals(this)) index = i;
		}
		if (index == descending.length - 1) return null;
		return descending[index + 1];
	}
	
	static private class Type {
		static final Type millisecond = new Type("millisecond");
		static final Type second = new Type("second");
		static final Type minute = new Type("minute");
		static final Type hour = new Type("hour");
		static final Type day = new Type("day");
		static final Type week = new Type("week");

		static final Type month = new Type("month");
		static final Type quarter = new Type("quarter");
		static final Type year = new Type("year");

		final String name;	
		
		Type(String name) {
			this.name = name;
		}
	}

}

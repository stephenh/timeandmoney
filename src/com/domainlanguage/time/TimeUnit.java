/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.io.*;
import java.util.*;

import com.domainlanguage.util.*;


class TimeUnit implements Comparable, Serializable, TimeUnitConversionFactors {
	static final TimeUnit millisecond = new TimeUnit(Type.millisecond, Type.millisecond, 1);
	static final TimeUnit second = new TimeUnit(Type.second, Type.millisecond, millisecondsPerSecond);
	static final TimeUnit minute = new TimeUnit(Type.minute, Type.millisecond, millisecondsPerMinute);
	static final TimeUnit hour = new TimeUnit(Type.hour, Type.millisecond, millisecondsPerHour);
	static final TimeUnit day = new TimeUnit(Type.day, Type.millisecond, millisecondsPerDay);
	static final TimeUnit week = new TimeUnit(Type.week, Type.millisecond, millisecondsPerWeek);
	static final TimeUnit[] descendingMillisecondBased = {week, day, hour, minute, second, millisecond};	
	static final TimeUnit[] descendingMillisecondBasedForDisplay = {day, hour, minute, second, millisecond};	
	static final TimeUnit month = new TimeUnit(Type.month, Type.month, 1);
	static final TimeUnit quarter = new TimeUnit(Type.quarter, Type.month, monthsPerQuarter);
	static final TimeUnit year = new TimeUnit(Type.year, Type.month, monthsPerYear);
	static final TimeUnit[] descendingMonthBased = {year, quarter, month};
	static final TimeUnit[] descendingMonthBasedForDisplay = {year, month};
	
	final Type type;
	final Type baseType;
	final int factor;
	
	private TimeUnit(Type type, Type baseType, int factor) {
		this.type = type;
		this.baseType = baseType;
		this.factor = factor;
	}
    
	TimeUnit baseUnit() {
		return baseType.equals(Type.millisecond) ? millisecond : month;
	}
	
	public boolean isConvertibleToMilliseconds() {
		return isConvertibleTo(millisecond);
	}
	
	public boolean isConvertibleTo(TimeUnit other) {
		return baseType.equals(other.baseType);
	}
	
	public int compareTo(Object object) {
		TimeUnit other = (TimeUnit) object;
		if (other.baseType.equals(baseType)) return factor - other.factor;
		if (baseType.equals(Type.month)) return 1;
		return -1;
	}
	
	int javaCalendarConstantForBaseType() {
		if (baseType.equals(Type.millisecond)) return Calendar.MILLISECOND;
		if (baseType.equals(Type.month)) return Calendar.MONTH;
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
		return isConvertibleToMilliseconds() ? 
			descendingMillisecondBased :
			descendingMonthBased;		
	}

	TimeUnit[] descendingUnitsForDisplay() {
		return isConvertibleToMilliseconds() ? 
			descendingMillisecondBasedForDisplay :
			descendingMonthBasedForDisplay;		
	}
	
	TimeUnit nextFinerUnit() {
		TimeUnit[] descending = descendingUnits();
		int index = -1;
		for (int i = 0; i < descending.length; i++) 
			if (descending[i].equals(this)) 
				index = i;
		if (index == descending.length - 1) return null;
		return descending[index + 1];
	}
	
    public boolean equals(Object object) {
		//revisit: maybe use: Reflection.equalsOverClassAndNull(this, other)
		if (object == null || !(object instanceof TimeUnit)) return false;
		TimeUnit other = (TimeUnit) object;
		return 
			this.baseType.equals(other.baseType) && 
			this.factor == other.factor && 
			this.type.equals(other.type);
	}
    
    public int hashCode() {
		return factor + baseType.hashCode() + type.hashCode();
	}

    
	static private class Type implements Serializable {
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
        
        public boolean equals(Object other) {
        	return
				TypeCheck.sameClassOrBothNull(this, other) &&
				this.name.equals(((Type) other).name);
		}
        
        public int hashCode() {
			return name.hashCode();
		}
        
	}
	
}

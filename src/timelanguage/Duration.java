package timelanguage;

import java.util.*;

public class Duration {
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
	
	private Duration(long howMany, TimeUnit unit) {
//		assert howMany >= 0;
		this.quantity = howMany;
		this.unit = unit;
	}
		
	long inBaseUnits() {
		return quantity * unit.factor;
	}
/**
 * TODO
 */	
//	long in(TimeUnit newUnit) {
//		return unit.factorFrom(unit);
//	}
	
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

	
//	public String toNormalizedString() {
//		StringBuffer buffer = new StringBuffer();
//		long remainder = quantity;
//		boolean first = true;
//		for (Iterator iterator = group().iterator(); iterator.hasNext();) {
//			TimeUnit each = (TimeUnit) iterator.next();
//			long portion = remainder / each.factor;
//			if (portion > 0) {
//				if (!first)
//					buffer.append(", ");
//				else
//					first = false;
//				buffer.append(each.toString(portion));
//			}
//			remainder = remainder % each.factor;
//		}
//		return buffer.toString();
//	}
	
	public String toString() {
		return toString(quantity, unit);
	}
	
	private String toString(long quantity, TimeUnit unit) {
			StringBuffer buffer = new StringBuffer();
			buffer.append(quantity);
			buffer.append(" ");
			buffer.append(unit);
			buffer.append(quantity == 1 ? "" : "s");
			return buffer.toString();
	}

	public boolean equals(Object other) {
		return
			(other instanceof Duration) &&
			((Duration) other).quantity == this.quantity &&
			((Duration) other).unit.equals(this.unit);
	}
	
	public int hashCode() {
		return (int) quantity;
	}
			
}

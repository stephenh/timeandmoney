package timelanguage;

import java.util.*;

abstract public class Duration implements TimeConstants {
	public static final Duration NONE = milliseconds(0);

	final long quantity;
	final TimeUnit unit;
	
	private static Duration milliseconds(long howMany, TimeUnit unit) {
		return new MillisecondDuration(howMany, unit);
	}

	public static Duration milliseconds(long howMany) {
		return milliseconds(howMany, TimeUnit.millisecond);
	}

	public static Duration millisecondsFrom(int days, int hours, int minutes, int seconds, int milliseconds) {
		return milliseconds( 
			days * millisecondsPerDay +
			hours * millisecondsPerHour +
			minutes * millisecondsPerMinute +
			seconds * millisecondsPerSecond +
			milliseconds
		);
	}

	public static Duration seconds(int howMany) {
		return milliseconds(howMany, TimeUnit.second);
	}
	
	public static Duration minutes(int howMany) {
		return milliseconds(howMany, TimeUnit.minute);
	}
	
	public static Duration hours(int howMany) {
		return milliseconds(howMany, TimeUnit.hour);
	}
	
	public static Duration days(int howMany) {
		return milliseconds(howMany, TimeUnit.day);
	}
	
	public static Duration weeks(int howMany) {
		return milliseconds(howMany, TimeUnit.week);
	}
	
	private static Duration months(long howMany, TimeUnit unit) {
		return new MonthDuration(howMany, unit);
	}

	public static Duration months(int howMany) {
		return months(howMany, TimeUnit.month);
	}
	
	public static Duration quarters(int howMany) {
		return months(howMany, TimeUnit.quarter);
	}
	
	public static Duration years(int howMany) {
		return months(howMany, TimeUnit.year);
	}
	
	public static Duration decades(int howMany) {
		return months(howMany, TimeUnit.decade);
	}
	
	public static Duration centurys(int howMany) {
		return months(howMany, TimeUnit.century);
	}
	
	public static Duration millenium(int howMany) {
		return months(howMany, TimeUnit.millenium);
	}
	
	private Duration(long howMany, TimeUnit unit) {
//		assert(howMany >= 0);
		this.quantity = howMany * unit.factor;
		this.unit = unit;
	}
		
	abstract int calendarTag();
	
	abstract List group();
	
	public TimePoint addedTo(TimePoint point) {
		Calendar calendar = point.asJavaCalendar();
		calendar.add(calendarTag(), (int) quantity);
		return TimePoint.from(calendar);
	}

	public TimePoint subtractedFrom(TimePoint point) {
		Calendar calendar = point.asJavaCalendar();
		calendar.add(calendarTag(), -1 * (int) quantity);
		return TimePoint.from(calendar);
	}
	
	public String toDetailedString() {
		StringBuffer buffer = new StringBuffer();
		long remainder = quantity;
		boolean first = true;
		for (Iterator iterator = group().iterator(); iterator.hasNext();) {
			TimeUnit each = (TimeUnit) iterator.next();
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
		return unit.toString(quantity);
	}

	public boolean equals(Object other) {
		return
			(other instanceof Duration) &&
			((Duration) other).quantity == this.quantity &&
			((Duration) other).unit.baseType == this.unit.baseType;
	}
	
	public int hashCode() {
		return (int) quantity;
	}

	
	private static class MillisecondDuration extends Duration {	
		MillisecondDuration(long quantity, TimeUnit unit) {
			super(quantity, unit);
		}	
		int calendarTag() {
			return Calendar.MILLISECOND;
		}
		List group() {
			TimeUnit[] group =  new TimeUnit[] {
				TimeUnit.week,
				TimeUnit.day, 
				TimeUnit.hour, 
				TimeUnit.minute, 
				TimeUnit.second, 
				TimeUnit.millisecond, 
			};			
			return Arrays.asList(group);
		}
	}
	
	private static class MonthDuration extends Duration {		
		MonthDuration(long quantity, TimeUnit unit) {
			super(quantity, unit);
		}
		int calendarTag() {
			return Calendar.MONTH;
		}	
		List group() {
			TimeUnit[] group =  new TimeUnit[] {
				TimeUnit.millenium,
				TimeUnit.century, 
				TimeUnit.decade, 
				TimeUnit.year, 
				TimeUnit.quarter, 
				TimeUnit.month, 
			};
			return Arrays.asList(group);
		}
	}
	
}

package timelanguage;

import java.util.TimeZone;

import fundamental.ComparableInterval;

public abstract class CalendarInterval extends ComparableInterval {

	public static CalendarDate date(int year, int month, int day) {
		return CalendarDate.from(year, month, day);
	}
	
	public static CalendarInterval inclusive(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
		CalendarDate startDate = CalendarDate.from(startYear, startMonth, startDay);
		CalendarDate endDate = CalendarDate.from(endYear, endMonth, endDay);
		return ConcreteCalendarInterval.from(startDate, endDate);
	}

	public static CalendarInterval inclusive(CalendarDate start, CalendarDate end) {
		return ConcreteCalendarInterval.from(start, end);
	}

	
	public abstract TimeInterval asTimeInterval(TimeZone zone);
	
	public boolean includesLowerLimit() {
		return true;
	}

	public boolean includesUpperLimit() {
		return true;
	}

	public boolean equals(Object arg) {
		if (!(arg instanceof CalendarInterval)) return false;
		CalendarInterval other = (CalendarInterval) arg;
		return getLowerBound().equals(other.getLowerBound()) && getUpperBound().equals(other.getUpperBound());
	}
	
	public int hashCode() {
		return getLowerBound().hashCode();
	}

	public static final CalendarInterval NEVER = CalendarDate.FAR_FUTURE;

	public CalendarInterval intersect(CalendarInterval other) {
		if (!intersects(other)) return NEVER;
	
		CalendarDate intersectLowerBound = (CalendarDate) greaterOfLowerLimits(other);
		CalendarDate intersectUpperBound = (CalendarDate) lesserOfUpperLimits(other);
	
		return inclusive(intersectLowerBound, intersectUpperBound);
	}


}

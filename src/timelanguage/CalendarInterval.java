package timelanguage;

import java.util.TimeZone;

public abstract class CalendarInterval {
	
	public static CalendarDate date(int year, int month, int day) {
		return CalendarDate.from(year, month, day);
	}
	
	public static CalendarInterval inclusive(int startYear, int startMonth, int startDay, int endYear, int endMonth, int endDay) {
		CalendarDate startDate = CalendarDate.from(startYear, startMonth, startDay);
		CalendarDate endDate = CalendarDate.from(endYear, endMonth, endDay);
		return CalendarDateRangeInclusive.from(startDate, endDate);
	}

	public static CalendarInterval inclusive(CalendarDate start, CalendarDate end) {
		return CalendarDateRangeInclusive.from(start, end);
	}

	
	public abstract TimeInterval asTimeInterval(TimeZone zone);
	
	public abstract CalendarDate start();

	public abstract CalendarDate end();

	public boolean includes(CalendarInterval other) {
		return !start().isAfter(other.start()) && !end().isBefore(other.end());
	}

}

package timelanguage;

import java.util.TimeZone;

class CalendarDateRangeInclusive extends CalendarInterval {
	
	private CalendarDate start;
	private CalendarDate end;
	
	static CalendarDateRangeInclusive from(CalendarDate start, CalendarDate end) {
		return new CalendarDateRangeInclusive(start, end);
	}
	
	CalendarDateRangeInclusive(CalendarDate start, CalendarDate end) {
		this.start = start;
		this.end = end;
	}
	
	public CalendarDate start() {
		return start;
	}

	public CalendarDate end() {
		return end;
	}

	public TimeInterval asTimeInterval(TimeZone zone) {
		TimePoint startPoint = start().asTimeInterval(zone).start();
		TimePoint endPoint = end().asTimeInterval(zone).end();
		return TimeInterval.over(startPoint, endPoint);
	}

}

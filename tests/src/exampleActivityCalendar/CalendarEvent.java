package exampleActivityCalendar;

import timelanguage.TimeInterval;

public class CalendarEvent {
	private TimeInterval timeInterval;
	
	public void setTimeInterval(TimeInterval anInterval) {
		timeInterval = anInterval;
	}
	
	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

}

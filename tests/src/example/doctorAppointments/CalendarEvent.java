package example.doctorAppointments;

import com.domainlanguage.time.TimeInterval;

public class CalendarEvent {
	private TimeInterval timeInterval;
	
	public void setTimeInterval(TimeInterval anInterval) {
		timeInterval = anInterval;
	}
	
	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

}

package exampleActivityCalendar;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimeZone;

import timelanguage.CalendarDate;
import timelanguage.TimeInterval;

public class EventCalendar {
	TimeZone defaultZone;
	Set events = new HashSet();
	
	public EventCalendar(TimeZone zone) {
		defaultZone = zone;
	}
	
	public void add(CalendarEvent anEvent) {
		events.add(anEvent);
	}
	
	public Collection eventsFor(CalendarDate calDate) {
		Set daysEvents = new HashSet();
		TimeInterval day = calDate.asTimeInterval(defaultZone);
		Iterator it = events.iterator();
		while (it.hasNext()) {
			CalendarEvent event = (CalendarEvent)it.next();
			if (event.getTimeInterval().intersects(day)) {
				daysEvents.add(event);
			}
		}
		return daysEvents;
	}
	
}

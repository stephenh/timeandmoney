/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.doctorAppointments;

import java.util.*;

import com.domainlanguage.time.*;


class EventCalendar {
	TimeZone defaultZone;
	Set events = new HashSet();
	
	EventCalendar(TimeZone zone) {
		defaultZone = zone;
	}
	
	void add(CalendarEvent anEvent) {
		events.add(anEvent);
	}
	
	Collection eventsFor(CalendarDate calDate) {
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

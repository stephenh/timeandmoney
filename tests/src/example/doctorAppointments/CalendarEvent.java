/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package example.doctorAppointments;

import com.domainlanguage.time.TimeInterval;

class CalendarEvent {
	private TimeInterval timeInterval;
	
	public void setTimeInterval(TimeInterval anInterval) {
		timeInterval = anInterval;
	}
	
	public TimeInterval getTimeInterval() {
		return timeInterval;
	}

}

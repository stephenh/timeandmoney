package timelanguage;

import java.util.*;


import junit.framework.*;

public class CalendarsTest extends TestCase {

	public void testSameDay() throws Exception {
		TimePoint morning = TimePoint.from(2004, 2, 19, 5);
		TimePoint afternoon = TimePoint.from(2004, 2, 19, 15);
		assertTrue(Calendars.isSameDay(morning, afternoon));
	}

}

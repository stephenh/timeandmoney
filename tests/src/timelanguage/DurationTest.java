package timelanguage;

import junit.framework.*;

public class DurationTest extends TestCase {

	public void testAddMillisecondsToPoint() {
		TimePoint dec20At1 = TimePoint.from(2003, 12, 20, 01, 0, 0, 0);
		TimePoint dec22At1 = TimePoint.from(2003, 12, 22, 01, 0, 0, 0);
		Duration twoDays = Duration.days(2);
		assertEquals(dec22At1, twoDays.addedTo(dec20At1));
	}

	public void testAddMonthsToPoint() {
		TimePoint oct20At1 = TimePoint.from(2003, 10, 20, 01, 0, 0, 0);
		TimePoint dec20At1 = TimePoint.from(2003, 12, 20, 01, 0, 0, 0);
		Duration twoMonths = Duration.months(2);
		assertEquals(dec20At1, twoMonths.addedTo(oct20At1));
	}
	public void testSubtractMillisecondsFromPoint() {
		TimePoint dec20At1 = TimePoint.from(2003, 12, 20, 01, 0, 0, 0);
		TimePoint dec18At1 = TimePoint.from(2003, 12, 18, 01, 0, 0, 0);
		Duration twoDays = Duration.days(2);
		assertEquals(dec18At1, twoDays.subtractedFrom(dec20At1));
	}

	public void testSubtractMonthsFromPoint() {
		TimePoint oct20At1 = TimePoint.from(2003, 10, 20, 01, 0, 0, 0);
		TimePoint dec20At1 = TimePoint.from(2003, 12, 20, 01, 0, 0, 0);
		Duration twoMonths = Duration.months(2);
		assertEquals(oct20At1, twoMonths.subtractedFrom(dec20At1));

		TimePoint dec20At1_2001 = TimePoint.from(2001, 12, 20, 01, 0, 0, 0);
		Duration twoYears = Duration.years(2);
		assertEquals(dec20At1_2001, twoYears.subtractedFrom(dec20At1));
	}

	public void testSubtractFromCalendarDate() {
		CalendarDate oct20 = CalendarDate.from(2003, 10, 20);
		CalendarDate dec20 = CalendarDate.from(2003, 12, 20);

		Duration twoMonths = Duration.months(2);
		assertEquals(oct20, twoMonths.subtractedFrom(dec20));

		Duration sixtyoneDays = Duration.days(61);
		assertEquals(oct20, sixtyoneDays.subtractedFrom(dec20));
		
		CalendarDate dec20_2001 = CalendarDate.from(2001, 12, 20);
		Duration twoYears = Duration.years(2);
		assertEquals(dec20_2001, twoYears.subtractedFrom(dec20));
	}

	public void testAddToCalendarDate() {
		CalendarDate oct20_2003 = CalendarDate.from(2003, 10, 20);
		CalendarDate dec20_2003 = CalendarDate.from(2003, 12, 20);

		Duration twoMonths = Duration.months(2);
		assertEquals(dec20_2003, twoMonths.addedTo(oct20_2003));

		Duration sixtyoneDays = Duration.days(61);
		assertEquals(dec20_2003, sixtyoneDays.addedTo(oct20_2003));
		
		CalendarDate dec20_2001 = CalendarDate.from(2001, 12, 20);
		Duration twoYears = Duration.years(2);
		assertEquals(dec20_2003, twoYears.addedTo(dec20_2001));
	}

	public void testConversionToBaseUnits() {
		Duration twoSeconds = Duration.seconds(2);
		assertEquals(2000, twoSeconds.inBaseUnits());
	}
}

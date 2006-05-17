/* Copyright (c) 2006 - Blue River Systems Group, LLC - All Rights Reserved */
package com.domainlanguage.time;

import junit.framework.TestCase;

/**
 * TimeOfDayTest
 *
 * @author davem
 * @since May 15, 2006
 * @copyright Copyright (c) 2006 - Blue River Systems Group, LLC - All Rights Reserved
 */
public class TimeOfDayTest extends TestCase {

    private static final int FIRST_HOUR = 0;
    private static final int TENTH_HOUR = 10;
    private static final int NOON_HOUR = 12;
    private static final int FIFTEENTH_HOUR = 15;
    private static final int LAST_HOUR = 23;

    private static final int FIRST_MINUTE = 0;
    private static final int TWENTIETH_MINUTE = 20;
    private static final int FORTIETH_MINUTE = 40;
    private static final int LAST_MINUTE = 59;

    private CalendarDate feb17 = CalendarDate.from(2006, 2, 17);
    private TimeOfDay startOfDay = TimeOfDay.hourAndMinute(FIRST_HOUR, FIRST_MINUTE);
    private TimeOfDay morning = TimeOfDay.hourAndMinute(TENTH_HOUR, TWENTIETH_MINUTE);
    private TimeOfDay middleOfDay = TimeOfDay.hourAndMinute(NOON_HOUR, FIRST_MINUTE);
    private TimeOfDay afternoon = TimeOfDay.hourAndMinute(FIFTEENTH_HOUR, FORTIETH_MINUTE);
    private TimeOfDay endOfDay = TimeOfDay.hourAndMinute(LAST_HOUR, LAST_MINUTE);
    
    /**
     * Constructs a TimeOfDayTest.
     */
    public TimeOfDayTest() {
        super();
    }

    /**
     * Constructs a TimeOfDayTest.
     * @param name
     */
    public TimeOfDayTest(String name) {
        super(name);
    }

    public void testOnStartOfDay() {
        CalendarMinute feb17AtStartOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, FIRST_HOUR, FIRST_MINUTE);
        assertEquals(feb17AtStartOfDay, startOfDay.on(feb17));
    }

    public void testOnMiddleOfDay() {
        CalendarMinute feb17AtMiddleOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, NOON_HOUR, FIRST_MINUTE);
        assertEquals(feb17AtMiddleOfDay, middleOfDay.on(feb17));
    }

    public void testOnEndOfDay() {
        CalendarMinute feb17AtEndOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, LAST_HOUR, LAST_MINUTE);
        assertEquals(feb17AtEndOfDay, endOfDay.on(feb17));
    }

    public void testEquals() {
        assertEquals(TimeOfDay.hourAndMinute(FIRST_HOUR, FIRST_MINUTE), startOfDay);
        assertEquals(TimeOfDay.hourAndMinute(TENTH_HOUR, TWENTIETH_MINUTE), morning);
        assertEquals(TimeOfDay.hourAndMinute(NOON_HOUR, FIRST_MINUTE), middleOfDay);
        assertEquals(TimeOfDay.hourAndMinute(FIFTEENTH_HOUR, FORTIETH_MINUTE), afternoon);
        assertEquals(TimeOfDay.hourAndMinute(LAST_HOUR, LAST_MINUTE), endOfDay);
    }

    public void testHashCode() {
        assertEquals(TimeOfDay.hourAndMinute(FIRST_HOUR, FIRST_MINUTE).hashCode(), startOfDay.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(TENTH_HOUR, TWENTIETH_MINUTE).hashCode(), morning.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(NOON_HOUR, FIRST_MINUTE).hashCode(), middleOfDay.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(FIFTEENTH_HOUR, FORTIETH_MINUTE).hashCode(), afternoon.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(LAST_HOUR, LAST_MINUTE).hashCode(), endOfDay.hashCode());
    }

    public void testAfterWithEarlierTimeOfDay() {
        assertTrue("expected endOfDay to be after startOfDay", endOfDay.after(startOfDay));
        assertTrue("expected afternoon to be after morning", afternoon.after(morning));
        assertTrue("expected middleOfDay to be after startOfDay", middleOfDay.after(startOfDay));
    }

    public void testAfterWithLaterTimeOfDay() {
        assertFalse("expected startOfDay not after endOfDay", startOfDay.after(endOfDay));
        assertFalse("expected morning not after afternoon", morning.after(afternoon));
        assertFalse("expected middleOfDay not after endOfDay", middleOfDay.after(endOfDay));
    }

    public void testAfterWithSameTimeOfDay() {
        assertFalse("expected startOfDay not after startOfDay", startOfDay.after(startOfDay));
        assertFalse("expected morning not after morning", morning.after(morning));
        assertFalse("expected afternoon not after afternoon", afternoon.after(afternoon));
        assertFalse("expected middleOfDay not after middleOfDay", middleOfDay.after(middleOfDay));
    }

    public void testBeforeWithEarlierTimeOfDay() {
        assertFalse("expected endOfDay not after startOfDay", endOfDay.before(startOfDay));
        assertFalse("expected afternoon not after morning", afternoon.before(morning));
        assertFalse("expected middleOfDay not after startOfDay", middleOfDay.before(startOfDay));
    }

    public void testBeforeWithLaterTimeOfDay() {
        assertTrue("expected startOfDay not after endOfDay", startOfDay.before(endOfDay));
        assertTrue("expected morning not after afternoon", morning.before(afternoon));
        assertTrue("expected middleOfDay not after endOfDay", middleOfDay.before(endOfDay));
    }

    public void testBeforeWithSameTimeOfDay() {
        assertFalse("expected startOfDay not after startOfDay", startOfDay.before(startOfDay));
        assertFalse("expected morning not after morning", morning.before(morning));
        assertFalse("expected afternoon not after afternoon", afternoon.before(afternoon));
        assertFalse("expected middleOfDay not after middleOfDay", middleOfDay.before(middleOfDay));
    }

    public void testGetHour() {
        assertEquals(FIRST_HOUR, startOfDay.getHour());
        assertEquals(TENTH_HOUR, morning.getHour());
        assertEquals(NOON_HOUR, middleOfDay.getHour());
        assertEquals(FIFTEENTH_HOUR, afternoon.getHour());
        assertEquals(LAST_HOUR, endOfDay.getHour());
    }

    public void testGetMinute() {
        assertEquals(FIRST_MINUTE, startOfDay.getMinute());
        assertEquals(TWENTIETH_MINUTE, morning.getMinute());
        assertEquals(FIRST_MINUTE, middleOfDay.getMinute());
        assertEquals(FORTIETH_MINUTE, afternoon.getMinute());
        assertEquals(LAST_MINUTE, endOfDay.getMinute());
    }
}

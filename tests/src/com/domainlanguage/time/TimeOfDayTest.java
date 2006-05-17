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

    private CalendarDate feb17 = CalendarDate.from(2006, 2, 17);
    private TimeOfDay midnight = TimeOfDay.hourAndMinute(0, 0);
    private TimeOfDay morning = TimeOfDay.hourAndMinute(10, 20);
    private TimeOfDay noon = TimeOfDay.hourAndMinute(12, 0);
    private TimeOfDay afternoon = TimeOfDay.hourAndMinute(15, 40);
    private TimeOfDay twoMinutesBeforeMidnight = TimeOfDay.hourAndMinute(23, 58);
    
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
        CalendarMinute feb17AtStartOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, 0, 0);
        assertEquals(feb17AtStartOfDay, midnight.on(feb17));
    }

    public void testOnMiddleOfDay() {
        CalendarMinute feb17AtMiddleOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, 12, 0);
        assertEquals(feb17AtMiddleOfDay, noon.on(feb17));
    }

    public void testOnEndOfDay() {
        CalendarMinute feb17AtEndOfDay = CalendarMinute.dateHourAndMinute(2006, 2, 17, 23, 58);
        assertEquals(feb17AtEndOfDay, twoMinutesBeforeMidnight.on(feb17));
    }

    public void testEquals() {
        assertEquals(TimeOfDay.hourAndMinute(0, 0), midnight);
        assertEquals(TimeOfDay.hourAndMinute(10, 20), morning);
        assertEquals(TimeOfDay.hourAndMinute(12, 0), noon);
        assertEquals(TimeOfDay.hourAndMinute(15, 40), afternoon);
        assertEquals(TimeOfDay.hourAndMinute(23, 58), twoMinutesBeforeMidnight);
    }

    public void testHashCode() {
        assertEquals(TimeOfDay.hourAndMinute(0, 0).hashCode(), midnight.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(10, 20).hashCode(), morning.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(12, 0).hashCode(), noon.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(15, 40).hashCode(), afternoon.hashCode());
        assertEquals(TimeOfDay.hourAndMinute(23, 58).hashCode(), twoMinutesBeforeMidnight.hashCode());
    }

    public void testAfterWithEarlierTimeOfDay() {
        assertTrue("expected twoMinutesBeforeMidnight to be after midnight", twoMinutesBeforeMidnight.after(midnight));
        assertTrue("expected afternoon to be after morning", afternoon.after(morning));
        assertTrue("expected noon to be after midnight", noon.after(midnight));
    }

    public void testAfterWithLaterTimeOfDay() {
        assertFalse("expected midnight not after twoMinutesBeforeMidnight", midnight.after(twoMinutesBeforeMidnight));
        assertFalse("expected morning not after afternoon", morning.after(afternoon));
        assertFalse("expected noon not after twoMinutesBeforeMidnight", noon.after(twoMinutesBeforeMidnight));
    }

    public void testAfterWithSameTimeOfDay() {
        assertFalse("expected midnight not after midnight", midnight.after(midnight));
        assertFalse("expected morning not after morning", morning.after(morning));
        assertFalse("expected afternoon not after afternoon", afternoon.after(afternoon));
        assertFalse("expected noon not after noon", noon.after(noon));
    }

    public void testBeforeWithEarlierTimeOfDay() {
        assertFalse("expected twoMinutesBeforeMidnight not after midnight", twoMinutesBeforeMidnight.before(midnight));
        assertFalse("expected afternoon not after morning", afternoon.before(morning));
        assertFalse("expected noon not after midnight", noon.before(midnight));
    }

    public void testBeforeWithLaterTimeOfDay() {
        assertTrue("expected midnight not after twoMinutesBeforeMidnight", midnight.before(twoMinutesBeforeMidnight));
        assertTrue("expected morning not after afternoon", morning.before(afternoon));
        assertTrue("expected noon not after twoMinutesBeforeMidnight", noon.before(twoMinutesBeforeMidnight));
    }

    public void testBeforeWithSameTimeOfDay() {
        assertFalse("expected midnight not after midnight", midnight.before(midnight));
        assertFalse("expected morning not after morning", morning.before(morning));
        assertFalse("expected afternoon not after afternoon", afternoon.before(afternoon));
        assertFalse("expected noon not after noon", noon.before(noon));
    }

    public void testGetHour() {
        assertEquals(0, midnight.getHour());
        assertEquals(10, morning.getHour());
        assertEquals(12, noon.getHour());
        assertEquals(15, afternoon.getHour());
        assertEquals(23, twoMinutesBeforeMidnight.getHour());
    }

    public void testGetMinute() {
        assertEquals(0, midnight.getMinute());
        assertEquals(20, morning.getMinute());
        assertEquals(0, noon.getMinute());
        assertEquals(40, afternoon.getMinute());
        assertEquals(58, twoMinutesBeforeMidnight.getMinute());
    }
}

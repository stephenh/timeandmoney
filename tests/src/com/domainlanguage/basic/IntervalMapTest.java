/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;

import java.util.*;
import java.util.Map;

import junit.framework.TestCase;

public class IntervalMapTest extends TestCase {
	
	public void testLookup() {
		Map regularOldMap = new HashMap();
		
		IntervalMap map = new LinearIntervalMap();
		map.put(ComparableInterval.closed(new Integer(1), new Integer(3)), "one-three");
		map.put(ComparableInterval.closed(new Integer(5), new Integer(9)), "five-nine");
		map.put(ComparableInterval.open(new Integer(9), new Integer(12)), "ten-eleven");
		
		assertFalse(map.includesKey(new Integer(0)));
		assertTrue(map.includesKey(new Integer(1)));
		assertTrue(map.includesKey(new Integer(2)));
		assertTrue(map.includesKey(new Integer(3)));
		assertFalse(map.includesKey(new Integer(4)));
		assertTrue(map.includesKey(new Integer(5)));
		assertTrue(map.includesKey(new Integer(9)));
		assertTrue(map.includesKey(new Integer(11)));
		assertFalse(map.includesKey(new Integer(12)));
		assertFalse(map.includesKey(new Integer(13)));
		assertFalse(map.includesKey(null));

		assertNull(map.get(new Integer(0)));
		assertEquals("one-three", map.get(new Integer(1)));
		assertEquals("one-three", map.get(new Integer(2)));
		assertEquals("one-three", map.get(new Integer(3)));
		assertNull(map.get(new Integer(4)));
		assertEquals("five-nine", map.get(new Integer(5)));
		assertEquals("five-nine", map.get(new Integer(9)));
		assertEquals("ten-eleven", map.get(new Integer(10)));
		assertEquals("ten-eleven", map.get(new Integer(11)));
		assertNull(map.get(new Integer(12)));
		assertNull(map.get(new Integer(13)));
		assertNull(map.get(null));
		
	}
	
	public void testConstruction() {
		IntervalMap map = new LinearIntervalMap();
		map.put(ComparableInterval.closed(new Integer(1), new Integer(3)), "one-three");
		map.put(ComparableInterval.closed(new Integer(5), new Integer(9)), "five-nine");
		map.put(ComparableInterval.open(new Integer(9), new Integer(12)), "ten-eleven");
		ConcreteComparableInterval eleven_thirteen = ComparableInterval.closed(new Integer(11), new Integer(13));
		assertTrue(map.containsIntersectingKey(eleven_thirteen));
		try {
			map.put(eleven_thirteen, "eleven-thirteen");
			fail("Throw exception when key intervals intersect"); //TODO: Maybe this should just override the overlapping part of the preexisting interval
		}
		catch (Exception e) {
			
		}
	}
}

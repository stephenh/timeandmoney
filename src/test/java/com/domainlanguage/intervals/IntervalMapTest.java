/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import junit.framework.Assert;
import junit.framework.TestCase;

public class IntervalMapTest extends TestCase {

  public void testLookup() {
    IntervalMap<Integer, String> map = new LinearIntervalMap<Integer, String>();
    map.put(Interval.closed(new Integer(1), new Integer(3)), "one-three");
    map.put(Interval.closed(new Integer(5), new Integer(9)), "five-nine");
    map.put(Interval.open(new Integer(9), new Integer(12)), "ten-eleven");

    Assert.assertFalse(map.containsKey(new Integer(0)));
    Assert.assertTrue(map.containsKey(new Integer(1)));
    Assert.assertTrue(map.containsKey(new Integer(2)));
    Assert.assertTrue(map.containsKey(new Integer(3)));
    Assert.assertFalse(map.containsKey(new Integer(4)));
    Assert.assertTrue(map.containsKey(new Integer(5)));
    Assert.assertTrue(map.containsKey(new Integer(9)));
    Assert.assertTrue(map.containsKey(new Integer(11)));
    Assert.assertFalse(map.containsKey(new Integer(12)));
    Assert.assertFalse(map.containsKey(new Integer(13)));
    Assert.assertFalse(map.containsKey(null));

    Assert.assertNull(map.get(new Integer(0)));
    Assert.assertEquals("one-three", map.get(new Integer(1)));
    Assert.assertEquals("one-three", map.get(new Integer(2)));
    Assert.assertEquals("one-three", map.get(new Integer(3)));
    Assert.assertNull(map.get(new Integer(4)));
    Assert.assertEquals("five-nine", map.get(new Integer(5)));
    Assert.assertEquals("five-nine", map.get(new Integer(9)));
    Assert.assertEquals("ten-eleven", map.get(new Integer(10)));
    Assert.assertEquals("ten-eleven", map.get(new Integer(11)));
    Assert.assertNull(map.get(new Integer(12)));
    Assert.assertNull(map.get(new Integer(13)));
    Assert.assertNull(map.get(null));
  }

  public void testRemove() {
    IntervalMap<Integer, String> map = new LinearIntervalMap<Integer, String>();
    map.put(Interval.closed(new Integer(1), new Integer(10)), "one-ten");
    map.remove(Interval.closed(new Integer(3), new Integer(5)));
    Assert.assertEquals("one-ten", map.get(new Integer(2)));
    Assert.assertNull(map.get(new Integer(3)));
    Assert.assertNull(map.get(new Integer(4)));
    Assert.assertNull(map.get(new Integer(5)));
    Assert.assertEquals("one-ten", map.get(new Integer(6)));
  }

  public void testConstructionOverwriteOverlap() {
    IntervalMap<Integer, String> map = new LinearIntervalMap<Integer, String>();
    map.put(Interval.closed(new Integer(1), new Integer(3)), "one-three");
    map.put(Interval.closed(new Integer(5), new Integer(9)), "five-nine");
    map.put(Interval.open(new Integer(9), new Integer(12)), "ten-eleven");
    Assert.assertEquals("ten-eleven", map.get(new Integer(10)));
    Assert.assertEquals("ten-eleven", map.get(new Integer(11)));
    Assert.assertNull(map.get(new Integer(12)));

    Interval<Integer> eleven_thirteen = Interval.closed(new Integer(11), new Integer(13));
    Assert.assertTrue(map.containsIntersectingKey(eleven_thirteen));
    map.put(eleven_thirteen, "eleven-thirteen");
    Assert.assertEquals("ten-eleven", map.get(new Integer(10)));
    Assert.assertEquals("eleven-thirteen", map.get(new Integer(11)));
    Assert.assertEquals("eleven-thirteen", map.get(new Integer(12)));
  }

  public void testConstructionOverwriteMiddle() {
    IntervalMap<Integer, String> map = new LinearIntervalMap<Integer, String>();
    map.put(Interval.closed(new Integer(1), new Integer(3)), "one-three");
    map.put(Interval.closed(new Integer(5), new Integer(9)), "five-nine");
    map.put(Interval.open(new Integer(9), new Integer(12)), "ten-eleven");
    Assert.assertEquals("five-nine", map.get(new Integer(6)));
    Assert.assertEquals("five-nine", map.get(new Integer(7)));
    Assert.assertEquals("five-nine", map.get(new Integer(8)));
    Assert.assertEquals("five-nine", map.get(new Integer(9)));

    Interval<Integer> seven_eight = Interval.closed(new Integer(7), new Integer(8));
    Assert.assertTrue(map.containsIntersectingKey(seven_eight));
    map.put(seven_eight, "seven-eight");
    Assert.assertEquals("five-nine", map.get(new Integer(6)));
    Assert.assertEquals("seven-eight", map.get(new Integer(7)));
    Assert.assertEquals("seven-eight", map.get(new Integer(8)));
    Assert.assertEquals("five-nine", map.get(new Integer(9)));
  }

  public void testConstructionOverwriteMultiple() {
    IntervalMap<Integer, String> map = new LinearIntervalMap<Integer, String>();
    map.put(Interval.closed(new Integer(1), new Integer(2)), "one-two");
    map.put(Interval.closed(new Integer(3), new Integer(4)), "three-four");
    map.put(Interval.closed(new Integer(5), new Integer(6)), "five-six");
    map.put(Interval.closed(new Integer(8), new Integer(9)), "eight-nine");
    map.put(Interval.closed(new Integer(3), new Integer(8)), "three-eight");
    Assert.assertEquals("one-two", map.get(new Integer(2)));
    Assert.assertEquals("three-eight", map.get(new Integer(3)));
    Assert.assertEquals("three-eight", map.get(new Integer(4)));
    Assert.assertEquals("three-eight", map.get(new Integer(5)));
    Assert.assertEquals("three-eight", map.get(new Integer(6)));
    Assert.assertEquals("three-eight", map.get(new Integer(7)));
    Assert.assertEquals("three-eight", map.get(new Integer(8)));
    Assert.assertEquals("eight-nine", map.get(new Integer(9)));
  }

}

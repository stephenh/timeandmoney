/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.framework.Assert;
import junit.framework.TestCase;

public class IntervalSequenceTest extends TestCase {
  private Interval<Integer> c5_10c = Interval.closed(new Integer(5), new Integer(10));
  private Interval<Integer> o10_12c = Interval.over(new Integer(10), false, new Integer(12), true);
  private Interval<Integer> o11_20c = Interval.over(new Integer(11), false, new Integer(20), true);
  private Interval<Integer> o12_20o = Interval.open(new Integer(12), new Integer(20));
  private Interval<Integer> c20_25c = Interval.closed(new Integer(20), new Integer(25));
  private Interval<Integer> o25_30c = Interval.over(new Integer(25), false, new Integer(30), true);
  private Interval<Integer> o30_35o = Interval.open(new Integer(30), new Integer(35));

  public void testIterate() {
    IntervalSequence<Integer> intervalSequence = new IntervalSequence<Integer>();
    Assert.assertTrue(intervalSequence.isEmpty());
    intervalSequence.add(c5_10c);
    intervalSequence.add(o10_12c);
    Iterator<Interval<Integer>> it = intervalSequence.iterator();
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(c5_10c, it.next());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(o10_12c, it.next());
    Assert.assertFalse(it.hasNext());
    try {
      Assert.assertNull(it.next());
      Assert.fail("Should throw NoSuchElementException");
      //TODO: Should all iterators throw NoSuchElementException or null
      // after end?
    } catch (NoSuchElementException e) {
    }
  }

  public void testInsertedOutOfOrder() {
    IntervalSequence<Integer> intervalSequence = new IntervalSequence<Integer>();
    intervalSequence.add(o10_12c);
    intervalSequence.add(c5_10c);
    //Iterator behavior should be the same regardless of order of
    // insertion.
    Iterator<Interval<Integer>> it = intervalSequence.iterator();
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(c5_10c, it.next());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(o10_12c, it.next());
    Assert.assertFalse(it.hasNext());

  }

  public void testGaps() {
    IntervalSequence<Integer> intervalSequence = new IntervalSequence<Integer>();
    intervalSequence.add(c5_10c);
    intervalSequence.add(o10_12c);
    intervalSequence.add(c20_25c);
    intervalSequence.add(o30_35o);
    IntervalSequence<Integer> gaps = intervalSequence.gaps();
    Iterator<Interval<Integer>> it = gaps.iterator();
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(o12_20o, it.next());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(o25_30c, it.next());
    Assert.assertFalse(it.hasNext());

  }

  public void testOverlapping() {
    IntervalSequence<Integer> intervalSequence = new IntervalSequence<Integer>();
    intervalSequence.add(o10_12c);
    intervalSequence.add(o11_20c);
    Iterator<Interval<Integer>> it = intervalSequence.iterator();
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(o10_12c, it.next());
    Assert.assertTrue(it.hasNext());
    Assert.assertEquals(o11_20c, it.next());
    Assert.assertFalse(it.hasNext());
  }

  //    public void testIntersections() {
  //        
  //        IntervalSequence intervalSequence = new IntervalSequence();
  //        intervalSequence.add(o10_12c);
  //        intervalSequence.add(o11_20c);
  //        intervalSequence.add(c20_25c);
  //        
  //        Iterator it = intervalSequence.intersections().iterator();
  //        assertTrue(it.hasNext());
  //        assertEquals(o11_12c, it.next());
  //        assertTrue(it.hasNext());
  //        assertEquals(c20_20c, it.next());
  //        assertFalse(it.hasNext());
  //    }

  public void testExtent() {
    IntervalSequence<Integer> intervalSequence = new IntervalSequence<Integer>();
    intervalSequence.add(c5_10c);
    intervalSequence.add(o10_12c);
    intervalSequence.add(c20_25c);
    Assert.assertEquals(Interval.closed(new Integer(5), new Integer(25)), intervalSequence.extent());
  }
}

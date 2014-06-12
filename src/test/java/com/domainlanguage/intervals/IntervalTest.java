/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;

import com.domainlanguage.tests.SerializationTester;

public class IntervalTest extends TestCase {

  private static final Interval<BigDecimal> empty = Interval.open(new BigDecimal(1), new BigDecimal(1));
  private static final Interval<BigDecimal> c5_10c = Interval.closed(new BigDecimal(5), new BigDecimal(10));
  private static final Interval<BigDecimal> c1_10c = Interval.closed(new BigDecimal(1), new BigDecimal(10));
  private static final Interval<BigDecimal> c4_6c = Interval.closed(new BigDecimal(4), new BigDecimal(6));
  private static final Interval<BigDecimal> c5_15c = Interval.closed(new BigDecimal(5), new BigDecimal(15));
  private static final Interval<BigDecimal> c12_16c = Interval.closed(new BigDecimal(12), new BigDecimal(16));
  private static final Interval<BigDecimal> o10_12c = Interval.over(new BigDecimal(10), false, new BigDecimal(12), true);
  private static final Interval<BigDecimal> o1_1c = Interval.over(new BigDecimal(1), false, new BigDecimal(1), true);
  private static final Interval<BigDecimal> c1_1o = Interval.over(new BigDecimal(1), true, new BigDecimal(1), false);
  private static final Interval<BigDecimal> c1_1c = Interval.over(new BigDecimal(1), true, new BigDecimal(1), true);
  private static final Interval<BigDecimal> o1_1o = Interval.over(new BigDecimal(1), false, new BigDecimal(1), false);

  public static IntervalLimit<Integer> exampleLimitForPersistentMappingTesting() {
    return IntervalLimit.upper(true, new Integer(78));
  }

  //    private Interval o1_10o = Interval.open(new BigDecimal(1), new BigDecimal(10));
  //    private Interval o10_12o = Interval.open(new BigDecimal(10), new BigDecimal(12));

  //TODO: either fix those tests, or delete them (Benny)
  //	public void testAssertions() {
  //		//Redundant, maybe, but with all the compiler default
  //		//confusion at the moment, I decided to throw this in.
  //		try {
  //			Interval.closed(new BigDecimal(2.0), new BigDecimal(1.0));
  //			fail("Lower bound mustn't be above upper bound.");
  //		} catch (AssertionError e) {
  //			//Correct. Do nothing.
  //		}
  //	}
  //
  //	public void testUpTo() {
  //		Interval range = Interval.upTo(new BigDecimal(5.5));
  //		assertTrue(range.includes(new BigDecimal(5.5)));
  //		assertTrue(range.includes(new BigDecimal(-5.5)));
  //		assertTrue(range.includes(new BigDecimal(Double.NEGATIVE_INFINITY)));
  //		assertTrue(!range.includes(new BigDecimal(5.5001)));
  //	}
  //
  //	public void testAndMore() {
  //		Interval range = Interval.andMore(5.5);
  //		assertTrue(range.includes(5.5));
  //		assertTrue(!range.includes(5.4999));
  //		assertTrue(!range.includes(-5.5));
  //		assertTrue(range.includes(Double.POSITIVE_INFINITY));
  //		assertTrue(range.includes(5.5001));
  //	}
  //
  public void testAbstractCreation() {
    Interval<Integer> concrete = new Interval<Integer>(new Integer(1), true, new Integer(3), true);
    Interval<Integer> newInterval = concrete.newOfSameType(new Integer(1), false, new Integer(4), false);

    Interval<Integer> expected = new Interval<Integer>(new Integer(1), false, new Integer(4), false);
    Assert.assertEquals(expected, newInterval);
  }

  public void testSerialization() {
    SerializationTester.assertCanBeSerialized(c5_10c);
  }

  public void testToString() {
    Assert.assertEquals("[1, 10]", c1_10c.toString());
    Assert.assertEquals("(10, 12]", o10_12c.toString());
    Assert.assertEquals("{}", empty.toString());
    Assert.assertEquals("{10}", Interval.closed(new Integer(10), new Integer(10)).toString());
  }

  public void testIsBelow() {
    Interval<BigDecimal> range = Interval.closed(new BigDecimal(-5.5), new BigDecimal(6.6));
    Assert.assertFalse(range.isBelow(new BigDecimal(5.0)));
    Assert.assertFalse(range.isBelow(new BigDecimal(-5.5)));
    Assert.assertFalse(range.isBelow(new BigDecimal(-5.4999)));
    Assert.assertFalse(range.isBelow(new BigDecimal(6.6)));
    Assert.assertTrue(range.isBelow(new BigDecimal(6.601)));
    Assert.assertFalse(range.isBelow(new BigDecimal(-5.501)));
  }

  public void testIncludes() {
    Interval<BigDecimal> range = Interval.closed(new BigDecimal(-5.5), new BigDecimal(6.6));
    Assert.assertTrue(range.includes(new BigDecimal(5.0)));
    Assert.assertTrue(range.includes(new BigDecimal(-5.5)));
    Assert.assertTrue(range.includes(new BigDecimal(-5.4999)));
    Assert.assertTrue(range.includes(new BigDecimal(6.6)));
    Assert.assertFalse(range.includes(new BigDecimal(6.601)));
    Assert.assertFalse(range.includes(new BigDecimal(-5.501)));
  }

  public void testOpenInterval() {
    Interval<BigDecimal> exRange = Interval.over(new BigDecimal(-5.5), false, new BigDecimal(6.6), true);
    Assert.assertTrue(exRange.includes(new BigDecimal(5.0)));
    Assert.assertFalse(exRange.includes(new BigDecimal(-5.5)));
    Assert.assertTrue(exRange.includes(new BigDecimal(-5.4999)));
    Assert.assertTrue(exRange.includes(new BigDecimal(6.6)));
    Assert.assertFalse(exRange.includes(new BigDecimal(6.601)));
    Assert.assertFalse(exRange.includes(new BigDecimal(-5.501)));
  }

  public void testIsEmpty() {
    Assert.assertFalse(Interval.closed(new Integer(5), new Integer(6)).isEmpty());
    Assert.assertFalse(Interval.closed(new Integer(6), new Integer(6)).isEmpty());
    Assert.assertTrue(Interval.open(new Integer(6), new Integer(6)).isEmpty());
    Assert.assertTrue(c1_10c.emptyOfSameType().isEmpty());
  }

  public void testIntersects() {
    Assert.assertTrue("c5_10c.intersects(c1_10c)", c5_10c.intersects(c1_10c));
    Assert.assertTrue("c1_10c.intersects(c5_10c)", c1_10c.intersects(c5_10c));
    Assert.assertTrue("c4_6c.intersects(c1_10c)", c4_6c.intersects(c1_10c));
    Assert.assertTrue("c1_10c.intersects(c4_6c)", c1_10c.intersects(c4_6c));
    Assert.assertTrue("c5_10c.intersects(c5_15c)", c5_10c.intersects(c5_15c));
    Assert.assertTrue("c5_15c.intersects(c1_10c)", c5_15c.intersects(c1_10c));
    Assert.assertTrue("c1_10c.intersects(c5_15c)", c1_10c.intersects(c5_15c));
    Assert.assertFalse("c1_10c.intersects(c12_16c)", c1_10c.intersects(c12_16c));
    Assert.assertFalse("c12_16c.intersects(c1_10c)", c12_16c.intersects(c1_10c));
    Assert.assertTrue("c5_10c.intersects(c5_10c)", c5_10c.intersects(c5_10c));
    Assert.assertFalse("c1_10c.intersects(o10_12c)", c1_10c.intersects(o10_12c));
    Assert.assertFalse("o10_12c.intersects(c1_10c)", o10_12c.intersects(c1_10c));
  }

  public void testIntersection() {
    Assert.assertEquals(c5_10c, c5_10c.intersect(c1_10c));
    Assert.assertEquals(c5_10c, c1_10c.intersect(c5_10c));
    Assert.assertEquals(c4_6c, c4_6c.intersect(c1_10c));
    Assert.assertEquals(c4_6c, c1_10c.intersect(c4_6c));
    Assert.assertEquals(c5_10c, c5_10c.intersect(c5_15c));
    Assert.assertEquals(c5_10c, c5_15c.intersect(c1_10c));
    Assert.assertEquals(c5_10c, c1_10c.intersect(c5_15c));
    Assert.assertTrue(c1_10c.intersect(c12_16c).isEmpty());
    Assert.assertEquals(empty, c1_10c.intersect(c12_16c));
    Assert.assertEquals(empty, c12_16c.intersect(c1_10c));
    Assert.assertEquals(c5_10c, c5_10c.intersect(c5_10c));
    Assert.assertEquals(empty, c1_10c.intersect(o10_12c));
    Assert.assertEquals(empty, o10_12c.intersect(c1_10c));
  }

  public void testGreaterOfLowerLimits() {
    Assert.assertEquals(new BigDecimal(5), c5_10c.greaterOfLowerLimits(c1_10c));
    Assert.assertEquals(new BigDecimal(5), c1_10c.greaterOfLowerLimits(c5_10c));
    Assert.assertEquals(new BigDecimal(12), c1_10c.greaterOfLowerLimits(c12_16c));
    Assert.assertEquals(new BigDecimal(12), c12_16c.greaterOfLowerLimits(c1_10c));
  }

  public void testLesserOfUpperLimits() {
    Assert.assertEquals(new BigDecimal(10), c5_10c.lesserOfUpperLimits(c1_10c));
    Assert.assertEquals(new BigDecimal(10), c1_10c.lesserOfUpperLimits(c5_10c));
    Assert.assertEquals(new BigDecimal(6), c4_6c.lesserOfUpperLimits(c12_16c));
    Assert.assertEquals(new BigDecimal(6), c12_16c.lesserOfUpperLimits(c4_6c));
  }

  public void testCoversInterval() {
    Assert.assertFalse(c5_10c.covers(c1_10c));
    Assert.assertTrue(c1_10c.covers(c5_10c));
    Assert.assertFalse(c4_6c.covers(c1_10c));
    Assert.assertTrue(c1_10c.covers(c4_6c));
    Assert.assertTrue(c5_10c.covers(c5_10c));
    Interval<BigDecimal> halfOpen5_10 = Interval.over(new BigDecimal(5), false, new BigDecimal(10), true);
    Assert.assertTrue("closed incl left-open", c5_10c.covers(halfOpen5_10));
    Assert.assertTrue("left-open incl left-open", halfOpen5_10.covers(halfOpen5_10));
    Assert.assertFalse("left-open doesn't include closed", halfOpen5_10.covers(c5_10c));
    //TODO: Need to test other half-open case and full-open case.
  }

  public void testGap() {
    Interval<Integer> c1_3c = Interval.closed(new Integer(1), new Integer(3));
    Interval<Integer> c5_7c = Interval.closed(new Integer(5), new Integer(7));
    Interval<Integer> o3_5o = Interval.open(new Integer(3), new Integer(5));
    Interval<Integer> c2_3o = Interval.over(new Integer(2), true, new Integer(3), false);

    Assert.assertEquals(o3_5o, c1_3c.gap(c5_7c));
    Assert.assertTrue(c1_3c.gap(o3_5o).isEmpty());
    Assert.assertTrue(c1_3c.gap(c2_3o).isEmpty());
    Assert.assertTrue(c2_3o.gap(o3_5o).isSingleElement());
  }

  public void testRelativeComplementDisjoint() {
    Interval<Integer> c1_3c = Interval.closed(new Integer(1), new Integer(3));
    Interval<Integer> c5_7c = Interval.closed(new Integer(5), new Integer(7));
    List<Interval<Integer>> complement = c1_3c.complementRelativeTo(c5_7c);
    Assert.assertEquals(1, complement.size());
    Assert.assertEquals(c5_7c, complement.get(0));
  }

  public void testRelativeComplementDisjointAdjacentOpen() {
    Interval<Integer> c1_3o = Interval.over(new Integer(1), true, new Integer(3), false);
    Interval<Integer> c3_7c = Interval.closed(new Integer(3), new Integer(7));
    List<Interval<Integer>> complement = c1_3o.complementRelativeTo(c3_7c);
    Assert.assertEquals(1, complement.size());
    Assert.assertEquals(c3_7c, complement.get(0));
  }

  public void testRelativeComplementOverlapLeft() {
    Interval<Integer> c1_5c = Interval.closed(new Integer(1), new Integer(5));
    Interval<Integer> c3_7c = Interval.closed(new Integer(3), new Integer(7));
    List<Interval<Integer>> complement = c3_7c.complementRelativeTo(c1_5c);
    Interval<Integer> c1_3o = Interval.over(new Integer(1), true, new Integer(3), false);
    Assert.assertEquals(1, complement.size());
    Assert.assertEquals(c1_3o, complement.get(0));
  }

  public void testRelativeComplementOverlapRight() {
    Interval<Integer> c1_5c = Interval.closed(new Integer(1), new Integer(5));
    Interval<Integer> c3_7c = Interval.closed(new Integer(3), new Integer(7));
    List<Interval<Integer>> complement = c1_5c.complementRelativeTo(c3_7c);
    Interval<Integer> o5_7c = Interval.over(new Integer(5), false, new Integer(7), true);
    Assert.assertEquals(1, complement.size());
    Assert.assertEquals(o5_7c, complement.get(0));
  }

  public void testRelativeComplementAdjacentClosed() {
    Interval<Integer> c1_3c = Interval.closed(new Integer(1), new Integer(3));
    Interval<Integer> c5_7c = Interval.closed(new Integer(5), new Integer(7));
    List<Interval<Integer>> complement = c1_3c.complementRelativeTo(c5_7c);
    Assert.assertEquals(1, complement.size());
    Assert.assertEquals(c5_7c, complement.get(0));
  }

  public void testRelativeComplementEnclosing() {
    Interval<Integer> c3_5c = Interval.closed(new Integer(3), new Integer(5));
    Interval<Integer> c1_7c = Interval.closed(new Integer(1), new Integer(7));
    List<Interval<Integer>> complement = c1_7c.complementRelativeTo(c3_5c);
    Assert.assertEquals(0, complement.size());
  }

  public void testRelativeComplementEqual() {
    Interval<Integer> c1_7c = Interval.closed(new Integer(1), new Integer(7));
    List<Interval<Integer>> complement = c1_7c.complementRelativeTo(c1_7c);
    Assert.assertEquals(0, complement.size());
  }

  public void testRelativeComplementEnclosed() {
    Interval<Integer> c3_5c = Interval.closed(new Integer(3), new Integer(5));
    Interval<Integer> c1_7c = Interval.closed(new Integer(1), new Integer(7));
    Interval<Integer> c1_3o = Interval.over(new Integer(1), true, new Integer(3), false);
    Interval<Integer> o5_7c = Interval.over(new Integer(5), false, new Integer(7), true);
    List<Interval<Integer>> complement = c3_5c.complementRelativeTo(c1_7c);
    Assert.assertEquals(2, complement.size());
    Assert.assertEquals(c1_3o, complement.get(0));
    Assert.assertEquals(o5_7c, complement.get(1));
  }

  public void testRelativeComplementEnclosedEndPoint() {
    Interval<Integer> o3_5o = Interval.open(new Integer(3), new Integer(5));
    Interval<Integer> c3_5c = Interval.closed(new Integer(3), new Integer(5));
    List<Interval<Integer>> complement = o3_5o.complementRelativeTo(c3_5c);
    Assert.assertEquals(2, complement.size());
    Assert.assertTrue(complement.get(0).includes(new Integer(3)));
  }

  public void testIsSingleElement() {
    Assert.assertTrue(o1_1c.isSingleElement());
    Assert.assertTrue(c1_1c.isSingleElement());
    Assert.assertTrue(c1_1o.isSingleElement());
    Assert.assertFalse(c1_10c.isSingleElement());
    Assert.assertFalse(o1_1o.isSingleElement());
  }

  public void testEqualsForOnePointIntervals() {
    assertFollowsEqualsContract(o1_1c, c1_1o);
    assertFollowsEqualsContract(o1_1c, c1_1c);
    assertFollowsEqualsContract(c1_1o, c1_1c);
    Assert.assertFalse(o1_1c.equals(o1_1o));
  }

  public void testEqualsForEmptyIntervals() {
    assertFollowsEqualsContract(c1_10c.emptyOfSameType(), c4_6c.emptyOfSameType());
  }

  public void testRelativeComplementEnclosedOpen() {
    Interval<Integer> o3_5o = Interval.open(new Integer(3), new Integer(5));
    Interval<Integer> c1_7c = Interval.closed(new Integer(1), new Integer(7));
    Interval<Integer> c1_3c = Interval.closed(new Integer(1), new Integer(3));
    Interval<Integer> c5_7c = Interval.closed(new Integer(5), new Integer(7));
    List<Interval<Integer>> complement = o3_5o.complementRelativeTo(c1_7c);
    Assert.assertEquals(2, complement.size());
    Assert.assertEquals(c1_3c, complement.get(0));
    Assert.assertEquals(c5_7c, complement.get(1));
  }

  private static <T> void assertFollowsEqualsContract(T a, T b) {
    Assert.assertEquals(a, b);
    Assert.assertEquals(b, a);
    Assert.assertEquals(a.hashCode(), b.hashCode());
  }

}

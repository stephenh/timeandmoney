/**
 *
 * This code is provided under the following licence: 
 * MIT Licence
 * timeandmoney.sourceforge.net
 * Copyright 2004, Domain Language, Inc.
 */

package fundamental;

import java.math.BigDecimal;

import junit.framework.TestCase;


public class IntervalTest extends TestCase {
	  public void testBasic() {
	  	Interval range = Interval.closed(new BigDecimal(-5.5), new BigDecimal(6.6));
	    assertTrue (range.includes(new BigDecimal(5.0)));
	    assertTrue (range.includes(new BigDecimal(-5.5)));
	    assertTrue (range.includes(new BigDecimal(-5.4999)));
	    assertTrue (range.includes(new BigDecimal(6.6)));
	    assertFalse (range.includes(new BigDecimal(6.601)));
	    assertFalse (range.includes(new BigDecimal(-5.501)));
	  }

	  public void testExclusives() {
	  	Interval exRange = new Interval(new BigDecimal(-5.5), false, new BigDecimal(6.6), true);
	    assertTrue (exRange.includes(new BigDecimal(5.0)));
	    assertFalse (exRange.includes(new BigDecimal(-5.5)));
	    assertTrue (exRange.includes(new BigDecimal(-5.4999)));
	    assertTrue (exRange.includes(new BigDecimal(6.6)));
	    assertFalse (exRange.includes(new BigDecimal(6.601)));
	    assertFalse (exRange.includes(new BigDecimal(-5.501)));
	  }
	  	

//		public void testEmpty() {
//	   assertTrue (!Interval.closed(new BigDecimal(5),new BigDecimal(6)).isEmpty());
//	   assertTrue (!Interval.closed(new BigDecimal(6),new BigDecimal(6)).isEmpty());
//	   assertTrue (Interval.closed(new BigDecimal(7),new BigDecimal(6)).isEmpty());
//	  }
//	  public void testUpTo() {
//	    Interval range = Interval.upTo(new BigDecimal(5.5));
//	    assertTrue(range.includes(new BigDecimal(5.5)));
//	    assertTrue(range.includes(new BigDecimal(-5.5)));
//	    assertTrue(range.includes(new BigDecimal(Double.NEGATIVE_INFINITY)));
//	    assertTrue(!range.includes(new BigDecimal(5.5001)));
//	  }
//	  public void testAndMore() {
//	    Interval range = Interval.andMore(5.5);
//	    assertTrue(range.includes(5.5));
//	    assertTrue(!range.includes(5.4999));
//	    assertTrue(!range.includes(-5.5));
//	    assertTrue(range.includes(Double.POSITIVE_INFINITY));
//	    assertTrue(range.includes(5.5001));
//	  }
		public void testIntersection() {
	   	Interval r5_10 = Interval.closed(new BigDecimal(5),new BigDecimal(10));
	    Interval r1_10 = Interval.closed(new BigDecimal(1),new BigDecimal(10));
	    Interval r4_6 = Interval.closed(new BigDecimal(4),new BigDecimal(6));
	    Interval r5_15 = Interval.closed(new BigDecimal(5),new BigDecimal(15));
	    Interval r12_16 = Interval.closed(new BigDecimal(12),new BigDecimal(16));
	    Interval x10_12 = new Interval(new BigDecimal(10), false, new BigDecimal(12), true);
	    assertTrue ("r5_10.intersects(r1_10)",r5_10.intersects(r1_10));
	    assertTrue ("r1_10.intersects(r5_10)",r1_10.intersects(r5_10));
	    assertTrue ("r4_6.intersects(r1_10)",r4_6.intersects(r1_10));
	    assertTrue ("r1_10.intersects(r4_6)",r1_10.intersects(r4_6));
	    assertTrue ("r5_10.intersects(r5_15)",r5_10.intersects(r5_15));
	    assertTrue ("r5_15.intersects(r1_10)",r5_15.intersects(r1_10));
	    assertTrue ("r1_10.intersects(r5_15)",r1_10.intersects(r5_15));
	    assertFalse ("r1_10.intersects(r12_16)",r1_10.intersects(r12_16));
	    assertFalse ("r12_16.intersects(r1_10)",r12_16.intersects(r1_10));
	    assertTrue ("r5_10.intersects(r5_10)",r5_10.intersects(r5_10));
	    assertFalse ("r1_10.intersects(x10_12)",r1_10.intersects(x10_12));
	    assertFalse ("x10_12.intersects(r1_10)",x10_12.intersects(r1_10));
	 	}
		public void testIncludesRange() {
	   	Interval r5_10 = Interval.closed(new BigDecimal(5),new BigDecimal(10));
	    Interval r1_10 = Interval.closed(new BigDecimal(1),new BigDecimal(10));
	    Interval r4_6 = Interval.closed(new BigDecimal(4),new BigDecimal(6));
	    assertFalse (r5_10.includes(r1_10));
	    assertTrue (r1_10.includes(r5_10));
	    assertFalse (r4_6.includes(r1_10));
	    assertTrue (r1_10.includes(r4_6));
	    assertTrue (r5_10.includes(r5_10));
	    Interval halfOpen5_10 = new Interval(new BigDecimal(5), false, new BigDecimal(10), true);
	    assertTrue("closed incl left-open", r5_10.includes(halfOpen5_10));
	    assertTrue("left-open incl left-open", halfOpen5_10.includes(halfOpen5_10));
	    assertFalse("left-open doesn't include closed", halfOpen5_10.includes(r5_10));
	    //TODO: Need to test other half-open case and full-open case.

	 	}

}

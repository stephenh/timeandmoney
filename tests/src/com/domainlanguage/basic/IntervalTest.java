/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;

import java.math.BigDecimal;
import java.util.List;

import com.domainlanguage.testutil.SerializationTest;

import junit.framework.TestCase;


public class IntervalTest extends TestCase {

   	Interval r5_10 = Interval.closed(new BigDecimal(5),new BigDecimal(10));
    Interval r1_10 = Interval.closed(new BigDecimal(1),new BigDecimal(10));
    Interval r4_6 = Interval.closed(new BigDecimal(4),new BigDecimal(6));
    Interval r5_15 = Interval.closed(new BigDecimal(5),new BigDecimal(15));
    Interval r12_16 = Interval.closed(new BigDecimal(12),new BigDecimal(16));
    Interval x10_12 = Interval.over(new BigDecimal(10), false, new BigDecimal(12), true);

    
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

    public void testSerialization() {
    	SerializationTest.assertSerializationWorks(r5_10);
    }
    
    public void testAbstractCreation() {
    	Interval concrete = new ConcreteInterval(new Integer(1), true, new Integer(3), true);
    	Interval newInterval = concrete.newOfSameType(new Integer(1), false, new Integer(4), false);
    	assertTrue(newInterval instanceof ConcreteInterval);
    	Interval expected = new ConcreteInterval(new Integer(1), false, new Integer(4), false); 
    	assertEquals(expected, newInterval);
    }
    
	public void testIsBelow() {
	  	Interval range = Interval.closed(new BigDecimal(-5.5), new BigDecimal(6.6));
	    assertFalse (range.isBelow(new BigDecimal(5.0)));
	    assertFalse (range.isBelow(new BigDecimal(-5.5)));
	    assertFalse (range.isBelow(new BigDecimal(-5.4999)));
	    assertFalse (range.isBelow(new BigDecimal(6.6)));
	    assertTrue (range.isBelow(new BigDecimal(6.601)));
	    assertFalse (range.isBelow(new BigDecimal(-5.501)));
	  }

	public void testIncludes() {
	  	Interval range = Interval.closed(new BigDecimal(-5.5), new BigDecimal(6.6));
	    assertTrue (range.includes(new BigDecimal(5.0)));
	    assertTrue (range.includes(new BigDecimal(-5.5)));
	    assertTrue (range.includes(new BigDecimal(-5.4999)));
	    assertTrue (range.includes(new BigDecimal(6.6)));
	    assertFalse (range.includes(new BigDecimal(6.601)));
	    assertFalse (range.includes(new BigDecimal(-5.501)));
	  }

	  public void testOpenInterval() {
	  	Interval exRange = Interval.over(new BigDecimal(-5.5), false, new BigDecimal(6.6), true);
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
		public void testIntersects() {
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
		
		
		
		public void testGreaterOfLowerLimits() {
		    assertEquals(new BigDecimal(5), r5_10.greaterOfLowerLimits(r1_10));
		    assertEquals(new BigDecimal(5), r1_10.greaterOfLowerLimits(r5_10));
		    assertEquals(new BigDecimal(12), r1_10.greaterOfLowerLimits(r12_16));
		    assertEquals(new BigDecimal(12), r12_16.greaterOfLowerLimits(r1_10));
	 	}

		public void testLesserOfUpperLimits() {
		    assertEquals(new BigDecimal(10), r5_10.lesserOfUpperLimits(r1_10));
		    assertEquals(new BigDecimal(10), r1_10.lesserOfUpperLimits(r5_10));
		    assertEquals(new BigDecimal(6), r4_6.lesserOfUpperLimits(r12_16));
		    assertEquals(new BigDecimal(6), r12_16.lesserOfUpperLimits(r4_6));
	 	}

		
		public void testIncludesRange() {
	    assertFalse (r5_10.includes(r1_10));
	    assertTrue (r1_10.includes(r5_10));
	    assertFalse (r4_6.includes(r1_10));
	    assertTrue (r1_10.includes(r4_6));
	    assertTrue (r5_10.includes(r5_10));
	    Interval halfOpen5_10 = Interval.over(new BigDecimal(5), false, new BigDecimal(10), true);
	    assertTrue("closed incl left-open", r5_10.includes(halfOpen5_10));
	    assertTrue("left-open incl left-open", halfOpen5_10.includes(halfOpen5_10));
	    assertFalse("left-open doesn't include closed", halfOpen5_10.includes(r5_10));
	    //TODO: Need to test other half-open case and full-open case.

	 	}

		public void testRelativeComplementDisjoint() {
			Interval c1_3c = Interval.closed(new Integer(1),new Integer(3));
			Interval c5_7c = Interval.closed(new Integer(5), new Integer(7));
			List complement = c1_3c.complementRelativeTo(c5_7c);
		    assertEquals(1, complement.size());
		    assertEquals(c5_7c,complement.get(0));
		}

		public void testRelativeComplementDisjointAdjacentOpen() {
			Interval c1_3o = Interval.over(new Integer(1), true, new Integer(3), false);
			Interval c3_7c = Interval.closed(new Integer(3), new Integer(7));
			List complement = c1_3o.complementRelativeTo(c3_7c);
		    assertEquals(1, complement.size());
		    assertEquals(c3_7c,complement.get(0));
		}
		
		
		public void testRelativeComplementOverlapLeft() {
			Interval c1_5c = Interval.closed(new Integer(1), new Integer(5));
			Interval c3_7c = Interval.closed(new Integer(3), new Integer(7));
			List complement = c3_7c.complementRelativeTo(c1_5c);
		    Interval c1_3o = Interval.over(new Integer(1), true, new Integer(3), false);
		    assertEquals(1, complement.size());
		    assertEquals(c1_3o,complement.get(0));
		}

		public void testRelativeComplementOverlapRight() {
			Interval c1_5c = Interval.closed(new Integer(1), new Integer(5));
			Interval c3_7c = Interval.closed(new Integer(3), new Integer(7));
			List complement = c1_5c.complementRelativeTo(c3_7c);
		    Interval o5_7c = Interval.over(new Integer(5), false, new Integer(7), true);
		    assertEquals(1, complement.size());
		    assertEquals(o5_7c,complement.get(0));
		}

		public void testRelativeComplementAdjacentClosed() {
			Interval c1_3c = Interval.closed(new Integer(1),new Integer(3));
			Interval c5_7c = Interval.closed(new Integer(5), new Integer(7));
			List complement = c1_3c.complementRelativeTo(c5_7c);
		    assertEquals(1, complement.size());
		    assertEquals(c5_7c,complement.get(0));
		}

		public void testRelativeComplementEnclosing() {
			Interval c3_5c = Interval.closed(new Integer(3),new Integer(5));
			Interval c1_7c = Interval.closed(new Integer(1), new Integer(7));
			List complement = c1_7c.complementRelativeTo(c3_5c);
		    assertEquals(0, complement.size());
		}

		public void testRelativeComplementEqual() {
			Interval c1_7c = Interval.closed(new Integer(1), new Integer(7));
			List complement = c1_7c.complementRelativeTo(c1_7c);
		    assertEquals(0, complement.size());
		}
		
		public void testRelativeComplementEnclosed() {
			Interval c3_5c = Interval.closed(new Integer(3),new Integer(5));
			Interval c1_7c = Interval.closed(new Integer(1), new Integer(7));
			Interval c1_3o = Interval.over(new Integer(1), true, new Integer(3), false);
			Interval o5_7c = Interval.over(new Integer(5), false, new Integer(7), true);
			List complement = c3_5c.complementRelativeTo(c1_7c);
		    assertEquals(2, complement.size());
		    assertEquals(c1_3o,complement.get(0));
		    assertEquals(o5_7c,complement.get(1));
		}

		public void testRelativeComplementEnclosedEndPoint() {
			Interval o3_5o = Interval.open(new Integer(3),new Integer(5));
			Interval c3_5c = Interval.closed(new Integer(3),new Integer(5));
			List complement = o3_5o.complementRelativeTo(c3_5c);
		    assertEquals(2, complement.size());
		    assertTrue(((Interval)complement.get(0)).includes(new Integer(3)));
		}

//		TODO: The following test seems logical to me, based on the sets, but need to look up the definitions.
//		public void testEqualsForOnePointIntervals() {
//			Interval o1_1c = Interval.over(new Integer(1), false, new Integer(1), true);
//			Interval c1_1o = Interval.over(new Integer(1), true, new Integer(1), false);
//			Interval c1_1c = Interval.over(new Integer(1), true, new Integer(1), true);
//			Interval o1_1o = Interval.over(new Integer(1), false, new Integer(1), false);
//			assertEquals(o1_1c, c1_1o);
//			assertEquals(o1_1c, c1_1c);
//			assertEquals(c1_1o, c1_1c);
//			assertFalse(o1_1c.equals(o1_1o));
//		}
		
		public void testRelativeComplementEnclosedOpen() {
			Interval o3_5o = Interval.open(new Integer(3),new Integer(5));
			Interval c1_7c = Interval.closed(new Integer(1), new Integer(7));
			Interval c1_3c = Interval.closed(new Integer(1), new Integer(3));
			Interval c5_7c = Interval.closed(new Integer(5), new Integer(7));
			List complement = o3_5o.complementRelativeTo(c1_7c);
		    assertEquals(2, complement.size());
		    assertEquals(c1_3c,complement.get(0));
		    assertEquals(c5_7c,complement.get(1));
		}


		
}

/**
 *
 * This code is provided under the following licence: 
 * MIT Licence
 * timeandmoney.sourceforge.net
 * Copyright 2004, Domain Language, Inc.
 */

package timelanguage;

import junit.framework.*;

public class MFDateIntervalTest extends TestCase {
	
	  CalendarDate dec15 = new CalendarDate(1999,12,15);
	  CalendarDate jan2 = new CalendarDate(2000,1,2);
	  CalendarDate jan10 = new CalendarDate(2000,1,10);
	  CalendarDate jan11 = new CalendarDate(2000,1,11);
	  CalendarDate jan15 = new CalendarDate(2000,1,15);
	  CalendarDate dec14 = new CalendarDate(1999,12,14);
	  CalendarDate jan16 = new CalendarDate(2000,1,16);
	  CalendarDate jan1 = new CalendarDate(2000,1,1);
	  CalendarDate feb2 = new CalendarDate(2000,2,2);
	    CalendarDate feb3 = new CalendarDate(2000,2,3);
	    CalendarDate feb8 = new CalendarDate(2000,2,8);
	    CalendarDate feb9 = new CalendarDate(2000,2,9);

	  CalendarInterval r15_15 = dec15.through(jan15);
	  CalendarInterval r15_16 = jan15.through(jan16);
	  CalendarInterval r16_2 = jan16.through(feb2);
	  CalendarInterval r1_16 = jan1.through(jan16);
	  CalendarInterval r1_15 =  jan1.through(jan15);
	  CalendarInterval r1_10 =  jan1.through(jan10);
	  CalendarInterval r2_2 =  feb2.through(feb2);
	  CalendarInterval f3_9 =  feb3.through(feb9);
	  CalendarInterval j1_f9 =  jan1.through(feb9);
	  CalendarInterval j2_15 =  jan2.through(jan15);
	  CalendarInterval f3_8 =  feb3.through(feb8);

		CalendarInterval[] contiguous = {r1_15, r16_2, f3_9};
		CalendarInterval[] withGap = {r1_15, f3_9};
		CalendarInterval[] overlap = {r1_15, r1_10, r16_2, f3_9};

		
		  public void testBasic() {
		    assertTrue(r15_15.includes(jan1));
		    assertTrue(r15_15.includes(jan15));
		    assertTrue(r15_15.includes(dec15));
		    assertTrue(!r15_15.includes(jan16));
		    assertTrue(!r15_15.includes(dec14));
		  }

		  public void testOneDate(){
		    assertTrue(r2_2.includes(feb2));
		    assertTrue(r2_2.includes(r2_2));
		    assertTrue(r16_2.includes(r2_2));
		  }

//		  public void testEmpty() {
//		    assertFalse(r15_15.isEmpty());
//		    assertTrue(dec15.through(dec14).isEmpty());
//		    assertTrue(CalendarInterval.NEVER.isEmpty());
//		  }
		  
		  public void testIncludesRange() {
		    assertTrue(r15_15.includes(r15_15));
		    assertTrue(r15_15.includes(r1_15));
		    assertTrue(!r1_15.includes(r15_15));
		    assertTrue(!r1_16.includes(r15_15));
			}
		  
//		  public void testOverlaps() {
//		  	assertTrue(r1_15.intersects(r1_16));
//		  	assertTrue(r1_16.intersects(r1_15));
//		  	assertTrue(r15_15.intersects(r15_15));
//		  	assertTrue(r15_15.intersects(r1_15));
//		  	assertTrue(r15_15.intersects(r15_16));
//		  	assertTrue(r15_16.intersects(r15_15));
//		  	assertFalse(r15_15.intersects(r16_2));
//		  	assertFalse(r16_2.intersects(r15_15));
//		  }
		  
		 	public void testEquals() {
		    assertEquals(r1_15,CalendarInterval.inclusive(jan1, jan15));
		    assertFalse(r1_15.equals(r1_16));
		    assertFalse(r15_15.equals(r15_16));
		  }
//			public void testCompare() {
//		   	assertTrue(dec15.compareTo(jan1) < 0);
//		 	assertTrue(r15_15.compareTo(r1_15) < 0);
//		   	assertTrue(r1_15.compareTo(r1_16) < 0);
//		    assertTrue(r1_15.compareTo(r1_15) == 0);
//		   	assertTrue(r1_16.compareTo(r1_15) > 0);
//		    assertTrue(r15_15.compareTo(r1_10) < 0);
//			}

//			public void testGap(){
//				DateRange actual = r1_10.gap(r16_2);
//				DateRange expected = new DateRange(jan11,jan15);
//				assertEquals(expected, r1_10.gap(r16_2));
//				assertEquals(expected, r16_2.gap(r1_10));
//
//				assert(r15_15.gap(r1_10).isEmpty());
//				assert(r1_15.gap(r15_16).isEmpty());
//				assert(r1_15.gap(r16_2).isEmpty());
//			}
//		    public void testAbuts() {
//			assert(r1_15.abuts(r16_2));
//			assert(r16_2.abuts(r1_15));
//			assert(!r1_15.abuts(r15_15));
//			assert(!r1_10.abuts(r15_16));
//		    }
//		    public void testCombine(){
//			assertEquals(j1_f9, DateRange.combination(complete));
//		    }
//
//		    public void testContiguous() {
//			assert(DateRange.isContiguous(complete));
//			assert(!DateRange.isContiguous(withGap));
//			assert(!DateRange.isContiguous(overlap));
//		    }
//
//		    public void testPartition() {
//				assert (j1_f9.partitionedBy(complete));
//				assert (!j1_f9.partitionedBy(withGap));
//				assert (!j1_f9.partitionedBy(overlap));
//				DateRange[] off_end = {r15_15, r16_2, f3_9};
//				assert (!j1_f9.partitionedBy(off_end));
//				DateRange[] missingStart = {j2_15, r16_2, f3_9};
//				assert (!j1_f9.partitionedBy(missingStart));
//				DateRange[] missingEnd = {r1_15, r16_2, f3_8};
//				assert (!j1_f9.partitionedBy(missingEnd));
//		    }
//
//		    public void testStarting() {
//				DateRange dr = DateRange.startingOn(dec15);
//				assert(dr.includes(jan2));
//			}

}

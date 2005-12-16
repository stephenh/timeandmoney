/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.time;

import java.util.*;

import com.domainlanguage.intervals.*;
import com.domainlanguage.util.*;


public class TimeInterval extends Interval {
	private Comparable lowerLimit;
	private boolean includesLowerLimit;
	private Comparable upperLimit;
	private boolean includesUpperLimit;
    
	public static final TimeInterval ALWAYS = over(TimePoint.FAR_PAST, TimePoint.FAR_FUTURE);

	public static TimeInterval over(TimePoint start, boolean closedStart, TimePoint end, boolean closedEnd) {
		return new TimeInterval(start, closedStart, end, closedEnd);
	}

	public static TimeInterval over(TimePoint start, TimePoint end) {
		//Uses the common default for time intervals, [start, end).
		return over(start, true, end, false);
	}

	public static TimeInterval startingFrom(TimePoint start, boolean startClosed, Duration length, boolean endClosed) {
		TimePoint end = start.plus(length);
		return over(start, startClosed, end, endClosed);
	}
	
	public static TimeInterval startingFrom(TimePoint start, Duration length) {
		//Uses the common default for time intervals, [start, end).
		return startingFrom(start, true, length, false);
	}

	public static TimeInterval preceding(TimePoint end, boolean startClosed, Duration length, boolean endClosed) {
		TimePoint start = end.minus(length);
		return over(start, startClosed, end, endClosed);
	}
	
	public static TimeInterval preceding(TimePoint end, Duration length) {
		//Uses the common default for time intervals, [start, end).
		return preceding(end, true, length, false);
	}

	public static TimeInterval closed(TimePoint start, TimePoint end) {
		return over(start, true, end, true);
	}

	public static TimeInterval open(TimePoint start, TimePoint end) {
		return over(start, false, end, false);
	}
	
	public static TimeInterval everFrom(TimePoint start) {
		return over(start, TimePoint.FAR_FUTURE);
	} 

	public static TimeInterval everPreceding(TimePoint end) {
		return over(TimePoint.FAR_PAST, end);
	} 

	public TimeInterval(TimePoint start, boolean startIncluded, TimePoint end, boolean endIncluded) {
//		assert start.compareTo(end) < 0; \
//This should really be an Interval invariant.
//	revisit: also, as with any java assert, an AssertionError will be thrown only
// if compiled with the -ea option (enableassertions)
//	so maybe we want to throw an IllegalArgumentException instead?
		assert start.compareTo(end) <= 0;
		lowerLimit = start;
		includesLowerLimit = startIncluded;
		upperLimit = end;
		includesUpperLimit = endIncluded;
	}
	
	public Comparable upperLimit() {
		return upperLimit;
	}
	
	public Comparable lowerLimit() {
		return lowerLimit;
	}
	
	public boolean includesLowerLimit() {
		return includesLowerLimit;
	}
	
	public boolean includesUpperLimit() {
		return includesUpperLimit;
	}
	
	public Interval newOfSameType(Comparable start, boolean isStartClosed, Comparable end, boolean isEndClosed) {
		return new TimeInterval((TimePoint) start, isStartClosed, (TimePoint) end, isEndClosed);
	}
	
	public boolean isBefore(TimePoint point) {
		return isBelow(point);
	}

	public boolean isAfter(TimePoint point) {
		return isAbove(point);
	}

	public Duration length() {
		long difference = end().millisecondsFromEpoc - start().millisecondsFromEpoc;
		return Duration.milliseconds(difference);
	}
	
	public Iterator daysIterator() {
		return new ImmutableIterator() {
			TimePoint next = start();
			public boolean hasNext() {
				return end().isAfter(next);
			}	
			public Object next() {
				Object current = next;
				next = next.nextDay();
				return current;
			}
		};
	}

	public Iterator subintervalIterator(Duration subintervalLength) {
		final Duration segmentLength = subintervalLength;
		final Interval totalInterval = this;
		return new ImmutableIterator() {
			TimeInterval next = segmentLength.startingFrom(start());
			public boolean hasNext() {
				return totalInterval.covers(next);
			}	
			public Object next() {
				if (!hasNext()) return null;
				Object current = next;
				next = segmentLength.startingFrom(next.end());
				return current;
			}
		};
	}

	public TimePoint start() {
		return (TimePoint) lowerLimit();
	}
	
	public TimePoint end() {
		return (TimePoint) upperLimit();
	}
	
	public TimeInterval intersect(TimeInterval interval) {
		return (TimeInterval)intersect((Interval)interval);
	}

    //for Hibernate
    TimeInterval() {
        //only for Hibernate
    }
    
    private boolean isIncludesLowerLimitForPersistentMapping() {
        return includesLowerLimit;
    }

    private void setIncludesLowerLimitForPersistentMapping(boolean includesLowerLimit) {
        this.includesLowerLimit = includesLowerLimit;
    }

    private boolean isIncludesUpperLimitForPersistentMapping() {
        return includesUpperLimit;
    }

    private void setIncludesUpperLimitForPersistentMapping(boolean includesUpperLimit) {
        this.includesUpperLimit = includesUpperLimit;
    }

    private Comparable getLowerLimitForPersistentMapping() {
        return lowerLimit;
    }

    private void setLowerLimitForPersistentMapping(Comparable lowerLimit) {
        this.lowerLimit = lowerLimit;
    }

    private Comparable getUpperLimitForPersistentMapping() {
        return upperLimit;
    }

    private void setUpperLimitForPersistentMapping(Comparable upperLimit) {
        this.upperLimit = upperLimit;
    }

	
}
 
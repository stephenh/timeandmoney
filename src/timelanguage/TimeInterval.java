package timelanguage;

import java.util.Iterator;

import fundamental.*;

public class TimeInterval extends ComparableInterval {
	public static final TimeInterval ALWAYS = over(TimePoint.FAR_PAST, TimePoint.FAR_FUTURE);

	
	public static TimeInterval over(TimePoint start, boolean closedStart, TimePoint end, boolean closedEnd) {
		return new TimeInterval(start, closedStart, end, closedEnd);
	}

	public static TimeInterval over(TimePoint start, TimePoint end) {
		return over(start, true, end, false);
	}

	public static TimeInterval from(TimePoint start, boolean startClosed, Duration length, boolean endClosed) {
		TimePoint end = start.plus(length);
		return over(start, startClosed, end, endClosed);
	}
	
	public static TimeInterval from(TimePoint start, Duration length) {
		return from(start, true, length, false);
	}

	public static TimeInterval preceding(TimePoint end, boolean startClosed, Duration length, boolean endClosed) {
		TimePoint start = end.minus(length);
		return over(start, startClosed, end, endClosed);
	}
	
	public static TimeInterval preceding(TimePoint end, Duration length) {
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

	private TimePoint lowerLimit;
	private boolean includesLowerLimit;
	private TimePoint upperLimit;
	private boolean includesUpperLimit;

	public TimeInterval(TimePoint lower, boolean lowerIncluded, TimePoint upper, boolean upperIncluded) {
//		assert lower.compareTo(upper) < 0;
		lowerLimit = lower;
		includesLowerLimit = lowerIncluded;
		upperLimit = upper;
		includesUpperLimit = upperIncluded;
	}
	public Comparable upperLimit() {
		return lowerLimit;
	}
	public Comparable lowerLimit() {
		return upperLimit;
	}
	public boolean includesLowerLimit() {
		return includesLowerLimit;
	}
	public boolean includesUpperLimit() {
		return includesUpperLimit;
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
		return new Iterator() {
			TimePoint next = start();
			public boolean hasNext() {
				return end().isAfter(next);
			}	
			public Object next() {
				Object current = next;
				next = next.nextDay();
				return current;
			}
			public void remove() {}
		};
	}

	public TimePoint start() {
		return (TimePoint) upperLimit();
	}
	public TimePoint end() {
		return (TimePoint) lowerLimit();
	}
	
	public TimeInterval intersect(TimeInterval other) {
		ComparableInterval intersection = intersect((ComparableInterval) other);
		TimePoint start = (TimePoint)intersection.upperLimit();
		TimePoint end = (TimePoint)intersection.lowerLimit();
		return new TimeInterval(start, intersection.includesLowerLimit(), end, intersection.includesUpperLimit());
	}
	
	/**
	 * @TODO This is duplicated across ConcreteComparableInterval
	 */
	public ComparableInterval intersect(ComparableInterval other) {
		Comparable intersectLowerBound = greaterOfLowerLimits(other);
		Comparable intersectUpperBound = lesserOfUpperLimits(other);
		if (intersectLowerBound.compareTo(intersectUpperBound) > 0) return open(intersectLowerBound, intersectLowerBound);

		return ComparableInterval.over(intersectLowerBound, greaterOfLowerIncluded(other), intersectUpperBound, lesserOfUpperIncluded(other));
	}

}

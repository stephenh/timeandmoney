package timelanguage;

import java.util.Iterator;

import fundamental.*;

public class TimeInterval extends ConcreteComparableInterval {
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

	public TimeInterval(TimePoint start, boolean closedStart, TimePoint end, boolean closedEnd) {
		super(start, closedStart, end, closedEnd);
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
		return (TimePoint) getLowerBound();
	}
	public TimePoint end() {
		return (TimePoint) getUpperBound();
	}
	
	public TimeInterval intersect(TimeInterval other) {
		ComparableInterval intersection = intersect((ComparableInterval) other);
		TimePoint start = (TimePoint)intersection.getLowerBound();
		TimePoint end = (TimePoint)intersection.getUpperBound();
		return new TimeInterval(start, intersection.isLowerBoundIncluded(), end, intersection.isUpperBoundIncluded());
	}

}

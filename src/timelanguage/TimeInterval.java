package timelanguage;
/**
 * Definition consistent with mathematical "interval"
 * see http://en.wikipedia.org/wiki/Interval_(mathematics)
 * 
 */
import java.util.Iterator;

public class TimeInterval {
	public static final TimeInterval ALWAYS = over(TimePoint.FAR_PAST, TimePoint.FAR_FUTURE);
	public static final TimeInterval NEVER = over(TimePoint.FAR_FUTURE, TimePoint.FAR_FUTURE);

	public final TimePoint start;
	public final TimePoint end;
	public final boolean closedStart;
	public final boolean closedEnd;

	
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

	public static TimeInterval until(TimePoint end, boolean startClosed, Duration length, boolean endClosed) {
		TimePoint start = end.minus(length);
		return over(start, startClosed, end, endClosed);
	}
	
	public static TimeInterval until(TimePoint end, Duration length) {
		return until(end, true, length, false);
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

	public static TimeInterval everUntil(TimePoint end) {
		return over(TimePoint.FAR_PAST, end);
	} 

	private TimeInterval(TimePoint start, boolean closedStart, TimePoint end, boolean closedEnd) {
		this.start = start;
		this.closedStart = closedStart;
		this.end = end;
		this.closedEnd = closedEnd;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append(closedStart ? "[" : "(");
		buffer.append(start.toString());
		buffer.append(", ");
		buffer.append(end.toString());
		buffer.append(closedEnd ? "]" : ")");
		return buffer.toString();
	}

	public boolean includes(TimePoint point) {
		return !this.isBefore(point) && !this.isAfter(point);
	}

	public boolean isBefore(TimePoint point) {
		if (end().isBefore(point)) return true;
		if (end().isAfter(point)) return false;
		//Equal to end, so period is before it if it isn't included
		return !closedEnd;
	}

	public boolean isAfter(TimePoint point) {
		if (start().isAfter(point)) return true;
		if (start().isBefore(point)) return false;
		//must be equal to start, so period is after it if it isn't included
		return !closedStart;
	}

	public int compareTo(Object arg) {
		TimeInterval other = (TimeInterval) arg;
		if (!start().equals(other.start())) return start().compareTo(other.start());
		return end().compareTo(other.end());
	}
	
	public Duration length() {
		long difference = end.millisecondsFromEpoc - start.millisecondsFromEpoc;
		return Duration.milliseconds(difference);
	}
	
	public Iterator daysIterator() {
		return new Iterator() {
			TimePoint next = start;
			public boolean hasNext() {
				return end.isAfter(next);
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
		return start;
	}
	public TimePoint end() {
		return end;
	}
	

	public TimeInterval intersect(TimeInterval other) {
		TimePoint intersectStart = start();
		TimePoint intersectEnd = end();
		if (start().isBefore(other.start())) {
			intersectStart = other.start();
		} else if (start().isAfter(other.start())) {
			intersectStart = start();
		}
		if (end().isAfter(other.end())) {
			intersectEnd = other.end();
		} else if (end().isBefore(other.end())) {
			intersectEnd = end();
		}
		if (intersectEnd.isBefore(intersectStart)) return NEVER;
		return TimeInterval.over(intersectStart, intersectEnd);
	}
	
	public boolean intersects(TimeInterval other) {
		TimeInterval intersection = this.intersect(other);
		return !(intersection.start().equals(NEVER.start()) && intersection.end().equals(NEVER.end()));
	}
}

package timelanguage;
/**
 * Definition consistent with mathematical "interval"
 * see http://en.wikipedia.org/wiki/Interval_(mathematics)
 * 
 */
import java.util.Iterator;

public class TimeInterval {
	public static final TimeInterval FOREVER = from(TimePoint.THE_BEGGINING, TimePoint.THE_END);

	public final TimePoint start;
	public final TimePoint end;
	public final boolean closedStart;
	public final boolean closedEnd;

	public static TimeInterval from(TimePoint start, boolean startClosed, Duration length, boolean endClosed) {
		TimePoint end = start.plus(length);
		return from(start, startClosed, end, endClosed);
	}
	
	public static TimeInterval from(TimePoint start, boolean closedStart, TimePoint end, boolean closedEnd) {
		return new TimeInterval(start, closedStart, end, closedEnd);
	}

	public static TimeInterval from(TimePoint start, TimePoint end) {
		return closed(start, end);
	} 

	public static TimeInterval closed(TimePoint start, TimePoint end) {
		return from(start, true, end, true);
	}

	public static TimeInterval open(TimePoint start, TimePoint end) {
		return from(start, false, end, false);
	}
	
	public static TimeInterval startsAt(TimePoint start) {
		return from(start, TimePoint.THE_END);
	} 

	public static TimeInterval endsAt(TimePoint end) {
		return from(TimePoint.THE_BEGGINING, end);
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
		return isOnTheEdge(point) || isInTheMiddle(point);
	}

	public boolean isInTheMiddle(TimePoint point) {
		return point.isAfter(start) && point.isBefore(end);
	}

	public boolean isOnTheEdge(TimePoint point) {
		return 
			(closedStart && start.equals(point)) || 
			(closedEnd && end.equals(point));
	}

	public boolean isBefore(TimePoint point) {
		if (end.isBefore(point)) return true;
		if (end.isAfter(point)) return false;
		//must be equal to end, so period is before it if it isn't included
		return !closedEnd;
	}

	public boolean isAfter(TimePoint point) {
		if (start.isAfter(point)) return true;
		if (start.isBefore(point)) return false;
		//must be equal to start, so period is after it if it isn't included
		return !closedStart;
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
				next = next.plus(Duration.days(1));
				return current;
			}
			public void remove() {}
		};
	}
	
}

package timelanguage;

import java.util.*;


class BusinessCalendar {
	static BusinessCalendar current = new BusinessCalendar();
	
	private Set holidays;
	
	private BusinessCalendar() {
		holidays = initializeHolidays();		
	}

	private Set initializeHolidays() {
		Set collected = new HashSet();
		String[] dates = HolidayDates.ALL;
		for (int i = 0; i < dates.length; i++)
			collected.add(TimePoint.from(dates[i], "yyyy/MM/dd"));
		return collected;
	}

	int getElapsedBusinessDays(TimeInterval interval) {
		int tally = 0;
		Iterator it = interval.daysIterator();
		while (it.hasNext()) {
			tally++;
			it.next();
		}
		return tally;
	}

	boolean isHoliday(TimePoint day) {
		for (Iterator iterator = holidays.iterator(); iterator.hasNext();) {
			TimePoint each = (TimePoint) iterator.next();
			if (each.isSameCalendarDayAs(day)) 
				return true;
		}
		return false;
	}

	boolean isBusinessDay(TimePoint day) {
		return !day.isWeekend() && !isHoliday(day);
	}

	private static final int openForBusiness = (8 * 60); /* 8:00 AM */
	private static final int closeForBusiness = (17 * 60); /* 5:00 PM */
	boolean isBusinessHours(TimePoint now) {
		Calendar date = now.asJavaCalendar();
		int theHour = date.get(Calendar.HOUR_OF_DAY);
		int theMinute = date.get(Calendar.MINUTE);
		int timeAsMinutes = (theHour * 60) + theMinute;
		return 
			timeAsMinutes >= openForBusiness && 
			timeAsMinutes <=  closeForBusiness;
	}

	boolean isInBusiness(TimePoint now) {
		return isBusinessDay(now) && isBusinessHours(now);
	}

	/** Returns true if <now> is a holiday.  
		An alternative to using <Holidays.ALL>
		
		It makes no effort to recognize "half-day holidays", 
		such as the Wednesday before Thanksgiving.
		Currently, it only recognizes these holidays:
			New Year's Day
			MLK Day
			President's Day
			Memorial Day
			Independence Day
			Labor Day
			Thanksgiving
			Christmas
	 */
	boolean isFederalHoliday(Calendar now) {
		int[] month_date = { Calendar.JANUARY, 1, Calendar.JULY, 4, Calendar.DECEMBER, 25, };
		int[] month_weekday_monthweek = { Calendar.JANUARY, Calendar.MONDAY, 3, // MLK Day, 3rd monday in Jan
			Calendar.FEBRUARY, Calendar.MONDAY, 3, // President's day
			Calendar.SEPTEMBER, Calendar.MONDAY, 1, // Labor day
			Calendar.NOVEMBER, Calendar.THURSDAY, 4, // Thanksgiving
		 };

		// Columbus Day is a federal holiday.
		// it is the second Monday in October
		int mm = now.get(Calendar.MONTH);
		int dd = now.get(Calendar.DAY_OF_MONTH);
		int dw = now.get(Calendar.DAY_OF_WEEK);
		int wm = now.get(Calendar.WEEK_OF_MONTH);

		// go over the month/day-of-month entries, return true on full match
		for (int i = 0; i < month_date.length; i += 2) {
			if ((mm == month_date[i + 0]) && (dd == month_date[i + 1])) 
				return true;
		}

		// go over month/weekday/week-of-month entries, return true on full match
		for (int i = 0; i < month_weekday_monthweek.length; i += 3) {
			if ((mm == month_weekday_monthweek[i + 0]) && 
				(dw == month_weekday_monthweek[i + 1]) && 
				(wm == month_weekday_monthweek[i + 2])) 
					return true;
		}

		if ((mm == Calendar.MAY) && (dw == Calendar.MONDAY) && (wm == now.getMaximum(Calendar.WEEK_OF_MONTH))) /* last week in May */
			return true;

		return false;
	}

}

/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.basic;

import java.util.*;

public class LinearIntervalMap implements IntervalMap{
	private Map keyValues = new HashMap(); 

	/* (non-Javadoc)
	 * @see com.domainlanguage.basic.IntervalMap#put(com.domainlanguage.basic.ComparableInterval, java.lang.Object)
	 */
	public void put(Interval keyInterval, Object value) {
		if (containsIntersectingKey(keyInterval)) throw new RuntimeException("IntervalMap keys can't intersect.");
		keyValues.put(keyInterval, value);
	}

	/* (non-Javadoc)
	 * @see com.domainlanguage.basic.IntervalMap#get(java.lang.Object)
	 */
	public Object get(Comparable key) {
		Interval keyInterval = findKeyIntervalContaining(key);
//		if (keyInterval == null) return null;
		return keyValues.get(keyInterval);
	}
	
	public boolean includesKey(Comparable key) {
		return findKeyIntervalContaining(key) != null;
	}

	private Interval findKeyIntervalContaining(Comparable key) {
		if (key == null) return null;
		Iterator it = keyValues.keySet().iterator();
		while (it.hasNext()) {
			Interval interval = (Interval)it.next();
			if (interval.includes(key)) return interval;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.domainlanguage.basic.IntervalMap#containsIntersectingKey(com.domainlanguage.basic.ComparableInterval)
	 */
	public boolean containsIntersectingKey(Interval otherInterval) {
		Iterator it = keyValues.keySet().iterator();
		while (it.hasNext()) {
			Interval keyInterval = (Interval)it.next();
			if (keyInterval.intersects(otherInterval)) return true;
		}
		return false;
	}
}

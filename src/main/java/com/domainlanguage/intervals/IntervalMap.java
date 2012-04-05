/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

public interface IntervalMap<K extends Comparable<K>, V> {

  void put(Interval<K> keyInterval, V value);

  V get(K key);

  void remove(Interval<K> keyInterval);

  boolean containsKey(K key);

  boolean containsIntersectingKey(Interval<K> interval);
}

/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.intervals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LinearIntervalMap<K extends Comparable<K>, V> implements IntervalMap<K, V> {

  private final Map<Interval<K>, V> keyValues;

  public LinearIntervalMap() {
    keyValues = new HashMap<Interval<K>, V>();
  }

  @Override
  public void put(Interval<K> keyInterval, V value) {
    remove(keyInterval);
    keyValues.put(keyInterval, value);
  }

  @Override
  public void remove(Interval<K> keyInterval) {
    List<Interval<K>> intervalSequence = intersectingKeys(keyInterval);
    for (Iterator<Interval<K>> iter = intervalSequence.iterator(); iter.hasNext();) {
      Interval<K> oldInterval = iter.next();
      V oldValue = keyValues.get(oldInterval);
      keyValues.remove(oldInterval);
      List<Interval<K>> complementIntervalSequence = keyInterval.complementRelativeTo(oldInterval);
      directPut(complementIntervalSequence, oldValue);
    }
  }

  @Override
  public V get(K key) {
    Interval<K> keyInterval = findKeyIntervalContaining(key);
    //		if (keyInterval == null) return null;
    return keyValues.get(keyInterval);
  }

  @Override
  public boolean containsKey(K key) {
    return findKeyIntervalContaining(key) != null;
  }

  @Override
  public boolean containsIntersectingKey(Interval<K> otherInterval) {
    return !intersectingKeys(otherInterval).isEmpty();
  }

  private void directPut(List<Interval<K>> intervalSequence, V value) {
    for (Iterator<Interval<K>> iter = intervalSequence.iterator(); iter.hasNext();) {
      keyValues.put(iter.next(), value);
    }
  }

  private Interval<K> findKeyIntervalContaining(K key) {
    if (key == null) {
      return null;
    }
    Iterator<Interval<K>> it = keyValues.keySet().iterator();
    while (it.hasNext()) {
      Interval<K> interval = it.next();
      if (interval.includes(key)) {
        return interval;
      }
    }
    return null;
  }

  private List<Interval<K>> intersectingKeys(Interval<K> otherInterval) {
    List<Interval<K>> intervalSequence = new ArrayList<Interval<K>>();
    Iterator<Interval<K>> it = keyValues.keySet().iterator();
    while (it.hasNext()) {
      Interval<K> keyInterval = it.next();
      if (keyInterval.intersects(otherInterval)) {
        intervalSequence.add(keyInterval);
      }
    }
    return intervalSequence;
  }

}

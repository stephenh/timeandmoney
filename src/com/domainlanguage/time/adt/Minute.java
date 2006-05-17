/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.time.adt;

public class Minute {
    int value;
    
    public static Minute value(int initial) {
        return new Minute(initial);
    }
    private Minute(int initial) {
        if (initial < 0 || initial > 59)
            throw new IllegalArgumentException("Illegal value for minute: " + initial + ", please use a value between 0 and 59");
        value = initial;
    }
    
    public boolean equals(Object another) {
        if (!(another instanceof Minute))
            return false;
        return equals((Minute)another);
    }
    public boolean equals(Minute another) {
        return value == another.value;
    }
    public int hashCode() {
        return value;
    }

    public boolean after(Minute another) {
        return value > another.value;
    }

    public boolean before(Minute another) {
        return value < another.value;
    }

    public int value() {
        return value;
    }
    public String toString() {
        return String.valueOf(value);
    }
    private static Class getPrimitivePersistenceMappingType() {
        return Integer.TYPE;
    }
}

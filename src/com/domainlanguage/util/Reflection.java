/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.util;


public abstract class Reflection {

    static public boolean is(Object instance, Class type) {
        return 
        	(instance != null) && 
        	type.isAssignableFrom(instance.getClass());
    }

	static public boolean is(Class subtype, Class type) {
		return type.isAssignableFrom(subtype);
	}

}

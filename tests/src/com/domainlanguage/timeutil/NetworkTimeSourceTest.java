/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import junit.framework.*;

public class NetworkTimeSourceTest extends TestCase {

	//TODO: I don't know how to test this, except to see if it
	//runs to completion. Maybe someone else will have an idea.
	public void testNowNIST() throws Exception{
		NetworkTimeSource.nowNIST();
		assertTrue(true);
	}
	
}

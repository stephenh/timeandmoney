/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.tests;

import java.util.*;

import junit.framework.*;

public class AllTests extends TestSuite {
	
    static public TestSuite suite() {
        TestSuite suite = new TestSuite();
		String[] excludedPackages = new String[] {
//			"???.", //note: add a package name to exclude it
		};
		TestSuite[] excludedSuites = new TestSuite[] {
//			???.suite(),  //note: add a suite name to exclude it  
		};
		FilteredTestCaseCollector collector = new FilteredTestCaseCollector();
		collector.excludedPackages(excludedPackages);
		collector.excludedSuites(excludedSuites);	
		Enumeration enumeration = collector.collectTests();
		while (enumeration.hasMoreElements())
			try {
				String each = (String) enumeration.nextElement();
				suite.addTestSuite(Class.forName(each));
			} catch (ClassNotFoundException ignore) {
			}
		return suite;    
    }
    
}
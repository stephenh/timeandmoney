/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.tests;

import java.lang.reflect.*;
import java.util.*;

import junit.framework.*;
import junit.runner.*;

import com.domainlanguage.util.*;

/**
 * I loads all TestCase subclasses on the class path, excluding specified filters
 */
public class FilteredTestCaseCollector extends ClassPathTestCollector {
	private TestCaseClassLoader loader = new TestCaseClassLoader();
	private String[] excludedPackages = new String[0];
	private Set excludedClasses = new HashSet();
	private Class[] excludedTypes = new Class[0];

	public final void excludedPackages(String[] excludedPackages) {
		this.excludedPackages = excludedPackages;
	}
	
	public final void excludedSuites(TestSuite[] excludedSuites) {
		Collection testCases = Tests.allTestCaseNames(excludedSuites);
		excludedClasses.addAll(testCases);
	}
	
	public final void excludedClasses(Class[] excludedClasses) {
		for (int i = 0; i < excludedClasses.length; i++) 
			this.excludedClasses.add(excludedClasses[i].getName());
	}
	
	public final void excludedTypes(Class[] excludedTypes) {
		this.excludedTypes = excludedTypes;
		reloadExcludedTypes();		
	}
	
	/**
	 *  due to class loaders discrepancy, we need to "normalize" classes 
	 * through one class loader. we choose our loader.
	 */
	private void reloadExcludedTypes() {
		try {
			for (int i = 0; i < excludedTypes.length; i++)
				excludedTypes[i] = loader.loadClass(excludedTypes[i].getName(), false);
		} catch (ClassNotFoundException ignore) {
		}
	}

	protected boolean isTestClass(String filename) {		
		if (!filename.endsWith(".class")) 
			return false;
		
		String className = classNameFromFile(filename);
		if (loader.isExcluded(className) || isExcluded(className)) 
			return false;
		
		try {
			Class testClass = loader.loadClass(className, false);
			return isTestClass(testClass) && !isExcludedByType(testClass);
		} catch (ClassNotFoundException ignore) {
		} catch (NoClassDefFoundError ignore) {
		}
		return false;
	}

	private boolean isExcluded(String className) {
		for (int i = 0; i < excludedPackages.length; i++) 
			if (className.startsWith(excludedPackages[i])) 
				return true;
		return excludedClasses.contains(className);
	}

	private boolean isExcludedByType(Class testClass) {
		for (int i = 0; i < excludedTypes.length; i++) 
			if (Reflection.is(testClass, excludedTypes[i])) 
				return true;
		return false;
	}

	private boolean isTestClass(Class testClass) {
		return 
			!Modifier.isAbstract(testClass.getModifiers()) &&
			Modifier.isPublic(testClass.getModifiers()) && 
			Reflection.is(testClass, TestCase.class);
	}

}
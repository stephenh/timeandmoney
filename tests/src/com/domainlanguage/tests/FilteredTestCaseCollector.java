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
 * I loads all TestCase subclasses on the class path, excluding specified
 * filters
 */
public class FilteredTestCaseCollector extends ClassPathTestCollector {
    private TestCaseClassLoader loader = new TestCaseClassLoader();
    private String[] exludedPackages = new String[0];
    private Set exludedClasses = new HashSet();
    private Class[] exludedTypes = new Class[0];

    public final void exludedPackages(String[] exludedPackages) {
        this.exludedPackages = exludedPackages;
    }

    public final void exludedSuites(TestSuite[] exludedSuites) {
        Collection testCases = Tests.allTestCaseNames(exludedSuites);
        exludedClasses.addAll(testCases);
    }

    public final void exludedClasses(Class[] exludedClasses) {
        for (int i = 0; i < exludedClasses.length; i++)
            this.exludedClasses.add(exludedClasses[i].getName());
    }

    public final void exludedTypes(Class[] exludedTypes) {
        this.exludedTypes = exludedTypes;
        reloadExcludedTypes();
    }

    /**
     * due to class loaders discepency, we need to "normalize" classes through
     * one class loader. we choose our loader.
     */
    private void reloadExcludedTypes() {
        try {
            for (int i = 0; i < exludedTypes.length; i++)
                exludedTypes[i] = loader.loadClass(exludedTypes[i].getName(), false);
        } catch (ClassNotFoundException ignore) {
        }
    }

    protected boolean isTestClass(String filename) {
        if (!filename.endsWith(".class"))
            return false;

        String className = classNameFromFile(filename);
        if (loader.isExcluded(className) || isExluded(className))
            return false;

        try {
            Class testClass = loader.loadClass(className, false);
            return isTestClass(testClass) && !isExludedByType(testClass);
        } catch (ClassNotFoundException ignore) {
        } catch (NoClassDefFoundError ignore) {
        }
        return false;
    }

    private boolean isExluded(String className) {
        for (int i = 0; i < exludedPackages.length; i++)
            if (className.startsWith(exludedPackages[i]))
                return true;
        return exludedClasses.contains(className);
    }

    private boolean isExludedByType(Class testClass) {
        for (int i = 0; i < exludedTypes.length; i++)
            if (TypeCheck.is(testClass, exludedTypes[i]))
                return true;
        return false;
    }

    private boolean isTestClass(Class testClass) {
        return !Modifier.isAbstract(testClass.getModifiers()) && Modifier.isPublic(testClass.getModifiers()) && TypeCheck.is(testClass, TestCase.class);
    }

}
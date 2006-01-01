/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.util;

import java.lang.reflect.*;
import java.math.*;
import java.util.*;

import com.domainlanguage.base.*;
import com.domainlanguage.intervals.*;
import com.domainlanguage.money.*;
import com.domainlanguage.time.*;

public class PersistentMappingVerification {
    private static final String FOR_PERSISTENT_MAPPING = "ForPersistentMapping";
    private static final String LINE_SEPARATOR = System
            .getProperty("line.separator");
    private static final Map TEST_TYPE_MAPPING;
    static {
        TEST_TYPE_MAPPING = new HashMap();
        TEST_TYPE_MAPPING
                .put(BigDecimal.class.getName(), BigDecimal.valueOf(1));
        TEST_TYPE_MAPPING.put(Boolean.TYPE.getName(), Boolean.TRUE);
        TEST_TYPE_MAPPING.put(CalendarDate.class.getName(), CalendarDate.date(
                2005, 12, 29));
        TEST_TYPE_MAPPING.put(Comparable.class.getName(), new Integer(2));
        TEST_TYPE_MAPPING.put(Currency.class.getName(), Currency
                .getInstance("EUR"));
        TEST_TYPE_MAPPING.put(Duration.class.getName(), Duration.days(11));
        TEST_TYPE_MAPPING.put(Integer.TYPE.getName(), new Integer(3));
        TEST_TYPE_MAPPING.put(Long.TYPE.getName(), new Long(4));
        TEST_TYPE_MAPPING.put(List.class.getName(), new ArrayList());
        TEST_TYPE_MAPPING.put(Map.class.getName(), new HashMap());
        TEST_TYPE_MAPPING.put(Set.class.getName(), new HashSet());
        TEST_TYPE_MAPPING.put(String.class.getName(), "sample value");
        TEST_TYPE_MAPPING.put(TimeRate.class.getName(), new TimeRate(BigDecimal
                .valueOf(5), Duration.days(6)));
        TEST_TYPE_MAPPING.put(TimeUnit.class.getName(), TimeUnit
                .exampleForPersistentMappingTesting());
        TEST_TYPE_MAPPING.put(TimeUnit.class.getName() + "$Type", TimeUnit
                .exampleTypeForPersistentMappingTesting());
    }
    private Class toVerify;
    private Object instance;
    private List problems;

    public static PersistentMappingVerification on(Class klass)
            throws ClassNotFoundException {
        return new PersistentMappingVerification(klass);
    }

    public boolean isPersistableRequirementsSatisfied() {
        return problems.isEmpty();
    }

    public String formatFailure() {
        StringBuffer buffer = new StringBuffer();
        boolean first = true;
        String nextProblem;
        for (Iterator problemsIterator = problems.iterator(); problemsIterator
                .hasNext();) {
            nextProblem = (String) problemsIterator.next();
            if (!first) {
                buffer.append(LINE_SEPARATOR);
            }
            first = false;
            buffer.append(nextProblem);
        }
        return buffer.toString();
    }

    private PersistentMappingVerification(Class klass) {
        initialize(klass);
    }

    private void initialize(Class klass) {
        this.toVerify = klass;
        this.problems = new ArrayList();
        try {
            checkEverything();
        } catch (RuntimeException ex) {
            addToProblems(ex.toString());
        }
    }

    private void checkEverything() {
        checkClass(toVerify);
        Class current = toVerify;
        while (current != null && current != Object.class) {
            checkFields(current);
            current = current.getSuperclass();
        }
    }

    private void checkFields(Class klass) {
        Field[] fields = klass.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            Field each = fields[index];
            if ((each.getModifiers() & Modifier.STATIC) > 0) {
                continue;
            }
            checkField(each);
        }
    }

    private void checkClass(Class klass) {
        if (klass.isInterface()) {
            return;
        }
        if (isAbstract(klass)) {
            return;
        }
        if(isFinal(klass)) {
            addToProblems(klass.toString() + " must not be final");
        }
        Constructor constructor = null;
        try {
            constructor = klass.getDeclaredConstructor(null);
            constructor.setAccessible(true);
            instance = constructor.newInstance(null);
        } catch (NoSuchMethodException ex) {
            addToProblems(klass.toString() + " has no default constructor");
        } catch (IllegalArgumentException ex) {
            addToProblems(constructor.toString()
                    + " had an illegal argument exception");
        } catch (InstantiationException ex) {
            addToProblems(constructor.toString()
                    + " had an instantion exception");
        } catch (IllegalAccessException ex) {
            addToProblems(constructor.toString()
                    + " had an illegal access exception");
        } catch (InvocationTargetException ex) {
            addToProblems(constructor.toString()
                    + " had an invocation exception");
        }
    }
    private boolean isAbstract(Class klass) {
        return (klass.getModifiers() & Modifier.ABSTRACT) > 0;
    }
    private boolean isFinal(Class klass) {
        return (klass.getModifiers() & Modifier.FINAL) > 0;
    }
    private void checkField(Field theField) {
        String name = capitalize(theField.getName());
        Method setter = null;
        Class type = theField.getType();
        Object toTest = getTestValueFor(type);
        Object actual = null;

        try {
            setter = getSetter(theField, "set" + name + FOR_PERSISTENT_MAPPING);
            if (!isMethodPrivate(setter)) {
                addToProblems(setter.toString() + " not declared private");
            }
            if (instance != null) {
                setter.setAccessible(true);
                setter.invoke(instance, new Object[] { toTest });
            }
        } catch (NoSuchMethodException ex) {
            addToProblems(theField.toString() + " setter does not exist");
        } catch (IllegalArgumentException ex) {
            addToProblems(setter.toString()
                    + " had an illegal argument exception");
        } catch (IllegalAccessException ex) {
            addToProblems(setter.toString()
                    + " had an illegal access exception");
        } catch (InvocationTargetException ex) {
            addToProblems(setter.toString()
                    + " had an invocation target exception");
        }

        Method getter = null;
        try {
            getter = getGetter(theField, name, "get" + name
                    + FOR_PERSISTENT_MAPPING);
            if (!isMethodPrivate(getter)) {
                addToProblems(getter.toString() + " not declared private");
            }
            if (instance != null) {
                getter.setAccessible(true);
                actual = getter.invoke(instance, null);
            }
        } catch (NoSuchMethodException ex) {
            addToProblems(theField.toString() + " getter does not exist");
        } catch (IllegalArgumentException ex) {
            addToProblems(getter.toString()
                    + " had an illegal argument exception");
        } catch (IllegalAccessException ex) {
            addToProblems(getter.toString()
                    + " had an illegal access exception");
        } catch (InvocationTargetException ex) {
            addToProblems(getter.toString()
                    + " had an invocation target exception");
        }
        if (!TypeCheck.sameClassOrBothNull(toTest, actual)) {
            addToProblems(theField.toString()
                    + " getter/setter result do not match, expected [" + toTest
                    + "], but got [" + actual + "]");
            return;
        }
    }

    private Object getTestValueFor(Class type) {
        Object result = TEST_TYPE_MAPPING.get(type.getName());
        if (result == null) {
            addToProblems("Add sample value for " + type.toString()
                    + " to TEST_TYPE_MAPPING");
        }
        return result;
    }

    private boolean isMethodPrivate(Method toCheck) {
        return (toCheck.getModifiers() & Modifier.PRIVATE) > 0;
    }

    private void addToProblems(String reason) {
        problems.add(reason);
    }

    private Method getSetter(Field theField, String setter)
            throws NoSuchMethodException {
        Class[] setterTypes = new Class[1];
        setterTypes[0] = theField.getType();
        return theField.getDeclaringClass().getDeclaredMethod(setter,
                setterTypes);
    }

    private Method getGetter(Field theField, String name, String getter)
            throws NoSuchMethodException {
        try {
            return theField.getDeclaringClass().getDeclaredMethod(getter, null);
        } catch (NoSuchMethodException unknownGetter) {
            return theField.getDeclaringClass().getDeclaredMethod(
                    "is" + name + FOR_PERSISTENT_MAPPING, null);
        }
    }

    private String capitalize(String string) {
        char chars[] = string.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}

/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.util;

import java.lang.reflect.*;
import java.util.*;

public class PersistentMappingVerification {
    private static final String FOR_PERSISTENT_MAPPING = "ForPersistentMapping";
    private static final String LINE_SEPARATOR=System.getProperty("line.separator");
    private Class klass;
    private List problems;

    public static PersistentMappingVerification on(Class klass) throws ClassNotFoundException {
        return new PersistentMappingVerification(klass);
    }
    
    public boolean hasAllPersistentMappingsForAllFields() {
        return problems.isEmpty();
    }

    public String formatFailure() {
        StringBuffer buffer = new StringBuffer();
        boolean first=true;
        String nextProblem;
        for (Iterator problemsIterator=problems.iterator(); problemsIterator.hasNext();) {
            nextProblem=(String)problemsIterator.next();
            if (!first) {
                buffer.append(LINE_SEPARATOR);
            }
            first=false;
            buffer.append(nextProblem);
        }
        return buffer.toString();
    }
    
    private PersistentMappingVerification(Class klass) {
        initialize(klass);
    }

    private void initialize(Class klass) {
        this.klass = klass;
        this.problems = new ArrayList();
        checkEverything();
    }

    private void checkEverything() {
        checkClass();
        checkFields();
    }

    private void checkFields() {
        Field[] fields = this.klass.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            Field each = fields[index];
            if ((each.getModifiers() & Modifier.STATIC) > 0) {
                continue;
            }
            checkField(each);
        }
    }

    private void checkClass() {
        try {
            if (this.klass.isInterface()) {
                return;
            }
            this.klass.getDeclaredConstructor(null);
        } catch (NoSuchMethodException ex) {
            addToProblems(this.klass.toString() + " has no default constructor");
        }
    }

    private void checkField(Field theField) {
        String name = capitalize(theField.getName());
        
        Method getter = null;
        try {
            getter=getGetter(name, "get" + name + FOR_PERSISTENT_MAPPING);
            if (!isMethodPrivate(getter)) { 
                addToProblems(getter.toString() + " not declared private");
            }
        } catch (NoSuchMethodException ex) {
            addToProblems(theField.toString() + " getter does not exist");
        }
        
        Method setter = null;
        try {
            setter=getSetter(theField, "set" + name + FOR_PERSISTENT_MAPPING);
            if (!isMethodPrivate(setter)) {
                addToProblems(setter.toString() + " not declared private");
            }
        } catch (NoSuchMethodException ex) {
            addToProblems(theField.toString() + " setter does not exist");
        }
        
        //check for getter/setter working properly
    }
    
    private boolean isMethodPrivate(Method toCheck) {
        return (toCheck.getModifiers() & Modifier.PRIVATE) > 0;
    }
    private void addToProblems(String reason) {
        problems.add(reason);
    }

    private Method getSetter(Field theField, String setter) throws NoSuchMethodException {
        Class[] setterTypes = new Class[1];
        setterTypes[0] = theField.getType();
        return klass.getDeclaredMethod(setter, setterTypes);
    }

    private Method getGetter(String name, String getter) throws NoSuchMethodException {
        try {
            return klass.getDeclaredMethod(getter, null);
        } catch (NoSuchMethodException unknownGetter) {
            return klass.getDeclaredMethod("is" + name + FOR_PERSISTENT_MAPPING,
                    null);
        }
    }

    private String capitalize(String string) {
        char chars[] = string.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }
}

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
    private Class klass;
    private List unmappedFieldNames;

    public static PersistentMappingVerification on(Class klass) throws ClassNotFoundException {
        return new PersistentMappingVerification(klass);
    }

    private PersistentMappingVerification(Class klass) {
        initialize(klass);
    }

    private void initialize(Class klass) {
        this.klass = klass;
        this.unmappedFieldNames = new ArrayList();
        check();
    }

    private void check() {
        Field[] fields = this.klass.getDeclaredFields();
        for (int index = 0; index < fields.length; index++) {
            Field each = fields[index];
            if ((each.getModifiers() & Modifier.STATIC) > 0) {
                continue;
            }
            check(each);
        }
    }

    private void check(Field theField) {
        String name = capitalize(theField.getName());
        String getter = "get" + name + FOR_PERSISTENT_MAPPING;
        String setter = "set" + name + FOR_PERSISTENT_MAPPING;

        try {
            checkGetter(name, getter);
            checkSetter(theField, setter);
        } catch (NoSuchMethodException ex) {
            unmappedFieldNames.add(theField.getName());
        }
    }

    private void checkSetter(Field theField, String setter) throws NoSuchMethodException {
        Class[] setterTypes = new Class[1];
        setterTypes[0] = theField.getType();
        klass.getDeclaredMethod(setter, setterTypes);
    }

    private void checkGetter(String name, String getter) throws NoSuchMethodException {
        try {
            klass.getDeclaredMethod(getter, null);
        } catch (NoSuchMethodException unknownGetter) {
            klass.getDeclaredMethod("is" + name + FOR_PERSISTENT_MAPPING,
                    null);
        }
    }

    private String capitalize(String string) {
        char chars[] = string.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
    }

    private String[] getFieldNamesWithoutPersistentMappings() {
        String[] result = new String[unmappedFieldNames.size()];
        unmappedFieldNames.toArray(result);
        return result;
    }

    public boolean hasAllPersistentMappingsForAllFields() {
        return unmappedFieldNames.isEmpty();
    }

    public String formatFailure() {
        String[] fieldNames = getFieldNamesWithoutPersistentMappings();
        StringBuffer buffer = new StringBuffer();
        buffer.append(klass.toString());
        buffer.append(" needs persistent mappings for: ");
        for (int fieldIndex = 0; fieldIndex < fieldNames.length; fieldIndex++) {
            if (0 != fieldIndex) {
                buffer.append(", ");
            }
            buffer.append(fieldNames[fieldIndex]);
        }
        return buffer.toString();
    }

}

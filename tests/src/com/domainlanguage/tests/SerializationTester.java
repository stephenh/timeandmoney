/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.tests;

import java.io.*;

import junit.framework.*;

import com.domainlanguage.util.*;

public class SerializationTester {
    private static final String SERIAL_FILENAME = "test.ser";

    public static void assertCanBeSerialized(Object serializble) throws AssertionFailedError {
        if (!TypeCheck.is(serializble, Serializable.class))
            throw new AssertionFailedError("Object doesn't implement java.io.Serializable interface");

        ObjectOutputStream out = null;
        ObjectInputStream in = null;
        File serialFile = null;
        try {
            serialFile = new File(SERIAL_FILENAME);
            out = new ObjectOutputStream(new FileOutputStream(serialFile));
            out.writeObject(serializble);
            out.flush();
            in = new ObjectInputStream(new FileInputStream(serialFile));
            Object deserialized = in.readObject();
            if (!serializble.equals(deserialized))
                throw new AssertionFailedError("Reconstituted object is expected to be equal to serialized");
        } catch (AssertionFailedError e) {
            throw e;
        } catch (Exception e) {
            Assert.fail("Exception while serializing: " + e);
        } finally {
            try {
                out.close();
                in.close();
                serialFile.delete();
            } catch (IOException ignore) {
            }
        }
    }

}
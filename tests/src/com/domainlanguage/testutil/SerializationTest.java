/**
 * Created on Nov 5, 2004
 */
package com.domainlanguage.testutil;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.Serializable;

import junit.framework.AssertionFailedError;



/**
 * @author Vladimir Gitlevich
 */
public class SerializationTest {
    
    public static void assertSerializationWorks(Object toSerialize) throws AssertionFailedError {
        if( !(toSerialize instanceof Serializable) ) {
            throw new AssertionFailedError("Object doesn't implement java.io.Serializable interface");
        }
        
        try {
            PipedOutputStream pipedOut = new PipedOutputStream();
            PipedInputStream pipedIn = new PipedInputStream(pipedOut);
            
            ObjectOutputStream out = new ObjectOutputStream(pipedOut);
            out.writeObject(toSerialize);
            
            ObjectInputStream in = new ObjectInputStream(pipedIn);
            Object deserialized = in.readObject();
            
            if( !(toSerialize.equals(deserialized))) {
                throw new AssertionFailedError("Reconstituted object is expected to be equal to serialized");
            }
        }
        catch (AssertionFailedError e) {
        	throw e;
        }
        catch (Exception e) {
            throw new AssertionFailedError("Exception while serializing: " + e);
        }
    }

}

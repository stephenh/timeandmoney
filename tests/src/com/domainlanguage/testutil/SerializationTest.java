/**
 * Created on Nov 5, 2004
 */
package com.domainlanguage.testutil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import junit.framework.AssertionFailedError;



/**
 * @author Vladimir Gitlevich
 */
public class SerializationTest {
    
    
private static final String TEST_FILE = "test.txt";
    public static void assertSerializationWorks(Object toSerialize) throws AssertionFailedError {
        if( !(toSerialize instanceof Serializable) ) {
            throw new AssertionFailedError("Object doesn't implement java.io.Serializable interface");
        }
        
        FileOutputStream fout = null;
        FileInputStream fin = null;
        File serFile = null;
        try {
            serFile = new File(TEST_FILE);
            fout = new FileOutputStream(serFile);
            fin = new FileInputStream(serFile);
            
            ObjectOutputStream out = new ObjectOutputStream(fout);
            out.writeObject(toSerialize);
            out.flush();
            
            ObjectInputStream in = new ObjectInputStream(fin);
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
        finally {
            try {
                if( fout != null ) {
                    fout.close();
                }
                if( fin != null ) {
                    fin.close();
                }
                serFile.delete();
            }
            catch (IOException e1) {
                // ignore
            }
        }
    }

}

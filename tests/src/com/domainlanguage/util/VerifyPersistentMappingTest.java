/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.util;

import junit.framework.*;

public class VerifyPersistentMappingTest extends TestCase {
    
    public void test() throws Exception {
        ClassGenerator generator=new ClassGenerator(new TimeAndMoneyDomainClassFilter()) {
            protected void next(Class klass) throws Exception {
                PersistentMappingVerification verification;
                verification = PersistentMappingVerification.on(klass);
                if (!verification.hasAllPersistentMappingsForAllFields()) {
                    fail(formatFailure(verification));
                }
            }
        };
        //Comment this in to execute test
        //It's commented out until we get all classes mapped
        //generator.go();
    }

    String formatFailure(PersistentMappingVerification verification) {
        return verification.formatFailure();
    }
}

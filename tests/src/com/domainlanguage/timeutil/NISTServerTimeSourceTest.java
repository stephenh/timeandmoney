/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import junit.framework.TestCase;

import com.domainlanguage.time.TimePoint;
import com.domainlanguage.time.TimeSource;

public class NISTServerTimeSourceTest extends TestCase {
    private String previousServerName;
    private String previousServerPort;
    private NISTServerStandIn timeServer;

    public void testNISTTimeSource() throws Exception {
        TimePoint now = null;
        TimePoint expected = TimePoint.from(1124679473000l);
        now = NISTServer.now();
        assertEquals(expected, now);

        // The following calls allow polymorphic substitution of TimeSources
        // either in applications or, more often, in testing.
        TimeSource source = NISTServer.timeSource();
        TimePoint nowAgain = source.now();
        assertEquals(expected, nowAgain);

    }

    public void setUp() throws Exception {
        super.setUp();

        timeServer = new NISTServerStandIn();
        timeServer.start();

        previousServerName = System
                .getProperty(NISTServer.NIST_TIMESERVER_HOST_PROPERTY_NAME);
        previousServerPort = System
                .getProperty(NISTServer.NIST_TIMESERVER_PORT_PROPERTY_NAME);

        System.setProperty(NISTServer.NIST_TIMESERVER_HOST_PROPERTY_NAME,
                timeServer.getHostName());
        System.setProperty(NISTServer.NIST_TIMESERVER_PORT_PROPERTY_NAME,
                timeServer.getPort());
    }

    public void tearDown() throws Exception {
        super.tearDown();
        timeServer.stop();
        if (previousServerName == null) {
            System.clearProperty(NISTServer.NIST_TIMESERVER_HOST_PROPERTY_NAME);
        } else {
            System.setProperty(NISTServer.NIST_TIMESERVER_HOST_PROPERTY_NAME,
                    previousServerName);
        }
        if (previousServerPort == null) {
            System.clearProperty(NISTServer.NIST_TIMESERVER_PORT_PROPERTY_NAME);
        } else {
            System.setProperty(NISTServer.NIST_TIMESERVER_PORT_PROPERTY_NAME,
                    previousServerPort);
        }
    }

}
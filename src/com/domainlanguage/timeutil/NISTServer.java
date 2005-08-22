/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.io.*;
import java.net.*;

import com.domainlanguage.time.*;

/**
 * National Institute of Standards and Technology provides an Internet time
 * server.
 */

public class NISTServer {
	public static String NIST_TIMESERVER_HOST_PROPERTY_NAME="NIST.TIMESERVER.SERVER";
	public static String NIST_TIMESERVER_PORT_PROPERTY_NAME="NIST.TIMESERVER.PORT";
	
	static String SERVER_DEFAULT="time.nist.gov";
	static String PORT_DEFAULT="13";
	
	public static TimeSource timeSource() {
		return new TimeSource() {
			public TimePoint now() {
				try {
					return NISTServer.now();
				} catch (IOException e) {
					throw new RuntimeException(
							"Problem obtaining network time: " + e.getMessage());
				}
			}
		};
	}

	public static TimePoint now() throws IOException {
		String timeServer = System.getProperty(NIST_TIMESERVER_HOST_PROPERTY_NAME, SERVER_DEFAULT);
		String timePort = System.getProperty(NIST_TIMESERVER_PORT_PROPERTY_NAME, PORT_DEFAULT);
		byte buffer[] = new byte[256];
		Socket socket = new Socket(timeServer, Integer.parseInt(timePort));
		try {
			int length = socket.getInputStream().read(buffer);
			String nistTime = new String(buffer, 0, length);
			String nistGist = nistTime.substring(7, 24);
			String pattern = "y-M-d HH:mm:ss";
			return TimePoint.parseGMTFrom(nistGist, pattern);
		} finally {
			socket.close();
		}
	}
}
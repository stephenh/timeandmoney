/**
 * Copyright (c) 2004 Domain Language, Inc. (http://domainlanguage.com)
 * This free software is distributed under the "MIT" licence. See file licence.txt. 
 * For more information, see http://timeandmoney.sourceforge.net.
 */

package com.domainlanguage.timeutil;

import java.io.IOException;
import java.net.Socket;

import com.domainlanguage.time.TimePoint;


public class NetworkTimeSource {
	public static TimePoint nowNIST() throws IOException {
		String timeServer = System.getProperty("NIST.TIMESERVER", "time.nist.gov");
			byte buffer[] = new byte[256];
			Socket socket = new Socket(timeServer, 13);
			int length = socket.getInputStream().read(buffer);
			String nistTime = new String(buffer, 0, length);
			String nistGist = nistTime.substring(7, 24);
			String pattern = "y-M-d HH:mm:ss";
			return TimePoint.from(nistGist, pattern);
	}
}

/*
 * Created on Apr 23, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package timelanguage;

import junit.framework.TestCase;

/**
 * @author Eric Evans
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class NetworkTimeSourceTest extends TestCase {
	public void testNowNIST() throws Exception{
		NetworkTimeSource.nowNIST();
		assertTrue(true);
	}
	
}

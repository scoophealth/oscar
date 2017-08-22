/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
package oscar.oscarLab.ca.all.upload.handlers;

// import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.log4j.Logger;
// import org.junit.Test;

/**
 * This test case is used to generate a log analysis during development only.
 * No assertions are used.
 * Uncomment the Test annotations to use.
 *
 */
public class IHAHandlerTest extends IHAHandler {

	private static Logger logger = Logger.getLogger( IHAHandlerTest.class );
	private static String TEST_FILE = "Path to test file";

	// @Test
	public void testParse() {
		logger.info("#------------>>  Testing IHAHHandler Uploader.");
		URL url = Thread.currentThread().getContextClassLoader().getResource(TEST_FILE);
		InputStream is = null;
		
		try {
	    	is = url.openStream();
	    	// parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr);
	    	String response = parse(null, "IHA", url.getPath(), 1, "127.0.0.1");
	        logger.info("RESPONSE: " + response);
		}catch (IOException e) {
        	 logger.error("Test Failed ", e);
        }finally {
        	try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
	}

}

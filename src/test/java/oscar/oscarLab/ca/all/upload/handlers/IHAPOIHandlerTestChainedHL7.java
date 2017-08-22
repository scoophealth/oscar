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


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;

import org.xml.sax.SAXException;

/**
 * This test case is used to generate a log analysis during development only.
 * No assertions are used.
 * Uncomment the Test annotations to use.
 *
 */
public class IHAPOIHandlerTestChainedHL7 extends IHAPOIHandler {
	
	private static Logger logger = Logger.getLogger( IHAPOIHandlerTestChainedHL7.class );
	private static String TEST_FILE = "path to test file";

	// @Test
	public void testParse() {
		logger.info("#------------>>  Testing IHAPOIHHandler Uploader.");
		URL url = Thread.currentThread().getContextClassLoader().getResource(TEST_FILE);
		InputStream is = null;
		
		try {
	    	is = url.openStream();
	    	Map<String, String> messageList = parse(is);
	    	Iterator<String> keySetIterator = messageList.keySet().iterator();
	    	while( keySetIterator.hasNext() ) {
	    		String messageId = keySetIterator.next();
	        	logger.info( "MESSAGE ID " + messageId + " ------->> " + messageList.get( messageId ) );
	        }
        } catch (ParserConfigurationException e) {
        	 logger.error("Test Failed ", e);
        } catch (SAXException e) {
        	 logger.error("Test Failed ", e);
        } catch (IOException e) {
        	 logger.error("Test Failed ", e);
        }finally {
        	try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	
	// @Test
	public void testParseDirect() {
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

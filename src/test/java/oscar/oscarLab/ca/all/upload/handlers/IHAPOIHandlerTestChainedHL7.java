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
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
// import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

public class IHAPOIHandlerTestChainedHL7 extends IHAPOIHandler {
	
	private static Logger logger = Logger.getLogger( IHAPOIHandlerTestChainedHL7.class );

	@Test
	public void testParse() {
		logger.info("#------------>>  Testing MEDITECHHandler Uploader.");
		URL url = Thread.currentThread().getContextClassLoader().getResource("iha-poi.20140905104455.xml");
		InputStream is = null;
		
		try {
	    	is = url.openStream();
	        List<String> messageList = parse(is);
	        for(String message : messageList ) {
	        	logger.info(message);
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

}

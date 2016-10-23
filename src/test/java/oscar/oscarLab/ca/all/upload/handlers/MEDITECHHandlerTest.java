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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.xml.sax.SAXException;

@RunWith(Parameterized.class)
public class MEDITECHHandlerTest extends MEDITECHHandler {

	private static Logger logger = Logger.getLogger(MEDITECHHandlerTest.class);
	private static String hl7Body;
	private static ZipFile zipFile;
	private static int TEST_COUNT = 0;
	
	@Parameterized.Parameters
	public static Collection<String[]> hl7BodyArray() {
		
		logger.info( "Creating MEDITECHHandlerTest test parameters" );
	
		URL url = Thread.currentThread().getContextClassLoader().getResource("MEDITECH_test_data.zip");
		
		try {
			zipFile = new ZipFile(url.getPath());
        } catch (IOException e) {
        	 logger.error("Test Failed ", e);
        }
		
		Enumeration<? extends ZipEntry> enumeration = zipFile.entries();		
		StringWriter writer = null;
		InputStream is = null;
		List<String[]> hl7BodyArray = new ArrayList<String[]>();
		String hl7Body = "";

		while( enumeration.hasMoreElements() ) {
			
			ZipEntry zipEntry = enumeration.nextElement();
						
			if(zipEntry.getName().endsWith(".txt")) {
				
				logger.debug( zipEntry.getName() );
				
				writer = new StringWriter();
				
				try {					
					is = zipFile.getInputStream( zipEntry );					
					IOUtils.copy(is, writer, StandardCharsets.UTF_8);														 
	            } catch (IOException e) {

	            	if( zipFile != null ) {
	            		try {
	                        zipFile.close();
	                        zipFile = null;
                        } catch (IOException e1) {
                        	 logger.error("Test Failed ", e);
                        }	            		
	            	}
	            	 logger.error("Test Failed ", e);
	            }finally {
	            	if( is != null ) {
	            		try {
	    	                is.close();
	    	                is = null;
	                    } catch (IOException e) {
	                    	 logger.error("Test Failed ", e);
	                    }
	            	}
	            	
	            	
	            }
				
				hl7Body = writer.toString();
				hl7BodyArray.add(new String[]{hl7Body});
			}
		}		
		return hl7BodyArray;
	}
	
	public MEDITECHHandlerTest(String hl7Body) {
		MEDITECHHandlerTest.hl7Body = hl7Body;
	}

	@Test
	public void testParse() {

		TEST_COUNT += 1;

		logger.info("#------------>>  Testing MEDITECHHandler Uploader for file: (" + TEST_COUNT + ")");

	    try {
	        logger.info( parse( new ByteArrayInputStream( hl7Body.getBytes(StandardCharsets.UTF_8) ) ) );
        } catch (ParserConfigurationException e) {
        	 logger.error("Test Failed ", e);
        } catch (SAXException e) {
        	 logger.error("Test Failed ", e);
        } catch (IOException e) {
        	 logger.error("Test Failed ", e);
        }
	}

}

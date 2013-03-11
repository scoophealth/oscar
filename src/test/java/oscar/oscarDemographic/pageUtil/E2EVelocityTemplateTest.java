/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package oscar.oscarDemographic.pageUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.dao.utils.SchemaUtils;
import org.oscarehr.util.MiscUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
/**
 * 
 * @author rrusk
 *  This test class is meant to test the E2E document
 *  generated from a Velocity template.
 */
public class E2EVelocityTemplateTest {
	
	private boolean isValid;  // used in ErrorHandler to flag invalid documents
	
	@BeforeClass
	public static void onlyOnce() throws Exception {
		// Loading in the test data takes too long to do it
		// before each unit test.
		//
		// In the sql script, configure FOREIGN_KEYS_CHECK=0
		// before loading the db and reset to original value
		// at end.
		assertEquals("Error loading test data",
				SchemaUtils.loadFileIntoMySQL(System.getProperty("basedir")+
						"/src/test/resources/e2e-test-db.sql"),0);
	}

	@Test
	public void testE2EVelocityTemplate() {
		assertNotNull(new E2EVelocityTemplate());
	}

	@Test
	public void testExport() {
		E2EVelocityTemplate e2etemplate = new E2EVelocityTemplate();
		assertNotNull(e2etemplate);
		PatientExport p = new PatientExport("1");
		assertNotNull(p);
		String s = null;
		try {
	        s = e2etemplate.export(p);
        } catch (Exception e) {
        	MiscUtils.getLogger().error(e.getMessage());
	        fail();
        }
		assertNotNull(s);
		assertFalse(s.isEmpty());
		// should be no $ variables in output
		assertFalse(s.contains("$"));
		
		// check output is well-formed
		assertTrue(isWellFormedXML(s));
		assertFalse(isWellFormedXML(s.replace("</ClinicalDocument>", "</clinicalDocument>")));

		// validate against XML schema
		assertTrue(isValidXML(s));
		assertFalse(isValidXML(s.replace("DOCSECT", "DOXSECT")));

	}
	
	// check input string is well-formed XML
	public boolean isWellFormedXML(String xmlstring) {
		boolean result = false;

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
        try {
	        SAXParser parser = factory.newSAXParser();
	        XMLReader reader = parser.getXMLReader();
			reader.setErrorHandler(new SimpleErrorHandler());
			// the parse method throws an exception
			// if the XML is not well-formed
			isValid = true;
			reader.parse(new InputSource(new StringReader(xmlstring)));
			result = isValid;
        } catch (ParserConfigurationException e) {
        	MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
        } catch (SAXException e) {
        	//MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
        } catch (IOException e) {
        	MiscUtils.getLogger().error(e.getCause());
        	isValid = false;
        }
		return result;
	}
		
	public boolean isValidXML(String xmlstring) {	
		boolean result = false;
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);

        try {
	        SAXParser parser = factory.newSAXParser();
	        parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", 
	              "http://www.w3.org/2001/XMLSchema");
	        XMLReader reader = parser.getXMLReader();
			reader.setErrorHandler(new SimpleErrorHandler());
			reader.setEntityResolver(new E2EEntityResolver());
			isValid = true;
	        reader.parse(new InputSource(new StringReader(xmlstring)));
	        result = isValid;
        } catch (ParserConfigurationException e) {
        	MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
        } catch (SAXNotRecognizedException e) {
        	MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
        } catch (SAXNotSupportedException e) {
        	MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
        } catch (SAXException e) {
        	MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
        } catch (IOException e) {
        	MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
        }
		return result;
	}
	
	public class SimpleErrorHandler implements ErrorHandler {
	    public void warning(SAXParseException e) throws SAXException {
        	//MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
	    }

	    public void error(SAXParseException e) throws SAXException {
	    	//Commented out to suppress output from forced failures
        	//MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
	    }

	    public void fatalError(SAXParseException e) throws SAXException {
	    	//Commented out to suppress output from forced failures
        	//MiscUtils.getLogger().error(e.getMessage());
        	isValid = false;
	    }
	}
	
	private class E2EEntityResolver implements EntityResolver {
		 
		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {

			// Grab only the filename part from the full path
			String filename = new File(systemId).getName();
		 
			// Now prepend the correct path
			String correctedId = System.getProperty("basedir")+
					"/src/test/resources/e2e/" + filename;
		 
			InputSource is = new InputSource(
					ClassLoader.getSystemResourceAsStream(correctedId));
			is.setSystemId(correctedId);
		 
			return is;
		}
	}
		
}

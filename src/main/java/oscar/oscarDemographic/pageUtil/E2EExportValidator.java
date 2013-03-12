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

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.oscarehr.util.MiscUtils;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXNotRecognizedException;
import org.xml.sax.SAXNotSupportedException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

public class E2EExportValidator {
	
	private static boolean isValid;  // used in ErrorHandler to flag invalid documents
	private static boolean loggingErrors;
	
	private E2EExportValidator() {}
	
	// check input string is well-formed XML
	public static boolean isWellFormedXML(String xmlstring,
			boolean logErrors) {
		boolean result = false;

		loggingErrors = logErrors;
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
        	if (loggingErrors) {
        		MiscUtils.getLogger().error(e.getMessage());
        	}
        	isValid = false;
        } catch (SAXException e) {
        	if (loggingErrors) {
        		MiscUtils.getLogger().error(e.getMessage());
        	}
        	isValid = false;
        } catch (IOException e) {
        	if (loggingErrors) {
        		MiscUtils.getLogger().error(e.getCause());
        	}
        	isValid = false;
        }
		return result;
	}
	
	public static boolean isValidXML(String xmlstring,
			boolean logErrors) {	
		boolean result = false;
		
		loggingErrors = logErrors;
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
        	if (loggingErrors) {
        		MiscUtils.getLogger().error(e.getMessage());
        	}
        	isValid = false;
        } catch (SAXNotRecognizedException e) {
        	if (loggingErrors) {
        		MiscUtils.getLogger().error(e.getMessage());
        	}
        	isValid = false;
        } catch (SAXNotSupportedException e) {
        	if (loggingErrors) {
        		MiscUtils.getLogger().error(e.getMessage());
        	}
        	isValid = false;
        } catch (SAXException e) {
        	if (loggingErrors) {
        		MiscUtils.getLogger().error(e.getMessage());
        	}
        	isValid = false;
        } catch (IOException e) {
        	if (loggingErrors) {
        		MiscUtils.getLogger().error(e.getMessage());
        	}
        	isValid = false;
        }
		return result;
	}

	private static class SimpleErrorHandler implements ErrorHandler {
		
	    public void warning(SAXParseException e) throws SAXException {
        	if (loggingErrors) {
        		MiscUtils.getLogger().error(e.getMessage());
        	}
        	isValid = false;
	    }

	    public void error(SAXParseException e) throws SAXException {
	    	if (loggingErrors) {
	    		MiscUtils.getLogger().error(e.getMessage());
	    	}
        	isValid = false;
	    }

	    public void fatalError(SAXParseException e) throws SAXException {
	    	if (loggingErrors) {
	    		MiscUtils.getLogger().error(e.getMessage());
	    	}
        	isValid = false;
	    }
	}
	
	private static class E2EEntityResolver implements EntityResolver {
		 
		public InputSource resolveEntity(String publicId, String systemId)
				throws SAXException, IOException {

			// Grab only the filename part from the full path
			String filename = new File(systemId).getName();
		 
			// Now prepend the correct path
			String correctedId = System.getProperty("basedir")+
					"/src/main/resources/e2e/" + filename;
		 
			InputSource is = new InputSource(
					ClassLoader.getSystemResourceAsStream(correctedId));
			is.setSystemId(correctedId);
		 
			return is;
		}
	}
}

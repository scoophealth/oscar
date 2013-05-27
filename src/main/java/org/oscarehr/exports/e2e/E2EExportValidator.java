/**
 * Copyright (c) 2013. Department of Family Practice, University of British Columbia. All Rights Reserved.
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
 * Department of Family Practice
 * Faculty of Medicine
 * University of British Columbia
 * Vancouver, Canada
 */
package org.oscarehr.exports.e2e;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
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
 * A class used to validate exported documents against the EMR2EMR XML schema.
 * 
 * @author Raymond Rusk
 */
public class E2EExportValidator {
	private static Logger logger=MiscUtils.getLogger();
	private E2EExportValidator() {}

	/**
	 * Checks if input string is a well-formed E2E XML document
	 * 
	 * @param xmlstring XML Document
	 * @return True if input is well-formed, else false
	 */
	public static boolean isWellFormedXML(String xmlstring) {
		boolean result = false;

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(false);
		factory.setNamespaceAware(true);
		try {
			SAXParser parser = factory.newSAXParser();
			XMLReader reader = parser.getXMLReader();
			reader.setErrorHandler(new SimpleErrorHandler());
			// the parse method throws an exception if the XML is not well-formed
			reader.parse(new InputSource(new StringReader(xmlstring)));
			result = true;
		} catch (ParserConfigurationException e) {
			logger.error("VALIDATION ERROR: ", e);
		} catch (SAXException e) {
			logger.warn("VALIDATION ERROR: " + e.getMessage());
		} catch (IOException e) {
			logger.error("VALIDATION ERROR: ", e);
		}
		return result;
	}

	/**
	 * Checks if input string is a valid XML document
	 * 
	 * @param xmlstring XML Document
	 * @return True if input is a valid, else false
	 */
	public static boolean isValidXML(String xmlstring) {
		boolean result = false;

		SAXParserFactory factory = SAXParserFactory.newInstance();
		factory.setValidating(true);
		factory.setNamespaceAware(true);

		try {
			SAXParser parser = factory.newSAXParser();
			parser.setProperty("http://java.sun.com/xml/jaxp/properties/schemaLanguage", "http://www.w3.org/2001/XMLSchema");
			XMLReader reader = parser.getXMLReader();
			reader.setErrorHandler(new SimpleErrorHandler());
			reader.setEntityResolver(new E2EEntityResolver());
			reader.parse(new InputSource(new StringReader(xmlstring)));
			result = true;
		} catch (ParserConfigurationException e) {
			logger.error("VALIDATION ERROR: ", e);
		} catch (SAXNotRecognizedException e) {
			logger.error("VALIDATION ERROR: ", e);
		} catch (SAXNotSupportedException e) {
			logger.error("VALIDATION ERROR: ", e);
		} catch (SAXException e) {
			logger.warn("VALIDATION ERROR: " + e.getMessage());
		} catch (IOException e) {
			logger.error("VALIDATION ERROR: ", e);
		}
		return result;
	}

	/**
	 * @author Raymond Rusk
	 */
	private static class SimpleErrorHandler implements ErrorHandler {
		public void warning(SAXParseException e) throws SAXException {
			throw new SAXException("(Parsing Warning) "+e.getMessage());
		}

		public void error(SAXParseException e) throws SAXException {
			throw new SAXException("(Parsing Error) "+e.getMessage());
		}

		public void fatalError(SAXParseException e) throws SAXException {
			throw new SAXException("(Parsing Fatal Error) "+e.getMessage());
		}
	}

	/**
	 * @author Raymond Rusk
	 */
	private static class E2EEntityResolver implements EntityResolver {
		public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
			// Grab only the filename part from the full path
			String filename = new File(systemId).getName();

			// Now prepend the correct path
			String correctedId = E2EExportValidator.class.getResource("/e2e/"+filename).getPath();

			InputSource is = new InputSource(ClassLoader.getSystemResourceAsStream(correctedId));
			is.setSystemId(correctedId);

			return is;
		}
	}
}

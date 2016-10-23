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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import oscar.oscarLab.ca.all.upload.MessageUploader;

public class MEDITECHHandler implements MessageHandler {

	private static Logger logger = Logger.getLogger(IHAHandler.class);
	private final String XML = "<(\\S+?)(.*?)>(.*?)</\\1>";
	
	@Override
	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {

		FileInputStream is = null;
		List<String> hl7BodyList = null;
		String success = "success";

		try {

			is = new FileInputStream( fileName );	        
			hl7BodyList = parse( is );	
			int index = 0;
			while ( "success".equals( success ) && index < hl7BodyList.size() ) {				
				success = MessageUploader.routeReport(loggedInInfo, serviceName, "MEDITECH", hl7BodyList.get(index), fileId);				
				index++;
			}
			
			if( success != null && success.isEmpty() ) {
				success = null;
			}

		} catch (Exception e) {
			success = null;
			logger.error("Could not upload MEDITECH message " + fileName , e);
		} finally {
			if( is != null ) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					logger.error("Failed to close MEDITECH InputStream ", e);
				}
			}
			if( success == null ) {
				logger.error( "Cleaning up MessageUploader file." );
				MessageUploader.clean(fileId);
			}
		}

		return success;

	}

	public List<String> parse(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		return textOrXml(is);
	}

	private List<String> textOrXml(InputStream is) throws ParserConfigurationException, SAXException, IOException {

		Pattern pattern;
		Matcher matcher;

		String hl7Body = getString(is).trim();
		List<String> hl7BodyList = null;

		if( hl7Body != null && hl7Body.length() > 0 ) {

			pattern = Pattern.compile( XML, Pattern.CASE_INSENSITIVE | Pattern.DOTALL | Pattern.MULTILINE );
			matcher = pattern.matcher( hl7Body );

			if( matcher.matches() ) {
				hl7BodyList = parseXml( hl7Body );
			} else {
				hl7BodyList = parseText( hl7Body );
			}

		}

		return hl7BodyList;
	}

	private List<String> parseXml(String hl7Body) throws ParserConfigurationException, SAXException, IOException {

		Node messageSpec = null;
		NodeList messages = null;

		List<String> hl7BodyList = null;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(hl7Body);

		if(doc != null) {
			messageSpec = doc.getFirstChild();
		}

		if(messageSpec != null) {
			messages = messageSpec.getChildNodes();

			for( int i = 0; i < messages.getLength(); i++ ) {
				if( hl7BodyList == null ) {
					hl7BodyList = new ArrayList<String>();
				}
				hl7BodyList.add( messages.item(i).getFirstChild().getTextContent() );
			}		
		}

		return hl7BodyList;
	}

	private List<String> parseText( String hl7Body ) {

		// anymore division and pre-parsing should be done here. 
		// so far, only one body per string is expected.		
		String[] hl7BodyList = new String[]{ hl7Body };
		return Arrays.asList( hl7BodyList );
	}

	private String getString(InputStream is) {

		StringWriter writer = new StringWriter();

		try {
			IOUtils.copy(is, writer, "UTF-8");
		} catch (IOException e) {
			logger.fatal("InputStream failure", e );
		} finally {
			if( is != null ) {
				try {
					is.close();
				} catch (IOException e) {
					logger.fatal("Failed to close InputStream ", e );
				}
			}
		}

		return writer.toString();
	}

}

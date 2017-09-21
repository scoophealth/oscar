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
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import oscar.oscarLab.ca.all.upload.MessageUploader;

public class IHAPOIHandler implements MessageHandler {
	
	public final String HL7_FORMAT = "IHAPOI";
	
	Logger logger = MiscUtils.getLogger();
	private final String XML_PATTERN = "<";
	private final String MESSAGE_ID_NODE_NAME = "msgId";
	private final String SUCCESS = "success:";
	private final String FAILED = "fail:";
	
	@Override
	public String parse(LoggedInInfo loggedInInfo, String serviceName, String fileName, int fileId, String ipAddr) {

		FileInputStream is = null;
		Map<String, String> hl7BodyMap = null;
		String messageId = "0";
		StringBuilder result = new StringBuilder( FAILED + messageId + "," );
		
		try {

			is = new FileInputStream( fileName );	        
			hl7BodyMap = parse( is );	
			Iterator<String> keySetIterator = null;
			
			if( hl7BodyMap != null && hl7BodyMap.size() > 0 ) {
				keySetIterator = hl7BodyMap.keySet().iterator();
			} 
			
			if( keySetIterator != null ) {
				result = new StringBuilder( "" );			
				while ( keySetIterator.hasNext() ) {
					messageId = keySetIterator.next();
					if( ! MessageUploader.routeReport( loggedInInfo, serviceName, HL7_FORMAT, hl7BodyMap.get(messageId), fileId ).isEmpty() ) {
						result.append( SUCCESS + messageId + "," );
					} else {
						result.append( FAILED + messageId + "," );
					}
				}
			}

		} catch (ExceptionInInitializerError e) {
			result = new StringBuilder( FAILED + messageId + "," );
			logger.error("There was an unknown internal error with file " + fileName + " message id " + messageId, e);
		} catch (Exception e) {
			result = new StringBuilder( FAILED + messageId + "," );
			logger.error("Could not upload IHAPOI message " + fileName + " due to an error with message id " + messageId, e);
		} finally {
			if( is != null ) {
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					logger.error("Failed to close IHAPOI InputStream ", e);
				}
			}
			if( FAILED.equals( result.toString().split(":")[0] + ":" ) ) {
				logger.error( "Cleaning up MessageUploader file." );
				MessageUploader.clean(fileId);
			}
		}

		if( result.length() > 1) {
			result = result.deleteCharAt( result.length() - 1 );
		}
		
		return result.toString();

	}

	public Map<String, String> parse(InputStream is) throws ParserConfigurationException, SAXException, IOException {
		return textOrXml(is);
	}

	private Map<String, String> textOrXml(InputStream is) throws ParserConfigurationException, SAXException, IOException {

		ByteArrayInputStream bais = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		org.apache.commons.io.IOUtils.copy(is, baos);
		byte[] bytes = baos.toByteArray();
		String hl7Body = new String( bytes, StandardCharsets.UTF_8 ).trim();
		Map<String, String> hl7BodyMap = null;

		if( hl7Body != null && hl7Body.length() > 0 ) {

			if( hl7Body.startsWith( XML_PATTERN ) ) {
				bais = new ByteArrayInputStream( bytes );
				bais.reset();
				hl7BodyMap = parseXml( bais );
			} else {
				hl7BodyMap = parseText( hl7Body );
			}

		}
		
		if( baos != null ) {
			baos.close();
		}
		
		if( bais != null ) {
			bais.close();
		}

		return hl7BodyMap;
	}

	protected Map<String, String> parseXml(InputStream is) throws ParserConfigurationException, SAXException, IOException {

		Element messageSpec = null;
		Element messagesElement = null;
		NodeList messagesNode = null;

		Map<String, String> hl7BodyMap = null;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		docFactory.setNamespaceAware(true);
		docFactory.setValidating(false);
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(is);

		if(doc != null) {
			messageSpec = doc.getDocumentElement();
		}
	
		if( messageSpec != null && messageSpec.hasChildNodes() ) {
			messageSpec.normalize();
			messagesNode = messageSpec.getChildNodes();
			for(int i = 0; i < messagesNode.getLength(); i++) {
				if (messagesNode.item(i) instanceof Element) {
					messagesElement = (Element) messagesNode.item(i);
					break;
				}
			}
		}
		
		if(messagesElement != null && messagesElement.hasChildNodes() ) {
			messagesNode = messagesElement.getChildNodes();
		}
		
		if( messagesNode.getLength() > 0 ) {
			for( int i = 0; i < messagesNode.getLength(); i++ ) {

				if( hl7BodyMap == null ) {
					hl7BodyMap = new HashMap<String, String>();
				}
				
				if( messagesNode.item(i) instanceof Element ) {					
					Element messageNode = (Element) messagesNode.item(i);					
					hl7BodyMap.put( getMessageId( messageNode ), messageNode.getTextContent() ); 
				}
			}		
		}

		return hl7BodyMap;
	}

	private Map<String, String> parseText( String hl7Body ) {

		// anymore division and pre-parsing should be done here. 
		// so far, only one body per string is expected.		
		HashMap<String, String> hl7BodyMap = new HashMap<String, String>();
		hl7BodyMap.put("0", hl7Body);
		return hl7BodyMap;
	}
	
	private String getMessageId( Element element ) {
		
		NamedNodeMap nodeAttributes = element.getAttributes();
		String messageId = "";
		
        if ( nodeAttributes != null ) {
            for (int i = 0; i < nodeAttributes.getLength(); i++) {
            	Node attribute = nodeAttributes.item(i);
                String attributeName = attribute.getNodeName();
                if( MESSAGE_ID_NODE_NAME.equalsIgnoreCase( attributeName ) ) {
                	messageId = attribute.getNodeValue();
                }
            }
        }
        
        return messageId;
	}

}

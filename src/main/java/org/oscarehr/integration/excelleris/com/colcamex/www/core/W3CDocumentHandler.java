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
package org.oscarehr.integration.excelleris.com.colcamex.www.core;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
/**
 * @author Dennis Warren Colcamex Resources
 * 
 * Major Contributors: 
 *  OSCARprn
 *  NERD
 *   
 * This community edition of Expedius is for use at your own risk, without warranty, and 
 * support. 
 * 
 */
public class W3CDocumentHandler {

	private static final String HL7_MESSAGE_TAG = "message";
	private static final String ATTRIBUTE_LAB_ID = "msgid";
	private static final String ATTRIBUTE_HL7_VERSION = "version";
	private static final String ATTRIBUTE_LAB_FORMAT = "format";
	private static final String ATTRIBUTE_MESSAGE_FORMAT = "messageformat"; //i.e.: ORUR01
	private static final String ATTRIBUTE_MESSAGE_COUNT = "messagecount";
	
	private static Logger logger = Logger.getLogger("ExpediusConnect");
	private DocumentBuilder documentBuilder;
	private Document document;
	private Document newDocument;
	private Node root;
	private NodeList children;

	
	public W3CDocumentHandler() {
		super();
		DocumentBuilderFactory documentBuilderFactory =  DocumentBuilderFactory.newInstance();	
		
		try {			
			setDocumentBuilder( documentBuilderFactory.newDocumentBuilder() );
			// setDocument( getDocumentBuilder().newDocument() );
		} catch (ParserConfigurationException e1) {
			logger.severe("Expedius document manager failed to instantiate document builder." + e1);
		}
	}
	
	/**
	 * Instantiate with a custom document builder factory.
	 * @param documentBuilderFactory
	 */
	public W3CDocumentHandler(DocumentBuilderFactory documentBuilderFactory) { 
		try {			
			setDocumentBuilder( documentBuilderFactory.newDocumentBuilder() );
		} catch (ParserConfigurationException e1) {
			logger.severe("Expedius document manager failed to instantiate document builder." + e1);
		}
	}

	public synchronized DocumentBuilder getDocumentBuilder() {
		return documentBuilder;
	}

	public synchronized void setDocumentBuilder(DocumentBuilder documentBuilder) {
		this.documentBuilder = documentBuilder;
	}

	public synchronized Document getDocument() {
		return document;
	}

	public synchronized void setDocument(Document document) {
		this.document = document;
	}

	public synchronized Node getRoot() {
		this.root = getDocument().getDocumentElement();
		return this.root;
	}

	public synchronized NodeList getRootChildren() {
		getRoot();
		if(this.root.hasChildNodes()) {
			this.children = getRoot().getChildNodes();
		}
		return this.children;
	}

	public synchronized int getMessageCount() {
		
		// try the root tag first.
		NodeList nodeList = null;
		String messageCount = getRootAttributeValue(ATTRIBUTE_MESSAGE_COUNT);

		if(messageCount == null) {
			nodeList = getMessageNodes();
			if(nodeList != null) {
				messageCount = nodeList.getLength()+"";
			}
		}
		
		if(messageCount != null) {
			return Integer.parseInt(messageCount);
		}
		
		return 0;
	}
	
	public synchronized NodeList getMessageNodes() {
		return getNodes(HL7_MESSAGE_TAG);
	}
	
	public synchronized String[] getMessageIdList() {
		return this.getAttributeValueArray(ATTRIBUTE_LAB_ID, getRootChildren());
	}

	public synchronized String getHL7Version() {
		return getFirstNodeAttributeValue(ATTRIBUTE_HL7_VERSION);
	}
	
	public synchronized String getHL7Format() {
		return getFirstNodeAttributeValue(ATTRIBUTE_LAB_FORMAT);
	}
	
	public synchronized String getMessageFormat() {
		return getFirstNodeAttributeValue(ATTRIBUTE_MESSAGE_FORMAT);
	}
	
	/**
	 * Parse and set a new Document object from an InputStream.
	 * @param is
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document parse(InputStream is) throws SAXException, IOException {

		if(this.document != null) {
			document = null;
		}
		
		setDocument( getDocumentBuilder().parse(is) );
		getDocumentBuilder().reset();
		return getDocument();
	}
	
	/**
	 * Overloaded parse from file path.
	 * @param documentPath
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public Document parse(String documentPath) throws SAXException, IOException {

		InputStream is = null;
		if(documentPath != null) {
			is = this.getClass().getClassLoader().getResourceAsStream(documentPath);
		}
		return parse(is);
	}

	public synchronized Document createNewDocument(String xmlFragment) throws SAXException, IOException {
		Node node = documentBuilder.parse(new ByteArrayInputStream(xmlFragment.getBytes())).getDocumentElement();
		return createNewDocument(node);
	}
	
	public synchronized Document createNewDocument(Node node) {

		this.newDocument = documentBuilder.newDocument();

		Node newRoot = this.newDocument.importNode(node, true);
		this.newDocument.appendChild(newRoot);
		
		return this.newDocument;
	}

	public synchronized Document getNewDocument() {
		return this.newDocument;
	}
	
	public synchronized String getRootAttributeValue(String attributeName) {
		return getNodeAttributeValue(attributeName, getRoot());
	}
	
	public synchronized String getFirstNodeAttributeValue(String attributeName) {

		// first try the root
		String nodeValue = getRootAttributeValue(attributeName);
		
		if(nodeValue == null) {
			// then the children
			nodeValue = getFirstNodeAttributeValue(attributeName, getRootChildren());
		}
		return nodeValue;
	}
	
	public synchronized String getFirstNodeAttributeValue(String attributeName, NodeList nodeList) {
		
		String nodeName = null;
		Node node = null;
		int length = nodeList.getLength();

		for( int i = 0; i < length; i++ ) {
			node = nodeList.item(i);
			if(node.getNodeType() == Node.ATTRIBUTE_NODE) {
				nodeName = node.getNodeName();
				if(attributeName.equalsIgnoreCase(nodeName)) {
					i = length;					
				}						
			}
		}
		return node.getNodeValue();
	}
	
	public synchronized String getNodeAttributeValue(String attributeName, Node node) {
		NamedNodeMap attributes = null;
		Node attribute = null;
		String nodeName = null;
		
		if(node != null) {
			attributes = node.getAttributes();
		}
		
		if(attributes != null) {
		    for( int i = 0; i < attributes.getLength(); i++ ) {	    	
		        attribute = attributes.item(i);
		        nodeName = attribute.getNodeName().toLowerCase();
		        if (attributeName.equalsIgnoreCase(nodeName) ) {	        	
		        	return attribute.getNodeValue();
		        }	        
		    }
		}
		
	    return null;
	}
	
	public synchronized NodeList getNodes(String elementName) {
		// if only XML developers would use proper naming convention...
		String childName = null;
		Node child = null;
		int length = 0;
		getRootChildren();
		
		if( this.children != null ) {
			
			length = this.children.getLength();
			
			for(int i = 0; i < length; i++) {
				child = this.children.item(i);
				childName = child.getNodeName();

				if( elementName.equalsIgnoreCase(childName) ) {
					i = length;
				}
			}
			
		}
		
		if(childName != null) {
			return getDocument().getElementsByTagName(childName);
		}
		
		return null;
	}
	
	public synchronized String[] getAttributeValueArray(String attributeName, String tagName) {
		return getAttributeValueArray(attributeName, getNodes(tagName));
	}
		
	public synchronized String[] getAttributeValueArray(String attributeName, NodeList nodes) {
		int listSize = nodes.getLength();
		String[] attributes = new String[listSize];
		Node node = null;
		String attribute = null;
		NamedNodeMap attributeMap = null;
		Node attributeItem = null;
		
		for(int i = 0; listSize > i; i++) {
			node = nodes.item(i);
			if(node.hasAttributes()) {				
				// if only XML developers would use proper naming convention (ie: lowercase for attribute names)
				attributeMap = node.getAttributes();
				for(int j = 0; attributeMap.getLength() > j; j++) {
					attributeItem = attributeMap.item(j);
					attribute = attributeItem.getNodeName();

					if( attributeName.equalsIgnoreCase(attribute) ) {
						attributes[i] = attributeItem.getNodeValue();
					}										
				}				
			}
		}
		
		return attributes;
	}

}

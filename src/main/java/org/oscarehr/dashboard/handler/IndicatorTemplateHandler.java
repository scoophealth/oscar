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
package org.oscarehr.dashboard.handler;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class IndicatorTemplateHandler {
	
	private static Logger logger = MiscUtils.getLogger();
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy");
	private Document indicatorTemplateDocument;
	private IndicatorTemplate indicatorTemplateEntity;
	private IndicatorTemplateXML indicatorTemplateXML; 
	private byte[] bytearray;
	
	public IndicatorTemplateHandler() {
		// default
	}
	
	public IndicatorTemplateHandler( byte[] bytearray ) {		
		read( bytearray );
	}
	
//	public boolean validate() {
//		Schema schema = null;
//		try {
//		  String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
//		  SchemaFactory factory = SchemaFactory.newInstance(language);
//		  schema = factory.newSchema(new File(name));
//		} catch (Exception e) {
//		    e.printStackStrace();
//		}
//		Validator validator = schema.newValidator();
//		validator.validate(new DOMSource(document));
//		return Boolean.TRUE;
//	}
	
	public void read( byte[] bytearray ) {
		this.bytearray = bytearray;
		// validate();
		setIndicatorTemplateDocument( this.bytearray );		
		setIndicatorTemplateXML( getIndicatorTemplateDocument() );
		setIndicatorTemplateEntity( getIndicatorTemplateXML() );
	}
	
	public Document getIndicatorTemplateDocument() {
		return indicatorTemplateDocument;
	}

	private void setIndicatorTemplateDocument( byte[] bytearray ) {
		try {
			this.indicatorTemplateDocument = byteToDocument( bytearray );
		} catch (SAXException e) {
			logger.error("error",e);
		} catch (IOException e) {
			logger.error("error",e);
		} catch (ParserConfigurationException e) {
			logger.error("error",e);
		}
	}

	/**
	 * The IndicatorTemplate JPA database entity. 
	 * Used to retrieve or persist the XML template and its header data.
	 */
	public IndicatorTemplate getIndicatorTemplateEntity() {
		return indicatorTemplateEntity;
	}
	
	private void setIndicatorTemplateEntity( IndicatorTemplateXML indicatorTemplateXML ) {
		this.indicatorTemplateEntity = indicatorTemplateEntityFactory( indicatorTemplateXML );
	}

	public IndicatorTemplateXML getIndicatorTemplateXML() {
		return this.indicatorTemplateXML;
	}

	private void setIndicatorTemplateXML( Document xmlDocument ) {
		this.indicatorTemplateXML = indicatorTemplateXMLFactory( xmlDocument );
		this.indicatorTemplateXML.setTemplate( new String( this.bytearray ) );
	}

	// helpers
	public static final IndicatorTemplateXML indicatorTemplateXMLFactory( final Document xmlDocument ) {
		
		IndicatorTemplateXML indicatorTemplateXML = new IndicatorTemplateXML();
		String category = "";
		String subCategory = "";
		String name = "";
		String definition = "";
		String framework = "";
		Date frameworkDate = null;
		String notes = "";
		
		xmlDocument.getDocumentElement().normalize();
		Node heading = xmlDocument.getElementsByTagName("heading").item(0);
		
		if( heading.getNodeType() == Node.ELEMENT_NODE ) {
			Element element = (Element) heading;
			category = element.getElementsByTagName("category").item(0).getTextContent();
			subCategory = element.getElementsByTagName("subCategory").item(0).getTextContent();
			name = element.getElementsByTagName("name").item(0).getTextContent();
			definition = element.getElementsByTagName("definition").item(0).getTextContent();
			framework = element.getElementsByTagName("framework").item(0).getTextContent();
			notes = element.getElementsByTagName("notes").item(0).getTextContent();
			String frameworkVersion = element.getElementsByTagName("frameworkVersion").item(0).getTextContent();
			if( frameworkVersion != null && ! frameworkVersion.isEmpty() ) {
				try {
					frameworkDate = simpleDateFormat.parse(frameworkVersion);
				} catch (ParseException e) {
					logger.error("Date parsing error",e);
				}
			}
		}
		
		indicatorTemplateXML.setCategory(category);
		indicatorTemplateXML.setSubCategory(subCategory);
		indicatorTemplateXML.setName(name);
		indicatorTemplateXML.setDefinition(definition);
		indicatorTemplateXML.setFramework(framework);
		indicatorTemplateXML.setFrameworkVersion(frameworkDate);
		indicatorTemplateXML.setNotes(notes);
		
		return indicatorTemplateXML;
		
//		IndicatorTemplateXML indicatorTemplate = null;
//		try {
//			JAXBContext jaxbContext = JAXBContext.newInstance(IndicatorTemplateXML.class);
//			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
//			InputStream inputStream = new ByteArrayInputStream( bytearray );
//			indicatorTemplate = (IndicatorTemplateXML) jaxbUnmarshaller.unmarshal( inputStream );
//			indicatorTemplate.setTemplate( new String(bytearray) );
//		} catch (JAXBException e) {
//			logger.error("error",e);
//		}
//		return indicatorTemplate;
	}
	
	public static final IndicatorTemplate indicatorTemplateEntityFactory( IndicatorTemplateXML indicatorTemplateXML ) {
		
		IndicatorTemplate indicatorTemplate = null;
		
		if(indicatorTemplateXML != null) {
			
			indicatorTemplate = new IndicatorTemplate();
			indicatorTemplate.setTemplate( indicatorTemplateXML.getTemplate() );			
			indicatorTemplate.setActive( Boolean.TRUE );
			indicatorTemplate.setLocked( Boolean.FALSE );
			indicatorTemplate.setCategory(indicatorTemplateXML.getCategory());
			indicatorTemplate.setSubCategory(indicatorTemplateXML.getSubCategory());
			indicatorTemplate.setName(indicatorTemplateXML.getName());
			indicatorTemplate.setDefinition(indicatorTemplateXML.getDefinition());
			indicatorTemplate.setFramework(indicatorTemplateXML.getFramework());
			indicatorTemplate.setFrameworkVersion(indicatorTemplateXML.getFrameworkVersion());
			indicatorTemplate.setNotes(indicatorTemplateXML.getNotes());
			
		}

		return indicatorTemplate;
	}


	public static final Document byteToDocument( final byte[] bytearray ) 
			throws SAXException, IOException, ParserConfigurationException {
		
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    // factory.setValidating(true);
	    DocumentBuilder builder = factory.newDocumentBuilder();
	    return builder.parse( new ByteArrayInputStream( bytearray ) );
	}

}

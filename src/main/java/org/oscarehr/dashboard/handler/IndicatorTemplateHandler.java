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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.log4j.Logger;
import org.oscarehr.common.model.IndicatorTemplate;
import org.oscarehr.util.MiscUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

/**
 * 
 * Converts a XML IndicatorTemplate string into several formats: 
 * Document
 * IndicatorTemplate Entity Bean
 * IndicatorTemplateXML Bean - for XML to POJO parsing.
 * IndicatorBean - for display layer
 *
 */
public class IndicatorTemplateHandler{
	
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

	// TODO validate XML template against schema. 
	// Validate method will be called from the Action class.
	// user will be informed of mal-formed XML
	
	// TODO add additional error check / filter class that will
	// handle all validations and error messages. 
	
	
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
		setIndicatorTemplateXML( new IndicatorTemplateXML( getIndicatorTemplateDocument() ) );
		setIndicatorTemplateEntity( indicatorTemplateEntityFromXML( getIndicatorTemplateXML() ) );
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
	
	private void setIndicatorTemplateEntity( IndicatorTemplate indicatorTemplateEntity ) {
		this.indicatorTemplateEntity = indicatorTemplateEntity;
	}

	public IndicatorTemplateXML getIndicatorTemplateXML() {
		return this.indicatorTemplateXML;
	}

	private void setIndicatorTemplateXML( IndicatorTemplateXML indicatorTemplateXML ) {
		indicatorTemplateXML.setTemplate( new String( this.bytearray ) );
		this.indicatorTemplateXML = indicatorTemplateXML;				
	}
	
	private static final IndicatorTemplate indicatorTemplateEntityFromXML( IndicatorTemplateXML indicatorTemplateXML ) {
		
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
			
			try {
				indicatorTemplate.setFrameworkVersion(simpleDateFormat.parse( indicatorTemplateXML.getFrameworkVersion() ));
			} catch (ParseException e) {
				logger.error("Date parsing error",e);
			}
			
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

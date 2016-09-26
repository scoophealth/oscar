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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

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
	private static final String schemaFile = "indicatorXMLTemplates/IndicatorXMLTemplateSchema.xsd";
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

	public boolean validate( StringBuilder message ) {
		return validateXML( message );
	}
	
	private boolean validateXML( StringBuilder message ) {
		boolean valid = Boolean.TRUE;
		try {
			
			SchemaFactory factory = SchemaFactory.newInstance( XMLConstants.W3C_XML_SCHEMA_NS_URI );
			URL schemaSource = Thread.currentThread().getContextClassLoader().getResource( schemaFile );

			File schemaFile = new File( schemaSource.toURI() );
			Schema schema = factory.newSchema( schemaFile );
			Validator validator = schema.newValidator();
			validator.validate( new DOMSource( getIndicatorTemplateDocument() ) );
			
		} catch (Exception e) {
			
			logger.error( "Failed XML Validation ", e );
			if( message != null ) {
				message.append( "Failed XML Validation " );
				message.append( e.getMessage() );
				valid = Boolean.FALSE;
			}
			
		}
		
		return valid;
	}
	

	public void read( byte[] bytearray ) {
		this.bytearray = bytearray;
		setIndicatorTemplateDocument( this.bytearray );	

		IndicatorTemplateXML indicatorTemplateXML =  new IndicatorTemplateXML( getIndicatorTemplateDocument() );
		setIndicatorTemplateXML( indicatorTemplateXML );
		setIndicatorTemplateEntity( indicatorTemplateEntityFromXML( getIndicatorTemplateXML() ) );
	}
	
	public Document getIndicatorTemplateDocument() {
		return indicatorTemplateDocument;
	}

	private void setIndicatorTemplateDocument( byte[] bytearray ) {
		this.indicatorTemplateDocument = byteToDocument( bytearray );
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


	public static final Document byteToDocument( final byte[] bytearray ) {
		
		Document document = null;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    // factory.setValidating(true);
	    try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			document = builder.parse( new ByteArrayInputStream( bytearray ) );
		} catch (ParserConfigurationException e) {
			logger.error("",e);
		} catch (SAXException e) {
			logger.error("",e);
		} catch (IOException e) {
			logger.error("",e);
		}
	    
	    return document;
	}

}

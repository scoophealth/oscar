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

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.eclipse.jdt.internal.core.Assert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.dashboard.query.RangeInterface;
import org.oscarehr.dashboard.query.RangeInterface.Limit;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;


public class IndicatorTemplateXMLTest {

	private static IndicatorTemplateXML indicatorTemplateXML;
	private static Document xmlDocument;
	
	@BeforeClass
	public static void setUpBeforeClass() {
		URL url = Thread.currentThread().getContextClassLoader().getResource("indicatorXMLTemplates/diabetes_hba1c_test.xml");
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    factory.setNamespaceAware(true);
	    
	    try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			xmlDocument = builder.parse( url.openStream() );
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
		indicatorTemplateXML = new IndicatorTemplateXML( xmlDocument );
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		indicatorTemplateXML = null;
		xmlDocument = null;
	}

	@Test
	public void testGetAuthor() {
		assertEquals( "Colcamex Resources Inc. Copyright 2016", indicatorTemplateXML.getAuthor() );
	}

	@Test
	public void testGetCategory() {
		assertEquals( "CDM", indicatorTemplateXML.getCategory() );
	}

	@Test
	public void testGetSubCategory() {
		assertEquals( "Diabetes", indicatorTemplateXML.getSubCategory() );
	}

	@Test
	public void testGetFramework() {
		assertEquals( "Based on and adapted from HQO's PCPM: Priority Measures for System and Practice Levels (Oct 2015)", indicatorTemplateXML.getFramework() );
	}

	@Test
	public void testGetFrameworkVersion() {
		assertEquals( "10-01-2015", indicatorTemplateXML.getFrameworkVersion() );
	}

	@Test
	public void testGetName() {
		assertEquals( "Diabetes with HbA1C Testing", indicatorTemplateXML.getName() );
	}

	@Test
	public void testGetDefinition() {
		assertEquals( "% of patients with diabetes aged 40 years and older who have had two or more HbA1C tests within the past 12 months", indicatorTemplateXML.getDefinition() );
	}

	@Test
	public void testGetNotes() {
		assertEquals( "This is a test template for the Diabetes with HbA1C Testing Indicator query", indicatorTemplateXML.getNotes() );
	}
	
	@Test
	public void testGetIndicatorQueryVersion() {
		assertEquals( "07-15-2016", indicatorTemplateXML.getIndicatorQueryVersion() );
	}

	@Test
	public void testGetIndicatorQuery() {
		Assert.isNotNull(indicatorTemplateXML.getIndicatorQuery());
	}

	@Test
	public void testGetIndicatorParameters() {
		assertEquals( "provider", indicatorTemplateXML.getIndicatorParameters().get(0).getId() );
	}
	
	@Test
	public void testGetIndicatorParametersSize() {
		assertEquals(3, indicatorTemplateXML.getIndicatorParameters().size() );
	}

	@Test
	public void testGetDrillDownQuery() {
		Assert.isNotNull( indicatorTemplateXML.getDrilldownQuery() );
	}
	
	@Test
	public void testGetDrilldownQueryVersion() {
		assertEquals( "07-20-2016", indicatorTemplateXML.getDrilldownQueryVersion() );
	}
	
	@Test
	public void testGetDrilldownParameters() {
		assertEquals( "provider", indicatorTemplateXML.getDrilldownParameters().get(0).getId() );
	}
	
	@Test
	public void testGetDrilldownParametersSize() {
		assertEquals(3, indicatorTemplateXML.getDrilldownParameters().size() );
	}
	
	@Test
	public void testGetDrilldownDisplayColumns() {
		assertEquals( "demographic", indicatorTemplateXML.getDrilldownDisplayColumns().get(0).getId() );
	}
	
	@Test
	public void testGetDrilldownDisplayColumnsSize() {
		assertEquals( 5, indicatorTemplateXML.getDrilldownDisplayColumns().size() );
	}
	
	@Test
	public void testGetDrilldownExportColumns() {
		assertEquals( "lastName", indicatorTemplateXML.getDrilldownExportColumns().get(2).getId() );
	}
	
	@Test
	public void testGetDrilldownExportColumnsSize() {
		assertEquals( 5, indicatorTemplateXML.getDrilldownExportColumns().size() );
	}
	
	@Test
	public void testGetIndicatorRanges() {
		Boolean verify = Boolean.FALSE;
		for( RangeInterface range : indicatorTemplateXML.getIndicatorRanges() ) {
			if( "a1c".equals( range.getId() ) ) {
				verify = Boolean.TRUE;
			}
		}
		assertTrue( verify );
	}
	
	@Test
	public void testGetIndicatorRangesSize() {
		assertEquals(  5, indicatorTemplateXML.getIndicatorRanges().size() );
	}
	
	@Test
	public void testGetIndicatorRangesUpperLimit() {
		Boolean verify = Boolean.FALSE;
		for( RangeInterface range : indicatorTemplateXML.getIndicatorRanges() ) {		
			if( Limit.RangeUpperLimit.name().equals( range.getClass().getSimpleName() ) ) {
				verify = Boolean.TRUE;
			}
		}
		assertTrue( verify );
	}
	
	@Test
	public void testGetIndicatorRangesLowerLimit() {		
		Boolean verify = Boolean.FALSE;
		for( RangeInterface range : indicatorTemplateXML.getIndicatorRanges() ) {		
			if( Limit.RangeLowerLimit.name().equals( range.getClass().getSimpleName() ) ) {
				verify = Boolean.TRUE;
			}
		}
		assertTrue( verify );
	}


}

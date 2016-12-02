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

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.dashboard.display.beans.GraphPlot;
import org.oscarehr.dashboard.query.Parameter;
import org.oscarehr.dashboard.query.RangeInterface;

/**
 * 
 * Cannot automate this test because the EntityManager is called 
 * through spring utils.
 *
 */
public class IndicatorQueryHandlerTest {

	private static String query = "SELECT COUNT(fin.patient) AS \"DM Patients\", IF ( COUNT(fin.patient) > 0, "
			+ "ROUND( SUM( CASE WHEN fin.a1c > 2 THEN 1 ELSE 0 END ) * 100 / COUNT(fin.patient) , 1 ), 0) AS \"HbA1c (%)\", "
			+ "IF ( COUNT(fin.patient) > 0, ROUND( SUM( CASE WHEN fin.a1c > 0 THEN 1 ELSE 0 END )  * 100 / "
			+ "COUNT(fin.patient) , 1 ), 0) AS \"HbA1c 2x (%)\" FROM ( SELECT d.demographic_no AS patient, "
			+ "A1C.a1cnumber AS a1c, A1C9.a1c9number AS a1c9 FROM demographic d INNER JOIN dxresearch dxr "
			+ "ON ( d.demographic_no = dxr.demographic_no) LEFT JOIN ( SELECT COUNT(*) AS a1cnumber, demographicNo "
			+ "FROM measurements WHERE type LIKE \"A1C\" AND ( DATE(dateObserved) BETWEEN DATE('12-12-2012') AND now() ) "
			+ "AND demographicNo > 0 AND providerNo LIKE '' GROUP BY demographicNo HAVING COUNT(demographicNo) > -1 ) "
			+ "A1C ON (d.demographic_no = A1C.demographicNo) LEFT JOIN ( SELECT COUNT(*) AS 'a1c9number', demographicNo "
			+ "FROM measurements WHERE type LIKE \"A1C\" AND demographicNo > 0 GROUP BY demographicNo "
			+ "HAVING COUNT(demographicNo) > -1 ) A1C9 ON (d.demographic_no = A1C9.demographicNo) "
			+ "WHERE d.patient_status LIKE \"%AC%\" AND dxr.coding_system LIKE \"icd9\" AND dxr.dxresearch_code "
			+ "LIKE \"250\" AND dxr.status NOT LIKE \"%D%\" AND d.demographic_no > 0 AND d.last_name LIKE 'test' AND "
			+ "( 30 <= ROUND( ABS( DATEDIFF( DATE( CONCAT(d.year_of_birth,\"-\",d.month_of_birth,\"-\",d.date_of_birth) ), "
			+ "DATE( now() ) ) / 365.25 ) ) AND 75 >= ROUND( ABS( DATEDIFF( "
			+ "DATE( CONCAT(d.year_of_birth,\"-\",d.month_of_birth,\"-\",d.date_of_birth) ), DATE( now() ) ) / 365.25 ) ) ) ) fin;";
	
	private static List<Parameter> parameters;
	private static List<RangeInterface> ranges;
	private static IndicatorQueryHandler indicatorQueryHandler;
	private static String altQueryString;
	private static List< GraphPlot[] > graphPlotList;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("indicatorXMLTemplates/diabetes_hba1c_in_range_test.xml");
		byte[] byteArray = null;
		InputStream is = null;
		try {
			is = url.openStream();
			byteArray = IOUtils.toByteArray(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	    // test new handler with a byte array feed.
	    IndicatorTemplateHandler indicatorTemplateHandler = new IndicatorTemplateHandler(byteArray);
	    IndicatorTemplateXML indicatorTemplateXML = indicatorTemplateHandler.getIndicatorTemplateXML();
	    String queryString = indicatorTemplateXML.getIndicatorQuery();

		parameters = indicatorTemplateXML.getIndicatorParameters();
		ranges = indicatorTemplateXML.getIndicatorRanges();
		
		indicatorQueryHandler = new IndicatorQueryHandler();
		
		altQueryString = indicatorQueryHandler.filterQueryString( queryString );
		altQueryString = indicatorQueryHandler.addParameters( parameters, altQueryString );
		altQueryString = indicatorQueryHandler.addRanges( ranges, altQueryString );
		
		indicatorQueryHandler.setParameters( parameters );
		indicatorQueryHandler.setRanges( ranges );
		indicatorQueryHandler.setQuery( queryString );

		List<Object> results = new ArrayList<Object>();
		HashMap<Object, Object> resultmap = new HashMap<Object, Object>();
		resultmap.put( "", 1);
		resultmap.put( "dennis", 2 );
		resultmap.put( "key", 3 );
		resultmap.put( "Unit", 4 );
		resultmap.put( "DOUBLE", 5 );
		/*
		HashMap<Object, Object> resultmap1 = new HashMap<Object, Object>();
		resultmap1.put("count", null );
		resultmap1.put( null, "resources" );
		resultmap1.put( "Date", 2 );
		resultmap1.put( "zero", 0.00 );
		resultmap1.put( "big number", new BigInteger("9") );
		*/
		results.add(resultmap);
		//results.add(resultmap1);
		
		graphPlotList = IndicatorQueryHandler.createGraphPlots( results );
	}

	@Test
	public void testGetParameters() {		
		assertEquals(parameters, indicatorQueryHandler.getParameters());
	}

	@Test
	public void testGetQuery() {
		assertEquals(query, indicatorQueryHandler.getQuery());
	}

	@Test
	public void testGetRanges() {
		assertEquals( ranges, indicatorQueryHandler.getRanges() );
	}
	
	@Test
	public void testAltQueryString() {
		assertEquals( query, altQueryString );
	}
	
	@Test
	public void testGetGraphPlots() {
		Double total = 0.0;		
		for( GraphPlot[] plot : graphPlotList ) {
			Double subtotal = 0.0;
			for( GraphPlot graphPlot : plot ) {
				subtotal = ( subtotal + graphPlot.getNumerator() );
			}
			total = ( total + subtotal );
		}

		assertEquals( new Double(14), total );
	}

}

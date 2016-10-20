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
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.dashboard.query.Column;
import org.oscarehr.dashboard.query.Parameter;
import org.oscarehr.dashboard.query.RangeInterface;

public class DrilldownQueryHandlerTest {

	private static List<Column> columns;
	private static List<Parameter> parameters;
	private static List<RangeInterface> ranges;
	private static DrilldownQueryHandler drilldownQueryHandler;

	private static String finalQueryString = "SELECT d.demographic_no AS 'Patient Id',d.first_name "
			+ "AS 'First Name',d.last_name AS 'Last Name',CONCAT( month_of_birth, '-', date_of_birth, '-', year_of_birth ) "
			+ "AS 'Date of Birth',a1c9number AS 'A1C 9 Months' FROM demographic d INNER JOIN dxresearch dxr "
			+ "ON ( d.demographic_no = dxr.demographic_no) LEFT JOIN ( SELECT COUNT(*) "
			+ "AS a1cnumber, demographicNo FROM measurements WHERE type LIKE \"A1C\" "
			+ "AND ( DATE(dateObserved) BETWEEN DATE('12-12-2012') AND now() ) AND demographicNo > 0 "
			+ "AND providerNo LIKE '' GROUP BY demographicNo HAVING COUNT(demographicNo) > -1 ) A1C "
			+ "ON (d.demographic_no = A1C.demographicNo) LEFT JOIN ( SELECT COUNT(*) AS 'a1c9number', demographicNo "
			+ "FROM measurements WHERE type LIKE \"A1C\" AND demographicNo > 0 GROUP BY demographicNo "
			+ "HAVING COUNT(demographicNo) > -1 ) A1C9 ON (d.demographic_no = A1C9.demographicNo) "
			+ "WHERE d.patient_status LIKE \"%AC%\" AND dxr.coding_system LIKE \"icd9\" AND dxr.dxresearch_code "
			+ "IN ('250','430') AND dxr.status NOT LIKE \"%D%\" AND d.demographic_no > 0 AND d.last_name LIKE 'test' "
			+ "AND ( 30 <= ROUND( ABS( DATEDIFF( DATE( CONCAT(d.year_of_birth,\"-\",d.month_of_birth,\"-\",d.date_of_birth) ), "
			+ "DATE( now() ) ) / 365.25 ) ) AND 75 >= ROUND( ABS( DATEDIFF( DATE( CONCAT(d.year_of_birth,\"-\",d.month_of_birth,\"-\",d.date_of_birth) ), "
			+ "DATE( now() ) ) / 365.25 ) ) )";

	@BeforeClass
	public static void setUpBeforeClass() {
		
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
		
		columns = indicatorTemplateXML.getDrilldownDisplayColumns();
		ranges = indicatorTemplateXML.getDrilldownRanges();
		parameters = indicatorTemplateXML.getDrilldownParameters();
		
		drilldownQueryHandler = new DrilldownQueryHandler();
		drilldownQueryHandler.setColumns(columns);
		drilldownQueryHandler.setParameters(parameters);
		drilldownQueryHandler.setRanges(ranges);
		drilldownQueryHandler.setQuery( indicatorTemplateXML.getDrilldownQuery() );
		
	}

	@Test
	public void testGetColumns() {
		assertEquals( columns, drilldownQueryHandler.getColumns() );
	}
	
	@Test
	public void testGetRanges() {
		assertEquals( ranges, drilldownQueryHandler.getRanges() );
	}

	@Test
	public void testGetQuery() {
		assertEquals( finalQueryString, drilldownQueryHandler.getQuery() );
	}

}

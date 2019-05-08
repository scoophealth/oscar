/**
 * Copyright (c) 2015-2019. The Pharmacists Clinic, Faculty of Pharmaceutical Sciences, University of British Columbia. All Rights Reserved.
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
 * The Pharmacists Clinic
 * Faculty of Pharmaceutical Sciences
 * University of British Columbia
 * Vancouver, British Columbia, Canada
 */
package oscar.form.pharmaForms.formBPMH.pdf;


import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.oscarehr.common.model.Demographic;

import oscar.form.pharmaForms.formBPMH.bean.BpmhDrug;
import oscar.form.pharmaForms.formBPMH.bean.BpmhFormBean;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */

public class PDFControllerTest {

	private static PDFController pdfController;
	private static BpmhFormBean data;
	private static Demographic demographic;
	private static BpmhDrug bpmhDrug1;
	private static URL url;
	
	@Before
	public void setUp() {
		ClassLoader loader = PDFController.class.getClassLoader();
		url = loader.getResource("oscar/form/prop/bpmh_template_marked.pdf");

		pdfController = new PDFController(url.getPath());			
		pdfController.setOutputPath("/var/lib/tomcat6");
		
		demographic = new Demographic();
		demographic.setDemographicNo(12345);
		demographic.setFirstName("Dennis");
		demographic.setLastName("Warren");
		demographic.setHin("9374636728674");
		demographic.setEffDate(new Date());
		
		bpmhDrug1 = new BpmhDrug();
		bpmhDrug1.setGenericName("GENERIC DRUG");
		bpmhDrug1.setWhy("This is a description.");
		bpmhDrug1.setWhat("chicken butt");
		
		BpmhDrug bpmhDrug2 = new BpmhDrug();		
		bpmhDrug2.setGenericName("DRUG NAME");
		bpmhDrug2.setWhy("take this drug daily");
		
		List<BpmhDrug> bpmhDrugList = new ArrayList<BpmhDrug>();
		bpmhDrugList.add(bpmhDrug1);
		bpmhDrugList.add(bpmhDrug2);
		
		data = new BpmhFormBean();
		data.setDemographicNo("2345");
		data.setFamilyDrName("Dr. Who");
		data.setDemographic(demographic);
		data.setDrugs(bpmhDrugList);
	}
	
	@After
	public void tearDown() {
		pdfController = null;
		data = null;
		demographic = null;
		bpmhDrug1 = null;
		url = null;
	}

	@Test
	public void testGetOutput() {
		assertEquals( "/var/lib/tomcat6", pdfController.getOutputPath() );
	}
	
	@Test
	public void testGetInput() {
		assertEquals("bpmh_template_marked.pdf", pdfController.getFilePath().getName());
	}
	
	@Test
	public void testWriteDataToPDF() {
		pdfController.writeDataToPDF(data, new String[]{"1"}, "6789");
	}
	
	@Test
	public void testGetGetterMethods() {
		pdfController.setDataObject(demographic);
		assertEquals("Dennis",pdfController.invokeValue("firstName"));
		assertEquals("9374636728674",pdfController.invokeValue("hin"));
	}
	
	@Test
	public void testGetGetterMethodsWithMissingValue() {
		
		Map<String,Method> getterMethods = PDFController.getGetterMethods(data);
		
		assertEquals(null, PDFController.invokeValue("democratic.fakemethod", getterMethods, data));
		assertEquals(null, PDFController.invokeValue("fakemethod", getterMethods, data));
		
	}
	
	@Test
	public void testGetGetterMethodsWithListDataTypes() {
//		Map<String,Method> getterMethods = PDFController.getGetterMethods(data);
		pdfController.setDataObject(data);
		assertEquals( "GENERIC DRUG" , pdfController.invokeValue("drugs#0.genericName") );
		assertEquals( "DRUG NAME" , pdfController.invokeValue("drugs#1.genericName") );
		assertEquals( "take this drug daily" , pdfController.invokeValue("drugs#1.why") );
		
//		Iterator<String> it = getterMethods.keySet().iterator();
//		
//		while(it.hasNext()) {

//		}

	}

}

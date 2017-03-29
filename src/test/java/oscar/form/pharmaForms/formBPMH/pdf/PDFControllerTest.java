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
package oscar.form.pharmaForms.formBPMH.pdf;


import static org.junit.Assert.*;

//import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
//import java.util.Map;



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

		PDFControllerConfig config = new PDFControllerConfig();
		config.setTargetBeans( new String[]{"org.oscarehr.common.model", "oscar.form.pharmaForms.formBPMH.bean"} );
		config.setJavaScript( new String[]{ "this.print({bUI: true, bSilent: true, bShrinkToFit:true});", "this.closeDoc(true);" } );
		config.addTableRowLimit("drugs", 10, 2);
		config.addTextBoxLineLimits("note", 10, 2);
		config.addTextLengthLimits("note", 1250, 2);
		config.addTextBoxLineLimits("note2", 10, 3);
		config.addTextLengthLimits("note2", 1250, 3);
		
		pdfController = new PDFController(url, config);			
		pdfController.setOutputPath("/var/lib/tomcat6");
		pdfController.setFileName("bpmh_test");
		
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
		
		BpmhDrug bpmhDrug3 = new BpmhDrug();		
		bpmhDrug3.setGenericName("DRUG NAME");
		bpmhDrug3.setWhy("take this drug daily");
		
		BpmhDrug bpmhDrug4 = new BpmhDrug();		
		bpmhDrug4.setGenericName("DRUG NAME");
		bpmhDrug4.setWhy("take this drug daily");
		
		List<BpmhDrug> bpmhDrugList = new ArrayList<BpmhDrug>();
		bpmhDrugList.add(bpmhDrug1);
		bpmhDrugList.add(bpmhDrug2);
		bpmhDrugList.add(bpmhDrug3);
		bpmhDrugList.add(bpmhDrug4);
		
		data = new BpmhFormBean();
		data.setDemographicNo("2345");
		data.setFamilyDrName("Dr. Who");
		data.setDemographic(demographic);
		data.setDrugs(bpmhDrugList);
		data.setNote(
				" 1 \n " +
				"2 \n " +
				"3\n " +
				"4\n " +
				"5\n " +
				"6\n " +
				"7\n " +
				"8\n " +
				"9\n " +
				"10\n " +
				"11\n " +
				"12\n " +
				"13\n " +
				"14\n " +
				"15"
				);
		
		
		
//		data.setNote(
//				 "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse iaculis tempor rutrum. Quisque nisl elit, pretium ut efficitur vel, convallis non metus. Nam blandit mattis orci. Proin dictum consequat ante, vel commodo enim maximus non. Morbi ut nisl imperdiet, iaculis enim non, convallis ligula. Phasellus nec sodales elit, nec dictum nisl. Aliquam erat volutpat. Curabitur ut bibendum quam. Sed ultricies pellentesque gravida. Pellentesque bibendum lorem nisl, ut lobortis risus luctus sed."
//				 + " Morbi porta, mi quis egestas euismod, quam risus accumsan ante, eget vulputate risus nulla vitae diam. Vestibulum imperdiet quam sed velit consequat sagittis. Morbi finibus felis nec enim fermentum, in consequat ipsum ultricies. Nam molestie ipsum ut leo fringilla fringilla. Morbi ultrices, metus nec tristique fermentum, nunc dolor aliquet dolor, ac eleifend mi ante vitae lorem. Pellentesque enim eros, iaculis tempus lectus eget, commodo interdum massa. Pellentesque eleifend tortor et eros feugiat dapibus. Duis ac erat id erat varius tempus. Integer accumsan, ante a aliquet malesuada, turpis tortor mollis risus, ac vehicula urna orci a lorem. Aenean orci ante, gravida at tempus non, auctor a ex. Suspendisse elementum magna nisl, sit amet lacinia augue aliquam et. Suspendisse ex mauris, ultrices nec cursus sit amet, maximus eget ipsum. Etiam tempor finibus finibus. In nec lacus ac ipsum blandit malesuada ut ac ligula. Donec pharetra ultricies fringilla. Vestibulum sed dui eget ex venenatis aliquam at at quam."
//				 + " Praesent varius ac neque feugiat luctus. Nunc vitae blandit risus, non faucibus purus. Suspendisse potenti. Aenean et ultricies arcu. Mauris id tellus sed nulla egestas placerat. Etiam elit mauris, rhoncus non rutrum ut, vulputate at eros. Cras lacinia volutpat interdum. Praesent eget volutpat eros. Phasellus tincidunt id turpis sit amet suscipit."
//				 + " Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse iaculis tempor rutrum. Quisque nisl elit, pretium ut efficitur vel, convallis non metus. Nam blandit mattis orci. Proin dictum consequat ante, vel commodo enim maximus non. Morbi ut nisl imperdiet, iaculis enim non, convallis ligula. Phasellus nec sodales elit, nec dictum nisl. Aliquam erat volutpat. Curabitur ut bibendum quam. Sed ultricies pellentesque gravida. Pellentesque bibendum lorem nisl, ut lobortis risus luctus sed."
//				 + " Morbi porta, mi quis egestas euismod, quam risus accumsan ante, eget vulputate risus nulla vitae diam. Vestibulum imperdiet quam sed velit consequat sagittis. Morbi finibus felis nec enim fermentum, in consequat ipsum ultricies. Nam molestie ipsum ut leo fringilla fringilla. Morbi ultrices, metus nec tristique fermentum, nunc dolor aliquet dolor, ac eleifend mi ante vitae lorem. Pellentesque enim eros, iaculis tempus lectus eget, commodo interdum massa. Pellentesque eleifend tortor et eros feugiat dapibus. Duis ac erat id erat varius tempus. Integer accumsan, ante a aliquet malesuada, turpis tortor mollis risus, ac vehicula urna orci a lorem. Aenean orci ante, gravida at tempus non, auctor a ex. Suspendisse elementum magna nisl, sit amet lacinia augue aliquam et. Suspendisse ex mauris, ultrices nec cursus sit amet, maximus eget ipsum. Etiam tempor finibus finibus. In nec lacus ac ipsum blandit malesuada ut ac ligula. Donec pharetra ultricies fringilla. Vestibulum sed dui eget ex venenatis aliquam at at quam."
//				 + " Praesent varius ac neque feugiat luctus. Nunc vitae blandit risus, non faucibus purus. Suspendisse potenti. Aenean et ultricies arcu. Mauris id tellus sed nulla egestas placerat. Etiam elit mauris, rhoncus non rutrum ut, vulputate at eros. Cras lacinia volutpat interdum. Praesent eget volutpat eros. Phasellus tincidunt id turpis sit amet suscipit." 
//		);
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
	public void testGetFileName() {
		assertEquals("bpmh_test", pdfController.getFileName());
	}
	
	@Test
	public void testWriteDataToPDF() {		

		pdfController.writeDataToPDF(data, new String[]{"1"}, "6789");
	}
	
	
	public void testGetGetterMethods() {
		pdfController.setDataObject(demographic);
		assertEquals("Dennis",pdfController.invokeValue("firstName"));
		assertEquals("9374636728674",pdfController.invokeValue("hin"));
	}
	
//	@Test uncomment and change method PDFController.invokeValue to public or protected to test
//	public void testGetGetterMethodsWithMissingValue() {
//		
//		Map<String,Method> getterMethods = PDFController.getGetterMethods(data);
//		
//		assertEquals(null, PDFController.invokeValue("democratic.fakemethod", getterMethods, data));
//		assertEquals(null, PDFController.invokeValue("fakemethod", getterMethods, data));
//		
//	}
	
	
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
	
	
	public void testPrintSmartTagsToPDF() {
		pdfController.printSmartTagsToPDF( new String[]{"1,2,3"} );
	} 

}

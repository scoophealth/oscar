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
package oscar.form.pharmaForms.formBPMH.util;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.oscarehr.common.model.Demographic;

import oscar.form.pharmaForms.formBPMH.bean.BpmhDrug;

/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */
public class JsonUtilTest {

	private static Demographic demographic;
	private static BpmhDrug bpmhDrug1;
	private static BpmhDrug bpmhDrug2;
	private static List<BpmhDrug> bpmhDrugList;
	private static String jsonListString = "[{\"atc\":\"\",\"code\":\"\",\"codingSystem\":\"\",\"comment\":\"\",\"comments\":\"\",\"customInstructions\":\"\",\"customName\":\"\",\"customNote\":\"\",\"demographicId\":\"\",\"dosage\":\"\",\"drugForm\":\"\",\"durUnit\":\"\",\"duration\":\"\",\"freqCode\":\"\",\"genericName\":\"GENERIC DRUG\",\"how\":\"this is how one\",\"id\":\"\",\"instruction\":\"\",\"method\":\"\",\"outsideProviderName\":\"\",\"outsideProviderOhip\":\"\",\"position\":\"\",\"prn\":\"\",\"quantity\":\"\",\"repeat\":\"\",\"route\":\"\",\"special\":\"\",\"special_instruction\":\"\",\"takeMax\":\"\",\"takeMin\":\"\",\"unit\":\"\",\"what\":\"GENERIC DRUG \",\"why\":\"This is a description.\"},{\"atc\":\"\",\"code\":\"\",\"codingSystem\":\"\",\"comment\":\"\",\"comments\":\"\",\"customInstructions\":\"\",\"customName\":\"\",\"customNote\":\"\",\"demographicId\":\"\",\"dosage\":\"\",\"drugForm\":\"\",\"durUnit\":\"\",\"duration\":\"\",\"freqCode\":\"\",\"genericName\":\"DRUG NAME\",\"how\":\"this is how two\",\"id\":\"\",\"instruction\":\"\",\"method\":\"\",\"outsideProviderName\":\"\",\"outsideProviderOhip\":\"\",\"position\":\"\",\"prn\":\"\",\"quantity\":\"\",\"repeat\":\"\",\"route\":\"\",\"special\":\"\",\"special_instruction\":\"\",\"takeMax\":\"\",\"takeMin\":\"\",\"unit\":\"\",\"what\":\"DRUG NAME \",\"why\":\"take this drug daily\"}]";
	private static String jsonString = "{\"atc\":\"\",\"code\":\"\",\"codingSystem\":\"\",\"comment\":\"\",\"comments\":\"\",\"customInstructions\":\"\",\"customName\":\"\",\"customNote\":\"\",\"demographicId\":\"\",\"dosage\":\"\",\"drugForm\":\"\",\"durUnit\":\"\",\"duration\":\"\",\"freqCode\":\"\",\"genericName\":\"GENERIC DRUG\",\"how\":\"this is how one\",\"id\":\"\",\"instruction\":\"\",\"method\":\"\",\"outsideProviderName\":\"\",\"outsideProviderOhip\":\"\",\"position\":\"\",\"prn\":\"\",\"quantity\":\"\",\"repeat\":\"\",\"route\":\"\",\"special\":\"\",\"special_instruction\":\"\",\"takeMax\":\"\",\"takeMin\":\"\",\"unit\":\"\",\"what\":\"GENERIC DRUG \",\"why\":\"This is a description.\"}";
	private static String[] ignoreMethods = new String[] {"handler", "hibernateLazyInitializer", "hours", "minutes", "seconds"};
	
	@BeforeClass
	public static void setUpBeforeClass() {
		
		demographic = new Demographic();
		demographic.setDemographicNo(12345);
		demographic.setFirstName("Dennis");
		demographic.setLastName("Warren");
		demographic.setHin("9374636728674");
		demographic.setEffDate(new java.sql.Date(new Date().getTime()));
		demographic.setFamilyDoctor("<rd>Who, Doctor</rd><rdohip>973637</rdohip>");
		
		bpmhDrug1 = new BpmhDrug();
		bpmhDrug1.setGenericName("GENERIC DRUG");
		bpmhDrug1.setWhy("This is a description.");
		bpmhDrug1.setHow("this is how one");
		
		bpmhDrug2 = new BpmhDrug();		
		bpmhDrug2.setGenericName("DRUG NAME");
		bpmhDrug2.setWhy("take this drug daily");
		bpmhDrug2.setHow("this is how two");
		
		bpmhDrugList = new ArrayList<BpmhDrug>();
		bpmhDrugList.add(bpmhDrug1);
		bpmhDrugList.add(bpmhDrug2);
	}

	@Test
	public void testPojoToJson() {				
		assertNotNull( JsonUtil.pojoToJson(demographic, ignoreMethods) );
	}
	
	@Test
	public void testPojoListToJson() {
		assertNotNull( JsonUtil.pojoCollectionToJson(bpmhDrugList, ignoreMethods) );
	}
	
	
    @Test
    @SuppressWarnings("unchecked")
	public void testJsonToPojoList() {
		List<BpmhDrug> drugList =  (List<BpmhDrug>) JsonUtil.jsonToPojoList(jsonListString, BpmhDrug.class);

		assertEquals("GENERIC DRUG", drugList.get(0).getGenericName());
		assertEquals("take this drug daily",drugList.get(1).getWhy());
		assertEquals("this is how one",drugList.get(0).getHow());

	}
	
	@Test
	public void testJsonToPojo() {
		BpmhDrug drug = (BpmhDrug) JsonUtil.jsonToPojo(jsonString, BpmhDrug.class);
		assertEquals("GENERIC DRUG",drug.getGenericName());
		assertEquals("This is a description.",drug.getWhy());
		assertEquals("this is how one",drug.getHow());
	}

}

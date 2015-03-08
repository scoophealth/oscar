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
package oscar.form.pharmaForms.formBPMH.bean;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
/*
 * Author: Dennis Warren 
 * Company: Colcamex Resources
 * Date: November 2014
 * For: UBC Pharmacy Clinic and McMaster Department of Family Medicine
 */
public class BpmhDrugTest {

	private static BpmhDrug drug;

	@Before
	public void setUp() {
		drug = new BpmhDrug();
	}

	@Test
	public void testWhy() {				
		drug.setWhy("this is a drug description.");		
		assertEquals("this is a drug description.", drug.getWhy());
	}
	
	@Test
	public void testWhyNull() {
		drug.setWhy(null);		
		assertEquals("", drug.getWhy());
	}
	
	@Test
	public void testWhat() {
		
		drug.setDosage("25");
		drug.setUnit("MG");
		drug.setGenericName("WARFARIN SODIUM");
		drug.setDrugForm("Tablet");
		
		assertEquals("WARFARIN SODIUM 25 MG Tablet", drug.getWhat());
	}
	
	@Test
	public void testWhatAlt() {
		drug.setDosage("25");
		drug.setUnit("MG");
		drug.setGenericName("WARFARIN SODIUM 25 MG");
		drug.setDrugForm("Tablet");
		
		assertEquals("WARFARIN SODIUM 25 MG Tablet", drug.getWhat());
	}
	
	@Test
	public void testWhatAltWithUnit() {
		drug.setDosage("25 MG");
		drug.setUnit("MG");
		drug.setGenericName("WARFARIN SODIUM 25 MG");
		drug.setDrugForm("Tablet");
		
		assertEquals("WARFARIN SODIUM 25 MG Tablet", drug.getWhat());
	}
	
	@Test
	public void testWhatAltNameWithoutDose() {
		drug.setDosage("25 MG");
		drug.setUnit("MG");
		drug.setGenericName("WARFARIN SODIUM");
		drug.setDrugForm("Tablet");
		
		assertEquals("WARFARIN SODIUM 25 MG Tablet", drug.getWhat());
	}
	
	@Test
	public void testWhatCustomDrug() {
		drug.setDosage("25");
		drug.setUnit("MG");
		drug.setGenericName("");
		drug.setDrugForm("Tablet");
		drug.setCustomName("VITAMIN B");
		
		assertEquals("VITAMIN B 25 MG Tablet", drug.getWhat());
	}
	
	@Test
	public void testHowSpecial() {
		drug.setSpecial("AMOXIL 250 CAP\n" +
		"Take 1 Tabs PO OD for 30Days\n" +
		"Qty:30 Repeats:0");
		
		assertEquals("Take 1 Tabs PO OD for 30Days", drug.getHow());
	}
	
	@Test
	public void testHowAlternate() {
		drug.setSpecial("AMOXIL 250 CAP");
		
		drug.setMethod("Take");
		drug.setTakeMin("1");
		drug.setFreqCode("OD");
		drug.setDrugForm("Tablet");
		drug.setDurUnit("D");
		drug.setDuration("30");
		drug.setRoute("PO");
		
		assertEquals("Take 1 Tablet OD PO ", drug.getHow());
	}
	
	@Test
	public void testInstruction() {
		drug.setInstruction("this is a temporary drug");		
		assertEquals("this is a temporary drug", drug.getInstruction());
	}

}

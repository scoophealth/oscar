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
package org.oscarehr.research.eaaps;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import oscar.OscarProperties;

public class EaapsMessageSupportTest {

	
	@Before
	public void beforeTest() {
		OscarProperties props = OscarProperties.getInstance();
		props.setProperty("DOCUMENT_DIR", System.getProperty("buildDirectory"));
	}
	
	@Test
	public void test() throws Exception {
		EaapsMessageSupport support = new EaapsMessageSupport();
		support.init(EaapsIntegrationTest.HL7);
		if (EaapsIntegrationTest.HASH == null || EaapsIntegrationTest.HASH.isEmpty()) {
			assertEquals(null, support.getDemographicHash());
		} else {
			assertEquals(EaapsIntegrationTest.HASH, support.getDemographicHash());
		}
		if (EaapsIntegrationTest.PROVIDER_ID == null || EaapsIntegrationTest.PROVIDER_ID.isEmpty()) {
			assertEquals(null, support.getOrderingProvider());
		} else {
			assertEquals(EaapsIntegrationTest.PROVIDER_ID, support.getOrderingProvider());
		}
		assertNotNull(support.getPdf());
		assertTrue(support.getPdfFileName().startsWith("eaaps_"));
		String providerNote = support.getProviderNote(); 
		assertTrue(providerNote.startsWith("MRP message for message with the AAP attachment"));
		String recs = support.getRecommendations();
		assertTrue(recs.startsWith("Note comment for message with the AAP attachment"));
		assertEquals("SENDING APP", support.getSourceFacility());
		
		support = new EaapsMessageSupport();
		support.init(EaapsIntegrationTest.PDF_AND_MRP_NTE1AND3);
		if (EaapsIntegrationTest.HASH == null || EaapsIntegrationTest.HASH.isEmpty()) {
			assertEquals(null, support.getDemographicHash());
		} else {
			assertEquals(EaapsIntegrationTest.HASH, support.getDemographicHash());
		}
		if (EaapsIntegrationTest.PROVIDER_ID == null || EaapsIntegrationTest.PROVIDER_ID.isEmpty()) {
			assertEquals(null, support.getOrderingProvider());
		} else {
			assertEquals(EaapsIntegrationTest.PROVIDER_ID, support.getOrderingProvider());
		}
		assertNotNull(support.getPdf());
		assertTrue(support.getPdfFileName().startsWith("eaaps_"));
		providerNote = support.getProviderNote(); 
		assertTrue(providerNote.equals("PDF AND MRP MESSAGE ONLY "));
		recs = support.getRecommendations();
		assertEquals(null, recs);
		assertEquals("SENDING APP", support.getSourceFacility());
		
		support = new EaapsMessageSupport();
		support.init(EaapsIntegrationTest.CHART_NOTE_AND_MRP_NTE2AND3);
		if (EaapsIntegrationTest.HASH == null || EaapsIntegrationTest.HASH.isEmpty()) {
			assertEquals(null, support.getDemographicHash());
		} else {
			assertEquals(EaapsIntegrationTest.HASH, support.getDemographicHash());
		}
		if (!EaapsIntegrationTest.PROVIDER_ID.isEmpty()) {
			assertEquals(EaapsIntegrationTest.PROVIDER_ID, support.getOrderingProvider());
		}
		assertTrue(support.getPdf() == null);
		assertTrue(support.getPdfFileName() == null);
		providerNote = support.getProviderNote(); 
		assertTrue(providerNote.startsWith("NOTE AND MRP"));
		assertTrue(support.getRecommendations().startsWith("NOTE AND MRP"));
		assertEquals("SENDING APP", support.getSourceFacility());
	}
	
}

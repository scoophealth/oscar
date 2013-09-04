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

import org.junit.Test;

public class EaapsMessageSupportTest {

	
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
		support.init(EaapsIntegrationTest.HL7_EMPTY_MESSAGE);
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
		assertTrue(providerNote == null);
		recs = support.getRecommendations();
		assertTrue(recs.startsWith("Note comment for the message without MRP note"));
		assertEquals("SENDING APP", support.getSourceFacility());
		
		support = new EaapsMessageSupport();
		support.init(EaapsIntegrationTest.HL7_EMPTY_PDF);
		if (EaapsIntegrationTest.HASH == null || EaapsIntegrationTest.HASH.isEmpty()) {
			assertEquals(null, support.getDemographicHash());
		} else {
			assertEquals(EaapsIntegrationTest.HASH, support.getDemographicHash());
		}
		assertEquals("999998", support.getOrderingProvider());
		assertTrue(support.getPdf() == null);
		assertTrue(support.getPdfFileName() == null);
		providerNote = support.getProviderNote(); 
		assertTrue(providerNote.startsWith("MRP message only without AAP attachment"));
		assertTrue(support.getRecommendations() == null);
		assertEquals("SENDING APP", support.getSourceFacility());
	}
	
}

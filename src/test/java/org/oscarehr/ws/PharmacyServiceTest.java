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
package org.oscarehr.ws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.oscarehr.ws.rest.PharmacyService;
import org.oscarehr.ws.rest.to.OscarSearchResponse;
import org.oscarehr.ws.rest.to.model.PharmacyInfoTo1;

public class PharmacyServiceTest extends BaseRestServiceTest {
	
	@Test
	public void testCrud() {
		if (!enabled) {
			return;
		}
		
		PharmacyService service = getResource(PharmacyService.class);
		
		OscarSearchResponse<PharmacyInfoTo1> pharmacies = service.getPharmacies(0, 0);
		assertNotNull(pharmacies);
		
		PharmacyInfoTo1 info = new PharmacyInfoTo1();
		populate(info);
		info.setId(null);
		
		info = service.addPharmacy(info);
		assertNotNull(info.getId());
		
		service.updatePharmacy(info);
		service.removePharmacy(info.getId());
		
		PharmacyInfoTo1 infoCheck = service.getPharmacy(info.getId());
		assertTrue(infoCheck == null || infoCheck.getStatus().equals("0"));
	}
	
}

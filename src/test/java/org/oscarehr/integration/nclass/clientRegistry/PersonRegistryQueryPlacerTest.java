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
package org.oscarehr.integration.nclass.clientRegistry;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.Calendar;

import org.junit.Test;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.integration.nclass.clientRegistry.impl.PlaceholderPersonRegistryQueryPlacer;
import org.oscarehr.integration.nclass.clientRegistry.model.Candidate;
import org.oscarehr.integration.nclass.clientRegistry.model.PersonDemographics;

public class PersonRegistryQueryPlacerTest {

	private PersonRegistryQueryPlacer placer = new PlaceholderPersonRegistryQueryPlacer();

	@Test
	public void testFindCandidate() {
		Demographic demo = new Demographic();
		demo.setFirstName("John");
		demo.setLastName("Doe");
		demo.setBirthDay(Calendar.getInstance());

		Candidate candidate = placer.findCandidate(null, demo);
		assertNotNull(candidate);
		assertNotNull(candidate.getFirst());
		assertNotNull(candidate.getLast());
		assertFalse(candidate.getIds().isEmpty());
	}
	
	@Test
	public void testGetPersonDemographics() {
		Demographic demo = new Demographic();
		demo.setFirstName("John");
		demo.setLastName("Doe");
		demo.setBirthDay(Calendar.getInstance());

		Candidate candidate = placer.findCandidate(null, demo);
		PersonDemographics personDemographics = placer.getPersonDemographics(candidate);
		assertNotNull(personDemographics);
	}
	
	

}

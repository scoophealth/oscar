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

import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Provider;
import org.oscarehr.integration.nclass.ReferencedType;
import org.oscarehr.integration.nclass.Storyboard;
import org.oscarehr.integration.nclass.clientRegistry.model.Candidate;
import org.oscarehr.integration.nclass.clientRegistry.model.PersonDemographics;

/**
 * Enables the source system to place find candidate requests to the client registry.
 */
@Storyboard("PRPA_ST101311CA")
@ReferencedType("PRPA_AR101101CA")
public interface PersonRegistryQueryPlacer {

	/**
	 * Searches for candidates matching the provided demographic.
	 * 
	 * @param demographic
	 * 		Demographic to find candidate records for.
	 * @return
	 * 		Returns the list of matching registry entries.
	 */
	public Candidate findCandidate(Provider provider, Demographic demographic);

	/**
	 * Finds person details based on the client ID 
	 * 
	 * @param demographic
	 * 		Demographic to find details for
	 * @return
	 * 		Returns the details or null if it's not available
	 */
	public PersonDemographics getPersonDemographics(Candidate demographic);

}

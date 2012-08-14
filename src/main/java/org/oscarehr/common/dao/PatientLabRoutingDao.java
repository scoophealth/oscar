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

package org.oscarehr.common.dao;

import javax.persistence.Query;

import org.oscarehr.common.model.PatientLabRouting;
import org.springframework.stereotype.Repository;

@Repository
public class PatientLabRoutingDao extends AbstractDao<PatientLabRouting> {

	public static final String HL7 = "HL7";

	public PatientLabRoutingDao() {
		super(PatientLabRouting.class);
	}

	/**
	 * Finds routing record containing reference to the demographic record with the 
	 * specified lab results reference number of {@link #HL7} lab type. 
	 * 
	 * LabId is also refereed to as Lab_no, and segmentId.
	 */
	public PatientLabRouting findDemographicByLabId(Integer labId) {
		return findDemographics(HL7, labId);
	}

	/**
	 * Finds routing record containing reference to the demographic record with the 
	 * specified lab type and lab results reference number. 
	 * 
	 * @param labType
	 * 		Type of the lab record to look up
	 * @param labNo
	 * 		Number of the lab record to look up
	 * @return
	 * 		Returns the container pointing to the demographics or null of no matching container is found.
	 */
	public PatientLabRouting findDemographics(String labType, Integer labNo) {
		Query query = entityManager.createQuery("FROM " + modelClass.getSimpleName() + " AS r WHERE r.labType = :labType AND r.labNumber = :labNo");
		query.setParameter("labType", labType);
		query.setParameter("labNo", labNo);
		return getSingleResultOrNull(query);
	}

}

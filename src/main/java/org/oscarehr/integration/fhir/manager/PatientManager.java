package org.oscarehr.integration.fhir.manager;
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

import java.util.ArrayList;
import java.util.List;

import org.oscarehr.common.model.Demographic;
import org.oscarehr.integration.fhir.model.Patient;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class PatientManager {
	
	@Autowired
	private DemographicManager demographicManager;
	
	public Patient getPatientByDemographicNumber( LoggedInInfo loggedInInfo, int demographic_no ) {
		Demographic demographic = demographicManager.getDemographic(loggedInInfo, demographic_no);
		Patient patient = new Patient( demographic );
		return patient;
	}
	
	public List<Patient> getPatientsByPHN( LoggedInInfo loggedInInfo, String hcn, String hcnType ) {
		List<Demographic> demographicList = demographicManager.getActiveDemosByHealthCardNo(loggedInInfo, hcn, hcnType);
		List<Patient> patientList = null;
		for(Demographic demographic : demographicList ) {
			
			if( patientList == null ) {
				patientList = new ArrayList<Patient>();
			}
			Patient patient = new Patient( demographic );
			patientList.add( patient );
		}
		return patientList;
	}

}

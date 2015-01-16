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
package org.oscarehr.ws.rest.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PersonaResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<PatientList> patientListTabItems = new ArrayList<PatientList>();
	
	private List<PatientList> patientListMoreTabItems = new ArrayList<PatientList>();

	
	private DashboardPreferences dashboardPreferences;
	
	
	public List<PatientList> getPatientListTabItems() {
		return patientListTabItems;
	}

	public void setPatientListTabItems(List<PatientList> patientListTabItems) {
		this.patientListTabItems = patientListTabItems;
	}

	public List<PatientList> getPatientListMoreTabItems() {
		return patientListMoreTabItems;
	}

	public void setPatientListMoreTabItems(List<PatientList> patientListMoreTabItems) {
		this.patientListMoreTabItems = patientListMoreTabItems;
	}

	public DashboardPreferences getDashboardPreferences() {
		return dashboardPreferences;
	}

	public void setDashboardPreferences(DashboardPreferences dashboardPreferences) {
		this.dashboardPreferences = dashboardPreferences;
	}

}

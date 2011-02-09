/*
 * Copyright (C) 2007  Heart & Stroke Foundation

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.

 <HSFO TEAM>
 *
 * This software was written for the
 * The Heart and Stroke Foundation of Ontario
 * Toronto, Ontario, Canada
 */



package oscar.form.study.HSFO;

import java.util.List;

/**
 * Class used by the HSFO Study
 * 
 */
public class PatientList {
    
    /** Creates a new instance of PatientList */
    public PatientList() {
    }
    
    	List patientHistory;
	VisitData visitData;

	

	public VisitData getVisitData() {
		return visitData;
	}

	public void setVisitData(VisitData visitData) {
		this.visitData = visitData;
	}
	
	public List getPatientHistory() {
		return patientHistory;
	}

	public void setPatientHistory(List patientHistory) {
		this.patientHistory = patientHistory;
	}



}

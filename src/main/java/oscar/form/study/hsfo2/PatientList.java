/**
 * Copyright (C) 2007  Heart & Stroke Foundation
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


package oscar.form.study.hsfo2;

import java.util.List;
import org.oscarehr.common.model.Hsfo2Visit;

/**
 * Class used by the HSFO Study
 * 
 */
public class PatientList {
    
    /** Creates a new instance of PatientList */
    public PatientList() {
    }
    
    	List patientHistory;
	Hsfo2Visit Hsfo2Visit;

	

	public Hsfo2Visit getHsfo2Visit() {
		return Hsfo2Visit;
	}

	public void setHsfo2Visit(Hsfo2Visit Hsfo2Visit) {
		this.Hsfo2Visit = Hsfo2Visit;
	}
	
	public List getPatientHistory() {
		return patientHistory;
	}

	public void setPatientHistory(List patientHistory) {
		this.patientHistory = patientHistory;
	}



}

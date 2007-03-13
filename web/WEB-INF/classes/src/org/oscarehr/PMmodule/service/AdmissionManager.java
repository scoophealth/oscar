/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.PMmodule.service;

import java.util.Date;
import java.util.List;

import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.AdmissionSearchBean;
import org.oscarehr.PMmodule.model.Program;

public interface AdmissionManager  {
	
	public Admission getAdmission(String programId, Integer demographicNo);
	
	public Admission getCurrentAdmission(String programId, Integer demographicNo);
	
	public List getAdmissions();

	public List getAdmissions(Integer demographicNo);
		
	public List getCurrentAdmissions(Integer demographicNo);

	public Admission getCurrentBedProgramAdmission(Integer demographicNo);

	public List getCurrentServiceProgramAdmission(Integer demographicNo);
	
	public Admission getCurrentCommunityProgramAdmission(Integer demographicNo);
	
	public List getCurrentAdmissionsByProgramId(String programId);
	
	public Admission getAdmission(Long id);
	
	public void saveAdmission(Admission admission);
	
	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes) throws ProgramFullException,AdmissionException;

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, Date admissionDate) throws ProgramFullException,AdmissionException;

	public void processAdmission(Integer demographicNo, String providerNo, Program program, String dischargeNotes, String admissionNotes, boolean tempAdmission) throws ProgramFullException, AdmissionException;

	public void processInitialAdmission(Integer demographicNo, String providerNo, Program program, String admissionNotes, Date admissionDate) throws ProgramFullException,AlreadyAdmittedException;

	public Admission getTemporaryAdmission(Integer demographicNo);
	
	public List getCurrentTemporaryProgramAdmission(Integer demographicNo);

	public List search(AdmissionSearchBean searchBean);
	
	public void processDischarge(Integer programId, Integer clientId, String dischargeNotes, String radioDischargeReason) throws AdmissionException;
	
	public void processDischargeToCommunity(Integer programId, Integer clientId, String providerNo, String dischargeNotes, String radioDischargeReason) throws AdmissionException;
	
}

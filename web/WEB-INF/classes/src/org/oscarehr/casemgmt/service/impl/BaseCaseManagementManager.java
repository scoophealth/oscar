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

package org.oscarehr.casemgmt.service.impl;

import org.oscarehr.PMmodule.dao.ClientDao;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.RoleManager;
import org.oscarehr.casemgmt.dao.AllergyDAO;
import org.oscarehr.casemgmt.dao.CaseManagementCPPDAO;
import org.oscarehr.casemgmt.dao.CaseManagementIssueDAO;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.dao.CaseManagementTmpSaveDAO;
import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.dao.EchartDAO;
import org.oscarehr.casemgmt.dao.ApptDAO;
import org.oscarehr.casemgmt.dao.EncounterFormDAO;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.dao.MessagetblDAO;
import org.oscarehr.casemgmt.dao.PrescriptionDAO;
import org.oscarehr.casemgmt.dao.ProviderSignitureDao;
import org.oscarehr.casemgmt.dao.RoleProgramAccessDAO;

public class BaseCaseManagementManager {

	protected String issueAccessType="access";
	
	
	protected CaseManagementNoteDAO caseManagementNoteDAO;
	protected CaseManagementIssueDAO caseManagementIssueDAO;
	protected IssueDAO issueDAO;
	protected CaseManagementCPPDAO caseManagementCPPDAO;
	protected AllergyDAO allergyDAO;
	protected PrescriptionDAO prescriptionDAO;
	protected EncounterFormDAO encounterFormDAO;
	protected MessagetblDAO messagetblDAO;
	protected EchartDAO echartDAO;
        protected ApptDAO apptDAO;
	protected ProviderDao providerDAO;
	protected ClientDao demographicDAO;
	protected ProviderSignitureDao providerSignitureDao;
	protected RoleProgramAccessDAO roleProgramAccessDAO;
	protected ClientImageDAO clientImageDAO;
	protected RoleManager roleManager;
	protected CaseManagementTmpSaveDAO caseManagementTmpSaveDAO;
	protected AdmissionManager admissionManager;
	
	private boolean enabled;
		
	public boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setEchartDAO(EchartDAO echartDAO) {
		this.echartDAO = echartDAO;
	}
        
        public void setApptDAO(ApptDAO apptDAO) {
                this.apptDAO = apptDAO;
        }

	public void setEncounterFormDAO(EncounterFormDAO dao) {
		this.encounterFormDAO = dao;
	}

	public void setMessagetblDAO(MessagetblDAO dao) {
		this.messagetblDAO = dao;
	}

	public void setCaseManagementNoteDAO(CaseManagementNoteDAO dao)	{
		this.caseManagementNoteDAO = dao;
	}

	public void setCaseManagementIssueDAO(CaseManagementIssueDAO dao) {
		this.caseManagementIssueDAO = dao;
	}

	public void setIssueDAO(IssueDAO dao) {
		this.issueDAO = dao;
	}

	public void setCaseManagementCPPDAO(CaseManagementCPPDAO dao) {
		this.caseManagementCPPDAO = dao;
	}

	public void setAllergyDAO(AllergyDAO dao) {
		this.allergyDAO = dao;
	}

	public void setPrescriptionDAO(PrescriptionDAO dao) {
		this.prescriptionDAO = dao;
	}
	
	public void setClientImageDAO(ClientImageDAO dao) {
		this.clientImageDAO = dao;
	}
	
	public void setRoleManager(RoleManager mgr) {
		this.roleManager= mgr;
	}

	public void setProviderSignitureDao(ProviderSignitureDao providerSignitureDao) {
		this.providerSignitureDao = providerSignitureDao;
	}

	public void setRoleProgramAccessDAO(RoleProgramAccessDAO roleProgramAccessDAO) {
		this.roleProgramAccessDAO = roleProgramAccessDAO;
	}
	
	public void setDemographicDAO(ClientDao demographicDAO) {
		this.demographicDAO = demographicDAO;
	}

	public void setProviderDAO(ProviderDao providerDAO)	{
		this.providerDAO = providerDAO;
	}
	
	public void setCaseManagementTmpSaveDAO(CaseManagementTmpSaveDAO dao) {
		this.caseManagementTmpSaveDAO = dao;
	}
	
	protected String removeFirstSpace(String withSpaces) {
        int spaceIndex = withSpaces.indexOf(' '); //use lastIndexOf to remove last space
        if (spaceIndex < 0) { //no spaces!
            return withSpaces;
        }
        return withSpaces.substring(0, spaceIndex)
            + withSpaces.substring(spaceIndex+1, withSpaces.length());
    }

	public void setAdmissionManager(AdmissionManager mgr) {
		this.admissionManager = mgr;
	}
}


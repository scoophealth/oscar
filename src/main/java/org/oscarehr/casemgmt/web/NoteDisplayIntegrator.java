/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.CodeType;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.NoteIssue;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.oscarRx.data.RxPrescriptionData;

public class NoteDisplayIntegrator implements NoteDisplay {
	private static final Logger logger=MiscUtils.getLogger();

	private IssueDAO issueDao=(IssueDAO) SpringUtils.getBean("IssueDAO");

	private CachedDemographicNote cachedDemographicNote;
	private String location="Integrated Facility : details n/a";
	private String programName="Unavailable";
	private String providerName="Unavailable";
	private ArrayList<String> issueDescriptions=new ArrayList<String>();

	@Override
    public Integer getAppointmentNo() {
		return null;
    }
	
	
	public NoteDisplayIntegrator(LoggedInInfo loggedInInfo,CachedDemographicNote cachedDemographicNote)
	{
		this.cachedDemographicNote=cachedDemographicNote;

    	try {
    		// note location
	        CachedFacility cachedFacility=CaisiIntegratorManager.getRemoteFacility(loggedInInfo, loggedInInfo.getCurrentFacility(),cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
	        if (cachedFacility!=null) location="Integrated Facility : "+cachedFacility.getName();

	        // program name
	    	FacilityIdIntegerCompositePk programPk=new FacilityIdIntegerCompositePk();
	    	programPk.setIntegratorFacilityId(cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
	    	programPk.setCaisiItemId(cachedDemographicNote.getCaisiProgramId());
	    	CachedProgram remoteProgram=CaisiIntegratorManager.getRemoteProgram(loggedInInfo, loggedInInfo.getCurrentFacility(),programPk);
	    	if (remoteProgram!=null) programName=remoteProgram.getName();

	    	// provider name
	    	FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
	    	providerPk.setIntegratorFacilityId(cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
	    	providerPk.setCaisiItemId(cachedDemographicNote.getObservationCaisiProviderId());
	    	CachedProvider remoteProvider=CaisiIntegratorManager.getProvider(loggedInInfo, loggedInInfo.getCurrentFacility(),providerPk);
	    	if (remoteProvider!=null) providerName=remoteProvider.getLastName()+", "+remoteProvider.getFirstName();

	    	// issue descriptions
			for (NoteIssue noteIssue : cachedDemographicNote.getIssues())
			{
				String ct = "";
				if(noteIssue.getCodeType() == CodeType.ICD_10) {
					ct = "icd10";
				}
				else if(noteIssue.getCodeType() == CodeType.ICD_9) {
					ct = "icd9";
				} else {
					ct = noteIssue.getCodeType().name().toLowerCase();
				}
				Issue issue=issueDao.findIssueByTypeAndCode(ct, noteIssue.getIssueCode());
				if (issue!=null) issueDescriptions.add(issue.getDescription());
				else issueDescriptions.add(noteIssue.getCodeType().name()+':'+noteIssue.getIssueCode());
			}
        } catch (Exception e) {
	        logger.error("Unexpected error.", e);
        }
	}

	public String getEncounterType() {
	    return(cachedDemographicNote.getEncounterType());
    }

	public boolean getHasHistory() {
	    return(false);
    }

	public String getLocation() {
	    return(location);
    }

	public String getNote() {
	    return(cachedDemographicNote.getNote());
    }

	public Integer getNoteId() {
	    return(null);
    }

	public Date getObservationDate() {
		if (cachedDemographicNote.getObservationDate()==null) return(null);
		else return(cachedDemographicNote.getObservationDate().getTime());
    }

	public String getProgramName() {
	    return(programName);
    }

	public String getProviderName() {
	    return(providerName);
    }

	public String getProviderNo() {
	    return(null);
    }

	public Integer getRemoteFacilityId() {
	    return(cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
    }

	public String getRevision() {
	    return(null);
    }

	public String getRoleName() {
	    return(cachedDemographicNote.getRole());
    }

	public String getStatus() {
	    return(cachedDemographicNote.getSigningCaisiProviderId()!=null?"Signed":"Unsigned");
    }

	public Date getUpdateDate() {
		if (cachedDemographicNote.getUpdateDate()==null) return(null);
		else return(cachedDemographicNote.getUpdateDate().getTime());
    }

	public String getUuid() {
	    return(cachedDemographicNote.getCachedDemographicNoteCompositePk().getUuid());
    }

	public boolean isDocument() {
	    return(false);
    }

	public boolean isRxAnnotation(){
		return (false);
	}

	public RxPrescriptionData.Prescription getRxFromAnnotation(CaseManagementNoteLink cmnl){
		return (null);
	}

	public boolean isEditable() {
	    return(false);
    }

	public boolean isLocked() {
	    return(false);
    }

	public boolean isSigned() {
	    return(cachedDemographicNote.getSigningCaisiProviderId()!=null);
    }

	public ArrayList<String> getEditorNames() {
	    return(new ArrayList<String>());
    }

	public ArrayList<String> getIssueDescriptions()
	{
		return(issueDescriptions);
	}

	public boolean isReadOnly() {return true;}

	public boolean isGroupNote() {
		return false;
	}

	public boolean isEformData() {
		return false;
	}

	public CaseManagementNoteLink getNoteLink() {
		return null;
	}

	public boolean isCpp() {
	    return false;
    }

	public boolean containsIssue(String issueCode) {
		return false;
	}

	public boolean isEncounterForm() {
	    return false;
    }

	public boolean isInvoice() {
		return false;
	}

	@Override
    public String getEncounterTime() {
	    // TODO Auto-generated method stub
	    return null;
    }

	@Override
    public String getEncounterTransportationTime() {
	    // TODO Auto-generated method stub
	    return null;
    }


	@Override
	public boolean isFreeDraw() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean isTicklerNote() {
		return false;
	}
}

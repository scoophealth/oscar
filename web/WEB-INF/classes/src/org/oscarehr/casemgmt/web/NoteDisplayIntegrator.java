package org.oscarehr.casemgmt.web;

import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.CachedFacility;
import org.oscarehr.caisi_integrator.ws.CachedProgram;
import org.oscarehr.caisi_integrator.ws.CachedProvider;
import org.oscarehr.caisi_integrator.ws.FacilityIdIntegerCompositePk;
import org.oscarehr.caisi_integrator.ws.FacilityIdStringCompositePk;
import org.oscarehr.caisi_integrator.ws.NoteIssue;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementNoteLink;
import org.oscarehr.casemgmt.model.Issue;
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
	
	public NoteDisplayIntegrator(CachedDemographicNote cachedDemographicNote)
	{
		this.cachedDemographicNote=cachedDemographicNote;
		
    	try {
    		// note location
	        CachedFacility cachedFacility=CaisiIntegratorManager.getRemoteFacility(cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
	        if (cachedFacility!=null) location="Integrated Facility : "+cachedFacility.getName();
	        
	        // program name
	    	FacilityIdIntegerCompositePk programPk=new FacilityIdIntegerCompositePk();
	    	programPk.setIntegratorFacilityId(cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
	    	programPk.setCaisiItemId(cachedDemographicNote.getCaisiProgramId());
	    	CachedProgram remoteProgram=CaisiIntegratorManager.getRemoteProgram(programPk);
	    	if (remoteProgram!=null) programName=remoteProgram.getName();
	    	
	    	// provider name
	    	FacilityIdStringCompositePk providerPk=new FacilityIdStringCompositePk();
	    	providerPk.setIntegratorFacilityId(cachedDemographicNote.getCachedDemographicNoteCompositePk().getIntegratorFacilityId());
	    	providerPk.setCaisiItemId(cachedDemographicNote.getObservationCaisiProviderId());
	    	CachedProvider remoteProvider=CaisiIntegratorManager.getProvider(providerPk);
	    	if (remoteProvider!=null) providerName=remoteProvider.getLastName()+", "+remoteProvider.getFirstName();
	    	
	    	// issue descriptions
			for (NoteIssue noteIssue : cachedDemographicNote.getIssues())
			{
				Issue issue=issueDao.findIssueByTypeAndCode(noteIssue.getCodeType().name().toLowerCase(), noteIssue.getIssueCode());
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
	    return(cachedDemographicNote.getObservationDate());
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
	    return(cachedDemographicNote.getUpdateDate());
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
}

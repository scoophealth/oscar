package org.oscarehr.PMmodule.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.AgencyManager;
import org.oscarehr.PMmodule.service.BedCheckTimeManager;
import org.oscarehr.PMmodule.service.BedDemographicManager;
import org.oscarehr.PMmodule.service.BedManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.ConsentManager;
import org.oscarehr.PMmodule.service.FormsManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.service.IntakeAManager;
import org.oscarehr.PMmodule.service.IntakeCManager;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.service.RoleManager;
import org.oscarehr.PMmodule.service.RoomDemographicManager;
import org.oscarehr.PMmodule.service.RoomManager;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.casemgmt.service.CaseManagementManager;

import com.quatro.service.LookupManager;

abstract class BaseGenericIntakeAction extends BaseAction {
	
	private static Log LOG = LogFactory.getLog(BaseGenericIntakeAction.class);

	// Parameters
	protected static final String METHOD = "method";
	protected static final String TYPE = "type";
	protected static final String CLIENT_ID = "clientId";
	protected static final String INTAKE_ID = "intakeId";
    protected static final String CLIENT_EDIT_ID = "id";
	protected static final String PROGRAM_ID = "programId";
	protected static final String START_DATE = "startDate";
	protected static final String END_DATE = "endDate";
	
	// Method Names
	protected static final String EDIT_CREATE = "create";
    protected static final String EDIT_CREATE_REMOTE = "create_remote";

    protected static final String EDIT_UPDATE = "update";
	
	// Session Attributes
	protected static final String CLIENT = "client";

    protected LookupManager lookupManager;

    protected CaseManagementManager caseManagementManager;

    protected AdmissionManager admissionManager;

    protected GenericIntakeManager genericIntakeManager;

    protected AgencyManager agencyManager;

    protected BedCheckTimeManager bedCheckTimeManager;

    protected RoomDemographicManager roomDemographicManager;

    protected BedDemographicManager bedDemographicManager;

    protected BedManager bedManager;

    protected ClientManager clientManager;

    protected ConsentManager consentManager;

    protected FormsManager formsManager;

    protected IntakeAManager intakeAManager;

    protected IntakeCManager intakeCManager;

    protected LogManager logManager;

    protected ProgramManager programManager;

    protected ProviderManager providerManager;

    protected ProgramQueueManager programQueueManager;

    protected RoleManager roleManager;

    protected RoomManager roomManager;
	
	// Parameter Accessors
	
	protected String getType(HttpServletRequest request) {
		return getParameter(request, TYPE);
	}

	protected Integer getClientId(HttpServletRequest request) {
		return Integer.valueOf(getParameter(request, CLIENT_ID));
	}

	protected Integer getIntakeId(HttpServletRequest request) {
		return Integer.valueOf(getParameter(request, INTAKE_ID));
	}

    protected Integer getProgramId(HttpServletRequest request) {
		Integer programId = null;
		
		String programIdParam = getParameter(request, PROGRAM_ID);
		
		if (programIdParam != null) {
			try {
				programId = Integer.valueOf(programIdParam);
			} catch (NumberFormatException e) {
				LOG.error(e);
			}
		}
		
		return programId;
	}
	
	protected Date getStartDate(HttpServletRequest request) {
		return DateTimeFormatUtils.getDateFromString(getParameter(request, START_DATE));
	}

	protected Date getEndDate(HttpServletRequest request) {
		return DateTimeFormatUtils.getDateFromString(getParameter(request, END_DATE));
	}

	// Session Attribute Accessors
	
	protected Demographic getClient(HttpServletRequest request) {
		Demographic client = (Demographic) getSessionAttribute(request, CLIENT);
		return (client != null) ? client : new Demographic();
	}

    public void setLookupManager(LookupManager lookupManager) {
    	this.lookupManager = lookupManager;
    }

    public void setCaseManagementManager(CaseManagementManager caseManagementManager) {
    	this.caseManagementManager = caseManagementManager;
    }

    public void setAdmissionManager(AdmissionManager mgr) {
    	this.admissionManager = mgr;
    }

    public void setGenericIntakeManager(GenericIntakeManager genericIntakeManager) {
        this.genericIntakeManager = genericIntakeManager;
    }

    public void setAgencyManager(AgencyManager mgr) {
    	this.agencyManager = mgr;
    }

    public void setBedCheckTimeManager(BedCheckTimeManager bedCheckTimeManager) {
        this.bedCheckTimeManager = bedCheckTimeManager;
    }

    public void setBedDemographicManager(BedDemographicManager demographicBedManager) {
    	this.bedDemographicManager = demographicBedManager;
    }

    public void setRoomDemographicManager(RoomDemographicManager roomDemographicManager) {
    	this.roomDemographicManager = roomDemographicManager;
    }

    public void setBedManager(BedManager bedManager) {
    	this.bedManager = bedManager;
    }

    public void setClientManager(ClientManager mgr) {
    	this.clientManager = mgr;
    }

    public void setConsentManager(ConsentManager mgr) {
    	this.consentManager = mgr;
    }

    public void setFormsManager(FormsManager mgr) {
    	this.formsManager = mgr;
    }

    public void setIntakeAManager(IntakeAManager mgr) {
    	this.intakeAManager = mgr;
    }

    public void setIntakeCManager(IntakeCManager mgr) {
    	this.intakeCManager = mgr;
    }

    public void setLogManager(LogManager mgr) {
    	this.logManager = mgr;
    }

    public void setProgramManager(ProgramManager mgr) {
    	this.programManager = mgr;
    }

    public void setProgramQueueManager(ProgramQueueManager mgr) {
    	this.programQueueManager = mgr;
    }

    public void setProviderManager(ProviderManager mgr) {
    	this.providerManager = mgr;
    }

    public void setRoleManager(RoleManager mgr) {
    	this.roleManager = mgr;
    }

    public void setRoomManager(RoomManager roomManager) {
    	this.roomManager = roomManager;
    }


}

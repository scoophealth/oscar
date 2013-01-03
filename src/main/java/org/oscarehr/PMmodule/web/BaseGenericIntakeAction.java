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

package org.oscarehr.PMmodule.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.AgencyManager;
import org.oscarehr.PMmodule.service.BedCheckTimeManager;
import org.oscarehr.PMmodule.service.BedDemographicManager;
import org.oscarehr.PMmodule.service.BedManager;
import org.oscarehr.PMmodule.service.ClientManager;
import org.oscarehr.PMmodule.service.FormsManager;
import org.oscarehr.PMmodule.service.GenericIntakeManager;
import org.oscarehr.PMmodule.service.LogManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.ProgramQueueManager;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.PMmodule.service.RoomDemographicManager;
import org.oscarehr.PMmodule.service.RoomManager;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.util.MiscUtils;

import com.quatro.service.LookupManager;

abstract class BaseGenericIntakeAction extends BaseAction {
	
	private static Logger LOG = MiscUtils.getLogger();

	// Parameters
	protected static final String METHOD = "method";
	protected static final String TYPE = "type";
	protected static final String CLIENT_ID = "clientId";
	protected static final String INTAKE_ID = "intakeId";
    protected static final String CLIENT_EDIT_ID = "id";
	protected static final String PROGRAM_ID = "programId";
	protected static final String START_DATE = "startDate";
	protected static final String END_DATE = "endDate";
	protected static final String INCLUDE_PAST = "includePast";
	
	// Method Names
	protected static final String EDIT_CREATE = "create";

    protected static final String EDIT_UPDATE = "update";
	
	// Session Attributes
	protected static final String CLIENT = "client";
	
	protected static final String APPOINTMENT = "appointment";
	protected static final String DEMOGRAPHIC_NO = "demographic_no";
	protected static final String FORM_ID = "fid";

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

    protected FormsManager formsManager;

    protected LogManager logManager;

    protected ProgramManager programManager;

    protected ProviderManager providerManager;

    protected ProgramQueueManager programQueueManager;

    protected RoomManager roomManager;
	
	// Parameter Accessors
	
	protected String getType(HttpServletRequest request) {
		return getParameter(request, TYPE);
	}

	protected Integer getClientIdAsInteger(HttpServletRequest request) {
		Integer clientId = null;
		String clientId_str = getParameter(request, CLIENT_ID);
		if(clientId_str!=null) {
			try {
				clientId = Integer.valueOf(clientId_str);
			} catch (NumberFormatException e) {
				LOG.error("Error", e);
			}
		}
		return clientId;
		
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
				LOG.error("Error", e);
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

	protected String getIncludePast(HttpServletRequest request) {
		return getParameter(request, INCLUDE_PAST);
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

    public void setFormsManager(FormsManager mgr) {
    	this.formsManager = mgr;
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

    public void setRoomManager(RoomManager roomManager) {
    	this.roomManager = roomManager;
    }


}

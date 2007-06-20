package org.oscarehr.PMmodule.web;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.utility.DateTimeFormatUtils;

abstract class BaseGenericIntakeAction extends BaseAction {
	
	private static Log LOG = LogFactory.getLog(BaseGenericIntakeAction.class);

	// Parameters
	protected static final String METHOD = "method";
	protected static final String TYPE = "type";
	protected static final String CLIENT_ID = "clientId";
	protected static final String CLIENT_EDIT_ID = "id";
	protected static final String PROGRAM_ID = "programId";
	protected static final String START_DATE = "startDate";
	protected static final String END_DATE = "endDate";
	
	// Method Names
	protected static final String EDIT_CREATE = "create";
	protected static final String EDIT_UPDATE = "update";
	
	// Session Attributes
	protected static final String CLIENT = "client";
	
	// Parameter Accessors
	
	protected String getType(HttpServletRequest request) {
		return getParameter(request, TYPE);
	}

	protected Integer getClientId(HttpServletRequest request) {
		return Integer.valueOf(getParameter(request, CLIENT_ID));
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

}

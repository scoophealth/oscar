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


package oscar.oscarLab.ca.all.pageUtil;

import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;
import org.oscarehr.casemgmt.dao.CaseManagementNoteDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.common.Gender;
import org.oscarehr.common.dao.CaseManagementIssueNotesDao;
import org.oscarehr.common.dao.Hl7TextMessageDao;
import org.oscarehr.common.dao.ProfessionalSpecialistDao;
import org.oscarehr.common.dao.PublicKeyDao;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Hl7TextMessage;
import org.oscarehr.common.model.ProfessionalSpecialist;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.PublicKey;
import org.oscarehr.managers.DemographicManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

/**
 * All Data with getters should be HTML escaped.
 * For the parameters read in as query parameters this should already be escaped but 
 * for things like the textMessage you need to escape it manually.
 */
public final class SendOruR01UIBean {
	
	private static Logger logger=MiscUtils.getLogger();
	private static ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
	private static Hl7TextMessageDao hl7TextMessageDao = (Hl7TextMessageDao) SpringUtils.getBean("hl7TextMessageDao");
	private static PublicKeyDao publicKeyDao = (PublicKeyDao) SpringUtils.getBean("publicKeyDao");
	private static DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	private static CaseManagementNoteDAO caseManagementNoteDAO = (CaseManagementNoteDAO) SpringUtils.getBean("caseManagementNoteDAO");
	private static CaseManagementIssueNotesDao caseManagementIssueNotesDao= (CaseManagementIssueNotesDao) SpringUtils.getBean("caseManagementIssueNotesDao");
	
	private Integer professionalSpecialistId=null;
	private String clientFirstName=null;
	private String clientLastName=null;
	private String clientHin=null;
	private String clientBirthDate=null;
	private String clientGender=null;
	private String subject=null;
	private String textMessage=null;
	
	/**
	 * So the general idea is that most fields can be passed in as generic data string.
	 * In some cases like with Notes, it's sufficient just to pass the noteId, given that, 
	 * we can extract the demographic data etc. This maybe true for other things as well
	 * so in the future if we add as an example prescriptions, just pass the prescriptionId
	 * and the data can be looked up in this method. 
	 */
	public SendOruR01UIBean(HttpServletRequest request)
	{
		professionalSpecialistId=getSelectedProfessionalSpecialistIdFromRequest(request);

		clientFirstName=StringUtils.trimToEmpty(request.getParameter("clientFirstName"));
		clientLastName=StringUtils.trimToEmpty(request.getParameter("clientLastName"));
		clientHin=StringUtils.trimToEmpty(request.getParameter("clientHin"));
		clientBirthDate=StringUtils.trimToEmpty(request.getParameter("clientBirthDate"));
		clientGender=StringUtils.trimToEmpty(request.getParameter("clientGender"));
		subject=StringUtils.trimToEmpty(request.getParameter("subject"));
		
		checkForDataFromNotes(request);
	}
	
	private void checkForDataFromNotes(HttpServletRequest request) {
		String temp=request.getParameter("noteId");
		if (temp!=null)
		{
			Long noteId=Long.parseLong(temp);
			CaseManagementNote caseManagementNote=caseManagementNoteDAO.getNote(noteId);
			
			// fill demographic info
			String demographicId=caseManagementNote.getDemographic_no();
			Demographic demographic=demographicManager.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), demographicId);
			clientFirstName=StringEscapeUtils.escapeHtml(StringUtils.trimToEmpty(demographic.getFirstName()));
			clientLastName=StringEscapeUtils.escapeHtml(StringUtils.trimToEmpty(demographic.getLastName()));
			clientHin=StringEscapeUtils.escapeHtml(StringUtils.trimToEmpty(demographic.getHin()));
			GregorianCalendar cal=demographic.getBirthDay();
			if (cal!=null) clientBirthDate=StringEscapeUtils.escapeHtml(DateFormatUtils.ISO_DATE_FORMAT.format(cal));
			clientGender=StringEscapeUtils.escapeHtml(StringUtils.trimToEmpty(demographic.getSex()));
			
			// fill note info
			subject=StringEscapeUtils.escapeHtml("Encounter Note");
			StringBuilder sb=new StringBuilder();
			sb.append(caseManagementNote.getNote());
			
			List<CaseManagementIssue> caseManagementIssues=caseManagementIssueNotesDao.getNoteIssues(caseManagementNote.getId().intValue());
			if (caseManagementIssues.size()>0)
			{
				sb.append("\n---------- Attached Issues ---------");
				for (CaseManagementIssue caseManagementIssue : caseManagementIssues)
				{
					sb.append('\n');
					Issue issue=caseManagementIssue.getIssue();
					sb.append(issue.getCode());
					sb.append(" (");
					sb.append(issue.getDescription());
					sb.append(") : acute=");
					sb.append(caseManagementIssue.isAcute());
					sb.append(", certain=");
					sb.append(caseManagementIssue.isCertain());
					sb.append(", major=");
					sb.append(caseManagementIssue.isMajor());
					sb.append(", resolved=");
					sb.append(caseManagementIssue.isResolved());
				}
			}
			else
			{
				sb.append("\n---------- No Attached Issues ---------");
			}
			
			textMessage=StringEscapeUtils.escapeHtml(sb.toString());
		}
    }

	private Integer getSelectedProfessionalSpecialistIdFromRequest(HttpServletRequest request)
	{
		// get direct from parameter
		String temp=request.getParameter("professionalSpecialistId");
		if (temp!=null) return(Integer.parseInt(temp));
		
		// get as a response to a previous message
		String hl7TextMessageId=request.getParameter("hl7TextMessageId");
		if (hl7TextMessageId!=null)
		{
			try
			{
				Hl7TextMessage hl7TextMessage=hl7TextMessageDao.find(Integer.parseInt(hl7TextMessageId));
				PublicKey publicKey=publicKeyDao.find(hl7TextMessage.getServiceName());
				return(publicKey.getMatchingProfessionalSpecialistId());
			}
			catch (Exception e)
			{
				logger.error("Unexpected error.", e);
			}
		}
		
		// if all else fails return null
		return(null);
	}

	public static String getLoggedInProviderDisplayLine(LoggedInInfo loggedInInfo) {
		Provider provider = loggedInInfo.getLoggedInProvider();

		StringBuilder sb = new StringBuilder();

		sb.append(provider.getLastName());
		sb.append(", ");
		sb.append(provider.getFirstName());

		if (provider.getPhone() != null) {
			sb.append(", ");
			sb.append(provider.getPhone());
		}

		if (provider.getEmail() != null) {
			sb.append(", ");
			sb.append(provider.getEmail());
		}

		if (provider.getAddress() != null) {
			sb.append(", ");
			sb.append(provider.getAddress());
		}

		return (StringEscapeUtils.escapeHtml(sb.toString()));
	}

	public static List<ProfessionalSpecialist> getRemoteCapableProfessionalSpecialists() {
		return(professionalSpecialistDao.findByEDataUrlNotNull());
	}

	public static String getProfessionalSpecialistDisplayString(ProfessionalSpecialist professionalSpecialist) {
		StringBuilder sb = new StringBuilder();

		sb.append(professionalSpecialist.getLastName());
		sb.append(", ");
		sb.append(professionalSpecialist.getFirstName());

		if (professionalSpecialist.getPhoneNumber() != null) {
			sb.append(", ");
			sb.append(professionalSpecialist.getPhoneNumber());
		}

		if (professionalSpecialist.getEmailAddress() != null) {
			sb.append(", ");
			sb.append(professionalSpecialist.getEmailAddress());
		}

		if (professionalSpecialist.getStreetAddress() != null) {
			sb.append(", ");
			sb.append(professionalSpecialist.getStreetAddress());
		}

		return (StringEscapeUtils.escapeHtml(sb.toString()));
	}

	public Integer getProfessionalSpecialistId() {
    	return professionalSpecialistId;
    }

	public String getClientFirstName() {
    	return clientFirstName;
    }

	public String getClientLastName() {
    	return clientLastName;
    }

	public String getClientHin() {
    	return clientHin;
    }

	public String getClientBirthDate() {
    	return clientBirthDate;
    }

	public String getClientGender() {
    	return clientGender;
    }

	public String getSubject() {
    	return subject;
    }

	public String getTextMessage() {
    	return textMessage;
    }

	public String renderSelectedProfessionalSpecialistOption(Integer professionalSpecialistId)
	{
		if (this.professionalSpecialistId!=null && this.professionalSpecialistId.equals(professionalSpecialistId))
		{
			return("selected=\"selected\"");
		}
		else
		{
			return("");
		}
	}

	public String renderSelectedGenderOption(Gender gender)
	{
		if (this.clientGender!=null && this.clientGender.equals(gender.name()))
		{
			return("selected=\"selected\"");
		}
		else
		{
			return("");
		}
	}
}

// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster University 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.pageUtil;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.util.MessageResources;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicIssue;
import org.oscarehr.caisi_integrator.ws.CachedDemographicNote;
import org.oscarehr.caisi_integrator.ws.NoteIssue;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.util.StringUtils;

/**
 * retrieves info to display Disease entries for demographic
 */
public class EctDisplayIssuesAction extends EctDisplayAction {
	private String cmd = "issues";

	private IssueDAO issueDao=(IssueDAO) SpringUtils.getBean("IssueDAO");
	
	private CaseManagementManager caseManagementMgr;
	private static Log log = LogFactory.getLog(EctDisplayIssuesAction.class);

	public void setCaseManagementManager(CaseManagementManager caseManagementMgr) {
		this.caseManagementMgr = caseManagementMgr;
	}

	public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO navBarDisplayDAO, MessageResources messages) {

		// set lefthand module heading and link
		navBarDisplayDAO.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.NavBar.Issues"));
		navBarDisplayDAO.setLeftURL("$('check_issue').value='';document.caseManagementViewForm.submit();");

		// set righthand link to same as left so we have visual consistency with other modules
		String url = "return false;";
		navBarDisplayDAO.setRightURL(url);
		navBarDisplayDAO.setRightHeadingID(cmd); // no menu so set div id to unique id for this action

		// grab all of the diseases associated with patient and add a list item for each
		List issues = null;
		int demographicId = Integer.parseInt(bean.getDemographicNo());
		issues = caseManagementMgr.getIssues(demographicId);
		String programId = (String) request.getSession().getAttribute("case_program_id");
		issues = caseManagementMgr.filterIssues(issues, programId);

		for (int idx = 0; idx < issues.size(); ++idx) {
			NavBarDisplayDAO.Item item = navBarDisplayDAO.Item();

			CaseManagementIssue issue = (CaseManagementIssue) issues.get(idx);
			String tmp = issue.getIssue().getDescription();

			String strTitle = StringUtils.maxLenString(tmp, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);

			item.setTitle(strTitle);
			item.setLinkTitle(tmp);
			url = "$('check_issue').value=" + issue.getIssue_id() + ";return filter();";
			item.setURL(url);
			navBarDisplayDAO.addItem(item);
		}

		
		LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();

		if (loggedInInfo.currentFacility.isIntegratorEnabled()) {
			try {
				
		
				List<CachedDemographicIssue> remoteIssues = CaisiIntegratorManager.getDemographicWs().getLinkedCachedDemographicIssuesByDemographicId(demographicId);
				
				for (CachedDemographicIssue cachedDemographicIssue : remoteIssues) {
					log.info(cachedDemographicIssue.getIssueDescription());
					NavBarDisplayDAO.Item item = navBarDisplayDAO.Item();

					String strTitle = StringUtils.maxLenString(cachedDemographicIssue.getIssueDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
					item.setTitle(strTitle);
					
					item.setLinkTitle(cachedDemographicIssue.getIssueDescription());
					
					// no link for now, will make this work later ... maybe
					url = "return fasle;";
					item.setURL(url);
					
					navBarDisplayDAO.addItem(item);	
					
				}
			} catch(Exception e ) {
				log.error("Unexpected error", e);
			}
		}
		
		
		// add integrator issues
		//LoggedInInfo loggedInInfo = LoggedInInfo.loggedInInfo.get();
/*
		if (loggedInInfo.currentFacility.isIntegratorEnabled()) {
			try {
				List<CachedDemographicNote> remoteNotes=CaisiIntegratorManager.getLinkedNotes(demographicId);
				
				// map is of <issueId,issueDescription>
				HashMap<String,String> remoteIssues=new HashMap<String,String>();
				
				for (CachedDemographicNote remoteNote : remoteNotes)
				{
					List<NoteIssue> noteIssues=remoteNote.getIssues();
					
					for (NoteIssue noteIssue: noteIssues)
					{
						String issueId=noteIssue.getCodeType().name()+':'+noteIssue.getIssueCode();
						
						String issueDescription;
						Issue issue=issueDao.findIssueByTypeAndCode(noteIssue.getCodeType().name().toLowerCase(), noteIssue.getIssueCode());
						if (issue!=null) issueDescription=issue.getDescription();
						else issueDescription=issueId;

						
						remoteIssues.put(issueId, issueDescription);
					}
				}
				
				for (Map.Entry<String,String> remoteIssue : remoteIssues.entrySet())
				{
					String issueId=remoteIssue.getKey();					
					String issueDescription=remoteIssue.getValue();
										
					NavBarDisplayDAO.Item item = navBarDisplayDAO.Item();

					String strTitle = StringUtils.maxLenString(issueDescription, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
					item.setTitle(strTitle);
					
					item.setLinkTitle(issueDescription);
					
					// no link for now, will make this work later ... maybe
					// url = "$('check_issue').value=" + issueId + ";return filter();";
					// item.setURL(url);
					
					navBarDisplayDAO.addItem(item);					
				}
			} catch (MalformedURLException e) {
				log.error("Unexpected error", e);
			}
		}
*/
		return true;
	}

	public String getCmd() {
		return cmd;
	}
}

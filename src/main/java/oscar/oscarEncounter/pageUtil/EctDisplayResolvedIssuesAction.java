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

package oscar.oscarEncounter.pageUtil;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.util.MessageResources;
import org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager;
import org.oscarehr.PMmodule.caisi_integrator.IntegratorFallBackManager;
import org.oscarehr.caisi_integrator.ws.CachedDemographicIssue;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.util.CppUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;

import oscar.util.StringUtils;

/**
 * retrieves info to display Disease entries for demographic
 */
public class EctDisplayResolvedIssuesAction extends EctDisplayAction {
	private String cmd = "resolvedIssues";

	
	private CaseManagementManager caseManagementMgr;
	private static Logger log = MiscUtils.getLogger();

	public void setCaseManagementManager(CaseManagementManager caseManagementMgr) {
		this.caseManagementMgr = caseManagementMgr;
	}

	@Override
    public boolean getInfo(EctSessionBean bean, HttpServletRequest request, NavBarDisplayDAO navBarDisplayDAO, MessageResources messages) {

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		String providerNo=loggedInInfo.getLoggedInProviderNo();

		// set lefthand module heading and link
		navBarDisplayDAO.setLeftHeading(messages.getMessage(request.getLocale(), "oscarEncounter.NavBar.resolvedIssues"));
		
		navBarDisplayDAO.setLeftURL("$('check_issue').value='';document.caseManagementViewForm.submit();");

		// set righthand link to same as left so we have visual consistency with other modules
		String url = "return false;";
		navBarDisplayDAO.setRightURL(url);
		navBarDisplayDAO.setRightHeadingID(cmd); // no menu so set div id to unique id for this action

		// grab all of the diseases associated with patient and add a list item for each
		List<CaseManagementIssue> issues = null;
		int demographicId = Integer.parseInt(bean.getDemographicNo());
		issues = caseManagementMgr.getIssues(demographicId);
		String programId = (String) request.getSession().getAttribute("case_program_id");
		issues = caseManagementMgr.filterIssues(loggedInInfo, providerNo, issues, programId);
	
		List<CaseManagementIssue> issues_unr = new ArrayList<CaseManagementIssue>();
		//only list resolved issues				
		for(CaseManagementIssue issue : issues) {
			if(containsIssue(CppUtils.cppCodes,issue.getIssue().getCode())) {
				continue;
			}
			
			if(issue.isResolved()) {
				issues_unr.add(issue);
			}				
		}
		
		
		for (int idx = 0; idx < issues_unr.size(); ++idx) {
			NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();

			CaseManagementIssue issue = issues_unr.get(idx);
			String tmp = issue.getIssue().getDescription();

			String strTitle = StringUtils.maxLenString(tmp, MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);

			item.setTitle(strTitle);
			item.setLinkTitle(tmp);
			//issues value=			
			url = "setIssueCheckbox('"+issue.getId()+"');return filter(false);";
			item.setURL(url);
			navBarDisplayDAO.addItem(item);
		}

		
		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
			try {
				
		
				List<CachedDemographicIssue> remoteIssues  = null;
				try {
					if (!CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
					   remoteIssues = CaisiIntegratorManager.getDemographicWs(loggedInInfo, loggedInInfo.getCurrentFacility()).getLinkedCachedDemographicIssuesByDemographicId(demographicId);
					}
				} catch (Exception e) {
					MiscUtils.getLogger().error("Unexpected error.", e);
					CaisiIntegratorManager.checkForConnectionError(loggedInInfo.getSession(),e);
				}
				
				if(CaisiIntegratorManager.isIntegratorOffline(loggedInInfo.getSession())){
				   remoteIssues = IntegratorFallBackManager.getRemoteDemographicIssues(loggedInInfo, demographicId);	
				}
				
				for (CachedDemographicIssue cachedDemographicIssue : remoteIssues) {
					if(cachedDemographicIssue.isResolved()!=null && !cachedDemographicIssue.isResolved())
						continue;
					
					log.info(cachedDemographicIssue.getIssueDescription());
					NavBarDisplayDAO.Item item = NavBarDisplayDAO.Item();

					String strTitle = StringUtils.maxLenString(cachedDemographicIssue.getIssueDescription(), MAX_LEN_TITLE, CROP_LEN_TITLE, ELLIPSES);
					item.setTitle(strTitle);
															
					item.setLinkTitle(cachedDemographicIssue.getIssueDescription());
					
					// no link for now, will make this work later ... maybe
					url = "return false;";
					item.setURL(url);
					
					boolean skip=false;
					for(int x=0;x<navBarDisplayDAO.numItems();x++) {
						if(navBarDisplayDAO.getItem(x).getTitle().equals(strTitle)) {
							skip=true;break;
						}
					}
					if(!skip)
						navBarDisplayDAO.addItem(item);	
					
				}
			} catch(Exception e ) {
				log.error("Unexpected error", e);
			}
		}
		
		
		// add integrator issues
/*
		if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
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
	public boolean containsIssue(String[]  issues, String issueCode) {
		for (String caseManagementIssue : issues) {
			if (caseManagementIssue.equals(issueCode)) {
					return(true);
			}
		}
		return false;
	}
}

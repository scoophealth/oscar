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

package org.caisi.core.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.caisi.dao.DefaultIssueDao;
import org.caisi.model.DefaultIssue;
import org.oscarehr.casemgmt.dao.IssueDAO;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.log.LogAction;

/**
 * @author Administrator
 *
 */
public class DefaultEncounterIssueAction extends DispatchAction {
	
	
	public ActionForward unspecified(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping,form,request,response);
	}
		
	public ActionForward list(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DefaultIssueDao issueDao = SpringUtils.getBean(DefaultIssueDao.class);
		if (issueDao == null) {
			return mapping.findForward("list");
		}
		List<DefaultIssue> issueList = issueDao.findAll();
		if (issueList != null && issueList.size() > 0) {
			request.setAttribute("issueList", (issueList != null) ? issueList : new ArrayList<DefaultIssue>());
		}
		return mapping.findForward("list");
	}
	
	private boolean saveDefaultIssuesToDb(String providerNo, String issueIds) {
		if (issueIds == null || issueIds.length() == 0) {
			return false;
		}
		DefaultIssueDao issueDao = SpringUtils.getBean(DefaultIssueDao.class);
		if (issueDao == null) {
			return false;
		}
		
		DefaultIssue defaultIssue = issueDao.getLastestDefaultIssue();
		if (defaultIssue == null) {
			defaultIssue = new DefaultIssue();
			defaultIssue.setAssignedtime(new Date());
		}
		defaultIssue.setUpdatetime(new Date());
		defaultIssue.setProviderNo(providerNo);
		defaultIssue.setIssueIds(issueIds);
		issueDao.saveDefaultIssue(defaultIssue);
		return true;
	}

	public ActionForward edit(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("edit");
	}
	
	public ActionForward editRemove(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return mapping.findForward("editRemove");
	}

	public ActionForward save(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String issueNames = request.getParameter("issueNames");
		if (issueNames == null || issueNames.length() == 0) {
			return mapping.findForward("list");
		}
		IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");
		if (issueDao == null) {
			return mapping.findForward("list");
		}

		Set<Long> issueIdSet = new HashSet<Long>();
		String[] issueNamesArr = issueNames.split(",");
		for (String issueName : issueNamesArr) {
			List<Issue> issueList = issueDao.findIssueBySearch(issueName.trim());
			if (issueList == null || issueList.size() == 0) {
				continue;
			}
			for (Issue issue : issueList) {
				issueIdSet.add(issue.getId());
			}
		}
		
		if (issueIdSet.size() == 0) {
			return mapping.findForward("list");
		}
		
		StringBuilder strIds = new StringBuilder();
		for (Long issueId : issueIdSet) {
			strIds.append(issueId.toString() + ",");
		}
		strIds.deleteCharAt(strIds.length() - 1);
		
		LogAction.log("write", "assign default issues", strIds.toString(), request);
		
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		saveDefaultIssuesToDb(loggedInInfo.getLoggedInProviderNo(), strIds.toString());
		
		return list(mapping,form,request,response);
	}
	
	private void removeDefaultIssues(Set<Long> issueIdSet) {
		DefaultIssueDao issueDao = SpringUtils.getBean(DefaultIssueDao.class);
		if (issueDao == null) {
			return;
		}
		DefaultIssue issue = issueDao.getLastestDefaultIssue();
		if (issue == null) {
			return;
		}
		String issueIds = issue.getIssueIds();
		String[] issueIdArr = issueIds.split(",");
		StringBuilder strIds = new StringBuilder();
		for (String issueId : issueIdArr) {
			try {
				if (!issueIdSet.contains(Long.parseLong(issueId))) { // this issueId needn't to remove 
					strIds.append(issueId + ",");
				}
			} catch (NumberFormatException e) {
				MiscUtils.getLogger().info("Failed to parse the issue id!");
			}
		}
		
		if (strIds.length() == 0) { // delete the default issue's record
			issueDao.remove(issue.getId());
		} else {
			strIds.deleteCharAt(strIds.length() - 1);
			issue.setIssueIds(strIds.toString());
			issueDao.saveDefaultIssue(issue);
		}
	}
	
	public ActionForward remove(ActionMapping mapping,ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String issueNames = request.getParameter("issueNames");
		if (issueNames == null || issueNames.length() == 0) {
			return mapping.findForward("list");
		}
		IssueDAO issueDao = (IssueDAO) SpringUtils.getBean("IssueDAO");
		if (issueDao == null) {
			return mapping.findForward("list");
		}
		
		Set<Long> issueIdSet = new HashSet<Long>();
		String[] issueNamesArr = issueNames.split(",");
		StringBuilder idSb = new StringBuilder();
		for (String issueName : issueNamesArr) {
			List<Issue> issueList = issueDao.findIssueBySearch(issueName.trim());
			if (issueList == null || issueList.size() == 0) {
				continue;
			}
			for (Issue issue : issueList) {
				issueIdSet.add(issue.getId());
				idSb.append(issue.getId().toString() + ",");
			}
		}
		
		if (issueIdSet.size() == 0) {
			return list(mapping,form,request,response);
		}
		
		idSb.deleteCharAt(idSb.length() - 1);
		
		LogAction.log("remove", "remove default issues", idSb.toString(), request);
		
		removeDefaultIssues(issueIdSet);
		
		return list(mapping,form,request,response);
	}
	
}

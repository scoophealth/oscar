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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionForm;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.ProviderManager;
import org.oscarehr.casemgmt.model.CaseManagementCPP;
import org.oscarehr.casemgmt.model.CaseManagementIssue;
import org.oscarehr.casemgmt.model.Issue;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.service.ClientImageManager;
import org.oscarehr.casemgmt.web.formbeans.CaseManagementEntryFormBean;
import org.oscarehr.common.dao.DxDao;
import org.oscarehr.util.SpringUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class BaseCaseManagementEntryAction extends DispatchAction {

	protected String relateIssueString = "Issues related to this note:";


	protected CaseManagementManager caseManagementMgr;
	protected ClientImageManager clientImageMgr;
    protected ProviderManager providerMgr;
    protected DxDao dxDao = (DxDao) SpringUtils.getBean("dxDao");

    public void setProviderManager(ProviderManager pmgr ) {
        this.providerMgr = pmgr;
    }

	public void setClientImageManager(ClientImageManager mgr) {
		this.clientImageMgr = mgr;
	}

	public void setCaseManagementManager(CaseManagementManager caseManagementMgr) {
		this.caseManagementMgr = caseManagementMgr;
	}

	protected String getDemographicNo(HttpServletRequest request) {
		String demono = request.getParameter("demographicNo");
		if (demono == null || "".equals(demono)) {
			demono = (String) request.getAttribute("casemgmt_DemoNo");
		} else {
			request.setAttribute("casemgmt_DemoNo", demono);
		}
		return demono;
	}

	protected String getDemoName(String demoNo) {
		if (demoNo == null) {
			return "";
		}
		return caseManagementMgr.getDemoName(demoNo);
	}

	protected String getDemoSex(String demoNo) {
            if(demoNo == null) {
                return "";
            }
            return caseManagementMgr.getDemoGender(demoNo);
        }

        protected String getDemoAge(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoAge(demoNo);
	}

	protected String getDemoDOB(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoDOB(demoNo);
	}


	protected boolean inCaseIssue(Issue iss, List<CaseManagementIssue> issues) {
		Iterator<CaseManagementIssue> itr = issues.iterator();
		while (itr.hasNext())
		{
			CaseManagementIssue cIss = itr.next();
			if (iss.getId().longValue() == cIss.getIssue_id())
				return true;
		}
		return false;
	}

	protected void SetChecked(CheckBoxBean[] checkedlist, int id) {
		for (int i = 0; i < checkedlist.length; i++)
		{
			if (checkedlist[i].getIssue().getId().intValue() == id)
			{
				checkedlist[i].setChecked("on");
				break;
			}
		}
	}

	protected boolean inCheckList(Long id, int[] list)	{
		boolean ret = false;
		for (int i = 0; i < list.length; i++) {
			if (list[i] == id.intValue())
				ret = true;
		}
		return ret;
	}

	protected WebApplicationContext getSpringContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
	}

	/* remove related issue list from note */
	protected String removeCurrentIssue(String noteString) {

		noteString = noteString.replaceAll("\r\n", "\n");
		noteString = noteString.replaceAll("\r", "\n");
		String rt = noteString;
		int index = noteString.indexOf("\n[" + relateIssueString);
		if (index >= 0) {
			String begString = noteString.substring(0, index);
			String endString = noteString.substring(index);
			endString = endString.substring(endString.indexOf("]\n") + 2);
			rt = begString + endString;
		}
		return rt;
	}

	/* remove signiature string */
	protected String removeSignature(String note) {

		note = note.replaceAll("\r\n", "\n");
		note = note.replaceAll("\r", "\n");
		String rt = note;
		String subStr = "\n[[";
		int indexb = note.lastIndexOf(subStr);
		if (indexb >= 0) {
			String subNote = note.substring(indexb);
			int indexe = subNote.indexOf("]]\n");
			if (indexe < 0)
				return rt;
			String begNote = note.substring(0, indexb);
			String endNote = subNote.substring(indexe + 3);
			// String midNote = subNote.substring(subStr.length());
			// String[] sp = midNote.split(" ");
			// midNote = "[" + sp[0] + " " + sp[1] + "]";
			rt = begNote + endNote;
		}
		return rt;
	}

	/* create related issue string */
	protected String createIssueString(Set<CaseManagementIssue> issuelist) {
		if (issuelist.isEmpty())
			return "";
		String rt = "\n[" + relateIssueString;
		Iterator<CaseManagementIssue> itr = issuelist.iterator();
		while (itr.hasNext()) {
			CaseManagementIssue iss =  itr.next();
			rt = rt + "\n" + iss.getIssue().getDescription() + "\t\t\n";
			if (iss.isCertain())
				rt = rt + "certain" + "  ";
			else
				rt = rt + "uncertain" + "  ";
			if (iss.isAcute())
				rt = rt + "acute" + "  ";
			else
				rt = rt + "chronic" + "  ";
			if (iss.isMajor())
				rt = rt + "major" + "  ";
			else
				rt = rt + "not major" + "  ";
			if (iss.isResolved())
				rt = rt + "resolved";
			else
				rt = rt + "unresolved";
		}
		return rt + "]\n";
	}

        protected CaseManagementIssue newIssueToCIssue(String demoNo, Issue iss, Integer programId) {
            	CaseManagementIssue cIssue = new CaseManagementIssue();
		// cIssue.setActive(true);
		cIssue.setAcute(false);
		cIssue.setCertain(false);
		cIssue.setDemographic_no(Integer.valueOf(demoNo));

		cIssue.setIssue_id(iss.getId().longValue());

		cIssue.setIssue(iss);
		cIssue.setMajor(false);
		// cIssue.setMedical_diagnosis(true);
		cIssue.setNotes(new HashSet());
		cIssue.setResolved(false);
		String issueType = iss.getRole();
		cIssue.setType(issueType);
		cIssue.setUpdate_date(new Date());
		cIssue.setProgram_id(programId);
		// add it to database
		List<CaseManagementIssue> uList = new ArrayList<CaseManagementIssue>();
		uList.add(cIssue);
		caseManagementMgr.saveAndUpdateCaseIssues(uList);
		// add new issues to ongoing concern
		//caseManagementMgr.addNewIssueToConcern((String) cform.getDemoNo(), iss.getDescription());
		return cIssue;
        }

	/**
	 * @param programId is optional, can be null for none.
	 */
	protected CaseManagementIssue newIssueToCIssue(CaseManagementEntryFormBean cform, Issue iss, Integer programId) {
            return newIssueToCIssue(cform.getDemoNo(),iss,programId);
	}

	protected Map<Long,CaseManagementIssue> convertIssueListToMap(List<CaseManagementIssue> issueList) {
		Map<Long,CaseManagementIssue> map = new HashMap<Long,CaseManagementIssue>();
		for(Iterator<CaseManagementIssue> iter=issueList.iterator();iter.hasNext();) {
			CaseManagementIssue issue = iter.next();
			map.put(issue.getIssue().getId(), issue);
		}
		return map;
	}

	protected void updateIssueToConcern(ActionForm form) {
		CaseManagementEntryFormBean cform = (CaseManagementEntryFormBean) form;
		CheckBoxBean[] oldList = cform.getIssueCheckList();
		List<CaseManagementIssue> caseiss = new ArrayList<CaseManagementIssue>();
		for (int i = 0; i < oldList.length; i++) {
			if (!oldList[i].getIssue().isResolved())
				caseiss.add(oldList[i].getIssue());
		}
		String demoNo = cform.getDemoNo();
		caseManagementMgr.updateCurrentIssueToCPP(demoNo, caseiss);
	}

	//TODO: update access model
	public void setCPPMedicalHistory(CaseManagementCPP cpp, String providerNo,List accessRight)	{

		if (caseManagementMgr.greaterEqualLevel(3, providerNo))	{
			String mHis = cpp.getMedicalHistory();
			if(mHis!=null) {
				mHis = mHis.replaceAll("\r\n", "\n");
				mHis = mHis.replaceAll("\r", "\n");
			}
			List<CaseManagementIssue> allIssues = caseManagementMgr.getIssues(Integer.parseInt(cpp.getDemographic_no()));

			Iterator<CaseManagementIssue> itr = allIssues.iterator();
			while (itr.hasNext()) {
				CaseManagementIssue cis = itr.next();
				String issustring = cis.getIssue().getDescription();
				if (cis.isMajor() && cis.isResolved()) {
					if (mHis!=null && mHis.indexOf(issustring) < 0)
						mHis = mHis + issustring + ";\n";
				} else {

					if (mHis!=null && mHis.indexOf(issustring) >= 0)
						mHis = mHis.replaceAll(issustring + ";\n", "");
				}
			}
			if(mHis!=null) {
				mHis = mHis.replaceAll("\r\n", "\n");
				mHis = mHis.replaceAll("\r", "\n");
			}
			cpp.setMedicalHistory(mHis);
		}
	}

}

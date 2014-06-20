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
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.service.AdmissionManager;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.service.SurveyManager;
import org.oscarehr.casemgmt.model.CaseManagementNote;
import org.oscarehr.casemgmt.model.ClientImage;
import org.oscarehr.casemgmt.service.CaseManagementManager;
import org.oscarehr.casemgmt.service.ClientImageManager;
import org.oscarehr.util.SpringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.quatro.service.security.RolesManager;

public class BaseCaseManagementViewAction extends DispatchAction {

	protected CaseManagementManager caseManagementMgr;
	protected ClientImageManager clientImageMgr;
	protected RolesManager roleMgr;
	protected ProgramManager programMgr;
	protected AdmissionManager admissionMgr;
	protected SurveyManager surveyMgr = (SurveyManager)SpringUtils.getBean("surveyManager2");


	public ApplicationContext getAppContext() {
		return WebApplicationContextUtils.getWebApplicationContext(getServlet().getServletContext());
	}

	public AdmissionManager getAdmissionManager() {
		return (AdmissionManager) getAppContext().getBean("admissionManager");
	}

	public void setAdmissionManager(AdmissionManager admMgr){
		this.admissionMgr = admMgr;
	}

	public void setCaseManagementManager(CaseManagementManager caseManagementMgr) {
		this.caseManagementMgr = caseManagementMgr;
	}

	public void setClientImageManager(ClientImageManager mgr) {
		this.clientImageMgr = mgr;
	}

	public void setRolesManager(RolesManager mgr) {
		this.roleMgr = mgr;
	}

	public void setProgramManager(ProgramManager mgr) {
		this.programMgr = mgr;
	}


	public String getDemographicNo(HttpServletRequest request) {
		String demono= request.getParameter("demographicNo");
		if (demono==null || "".equals(demono))
			demono=(String)request.getSession().getAttribute("casemgmt_DemoNo");
		else
			request.getSession().setAttribute("casemgmt_DemoNo", demono);
		return demono;
	}

	public String getDemoName(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoName(demoNo);
	}

	public String getDemoAge(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoAge(demoNo);
	}

	public String getDemoDOB(String demoNo){
		if (demoNo==null) return "";
		return caseManagementMgr.getDemoDOB(demoNo);
	}

	public String getProviderNo(HttpServletRequest request){
		String providerNo=request.getParameter("providerNo");
		if (providerNo==null)
			providerNo=(String)request.getSession().getAttribute("user");
		return providerNo;
	}

	@Deprecated
    public int getProviderId(HttpServletRequest request){
        return(Integer.parseInt(getProviderNo(request)));
    }

	protected String getImageFilename(String demoNo, HttpServletRequest request) {
		ClientImage img = clientImageMgr.getClientImage(Integer.parseInt(demoNo));

		if(img != null) {
			String path=request.getSession().getServletContext().getRealPath("/");
			int encodedValue = (int)(Math.random()*Integer.MAX_VALUE);
			String filename = "client" +encodedValue+"."+ img.getImage_type();
			try {
				java.io.FileOutputStream os= new java.io.FileOutputStream(path+"/images/"+filename);
				os.write(img.getImage_data());
				os.flush();
				os.close();
				return filename;
			}catch(Exception e) {
				log.warn("Warning", e);
			}
		}
		return null;
	}

	public List<CaseManagementNote> notesOrderByDate(List<CaseManagementNote> notes,String providerNo,String demoNo)
	{
		List<CaseManagementNote> rtNotes=new ArrayList<CaseManagementNote>();
		int noteSize=notes.size();
		for (int i=0; i<noteSize; i++){
			Iterator<CaseManagementNote> itr=notes.iterator();
			CaseManagementNote inote= itr.next();

			// check note access here.
			if(inote.getProgram_no() == null || inote.getProgram_no().length()==0) {
				//didn't save this data at this time - older note
				rtNotes.add(inote);
			} else {
				if(caseManagementMgr.hasAccessRight(removeFirstSpace(caseManagementMgr.getCaisiRoleById(inote.getReporter_caisi_role()))+"notes","access",providerNo,demoNo,inote.getProgram_no())){
					rtNotes.add(inote);
				}
			}

			notes.remove(inote);

		}
		return rtNotes;
	}

	String removeFirstSpace(String withSpaces) {
        int spaceIndex = withSpaces.indexOf(' '); //use lastIndexOf to remove last space
        if (spaceIndex < 0) { //no spaces!
            return withSpaces;
        }
        return withSpaces.substring(0, spaceIndex)
            + withSpaces.substring(spaceIndex+1, withSpaces.length());
    }

	protected void addMessage(HttpServletRequest request, String key) {
		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(key));
		saveMessages(request, messages);
	}

}

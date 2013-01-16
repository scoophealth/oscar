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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.util.LabelValueBean;
import org.caisi.service.InfirmBedProgramManager;
import org.oscarehr.common.model.Admission;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.myoscar.util.MiscUtils;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SessionConstants;

import oscar.OscarProperties;

public class InfirmAction extends BaseAction
{
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(InfirmAction.class);

	public ActionForward showProgram(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		logger.debug("====> inside showProgram action.");



		HttpSession se = request.getSession();
		se.setAttribute("infirmaryView_initflag", "true");
		String providerNo=(String) se.getAttribute("user");

		//clear memory for programbean
		//List memob=(List) se.getAttribute("infirmaryView_demographicBeans");
		//if (memob!=null) memob.clear();

		List programBean;

		String archiveView = (String)request.getSession().getAttribute("archiveView");
		/*
		if(archiveView != null && archiveView.equals("true")){

			ProgramManager manager = getProgramManager();
			programBean = manager.getProgramBeans(providerNo);
			se.setAttribute("infirmaryView_programBeans",programBean );
		}
		else {
			InfirmBedProgramManager manager=getInfirmBedProgramManager();
			programBean=manager.getProgramBeans(providerNo);
			se.setAttribute("infirmaryView_programBeans",programBean );
		}
		*/
		InfirmBedProgramManager manager=getInfirmBedProgramManager();
		Integer facilityId=null;

		// facility filtering
        if (OscarProperties.getInstance().getBooleanProperty("FILTER_ON_FACILITY", "true")) {
        	LoggedInInfo loggedInInfo=LoggedInInfo.loggedInInfo.get();
            facilityId = loggedInInfo.currentFacility.getId();
        }

		programBean=manager.getProgramBeans(providerNo, facilityId);
		//programBean_oscarClinic = manager.getProgramForApptViewBeans(providerNo, facilityId);
		//if(OscarProperties.getInstance().getBooleanProperty("oscarClinic", "true")) {
		//	se.setAttribute("infirmaryView_programBeans",programBean_oscarClinic);
		//} else {
			se.setAttribute("infirmaryView_programBeans",programBean);
		//}

		//set default program
		int defaultprogramId=getInfirmBedProgramManager().getDefaultProgramId(providerNo);
		boolean defaultInList=false;
		for (int i=0;i<programBean.size();i++){
			int id=new Integer(((LabelValueBean) programBean.get(i)).getValue()).intValue();
			if ( defaultprogramId == id ) defaultInList=true;
		}
		if (!defaultInList) defaultprogramId=0;
		int OriprogramId=0;
		if (programBean.size()>0) OriprogramId=new Integer(((LabelValueBean) programBean.get(0)).getValue()).intValue();
		int programId=0;
		if (defaultprogramId!=0 && OriprogramId!=0) programId=defaultprogramId;
		else {
			if (OriprogramId==0) programId=0;
			if (defaultprogramId==0 && OriprogramId!=0) programId=OriprogramId;
		}
		if (se.getAttribute(SessionConstants.CURRENT_PROGRAM_ID)!=null){
			programId=Integer.valueOf((String)se.getAttribute(SessionConstants.CURRENT_PROGRAM_ID)).intValue();
		}
		//if (programId!=defaultprogramId) getInfirmBedProgramManager().setDefaultProgramId(providerNo,programId);

		se.setAttribute(SessionConstants.CURRENT_PROGRAM_ID,String.valueOf(programId));

		//if()
		if(programId != 0) {
			se.setAttribute("case_program_id",String.valueOf(programId));
		}

		if(programId != 0) {
			ProgramManager programManager = getProgramManager();
			se.setAttribute("program_client_statuses",programManager.getProgramClientStatuses(new Integer(programId)));

		}


		String[] programInfo = getInfirmBedProgramManager().getProgramInformation(programId);
		if(programInfo[0] != null) {
			se.setAttribute("infirmaryView_programAddress",programInfo[0].replaceAll("\\n", "<br>"));
		} else {
			se.setAttribute("infirmaryView_programAddress","");
		}
		if(programInfo[1] != null) {
			se.setAttribute("infirmaryView_programTel",programInfo[1]);
		} else {
			se.setAttribute("infirmaryView_programTel","");
		}
		if(programInfo[2] != null) {
			se.setAttribute("infirmaryView_programFax",programInfo[2]);
		} else {
			se.setAttribute("infirmaryView_programFax","");
		}

		Date dt;

		if (se.getAttribute("infirmaryView_date")!=null)
		{
			dt=(Date) se.getAttribute("infirmaryView_date") ; //new Date(year,month-1,day);
		}else{
			dt=new Date();
		}
		
		//lets not load the demographic beans when in appointment view..no point
		org.oscarehr.PMmodule.model.Program p = this.getProgramManager().getProgram(programId);
		if(p != null) {
			String exclusiveView = p.getExclusiveView();
			
			if(exclusiveView != null && exclusiveView.equals("appointment"))
				return null;
		}
		
		//release memory
		//List memo=(List) se.getAttribute("infirmaryView_demographicBeans");
		//if (memo!=null) memo.clear();
		List<LabelValueBean> demographicBeans = getInfirmBedProgramManager().getDemographicByBedProgramIdBeans(programId,dt,archiveView);
		List<LabelValueBean> filteredDemographicBeans = new ArrayList<LabelValueBean>();
		if( request.getParameter("infirmaryView_clientStatusId") != null) {
			//int statusId = new Integer(request.getParameter("infirmaryView_clientStatusId")).intValue();
			String statusId = request.getParameter("infirmaryView_clientStatusId");
			if(statusId.equals("0")){
				filteredDemographicBeans = demographicBeans;
			}
			else {
			Admission admission = new Admission();
			List<Admission> admissions = new ArrayList<Admission>();
			Integer csi;
			for(Iterator<LabelValueBean> iter=demographicBeans.iterator();iter.hasNext();) {
				LabelValueBean bean = iter.next();
				String demographicNo = bean.getValue();
				admission = null;
				admissions = null;
				//admission = getAdmissionManager().getAdmission(String.valueOf(programId), new Integer(demographicNo));
				if(archiveView!=null && archiveView.equals("true")){
					admissions = getAdmissionManager().getAdmissions_archiveView(String.valueOf(programId), new Integer(demographicNo));
					for(Iterator<Admission> i1=admissions.iterator();i1.hasNext();) {
						admission = i1.next();
						csi = admission.getClientStatusId();
						if(csi==null)
							csi = 0;
						if(statusId!=null && statusId.equals(csi.toString())){
							filteredDemographicBeans.add(bean);
							break;
						}
					}

				}
				else {
					admission = getAdmissionManager().getCurrentAdmission(String.valueOf(programId), new Integer(demographicNo));
					if (admission != null)
					{
						csi = admission.getClientStatusId();
						if(csi==null)
							csi=0;
						if(statusId!=null && statusId.equals(csi.toString())){
							filteredDemographicBeans.add(bean);
						}
					}
				}
			}
			request.setAttribute("infirmaryView_clientStatusId",statusId );
			}
		} else {
			filteredDemographicBeans = demographicBeans;
		}
		se.setAttribute("infirmaryView_demographicBeans",filteredDemographicBeans);

		/*java.util.Enumeration enu =  se.getAttributeNames();
		while (enu.hasMoreElements())
			logger.info(enu.nextElement());
		*/
//		response.sendRedirect(se.getAttribute("infirmaryView_OscarURL")+"?"+se.getAttribute("infirmaryView_OscarQue"));
		return null;
	}

	public ActionForward getSig(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("====> inside getSig action.");
		String providerNo=request.getParameter("providerNo");
		if (providerNo==null) providerNo=(String) request.getSession().getAttribute("user");
		Boolean onsig=getInfirmBedProgramManager().getProviderSig(providerNo);
		request.getSession().setAttribute("signOnNote",onsig);
		int pid = getInfirmBedProgramManager().getDefaultProgramId(providerNo);

		/*Disable schedule view associated with the program. Make the default program id equal to "0".
		String ppid1 = (String)request.getSession().getAttribute("programId_oscarView");
		if(ppid1==null) {
			request.getSession().setAttribute("programId_oscarView",String.valueOf(pid));
		} else {
			request.getSession().setAttribute("programId_oscarView", ppid1);
			//getInfirmBedProgramManager().setDefaultProgramId(providerNo,Integer.valueOf(ppid).intValue());
		}
		*/
		request.getSession().setAttribute("programId_oscarView","0");

		String ppid = (String)request.getSession().getAttribute("case_program_id");
		if(ppid==null) {
			request.getSession().setAttribute("case_program_id",String.valueOf(pid));
		} else {
			request.getSession().setAttribute("case_program_id", ppid);
			//getInfirmBedProgramManager().setDefaultProgramId(providerNo,Integer.valueOf(ppid).intValue());
		}

		return null;
	}

	public ActionForward toggleSig(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)

	{
		logger.debug("====> inside toggleSig action.");
		String providerNo=request.getParameter("providerNo");
		if (providerNo==null) providerNo=(String) request.getSession().getAttribute("user");
		Boolean onsig=getInfirmBedProgramManager().getProviderSig(providerNo);
		request.getSession().setAttribute("signOnNote",onsig);
		return null;
	}
	
	public ActionForward getProgramList(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		String providerNo = request.getParameter("providerNo");
		String facilityId = request.getParameter("facilityId");
		
		String outTxt = "<option value='all'>All Programs</option>";
		PrintWriter out;
        try {
	        out = response.getWriter();
        } catch (IOException e) {
	        MiscUtils.getLogger().error(e.getMessage(),e);
	        return null;
        }
		out.print(outTxt);
		if (providerNo==null || facilityId==null || providerNo.isEmpty() || facilityId.isEmpty()) {
			return null;
		}
		int facility_id = 1;
		try {
			facility_id = Integer.parseInt(facilityId);
		} catch (NumberFormatException e) {
			MiscUtils.getLogger().error(e.getMessage(),e);
			return null;
		}
		InfirmBedProgramManager manager=getInfirmBedProgramManager();
		List<LabelValueBean> programBeans = null;
		if (providerNo.equalsIgnoreCase("all")) {
			programBeans = manager.getProgramBeansByFacilityId(facility_id);
		} else {
			programBeans = manager.getProgramBeans(providerNo, facility_id);
		}
		for (LabelValueBean pb : programBeans) {
			out.print(String.format("<option value='%s'>%s</option>", pb.getValue(), pb.getLabel()));
		}
		
		return null;
	}

}

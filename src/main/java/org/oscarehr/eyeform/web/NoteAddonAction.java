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


package org.oscarehr.eyeform.web;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.caisi.model.Tickler;
import org.caisi.service.TicklerManager;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.eyeform.dao.EyeFormDao;
import org.oscarehr.eyeform.dao.FollowUpDao;
import org.oscarehr.eyeform.dao.ProcedureBookDao;
import org.oscarehr.eyeform.dao.TestBookRecordDao;
import org.oscarehr.eyeform.model.EyeForm;
import org.oscarehr.eyeform.model.EyeformFollowUp;
import org.oscarehr.eyeform.model.EyeformProcedureBook;
import org.oscarehr.eyeform.model.EyeformTestBook;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

public class NoteAddonAction extends DispatchAction {

	static Logger logger = Logger.getLogger(NoteAddonAction.class);
	
	private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	
	public ActionForward getCurrentNoteData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");
		String noteId = request.getParameter("noteId");
		String appointmentNo = request.getParameter("appointmentNo");
		
		request.setAttribute("internalList", providerDao.getActiveProviders());

		//load up tests/procedures and extra info
		FollowUpDao followUpDao = (FollowUpDao)SpringUtils.getBean("FollowUpDAO");
		TestBookRecordDao testDao = (TestBookRecordDao)SpringUtils.getBean("TestBookDAO");
		ProcedureBookDao procedureBookDao = (ProcedureBookDao)SpringUtils.getBean("ProcedureBookDAO");
		EyeFormDao eyeformDao = (EyeFormDao)SpringUtils.getBean("eyeFormDao");
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
		
		List<EyeformFollowUp> followUps = followUpDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
		for(EyeformFollowUp fu:followUps) {
			fu.setProvider(providerDao.getProvider(fu.getFollowupProvider()));
			fu.setDemographic(demographicDao.getDemographic(String.valueOf(fu.getDemographicNo())));			
		}		
		request.setAttribute("followUps",followUps);
		
		List<EyeformTestBook> testBookRecords = testDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
		request.setAttribute("testBookRecords",testBookRecords);
		
		List<EyeformProcedureBook> procedures = procedureBookDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
		request.setAttribute("procedures",procedures);
		
		EyeForm eyeform = eyeformDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
		request.setAttribute("eyeform", eyeform);
		
		
		return mapping.findForward("current_note");
	}
	
	/*
	 * followNo
	 * followFrame
	 * internalNo
	 * ack1 - check
	 * ack2
	 * ack3
	 * 
	 * procedure/test
	 */	
	
	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String appointmentNo = request.getParameter("appointmentNo");
	
		String ack1Checked = request.getParameter("ack1_checked");
		String ack2Checked = request.getParameter("ack2_checked");
		String ack3Checked = request.getParameter("ack3_checked");
		
		
		EyeFormDao dao = (EyeFormDao)SpringUtils.getBean("EyeFormDao");
		
		EyeForm eyeform = dao.getByAppointmentNo(Integer.parseInt(appointmentNo));
	
		if(eyeform == null) {
			eyeform = new EyeForm();
			eyeform.setDate(new Date());
			eyeform.setAppointmentNo(Integer.parseInt(appointmentNo));			
		}
		
		eyeform.setDischarge(ack1Checked);
		eyeform.setStat(ack2Checked);
		eyeform.setOpt(ack3Checked);
		
		dao.save(eyeform);
		
		return null;
	}
	
	public ActionForward getNoteText(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
    	String appointmentNo = request.getParameter("appointmentNo");
    	
    	EyeFormDao dao = (EyeFormDao)SpringUtils.getBean("EyeFormDao");
    	
    	EyeForm eyeform = dao.getByAppointmentNo(Integer.parseInt(appointmentNo));
    	StringBuilder sb = new StringBuilder();
    	
    	if(eyeform != null) {
    		if(eyeform.getDischarge()!=null&&eyeform.getDischarge().equals("true")) {
    			sb.append("Patient is discharged from my active care.");    		
    			sb.append("\n");
    		}
    		
    		if(eyeform.getStat()!=null&&eyeform.getStat().equals("true")) {
    			sb.append("Follow up as needed with me STAT or PRN if symptoms are worse.");    		
    			sb.append("\n");
    		}
    		
    		if(eyeform.getOpt()!=null&&eyeform.getOpt().equals("true")) {
    			sb.append("Routine eye care by an optometrist is recommended.");    		
    			sb.append("\n");
    		}
    	
    	}
    	
    	try {
    		response.getWriter().print(sb.toString());
    	}catch(IOException e) {logger.error(e);}
    	return null;
    }
	
	/*
	 * appointmentNo
	 * test
	 * recip
	 */
	
	public ActionForward sendTickler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String aptNo = request.getParameter("appointmentNo");
		String text = request.getParameter("text");
		String recipient = request.getParameter("recip");
		String demographicNo = request.getParameter("demographicNo");
				
		
		Tickler t = new Tickler();
		t.setCreator(LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo());
		t.setAssignee(providerDao.getProvider(recipient));
		//t.setDemographic(demographic)
		t.setDemographic_no(demographicNo);
		t.setMessage(text);
		t.setPriority("Normal");
		t.setProvider(LoggedInInfo.loggedInInfo.get().loggedInProvider);
		//t.setProgram(program);
		t.setProgram_id(Integer.valueOf((String)request.getSession().getAttribute("programId_oscarView")));
		t.setService_date(new Date());
		t.setStatus('A');
		t.setTask_assigned_to(recipient);
		t.setUpdate_date(new Date());
		
		TicklerManager ticklerMgr = (TicklerManager)SpringUtils.getBean("ticklerManagerT");
		ticklerMgr.addTickler(t);
		
		
		
	    return null;
	}
	
}

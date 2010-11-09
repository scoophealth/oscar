package org.oscarehr.eyeform.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.eyeform.dao.FollowUpDao;
import org.oscarehr.eyeform.dao.ProcedureBookDao;
import org.oscarehr.eyeform.dao.TestBookRecordDao;
import org.oscarehr.eyeform.model.FollowUp;
import org.oscarehr.eyeform.model.ProcedureBook;
import org.oscarehr.eyeform.model.TestBookRecord;
import org.oscarehr.util.SpringUtils;

public class NoteAddonAction extends DispatchAction {

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
		
		ProviderDao providerDao = (ProviderDao)SpringUtils.getBean("providerDao");
		DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
		
		List<FollowUp> followUps = followUpDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
		for(FollowUp fu:followUps) {
			fu.setProvider(providerDao.getProvider(fu.getFollowupProvider()));
			fu.setDemographic(demographicDao.getDemographic(String.valueOf(fu.getDemographicNo())));			
		}		
		request.setAttribute("followUps",followUps);
		
		List<TestBookRecord> testBookRecords = testDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
		request.setAttribute("testBookRecords",testBookRecords);
		
		List<ProcedureBook> procedures = procedureBookDao.getByAppointmentNo(Integer.parseInt(appointmentNo));
		request.setAttribute("procedures",procedures);
		
		
		
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
		return null;
	}
}

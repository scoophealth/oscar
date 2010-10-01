package org.oscarehr.eyeform.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.util.SpringUtils;

public class NoteAddonAction extends DispatchAction {

	private static ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
	
	public ActionForward getCurrentNoteData(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String demographicNo = request.getParameter("demographicNo");
		String noteId = request.getParameter("noteId");
		
		request.setAttribute("internalList", providerDao.getActiveProviders());

		//load up tests/procedures and extra info
		
		
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

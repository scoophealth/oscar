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


package oscar.facility;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.dao.IntegratorControlDao;
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;

public class FacilityManagerAction extends DispatchAction {

	private FacilityDao facilityDao=(FacilityDao) SpringUtils.getBean("facilityDao");
	private IntegratorControlDao integratorControlDao = (IntegratorControlDao) SpringUtils.getBean("integratorControlDao");

	private static final String FORWARD_EDIT = "edit";
	private static final String FORWARD_LIST = "list";
	private static final String BEAN_FACILITIES = "facilities";

	@Override
	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping, form, request, response);
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<Facility> facilities = facilityDao.findAll(true);
		request.setAttribute(BEAN_FACILITIES, facilities);

		return mapping.findForward(FORWARD_LIST);
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Facility facility = facilityDao.find(Integer.valueOf(id));

		FacilityManagerForm managerForm = (FacilityManagerForm) form;
		managerForm.setFacility(facility);

		request.setAttribute("id", facility.getId());
		request.setAttribute("orgId", facility.getOrgId());
		request.setAttribute("sectorId", facility.getSectorId());

		boolean removeDemoId = integratorControlDao.readRemoveDemographicIdentity(Integer.valueOf(id));
		managerForm.setRemoveDemographicIdentity(removeDemoId);

		return mapping.findForward(FORWARD_EDIT);
	}

	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Facility facility = facilityDao.find(Integer.valueOf(id));
		facility.setDisabled(true);
		facilityDao.merge(facility);

		return list(mapping, form, request, response);
	}

	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		Facility facility = new Facility("", "");
		((FacilityManagerForm) form).setFacility(facility);
		((FacilityManagerForm) form).setRemoveDemographicIdentity(true);
		// Ronnie ((FacilityManagerForm) form).setUpdateInterval(0);

		return mapping.findForward(FORWARD_EDIT);
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		oscar.facility.FacilityManagerForm mform = (oscar.facility.FacilityManagerForm) form;
		Facility facility = mform.getFacility();
		
		boolean rdid = WebUtils.isChecked(request, "removeDemographicIdentity");
		if (request.getParameter("facility.hic") == null) facility.setHic(false);

		if (isCancelled(request)) {
			return list(mapping, form, request, response);
		}

		facility.setIntegratorEnabled(WebUtils.isChecked(request, "facility.integratorEnabled"));
		facility.setAllowSims(WebUtils.isChecked(request, "facility.allowSims"));
		facility.setEnableIntegratedReferrals(WebUtils.isChecked(request, "facility.enableIntegratedReferrals"));
		facility.setEnableHealthNumberRegistry(WebUtils.isChecked(request, "facility.enableHealthNumberRegistry"));
		facility.setEnableDigitalSignatures(WebUtils.isChecked(request, "facility.enableDigitalSignatures"));
		if (facility.getId() == null || facility.getId() == 0) facilityDao.persist(facility);
		else facilityDao.merge(facility);

		// if we just updated our current facility, refresh local cached data in the session / thread local variable
		if (loggedInInfo.getCurrentFacility().getId().intValue() == facility.getId().intValue()) {
			request.getSession().setAttribute(SessionConstants.CURRENT_FACILITY, facility);
			loggedInInfo.setCurrentFacility(facility);
		}
		ActionMessages mssgs = new ActionMessages();
		mssgs.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("facility.saved", facility.getName()));
		saveMessages(request, mssgs);
		request.setAttribute("id", facility.getId());

		integratorControlDao.saveRemoveDemographicIdentity(facility.getId(), rdid);

		return list(mapping, form, request, response);
	}
}

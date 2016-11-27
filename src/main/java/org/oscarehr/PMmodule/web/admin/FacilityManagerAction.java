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

package org.oscarehr.PMmodule.web.admin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.service.ProgramManager;
import org.oscarehr.PMmodule.web.FacilityDischargedClients;
import org.oscarehr.common.dao.AdmissionDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.EFormDao;
import org.oscarehr.common.dao.FacilityDao;
import org.oscarehr.common.model.Admission;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.Facility;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SessionConstants;
import org.oscarehr.util.SpringUtils;
import org.oscarehr.util.WebUtils;

import oscar.log.LogAction;

import com.quatro.service.LookupManager;

/**
 */
public class FacilityManagerAction extends DispatchAction {

	private AdmissionDao admissionDao;
	private DemographicDao demographicDao;
	private ProgramManager programManager;
	private LookupManager lookupManager;
	
	private FacilityDao facilityDao;
    private EFormDao eFormDao = (EFormDao) SpringUtils.getBean("EFormDao");

	public void setFacilityDao(FacilityDao facilityDao) {
		this.facilityDao = facilityDao;
	}

	private static final String FORWARD_EDIT = "edit";
	private static final String FORWARD_VIEW = "view";
	private static final String FORWARD_LIST = "list";

	private static final String BEAN_FACILITIES = "facilities";
	private static final String BEAN_ASSOCIATED_PROGRAMS = "associatedPrograms";
	private static final String BEAN_ASSOCIATED_CLIENTS = "associatedClients";
    private static final String registrationIntakeName = "Registration Intake";

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return list(mapping, form, request, response);
	}

	public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		List<Facility> facilities = facilityDao.findAll(true);

		request.setAttribute(BEAN_FACILITIES, facilities);

		// get agency's organization list from caisi editor table
		request.setAttribute("orgList", lookupManager.LoadCodeList("OGN", true, null, null));

		// get agency's sector list from caisi editor table
		request.setAttribute("sectorList", lookupManager.LoadCodeList("SEC", true, null, null));

		return mapping.findForward(FORWARD_LIST);
	}

	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String idStr = request.getParameter("id");
		Integer id = Integer.valueOf(idStr);
		Facility facility = facilityDao.find(id);

		FacilityManagerForm facilityForm = (FacilityManagerForm) form;
		facilityForm.setFacility(facility);
        facilityForm.setRegistrationIntakeForms(eFormDao.getEfromInGroupByGroupName(registrationIntakeName));

		List<FacilityDischargedClients> facilityClients = new ArrayList<FacilityDischargedClients>();

		// Get program list by facility id in table room.
		for (Program program : programManager.getPrograms(id)) {
			if (program != null) {
				// Get admission list by program id and automatic_discharge=true

				List<Admission> admissions = admissionDao.getAdmissionsByProgramId(program.getId(), new Boolean(true), new Integer(-7));
				if (admissions != null) {
					Iterator<Admission> it = admissions.iterator();
					while (it.hasNext()) {

						Admission admission = it.next();

						// Get demographic list by demographic_no
						Demographic client = demographicDao.getClientByDemographicNo(admission.getClientId());

						String name = client.getFirstName() + " " + client.getLastName();
						String dob = client.getFormattedDob();
						String pName = program.getName();
						Date dischargeDate = admission.getDischargeDate();
						String dDate = dischargeDate.toString();

						// today's date
						Calendar calendar = Calendar.getInstance();

						// today's date - days
						calendar.add(Calendar.DAY_OF_YEAR, -1);

						Date oneDayAgo = calendar.getTime();

						FacilityDischargedClients fdc = new FacilityDischargedClients();
						fdc.setName(name);
						fdc.setDob(dob);
						fdc.setProgramName(pName);
						fdc.setDischargeDate(dDate);

						if (dischargeDate.after(oneDayAgo)) {
							fdc.setInOneDay(true);
						} else {
							fdc.setInOneDay(false);
						}
						facilityClients.add(fdc);

					}
				}
			}
		}
		request.setAttribute(BEAN_ASSOCIATED_CLIENTS, facilityClients);

		request.setAttribute(BEAN_ASSOCIATED_PROGRAMS, programManager.getPrograms(id));

		request.setAttribute("id", facility.getId());

		return mapping.findForward(FORWARD_VIEW);
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Facility facility = facilityDao.find(Integer.valueOf(id));

		FacilityManagerForm managerForm = (FacilityManagerForm) form;
		managerForm.setFacility(facility);
        managerForm.setRegistrationIntakeForms(eFormDao.getEfromInGroupByGroupName(registrationIntakeName));

		request.setAttribute("id", facility.getId());
		request.setAttribute("orgId", facility.getOrgId());
		request.setAttribute("sectorId", facility.getSectorId());

		// get agency's organization list from caisi editor table
		request.setAttribute("orgList", lookupManager.LoadCodeList("OGN", true, null, null));

		// get agency's sector list from caisi editor table
		request.setAttribute("sectorList", lookupManager.LoadCodeList("SEC", true, null, null));

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

		// get agency's organization list from caisi editor table
		request.setAttribute("orgList", lookupManager.LoadCodeList("OGN", true, null, null));

		// get agency's sector list from caisi editor table
		request.setAttribute("sectorList", lookupManager.LoadCodeList("SEC", true, null, null));

		return mapping.findForward(FORWARD_EDIT);
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		FacilityManagerForm mform = (FacilityManagerForm) form;
		Facility facility = mform.getFacility();

		if (request.getParameter("facility.hic") == null) facility.setHic(false);

		if (isCancelled(request)) {
			request.getSession().removeAttribute("facilityManagerForm");

			return list(mapping, form, request, response);
		}

		try {
			facility.setIntegratorEnabled(WebUtils.isChecked(request, "facility.integratorEnabled"));
			facility.setAllowSims(WebUtils.isChecked(request, "facility.allowSims"));
			facility.setEnableIntegratedReferrals(WebUtils.isChecked(request, "facility.enableIntegratedReferrals"));
			facility.setEnableHealthNumberRegistry(WebUtils.isChecked(request, "facility.enableHealthNumberRegistry"));
			facility.setEnableDigitalSignatures(WebUtils.isChecked(request, "facility.enableDigitalSignatures"));

			facility.setEnableAnonymous(WebUtils.isChecked(request, "facility.enableAnonymous"));
            facility.setEnablePhoneEncounter(WebUtils.isChecked(request, "facility.enablePhoneEncounter"));
			facility.setEnableGroupNotes(WebUtils.isChecked(request, "facility.enableGroupNotes"));
			facility.setEnableOcanForms(WebUtils.isChecked(request, "facility.enableOcanForms"));
			facility.setEnableEncounterTime(WebUtils.isChecked(request, "facility.enableEncounterTime"));
			facility.setEnableEncounterTransportationTime(WebUtils.isChecked(request, "facility.enableEncounterTransportationTime"));
			facility.setEnableCbiForm(WebUtils.isChecked(request, "facility.enableCbiForm"));
			if(facility.getRegistrationIntake()!=null&&facility.getRegistrationIntake()<0)facility.setRegistrationIntake(null);
			
			if (facility.getId() == null || facility.getId() == 0) facilityDao.persist(facility);
			else facilityDao.merge(facility);

			// if we just updated our current facility, refresh local cached data in the session / thread local variable
			if (loggedInInfo.getCurrentFacility().getId().intValue()==facility.getId().intValue())
			{
				request.getSession().setAttribute(SessionConstants.CURRENT_FACILITY, facility);
				loggedInInfo.setCurrentFacility(facility);
			}
			
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("facility.saved", facility.getName()));
			saveMessages(request, messages);

			request.setAttribute("id", facility.getId());

			LogAction.log("write", "facility", facility.getId().toString(), request);

			return list(mapping, form, request, response);
		} catch (Exception e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("duplicateKey", "The name " + facility.getName()));
			saveMessages(request, messages);

			return mapping.findForward(FORWARD_EDIT);
		}
	}

	public void setAdmissionDao(AdmissionDao admissionDao) {
		this.admissionDao = admissionDao;
	}

	public void setDemographicDao(DemographicDao demographicDao) {
		this.demographicDao = demographicDao;
	}

	public void setLookupManager(LookupManager lookupManager) {
		this.lookupManager = lookupManager;
	}

	public void setProgramManager(ProgramManager mgr) {
		this.programManager = mgr;
	}

}

/*
 * 
 * Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for 
 * Centre for Research on Inner City Health, St. Michael's Hospital, 
 * Toronto, Ontario, Canada 
 */

package org.oscarehr.PMmodule.web.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.exception.BedReservedException;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.exception.RoomHasActiveBedsException;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.Room;
import org.oscarehr.PMmodule.web.BaseAction;
import org.oscarehr.PMmodule.web.formbean.AgencyManagerViewFormBean;

public class AgencyManagerAction extends BaseAction {

	private static final Log log = LogFactory.getLog(AgencyManagerAction.class);

	private static final String FORWARD_EDIT = "edit";
	private static final String FORWARD_VIEW = "view";

	private static final String BEAN_AGENCY = "agency";
	private static final String BEAN_ROOMS = "rooms";
	private static final String BEAN_ROOM_TYPES = "roomTypes";
	private static final String BEAN_NUM_ROOMS = "numRooms";
	private static final String BEAN_BEDS = "beds";
	private static final String BEAN_BED_TYPES = "bedTypes";
	private static final String BEAN_NUM_BEDS = "numBeds";
	private static final String BEAN_PROGRAMS = "programs";

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return view(mapping, form, request, response);
	}

	public ActionForward view(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm) form;

		AgencyManagerViewFormBean tabBean = (AgencyManagerViewFormBean) agencyForm.get("view");

		boolean integratorEnabled = integratorManager.isEnabled();
		if (tabBean.getTab() != null && tabBean.getTab().equalsIgnoreCase("community")) {
			request.setAttribute("agencies", integratorManager.getAgencies());
		}

		request.setAttribute(BEAN_AGENCY, agencyManager.getLocalAgency());
		request.setAttribute(BEAN_ROOMS, roomManager.getRooms());
		request.setAttribute(BEAN_BEDS, bedManager.getBeds());

		request.setAttribute("integrator_enabled", integratorEnabled);

		if (integratorEnabled) request.setAttribute("integrator_version", integratorManager.getIntegratorVersion());

		return mapping.findForward(FORWARD_VIEW);
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm) form;

		Agency localAgency = agencyManager.getLocalAgency();

		agencyForm.set(BEAN_AGENCY, localAgency);
		agencyForm.set(BEAN_ROOMS, roomManager.getRooms());
		agencyForm.set(BEAN_ROOM_TYPES, roomManager.getRoomTypes());
		agencyForm.set(BEAN_NUM_ROOMS, new Integer(1));
		agencyForm.set(BEAN_BEDS, bedManager.getBeds());
		agencyForm.set(BEAN_BED_TYPES, bedManager.getBedTypes());
		agencyForm.set(BEAN_NUM_BEDS, new Integer(1));
		agencyForm.set(BEAN_PROGRAMS, programManager.getBedPrograms());

		request.setAttribute("id", localAgency.getId());
		request.setAttribute("integratorEnabled", localAgency.isIntegratorEnabled());
		request.setAttribute("integrator_enabled", integratorManager.isEnabled());

		return mapping.findForward(FORWARD_EDIT);
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm) form;

		Agency agency = (Agency) agencyForm.get(BEAN_AGENCY);

		if (isCancelled(request)) {
			request.getSession().removeAttribute("agencyManagerForm");
			request.setAttribute("id", agency.getId());

			return view(mapping, form, request, response);
		}

		if (request.getParameter("agency.hic") == null) {
			agency.setHic(false);
		}

		agencyManager.saveLocalAgency(agency);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("agency.saved", agency.getName()));
		saveMessages(request, messages);

		request.setAttribute("id", agency.getId());
		request.setAttribute("integratorEnabled", agency.isIntegratorEnabled());

		logManager.log("write", "agency", agency.getId().toString(), request);

		return mapping.findForward(FORWARD_EDIT);
	}

	public ActionForward saveRooms(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm) form;

		Room[] rooms = (Room[]) agencyForm.get(BEAN_ROOMS);

		// detect check box false
		for (int i = 0; i < rooms.length; i++) {
			if (request.getParameter("rooms[" + i + "].active") == null) {
				rooms[i].setActive(false);
			}
		}

		try {
			roomManager.saveRooms(rooms);
		}
		catch (RoomHasActiveBedsException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("room.active.beds.error", e.getMessage()));
			saveMessages(request, messages);
		}

		return edit(mapping, form, request, response);
	}

	public ActionForward saveBeds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm) form;

		Bed[] beds = (Bed[]) agencyForm.get(BEAN_BEDS);

		for (int i = 0; i < beds.length; i++) {
			if (request.getParameter("beds[" + i + "].active") == null) {
				beds[i].setActive(false);
			}
		}

		try {
			bedManager.saveBeds(beds);
		}
		catch (BedReservedException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reserved.error", e.getMessage()));
			saveMessages(request, messages);
		}

		return edit(mapping, form, request, response);
	}

	public ActionForward addRooms(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm) form;
		Integer numRooms = (Integer) agencyForm.get("numRooms");

		if (numRooms != null && numRooms > 0) {
			try {
				roomManager.addRooms(numRooms);
			}
			catch (RoomHasActiveBedsException e) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("room.active.beds.error", e.getMessage()));
				saveMessages(request, messages);
			}
		}

		return edit(mapping, form, request, response);
	}

	public ActionForward addBeds(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm agencyForm = (DynaActionForm) form;
		Integer numBeds = (Integer) agencyForm.get("numBeds");

		if (numBeds != null && numBeds > 0) {
			try {
				bedManager.addBeds(numBeds);
			}
			catch (BedReservedException e) {
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reserved.error", e.getMessage()));
				saveMessages(request, messages);
			}
		}

		return edit(mapping, form, request, response);
	}

	public ActionForward enable_integrator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		Agency agency = agencyManager.getLocalAgency();
		agency.setIntegratorEnabled(true);
		agencyManager.saveAgency(agency);
		integratorManager.refresh();

		try {
			Long id = integratorManager.register(agency);
		}
		catch (Throwable e) {
			e.printStackTrace();

			agency.setIntegratorEnabled(false);
			agencyManager.saveAgency(agency);
			integratorManager.refresh();

			log.error(e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("integrator.registered.failed", e.getMessage()));
			saveMessages(request, messages);
			request.setAttribute("id", agency.getId());
			request.setAttribute("integratorEnabled", agency.isIntegratorEnabled());

			return mapping.findForward(FORWARD_EDIT);
		}

		request.setAttribute("id", agency.getId());
		request.setAttribute("integratorEnabled", agency.isIntegratorEnabled());

		return mapping.findForward(FORWARD_EDIT);
	}

	public ActionForward disable_integrator(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		Agency agency = agencyManager.getLocalAgency();

		agency.setIntegratorEnabled(false);
		agencyManager.saveAgency(agency);
		integratorManager.refresh();

		request.setAttribute("id", agency.getId());
		request.setAttribute("integratorEnabled", agency.isIntegratorEnabled());

		return mapping.findForward(FORWARD_EDIT);
	}

	public ActionForward register(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			Agency agency = agencyManager.getLocalAgency();

			if (agency.getId() == 0) {
				Long id = integratorManager.register(agency);

				if (id != null) {
					agency.setId(id);
					agencyManager.saveLocalAgency(agency);
				}
				else {
					log.error("error");
				}
				integratorManager.refresh();
			}
			else {
				log.warn("already registered!!");
			}
		}
		catch (Throwable e) {
			log.error(e);
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("integrator.registered.failed"));
			saveMessages(request, messages);
		}

		return view(mapping, form, request, response);
	}

	public ActionForward refresh_programs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			integratorManager.refreshPrograms(programManager.getProgramsByAgencyId("0"));
		}
		catch (IntegratorException e) {
			log.error(e);
		}

		return view(mapping, form, request, response);
	}

	public ActionForward refresh_admissions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			integratorManager.refreshAdmissions(admissionManager.getAdmissions());
		}
		catch (IntegratorException e) {
			log.error(e);
		}

		return view(mapping, form, request, response);
	}

	public ActionForward refresh_referrals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			integratorManager.refreshReferrals(clientManager.getReferrals());
		}
		catch (IntegratorException e) {
			log.error(e);
		}

		return view(mapping, form, request, response);
	}

	public ActionForward refresh_clients(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		try {
			integratorManager.refreshClients(clientManager.getClients());
		}
		catch (IntegratorException e) {
			log.error(e);
		}

		return view(mapping, form, request, response);
	}

}
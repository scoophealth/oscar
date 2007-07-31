/**
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
package org.oscarehr.PMmodule.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.action.DynaActionForm;
import org.oscarehr.PMmodule.exception.AdmissionException;
import org.oscarehr.PMmodule.exception.AlreadyAdmittedException;
import org.oscarehr.PMmodule.exception.AlreadyQueuedException;
import org.oscarehr.PMmodule.exception.IntegratorException;
import org.oscarehr.PMmodule.exception.ProgramFullException;
import org.oscarehr.PMmodule.model.Admission;
import org.oscarehr.PMmodule.model.Agency;
import org.oscarehr.PMmodule.model.Bed;
import org.oscarehr.PMmodule.model.BedDemographic;
import org.oscarehr.PMmodule.model.ClientReferral;
import org.oscarehr.PMmodule.model.Consent;
import org.oscarehr.PMmodule.model.Demographic;
import org.oscarehr.PMmodule.model.DemographicExt;
import org.oscarehr.PMmodule.model.Intake;
import org.oscarehr.PMmodule.model.Program;
import org.oscarehr.PMmodule.model.ProgramProvider;
import org.oscarehr.PMmodule.model.ProgramQueue;
import org.oscarehr.PMmodule.model.Provider;
import org.oscarehr.PMmodule.web.formbean.ClientManagerFormBean;
import org.oscarehr.PMmodule.web.formbean.ErConsentFormBean;
import org.oscarehr.PMmodule.web.utils.UserRoleUtils;
import org.oscarehr.survey.model.oscar.OscarFormInstance;

public class ClientManagerAction extends BaseAction {

	private static Log log = LogFactory.getLog(ClientManagerAction.class);

	// Parameter
	public static final String ID = "id";

	public ActionForward unspecified(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;
		clientForm.set("view", new ClientManagerFormBean());

		return edit(mapping, form, request, response);
	}

	public ActionForward admit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;

		Admission admission = (Admission) clientForm.get("admission");
		Program program = (Program) clientForm.get("program");
		String demographicNo = request.getParameter("id");

		Program fullProgram = programManager.getProgram(String.valueOf(program.getId()));

		try {
			admissionManager.processAdmission(Integer.valueOf(demographicNo), getProviderNo(request), fullProgram, admission.getDischargeNotes(), admission.getAdmissionNotes(), admission.isTemporaryAdmission());
		} catch (ProgramFullException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", "Program is full."));
			saveMessages(request, messages);
		} catch (AdmissionException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", e.getMessage()));
			saveMessages(request, messages);
		}

		logManager.log("write", "admit", demographicNo, request);

		setEditAttributes(form, request, demographicNo);
		return mapping.findForward("edit");
	}

	public ActionForward admit_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;

		Program program = (Program) clientForm.get("program");
		String demographicNo = request.getParameter("id");
		setEditAttributes(form, request, demographicNo);

		program = programManager.getProgram(program.getId());

		/*
		 * If the user is currently enrolled in a bed program, we must warn the provider that this will also be a discharge
		 */
		if (program.getType().equalsIgnoreCase("bed")) {
			Admission currentAdmission = admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(demographicNo));
			if (currentAdmission != null) {
				request.setAttribute("current_admission", currentAdmission);
				request.setAttribute("current_program", programManager.getProgram(currentAdmission.getProgramId()));
			}
		}
		request.setAttribute("do_admit", new Boolean(true));

		return mapping.findForward("edit");
	}

	public ActionForward cancel(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;
		Admission admission = (Admission) clientForm.get("admission");
		admission.setDischargeNotes("");
		admission.setRadioDischargeReason("");

		clientForm.set("view", new ClientManagerFormBean());
		return edit(mapping, form, request, response);
	}

	public ActionForward discharge(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;

		Admission admission = (Admission) clientForm.get("admission");
		Program p = (Program) clientForm.get("program");
		String id = request.getParameter("id");
		
		boolean success = true;

		try {
			admissionManager.processDischarge(p.getId(), new Integer(id), admission.getDischargeNotes(), admission.getRadioDischargeReason());			
		} catch (AdmissionException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.failure", e.getMessage()));
			saveMessages(request, messages);
			success = false;
		}

		if (success) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.success"));
			saveMessages(request, messages);
			logManager.log("write", "discharge", id, request);
		}

		setEditAttributes(form, request, id);
		admission.setDischargeNotes("");
		admission.setRadioDischargeReason("");
		return mapping.findForward("edit");
	}

	public ActionForward discharge_community(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;

		Admission admission = (Admission) clientForm.get("admission");
		Program program = (Program) clientForm.get("program");
		String clientId = request.getParameter("id");

		ActionMessages messages = new ActionMessages();

		try {
			admissionManager.processDischargeToCommunity(program.getId(), new Integer(clientId), getProviderNo(request), admission.getDischargeNotes(), admission.getRadioDischargeReason());
			logManager.log("write", "discharge", clientId, request);

			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.success"));
			saveMessages(request, messages);
		} catch (AdmissionException e) {
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("discharge.failure", e.getMessage()));
			saveMessages(request, messages);
		}

		setEditAttributes(form, request, clientId);
		admission.setDischargeNotes("");
		admission.setRadioDischargeReason("");

		return mapping.findForward("edit");
	}

	public ActionForward discharge_community_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		setEditAttributes(form, request, id);

		request.setAttribute("do_discharge", new Boolean(true));
		request.setAttribute("community_discharge", new Boolean(true));
		return mapping.findForward("edit");
	}

	public ActionForward nested_discharge_community_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("nestedReason", "true");
		return discharge_community_select_program(mapping, form, request, response);
	}

	public ActionForward discharge_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		DynaActionForm clientForm = (DynaActionForm) form;
		Program program = (Program) clientForm.get("program");
		request.setAttribute("programId",String.valueOf(program.getId()));
		setEditAttributes(form, request, id);

		request.setAttribute("do_discharge", new Boolean(true));

		return mapping.findForward("edit");
	}

	public ActionForward nested_discharge_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		request.setAttribute("nestedReason", "true");		
		setEditAttributes(form,request,request.getParameter("id"));
		request.setAttribute("do_discharge", new Boolean(true));
		return mapping.findForward("edit");
	}

	public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		if (id == null || id.equals("")) {
			Object o = request.getAttribute("demographicNo");

			if (o instanceof String) {
				id = (String) o;
			}

			if (o instanceof Long) {
				id = String.valueOf((Long) o);
			}
		}
		
		
		setEditAttributes(form, request, id);

		logManager.log("read", "pmm client record", id, request);

		String roles = (String) request.getSession().getAttribute("userrole");

		// for Vaccine Provider
		if (roles.indexOf("Vaccine Provider") != -1) {
			return new ActionForward("/VaccineProviderReport.do?id=" + id, true);
		}

		// for ERModule
		if (roles.indexOf(UserRoleUtils.Roles.er_clerk.name()) != -1) {
			Map<?, ?> consentMap = (Map<?, ?>) request.getSession().getAttribute("er_consent_map");

			if (consentMap == null) {
				return mapping.findForward("consent");
			}

			if (consentMap.get(id) == null) {
				return mapping.findForward("consent");
			}

			request.getSession().setAttribute("er_consent_map", consentMap);
			return mapping.findForward("er-redirect");
		}

		return mapping.findForward("edit");
	}

	public ActionForward getLinks(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		if (id != null) {
			Demographic client = integratorManager.getClient(id);
			request.setAttribute("client", client);
		}
		return mapping.findForward("links");
	}

	// TODO:Better Error Handling
	public ActionForward integrator_admissions(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		try {
			List<?> results = integratorManager.getCurrentAdmissions(Long.valueOf(id).longValue());
			request.setAttribute("admissions", results);
		} catch (IntegratorException e) {
			log.error(e);
		}

		return mapping.findForward("integrator_admissions");
	}

	// TODO: Better Error Handling
	public ActionForward integrator_referrals(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");

		try {
			List<?> results = integratorManager.getCurrentReferrals(Long.valueOf(id).longValue());
			request.setAttribute("referrals", results);
		} catch (IntegratorException e) {
			log.error(e);
		}

		return mapping.findForward("integrator_referrals");
	}

	public ActionForward refer(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;
		ClientReferral referral = (ClientReferral) clientForm.get("referral");
		Program p = (Program) clientForm.get("program");
		String id = request.getParameter("id");

		Program program = programManager.getProgram(p.getId());

		referral.setAgencyId(new Long(0));
		referral.setSourceAgencyId(new Long(0));
		referral.setClientId(Long.valueOf(id));
		referral.setProgramId(new Long(program.getId().longValue()));
		referral.setProviderNo(Long.valueOf(getProviderNo(request)));
		referral.setReferralDate(new Date());
		referral.setStatus("active");

		boolean success = true;
		try {
			clientManager.processReferral(referral);
		} catch (AlreadyAdmittedException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_admitted"));
			saveMessages(request, messages);
			success = false;
		} catch (AlreadyQueuedException e) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_referred"));
			saveMessages(request, messages);
			success = false;
		}

		if (success) {
			ActionMessages messages = new ActionMessages();
			messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.success"));
			saveMessages(request, messages);
		}

		clientForm.set("program", new Program());
		clientForm.set("referral", new ClientReferral());
		setEditAttributes(form, request, id);
		logManager.log("write", "referral", id, request);

		return mapping.findForward("edit");
	}

	public ActionForward refer_select_program(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;
		Program p = (Program) clientForm.get("program");
		String id = request.getParameter("id");
		setEditAttributes(form, request, id);

		Program program = programManager.getProgram(p.getId());
		p.setName(program.getName());

		request.setAttribute("do_refer", new Boolean(true));
		request.setAttribute("program", program);
		request.setAttribute("temporaryAdmission",programManager.getEnabled());
		
		return mapping.findForward("edit");
	}

	public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return edit(mapping, form, request, response);
	}

	public ActionForward saveBedReservation(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;

		BedDemographic bedDemographic = (BedDemographic) clientForm.get("bedDemographic");

		// detect check box false
		if (request.getParameter("bedDemographic.latePass") == null) {
			bedDemographic.setLatePass(false);
		}

		bedDemographicManager.saveBedDemographic(bedDemographic);

		ActionMessages messages = new ActionMessages();
		messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("bed.reservation.success"));
		saveMessages(request, messages);

		return edit(mapping, form, request, response);
	}

	public ActionForward save_survey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;
		OscarFormInstance formInstance = (OscarFormInstance) clientForm.get("form");

		ClientManagerFormBean formBean = (ClientManagerFormBean) clientForm.get("view");

		formInstance.setFormId(0);

		String clientId = (String) request.getAttribute("clientId");
		if (clientId == null) {
			clientId = request.getParameter("id");
		}

		formBean.setTab("Forms");

		setEditAttributes(form, request, clientId);
		return mapping.findForward("edit");
	}

	public ActionForward search_programs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;

		Program criteria = (Program) clientForm.get("program");
		request.setAttribute("programs", programManager.search(criteria));

		return mapping.findForward("search_programs");
	}

	public ActionForward submit_erconsent(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;
		ErConsentFormBean consentFormBean = (ErConsentFormBean) clientForm.get("erconsent");
		boolean success = true;

		String demographicNo = request.getParameter("id");

		// save consent to session
		Map<String, ErConsentFormBean> consentMap = (Map) request.getSession().getAttribute("er_consent_map");

		if (consentMap == null) {
			consentMap = new HashMap<String, ErConsentFormBean>();
		}
		consentMap.put(demographicNo, consentFormBean);

		request.getSession().setAttribute("er_consent_map", consentMap);

		List<?> programDomain = providerManager.getProgramDomain(getProviderNo(request));
		if (programDomain.size() > 0) {
			boolean doAdmit = true;
			boolean doRefer = true;
			ProgramProvider program = (ProgramProvider) programDomain.get(0);
			// refer/admin client to service program associated with this user
			ClientReferral referral = new ClientReferral();
			referral.setAgencyId(new Long(0));
			referral.setClientId(new Long(demographicNo));
			referral.setNotes("ER Automated referral\nConsent Type: " + consentFormBean.getConsentType() + "\nReason: " + consentFormBean.getConsentReason());
			referral.setProgramId(new Long(program.getProgramId().longValue()));
			referral.setProviderNo(Long.valueOf(getProviderNo(request)));
			referral.setReferralDate(new Date());
			referral.setSourceAgencyId(new Long(0));
			referral.setStatus("active");

			Admission currentAdmission = admissionManager.getCurrentAdmission(String.valueOf(program.getProgramId()), Integer.valueOf(demographicNo));
			if (currentAdmission != null) {
				referral.setStatus("rejected");
				referral.setCompletionNotes("Client currently admitted");
				referral.setCompletionDate(new Date());
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_admitted"));
				saveMessages(request, messages);
				doAdmit = false;
			}
			ProgramQueue queue = programQueueManager.getActiveProgramQueue(String.valueOf(program.getId()), demographicNo);
			if (queue != null) {
				referral.setStatus("rejected");
				referral.setCompletionNotes("Client already in queue");
				referral.setCompletionDate(new Date());
				ActionMessages messages = new ActionMessages();
				messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("refer.already_referred"));
				saveMessages(request, messages);
				doRefer = false;
			}
			if (doRefer) {
				clientManager.saveClientReferral(referral);
			}

			if (doAdmit) {
				String admissionNotes = "ER Automated admission\nConsent Type: " + consentFormBean.getConsentType() + "\nReason: " + consentFormBean.getConsentReason();
				try {
					admissionManager.processAdmission(Integer.valueOf(demographicNo), getProviderNo(request), programManager.getProgram(String.valueOf(program.getProgramId())), null, admissionNotes);
				} catch (Exception e) {
					ActionMessages messages = new ActionMessages();
					messages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("admit.error", e.getMessage()));
					saveMessages(request, messages);
					success = false;
				}
			}
		}

		clientForm.set("erconsent", new ErConsentFormBean());
		request.setAttribute("id", demographicNo);
		if (success) {
			return mapping.findForward("er-redirect");
		} else {
			return mapping.findForward("search");
		}
	}

	public ActionForward survey(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		DynaActionForm clientForm = (DynaActionForm) form;
		OscarFormInstance formInstance = (OscarFormInstance) clientForm.get("form");

		if (request.getAttribute("survey_saved") != null) {
			setEditAttributes(form, request, (String) request.getAttribute("clientId"));
			return mapping.findForward("edit");
		}

		String clientId = request.getParameter("id");
		String formId = String.valueOf(formInstance.getFormId());

		formInstance.setFormId(0);

		return new ActionForward("/PMmodule/Forms/SurveyExecute.do?method=survey&formId=" + formId + "&clientId=" + clientId);
	}

	public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		return edit(mapping, form, request, response);
	}

    public ActionForward view_referral(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String referralId = request.getParameter("referralId");
        ClientReferral referral = clientManager.getClientReferral(referralId);
        Agency agency = agencyManager.getAgency("" + referral.getAgencyId());
        Demographic client = clientManager.getClientByDemographicNo("" + referral.getClientId());

        Long providerNo = referral.getProviderNo();
        Provider provider = providerManager.getProvider("" + providerNo);
        DynaActionForm clientForm = (DynaActionForm) form;

        clientForm.set("referral", referral);
        clientForm.set("agency", agency);
        clientForm.set("client", client);

        clientForm.set("provider", provider);
        
        return mapping.findForward("view_referral");
    }

    public ActionForward view_admission(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
        String admissionId = request.getParameter("admissionId");
        Admission admission = admissionManager.getAdmission(Long.valueOf(admissionId));
        Agency agency = agencyManager.getAgency("" + admission.getAgencyId());        
        Demographic client = clientManager.getClientByDemographicNo("" + admission.getClientId());
        Long providerNo = admission.getProviderNo();
        Provider provider = providerManager.getProvider("" + providerNo);

        DynaActionForm clientForm = (DynaActionForm) form;
        clientForm.set("admission", admission);
        clientForm.set("client", client);
        clientForm.set("agency", agency);
        clientForm.set("provider", provider);

        
        return mapping.findForward("view_admission");
    }

    public ActionForward update_sharing_opting(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		String sharingOptinChecked = request.getParameter("state");
		if (sharingOptinChecked != null) {
			int id = Integer.parseInt(request.getParameter("id"));

			String value = null;

			if ("true".equals(sharingOptinChecked)) value = Demographic.OptingStatus.EXPLICITLY_OPTED_IN.name();
			else if ("false".equals(sharingOptinChecked)) value = Demographic.OptingStatus.EXPLICITLY_OPTED_OUT.name();
			else throw (new IllegalStateException("Unexpected state, sharingOptinCheckbox state = " + sharingOptinChecked));

			clientManager.saveDemographicExt(id, Demographic.SHARING_OPTING_KEY, value);

			logManager.log("update", "DataSharingOpting:"+value, String.valueOf(id), request);
		}

		return(unspecified(mapping, form, request, response));
	}

    private boolean isInDomain(long programId, List<?> programDomain) {
		for (int x = 0; x < programDomain.size(); x++) {
			ProgramProvider p = (ProgramProvider) programDomain.get(x);

			if (p.getProgramId().longValue() == programId) {
				return true;
			}
		}

		return false;
	}

	private void setEditAttributes(ActionForm form, HttpServletRequest request, String demographicNo) {
		DynaActionForm clientForm = (DynaActionForm) form;

		ClientManagerFormBean tabBean = (ClientManagerFormBean) clientForm.get("view");

		request.setAttribute("id", demographicNo);
		request.setAttribute("client", clientManager.getClientByDemographicNo(demographicNo));
		
		DemographicExt sharingOptingStatus=clientManager.getDemographicExt(Integer.parseInt(demographicNo), Demographic.SHARING_OPTING_KEY);
		String sharingOptingStatusValue=null;
		if (sharingOptingStatus!=null) sharingOptingStatusValue=sharingOptingStatus.getValue();
		else sharingOptingStatusValue="none";
		request.setAttribute("sharingOptingStatus", sharingOptingStatusValue);
		boolean sharingOptingStatusChecked=Demographic.OptingStatus.IMPLICITLY_OPTED_IN.name().equals(sharingOptingStatusValue) || Demographic.OptingStatus.EXPLICITLY_OPTED_IN.name().equals(sharingOptingStatusValue); 
		request.setAttribute("sharingOptingCheckBoxState", sharingOptingStatusChecked?"checked=\"checked\"":"");

		String providerNo = getProviderNo(request);
		
		// program domain
		List<Program> programDomain = new ArrayList<Program>();

		for (Iterator<?> i = providerManager.getProgramDomain(providerNo).iterator(); i.hasNext();) {
			ProgramProvider programProvider = (ProgramProvider) i.next();
			programDomain.add(programManager.getProgram(programProvider.getProgramId()));
		}

		request.setAttribute("programDomain", programDomain);

		// tab override - from survey module
		String tabOverride = (String) request.getAttribute("tab.override");

		if (tabOverride != null && tabOverride.length() > 0) {
			tabBean.setTab(tabOverride);
		}

		if (tabBean.getTab().equals("Summary")) {
			request.setAttribute("admissions", admissionManager.getCurrentAdmissions(Integer.valueOf(demographicNo)));

			Intake mostRecentQuickIntake = genericIntakeManager.getMostRecentQuickIntake(Integer.valueOf(demographicNo));
			request.setAttribute("mostRecentQuickIntake", mostRecentQuickIntake);

			Consent consent = consentManager.getMostRecentConsent(Long.valueOf(demographicNo));
			request.setAttribute("consent", consent);

			if (consent == null) {
				DemographicExt remote_consent = clientManager.getDemographicExt(new Integer(demographicNo), "consent_st");

				if (remote_consent != null) {
					request.setAttribute("remote_consent", remote_consent);
					request.setAttribute("remote_consent_exclusions", clientManager.getDemographicExt(new Integer(demographicNo), "consent_ex"));

					DemographicExt remoteConsentAgency = clientManager.getDemographicExt(new Integer(demographicNo), "consent_ag");
					if (remoteConsentAgency != null) {
						request.setAttribute("remote_consent_agency", remoteConsentAgency);
						request.setAttribute("remote_consent_agency_name", Agency.getAgencyName(Long.parseLong(remoteConsentAgency.getValue())));
					}

					request.setAttribute("remote_consent_date", clientManager.getDemographicExt(new Integer(demographicNo), "consent_dt"));
				}
			}

			request.setAttribute("referrals", clientManager.getActiveReferrals(demographicNo));
		}

		/* history */
		if (tabBean.getTab().equals("History")) {
			request.setAttribute("admissionHistory", admissionManager.getAdmissions(Integer.valueOf(demographicNo)));
			request.setAttribute("referralHistory", clientManager.getReferrals(demographicNo));
		}

		List<?> currentAdmissions = admissionManager.getCurrentAdmissions(Integer.valueOf(demographicNo));

		for (int x = 0; x < currentAdmissions.size(); x++) {
			Admission admission = (Admission) currentAdmissions.get(x);

			if (isInDomain(admission.getProgramId().longValue(), providerManager.getProgramDomain(providerNo))) {
				request.setAttribute("isInProgramDomain", Boolean.TRUE);
				break;
			}
		}

		/* bed reservation view */
		BedDemographic bedDemographic = bedDemographicManager.getBedDemographicByDemographic(Integer.valueOf(demographicNo));
		request.setAttribute("bedDemographic", bedDemographic);

		/* bed reservation edit */
		if (tabBean.getTab().equals("Bed Reservation")) {
			// set bed program id
			Admission bedProgramAdmission = admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(demographicNo));
			Integer bedProgramId = (bedProgramAdmission != null) ? bedProgramAdmission.getProgramId().intValue() : null;

			request.setAttribute("bedProgramId", bedProgramId);

			// set bed demographic
			boolean reservationExists = (bedDemographic != null);
			bedDemographic = reservationExists ? bedDemographic : BedDemographic.create(Integer.valueOf(demographicNo), bedDemographicManager.getDefaultBedDemographicStatus(), providerNo);
			Bed reservedBed = reservationExists ? bedManager.getBed(bedDemographic.getBedId()) : null;

			clientForm.set("bedDemographic", bedDemographic);

			// set unreserved beds
			Bed[] unreservedBeds = bedManager.getBedsByProgram(bedProgramId, false);
			unreservedBeds = reservationExists ? (Bed[]) ArrayUtils.add(unreservedBeds, 0, reservedBed) : unreservedBeds;

			clientForm.set("unreservedBeds", unreservedBeds);

			// set bed demographic statuses
			clientForm.set("bedDemographicStatuses", bedDemographicManager.getBedDemographicStatuses());
		}

		/* forms */
		if (tabBean.getTab().equals("Forms")) {
			request.setAttribute("quickIntakes", genericIntakeManager.getQuickIntakes(Integer.valueOf(demographicNo)));
			request.setAttribute("indepthIntakes", genericIntakeManager.getIndepthIntakes(Integer.valueOf(demographicNo)));
			request.setAttribute("programIntakes", genericIntakeManager.getProgramIntakes(Integer.valueOf(demographicNo)));
			request.setAttribute("programsWithIntake", genericIntakeManager.getProgramsWithIntake(Integer.valueOf(demographicNo)));

			/* survey module */
			request.setAttribute("survey_list", surveyManager.getAllForms());
			request.setAttribute("surveys", surveyManager.getForms(demographicNo));
		}

		/* refer */
		if (tabBean.getTab().equals("Refer")) {
			request.setAttribute("referrals", clientManager.getActiveReferrals(demographicNo));
		}

		/* discharge */
		if (tabBean.getTab().equals("Discharge")) {
			request.setAttribute("communityPrograms", programManager.getCommunityPrograms());
			request.setAttribute("serviceAdmissions", admissionManager.getCurrentServiceProgramAdmission(Integer.valueOf(demographicNo)));
			request.setAttribute("temporaryAdmissions", admissionManager.getCurrentTemporaryProgramAdmission(Integer.valueOf(demographicNo)));
			request.setAttribute("current_bed_program", admissionManager.getCurrentBedProgramAdmission(Integer.valueOf(demographicNo)));
			request.setAttribute("current_community_program", admissionManager.getCurrentCommunityProgramAdmission(Integer.valueOf(demographicNo)));
		}
	}

}

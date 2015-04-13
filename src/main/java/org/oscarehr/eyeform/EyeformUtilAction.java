/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.eyeform;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsDateJsonBeanProcessor;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.PMmodule.dao.ProviderDao;
import org.oscarehr.PMmodule.dao.SecUserRoleDao;
import org.oscarehr.PMmodule.model.SecUserRole;
import org.oscarehr.common.dao.BillingServiceDao;
import org.oscarehr.common.dao.DemographicDao;
import org.oscarehr.common.dao.DiagnosticCodeDao;
import org.oscarehr.common.dao.EyeformMacroDao;
import org.oscarehr.common.dao.OscarAppointmentDao;
import org.oscarehr.common.model.Appointment;
import org.oscarehr.common.model.BillingService;
import org.oscarehr.common.model.Demographic;
import org.oscarehr.common.model.DiagnosticCode;
import org.oscarehr.common.model.EyeformMacro;
import org.oscarehr.common.model.Provider;
import org.oscarehr.common.model.Tickler;
import org.oscarehr.eyeform.model.EyeformMacroBillingItem;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.managers.TicklerManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.SpringUtils;

import oscar.oscarTickler.TicklerCreator;
import oscar.util.StringUtils;

public class EyeformUtilAction extends DispatchAction {

	private TicklerManager ticklerManager = SpringUtils.getBean(TicklerManager.class);
	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	
	public ActionForward getProviders(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
		List<Provider> activeProviders = providerDao.getActiveProviders();

		HashMap<String, List<Provider>> hashMap = new HashMap<String, List<Provider>>();
		hashMap.put("providers", activeProviders);

		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}


	public ActionForward getTickler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_tickler", "r", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		Tickler t = ticklerManager.getTickler(loggedInInfo,Integer.parseInt(request.getParameter("tickler_no")));

		HashMap<String, HashMap<String, Object>> hashMap = new HashMap<String, HashMap<String, Object>>();
		HashMap<String, Object> ticklerMap = new HashMap<String, Object>();

		ticklerMap.put("message", t.getMessage());
		ticklerMap.put("updateDate", t.getUpdateDate());
		ticklerMap.put("provider", t.getProvider().getFormattedName());
		ticklerMap.put("providerNo", t.getProvider().getProviderNo());
		ticklerMap.put("toProvider", t.getAssignee().getFormattedName());
		ticklerMap.put("toProviderNo", t.getAssignee().getProviderNo());

		hashMap.put("tickler", ticklerMap);

		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}

	public ActionForward getBillingAutocompleteList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		BillingServiceDao billingServiceDao = (BillingServiceDao) SpringUtils.getBean("billingServiceDao");

		String query = request.getParameter("query");

		List<BillingService> queryResults = billingServiceDao.findBillingCodesByCode("%" + query + "%", "ON", new Date(), 1);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		hashMap.put("billing", queryResults);

		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());


		return null;
	}

	public ActionForward getBillingDxAutocompleteList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		DiagnosticCodeDao diagnosticCodeDao = (DiagnosticCodeDao) SpringUtils.getBean("diagnosticCodeDao");

		String query = request.getParameter("query");

		List<DiagnosticCode> queryResults = diagnosticCodeDao.findByDiagnosticCodeAndRegion(query, "ON");
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		hashMap.put("dxCode", queryResults);

		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());


		return null;
	}


	public ActionForward getMacroList(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		EyeformMacroDao eyeformMacroDao = (EyeformMacroDao) SpringUtils.getBean("eyeformMacroDao");

		List<EyeformMacro> macroList = eyeformMacroDao.getMacros();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("macros", macroList);

		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}

	public ActionForward saveMacro(ActionMapping maping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		EyeformMacroDao eyeformMacroDao = (EyeformMacroDao) SpringUtils.getBean("eyeformMacroDao");
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		try {
			EyeformMacro macro = new EyeformMacro();
			if (request.getParameter("macroIdField") != null && request.getParameter("macroIdField").length() > 0) {
				macro = eyeformMacroDao.find(Integer.parseInt(request.getParameter("macroIdField")));
				macro = (macro == null ? new EyeformMacro() : macro);
			}

			macro.setMacroName(request.getParameter("macroNameBox"));
			macro.setImpressionText(request.getParameter("macroImpressionBox"));
			macro.setPlanText(request.getParameter("macroPlanBox"));
			macro.setCopyFromLastImpression(Boolean.parseBoolean(request.getParameter("macroCopyFromLastImpression")));

			if (request.getParameter("billingData") != null && request.getParameter("billingData").length() > 0) {
				String[] billingItems = request.getParameterValues("billingData");

				List<EyeformMacroBillingItem> billingItemList = new LinkedList<EyeformMacroBillingItem>();
				for (String b : billingItems) {
					EyeformMacroBillingItem billingItem = new EyeformMacroBillingItem();
					String billingCode = b.substring(0, b.indexOf("|"));
					String multiplier = b.substring(b.indexOf("|"));

					billingItem.setBillingServiceCode(billingCode);
					billingItem.setMultiplier(Double.parseDouble(multiplier));

					billingItemList.add(billingItem);
				}

				macro.setBillingItems(billingItemList);
			}

			eyeformMacroDao.merge(macro);

			hashMap.put("saved", macro.getId());
		} catch (Exception e) {
			hashMap.put("error", e.getMessage());
		}

		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}

	public ActionForward sendPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
		SecUserRoleDao secUserRoleDao = (SecUserRoleDao) SpringUtils.getBean("secUserRoleDao");
		
		List<Provider> activeReceptionists = new ArrayList<Provider>();
		for(SecUserRole sur:secUserRoleDao.getSecUserRolesByRoleName("receptionist")) {
			Provider p = providerDao.getProvider(sur.getProviderNo());
			if(p != null && p.getStatus().equals("1")) {
				activeReceptionists.add(p);
			}
		}

		TicklerCreator tc = new TicklerCreator();
		String demographicNo = request.getParameter("demographicNo");

		String message = request.getParameter("value");

		if (demographicNo != null && message != null && message.trim().length() > 0 && activeReceptionists != null) {
			for (Provider p : activeReceptionists) {
				tc.createTickler(loggedInInfo, demographicNo, p.getProviderNo(), message);
			}

			hashMap.put("sentToReceptionists", activeReceptionists.size());
		}

		return null;
	}

	public ActionForward getBillingArgs(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		DemographicDao demographicDao = (DemographicDao) SpringUtils.getBean("demographicDao");
		OscarAppointmentDao appointmentDao = (OscarAppointmentDao) SpringUtils.getBean("oscarAppointmentDao");

		Appointment appointment = null;
		try {
			appointment = appointmentDao.find(Integer.parseInt(request.getParameter("appointment_no")));
		} catch (Exception e) {
			// appointment_no is not a number, I guess
			appointment = null;
		}

		Demographic demographic = demographicDao.getDemographic(request.getParameter("demographic_no"));


		hashMap.put("ohip_version", "V03G");

		if (demographic != null) {
			Integer sex = null;
			if (demographic.getSex().equalsIgnoreCase("M"))
				sex = 1;
			else if (demographic.getSex().equalsIgnoreCase("F"))
				sex = 2;

			String dateOfBirth = StringUtils.join(new String[] { demographic.getYearOfBirth(), demographic.getMonthOfBirth(), demographic.getDateOfBirth() }, "");

			hashMap.put("hin", demographic.getHin());
			hashMap.put("ver", demographic.getVer());
			hashMap.put("hc_type", demographic.getHcType());
			hashMap.put("sex", sex);
			hashMap.put("demographic_dob", dateOfBirth);
			hashMap.put("demographic_name", demographic.getLastName() + "," + demographic.getFirstName());
		}

		if (appointment != null) {
			hashMap.put("apptProvider_no", appointment.getProviderNo());
			hashMap.put("start_time", appointment.getStartTime().toString());
			hashMap.put("appointment_date", appointment.getAppointmentDate().getTime());
		}

		hashMap.put("current_provider_no", loggedInInfo.getLoggedInProviderNo());
		hashMap.put("demo_mrp_provider_no", demographic.getProviderNo());


		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());



		return null;
	}

	public ActionForward sendTickler(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) {


		return null;
	}

	public ActionForward updateAppointmentReason(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HashMap<String, Object> hashMap = new HashMap<String, Object>();

		OscarAppointmentDao appointmentDao = (OscarAppointmentDao) SpringUtils.getBean("oscarAppointmentDao");

		Appointment appointment = appointmentDao.find(Integer.parseInt(request.getParameter("appointmentNo")));


		if (appointment != null) {
			appointment.setReason(request.getParameter("reason"));
			appointmentDao.merge(appointment);

			hashMap.put("success", true);
			hashMap.put("appointmentNo", appointment.getId());
		}

		JsonConfig config = new JsonConfig();
		config.registerJsonBeanProcessor(java.sql.Date.class, new JsDateJsonBeanProcessor());

		JSONObject json = JSONObject.fromObject(hashMap, config);
		response.getOutputStream().write(json.toString().getBytes());

		return null;
	}
}

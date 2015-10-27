/**
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

package org.oscarehr.caseload;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.oscarehr.common.dao.CaseloadDao;
import org.oscarehr.managers.SecurityInfoManager;
import org.oscarehr.util.LoggedInInfo;
import org.oscarehr.util.MiscUtils;
import org.oscarehr.util.SpringUtils;

import oscar.OscarProperties;
import oscar.log.LogAction;
import oscar.util.OscarRoleObjectPrivilege;
import oscar.util.StringUtils;

public class CaseloadContentAction extends DispatchAction {

	private SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	   

	public ActionForward noteSearch(ActionMapping actionMapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		
		if(!securityInfoManager.hasPrivilege(LoggedInInfo.getLoggedInInfoFromSession(request), "_demographic", "r", null)) {
        	throw new SecurityException("missing required security object (_demographic)");
        }
		
		String caseloadProv     = StringEscapeUtils.escapeSql(request.getParameter("clProv"));
		String caseloadQuery    = StringEscapeUtils.escapeSql(request.getParameter("clQ"));
		boolean sortAscending   = "true".equals(request.getParameter("clSortAsc"));

		Integer caseloadPage;
		Integer caseloadPageSize;

		HttpSession session = request.getSession();
		String curUser_no = (String) session.getAttribute("user");

		try {
			caseloadPage = Integer.parseInt(request.getParameter("clPage"));
		}
		catch (Exception e) { caseloadPage = 0; }
		try {
			caseloadPageSize = Integer.parseInt(request.getParameter("clPSize"));
		}
		catch (Exception e) { caseloadPageSize = 100; }

		CaseloadCategory caseloadCategory = CaseloadCategory.getCategory(request.getParameter("clCat"));
		if (caseloadCategory == null) { caseloadCategory = CaseloadCategory.Demographic; }

		String clSearchQuery = "search_notes";
		String[] clSearchParams = new String[] { caseloadProv, caseloadProv, caseloadProv, caseloadProv, "%"+caseloadQuery+"%" };
		String[] clSortParams = null;

		switch (caseloadCategory) {
			case Demographic:
			case Age:
			case Sex:
			case LastAppt:
			case NextAppt:
			case ApptsLYTD:
			case Tickler:
			case Msg:
				clSortParams = null;
				break;
			case Lab:
			case Doc:
				clSortParams = new String[] { curUser_no };
				break;
			case BMI:
			case BP:
			case WT:
			case SMK:
			case A1C:
			case ACR:
			case SCR:
			case LDL:
			case HDL:
			case TCHD:
			case EGFR:
			case EYEE:
				clSortParams = new String[] { caseloadCategory.getLabel() };
				break;
		}

		CaseloadDao caseloadDao = (CaseloadDao)SpringUtils.getBean("caseloadDao");
		List<Integer> demoSearchResult = caseloadDao.getCaseloadDemographicSet(clSearchQuery, clSearchParams, clSortParams, caseloadCategory, sortAscending ? "ASC" : "DESC", caseloadPage, caseloadPageSize);
		JSONArray data = generateCaseloadDataForDemographics(request, response, caseloadProv, demoSearchResult);


		response.setContentType("text/x-json");
		JSONObject json = new JSONObject();
		json.put("data", data);

		if (caseloadPage == 0) {
			Integer size = caseloadDao.getCaseloadDemographicSearchSize(clSearchQuery, clSearchParams);
			json.put("size", size);
		}

		LogAction.addLogSynchronous(LoggedInInfo.getLoggedInInfoFromSession(request), "CaseloadContentAction", "view caseload");

		try {
			json.write(response.getWriter());
		} catch (IOException e) {
			MiscUtils.getLogger().error("Couldn't get data for caseload", e);
		}

		return null;
	}

	public ActionForward search(ActionMapping actionMapping,
			ActionForm actionForm,
			HttpServletRequest request,
			HttpServletResponse response) {
		LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

		String caseloadDx       = StringEscapeUtils.escapeSql(request.getParameter("clDx"));
		String caseloadProv     = StringEscapeUtils.escapeSql( request.getParameter("clProv"));
		String caseloadRoster   = StringEscapeUtils.escapeSql( request.getParameter("clRo"));
		String caseloadProgram	= StringEscapeUtils.escapeSql( request.getParameter("clProg"));
		boolean sortAscending   = "true".equals(request.getParameter("clSortAsc"));

		Integer caseloadPage;
		Integer caseloadPageSize;

		HttpSession session = request.getSession();
		String curUser_no = (String) session.getAttribute("user");

		try {
			caseloadPage = Integer.parseInt(request.getParameter("clPage"));
		}
		catch (Exception e) { caseloadPage = 0; }
		try {
			caseloadPageSize = Integer.parseInt(request.getParameter("clPSize"));
		}
		catch (Exception e) { caseloadPageSize = 100; }

		CaseloadCategory caseloadCategory = CaseloadCategory.getCategory(request.getParameter("clCat"));
		if (caseloadCategory == null) { caseloadCategory = CaseloadCategory.Demographic; }

		String clSearchQuery;
		String[] clSearchParams;
		String[] clSortParams = null;
		
		if ("all".equals(caseloadProgram) && "all".equals(caseloadProv)){ // program and provider are all
			
			Integer facilityId = loggedInInfo.getCurrentFacility().getId();
			
			if (!StringUtils.isNullOrEmpty(caseloadDx) && !StringUtils.isNullOrEmpty(caseloadRoster)) {
				// filter on dx and roster status
				clSearchQuery="search_allpg_alldemo_rodxfilter";
				clSearchParams = new String[3];
				clSearchParams[0] = caseloadDx;
				clSearchParams[1] = caseloadRoster;
				clSearchParams[2] = facilityId.toString();
			} else if (!StringUtils.isNullOrEmpty(caseloadRoster)) {
				// filter on dx and roster status
				clSearchQuery="search_allpg_alldemo_rofilter";
				clSearchParams = new String[2];
				clSearchParams[0] = caseloadRoster;
				clSearchParams[1] = facilityId.toString();
			}
			else if (!StringUtils.isNullOrEmpty(caseloadDx)) {
				// filter on dx
				clSearchQuery="search_allpg_alldemo_dxfilter";
				clSearchParams = new String[2];
				clSearchParams[0] = caseloadDx;
				clSearchParams[1] = facilityId.toString();
			} else {
				// no dx filter
				clSearchQuery="search_allpg_alldemo_nofilter";
				clSearchParams = new String[1];
				clSearchParams[0] = facilityId.toString();
			}
		} else if ("all".equals(caseloadProgram)) { // program is all
			// demographics from a specific provider
			
			Integer facilityId = loggedInInfo.getCurrentFacility().getId();
			
			if (!StringUtils.isNullOrEmpty(caseloadDx) && !StringUtils.isNullOrEmpty(caseloadRoster)) {
				// filter on dx
				clSearchQuery="search_allpg_provdemo_rodxfilter";
				clSearchParams = new String[7];
				clSearchParams[0] = caseloadProv;
				clSearchParams[1] = caseloadProv;
				clSearchParams[2] = caseloadProv;
				clSearchParams[3] = caseloadProv;
				clSearchParams[4] = caseloadRoster;
				clSearchParams[5] = caseloadDx;
				clSearchParams[6] = facilityId.toString();
			} else if (!StringUtils.isNullOrEmpty(caseloadRoster)) {
				// no dx filter
				clSearchQuery="search_allpg_provdemo_dxfilter";
				clSearchParams = new String[6];
				clSearchParams[0] = caseloadProv;
				clSearchParams[1] = caseloadProv;
				clSearchParams[2] = caseloadProv;
				clSearchParams[3] = caseloadProv;
				clSearchParams[4] = caseloadRoster;
				clSearchParams[5] = facilityId.toString();
			} else if (!StringUtils.isNullOrEmpty(caseloadDx)) {
				// filter on dx
				clSearchQuery="search_allpg_provdemo_rofilter";
				clSearchParams = new String[6];
				clSearchParams[0] = caseloadProv;
				clSearchParams[1] = caseloadProv;
				clSearchParams[2] = caseloadProv;
				clSearchParams[3] = caseloadProv;
				clSearchParams[4] = caseloadDx;
				clSearchParams[5] = facilityId.toString();
			} else {
				// no dx filter
				clSearchQuery="search_allpg_provdemo_nofilter";
				clSearchParams = new String[5];
				clSearchParams[0] = caseloadProv;
				clSearchParams[1] = caseloadProv;
				clSearchParams[2] = caseloadProv;
				clSearchParams[3] = caseloadProv;
				clSearchParams[4] = facilityId.toString();
		    }
		} else if ("all".equals(caseloadProv)) { // provider is all
			// all demographics
			if (!StringUtils.isNullOrEmpty(caseloadDx) && !StringUtils.isNullOrEmpty(caseloadRoster)) {
				// filter on dx and roster status
				clSearchQuery="search_alldemo_rodxfilter";
				clSearchParams = new String[3];
				clSearchParams[0] = caseloadDx;
				clSearchParams[1] = caseloadRoster;
				clSearchParams[2] = caseloadProgram;
			} else if (!StringUtils.isNullOrEmpty(caseloadRoster)) {
				// filter on dx and roster status
				clSearchQuery="search_alldemo_rofilter";
				clSearchParams = new String[2];
				clSearchParams[0] = caseloadRoster;
				clSearchParams[1] = caseloadProgram;
			}
			else if (!StringUtils.isNullOrEmpty(caseloadDx)) {
				// filter on dx
				clSearchQuery="search_alldemo_dxfilter";
				clSearchParams = new String[2];
				clSearchParams[0] = caseloadDx;
				clSearchParams[1] = caseloadProgram;
			} else {
				// no dx filter
				clSearchQuery="search_alldemo_nofilter";
				clSearchParams = new String[1];
				clSearchParams[0] = caseloadProgram;
			}
		} else { // program and provider aren't all
			// demographics from a specific provider
			if (!StringUtils.isNullOrEmpty(caseloadDx) && !StringUtils.isNullOrEmpty(caseloadRoster)) {
				// filter on dx
				clSearchQuery="search_provdemo_rodxfilter";
				clSearchParams = new String[7];
				clSearchParams[0] = caseloadProv;
				clSearchParams[1] = caseloadProv;
				clSearchParams[2] = caseloadProv;
				clSearchParams[3] = caseloadProv;
				clSearchParams[4] = caseloadRoster;
				clSearchParams[5] = caseloadDx;
				clSearchParams[6] = caseloadProgram;
			} else if (!StringUtils.isNullOrEmpty(caseloadRoster)) {
				// no dx filter
				clSearchQuery="search_provdemo_rofilter";
				clSearchParams = new String[6];
				clSearchParams[0] = caseloadProv;
				clSearchParams[1] = caseloadProv;
				clSearchParams[2] = caseloadProv;
				clSearchParams[3] = caseloadProv;
				clSearchParams[4] = caseloadRoster;
				clSearchParams[5] = caseloadProgram;
			} else if (!StringUtils.isNullOrEmpty(caseloadDx)) {
				// filter on dx
				clSearchQuery="search_provdemo_dxfilter";
				clSearchParams = new String[6];
				clSearchParams[0] = caseloadProv;
				clSearchParams[1] = caseloadProv;
				clSearchParams[2] = caseloadProv;
				clSearchParams[3] = caseloadProv;
				clSearchParams[4] = caseloadDx;
				clSearchParams[5] = caseloadProgram;
			} else {
				// no dx filter
				clSearchQuery="search_provdemo_nofilter";
				clSearchParams = new String[5];
				clSearchParams[0] = caseloadProv;
				clSearchParams[1] = caseloadProv;
				clSearchParams[2] = caseloadProv;
				clSearchParams[3] = caseloadProv;
				clSearchParams[4] = caseloadProgram;
		    }
		}

		switch (caseloadCategory) {
			case Demographic:
			case Age:
			case Sex:
			case LastAppt:
			case NextAppt:
			case ApptsLYTD:
			case Tickler:
			case Msg:
				clSortParams = null;
				break;
			case Lab:
			case Doc:
				clSortParams = new String[] { curUser_no };
				break;
			case BMI:
			case BP:
			case WT:
			case SMK:
			case A1C:
			case ACR:
			case SCR:
			case LDL:
			case HDL:
			case TCHD:
			case EGFR:
			case EYEE:
				clSortParams = new String[] { caseloadCategory.getLabel(), caseloadCategory.getLabel() };
				break;
			case LastEncounterDate:
			case LastEncounterType:
				clSortParams = null;
				break;
			case CashAdmissionDate:
				clSortParams = new String[] {"CASH WAITLIST PROGRAM"};
				break;
			case Access1AdmissionDate:
				clSortParams = new String[] {"ACCESS 1 WAITLIST PROGRAM"};
				break;
		}

	    CaseloadDao caseloadDao = (CaseloadDao)SpringUtils.getBean("caseloadDao");
		List<Integer> demoSearchResult = caseloadDao.getCaseloadDemographicSet(clSearchQuery, clSearchParams, clSortParams, caseloadCategory, sortAscending ? "ASC" : "DESC", caseloadPage, caseloadPageSize);
		JSONArray data = generateCaseloadDataForDemographics(request, response, caseloadProv, demoSearchResult);

		response.setContentType("text/x-json");
		JSONObject json = new JSONObject();
		json.put("data", data);

		if (caseloadPage == 0) {
			Integer size = caseloadDao.getCaseloadDemographicSearchSize(clSearchQuery, clSearchParams);
			json.put("size", size);
		}

		try {
			json.write(response.getWriter());
		} catch (IOException e) {
			MiscUtils.getLogger().error("Couldn't get data for caseload", e);
		}

		return null;
	}


	private JSONArray generateCaseloadDataForDemographics(HttpServletRequest request, HttpServletResponse response, String caseloadProv, List<Integer> demoSearchResult) {
		JSONArray entry;
		String buttons;
		JSONArray data = new JSONArray();

		CaseloadDao caseloadDao = (CaseloadDao)SpringUtils.getBean("caseloadDao");

		HttpSession session = request.getSession();

		String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

		String curUser_no = (String) session.getAttribute("user");
		String userfirstname = (String) session.getAttribute("userfirstname");
	    String userlastname = (String) session.getAttribute("userlastname");

	    GregorianCalendar cal = new GregorianCalendar();
	    int curYear = cal.get(Calendar.YEAR);
	    int curMonth = (cal.get(Calendar.MONTH)+1);
	    int curDay = cal.get(Calendar.DAY_OF_MONTH);

	    int year = Integer.parseInt(request.getParameter("year"));
	    int month = Integer.parseInt(request.getParameter("month"));
	    int day = Integer.parseInt(request.getParameter("day"));

	    java.util.Date apptime=new java.util.Date();

	    OscarProperties oscarProperties = OscarProperties.getInstance();
	    boolean bShortcutForm = oscarProperties.getProperty("appt_formview", "").equalsIgnoreCase("on") ? true : false;
	    String formName = bShortcutForm ? oscarProperties.getProperty("appt_formview_name") : "";
		String formNameShort = formName.length() > 3 ? (formName.substring(0,2)+".") : formName;
	    String formName2 = bShortcutForm ? oscarProperties.getProperty("appt_formview_name2", "") : "";
		String formName2Short = formName2.length() > 3 ? (formName2.substring(0,2)+".") : formName2;
	    boolean bShortcutForm2 = bShortcutForm && !formName2.equals("");
	    boolean bShortcutIntakeForm = oscarProperties.getProperty("appt_intake_form", "").equalsIgnoreCase("on") ? true : false;

	    String monthDay = String.format("%02d", month) + "-" + String.format("%02d", day);

	    String prov= oscarProperties.getProperty("billregion","").trim().toUpperCase();

		for (Integer result : demoSearchResult) {

			String demographic_no = result.toString();
			entry = new JSONArray();
			// name
			String demographicQuery = "cl_demographic_query";
			String[] demographicParam = new String[1];
			demographicParam[0] = demographic_no;
			List<Map<String, Object>> demographicResult = caseloadDao.getCaseloadDemographicData(demographicQuery, demographicParam);

			String clLastName = demographicResult.get(0).get("last_name").toString();
			String clFirstName = demographicResult.get(0).get("first_name").toString();
			String clFullName = StringEscapeUtils.escapeJavaScript(clLastName + ", " + clFirstName).toUpperCase();
			entry.add(clFullName);

			// add E button to string
			buttons = "";
			if (hasPrivilege("_caseload.DisplayMode", roleName$)){
				if (hasPrivilege("_eChart", roleName$)) {
					String encType = "";
					try {
						encType = URLEncoder.encode("face to face encounter with client","UTF-8");
					} catch (UnsupportedEncodingException e) {
						MiscUtils.getLogger().error("Couldn't encode string", e);
					}
					String eURL = "../oscarEncounter/IncomingEncounter.do?providerNo="+curUser_no+"&appointmentNo=0&demographicNo="+demographic_no+"&curProviderNo="+caseloadProv+"&reason=&encType="+encType+"&userName="+URLEncoder.encode( userfirstname+" "+userlastname)+"&curDate="+curYear+"-"+curMonth+"-"+curDay+"&appointmentDate="+year+"-"+month+"-"+day+"&startTime="+apptime.getHours()+":"+apptime.getMinutes()+"&status=T"+"&apptProvider_no="+caseloadProv+"&providerview="+caseloadProv;
					buttons += "<a href='#' onClick=\"popupWithApptNo(710, 1024,'../oscarSurveillance/CheckSurveillance.do?demographicNo="+demographic_no+"&proceed="+URLEncoder.encode(eURL)+"', 'encounter');return false;\" title='Encounter'>E</a> ";
				}

				// add form links to string
				if (hasPrivilege("_billing", roleName$)) {
					buttons += bShortcutForm?"| <a href=# onClick='popupPage2( \"../form/forwardshortcutname.jsp?formname="+formName+"&demographic_no="+demographic_no+"\")' title='form'>"+formNameShort+"</a> " : "";
					buttons += bShortcutForm2?"| <a href=# onClick='popupPage2( \"../form/forwardshortcutname.jsp?formname="+formName2+"&demographic_no="+demographic_no+"\")' title='form'>"+formName2Short+"</a> " : "";
					buttons += (bShortcutIntakeForm) ? "| <a href='#' onClick='popupPage(700, 1024, \"formIntake.jsp?demographic_no="+demographic_no+"\")'>In</a> " : "";
				}

				// add B button to string
				if (hasPrivilege("_billing", roleName$)) {
					buttons += "| <a href='#' onClick=\"popupPage(700,1000,'../billing.do?skipReload=true&billRegion="+URLEncoder.encode(prov)+"&billForm="+URLEncoder.encode(oscarProperties.getProperty("default_view"))+"&hotclick=&appointment_no=0&demographic_name="+URLEncoder.encode(clLastName)+"%2C"+URLEncoder.encode(clFirstName)+"&demographic_no="+demographic_no+"&providerview=1&user_no="+curUser_no+"&apptProvider_no=none&appointment_date="+year+"-"+month+"-"+day+"&start_time=0:00&bNewForm=1&status=t');return false;\" title='Billing'>B</a> ";
					buttons += "| <a href='#' onClick=\"popupPage(700,1000,'../billing/CA/ON/billinghistory.jsp?demographic_no="+demographic_no+"&last_name="+URLEncoder.encode(clLastName)+"&first_name="+URLEncoder.encode(clFirstName)+"&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10');return false;\" title='Billing'>BHx</a> ";
				}

				// add M button to string
				if (hasPrivilege("_masterLink", roleName$)) {
					buttons += "| <a href='#' onClick=\"popupPage(700,1000,'../demographic/demographiccontrol.jsp?demographic_no="+demographic_no+"&displaymode=edit&dboperation=search_detail');return false;\" title='Master File'>M</a> ";
				}

				// add Rx button to string
				if (isModuleLoaded(request, "TORONTO_RFQ", true) && hasPrivilege("_appointment.doctorLink", roleName$)) {
					buttons += "| <a href='#' onClick=\"popupOscarRx(700,1027,'../oscarRx/choosePatient.do?providerNo="+curUser_no+"&demographicNo="+demographic_no+"');return false;\">Rx</a> ";
				}

				// add Tickler button to string
				buttons += "| <a href='#' onclick=\"popupPage('700', '1000', '../tickler/ticklerAdd.jsp?name="+URLEncoder.encode(clLastName)+"%2C"+URLEncoder.encode(clFirstName)+"&chart_no=&bFirstDisp=false&demographic_no="+demographic_no+"&messageID=null&doctor_no="+curUser_no+"'); return false;\">T</a> ";

				// add Msg button to string
				buttons += "| <a href='#' onclick=\"popupPage('700', '1000', '../oscarMessenger/SendDemoMessage.do?demographic_no="+demographic_no+"'); return false;\">Msg</a> ";
				
				entry.add(buttons);
			}
			
			// age
			if (hasPrivilege("_caseload.Age", roleName$)){
				String clAge = demographicResult.get(0).get("age") != null ? demographicResult.get(0).get("age").toString() : "";
				String clBDay = demographicResult.get(0).get("month_of_birth").toString()+"-"+demographicResult.get(0).get("date_of_birth").toString();
				if (isBirthday(monthDay,clBDay)) {
					clAge += " <img src='../images/cake.gif' height='20' />";
				}
				entry.add(clAge);
			}

			// sex
			if (hasPrivilege("_caseload.Sex", roleName$)){
				String clSex = demographicResult.get(0).get("sex").toString();
				entry.add(clSex);
			}

			// last appt
			if (hasPrivilege("_caseload.LastAppt", roleName$)){
				String lapptQuery = "cl_last_appt";
				List<Map<String, Object>> lapptResult = caseloadDao.getCaseloadDemographicData(lapptQuery, demographicParam);
				if ((!lapptResult.isEmpty()) && lapptResult.get(0).get("max(appointment_date)")!=null && !lapptResult.get(0).get("max(appointment_date)").toString().equals("")) {
					String clLappt = lapptResult.get(0).get("max(appointment_date)").toString();

					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../demographic/demographiccontrol.jsp?demographic_no="+demographic_no+"&last_name="+URLEncoder.encode(clLastName)+"&first_name="+URLEncoder.encode(clFirstName)+"&orderby=appttime&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25'); return false;\">"+ clLappt + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// next appt
			if (hasPrivilege("_caseload.NextAppt", roleName$)){
				String napptQuery = "cl_next_appt";
				List<Map<String, Object>> napptResult = caseloadDao.getCaseloadDemographicData(napptQuery, demographicParam);
				if (!napptResult.isEmpty() && napptResult.get(0).get("min(appointment_date)")!=null && !napptResult.get(0).get("min(appointment_date)").toString().equals("")) {
					String clNappt = napptResult.get(0).get("min(appointment_date)").toString();
					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../demographic/demographiccontrol.jsp?demographic_no="+demographic_no+"&last_name="+URLEncoder.encode(clLastName)+"&first_name="+URLEncoder.encode(clFirstName)+"&orderby=appttime&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25'); return false;\">" + clNappt + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// num appts in last year
			if (hasPrivilege("_caseload.ApptsLYTD", roleName$)){
				String numApptsQuery = "cl_num_appts";
				List<Map<String, Object>> numApptsResult = caseloadDao.getCaseloadDemographicData(numApptsQuery, demographicParam);
				if (!numApptsResult.isEmpty() && numApptsResult.get(0).get("count(*)")!=null && !numApptsResult.get(0).get("count(*)").toString().equals("") && !numApptsResult.get(0).get("count(*)").toString().equals("0")) {
					String clNumAppts = numApptsResult.get(0).get("count(*)").toString();
					entry.add(clNumAppts);
				} else {
					entry.add("&nbsp;");
				}
			}

			// new labs
			String[] userDemoParam = new String[2];
			
			if (hasPrivilege("_caseload.Lab", roleName$)){
				userDemoParam[0] = curUser_no;
				userDemoParam[1] = demographic_no;
				String newLabQuery = "cl_new_labs";
				List<Map<String, Object>> newLabResult = caseloadDao.getCaseloadDemographicData(newLabQuery, userDemoParam);
				if (!newLabResult.isEmpty() && newLabResult.get(0).get("count(*)")!=null && !newLabResult.get(0).get("count(*)").toString().equals("") && !newLabResult.get(0).get("count(*)").toString().equals("0")) {
					String clNewLab = newLabResult.get(0).get("count(*)").toString();

					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../dms/inboxManage.do?method=prepareForIndexPage&providerNo="+curUser_no+"&selectedCategory=CATEGORY_PATIENT_SUB&selectedCategoryPatient="+demographic_no+"&selectedCategoryType=CATEGORY_TYPE_HL7'); return false;\">" + clNewLab + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}
			
			// new docs
			if (hasPrivilege("_caseload.Doc", roleName$)){
				String newDocQuery = "cl_new_docs";
				List<Map<String, Object>> newDocResult = caseloadDao.getCaseloadDemographicData(newDocQuery, userDemoParam);
				if (!newDocResult.isEmpty() && newDocResult.get(0).get("count(*)")!=null && !newDocResult.get(0).get("count(*)").toString().equals("") && !newDocResult.get(0).get("count(*)").toString().equals("0")) {
					String clNewDoc = newDocResult.get(0).get("count(*)").toString();
					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../dms/inboxManage.do?method=prepareForIndexPage&providerNo="+curUser_no+"&selectedCategory=CATEGORY_PATIENT_SUB&selectedCategoryPatient="+demographic_no+"&selectedCategoryType=CATEGORY_TYPE_DOC'); return false;\">" + clNewDoc + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// new ticklers
			if (hasPrivilege("_caseload.Tickler", roleName$)){
				String newTicklerQuery = "cl_new_ticklers";
			    List<Map<String, Object>> newTicklerResult = caseloadDao.getCaseloadDemographicData(newTicklerQuery, demographicParam);
			    if (!newTicklerResult.isEmpty() && newTicklerResult.get(0).get("count(*)")!=null && !newTicklerResult.get(0).get("count(*)").toString().equals("") && !newTicklerResult.get(0).get("count(*)").toString().equals("0")) {
					String clNewTickler = newTicklerResult.get(0).get("count(*)").toString();
					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../tickler/ticklerDemoMain.jsp?demoview="+demographic_no+"'); return false;\">" + clNewTickler + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// new messages
			if (hasPrivilege("_caseload.Msg", roleName$)){
				String newMsgQuery = "cl_new_msgs";
				List<Map<String, Object>> newMsgResult = caseloadDao.getCaseloadDemographicData(newMsgQuery, demographicParam);
				if (!newMsgResult.isEmpty() && newMsgResult.get(0).get("count(*)")!=null && !newMsgResult.get(0).get("count(*)").toString().equals("") && !newMsgResult.get(0).get("count(*)").toString().equals("0")) {
					String clNewMsg = newMsgResult.get(0).get("count(*)").toString();
					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarMessenger/DisplayDemographicMessages.do?orderby=date&boxType=3&demographic_no="+demographic_no+"&providerNo="+curUser_no+"&userName="+URLEncoder.encode(userfirstname+" "+userlastname)+"'); return false;\">" + clNewMsg + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// measurements
			String msmtQuery = "cl_measurement";
			String[] msmtParam = new String[2];
			msmtParam[1]=demographic_no;
			List<Map<String, Object>> msmtResult = null;
			
			if (hasPrivilege("_caseload.BMI", roleName$)){
				// BMI
				msmtParam[0] = "BMI";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clBmi = msmtResult.get(0).get("dataField").toString();
					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=BMI'); return false;\">" + clBmi + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// BP
			if (hasPrivilege("_caseload.BP", roleName$)){
				msmtParam[0] = "BP";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clBp = msmtResult.get(0).get("dataField").toString();
			 		entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=BP'); return false;\">" + clBp + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// WT
			if (hasPrivilege("_caseload.WT", roleName$)){
				msmtParam[0] = "WT";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clWt = msmtResult.get(0).get("dataField").toString();
			 		entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=WT'); return false;\">" + clWt + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// SMK
			if (hasPrivilege("_caseload.SMK", roleName$)){
				msmtParam[0] = "SMK";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clSmk = msmtResult.get(0).get("dataField").toString();
			 		entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=SMK'); return false;\">" + clSmk + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// A1C
			if (hasPrivilege("_caseload.A1C", roleName$)){
				msmtParam[0] = "A1C";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clA1c = msmtResult.get(0).get("dataField").toString();
			 		entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=A1C'); return false;\">" + clA1c + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// ACR
			if (hasPrivilege("_caseload.ACR", roleName$)){
				msmtParam[0] = "ACR";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clAcr = msmtResult.get(0).get("dataField").toString();
					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=ACR'); return false;\">" + clAcr + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// SCR
			if (hasPrivilege("_caseload.SCR", roleName$)){
				msmtParam[0] = "SCR";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clScr = msmtResult.get(0).get("dataField").toString();
			 		entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=SCR'); return false;\">" + clScr + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// LDL
			if (hasPrivilege("_caseload.LDL", roleName$)){
				msmtParam[0] = "LDL";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clLdl = msmtResult.get(0).get("dataField").toString();
					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=LDL'); return false;\">" + clLdl + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// HDL
			if (hasPrivilege("_caseload.HDL", roleName$)){
				msmtParam[0] = "HDL";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clHdl = msmtResult.get(0).get("dataField").toString();
			 		entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=HDL'); return false;\">" + clHdl + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// TCHD
			if (hasPrivilege("_caseload.TCHD", roleName$)){
				msmtParam[0] = "TCHD";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clTchd = msmtResult.get(0).get("dataField").toString();
			 		entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=TCHD'); return false;\">" + clTchd + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// EGFR
			if (hasPrivilege("_caseload.EGFR", roleName$)){
				msmtParam[0] = "EGFR";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clEgfr = msmtResult.get(0).get("dataField").toString();
			 		entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=EGFR'); return false;\">" + clEgfr + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}

			// EYEE
			if (hasPrivilege("_caseload.EYEE", roleName$)){
				msmtParam[0] = "EYEE";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null && !msmtResult.get(0).get("dataField").toString().equals("")) {
					String clEyee = msmtResult.get(0).get("dataField").toString();
					entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=EYEE'); return false;\">" + clEyee + "</a>");
				} else {
					entry.add("&nbsp;");
				}
			}
			
			// LastEncounterDate
			if (hasPrivilege("_caseload.LastEncounterDate", roleName$)){
				msmtQuery = "LastEncounterDate";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, demographicParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("update_date")!=null && !msmtResult.get(0).get("update_date").toString().equals("")) {
					String lastEncDate = msmtResult.get(0).get("update_date").toString();
					int endIdx = lastEncDate.lastIndexOf('.');
					if (-1 != endIdx){
						lastEncDate = lastEncDate.substring(0, endIdx);
					}
					//entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=LastEncounterDate'); return false;\">" + clEyee + "</a>");
					entry.add(lastEncDate);
				} else {
					entry.add("&nbsp;");
				}
			}
			// LastEncounterType
			if (hasPrivilege("_caseload.LastEncounterType", roleName$)){
				msmtQuery = "LastEncounterType";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, demographicParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("encounter_type")!=null && !msmtResult.get(0).get("encounter_type").toString().equals("")) {
					String lastEncType = msmtResult.get(0).get("encounter_type").toString();
					//entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=LastEncounterType'); return false;\">" + clEyee + "</a>");
					entry.add(lastEncType);
				} else {
					entry.add("&nbsp;");
				}
			}
			
			msmtParam[0]=demographic_no;
			
			// [CASH]AdmissionDate
			if (hasPrivilege("_caseload.CashAdmissionDate", roleName$)){
				msmtQuery = "CashAdmissionDate";
				msmtParam[1]="CASH WAITLIST PROGRAM";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("referral_date")!=null && !msmtResult.get(0).get("referral_date").toString().equals("")) {
					String cashAdDate = msmtResult.get(0).get("referral_date").toString();
					//entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=CashAdmissionDate'); return false;\">" + clEyee + "</a>");
					int endIdx = cashAdDate.lastIndexOf('.');
					if (-1 != endIdx){
						cashAdDate = cashAdDate.substring(0, endIdx);
					}
					entry.add(cashAdDate);
				} else {
					entry.add("&nbsp;");
				}
			}
			
			// [ACCESS1]AdmissionDate
			if (hasPrivilege("_caseload.Access1AdmissionDate", roleName$)){
				msmtQuery = "Access1AdmissionDate";
				msmtParam[1]="ACCESS 1 WAITLIST PROGRAM";
				msmtResult = caseloadDao.getCaseloadDemographicData(msmtQuery, msmtParam);
				if (!msmtResult.isEmpty() && msmtResult.get(0).get("referral_date")!=null && !msmtResult.get(0).get("referral_date").toString().equals("")) {
					String access1Date = msmtResult.get(0).get("referral_date").toString();
					//entry.add("<a href='#' onclick=\"popupPage('700', '1000', '../oscarEncounter/oscarMeasurements/SetupDisplayHistory.do?demographicNo="+demographic_no+"&type=Access1AdmissionDate'); return false;\">" + clEyee + "</a>");
					int endIdx = access1Date.lastIndexOf('.');
					if (-1 != endIdx){
						access1Date = access1Date.substring(0, endIdx);
					}
					entry.add(access1Date);
				} else {
					entry.add("&nbsp;");
				}
			}

			data.add(entry);
		}
		return data;
	}

	public boolean hasPrivilege(String objectName, String roleName) {
		ArrayList<Object> v = OscarRoleObjectPrivilege.getPrivilegePropAsArrayList(objectName);
		return OscarRoleObjectPrivilege.checkPrivilege(roleName, (Properties) v.get(0), (ArrayList<String>) v.get(1));
	}

	public boolean isModuleLoaded(HttpServletRequest request, String moduleName, boolean reverse) {
        OscarProperties proper = OscarProperties.getInstance();
        boolean result = false;
        if (proper.getProperty(moduleName, "").equalsIgnoreCase("yes") || proper.getProperty(moduleName, "").equalsIgnoreCase("true") || proper.getProperty(moduleName, "").equalsIgnoreCase("on")) {
            result = true;
        }
        return reverse ? !result : result;
    }

	/**
	Checks if the schedule day is patients birthday
	**/
	public boolean isBirthday(String schedDate,String demBday){
		return schedDate.equals(demBday);
	}
	public boolean patientHasOutstandingPrivateBills(String demographicNo){
		oscar.oscarBilling.ca.bc.MSP.MSPReconcile msp = new oscar.oscarBilling.ca.bc.MSP.MSPReconcile();
		return msp.patientHasOutstandingPrivateBill(demographicNo);
	}
}

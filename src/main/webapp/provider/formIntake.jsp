<%--

    Copyright (c) 2008-2012 Indivica Inc.

    This software is made available under the terms of the
    GNU General Public License, Version 2, 1991 (GPLv2).
    License details are available via "indivica.ca/gplv2"
    and "gnu.org/licenses/gpl-2.0.html".

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ page import="java.util.*, java.sql.*, oscar.*, oscar.oscarRx.util.*, oscar.util.*" errorPage="errorpage.jsp"%>
<%@ page import="org.oscarehr.common.dao.MeasurementDao" %>
<%@ page import="org.oscarehr.common.model.Measurement" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>


<%
	MeasurementDao dao = SpringUtils.getBean(MeasurementDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
%>

<%@page import="oscar.OscarProperties" %>


<%!
public boolean isNumeric (String s) {
	try{
		Float.parseFloat(s);
	} catch(NumberFormatException e) {
		return false;
	}
	return true;
}
%>
<%
OscarProperties props = OscarProperties.getInstance();
String project = request.getContextPath();
String curUser_no = (String) session.getAttribute("user");
String demographic_no = ""+Integer.parseInt(request.getParameter("demographic_no"));
boolean selfSubmit = (request.getParameter("selfsubmit") != null) ? request.getParameter("selfsubmit").equals("1") : false;

SuperSiteUtil.getInstance().checkSuperSiteAccess(request, response, "demographic_no");

// on submit
if (selfSubmit) {
	
	
	List<Map<String, Object>> measurementSetResult;

	if (request.getParameter("ht_value")!=null && isNumeric(request.getParameter("ht_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("ht_value"));
		m.setMeasuringInstruction("cm");
		m.setComments((request.getParameter("ht_comment") != null) ? request.getParameter("ht_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("HT");
		dao.persist(m);
	}
	
	if (request.getParameter("wt_value")!=null && isNumeric(request.getParameter("wt_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("wt_value"));
		m.setMeasuringInstruction("kg");
		m.setComments((request.getParameter("wt_comment") != null) ? request.getParameter("wt_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("WT");
		dao.persist(m);
	}

	if (request.getParameter("bmi_value")!=null && isNumeric(request.getParameter("bmi_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("bmi_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("bmi_comment") != null) ? request.getParameter("bmi_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("BMI");
		dao.persist(m);
	}

	if (request.getParameter("head_value")!=null && isNumeric(request.getParameter("head_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("head_value"));
		m.setMeasuringInstruction("cm");
		m.setComments((request.getParameter("head_comment") != null) ? request.getParameter("head_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("HEAD");
		dao.persist(m);
	}

	if (request.getParameter("waist_value")!=null && isNumeric(request.getParameter("waist_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("waist_value"));
		m.setMeasuringInstruction("cm");
		m.setComments((request.getParameter("waist_comment") != null) ? request.getParameter("waist_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("WAIS");
		dao.persist(m);
	}

	if (request.getParameter("hip_value")!=null && isNumeric(request.getParameter("hip_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("hip_value"));
		m.setMeasuringInstruction("cm");
		m.setComments((request.getParameter("hip_comment") != null) ? request.getParameter("hip_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("HIP");
		dao.persist(m);
	}

	if (request.getParameter("whr_value")!=null && isNumeric(request.getParameter("whr_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("whr_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("whr_comment") != null) ? request.getParameter("whr_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("WHR");
		dao.persist(m);
	}

	if (request.getParameter("bp_value_s")!=null && isNumeric(request.getParameter("bp_value_s")) && request.getParameter("bp_value_d")!=null && isNumeric(request.getParameter("bp_value_d"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("bp_value_s") + "/" + request.getParameter("bp_value_d"));
		m.setMeasuringInstruction(request.getParameter("bp_instruction"));
		m.setComments((request.getParameter("bp_comment") != null) ? request.getParameter("bp_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("BP");
		dao.persist(m);
	}

	if (request.getParameter("pulse_value")!=null && isNumeric(request.getParameter("pulse_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("pulse_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("pulse_comment") != null) ? request.getParameter("pulse_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("HR");
		dao.persist(m);
	}

	if (request.getParameter("resp_value")!=null && isNumeric(request.getParameter("resp_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("resp_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("resp_comment") != null) ? request.getParameter("resp_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("RESP");
		dao.persist(m);
	}

	if (request.getParameter("temp_value")!=null && isNumeric(request.getParameter("temp_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("temp_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("temp_comment") != null) ? request.getParameter("temp_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("TEMP");
		dao.persist(m);
	}

	if (request.getParameter("o2_value")!=null && isNumeric(request.getParameter("o2_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("o2_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("o2_comment") != null) ? request.getParameter("o2_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("O2");
		dao.persist(m);
	}

	if (request.getParameter("feet_sensation_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("feet_sensation_value"));
		m.setMeasuringInstruction("Sensation");
		m.setComments((request.getParameter("feet_sensation_comment") != null) ? request.getParameter("feet_sensation_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("FEET");
		dao.persist(m);
	}

	if (request.getParameter("feet_vibration_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("feet_vibration_value"));
		m.setMeasuringInstruction("Vibration");
		m.setComments((request.getParameter("feet_vibration_comment") != null) ? request.getParameter("feet_vibration_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("FEET");
		dao.persist(m);
	}

	if (request.getParameter("feet_reflexes_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("feet_reflexes_value"));
		m.setMeasuringInstruction("Reflexes");
		m.setComments((request.getParameter("feet_reflexes_comment") != null) ? request.getParameter("feet_reflexes_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("FEET");
		dao.persist(m);
	}

	if (request.getParameter("feet_pulses_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("feet_pulses_value"));
		m.setMeasuringInstruction("Pulses");
		m.setComments((request.getParameter("feet_pulses_comment") != null) ? request.getParameter("feet_pulses_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("FEET");
		dao.persist(m);
	}

	if (request.getParameter("feet_infection_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("feet_infection_value"));
		m.setMeasuringInstruction("Infection");
		m.setComments((request.getParameter("feet_infection_comment") != null) ? request.getParameter("feet_infection_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("FEET");
		dao.persist(m);
	}

	if (request.getParameter("exercise_value")!=null && isNumeric(request.getParameter("exercise_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("exercise_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("exercise_comment") != null) ? request.getParameter("exercise_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("Exer");
		dao.persist(m);
	}

	if (request.getParameter("drinks_value")!=null && isNumeric(request.getParameter("drinks_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("drinks_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("drinks_comment") != null) ? request.getParameter("drinks_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("DRPW");
		dao.persist(m);
	}

	if (request.getParameter("smoking_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("smoking_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("smoking_comment") != null) ? request.getParameter("smoking_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("SMK");
		dao.persist(m);
	}

	if (request.getParameter("smokingrecent_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("smokingrecent_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("smokingrecent_comment") != null) ? request.getParameter("smokingrecent_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("SmkR");
		dao.persist(m);
	}

	if (request.getParameter("smokingstartyear_value")!=null && isNumeric(request.getParameter("smokingstartyear_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(String.format("%04d", Integer.parseInt(request.getParameter("smokingstartyear_value"))));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("smokingstart_comment") != null) ? request.getParameter("smokingstart_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("SmkS");
		dao.persist(m);
	}

	if (request.getParameter("smokingcessyear_value")!=null && isNumeric(request.getParameter("smokingcessyear_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(String.format("%04d", Integer.parseInt(request.getParameter("smokingcessyear_value"))));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("smokingcess_comment") != null) ? request.getParameter("smokingcess_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("SmkC");
		dao.persist(m);
	}

	if (request.getParameter("smokingpks_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("smokingpks_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("smokingpks_comment") != null) ? request.getParameter("smokingpks_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("SmkD");
		dao.persist(m);
	}

	if (request.getParameter("smokingpyhx_value")!=null && isNumeric(request.getParameter("smokingpyhx_value"))) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("smokingpyhx_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("smokingpyhx_comment") != null) ? request.getParameter("smokingpyhx_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("SmkPY");
		dao.persist(m);
	}

	if (request.getParameter("smokingadvised_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("smokingadvised_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("smokingadvised_comment") != null) ? request.getParameter("smokingadvised_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("SmkA");
		dao.persist(m);
	}

	if (request.getParameter("smokingquit_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("smokingquit_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("smokingquit_comment") != null) ? request.getParameter("smokingquit_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("SmkQ");
		dao.persist(m);
	}

	if (request.getParameter("smokingfollowup_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("smokingfollowup_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("smokingfollowup_comment") != null) ? request.getParameter("smokingfollowup_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("SmkF");
		dao.persist(m);
	}

	if (request.getParameter("pregnancy_test_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("pregnancy_test_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("pregnancy_test_comment") != null) ? request.getParameter("pregnancy_test_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("PRGT");
		dao.persist(m);
	}
	
	if (request.getParameter("urine_dip_test_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("urine_dip_test_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("urine_dip_test_comment") != null) ? request.getParameter("urine_dip_test_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("UDIP");
		dao.persist(m);
	}
	
	if (request.getParameter("glucose_monitor_test_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("glucose_monitor_test_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("glucose_monitor_test_comment") != null) ? request.getParameter("glucose_monitor_test_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("GLMT");
		dao.persist(m);
	}
	
	if (request.getParameter("eye_check_up_l_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("eye_check_up_l_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("eye_check_up_l_comment") != null) ? request.getParameter("eye_check_up_l_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("LECM");
		dao.persist(m);
	}
	

	if (request.getParameter("eye_check_up_r_value")!=null) {
		Measurement m = new Measurement();
		m.setDemographicId(Integer.parseInt(demographic_no));
		m.setProviderNo(curUser_no);
		m.setAppointmentNo(0);
		m.setDataField(request.getParameter("eye_check_up_r_value"));
		m.setMeasuringInstruction("");
		m.setComments((request.getParameter("eye_check_up_r_comment") != null) ? request.getParameter("eye_check_up_r_comment") : "");
		m.setDateObserved(new java.util.Date());
		m.setType("RECM");
		dao.persist(m);
	}
	
}

// no submit
String[] demographicParam = new String[1];
demographicParam[0] = demographic_no;


Demographic demographic = demographicDao.getDemographic(demographic_no);
String demographicName = demographic.getFormattedName();
java.util.Date demographicDOB = new GregorianCalendar(Integer.parseInt(demographic.getYearOfBirth()), Integer.parseInt(demographic.getMonthOfBirth())-1, Integer.parseInt(demographic.getDateOfBirth())).getTime();
String demographicBirthYear = demographic.getYearOfBirth();

String remindersQuery = "intake_reminders";
List<Map<String, Object>> remindersResult = oscarSuperManager.find("providerDao", remindersQuery, demographicParam);
String remindersList = (!remindersResult.isEmpty() && remindersResult.get(0).get("note")!=null) ? remindersResult.get(0).get("note").toString() : "";
if (remindersResult.size() > 1) {
	for (int i=1; i<remindersResult.size(); i++) {
		remindersList += ",<br/>" + remindersResult.get(i).get("note").toString();
	}
}

String pharmacyQuery = "intake_pharmacy";
List<Map<String, Object>> pharmacyResult = oscarSuperManager.find("providerDao", pharmacyQuery, demographicParam);
String pharmacyName = (!pharmacyResult.isEmpty() && pharmacyResult.get(0).get("name")!=null) ? pharmacyResult.get(0).get("name").toString() : "";

String allergiesQuery = "intake_allergies";
List<Map<String, Object>> allergiesResult = oscarSuperManager.find("providerDao", allergiesQuery, demographicParam);
String allergiesList = (!allergiesResult.isEmpty() && allergiesResult.get(0).get("DESCRIPTION")!=null) ? allergiesResult.get(0).get("DESCRIPTION").toString() : "";
if (allergiesResult.size() > 1) {
	for (int i=1; i<allergiesResult.size(); i++) {
		allergiesList += ",<br/>" + allergiesResult.get(i).get("DESCRIPTION").toString();
	}
}

String medicationsQuery = "intake_medications";
List<Map<String, Object>> medicationsResult = oscarSuperManager.find("providerDao", medicationsQuery, demographicParam);
String medicationsList = "";
if (!medicationsResult.isEmpty()) {
	for (int i=0; i<medicationsResult.size(); i++) {
		if (i != 0) {
			medicationsList += ",<br/>";
		}

		if (medicationsResult.get(i).get("customName")!=null && !medicationsResult.get(i).get("customName").toString().equals("null")) {
			medicationsList += medicationsResult.get(i).get("customName").toString();
		} else if (Integer.parseInt(medicationsResult.get(i).get("GCN_SEQNO").toString())==0) {
			medicationsList += "Unknown";	
		} else {
			if(medicationsResult.get(i).get("BN")!=null)
			medicationsList += medicationsResult.get(i).get("BN").toString();
		}
		
		medicationsList += " " + RxUtil.FloatToString(Float.parseFloat(medicationsResult.get(i).get("takemin").toString()));
		if (!medicationsResult.get(i).get("takemin").toString().equals(medicationsResult.get(i).get("takemax").toString())) {
			medicationsList += "-" + RxUtil.FloatToString(Float.parseFloat(medicationsResult.get(i).get("takemax").toString()));
		}

		
		if (medicationsResult.get(i).get("freqcode") != null) {
			medicationsList += " " + medicationsResult.get(i).get("freqcode").toString();
		}
		
		if (medicationsResult.get(i).get("prn").toString().equals("1")) {
			medicationsList += " PRN";
		}

		if (medicationsResult.get(i).get("duration") != null && !medicationsResult.get(i).get("duration").toString().equals("null")) {
			medicationsList += " " + medicationsResult.get(i).get("duration").toString();
			if (medicationsResult.get(i).get("durunit")!=null) {
				if (medicationsResult.get(i).get("durunit").toString().equals("D")) {
					medicationsList += " Day";
				} else if (medicationsResult.get(i).get("durunit").toString().equals("W")) {
					medicationsList += " Week";
				} else if (medicationsResult.get(i).get("durunit").toString().equals("M")) {
					medicationsList += " Month";
				}
			}
		}
		
		if (medicationsResult.get(i).get("duration")!=null && !medicationsResult.get(i).get("duration").toString().equals("null") && !medicationsResult.get(i).get("duration").toString().equals("") && Integer.parseInt(medicationsResult.get(i).get("duration").toString())>1) {
			medicationsList += "s";
		}

		medicationsList += "  " + medicationsResult.get(i).get("quantity").toString() + " Qty  Repeats: ";

		medicationsList += medicationsResult.get(i).get("repeat").toString();

		if (medicationsResult.get(i).get("repeat").toString().equals("1")) {
			medicationsList += " No subs";
		}
	}
}

String preventionsQuery = "intake_preventions";
List<Map<String, Object>> preventionsResult = oscarSuperManager.find("providerDao", preventionsQuery, demographicParam);

String preventionsList = "";
String curPrevention = "";
String preventionDateStr, demographicAge;
String[] preventionDateArr;
java.util.Date preventionDate;

if (!preventionsResult.isEmpty()) {
        for (int i=0; i<preventionsResult.size(); i++) {
	       	try {
				preventionDateStr = preventionsResult.get(i).get("prevention_date").toString();
				preventionDateArr = preventionsResult.get(i).get("prevention_date").toString().split("-");
				preventionDate = new GregorianCalendar(Integer.parseInt(preventionDateArr[0]), Integer.parseInt(preventionDateArr[1])-1, Integer.parseInt(preventionDateArr[2])).getTime(); 
				
	       	} catch (Exception e) {
	       		preventionDateStr = "0001-01-01";
	       		preventionDateArr = preventionDateStr.split("-");
	       		preventionDate = new GregorianCalendar(1,1,1).getTime();
	       	}
	       	demographicAge = UtilDateUtilities.calcAgeAtDate(demographicDOB, preventionDate);
			if (!preventionsResult.get(i).get("prevention_type").toString().equals(curPrevention)) {
				if (i != 0) {
					preventionsList += "<br/>";
				} 
				curPrevention = preventionsResult.get(i).get("prevention_type").toString();
				preventionsList += "<b>" + curPrevention + ":</b> " + preventionDateStr + " (" + demographicAge + ")";
			} else {
				preventionsList += ", " + preventionDateStr + " (" + demographicAge + ")";
			}
	}
}

String dxCodeQuery = "intake_patient_dxcode";
String[] dxCodeParam = new String[2];
dxCodeParam[0] = demographic_no;
String dxCodeList = "";

dxCodeParam[1] = "250";
List<Map<String, Object>> dxCodeResult = oscarSuperManager.find("providerDao", dxCodeQuery, dxCodeParam);
if (!dxCodeResult.isEmpty()) {
	dxCodeList += "<a href='#' onclick='popupPage(\"700\", \"1000\", \""+project+"/oscarEncounter/oscarMeasurements/TemplateFlowSheet.jsp?demographic_no="+demographic_no+"&template=diab2\"); return false;'>Diabetic Flowsheet</a><br />";
}

dxCodeParam[1] = "401";
dxCodeResult = oscarSuperManager.find("providerDao", dxCodeQuery, dxCodeParam);
if (!dxCodeResult.isEmpty()) {
	dxCodeList += "<a href='#' onclick='popupPage(\"700\", \"1000\", \""+project+"/oscarEncounter/oscarMeasurements/TemplateFlowSheet.jsp?demographic_no="+demographic_no+"&template=hyptension\"); return false;'>Hypertension Flowsheet</a><br />";
}

boolean showInr = false;
dxCodeParam[1] = "42731";
dxCodeResult = oscarSuperManager.find("providerDao", dxCodeQuery, dxCodeParam);
showInr = showInr || !dxCodeResult.isEmpty();
dxCodeParam[1] = "V1251";
dxCodeResult = oscarSuperManager.find("providerDao", dxCodeQuery, dxCodeParam);
showInr = showInr || !dxCodeResult.isEmpty();
dxCodeParam[1] = "V5861";
dxCodeResult = oscarSuperManager.find("providerDao", dxCodeQuery, dxCodeParam);
showInr = showInr || !dxCodeResult.isEmpty();
if (showInr) {
	dxCodeList += "<a href='#' onclick='popupPage(\"700\", \"1000\", \""+project+"/oscarEncounter/oscarMeasurements/TemplateFlowSheet.jsp?demographic_no="+demographic_no+"&template=inrFlow\"); return false;'>INR Flowsheet</a><br />";
}

dxCodeParam[1] = "042";
dxCodeResult = oscarSuperManager.find("providerDao", dxCodeQuery, dxCodeParam);
if (!dxCodeResult.isEmpty()) {
	dxCodeList += "<a href='#' onclick='popupPage(\"700\", \"1000\",  \""+project+"/oscarEncounter/oscarMeasurements/TemplateFlowSheet.jsp?demographic_no="+demographic_no+"&template=hiv\"); return false;'>HIV Flowsheet</a><br />";
}

String msmtQuery = "intake_get_measurement";
String[] msmtParam = new String[2];
msmtParam[1] = demographic_no;

msmtParam[0] = "HT";
List<Map<String, Object>> msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueHT = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" cm" : "";
String msmtDateHT = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentHT = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "WT";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueWT = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" kg" : "";
String msmtDateWT = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentWT = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "BMI";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueBMI = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateBMI = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentBMI = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";


msmtParam[0] = "HEAD";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueHEAD = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" cm" : "";
String msmtDateHEAD = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentHEAD = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "WAIS";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueWAIST = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" cm" : "";
String msmtDateWAIST = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentWAIST = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "HIP";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueHIP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" cm" : "";
String msmtDateHIP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentHIP = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "WHR";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueWHR = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateWHR = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentWHR = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "BP";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueBP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+"<br />("+msmtResult.get(0).get("measuringInstruction").toString()+")" : "";
String msmtDateBP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentBP = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "HR";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValuePULSE = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" bpm" : "";
String msmtDatePULSE = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentPULSE = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "RESP";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueRESP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" bpm" : "";
String msmtDateRESP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentRESP = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "TEMP";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueTEMP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" &deg;C" : "";
String msmtDateTEMP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentTEMP = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "O2";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueO2 = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateO2 = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentO2 = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

String msmtQueryEx = "intake_get_measurement_ex";
String[] msmtParamEx = new String[3];
msmtParamEx[0] = "FEET";
msmtParamEx[1] = demographic_no;

msmtParamEx[2] = "Sensation";
List<Map<String, Object>> msmtResultEx = oscarSuperManager.find("providerDao", msmtQueryEx, msmtParamEx);
String msmtValueFEETsensation = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dataField")!=null) ? msmtResultEx.get(0).get("dataField").toString() : "";
String msmtDateFEETsensation = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dateObserved")!=null) ? msmtResultEx.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentFEETsensation = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("comments")!=null) ? msmtResultEx.get(0).get("comments").toString() : "";

msmtParamEx[2] = "Vibration";
msmtResultEx = oscarSuperManager.find("providerDao", msmtQueryEx, msmtParamEx);
String msmtValueFEETvibration = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dataField")!=null) ? msmtResultEx.get(0).get("dataField").toString() : "";
String msmtDateFEETvibration = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dateObserved")!=null) ? msmtResultEx.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentFEETvibration = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("comments")!=null) ? msmtResultEx.get(0).get("comments").toString() : "";

msmtParamEx[2] = "Reflexes";
msmtResultEx = oscarSuperManager.find("providerDao", msmtQueryEx, msmtParamEx);
String msmtValueFEETreflexes = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dataField")!=null) ? msmtResultEx.get(0).get("dataField").toString() : "";
String msmtDateFEETreflexes = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dateObserved")!=null) ? msmtResultEx.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentFEETreflexes = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("comments")!=null) ? msmtResultEx.get(0).get("comments").toString() : "";

msmtParamEx[2] = "Pulses";
msmtResultEx = oscarSuperManager.find("providerDao", msmtQueryEx, msmtParamEx);
String msmtValueFEETpulses = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dataField")!=null) ? msmtResultEx.get(0).get("dataField").toString() : "";
String msmtDateFEETpulses = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dateObserved")!=null) ? msmtResultEx.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentFEETpulses = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("comments")!=null) ? msmtResultEx.get(0).get("comments").toString() : "";

msmtParamEx[2] = "Infection";
msmtResultEx = oscarSuperManager.find("providerDao", msmtQueryEx, msmtParamEx);
String msmtValueFEETinfection = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dataField")!=null) ? msmtResultEx.get(0).get("dataField").toString() : "";
String msmtDateFEETinfection = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("dateObserved")!=null) ? msmtResultEx.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentFEETinfection = (!msmtResultEx.isEmpty() && msmtResultEx.get(0).get("comments")!=null) ? msmtResultEx.get(0).get("comments").toString() : "";

msmtParam[0] = "Exer";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueEXERCISE = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" min/wk" : "";
String msmtDateEXERCISE = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentEXERCISE = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "DRPW";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueDRINKS = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" drinks/wk" : "";
String msmtDateDRINKS = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentDRINKS = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "SMK";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueSMK = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateSMK = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentSMK = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "SmkR";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueSmkR = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateSmkR = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentSmkR = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "SmkS";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueSmkS = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateSmkS = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentSmkS = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "SmkC";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueSmkC = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateSmkC = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentSmkC = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "SmkD";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueSmkD = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" ppd" : "";
String msmtDateSmkD = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentSmkD = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "SmkPY";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueSmkPY = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString()+" pyhx" : "";
String msmtDateSmkPY = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentSmkPY = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "SmkA";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueSmkA = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateSmkA = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentSmkA = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "SmkQ";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueSmkQ = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateSmkQ = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentSmkQ = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "SmkF";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueSmkF = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateSmkF = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentSmkF = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "PRGT";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValuePRGT = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDatePRGT = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentPRGT = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "UDIP";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueUDIP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateUDIP = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentUDIP = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "GLMT";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueGLMT = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateGLMT = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentGLMT = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "LECM";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueLECM = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateLECM = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentLECM = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";

msmtParam[0] = "RECM";
msmtResult = oscarSuperManager.find("providerDao", msmtQuery, msmtParam);
String msmtValueRECM = (!msmtResult.isEmpty() && msmtResult.get(0).get("dataField")!=null) ? msmtResult.get(0).get("dataField").toString() : "";
String msmtDateRECM = (!msmtResult.isEmpty() && msmtResult.get(0).get("dateObserved")!=null) ? msmtResult.get(0).get("dateObserved").toString().split(" ")[0] : "";
String msmtCommentRECM = (!msmtResult.isEmpty() && msmtResult.get(0).get("comments")!=null) ? msmtResult.get(0).get("comments").toString() : "";
%>
<html>
<head>
<title>Intake</title>
<style>

table {
	font-family: Verdana,Tahoma,Arial,sans-serif;
	font-size: 12px;
	line-height: 14px;
	border: 1px solid #9d9d9d;
	border-collapse:collapse;
	margin-left: auto;
	margin-right: auto;
	width: 100%;
}

th {
	background: #9d9d9d;
	color: #fff;
	text-align: left;
	padding: 0.2em 0.5em;
}

td {
	padding: 0.4em 0.5em;
}

.rowheader {
	text-align: right;
	vertical-align: text-top;
}

.rowheader2 {
	text-align: right;
}

.header {
	font-weight: bold;
}

a {
	font-weight: bold;
	color: #000;
}

.centerbox {
	width: 100%;
	text-align: center;
}

p {
	max-width: 15em;
}
</style>
<script type="text/javascript">
var myPopup;
function popupPage(vheight,vwidth,varpage) {
	if (myPopup != null) {
		myPopup.close();
	}
	var page = "" + varpage;
	windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=50,screenY=50,top=0,left=0";
	myPopup=window.open(page, "apptProvider", windowprops);
	if (myPopup != null) {
		if (myPopup.opener == null) {
			myPopup.opener = self;
		}
		myPopup.focus();
	}
}

function isNumber(n) {
  return !isNaN(parseFloat(n)) && isFinite(n);
}

function calcBMI() {
	if (isNumber(document.getElementsByName("ht_value")[0].value) && isNumber(document.getElementsByName("wt_value")[0].value)) {
		document.getElementsByName("bmi_value")[0].value = (document.getElementsByName("wt_value")[0].value/Math.pow(document.getElementsByName("ht_value")[0].value/100,2)).toFixed(1);
	}
}

function calcWHR() {
	if (isNumber(document.getElementsByName("waist_value")[0].value) && isNumber(document.getElementsByName("hip_value")[0].value)) {
		document.getElementsByName("whr_value")[0].value = (document.getElementsByName("waist_value")[0].value/document.getElementsByName("hip_value")[0].value).toFixed(2);
	}
}

function calcSmkSYear() {
	if (isNumber(document.getElementsByName("smokingstartage_value")[0].value)) {
		document.getElementsByName("smokingstartyear_value")[0].value = <%=demographicBirthYear%> + parseInt(document.getElementsByName("smokingstartage_value")[0].value);
	}
}

function calcSmkSAge() {
	if (isNumber(document.getElementsByName("smokingstartyear_value")[0].value)) {
		document.getElementsByName("smokingstartage_value")[0].value = parseInt(document.getElementsByName("smokingstartyear_value")[0].value) - <%=demographicBirthYear%>;
	}
}

function calcSmkCYear() {
	if (isNumber(document.getElementsByName("smokingcessage_value")[0].value)) {
		document.getElementsByName("smokingcessyear_value")[0].value = <%=demographicBirthYear%> + parseInt(document.getElementsByName("smokingcessage_value")[0].value);
	}
}

function calcSmkCAge() {
	if (isNumber(document.getElementsByName("smokingcessyear_value")[0].value)) {
		document.getElementsByName("smokingcessage_value")[0].value = parseInt(document.getElementsByName("smokingcessyear_value")[0].value) - <%=demographicBirthYear%>;
	}
}

function calcPYHX() {
	ppd = -1;
	for (i=0; i<document.getElementsByName("smokingpks_value").length; i++) {
		if (document.getElementsByName("smokingpks_value")[i].checked) {
			ppd = parseFloat(document.getElementsByName("smokingpks_value")[i].value.replace("1/8",1/8).replace("1/4",1/4).replace("1/2",1/2).replace("3/4",3/4));
		}
	}
	if (isNumber(document.getElementsByName("smokingstartyear_value")[0].value) && isNumber(document.getElementsByName("smokingcessyear_value")[0].value)) {
		if (ppd != -1 && isNumber(ppd)) {
			document.getElementsByName("smokingpyhx_value")[0].value = (parseInt(document.getElementsByName("smokingcessyear_value")[0].value) - parseInt(document.getElementsByName("smokingstartyear_value")[0].value))*parseFloat(ppd);
		}	
	}
	// If only the starting year has been set then we use the current year as the cessation year.
	else if (isNumber(document.getElementsByName("smokingstartyear_value")[0].value)) {
		if (ppd != -1 && isNumber(ppd)) {
			document.getElementsByName("smokingpyhx_value")[0].value = (new Date().getFullYear() - parseInt(document.getElementsByName("smokingstartyear_value")[0].value))*parseFloat(ppd);
		}
	}
}
</script>
</head>
<body>
<% if (selfSubmit) { %>
<div class="centerbox">Form Submitted!</div>
<% } %>

<form method="post" action="">
<input type="hidden" name="demographic_no" value="<%=demographic_no%>" />
<input type="hidden" name="selfsubmit" value="1" />
<table>
<tr><th colspan="8"><%=demographicName%></th></tr>
<tr>
	<td class="rowheader"><a href="#" onclick="popupPage('700', '1000', '<%=project%>/CaseManagementEntry.do?method=issuehistory&demographicNo=<%=demographic_no%>&issueIds=38'); return false;">Reminders:</a></td>
	<td colspan="7"><%=remindersList%></td>
</tr><tr>
	<td class="rowheader"><a href="#" onclick="popupPage('700', '1000', '<%=project%>/oscarRx/choosePatient.do?providerNo=999998&demographicNo=<%=demographic_no%>&pharmaList=true'); return false;">Pharmacy:</a></td>
	<td colspan="7"><%=pharmacyName%></td>
</tr><tr>
	<td class="rowheader"><a href="#" onclick="popupPage('700', '1000', '<%=project%>/oscarRx/showAllergy.do?demographicNo=<%=demographic_no%>'); return false;">Allergies:</a></td>
	<td colspan="7"><%=allergiesList%></td>
</tr><tr>
	<td class="rowheader"><a href="#" onclick="popupPage('700', '1000', '<%=project%>/oscarRx/choosePatient.do?providerNo=<%=curUser_no%>&demographicNo=<%=demographic_no%>'); return false;">Medications:</a></td>
	<td colspan="7"><%=medicationsList%></td>
</tr><tr>
	<td class="rowheader"><a href="#" onclick="popupPage('700', '1000', '<%=project%>/oscarPrevention/index.jsp?demographic_no=<%=demographic_no%>'); return false;">Preventions:</a></td>
	<td colspan="7"><%=preventionsList%></td>
</tr><tr>
	<td class="rowheader">Other:</td>
	<td colspan="7"><%=dxCodeList%></td>
</tr>

<tr><th colspan="8">Vitals</th></tr>
<tr class="header">
	<td class="rowheader2">Measurement</td>
	<td colspan="3">Value</td>
	<td>Comment</td>
	<td>Last Value</td>
	<td>Last Date</td>
	<td>Last Comment</td>
</tr>
<tr>
	<td class="rowheader2">Height:</td>
	<td colspan="3"><input type="text" name="ht_value" size="5" onchange="calcBMI()" /> cm</td>
	<td><input type="text" name="ht_comment" /></td>
	<td><%=msmtValueHT%></td>
	<td><%=msmtDateHT%></td>
	<td><p><%=msmtCommentHT%></p></td>
</tr>
<tr>
	<td class="rowheader2">Weight:</td>
	<td colspan="3"><input type="text" name="wt_value" size="5" onchange="calcBMI()" /> kg</td>
	<td><input type="text" name="wt_comment" /></td>
	<td><%=msmtValueWT%></td>
	<td><%=msmtDateWT%></td>
	<td><p><%=msmtCommentWT%></p></td>
</tr>
<tr>
	<td class="rowheader2">BMI:</td>
	<td colspan="3"><input type="text" name="bmi_value" size="5" /></td>
	<td><input type="text" name="bmi_comment" /></td>
	<td><%=msmtValueBMI%></td>
	<td><%=msmtDateBMI%></td>
	<td><p><%=msmtCommentBMI%></p></td>
</tr>
<tr>
	<td class="rowheader2">Head Circ:</td>
	<td colspan="3"><input type="text" name="head_value" size="5" /> cm</td>
	<td><input type="text" name="head_comment" /></td>
	<td><%=msmtValueHEAD%></td>
	<td><%=msmtDateHEAD%></td>
	<td><p><%=msmtCommentHEAD%></p></td>
</tr>
<tr>
	<td class="rowheader2">Waist:</td>
	<td colspan="3"><input type="text" name="waist_value" size="5" onchange="calcWHR()" /> cm</td>
	<td><input type="text" name="waist_comment" /></td>
	<td><%=msmtValueWAIST%></td>
	<td><%=msmtDateWAIST%></td>
	<td><p><%=msmtCommentWAIST%></p></td>
</tr>
<tr>
	<td class="rowheader2">Hip:</td>
	<td colspan="3"><input type="text" name="hip_value" size="5" onchange="calcWHR()" /> cm</td>
	<td><input type="text" name="hip_comment" /></td>
	<td><%=msmtValueHIP%></td>
	<td><%=msmtDateHIP%></td>
	<td><p><%=msmtCommentHIP%></p></td>
</tr>
<tr>
	<td class="rowheader2">Waist:Hip Ratio:</td>
	<td colspan="3"><input type="text" name="whr_value" size="5" /></td>
	<td><input type="text" name="whr_comment" /></td>
	<td><%=msmtValueWHR%></td>
	<td><%=msmtDateWHR%></td>
	<td><p><%=msmtCommentWHR%></p></td>
</tr>
<tr>
	<td class="rowheader2" rowspan="2">BP:</td>
	<td><input type="text" name="bp_value_s" size="5" /> (Sys.)</td>
	<td><input type="radio" name="bp_instruction" value="BP Tru">BP Tru</input></td>
	<td><input type="radio" name="bp_instruction" value="Sitting" checked>Sitting</input></td>
	<td rowspan="2"><input type="text" name="bp_comment" /></td>
	<td rowspan="2"><%=msmtValueBP%></td>
	<td rowspan="2"><%=msmtDateBP%></td>
	<td rowspan="2"><p><%=msmtCommentBP%></p></td>
</tr>
<tr>
	<td><input type="text" name="bp_value_d" size="5" /> (Dia.)</td>
	<td><input type="radio" name="bp_instruction" value="Standing">Standing</input></td>
	<td><input type="radio" name="bp_instruction" value="Supine">Supine</input></td>
</tr>
<tr>
	<td class="rowheader2">Pulse:</td>
	<td colspan="3"><input type="text" name="pulse_value" size="5" /> bpm</td>
	<td><input type="text" name="pulse_comment" /></td>
	<td><%=msmtValuePULSE%></td>
	<td><%=msmtDatePULSE%></td>
	<td><p><%=msmtCommentPULSE%></p></td>
</tr>
<tr>
	<td class="rowheader2">Resp:</td>
	<td colspan="3"><input type="text" name="resp_value" size="5" /> bpm</td>
	<td><input type="text" name="resp_comment" /></td>
	<td><%=msmtValueRESP%></td>
	<td><%=msmtDateRESP%></td>
	<td><p><%=msmtCommentRESP%></p></td>
</tr>
<tr>
	<td class="rowheader2">Temp:</td>
	<td colspan="3"><input type="text" name="temp_value" size="5" /> &deg;C</td>
	<td><input type="text" name="temp_comment" /></td>
	<td><%=msmtValueTEMP%></td>
	<td><%=msmtDateTEMP%></td>
	<td><p><%=msmtCommentTEMP%></p></td>
</tr>
<tr>
	<td class="rowheader2">O2 Sats:</td>
	<td colspan="3"><input type="text" name="o2_value" size="5" /></td>
	<td><input type="text" name="o2_comment" /></td>
	<td><%=msmtValueO2%></td>
	<td><%=msmtDateO2%></td>
	<td><p><%=msmtCommentO2%></p></td>
</tr>
<tr>
	<td class="rowheader2">Feet - Sensation:</td>
	<td colspan="3"><input type="radio" name="feet_sensation_value" value="Yes">Yes</input><input type="radio" name="feet_sensation_value" value="No">No</input></td>
	<td><input type="text" name="feet_sensation_comment" /></td>
	<td><%=msmtValueFEETsensation%></td>
	<td><%=msmtDateFEETsensation%></td>
	<td><p><%=msmtCommentFEETsensation%></p></td>
</tr>
<tr>
	<td class="rowheader2">Feet - Vibration:</td>
	<td colspan="3"><input type="radio" name="feet_vibration_value" value="Yes">Yes</input><input type="radio" name="feet_vibration_value" value="No">No</input></td>
	<td><input type="text" name="feet_vibration_comment" /></td>
	<td><%=msmtValueFEETvibration%></td>
	<td><%=msmtDateFEETvibration%></td>
	<td><p><%=msmtCommentFEETvibration%></p></td>
</tr>
<tr>
	<td class="rowheader2">Feet - Reflexes:</td>
	<td colspan="3"><input type="radio" name="feet_reflexes_value" value="Yes">Yes</input><input type="radio" name="feet_reflexes_value" value="No">No</input></td>
	<td><input type="text" name="feet_reflexes_comment" /></td>
	<td><%=msmtValueFEETreflexes%></td>
	<td><%=msmtDateFEETreflexes%></td>
	<td><p><%=msmtCommentFEETreflexes%></p></td>
</tr>
<tr>
	<td class="rowheader2">Feet - Pulses:</td>
	<td colspan="3"><input type="radio" name="feet_pulses_value" value="Yes">Yes</input><input type="radio" name="feet_pulses_value" value="No">No</input></td>
	<td><input type="text" name="feet_pulses_comment" /></td>
	<td><%=msmtValueFEETpulses%></td>
	<td><%=msmtDateFEETpulses%></td>
	<td><p><%=msmtCommentFEETpulses%></p></td>
</tr>
<tr>
	<td class="rowheader2">Feet - Infection:</td>
	<td colspan="3"><input type="radio" name="feet_infection_value" value="Yes">Yes</input><input type="radio" name="feet_infection_value" value="No">No</input></td>
	<td><input type="text" name="feet_infection_comment" /></td>
	<td><%=msmtValueFEETinfection%></td>
	<td><%=msmtDateFEETinfection%></td>
	<td><p><%=msmtCommentFEETinfection%></p></td>
</tr>
<tr>
	<td class="rowheader2">Exercise:</td>
	<td colspan="3"><input type="text" name="exercise_value" size="5" /> min/wk</td>
	<td><input type="text" name="exercise_comment" /></td>
	<td><%=msmtValueEXERCISE%></td>
	<td><%=msmtDateEXERCISE%></td>
	<td><p><%=msmtCommentEXERCISE%></p></td>
</tr>
<tr>
	<td class="rowheader2">Drinks per Week:</td>
	<td colspan="3"><input type="text" name="drinks_value" size="5" /> drinks/wk</td>
	<td><input type="text" name="drinks_comment" /></td>
	<td><%=msmtValueDRINKS%></td>
	<td><%=msmtDateDRINKS%></td>
	<td><p><%=msmtCommentDRINKS%></p></td>
</tr>
<tr>
	<td class="rowheader2">Smoking:</td>
	<td><input type="radio" name="smoking_value" value="Current">Current</input></td>
	<td><input type="radio" name="smoking_value" value="Former">Former</input></td>
	<td><input type="radio" name="smoking_value" value="Never">Never</input></td>
	<td><input type="text" name="smoking_comment" /></td>
	<td><%=msmtValueSMK%></td>
	<td><%=msmtDateSMK%></td>
	<td><p><%=msmtCommentSMK%></p></td>
</tr>
<tr>
	<td class="rowheader2">Recent Tobacco Use:</td>
	<td><input type="radio" name="smokingrecent_value" value="Last 7 Days">Last 7 Days</input></td>
	<td><input type="radio" name="smokingrecent_value" value="Last 6 Months">Last 6 Months</input></td>
	<td><input type="radio" name="smokingrecent_value" value="None">None</input></td>
	<td><input type="text" name="smokingrecent_comment" /></td>
	<td><%=msmtValueSmkR%></td>
	<td><%=msmtDateSmkR%></td>
	<td><%=msmtCommentSmkR%></td>
</tr>
<tr>
	<td class="rowheader2">Smoking Start:</td>
	<td><input type="text" name="smokingstartage_value" size="5" onchange="calcSmkSYear(); calcPYHX()"/> (age)</td>
	<td colspan="2"><input type="text" name="smokingstartyear_value" size="5" onchange="calcSmkSAge(); calcPYHX()"/> (year)</td>
	<td><input type="text" name="smokingstart_comment" /></td>
	<td><%=msmtValueSmkS%></td>
	<td><%=msmtDateSmkS%></td>
	<td><p><%=msmtCommentSmkS%></p></td>
</tr>
<tr>
	<td class="rowheader2">Smoking Cessation:</td>
	<td><input type="text" name="smokingcessage_value" size="5" onchange="calcSmkCYear(); calcPYHX()"/> (age)</td>
	<td colspan="2"><input type="text" name="smokingcessyear_value" size="5" onchange="calcSmkCAge(); calcPYHX()"/> (year)</td>
	<td><input type="text" name="smokingcess_comment" /></td>
	<td><%=msmtValueSmkC%></td>
	<td><%=msmtDateSmkC%></td>
	<td><p><%=msmtCommentSmkC%></p></td>
</tr>
<tr>
	<td class="rowheader2">Packs Per Day:</td>
	<td colspan="3">
		<input type="radio" name="smokingpks_value" value="1/8" onchange="calcPYHX()">&#8539;</input>
		<input type="radio" name="smokingpks_value" value="1/4" onchange="calcPYHX()">&frac14;</input>
		<input type="radio" name="smokingpks_value" value="1/2" onchange="calcPYHX()">&frac12;</input>
		<input type="radio" name="smokingpks_value" value="3/4" onchange="calcPYHX()">&frac34;</input>
		<input type="radio" name="smokingpks_value" value="1" onchange="calcPYHX()">1</input>
		<input type="radio" name="smokingpks_value" value="2" onchange="calcPYHX()">2</input>
		<input type="radio" name="smokingpks_value" value="3" onchange="calcPYHX()">3</input>
		<input type="radio" name="smokingpks_value" value="4" onchange="calcPYHX()">4</input>
	</td>
	<td><input type="text" name="smokingpks_comment" /></td>
	<td><%=msmtValueSmkD%></td>
	<td><%=msmtDateSmkD%></td>
	<td><p><%=msmtCommentSmkD%></p></td>
</tr>
<tr>
	<td class="rowheader2">Pack Years:</td>
	<td colspan="3"><input type="text" name="smokingpyhx_value" size="5" /> pyhx</td>
	<td><input type="text" name="smokingpyhx_comment" /></td>
	<td><%=msmtValueSmkPY%></td>
	<td><%=msmtDateSmkPY%></td>
	<td><%=msmtCommentSmkPY%></td>
</tr>
<tr>
	<td class="rowheader2">Advised Patient to Quit:</td>
	<td colspan="3"><input type="radio" name="smokingadvised_value" value="Yes">Yes</input><input type="radio" name="smokingadvised_value" value="No">No</input></td>
	<td><input type="text" name="smokingadvised_comment" /></td>
	<td><%=msmtValueSmkA%></td>
	<td><%=msmtDateSmkA%></td>
	<td><%=msmtCommentSmkA%></td>
</tr>
<tr>
	<td class="rowheader2">Patient Ready to Quit:</td>
	<td colspan="3"><input type="radio" name="smokingquit_value" value="Yes">Yes</input><input type="radio" name="smokingquit_value" value="No">No</input></td>
	<td><input type="text" name="smokingquit_comment" /></td>
	<td><%=msmtValueSmkQ%></td>
	<td><%=msmtDateSmkQ%></td>
	<td><p><%=msmtCommentSmkQ%></p></td>
</tr>

<tr>
	<td class="rowheader2">Patient Wants Follow-up:</td>
	<td colspan="3"><input type="radio" name="smokingfollowup_value" value="Yes">Yes</input><input type="radio" name="smokingfollowup_value" value="No">No</input></td>
	<td><input type="text" name="smokingfollowup_comment" /></td>
	<td><%=msmtValueSmkF%></td>
	<td><%=msmtDateSmkF%></td>
	<td><p><%=msmtCommentSmkF%></p></td>
</tr>

<tr>
	<td class="rowheader2">Pregnancy Test:</td>
	<td colspan="3"><input type="radio" name="pregnancy_test_value" value="Yes">Yes</input><input type="radio" name="pregnancy_test_value" value="No">No</input></td>
	<td><input type="text" name="pregnancy_test_comment" /></td>
	<td><%=msmtValuePRGT%></td>
	<td><%=msmtDatePRGT%></td>
	<td><p><%=msmtCommentPRGT%></p></td>
</tr>

<tr>
	<td class="rowheader2">Urine Dip Test:</td>
	<td colspan="3"><input type="text" name="urine_dip_test_value" value=""></td>
	<td><input type="text" name="urine_dip_test_comment" /></td>
	<td><%=msmtValueUDIP%></td>
	<td><%=msmtDateUDIP%></td>
	<td><p><%=msmtCommentUDIP%></p></td>
</tr>

<tr>
	<td class="rowheader2">Glucose Monitor Test:</td>
	<td colspan="3"><input type="text" name="glucose_monitor_test_value" size="5" value=""></td>
	<td><input type="text" name="glucose_monitor_test_comment" /></td>
	<td><%=msmtValueGLMT%></td>
	<td><%=msmtDateGLMT%></td>
	<td><p><%=msmtCommentGLMT%></p></td>
</tr>

<tr>
	<td class="rowheader2">Left Eye Check up Measurement:</td>
	<td colspan="3">L&nbsp;<input type="text" name="eye_check_up_l_value" size="5" value="">&nbsp;20</td>
	<td><input type="text" name="eye_check_up_l_comment" /></td>
	<td><%=msmtValueLECM%></td>
	<td><%=msmtDateLECM%></td>
	<td><p><%=msmtCommentLECM%></p></td>
</tr>

<tr>
	<td class="rowheader2">Right Eye Check up Measurement:</td>
	<td colspan="3">R&nbsp;<input type="text" name="eye_check_up_r_value" size="5" value="">&nbsp;20</td>
	<td><input type="text" name="eye_check_up_r_comment" /></td>
	<td><%=msmtValueRECM%></td>
	<td><%=msmtDateRECM%></td>
	<td><p><%=msmtCommentRECM%></p></td>
</tr>

</table>
<br />
<div class="centerbox">
<input type="submit" value="Submit"/>
</div>

</form>

</body>
</html>

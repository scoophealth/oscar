<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.model.Measurements" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="oscar.oscarEncounter.oscarMeasurements.dao.MeasurementsDao" %>

<%
	String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
	Measurements m = new Measurements();
	m.setComments("");
	m.setDataField("Yes");
	m.setDateEntered(new java.util.Date());
	m.setDateObserved(new java.util.Date());
	m.setDemographicNo(Integer.parseInt(request.getParameter("demographicNo")));
	m.setMeasuringInstruction("");
	m.setProviderNo(providerNo);
	m.setType("medr");
	
	MeasurementsDao dao = (MeasurementsDao)SpringUtils.getBean("measurementsDao");
	dao.addMeasurements(m);
%>
<%--

    Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Computer Science
    LeadLab
    University of Victoria
    Victoria, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="oscar.log.*"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.apache.commons.lang.StringUtils"%>

<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>

<%@ page import="org.oscarehr.integration.cdx.CDXImport" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.receive.IDocument" %>
<%@ page import="ca.uvic.leadlab.obibconnector.facades.receive.ITelco" %>
<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.common.model.CtlDocument" %>
<%@ page import="org.oscarehr.common.dao.CtlDocumentDao" %>
<%@ page import="static org.oscarehr.util.SpringUtils.getBean" %>
<%@ page import="org.oscarehr.common.model.PatientLabRouting" %>
<%@ page import="org.oscarehr.common.dao.PatientLabRoutingDao" %>
<%@ page import="org.oscarehr.PMmodule.web.GenericIntakeEditAction" %>
<%@ page import="org.oscarehr.PMmodule.service.ProgramManager" %>
<%@ page import="org.oscarehr.PMmodule.model.Program" %>
<%@ page import="java.util.Set" %>
<%@ page import="org.oscarehr.PMmodule.service.AdmissionManager" %>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<% 
	java.util.Properties oscarVariables = oscar.OscarProperties.getInstance();

	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");

	CtlDocumentDao ctlDocDao = SpringUtils.getBean(CtlDocumentDao.class);

	PatientLabRoutingDao patientLabRoutingDao = SpringUtils.getBean(PatientLabRoutingDao.class);

	ProgramManager pm = SpringUtils.getBean(ProgramManager.class);

	AdmissionManager am = SpringUtils.getBean(AdmissionManager.class);
		
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" href="../web.css" />
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
      this.resizeTo(1000,700);
    }
    function closeit() {
      //parent.refresh();
      close();
    }
    //-->
</script>
</head>

<body onload="start()" bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

	CDXImport cdxImport = new CDXImport();

	IDocument cdxDoc = cdxImport.retrieveDocument(request.getParameter("msg_id"));

	Demographic demographic = new Demographic();

	demographic.setLastName(cdxDoc.getPatient().getLastName().toUpperCase());
	demographic.setFirstName(cdxDoc.getPatient().getFirstName().toUpperCase());
	demographic.setAddress(cdxDoc.getPatient().getStreetAddress());
	demographic.setCity(cdxDoc.getPatient().getCity());
	demographic.setProvince(cdxDoc.getPatient().getProvince());
	demographic.setPostal(cdxDoc.getPatient().getPostalCode());

	List<ITelco> phones = cdxDoc.getPatient().getPhones();
	if (phones.size() >0)
		demographic.setPhone(phones.get(0).getAddress());
	if (phones.size() >1)
		demographic.setPhone2(phones.get(1).getAddress());

	List<ITelco> emails = cdxDoc.getPatient().getEmails();
	if (emails.size() >0)
		demographic.setEmail(emails.get(0).getAddress());




	demographic.setYearOfBirth(Integer.toString(1900+cdxDoc.getPatient().getBirthdate().getYear()));

	String month = Integer.toString(1+cdxDoc.getPatient().getBirthdate().getMonth());
	if (month.length()==1)
		month = "0"+month;
	demographic.setMonthOfBirth(month);

	String day = Integer.toString(cdxDoc.getPatient().getBirthdate().getDate());
	if (day.length()==1)
		day = "0"+day;
	demographic.setDateOfBirth(day);

	demographic.setHin(cdxDoc.getPatient().getID());
	demographic.setSex(cdxDoc.getPatient().getGender().label);

	demographic.setPatientStatus(Demographic.PatientStatus.AC.toString());

    demographicDao.save(demographic);

    // -------------- now register new patient with program "OSCAR"

	GenericIntakeEditAction gieat = new GenericIntakeEditAction();
	gieat.setProgramManager(pm);
	gieat.setAdmissionManager(am);

	String _pvid = loggedInInfo.getLoggedInProviderNo();
	Set<Program> pset = gieat.getActiveProviderProgramsInFacility(loggedInInfo,_pvid,loggedInInfo.getCurrentFacility().getId());
	List<Program> bedP = gieat.getBedPrograms(pset,_pvid);

	Program prog = null;

	for(Program _p:bedP){
		if ("OSCAR".equals(_p.getName()))
			prog = _p;
	}

	gieat.admitBedCommunityProgram(demographic.getDemographicNo(),loggedInInfo.getLoggedInProviderNo(),prog.getId(),"","",null);


	// ------------- now link the document to the patient

    int docNo = Integer.parseInt(request.getParameter("doc_no"));

	CtlDocument ctlDoc = ctlDocDao.findByDocumentNoAndModule(docNo, "demographic").get(0);

	ctlDoc.getId().setModuleId(demographic.getId());
	ctlDoc.setStatus("A");
	ctlDocDao.persist(ctlDoc);

	PatientLabRouting patientLabRouting = new PatientLabRouting();

	patientLabRouting.setLabNo(docNo);
	patientLabRouting.setLabType("DOC");
	patientLabRouting.setDemographicNo(demographic.getId());
	patientLabRoutingDao.persist(patientLabRouting);
    int dem = demographic.getId();



	 %>



<p>
<h2>Successfully created new Demographic record for patient named in CDX document</h2>

You can open the  <a href="../demographic/demographiccontrol.jsp?demographic_no=<%=dem%>&displaymode=edit&dboperation=search_detail">demographic record</a> or <a href="javascript:window.close();"> close this window</a>.

<script>
	window.onunload = refreshParent;
	function refreshParent() {
		window.opener.location.reload();
	}
</script>
</body>
</html:html>

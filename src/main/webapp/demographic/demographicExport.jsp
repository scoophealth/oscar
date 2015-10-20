<!DOCTYPE html>
<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic,_demographicExport" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic&type=_demographicExport");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.sharingcenter.SharingCenterUtil"%>
<%@page import="org.oscarehr.sharingcenter.dao.AffinityDomainDao"%>
<%@page import="org.oscarehr.sharingcenter.model.AffinityDomainDataObject"%>
<%@page
	import="java.util.*,oscar.oscarDemographic.data.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarPrevention.pageUtil.*,oscar.oscarDemographic.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<%

  oscar.OscarProperties op = oscar.OscarProperties.getInstance();
  String tmp_dir = op.getProperty("TMP_DIR");
  boolean tmp_dir_ready = Util.checkDir(tmp_dir);

  String pgp_ready = (String)session.getAttribute("pgp_ready");
  if (pgp_ready==null || pgp_ready.equals("No")) {
      PGPEncrypt pgp = new PGPEncrypt();
      if (pgp.check(tmp_dir)) pgp_ready = "Yes";
      else pgp_ready = "No";
  }
  session.setAttribute("pgp_ready", pgp_ready);

  String demographicNo = request.getParameter("demographicNo");
  DemographicSets  ds = new DemographicSets();
  List<String> sets = ds.getDemographicSets();

//  oscar.oscarReport.data.RptSearchData searchData  = new oscar.oscarReport.data.RptSearchData();
//  ArrayList queryArray = searchData.getQueryTypes();

  String userRole = (String)session.getAttribute("userrole");

// MARC-HI's Sharing Center
boolean isSharingCenterEnabled = SharingCenterUtil.isEnabled();

//get all installed affinity domains
AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
List<AffinityDomainDataObject> affinityDomains = affDao.getAllAffinityDomains();
%>

<html:html locale="true">
<head>
<title><bean:message key="demographic.demographicexport.title" /></title>
	
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<SCRIPT LANGUAGE="JavaScript">

function showHideItem(id){
    if(document.getElementById(id).style.display == 'none')
        document.getElementById(id).style.display = '';
    else
        document.getElementById(id).style.display = 'none';
}

function showItem(id){
        document.getElementById(id).style.display = '';
}

function hideItem(id){
        document.getElementById(id).style.display = 'none';
}

function showHideNextDate(id,nextDate,nexerWarn){
    if(document.getElementById(id).style.display == 'none'){
        showItem(id);
    }else{
        hideItem(id);
        document.getElementById(nextDate).value = "";
        document.getElementById(nexerWarn).checked = false ;

    }
}

function disableifchecked(ele,nextDate){
    if(ele.checked == true){
       document.getElementById(nextDate).disabled = true;
    }else{
       document.getElementById(nextDate).disabled = false;
    }
}

function checkSelect(slct) {
    if (slct==-1) {
	alert("Please select a Patient Set");
	return false;
    }
    else return true;
}

function checkAll(all) {
    var frm = document.DemographicExportForm;
    if (all) {
   	frm.exPersonalHistory.checked = true;
	frm.exFamilyHistory.checked = true;
	frm.exPastHealth.checked = true;
	frm.exProblemList.checked = true;
	frm.exRiskFactors.checked = true;
	frm.exAllergiesAndAdverseReactions.checked = true;
	frm.exMedicationsAndTreatments.checked = true;
	frm.exImmunizations.checked = true;
	frm.exLaboratoryResults.checked = true;
	frm.exAppointments.checked = true;
	frm.exClinicalNotes.checked = true;
	frm.exReportsReceived.checked = true;
	frm.exAlertsAndSpecialNeeds.checked = true;
	frm.exCareElements.checked = true;
    } else {
   	frm.exPersonalHistory.checked = false;
	frm.exFamilyHistory.checked = false;
	frm.exPastHealth.checked = false;
	frm.exProblemList.checked = false;
	frm.exRiskFactors.checked = false;
	frm.exAllergiesAndAdverseReactions.checked = false;
	frm.exMedicationsAndTreatments.checked = false;
	frm.exImmunizations.checked = false;
	frm.exLaboratoryResults.checked = false;
	frm.exAppointments.checked = false;
	frm.exClinicalNotes.checked = false;
	frm.exReportsReceived.checked = false;
	frm.exAlertsAndSpecialNeeds.checked = false;
	frm.exCareElements.checked = false;
    }
}

function toggle(source) {
	var c = new Array();
	c = document.getElementsByTagName('input');
	for (var i = 0; i < c.length; i++)
	{
	    if (c[i].type == 'checkbox')
	    {
			c[i].checked = source.checked;
	    }
	}
}
</SCRIPT>

<style type="text/css">
	input[type="checkbox"] {
	    line-height: normal;
	    margin: 4px 4px 4px;
	}
</style>

</head>

<body>
<%
if (!userRole.toLowerCase().contains("admin")) { %>
    <div class="alert alert-block alert-error">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
     <bean:message key="demographic.demographicexport.msgsorry" />
    </div>
<%
} else if (!tmp_dir_ready) { %>
    <div class="alert alert-block alert-error">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
     <bean:message key="demographic.demographicexport.msgerror" />
    </div>
<%
} else {
%>

<div class="container-fluid well">
<h3><bean:message key="demographic.demographicexport.title" /> <small><oscar:help keywords="export demographic" key="app.top1"/></small></h3>

<div class="span2">
	<% if (demographicNo== null) { %>
	<a href="diabetesExport.jsp"><bean:message key="demographic.demographicexport.diabetesexport" /></a><br>
	<a href='<c:out value="${ctx}/demographic/cihiExportOMD4.do"></c:out>'><bean:message key="demographic.demographicexport.cihiexport" /></a><br>
	<a href='<c:out value="${ctx}/demographic/eRourkeExport.do"></c:out>'><bean:message key="demographic.demographicexport.rourke2009export" /></a>
	<%} %>
</div><!--span2-->

<div class="span4">

<html:form action="/demographic/DemographicExport" method="get" onsubmit="return checkSelect(patientSet.value);">

	<% if (demographicNo!= null) { %>
	<html:hidden property="demographicNo" value="<%=demographicNo%>" />
	<bean:message key="demographic.demographicexport.exportingdemographicno" /><%=demographicNo%>
	<%} else {%>
	<bean:message key="demographic.demographicexport.patientset" /><br>
	<html:select style="width: 189px" property="patientSet">
	    <html:option value="-1"><bean:message key="demographic.demographicexport.selectset" /></html:option>
	<%
	/*			    for (int i =0 ; i < queryArray.size(); i++){
	RptSearchData.SearchCriteria sc = (RptSearchData.SearchCriteria) queryArray.get(i);
	String qId = sc.id;
	String qName = sc.queryName;
	*/
	for (int i=0; i<sets.size(); i++) {
	String setName = sets.get(i);
	%>
	<html:option value="<%=setName%>"><%=setName%></html:option>
	<%}%>
	</html:select>
	<%}%>

	<br>	   

	<bean:message key="demographic.demographicexport.exporttemplate" /><br>
	<html:select style="width: 189px" property="template">
		<html:option value="<%=(new Integer(DemographicExportAction4.CMS4)).toString() %>">CMS Spec 4.0</html:option>
		<html:option value="<%=(new Integer(DemographicExportAction4.E2E)).toString() %>">E2E</html:option>
	</html:select>
	   
<br>

<bean:message key="demographic.demographicexport.exportcategories" /><br>

<input type="checkbox" onClick="toggle(this)" />Select All<br/>

<html:checkbox property="exPersonalHistory"><bean:message key="demographic.demographicexport.personalhistory" /></html:checkbox><br>
<html:checkbox property="exFamilyHistory"><bean:message key="demographic.demographicexport.familyhistory" /></html:checkbox><br>
<html:checkbox property="exPastHealth"><bean:message key="demographic.demographicexport.pasthealth" /></html:checkbox><br>
<html:checkbox property="exProblemList"><bean:message key="demographic.demographicexport.problemlist" /></html:checkbox><br>
<html:checkbox property="exRiskFactors"><bean:message key="demographic.demographicexport.riskfactors" /></html:checkbox><br>
<html:checkbox property="exAllergiesAndAdverseReactions"><bean:message key="demographic.demographicexport.allergiesadversereaction" /></html:checkbox><br>
<html:checkbox property="exMedicationsAndTreatments"><bean:message key="demographic.demographicexport.medicationstreatments" /></html:checkbox><br>

<html:checkbox property="exImmunizations"><bean:message key="demographic.demographicexport.immunization" /></html:checkbox><br>
<html:checkbox property="exLaboratoryResults"><bean:message key="demographic.demographicexport.laboratoryresults" /></html:checkbox><br>
<html:checkbox property="exAppointments"><bean:message key="demographic.demographicexport.appointments" /></html:checkbox><br>
<html:checkbox property="exClinicalNotes"><bean:message key="demographic.demographicexport.clinicalnotes" /></html:checkbox><br>
<html:checkbox property="exReportsReceived"><bean:message key="demographic.demographicexport.reportsreceived" /></html:checkbox><br>
<html:checkbox property="exCareElements"><bean:message key="demographic.demographicexport.careelements" /></html:checkbox><br>
<html:checkbox property="exAlertsAndSpecialNeeds"><bean:message key="demographic.demographicexport.alertsandspecialneeds" /></html:checkbox>                    

<br>
<html:hidden property="pgpReady" value="<%=pgp_ready%>" />
		
<%  boolean pgpReady = pgp_ready.equals("Yes") ? true : false;
    pgpReady = true; //To be removed after CMS4
    if (!pgpReady) { %>
                   
    <div class="alert alert-block alert-error">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
     <bean:message key="demographic.demographicexport.msgwarning" />
    </div>

<%  } %>

<input class="btn btn-primary" type="submit" value="<bean:message key="export" />"<%=pgpReady?"":"disabled"%> />

<%	if (isSharingCenterEnabled) { %>
	<!-- Sharing Center Submission -->
    <br />
    <br />
    <div class="pull-left">
      <select name="affinityDomain" class="pull-left">

        <% for(AffinityDomainDataObject domain : affinityDomains) { %>
          <option value="<%=domain.getId()%>"><%=domain.getName()%></option>
        <% } %>

      </select>
      <input type="submit" class="btn btn-info" id="SendToAffinityDomain" name="SendToAffinityDomain" value="Share">
    </div>
<% } %>

</html:form>

</div><!--span4-->

</div><!--container-->
<%}%>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>

</body>
</html:html>

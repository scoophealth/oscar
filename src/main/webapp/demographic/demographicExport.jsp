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

<%@page
	import="java.util.*,oscar.oscarDemographic.data.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarPrevention.pageUtil.*,oscar.oscarDemographic.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />

<%
  if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");

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
%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<!--I18n-->
<title><bean:message key="demographic.demographicexport.title" /></title>
<script src="../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css"
	href="../share/css/OscarStandardLayout.css">
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

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
</SCRIPT>




<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="BodyStyle" vlink="#0000FF">
<%
if (!userRole.toLowerCase().contains("admin")) { %>
<p>
<h2><bean:message key="demographic.demographicexport.msgsorry" /></h2>

<%
} else if (!tmp_dir_ready) { %>
<p>
<h2><bean:message key="demographic.demographicexport.msgerror" /></h2>

<%
} else {
%>

<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td style="max-width:200px;" class="MainTableTopRowLeftColumn">
		<bean:message key="demographic.demographicexport.title" /></td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td><bean:message key="demographic.demographicexport.title" /></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="export demographic" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top" nowrap="nowrap">
		    <% if (demographicNo== null) { %>
		    <a href="diabetesExport.jsp"><bean:message key="demographic.demographicexport.diabetesexport" /></a><br>
            <a href='<c:out value="${ctx}/demographic/cihiExportOMD4.do"></c:out>'><bean:message key="demographic.demographicexport.cihiexport" /></a><br>
            <a href='<c:out value="${ctx}/demographic/eRourkeExport.do"></c:out>'><bean:message key="demographic.demographicexport.rourke2009export" /></a>
		    <%} %>
		</td>
		<td valign="top" class="MainTableRightColumn">
		    <html:form action="/demographic/DemographicExport" method="get" onsubmit="return checkSelect(patientSet.value);">
		    <div>
		    <% if (demographicNo!= null) { %>
                            <html:hidden property="demographicNo" value="<%=demographicNo%>" />
                            <bean:message key="demographic.demographicexport.exportingdemographicno" /><%=demographicNo%>
		    <%} else {%>
			    <bean:message key="demographic.demographicexport.patientset" /><html:select property="patientSet">
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
		    	<bean:message key="demographic.demographicexport.exporttemplate" /><html:select property="template">
		    		<html:option value="<%=(new Integer(DemographicExportAction4.CMS4)).toString() %>">CMS Spec 4.0</html:option>
		    		<html:option value="<%=(new Integer(DemographicExportAction4.E2E)).toString() %>">E2E</html:option>
		    	</html:select>
		    </div>
                    <p><table border="1"><tr><td>
		       <bean:message key="demographic.demographicexport.exportcategories" />
		       <table cellpadding="10"><tr><td>
		       <html:checkbox property="exPersonalHistory"><bean:message key="demographic.demographicexport.personalhistory" /></html:checkbox><br>
		       <html:checkbox property="exFamilyHistory"><bean:message key="demographic.demographicexport.familyhistory" /></html:checkbox><br>
		       <html:checkbox property="exPastHealth"><bean:message key="demographic.demographicexport.pasthealth" /></html:checkbox><br>
		       <html:checkbox property="exProblemList"><bean:message key="demographic.demographicexport.problemlist" /></html:checkbox><br>
		       <html:checkbox property="exRiskFactors"><bean:message key="demographic.demographicexport.riskfactors" /></html:checkbox><br>
		       <html:checkbox property="exAllergiesAndAdverseReactions"><bean:message key="demographic.demographicexport.allergiesadversereaction" /></html:checkbox><br>
		       <html:checkbox property="exMedicationsAndTreatments"><bean:message key="demographic.demographicexport.medicationstreatments" /></html:checkbox><br>
		       </td><td>
		       <html:checkbox property="exImmunizations"><bean:message key="demographic.demographicexport.immunization" /></html:checkbox><br>
		       <html:checkbox property="exLaboratoryResults"><bean:message key="demographic.demographicexport.laboratoryresults" /></html:checkbox><br>
		       <html:checkbox property="exAppointments"><bean:message key="demographic.demographicexport.appointments" /></html:checkbox><br>
		       <html:checkbox property="exClinicalNotes"><bean:message key="demographic.demographicexport.clinicalnotes" /></html:checkbox><br>
		       <html:checkbox property="exReportsReceived"><bean:message key="demographic.demographicexport.reportsreceived" /></html:checkbox><br>
		       <html:checkbox property="exCareElements"><bean:message key="demographic.demographicexport.careelements" /></html:checkbox><br>
		       <html:checkbox property="exAlertsAndSpecialNeeds"><bean:message key="demographic.demographicexport.alertsandspecialneeds" /></html:checkbox>
		       </td><td>
			   <input type="button" value="<bean:message key="checkall" />" onclick="checkAll(true);"/><p>
			   <input type="button" value="<bean:message key="checknone" />" onclick="checkAll(false);"/>
		       </td></tr></table>
		   </td></tr></table>
                       <html:hidden property="pgpReady" value="<%=pgp_ready%>" />

		    <p>&nbsp;</p>
<%  boolean pgpReady = pgp_ready.equals("Yes") ? true : false;
    pgpReady = true; //To be removed after CMS4
    if (!pgpReady) { %>
                    <bean:message key="demographic.demographicexport.msgwarning" /><br>
<%  } %>
                    <input type="submit" value="<bean:message key="export" />"<%=pgpReady?"":"disabled"%> />
		</html:form></td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
<script type="text/javascript">
    //Calendar.setup( { inputField : "asofDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
</script>

<%}%>
</body>
</html:html>
<%!

%>

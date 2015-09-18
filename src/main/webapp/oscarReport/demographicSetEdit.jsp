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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_report&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!-- page updated to support better use of CRUD operations -->

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page
	import="oscar.oscarDemographic.data.*,java.util.*,oscar.oscarPrevention.*,oscar.oscarProvider.data.*,oscar.util.*,oscar.oscarReport.data.*,oscar.oscarPrevention.pageUtil.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />

<%

  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no"));
  String demographic_no = request.getParameter("demographic_no");

  DemographicSets  ds = new DemographicSets();
  List<String> sets = ds.getDemographicSets();

  DemographicData dd = new DemographicData();

%>

<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Demographic Set Edit I18n</title>
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

</SCRIPT>




<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>

<body class="preview" id="top" data-spy="scroll" data-target=".subnav" data-offset="180">

  <div class="container">
  
  <div class="page-header">
    <h1><bean:message key="oscarReport.oscarReportDemoSetEdit.msgDemographic"/> - <bean:message key="oscarReport.oscarReportDemoSetEdit.msgSetEdit"/></h1>
  </div>

  	<section id="mainContent">
		<% if(request.getAttribute("deleteSetSuccess")!=null && (Boolean)request.getAttribute("deleteSetSuccess")){ %>
			<div class="alert alert-block alert-success fade in">
				<button type="button" class="close" data-dismiss="alert">×</button>
				<h4 class="alert-heading">Success!</h4>
				<p>Patient set "${requestScope.setname}" has been successfully deleted.</p>
			</div>
		<% } %>
		<div class="row">
		<div class="span12">
		<html:form styleClass="form-horizontal well form-search" 
			action="/report/DemographicSetEdit">
			<div><bean:message key="oscarReport.oscarReportDemoSetEdit.msgPatientSet"/>: <html:select property="patientSet">
				<html:option value="-1"><bean:message key="oscarReport.oscarReportDemoSetEdit.msgOptionSet"/></html:option>
				<% for ( int i = 0 ; i < sets.size(); i++ ){
                            String s = sets.get(i);%>
				<html:option value="<%=s%>"><%=s%></html:option>
				<%}%>
			</html:select> <input type="submit" value="<bean:message key="oscarReport.oscarReportDemoSetEdit.btnDisplaySet"/>" /></div>

		</html:form> <%if( request.getAttribute("SET") != null ) {
                   List<Map<String,String>> list = (List<Map<String,String>>) request.getAttribute("SET");
                   String setName = (String) request.getAttribute("setname");%>
		<div><html:form action="/report/SetEligibility">
			<input type="submit" value="<bean:message key="oscarReport.oscarReportDemoSetEdit.btnSetIneligible"/>" /> <bean:message key="oscarReport.oscarReportDemoSetEdit.msgIneligible"/><br>
                        <input type="submit" name="delete" value="<bean:message key="oscarReport.oscarReportDemoSetEdit.btnDelete"/>"/><bean:message key="oscarReport.oscarReportDemoSetEdit.msgDelete"/>
                   <input type="hidden" name="setName"
				value="<%=setName%>" />
			<table class="ele">
				<tr>
					<th>&nbsp;</th>
					<th><bean:message key="oscarReport.oscarReportDemoSetEdit.msgDemo"/></th>
					<th><bean:message key="oscarReport.oscarReportDemoSetEdit.msgName"/></th>
					<th><bean:message key="oscarReport.oscarReportDemoSetEdit.msgDOB"/></th>
					<th><bean:message key="oscarReport.oscarReportDemoSetEdit.msgAge"/></th>
					<th><bean:message key="oscarReport.oscarReportDemoSetEdit.msgRoster"/></th>
					<th><bean:message key="oscarReport.oscarReportDemoSetEdit.msgDoctor"/></th>
					<th><bean:message key="oscarReport.oscarReportDemoSetEdit.msgEligibility" /></th>
				</tr>
				<%for (int i=0; i < list.size(); i++){
                     Map<String,String> h = list.get(i);
                     org.oscarehr.common.model.Demographic demo = dd.getDemographic(LoggedInInfo.getLoggedInInfoFromSession(request), h.get("demographic_no"));  %>
				<tr>
					<td><input type="checkbox" name="demoNo"
						value="<%=h.get("demographic_no")%>" />
					<td><%=h.get("demographic_no")%></td>
					<td><%=demo.getLastName()%>, <%=demo.getFirstName()%></td>
					<td><%=oscar.oscarDemographic.data.DemographicData.getDob(demo,"-")%></td>
					<td><%=demo.getAge()%></td>
					<td><%=demo.getRosterStatus()%></td>
					<td><%=providerBean.getProperty(demo.getProviderNo(),"")%></td>
					<td><%=elle(h.get("eligibility"))%></td>
				</tr>
				<%}%>
			</table>
		</html:form></div>
		<%}%>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
<script type="text/javascript">
    //Calendar.setup( { inputField : "asofDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date", singleClick : true, step : 1 } );
</script>

	</section>
</div>

	<div id="delete-set-confirm" class="modal hide fade" tabindex="-1" role="dialog">
		<div class="modal-header">
			<button type="button" class="close" data-dismiss="modal">×</button>
			<h3>Delete Set</h3>
		</div>
		<div class="modal-body">
			<p>This will permanently delete the set, this procedure is
				irreversible.</p>
			<p>Are you sure you want to proceed?</p>
		</div>
		<div class="modal-footer">
			<a href="javascript:onDeleteConfirm()" class="btn btn-danger">Yes</a> 
			<a href="javascript:$('#delete-set-confirm').modal('hide')" class="btn secondary">No</a>
		</div>
	</div>	

    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-1.7.1.min.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
	
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/global.js"></script>

	<script type="text/javascript">
	
	function onDeleteConfirm(){
		$('#delete-set-confirm').modal('hide');
    	$('#deleteSet').val('deleteSet');
    	$('form[name="DemographicSetEditForm"]').submit();
	}
	

	function onDeleteSetClick() {
	    //e.preventDefault();
	    
	    var id = $(this).data('id');
		$('#delete-set-confirm').modal({ backdrop: true });
	    $('#delete-set-confirm').data('id', id).modal('show');
	};
	
	</script>
</body>
</html:html>
<%!
String elle(Object s){
    ResourceBundle prop = ResourceBundle.getBundle("oscarResources");
    String ret = prop.getString("oscarReport.oscarReportDemoSetEdit.msgStatusEligibile");
    if (s != null && s instanceof String && ((String) s).equals("1")){
        ret = prop.getString("oscarReport.oscarReportDemoSetEdit.msgStatusIneligibile");
    }
    return ret;
}
%>

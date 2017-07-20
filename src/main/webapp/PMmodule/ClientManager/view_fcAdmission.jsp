<%@ page import="org.apache.struts.validator.DynaValidatorForm"%>
<%@ page import="org.oscarehr.common.model.Admission"%>
<%@ page import="org.oscarehr.PMmodule.model.DischargeReason"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>

<%@ include file="/taglibs.jsp"%>
<%@page import="org.oscarehr.common.model.FunctionalCentreAdmission"%>

		<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />

<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.js"></script>
		<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.form.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/jquery.metadata.js"></script>
		

<script>
	
function validateAndClose() {
	if(checkDates()) {
		RefreshParent(); 
		window.close();
		
	} else {
		return false;
	}
}


function RefreshParent() {	
    if (window.opener != null && !window.opener.closed) {    
       window.opener.location.reload(); 
       //window.opener.location.href = window.opener.location.href;
    }
}

function checkDates() {
	var referralDate = document.fcAdmissionForm.referralDate.value;
	var serviceInitDate = document.fcAdmissionForm.serviceInitiationDate.value;
	var admissionDate = document.fcAdmissionForm.admissionDate.value;	
	var dischargeDate = document.fcAdmissionForm.dischargeDate.value;	
		
	if(!serviceInitDate || typeof serviceInitDate == 'undefined') {
		serviceInitDate = "";
	}
	if(!dischargeDate || typeof dischargeDate == 'undefined') {
		dischargeDate = "";
	}
	
	if(!compareDates(admissionDate, referralDate)) {
		alert("The referral date should be later or equal to the admission date.");
		return false;
	}
	if(!compareDates(serviceInitDate, admissionDate)) {
		alert("The service initiation date should be later or equal to the admission date.");
		return false;
	} 
	if(dischargeDate != "") {
		if(!compareDates(dischargeDate, admissionDate)) {
				alert("The admission date should be earlier or equal to the discharge date.");
				return false;
		} 
	}
	
	return true;	
	
}

function compareDates(date1, date2) {	
	
	var aDateString = date1.split('-') ;	
	
	if(aDateString[1]=='01') aDateString[1]=1;
	if(aDateString[1]=='02') aDateString[1]=2;
	if(aDateString[1]=='03') aDateString[1]=3;
	if(aDateString[1]=='04') aDateString[1]=4;
	if(aDateString[1]=='05') aDateString[1]=5;
	if(aDateString[1]=='06') aDateString[1]=6;
	if(aDateString[1]=='07') aDateString[1]=7;
	if(aDateString[1]=='08') aDateString[1]=8;
	if(aDateString[1]=='09') aDateString[1]=9;
	
	if(aDateString[2]=='01') aDateString[2]=1;
	if(aDateString[2]=='02') aDateString[2]=2;
	if(aDateString[2]=='03') aDateString[2]=3;
	if(aDateString[2]=='04') aDateString[2]=4;
	if(aDateString[2]=='05') aDateString[2]=5;
	if(aDateString[2]=='06') aDateString[2]=6;
	if(aDateString[2]=='07') aDateString[2]=7;
	if(aDateString[2]=='08') aDateString[2]=8;
	if(aDateString[2]=='09') aDateString[2]=9;	
	
	var sDateString ;
	if(date2 && typeof date2 != 'undefined') {
		sDateString = date2.split('-') ; 
		if(sDateString[1]=='01') sDateString[1]=1;
		if(sDateString[1]=='02') sDateString[1]=2;
		if(sDateString[1]=='03') sDateString[1]=3;
		if(sDateString[1]=='04') sDateString[1]=4;
		if(sDateString[1]=='05') sDateString[1]=5;
		if(sDateString[1]=='06') sDateString[1]=6;
		if(sDateString[1]=='07') sDateString[1]=7;
		if(sDateString[1]=='08') sDateString[1]=8;
		if(sDateString[1]=='09') sDateString[1]=9;
		
		if(sDateString[2]=='01') sDateString[2]=1;
		if(sDateString[2]=='02') sDateString[2]=2;
		if(sDateString[2]=='03') sDateString[2]=3;
		if(sDateString[2]=='04') sDateString[2]=4;
		if(sDateString[2]=='05') sDateString[2]=5;
		if(sDateString[2]=='06') sDateString[2]=6;
		if(sDateString[2]=='07') sDateString[2]=7;
		if(sDateString[2]=='08') sDateString[2]=8;
		if(sDateString[2]=='09') sDateString[2]=9;		
		
		if (sDateString[0]>aDateString[0]) {		  
		  	return false;
		} else if(sDateString[0]==aDateString[0] && sDateString[1] > aDateString[1]) {
			return false;
		} else if(sDateString[0]==aDateString[0] && sDateString[1] ==aDateString[1] && sDateString[2] > aDateString[2]) {
			return false;
		} else {
			return true;
		}
	}
	return true;
}

</script>
<script type="text/javascript">
function clearDate(el)
{
	$("#"+el).val("");
	
}
</script>	

<%
FunctionalCentreAdmission fcAdmission = (FunctionalCentreAdmission)request.getAttribute("fcAdmission");
SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

Date dischargeDate = fcAdmission.getDischargeDate();
String str_dischargeDate = dischargeDate==null?"":formatter.format(dischargeDate);

Date serviceInitiationDate = fcAdmission.getServiceInitiationDate();
String str_serviceInitiationDate = serviceInitiationDate==null?"":formatter.format(serviceInitiationDate);

Date admissionDate = fcAdmission.getAdmissionDate();
String str_admissionDate = admissionDate==null?"":formatter.format(admissionDate);

Date referralDate = fcAdmission.getReferralDate();
String str_referralDate = referralDate==null?"":formatter.format(referralDate);

%>
<html>
<head></head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Functional Centre Admission Details</title>

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<body>
<form id="fcAdmissionForm" name="fcAdmissionForm" action="ClientManager/functionalCentreAdmissionAction.jsp" method="GET" >	

<table width="100%" border="0" cellspacing="1" cellpadding="1">

<input type="hidden" id="fcAdmissionId" name="fcAdmissionId" value=<%=fcAdmission.getId() %> >

<input type="hidden" id="clientId" name="clientId" value=<%=fcAdmission.getDemographicNo() %> >

<tr class="b">
	<td width="60%">Referral Date (YYYY-MM-DD):</td>
	<td><input type="text" name="referralDate" id="referralDate" value="<%=str_referralDate %>" size="10" maxlength="10" onfocus="this.blur()" readonly="readonly" class="{validate: {required:true}}" >
	<img title="Calendar" id="cal_referralDate" src="../images/cal.gif" alt="Calendar" border="0">
	<script type="text/javascript">
	Calendar.setup({inputField:'referralDate',ifFormat :'%Y-%m-%d',button :'cal_referralDate',align :'cr',singleClick :true,firstDay :1});
	</script>	
		<img src="../images/icon_clear.gif" border="0" onclick="clearDate('referralDate');">
	</td>
</tr>

<tr class="b">
	<td width="60%">Admission Date (YYYY-MM-DD):</td>
	<td><input type="text" name="admissionDate" id="admissionDate" value="<%=str_admissionDate %>" size="10" maxlength="10" onfocus="this.blur()" readonly="readonly" class="{validate: {required:true}}" >
	<img title="Calendar" id="cal_admissionDate" src="../images/cal.gif" alt="Calendar" border="0">
	<script type="text/javascript">
	Calendar.setup({inputField:'admissionDate',ifFormat :'%Y-%m-%d',button :'cal_admissionDate',align :'cr',singleClick :true,firstDay :1});
	</script>		
	<img src="../images/icon_clear.gif" border="0" onclick="clearDate('admissionDate');">
	</td>
</tr>

<tr class="b">
	<td width="60%">Service Initiation Date (YYYY-MM-DD):</td>
	<td><input type="text" name="serviceInitiationDate" id="serviceInitiationDate" value="<%=str_serviceInitiationDate %>" size="10" maxlength="10" onfocus="this.blur()" readonly="readonly" class="{validate: {required:true}}" >
	<img title="Calendar" id="cal_serviceInitiationDate" src="../images/cal.gif" alt="Calendar" border="0">
	<script type="text/javascript">
	Calendar.setup({inputField:'serviceInitiationDate',ifFormat :'%Y-%m-%d',button :'cal_serviceInitiationDate',align :'cr',singleClick :true,firstDay :1});
	</script>		
		<img src="../images/icon_clear.gif" border="0" onclick="clearDate('serviceInitiationDate');">
	
	</td>
</tr>

<tr class="b">
	<td width="60%">Discharge Date (YYYY-MM-DD):</td>
	<td><input type="text" name="dischargeDate" id="dischargeDate" value="<%=str_dischargeDate %>" size="10" maxlength="10" onfocus="this.blur()" readonly="readonly" class="{validate: {required:false}}" >
	<img title="Calendar" id="cal_dischargeDate" src="../images/cal.gif" alt="Calendar" border="0">
	<script type="text/javascript">
	Calendar.setup({inputField:'dischargeDate',ifFormat :'%Y-%m-%d',button :'cal_dischargeDate',align :'cr',singleClick :true,firstDay :1});
	</script>		
			<img src="../images/icon_clear.gif" border="0" onclick="clearDate('dischargeDate');">
	
	</td>
</tr>

<tr>
	<td colspan="2">
		<input type="submit" value="Save" onclick="return checkDates(); "/>  		
		<input type="button" name="cancel" value="Cancel" onclick="window.close()" />
	</td>
</tr>

</table>
</form>

</body>
</html>

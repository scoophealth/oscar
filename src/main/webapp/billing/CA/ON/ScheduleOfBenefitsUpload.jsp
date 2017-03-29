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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
  
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
  
  List warnings = (List) request.getAttribute("warnings"); 
%>

<%@ page import="oscar.oscarDemographic.data.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>


<html:html locale="true">

<head>
<title><bean:message key="admin.admin.scheduleOfBenefits"/></title>
<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">

<script type="text/javascript" LANGUAGE="JavaScript">

function checkForm() {
	var result = true;
	if (document.getElementById("updateAssistantInput").style.display == "inline") {
		if (!checkFee(document.getElementById("updateAssistantFeesValue").value)) {
			alert("An invalid assistant fee was provided. Please correct it or disable update of the assistant fees.");
			result = false;
		}
	}
	if (document.getElementById("updateAnaesthetistInput").style.display == "inline") {
		if (!checkFee(document.getElementById("updateAnaesthetistFeesValue").value)) {
			alert("An invalid anaesthetist fee was provided. Please correct it or disable update of the anaesthetist fees.");
			result = false;
		}
	}	
	return result && displayAndDisable();	
}

function checkFee(fee) {
	if (fee == null || fee.trim() == "") { return false; }
	var feeFormat = /^\d+?(\.\d{0,2})$/;
	if (fee.match(feeFormat)) {
		return true;
	} else {
		return false;
	} 
}

function toggleAnaesthetistInput(el) {
	document.getElementById("updateAnaesthetistInput").style.display = el.checked ? "inline" : "none";
}

function toggleAssistantInput(el) {
	document.getElementById("updateAssistantInput").style.display = el.checked ? "inline" : "none";
}

function displayAndDisable(){
   document.forms[0].Submit.disabled = true;
   //showHideItem('waitingMessage');
   return true;
}

function checkAll(formId){
   var f = document.getElementById(formId);
   var val = f.checkA.checked;
   for (i =0; i < f.change.length; i++){
      f.change[i].checked = val;
   }
}
</script>
</head>

<body>
<h3><bean:message key="admin.admin.scheduleOfBenefits"/> <small><oscar:help keywords="1.6.4" key="app.top1"/></small></h3>
<div class="container-fluid form-inline">


<div class="well">

<div>
1. Download the text file from <a href="http://www.health.gov.on.ca/english/providers/program/ohip/sob/schedule_master.html" target="_blank">OHIP Fee Schedule</a> and save it to your computer.
</div><!--#1-->

<div>
2. Browse & find file:
<% if ( warnings == null ){ %> 
<html:form
action="/billing/CA/ON/benefitScheduleUpload" method="POST"
enctype="multipart/form-data" onsubmit="return checkForm();">
<input type="file" name="importFile" value="/root/apr05sob.001">
<input class="btn btn-primary" type="submit" name="Submit" value="Import">
<div>			
<input type="checkbox" name="showChangedCodes" value="on" checked tabindex="1" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.showCodesChangedPrices" /><br>			
<input type="checkbox" name="showNewCodes" value="on" tabindex="2" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.showNewCodes" /><br>
<input type="checkbox" name="forceUpdate" value="on" tabindex="3" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.forceUpdate" /><br>				
<input type="checkbox" name="updateAssistantFees" onclick="toggleAssistantInput(this);" value="on" tabindex="5" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.updateAssistantFees" /><span id="updateAssistantInput" style="display:none;"><input type="text" name="updateAssistantFeesValue" id="updateAssistantFeesValue" size="7" maxlength="8" style="margin-left:30px;" tabindex="7" /></span><br/>
<input type="checkbox" name="updateAnaesthetistFees" onclick="toggleAnaesthetistInput(this);" value="on" tabindex="6" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.updateAnaesthetistFees" /><span id="updateAnaesthetistInput" style="display:none;"><input type="text" name="updateAnaesthetistFeesValue" id="updateAnaesthetistFeesValue" size="7" maxlength="8" style="margin-left:8px;" tabindex="8"/></span>
</div>
</html:form> 
<% } else{ %> 
<a href="ScheduleOfBenefitsUpload.jsp">Try again</a>
<%}%>
</div><!--#2-->

<div>
3. Click "Import" when file found 
</div><!--#3-->

<br>
<%
String outcome = (String) request.getAttribute("outcome");
if(outcome != null && outcome.equals("success")){ %>
<div class="alert alert-success">SOB File Successfully Uploaded</div>
<%}else if(outcome != null && outcome.equals("exception")){ %>
<div class="alert alert-error">There was a problem uploading this SOB file</div>
<%}else if(outcome != null && outcome.equals("uploadedPreviously")){ %>
<div class="alert ">This file has already been processed</div>
<%}%> 


<% if ( warnings != null && outcome.equals("success")) { %> 
<div>
4. Click "Update" checkbox to select All<br>

	<html:form
	action="/billing/CA/ON/benefitScheduleChange" method="POST"
	styleId="sbForm">
	<table class="table table-striped  table-condensed">
		<tr>
			<th nowrap><oscar:oscarPropertiesCheck property="SOB_CHECKALL"
				value="yes">
				<input type="checkbox" name="checkAll2"
					onclick="checkAll('sbForm')" id="checkA" />
			</oscar:oscarPropertiesCheck> Update</th>
			<th>Fee Code</th>
			<th>Current Price</th>
			<th>New Price</th>
			<th>Diff</th>
			<th>Description</th>
	<th>Effective Date</th>
	<th>Termination Date</th>
		</tr>
		<% for (int i = 0; i < warnings.size(); i++){ 
	Hashtable h = (Hashtable) warnings.get(i);


	%>
		<tr>
			<td><input type="checkbox" name="change"
				value="<%=h.get("feeCode")%>|<%=h.get("newprice")%>|<%=h.get("effectiveDate")%>|<%=h.get("terminactionDate")%>|<%=h.get("description")%>" /></td>
			<td><%=h.get("feeCode")%></td>
			<td><%=h.get("oldprice")%></td>
			<td><%=h.get("newprice")%></td>
			<td><%=h.get("diff")%></td>
			<td title="<%=h.get("prices")%>"><%=h.get("description")%></td>
	<td><%=h.get("effectiveDate")%></td>
	<td><%=h.get("terminactionDate")%></td>
		</tr>
		<%}%>
	</table>
	<input class="btn btn-primary" type="submit" value="Update Billing Code Prices">
	</html:form> 

5. Click "Update Billing Code Prices"
</div><!--#4-->
<% } %> 



<% List l = (List) request.getAttribute("changes");
if ( l != null) {          %>
<ul>
	<% for (int i = 0; i < l.size(); i++){ 
      Hashtable h = (Hashtable) l.get(i); %>
	<li><%=h.get("code")%> value updated to : <%=h.get("value")%></li>
	<%}%>
</ul>
<% }%>


</div><!--main well-->





</div><!--container-->


</body>
</html:html>

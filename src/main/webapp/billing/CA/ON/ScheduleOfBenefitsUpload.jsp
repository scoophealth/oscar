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

<%
  
  //int demographic_no = Integer.parseInt(request.getParameter("demographic_no")); 
  String demographic_no = request.getParameter("demographic_no"); 
  
  List warnings = (List) request.getAttribute("warnings"); 
%>

<%@page import="oscar.oscarDemographic.data.*,java.util.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>


<html:html locale="true">

<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<!--I18n-->
<title>Schedule Of Benefits Upload Utility</title>
<html:base />
<link rel="stylesheet" type="text/css"
	href="../../../share/css/OscarStandardLayout.css">
<script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>

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


<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />


</head>

<body class="BodyStyle" vlink="#0000FF">
<!--  -->
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn" width="175"><bean:message
			key="demographic.demographiceditdemographic.msgPatientDetailRecord" />
		</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>Upload <!--i18n--></td>
				<td>&nbsp;</td>
				<td style="text-align: right"><oscar:help keywords="1.6.4" key="app.top1"/> | <a
					href="javascript:popupStart(300,400,'About.jsp')"><bean:message
					key="global.about" /></a> | <a
					href="javascript:popupStart(300,400,'License.jsp')"><bean:message
					key="global.license" /></a></td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">&nbsp; <a
			href="http://www.health.gov.on.ca/english/providers/program/ohip/sob/schedule_master.html">OHIP
		Fee Schedule</a><br />
		<br />
		<li>Download the text file.</li>
		<li>Brows&find file on HD.</li>
		<li>Click "Import".</li>
		<li>Click "Update" checkbox to select All.</li>
		<li>Click "Update Billing Code Prices" at bottom.</li>
		<li>Click "Import".</li>
		</td>
		<td valign="top" class="MainTableRightColumn">
		<% if ( warnings == null ){ %> <html:form
			action="/billing/CA/ON/benefitScheduleUpload" method="POST"
			enctype="multipart/form-data" onsubmit="return checkForm();">
			<input type="file" name="importFile" value="/root/apr05sob.001">
			<input type="submit" name="Submit" value="Import">
			<div>			
				<input type="checkbox" name="showChangedCodes" value="on" checked tabindex="1" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.showCodesChangedPrices" /><br>			
				<input type="checkbox" name="showNewCodes" value="on" tabindex="2" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.showNewCodes" /><br>
				<input type="checkbox" name="forceUpdate" value="on" tabindex="3" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.forceUpdate" /><br>				
				<input type="checkbox" name="updateAssistantFees" onclick="toggleAssistantInput(this);" value="on" tabindex="5" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.updateAssistantFees" /><span id="updateAssistantInput" style="display:none;"><input type="text" name="updateAssistantFeesValue" id="updateAssistantFeesValue" size="7" maxlength="8" style="margin-left:30px;" tabindex="7" /></span><br/>
				<input type="checkbox" name="updateAnaesthetistFees" onclick="toggleAnaesthetistInput(this);" value="on" tabindex="6" /><bean:message key="oscar.billing.CA.ON.billingON.sobUpload.updateAnaesthetistFees" /><span id="updateAnaesthetistInput" style="display:none;"><input type="text" name="updateAnaesthetistFeesValue" id="updateAnaesthetistFeesValue" size="7" maxlength="8" style="margin-left:8px;" tabindex="8"/></span>
			</div>
		</html:form> <% } %> <%
            String outcome = (String) request.getAttribute("outcome");
            if(outcome != null && outcome.equals("success")){ %>
		<div>SOB File Successfully Uploaded</div>
		<%}else if(outcome != null && outcome.equals("exception")){ %>
		<div>There has been a problem Uploading this SOB file</div>
		<%}else if(outcome != null && outcome.equals("uploadedPreviously")){ %>
		<div>This file has already been processed</div>
		<%}%> <%
               if ( warnings != null) {          %> <html:form
			action="/billing/CA/ON/benefitScheduleChange" method="POST"
			styleId="sbForm">
			<table class="ele">
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
			<input type="submit" value="Update Billing Code Prices">
		</html:form> <% } %> <% List l = (List) request.getAttribute("changes");
               if ( l != null) {          %>
		<ul>
			<% for (int i = 0; i < l.size(); i++){ 
                      Hashtable h = (Hashtable) l.get(i); %>
			<li><%=h.get("code")%> value updated to : <%=h.get("value")%></li>
			<%}%>
		</ul>
		<% }%>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn">&nbsp;</td>
		<td class="MainTableBottomRowRightColumn" valign="top">&nbsp;</td>
	</tr>
</table>
</body>
</html:html>

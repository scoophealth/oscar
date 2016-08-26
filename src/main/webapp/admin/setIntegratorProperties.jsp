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

<%@page contentType="text/html"%>
<%@ include file="/casemgmt/taglibs.jsp"%>
<%@page import="org.oscarehr.common.model.UserProperty" %>

<%
  String curUser_no;
  curUser_no = (String) session.getAttribute("user");
   String tite = (String) request.getAttribute("provider.title");

%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<html:html>
<head>
<html:base />
<title><bean:message key="provider.btnSetIntegratorPreferences" /></title>

<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>
<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/DT_bootstrap.css" rel="stylesheet" type="text/css">
<link href="<%=request.getContextPath() %>/css/bootstrap-responsive.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
<%--
<script type="text/javascript">

$(document).ready(function() {
	$("input[type='radio']").change(function() {
		switch ($(this).attr("name")) {
		case "integrator_demographic_drugs":
			if ($(this).val() == "0") {
				$("input[name='integrator_demographic_dxresearch'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_dxresearch']").attr("disabled", true);
			} else {
				$("input[name='integrator_demographic_dxresearch']").removeAttr("disabled");
			}
			break;
		case "integrator_demographic_issues":
			if ($(this).val() == "0") {
				$("input[name='integrator_demographic_notes'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_notes']").attr("disabled", true);
			} else {
				$("input[name='integrator_demographic_notes']").removeAttr("disabled");
			}
			break;
		case "integrator_demographic_sync":
			if ($(this).val() == "0") {
				$("input[name='integrator_demographic_allergies'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_allergies']").attr("disabled", true);
				$("input[name='integrator_demographic_appointments'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_appointments']").attr("disabled", true);
				$("input[name='integrator_demographic_billing'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_billing']").attr("disabled", true);
				$("input[name='integrator_demographic_consent'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_consent']").attr("disabled", true);
				$("input[name='integrator_demographic_documents'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_documents']").attr("disabled", true);
				$("input[name='integrator_demographic_eforms'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_eforms']").attr("disabled", true);
				$("input[name='integrator_demographic_labreq'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_labreq']").attr("disabled", true);
				$("input[name='integrator_demographic_measurements'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_measurements']").attr("disabled", true);
				$("input[name='integrator_demographic_issues'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_issues']").attr("disabled", true);
				$("input[name='integrator_demographic_notes'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_notes']").attr("disabled", true);
				$("input[name='integrator_demographic_drugs'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_drugs']").attr("disabled", true);
				$("input[name='integrator_demographic_dxresearch'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_dxresearch']").attr("disabled", true);
				$("input[name='integrator_demographic_preventions'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_preventions']").attr("disabled", true);
				$("input[name='integrator_demographic_providers'][value='0']").attr("checked", "checked");
				$("input[name='integrator_demographic_providers']").attr("disabled", true);
			} else {
				$("input[name='integrator_demographic_allergies'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_allergies']").removeAttr("disabled");
				$("input[name='integrator_demographic_appointments']").removeAttr("disabled");
				$("input[name='integrator_demographic_billing']").removeAttr("disabled");
				$("input[name='integrator_demographic_consent'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_consent']").removeAttr("disabled");
				$("input[name='integrator_demographic_documents']").removeAttr("disabled");
				$("input[name='integrator_demographic_eforms']").removeAttr("disabled");
				$("input[name='integrator_demographic_labreq'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_labreq']").removeAttr("disabled");
				$("input[name='integrator_demographic_measurements'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_measurements']").removeAttr("disabled");
				$("input[name='integrator_demographic_issues'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_issues']").removeAttr("disabled");
				$("input[name='integrator_demographic_notes'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_notes']").removeAttr("disabled");
				$("input[name='integrator_demographic_drugs'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_drugs']").removeAttr("disabled");
				$("input[name='integrator_demographic_dxresearch'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_dxresearch']").removeAttr("disabled");
				$("input[name='integrator_demographic_preventions'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_preventions']").removeAttr("disabled");
				$("input[name='integrator_demographic_providers'][value='1']").attr("checked", "checked");
				$("input[name='integrator_demographic_providers']").removeAttr("disabled");
			}
			break;
		}
	});
	
	if ($("input[name='integrator_demographic_issues']:checked").val() == "0") {
		$("input[name='integrator_demographic_notes']").attr("disabled", true);
	}
	
	if ($("input[name='integrator_demographic_drugs']:checked").val() == "0") {
		$("input[name='integrator_demographic_dxresearch']").attr("disabled", true);
	}
	
	if ($("input[name='integrator_demographic_sync']:checked").val() == "0") {
		$("input[name='integrator_demographic_allergies']").attr("disabled", true);
		$("input[name='integrator_demographic_appointments']").attr("disabled", true);
		$("input[name='integrator_demographic_billing']").attr("disabled", true);
		$("input[name='integrator_demographic_consent']").attr("disabled", true);
		$("input[name='integrator_demographic_documents']").attr("disabled", true);
		$("input[name='integrator_demographic_eforms']").attr("disabled", true);
		$("input[name='integrator_demographic_labreq']").attr("disabled", true);
		$("input[name='integrator_demographic_measurements']").attr("disabled", true);
		$("input[name='integrator_demographic_issues']").attr("disabled", true);
		$("input[name='integrator_demographic_notes']").attr("disabled", true);
		$("input[name='integrator_demographic_drugs']").attr("disabled", true);
		$("input[name='integrator_demographic_dxresearch']").attr("disabled", true);
		$("input[name='integrator_demographic_preventions']").attr("disabled", true);
		$("input[name='integrator_demographic_providers']").attr("disabled", true);
	}
	
	if ($("input[name='integrator_facility']:checked").val() == "0") {
		$("input[type='radio']").attr("disabled", true);
		$("input[name='integrator_facility']").removeAttr("disabled");
	}
	
	
});
</script>
--%>
</head>

<body class="BodyStyle" vlink="#0000FF">
<h4><bean:message key="provider.integratorPreferences.preferences" /></h4>
<p><bean:message key="provider.integratorPreferences.chooseDataSets" /></p>

<% if (request.getAttribute("saved") != null) { %>
<div style="colour: red; border: 1px solid red; padding: 5px; margin: 10px;">Preferences Saved</div>
<% } %>
<%-- Not enabled yet
<div style="margin: 5px;">
	<strong>Warning!</strong> Changing these settings will affect how OSCAR sends information to the Integrated Community.
	<ol>
		<li>By disabling <em>Demographic Records</em>, no patient data will be sent to the Integrated Community.
		<li>By disabling <em>Issues</em> or <em>Prescriptions</em>, no Notes or Dx Research will be sent to the Integrator (respectively).
		<li>Disabling certain combinations of data sets can cause issues at a remote clinic.  Please consult technical support for further assistance.
	</ol>
	<em>Note:</em> Disabling or enabling certain data sets will affect a push to the Integrator if there is one currently underway.
</div>
--%>
<form action="${ctx}/setProviderStaleDate.do" class="well">
	<% UserProperty[] properties = (UserProperty[]) request.getAttribute("integratorProperties"); %>
	<p>During the next Integrator push, send the following patients:</p>
	<input type="hidden" name="method" value="saveIntegratorProperties" />
	<div class="control-group">
		<div class="controls">
			<label class="radio">
				<input type="radio" name="integrator_full_push" value="1" <%=getChecked(properties[18], true) %> /> All Rostered Patients
			</label>
		</div>
	</div>
	<div class="control-group">
		<div class="controls">
			<label class="radio">
				<input type="radio" name="integrator_full_push" value="0" <%=getChecked(properties[18], false) %> /> All Patients with Charts Opened Since <%=(properties[19] != null ? properties[19].getValue() : "<em>[never]</em>") %>
			</label>
		</div>
	</div>
	<oscar:oscarPropertiesCheck property="USE_NEW_PATIENT_CONSENT_MODULE" value="true" >
		<hr />
		<div class="control-group">
			<div class="controls">
				<label class="checkbox">
					<input type="checkbox" name="integrator_patient_consent" value="1" <%=getChecked(properties[20], true) %> /> Push Patient Files With Patient Consent Only. 
				</label>
			</div>
		</div>
	</oscar:oscarPropertiesCheck>
	<hr />
	<input class="btn btn-primary" type="submit" onclick="$('input[type=radio]').removeAttr('disabled');" value="<bean:message key="provider.integratorPreferences.save" />" />
	<%--
	<table id="integratorPrefTable" border=0>
		<tr>
			<td></td>
			<th><bean:message key="provider.integratorPreferences.enabled" /></th>
			<th><bean:message key="provider.integratorPreferences.disabled" /></th>
		</tr>
		<tr>
			<td>Demographic Records</td>
			<td><input type="radio"  name="integrator_demographic_sync" value="1" <%=getChecked(properties[0], true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_sync" value="0" <%=getChecked(properties[0], false) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Allergies</td>
			<td><input type="radio"  name="integrator_demographic_allergies" value="1" <%=getChecked(properties[2], true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_allergies" value="0" <%=getChecked(properties[2], false) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Appointments</td>
			<td><input type="radio"  name="integrator_demographic_appointments" value="1" <%=getChecked(properties[3], true, true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_appointments" value="0" <%=getChecked(properties[3], false, true) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Billing</td>
			<td><input type="radio"  name="integrator_demographic_billing" value="1" <%=getChecked(properties[4], true, true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_billing" value="0" <%=getChecked(properties[4], false, true) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Consent</td>
			<td><input type="radio"  name="integrator_demographic_consent" value="1" <%=getChecked(properties[5], true) %>/></td>
			<td><input type="radio"  name="integrator_demographic_consent" value="0" <%=getChecked(properties[5], false) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Documents</td>
			<td><input type="radio"  name="integrator_demographic_documents" value="1" <%=getChecked(properties[6], true, true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_documents" value="0" <%=getChecked(properties[6], false, true) %> /></td>
		</tr>
		<tr>
			<td>&raquo; E-Forms</td>
			<td><input type="radio"  name="integrator_demographic_eforms" value="1" <%=getChecked(properties[9], true, true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_eforms" value="0" <%=getChecked(properties[9], false, true) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Lab Requisitions</td>
			<td><input type="radio"  name="integrator_demographic_labreq" value="1" <%=getChecked(properties[11], true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_labreq" value="0" <%=getChecked(properties[11], false) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Measurements</td>
			<td><input type="radio"  name="integrator_demographic_measurements" value="1" <%=getChecked(properties[12], true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_measurements" value="0" <%=getChecked(properties[12], false) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Issues</td>
			<td><input type="radio"  name="integrator_demographic_issues" value="1" <%=getChecked(properties[10], true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_issues" value="0" <%=getChecked(properties[10], false) %> /></td>
		</tr>
		<tr>
			<td>&raquo; &raquo; Notes</td>
			<td><input type="radio"  name="integrator_demographic_notes" value="1" <%=getChecked(properties[13], true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_notes" value="0" <%=getChecked(properties[13], false) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Prescriptions</td>
			<td><input type="radio"  name="integrator_demographic_drugs" value="1" <%=getChecked(properties[7], true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_drugs" value="0" <%=getChecked(properties[7], false) %> /></td>
		</tr>
		<tr>
			<td>&raquo; &raquo; Dx Research</td>
			<td><input type="radio"  name="integrator_demographic_dxresearch" value="1" <%=getChecked(properties[8], true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_dxresearch" value="0" <%=getChecked(properties[8], false) %> /></td>
		</tr>
		<tr>
			<td>&raquo; Preventions</td>
			<td><input type="radio"  name="integrator_demographic_preventions" value="1" <%=getChecked(properties[14], true) %> /></td>
			<td><input type="radio"  name="integrator_demographic_preventions" value="0" <%=getChecked(properties[14], false) %> /></td>
		</tr>
		
	</table>
	 --%>
</form>
</body>
</html:html>
<%!
public String getChecked(UserProperty val, boolean trueBtn) {
	if ((val != null && val.getValue().equalsIgnoreCase("0") && !trueBtn) || (val != null && val.getValue().equalsIgnoreCase("1") && trueBtn) || (val == null && trueBtn)) return "checked=\"checked\"";
	return "";
}

public String getChecked(UserProperty val, boolean trueBtn, boolean defaultFalse) {
	if ((val != null && val.getValue().equalsIgnoreCase("0") && !trueBtn) || (val != null && val.getValue().equalsIgnoreCase("1") && trueBtn) || (val == null && !trueBtn && defaultFalse)) return "checked=\"checked\"";
	return "";
}
%>

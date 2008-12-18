<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%
	java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY); 
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="admin.demographicaddrecordhtm.title" /></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />
<script language="JavaScript">
function setfocus() {
  this.focus();
  document.adddemographic.last_name.focus();
  document.adddemographic.last_name.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}

function checkTypeNum(typeIn) {
	var typeInOK = true;
	var i = 0;
	var length = typeIn.length;
	var ch;
	// walk through a string and find a number
	if (length>=1) {
	  while (i <  length) {
		ch = typeIn.substring(i, i+1);
		if ((ch < "0") || (ch > "9")) {
			typeInOK = false;
			break;
		}
	    i++;
      }
	} else typeInOK = false;
	return typeInOK;
}

function checkTypeIn() {
	var typeInOK = false;
	if(document.adddemographic.last_name.value!="" && document.adddemographic.first_name.value!="" && document.adddemographic.sex.value!="") {
      if(checkTypeNum(document.adddemographic.year_of_birth.value) && checkTypeNum(document.adddemographic.month_of_birth.value) && checkTypeNum(document.adddemographic.date_of_birth.value) ){
	    typeInOK = true;
	  }
	}
	if(!typeInOK) alert ("<bean:message key="demographic.demographicaddrecordhtm.msgMissingFields"/>");
	return typeInOK;
}

</script>
</head>
<!--base target="pt_srch_main"-->
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="admin.demographicaddrecordhtm.msgTitle" /></font></th>
	</tr>
</table>
<p>
<table border="0" cellpadding="1" cellspacing="0" width="100%">
	<form method="post" name="adddemographic" action="admincontrol.jsp"
		onsubmit="return checkTypeIn()">
	<tr>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formLastName" /><font color="red">:</font>
		</b></td>
		<td align="left"><input type="text" name="last_name"
			onBlur="upCaseCtrl(this)"></td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formFirstName" /><font color="red">:</font>
		</b></td>
		<td align="left"><input type="text" name="first_name"
			onBlur="upCaseCtrl(this)"></td>
	</tr>
	<% 
    if (vLocale.getCountry().equals("BR")) { %>
	<tr>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formRG" />:</b></td>
		<td align="left"><input type="text" name="rg"
			onBlur="upCaseCtrl(this)"></td>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formCPF" />:</b></td>
		<td align="left"><input type="text" name="cpf"
			onBlur="upCaseCtrl(this)"></td>
	</tr>
	<tr>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formMaritalState" />:</b></td>
		<td align="left"><select name="marital_state">
			<option value="-">-</option>
			<option value="S"><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optSingle" /></option>
			<option value="M"><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optMarried" /></option>
			<option value="R"><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optSeparated" /></option>
			<option value="D"><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optDivorced" /></option>
			<option value="W"><bean:message
				key="demographic.demographicaddrecordhtm.formMaritalState.optWidower" /></option>
		</select></td>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formBirthCertificate" />:</b></td>
		<td align="left"><input type="text" name="birth_certificate"
			onBlur="upCaseCtrl(this)"></td>
	</tr>
	<tr>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formMarriageCertificate" />:</b></td>
		<td align="left"><input type="text" name="marriage_certificate"
			onBlur="upCaseCtrl(this)"></td>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formPartnerName" />:</b></td>
		<td align="left"><input type="text" name="partner_name"
			onBlur="upCaseCtrl(this)"></td>
	</tr>
	<tr>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formFatherName" />:</b></td>
		<td align="left"><input type="text" name="father_name"
			onBlur="upCaseCtrl(this)"></td>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formMotherName" />:</b></td>
		<td align="left"><input type="text" name="mother_name"
			onBlur="upCaseCtrl(this)"></td>
	</tr>
	<%}%>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formAddr" />: </b></td>
		<td align="left"><input type="text" name="address"
			onBlur="upCaseCtrl(this)">
		<% if (vLocale.getCountry().equals("BR")) { %> <b><bean:message
			key="demographic.demographicaddrecordhtm.formAddressNo" />:</b> <input
			type="text" name="address_no" size="6"> <%}%>
		</td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formCity" />: </b></td>
		<td align="left"><input type="text" name="city"
			onBlur="upCaseCtrl(this)"></td>
	</tr>
	<% if (vLocale.getCountry().equals("BR")) { %>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formComplementaryAddress" />:
		</b></td>
		<td align="left"><input type="text" name="complementary_address"
			onBlur="upCaseCtrl(this)"></td>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formDistrict" />: </b></td>
		<td align="left"><input type="text" name="district"
			onBlur="upCaseCtrl(this)"></td>
	</tr>
	<%}%>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formProvince" />: </b></td>
		<td align="left"><input type="text" name="province"
			onBlur="upCaseCtrl(this)"></td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formPostal" />: </b></td>
		<td align="left"><input type="text" name="postal"
			onBlur="upCaseCtrl(this)"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formPhoneH" />: </b></td>
		<td align="left"><input type="text" name="phone" value=""></td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formPhoneW" />:</b></td>
		<td align="left"><input type="text" name="phone2" value=""></td>
	</tr>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formDOB" /><font color="red">:</font></b></td>
		<td align="left">
		<table border="0" cellpadding="0" cellspacing="0">
			<tr>
				<td><input type="text" name="year_of_birth" size="4"
					maxlength="4"></td>
				<td>-</td>
				<td><input type="text" name="month_of_birth" size="2"
					maxlength="2"></td>
				<td>-</td>
				<td><input type="text" name="date_of_birth" size="2"
					maxlength="2"></td>
			</tr>
		</table>
		</td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formAge" />: </b></td>
		<td align="left"><input type="text" name="age"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formHIN" />: </b></td>
		<td align="left"><input type="text" name="hin"></td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formVer" />:</b></td>
		<td align="left"><input type="text" name="ver" value="">
		</td>
	</tr>
	<!--tr valign="top"> 
      <td align="right"><b><bean:message key="admin.demographicaddrecordhtm.formRoster"/>: </b> </td>
      <td align="left" > 
        <input type="text" name="roster_status" onBlur="upCaseCtrl(this)">
      </td>
      <td align="right"> <b><bean:message key="admin.demographicaddrecordhtm.formPatientStatus"/>:</b> <b> </b></td>
      <td align="left"> 
        <input type="text" name="patient_status" onBlur="upCaseCtrl(this)">
      </td>
    </tr-->
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formDatejoined" />: </b></td>
		<td align="left"><input type="text" name="date_joined"
			value="0001-01-01"></td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formHCType" />: </b></td>
		<td align="left"><select name="hc_type">
			<option value="ON" selected>Ontario</option>
			<option value="AB">Alberta</option>
			<option value="BC">British Columbia</option>
			<option value="MB">Manitoba</option>
			<option value="NF">Newfoundland</option>
			<option value="NB">New Brunswick</option>
			<option value="YT">Yukon</option>
			<option value="NS">Nova Scotia</option>
			<option value="PE">Prince Edward Island</option>
			<option value="SK">Saskatchewan</option>
		</select></td>
	</tr>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formStaff" />: </b></td>
		<td align="left"><input type="text" name="staff"></td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formSex" /> </b><b><font
			color="red">:</font> </b></td>
		<td align="left"><select name="sex">
			<option value="F" selected><bean:message
				key="admin.demographicaddrecordhtm.formF" /></option>
			<option value="M"><bean:message
				key="admin.demographicaddrecordhtm.formM" /></option>
		</select></td>
	</tr>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formEndDate" />: </b></td>
		<td align="left"><input type="text" name="end_date"
			value="0001-01-01"></td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formEFF" />: </b></td>
		<td align="left"><input type="text" name="eff_date"
			value="0001-01-01"></td>
	</tr>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formPCNInd" />: </b></td>
		<td align="left"><input type="text" name="pcn_indicator"
			onBlur="upCaseCtrl(this)"></td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formChartNo" />:</b></td>
		<td align="left"><input type="text" name="chart_no" value="">
		</td>
	</tr>
	<% 
    if (vLocale.getCountry().equals("BR")) { %>
	<tr valign="top">
		<td align="right"><b></b></td>
		<td align="left"></td>
		<td align="right"><b><bean:message
			key="demographic.demographicaddrecordhtm.formChartAddress" />:</b></td>
		<td align="left"><input type="text" name="chart_address" value="">
		</td>
	</tr>
	<%}%>
	<tr valign="top">
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formHCRenewDate" />: </b></td>
		<td align="left"><input type="text" name="hc_renew_date"
			value="0001-01-01"></td>
		<td align="right"><b><bean:message
			key="admin.demographicaddrecordhtm.formFamilyDoc" />: </b></td>
		<td align="left"><input type="text" name="family_doctor">
		</td>
	</tr>
	<tr>
		<td colspan="4">&nbsp;</td>
	</tr>
	<tr bgcolor="#486ebd">
		<td colspan="4">
		<div align="center"><input type="hidden" name="dboperation"
			value="demographic_add_record"> <% 
          if (vLocale.getCountry().equals("BR")) { %> <input
			type="hidden" name="dboperation2" value="demographic_add_record_ptbr">
		<%}%> <input type="hidden" name="displaymode"
			value="Demographic_Add_Record"> <input type="submit"
			name="subbutton"
			value="<bean:message key="admin.demographicaddrecordhtm.btnAdd"/>">
		<input type="reset" name="Reset"
			value="<bean:message key="global.btnCancel"/>"></div>
		</td>
	</tr>
	</form>
</table>

<hr width="100%" color="navy">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="admin.jsp"> <img src="../images/leftarrow.gif"
			border="0" width="25" height="20" align="absmiddle"> <bean:message
			key="admin.demographicaddrecordhtm.btnBack" /></a></td>
		<td align="right"><a href="../logout.jsp"> <bean:message
			key="admin.demographicaddrecordhtm.btnLogOut" /><img
			src="../images/rightarrow.gif" border="0" width="25" height="20"
			align="absmiddle"></a></td>
	</tr>
</table>

</body>
</html:html>

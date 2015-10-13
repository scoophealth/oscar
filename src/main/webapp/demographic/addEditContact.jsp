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

<!--
/*
 *
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 * <OSCAR TEAM>
 */
-->

<%@ include file="/taglibs.jsp"%>
<%@ page import="java.util.Properties"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
  
  String msg = "Enter contact details.";
  Properties	prop  = new Properties();
  
%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Add/Edit Contact</title>
<script language="JavaScript">

      <!--
		function setfocus() {
		  this.focus();
		  document.forms[0].referral_no.focus();
		  document.forms[0].referral_no.select();
		}
	    function onSearch() {
	        //document.forms[0].submit.value="Search";
	        var ret = checkreferral_no();
	        return ret;
	    }
	    function onSave() {
	        //document.forms[0].submit.value="Save";
	        /*
	        var ret = true;
	        if(ret==true) {
				ret = checkAllFields();
			}
	        if(ret==true) {
	            ret = confirm("Are you sure you want to save?");
	        }
	        */
	        return true;
	    }
		
		function checkAllFields() {
	        var b = true;
	        if(document.forms[0].last_name.value.length<=0){
	            b = false;
	            alert ("The field \"Last Name\" is empty.");
	        } else if(document.forms[0].first_name.value.length<=0) {
	            b = false;
	            alert ("The field \"First Name\" is empty.");
	        }
			return b;
	    }
	    function isNumber(s){
	        var i;
	        for (i = 0; i < s.length; i++){
	            // Check that current character is number.
	            var c = s.charAt(i);
	            if (c == ".") continue;
	            if (((c < "0") || (c > "9"))) return false;
	        }
	        // All characters are numbers.
	        return true;
	    }
//-->

      </script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td align="left">&nbsp;</td>
	</tr>
</table>

<center>
<table BORDER="1" CELLPADDING="0" CELLSPACING="0" WIDTH="80%">
	<tr BGCOLOR="#CCFFFF">
		<th><%=msg%></th>
	</tr>
</table>
</center>
<html:form action="/demographic/Contact">
	<input type="hidden" name="contact.id" value="<c:out value="${contact.id}"/>"/>
	<input type="hidden" name="method" value="saveContact"/>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<tr>
		<td>&nbsp;</td>
	</tr>	
	<tr>
		<td align="right"><b>Last Name</b></td>
		<td>
			<input type="text" name="contact.lastName"	value="<c:out value="${contact.lastName}"/>" size="30">
		</td>
	</tr>
	<tr>
		<td align="right"><b>First Name</b></td>
		<td>
			<input type="text" name="contact.firstName" value="<c:out value="${contact.firstName}"/>" size="30">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Address</b></td>
		<td>
			<input type="text" name="contact.address" value="<c:out value="${contact.address}"/>" size="50">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Address2</b></td>
		<td>
			<input type="text" name="contact.address2" value="<c:out value="${contact.address2}"/>" size="50">
		</td>
	</tr>
	<tr>
		<td align="right"><b>City</b></td>
		<td>
			<input type="text" name="contact.city" value="<c:out value="${contact.city}"/>" size="30">
		</td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td align="right"><b>Province</b></td>
		<td>
		<% String region = prop.getProperty("province", "");
              	 region = "".equals(region) ? "ON" : region;
              %> <select name="contact.province">
			<option value="AB" <%=region.equals("AB")?" selected":""%>>AB-Alberta</option>
			<option value="BC" <%=region.equals("BC")?" selected":""%>>BC-British
			Columbia</option>
			<option value="MB" <%=region.equals("MB")?" selected":""%>>MB-Manitoba</option>
			<option value="NB" <%=region.equals("NB")?" selected":""%>>NB-New
			Brunswick</option>
			<option value="NL" <%=region.equals("NL")?" selected":""%>>NL-Newfoundland
			& Labrador</option>
			<option value="NT" <%=region.equals("NT")?" selected":""%>>NT-Northwest
			Territory</option>
			<option value="NS" <%=region.equals("NS")?" selected":""%>>NS-Nova
			Scotia</option>
			<option value="NU" <%=region.equals("NU")?" selected":""%>>NU-Nunavut</option>
			<option value="ON" <%=region.equals("ON")?" selected":""%>>ON-Ontario</option>
			<option value="PE" <%=region.equals("PE")?" selected":""%>>PE-Prince
			Edward Island</option>
			<option value="QC" <%=region.equals("QC")?" selected":""%>>QC-Quebec</option>
			<option value="SK" <%=region.equals("SK")?" selected":""%>>SK-Saskatchewan</option>
			<option value="YT" <%=region.equals("YT")?" selected":""%>>YT-Yukon</option>
			<option value="US" <%=region.equals("US")?" selected":""%>>US
			resident</option>
		</select> Country 
		<input type="text" name="contact.country" value="<c:out value="${contact.country}"/>" size="2" maxlength="2">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Postal</b></td>
		<td>
			<input type="text" name="contact.postal" value="<c:out value="${contact.postal}"/>" size="30">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Res. Phone</b></td>
		<td>
			<input type="text" name="contact.residencePhone" value="<c:out value="${contact.residencePhone}"/>" size="30">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Cell Phone</b></td>
		<td>
			<input type="text" name="contact.cellPhone" value="<c:out value="${contact.cellPhone}"/>" size="30">
		</td>
	</tr>	
	<tr>
		<td align="right"><b>Work Phone</b></td>
		<td>
			<input type="text" name="contact.workPhone"	value="<c:out value="${contact.workPhone}"/>" size="15"/>
		Ext: <input type="text" name="contact.workPhoneExtension" value="<c:out value="${contact.workPhoneExtension}"/>" size="10"/>
		</td>
	</tr>
	<tr>
		<td align="right"><b>Fax</b></td>
		<td>
			<input type="text" name="contact.fax" value="<c:out value="${contact.fax}"/>" size="30">
		</td>
	</tr>	
	<tr>
		<td align="right"><b>Email</b></td>
		<td>
			<input type="text" name="contact.email" value="<c:out value="${contact.email}"/>" size="30">
		</td>
	</tr>	
	<tr>
		<td align="right"><b>Note</b></td>
		<td>
			<input type="text" name="contact.note" value="<c:out value="${contact.note}"/>" size="30">
		</td>
	</tr>	
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align="center" bgcolor="#CCCCFF" colspan="2">
			<input type="submit" name="submit" value="<bean:message key="admin.resourcebaseurl.btnSave"/>" onclick="javascript:return onSave();"> 			
			<input type="button" name="Cancel" value="<bean:message key="admin.resourcebaseurl.btnExit"/>" onClick="window.close()">
		</td>
	</tr>	
</table>
</html:form>
</body>
</html:html>

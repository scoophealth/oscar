<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../logout.jsp");
  }
%>
<%@ page errorPage="../appointment/errorpage.jsp"
	import="java.util.*,
                                                           java.sql.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Billingreferral" %>
<%@page import="org.oscarehr.common.dao.BillingreferralDao" %>
<%
	BillingreferralDao billingReferralDao = (BillingreferralDao)SpringUtils.getBean("BillingreferralDAO");
%>
<%
  int referralNoLen = 6;
  String msg = "Type in a doctor's ref. # and search first to see if it is available.";
  String action = "search"; // add/edit
  //BillingServiceCode serviceCodeObj = new BillingServiceCode();
  Properties	prop  = new Properties();
  if (request.getParameter("submit") != null && request.getParameter("submit").equals("Save")) {
    // check the input data

    if(request.getParameter("action").startsWith("edit")) {
      	// update the service code
		String referral_no = request.getParameter("referral_no");
		if(referral_no.equals(request.getParameter("action").substring("edit".length()))) {
			Billingreferral billingReferral = billingReferralDao.getByReferralNo(referral_no);
			if(billingReferral == null) {
				billingReferral = new Billingreferral();
			}
			billingReferral.setLastName(request.getParameter("last_name"));
			billingReferral.setFirstName(request.getParameter("first_name"));
			billingReferral.setSpecialty(request.getParameter("specialty"));
			billingReferral.setAddress1(request.getParameter("address1"));
			billingReferral.setAddress2(request.getParameter("address2"));
			billingReferral.setCity(request.getParameter("city"));
			billingReferral.setProvince(request.getParameter("province"));
			billingReferral.setCountry(request.getParameter("country"));
			billingReferral.setPostal(request.getParameter("postal"));
			billingReferral.setPhone(request.getParameter("phone"));
			billingReferral.setFax(request.getParameter("fax"));
			billingReferral.setReferralNo(referral_no);
			billingReferralDao.updateBillingreferral(billingReferral);

			msg = referral_no + " is updated.<br>" + "Type in a doctor's ref. # and search first to see if it is available.";
  			action = "search";
		    prop.setProperty("referral_no", referral_no);

		} else {
      		msg = "You can <font color='red'>NOT</font> save the doctor's ref. # - " + referral_no + ". Please search the doctor's ref. # first.";
  			action = "search";
		    prop.setProperty("referral_no", referral_no);
		}
    } else if (request.getParameter("action").startsWith("add")) {
		String referral_no = request.getParameter("referral_no");
		if(referral_no.equals(request.getParameter("action").substring("add".length()))) {
			Billingreferral billingReferral =  new Billingreferral();
			billingReferral.setLastName(request.getParameter("last_name"));
			billingReferral.setFirstName(request.getParameter("first_name"));
			billingReferral.setSpecialty(request.getParameter("specialty"));
			billingReferral.setAddress1(request.getParameter("address1"));
			billingReferral.setAddress2(request.getParameter("address2"));
			billingReferral.setCity(request.getParameter("city"));
			billingReferral.setProvince(request.getParameter("province"));
			billingReferral.setCountry(request.getParameter("country"));
			billingReferral.setPostal(request.getParameter("postal"));
			billingReferral.setPhone(request.getParameter("phone"));
			billingReferral.setFax(request.getParameter("fax"));
			billingReferral.setReferralNo(referral_no);
			billingReferralDao.updateBillingreferral(billingReferral);

			msg = referral_no + " is added.<br>" + "Type in a doctor's ref. # and search first to see if it is available.";
  			action = "search";
		    prop.setProperty("referral_no", referral_no);

		} else {
      		msg = "You can <font color='red'>NOT</font> save the ref. code - " + referral_no + ". Please search the ref. # first.";
  			action = "search";
		    prop.setProperty("referral_no", referral_no);
		}
    } else {
      msg = "You can <font color='red'>NOT</font> save the ref. code. Please search the ref. code first.";
    }
  } else if (request.getParameter("submit") != null && request.getParameter("submit").equals("Search")) {
    // check the input data
    if(request.getParameter("referral_no") == null || request.getParameter("referral_no").length() != referralNoLen) {
      msg = "Please type in a ref. code.";
    } else {
        String referral_no = request.getParameter("referral_no");
        Billingreferral billingReferral = billingReferralDao.getByReferralNo(referral_no);

		if (billingReferral != null) {
		    prop.setProperty("referral_no", referral_no);
		    prop.setProperty("last_name", billingReferral.getLastName());
		    prop.setProperty("first_name", billingReferral.getFirstName());
		    prop.setProperty("specialty",billingReferral.getSpecialty());
		    prop.setProperty("address1", billingReferral.getAddress1());
		    prop.setProperty("address2", billingReferral.getAddress2());
		    prop.setProperty("city", billingReferral.getCity());
		    prop.setProperty("province", billingReferral.getProvince());
		    prop.setProperty("country", billingReferral.getCountry());
		    prop.setProperty("postal", billingReferral.getPostal());
		    prop.setProperty("phone",billingReferral.getPhone());
		    prop.setProperty("fax", billingReferral.getFax());
		    msg = "You can edit the ref. code.";
		    action = "edit" + referral_no;
		} else {
		    prop.setProperty("referral_no", referral_no);
		    msg = "It is a NEW ref. code. You can add it.";
		    action = "add" + referral_no;
		}
	}
  }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Add/Edit Service Code</title>
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
	        var ret = checkreferral_no();
	        if(ret==true) {
				ret = checkAllFields();
			}
	        if(ret==true) {
	            ret = confirm("Are you sure you want to save?");
	        }
	        return ret;
	    }
		function checkreferral_no() {
	        var b = true;
	        if(document.forms[0].referral_no.value.length!=6 || !isreferral_no(document.forms[0].referral_no.value)){
	            b = false;
	            alert ("You must type in a ref. code with 6 digits.");
	        }
	        return b;
	    }
    function isreferral_no(s){
        // temp for 0.
    	if(s.length==0) return true;
    	if(s.length!=6) return false;
        var i;
        for (i = 0; i < s.length; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
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
<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<form method="post" name="baseurl" action="addEditRefDoc.jsp">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td align="right"><b>Referral Doc. Code</b></td>
		<td><input type="text" name="referral_no"
			value="<%=prop.getProperty("referral_no", "")%>" size='5'
			maxlength='6'> (6 digits, e.g. 123456) <input type="submit"
			name="submit" value="Search" onclick="javascript:return onSearch();">
		</td>
	</tr>
	<tr>
		<td align="right"><b>Last Name</b></td>
		<td><input type="text" name="last_name"
			value="<%=prop.getProperty("last_name", "")%>" size='30'
			maxlength='30'></td>
	</tr>
	<tr>
		<td align="right"><b>First Name</b></td>
		<td><input type="text" name="first_name"
			value="<%=prop.getProperty("first_name", "")%>" size='30'
			maxlength='30'></td>
	</tr>
	<tr>
		<td align="right"><b>Specialty</b></td>
		<td><input type="text" name="specialty"
			value="<%=prop.getProperty("specialty", "")%>" size='30'
			maxlength='30'></td>
	</tr>
	<tr>
		<td align="right"><b>Address1</b></td>
		<td><input type="text" name="address1"
			value="<%=prop.getProperty("address1", "")%>" size='50'
			maxlength='50'></td>
	</tr>
	<tr>
		<td align="right"><b>Address2</b></td>
		<td><input type="text" name="address2"
			value="<%=prop.getProperty("address2", "")%>" size='50'
			maxlength='50'></td>
	</tr>
	<tr>
		<td align="right"><b>City</b></td>
		<td><input type="text" name="city"
			value="<%=prop.getProperty("city", "")%>" size='30' maxlength='30'>
		</td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td align="right"><b>Province</b></td>
		<td>
		<% String region = prop.getProperty("province", "");
              	 region = "".equals(region) ? "ON" : region;
              %> <select name="province">
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
		</select> Country <input type="text" name="country"
			value="<%=prop.getProperty("country", "CA")%>" size='2' maxlength='2'>
		</td>
	</tr>
	<tr>
		<td align="right"><b>Postal</b></td>
		<td><input type="text" name="postal"
			value="<%=prop.getProperty("postal", "")%>" size='30' maxlength='30'>
		</td>
	</tr>
	<tr>
		<td align="right"><b>phone</b></td>
		<td><input type="text" name="phone"
			value="<%=prop.getProperty("phone", "")%>" size='10' maxlength='12'>
		Fax <input type="text" name="fax"
			value="<%=prop.getProperty("fax", "")%>" size='10' maxlength='12'>
		</td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align="center" bgcolor="#CCCCFF" colspan="2"><input
			type="hidden" name="action" value='<%=action%>'> <% if(!"search".equals(action)) {%>
		<input type="submit" name="submit"
			value="<bean:message key="admin.resourcebaseurl.btnSave"/>"
			onclick="javascript:return onSave();"> <% }%> <input
			type="button" name="Cancel"
			value="<bean:message key="admin.resourcebaseurl.btnExit"/>"
			onClick="window.close()"></td>
	</tr>
	</form>
</table>
</body>
</html:html>

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

<%if (session.getAttribute("user") == null) {
				response.sendRedirect("../logout.jsp");
			}
			String user_no = (String) session.getAttribute("user");

			%>
<%@ page errorPage="../../../appointment/errorpage.jsp"
	import="java.util.*,java.sql.*,oscar.*,java.text.*,java.net.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<% //
			int serviceCodeLen = 5;
			String msg = "Type in a name and search first to see if it is available.";
			String action = "search"; // add/edit
			//BillingServiceCode serviceCodeObj = new BillingServiceCode();
			Properties prop = new Properties();
			JdbcBilling3rdPartImpl dbObj = new JdbcBilling3rdPartImpl();
			if (request.getParameter("submit") != null && request.getParameter("submit").equals("Save")) {
				if (request.getParameter("action").startsWith("edit")) {
					// update the service code
					String company_name = request.getParameter("company_name");
					if (company_name.equals(request.getParameter("action").substring("edit".length()))) {
						String list = "";
						Properties val = new Properties();
						val.setProperty("id", request.getParameter("id"));
						val.setProperty("attention", request.getParameter("attention"));
						val.setProperty("company_name", request.getParameter("company_name"));
						val.setProperty("address", request.getParameter("address"));
						val.setProperty("city", request.getParameter("city"));
						val.setProperty("province", request.getParameter("province"));
						val.setProperty("postcode", request.getParameter("postcode"));
						val.setProperty("telephone", request.getParameter("telephone"));
						val.setProperty("fax", request.getParameter("fax"));

						boolean ni = dbObj.update3rdAddr(request.getParameter("id"), val);
						if (ni) {
							msg = company_name + " is updated.<br>"
									+ "Type in a name and search first to see if it is available.";
							action = "search";
							prop.setProperty("company_name", company_name);
						} else {
							msg = company_name + " is <font color='red'>NOT</font> updated. Action failed! Try edit it again.";
							action = "edit" + company_name;
							prop.setProperty("company_name", company_name);
							prop.setProperty("id", request.getParameter("id"));
							prop.setProperty("attention", request.getParameter("attention"));
							prop.setProperty("company_name", request.getParameter("company_name"));
							prop.setProperty("address", request.getParameter("address"));
							prop.setProperty("city", request.getParameter("city"));
							prop.setProperty("province", request.getParameter("province"));
							prop.setProperty("postcode", request.getParameter("postcode"));
							prop.setProperty("telephone", request.getParameter("telephone"));
							prop.setProperty("fax", request.getParameter("fax"));
						}
					} else {
						msg = "You can <font color='red'>NOT</font> save the name - " + company_name
								+ ". Please search the name first.";
						action = "search";
						prop.setProperty("company_name", company_name);
					}

				} else if (request.getParameter("action").startsWith("add")) {
					String company_name = request.getParameter("company_name");
					if (company_name.equals(request.getParameter("action").substring("add".length()))) {
						Properties val = new Properties();
						val.setProperty("attention", request.getParameter("attention"));
						val.setProperty("company_name", request.getParameter("company_name"));
						val.setProperty("address", request.getParameter("address"));
						val.setProperty("city", request.getParameter("city"));
						val.setProperty("province", request.getParameter("province"));
						val.setProperty("postcode", request.getParameter("postcode"));
						val.setProperty("telephone", request.getParameter("telephone"));
						val.setProperty("fax", request.getParameter("fax"));
						int ni = dbObj.addOne3rdAddrRecord(val);
						if (ni > 0) {
							msg = company_name + " is added.<br>"
									+ "Type in a name and search first to see if it is available.";
							action = "search";
							prop.setProperty("company_name", company_name);
						} else {
							msg = company_name + " is <font color='red'>NOT</font> added. Action failed! Try edit it again.";
							action = "add" + company_name;
							prop.setProperty("company_name", company_name);
							prop.setProperty("attention", request.getParameter("attention"));
							prop.setProperty("company_name", request.getParameter("company_name"));
							prop.setProperty("address", request.getParameter("address"));
							prop.setProperty("city", request.getParameter("city"));
							prop.setProperty("province", request.getParameter("province"));
							prop.setProperty("postcode", request.getParameter("postcode"));
							prop.setProperty("telephone", request.getParameter("telephone"));
							prop.setProperty("fax", request.getParameter("fax"));
						}
					} else {
						msg = "You can <font color='red'>NOT</font> save the name - " + company_name
								+ ". Please search the name first.";
						action = "search";
						prop.setProperty("company_name", company_name);
					}
				} else {
					msg = "You can <font color='red'>NOT</font> save the name. Please search the name first.";
				}
			} else if (request.getParameter("submit") != null && request.getParameter("submit").equals("Search")) {
				// check the input data
				if (request.getParameter("company_name") == null) {
					msg = "Please type in a right name.";
				} else {
					String company_name = request.getParameter("company_name");
					Properties ni = dbObj.get3rdAddrProp(company_name);
					if (!ni.getProperty("company_name", "").equals("")) {
						prop = ni; //.setProperty("company_name", company_name);
						int n = 0;
						msg = "You can edit the name.";
						action = "edit" + company_name;

					} else {
						prop.setProperty("company_name", company_name);
						msg = "It is a NEW name. You can add it.";
						action = "add" + company_name;
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
<link rel="stylesheet" type="text/css" href="billingON.css" />
<link rel="StyleSheet" type="text/css" href="../web.css" />
<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>">
              </script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript"
	src="../../../share/calendar/calendar-setup.js"></script>
<script language="JavaScript">

      <!--
		function setfocus() {
		  this.focus();
		  document.forms[0].company_name.focus();
		  document.forms[0].company_name.select();
		}
	    function onSearch() {
	        //document.forms[0].submit.value="Search";
	        var ret = checkServiceCode();
	        return ret;
	    }
	    function onSave() {
	        //document.forms[0].submit.value="Save";
	        var ret = checkServiceCode();
	        if(ret==true) {
				ret = checkAllFields();
			}
	        if(ret==true) {
	            ret = confirm("Are you sure you want to save?");
	        }
	        return ret;
	    }
		function checkServiceCode() {
	        var b = true;
	        if(document.forms[0].service_code.value.length!=5 || !isServiceCode(document.forms[0].service_code.value)){
	            b = false;
	            alert ("You must type in a service code with 5 (upper case) letters/digits. The service code ends with \'A\' or \'B\'...");
	        }
	        return b;
	    }
    function isServiceCode(s){
        // temp for 0.
    	if(s.length==0) return true;
    	if(s.length!=5) return false;
        if((s.charAt(0) < "A") || (s.charAt(0) > "Z")) return false;
        if((s.charAt(4) < "A") || (s.charAt(4) > "Z")) return false;

        var i;
        for (i = 1; i < s.length-1; i++){
            // Check that current character is number.
            var c = s.charAt(i);
            if (((c < "0") || (c > "9"))) return false;
        }
        return true;
    }
		function checkAllFields() {
	        var b = true;
	        for(var i=0; i<10; i++) {
	        	var fieldItem = eval("document.forms[1].serviceCode" + i);
	        	if(fieldItem.value.length>0) {
			        if(!isServiceCode(fieldItem.value)){
			            b = false;
			            alert ("You must type in a Service Code in the field!");
			        }
	        	}
	        	var fieldItem1 = eval("document.forms[1].serviceUnit" + i);
	        	var fieldItem2 = eval("document.forms[1].serviceAt" + i);
	        	if(fieldItem1.value.length>0) {
			        if(!isNumber(fieldItem1.value)){
			            b = false;
			            alert ("You must type in a number in the field!");
			        }
	        	}
	        	if(fieldItem2.value.length>0) {
			        if(!isNumber(fieldItem2.value)){
			            b = false;
			            alert ("You must type in a number in the field!");
			        }
	        	}
	        }
	        var fieldItemDx = eval("document.forms[1].dx");
	        if(fieldItemDx.value.length>0){
			        if(!isNumber(fieldItemDx.value) || fieldItemDx.value.length!=3){
			            b = false;
			            alert ("You must type in a number in the right Dx field!");
			        }
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
	    function upCaseCtrl(ctrl) {
			ctrl.value = ctrl.value.toUpperCase();
		}
	    
//-->

      </script>
</head>
<body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0"
	rightmargin="0">
<center>
<table BORDER="1" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr class="myDarkGreen">
		<th><font color="white"><%=msg%></font></th>
	</tr>
</table>
</center>

<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%"
	class="myYellow">
	<form method="post" name="baseur0" action="onAddEdit3rdAddr.jsp">
	
	<tr>
		<td align="right" width="50%"><select name="company_name"
			id="company_name">
			<option selected="selected" value="">- choose one -</option>
			<%//
				List sL = dbObj.get3rdAddrNameList();
				for (int i = 0; i < sL.size(); i++) {
					Properties propT = (Properties) sL.get(i);
					%>
			<option value="<%=propT.getProperty("company_name", "")%>"><%=propT.getProperty("company_name", "")%></option>
			<%}
				%>
		</select></td>
		<td><input type="hidden" name="submit" value="Search"> <input
			type="submit" name="action" value=" Edit "></td>
	</tr>
	</form>
</table>
<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<form method="post" name="baseurl" action="onAddEdit3rdAddr.jsp">
	<tr class="myGreen">
		<td align="right"><b>Company Name</b></td>
		<td><input type="text" name="company_name"
			value="<%=prop.getProperty("company_name", "")%>" size='40'
			maxlength='50' /> <input type="submit" name="submit" value="Search"
			onclick="javascript:return onSearch();"></td>
	</tr>
	<tr class="myIvory">
		<td align="right"><b>Attention</b></td>
		<td><input type="text" name="attention"
			value="<%=prop.getProperty("attention", "")%>" size='40'
			maxlength='50' /></td>
	</tr>
	<tr class="myGreen">
		<td align="right"><b>Address</b></td>
		<td><input type="text" name="address"
			value="<%=prop.getProperty("address", "")%>" size='40' maxlength='50' />
		</td>
	</tr>
	<tr class="myIvory">
		<td align="right"><b>City</b></td>
		<td><input type="text" name="city"
			value="<%=prop.getProperty("city", "")%>" size='40' maxlength='50' />
		</td>
	</tr>
	<tr class="myGreen">
		<td align="right"><b>Province</b></td>
		<td><input type="text" name="province"
			value="<%=prop.getProperty("province", "")%>" size='20'
			maxlength='20' /></td>
	</tr>
	<tr class="myIvory">
		<td align="right"><b>postcode</b></td>
		<td><input type="text" name="postcode"
			value="<%=prop.getProperty("postcode", "")%>" size='10'
			maxlength='10' /></td>
	</tr>
	<tr class="myGreen">
		<td align="right"><b>Tel.</b></td>
		<td><input type="text" name="telephone"
			value="<%=prop.getProperty("telephone", "")%>" size='40'
			maxlength='50' /></td>
	</tr>
	<tr class="myIvory">
		<td align="right"><b>Fax</b></td>
		<td><input type="text" name="fax"
			value="<%=prop.getProperty("fax", "")%>" size='40' maxlength='50' />
		</td>
	</tr>
	<tr>
		<td align="center" class="myGreen" colspan="2"><input
			type="hidden" name="action" value='<%=action%>'> <input
			type="submit" name="submit"
			value="<bean:message key="admin.resourcebaseurl.btnSave"/>"
			onclick="javascript:return onSave();"> <input type="button"
			name="Cancel"
			value="<bean:message key="admin.resourcebaseurl.btnExit"/>"
			onClick="window.close()"> <input type="hidden" name="id"
			value="<%=prop.getProperty("id", "")%>" /></td>
	</tr>
	</form>
</table>
</body>
<script type="text/javascript">
//Calendar.setup( { inputField : "billingservice_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "billingservice_date_cal", singleClick : true, step : 1 } );
</script>
</html:html>

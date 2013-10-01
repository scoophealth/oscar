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
<%if (session.getAttribute("user") == null) {
				response.sendRedirect("../logout.jsp");
			}

			%>
<%@ page errorPage="../appointment/errorpage.jsp"
	import="java.util.*,java.sql.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%//
			//int serviceCodeLen = 5;
			String msg = "Type in a service code and search first to see if it is available.";
			String action = "search"; // add/edit
			//BillingServiceCode serviceCodeObj = new BillingServiceCode();
			Properties prop = new Properties();
			JdbcBillingCodeImpl dbObj = new JdbcBillingCodeImpl();
			if (request.getParameter("submit") != null && request.getParameter("submit").equals("Save")) {
				String valuePara = request.getParameter("value");
				if (request.getParameter("action").startsWith("edit")) {
					// update the service code
					String serviceCode = request.getParameter("service_code");
					if (serviceCode == null)
						serviceCode = "";
					serviceCode = "_" + serviceCode;
					if (serviceCode.equals(request.getParameter("action").substring("edit".length()))) {
						if (dbObj.updateCodeByName(serviceCode, request.getParameter("description"), valuePara, "0.00",
								request.getParameter("billingservice_date"), request.getParameter("gstFlag"))) {
							msg = serviceCode + " is updated.<br>"
									+ "Type in a service code and search first to see if it is available.";
							action = "search";
							prop.setProperty("service_code", serviceCode);
						} else {
							msg = serviceCode
									+ " is <font color='red'>NOT</font> updated. Action failed! Try edit it again.";
							action = "edit" + serviceCode;
							prop.setProperty("service_code", serviceCode);
							String description = request.getParameter("description");
							if (description == null)
								description = "";
							prop.setProperty("description", description);
							prop.setProperty("value", request.getParameter("value"));
							prop.setProperty("billingservice_date", request.getParameter("billingservice_date"));
                                                        prop.setProperty("gstFlag", request.getParameter("gstFlag"));
						}
					} else {
						msg = "You can <font color='red'>NOT</font> save the service code - " + serviceCode
								+ ". Please search the service code first.";
						action = "search";
						prop.setProperty("service_code", serviceCode);
					}
				} else if (request.getParameter("action").startsWith("add")) {
					String serviceCode = request.getParameter("service_code");
					if (serviceCode == null)
						serviceCode = "";
					serviceCode = "_" + serviceCode;
					if (serviceCode.equals(request.getParameter("action").substring("add".length()))) {
						if (dbObj.addCodeByStr(serviceCode, request.getParameter("description"), valuePara, "0.00",
								request.getParameter("billingservice_date"), request.getParameter("gstFlag")) > 0) {
							msg = serviceCode + " is added.<br>"
									+ "Type in a service code and search first to see if it is available.";
							action = "search";
							prop.setProperty("service_code", serviceCode);
						} else {
							msg = serviceCode
									+ " is <font color='red'>NOT</font> added. Action failed! Try edit it again.";
							action = "add" + serviceCode;
							prop.setProperty("service_code", serviceCode);
							String description = request.getParameter("description");
							if (description == null)
								description = "";
							prop.setProperty("description", description);
							prop.setProperty("value", request.getParameter("value"));
							prop.setProperty("billingservice_date", request.getParameter("billingservice_date"));
                                                        prop.setProperty("gstFlag", request.getParameter("gstFlag"));
						}
					} else {
						msg = "You can <font color='red'>NOT</font> save the service code - " + serviceCode
								+ ". Please search the service code first.";
						action = "search";
						prop.setProperty("service_code", serviceCode);
					}
				} else {
					msg = "You can <font color='red'>NOT</font> save the service code. Please search the service code first.";
				}
			} else if (request.getParameter("submit") != null && request.getParameter("submit").equals("Search")) {
				// check the input data
				if (request.getParameter("service_code") == null) {
					msg = "Please type in a right service code.";
				} else {
					String serviceCode = request.getParameter("service_code");
					if (serviceCode == null)
						serviceCode = "";
					serviceCode = "_" + serviceCode;
					List ls = dbObj.getBillingCodeAttr(serviceCode);
					if (ls.size() > 0) {
						prop.setProperty("service_code", serviceCode);
						String description = (String) ls.get(1);
						if (description == null)
							description = "";
						prop.setProperty("description", description);
						prop.setProperty("value", "" + ls.get(2));
						prop.setProperty("percentage", "" + ls.get(3));
						prop.setProperty("billingservice_date", "" + ls.get(4));
                                                prop.setProperty("gstFlag", "" + ls.get(5));
						msg = "You can edit the service code.";
						action = "edit" + serviceCode;
					} else {
						prop.setProperty("service_code", serviceCode);
						msg = "It is a NEW service code. You can add it.";
						action = "add" + serviceCode;
					}
				}
			} else if (request.getParameter("submit") != null && request.getParameter("submit").equals("Delete")) {
				if (request.getParameter("service_code") == null) {
					msg = "Please type in a right service code.";
				} else {
					String serviceCode = request.getParameter("service_code");
					if (serviceCode == null)
						serviceCode = "";
					serviceCode = "_" + serviceCode;
					if (dbObj.deletePrivateCode(serviceCode)) {
						msg = serviceCode + " is deleted.<br>"
								+ "Type in a service code and search first to see if it is available.";
						action = "search";
						prop.setProperty("service_code", "_");
					} 
				}
			}
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Add/Edit Private Service Code</title>
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
		  document.forms[1].service_code.focus();
		  document.forms[1].service_code.select();
                <% if ( prop.getProperty("gstFlag") != null ) {%>
                if( "<%=prop.getProperty("gstFlag")%>" == "1" ){
                    document.getElementById("gstCheck").checked = true;
                    document.getElementById("gstFlag").value = 1;
                } else {
                    document.getElementById("gstCheck").checked = false;
                    document.getElementById("gstFlag").value = 0;
                }
                <%}%>
		}
	    function onSearch() {
	        //document.forms[1].submit.value="Search";
	        var ret = checkServiceCode();
	        return ret;
	    }
	    function onSave() {
	        //document.forms[1].submit.value="Save";
	        var ret = checkServiceCode();
	        if(ret==true) {
				ret = checkAllFields();
			}
	        if(ret==true) {
	            ret = confirm("Are you sure you want to save?");
	        }
	        return ret;
	    }
	    function onDelete() {
	        var ret = checkServiceCode();
	        if(ret==true) {
	            ret = confirm("Are you sure you want to Delete?");
	        }
	        return ret;
	    }
		function checkServiceCode() {
	        var b = true;
	        if(document.forms[1].service_code.value.length==0){
	            b = false;
	            alert ("You must type in a service code with letters/digits.");
	        }
	        return b;
	    }
		function checkAllFields() {
	        var b = true;
	        b = checkServiceCode();
	        if(document.forms[1].value.value.length>0) {
		        if(!isNumber(document.forms[1].value.value)){
		            b = false;
		            alert ("You must type in a number in the field fee");
		        }
	        } else if(document.forms[1].value.value.length==0) {
	            b = false;
	            alert ("You must type in a number in the field fee");
	        }

			if(document.forms[1].billingservice_date.value.length<10) {
	            b = false;
	            alert ("You need to select a date from the calendar.");
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
            
            function setFlag(){
                if( document.getElementById("gstCheck").checked == true){
                    document.getElementById("gstFlag").value = "1";
                } else {
                    document.getElementById("gstFlag").value = "0";
                }
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
	<form method="post" name="baseur0"
		action="billingONEditPrivateCode.jsp">
	<tr>
		<td align="right" width="70%"><select name="service_code"
			id="service_code">
			<option selected="selected" value="">- choose one -</option>
			<%//
				List sL = dbObj.getPrivateBillingCodeDesc();
				String strCode = "";
				String strDesc = "";
				for (int i = 0; i < sL.size(); i = i + 2) {
					try {
						strCode = ((String) sL.get(i)).substring(1);
					} catch (NullPointerException e) {
						strCode = "";
						MiscUtils.getLogger().warn("NULL value set for a private billing code");
					}
					
					try {
						strDesc = (String) sL.get(i+1);
						strDesc = strDesc.length() > 30 ? strDesc.substring(0,30): strDesc;
					} catch (NullPointerException e) {
						strDesc = "";
						MiscUtils.getLogger().warn("NULL value set for a private billing code description (code is '"+strCode+"')");
					}
				%>
			<option value="<%=strCode%>"><%=(strCode + "| " + strDesc)%></option>
			<%}

				%>
		</select></td>
		<td><input type="hidden" name="submit" value="Search"> <input
			type="submit" name="action" value=" Edit "></td>
	</tr>
	</form>
</table>

<form method="post" name="baseurl" action="billingONEditPrivateCode.jsp">
<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<tr class="myGreen">
		<td align="right"><b><font color="red">Private</font> Code</b></td>
		<td>_<input type="text" name="service_code"
			value="<%=prop.getProperty("service_code", "?").substring(1)%>"
			size='8' maxlength='10' onblur="upCaseCtrl(this)" /> (e.g. O001A) <input
			type="submit" name="submit" value="Search"
			onclick="javascript:return onSearch();"></td>
	</tr>
	<tr>
		<td align="right"><b>Description</b></td>
		<td><input type="text" name="description"
			value="<%=prop.getProperty("description", "")%>" size='50'></td>
	</tr>
	<tr class="myGreen">
		<td align="right"><b>Fee</b></td>
		<td><input type="text" name="value"
			value="<%=prop.getProperty("value", "")%>" size='8' maxlength='8'>Add
		GST<input type="checkbox" name="gstCheck" id="gstCheck"
			onclick="setFlag()" /> (format: xx.xx, e.g. 18.20)
		<input type="hidden" value="" id="gstFlag" name="gstFlag" /></td>
	</tr>
	<tr>
		<td align="right"><b>Issued Date</b></td>
		<td><input type="text" name="billingservice_date"
			id="billingservice_date"
			value="<%=prop.getProperty("billingservice_date", "")%>" size='10'
			maxlength='10' readonly> (effective date) <img
			src="../../../images/cal.gif" id="billingservice_date_cal"></td>
	</tr>
	<tr class="myGreen">
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td><input type="submit" name="submit" value="Delete"
			onclick="javascript:return onDelete();"></td>
		<td align="center"><input type="hidden" name="action"
			value='<%=action%>'> <input type="submit" name="submit"
			value="<bean:message key="admin.resourcebaseurl.btnSave"/>"
			onclick="javascript:return onSave();"> <input type="button"
			name="Cancel"
			value="<bean:message key="admin.resourcebaseurl.btnExit"/>"
			onClick="window.close()"></td>
	</tr>
</table>
</form>
</body>
<script type="text/javascript">
Calendar.setup( { inputField : "billingservice_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "billingservice_date_cal", singleClick : true, step : 1 } );
</script>
</html:html>

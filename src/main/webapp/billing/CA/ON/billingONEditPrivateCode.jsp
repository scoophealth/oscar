<!DOCTYPE html>
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

<%@ page errorPage="../appointment/errorpage.jsp" import="java.util.*,java.sql.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.oscarehr.util.MiscUtils"%>
<%//
			//int serviceCodeLen = 5;
			String msg = "Type in a service code and search first to see if it is available.";
			String alert = "info";
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
									+ " is not added. Action failed! Try edit it again.";
							action = "add" + serviceCode;
							prop.setProperty("service_code", serviceCode);
							String description = request.getParameter("description");
							if (description == null)
								description = "";
							prop.setProperty("description", description);
							prop.setProperty("value", request.getParameter("value"));
							prop.setProperty("billingservice_date", request.getParameter("billingservice_date"));
                                                        prop.setProperty("gstFlag", request.getParameter("gstFlag"));
							alert = "error";
						}
					} else {
						msg = "You can not save the service code - " + serviceCode
								+ ". Please search the service code first.";
						action = "search";
						prop.setProperty("service_code", serviceCode);
						alert = "error";
					}
				} else {
					msg = "You can not save the service code. Please search the service code first.";
					alert = "error";
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

<!DOCTYPE html>
<html:html locale="true">
<head>
<title><bean:message key="admin.admin.managePrivBillingCode"/></title>

	<link href="<%=request.getContextPath() %>/css/bootstrap.css" rel="stylesheet">
	<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet">


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
<body>

<h3><bean:message key="admin.admin.managePrivBillingCode"/></h3>

<div class="container-fluid">



<div class="well">
<form method="post" name="baseur0" action="billingONEditPrivateCode.jsp" class="form-inline">

Select Code to edit:<br>
	<select name="service_code" id="service_code" required>
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
	</select>
	<input type="hidden" name="submit" value="Search"> 
	<input class="btn" type="submit" name="action" value="Edit">
</form>
</div><!--select code to edit well-->

<div class="well">
<form method="post" name="baseurl" action="billingONEditPrivateCode.jsp">

<div class="alert alert-<%=alert%>">
  <%=msg%>
</div>

Private Code_ <small>(e.g. O001A)</small><br>
<div class="input-append">
<input type="text" name="service_code" value="<%=prop.getProperty("service_code", "?").substring(1)%>" class="span2" maxlength='10' onblur="upCaseCtrl(this)" required/> 
<button type="submit" name="submit" class="btn btn-primary" onclick="javascript:return onSearch();" value="Search">Search</button>
</div>

<br>

Description<br>
<input type="text" name="description" value="<%=prop.getProperty("description", "")%>" size='50'><br>

Fee <small>(format: xx.xx, e.g. 18.20)</small><br>
<input type="text" name="value" value="<%=prop.getProperty("value", "")%>" size='8' maxlength='8'> <br>

<input type="checkbox" name="gstCheck" id="gstCheck" onclick="setFlag()" /> Add GST <br>

<input type="hidden" value="" id="gstFlag" name="gstFlag" />

<br>

Issued Date <small>(effective date)</small><br>

<div class="input-append date" id="billingservice_date" data-date="2014-02-04" data-date-format="yyyy-mm-dd">
<input  style="width:90px" name="billingservice_date"  id="billingservice_date" size="16" type="text" value="" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" readonly>
<span class="add-on"><i class="icon-calendar"></i></span>
</div>


<br>
<input class="btn" type="submit" name="submit" value="Delete" onclick="javascript:return onDelete();">
<input type="hidden" name="action" value='<%=action%>'> 
<input class="btn" type="submit" name="submit" value="<bean:message key="admin.resourcebaseurl.btnSave"/>" onclick="javascript:return onSave();"> 
</form>
</div><!--edit/add well-->

</div>

	<script src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script> 
	<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>	
	<script src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>

</body>
<script type="text/javascript">
$(function (){  
	$('#billingservice_date').datepicker();
});
</script>
</html:html>

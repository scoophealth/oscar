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
<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../logout.jsp");
  }
%>
  <%@ page errorPage="../appointment/errorpage.jsp"import="java.util.*,
                                                           java.sql.*,
                                                           oscar.*,
                                                           java.text.*,
                                                           java.lang.*,
                                                           java.net.*" %>
  <%@ page import="oscar.oscarBilling.ca.on.data.BillingONDataHelp" %>
  <%@ page import="org.apache.commons.lang.StringEscapeUtils" %>
<%
  int serviceCodeLen = 5;
  String msg = "Type in a service code and search first to see if it is available.";
  String action = "search"; // add/edit
  //BillingServiceCode serviceCodeObj = new BillingServiceCode();
  Properties	prop  = new Properties();
  BillingONDataHelp dbObj = new BillingONDataHelp();
  if (request.getParameter("submit") != null && request.getParameter("submit").equals("Save")) {
    // check the input data

    if(request.getParameter("action").startsWith("edit")) {
      	// update the service code
		String serviceCode = request.getParameter("service_code");
		if(serviceCode.equals(request.getParameter("action").substring("edit".length()))) {
			String	sql   = "update billingservice set description='" + StringEscapeUtils.escapeSql(request.getParameter("description")) + "', ";
			sql += " value='" + request.getParameter("value") + "', ";
			sql += " percentage='" + request.getParameter("percentage") + "', ";
			sql += " billingservice_date='" + request.getParameter("billingservice_date") + "' ";
			sql += "where service_code='" + serviceCode + "'";
			System.out.println(sql);
			if(dbObj.updateDBRecord(sql)) {
	  			msg = serviceCode + " is updated.<br>" + "Type in a service code and search first to see if it is available.";
	  			action = "search";
			    prop.setProperty("service_code", serviceCode);
			} else {
	  			msg = serviceCode + " is <font color='red'>NOT</font> updated. Action failed! Try edit it again." ;
			    action = "edit" + serviceCode;
			    prop.setProperty("service_code", serviceCode);
			    prop.setProperty("description", request.getParameter("description"));
			    prop.setProperty("value", request.getParameter("value"));
			    prop.setProperty("percentage", request.getParameter("percentage"));
			    prop.setProperty("billingservice_date", request.getParameter("billingservice_date"));
			}
		} else {
      		msg = "You can <font color='red'>NOT</font> save the service code - " + serviceCode + ". Please search the service code first.";
  			action = "search";
		    prop.setProperty("service_code", serviceCode);
		}
    } else if (request.getParameter("action").startsWith("add")) {
      	// insert into the service code
		String serviceCode = request.getParameter("service_code");
		if(serviceCode.equals(request.getParameter("action").substring("add".length()))) {
			String	sql   = "insert into billingservice (service_compositecode, service_code, description, value, percentage, billingservice_date) values ('', '";
			sql += serviceCode + "', '";
			sql += request.getParameter("description") + "', '";
			sql += request.getParameter("value") + "', '";
			sql += request.getParameter("percentage") + "', '";
			sql += request.getParameter("billingservice_date") + "' )";
			if(dbObj.updateDBRecord(sql)) {
	  			msg = serviceCode + " is added.<br>" + "Type in a service code and search first to see if it is available.";
	  			action = "search";
			    prop.setProperty("service_code", serviceCode);
			} else {
	  			msg = serviceCode + " is <font color='red'>NOT</font> added. Action failed! Try edit it again." ;
			    action = "add" + serviceCode;
			    prop.setProperty("service_code", serviceCode);
			    prop.setProperty("description", request.getParameter("description"));
			    prop.setProperty("value", request.getParameter("value"));
			    prop.setProperty("percentage", request.getParameter("percentage"));
			    prop.setProperty("billingservice_date", request.getParameter("billingservice_date"));
			}
		} else {
      		msg = "You can <font color='red'>NOT</font> save the service code - " + serviceCode + ". Please search the service code first.";
  			action = "search";
		    prop.setProperty("service_code", serviceCode);
		}
    } else {
      msg = "You can <font color='red'>NOT</font> save the service code. Please search the service code first.";
    }
  } else if (request.getParameter("submit") != null && request.getParameter("submit").equals("Search")) {
    // check the input data
    if(request.getParameter("service_code") == null || request.getParameter("service_code").length() != serviceCodeLen) {
      msg = "Please type in a right service code.";
    } else {
        String serviceCode = request.getParameter("service_code");
		String	sql   = "select * from billingservice where service_code='" + serviceCode + "'";
		ResultSet rs = dbObj.searchDBRecord(sql);

		if (rs.next()) {
		    prop.setProperty("service_code", serviceCode);
		    prop.setProperty("description", rs.getString("description"));
		    prop.setProperty("value", rs.getString("value"));
		    prop.setProperty("percentage", rs.getString("percentage"));
		    prop.setProperty("billingservice_date", rs.getString("billingservice_date"));
		    msg = "You can edit the service code.";
		    action = "edit" + serviceCode;
		} else {
		    prop.setProperty("service_code", serviceCode);
		    msg = "It is a NEW service code. You can add it.";
		    action = "add" + serviceCode;
		}
	}
  }
%>
  <%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
  <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
  <html:html locale="true">
    <head>
      <title>
        Add/Edit Service Code
      </title>
      <meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
      <meta http-equiv="Cache-Control" content="no-cache">
      <LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
      <!-- calendar stylesheet -->
      <link rel="stylesheet" type="text/css" media="all" href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
      <!-- main calendar program -->
      <script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
      <!-- language for the calendar -->
      <script type="text/javascript" src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>">
              </script>
      <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
      <script type="text/javascript" src="../../../share/calendar/calendar-setup.js"></script>
      <script language="JavaScript">

      <!--
		function setfocus() {
		  this.focus();
		  document.forms[0].service_code.focus();
		  document.forms[0].service_code.select();
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
	        if(document.forms[0].service_code.value.length!=5){
	            b = false;
	            alert ("You must type in a service code with 5 letters/digits. The service code ends with \'A\' or \'B\'...");
	        }
	        return b;
	    }
		function checkAllFields() {
	        var b = true;
	        if(document.forms[0].value.value.length>0 && document.forms[0].percentage.value.length>0){
	            b = false;
	            alert ("You can NOT type in a fee and a percentage at the same time. (leave one blank)");
	        } else if(document.forms[0].value.value.length>0) {
		        if(!isNumber(document.forms[0].value.value)){
		            b = false;
		            alert ("You must type in a number in the field fee");
		        }
	        } else if(document.forms[0].percentage.value.length>0) {
		        if(!isNumber(document.forms[0].percentage.value)){
		            b = false;
		            alert ("You must type in a number in the field percentage");
		        } else if(document.forms[0].percentage.value > 1){
		            b = false;
		            alert ("The percentage should be less than 1.");
		        }
	        } else if(document.forms[0].value.value.length==0 && document.forms[0].percentage.value.length==0) {
	            b = false;
	            alert ("You must type in a number in the field fee");
	        }

			if(document.forms[0].billingservice_date.value.length<10) {
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
//-->

      </script>
    </head>
    <body bgcolor="ivory" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
      <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
        <tr>
          <td align="left">
            &nbsp;
          </td>
        </tr>
      </table>

      <center>
      <table BORDER="1" CELLPADDING="0" CELLSPACING="0" WIDTH="80%">
        <tr BGCOLOR="#CCFFFF">
          <th>
            <%=msg%>
          </th>
        </tr>
      </table>
      </center>
      <table width="100%" border="0" cellspacing="2" cellpadding="2">
        <form method="post" name="baseurl" action="addEditServiceCode.jsp">
          <tr>
            <td>
              &nbsp;
            </td>
          </tr>
          <tr bgcolor="#EEEEFF">
            <td align="right">
              <b>Service Code</b>
            </td>
            <td>
              <input type="text" name="service_code" value="<%=prop.getProperty("service_code", "")%>" size='4' maxlength='5'>
              (5 letters, e.g. A001A)
              <input type="submit" name="submit" value="Search" onclick="javascript:return onSearch();">
            </td>
          </tr>
          <tr>
            <td align="right">
              <b>Description</b>
            </td>
            <td>
              <input type="text" name="description" value="<%=prop.getProperty("description", "")%>" size='50' maxlength='50'>
              (50 letters)
            </td>
          </tr>
          <tr bgcolor="#EEEEFF">
            <td align="right">
              <b>Fee</b>
            </td>
            <td>
              <input type="text" name="value" value="<%=prop.getProperty("value", "")%>" size='8' maxlength='8'>
              (format: xx.xx, e.g. 18.20)
            </td>
          </tr>
          <tr>
            <td align="right">
              <b>Percentage</b>
            </td>
            <td>
              <input type="text" name="percentage" value="<%=prop.getProperty("percentage", "")%>" size='8' maxlength='8'>
              (format: 0.xx, e.g. 0.20)
            </td>
          </tr>
          <tr bgcolor="#EEEEFF">
            <td align="right">
              <b>Issued Date</b>
            </td>
            <td>
              <input type="text" name="billingservice_date" value="<%=prop.getProperty("billingservice_date", "")%>" size='10' maxlength='10' readonly>
              (effective date)
              <img src="../../../images/cal.gif" id="billingservice_date_cal">
            </td>
          </tr>
          <tr>
            <td>
              &nbsp;
            </td>
            <td>
              &nbsp;
            </td>
          </tr>
          <tr>
            <td align="center" bgcolor="#CCCCFF" colspan="2">
              <input type="hidden" name="action" value='<%=action%>'>
              <input type="submit" name="submit" value="<bean:message key="admin.resourcebaseurl.btnSave"/>" onclick="javascript:return onSave();">
              <input type="button" name="Cancel" value="<bean:message key="admin.resourcebaseurl.btnExit"/>" onClick="window.close()">
            </td>
          </tr>
        </form>
      </table>
    </body>
    <script type="text/javascript">
Calendar.setup( { inputField : "billingservice_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "billingservice_date_cal", singleClick : true, step : 1 } );
</script>
  </html:html>

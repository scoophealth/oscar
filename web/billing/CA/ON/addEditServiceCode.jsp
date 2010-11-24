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
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */
-->
<%
  if (session.getAttribute("user") == null) {
    response.sendRedirect("../logout.jsp");
  }
%>
<%@ page errorPage="../../../appointment/errorpage.jsp"
	import="java.util.*,
                                                           java.sql.*,
                                                           oscar.*,
                                                           java.text.*,
                                                           java.lang.*,
                                                           java.net.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.BillingONDataHelp"%>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
  int serviceCodeLen = 5;
  String msg = "Type in a service code and search first to see if it is available.";
  String action = "search"; // add/edit
  String action2 = ""; //add a new record even if code already exists
  //BillingServiceCode serviceCodeObj = new BillingServiceCode();
  Properties	prop  = new Properties();
  LinkedHashMap codes = new LinkedHashMap();
  BillingONDataHelp dbObj = new BillingONDataHelp();
  if (request.getParameter("submitFrm") != null && (request.getParameter("submitFrm").equals("Save") || request.getParameter("submitFrm").equalsIgnoreCase("Add Service Code"))) {
    // check the input data
    // if input the perc code,
    String valuePara = request.getParameter("value");
    if(request.getParameter("percentage").length()>0 && valuePara.trim().equals("") ) {
    	valuePara = ".00";
    }

    if(request.getParameter("action").startsWith("edit")) {
      	// update the service code
		String serviceCode = request.getParameter("service_code");
                String billingservice_no = request.getParameter("billingservice_no");
		if(serviceCode.equals(request.getParameter("action").substring("edit".length()))) {
			String	sql   = "update billingservice set description='" + StringEscapeUtils.escapeSql(request.getParameter("description")) + "', ";
			sql += " value='" + valuePara + "', ";
			sql += " percentage='" + request.getParameter("percentage") + "', ";
			sql += " billingservice_date='" + request.getParameter("billingservice_date") + "', ";
                        sql += " termination_date='" + request.getParameter("termination_date") + "' ";
                        if( billingservice_no == null ) {
                            sql += " where service_code = '" + serviceCode + "'";
                        }
                        else {
                            sql += " where billingservice_no = " + billingservice_no;
                        }
	
			if(request.getParameter("percentage").length()>1 && request.getParameter("min").length()>1 && request.getParameter("max").length()>1) {
				String sqlMinMax = "select * from billingperclimit where service_code='" + serviceCode + "'";
				boolean bAdd = true;
				ResultSet rs1 = dbObj.searchDBRecord(sqlMinMax);
				if (rs1.next()) {
					bAdd = false;
				}
				if(bAdd) {
					sqlMinMax = "insert into billingperclimit (service_code,min,max,effective_date) values('";
					sqlMinMax += serviceCode + "', '";
					sqlMinMax += request.getParameter("min") + "', '";
					sqlMinMax += request.getParameter("max") + "','";
                                        sqlMinMax += request.getParameter("billingservice_date") + "' )";
					dbObj.updateDBRecord(sqlMinMax);
				} else {
					sqlMinMax = "update billingperclimit set min='";
					sqlMinMax += request.getParameter("min") + "', max='";
					sqlMinMax += request.getParameter("max") + "' where service_code='";
					sqlMinMax += serviceCode + "' and effective_date = '";
                                        sqlMinMax += request.getParameter("billingservice_date") + "'";                                        
					dbObj.updateDBRecord(sqlMinMax);
				}
			}
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
			String	sql   = "insert into billingservice (service_compositecode, service_code, description, value, percentage, billingservice_date,specialty,region,anaesthesia, termination_date) values ('', '";
			sql += serviceCode + "', '";
			sql += request.getParameter("description") + "', '";
			sql += valuePara + "', '";
			sql += request.getParameter("percentage") + "', '";
			sql += request.getParameter("billingservice_date") + "',";
                        sql += "'','ON','00', '" + request.getParameter("termination_date") + "')";
			if(request.getParameter("percentage").length()>1 && request.getParameter("min").length()>1 && request.getParameter("max").length()>1) {
				String sqlMinMax = "insert into billingperclimit (service_code, min, max, effective_date) values('";
				sqlMinMax += serviceCode + "', '";
				sqlMinMax += request.getParameter("min") + "', '";
				sqlMinMax += request.getParameter("max") + "', '";
                                sqlMinMax += request.getParameter("billingservice_date") +"' )";
				dbObj.updateDBRecord(sqlMinMax);
			}
			if(dbObj.updateDBRecord(sql) ) {
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
			    prop.setProperty("min", request.getParameter("min"));
			    prop.setProperty("max", request.getParameter("max"));
			}
		} else {
      		msg = "You can <font color='red'>NOT</font> save the service code - " + serviceCode + ". Please search the service code first.";
  			action = "search";
		    prop.setProperty("service_code", serviceCode);
		}
    } else {
      msg = "You can <font color='red'>NOT</font> save the service code. Please search the service code first.";
    }
  } else if (request.getParameter("submitFrm") != null && request.getParameter("submitFrm").equals("Search")) {
    // check the input data
    if(request.getParameter("service_code") == null || request.getParameter("service_code").length() != serviceCodeLen) {
      msg = "Please type in a right service code.";
    } else {
        String serviceCode = request.getParameter("service_code");                
                
		String	sql   = "select * from billingservice where service_code='" + serviceCode + "'  order by billingservice_date desc";
		ResultSet rs = dbObj.searchDBRecord(sql);
                String tmp;
                int count = 0;
                
		while (rs.next()) {
                    ++count;
                    codes.put(rs.getString("billingservice_date"), rs.getString("billingservice_no"));
                    if( count == 1 ) {
                        prop.setProperty("service_code", serviceCode);
                        tmp = rs.getString("description") == null ? "" : rs.getString("description");
                        prop.setProperty("description", tmp);
                        tmp = rs.getString("value") == null ? "" : rs.getString("value");
                        prop.setProperty("value", tmp);
                        tmp = rs.getString("percentage") == null ? "" : rs.getString("percentage");
                        prop.setProperty("percentage", tmp);                    
                        tmp = rs.getString("billingservice_date") == null ? "" : rs.getString("billingservice_date");
                        prop.setProperty("billingservice_date", tmp);
                        tmp = rs.getString("termination_date") == null ? "" : rs.getString("termination_date");
                        prop.setProperty("termination_date", tmp);
                        msg = "You can edit the service code by clicking 'Save' or add a new entry for this code by clicking 'Add Service Code'";
                        action = "edit" + serviceCode;
                        action2 = "add" + serviceCode;

		    String sqlMinMax = "select * from billingperclimit where service_code='" + serviceCode + "' and effective_date = '" + prop.getProperty("billingservice_date") + "'";;
			ResultSet rs2 = dbObj.searchDBRecord(sqlMinMax);
			if (rs2.next()) {
                            prop.setProperty("min", rs2.getString("min"));
                            prop.setProperty("max", rs2.getString("max"));
			}
                    }
                }

		if( count == 0 ) {
		    prop.setProperty("service_code", serviceCode);
		    msg = "It is a NEW service code. You can add it.";
		    action = "add" + serviceCode;
		}
	}
  }
  else if( request.getParameter("action") != null && request.getParameter("action").startsWith("single") ) {
      String serviceCode = request.getParameter("service_code");
      
      int serviceNo = Integer.parseInt(request.getParameter("billingservice_no"));
      String sql = "select * from billingservice where service_code = '" + serviceCode + "'  order by billingservice_date desc";
      ResultSet rs = dbObj.searchDBRecord(sql);
      
      String tmp;
    int count = 0;

    while (rs.next()) {
        ++count;
        codes.put(rs.getString("billingservice_date"), rs.getString("billingservice_no"));
        if( serviceNo == rs.getInt("billingservice_no") ) {
            prop.setProperty("service_code", serviceCode);
            tmp = rs.getString("description") == null ? "" : rs.getString("description");
            prop.setProperty("description", tmp);
            tmp = rs.getString("value") == null ? "" : rs.getString("value");
            prop.setProperty("value", tmp);
            tmp = rs.getString("percentage") == null ? "" : rs.getString("percentage");
            prop.setProperty("percentage", tmp);                    
            tmp = rs.getString("billingservice_date") == null ? "" : rs.getString("billingservice_date");
            prop.setProperty("billingservice_date", tmp);
            tmp = rs.getString("termination_date") == null ? "" : rs.getString("termination_date");
            prop.setProperty("termination_date", tmp);
            msg = "You can edit the service code by clicking 'Save' or add a new entry for this code by clicking 'Add Service Code'";
            action = "edit" + serviceCode;
            action2 = "add" + serviceCode;

        String sqlMinMax = "select * from billingperclimit where service_code='" + serviceCode + "' and effective_date = '" + prop.getProperty("billingservice_date") + "'";

            ResultSet rs2 = dbObj.searchDBRecord(sqlMinMax);
            if (rs2.next()) {
                prop.setProperty("min", rs2.getString("min"));
                prop.setProperty("max", rs2.getString("max"));
            }
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
<LINK REL="StyleSheet" HREF="../web.css" TYPE="text/css">
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
		        if(document.forms[0].min.value.length>0 && document.forms[0].max.value.length>0) {
					if(!isNumber(document.forms[0].min.value) || !isNumber(document.forms[0].max.value)){
		            	b = false;
		            	alert ("You must type in a number in the min/max fields.");
		        	}
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
	    function upCaseCtrl(ctrl) {
			ctrl.value = ctrl.value.toUpperCase();
		}

                
                function fetchBillService(billno) {
                    document.getElementById('action').value="single" + billno;
                    var frm = document.getElementById("baseurl");
                    frm.submit();                    
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
<form method="post" id="baseurl" name="baseurl" action="addEditServiceCode.jsp">
<table width="100%" border="0" cellspacing="2" cellpadding="2">
	<tr>
		<td>&nbsp;</td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td align="right"><b>Service Code</b></td>
		<td><input type="text" name="service_code"
			value="<%=prop.getProperty("service_code", "")%>" size='4'
			maxlength='5' onblur="upCaseCtrl(this)" /> (5 letters, e.g. A001A) <input
			type="submit" name="submitFrm" value="Search"
			onclick="javascript:return onSearch();"></td>
	</tr>
        <%
            if( codes.size() > 1 ) {
                Set dates = codes.keySet();
                Iterator<String> i = dates.iterator();    
                String date;
        %>                        
        <tr>
            <td align="right"><b>Edit Entry</b></td>
            <td>
                <select name="billingservice_no" onchange="fetchBillService(this.options[this.selectedIndex].value);">
                    <%
                        while( i.hasNext() ) {
                            date = i.next();
                    %>
                            <option value="<%=codes.get(date)%>" <%=prop.getProperty("billingservice_date", "").equalsIgnoreCase(date) ? "selected" : ""%>><%=date%>
                       <%}%>
                </select>
            </td>
        </tr>
        <%}%>
	<tr>
		<td align="right"><b>Description</b></td>
		<td><input type="text" name="description"
			value="<%=prop.getProperty("description", "")%>" size='50'
			maxlength='50'> (50 letters)</td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td align="right"><b>Fee</b></td>
		<td><input type="text" name="value"
			value="<%=prop.getProperty("value", "")%>" size='8' maxlength='8'>
		(format: xx.xx, e.g. 18.20)</td>
	</tr>
	<tr>
		<td align="right"><b>Percentage</b></td>
		<td><input type="text" name="percentage"
			value="<%=prop.getProperty("percentage", "")%>" size='8'
			maxlength='8'> (format: 0.xx, e.g. 0.20) min. <input
			type="text" name="min" value="<%=prop.getProperty("min", "")%>"
			size='7' maxlength='8'> max.<input type="text" name="max"
			value="<%=prop.getProperty("max", "")%>" size='7' maxlength='8'>
		</td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td align="right"><b>Issued Date</b></td>
		<td><input type="text" name="billingservice_date"
			id="billingservice_date"
			value="<%=prop.getProperty("billingservice_date", "")%>" size='10'
			maxlength='10' readonly> (effective date) <img
			src="../../../images/cal.gif" id="billingservice_date_cal"></td>
	</tr>
    <tr bgcolor="#EEEEFF">
		<td align="right"><b>Termination Date</b></td>
		<td><input type="text" name="termination_date"
			id="termination_date"
			value="<%=prop.getProperty("termination_date", "9999-12-31")%>" size='10'
			maxlength='10' readonly> (stale date) <img
			src="../../../images/cal.gif" id="termination_date_cal"></td>
	</tr>
	<tr>
		<td>&nbsp;</td>
		<td>&nbsp;</td>
	</tr>
	<tr>
		<td align="center" bgcolor="#CCCCFF" colspan="2"><input
			type="hidden" id="action" name="action" value=''> <input
			type="submit" name="submitFrm"
			value="<bean:message key="admin.resourcebaseurl.btnSave"/>"
			onclick="document.getElementById('action').value='<%=action%>';return onSave();"> 
                        <%
                        if( !action2.equals("") ) {
                        %>                        
                            <input type="submit" name="submitFrm" value="<bean:message key="admin.resourcebaseurl.btnAdd"/>" 
                            onclick="document.getElementById('action').value='<%=action2%>';return onSave();">
                       <%}%>
                        <input type="button"
			name="Cancel"
			value="<bean:message key="admin.resourcebaseurl.btnExit"/>"
			onClick="window.close()"></td>
	</tr>
</table>
</form>
</body>
<script type="text/javascript">
Calendar.setup( { inputField : "billingservice_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "billingservice_date_cal", singleClick : true, step : 1 } );
Calendar.setup( { inputField : "termination_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "termination_date_cal", singleClick : true, step : 1 } );
</script>
</html:html>

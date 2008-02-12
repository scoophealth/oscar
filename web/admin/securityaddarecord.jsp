<!--
/*
 *
 * Copyright (c) 2005 Department of Family Medicine, McMaster University. All Rights Reserved. *
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
 */
-->
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin" rights="r" reverse="<%=true%>" >
<%response.sendRedirect("../logout.jsp");%>
</security:oscarSec>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ page import="java.sql.*, java.util.*, oscar.OscarProperties" errorPage="errorpage.jsp" %>
<%
  //if(session.getAttribute("user") == null && session.getValue("user").equals("admin") )  response.sendRedirect("../logout.jsp");
%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>
<%
  String [][] dbQueries=new String[][] {
    {"provider_search_providerno", "select provider_no, first_name, last_name from provider order by last_name"},
  };
  String[][] responseTargets=new String[][] {  };
  apptMainBean.doConfigure(dbParams,dbQueries,responseTargets);
%>

<html:html locale="true">
<head>
<meta http-equiv="Cache-Control" content="no-cache" />
<title><bean:message key="admin.securityaddarecord.title"/></title>
  <!-- calendar stylesheet -->
  <link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

  <!-- main calendar program -->
  <script type="text/javascript" src="../share/calendar/calendar.js"></script>

  <!-- language for the calendar -->
  <script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

  <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
  <script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<link rel="stylesheet" href="../web.css">
<script LANGUAGE="JavaScript">
    <!--

		function setfocus() {
		  document.searchprovider.user_name.focus();
		  document.searchprovider.user_name.select();
		}

    function onsub() {
      if(document.searchprovider.user_name.value=="" ||
		 document.searchprovider.password.value==""||
		 document.searchprovider.provider_no.value==""		
		) {
        alert('<bean:message key="global.msgInputKeyword"/>');
        return false;
      } else if(document.forms[0].b_ExpireSet.checked && document.forms[0].date_ExpireDate.value.length<10) {
        alert('<bean:message key="global.msgInputKeyword"/>');
        return false;
      } else if(document.forms[0].b_RemoteLockSet.checked && document.forms[0].pin.value.length<3) {
        alert('<bean:message key="global.msgInputKeyword"/>');
        return false;
      } else if(document.forms[0].b_LocalLockSet.checked && document.forms[0].pin.value.length<3) {
        alert('<bean:message key="global.msgInputKeyword"/>');
        return false;
      }else if(document.forms[0].password.value != document.forms[0].conPassword.value) {
        alert('You have not confirmed your password. Please input your password again.');
        return false;
      } else if(document.forms[0].pin.value != document.forms[0].conPin.value) {
        alert('You have not confirmed your pin. Please input your pin again.');
        return false;
      } else 
      	return true;
      // do nothing at the moment
      // check input data in the future
     }
    //-->
    </script>
</head>

<body background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<center>
    <table border="0" cellspacing="0" cellpadding="0" width="100%" >
      <tr bgcolor="#486ebd">
            <th align="CENTER"><font face="Helvetica" color="#FFFFFF"><bean:message key="admin.securityaddarecord.description"/></font></th>
      </tr>
    </table>
    <form method="post" action="admincontrol.jsp" name="searchprovider" onsubmit="return onsub()">
        
  <table cellspacing="0" cellpadding="2" width="90%" border="0">
      <tr>
        <td>
          <div align="right"><bean:message key="admin.securityrecord.formUserName"/><font color="red">:</font> </div>
        </td>
        <td>
          <input type="text" name="user_name" size="20" maxlength="30">
        </td>
      </tr>
      <tr>
        <td>
          <div align="right"><bean:message key="admin.securityrecord.formPassword"/><font color="red">:</font> </div>
        </td>
        <td>
          <input type="password" name="password" size="20" maxlength="80" >
        </td>
      </tr>
      <tr>
        <td>    
            <div align="right">Confirm:</div>
        </td>
        <td>
          <input type="password" name="conPassword" size="20" maxlength="80">
        </td>
      </tr>
      <tr>
        <td width="50%" align="right"><bean:message key="admin.securityrecord.formProviderNo"/><font color="red"> </font>:
        </td>
        <td>
                <select name="provider_no">
                  <option value="">---None---</option>
<%
   ResultSet rsgroup = apptMainBean.queryResults("provider_search_providerno");
 	 while (rsgroup.next()) {
%>
                  <option value="<%=rsgroup.getString("provider_no")%>"><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
   apptMainBean.closePstmtConn();

%>
                </select>
          <!--input type="text" name="provider_no" -->
        </td>
      </tr>
<!-- new security -->
      <tr>
        <td  align="right" nowrap>Expiry Date:</td>
        <td>
          <input type="checkbox" name="b_ExpireSet" value="1" <%="checked" %>" />
          Date:
          <input type="text" name="date_ExpireDate" id="date_ExpireDate" value="" size="10" readonly />
		  <img src="../images/cal.gif" id="date_ExpireDate_cal"/>
        </td>
      </tr>
      
      <% if (OscarProperties.getInstance().getBooleanProperty("NEW_USER_PIN_CONTROL","yes")){%>
      <tr>
        <td align="right" nowrap>Pin(remote) Enable:</td>
        <td>
          <input type="checkbox" name="b_RemoteLockSet" value="1" <%="checked" %>" />
          Pin(local) Enable:
          <input type="checkbox" name="b_LocalLockSet" value="1" />
        </td>
      </tr>
      <%}else{%>
        <input type="hidden" name="b_RemoteLockSet" value="1" />
        <input type="hidden" name="b_LocalLockSet" value="1" />
      <%}%>  
<!-- new security -->
      <tr>
        <td>
          <div align="right"><bean:message key="admin.securityrecord.formPIN"/>: </div>
        </td>
        <td>
          <input type="password" name="pin" size="6" maxlenght="6"/>
          <font size="-1">more than 3 digits<!-- bean:message key="admin.securityaddarecord.onlyInternet"/--></font> 
          Confirm:<input type="password" name="conPin" size="6" maxlength="6"/></td>
      </tr>
      <tr>
        <td colspan="2">
          <div align="center">
            <input type="hidden" name="dboperation" value="security_add_record">
            <input type="hidden" name="displaymode" value="Security_Add_Record">
            <input type="submit" name="subbutton" value='<bean:message key="admin.securityaddarecord.btnSubmit"/>'>
          </div>
        </td>
      </tr>
  </table>
    </form>

  <p></p>
  <hr width="100%" color="orange">
  <table border="0" cellspacing="0" cellpadding="0" width="100%">
    <tr>
      <td><a href="admin.jsp"> <img src="../images/leftarrow.gif" border="0" width="25" height="20" align="absmiddle"><bean:message key="global.btnBack"/></a></td>
      <td align="right"><a href="../logout.jsp"><bean:message key="global.btnLogout"/><img src="../images/rightarrow.gif"  border="0" width="25" height="20" align="absmiddle"></a></td>
    </tr>
  </table>

</center>
<script type="text/javascript">
Calendar.setup({ inputField : "date_ExpireDate", ifFormat : "%Y-%m-%d", showsTime :false, button : "date_ExpireDate_cal" });
</script>
</body>
</html:html>

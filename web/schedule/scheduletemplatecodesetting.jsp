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
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="scheduleMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="dataBean" class="java.util.Properties" scope="page" />
<% //save or delete the settings
  int rowsAffected = 0;
  if(request.getParameter("dboperation")!=null && request.getParameter("dboperation").compareTo(" Save ")==0 ) {
    String[] param1 =new String[4];
    param1[0]=request.getParameter("code");
    param1[1]=request.getParameter("description");
    param1[2]=request.getParameter("duration");
    param1[3]=request.getParameter("color");
    rowsAffected = scheduleMainBean.queryExecuteUpdate(request.getParameter("code"),"delete_scheduletemplatecode");
    rowsAffected = scheduleMainBean.queryExecuteUpdate(param1,"add_scheduletemplatecode");
  }
  if(request.getParameter("dboperation")!=null && request.getParameter("dboperation").equals("Delete") ) {
    rowsAffected = scheduleMainBean.queryExecuteUpdate(request.getParameter("code"),"delete_scheduletemplatecode");
  }
%>

<html>
<head>
<title>TEMPLATE CODE SETTING</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.addtemplatecode.code.focus();
  document.addtemplatecode.code.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function checkInput() {
	if(document.schedule.holiday_name.value == "") {
	  alert("Please check your input!!!");
	  return false;
	} else {
	  return true;
	}
}
//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

  <table border="0" width="100%">
    <tr> 
      <td width="50" bgcolor="#009966">&nbsp;</td>
      <td align="center">
	  
        <table width="100%" border="0" cellspacing="0" cellpadding="5">
<form name="deletetemplatecode" method="post" action="scheduletemplatecodesetting.jsp">
          <tr bgcolor="#CCFFCC"> 
            <td> 
              <p align="right">Template Code: </p>
            </td>
            <td>
      <select name="code" >
	<%
   ResultSet rsdemo = null;
   rsdemo = scheduleMainBean.queryResults("code", "search_scheduletemplatecode");
   while (rsdemo.next()) { 
	%>
        <option value="<%=rsdemo.getString("code")%>"><%=rsdemo.getString("code")+" |"+rsdemo.getString("description")%></option>
  <%
     }
	%>
      </select>
            <td><input type="submit" name="dboperation" value=" Edit "></td>
          </tr>
</form>
        </table>

      <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="95%">
  			<tr>
        	  <td width="50%" align="center" >&nbsp; </td>
  			</TR>
		</table>

          <table width="95%" border="1" cellspacing="0" cellpadding="2"  bgcolor="silver" >
<form name="addtemplatecode" method="post" action="scheduletemplatecodesetting.jsp">
	<%
   rsdemo = null;
   boolean bEdit=request.getParameter("dboperation")!=null&&request.getParameter("dboperation").equals(" Edit ")?true:false;
   if(bEdit) {
     rsdemo = scheduleMainBean.queryResults(request.getParameter("code"), "search_scheduletemplatecodesingle");
     while (rsdemo.next()) { 
       dataBean.setProperty("code", rsdemo.getString("code") );
       dataBean.setProperty("description", rsdemo.getString("description") );
       dataBean.setProperty("duration", rsdemo.getString("duration")==null?"":rsdemo.getString("duration") );
       dataBean.setProperty("color", rsdemo.getString("color")==null?"":rsdemo.getString("color") );

     }
   }
%>              

          <tr bgcolor="#FOFOFO" align="center"> 
              <td colspan=2><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">Appt Template Code</font></td>
            </tr>
            <tr bgcolor='ivory'>
             <td><font color="red">Code:</font></td>
             <td><input type="text" name="code" size="1" maxlength="1" <%=bEdit?("value='"+dataBean.getProperty("code")+"'"):"value=''"%> ></td>
            </tr>
            <tr bgcolor='ivory'>
             <td><font color="red">Description:</font></td>
             <td><input type="text" name="description"  size="25" <%=bEdit?("value='"+dataBean.getProperty("description")+"'"):"value=''"%> ></td>
            </tr>
            <tr bgcolor='ivory'>
             <td><font color="red">Duration:</font></td>
             <td><input type="text" name="duration"  size="3" maxlength="3" <%=bEdit?("value='"+dataBean.getProperty("duration")+"'"):"value=''"%> > mins.</td>
            </tr>
            <tr bgcolor='ivory'>
             <td><font color="red">Color:</font></td>
             <td><input type="text" name="color"  size="10" maxlength="10" <%=bEdit?("value='"+dataBean.getProperty("color")+"'"):"value=''"%> > e.g. #1b2c3d, ivory.</td>
            </tr>
          </table>
            
          <table width="95%" border="0" cellspacing="0" cellpadding="2"  bgcolor="silver" >
          <tr bgcolor="#FOFOFO">
            <td><input type="submit" name="dboperation" value="Delete"></td>
			<td align="right"> 
                <input type="submit" name="dboperation" value=" Save ">
                <input type="button" name="Button" value=" Exit " onClick="window.close()">
            </td>
          </tr>
</form>
        </table>
        <p align='left'>
      &nbsp;  Code: only one char.<br>
      &nbsp;  Description: less than 40 chars.<br>
      &nbsp;  Duration: unit is minute.<br>
      &nbsp;  Color: html code for background color.</p>
        
      </td>
    </tr>
  </table>

</form>
</body>
</html>
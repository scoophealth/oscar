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

<%--
/*
 * $RCSfile: scheduledatepopup.jsp,v $ *
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
 * (your name here) 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster Unviersity 
 * Hamilton 
 * Ontario, Canada 
*/
--%>
<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="scheduleMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="scheduleDateBean" class="java.util.Hashtable" scope="session" />
<%
  String year = request.getParameter("year");
  String month = MyDateFormat.getDigitalXX(Integer.parseInt(request.getParameter("month")));
  String day = MyDateFormat.getDigitalXX(Integer.parseInt(request.getParameter("day")));

  String available = "checked", strHour = "", strReason = "value=''", strCreator="Me";
  HScheduleDate aHScheduleDate= (HScheduleDate) scheduleDateBean.get(year+"-"+month+"-"+day);
  if (aHScheduleDate!=null) {
    available = aHScheduleDate.available.compareTo("1")==0?"checked":""  ;
    strHour = aHScheduleDate.hour;
    //strHour = "value='"+ aHScheduleDate.hour +"'";
    strReason = "value='"+ aHScheduleDate.reason +"'" ;
    strCreator= aHScheduleDate.creator;
  }

%>
<html>
<head>
<title>SCHEDULE SETTING</title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  document.schedule.hour.focus();
  //document.schedule.hour.select();
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<form method="post" name="schedule" action="scheduledatesave.jsp">

  <table border="0" width="100%">
    <tr> 
      <td width="50" bgcolor="#009966">&nbsp;</td>
      <td>	  
        <table width="95%" border="0" cellspacing="0" cellpadding="5">
          <tr> 
            <td bgcolor="#CCFFCC"> 
              <p align="right">Date: </p>
            </td>
            <td bgcolor="#CCFFCC"><%=year%>-<%=month%>-<%=day%></td><input type="hidden" name="date" value="<%=year%>-<%=month%>-<%=day%>">
          </tr>
          <tr> 
            <td> 
              <div align="right">Available: </div>
            </td>
            <td> 
              <input type="radio" name="available" value="1" <%=available.equals("checked")?"checked":""%> >
              Yes 
              <input type="radio" name="available" value="0" <%=available.equals("checked")?"":"checked"%> >
              No </td>
          </tr>
          <tr> 
            <td> 
              <div align="right">Template: </div>
            </td>
            <td> 
              <!--input type="text" name="hour1" <%=strHour%> -->
   <select name="hour" >
	<%
   ResultSet rsdemo = null;
   String[] param =new String[2];
   param[0]="Public";
   param[1]=request.getParameter("name");
   rsdemo = scheduleMainBean.queryResults(param, "search_scheduletemplate");
   while (rsdemo.next()) { 
	%>
        <option value="<%=rsdemo.getString("name")%>" <%=strHour.equals(rsdemo.getString("name"))?"selected":""%> ><%=rsdemo.getString("name")+" |"+rsdemo.getString("summary")%></option>
  <%
   }
   param[0]=request.getParameter("provider_no");
   param[1]=request.getParameter("name");
   rsdemo = scheduleMainBean.queryResults(param, "search_scheduletemplate");
   while (rsdemo.next()) { //System.out.println(strHour +" : "+rsdemo.getString("name"));
	%>
        <option value="<%=rsdemo.getString("name")%>" <%=rsdemo.getString("name").equals(strHour)?"selected":""%> ><%=rsdemo.getString("name")+" |"+rsdemo.getString("summary")%></option>
  <% }	%>
   </select>             
              
            </td>
          </tr>
          <!--tr>
            <td>
              <div align="right">Summary: </div>
            </td>
            <td>
              <input type="text" name="reason" <%=strReason%> >
            </td>
          </tr-->
              <input type="hidden" name="reason" <%=strReason%> >
          <tr>
            <td>
              <div align="right">By: </div>
            </td>
            <td>
              <%=strCreator%>
            </td>
          </tr>
        </table>
        <table width="100%" border="0" cellspacing="0" cellpadding="0">
          <tr> 
            <td>&nbsp; </td>
          </tr>
          <tr>
            <td bgcolor="#CCFFCC"> 
              <div align="right"> 
                <input type="hidden" name="provider_no" value="<%=request.getParameter("provider_no")%>">
                <input type="submit" name="Submit" value=" Save ">
                <input type="button" name="Button" value="Cancel" onClick="window.close()">
                <input type="submit" name="Submit" value=" Delete ">
              </div>
            </td>
          </tr>
        </table>
        <br>
      </td>
    </tr>
  </table>

</form>
</body>
</html>
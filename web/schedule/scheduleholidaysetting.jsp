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
 * $RCSfile: scheduleholidaysetting.jsp,v $ *
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
<jsp:useBean id="scheduleHolidayBean" class="java.util.Hashtable" scope="session" />
<% //save or delete the holiday settings
  if(request.getParameter("dboperation")!=null && (request.getParameter("dboperation").compareTo(" Save ")==0 || request.getParameter("dboperation").equals("Delete")) ) {
    //save the record first, change holidaybean next
    String temp = null;
    int rowsAffected = 0;
    String[] param1 =new String[2];
	  for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		  temp=e.nextElement().toString();
		  if( !temp.startsWith("sdate_") || request.getParameter(temp).equals("")) continue;
      rowsAffected = scheduleMainBean.queryExecuteUpdate(request.getParameter(temp),"delete_scheduleholiday");
      scheduleHolidayBean.remove( request.getParameter(temp) );
      if(request.getParameter("dboperation").compareTo(" Save ")==0 ) {
      param1[0]=request.getParameter(temp);
      param1[1]=request.getParameter("holiday_name");
      rowsAffected = scheduleMainBean.queryExecuteUpdate(param1,"add_scheduleholiday");
      scheduleHolidayBean.put(request.getParameter(temp), new HScheduleHoliday(request.getParameter("holiday_name") ));
      }
    }
  }
%>

<%
  //to prepare calendar display  
  GregorianCalendar now = new GregorianCalendar();
  int year = now.get(Calendar.YEAR);
  int month = now.get(Calendar.MONTH)+1;
  int day = now.get(Calendar.DATE);
  int delta = 0; //add or minus month
  now = new GregorianCalendar(year,month-1,1);

  if(request.getParameter("bFirstDisp")!=null && request.getParameter("bFirstDisp").compareTo("0")==0) {
    year = Integer.parseInt(request.getParameter("year"));
    month = Integer.parseInt(request.getParameter("month"));
    day = Integer.parseInt(request.getParameter("day"));
	  delta = Integer.parseInt(request.getParameter("delta")==null?"0":request.getParameter("delta"));
	  now = new GregorianCalendar(year,month-1,1);
  	now.add(now.MONTH, delta);
    year = now.get(Calendar.YEAR);
    month = now.get(Calendar.MONTH)+1;
  }

if(request.getParameter("bFirstDisp")==null || request.getParameter("bFirstDisp").compareTo("1")==0) {
  //create scheduleHolidayBean record
  scheduleHolidayBean.clear();
  ResultSet rsgroup = scheduleMainBean.queryResults("%","search_scheduleholiday");
  while (rsgroup.next()) { 
    scheduleHolidayBean.put(rsgroup.getString("sdate"), new HScheduleHoliday(rsgroup.getString("holiday_name") ));
  }
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
  document.schedule.holiday_name.focus();
  document.schedule.holiday_name.select();
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
function addspace() {
	document.schedule.holiday_name.value = " ";
}
//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<form method="post" name="schedule" action="scheduleholidaysetting.jsp" onSubmit="return(checkInput());">

  <table border="0" width="100%">
    <tr> 
      <td width="50" bgcolor="#009966">&nbsp;</td>
      <td align="center">
	  
        <table width="95%" border="0" cellspacing="0" cellpadding="5">
          <tr> 
            <td bgcolor="#CCFFCC"> 
              <p align="right">Holiday Name: </p>
            </td>
            <td bgcolor="#CCFFCC"><input type="text" name="holiday_name" ></td>
          </tr>
        </table>
<%
	//now = new GregorianCalendar(year, month+1, 1);
  now.add(now.DATE, -1); 
  DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
  int [][] dateGrid = aDate.getMonthDateGrid();
%>
      <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="95%">
  			<tr>
        	  <td width="50%" align="center" >
			  <a href="scheduleholidaysetting.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&delta=-1&bFirstDisp=0"> &nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="View Last Month" vspace="2"> last month&nbsp;&nbsp; 
              </a>  <b><span CLASS=title><%=year%>-<%=month%></span></b>
        <a href="scheduleholidaysetting.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&delta=1&bFirstDisp=0"> &nbsp;&nbsp;next month <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="View Next Month" vspace="2">&nbsp;&nbsp;</a></td>
  			</TR>
		</table>

          <table width="95%" border="1" cellspacing="0" cellpadding="2"  bgcolor="silver" >
            <tr bgcolor="#FOFOFO" align="center"> 
              <td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">Sun</font></td>
              <td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Mon</font></td>
              <td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Tue</font></td>
              <td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Wed</font></td>
              <td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Thu</font></td>
              <td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Fri</font></td>
              <td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="green">Sat</font></td>
            </tr>
            <%
              StringBuffer bgcolor = new StringBuffer("white");
              StringBuffer strHolidayName = new StringBuffer("");
              HScheduleHoliday aHScheduleHoliday = null;
              for (int i=0; i<dateGrid.length; i++) {
                out.println("<tr>");
                for (int j=0; j<7; j++) {
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    now.add(now.DATE, 1);
                    bgcolor = new StringBuffer("ivory");
                    strHolidayName = new StringBuffer("");
                    aHScheduleHoliday = (HScheduleHoliday) scheduleHolidayBean.get(year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j]));
                    if (aHScheduleHoliday!=null) {
                      bgcolor = new StringBuffer("pink");
                      strHolidayName = new StringBuffer(aHScheduleHoliday.holiday_name) ;
                    }
                      
            %>
                      <td bgcolor='<%=bgcolor.toString()%>'>
                      <font color="red"><%= dateGrid[i][j] %></font>
                      <input type="checkbox" name="sdate_<%=month+"_"+dateGrid[i][j]%>" value="<%=year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j])%>" >
                      <font size="-2">
                      <br>&nbsp;<%=strHolidayName.toString()%></font></td>
            <%    
                  }
                }
                out.println("</tr>");
              }
            %>
        </table>
            
            
          <table width="95%" border="0" cellspacing="0" cellpadding="2"  bgcolor="silver" >
          <tr bgcolor="#CCFFCC">
            <td><input type="submit" name="dboperation" value="Delete" onClick="addspace()"></td>
			<td><div align="right"> 
                <input type="hidden" name="year" value="<%=year%>">
                <input type="hidden" name="month" value="<%=month%>">
                <input type="hidden" name="day" value="<%=day%>">
                <input type="hidden" name="bFirstDisp" value="0">
                <input type="submit" name="dboperation" value=" Save ">
                <input type="button" name="Button" value=" Exit " onClick="window.close()">
                
              </div>
            </td>
          </tr>
        </table>
        <p>&nbsp;</p>
        
      </td>
    </tr>
  </table>

</form>
</body>
</html>
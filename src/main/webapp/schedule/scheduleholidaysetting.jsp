<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%--
/*
 * $RCSfile: AbstractApplication.java,v $ *
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
 * McMaster University
 * Hamilton
 * Ontario, Canada
*/
--%>
<%

%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<jsp:useBean id="scheduleHolidayBean" class="java.util.Hashtable" scope="session" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ScheduleHoliday" %>
<%@ page import="org.oscarehr.common.dao.ScheduleHolidayDao" %>
<%
	ScheduleHolidayDao scheduleHolidayDao = SpringUtils.getBean(ScheduleHolidayDao.class);
%>

<% //save or delete the holiday settings
  if(request.getParameter("dboperation")!=null && (request.getParameter("dboperation").compareTo(" Save ")==0 || request.getParameter("dboperation").equals("Delete")) ) {
    //save the record first, change holidaybean next
    String temp = null;
    int rowsAffected = 0;
    String[] param1 =new String[2];
      for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	 temp=e.nextElement().toString();

         if( !temp.startsWith("sdate_") || request.getParameter(temp).equals("")) continue;

         ScheduleHoliday sh = scheduleHolidayDao.find(MyDateFormat.getSysDate(request.getParameter(temp)));
         if(sh != null) {
        	 scheduleHolidayDao.remove(sh.getId());
         }

         scheduleHolidayBean.remove( request.getParameter(temp) );

         if(request.getParameter("dboperation").compareTo(" Save ")==0 ) {
             sh = new ScheduleHoliday();
             sh.setId(MyDateFormat.getSysDate(request.getParameter(temp)));
             sh.setHolidayName(request.getParameter("holiday_name"));
             scheduleHolidayDao.persist(sh);
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
  	now.add(Calendar.MONTH, delta);
    year = now.get(Calendar.YEAR);
    month = now.get(Calendar.MONTH)+1;
  }

if(request.getParameter("bFirstDisp")==null || request.getParameter("bFirstDisp").compareTo("1")==0) {
  //create scheduleHolidayBean record
  scheduleHolidayBean.clear();
  
  for(ScheduleHoliday sh : scheduleHolidayDao.findAll()) {
  
    scheduleHolidayBean.put(oscar.util.ConversionUtils.toDateString(sh.getId()), new HScheduleHoliday(sh.getHolidayName() ));
  }
}

%>
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="schedule.scheduleholidaysetting.title" /></title>
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
	  alert('<bean:message key="schedule.scheduleholidaysetting.msgCheckInput"/>');
	  return false;
	} else {
	  return true;
	}
}
function addspace() {
	document.schedule.holiday_name.value = " ";
}

function deleteHoliday() {
     addspace();
     document.forms['schedule'].dboperation.value='Delete';
     document.forms['schedule'].submit();
}

function saveHoliday() {
     if (checkInput()) {
        document.forms['schedule'].dboperation.value=' Save ';
        document.forms['schedule'].submit();
     } else {
        return false;
     }
     return true;
}

//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<form method="post" name="schedule" action="scheduleholidaysetting.jsp"
	onSubmit="return(checkInput());">

<table border="0" width="100%">
	<tr>
		<td width="50" bgcolor="#009966">&nbsp;</td>
		<td align="center">

		<table width="95%" border="0" cellspacing="0" cellpadding="5">
			<tr>
				<td bgcolor="#CCFFCC">
				<p align="right"><bean:message
					key="schedule.scheduleholidaysetting.formHolidayName" />:</p>
				</td>
				<td bgcolor="#CCFFCC"><input type="text" name="holiday_name"></td>
			</tr>
		</table>
		<%
	//now = new GregorianCalendar(year, month+1, 1);
  now.add(Calendar.DATE, -1);
  DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
  int [][] dateGrid = aDate.getMonthDateGrid();
%>
		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="95%">
			<tr>
				<td width="50%" align="center"><a
					href="scheduleholidaysetting.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&delta=-1&bFirstDisp=0">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0"
					ALT='<bean:message key="schedule.scheduleholidaysetting.btnLastMonthTip"/>'
					vspace="2"> <bean:message
					key="schedule.scheduleholidaysetting.btnLastMonth" />&nbsp;&nbsp; </a>
				<b><span CLASS=title><%=year%>-<%=month%></span></b> <a
					href="scheduleholidaysetting.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&delta=1&bFirstDisp=0">
				&nbsp;&nbsp;<bean:message
					key="schedule.scheduleholidaysetting.btnNextMonth" /> <img
					src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT='<bean:message key="schedule.scheduleholidaysetting.btnNextMonthTip"/>'
					vspace="2">&nbsp;&nbsp;</a></td>
			</TR>
		</table>

		<table width="95%" border="1" cellspacing="0" cellpadding="2"
			bgcolor="silver">
			<tr bgcolor="#FOFOFO" align="center">
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
					color="red"><bean:message
					key="schedule.scheduleholidaysetting.msgSunday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.scheduleholidaysetting.msgMonday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.scheduleholidaysetting.msgTuesday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.scheduleholidaysetting.msgWednesday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.scheduleholidaysetting.msgThursday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.scheduleholidaysetting.msgFriday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
					color="green"><bean:message
					key="schedule.scheduleholidaysetting.msgSaturday" /></font></td>
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
                    now.add(Calendar.DATE, 1);
                    bgcolor = new StringBuffer("ivory");
                    strHolidayName = new StringBuffer("");
                    aHScheduleHoliday = (HScheduleHoliday) scheduleHolidayBean.get(year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j]));
                    if (aHScheduleHoliday!=null) {
                      bgcolor = new StringBuffer("pink");
                      strHolidayName = new StringBuffer(aHScheduleHoliday.holiday_name) ;
                    }

            %>
			<td bgcolor='<%=bgcolor.toString()%>'><font color="red"><%= dateGrid[i][j] %></font>
			<input type="checkbox" name="sdate_<%=month+"_"+dateGrid[i][j]%>"
				value="<%=year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j])%>">
			<font size="-2"> <br>
			&nbsp;<%=strHolidayName.toString()%></font></td>
			<%
                  }
                }
                out.println("</tr>");
              }
            %>
		</table>


		<table width="95%" border="0" cellspacing="0" cellpadding="2"
			bgcolor="silver">
			<tr bgcolor="#CCFFCC">
				<input type="hidden" name="dboperation" value="">
				<td><input type="button"
					value='<bean:message key="schedule.scheduleholidaysetting.btnDelete"/>'
					onclick="deleteHoliday();"></td>
				<td>
				<div align="right"><input type="hidden" name="year"
					value="<%=year%>"> <input type="hidden" name="month"
					value="<%=month%>"> <input type="hidden" name="day"
					value="<%=day%>"> <input type="hidden" name="bFirstDisp"
					value="0"> <input type="button"
					value='<bean:message key="schedule.scheduleholidaysetting.btnSave"/>'
					onclick="return(saveHoliday());"> <input type="button"
					value='<bean:message key="global.btnClose"/>'
					onClick="window.close()"></div>
				</td>
			</tr>
		</table>
		<p>&nbsp;</p>

		</td>
	</tr>
</table>

</form>
</body>
</html:html>

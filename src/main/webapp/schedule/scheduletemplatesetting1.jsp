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

<%
  
  String weekdaytag[] = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*" errorPage="../appointment/errorpage.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.RSchedule" %>
<%@page import="org.oscarehr.common.dao.RScheduleDao" %>
<%@page import="oscar.util.ConversionUtils" %>

<%
	RScheduleDao rScheduleDao = SpringUtils.getBean(RScheduleDao.class);
%>

<jsp:useBean id="scheduleRscheduleBean" class="oscar.RscheduleBean" scope="session" />


<% scheduleRscheduleBean.clear(); %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>SCHEDULE SETTING</title>
<link rel="stylesheet" href="../web.css" />

</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<form method="post" name="schedule" action="schedulecreatedate.jsp"
	onSubmit="addDataString();return(addDataString1())">

<table border="0" width="100%">
	<tr>
		<td width="150" bgcolor="#009966"><!--left column-->
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr bgcolor="#486ebd">
				<th align="CENTER" bgcolor="#009966">
				<p>&nbsp;</p>
				<p><font face="Helvetica" color="#FFFFFF">SCHEDULE
				TEMPLATE SETTING</font></p>
				</th>
			</tr>
		</table>
		<table width="98%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<p>&nbsp;</p>
				<p><font size="-1">1. Use the current R Schedule or
				select a different one from the select field.</font></p>
				<p><font size="-1">2. Type in the start date and end date
				for this R Schedule.</font></p>
				<p><font size="-1">3. Check the day of week which is
				AVAILABLE.</font></p>
				<p><font size="-1">4. Click the 'Next' button.</font></p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				</td>
			</tr>
		</table>

		</td>
		<td>

		<center>
		<%
  int rowsAffected = 0;
  GregorianCalendar now = new GregorianCalendar();
  int year = now.get(Calendar.YEAR);
  int month = now.get(Calendar.MONTH)+1;
  int day = now.get(Calendar.DATE);
  String today = now.get(Calendar.YEAR)+"-"+MyDateFormat.getDigitalXX((now.get(Calendar.MONTH)+1))+"-"+MyDateFormat.getDigitalXX(now.get(Calendar.DATE));
  

  RSchedule r = rScheduleDao.search_rschedule_current(request.getParameter("provider_no"), "1", ConversionUtils.fromDateString(request.getParameter("sdate")!=null?request.getParameter("sdate"):today));
  if(r != null) {
  	scheduleRscheduleBean.setRscheduleBean(r.getProviderNo(),ConversionUtils.toDateString(r.getsDate()),ConversionUtils.toDateString(r.geteDate()),r.getAvailable(),r.getDayOfWeek(), null, r.getAvailHour(), r.getCreator());
  } 

  String syear = "",smonth="",sday="",eyear="",emonth="",eday="";
  String[] param2 =new String[7];
  for(int i=0; i<7; i++) {param2[i]="";}
  String[][] param3 =new String[7][2];
  for(int i=0; i<7; i++) {
    for(int j=0; j<2; j++) {
	    param3[i][j]="";
	  }
  }
  if(scheduleRscheduleBean.provider_no!="") {
    syear = ""+MyDateFormat.getYearFromStandardDate(scheduleRscheduleBean.sdate);
    smonth = ""+MyDateFormat.getMonthFromStandardDate(scheduleRscheduleBean.sdate);
    sday = ""+MyDateFormat.getDayFromStandardDate(scheduleRscheduleBean.sdate);
    eyear = ""+MyDateFormat.getYearFromStandardDate(scheduleRscheduleBean.edate);
    emonth = ""+MyDateFormat.getMonthFromStandardDate(scheduleRscheduleBean.edate);
    eday = ""+MyDateFormat.getDayFromStandardDate(scheduleRscheduleBean.edate);

    String availhour = scheduleRscheduleBean.avail_hour;
    StringTokenizer st = new StringTokenizer(scheduleRscheduleBean.day_of_week);
    while (st.hasMoreTokens() ) {
      int j = Integer.parseInt(st.nextToken())-1;
	    int i = j==7?0:j;
      param2[i]="checked";
      if(SxmlMisc.getXmlContent(availhour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">") != null) {
	      StringTokenizer sthour = new StringTokenizer(SxmlMisc.getXmlContent(availhour, ("<"+weekdaytag[i]+">"),"</"+weekdaytag[i]+">"), "-");
        j = 0;
		    while (sthour.hasMoreTokens() ) {
          param3[i][j]=sthour.nextToken(); j++;
        }
	    }
    }
  }

%>
		<p>&nbsp;</p>
		<table width="95%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td bgcolor="#CCFFCC"><b><%=request.getParameter("provider_name")%></b>
				<input type="hidden" name="provider_name"
					value="<%=request.getParameter("provider_name")%>"></td>
				<td bgcolor="#CCFFCC">
				<div align="right"><select name="select"
					onChange="selectrschedule(this)">
					<option value="<%=today%>"
						<%=request.getParameter("sdate")!=null?(today.equals(request.getParameter("sdate"))?"selected":""):""%>>Current
					R Schedule</option>
					<%
 
  for(RSchedule rs: rScheduleDao.search_rschedule_future(request.getParameter("provider_no"),"1",ConversionUtils.fromDateString(today))) {
  
%>
					<option value="<%=ConversionUtils.toDateString(rs.getsDate())%>"
						<%=request.getParameter("sdate")!=null?(ConversionUtils.toDateString(rs.getsDate()).equals(request.getParameter("sdate"))?"selected":""):""%>>
					<%=ConversionUtils.toDateString(rs.getsDate())+" ~ "+ConversionUtils.toDateString(rs.geteDate())%></option>
					<%
 	}
%>
				</select></div>
				</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2">Date:</td>
				<!--is not available every day of month:-->
			</tr>
			<tr>
				<td bgcolor="#CCFFCC" colspan="2">&nbsp; from<font size="-2">(yyyy-mm-dd)</font>:
				<input type="text" name="syear" size="4" maxlength="4"
					value="<%=syear%>"> - <input type="text" name="smonth"
					size="2" maxlength="2" value="<%=smonth%>"> - <input
					type="text" name="sday" size="2" maxlength="2" value="<%=sday%>">
				&nbsp; &nbsp; to<font size="-2">(yyyy-mm-dd)</font>: <input
					type="text" name="eyear" size="4" maxlength="4" value="<%=eyear%>">
				- <input type="text" name="emonth" size="2" maxlength="2"
					value="<%=emonth%>"> - <input type="text" name="eday"
					size="2" maxlength="2" value="<%=eday%>"> <input
					type="hidden" name="day_of_month1" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month2" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month3" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month4" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month5" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month6" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month7" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month8" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month9" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month10" size="3" maxlength="2"
					onBlur="addDataString1()"> <input type="hidden"
					name="day_of_month" value=""> <input type="hidden"
					name="day_of_year" value=""></td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2">is available EVERY<font size="-2"> (Day
				of Week)</font>:</td>
			</tr>
			<tr>
				<td nowrap align="right" colspan="2">
				<table border=0 width=80%>
					<tr bgcolor="#CCFFCC">
						<td>
						<p><font size="-1"> <input type="checkbox"
							name="checksun" value="1" onClick="addDataString()"
							<%=param2[0]%>> Sun. </font>
						</td>
						<td><font size="-1">from: <input type="text"
							name="sunfrom" size="5" maxlength="5" value="<%=param3[0][0]%>">
						&nbsp;&nbsp;to: <input type="text" name="sunto" size="5"
							maxlength="5" value="<%=param3[0][1]%>"> </font></td>
					</tr>
					<tr>
						<td><font size="-1"> <input type="checkbox"
							name="checkmon" value="2" onClick="addDataString()"
							<%=param2[1]%>> Mon.</font></td>
						<td><font size="-1">from: <input type="text"
							name="monfrom" size="5" maxlength="5" value="<%=param3[1][0]%>">
						&nbsp;&nbsp;to: <input type="text" name="monto" size="5"
							maxlength="5" value="<%=param3[1][1]%>"> </font></td>
					</tr>
					<tr bgcolor="#CCFFCC">
						<td><font size="-1"> <input type="checkbox"
							name="checktue" value="3" onClick="addDataString()"
							<%=param2[2]%>> Tue.</font></td>
						<td><font size="-1">from: <input type="text"
							name="tuefrom" size="5" maxlength="5" value="<%=param3[2][0]%>">
						&nbsp;&nbsp;to: <input type="text" name="tueto" size="5"
							maxlength="5" value="<%=param3[2][1]%>"> </font></td>
					</tr>
					<tr>
						<td><font size="-1"> <input type="checkbox"
							name="checkwed" value="4" onClick="addDataString()"
							<%=param2[3]%>> Wed.</font></td>
						<td><font size="-1">from: <input type="text"
							name="wedfrom" size="5" maxlength="5" value="<%=param3[3][0]%>">
						&nbsp;&nbsp;to: <input type="text" name="wedto" size="5"
							maxlength="5" value="<%=param3[3][1]%>"> </font></td>
					</tr>
					<tr bgcolor="#CCFFCC">
						<td><font size="-1"> <input type="checkbox"
							name="checkthu" value="5" onClick="addDataString()"
							<%=param2[4]%>> Thu.</font></td>
						<td><font size="-1">from: <input type="text"
							name="thufrom" size="5" maxlength="5" value="<%=param3[4][0]%>">
						&nbsp;&nbsp;to: <input type="text" name="thuto" size="5"
							maxlength="5" value="<%=param3[4][1]%>"> </font></td>
					</tr>
					<tr>
						<td><font size="-1"> <input type="checkbox"
							name="checkfri" value="6" onClick="addDataString()"
							<%=param2[5]%>> Fri.</font></td>
						<td><font size="-1">from: <input type="text"
							name="frifrom" size="5" maxlength="5" value="<%=param3[5][0]%>">
						&nbsp;&nbsp;to: <input type="text" name="frito" size="5"
							maxlength="5" value="<%=param3[5][1]%>"> </font></td>
					</tr>
					<tr bgcolor="#CCFFCC">
						<td><font size="-1"> <input type="checkbox"
							name="checksat" value="7" onClick="addDataString()"
							<%=param2[6]%>> Sat.</font></td>
						<td><font size="-1">from: <input type="text"
							name="satfrom" size="5" maxlength="5" value="<%=param3[6][0]%>">
						&nbsp;&nbsp;to: <input type="text" name="satto" size="5"
							maxlength="5" value="<%=param3[6][1]%>"> </font></td>
					</tr>
				</table>
				</td>
				<input type="hidden" name="day_of_week" value="">
				<input type="hidden" name="avail_hour" value="">
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td colspan="2">&nbsp;</td>
			</tr>
			<tr>
				<td bgcolor="#CCFFCC" colspan="2">
				<div align="right"><input type="hidden" name="provider_no"
					value="<%=request.getParameter("provider_no")%>"> <input
					type="hidden" name="available" value="1"> <input
					type="submit" name="Submit" value=" Next "> <input
					type="button" name="Cancel" value="Cancel" onClick="window.close()">
				</div>
				</td>
			</tr>
		</table>
		<p>
		<p>&nbsp;</p>
		</center>
		</td>
	</tr>
</table>


</form>
</body>
</html>

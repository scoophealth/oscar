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
  if(session.getValue("user") == null)
    response.sendRedirect("../login.htm");
  String curProvider_no,userfirstname,userlastname;
  curProvider_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  //display the main provider page
  //includeing the provider name and a month calendar
%>
<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.sql.*,oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />

<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR); //curYear should be the real now date
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int year = Integer.parseInt(request.getParameter("year"));
  int month = Integer.parseInt(request.getParameter("month"));
  int day = Integer.parseInt(request.getParameter("day"));
  String strYear=null, strMonth=null, strDay=null;
  String strDayOfWeek=null;
  String[] arrayDayOfWeek = new String[] {
    "Sun","Mon","Tue","Wed","Thu","Fri","Sat"
  };

  //verify the input date is really existed 
	now=new GregorianCalendar(year,(month-1),day);
  year = now.get(Calendar.YEAR); //month should be the current main display date, not the real now date
  month = (now.get(Calendar.MONTH)+1);
  day = now.get(Calendar.DAY_OF_MONTH);
  int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
  strDayOfWeek=arrayDayOfWeek[dayOfWeek-1];
  strYear=""+year;
  strMonth=month>9?(""+month):("0"+month);
  strDay=day>9?(""+day):("0"+day);
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>StoneChurch Provider Appointment Access</title>
<link rel="stylesheet" href="provapptstyle.css" type="text/css">
</head>
<script language="JavaScript">
<!--
function setfocus() {
  document.jumptodate.year.focus();
  document.jumptodate.year.select();
}

function popupPage(varpage) { //open a new popup window
var page = "" + varpage;
windowprops = "height=560,width=680,location=no,"
+ "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";
window.open(page, "Popup", windowprops);
}
function popupPage2(varpage) {
var page = "" + varpage;
windowprops = "height=550,width=678,location=no,"
+ "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=15,left=15";
window.open(page, "Popup", windowprops);
}
function findObj(n, d) { 
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
  d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document); return x;
}
function showHideLayers() { 
  var i,p,v,obj,args=showHideLayers.arguments;
  for (i=0; i<(args.length-2); i+=3) if ((obj=findObj(args[i]))!=null) { v=args[i+2];
    if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v='hide')?'hidden':v; }
    obj.visibility=v; }
}
//-->
</SCRIPT>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()">
<center>
<table width="100%" border="0" cellspacing="2" cellpadding="0"
	bgcolor="#096886">
	<tr>
		<td width="25%" bgcolor="#3EA4E1" align="center"><font
			face="sans-serif" size="2"> <a
			href="appointmentcontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&displaymode=day&dboperation=searchappointmentday"
			title="Your Schedule and appointments"
			onmouseover="window.status='View your Schedule and appointments';return true">Schedule
		for Today</a></font></td>
		<td width="25%" bgcolor="#3EA4E1" align="center"><font
			face="sans-serif" size="2"> <a
			href="appointmentcontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&displaymode=month&dboperation=searchappointmentmonth"
			TITLE='View your template'
			onmouseover="window.status='View your template';return true">Monthly
		Template</a></font></td>
		<td width="25%" bgcolor="#3EA4E1" align="center"><font
			face="sans-serif" size="2"> <a HREF="#"
			ONCLICK="popupPage2('../cpp/search.htm')"
			TITLE='Search for patient records'
			onmouseover="window.status='Search for patient records';return true">Search/Add
		Patients</a></font></td>
		<td width="25%" bgcolor="#3EA4E1" align="center"><font
			face="sans-serif" size="2"> <a HREF="../hlp.html"
			TITLE='Generate a report'
			onmouseover="window.status='Generate a report';return true">Generate
		a Report</a></font></td>
	</tr>
</table>
</center>

<div id="jumpmenu"
	style="position: absolute; width: 140px; height: 100px; z-index: 2; left: 240px; top: 30px; visibility: hidden">
<table width="85%" bgcolor="#F0F0F0" cellpadding="0" cellspacing="2">
	<tr>
		<td bgcolor="#F0F0F0" ALIGN="LEFT">
		<p>yyyy mm dd-date</p>
		</td>
	</tr>
	<tr>
		<td bgcolor="#F0F0F0" ALIGN="LEFT">
		<p>[+/-n]d - n days</p>
		</td>
	</tr>
	<tr>
		<td bgcolor="#F0F0F0" ALIGN="LEFT">
		<p>[+/-n]w - n weeks</p>
		</td>
	</tr>
	<tr>
		<td bgcolor="#F0F0F0" ALIGN="LEFT">
		<p>[+/-n]m - n months</p>
		</td>
	</tr>
</table>
</div>

<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td VALIGN="BOTTOM" HEIGHT="20">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" height="20">
			<tr>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap height="20"><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&displaymode=day&dboperation=searchappointmentday"
					TITLE='View your daily schedule'
					OnMouseOver="window.status='View your daily schedule' ; return true">
				&nbsp;&nbsp;Day&nbsp;&nbsp; </a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&displaymode=week&dboperation=searchapptweek"
					TITLE='View your weekly schedule'
					OnMouseOver="window.status='View your weekly schedule' ; return true">Week</a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="ivory" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth"
					TITLE='View your monthly template'
					OnMouseOver="window.status='View your monthly template' ; return true">Month</a></font></td>
				<td></td>
			</tr>
			<tr>
				<td valign="bottom"><img
					src="../images/tabs_l_inactive_end.gif" width="14" height="20"
					border="0"></td>
				<td valign="bottom"><img src="../images/tabs_both_inactive.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img src="../images/tabs_l_active_end.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img
					src="../images/tabs_r_active_end_alone.gif" width="17" height="20"
					border="0"></td>
			</tr>
		</table>

		</td>
		<form method="post" name="jumptodate" action="appointmentcontrol.jsp">
		<td align="right" valign="bottom"><a href="#"
			onclick="showHideLayers('jumpmenu','','show');"
			onmouseout="showHideLayers('jumpmenu','','hide');"
			title="Click to bring up a menu">Jump to <i>(yyyy-mm-dd)</i>:</a> <INPUT
			TYPE="text" NAME="year" VALUE="<%=strYear%>" WIDTH="4" HEIGHT="10"
			border="0" size="4" maxlength="4">- <INPUT TYPE="text"
			NAME="month" VALUE="<%=strMonth%>" WIDTH="2" HEIGHT="10" border="0"
			size="2" maxlength="2">- <INPUT TYPE="text" NAME="day"
			VALUE="<%=strDay%>" WIDTH="2" HEIGHT="10" border="0" size="2"
			maxlength="2"> <INPUT TYPE="hidden" NAME="displaymode"
			VALUE="month"> <INPUT TYPE="hidden" NAME="dboperation"
			VALUE="searchappointmentmonth"> <INPUT TYPE="SUBMIT"
			NAME="Go" VALUE="GO" SIZE="5"></td>
		</form>
	</tr>
</table>


<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C0C0C0">
	<tr>
		<td>
		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
			<tr>
				<td BGCOLOR="ivory" width="33%"><a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=(month-1)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0" ALT="View Previous MONTH" vspace="2"></a> <b><span
					CLASS=title><%=strYear%>-<%=strMonth%></span></b> <a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=(month+1)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth">
				<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT="View Next MONTH" vspace="2">&nbsp;&nbsp;</a></td>
				<TD ALIGN="center" BGCOLOR="ivory" width="33%"><B>Hello <%= userfirstname+" "+userlastname %>
				</b></TD>
				<td ALIGN="RIGHT" BGCOLOR="Ivory"><a href="../index.html"
					title="Click to return to home"><font face="Arial, Helvetica">StoneChurch
				Family Health Centre&nbsp;</font></a></td>
			</tr>
			<tr>
				<td align="center" VALIGN="TOP" colspan="3" bgcolor="ivory">
				<%
            DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
            int [][] dateGrid = aDate.getMonthDateGrid();
%>

				<table width="98%" border="1" cellspacing="1" cellpadding="6"
					bgcolor="#99cccc">
					<tr bgcolor="#FOFOFO">
						<td width="12.5%">
						<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
							SIZE="2" color="blue">Week</font></div>
						</td>
						<td width="12.5%">
						<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
							SIZE="2" color="red">Sun</font></div>
						</td>
						<td width="12.5%">
						<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
							SIZE="2">Mon</font></div>
						</td>
						<td width="12.5%">
						<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
							SIZE="2">Tue</font></div>
						</td>
						<td width="12.5%">
						<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
							SIZE="2">Wed</font></div>
						</td>
						<td width="12.5%">
						<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
							SIZE="2">Thu</font></div>
						</td>
						<td width="12.5%">
						<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
							SIZE="2">Fri</font></div>
						</td>
						<td width="12.5%">
						<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
							SIZE="2" color="green">Sat</font></div>
						</td>
					</tr>

					<%
              for (int i=0; i<dateGrid.length; i++) {
                out.println("</tr>");
                for (int j=-1; j<7; j++) {
                  if(j==-1) {
            %>
					<td align='center' bgcolor='#FOFOFO'><font
						FACE='VERDANA,ARIAL,HELVETICA' SIZE='2'> <a
						href='../appointment/appointmentcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=dateGrid[i][j+1]==0?1:dateGrid[i][j+1]%>&displaymode=week&dboperation=searchapptweek'>
					<%=(i+1)%></font></td>
					<%
                    continue;
                  }
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    if(dateGrid[i][j]==day) {
            %>
					<td align='center'><a
						href='../appointment/appointmentcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(day)%>&displaymode=day&dboperation=searchappointmentday'>
					<font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">
					<div class='specialtxt'><%= dateGrid[i][j] %></div>
					</font></a></td>
					<%        } else out.println("<td align='center'><font FACE='VERDANA,ARIAL,HELVETICA' SIZE='2' color='white'><a href='../appointment/appointmentcontrol.jsp?year="+year+"&month="+MyDateFormat.getDigitalXX(month)+"&day="+MyDateFormat.getDigitalXX(dateGrid[i][j])+"&displaymode=day&dboperation=searchappointmentday'>" 
                        + dateGrid[i][j] + "</a></font></td>");
                  }
                }
                out.println("</tr>");
              }
            %>

				</table>


				</td>
			</tr>
			<tr>
				<td BGCOLOR="ivory" width="33%"><a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=(month-1)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0" ALT="View Previous MONTH" vspace="2"></a> <b><span
					CLASS=title><%=strYear%>-<%=strMonth%></span></b> <a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=(month+1)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth">
				<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT="View Next MONTH" vspace="2">&nbsp;&nbsp;</a></td>
				<TD ALIGN="center" BGCOLOR="ivory" width="33%"></TD>
				<td ALIGN="RIGHT" BGCOLOR="Ivory"><a href="../index.html"
					title="Click to return to home"><font face="Arial, Helvetica">StoneChurch
				Family Health Centre&nbsp;</font></a></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<!-- End sub page table -->
<p></p>
<%@ include file="footer.htm"%>
</body>
</html>
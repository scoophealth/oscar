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
  if(session.getValue("user") == null)
    response.sendRedirect("../login.htm");
  String curProvider_no,userfirstname,userlastname;
  curProvider_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  //display the main provider page
  //includeing the provider name and a month calendar
%>
<%@ page import="java.lang.*, java.util.*, java.text.*,java.sql.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="mainBean"
	class="oscar.AppointmentMainBean, oscar.MyDateFormat" scope="session" />

<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int year = Integer.parseInt(request.getParameter("year"));
  int month = Integer.parseInt(request.getParameter("month"));
  int day = Integer.parseInt(request.getParameter("day"));
  String strYear=null, strMonth=null, strDay=null, strMonthOfYear=null, strDayOfWeekInMonth=null;
  String strDayOfWeek=null;
  String[] arrayDayOfWeek = new String[] {
    "Sun","Mon","Tue","Wed","Thu","Fri","Sat" };
  String[] arrayMonthOfYear = new String[] {
    "January","February","March","April","May","June","July","August","September","October","November","December" };
  int iFebDays = 28;

  //verify the input date is really existed 
	now=new GregorianCalendar(year,(month-1),day);
  year = now.get(Calendar.YEAR);
//  if(now.isLeapYear(year) iFebDays = 29;
//  int[] dayOfMonth = new int[] {31,iFebDays ,31,30,31,30,31,31,30,31,30,31};
  month = (now.get(Calendar.MONTH)+1);
  day = now.get(Calendar.DAY_OF_MONTH);
  int dayOfWeek = now.get(Calendar.DAY_OF_WEEK);
  strDayOfWeek=arrayDayOfWeek[dayOfWeek-1];
  strYear=""+year;
  strMonth=MyDateFormat.getDigitalXX(month);
  strDay=MyDateFormat.getDigitalXX(day);
  strMonthOfYear=arrayMonthOfYear[month-1];
  strDayOfWeekInMonth=now.get(Calendar.DAY_OF_WEEK_IN_MONTH));
            System.out.println("before resultSet: ");
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>StoneChurch Provider Appointment Access</title>
<link rel="stylesheet" href="provapptstyle.css" type="text/css">
</head>
<script language="JavaScript">
<!--
function popupPage(varpage) { //open a new popup window
var page = "" + varpage;
windowprops = "height=500,width=640,location=no,"
+ "scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
window.open(page, "Popup", windowprops);
}
function popupPage2(varpage) {
var page = "/" + varpage;
windowprops = "height=450,width=600,location=no,"
+ "scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
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
<body background="../images/gray_bg.jpg" bgproperties="fixed">
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
			href="providerapptadmin?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&displaymode=month&dboperation=searchappointmentmonth"
			TITLE='View your template'
			onmouseover="window.status='View your template';return true">Monthly
		Template</a></font></td>
		<td width="25%" bgcolor="#3EA4E1" align="center"><font
			face="sans-serif" size="2"> <a HREF=#
			ONCLICK="popupPage2('pt_searchMAIN.html')"
			TITLE='Search for patient records'
			onmouseover="window.status='Search for patient records';return true">Search
		for Patients</a></font></td>
		<td width="25%" bgcolor="#3EA4E1" align="center"><font
			face="sans-serif" size="2"> <a
			HREF="inet_prov_ENTER_REPORT?ACCESSKEY=17412092&PROVIDER_ID=174"
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
				<td rowspan="2" BGCOLOR="ivory" ALIGN="MIDDLE" nowrap height="20"><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&displaymode=day&dboperation=searchappointmentday"
					TITLE='View your daily schedule'
					OnMouseOver="window.status='View your daily schedule' ; return true">
				&nbsp;&nbsp;Day&nbsp;&nbsp; </a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&displaymode=week&dboperation=searchappointmentweek"
					TITLE='View your daily schedule'
					OnMouseOver="window.status='View your weekly schedule' ; return true">Week</a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
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
				<td valign="bottom"><img src="../images/tabs_l_active_end.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img src="../images/tabs_r_active_end.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img
					src="../images/tabs_r_inactive_end.gif" width="17" height="20"
					border="0"></td>
			</tr>
		</table>

		</td>
		<form method="post" action="appointmentcontrol.jsp">
		<td align="right" valign="bottom"><a href="#"
			onclick="showHideLayers('jumpmenu','','show');"
			onmouseout="showHideLayers('jumpmenu','','hide');"
			title="Click to bring up a menu">Jump to <i>(yyyy-mm-dd)</i>:</a> <INPUT
			TYPE="text" NAME="year" VALUE="" WIDTH="4" HEIGHT="10" border="0"
			size="4" maxlength="4">- <INPUT TYPE="text" NAME="month"
			VALUE="" WIDTH="2" HEIGHT="10" border="0" size="2" maxlength="2">-
		<INPUT TYPE="text" NAME="day" VALUE="" WIDTH="2" HEIGHT="10"
			border="0" size="2" maxlength="2"> <INPUT TYPE="hidden"
			NAME="displaymode" VALUE="week"> <INPUT TYPE="hidden"
			NAME="dboperation" VALUE="searchappointmentweek"> <INPUT
			TYPE="SUBMIT" NAME="Go" VALUE="GO" SIZE="5"></td>
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
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day-7)%>&displaymode=week&dboperation=searchappointmentweek">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0" ALT="View Previous WEEK" vspace="2"></a> <b><span
					CLASS=title><%=strMonthOfYear%>, Week <%=strDayOfWeekInMonth%></span></b>
				<a
					href="appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day+7)%>&displaymode=week&dboperation=searchappointmentweek">
				<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT="View Next WEEK" vspace="2">&nbsp;&nbsp;</a></td>
				<TD ALIGN="center" BGCOLOR="ivory" width="33%"><B>Hello <%= userfirstname+" "+userlastname %>
				</b></TD>
				<td ALIGN="RIGHT" BGCOLOR="Ivory"><a href="../index.html"
					title="Click to return to home"><font face="Arial, Helvetica">StoneChurch
				Family Health Centre&nbsp;</font></a></td>
			</tr>

			<tr>
				<td align="center" VALIGN="TOP" colspan="3" bgcolor="ivory"><!-- table for hours of day start -->
				<table border="1" cellpadding="2" bgcolor="#486ebd" cellspacing="1"
					width="98%">
					<tr>
						<td BGCOLOR="#FOFOFO" WIDTH="5%">&nbsp;</td>
						<%
        	for(int ih=0; ih<7; ih++){
        	
        %>
						<td NOWRAP BGCOLOR="#F0F0F0" VALIGN="TOP" ALIGN="MIDDLE" width=13%><img
							src="../images/clear.gif height=" 32" width="1" border="0">
						<br>
						<font COLOR="RED" FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
						<a
							HREF="/cgi-bin/oicgi.exe/inet_prov_access?option=DAY&DAY=4&MONTH_NUM=2&YEAR=2001&PROVIDER_ID=174&ACCESSKEY=17412092&MONTH_TODAY=2&YEAR_TODAY=2001&DAY_TODAY=7""><font
							COLOR="BLACK" FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><%=arrayDayOfWeek[ih]%>Sun.<br>
						<b>4</b></font></a> <%
        	}
        %>
						
						<td BGCOLOR="#FOFOFO" WIDTH="5%">&nbsp;</td>
					</tr>
					<%
				  int iCols=0, iRows=0, iS=0,iE=0; //for each starting hour, how many events
          int startHour=7, endHour=21, hourCursor=0;
          String am_pm=null;
          boolean bFirstTimeRs=true;

    			String[] param =new String[3];
  				param[0]=curProvider_no;
	 				param[1]=year+"-"+month+"-"+(day-dayOfWeek+1);//e.g."2001-02-02";
	 				param[2]=year+"-"+month+"-"+(day+(7-dayOfWeek));//e.g."2001-02-02";
	  			String strsearchappointmentweek=request.getParameter("dboperation");
            System.out.println("before resultSet: ");
   				ResultSet rs = mainBean.queryResults(param, "searchappointmentweek");

          for(ih=startHour; ih<=endHour; ih++) {
            hourCursor = ih>12?ih-12:ih;
            am_pm = ih<12?"am":"pm";
        %>
					<tr>
						<td align="RIGHT" bgcolor="#3EA4E1" NOWRAP WIDTH="5%"><font
							color="WHITE" face="verdana,arial,helvetica" size="2"><%=(hourCursor+am_pm)%></font></td>

						<td BGCOLOR="#486ebd" ROWSPAN="1" width=13%><a
							HREF="/cgi-bin/oicgi.exe/inet_prov_access?option=DAY&DAY=4&MONTH_NUM=2&YEAR=2001&PROVIDER_ID=174&ACCESSKEY=17412092"
							TITLE="VIEW APPT."><img src="../images/clear.gif" border=0
							width=100% height=100%></A></td>

						<td align="RIGHT" bgcolor="#3EA4E1" width="10%" NOWRAP><b><font
							face="verdana,arial,helvetica" size="2"> <a href=#
							onClick="popupPage('addappointment.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&start_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":00"+am_pm %>&end_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":15"+am_pm%>')"
							title='Click to make an appointment at <%=(hourCursor+":00"+am_pm)%>'
							onMouseOver="window.status='Click to make an appointment at <%=(hourCursor+":00"+am_pm)%>'; return true"
							class="adhour"> <%=(hourCursor+" "+am_pm)%> &nbsp;</a></font></b></td>
						<%
          	while (bFirstTimeRs?rs.next():true) { //if it's not the first time to parse the standard time, should pass it by
          	  iS=Integer.parseInt(rs.getString("start_time").substring(0,2));
          	  if((ih) < iS) { //means no events at this period, get to the next hour
          	  	//out.println("<td width='10'>888</td>");
          	  	bFirstTimeRs=false;
          	  	break;
          	  }
         	    iE=Integer.parseInt(rs.getString("end_time").substring(0,2));
         	    iRows=(iE-iS)+1; //to see if the period across an hour period
         	    
         	    //get time format: 00:00am/pm
         	    String startTime = (iS>12?("0"+(iS-12)):rs.getString("start_time").substring(0,2))+":"+rs.getString("start_time").substring(3,5)+am_pm ; 
         	    String endTime   = (iE>12?("0"+(iE-12)):rs.getString("end_time").substring(0,2))  +":"+rs.getString("end_time").substring(3,5)+(iE<12?"am":"pm");
          	  String name = rs.getString("name");
          	  String hin = rs.getString("hin");
          	  String reason = rs.getString("reason");
          	  String notes = rs.getString("notes");
          	  String status = rs.getString("status");
          	  bFirstTimeRs=true;
        %>
						<td bgcolor="#FDFEC7" rowspan="<%=iRows%>"><a href=#
							onClick="popupPage('appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=startTime%>&displaymode=edit&dboperation=searchappointment')"
							title="REASON=<%=reason%>; NOTES =<%=notes%>; STATUS=<%=status%>">
						<font size="1.75"><b>>></b>&nbsp;<%=startTime%> - <%=endTime%></font></a><font
							size="1.75"> <br>
						<%
        			if(name.equals("")) out.println(".Monitor</font></td>");
        			else {
        			  //System.out.println(name+"/");
				%> <a href=#
							onClick="popupPage('../cpp/cppcontrol.jsp?hin=<%=hin%>&displaymode=edit&dboperation=searchcpp')"
							title="edit the patient record"><%=name%></a></font></td>
						<% 
        			}
          	}
            out.println("<td width='1'></td></tr>");
          }
    			mainBean.closePstmtConn();
				%>
					
				</table>
				</td>
			</tr>
			<tr>
				<td colspan="3">

				<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
					<tr>
						<td BGCOLOR="ivory" width="50%"><a
							href="appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day-1)%>&displaymode=day&dboperation=searchappointmentday">
						&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10"
							HEIGHT="9" BORDER="0" ALT="View Previous DAY" vspace="2"></a> <b><span
							CLASS=title><%=strDayOfWeek%>, <%=strYear%>-<%=strMonth%>-<%=strDay%></span></b>
						<a
							href="appointmentcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day+1)%>&displaymode=day&dboperation=searchappointmentday">
						<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
							ALT="View Next DAY" vspace="2">&nbsp;&nbsp;</a></td>
						<td ALIGN="RIGHT" BGCOLOR="Ivory"><a href="../index.html"
							title="Click to return to home"><font face="Arial, Helvetica">StoneChurch
						Family Health Centre&nbsp;</font></a></td>
					</TR>
				</table>

				</TD>
			</tr>
		</table>
		</td>
	</tr>
</table>

<!-- End sub page table -->
<p></p>
<hr width="100%"></hr>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href="../provider/provideradmin.jsp">&lt;--Back to
		Provider Admin Page</a></td>
		<td align="right"><a href="../logout.jsp">Logout</a></td>
	</tr>
</table>
</body>
</html>
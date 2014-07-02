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
<%!
//multisite starts =====================
private	List<Site> sites;
private boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();

private String getSiteHTML(String reason, List<Site> sites) {
	 if (reason==null||reason.trim().length()==0)
		 return "";
	 else
		 return "<span style='background-color:"+ApptUtil.getColorFromLocation(sites, reason)+"'>"+ApptUtil.getShortNameFromLocation(sites, reason)+"</span>";
}
%>
<%
if (bMultisites) {
	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
	sites = siteDao.getAllSites();
}
//multisite ends =====================
%>
<%

  String user_no = (String) session.getAttribute("user");
  String user_name = (String) session.getAttribute("userlastname")+","+ (String) session.getAttribute("userfirstname");
  boolean bAlternate =(request.getParameter("alternate")!=null&&request.getParameter("alternate").equals("checked") )?true:false;
  int yearLimit = Integer.parseInt(session.getAttribute("schedule_yearlimit") != null ? ((String)session.getAttribute("schedule_yearlimit")) : "10");
  boolean scheduleOverlaps = false;
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<jsp:useBean id="scheduleRscheduleBean" class="oscar.RscheduleBean"	scope="session" />
<jsp:useBean id="scheduleDateBean" class="java.util.Hashtable"	scope="session" />
<jsp:useBean id="scheduleHolidayBean" class="java.util.Hashtable" scope="session" />
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ScheduleDate" %>
<%@ page import="org.oscarehr.common.dao.ScheduleDateDao" %>
<%@ page import="org.oscarehr.common.model.RSchedule" %>
<%@ page import="org.oscarehr.common.dao.RScheduleDao" %>
<%@ page import="org.oscarehr.common.model.ScheduleHoliday" %>
<%@ page import="org.oscarehr.common.dao.ScheduleHolidayDao" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	ScheduleDateDao scheduleDateDao = SpringUtils.getBean(ScheduleDateDao.class);
	RScheduleDao rScheduleDao = (RScheduleDao)SpringUtils.getBean("rScheduleDao");
	ScheduleHolidayDao scheduleHolidayDao = SpringUtils.getBean(ScheduleHolidayDao.class);
%>
<%
  String provider_name = URLDecoder.decode(request.getParameter("provider_name"));
  String provider_no = request.getParameter("provider_no");
  if(provider_no==null || provider_no=="") response.sendRedirect("../logout.jsp");

  //to prepare calendar display
  GregorianCalendar now = new GregorianCalendar();
  int year = now.get(Calendar.YEAR);
  int month = now.get(Calendar.MONTH)+1;
  int day = now.get(Calendar.DATE);
  int delta = 0; //add or minus month
  now = new GregorianCalendar(year,month-1,1);
  String weekdaytag[] = {"SUN","MON","TUE","WED","THU","FRI","SAT"};
  String reasontag[] = {"A7","A1","A2","A3","A4","A5","A6"};

  if(request.getParameter("bFirstDisp")!=null && request.getParameter("bFirstDisp").compareTo("0")==0) {
    year = Integer.parseInt(request.getParameter("year"));
    month = Integer.parseInt(request.getParameter("month"));
    day = Integer.parseInt(request.getParameter("day"));
	  delta = Integer.parseInt(request.getParameter("delta"));
	  now = new GregorianCalendar(year,month-1,1);
  	now.add(Calendar.MONTH, delta);
    year = now.get(Calendar.YEAR);
    month = now.get(Calendar.MONTH)+1;
  }

/////////////////////////////////////
if(request.getParameter("bFirstDisp")==null || request.getParameter("bFirstDisp").compareTo("1")==0) {
  int y = Integer.parseInt(request.getParameter("syear")); //cal.get(Calendar.YEAR);
  int m = Integer.parseInt(request.getParameter("smonth")); //cal.get(Calendar.MONTH)+1;
  int d = Integer.parseInt(request.getParameter("sday")); //cal.get(Calendar.DATE);

  String sdate = MyDateFormat.getMysqlStandardDate(y,m,d );
  String edate = MyDateFormat.getMysqlStandardDate(Integer.parseInt(request.getParameter("eyear")),Integer.parseInt(request.getParameter("emonth")),Integer.parseInt(request.getParameter("eday")) );
  String origEdate;
  if( request.getParameter("origeyear").equals("") )
    origEdate = "1970-01-01";
  else
    origEdate = MyDateFormat.getMysqlStandardDate(Integer.parseInt(request.getParameter("origeyear")),Integer.parseInt(request.getParameter("origemonth")),Integer.parseInt(request.getParameter("origeday")) );

  int rowsAffected = 0;
  if(sdate.equals(scheduleRscheduleBean.sdate) ) {
    List<RSchedule> rsl = rScheduleDao.findByProviderAvailableAndDate(request.getParameter("provider_no"),"1",MyDateFormat.getSysDate(sdate));
	for(RSchedule rs:rsl) {
		rs.setStatus("D");
		rScheduleDao.merge(rs);
	}
	rsl = rScheduleDao.findByProviderAvailableAndDate(request.getParameter("provider_no"),"A",MyDateFormat.getSysDate(sdate));
	for(RSchedule rs:rsl) {
		rs.setStatus("D");
		rScheduleDao.merge(rs);
	}
  }


  Long overLapResult = rScheduleDao.search_rschedule_overlaps(request.getParameter("provider_no"), ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate), 
		ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate),  ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate), 
		ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate),  ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate), 
		ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate),  ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate));
  
  
  scheduleOverlaps = overLapResult > 0;
  

  //if the schedule is the same we are editing instead
  
  Long existsResult = rScheduleDao.search_rschedule_exists(request.getParameter("provider_no"), ConversionUtils.fromDateString(sdate), ConversionUtils.fromDateString(edate));
  boolean editingSchedule = false;
  editingSchedule = existsResult > 0;
  

  //save rschedule data
  if(bAlternate)
    scheduleRscheduleBean.setRscheduleBean(provider_no, sdate,edate,request.getParameter("available"),request.getParameter("day_of_week"), request.getParameter("day_of_weekB") ,request.getParameter("avail_hourB"), request.getParameter("avail_hour"), user_name);
  else
    scheduleRscheduleBean.setRscheduleBean(provider_no, sdate,edate,request.getParameter("available"),request.getParameter("day_of_week"), request.getParameter("avail_hourB"), request.getParameter("avail_hour"), user_name);

  if( editingSchedule ) {
	List<RSchedule> rsl = rScheduleDao.findByProviderAvailableAndDate(scheduleRscheduleBean.provider_no,scheduleRscheduleBean.available,MyDateFormat.getSysDate(scheduleRscheduleBean.sdate));
	for(RSchedule rs:rsl) {
		rs.setDayOfWeek(scheduleRscheduleBean.day_of_week);
		rs.setAvailHourB(scheduleRscheduleBean.avail_hourB);
		rs.setAvailHour(scheduleRscheduleBean.avail_hour);
		rs.setCreator(scheduleRscheduleBean.creator);
		rs.setStatus(scheduleRscheduleBean.active);
		rScheduleDao.merge(rs);
	}
  }
  else {
    RSchedule rs = new RSchedule();
    rs.setProviderNo(scheduleRscheduleBean.provider_no);
    rs.setsDate(MyDateFormat.getSysDate(scheduleRscheduleBean.sdate));
    rs.seteDate(MyDateFormat.getSysDate(scheduleRscheduleBean.edate));
    rs.setAvailable(scheduleRscheduleBean.available);
    rs.setDayOfWeek(scheduleRscheduleBean.day_of_week);
    rs.setAvailHourB(scheduleRscheduleBean.avail_hourB);
    rs.setAvailHour(scheduleRscheduleBean.avail_hour);
    rs.setCreator(scheduleRscheduleBean.creator);
    rs.setStatus(scheduleRscheduleBean.active);
    rScheduleDao.persist(rs);
  }

  //create scheduledate record and initial scheduleDateBean
  scheduleDateBean.clear();
  for(ScheduleDate sd:scheduleDateDao.search_scheduledate_c(request.getParameter("provider_no"))) {
    scheduleDateBean.put(ConversionUtils.toDateString(sd.getDate()), new HScheduleDate(String.valueOf(sd.getAvailable()), String.valueOf(sd.getPriority()),sd.getReason(),sd.getHour(), sd.getCreator() ));
  }

  //initial scheduleHolidayBean record
  if(scheduleHolidayBean.isEmpty() ) {
	for(ScheduleHoliday sh : scheduleHolidayDao.findAll()) {
      scheduleHolidayBean.put(ConversionUtils.toDateString(sh.getId()), new HScheduleHoliday(sh.getHolidayName() ));
    }
  }

  //create scheduledate record by 'b' rate
  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  java.util.Date dnewEdate = df.parse(edate);
  java.util.Date dorigEdate = df.parse(origEdate);

  List<ScheduleDate> sds = scheduleDateDao.findByProviderPriorityAndDateRange(provider_no,'b',MyDateFormat.getSysDate(sdate), MyDateFormat.getSysDate(dnewEdate.before(dorigEdate) ? origEdate : edate));
  for(ScheduleDate sd:sds) {
	  sd.setStatus('D');
	  scheduleDateDao.merge(sd);
  }

  String[] param3 =new String[8];
  GregorianCalendar cal = new GregorianCalendar(y,m-1,d);
//  GregorianCalendar cal = new GregorianCalendar(year,month-1,1);
  for(int i=0;i<365*yearLimit;i++) {
    y = cal.get(Calendar.YEAR);
    m = cal.get(Calendar.MONTH)+1;
    d = cal.get(Calendar.DATE);
    if(scheduleDateBean.get(y+"-"+MyDateFormat.getDigitalXX(m)+"-"+MyDateFormat.getDigitalXX(d)) ==null && scheduleRscheduleBean.getDateAvail(cal)) {
      ScheduleDate sd = new ScheduleDate();
      sd.setDate(MyDateFormat.getSysDate(y+"-"+m+"-"+d));
      sd.setProviderNo(provider_no);
      sd.setAvailable('1');
      sd.setPriority('b');
      sd.setReason(scheduleRscheduleBean.getSiteAvail(cal));
      sd.setHour(scheduleRscheduleBean.getDateAvailHour(cal));
      sd.setCreator(user_name);
      sd.setStatus(scheduleRscheduleBean.active.toCharArray()[0]);
      scheduleDateDao.persist(sd);


    }
    if((y+"-"+MyDateFormat.getDigitalXX(m)+"-"+MyDateFormat.getDigitalXX(d)).equals(edate)) break;
        cal.add(Calendar.DATE, 1);
  }

}

/////////////////////////////////////

%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="oscar.appt.ApptUtil"%>
<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="schedule.schedulecreatedate.title" /></title>
<link rel="stylesheet" href="../web.css" />

<script language="JavaScript">
<!--
function setfocus() {
  this.focus();
  //document.schedule.keyword.focus();
  //document.schedule.keyword.select();
}

function refresh() {
  history.go(0);//
  //window.location.reload(); //
}

//-->
</script>
</head>
<body bgcolor="ivory" bgproperties="fixed" onLoad="setfocus()"
	topmargin="0" leftmargin="0" rightmargin="0">
<form method="post" name="schedule" action="scheduledatefinal.jsp">

<table border="0" width="100%">
	<tr>
		<td width="128" bgcolor="#009966"><!--left column-->
		<table border="0" cellspacing="0" cellpadding="0" width="100%">
			<tr bgcolor="#486ebd">
				<th align="CENTER" bgcolor="#009966">
				<p>&nbsp;</p>
				<p><font face="Helvetica" color="#FFFFFF"><bean:message
					key="schedule.schedulecreatedate.msgMainLabel" /></font></p>
				</th>
			</tr>
		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>
				<p>&nbsp;</p>
				<p><font size="-1"><bean:message
					key="schedule.schedulecreatedate.msgStepOne" /></font></p>
				<p><font size="-1"><bean:message
					key="schedule.schedulecreatedate.msgStepTwo" /></font></p>
				<p><font size="-1"><bean:message
					key="schedule.schedulecreatedate.msgStepThree" /></font></p>
				<p><font size="-1"><bean:message
					key="schedule.schedulecreatedate.msgStepFour" /></font></p>
				<p><font size="-1"><bean:message
					key="schedule.schedulecreatedate.msgStepFive" /></font></p>
				<p>&nbsp;</p>
				<p>&nbsp;</p>
				</td>
			</tr>
		</table>

		</td>
		<td><br>
		<b><%=provider_name%></b> &nbsp; &nbsp; <font size="-1"><bean:message
			key="schedule.schedulecreatedate.msgEffective" />&nbsp;<b>(<%=scheduleRscheduleBean.sdate +" - "+scheduleRscheduleBean.edate%>)</b></font>
		<center>
		<%
	//now = new GregorianCalendar(year, month+1, 1);
  now.add(Calendar.DATE, -1);
            DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
            int [][] dateGrid = aDate.getMonthDateGrid();
%>
		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
			<%
            if( scheduleOverlaps ) {
          %>
			<tr>
				<td style="color: red"><bean:message
					key="schedule.schedulecreatedate.msgConflict" /></td>
			</tr>
			<%
            }
         %>
			<tr>
				<td BGCOLOR="#CCFFCC" width="50%" align="center"><a
					href="schedulecreatedate.jsp?provider_no=<%=provider_no%>&provider_name=<%=URLEncoder.encode(provider_name)%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&delta=-1&bFirstDisp=0">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0"
					ALT='<bean:message key="schedule.schedulecreatedate.btnLastMonthTip"/>'
					vspace="2"> <bean:message
					key="schedule.schedulecreatedate.btnLastMonth" />&nbsp;&nbsp; </a> <b><span
					CLASS=title><%=year%>-<%=month%></span></b> <a
					href="schedulecreatedate.jsp?provider_no=<%=provider_no%>&provider_name=<%=URLEncoder.encode(provider_name)%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&delta=1&bFirstDisp=0">
				&nbsp;&nbsp;<bean:message
					key="schedule.schedulecreatedate.btnNextMonth" /><img
					src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT='<bean:message key="schedule.schedulecreatedate.btnNextMonthTip"/>'
					vspace="2">&nbsp;&nbsp;</a></td>
			</TR>
		</table>
		<p>
		<table width="100%" border="1" cellspacing="0" cellpadding="1"
			bgcolor="silver">
			<tr bgcolor="#FOFOFO" align="center">
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
					color="red"><bean:message
					key="schedule.schedulecreatedate.msgSunday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.schedulecreatedate.msgMonday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.schedulecreatedate.msgTuesday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.schedulecreatedate.msgWednesday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.schedulecreatedate.msgThursday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"><bean:message
					key="schedule.schedulecreatedate.msgFriday" /></font></td>
				<td width="12.5%"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"
					color="green"><bean:message
					key="schedule.schedulecreatedate.msgSaturday" /></font></td>
			</tr>

			<%
              HScheduleHoliday aHScheduleHoliday = null;
              HScheduleDate aHScheduleDate = null;
              StringBuffer bgcolor = new StringBuffer();
              StringBuffer strHolidayName = new StringBuffer();
              StringBuffer strHour = new StringBuffer();
              StringBuffer strReason = new StringBuffer();
              for (int i=0; i<dateGrid.length; i++) {
                out.println("<tr>");
                for (int j=0; j<7; j++) {
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    now.add(Calendar.DATE, 1);
                    bgcolor = new StringBuffer("navy"); //default color for absence
                    strHour = new StringBuffer();
                    strReason = new StringBuffer();
                    strHolidayName = new StringBuffer();
                   if(scheduleRscheduleBean.getDateAvail(now) ) {
                      bgcolor = new StringBuffer("white"); //color for attendance
                      strHour = new StringBuffer(SxmlMisc.getXmlContent(scheduleRscheduleBean.getAvailHour(now), weekdaytag[now.get(Calendar.DAY_OF_WEEK)-1]));
                      if (bMultisites)
                    	  strReason.append(SxmlMisc.getXmlContent(scheduleRscheduleBean.getAvailHour(now), reasontag[now.get(Calendar.DAY_OF_WEEK)-1]));
                    }
                    aHScheduleHoliday = (HScheduleHoliday) scheduleHolidayBean.get(year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j]));
                    if (aHScheduleHoliday!=null) {
                      bgcolor = new StringBuffer("pink");
                      strHolidayName = new StringBuffer(aHScheduleHoliday.holiday_name) ;
                    }
                    aHScheduleDate= (HScheduleDate) scheduleDateBean.get(year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j]));
                    if (aHScheduleDate!=null) {
                      bgcolor = new StringBuffer("gold");
                      if(aHScheduleDate.available.equals("0")) bgcolor = new StringBuffer("navy");
                      strHour = new StringBuffer(aHScheduleDate.hour);
                      strReason = new StringBuffer(aHScheduleDate.reason!=null?aHScheduleDate.reason:"");
                    }

            %>
			<td bgcolor='<%=bgcolor.toString()%>'><a href="#"
				onclick="popupPage(260,720,'scheduledatepopup.jsp?provider_no=<%=provider_no%>&year=<%=year%>&month=<%=month%>&day=<%=dateGrid[i][j]%>&bFistDisp=1')">
			<font color="red"><%= dateGrid[i][j] %></font> <font size="-3"
				color="blue"><%=strHolidayName.toString()%></font> <br>
			<font size="-2">&nbsp;<%=strHour.toString()%> <br>
			&nbsp;<%=bMultisites?getSiteHTML(strReason.toString(), sites):strReason.toString()%></font></a></td>
			<%
                  }
                }
                out.println("</tr>");
              }
            %>

		</table>
		<table width="100%" border="0" cellspacing="0" cellpadding="0">
			<tr>
				<td>&nbsp;</td>
			</tr>
			<tr>
				<td bgcolor="#CCFFCC">
				<div align="right"><!--input type="hidden" name="available" value="0"-->
				<input type="hidden" name="provider_no" value="<%=provider_no%>">
				<input type="hidden" name="Submit" value=" Next "> <input
					type="submit"
					value='<bean:message key="schedule.schedulecreatedate.btnNext"/>'>
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
</html:html>

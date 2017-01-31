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

<%@page import="oscar.appt.ApptData"%>
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.model.MyGroup" %>
<%@page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@page import="org.oscarehr.common.model.Appointment" %>
<%@page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@page import="org.oscarehr.common.model.ScheduleTemplate" %>
<%@page import="org.oscarehr.common.model.ScheduleDate" %>
<%@page import="org.oscarehr.common.dao.ScheduleTemplateDao" %>
<%@page import="org.oscarehr.common.model.ScheduleTemplateCode" %>
<%@page import="org.oscarehr.common.dao.ScheduleTemplateCodeDao" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
    MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
    OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
    ScheduleTemplateDao scheduleTemplateDao = SpringUtils.getBean(ScheduleTemplateDao.class);
    ScheduleTemplateCodeDao scheduleTemplateCodeDao = SpringUtils.getBean(ScheduleTemplateCodeDao.class);
%>

<%!
//multisite starts =====================
private boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();	
private JdbcApptImpl jdbc = new JdbcApptImpl();
private List<Site> sites;
private String [] curScheduleMultisite;
private String getSiteHTML(String scDate, String provider_no, List<Site> sites) {
	 if (!bMultisites) return "";
	 String _loc = jdbc.getLocationFromSchedule(scDate, provider_no);
	 return "<span style='background-color:"+ApptUtil.getColorFromLocation(sites, _loc)+"'>"+ApptUtil.getShortNameFromLocation(sites, _loc)+"</span>";	
}
%>
<% if (bMultisites) {
SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
sites = siteDao.getAllSites(); 
}
//multisite ends =======================
%>

<%
  
  ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
  int nStartTime=providerPreference.getStartHour();
  int nEndTime=providerPreference.getEndHour();
  int nStep=providerPreference.getEveryMin();
  String mygroupno = providerPreference.getMyGroupNo();  

  String curProvider_no=request.getParameter("provider_no")!=null?request.getParameter("provider_no"):"174";
  String curDemoNo = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):"";
  String curDemoName = request.getParameter("demographic_name")!=null?request.getParameter("demographic_name"):"";
  String [] param = new String[3];
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*"
	errorPage="../appointment/errorpage.jsp"%>

<jsp:useBean id="DateTimeCodeBean" class="java.util.Hashtable"
	scope="page" />
	
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<%@page import="oscar.appt.JdbcApptImpl"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="oscar.appt.ApptUtil"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="schedule.scheduleflipview.title" /></title>
<link rel="stylesheet" href="../web.css" type="text/css">

<script language="JavaScript">
<!--
function changePro(providerno) {
  a="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no="+providerno+<%=request.getParameter("startDate")!=null?("\"&startDate="+request.getParameter("startDate")+"\""):"\""%>;
  self.location.href = a;
}
function selectprovider(s) {
  a="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no="+s.options[s.selectedIndex].value+<%=request.getParameter("startDate")!=null?("\"&startDate="+request.getParameter("startDate")+"\""):"\""%>;
  self.location.href = a;
}//-->

  


function t(s1,s2,s3,s4,s5,s6, doConfirm, allowDay, allowWeek) {
	if (doConfirm == "Yes") {
        if (confirm("<bean:message key="provider.appointmentProviderAdminDay.confirmBooking"/>")){
        	popupPage(360,680,('../appointment/addappointment.jsp?demographic_no=<%=curDemoNo%>&name=<%=curDemoName%>&provider_no=<%=curProvider_no%>&bFirstDisp=<%=true%>&year='+s1+'&month='+s2+'&day='+s3+'&start_time='+s4+'&end_time='+s5+'&duration='+s6 ) );
        }
	}
	else if (doConfirm == "Day"){
	        if (allowDay == "No") {
	                alert("<bean:message key="provider.appointmentProviderAdminDay.sameDay"/>");
	        }
	        else {
	        	popupPage(360,680,('../appointment/addappointment.jsp?demographic_no=<%=curDemoNo%>&name=<%=curDemoName%>&provider_no=<%=curProvider_no%>&bFirstDisp=<%=true%>&year='+s1+'&month='+s2+'&day='+s3+'&start_time='+s4+'&end_time='+s5+'&duration='+s6 ) );
	        }
	}
	else if (doConfirm == "Wk"){
	        if (allowWeek == "No") {
	                alert("<bean:message key="provider.appointmentProviderAdminDay.sameWeek"/>");
	        }
	        else {
	        	popupPage(360,680,('../appointment/addappointment.jsp?demographic_no=<%=curDemoNo%>&name=<%=curDemoName%>&provider_no=<%=curProvider_no%>&bFirstDisp=<%=true%>&year='+s1+'&month='+s2+'&day='+s3+'&start_time='+s4+'&end_time='+s5+'&duration='+s6 ) );
	        }
	}
	else if( doConfirm == "Onc" ) {
		if( allowDay == "No" ) {
			if( confirm("This is an On Call Urgent appointment.  Are you sure you want to book?") ) {
				popupPage(360,680,('../appointment/addappointment.jsp?demographic_no=<%=curDemoNo%>&name=<%=curDemoName%>&provider_no=<%=curProvider_no%>&bFirstDisp=<%=true%>&year='+s1+'&month='+s2+'&day='+s3+'&start_time='+s4+'&end_time='+s5+'&duration='+s6 ) );
			}
		}
		else {
			popupPage(360,680,('../appointment/addappointment.jsp?demographic_no=<%=curDemoNo%>&name=<%=curDemoName%>&provider_no=<%=curProvider_no%>&bFirstDisp=<%=true%>&year='+s1+'&month='+s2+'&day='+s3+'&start_time='+s4+'&end_time='+s5+'&duration='+s6 ) );
		}
	}
	else {
		popupPage(360,680,('../appointment/addappointment.jsp?demographic_no=<%=curDemoNo%>&name=<%=curDemoName%>&provider_no=<%=curProvider_no%>&bFirstDisp=<%=true%>&year='+s1+'&month='+s2+'&day='+s3+'&start_time='+s4+'&end_time='+s5+'&duration='+s6 ) );
	}
  
}
</SCRIPT>

</head>
<%

	
  //int nStartTime=9, nEndTime=17, nStep = 15;
  int colscode = (nEndTime-nStartTime)*60/nStep;
  String rColor1 = "#FFFFE0", rColor2 = "#FFFFE0", bgcolor = "gold";
  String startDate = request.getParameter("startDate")!=null?request.getParameter("startDate"):"today";
  SimpleDateFormat inform = new SimpleDateFormat ("yyyy-MM-dd", request.getLocale());
  SimpleDateFormat outform = new SimpleDateFormat ("EEE, yyyy/MM/dd", request.getLocale());
  GregorianCalendar now = new GregorianCalendar();

  if(!startDate.equals("today")) now.setTime(inform.parse(startDate));
  GregorianCalendar cal = (GregorianCalendar) now.clone();
  GregorianCalendar lastMonth = (GregorianCalendar) now.clone();
  GregorianCalendar nextMonth = (GregorianCalendar) now.clone();
  lastMonth.add(Calendar.MONTH, -1);
  nextMonth.add(Calendar.MONTH, 1);
  // note: brain-dead calendar numbers months from 0, thus all the +1s in the expressions below
//  String dateString1 = outform.format(inform.parse(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE)) );
%>
<body bgcolor="#999FFF" text="#000000" topmargin="0" leftmargin="0"
	rightmargin="0">


<div style="colur: #FF0000; text-decoration: none"><a
	href="javascript:history.go(-1)"
	style="text-decoration: none; color: #000000">Go Back</a> <a
	href="../provider/providercontrol.jsp"
	style="text-decoration: none; color: #000000">Day Page</a></div>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr align="center" bgcolor="#CCCCFF">
<% if (bMultisites) out.print("<td>Site</td>"); %>	
		<td width="15%" nowrap><a
			href="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no=<%=curProvider_no%>&startDate=<%=lastMonth.get(Calendar.YEAR)+"-"+(lastMonth.get(Calendar.MONTH)+1)+"-"+lastMonth.get(Calendar.DATE)%>"
			title="<bean:message key="schedule.scheduleflipview.msgLastMonth"/>"
			border='0'><img src="../images/previous.gif"></a> <select
			name="provider_no" onChange="selectprovider(this)">
			<%
			
			 
			if(bMultisites) {
				Provider p = providerDao.getProvider(curProvider_no);
				if(p != null) {
					%>
					<option value="<%=p.getProviderNo()%>" <%=p.getProviderNo().equals(curProvider_no)?"selected":""%>><%=Misc.getShortStr( p.getFormattedName(),"",12)%></option>
					<%
				}
			} else {
				List<MyGroup> mgs = myGroupDao.getGroupByGroupNo(mygroupno);
				for(MyGroup mg:mgs) {
					%>
					<option value="<%=mg.getId().getProviderNo()%>" <%=mg.getId().getProviderNo().equals(curProvider_no)?"selected":""%>><%=Misc.getShortStr(mg.getLastName() + "," + mg.getFirstName(),"",12)%></option>
					<%
				}
			}
%>
		</select><a
			href="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no=<%=curProvider_no%>&startDate=<%=nextMonth.get(Calendar.YEAR)+"-"+(nextMonth.get(Calendar.MONTH)+1)+"-"+nextMonth.get(Calendar.DATE)%>"
			title="<bean:message key="schedule.scheduleflipview.msgNextmonth"/>"
			border='0'><img src="../images/next.gif"></a></td>
		<% for(int j=0; j<colscode; j++) { %>
		<td>
		<%  if(nStep<60) { %> <%=j%(60/nStep)==0?""+(j/(60/nStep)+nStartTime):""%>
		<%	} else { //show everyhour %> <%=j+nStartTime%> <%  } %>
		</td>
		<% } %>
	</tr>
	<% 
  cal.add(Calendar.DATE, 31);
  int starttime = 0, endtime = 0;
  StringBuffer hourmin = null;
  int hour = 0, min = 0;
  
  //find the appts above the schedule
  Integer numOfAppts;
  param[0]= curProvider_no;
  param[1]= startDate;
  param[2]= cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
  
  for(Appointment a: appointmentDao.search_appt(ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE)), curProvider_no)) {
 
  	starttime = Integer.parseInt(ConversionUtils.toTimeString(a.getStartTime()).substring(0,2) )*60 + Integer.parseInt(ConversionUtils.toTimeString(a.getStartTime()).substring(3,5) );
  	endtime = Integer.parseInt(ConversionUtils.toTimeString(a.getEndTime()).substring(0,2) )*60 + Integer.parseInt(ConversionUtils.toTimeString(a.getEndTime()).substring(3,5) );
        
    for(int k=nStartTime*60; k<(nEndTime+1)*60; k+=nStep) {
	    if(starttime>k) continue;
	    else {
	      if(endtime>k && !a.getStatus().equals("C")) {
	        hour = k/60;
	        min = k%60;
          	hourmin = new StringBuffer(ConversionUtils.toDateString(a.getAppointmentDate())+ (hour<10?"0":"") +hour + (min<10?":0":":") +min +":00");
		
	        if(DateTimeCodeBean.get(hourmin.toString() ) == null) {
            		//DateTimeCodeBean.put(hourmin.toString(), "-");
                        DateTimeCodeBean.put(hourmin.toString(), 1);
                }
                else {
                    numOfAppts = (Integer)DateTimeCodeBean.get(hourmin.toString());
                    ++numOfAppts;
                    DateTimeCodeBean.put(hourmin.toString(),numOfAppts);
                }
                 
	        continue;
		  } else break; //e<=k
	  }
	}
  }
  
  //store timecode for every available day
  String bgcolordef = "#FFFFE0" ;
  for(Object[] result : scheduleTemplateDao.findSchedules(ConversionUtils.fromDateString(startDate), ConversionUtils.fromDateString(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE)),curProvider_no)) {
	  ScheduleTemplate st = (ScheduleTemplate)result[0];
	  ScheduleDate sd = (ScheduleDate)result[1];
    DateTimeCodeBean.put(ConversionUtils.toDateString(sd.getDate()), st.getTimecode());
  }
  
  //color for template code
  List<ScheduleTemplateCode> stcs = scheduleTemplateCodeDao.findAll();
  Collections.sort(stcs,ScheduleTemplateCode.CodeComparator);
		  
  for (ScheduleTemplateCode stc : stcs) { 
    //DateTimeCodeBean.put("description"+rsdemo.getString("code"), rsdemo.getString("description"));
    DateTimeCodeBean.put("duration"+stc.getCode(), stc.getDuration());
    DateTimeCodeBean.put("color"+stc.getCode(), (stc.getColor()==null || stc.getColor().equals(""))?bgcolordef:stc.getColor() );
    DateTimeCodeBean.put("bookinglimit" + stc.getCode(), String.valueOf(stc.getBookinglimit()));
    DateTimeCodeBean.put("confirm"+stc.getCode(), stc.getConfirm());
  } 

  DateTimeCodeBean.put("color-", "silver");
  DateTimeCodeBean.put("color|", "gold");
  DateTimeCodeBean.put("color||", "red");

  cal.add(Calendar.DATE, -31);
  StringBuffer temp = null;
  String strTempDate = null;

  for(int i=0; i<31; i++) {
    temp = new StringBuffer();
	  bgcolor = cal.get(Calendar.DAY_OF_WEEK)==7||cal.get(Calendar.DAY_OF_WEEK)==1?"#EEEEFF":(i%2==0?rColor1:rColor2);
	  //bgcolor = cal.get(Calendar.DAY_OF_WEEK)==7?"#FFF68F":cal.get(Calendar.DAY_OF_WEEK)==1?"#FFF68F":(i%2==0?rColor1:rColor2);
    temp = temp.append(cal.get(Calendar.YEAR)).append("-").append(cal.get(Calendar.MONTH)+1).append("-").append(cal.get(Calendar.DATE));
    strTempDate = inform.format(inform.parse(temp.toString()));

    /* This calendar will set the end_time of a appointment */
    Calendar appointmentTime = Calendar.getInstance();
    appointmentTime.set(Calendar.HOUR_OF_DAY, nStartTime);
    appointmentTime.set(Calendar.MINUTE, 0);
    /* this -1 is explained below */
    appointmentTime.add(Calendar.MINUTE, -1);
%>
	<tr align="center" bgcolor="<%=bgcolor%>">
<% if (bMultisites) out.print("<td align='right'>"+getSiteHTML(strTempDate, curProvider_no, sites)+"</td>"); %>			
		<td align="right" nowrap><a
			href="<%=request.getParameter("originalpage")%>?year=<%=cal.get(Calendar.YEAR)%>&month=<%=cal.get(Calendar.MONTH)+1%>&day=<%=cal.get(Calendar.DATE)%>&view=0&displaymode=day&dboperation=searchappointmentday"><%=outform.format(inform.parse(strTempDate) )%>&nbsp;</a></td>
		<%
  String bookinglimit;
  String scheduleCode;
  //calculate the ratio by the length of timecode
  for(int j=0; j<colscode; j++) {
        scheduleCode = "";
	hour = (nStartTime*60 + j*nStep)/60;
	min = j*nStep%60;
	/* This appoint will finish one minute before the next appointment
	   To do this minute before, set -1 outside this loop
	 */
	appointmentTime.add(Calendar.MINUTE, nStep);
    if(DateTimeCodeBean.get(MyDateFormat.getMysqlStandardDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE) ))!=null ) {
	  int nLen = 24*60 / ((String) DateTimeCodeBean.get(MyDateFormat.getMysqlStandardDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE) ))).length();
	  int ratio = (hour*60+min)/nLen;
	  temp = new StringBuffer( ((String) DateTimeCodeBean.get(MyDateFormat.getMysqlStandardDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE) ))).substring(ratio,ratio+1) ); //(nStartTime*60*ratio/nStep+j*ratio),(nStartTime*60*ratio/nStep+j*ratio)+1)
          scheduleCode = temp.toString();  
        bookinglimit = String.valueOf(DateTimeCodeBean.get("bookinglimit"+scheduleCode));
        if( bookinglimit == null || bookinglimit.equals("null") ) {
            bookinglimit = "";
        }
	} else {
        temp = new StringBuffer("&nbsp;");
        bookinglimit = "";
    }
                        
        String strNumOfAppts = "";
        int limitDelta = 0;
        int limit = bookinglimit.length() > 0 ? Integer.parseInt(bookinglimit) : 1;
        hourmin = new StringBuffer(strTempDate+ (hour<10?"0":"") +hour + (min<10?":0":":") +min +":00");
	if(DateTimeCodeBean.get(hourmin.toString() ) != null) {
	  numOfAppts = (Integer) DateTimeCodeBean.get(hourmin.toString());
          strNumOfAppts = String.valueOf(numOfAppts);
          limitDelta = limit - numOfAppts;
          if( limitDelta == 0 ) {
            temp = new StringBuffer("-");
          }
          else if( limitDelta == -1 ) {
            temp = new StringBuffer("|");
          }
          else if( limitDelta <= -2 ) {
            temp = new StringBuffer("||");
          }
  	
	}
        
	Calendar minDate = Calendar.getInstance();
	minDate.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
	minDate.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
	minDate.set(Calendar.SECOND, cal.get(Calendar.SECOND));
	minDate.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));
	
	String allowDay = "";
	if (cal.equals(minDate)) {
	    allowDay = "Yes";
	    } else {
	    allowDay = "No";
	}
	minDate.add(Calendar.DATE, 7);
	String allowWeek = "";
	if (cal.before(minDate)) {
	    allowWeek = "Yes";
	    } else {
	    allowWeek = "No";
	}

%>
		<td
			<%=DateTimeCodeBean.get("color"+temp.toString())!=null?("bgcolor="+DateTimeCodeBean.get("color"+temp.toString()) ):""%>
                            title="<%=hour+":"+(min<10?"0":"")+min%>"><table style="display:inline; font-size:x-small;"><tr><td rowspan="2" style="vertical-align:middle;"><a href=#
			onClick="t(<%=cal.get(Calendar.YEAR)%>,<%=cal.get(Calendar.MONTH)+1%>,<%=cal.get(Calendar.DATE)%>,'<%=(hour<10?"0":"")+hour+":"+(min<10?"0":"")+min %>','<%=appointmentTime.get(Calendar.HOUR_OF_DAY)%>:<%=appointmentTime.get(Calendar.MINUTE)%>','<%=DateTimeCodeBean.get("duration"+temp.toString())%>','<%=DateTimeCodeBean.get("confirm"+scheduleCode)%>','<%=allowDay%>','<%=allowWeek%>');return false;">
                    <%=temp.toString()%></a></td><td title="<bean:message key="schedule.scheduleflipview.msgbookings"/>" style="vertical-align:top; font-size: x-small;"><%=strNumOfAppts%></td></tr><tr><td style="vertical-align:bottom; font-size: x-small;" title="<bean:message key="schedule.scheduleflipview.msgbookinglimit"/>"><%=bookinglimit%></td></tr></table></td>
		<%
  }
%>
	</tr>
	<%
    cal.add(Calendar.DATE, 1);
  }
%>

</table>
<a
	href="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no=<%=curProvider_no%>&startDate=<%=lastMonth.get(Calendar.YEAR)+"-"+(lastMonth.get(Calendar.MONTH)+1)+"-"+lastMonth.get(Calendar.DATE)%>"><bean:message
	key="schedule.scheduleflipview.btnLastMonth" /> </a>
|
<a
	href="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no=<%=curProvider_no%>&startDate=<%=nextMonth.get(Calendar.YEAR)+"-"+(nextMonth.get(Calendar.MONTH)+1)+"-"+nextMonth.get(Calendar.DATE)%>"><bean:message
	key="schedule.scheduleflipview.btnNextMonth" /></a>
</body>
</html:html>

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
  
  int nStartTime=Integer.parseInt(((String) session.getAttribute("starthour")).trim());
  int nEndTime=Integer.parseInt(((String) session.getAttribute("endhour")).trim());
  int nStep=Integer.parseInt(((String) session.getAttribute("everymin")).trim());
  String mygroupno = (String) session.getAttribute("groupno");  

  String curProvider_no=request.getParameter("provider_no")!=null?request.getParameter("provider_no"):"174";
  String curDemoNo = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):"";
  String curDemoName = request.getParameter("demographic_name")!=null?request.getParameter("demographic_name"):"";
  String [] param = new String[3];
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*" errorPage="../appointment/errorpage.jsp" %>
<jsp:useBean id="flipviewMainBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="DateTimeCodeBean" class="java.util.Hashtable" scope="page" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
  String [][] dbQueries=new String[][] { 
    {"search_timecode", "select scheduletemplate.timecode, scheduledate.sdate from scheduletemplate, scheduledate where scheduletemplate.name=scheduledate.hour and scheduledate.provider_no=? and scheduledate.sdate>=? and scheduledate.sdate<=? and scheduledate.status = 'A' and (scheduletemplate.provider_no=scheduledate.provider_no or scheduletemplate.provider_no='Public') order by scheduledate.sdate"}, 
    {"search_appt", "select * from appointment where provider_no=? and appointment_date>=? and appointment_date<=? order by appointment_date, start_time, end_time"}, 
    {"searchmygroupprovider", "select provider_no, last_name, first_name from mygroup where mygroup_no=? order by first_name"}, 
    {"search_timecodesingle", "select * from scheduletemplatecode order by code"}, 
  };
  String[][] responseTargets=new String[][] {  };
  flipviewMainBean.doConfigure(dbParams,dbQueries,responseTargets);
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<html:html locale="true">
<head>
<title><bean:message key="schedule.scheduleflipview.title"/></title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
<meta http-equiv=Expires content=-1>
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

  


function t(s1,s2,s3,s4,s5,s6) {
  popupPage(360,680,('../appointment/addappointment.jsp?demographic_no=<%=curDemoNo%>&name=<%=curDemoName%>&provider_no=<%=curProvider_no%>&bFirstDisp=<%=true%>&year='+s1+'&month='+s2+'&day='+s3+'&start_time='+s4+'&end_time='+s5+'&duration='+s6 ) );
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
<body bgcolor="#999FFF" text="#000000" topmargin="0" leftmargin="0" rightmargin="0">
<%--
  ResultSet rsdemo = flipviewMainBean.queryResults(mygroupno, "searchmygroupprovider");
  while (rsdemo.next()) { 
    if(rsdemo.getString("provider_no").equals(curProvider_no) ) {
--%>
<%--=rsdemo.getString("first_name")+" "+rsdemo.getString("last_name")--%>
<%--  } else { --%>
<!--a href=# onClick="changePro(<%--=rsdemo.getString("provider_no")--%>)" -->
  <!--font color='silver'--><%--=rsdemo.getString("first_name")+" "+rsdemo.getString("last_name")--%><!--/font></a-->
<%-- }  } --%>

<div style="colur:#FF0000;text-decoration: none">
<a href="javascript:history.go(-1)" style="text-decoration: none;color: #000000">Go Back</a>
<a href="../provider/providercontrol.jsp" style="text-decoration: none;color: #000000">Day Page</a>
</div>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
  <tr align="center" bgcolor="#CCCCFF"> 
    <td width="15%" nowrap>
	<a href="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no=<%=curProvider_no%>&startDate=<%=lastMonth.get(Calendar.YEAR)+"-"+(lastMonth.get(Calendar.MONTH)+1)+"-"+lastMonth.get(Calendar.DATE)%>" title="<bean:message key="schedule.scheduleflipview.msgLastMonth"/>" border='0'><img src="../images/previous.gif"></a>
    <select name="provider_no" onChange="selectprovider(this)">
<%
  ResultSet rsdemo = flipviewMainBean.queryResults(mygroupno, "searchmygroupprovider");
  while (rsdemo.next()) { 
%>
  <option value="<%=rsdemo.getString("provider_no")%>" <%=rsdemo.getString("provider_no").equals(curProvider_no)?"selected":""%> >
  <%=Misc.getShortStr( (rsdemo.getString("last_name")+","+rsdemo.getString("first_name")),"",12)%></option>
<%
  }
%>
    </select><a href="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no=<%=curProvider_no%>&startDate=<%=nextMonth.get(Calendar.YEAR)+"-"+(nextMonth.get(Calendar.MONTH)+1)+"-"+nextMonth.get(Calendar.DATE)%>" title="<bean:message key="schedule.scheduleflipview.msgNextmonth"/>" border='0'><img src="../images/next.gif"></a></td>
<% for(int j=0; j<colscode; j++) { %>
<td>
<%  if(nStep<60) { %>	
	  <%=j%(60/nStep)==0?""+(j/(60/nStep)+nStartTime):""%>
<%	} else { //show everyhour %>
      <%=j+nStartTime%>
<%  } %>  
	  </td>
<% } %>  
  </tr>
<% 
  cal.add(cal.DATE, 31);
  int starttime = 0, endtime = 0;
  StringBuffer hourmin = null;
  int hour = 0, min = 0;
  
  //find the appts above the schedule
  param[0]= curProvider_no;
  param[1]= startDate;
  param[2]= cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DATE);
  rsdemo = flipviewMainBean.queryResults(param, "search_appt");
  while (rsdemo.next()) { 
  	starttime = Integer.parseInt(rsdemo.getString("start_time").substring(0,2) )*60 + Integer.parseInt(rsdemo.getString("start_time").substring(3,5) );
  	endtime = Integer.parseInt(rsdemo.getString("end_time").substring(0,2) )*60 + Integer.parseInt(rsdemo.getString("end_time").substring(3,5) );
    for(int k=nStartTime*60; k<(nEndTime+1)*60; k+=nStep) {
	    if(starttime>k) continue;
	    else {
	      if(endtime>k && !rsdemo.getString("status").equals("C")) {
	        hour = k/60;
	        min = k%60;
          	hourmin = new StringBuffer(rsdemo.getString("appointment_date")+ (hour<10?"0":"") +hour + (min<10?":0":":") +min +":00");
		
	        if(DateTimeCodeBean.get(hourmin.toString() ) == null) 
            		DateTimeCodeBean.put(hourmin.toString(), "-");
          	else if(DateTimeCodeBean.get(hourmin.toString() ).equals("-") ) 
            		DateTimeCodeBean.put(hourmin.toString(), "||");
          	else
            		DateTimeCodeBean.put(hourmin.toString(), "|||");
          		//System.out.println(hourmin.toString()+"*wwwwwww*"+rsdemo.getString("appointment_date")+rsdemo.getString("start_time")+rsdemo.getString("end_time"));
	        continue;
		  } else break; //e<=k
	  }
	}
//    System.out.println(rsdemo.getString("appointment_date")+rsdemo.getString("start_time")+rsdemo.getString("end_time"));
  }
  
  //store timecode for every available day
  String bgcolordef = "#FFFFE0" ;
  rsdemo = flipviewMainBean.queryResults(param, "search_timecode");
  while (rsdemo.next()) { 
    DateTimeCodeBean.put(rsdemo.getString("sdate"), rsdemo.getString("timecode"));
//    System.out.println(param[1]+"-----"+rsdemo.getString("sdate")+rsdemo.getString("timecode"));
  }
  
  //color for template code
  rsdemo = flipviewMainBean.queryResults("search_timecodesingle");
  while (rsdemo.next()) { 
    //DateTimeCodeBean.put("description"+rsdemo.getString("code"), rsdemo.getString("description"));
    DateTimeCodeBean.put("duration"+rsdemo.getString("code"), rsdemo.getString("duration"));
    DateTimeCodeBean.put("color"+rsdemo.getString("code"), (rsdemo.getString("color")==null || rsdemo.getString("color").equals(""))?bgcolordef:rsdemo.getString("color") );
  } 
  flipviewMainBean.closePstmtConn();
  DateTimeCodeBean.put("color-", "silver");
  DateTimeCodeBean.put("color||", "gold");
  DateTimeCodeBean.put("color|||", "red");

  cal.add(cal.DATE, -31);
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
    appointmentTime.set(appointmentTime.HOUR_OF_DAY, nStartTime);
    appointmentTime.set(appointmentTime.MINUTE, 0);
    /* this -1 is explained below */
    appointmentTime.add(appointmentTime.MINUTE, -1);
%>  
	<tr align="center" bgcolor="<%=bgcolor%>"><td align="right" nowrap>
	<a href="<%=request.getParameter("originalpage")%>?year=<%=cal.get(Calendar.YEAR)%>&month=<%=cal.get(Calendar.MONTH)+1%>&day=<%=cal.get(Calendar.DATE)%>&view=0&displaymode=day&dboperation=searchappointmentday"><%=outform.format(inform.parse(strTempDate) )%>&nbsp;</a></td>
<%
  //calculate the ratio by the length of timecode
  for(int j=0; j<colscode; j++) {
	hour = (nStartTime*60 + j*nStep)/60;
	min = j*nStep%60;
	/* This appoint will finish one minute before the next appointment
	   To do this minute before, set -1 outside this loop
	 */
	appointmentTime.add(appointmentTime.MINUTE, nStep);
    if(DateTimeCodeBean.get(MyDateFormat.getMysqlStandardDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE) ))!=null ) {
	  int nLen = 24*60 / ((String) DateTimeCodeBean.get(MyDateFormat.getMysqlStandardDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE) ))).length();
	  int ratio = (hour*60+min)/nLen;
	  //System.out.println((hour*60+min)+"  "+nLen +"  "+ratio);
	  temp = new StringBuffer( ((String) DateTimeCodeBean.get(MyDateFormat.getMysqlStandardDate(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DATE) ))).substring(ratio,ratio+1) ); //(nStartTime*60*ratio/nStep+j*ratio),(nStartTime*60*ratio/nStep+j*ratio)+1)
	} else temp = new StringBuffer("&nbsp;");
    hourmin = new StringBuffer(strTempDate+ (hour<10?"0":"") +hour + (min<10?":0":":") +min +":00");
//System.out.println(hourmin.toString());
	if(DateTimeCodeBean.get(hourmin.toString() ) != null) {
	  String aTem = (String) DateTimeCodeBean.get(hourmin.toString()) ;
  	temp = new StringBuffer( aTem );
//	temp = new StringBuffer("-");
//System.out.println("temp"+temp);
	}
%>  
    <td <%=DateTimeCodeBean.get("color"+temp.toString())!=null?("bgcolor="+DateTimeCodeBean.get("color"+temp.toString()) ):""%> title="<%=hour+":"+(min<10?"0":"")+min%>">
	<a href=# onClick ="t(<%=cal.get(Calendar.YEAR)%>,<%=cal.get(Calendar.MONTH)+1%>,<%=cal.get(Calendar.DATE)%>,'<%=(hour<10?"0":"")+hour+":"+(min<10?"0":"")+min %>','<%=appointmentTime.get(appointmentTime.HOUR_OF_DAY)%>:<%=appointmentTime.get(appointmentTime.MINUTE)%>','<%=DateTimeCodeBean.get("duration"+temp.toString())%>');return false;" >
<%=temp.toString()%></a></td>
<%
  }
%>  
  </tr>
<%
    cal.add(cal.DATE, 1);
  }
%>  
  
</table>
<a href="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no=<%=curProvider_no%>&startDate=<%=lastMonth.get(Calendar.YEAR)+"-"+(lastMonth.get(Calendar.MONTH)+1)+"-"+lastMonth.get(Calendar.DATE)%>"><bean:message key="schedule.scheduleflipview.btnLastMonth"/> </a> |
<a href="scheduleflipview.jsp?originalpage=<%=request.getParameter("originalpage")%>&provider_no=<%=curProvider_no%>&startDate=<%=nextMonth.get(Calendar.YEAR)+"-"+(nextMonth.get(Calendar.MONTH)+1)+"-"+nextMonth.get(Calendar.DATE)%>"><bean:message key="schedule.scheduleflipview.btnNextMonth"/></a>
</body>
</html:html>

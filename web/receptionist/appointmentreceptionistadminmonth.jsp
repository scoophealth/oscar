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

<%@ taglib uri="/WEB-INF/msg-tag.tld" prefix="oscarmessage" %>
<%@ page import="java.lang.*, java.util.*, java.text.*,java.sql.*,oscar.*" errorPage="errorpage.jsp" %>

<%
  if(session.getValue("user") == null) response.sendRedirect("../logout.jsp");
  String curUser_no, curProvider_no,userfirstname,userlastname, mygroupno;
  curProvider_no = (String) session.getAttribute("user");

  curUser_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  mygroupno = (String) session.getAttribute("groupno");  
  int startHour=Integer.parseInt((String) session.getAttribute("starthour"));
  int endHour=Integer.parseInt((String) session.getAttribute("endhour"));
  int everyMin=Integer.parseInt((String) session.getAttribute("everymin"));
  int view=0;
%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="scheduleHolidayBean" class="java.util.Hashtable" scope="session" />
<jsp:useBean id="myGrpBean" class="java.util.Properties" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />

<%
  String resourcebaseurl = "http://67.69.12.117:8080/oscarResource/";
  ResultSet rsgroup1 = apptMainBean.queryResults("resource_baseurl", "search_resource_baseurl");
  while (rsgroup1.next()) { 
 	  resourcebaseurl = rsgroup1.getString("value");
  }

  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR); //curYear should be the real now date
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int year = Integer.parseInt(request.getParameter("year"));
  int month = Integer.parseInt(request.getParameter("month"));
  int day = Integer.parseInt(request.getParameter("day"));
  String strYear=null, strMonth=null, strDay=null;
  String strDayOfWeek=null;
  String[] arrayDayOfWeek = new String[] {"Sun","Mon","Tue","Wed","Thu","Fri","Sat" };
  String[] arrayMonthOfYear = new String[] {
    "January","February","March","April","May","June","July","August","September","October","November","December" };

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

  //initial holiday bean
  ResultSet rsgroup = null;
  if(scheduleHolidayBean.isEmpty() ) {
    rsgroup = apptMainBean.queryResults(((year-1)+"-"+month+"-01"),"search_scheduleholiday");
    while (rsgroup.next()) { 
      scheduleHolidayBean.put(rsgroup.getString("sdate"), new HScheduleHoliday(rsgroup.getString("holiday_name") ));
    }
  }
  //declare display schedule string
  StringBuffer bgcolor = new StringBuffer();
  StringBuffer strHolidayName = new StringBuffer();
  StringBuffer strHour = new StringBuffer();
  StringBuffer strReason = new StringBuffer();
  HScheduleHoliday aHScheduleHoliday = null;
  HScheduleDate aHScheduleDate = null;
  
  if(providerBean.get(mygroupno) != null) { //single appointed provider in the group
    myGrpBean.setProperty(mygroupno, mygroupno);
  } else {
    if(!mygroupno.equals("all")) { //find group members
      rsgroup = apptMainBean.queryResults(mygroupno, "searchmygroupprovider");
      while (rsgroup.next()) { 
        myGrpBean.setProperty(rsgroup.getString("provider_no"), mygroupno);
      }
    } else { //find all providers
      // the following line works only on MySQL. Should not be used
      // rsgroup = apptMainBean.queryResults("last_name", "searchallprovider");
      rsgroup = apptMainBean.queryResults( "searchallprovider");

 	    while (rsgroup.next()) { 
 	      myGrpBean.setProperty(rsgroup.getString("provider_no"), new String( rsgroup.getString("last_name")+","+rsgroup.getString("first_name") ));
 	    }
    }
  }

%>

<html>
<head>
<title>Receptionist Appointment Access</title>
<link rel="stylesheet" href="receptionistapptstyle.css" type="text/css">
      <meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
      <meta http-equiv="Pragma" content="no-cache">
</head>
<script language="JavaScript">
<!--
function setfocus() {
  document.jumptodate.year.focus();
  document.jumptodate.year.select();
}

function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=0,left=0";//360,680
  var popup=window.open(page, "apptReception", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}

function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscarRx", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}

function popupPage2(varpage) {
var page = "" + varpage;
windowprops = "height=550,width=678,location=no,"
+ "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=15,left=15";
window.open(page, "Popup", windowprops);
}
function selectprovider(s) {
  if(s.options[s.selectedIndex].value.indexOf("_grp_")!=-1 ) {
	  var newGroupNo = s.options[s.selectedIndex].value.substring(5) ;
	  popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
  } else {
	  var newGroupNo = s.options[s.selectedIndex].value ;
	  popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
	}

}

function refresh() {
  history.go(0);
}
function refresh1() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}
//-->
</SCRIPT>
<body background="../images/gray_bg.jpg" bgproperties="fixed" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
<tr>
  <td VALIGN="BOTTOM" HEIGHT="20"> 

    <table BORDER="0" CELLPADDING="0" CELLSPACING="0" height="20">
      <tr>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap height="20"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a href="receptionistcontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday" TITLE='View your daily schedule' OnMouseOver="window.status='View your daily schedule' ; return true"> 
         &nbsp;&nbsp;Day&nbsp;&nbsp; </a></font></td>
        <td></td><td rowspan="2" BGCOLOR="ivory" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a href="receptionistcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth"   TITLE='View your monthly template' OnMouseOver="window.status='View your monthly template' ; return true">Month</a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a href="#" ONCLICK ="popupPage(550,800,'<%=resourcebaseurl%>');return false;" title="Manage Clinical Resource" >Resource</a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupPage2('../demographic/search.jsp');return false;"  TITLE='Search for patient records' OnMouseOver="window.status='Search for patient records' ; return true">Search</a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupPage2('../billing/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;" TITLE='Generate a billing report' onmouseover="window.status='Generate a billing report';return true">Billing</a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a HREF="#" ONCLICK ="popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=userfirstname%>%20<%=userlastname%>')">
         <oscarmessage:newMessage providerNo="<%=curUser_no%>"/></a></font></td>
        <td></td><td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">
         <a href=# onClick ="popupPage(200,680,'receptionistpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>');return false;">Pref</a></font></td>
        <td></td>
      </tr><tr>
        <td valign="bottom"><img src="../images/tabs_l_inactive_end.gif" width="14" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_l_active_end.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_r_active_end.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_both_inactive.gif" width="15" height="20" border="0"></td>
        <td valign="bottom"><img src="../images/tabs_r_inactive_end.gif" width="17" height="20" border="0"></td>
      </tr>
    </table>

  </td>
  <form method="post" name="jumptodate" action="receptionistcontrol.jsp">
  <td align="right" valign="bottom">
   <INPUT TYPE="text" NAME="year" VALUE="<%=strYear%>" WIDTH="4"  border="0"  size="4" maxlength="4">-
   <INPUT TYPE="text" NAME="month" VALUE="<%=strMonth%>" WIDTH="2"  border="0" size="2" maxlength="2">-
   <INPUT TYPE="text" NAME="day" VALUE="<%=strDay%>" WIDTH="2"  border="0" size="2" maxlength="2">
   <INPUT TYPE="hidden" NAME="view" VALUE="<%=view%>" >
   <INPUT TYPE="hidden" NAME="curProvider" VALUE="<%=request.getParameter("curProvider")%>" >
   <INPUT TYPE="hidden" NAME="curProviderName" VALUE="<%=request.getParameter("curProviderName")%>" >
   <INPUT TYPE="hidden" NAME="displaymode" VALUE="day" >
   <INPUT TYPE="hidden" NAME="dboperation" VALUE="searchappointmentday" >
   <INPUT TYPE="SUBMIT" NAME="Go" VALUE="GO" SIZE="5">
  </td></form>
</tr>
</table>

<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C0C0C0">
  <tr><td>
    <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
      <tr BGCOLOR="ivory">
        <td width="33%">
         <a href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month-1)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth">
         &nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="<%=arrayMonthOfYear[((month+10)%12)]%>" vspace="2"></a> 
         <b><span CLASS=title><%=strYear%>-<%=strMonth%></span></b>
         <a href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month+1)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth">
         <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="<%=arrayMonthOfYear[month%12]%>" vspace="2">&nbsp;&nbsp;</a></td>
        <TD ALIGN="center" width="33%"><B><%= arrayMonthOfYear[(month+11)%12] %> </b> </TD>
        <td ALIGN="RIGHT"><b>View:</b> 
  <select name="provider_no" onChange="selectprovider(this)">
  <option value="all"  >All Doctors</option>
<% rsgroup = apptMainBean.queryResults("searchmygroupno");
 	 while (rsgroup.next()) { 
%>
  <option value="<%="_grp_"+rsgroup.getString("mygroup_no")%>" <%=mygroupno.equals(rsgroup.getString("mygroup_no"))?"selected":""%> >GRP: <%=rsgroup.getString("mygroup_no")%></option>
<% } %>
  
<% rsgroup = apptMainBean.queryResults("searchprovider");
 	 while (rsgroup.next()) { 
%>
  <option value="<%=rsgroup.getString("provider_no")%>" <%=mygroupno.equals(rsgroup.getString("provider_no"))?"selected":""%> ><%=rsgroup.getString("last_name")+", "+rsgroup.getString("first_name")%></option>
<%
 	 }
%>
                </select>
         <a href="../logout.jsp">Log Out <img src="../images/next.gif"  border="0" width="10" height="9" align="absmiddle"> &nbsp;</a> </td>
      </tr>
      <tr>
        <td align="center" VALIGN="TOP" colspan="3" bgcolor="ivory"> 

				<%
            DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
            int [][] dateGrid = aDate.getMonthDateGrid();
        %>

          <table width="98%" border="1" cellspacing="0" cellpadding="2"  bgcolor="silver" >
            <tr bgcolor="#FOFOFO" align="center"> 
              <td width="14.2%"><font SIZE="2" color="red">Sun</font></td>
              <td width="14.3%"><font SIZE="2">Mon</font></td>
              <td width="14.3%"><font SIZE="2">Tue</font></td>
              <td width="14.3%"><font SIZE="2">Wed</font></td>
              <td width="14.3%"><font SIZE="2">Thu</font></td>
              <td width="14.3%"><font SIZE="2">Fri</font></td>
              <td width="14.2%"><font SIZE="2" color="green">Sat</font></td>
            </tr>
            
<%
    String[] param = new String[2];
    boolean bFistEntry = true;
   	GregorianCalendar cal = new GregorianCalendar(year,(month-1),1);
   	cal.add(cal.MONTH,1);
    if(providerBean.get(mygroupno) == null) { //it is a real group defined by users
      param[0] = year+"-"+month+"-"+"1";
      param[1] = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"1";
      rsgroup = apptMainBean.queryResults(param,"search_scheduledate_datep");
    } else {
      String[] param1 = new String[3];
      param1[0] = year+"-"+month+"-"+"1";
      param1[1] = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"1";
      param1[2] = mygroupno;
      rsgroup = apptMainBean.queryResults(param1,"search_scheduledate_singlep");
    }
              for (int i=0; i<dateGrid.length; i++) {
                out.println("</tr>");
                for (int j=0; j<7; j++) {
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    bgcolor = new StringBuffer("ivory"); //default color for absence
                    strHour = new StringBuffer();
                    strReason = new StringBuffer();
                    strHolidayName = new StringBuffer();
                    aHScheduleHoliday = (HScheduleHoliday) scheduleHolidayBean.get(year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j]));
                    if (aHScheduleHoliday!=null) {
                      bgcolor = new StringBuffer("pink");
                      strHolidayName = new StringBuffer(aHScheduleHoliday.holiday_name) ;
                    }
                 
            %>
                      <td nowrap bgcolor="<%=bgcolor.toString()%>" valign="top"><a href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'> 
                      <span class='date'>&nbsp;<%=dateGrid[i][j] %> </span>
                      <font size="-2" color="blue"><%=strHolidayName.toString()%></font>
<%
    while (bFistEntry?rsgroup.next():true) { 
    if(!rsgroup.getString("sdate").equals(year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j])) ) {
      bFistEntry = false;
      break;
    } else {  
      //System.out.println("ccc "+dateGrid[i][j]);
      bFistEntry = true;
      if(rsgroup.getString("available").equals("0")) continue;
    }
    if( myGrpBean.containsKey(rsgroup.getString("provider_no")) ) {
%>                        
                        <br><span class='datepname'>&nbsp;<%=Misc.getShortStr(providerBean.getProperty(rsgroup.getString("provider_no")),"",11 )%></span><span class='datephour'><%=rsgroup.getString("hour") %></span><span class='datepreason'><%=rsgroup.getString("reason") %></span>
<%  }  }  %>                        
                        </a></font></td>
            <%
                  }
                }
                out.println("</tr>");
              }
            %>
          </table>
<!--last month & next month -->
            <table width="98%" border="1" cellspacing="1" cellpadding="6"  bgcolor="#EEE9BF" >
              <tr bgcolor="#CCCCCC"> 
                <%  	now=new GregorianCalendar(year,(month-2),day);
                  year = now.get(Calendar.YEAR); //month should be the current main display date, not the real now date
                  month = (now.get(Calendar.MONTH)+1);
                  day = now.get(Calendar.DAY_OF_MONTH);
                  aDate = new DateInMonthTable(year, month-1, 1);
                  dateGrid = aDate.getMonthDateGrid();
            %>
                <td><b> <a href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth"  title="Last Month: <%=arrayMonthOfYear[((month+10)%12)]%>"> 
                  &nbsp; &nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="Last Month: <%=arrayMonthOfYear[((month+10)%12)]%>" vspace="2"> 
                  Last Month </a>&nbsp; &nbsp;  &nbsp;<%=year%>-<%=month%> &nbsp; &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;<%=arrayMonthOfYear[((month+11)%12)]%> </b> <br>
              <table width="98%" border="1" cellspacing="1" cellpadding="6"  bgcolor="#EEE9BF" >
                <tr bgcolor="#FOFOFO"> 
            
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="blue">Week</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">Sun</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Mon</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Tue</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Wed</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Thu</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Fri</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="green">Sat</font></div></td>
            </tr>
            
            <%
              for (int i=0; i<dateGrid.length; i++) {
                out.println("</tr>");
                for (int j=-1; j<7; j++) {
                  if(j==-1) {
            %>
                    <td align='center' bgcolor='#FOFOFO'><font FACE='VERDANA,ARIAL,HELVETICA' SIZE='2'>
                    <a href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=dateGrid[i][j+1]==0?1:dateGrid[i][j+1]%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=week&dboperation=searchapptweek'>
                    <%=(i+1)%></font></td>
            <%
                    continue;
                  }
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    if(dateGrid[i][j]==day) {
            %>
                      <td align='center'><a href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(day)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday'>
                      <font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red"><div class='specialtxt'><%= dateGrid[i][j] %></div></font></a></td>
            <%      } else {
            %>
                      <td align='center'><font FACE='VERDANA,ARIAL,HELVETICA' SIZE='2' color='white'><a href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'> 
                        <%=dateGrid[i][j] %></a></font></td>
            <%
                    }
                  }
                }
                out.println("</tr>");
              }
            %>
            
          </table>
              </td>
            <%  	now=new GregorianCalendar(year,(month+1),day);
                  year = now.get(Calendar.YEAR); //month should be the current main display date, not the real now date
                  month = (now.get(Calendar.MONTH)+1);
                  day = now.get(Calendar.DAY_OF_MONTH);
                  aDate = new DateInMonthTable(year, month-1, 1);
                  dateGrid = aDate.getMonthDateGrid();
            %>
                <td align='right'><b><%= arrayMonthOfYear[(month+11)%12]%>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;  <%=year%>-<%=month%></b> <a href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth"  title="Next Month: <%=arrayMonthOfYear[month%12]%>">
                &nbsp; &nbsp; &nbsp; Next Month <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="Next Month: <%=arrayMonthOfYear[(month)%12]%>" vspace="2">&nbsp;&nbsp;</a><br>

              <table width="98%" border="1" cellspacing="1" cellpadding="6"  bgcolor="#EEE9BF" >
                <tr bgcolor="#FOFOFO"> 
            
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="blue">Week</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">Sun</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Mon</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Tue</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Wed</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Thu</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2">Fri</font></div></td>
              <td width="12.5%"><div align="center"><font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="green">Sat</font></div></td>
            </tr>
            
            <%
              for (int i=0; i<dateGrid.length; i++) {
                out.println("</tr>");
                for (int j=-1; j<7; j++) {
                  if(j==-1) {
            %>
                    <td align='center' bgcolor='#FOFOFO'><font FACE='VERDANA,ARIAL,HELVETICA' SIZE='2'>
                    <a href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=dateGrid[i][j+1]==0?1:dateGrid[i][j+1]%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=week&dboperation=searchapptweek'>
                    <%=(i+1)%></font></td>
            <%
                    continue;
                  }
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    if(dateGrid[i][j]==day) {
            %>
                      <td align='center'><a href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(day)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday'>
                      <font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red"><div class='specialtxt'><%= dateGrid[i][j] %></div></font></a></td>
            <%      } else {
            %>
                      <td align='center'><font FACE='VERDANA,ARIAL,HELVETICA' SIZE='2' color='white'><a href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'> 
                        <%=dateGrid[i][j] %></a></font></td>
            <%
                    }
                  }
                }
                out.println("</tr>");
              }
            %>
            
          </table>
              
              </td>
            </tr>
          </table> 

	</td></tr>
      <tr>
        <td BGCOLOR="ivory" width="33%">
         <a href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month-1)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth">
         &nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="View Previous MONTH" vspace="2"></a> 
         <b><span CLASS=title><%=strYear%>-<%=strMonth%></span></b>
         <a href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month+1)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth">
         <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="View Next MONTH" vspace="2">&nbsp;&nbsp;</a></td>
        <TD ALIGN="center"  BGCOLOR="ivory" width="33%"></TD>
        <td ALIGN="RIGHT" BGCOLOR="Ivory">
         <a href="../logout.jsp">Log Out <img src="../images/next.gif"  border="0" width="10" height="9" align="absmiddle"> &nbsp;</a> </td>
      </tr>
  </table>
  </td></tr>
</table>

</body>
</html>

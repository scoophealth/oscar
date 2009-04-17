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
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%
  if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("doctor"))
    response.sendRedirect("../logout.jsp");
    
	//if(request.getHeader("User-Agent").indexOf("MSIE")==-1) //not IE, see different pages
	//	response.sendRedirect("../hlp.html");

  String curUser_no,userfirstname,userlastname, userprofession, mygroupno, n_t_w_w="";
  curUser_no = (String) session.getAttribute("user");
  mygroupno = (String) session.getAttribute("groupno");  
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  userprofession = (String) session.getAttribute("userprofession");
  int startHour=Integer.parseInt((String) session.getAttribute("starthour"));
  int endHour=Integer.parseInt((String) session.getAttribute("endhour"));
  int everyMin=Integer.parseInt((String) session.getAttribute("everymin"));
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
  n_t_w_w= (String) session.getAttribute("newticklerwarningwindow");
  System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@"+n_t_w_w);
}
  int view=0;
  int lenLimitedL=11, lenLimitedS=3;
  int len = lenLimitedL;
  if(request.getParameter("view")!=null) view=Integer.parseInt(request.getParameter("view")); //0-multiple views, 1-single view
%>
<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.sql.*, java.net.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<%
	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int year = Integer.parseInt(request.getParameter("year"));
  int month = Integer.parseInt(request.getParameter("month"));
  int day = Integer.parseInt(request.getParameter("day"));
  String strYear=null, strMonth=null, strDay=null;
  String strDayOfWeek=null;
  String[] arrayDayOfWeek = new String[] {  "Sun","Mon","Tue","Wed","Thu","Fri","Sat"  };

  //verify the input date is really existed 
	now=new GregorianCalendar(year,(month-1),day);
  year = now.get(Calendar.YEAR);
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
<title>Doctor Appointment Access - appointmentprovideradminday</title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css"
	type="text/css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
</head>
<script language="JavaScript">
<!--
function setfocus() {
  document.jumptodate.year.focus();
  document.jumptodate.year.select();
}

function popupPage2(varpage) {
var page = "" + varpage;
windowprops = "height=600,width=700,location=no,"
+ "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=50,left=50";
window.open(page, "apptProviderSearch", windowprops);
}

function review(key) {
if(self.location.href.lastIndexOf("?") > 0) {
  if(self.location.href.lastIndexOf("&viewall=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&viewall="));
  else a = self.location.href;
} else {
  a="providercontrol.jsp?year="+document.jumptodate.year.value+"&month="+document.jumptodate.month.value+"&day="+document.jumptodate.day.value+"&view=0&displaymode=day&dboperation=searchappointmentday";
}
	self.location.href = a + "&viewall="+key ;
}


  
function onUnbilled(url) {
  if(confirm("You are about to delete the previous billing, are you sure?")) {
    popupPage(700,720, url);
  }
}
function changeGroup(s) {
	var newGroupNo = s.options[s.selectedIndex].value;
<%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){%>
	popupPage(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=n_t_w_w%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
<%}else{%> 
	popupPage(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
<%}%>
}
function ts1(s) {
  popupPage(360,680,('../appointment/addappointment.jsp?'+s));
}
function tsr(s) {
  popupPage(360,680,('../appointment/appointmentcontrol.jsp?displaymode=edit&dboperation=search&'+s)); 
}
//-->
</SCRIPT>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<%
   int numProvider=0;
   String [] curProvider_no;
   String [] curProviderName;
   if(view==0) { //multiple views
	 List<Map> resultList = oscarSuperManager.find("providerDao", "searchmygroupcount", new Object[] {mygroupno});
	 for (Map group : resultList) {
       numProvider=((Long)group.get("count(provider_no)")).intValue();
     }
     if(numProvider==0) {
       numProvider=1; //the login user
       curProvider_no = new String []{curUser_no};  //[numProvider];
       curProviderName = new String []{(userlastname+", "+userfirstname)}; //[numProvider];
     } else {
       if(numProvider >= 5) {lenLimitedL = 3; lenLimitedS = 2; }
       curProvider_no = new String [numProvider];
       curProviderName = new String [numProvider];

       int iTemp=0;
	   resultList = oscarSuperManager.find("providerDao", "searchmygroupprovider", new Object[] {mygroupno});
	   for (Map provider : resultList) {
         curProvider_no[iTemp]=String.valueOf(provider.get("provider_no"));
         curProviderName[iTemp]=provider.get("first_name")+" "+provider.get("last_name");
         iTemp++;
       }
     }
   } else { //single view
     numProvider=1;
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];
     curProvider_no[0]=request.getParameter("curProvider");
     curProviderName[0]=request.getParameter("curProviderName");
   }
%>
<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td VALIGN="BOTTOM" HEIGHT="20">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" height="20">
			<tr>
				<td></td>
				<td rowspan="2" BGCOLOR="ivory" ALIGN="MIDDLE" nowrap height="20"><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday"
					TITLE='View your daily schedule'
					OnMouseOver="window.status='View your daily schedule' ; return true">
				&nbsp;&nbsp;Day&nbsp;&nbsp; </a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth"
					TITLE='View your monthly template'
					OnMouseOver="window.status='View your monthly template' ; return true">Month</a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a href="#"
					ONCLICK="popupPage2('http://oscar1.mcmaster.ca:8888/oscarResource/index_html');return false;"
					title="Resources"
					onmouseover="window.status='View Resources';return true">Resource</a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupPage2('../demographic/search.htm');return false;"
					TITLE='Search for patient records'
					OnMouseOver="window.status='Search for patient records' ; return true">Search</a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupPage2('../report/reportindex.jsp');return false;"
					TITLE='Generate a report'
					OnMouseOver="window.status='Generate a report' ; return true">Report</a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupPage2('../billing/billingreport.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;"
					TITLE='Generate a billing report'
					onmouseover="window.status='Generate a billing report';return true">Billing</a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupPage2('../lab/lablinks.htm');return false;"
					TITLE='View lab reports'>Lab</a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <caisi:isModuleLoad
					moduleName="ticklerplus">
					<a href=#
						onClick="popupPage(200,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=n_t_w_w%>');return false;"
						TITLE='Edit your personal setting'
						OnMouseOver="window.status='Edit your personal setting' ; return true">Preference</a>
				</caisi:isModuleLoad></font></td>
				<td></td>
			</tr>
			<tr>
				<td valign="bottom"><img
					src="../images/tabs_l_active_end_alone.gif" width="14" height="20"
					border="0"></td>
				<td valign="bottom"><img src="../images/tabs_r_active_end.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img src="../images/tabs_both_inactive.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img src="../images/tabs_both_inactive.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img src="../images/tabs_both_inactive.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img src="../images/tabs_both_inactive.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img src="../images/tabs_both_inactive.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img src="../images/tabs_both_inactive.gif"
					width="15" height="20" border="0"></td>
				<td valign="bottom"><img
					src="../images/tabs_r_inactive_end.gif" width="17" height="20"
					border="0"></td>
			</tr>
		</table>

		</td>
		<form method="post" name="jumptodate" action="providercontrol.jsp">
		<td align="right" valign="bottom"><a href=#
			onClick="popupPage(300,450,'providerchangemygroup.jsp?mygroup_no=<%=mygroupno%>' );return false;"
			title="Change your Group No.">Group:</a> <select name="mygroup_no"
			onChange="changeGroup(this)">
			<option value=".">.</option>
<%
   List<Map> resultList = oscarSuperManager.find("providerDao", "searchmygroupno", new Object[] {});
   for (Map group : resultList) {
%>
			<option value="<%=group.get("mygroup_no")%>"
				<%=mygroupno.equals(group.get("mygroup_no"))?"selected":""%>><%=group.get("mygroup_no")%></option>
<%
   }
%>
		</select> <%--=mygroupno--%> &nbsp; <INPUT TYPE="text" NAME="year"
			VALUE="<%=strYear%>" WIDTH="4" HEIGHT="10" border="0" size="4"
			maxlength="4">- <INPUT TYPE="text" NAME="month"
			VALUE="<%=strMonth%>" WIDTH="2" HEIGHT="10" border="0" size="2"
			maxlength="2">- <INPUT TYPE="text" NAME="day"
			VALUE="<%=strDay%>" WIDTH="2" HEIGHT="10" border="0" size="2"
			maxlength="2"> <INPUT TYPE="hidden" NAME="view"
			VALUE="<%=view%>"> <INPUT TYPE="hidden" NAME="curProvider"
			VALUE="<%=request.getParameter("curProvider")%>"> <INPUT
			TYPE="hidden" NAME="curProviderName"
			VALUE="<%=request.getParameter("curProviderName")%>"> <INPUT
			TYPE="hidden" NAME="displaymode" VALUE="day"> <INPUT
			TYPE="hidden" NAME="dboperation" VALUE="searchappointmentday">
		<INPUT TYPE="SUBMIT" NAME="Go" VALUE="GO" SIZE="5"></td>
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
					href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day-1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0" ALT="View Previous DAY" vspace="2"></a> <b><span
					CLASS=title><%=strDayOfWeek%>, <%=strYear%>-<%=strMonth%>-<%=strDay%></span></b>
				<a
					href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day+1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
				<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT="View Next DAY" vspace="2">&nbsp;&nbsp;</a></td>
				<TD ALIGN="center" BGCOLOR="ivory" width="33%">
				<% if(view==1) out.println("<a href='providercontrol.jsp?year="+strYear+"&month="+strMonth+"&day="+strDay+"&view=0&displaymode=day&dboperation=searchappointmentday'>RESTORE</a>"); else out.println("<B>Hello "+ userfirstname+" "+userlastname +"</b>"); %>
				</TD>
				<td ALIGN="RIGHT" BGCOLOR="Ivory">
				<%
  if(request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1") ) {
%> <a href=# onClick="review('0')" title="View providers available">Schedule
				View</a> &nbsp;|&nbsp; <% } else { %> <a href=# onClick="review('1')"
					title="View all providers in the group">View All</a> &nbsp;|&nbsp;
				<% } %> <a href="../logout.jsp">Log Out <img
					src="../images/next.gif" border="0" width="10" height="9"
					align="absmiddle"> &nbsp;</a></td>
			</tr>

			<tr>
				<td colspan="3">
				<table border="0" cellpadding="0" bgcolor="#486ebd" cellspacing="0"
					width="100%">
					<tr>
						<%
          int hourCursor=0, minuteCursor=0, depth=everyMin; //depth is the period, e.g. 10,15,30,60min.
          String am_pm=null;
          boolean bColor=true, bColorHour=true; //to change color 

   int iCols=0, iRows=0, iS=0,iE=0,iSm=0,iEm=0; //for each S/E starting/Ending hour, how many events
   int ih=0, im=0, iSn=0, iEn=0 ; //hour, minute, nthStartTime, nthEndTime, rowspan
   boolean bFirstTimeRs=true;
   boolean bFirstFirstR=true;

 	 String[] param =new String[2];
	 String strsearchappointmentday=request.getParameter("dboperation");

   boolean userAvail = true; 
   int me = -1;
   for(int nProvider=0;nProvider<numProvider;nProvider++) {
     if(curUser_no.equals(curProvider_no[nProvider]) ) {
       //userInGroup = true;
       me = nProvider; break;
     }
   }
//   System.out.println(me +"    "+userInGroup);

   String [] param1 = new String[2];
   for(int nProvider=0;nProvider<numProvider;nProvider++) {
     bColor=bColor?false:true;
     userAvail = true; 
     param1[0] = strYear+"-"+strMonth+"-"+strDay;
     param1[1] = curProvider_no[nProvider];
     resultList = oscarSuperManager.find("providerDao", "search_scheduledate_single", param1);
     
     //viewall function 
     if(request.getParameter("viewall")==null || request.getParameter("viewall").equals("0") ) {
       if(resultList.size()==0 || "0".equals(resultList.get(0).get("available"))) {
         if(nProvider!=me ) continue;
         else userAvail = false;
       }
     }
 %>
						<td valign="top" width="<%=1*100/numProvider%>%"><!-- for the first provider's schedule -->

						<table border="0" cellpadding="0" bgcolor="#486ebd"
							cellspacing="0" width="100%">
							<!-- for the first provider's name -->
							<tr>
								<td ALIGN="center" BGCOLOR="<%=bColor?"#bfefff":"silver"%>">
								<b><a
									href="providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider=<%=curProvider_no[nProvider]%>&curProviderName=<%=curProviderName[nProvider]%>&displaymode=day&dboperation=searchappointmentday"
									title="zoom view"> <%=curProviderName[nProvider]%></a></b> <%=userAvail?"":"[not on Schedule]"%></td>
							</tr>
							<tr>
								<td valign="top"><!-- table for hours of day start -->
								<table border="1" cellpadding="0"
									bgcolor="<%=userAvail?"#486ebd":"silver"%>" cellspacing="0"
									width="100%">
									<%
          bFirstTimeRs=true;
          bFirstFirstR=true;
          param[0]=curProvider_no[nProvider];
          param[1]=year+"-"+month+"-"+day;//e.g."2001-02-02";
          resultList = oscarSuperManager.find("providerDao", strsearchappointmentday, param);

          Iterator<Map> it = resultList.iterator();
          Map appt = null;

          for(ih=startHour*60; ih<=endHour*60; ih+=depth) { // use minutes as base
            hourCursor = ih/60;
            minuteCursor = ih%60;
            bColorHour=minuteCursor==0?true:false; //every 00 minute, change color
        %>
									<tr>
										<td align="RIGHT"
											bgcolor="<%=bColorHour?"#3EA4E1":"#00A488"%>" width="5%"
											NOWRAP><b><font face="verdana,arial,helvetica"
											size="2"> <a href=#
											onClick="popupPage(360,680,'../appointment/addappointment.jsp?provider_no=<%=curProvider_no[nProvider]%>&bFirstDisp=<%=true%>&year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&start_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+ (minuteCursor<10?"0":"") +minuteCursor %>&end_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+(minuteCursor+depth-1)%>');return false;"
											title='<%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+ (minuteCursor<10?"0":"")+minuteCursor)%> - <%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+((minuteCursor+depth-1)<10?"0":"")+(minuteCursor+depth-1))%>'
											class="adhour"> <%=(hourCursor<10?"0":"") +hourCursor+ ":"%><%=(minuteCursor<10?"0":"")+minuteCursor%>&nbsp;</a></font></b></td>
										<%

          	while (bFirstTimeRs?it.hasNext():true) { //if it's not the first time to parse the standard time, should pass it by
          	  len = bFirstTimeRs&&!bFirstFirstR?lenLimitedS:lenLimitedL;
          	  appt = bFirstTimeRs?it.next():appt;
        	    iS=Integer.parseInt(String.valueOf(appt.get("start_time")).substring(0,2));
        	    iSm=Integer.parseInt(String.valueOf(appt.get("start_time")).substring(3,5));
         	    iE=Integer.parseInt(String.valueOf(appt.get("end_time")).substring(0,2));
     	        iEm=Integer.parseInt(String.valueOf(appt.get("end_time")).substring(3,5));
          	  if( (ih < iS*60+iSm) && (ih+depth-1)<iS*60+iSm ) { //iS not in this period (both start&end), get to the next period
          	  	//out.println("<td width='10'>&nbsp;</td>"); //should be comment
          	  	bFirstTimeRs=false;
          	  	break;
          	  }
         	    iRows=(iE-iS)*60/depth+iEm/depth-iSm/depth+1; //to see if the period across an hour period
         	    
         	    //get time format: 00:00am/pm
         	    //String startTime = (iS>12?("0"+(iS-12)):apptMainBean.getString(rs,"start_time").substring(0,2))+":"+apptMainBean.getString(rs,"start_time").substring(3,5)+am_pm ; 
         	    //String endTime   = (iE>12?("0"+(iE-12)):apptMainBean.getString(rs,"end_time").substring(0,2))  +":"+apptMainBean.getString(rs,"end_time").substring(3,5)+(iE<12?"am":"pm");
          	  String name = String.valueOf(appt.get("name"));
          	  int demographic_no = (Integer)appt.get("demographic_no");
          	  String reason = String.valueOf(appt.get("reason"));
          	  String notes = String.valueOf(appt.get("notes"));
          	  String status = String.valueOf(appt.get("status"));
          	  bFirstTimeRs=true;
        %>
										<td
											bgcolor=<%=status.indexOf('T')!=-1?"#FDFEC7":status.indexOf('P')!=-1?"#e0ffff":status.indexOf('H')!=-1?"#00ee00":status.indexOf('B')!=-1?"#3ea4e1":status.indexOf('N')!=-1?"#cccccc":"#999999"%>
											rowspan="<%=iRows%>" nowrap>
										<%
              if(status.indexOf('T')!=-1) {
            %> <a
											href="providercontrol.jsp?appointment_no=<%=appt.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&status=<%=status.replace('T',' ').trim()%>&statusch=H&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=appt.get("start_time")%>&demographic_no=<%=demographic_no==0?"0":(""+demographic_no)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=addstatus&dboperation=updateapptstatus&viewall=<%=request.getParameter("viewall")==null?"0":(request.getParameter("viewall"))%>"
											; title="To Do"> <img src="../images/todo.gif"
											border="0" height="10"></a> <%
              } else if(status.indexOf('P')!=-1) {
            %> <a
											href="providercontrol.jsp?appointment_no=<%=appt.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&status=<%=status.replace('P',' ').trim()%>&statusch=N&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=appt.get("start_time")%>&demographic_no=<%=demographic_no==0?"0":(""+demographic_no)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=addstatus&dboperation=updateapptstatus&viewall=<%=request.getParameter("viewall")==null?"0":(request.getParameter("viewall"))%>"
											; title="Picked"> <img src="../images/picked.gif"
											border="0"></a> <%
              } else if(status.indexOf('H')!=-1) {
            %> <a
											href="providercontrol.jsp?appointment_no=<%=appt.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&status=<%=status.replace('H',' ').trim()%>&statusch=P&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=appt.get("start_time")%>&demographic_no=<%=demographic_no==0?"0":(""+demographic_no)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=addstatus&dboperation=updateapptstatus&viewall=<%=request.getParameter("viewall")==null?"0":(request.getParameter("viewall"))%>"
											; title="Here"> <img src="../images/here.gif" border="0"></a>
										<%
              } else if(status.indexOf('N')!=-1) {
            %> <a
											href="providercontrol.jsp?appointment_no=<%=appt.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&status=<%=status.replace('N',' ').trim()%>&statusch=C&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=appt.get("start_time")%>&demographic_no=<%=demographic_no==0?"0":(""+demographic_no)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=addstatus&dboperation=updateapptstatus&viewall=<%=request.getParameter("viewall")==null?"0":(request.getParameter("viewall"))%>"
											; title="No Show"> <img src="../images/noshow.gif"
											border="0"></a> <%
              } else if(status.indexOf('C')!=-1) {
            %> <a
											href="providercontrol.jsp?appointment_no=<%=appt.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&status=<%=status.replace('C',' ').trim()%>&statusch=T&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=appt.get("start_time")%>&demographic_no=<%=demographic_no==0?"0":(""+demographic_no)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=addstatus&dboperation=updateapptstatus&viewall=<%=request.getParameter("viewall")==null?"0":(request.getParameter("viewall"))%>"
											; title="Cacelled"> <img src="../images/cancel.gif"
											border="0"></a> <%
              } else if(status.indexOf('B')!=-1) {
            %> <img src="../images/billed.gif" border="0" title="Billed">
										<%
              } else {
            %> &nbsp; <%
              } 
            %> | <%
        			if(demographic_no==0) {
        %> <a href=#
											onClick="popupPage(360,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=appt.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=0&displaymode=edit&dboperation=search');return false;"
											title="<%=iS+":"+(iSm>10?"":"0")+iSm%>-<%=iE+":"+iEm%>">
										.<%=view==0?(name.length()>len?name.substring(0,len):name):name%></font></a></td>
										<%
        			} else {
        			  //System.out.println(name+" / " +demographic_no);
				%>
										<a href=#
											onClick="popupPage(360,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=appt.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search');return false;"
											title="reason=<%=reason%> notes=<%=notes%>"> <%=view==0?(name.length()>len?name.substring(0,len):name):name%></a>
										<a href=#
											onClick="popupPage2('providercontrol.jsp?appointment_no=<%=appt.get("appointment_no")%>&demographic_no=<%=demographic_no%>&curProvider_no=<%=curProvider_no[nProvider]%>&reason=<%=URLEncoder.encode(reason)%>&username=<%= userfirstname+" "+userlastname %>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=iS+":"+iSm%>&status=<%=status%>&displaymode=encounter&dboperation=search_demograph&template=');return false;"
											title="Encounter"> <!--img src="../images/encounter.gif" border="0" height="10"-->&nbsp;|
										E </a>

										<% if(status.indexOf('B')==-1) { %>
										<a href=#
											onClick='popupPage(700,720, "../billing/billingOB.jsp?hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=appt.get("appointment_no")%>&demographic_name=<%=URLEncoder.encode(name)%>&demographic_no=<%=demographic_no%>&providerview=<%=curProvider_no[nProvider]%>&user_no=<%=curUser_no%>&apptProvider_no=<%=curProvider_no[nProvider]%>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=iS+":"+iSm%>&bNewForm=1");return false;'
											title="Billing">| B | </a>
										<% } else {%>
										<a href=#
											onClick='onUnbilled("../billing/billingDeleteWithoutNo.jsp?appointment_no=<%=appt.get("appointment_no")%>");return false;'
											title="Billing">| -B | </a>
										<% } %>
										<a href=#
											onClick="popupPage2('../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail');return false;"
											title="Master file"> <!--img src="../images/master.gif" border="0" height="10"-->
										M </a>
										</font>
										</td>
										<% 
        			}
        			bFirstFirstR = false;
          	}
            //out.println("<td width='1'>&nbsp;</td></tr>"); give a grid display
            out.println("<td width='1'></td></tr>"); //no grid display
          }
				%>
									
								</table>
								<!-- end table for each provider schedule display --></td>
							</tr>
						</table>
						<!-- end table for each provider name --></td>
						<%
   } //end of display team a, etc.    
 %>


					</tr>
				</table>
				<!-- end table for the whole schedule row display --></td>
			</tr>

			<tr>
				<td colspan="3">
				<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
					<tr>
						<td BGCOLOR="ivory" width="50%"><a
							href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day-1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
						&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10"
							HEIGHT="9" BORDER="0" ALT="View Previous DAY" vspace="2"></a> <b><span
							CLASS=title><%=strDayOfWeek%>, <%=strYear%>-<%=strMonth%>-<%=strDay%></span></b>
						<a
							href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day+1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
						<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
							ALT="View Next DAY" vspace="2">&nbsp;&nbsp;</a></td>
						<td ALIGN="RIGHT" BGCOLOR="Ivory"><a href="../logout.jsp">Log
						Out <img src="../images/next.gif" border="0" width="10" height="9"
							align="absmiddle"> &nbsp;</a></td>
					</TR>
				</table>
				</td>
			</tr>

		</table>
		</td>
	</tr>
</table>

</body>
</html>
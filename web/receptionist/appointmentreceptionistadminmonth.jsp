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

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/msg-tag.tld" prefix="oscarmessage"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.sql.*,oscar.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<%
  String curUser_no, curProvider_no,userfirstname,userlastname, mygroupno,n_t_w_w="";
  curProvider_no = (String) session.getAttribute("user");

  curUser_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  mygroupno = (String) session.getAttribute("groupno");  
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
  n_t_w_w = (String) session.getAttribute("newticklerwarningwindow");
}
  int startHour=Integer.parseInt(((String) session.getAttribute("starthour")).trim());
  int endHour=Integer.parseInt(((String) session.getAttribute("endhour")).trim());
  int everyMin=Integer.parseInt(((String) session.getAttribute("everymin")).trim());
  int view=0;
%>
<jsp:useBean id="scheduleHolidayBean" class="java.util.Hashtable"
	scope="session" />
<jsp:useBean id="myGrpBean" class="java.util.Properties" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />

<%
  String resourcebaseurl = "http://resource.oscarmcmaster.org/oscarResource/";
  List<Map> resultList = oscarSuperManager.find("receptionistDao", "search_resource_baseurl", new String[] {"resource_baseurl"});
  for (Map url : resultList) {
 	  resourcebaseurl = (String) url.get("value");
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
  java.util.ResourceBundle prop = ResourceBundle.getBundle("oscarResources", request.getLocale());
  String[] arrayDayOfWeek = new String[] {prop.getString("provider.appointmentprovideradminmonth.msgSun"),
                                          prop.getString("provider.appointmentprovideradminmonth.msgMon"),
                                          prop.getString("provider.appointmentprovideradminmonth.msgTue"),
                                          prop.getString("provider.appointmentprovideradminmonth.msgWed"),
                                          prop.getString("provider.appointmentprovideradminmonth.msgThu"),
                                          prop.getString("provider.appointmentprovideradminmonth.msgFri"),
                                          prop.getString("provider.appointmentprovideradminmonth.msgSat")
                                          };
  String[] arrayMonthOfYear = new String[] {
                                            prop.getString("provider.appointmentprovideradminmonth.msgJanuary"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgFebruary"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgMarch"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgApril"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgMay"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgJune"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgJuly"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgAugust"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgSeptember"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgOctober"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgNovember"),
                                            prop.getString("provider.appointmentprovideradminmonth.msgDecember"),
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

  //initial holiday bean
  if(scheduleHolidayBean.isEmpty() ) {
    resultList = oscarSuperManager.find("receptionistDao", "search_scheduleholiday", new String[] {(year-1)+"-"+month+"-01"});
    for (Map holiday : resultList) {
      scheduleHolidayBean.put(String.valueOf(holiday.get("sdate")), new HScheduleHoliday(String.valueOf(holiday.get("holiday_name"))));
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
      resultList = oscarSuperManager.find("receptionistDao", "searchmygroupprovider", new Object[] {mygroupno});
      for (Map provider : resultList) {
        myGrpBean.setProperty(String.valueOf(provider.get("provider_no")), mygroupno);
      }
    } else { //find all providers
      // the following line works only on MySQL. Should not be used
      // rsgroup = apptMainBean.queryResults("last_name", "searchallprovider");
      resultList = oscarSuperManager.find("receptionistDao", "searchallprovider", new String[] {});
      for (Map provider : resultList) {
    	myGrpBean.setProperty(String.valueOf(provider.get("provider_no")), provider.get("last_name")+","+provider.get("first_name"));
 	  }
    }
  }

%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="receptionist.appointmentrecepcionistmonth.title" /></title>
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
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	  popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=n_t_w_w%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
}else popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
  } else {
	  var newGroupNo = s.options[s.selectedIndex].value ;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	  popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=n_t_w_w%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
}else popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
	}

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
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td VALIGN="BOTTOM" HEIGHT="20">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" height="20">
			<tr>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap height="20"><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="receptionistcontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday"
					TITLE='<bean:message key="receptionist.appointmentrecepcionistmonth.msgDaySched"/>'
					OnMouseOver="window.status='<bean:message key="receptionist.appointmentrecepcionistmonth.msgDaySched"/>' ; return true">
				&nbsp;&nbsp;<bean:message
					key="receptionist.appointmentrecepcionistmonth.btnDay" />&nbsp;&nbsp;
				</a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="ivory" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="receptionistcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth"
					TITLE='<bean:message key="receptionist.appointmentrecepcionistmonth.msgMonthTemplate"/>'
					OnMouseOver="window.status='<bean:message key="receptionist.appointmentrecepcionistmonth.msgMonthTemplate"/>' ; return true"><bean:message
					key="receptionist.appointmentrecepcionistmonth.btnMonth" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a href="#"
					ONCLICK="popupPage(550,800,'<%=resourcebaseurl%>');return false;"
					title="<bean:message key="receptionist.appointmentrecepcionistmonth.msgResources"/>"><bean:message
					key="receptionist.appointmentrecepcionistmonth.btnResource" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupPage2('../demographic/search.jsp');return false;"
					TITLE='<bean:message key="receptionist.appointmentrecepcionistmonth.msgRecords"/>'
					OnMouseOver="window.status='<bean:message key="receptionist.appointmentrecepcionistmonth.msgRecords"/>' ; return true"><bean:message
					key="receptionist.appointmentrecepcionistmonth.btnSearch" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupPage2('../billing/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;"
					TITLE='<bean:message key="receptionist.appointmentrecepcionistmonth.msgBilling"/>'
					onmouseover="window.status='<bean:message key="receptionist.appointmentrecepcionistmonth.msgBilling"/>';return true"><bean:message
					key="receptionist.appointmentrecepcionistmonth.btnBilling" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=userfirstname%>%20<%=userlastname%>')">
				<oscarmessage:newMessage providerNo="<%=curUser_no%>" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <caisi:isModuleLoad
					moduleName="ticklerplus">
					<a href=#
						onClick="popupPage(200,680,'receptionistpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=n_t_w_w%>');return false;"><bean:message
						key="receptionist.appointmentrecepcionistmonth.btnPref" /></a>
				</caisi:isModuleLoad>
				<caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
					<a href=#
						onClick="popupPage(200,680,'receptionistpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>');return false;"><bean:message
						key="receptionist.appointmentrecepcionistmonth.btnPref" /></a>
				</caisi:isModuleLoad>
				</font>
				</td>
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
		<form method="post" name="jumptodate" action="receptionistcontrol.jsp">
		<td align="right" valign="bottom"><INPUT TYPE="text" NAME="year"
			VALUE="<%=strYear%>" WIDTH="4" border="0" size="4" maxlength="4">-
		<INPUT TYPE="text" NAME="month" VALUE="<%=strMonth%>" WIDTH="2"
			border="0" size="2" maxlength="2">- <INPUT TYPE="text"
			NAME="day" VALUE="<%=strDay%>" WIDTH="2" border="0" size="2"
			maxlength="2"> <INPUT TYPE="hidden" NAME="view"
			VALUE="<%=view%>"> <INPUT TYPE="hidden" NAME="curProvider"
			VALUE="<%=request.getParameter("curProvider")%>"> <INPUT
			TYPE="hidden" NAME="curProviderName"
			VALUE="<%=request.getParameter("curProviderName")%>"> <INPUT
			TYPE="hidden" NAME="displaymode" VALUE="day"> <INPUT
			TYPE="hidden" NAME="dboperation" VALUE="searchappointmentday">
		<input type="hidden" name="Go" value="GO"> <INPUT
			TYPE="SUBMIT"
			VALUE="<bean:message key="receptionist.appointmentrecepcionistmonth.btnGo"/>"
			SIZE="5"></td>
		</form>
	</tr>
</table>

<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C0C0C0">
	<tr>
		<td>
		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
			<tr BGCOLOR="ivory">
				<td width="33%"><a
					href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month-1)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0" ALT="<%=arrayMonthOfYear[((month+10)%12)]%>" vspace="2"></a>
				<b><span CLASS=title><%=strYear%>-<%=strMonth%></span></b> <a
					href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month+1)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth">
				<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT="<%=arrayMonthOfYear[month%12]%>" vspace="2">&nbsp;&nbsp;</a></td>
				<TD ALIGN="center" width="33%"><B><%= arrayMonthOfYear[(month+11)%12] %>
				</b></TD>
				<td ALIGN="RIGHT"><b><bean:message
					key="receptionist.appointmentrecepcionistmonth.formView" />:</b> <select
					name="provider_no" onChange="selectprovider(this)">
					<option value="all"><bean:message
						key="receptionist.appointmentrecepcionistmonth.formAllDocs" /></option>
<%
	resultList = oscarSuperManager.find("receptionistDao", "searchmygroupno", new Object[] {});
	for (Map group : resultList) {
%>
					<option value="<%="_grp_"+group.get("mygroup_no")%>"
						<%=mygroupno.equals(group.get("mygroup_no"))?"selected":""%>><bean:message
						key="receptionist.appointmentrecepcionistmonth.formGRP" />: <%=group.get("mygroup_no")%></option>
<%
	}

	resultList = oscarSuperManager.find("receptionistDao", "searchprovider", new Object[] {});
	for (Map provider : resultList) {
%>
					<option value="<%=provider.get("provider_no")%>" <%=mygroupno.equals(provider.get("provider_no"))?"selected":""%>>
					<%=provider.get("last_name")+", "+provider.get("first_name")%></option>
<%
	}
%>
				</select> <a href="../logout.jsp"><bean:message
					key="receptionist.appointmentrecepcionistmonth.btnLogOut" /> <img
					src="../images/next.gif" border="0" width="10" height="9"
					align="absmiddle"> &nbsp;</a></td>
			</tr>
			<tr>
				<td align="center" VALIGN="TOP" colspan="3" bgcolor="ivory">
				<%
            DateInMonthTable aDate = new DateInMonthTable(year, month-1, 1);
            int [][] dateGrid = aDate.getMonthDateGrid();
        %>

				<table width="98%" border="1" cellspacing="0" cellpadding="2"
					bgcolor="silver">
					<tr bgcolor="#FOFOFO" align="center">
						<td width="14.2%"><font SIZE="2" color="red"><bean:message
							key="provider.appointmentprovideradminmonth.msgSun" /></font></td>
						<td width="14.3%"><font SIZE="2"><bean:message
							key="provider.appointmentprovideradminmonth.msgMon" /></font></td>
						<td width="14.3%"><font SIZE="2"><bean:message
							key="provider.appointmentprovideradminmonth.msgTue" /></font></td>
						<td width="14.3%"><font SIZE="2"><bean:message
							key="provider.appointmentprovideradminmonth.msgWed" /></font></td>
						<td width="14.3%"><font SIZE="2"><bean:message
							key="provider.appointmentprovideradminmonth.msgThu" /></font></td>
						<td width="14.3%"><font SIZE="2"><bean:message
							key="provider.appointmentprovideradminmonth.msgFri" /></font></td>
						<td width="14.2%"><font SIZE="2" color="green"><bean:message
							key="provider.appointmentprovideradminmonth.msgSat" /></font></td>
					</tr>

					<%
    String[] param = new String[2];
    boolean bFistEntry = true;
   	GregorianCalendar cal = new GregorianCalendar(year,(month-1),1);
   	cal.add(cal.MONTH,1);
    if(providerBean.get(mygroupno) == null) { //it is a real group defined by users
      param[0] = year+"-"+month+"-"+"1";
      param[1] = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"1";
      resultList = oscarSuperManager.find("receptionistDao", "search_scheduledate_datep", param);
    } else {
      String[] param1 = new String[3];
      param1[0] = year+"-"+month+"-"+"1";
      param1[1] = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"1";
      param1[2] = mygroupno;
      resultList = oscarSuperManager.find("receptionistDao", "search_scheduledate_singlep", param1);
    }
              Iterator<Map> it = resultList.iterator();
              Map date = null;
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
					<td nowrap bgcolor="<%=bgcolor.toString()%>" valign="top"><a
						href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'>
					<span class='date'>&nbsp;<%=dateGrid[i][j] %> </span> <font
						size="-2" color="blue"><%=strHolidayName.toString()%></font> <%
    while (bFistEntry?it.hasNext():true) {
      date = bFistEntry?it.next():date;
      if(!String.valueOf(date.get("sdate")).equals(year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j])) ) {
        bFistEntry = false;
        break;
      } else {  
        //System.out.println("ccc "+dateGrid[i][j]);
        bFistEntry = true;
        if(String.valueOf(date.get("available")).equals("0")) continue;
      }
      if( myGrpBean.containsKey(String.valueOf(date.get("provider_no"))) ) {
%> <br>
					<span class='datepname'>&nbsp;<%=Misc.getShortStr(providerBean.getProperty(String.valueOf(date.get("provider_no"))),"",11 )%></span><span
						class='datephour'><%=date.get("hour") %></span><span
						class='datepreason'><%=date.get("reason") %></span>
<%    }
    }
%>
					</a></font></td>
					<%
                  }
                }
                out.println("</tr>");
              }
            %>
				</table>
				<!--last month & next month -->
				<table width="98%" border="1" cellspacing="1" cellpadding="6"
					bgcolor="#EEE9BF">
					<tr bgcolor="#CCCCCC">
						<%  	now=new GregorianCalendar(year,(month-2),day);
                  year = now.get(Calendar.YEAR); //month should be the current main display date, not the real now date
                  month = (now.get(Calendar.MONTH)+1);
                  day = now.get(Calendar.DAY_OF_MONTH);
                  aDate = new DateInMonthTable(year, month-1, 1);
                  dateGrid = aDate.getMonthDateGrid();
            %>
						<td><b> <a
							href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth"
							title="Last Month: <%=arrayMonthOfYear[((month+10)%12)]%>">
						&nbsp; &nbsp;<img src="../images/previous.gif" WIDTH="10"
							HEIGHT="9" BORDER="0"
							ALT="Last Month: <%=arrayMonthOfYear[((month+10)%12)]%>"
							vspace="2"> <bean:message
							key="receptionist.appointmentrecepcionistmonth.btnLastMonth" /> </a>&nbsp;
						&nbsp; &nbsp;<%=year%>-<%=month%> &nbsp; &nbsp; &nbsp; &nbsp;
						&nbsp; &nbsp; &nbsp;<%=arrayMonthOfYear[((month+11)%12)]%> </b> <br>
						<table width="98%" border="1" cellspacing="1" cellpadding="6"
							bgcolor="#EEE9BF">
							<tr bgcolor="#FOFOFO">

								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2" color="blue"><bean:message
									key="provider.appointmentprovideradminmonth.msgWeek" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2" color="red"><bean:message
									key="provider.appointmentprovideradminmonth.msgSun" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgMon" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgTue" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgWed" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgThu" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgFri" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2" color="green"><bean:message
									key="provider.appointmentprovideradminmonth.msgSat" /></font></div>
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
								href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=dateGrid[i][j+1]==0?1:dateGrid[i][j+1]%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=week&dboperation=searchapptweek'>
							<%=(i+1)%></font></td>
							<%
                    continue;
                  }
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    if(dateGrid[i][j]==day) {
            %>
							<td align='center'><a
								href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(day)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday'>
							<font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">
							<div class='specialtxt'><%= dateGrid[i][j] %></div>
							</font></a></td>
							<%      } else {
            %>
							<td align='center'><font FACE='VERDANA,ARIAL,HELVETICA'
								SIZE='2' color='white'><a
								href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'>
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
						<td align='right'><b><%= arrayMonthOfYear[(month+11)%12]%>&nbsp;
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <%=year%>-<%=month%></b> <a
							href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth"
							title="<bean:message key="receptionist.appointmentrecepcionistmonth.msgNextMonth"/>: <%=arrayMonthOfYear[month%12]%>">
						&nbsp; &nbsp; &nbsp; <bean:message
							key="receptionist.appointmentrecepcionistmonth.msgNextMonth" /> <img
							src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
							ALT="Next Month: <%=arrayMonthOfYear[(month)%12]%>" vspace="2">&nbsp;&nbsp;</a><br>

						<table width="98%" border="1" cellspacing="1" cellpadding="6"
							bgcolor="#EEE9BF">
							<tr bgcolor="#FOFOFO">

								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2" color="blue"><bean:message
									key="provider.appointmentprovideradminmonth.msgWeek" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2" color="red"><bean:message
									key="provider.appointmentprovideradminmonth.msgSun" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgMon" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgTue" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgWed" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgThu" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2"><bean:message
									key="provider.appointmentprovideradminmonth.msgFri" /></font></div>
								</td>
								<td width="12.5%">
								<div align="center"><font FACE="VERDANA,ARIAL,HELVETICA"
									SIZE="2" color="green"><bean:message
									key="provider.appointmentprovideradminmonth.msgSat" /></font></div>
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
								href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=dateGrid[i][j+1]==0?1:dateGrid[i][j+1]%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=week&dboperation=searchapptweek'>
							<%=(i+1)%></font></td>
							<%
                    continue;
                  }
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    if(dateGrid[i][j]==day) {
            %>
							<td align='center'><a
								href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(day)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday'>
							<font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">
							<div class='specialtxt'><%= dateGrid[i][j] %></div>
							</font></a></td>
							<%      } else {
            %>
							<td align='center'><font FACE='VERDANA,ARIAL,HELVETICA'
								SIZE='2' color='white'><a
								href='receptionistcontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'>
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

				</td>
			</tr>
			<tr>
				<td BGCOLOR="ivory" width="33%"><a
					href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month-1)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0" ALT="View Previous MONTH" vspace="2"></a> <b><span
					CLASS=title><%=strYear%>-<%=strMonth%></span></b> <a
					href="receptionistcontrol.jsp?year=<%=year%>&month=<%=(month+1)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth">
				<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT="View Next MONTH" vspace="2">&nbsp;&nbsp;</a></td>
				<TD ALIGN="center" BGCOLOR="ivory" width="33%"></TD>
				<td ALIGN="RIGHT" BGCOLOR="Ivory"><a href="../logout.jsp"><bean:message
					key="receptionist.appointmentrecepcionistmonth.btnLogOut" /> <img
					src="../images/next.gif" border="0" width="10" height="9"
					align="absmiddle"> &nbsp;</a></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</body>
</html:html>

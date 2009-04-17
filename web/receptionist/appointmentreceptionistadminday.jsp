<%@ taglib uri="/WEB-INF/msg-tag.tld" prefix="oscarmessage"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.sql.*,java.net.*, oscar.*, oscar.appt.*"
	errorPage="errorpage.jsp"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<%
  if(session.getValue("user") == null || !((String) session.getValue("userprofession")).equalsIgnoreCase("receptionist"))
    response.sendRedirect("../logout.jsp");

  oscar.oscarSecurity.CookieSecurity cs = new oscar.oscarSecurity.CookieSecurity();
  response.addCookie(cs.GiveMeACookie(cs.receptionistCookie));

  String tickler_no="", textColor="", tickler_note="";
  String curUser_no,userfirstname,userlastname, userprofession, mygroupno;
  curUser_no = (String) session.getAttribute("user");
  mygroupno = (String) session.getAttribute("groupno");  
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  userprofession = (String) session.getAttribute("userprofession");
  String newticklerwarningwindow = (String) session.getAttribute("newticklerwarningwindow");
  int startHour=Integer.parseInt(((String) session.getAttribute("starthour")).trim());
  int endHour=Integer.parseInt(((String) session.getAttribute("endhour")).trim());
  int everyMin=Integer.parseInt(((String) session.getAttribute("everymin")).trim());
  int view=0;
  int lenLimitedL=11, lenLimitedS=3;
  int len = lenLimitedL;
  if(request.getParameter("view")!=null) view=Integer.parseInt(request.getParameter("view")); //0-multiple views, 1-single view
  OscarProperties props = OscarProperties.getInstance();
  boolean bDispTemplatePeriod = ( props.getProperty("receptionist_alt_view") != null && props.getProperty("receptionist_alt_view").equals("yes") ); // true - display as schedule template period, false - display as preference
%>
<jsp:useBean id="as" class="oscar.appt.ApptStatusData" scope="page" />
<jsp:useBean id="DateTimeCodeBean" class="java.util.Hashtable"
	scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<%
  String resourcebaseurl = "http://resource.oscarmcmaster.org/oscarResource/";
  List<Map> resultList = oscarSuperManager.find("receptionistDao", "search_resource_baseurl", new String[] {"resource_baseurl"});
  for (Map url : resultList) {
 	  resourcebaseurl = (String) url.get("value");
  }

  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int year = Integer.parseInt(request.getParameter("year"));
  int month = Integer.parseInt(request.getParameter("month"));
  int day = Integer.parseInt(request.getParameter("day"));
  String strYear=null, strMonth=null, strDay=null, strDayOfWeek=null;
  String[] arrayDayOfWeek = new String[] { "Sun","Mon","Tue","Wed","Thu","Fri","Sat"  };

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
<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="receptionist.appointmentreceptionistadminday.title" /></title>
<link rel="stylesheet" href="receptionistapptstyle.css" type="text/css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
</head>
<script language="JavaScript">

function setfocus() {
  this.focus();
  document.findprovider.providername.focus();
  document.findprovider.providername.select();
}

function popupPage2(varpage) {
var page = "" + varpage;
windowprops = "height=700,width=1000,location=no,"
+ "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=15,left=10";
window.open(page, '<bean:message key="receptionist.appointmentreceptionistadminday.titlePopupPage2"/>', windowprops);
}

function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, '<bean:message key="receptionist.appointmentreceptionistadminday.titlePopupOscarRx"/>', windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}

function review(key) {
  if(self.location.href.lastIndexOf("&viewall=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&viewall="));
  else a = self.location.href;

	self.location.href = a + "&viewall="+key ;
}

function refresh() {
  document.location.reload();
}
function refresh1() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    document.location.reload();
  }
}
function changeGroup(s) {
	var newGroupNo = s.options[s.selectedIndex].value;
	if(newGroupNo.indexOf("_grp_") != -1) {
	  newGroupNo = s.options[s.selectedIndex].value.substring(5);
<%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){%>
 	  popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
<%}else{%>
	  popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
<%}%>
	} else {
	  newGroupNo = s.options[s.selectedIndex].value;
<%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){%>
	  popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
<%}else{%>
	  popupPage(10,10, "receptionistcontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
<%}%>
	}
}
function ts1(s) {
  popupPage(400,680,('../appointment/addappointment.jsp?'+s));
}
function tsr(s) {
  popupPage(400,680,('../appointment/appointmentcontrol.jsp?displaymode=edit&dboperation=search&'+s)); 
}
function goFilpView(s) {
	self.location.href = "../schedule/scheduleflipview.jsp?originalpage=../receptionist/receptionistcontrol.jsp&startDate=<%=year+"-"+month+"-"+day%>" + "&provider_no="+s ;
}
function goZoomView(s, n) {
	self.location.href = "receptionistcontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider="+s+"&curProviderName="+n+"&displaymode=day&dboperation=searchappointmentday" ;
}
function findProvider(p,m,d) {
  popupPage(300,400, "receptionistfindprovider.jsp?pyear=" +p+ "&pmonth=" +m+ "&pday=" +d+ "&providername="+ document.findprovider.providername.value );
}

</SCRIPT>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">

<%
   int numProvider=0, numAvailProvider=0;
   String [] curProvider_no;
   String [] curProviderName;
  
   //initial provider bean for all the application
   if (providerBean.isEmpty()) {
     // the following line works only on MySQL. Should not be used
     // rsgroup = apptMainBean.queryResults("last_name", "searchallprovider");
     resultList = oscarSuperManager.find("receptionistDao", "searchallprovider", new String[] {});
     for (Map provider : resultList) {
        providerBean.setProperty(String.valueOf(provider.get("provider_no")), provider.get("last_name")+","+provider.get("first_name"));
     }
   }

   if (providerBean.get(mygroupno) != null) { //single appointed provider view
     numProvider=1;
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];
     curProvider_no[0]=mygroupno;
     curProviderName[0]=providerBean.getProperty(mygroupno);
   } else if (view == 0) { //multiple views
     resultList = oscarSuperManager.find("receptionistDao", "searchmygroupcount", new Object[] {mygroupno});
     for (Map count : resultList) {
       numProvider = ((Long)(count.get(count.keySet().toArray()[0]))).intValue();
//       System.out.print(count.keySet()+"\n\nnumProvider="+numProvider+"\n\n");
     }

     if (session.getAttribute(mygroupno+"_$navailprovider")!=null) {
       numAvailProvider = Integer.parseInt((String) session.getAttribute(mygroupno+"_$navailprovider")) ;
     } else {
       String [] param3 = new String[2];
       param3[0] = mygroupno;
       param3[1] = strYear +"-"+ strMonth +"-"+ strDay ;
       resultList = oscarSuperManager.find("receptionistDao", "search_numgrpscheduledate", param3);
       for (Map count : resultList) {
         numAvailProvider = ((Long)(count.get(count.keySet().toArray()[0]))).intValue();
//         System.out.print(count.keySet()+"\n\nnumAvailProvider="+numAvailProvider+"\n\n");
       }
       session.setAttribute(mygroupno+"_$navailprovider", ""+numAvailProvider);
     }

     if (request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1")) {
       if(numProvider >= 5) {lenLimitedL = 3; lenLimitedS = 2; }
     } else {
       if(numAvailProvider >= 5) {lenLimitedL = 3; lenLimitedS = 2; }
     }

     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];

     int iTemp = 0;
     resultList = oscarSuperManager.find("receptionistDao", "searchmygroupprovider", new Object[] {mygroupno});
     for (Map provider : resultList) {
       curProvider_no[iTemp] = String.valueOf(provider.get("provider_no"));
       curProviderName[iTemp] = provider.get("first_name")+" "+provider.get("last_name");
       iTemp++;
     }
   } else { //single view
     numProvider=1;
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];
     curProvider_no[0]=request.getParameter("curProvider");
     curProviderName[0]=request.getParameter("curProviderName");
   }

   //set timecode bean
   String bgcolordef = "#FFFFE0" ;
   String [] param3 = new String[2];
   param3[0] = strYear+"-"+strMonth+"-"+strDay;
   for(int nProvider=0;nProvider<numProvider;nProvider++) {
     param3[1] = curProvider_no[nProvider];
     resultList = oscarSuperManager.find("receptionistDao", "search_appttimecode", param3);
     for (Map provider : resultList) {
       DateTimeCodeBean.put(String.valueOf(provider.get("provider_no")), String.valueOf(provider.get("timecode")));
     } 
   }

   resultList = oscarSuperManager.find("receptionistDao", "search_timecode", new Object[] {});
   for (Map appt : resultList) {
     DateTimeCodeBean.put("description"+appt.get("code"), appt.get("description"));
     DateTimeCodeBean.put("duration"+appt.get("code"), appt.get("duration"));
     DateTimeCodeBean.put("color"+appt.get("code"), (appt.get("color")==null || "".equals(appt.get("color")))?bgcolordef:appt.get("color"));
   }
   java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);   
%>

<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td VALIGN="BOTTOM" HEIGHT="20">

		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" height="20">
			<tr>
				<td></td>
				<td rowspan="2" BGCOLOR="ivory" ALIGN="MIDDLE" nowrap height="20"><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="receptionistcontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday"
					TITLE='<bean:message key="receptionist.appointmentreceptionistadminday.viewDailySchedule"/>'
					OnMouseOver="window.status='<bean:message key="receptionist.appointmentreceptionistadminday.viewDailySchedule"/>'; return true"><bean:message
					key="receptionist.appointmentreceptionistadminday.btnDay" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a
					href="receptionistcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth"
					TITLE='<bean:message key="receptionist.appointmentreceptionistadminday.viewMonthlyTemplate"/>'
					OnMouseOver="window.status='<bean:message key="receptionist.appointmentreceptionistadminday.viewMonthlyTemplate"/>'; return true"><bean:message
					key="receptionist.appointmentreceptionistadminday.btnMonth" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a href="#"
					ONCLICK="popupPage(550,800,'<%=resourcebaseurl%>');return false;"
					title='<bean:message key="receptionist.appointmentreceptionistadminday.manageClinicalResource"/>'><bean:message
					key="receptionist.appointmentreceptionistadminday.btnResource" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupPage(550,760,'../demographic/search.jsp');return false;"
					TITLE='<bean:message key="receptionist.appointmentreceptionistadminday.searchPatientRecords"/>'
					OnMouseOver="window.status='<bean:message key="receptionist.appointmentreceptionistadminday.searchPatientRecords"/>'; return true"><bean:message
					key="receptionist.appointmentreceptionistadminday.btnSearch" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupPage2('../report/reportindex.jsp');return false;"
					TITLE='<bean:message key="receptionist.appointmentreceptionistadminday.generateReport"/>'
					OnMouseOver="window.status='<bean:message key="receptionist.appointmentreceptionistadminday.generateReport"/>'; return true"><bean:message
					key="receptionist.appointmentreceptionistadminday.btnReport" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <% 
                //java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
                if (vLocale.getCountry().equals("BR")) { %> <a HREF="#"
					ONCLICK="popupPage2('../oscar/billing/consultaFaturamentoMedico/init.do');return false;"
					TITLE='<bean:message key="global.genBillReport"/>'
					onmouseover="window.status='<bean:message key="global.genBillReport"/>';return true"><bean:message
					key="global.billing" /></a>
				<% } else {%>
				<a HREF="#"
					ONCLICK="popupPage2('../billing/billingReportCenter.jsp??displaymode=billreport&providerview=<%=curUser_no%>');return false;"
					TITLE='<bean:message key="receptionist.appointmentreceptionistadminday.generateBillingReport"/>'
					onmouseover="window.status='<bean:message key="receptionist.appointmentreceptionistadminday.generateBillingReport"/>';return true"><bean:message
					key="receptionist.appointmentreceptionistadminday.btnBilling" /></a>
				<% } %>
				</font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')">
				<oscarmessage:newMessage providerNo="<%=curUser_no%>" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
					ONCLICK="popupOscarRx(600,900,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')"><bean:message
					key="receptionist.appointmentreceptionistadminday.btnCon" /></a></font></td>
				<td></td>
				<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
					FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <caisi:isModuleLoad
					moduleName="ticklerplus">
					<a href="#"
						onClick="popupPage(200,680,'receptionistpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=newticklerwarningwindow%>');return false;"><bean:message
						key="receptionist.appointmentreceptionistadminday.btnPref" /></a>
				</caisi:isModuleLoad>
				<caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
					<a href="#"
						onClick="popupPage(200,680,'receptionistpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>');return false;"><bean:message
						key="receptionist.appointmentreceptionistadminday.btnPref" /></a>
					</caisi:isModuleLoad>
					</font>
					</td>
					<td></td>
					<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
						FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
						ONCLICK="popupPage2('../dms/documentReport.jsp?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>');return false;"
						TITLE='<bean:message key="receptionist.appointmentreceptionistadminday.viewEDocument"/>'><bean:message
						key="receptionist.appointmentreceptionistadminday.btnEDoc" /></a></font></td>
					<td></td>
					<td rowspan="2" BGCOLOR="#C0C0C0" ALIGN="MIDDLE" nowrap><font
						FACE="VERDANA,ARIAL,HELVETICA" SIZE="2"> <a HREF="#"
						ONCLICK="popupPage2('../tickler/ticklerMain.jsp');return false;"
						TITLE='<bean:message key="receptionist.appointmentreceptionistadminday.viewTickler"/>'><bean:message
						key="receptionist.appointmentreceptionistadminday.btnTickler" /></a></font></td>

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
		<form method="post" name="findprovider"
			onSubmit="findProvider(<%=year%>,<%=month%>,<%=day%>);return false;"
			target="apptReception" action="receptionistfindprovider.jsp">
		<td align="right" valign="bottom"><INPUT TYPE="text"
			NAME="providername" VALUE="" WIDTH="2" HEIGHT="10" border="0"
			size="10" maxlength="10">- <INPUT TYPE="SUBMIT" NAME="Go"
			VALUE='<bean:message key="receptionist.appointmentreceptionistadminday.btnGo"/>'
			onClick="findProvider(<%=year%>,<%=month%>,<%=day%>);return false;">
		</td>
		</form>
	</tr>
</table>

<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%"
	BGCOLOR="#C0C0C0">
	<tr>
		<td>
		<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
			<tr BGCOLOR="ivory">
				<td nowrap><a
					href="receptionistcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day-1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0"
					ALT='<bean:message key="receptionist.appointmentreceptionistadminday.altImgViewPreviousDay"/>'
					vspace="2"></a> <b><span CLASS=title><%=strDayOfWeek%>,
				<%=strYear%>-<%=strMonth%>-<%=strDay%></span></b> <a
					href="receptionistcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day+1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
				<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT='<bean:message key="receptionist.appointmentreceptionistadminday.altImgViewNextDay"/>'
					vspace="2">&nbsp;&nbsp;</a> <a href=#
					onClick="popupPage(310,430,'../share/CalendarPopup.jsp?urlfrom=../receptionist/receptionistcontrol.jsp&year=<%=strYear%>&month=<%=strMonth%>&param=<%=URLEncoder.encode("&view=0&displaymode=day&dboperation=searchappointmentday")%>')"><bean:message
					key="receptionist.appointmentreceptionistadminday.btnCalendar" /></a></td>
				<TD ALIGN="center" width="33%">
				<% if(view==1) out.println("<a href='receptionistcontrol.jsp?year="+strYear+"&month="+strMonth+"&day="+strDay+"&view=0&displaymode=day&dboperation=searchappointmentday'>Group View</a>"); %>
				</TD>
				<td ALIGN="RIGHT"><a href=#
					onClick="popupPage(300,450,'receptionistchangemygroup.jsp?mygroup_no=<%=mygroupno%>' );return false;"
					title='<bean:message key="receptionist.appointmentreceptionistadminday.btnChangeGroupNo"/>'><bean:message
					key="receptionist.appointmentreceptionistadminday.btnGroup" />:</a> <select
					name="mygroup_no" onChange="changeGroup(this)">
					<option value=".default">.<bean:message
						key="receptionist.appointmentreceptionistadminday.optionDefault" /></option>
<%
	resultList = oscarSuperManager.find("receptionistDao", "searchmygroupno", new Object[] {});
	for (Map group : resultList) {
%>
					<option value="<%="_grp_"+group.get("mygroup_no")%>"
						<%=mygroupno.equals(group.get("mygroup_no"))?"selected":""%>><%=group.get("mygroup_no")%></option>
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
				</select>
<%
  if (request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1")) {
%>
				<a href="#" onClick="review('0')"
					title='<bean:message key="receptionist.appointmentreceptionistadminday.viewProvidersAvailable"/>'><bean:message
					key="receptionist.appointmentreceptionistadminday.btnScheduleView" /></a>
				&nbsp;|&nbsp; <% } else { %> <a href=# onClick="review('1')"
					title='<bean:message key="receptionist.appointmentreceptionistadminday.viewAllProvidersInGroup"/>'><bean:message
					key="receptionist.appointmentreceptionistadminday.btnViewAll" /></a>
				&nbsp;|&nbsp; <% } %> <a href="../logout.jsp"><bean:message
					key="global.btnLogout" /><img src="../images/next.gif" border="0"
					width="10" height="9" align="absmiddle"> &nbsp;</a></td>
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
   String[] paramTickler = new String[2];
   String[] param = new String[2];
   String strsearchappointmentday=request.getParameter("dboperation");

   StringBuffer hourmin = null;
   String[] param1 = new String[2];

   for(int nProvider=0;nProvider<numProvider;nProvider++) {
     int timecodeLength = DateTimeCodeBean.get(curProvider_no[nProvider])!=null?((String) DateTimeCodeBean.get(curProvider_no[nProvider]) ).length() : 4*24;
     depth = bDispTemplatePeriod ? (24*60 / timecodeLength) : everyMin; // add function to display different time slot	

     param1[0] = strYear+"-"+strMonth+"-"+strDay;
     param1[1] = curProvider_no[nProvider];

     resultList = oscarSuperManager.find("receptionistDao", "search_scheduledate_single", param1);
     
     //viewall function 
     if(request.getParameter("viewall")==null || request.getParameter("viewall").equals("0") ) {if(resultList.size()==0 || "0".equals(resultList.get(0).get("available")) ) continue;}
     bColor=bColor?false:true;
 %>
						<td valign="top" width="<%=1*100/numProvider%>%"><!-- for the first provider's schedule -->

						<table border="0" cellpadding="0" bgcolor="#486ebd"
							cellspacing="0" width="100%">
							<!-- for the first provider's name -->
							<tr>
								<td NOWRAP ALIGN="center"
									BGCOLOR="<%=bColor?"#bfefff":"silver"%>"><b><input
									type='radio' name='flipview'
									onClick="goFilpView('<%=curProvider_no[nProvider]%>')"
									title='<bean:message key="receptionist.appointmentreceptionistadminday.formFlipView"/>'>
								<a href=#
									onClick="goZoomView('<%=curProvider_no[nProvider]%>','<%=curProviderName[nProvider]%>')"
									title='<bean:message key="receptionist.appointmentreceptionistadminday.formZoomView"/>'>
								<%=curProviderName[nProvider]%></a></b></td>
							</tr>
							<tr>
								<td valign="top"><!-- table for hours of day start -->
								<table border="1" cellpadding="0" bgcolor="#486ebd"
									cellspacing="0" width="100%">
<%
     bFirstTimeRs=true;
     bFirstFirstR=true;
     param[0]=curProvider_no[nProvider];
     param[1]=year+"-"+month+"-"+day;//e.g."2001-02-02";

     List<Map> appointmentList = oscarSuperManager.find("receptionistDao", strsearchappointmentday, param);
     Iterator<Map> it = appointmentList.iterator();
     Map appointment = null;

     for (ih=startHour*60; ih<=(endHour*60+(60/depth-1)*depth); ih+=depth) { // use minutes as base
       hourCursor = ih/60;
       minuteCursor = ih%60;
       bColorHour = minuteCursor==0?true:false; //every 00 minute, change color

       //templatecode     
       if (DateTimeCodeBean.get(curProvider_no[nProvider]) != null) {       
         int nLen = 24*60 / ((String) DateTimeCodeBean.get(curProvider_no[nProvider]) ).length();
         int ratio = (hourCursor*60+minuteCursor)/nLen;
         hourmin = new StringBuffer(DateTimeCodeBean.get(curProvider_no[nProvider])!=null?((String) DateTimeCodeBean.get(curProvider_no[nProvider])).substring(ratio,ratio+1):" " );
       } else {
    	 hourmin = new StringBuffer();
       }
%>
									<tr>
										<td align="RIGHT"
											bgcolor="<%=bColorHour?"#3EA4E1":"#00A488"%>" width="5%"
											NOWRAP><b><font face="verdana,arial,helvetica"
											size="2"> <a href=#
											onClick="ts1('provider_no=<%=curProvider_no[nProvider]%>&bFirstDisp=<%=true%>&year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&start_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+ (minuteCursor<10?"0":"") +minuteCursor %>&end_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+(minuteCursor+depth-1)%>&duration=<%=DateTimeCodeBean.get("duration"+hourmin.toString())!=null?DateTimeCodeBean.get("duration"+hourmin.toString()):(""+depth) %>');return false;"
											title='<%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+ (minuteCursor<10?"0":"")+minuteCursor)%> - <%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+((minuteCursor+depth-1)<10?"0":"")+(minuteCursor+depth-1))%>'
											class="adhour"> <%=(hourCursor<10?"0":"") +hourCursor+ ":"%><%=(minuteCursor<10?"0":"")+minuteCursor%>&nbsp;</a></font></b></td>
										<td width='1%'
											<%=DateTimeCodeBean.get("color"+hourmin.toString())!=null?("bgcolor="+DateTimeCodeBean.get("color"+hourmin.toString()) ):("bgcolor="+bgcolordef) %>
											title='<%=DateTimeCodeBean.get("description"+hourmin.toString())%>'><font
											color='<%=(DateTimeCodeBean.get("color"+hourmin.toString())!=null && !DateTimeCodeBean.get("color"+hourmin.toString()).equals(bgcolordef) )?"black":"black" %>'><%=hourmin.toString() %></font>
										</td>
<%
       while (bFirstTimeRs?it.hasNext():true) { //if it's not the first time to parse the standard time, should pass it by
          	  len = bFirstTimeRs&&!bFirstFirstR?lenLimitedS:lenLimitedL;
          	  appointment = bFirstTimeRs?it.next():appointment;
          	  iS=Integer.parseInt(String.valueOf(appointment.get("start_time")).substring(0,2));
        	  iSm=Integer.parseInt(String.valueOf(appointment.get("start_time")).substring(3,5));
         	  iE=Integer.parseInt(String.valueOf(appointment.get("end_time")).substring(0,2));
     	      iEm=Integer.parseInt(String.valueOf(appointment.get("end_time")).substring(3,5));
          	  if( (ih < iS*60+iSm) && (ih+depth-1)<iS*60+iSm ) { //appt after this time slot, iS not in this time slot (both start&end), get to the next period
          	  	bFirstTimeRs=false;
          	  	break;
          	  }
          	  if( (ih > iE*60+iEm) ) { //appt before this time slot (both start&end), get to the next period
          	  	bFirstTimeRs=true;
          	  	continue;
          	  }
         	  iRows=((iE*60+iEm)-ih)/depth+1; //to see if the period across an hour period
          	  String name = Misc.toUpperLowerCase(String.valueOf(appointment.get("name")));
          	  int demographic_no = (Integer)appointment.get("demographic_no");

          	  paramTickler[0]=String.valueOf(demographic_no);
		  	  paramTickler[1]=year+"-"+month+"-"+day;//e.g."2001-02-02";
		      List<Map> ticklerList = oscarSuperManager.find("receptionistDao", "search_tickler", paramTickler);

		      tickler_no = "";
		  	  for (Map tickler : ticklerList) {
		        tickler_no = String.valueOf(tickler.get("tickler_no"));
		        tickler_note = tickler.get("message")==null?tickler_note:tickler_note + "\n" + tickler.get("message");
          	  }

		  	  String reason = String.valueOf(appointment.get("reason")).trim();
          	  String notes = String.valueOf(appointment.get("notes")).trim();
          	  String status = String.valueOf(appointment.get("status")).trim();
          	  bFirstTimeRs=true;

          	  //ApptStatusData as = new ApptStatusData();
			  as.setApptStatus(status);
%>
										<td bgcolor='<%=as.getBgColor()%>' rowspan="<%=iRows%>"
											<%--=view==0?(len==lenLimitedL?"nowrap":""):"nowrap"--%> nowrap>
<%
		      if (as.getNextStatus() != null && !as.getNextStatus().equals("")) {
%>
										<a
											href="receptionistcontrol.jsp?appointment_no=<%=appointment.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&status=&statusch=<%=as.getNextStatus()%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=addstatus&dboperation=updateapptstatus&viewall=<%=request.getParameter("viewall")==null?"0":(request.getParameter("viewall"))%>"
											title="<bean:message key='<%=as.getTitle()%>' />">
<%
		      } 
			  if (as.getNextStatus() != null) {
%>
										<img src="../images/<%=as.getImageName()%>" border="0" height="10"
											title="<bean:message key='<%=as.getTitle()%>' />"></a>
<%
              } else {
                out.print("&nbsp;");
              }
%>
<%--|--%>
<%
     		  if (demographic_no==0) {
                if (tickler_no.compareTo("") != 0) {
%>
										<a href="#"
											onClick="popupPage(700,1000, '../tickler/ticklerDemoMain.jsp?demoview=0');return false;"
											title='<bean:message key="receptionist.appointmentreceptionistadminday.ticklerMsg"/>: <%=Misc.htmlEscape(tickler_note)%>'><font
											color="red">!</font></a>
<%
                }
%>
										<a href="#"
											onClick="popupPage(400,680,'../appointment/appointmentcontrol.jsp?appointment_no=<%=appointment.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=0&displaymode=edit&dboperation=search');return false;"
											title="<%=iS+":"+(iSm>10?"":"0")+iSm%>-<%=iE+":"+iEm%>
reason: <%=Misc.htmlEscape(reason)%>
notes: <%=Misc.htmlEscape(notes)%>">
										.<%=(view==0?(name.length()>len?name.substring(0,len):name):name).toUpperCase()%></font></a></td>
<%
        	  } else {
        			  //System.out.println(name+" / " +demographic_no);
                if (tickler_no.compareTo("") != 0) {
%>
										<a href="#"
											onClick="popupPage(700,1000, '../tickler/ticklerDemoMain.jsp?demoview=<%=demographic_no%>');return false;"
											title='<bean:message key="receptionist.appointmentreceptionistadminday.ticklerMsg"/>: <%=Misc.htmlEscape(tickler_note)%>'><font
											color="red">!</font></a>
<%              } %>
										<a href=#
											onClick="tsr('appointment_no=<%=appointment.get("appointment_no")%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=<%=demographic_no%>');return false;"
											title="<%=name%>
reason: <%=Misc.htmlEscape(reason)%>
notes: <%=Misc.htmlEscape(notes)%>">
										<%=view==0?(name.length()>len?name.substring(0,len):name):name%></a>
<%              if(len==lenLimitedL || view!=0) {%>
										<% if (vLocale.getCountry().equals("BR")) { %>
										<a href=#
											onClick="popupPage2('../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail_ptbr');return false;"
											title='<bean:message key="receptionist.appointmentreceptionistadminday.masterFile"/>'>
										| <bean:message
											key="provider.appointmentProviderAdminDay.btnM" /> </a>
										<%}else{%>
										<a href=#
											onClick="popupPage2('../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search_detail');return false;"
											title='<bean:message key="receptionist.appointmentreceptionistadminday.masterFile"/>'>
										| <bean:message
											key="provider.appointmentProviderAdminDay.btnM" /> </a>
										<%}%>
<%              } %>
										</font>
										</td>
<% 
              }
       		  bFirstFirstR = false;
       } // while
       out.println("<td width='1'></td></tr>"); //no grid display
     } // for
%>
									
								</table>
								<!-- end table for each provider schedule display --></td>
							</tr>
							<tr>
								<td ALIGN="center" BGCOLOR="<%=bColor?"#bfefff":"silver"%>">
								<b><input type='radio' name='flipview'
									onClick="goFilpView('<%=curProvider_no[nProvider]%>')"
									title='<bean:message key="receptionist.appointmentreceptionistadminday.formFlipView"/>'>
								<a href=#
									onClick="goZoomView('<%=curProvider_no[nProvider]%>','<%=curProviderName[nProvider]%>')"
									title='<bean:message key="receptionist.appointmentreceptionistadminday.zoomView"/>'>
								<%=curProviderName[nProvider]%></a></b></td>
							</tr>

						</table>
						<!-- end table for each provider name --></td>
<%
   } // for: end of display team a, etc.    
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
							href="receptionistcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day-1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
						&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10"
							HEIGHT="9" BORDER="0"
							ALT='<bean:message key="receptionist.appointmentreceptionistadminday.altImgViewPreviousDay"/>'
							vspace="2"></a> <b><span CLASS=title><%=strDayOfWeek%>,
						<%=strYear%>-<%=strMonth%>-<%=strDay%></span></b> <a
							href="receptionistcontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=(day+1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday">
						<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
							ALT='<bean:message key="receptionist.appointmentreceptionistadminday.altImgViewNextDay"/>'
							vspace="2">&nbsp;&nbsp;</a></td>
						<td ALIGN="RIGHT" BGCOLOR="Ivory"><a href="../logout.jsp"><bean:message
							key="global.btnLogout" /><img src="../images/next.gif" border="0"
							width="10" height="9" align="absmiddle"> &nbsp;</a></td>
					</TR>
				</table>
				</td>
			</tr>

		</table>
		</td>
	</tr>
</table>

</body>
</html:html>

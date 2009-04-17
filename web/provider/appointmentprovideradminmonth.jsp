<!--  
/* 
 * 
 * Copyright (c) 2001-2008. Department of Family Medicine, McMaster University. All Rights Reserved. *
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

<!--oscarMessenger Code block -->
<%@ taglib uri="/WEB-INF/msg-tag.tld" prefix="oscarmessage"%>
<!--/oscarMessenger Code block -->

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%
  if(session.getValue("user") == null)  response.sendRedirect("../logout.jsp");
  String curUser_no, curProvider_no,userfirstname,userlastname,mygroupno,n_t_w_w="";
  curProvider_no = (String) session.getAttribute("user");
  String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

  curUser_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  mygroupno = (String) session.getAttribute("groupno");  
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
  n_t_w_w = (String) session.getAttribute("newticklerwarningwindow");
}
    String newticklerwarningwindow=null;
    String default_pmm=null;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	newticklerwarningwindow = (String) session.getAttribute("newticklerwarningwindow");
	default_pmm = (String)session.getAttribute("default_pmm");
}    
  int startHour=Integer.parseInt(((String) session.getAttribute("starthour")).trim());
  int endHour=Integer.parseInt(((String) session.getAttribute("endhour")).trim());
  int everyMin=Integer.parseInt(((String) session.getAttribute("everymin")).trim());
  int view=0;
  String providerview = request.getParameter("providerview")==null?(mygroupno.equals(".default")?curProvider_no:("_grp_"+mygroupno)):request.getParameter("providerview") ;
  //String providerview = request.getParameter("providerview")==null?curProvider_no:request.getParameter("providerview") ;
  int NameMaxLen = 15;
%>
<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.net.*,java.sql.*,oscar.*"
	errorPage="errorpage.jsp"%>
<jsp:useBean id="oscarVariables" class="java.util.Properties"
	scope="session" />
<jsp:useBean id="scheduleHolidayBean" class="java.util.Hashtable"
	scope="session" />
<jsp:useBean id="providerNameBean" class="oscar.Dict" scope="page" />
<jsp:useBean id="myGrpBean" class="java.util.Properties" scope="page" />

<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>

<%
  String prov= ((String ) oscarVariables.getProperty("billregion","")).trim().toUpperCase();
  String resourcebaseurl = "http://resource.oscarmcmaster.org/oscarResource/";
  List<Map> resultList = oscarSuperManager.find("providerDao", "search_resource_baseurl", new String[] {"resource_baseurl"});
  for (Map url : resultList) {
 	  resourcebaseurl = (String) url.get("value");
  }

	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR); //curYear should be the real now date
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  int year = Integer.parseInt((request.getParameter("year")).trim());
  int month = Integer.parseInt((request.getParameter("month")).trim());
  int day = Integer.parseInt((request.getParameter("day")).trim());
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
    resultList = oscarSuperManager.find("providerDao", "search_scheduleholiday", new String[] {(year-1)+"-"+month+"-01"});
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
  
  //initial myGrp bean
  if(providerview.startsWith("_grp_",0)) {
	String curGrp = providerview.substring(5);
	resultList = oscarSuperManager.find("providerDao", "searchmygroupprovider", new Object[] {curGrp});
	for (Map provider : resultList) {
		myGrpBean.setProperty(String.valueOf(provider.get("provider_no")), curGrp);
	}
  }
  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);   
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message
	key="provider.appointmentprovideradminmonth.title" /></title>
<link rel="stylesheet" href="../receptionist/receptionistapptstyle.css"
	type="text/css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">

<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
   <style type="text/css">    
        #navlist{
            margin: 0;
            padding: 0;
            white-space: nowrap;
        }    

        #navlist li {
            padding-top: 0.5px;
            padding-bottom: 0.5px;
            padding-left: 2.5px;
            padding-right: 2.5px;
            display: inline; 
        }

        #navlist li:hover { color: #fff; background-color: #486ebd; }
        #navlist li a:hover { color: #fff; background-color: #486ebd; }
        
        
    </style>
</head>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script language="JavaScript">
<!--
function setfocus() {
  document.jumptodate.year.focus();
  document.jumptodate.year.select();
}


//<!--oscarMessenger code block-->
function popupOscarRx(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(varpage, "oscar_appt", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}
//<!--/oscarMessenger code block -->


    function selectprovider(s) {
        if(s.options[s.selectedIndex].value.indexOf("_grp_")!=-1 ) 
        {
            var newGroupNo = s.options[s.selectedIndex].value.substring(5) ;
            <%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){%>
            {
                popupOscarRx(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=n_t_w_w%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
            }
            <%}else {%>
                popupOscarRx(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo);
            <%}%>
        } 
        else 
        {
            if(self.location.href.lastIndexOf("&providerview=") > 0 ) 
                a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
            else 
                a = self.location.href;
            self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
        }
    }


  
function refresh1() {
  var u = self.location.href;
  if(u.lastIndexOf("&providerview=") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("&providerview=")) ;
  } else {
    history.go(0);
  }
}

<%-- Refresh tab alerts --%>
function refreshAllTabAlerts() {
    refreshTabAlerts("oscar_new_lab");
    refreshTabAlerts("oscar_new_msg");
    refreshTabAlerts("oscar_new_tickler");
    refreshTabAlerts("oscar_scratch");
}

function callRefreshTabAlerts(id) {
    setTimeout("refreshTabAlerts('"+id+"')", 10);
}

function refreshTabAlerts(id) {
    var url = "../provider/tabAlertsRefresh.jsp";
    var pars = "id=" + id;
    
    var myAjax = new Ajax.Updater(id, url, {method: 'get', parameters: pars});
}
//-->
</SCRIPT>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="refreshAllTabAlerts();" topmargin="0" leftmargin="0"
	rightmargin="0">

<div id="jumpmenu"
	style="position: absolute; width: 140px; height: 100px; z-index: 2; left: 240px; top: 30px; visibility: hidden">
<table width="85%" bgcolor="#F0F0F0" cellpadding="0" cellspacing="2">
	<tr>
		<td bgcolor="#F0F0F0" ALIGN="LEFT">
		<p><bean:message
			key="provider.appointmentprovideradminmonth.msgDateFormat" /></p>
		</td>
	</tr>
	<tr>
		<td bgcolor="#F0F0F0" ALIGN="LEFT">
		<p><bean:message
			key="provider.appointmentprovideradminmonth.msgDateDays" /></p>
		</td>
	</tr>
	<tr>
		<td bgcolor="#F0F0F0" ALIGN="LEFT">
		<p><bean:message
			key="provider.appointmentprovideradminmonth.msgDateWeeks" /></p>
		</td>
	</tr>
	<tr>
		<td bgcolor="#F0F0F0" ALIGN="LEFT">
		<p><bean:message
			key="provider.appointmentprovideradminmonth.msgDateMonths" /></p>
		</td>
	</tr>
</table>
</div>

<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%">
	<tr>
		<td>
		<ul id="navlist">
			<li><a
				href="providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday"
				TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>'
				OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>' ; return true"><bean:message
				key="global.day" /></a></li>
			<li><a
				href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth"
				TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewMonthSched"/>'
				OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewMonthSched"/>' ; return true"><bean:message
				key="global.month" /></a></li>
			<li><a href="#"
				ONCLICK="popupOscarRx(550,687,'<%=resourcebaseurl%>');return false;"
				title="<bean:message key="global.resources"/>"
				onmouseover="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewResources"/>';return true"><bean:message
				key="global.resources" /></a></li>
			<li><caisi:isModuleLoad moduleName="caisi">
				<a HREF="../PMmodule/ClientSearch2.do"
					TITLE='<bean:message key="global.searchPatientRecords"/>'
					OnMouseOver="window.status='<bean:message key="global.searchPatientRecords"/>' ; return true"><bean:message
					key="provider.appointmentProviderAdminDay.search" /></a>
			</caisi:isModuleLoad> <caisi:isModuleLoad moduleName="caisi" reverse="true">
				<a HREF="#"
					ONCLICK="popupOscarRx(550,687,'../demographic/search.jsp');return false;"
					TITLE='<bean:message key="global.searchPatientRecords"/>'
					OnMouseOver="window.status='<bean:message key="global.searchPatientRecords"/>' ; return true"><bean:message
					key="provider.appointmentProviderAdminDay.search" /></a>
			</caisi:isModuleLoad></li>
			<li><a HREF="#"
				ONCLICK="popupOscarRx(650,900,'../report/reportindex.jsp','reportPage');return false;"
				TITLE='<bean:message key="global.genReport"/>'
				OnMouseOver="window.status='<bean:message key="global.genReport"/>' ; return true"><bean:message
				key="global.report" /></a></li>
			<li>
			<%if (vLocale.getCountry().equals("BR")) { %> <a HREF="#"
				ONCLICK="popupOscarRx(550,687,'../oscar/billing/consultaFaturamentoMedico/init.do');return false;"
				TITLE='<bean:message key="global.genBillReport"/>'
				onMouseOver="window.status='<bean:message key="global.genBillReport"/>';return true"><bean:message
				key="global.billing" /></a> <% } else {%> <a HREF="#"
				ONCLICK="popupOscarRx(550,687,'../billing/CA/<%=prov%>/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;"
				TITLE='<bean:message key="global.genBillReport"/>'
				onMouseOver="window.status='<bean:message key="global.genBillReport"/>';return true"><bean:message
				key="global.billing" /></a> <% } %>
			</li>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_appointment.doctorLink" rights="r">
				<li><a HREF="#"
					ONCLICK="popupOscarRx(600,900,'../oscarMDS/Index.jsp?providerNo=<%=curUser_no%>', '<bean:message key="global.lab"/>');return false;"
					TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'>
				<span id="oscar_new_lab"></span> </a> <oscar:newUnclaimedLab>
					<a class="tabalert" HREF="#"
						ONCLICK="popupOscarRx(600,900,'../oscarMDS/Index.jsp?providerNo=0&searchProviderNo=0&status=N&lname=&fname=&hnum=&pageNum=1&startIndex=0', '<bean:message key="global.lab"/>');return false;"
						TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'>*</a>
				</oscar:newUnclaimedLab></li>
			</security:oscarSec>
			<li><a HREF="#"
				ONCLICK="popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')"
				title="<bean:message key="global.messenger"/>"> <span
				id="oscar_new_msg"></span></a></li>
			<li><a HREF="#"
				ONCLICK="popupOscarRx(625,900,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')"
				title="<bean:message key="provider.appointmentProviderAdminDay.viewConReq"/>"><bean:message
				key="global.con" /></a></li>
			<li><!-- remove this and let providerpreference check --> <caisi:isModuleLoad
				moduleName="ticklerplus">
				<a href="#"
					onClick="popupOscarRx(500,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>');return false;"
					TITLE='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>'
					OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' ; return true"><bean:message
					key="global.pref" /></a>
			</caisi:isModuleLoad> <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
				<a href="#"
					onClick="popupOscarRx(400,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>');return false;"
					TITLE='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>'
					OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' ; return true"><bean:message
					key="global.pref" /></a>
			</caisi:isModuleLoad></li>
			<li><a HREF="#"
				onclick="popupOscarRx('700', '1000', '../dms/documentReport.jsp?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>', 'edocView');"
				TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewEdoc"/>'><bean:message
				key="global.edoc" /></a></li>
			<li><caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
				<a HREF="#"
					ONCLICK="popupOscarRx(550,687,'../tickler/ticklerMain.jsp','<bean:message key="global.tickler"/>');return false;"
					TITLE='<bean:message key="global.tickler"/>'> <span
					id="oscar_new_tickler"></span></a>
			</caisi:isModuleLoad> <caisi:isModuleLoad moduleName="ticklerplus">
				<a HREF="#"
					ONCLICK="popupOscarRx(550,687,'../Tickler.do','<bean:message key="global.tickler"/>');return false;"
					TITLE='Tickler+'> <span id="oscar_new_tickler"></span></a>
			</caisi:isModuleLoad></li>
			<oscar:oscarPropertiesCheck property="WORKFLOW" value="yes">
				<li><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupOscarRx(700,1000,'../oscarWorkflow/WorkFlowList.jsp','<bean:message key="global.workflow"/>')"><bean:message
					key="global.btnworkflow" /></a></li>
			</oscar:oscarPropertiesCheck>
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc"
				rights="r">
				<li><a HREF="#"
					ONCLICK="popupOscarRx(700,687,'../admin/admin.jsp', 'Admin');return false;"><bean:message
					key="global.admin" /></a></li>
			</security:oscarSec>

			<%int menuTagNumber=0; %>
			<caisi:isModuleLoad moduleName="caisi">
				<li><a href='<html:rewrite page="/PMmodule/ProviderInfo.do"/>'>Program</a>
				<% menuTagNumber++ ; %>
				</li>
			</caisi:isModuleLoad>
		</ul>
		</td>

		<form method="post" name="jumptodate" action="providercontrol.jsp">
		<td align="right" valign="bottom"><INPUT TYPE="text" NAME="year"
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
		<input type="hidden" name="Go" value=""> <INPUT TYPE="SUBMIT"
			VALUE="<bean:message key="provider.appointmentprovideradminmonth.btnGo"/>"
			onclick="document.forms['jumptodate'].Go.value='GO'; document.forms['jumptodate'].submit();"
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
					href="providercontrol.jsp?year=<%=year%>&month=<%=(month-1)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth&providerview=<%=providerview%>">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0" ALT="<%=arrayMonthOfYear[((month+10)%12)]%>" vspace="2"></a>
				<b><span CLASS=title><%=strYear%>-<%=strMonth%></span></b> <a
					href="providercontrol.jsp?year=<%=year%>&month=<%=(month+1)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth&providerview=<%=providerview%>">
				<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT="<%=arrayMonthOfYear[month%12]%>" vspace="2">&nbsp;&nbsp;</a></td>
				<TD ALIGN="center" width="33%"><B> <%= arrayMonthOfYear[(month+11)%12] %>
				</b></TD>
				<td ALIGN="RIGHT"><select name="provider_no"
					onChange="selectprovider(this)">
					<option value="all" <%=providerview.equals("all")?"selected":""%>><bean:message
						key="provider.appointmentprovideradminmonth.formAllProviders" /></option>
<%
	resultList = oscarSuperManager.find("providerDao", "searchmygroupno", new Object[] {});
	for (Map group : resultList) {
%>
					<option value="<%="_grp_"+group.get("mygroup_no")%>"
						<%=mygroupno.equals(group.get("mygroup_no"))?"selected":""%>><bean:message
						key="provider.appointmentprovideradminmonth.formGRP" />: <%=group.get("mygroup_no")%></option>
<%
	}

	resultList = oscarSuperManager.find("providerDao", "searchprovider", new Object[] {});
	for (Map provider : resultList) {
		providerNameBean.setDef(String.valueOf(provider.get("provider_no")), provider.get("last_name")+","+provider.get("first_name"));
%>
					<option value="<%=provider.get("provider_no")%>"
						<%=providerview.equals(provider.get("provider_no"))?"selected":""%>><%=providerNameBean.getShortDef(String.valueOf(provider.get("provider_no")), "", NameMaxLen)%></option>
<%
	}
%>
				</select> <a href="../logout.jsp"><bean:message
					key="provider.appointmentprovideradminmonth.btnlogOut" /> <img
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
    if(providerview.equals("all") || providerview.startsWith("_grp_",0)) {
      param[0] = year+"-"+month+"-"+"1";
      param[1] = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"1";
      resultList = oscarSuperManager.find("providerDao", "search_scheduledate_datep", param);
    } else {
      String[] param1 = new String[3];
      param1[0] = year+"-"+month+"-"+"1";
      param1[1] = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"1";
      param1[2] = providerview;
      resultList = oscarSuperManager.find("providerDao", "search_scheduledate_singlep", param1);
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
						href='providercontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'>
					<span class='date'>&nbsp;<%=dateGrid[i][j] %> </span> <font
						size="-2" color="blue"><%=strHolidayName.toString()%></font> <%
  while (bFistEntry?it.hasNext():true) { 
    date = bFistEntry?it.next():date;
    if(!String.valueOf(date.get("sdate")).equals(year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j])) ) {
      bFistEntry = false;
      break;
    } else {    //System.out.println("ok "+rsgroup.getString("sdate")+" "+dateGrid[i][j]);
      bFistEntry = true;
      if(String.valueOf(date.get("available")).equals("0")) continue;
    }
    if(!providerview.startsWith("_grp_",0) || myGrpBean.containsKey(String.valueOf(date.get("provider_no"))) ) {
%> <br>
					<span class='datepname'>&nbsp;<%=providerNameBean.getShortDef(String.valueOf(date.get("provider_no")),"",NameMaxLen )%></span><span
						class='datephour'><%=date.get("hour") %></span><span
						class='datepreason'><%=date.get("reason") %></span> <%  }  } %>
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
					bgcolor="#99cccc">
					<tr bgcolor="#CCCCCC">
						<%  	now=new GregorianCalendar(year,(month-2),day);
                  year = now.get(Calendar.YEAR); //month should be the current main display date, not the real now date
                  month = (now.get(Calendar.MONTH)+1);
                  day = now.get(Calendar.DAY_OF_MONTH);
                  aDate = new DateInMonthTable(year, month-1, 1);
                  dateGrid = aDate.getMonthDateGrid();
            %>
						<td><b> <a
							href="providercontrol.jsp?year=<%=year%>&month=<%=(month)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth"
							title="Last Month: <%=arrayMonthOfYear[((month+10)%12)]%>">
						&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10"
							HEIGHT="9" BORDER="0"
							ALT="Last Month: <%=arrayMonthOfYear[((month+10)%12)]%>"
							vspace="2"> <bean:message
							key="provider.appointmentprovideradminmonth.btnLastMonth" /> </a>&nbsp;
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
								href='providercontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=dateGrid[i][j+1]==0?1:dateGrid[i][j+1]%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=week&dboperation=searchapptweek'>
							<%=(i+1)%></font></td>
							<%
                    continue;
                  }
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    if(dateGrid[i][j]==day) {
            %>
							<td align='center'><a
								href='providercontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(day)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday'>
							<font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">
							<div class='specialtxt'><%= dateGrid[i][j] %></div>
							</font></a></td>
							<%      } else {
            %>
							<td align='center'><font FACE='VERDANA,ARIAL,HELVETICA'
								SIZE='2' color='white'><a
								href='providercontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'>
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
						&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; <%=year%>-<%=month%> </b><a
							href="providercontrol.jsp?year=<%=year%>&month=<%=(month)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth"
							title="Next Month: <%=arrayMonthOfYear[month%12]%>"> &nbsp;
						&nbsp; &nbsp; <bean:message
							key="provider.appointmentprovideradminmonth.btnNextMonth" /> <img
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
								href='providercontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=dateGrid[i][j+1]==0?1:dateGrid[i][j+1]%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=week&dboperation=searchapptweek'>
							<%=(i+1)%></font></td>
							<%
                    continue;
                  }
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    if(dateGrid[i][j]==day) {
            %>
							<td align='center'><a
								href='providercontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(day)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday'>
							<font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">
							<div class='specialtxt'><%= dateGrid[i][j] %></div>
							</font></a></td>
							<%      } else {
            %>
							<td align='center'><font FACE='VERDANA,ARIAL,HELVETICA'
								SIZE='2' color='white'><a
								href='providercontrol.jsp?year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'>
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
					href="providercontrol.jsp?year=<%=year%>&month=<%=(month-1)%>&day=<%=(day)%>&displaymode=month&dboperation=searchappointmentmonth">
				&nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9"
					BORDER="0" ALT="View Previous MONTH" vspace="2"></a> <b><span
					CLASS=title><%=strYear%>-<%=strMonth%></span></b> <a
					href="providercontrol.jsp?year=<%=year%>&month=<%=(month+1)%>&day=<%=day%>&displaymode=month&dboperation=searchappointmentmonth">
				<img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0"
					ALT="View Next MONTH" vspace="2">&nbsp;&nbsp;</a></td>
				<TD ALIGN="center" BGCOLOR="ivory" width="33%"></TD>
				<td ALIGN="RIGHT" BGCOLOR="Ivory"><a href="../logout.jsp"><bean:message
					key="provider.appointmentprovideradminmonth.btnlogOut" /> <img
					src="../images/next.gif" border="0" width="10" height="9"
					align="absmiddle"> &nbsp;</a></td>
			</tr>
		</table>
		</td>
	</tr>
</table>

</body>
<script language="JavaScript">
// keycode shortcut block added by phc
// pasted from appointmentprovideradminday.jsp 
// please check documentation there
document.onkeypress=function(e){
	evt = e || window.event;  // window.event is the IE equivalent
	if (evt.altKey) {
		//use if (evt.altKey || evt.metaKey) Alt+A (and)/or for Mac when the browser supports it, Command+A
		switch(evt.keyCode) {
			case <bean:message key="global.adminShortcut"/> : popupOscarRx(700,687,'../admin/admin.jsp');  return false;  //run code for 'A'dmin
			case <bean:message key="global.billingShortcut"/> : popupOscarRx(600,1000,'../billing/CA/<%=prov%>/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;  //code for 'B'illing
			case <bean:message key="global.calendarShortcut"/> : popupOscarRx(310,430,'../share/CalendarPopup.jsp?urlfrom=../provider/providercontrol.jsp&year=<%=strYear%>&month=<%=strMonth%>&param=<%=URLEncoder.encode("&view=0&displaymode=day&dboperation=searchappointmentday","UTF-8")%>');  return false;  //run code for 'C'alendar
			case <bean:message key="global.edocShortcut"/> : popupOscarRx('700', '1000', '../dms/documentReport.jsp?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>', 'edocView');  return false;  //run code for e'D'oc
			case <bean:message key="global.resourcesShortcut"/> : popupOscarRx(550,687,'<%=resourcebaseurl%>'); return false; // code for R'e'sources
			case <bean:message key="global.helpShortcut"/> : popupOscarRx(600,750,'<%=resourcebaseurl%>');  return false;  //run code for 'H'elp
			case <bean:message key="global.ticklerShortcut"/> : {  
				<caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
					popupOscarRx(700,1000,'../tickler/ticklerMain.jsp','<bean:message key="global.tickler"/>') //run code for t'I'ckler  
				</caisi:isModuleLoad>
				<caisi:isModuleLoad moduleName="ticklerplus">
					popupOscarRx(700,1000,'../Tickler.do','<bean:message key="global.tickler"/>'); //run code for t'I'ckler+  
				</caisi:isModuleLoad>
				return false;
			}
			case <bean:message key="global.labShortcut"/> : popupOscarRx(600,900,'../oscarMDS/Index.jsp?providerNo=<%=curUser_no%>', '<bean:message key="global.lab"/>');  return false;  //run code for 'L'ab
			case <bean:message key="global.msgShortcut"/> : popupOscarRx(600,900,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>'); return false;  //run code for 'M'essage
			case <bean:message key="global.monthShortcut"/> : window.open("providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth","_self"); return false ;  //run code for Mo'n'th
			case <bean:message key="global.conShortcut"/> : popupOscarRx(625,900,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>');  return false;  //run code for c'O'nsultation
			case <bean:message key="global.reportShortcut"/> : popupOscarRx(650,900,'../report/reportindex.jsp','reportPage');  return false;  //run code for 'R'eports
			case <bean:message key="global.prefShortcut"/> : {
				    <caisi:isModuleLoad moduleName="ticklerplus">
					popupOscarRx(500,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>'); //run code for tickler+ 'P'references 
					return false;   
				    </caisi:isModuleLoad>	
			            <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
					popupOscarRx(400,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>'); //run code for 'P'references 
					return false; 
			            </caisi:isModuleLoad>
			}
			case <bean:message key="global.searchShortcut"/> : popupOscarRx(550,687,'../demographic/search.jsp');  return false;  //run code for 'S'earch
			case <bean:message key="global.dayShortcut"/> : window.open("providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday","_self") ;  return false;  //run code for 'T'oday                       
			case <bean:message key="global.viewShortcut"/> : {
				<% if(request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1") ) { %>
				         review('0');  return false; //scheduled providers 'V'iew 
				<% } else {  %>
				         review('1');  return false; //all providers 'V'iew 
				<% } %>
			}
			case <bean:message key="global.workflowShortcut"/> : popupOscarRx(700,1000,'../oscarWorkflow/WorkFlowList.jsp','<bean:message key="global.workflow"/>'); return false ; //code for 'W'orkflow
			case <bean:message key="global.myoscarShortcut"/> : popupOscarRx('600', '900','../phr/PhrMessage.do?method=viewMessages','INDIVOMESSENGER2<%=curUser_no%>')
			default : return;
               }
	}
	if (evt.ctrlKey) {
               switch(evt.keyCode || evt.charCode) {
			case <bean:message key="global.btnLogoutShortcut"/> : window.open('../logout.jsp','_self');  return false;  // 'Q'uit/log out
			default : return;
               }        

        }
}

</script>
</html:html>

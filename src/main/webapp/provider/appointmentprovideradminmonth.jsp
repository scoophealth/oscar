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

<%@ page import="org.oscarehr.common.dao.ProviderSiteDao"%>
<%@ page import="org.oscarehr.util.SessionConstants"%>
<%@ page import="org.oscarehr.common.model.ProviderPreference"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<%@ page import="org.oscarehr.common.dao.ScheduleHolidayDao" %>
<%@ page import="org.oscarehr.common.model.ScheduleHoliday" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="oscar.util.ConversionUtils" %>
<%@ page import="org.oscarehr.common.dao.ScheduleDateDao" %>
<%@ page import="org.oscarehr.common.model.ScheduleDate" %>
<%@ page import="org.oscarehr.common.dao.ProviderSiteDao" %>

<%
	UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
    ScheduleHolidayDao scheduleHolidayDao = SpringUtils.getBean(ScheduleHolidayDao.class);
    MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
    ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
    ScheduleDateDao scheduleDateDao = SpringUtils.getBean(ScheduleDateDao.class);
    ProviderSiteDao providerSiteDao = SpringUtils.getBean(ProviderSiteDao.class);
%>

<%!
//multisite starts =====================
private	List<Site> sites; 
private boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();

private List<Site> curUserSites = new ArrayList<Site>();
private String [] curScheduleMultisite;
private List<String> siteProviderNos = new ArrayList<String>();
private List<String> siteGroups = new ArrayList<String>();
private String selectedSite = null;
private HashMap<String,String> siteBgColor = new HashMap<String,String>();
private HashMap<String,String> CurrentSiteMap = new HashMap<String,String>();
private String getSiteHTML(String reason, List<Site> sites) {
	 if (reason==null||reason.trim().length()==0) 
		 return "";
	 else 
		 return "<span style='background-color:"+ApptUtil.getColorFromLocation(sites, reason)+"'>"+ApptUtil.getShortNameFromLocation(sites, reason)+"</span>";	
}
//multisite ends =====================
%>

<!--oscarMessenger Code block -->
<%@ taglib uri="/WEB-INF/msg-tag.tld" prefix="oscarmessage"%>
<!--/oscarMessenger Code block -->

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<%
  String curUser_no, curProvider_no,userfirstname,userlastname,mygroupno,n_t_w_w="";
  curProvider_no = (String) session.getAttribute("user");
  String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

  ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);

  curUser_no = (String) session.getAttribute("user");
  userfirstname = (String) session.getAttribute("userfirstname");
  userlastname = (String) session.getAttribute("userlastname");
  mygroupno = (request.getParameter("mygroup_no") == null ? providerPreference.getMyGroupNo() : request.getParameter("mygroup_no"));  
  if(mygroupno==null)
	  mygroupno=".default";
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
  n_t_w_w = (String) session.getAttribute("newticklerwarningwindow");
}
    String newticklerwarningwindow=null;
    String default_pmm=null;
if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	newticklerwarningwindow = (String) session.getAttribute("newticklerwarningwindow");
	default_pmm = (String)session.getAttribute("default_pmm");
}    

  int startHour=providerPreference.getStartHour();
  int endHour=providerPreference.getEndHour();
  int everyMin=providerPreference.getEveryMin();
  int view=0;

  Collection<Integer> eforms = providerPreference.getAppointmentScreenEForms();
  StringBuilder eformIds = new StringBuilder();
  if(eforms != null) {
	  for( Integer eform : eforms ) {
	  	eformIds = eformIds.append("&eformId=" + eform);
	  }
  }

  Collection<String> forms = providerPreference.getAppointmentScreenForms();
  StringBuilder ectFormNames = new StringBuilder();
  if(forms != null) {
	  for( String formName : forms ) {
	  	ectFormNames = ectFormNames.append("&encounterFormName=" + formName);
	  }
  }
  
  
  boolean isMygroupnoNumber = true;
  
  if (mygroupno!=null) {
      try {
          Integer.parseInt(mygroupno);
      }
      catch (NumberFormatException ex) {
    	  isMygroupnoNumber = false;
      }	  
  }
  else {
	  isMygroupnoNumber = false;
  }
  
  String providerview;
  
  if (isMygroupnoNumber) {
	  providerview = request.getParameter("providerview")==null?(mygroupno.equals(".default")?curProvider_no:mygroupno):request.getParameter("providerview") ;
  }
  else {
	  providerview = request.getParameter("providerview")==null?(mygroupno.equals(".default")?curProvider_no:("_grp_"+mygroupno)):request.getParameter("providerview") ;
  }
  //String providerview = request.getParameter("providerview")==null?curProvider_no:request.getParameter("providerview") ;
  int NameMaxLen = 15;
%>
<%
//multisite starts =====================
	boolean isSiteAccessPrivacy=false;
	boolean isTeamAccessPrivacy=false; 
%>

<%
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment,_month" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_appointment");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isTeamAccessPrivacy=true; %>
</security:oscarSec>

<% 
if (bMultisites) {
	SiteDao siteDao = (SiteDao)SpringUtils.getBean("siteDao");
	sites = siteDao.getAllActiveSites(); 
	
	String requestSite = request.getParameter("site") ;
	if (requestSite!=null) 
	{
		requestSite = (requestSite.equals("none") ? null : requestSite);
		session.setAttribute("site_selected", requestSite );
	}
	selectedSite = (requestSite == null ? (String)session.getAttribute("site_selected") : requestSite) ;
	
	if (isSiteAccessPrivacy || isTeamAccessPrivacy) {
		//user has Access Privacy, set user provider and group list
		String siteManagerProviderNo = (String) session.getAttribute("user");
		curUserSites = siteDao.getActiveSitesByProviderNo(siteManagerProviderNo);
		if (selectedSite==null) {
			siteProviderNos = siteDao.getProviderNoBySiteManagerProviderNo(siteManagerProviderNo);
			siteGroups = siteDao.getGroupBySiteManagerProviderNo(siteManagerProviderNo);
		}
	}
	else {
		//get all active site as user site list
		curUserSites = sites;
	}
	
	for (Site s : curUserSites) {
		CurrentSiteMap.put(s.getName(),"Y");
	}
	
	CurrentSiteMap.put("NONE", "Y"); // added by vic for the reason that some provider could work in multiple clinics in same day, when the schedule template will set the default location to NONE.

	// a site has been seleceted
	if (selectedSite != null) {
		//get site provider list
		siteProviderNos = siteDao.getProviderNoBySiteLocation(selectedSite);
		siteGroups = siteDao.getGroupBySiteLocation(selectedSite);
	}
	
	//get all sites bgColors
	for (Site st : sites) {
		siteBgColor.put(st.getName(),st.getBgColor());
	}
	

}
//multisite ends =======================
%>
<%@ page import="oscar.dao.*" %>
<%@ page
	import="java.lang.*, java.util.*, java.text.*,java.net.*,java.sql.*,oscar.*"
	errorPage="errorpage.jsp"%>
<% 
	java.util.Properties oscarVariables = OscarProperties.getInstance();
%>
	
<jsp:useBean id="scheduleHolidayBean" class="java.util.Hashtable"
	scope="session" />
<jsp:useBean id="providerNameBean" class="oscar.Dict" scope="page" />
<jsp:useBean id="myGrpBean" class="java.util.Properties" scope="page" />


<%
	String prov=  oscarVariables.getProperty("billregion","").trim().toUpperCase();
	String resourcebaseurl =  oscarVariables.getProperty("resource_base_url");
	
	UserProperty rbu = userPropertyDao.getProp("resource_baseurl");
	if(rbu != null) {
		resourcebaseurl = rbu.getValue();
	}
	    
    String resourcehelpHtml = ""; 
    UserProperty rbuHtml = userPropertyDao.getProp("resource_helpHtml");
    if(rbuHtml != null) {
    	resourcehelpHtml = rbuHtml.getValue();
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

  List<Map<String, Object>> resultList = null;
  //initial holiday bean
  if(scheduleHolidayBean.isEmpty() ) {
	 for(ScheduleHoliday sd: scheduleHolidayDao.findAfterDate(ConversionUtils.fromDateString((year-1)+"-"+month+"-01"))) {
		 scheduleHolidayBean.put(ConversionUtils.toDateString(sd.getId()), new HScheduleHoliday(sd.getHolidayName()));
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
	for(MyGroup g:myGroupDao.getGroupByGroupNo(curGrp)) {
		myGrpBean.setProperty(g.getId().getProviderNo(), curGrp);	
	}
	
  }
  java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);   
%>


<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="oscar.appt.JdbcApptImpl"%>
<%@page import="oscar.appt.ApptUtil"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="monthview"/>

<title><bean:message
	key="provider.appointmentprovideradminmonth.title" /></title>
<link rel="stylesheet" href="../css/receptionistapptstyle.css" type="text/css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<link rel="stylesheet" href="../css/helpdetails.css" type="text/css">
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
<script language="javascript" type="text/javascript" src="../share/javascript/Oscar.js" ></script>
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
                popupOscarRx(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=n_t_w_w%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo) + "<%=eformIds.toString()%><%=ectFormNames.toString()%>";
            }
            <%}else {%>
                popupOscarRx(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo + "<%=eformIds.toString()%><%=ectFormNames.toString()%>");
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
  } 
  if(u.lastIndexOf("&mygroup_no=") > 0) { // group switch should be treated same as provider switch
		    self.location.href = u.substring(0,u.lastIndexOf("&mygroup_no=")) ;
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
<body bgcolor="#EEEEFF"	onLoad="refreshAllTabAlerts();" topmargin="0" leftmargin="0">

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
	<td align="center" >
		<a href="http://oscarmcmaster.org/" target="_blank" title="OSCAR EMR"><img src="<%=request.getContextPath()%>/images/oscar_small.png" border="0"></a>
	</td>
		<td>
		<ul id="navlist">
			<li><a href='providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=0&displaymode=day&dboperation=searchappointmentday&viewall=1'><bean:message key="provider.appointmentProviderAdminDay.schedView"/></a></li>
			 <li>
			 <a href='providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=0&displaymode=day&dboperation=searchappointmentday&caseload=1&clProv=<%=curUser_no%>'><bean:message key="global.caseload"/></a>
			 </li>
			<li><a href="#"
				ONCLICK="popupOscarRx(550,687,'<%=resourcebaseurl%>');return false;"
				title="<bean:message key="provider.appointmentProviderAdminDay.viewResources"/>"
				onmouseover="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewResources"/>';return true"><bean:message
				key="oscarEncounter.Index.clinicalResources" /></a></li>
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
				ONCLICK="popupOscarRx(650,1024,'../report/reportindex.jsp','reportPage');return false;"
				TITLE='<bean:message key="global.genReport"/>'
				OnMouseOver="window.status='<bean:message key="global.genReport"/>' ; return true"><bean:message
				key="global.report" /></a></li>
			
			<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r">
			<li>
				<a HREF="#"
					ONCLICK="popupOscarRx(550,687,'../billing/CA/<%=prov%>/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;"
					TITLE='<bean:message key="global.genBillReport"/>'
					onMouseOver="window.status='<bean:message key="global.genBillReport"/>';return true"><bean:message
					key="global.billing" /></a>
			</li>
			</security:oscarSec>
			
			<security:oscarSec roleName="<%=roleName$%>"
				objectName="_appointment.doctorLink" rights="r">
				<li><a HREF="#"
					ONCLICK="popupOscarRx(600,1024,'../dms/inboxManage.do?method=prepareForIndexPage&providerNo=<%=curUser_no%>', '<bean:message key="global.inbox"/>');return false;"
					TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'>
				<span id="oscar_new_lab"></span> </a> <oscar:newUnclaimedLab>
					<a class="tabalert" HREF="#"
						ONCLICK="popupOscarRx(600,1024,'../dms/inboxManage.do?method=prepareForIndexPage&providerNo=0&searchProviderNo=0&status=N&lname=&fname=&hnum=&pageNum=1&startIndex=0', '<bean:message key="global.lab"/>');return false;"
						TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'>*</a>
				</oscar:newUnclaimedLab></li>
			</security:oscarSec>
			<li><a HREF="#"
				ONCLICK="popupOscarRx(600,1024,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')"
				title="<bean:message key="global.messenger"/>"> <span
				id="oscar_new_msg"></span></a></li>
			<li><a HREF="#"
				ONCLICK="popupOscarRx(625,1024,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')"
				title="<bean:message key="provider.appointmentProviderAdminDay.viewConReq"/>"><bean:message
				key="global.con" /></a></li>
			<li><!-- remove this and let providerpreference check --> <caisi:isModuleLoad
				moduleName="ticklerplus">
				<a href="#"
					onClick="popupOscarRx(715,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>');return false;"
					TITLE='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>'
					OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' ; return true"><bean:message
					key="global.pref" /></a>
			</caisi:isModuleLoad> <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
				<a href="#"
					onClick="popupOscarRx(715,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>');return false;"
					TITLE='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>'
					OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' ; return true"><bean:message
					key="global.pref" /></a>
			</caisi:isModuleLoad></li>
			<li><a HREF="#"
				onclick="popupOscarRx('700', '1024', '../dms/documentReport.jsp?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>', 'edocView');"
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
					onClick="popupOscarRx(700,1024,'../oscarWorkflow/WorkFlowList.jsp','<bean:message key="global.workflow"/>')"><bean:message
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

		
		<td align="right" valign="bottom">
		
		  <a href="javascript: function myFunction() {return false; }" onClick="popup(700,1000,'../scratch/index.jsp','scratch')"><span id="oscar_scratch"></span></a>&nbsp;
		  
			<%if(resourcehelpHtml==""){ %>
				<a href="javascript:void(0)" onClick ="popupPage(600,750,'<%=resourcebaseurl%>')"><bean:message key="global.help"/></a>
			<%}else{%>
			<div id="help-link">
				
				    <a href="javascript:void(0)" onclick="document.getElementById('helpHtml').style.display='block';document.getElementById('helpHtml').style.right='0px';"><bean:message key="global.help"/></a>
			
				<div id="helpHtml">
				<div class="help-title">Help</div>
				
				<div class="help-body">
				
				<%=resourcehelpHtml%>
				</div>
				<a href="javascript:void(0)" class="help-close" onclick="document.getElementById('helpHtml').style.right='-280px';document.getElementById('helpHtml').style.display='none'">(X)</a>
					</div>
			
			</div>
			<%}%>
			
			
			| <a href="../logout.jsp"><bean:message key="provider.appointmentprovideradminmonth.btnlogOut" />  &nbsp;</a>

			</td>
		
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
					ALT="<%=arrayMonthOfYear[month%12]%>" vspace="2">&nbsp;&nbsp;</a>
				
         		| <u><a href="providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=0&displaymode=day&dboperation=searchappointmentday&viewall=1"  title="<bean:message key="provider.appointmentProviderAdminDay.viewAllProv"/>"><bean:message key="provider.appointmentProviderAdminDay.viewAll"/></a></u>				
					
				| <a
				href="providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday"
				TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>'
				OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>' ; return true"><bean:message key="global.today" /></a>	
				
				| <span style="color:#333">Month</span>
				
				</td>
				<TD ALIGN="center" width="33%"><B> <%= arrayMonthOfYear[(month+11)%12] %>
				</b></TD>
				<td ALIGN="RIGHT">
		<form method="post" name="jumptodate" action="providercontrol.jsp" style="display:inline;margin:0px;padding:0px;padding-right:10px;">
		<INPUT TYPE="text" NAME="year"
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
			SIZE="5">
		</form>

<% boolean isTeamOnly=false; %>

	<!--  multi-site , add site dropdown list -->
 <%if (bMultisites) { %>		
	   <script>
			function changeSite(sel) {
				sel.style.backgroundColor=sel.options[sel.selectedIndex].style.backgroundColor;
				var siteName = sel.options[sel.selectedIndex].value;
				var newGroupNo = "<%=(mygroupno == null ? "all" : mygroupno)%>";
				var providerview = "<%=providerview%>" ;
				if(providerview.indexOf("_grp_")!=-1 ) 
        		{				
        		
        			window.open("providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth" + "&site=" + siteName +"&mygroup_no="+newGroupNo,"_self");	
	            }
	            else
		        {
					window.open("providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth" + "&site=" + siteName + "&providerview=" + providerview,"_self");
		        }
			}
      </script>
      
    	<select id="site" name="site" onchange="changeSite(this)" style="background-color: <%=( selectedSite == null || siteBgColor.get(selectedSite) == null ? "#FFFFFF" : siteBgColor.get(selectedSite))%>">
    		<option value="none" style="background-color:white">---all clinic---</option>
    	<%
    	for (int i=0; i<curUserSites.size(); i++) {
    	%>
    		<option value="<%= curUserSites.get(i).getName() %>" style="background-color:<%= curUserSites.get(i).getBgColor() %>" 
    				<%=(curUserSites.get(i).getName().equals(selectedSite)) ? " selected " : "" %> >
    			<%= curUserSites.get(i).getName() %>
    		</option>
    	<% } %>
    	</select>
<%} %>    	
				<select name="provider_no" onChange="selectprovider(this)">
					<option value="all" <%=providerview.equals("all")?"selected":""%>><bean:message
						key="provider.appointmentprovideradminmonth.formAllProviders" /></option>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_team_schedule_only" rights="r" reverse="false">
<%
	isTeamOnly=true;
	String provider_no = curUser_no;
	for(Provider p : providerDao.getActiveProviders()) {
	
		providerNameBean.setDef(p.getProviderNo(), p.getLastName()+","+p.getFirstName());		
%>
					<option value="<%=p.getProviderNo()%>"
						<%=providerview.equals(p.getProviderNo())?"selected":""%>><%=providerNameBean.getShortDef(p.getProviderNo(), "", NameMaxLen)%></option>
<%
	}
%>

</security:oscarSec>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_team_schedule_only" rights="r" reverse="true">				
<%
	for(MyGroup g : myGroupDao.searchmygroupno()) {
	
		if (!bMultisites || siteGroups == null || siteGroups.size() == 0 || siteGroups.contains(g.getId().getMyGroupNo())) {  		
%>
					<option value="<%="_grp_"+g.getId().getMyGroupNo()%>"
						<%=(providerview.indexOf("_grp_") != -1 && mygroupno.equals(g.getId().getMyGroupNo()))?"selected":""%>><bean:message
						key="provider.appointmentprovideradminmonth.formGRP" />: <%=g.getId().getMyGroupNo()%></option>
<%
		}
	}

	for(Provider p : providerDao.getActiveProviders()) {
		if (!bMultisites || siteProviderNos  == null || siteProviderNos.size() == 0 || siteProviderNos.contains(p.getProviderNo())) { 
		providerNameBean.setDef(p.getProviderNo(), p.getLastName()+","+p.getFirstName());
%>
					<option value="<%=p.getProviderNo()%>"
						<%=providerview.equals(p.getProviderNo())?"selected":""%>><%=providerNameBean.getShortDef(p.getProviderNo(), "", NameMaxLen)%></option>
<%
		}
	}
%>
</security:oscarSec>
				</select>

				</td>
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
                                        <% String caisi = "";%>
                                        <caisi:isModuleLoad moduleName="caisi"> 
                                            <% caisi = "infirmaryView_isOscar=true&GoToCaisiViewFromOscarView=fals&viewall=1&";%>
                                        </caisi:isModuleLoad>
					<%
    String[] param = new String[2];
    boolean bFistEntry = true;
   	GregorianCalendar cal = new GregorianCalendar(year,(month-1),1);
   	cal.add(GregorianCalendar.MONTH,1);

   		

   	List<ScheduleDate> sds = null;
   	if (isTeamOnly && (providerview.equals("all") || providerview.startsWith("_grp_"))) {
   		// only display providers that has the same team field value.
   	   	
   	   	param[0] = year+"-"+month+"-"+"1";
        param[1] = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"1";
        
        List<String> ps = providerDao.getProvidersInTeam(providerDao.getProvider(curUser_no).getTeam());
        ps.add(curUser_no);
        sds = scheduleDateDao.search_scheduledate_teamp(ConversionUtils.fromDateString(year+"-"+month+"-"+"01"),ConversionUtils.fromDateString(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"01"),"A",ps);   
   		
   	} else
    if(providerview.equals("all") || providerview.startsWith("_grp_",0)) {
	      param[0] = year+"-"+month+"-"+"1";
	      param[1] = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"1";
		  if (selectedSite == null) {  
		      sds = scheduleDateDao.search_scheduledate_datep(ConversionUtils.fromDateString(year+"-"+month+"-"+"01"),ConversionUtils.fromDateString(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"01"),"A");
	    	}
	    	else	{
	    	  List<String> ps = providerSiteDao.findByProviderNoBySiteName(selectedSite);
	    	  sds = scheduleDateDao.search_scheduledate_teamp(ConversionUtils.fromDateString(year+"-"+month+"-"+"01"),ConversionUtils.fromDateString(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"01"),"A",ps);   
	     		
	      }
    } else {
      String[] param1 = new String[3];
      param1[0] = year+"-"+month+"-"+"1";
      param1[1] = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"1";
      param1[2] = providerview;
      sds = scheduleDateDao.search_scheduledate_teamp(ConversionUtils.fromDateString(year+"-"+month+"-"+"01"),ConversionUtils.fromDateString(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+"01"),"A",Arrays.asList(new String[]{providerview}));   
   	
    }

              Iterator<ScheduleDate> it = sds.iterator();
              ScheduleDate date = null;
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
						href='providercontrol.jsp?<%=caisi%>year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'>
					<span class='date'>&nbsp;<%=dateGrid[i][j] %> </span> <font
						size="-2" color="blue"><%=strHolidayName.toString()%></font> <%
  while (bFistEntry?it.hasNext():true) { 
    date = bFistEntry?it.next():date;
    String _scheduleDate = year+"-"+MyDateFormat.getDigitalXX(month)+"-"+MyDateFormat.getDigitalXX(dateGrid[i][j]);    
    if(!ConversionUtils.toDateString(date.getDate()).equals(_scheduleDate) ) {
      bFistEntry = false;
      break;
    } else {
      bFistEntry = true;
      if(String.valueOf(date.getAvailable()).equals("0")) continue;
    }
    if(isTeamOnly || !providerview.startsWith("_grp_",0) || myGrpBean.containsKey(date.getProviderNo()) ) {
    	%>
    <br><span class='datepname'>&nbsp;<%=providerNameBean.getShortDef(date.getProviderNo(),"",NameMaxLen )%></span><span
						class='datephour'><%=date.getHour() %></span>
	<%
    	if (bMultisites && CurrentSiteMap.get(date.getReason()) != null && ( selectedSite == null || "NONE".equals(date.getReason()) || selectedSite.equals(date.getReason()))) {
%> 
<% if (bMultisites) { out.print(getSiteHTML(date.getReason(), sites)); } %>
					
<% if (!bMultisites) { %>	
					
						<span class='datepreason'><%=date.getReason() %></span>
<% } %>
<%  } } } %>
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
								href='providercontrol.jsp?<%=caisi%>year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=dateGrid[i][j+1]==0?1:dateGrid[i][j+1]%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=week&dboperation=searchapptweek'>
							<%=(i+1)%></font></td>
							<%
                    continue;
                  }
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    if(dateGrid[i][j]==day) {
            %>
							<td align='center'><a
								href='providercontrol.jsp?<%=caisi%>year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(day)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday'>
							<font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">
							<div class='specialtxt'><%= dateGrid[i][j] %></div>
							</font></a></td>
							<%      } else {
            %>
							<td align='center'><font FACE='VERDANA,ARIAL,HELVETICA'
								SIZE='2' color='white'><a
								href='providercontrol.jsp?<%=caisi%>year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'>
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
								href='providercontrol.jsp?<%=caisi%>year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=dateGrid[i][j+1]==0?1:dateGrid[i][j+1]%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=week&dboperation=searchapptweek'>
							<%=(i+1)%></font></td>
							<%
                    continue;
                  }
                  if(dateGrid[i][j]==0) out.println("<td></td>");
                  else {
                    if(dateGrid[i][j]==day) {
            %>
							<td align='center'><a
								href='providercontrol.jsp?<%=caisi%>year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(day)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=day&dboperation=searchappointmentday'>
							<font FACE="VERDANA,ARIAL,HELVETICA" SIZE="2" color="red">
							<div class='specialtxt'><%= dateGrid[i][j] %></div>
							</font></a></td>
							<%      } else {
            %>
							<td align='center'><font FACE='VERDANA,ARIAL,HELVETICA'
								SIZE='2' color='white'><a
								href='providercontrol.jsp?<%=caisi%>year=<%=year%>&month=<%=MyDateFormat.getDigitalXX(month)%>&day=<%=MyDateFormat.getDigitalXX(dateGrid[i][j])%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName"))%>&displaymode=day&dboperation=searchappointmentday'>
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
				<td ALIGN="RIGHT" BGCOLOR="Ivory">| <a href="../logout.jsp"><bean:message
					key="provider.appointmentprovideradminmonth.btnlogOut" /> &nbsp;</a></td>
					
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
			case <bean:message key="global.billingShortcut"/> : popupOscarRx(600,1024,'../billing/CA/<%=prov%>/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;  //code for 'B'illing
			case <bean:message key="global.calendarShortcut"/> : popupOscarRx(425,430,'../share/CalendarPopup.jsp?urlfrom=../provider/providercontrol.jsp&year=<%=strYear%>&month=<%=strMonth%>&param=<%=URLEncoder.encode("&view=0&displaymode=day&dboperation=searchappointmentday","UTF-8")%>');  return false;  //run code for 'C'alendar
			case <bean:message key="global.edocShortcut"/> : popupOscarRx('700', '1024', '../dms/documentReport.jsp?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>', 'edocView');  return false;  //run code for e'D'oc
			case <bean:message key="global.resourcesShortcut"/> : popupOscarRx(550,687,'<%=resourcebaseurl%>'); return false; // code for R'e'sources
			case <bean:message key="global.helpShortcut"/> : popupOscarRx(600,750,'<%=resourcebaseurl%>');  return false;  //run code for 'H'elp
			case <bean:message key="global.ticklerShortcut"/> : {  
				<caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
					popupOscarRx(700,1024,'../tickler/ticklerMain.jsp','<bean:message key="global.tickler"/>') //run code for t'I'ckler
				</caisi:isModuleLoad>
				<caisi:isModuleLoad moduleName="ticklerplus">
					popupOscarRx(700,1024,'../Tickler.do','<bean:message key="global.tickler"/>'); //run code for t'I'ckler+
				</caisi:isModuleLoad>
				return false;
			}
			case <bean:message key="global.labShortcut"/> : popupOscarRx(600,1024,'../dms/inboxManage.do?method=prepareForIndexPage&providerNo=<%=curUser_no%>', '<bean:message key="global.lab"/>');  return false;  //run code for 'L'ab
			case <bean:message key="global.msgShortcut"/> : popupOscarRx(600,1024,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>'); return false;  //run code for 'M'essage
			case <bean:message key="global.monthShortcut"/> : window.open("providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+request.getParameter("curProviderName") )%>&displaymode=month&dboperation=searchappointmentmonth","_self"); return false ;  //run code for Mo'n'th
			case <bean:message key="global.conShortcut"/> : popupOscarRx(625,1024,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>');  return false;  //run code for c'O'nsultation
			case <bean:message key="global.reportShortcut"/> : popupOscarRx(650,1024,'../report/reportindex.jsp','reportPage');  return false;  //run code for 'R'eports
			case <bean:message key="global.prefShortcut"/> : {
				    <caisi:isModuleLoad moduleName="ticklerplus">
					popupOscarRx(715,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>'); //run code for tickler+ 'P'references
					return false;   
				    </caisi:isModuleLoad>	
			            <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
					popupOscarRx(715,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>'); //run code for 'P'references
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
			case <bean:message key="global.workflowShortcut"/> : popupOscarRx(700,1024,'../oscarWorkflow/WorkFlowList.jsp','<bean:message key="global.workflow"/>'); return false ; //code for 'W'orkflow
			case <bean:message key="global.phrShortcut"/> : popupOscarRx('600', '1024','../phr/PhrMessage.do?method=viewMessages','INDIVOMESSENGER2<%=curUser_no%>')
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

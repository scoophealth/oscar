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
<%@page import="org.oscarehr.managers.ProgramManager2"%>
<%@page import="org.oscarehr.managers.ProviderManager2"%>
<%@page import="org.oscarehr.managers.ScheduleManager"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@ page import="org.oscarehr.common.model.Appointment.BookingSource"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ page import="org.oscarehr.common.model.Provider,org.oscarehr.common.model.BillingONCHeader1"%>
<%@ page import="org.oscarehr.common.model.ProviderPreference"%>
<%@ page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%>
<%@ page import="org.oscarehr.common.dao.DemographicDao, org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicCustDao, org.oscarehr.common.model.DemographicCust" %>
<%@ page import="org.oscarehr.common.dao.MyGroupAccessRestrictionDao" %>
<%@ page import="org.oscarehr.common.model.MyGroupAccessRestriction" %>
<%@ page import="org.oscarehr.common.dao.DemographicStudyDao" %>
<%@ page import="org.oscarehr.common.model.DemographicStudy" %>
<%@ page import="org.oscarehr.common.dao.StudyDao" %>
<%@ page import="org.oscarehr.common.model.Study" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.dao.SiteDao" %>
<%@ page import="org.oscarehr.common.model.Site" %>
<%@ page import="org.oscarehr.common.dao.MyGroupDao" %>
<%@ page import="org.oscarehr.common.model.MyGroup" %>
<%@ page import="org.oscarehr.common.dao.ScheduleTemplateCodeDao" %>
<%@ page import="org.oscarehr.common.model.ScheduleTemplateCode" %>
<%@ page import="org.oscarehr.common.dao.ScheduleDateDao" %>
<%@ page import="org.oscarehr.common.model.ScheduleDate" %>
<%@ page import="org.oscarehr.common.dao.ProviderSiteDao" %>
<%@ page import="org.oscarehr.common.model.ScheduleTemplate" %>
<%@ page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@ page import="org.oscarehr.common.model.Appointment" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<%@ page import="org.oscarehr.common.model.Tickler" %>
<%@ page import="org.oscarehr.managers.TicklerManager" %>
<%@page import="org.oscarehr.managers.ProgramManager2"%>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider"%>
<%@page import="org.oscarehr.managers.LookupListManager" %>
<%@page import="org.oscarehr.common.model.LookupList" %>
<%@page import="org.oscarehr.common.model.LookupListItem" %>
<%@page import="org.oscarehr.managers.SecurityInfoManager" %>
<%@page import="org.oscarehr.managers.AppManager" %>
<%@page import="org.oscarehr.managers.DashboardManager" %>
<%@ page import="org.oscarehr.common.model.Dashboard" %>
<%@ page import="org.oscarehr.util.LoggedInInfo" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="org.oscarehr.util.SessionConstants" %>

<!-- add by caisi -->
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/indivo-tag.tld" prefix="myoscar" %>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr" %>

<%
	LoggedInInfo loggedInInfo1=LoggedInInfo.getLoggedInInfoFromSession(request);
	SecurityInfoManager securityInfoManager = SpringUtils.getBean(SecurityInfoManager.class);
	
	TicklerManager ticklerManager= SpringUtils.getBean(TicklerManager.class);
	DemographicStudyDao demographicStudyDao = SpringUtils.getBean(DemographicStudyDao.class);
	StudyDao studyDao = SpringUtils.getBean(StudyDao.class);
	UserPropertyDAO userPropertyDao = SpringUtils.getBean(UserPropertyDAO.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	SiteDao siteDao = SpringUtils.getBean(SiteDao.class);
	MyGroupDao myGroupDao = SpringUtils.getBean(MyGroupDao.class);
	DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
	ScheduleTemplateCodeDao scheduleTemplateCodeDao = SpringUtils.getBean(ScheduleTemplateCodeDao.class);
	ScheduleDateDao scheduleDateDao = SpringUtils.getBean(ScheduleDateDao.class);
	ProviderSiteDao providerSiteDao = SpringUtils.getBean(ProviderSiteDao.class);
	OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
	DemographicCustDao demographicCustDao = SpringUtils.getBean(DemographicCustDao.class);
	ScheduleManager scheduleManager = SpringUtils.getBean(ScheduleManager.class);
	ProviderManager2 providerManager = SpringUtils.getBean(ProviderManager2.class);
	ProgramManager2 programManager = SpringUtils.getBean(ProgramManager2.class);
	AppManager appManager = SpringUtils.getBean(AppManager.class);
	
	LookupListManager lookupListManager = SpringUtils.getBean(LookupListManager.class);
	LookupList reasonCodes = lookupListManager.findLookupListByName(loggedInInfo1, "reasonCode");
	Map<Integer,LookupListItem> reasonCodesMap = new  HashMap<Integer,LookupListItem>();
	for(LookupListItem lli:reasonCodes.getItems()) {
		reasonCodesMap.put(lli.getId(),lli);	
	}

	String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false;

    MyGroupAccessRestrictionDao myGroupAccessRestrictionDao = SpringUtils.getBean(MyGroupAccessRestrictionDao.class);
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment,_day" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_appointment");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%
		isSiteAccessPrivacy=true;
	%>
</security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%
		isTeamAccessPrivacy=true;
	%>
</security:oscarSec>

<%!//multisite starts =====================
private boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();
private JdbcApptImpl jdbc = new JdbcApptImpl();
private List<Site> sites = new ArrayList<Site>();
private List<Site> curUserSites = new ArrayList<Site>();
private List<String> siteProviderNos = new ArrayList<String>();
private List<String> siteGroups = new ArrayList<String>();
private String selectedSite = null;
private HashMap<String,String> siteBgColor = new HashMap<String,String>();
private HashMap<String,String> CurrentSiteMap = new HashMap<String,String>();%>

<%
	if (bMultisites) {
	sites = siteDao.getAllActiveSites();
	selectedSite = (String)session.getAttribute("site_selected");

	if (selectedSite != null) {
		//get site provider list
		siteProviderNos = siteDao.getProviderNoBySiteLocation(selectedSite);
		siteGroups = siteDao.getGroupBySiteLocation(selectedSite);
	}

	if (isSiteAccessPrivacy || isTeamAccessPrivacy) {
		String siteManagerProviderNo = (String) session.getAttribute("user");
		curUserSites = siteDao.getActiveSitesByProviderNo(siteManagerProviderNo);
		if (selectedSite==null) {
	siteProviderNos = siteDao.getProviderNoBySiteManagerProviderNo(siteManagerProviderNo);
	siteGroups = siteDao.getGroupBySiteManagerProviderNo(siteManagerProviderNo);
		}
	}
	else {
		curUserSites = sites;
	}

	for (Site s : curUserSites) {
		CurrentSiteMap.put(s.getName(),"Y");
	}

	//get all sites bgColors
	for (Site st : sites) {
		siteBgColor.put(st.getName(),st.getBgColor());
	}
}
//multisite ends =======================
%>



<!-- add by caisi end<style>* {border:1px solid black;}</style> -->

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<%
	long loadPage = System.currentTimeMillis();
%>


<!-- caisi infirmary view extension add -->
<caisi:isModuleLoad moduleName="caisi">
<%
	if (request.getParameter("year")!=null && request.getParameter("month")!=null && request.getParameter("day")!=null)
	{
		java.util.Date infirm_date=new java.util.GregorianCalendar(Integer.valueOf(request.getParameter("year")).intValue(), Integer.valueOf(request.getParameter("month")).intValue()-1, Integer.valueOf(request.getParameter("day")).intValue()).getTime();
		session.setAttribute("infirmaryView_date",infirm_date);

	}else
	{
		session.setAttribute("infirmaryView_date",null);
	}
	String reqstr =request.getQueryString();
	if (reqstr == null)
	{
		//Hack:: an unknown bug of struts or JSP causing the queryString to be null
		String year_q = request.getParameter("year");
	    String month_q =request.getParameter("month");
	    String day_q = request.getParameter("day");
	    String view_q = request.getParameter("view");
	    String displayMode_q = request.getParameter("displaymode");
	    reqstr = "year=" + year_q + "&month=" + month_q
           + "&day="+ day_q + "&view=" + view_q + "&displaymode=" + displayMode_q;
	}
	session.setAttribute("infirmaryView_OscarQue",reqstr);
%>
<c:import url="/infirm.do?action=showProgram" />
</caisi:isModuleLoad>
<!-- caisi infirmary view extension add end -->

<%@ page import="java.util.*,java.text.*,java.sql.*,java.net.*,oscar.*,oscar.util.*,org.oscarehr.provider.model.PreventionManager" %>

<%@ page import="org.apache.commons.lang.*" %>

<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<jsp:useBean id="as" class="oscar.appt.ApptStatusData" scope="page" />
<jsp:useBean id="dateTimeCodeBean" class="java.util.Hashtable" scope="page" />
<%
	Properties oscarVariables = OscarProperties.getInstance();
%>

<!-- Struts for i18n -->
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%
	PreventionManager prevMgr = (PreventionManager)SpringUtils.getBean("preventionMgr");
%>
<%!/**
Checks if the schedule day is patients birthday
**/
public boolean isBirthday(String schedDate,String demBday){
	return schedDate.equals(demBday);
}
public boolean patientHasOutstandingPrivateBills(String demographicNo){
	oscar.oscarBilling.ca.bc.MSP.MSPReconcile msp = new oscar.oscarBilling.ca.bc.MSP.MSPReconcile();
	return msp.patientHasOutstandingPrivateBill(demographicNo);
}%>
<%
	if(session.getAttribute("user") == null )
        response.sendRedirect("../logout.jsp");

	String curUser_no = (String) session.getAttribute("user");

    ProviderPreference providerPreference2=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);

    String mygroupno = providerPreference2.getMyGroupNo();
    if(mygroupno == null){
    	mygroupno = ".default";
    }
    String caisiView = null;
    caisiView = request.getParameter("GoToCaisiViewFromOscarView");
    boolean notOscarView = "false".equals(session.getAttribute("infirmaryView_isOscar"));
    if((caisiView!=null && "true".equals(caisiView)) || notOscarView) {
    	mygroupno = ".default";
    }
    String userfirstname = (String) session.getAttribute("userfirstname");
    String userlastname = (String) session.getAttribute("userlastname");
    String prov= (oscarVariables.getProperty("billregion","")).trim().toUpperCase();

    int startHour=providerPreference2.getStartHour();
    int endHour=providerPreference2.getEndHour();
    int everyMin=providerPreference2.getEveryMin();
    String defaultServiceType = (String) session.getAttribute("default_servicetype");
	ProviderPreference providerPreference=ProviderPreferencesUIBean.getProviderPreference(loggedInInfo1.getLoggedInProviderNo());
    if( defaultServiceType == null && providerPreference!=null) {
    	defaultServiceType = providerPreference.getDefaultServiceType();
    }

    if( defaultServiceType == null ) {
        defaultServiceType = "";
    }

    Collection<Integer> eforms = providerPreference2.getAppointmentScreenEForms();
    StringBuilder eformIds = new StringBuilder();
    for( Integer eform : eforms ) {
    	eformIds = eformIds.append("&eformId=" + eform);
    }

    Collection<String> forms = providerPreference2.getAppointmentScreenForms();
    StringBuilder ectFormNames = new StringBuilder();
    for( String formName : forms ) {
    	ectFormNames = ectFormNames.append("&encounterFormName=" + formName);
    }

	boolean prescriptionQrCodes = providerPreference2.isPrintQrCodeOnPrescriptions();
	boolean erx_enable = providerPreference2.isERxEnabled();
	boolean erx_training_mode = providerPreference2.isERxTrainingMode();
    
    boolean bShortcutIntakeForm = oscarVariables.getProperty("appt_intake_form", "").equalsIgnoreCase("on") ? true : false;

    String newticklerwarningwindow=null;
    String default_pmm=null;
    String programId_oscarView=null;
	String ocanWarningWindow=null;
	String cbiReminderWindow=null;
	String caisiBillingPreferenceNotDelete = null;
	String tklerProviderNo = null;
	
	UserPropertyDAO propDao =(UserPropertyDAO)SpringUtils.getBean("UserPropertyDAO");
	UserProperty userprop = propDao.getProp(curUser_no, UserProperty.PROVIDER_FOR_TICKLER_WARNING);
	if (userprop != null) {
		tklerProviderNo = userprop.getValue();
	} else {
		tklerProviderNo = curUser_no;
	}
	
	if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.propertiesOn("OCAN_warning_window") ) {
        ocanWarningWindow = (String)session.getAttribute("ocanWarningWindow");
	}
	
	if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.propertiesOn("CBI_REMINDER_WINDOW") ) {
        cbiReminderWindow = (String)session.getAttribute("cbiReminderWindow");
	}
	
	//Hide old echart link
	boolean showOldEchartLink = true;
	UserProperty oldEchartLink = propDao.getProp(curUser_no, UserProperty.HIDE_OLD_ECHART_LINK_IN_APPT);
	if (oldEchartLink!=null && "Y".equals(oldEchartLink.getValue())) showOldEchartLink = false;

if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){
	newticklerwarningwindow = (String) session.getAttribute("newticklerwarningwindow");
	default_pmm = (String)session.getAttribute("default_pmm");

	caisiBillingPreferenceNotDelete = String.valueOf(session.getAttribute("caisiBillingPreferenceNotDelete"));
    if(caisiBillingPreferenceNotDelete==null) {
    	ProviderPreference pp = ProviderPreferencesUIBean.getProviderPreferenceByProviderNo(curUser_no);
    	if(pp!=null) {
    		caisiBillingPreferenceNotDelete = String.valueOf(pp.getDefaultDoNotDeleteBilling());
    	}

    }

	//Disable schedule view associated with the program
	//Made the default program id "0";
	//programId_oscarView= (String)session.getAttribute("programId_oscarView");
	programId_oscarView = "0";
} else {
	programId_oscarView="0";
	session.setAttribute("programId_oscarView",programId_oscarView);
}
    int lenLimitedL=11; //L - long
    if(OscarProperties.getInstance().getProperty("APPT_SHOW_FULL_NAME","").equalsIgnoreCase("true")) {
    	lenLimitedL = 25;
    }
    int lenLimitedS=3; //S - short
    int len = lenLimitedL;
    int view = request.getParameter("view")!=null ? Integer.parseInt(request.getParameter("view")) : 0; //0-multiple views, 1-single view
    //// THIS IS THE VALUE I HAVE BEEN LOOKING FOR!!!!!
	boolean bDispTemplatePeriod = ( oscarVariables.getProperty("receptionist_alt_view") != null && oscarVariables.getProperty("receptionist_alt_view").equals("yes") ); // true - display as schedule template period, false - display as preference
%>
<%
	String tickler_no="", textColor="", tickler_note="";
    String ver = "", roster="";
    String yob = "";
    String mob = "";
    String dob = "";
    String demBday = "";
    StringBuffer study_no=null, study_link=null,studyDescription=null;
	String studySymbol = "\u03A3", studyColor = "red";

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
    

    boolean isWeekView = false;
    String provNum = request.getParameter("provider_no");
    if (provNum != null) {
        isWeekView = true;
    }
    if(caisiView!=null && "true".equals(caisiView)) {
    	isWeekView = false;
    }
int nProvider;

boolean caseload = "1".equals(request.getParameter("caseload"));

GregorianCalendar cal = new GregorianCalendar();
int curYear = cal.get(Calendar.YEAR);
int curMonth = (cal.get(Calendar.MONTH)+1);
int curDay = cal.get(Calendar.DAY_OF_MONTH);

int year = Integer.parseInt(request.getParameter("year"));
int month = Integer.parseInt(request.getParameter("month"));
int day = Integer.parseInt(request.getParameter("day"));

//verify the input date is really existed
cal = new GregorianCalendar(year,(month-1),day);

if (isWeekView) {
cal.add(Calendar.DATE, -(cal.get(Calendar.DAY_OF_WEEK)-1)); // change the day to the current weeks initial sunday
}

int week = cal.get(Calendar.WEEK_OF_YEAR);
year = cal.get(Calendar.YEAR);
month = (cal.get(Calendar.MONTH)+1);
day = cal.get(Calendar.DAY_OF_MONTH);

String strDate = year + "-" + month + "-" + day;
String monthDay = String.format("%02d", month) + "-" + String.format("%02d", day);
SimpleDateFormat inform = new SimpleDateFormat ("yyyy-MM-dd", request.getLocale());
String formatDate;
try {
java.util.ResourceBundle prop = ResourceBundle.getBundle("oscarResources", request.getLocale());
formatDate = UtilDateUtilities.DateToString(inform.parse(strDate), prop.getString("date.EEEyyyyMMdd"),request.getLocale());
} catch (Exception e) {
	MiscUtils.getLogger().error("Error", e);
formatDate = UtilDateUtilities.DateToString(inform.parse(strDate), "EEE, yyyy-MM-dd");
}
String strYear=""+year;
String strMonth=month>9?(""+month):("0"+month);
String strDay=day>9?(""+day):("0"+day);
   

Calendar apptDate = Calendar.getInstance();
apptDate.set(year, month-1 , day);
Calendar minDate = Calendar.getInstance();
minDate.set( minDate.get(Calendar.YEAR), minDate.get(Calendar.MONTH), minDate.get(Calendar.DATE) );
String allowDay = "";
if (apptDate.equals(minDate)) {
    allowDay = "Yes";
    } else {
    allowDay = "No";
}
minDate.add(Calendar.DATE, 7);
String allowWeek = "";
if (apptDate.before(minDate)) {
    allowWeek = "Yes";
    } else {
    allowWeek = "No";
}
%>
<%@page import="oscar.util.*"%>
<%@page import="oscar.oscarDB.*"%>

<%@page import="oscar.appt.JdbcApptImpl"%>
<%@page import="oscar.appt.ApptUtil"%>
<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.web.admin.ProviderPreferencesUIBean"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="org.oscarehr.web.AppointmentProviderAdminDayUIBean"%>
<%@page import="org.oscarehr.common.model.EForm"%><html:html locale="true">
<head>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<title><%=WordUtils.capitalize(userlastname + ", " +  org.apache.commons.lang.StringUtils.substring(userfirstname, 0, 1)) + "-"%><bean:message key="provider.appointmentProviderAdminDay.title"/></title>

<!-- Determine which stylesheet to use: mobile-optimized or regular -->
<%
	boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;
if (isMobileOptimized) {
%>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width"/>
    <link rel="stylesheet" href="../mobile/receptionistapptstyle.css" type="text/css">
<%
	} else {
%>
<link rel="stylesheet" href="../css/receptionistapptstyle.css" type="text/css">
<link rel="stylesheet" href="../css/helpdetails.css" type="text/css">
<%
	}
%>

<%
	if (!caseload) {
        boolean caisiEnabled = (OscarProperties.getInstance().getProperty("ModuleNames", "").indexOf("Caisi") != -1);

        if(caisiEnabled && notOscarView) {
                //don't refresh for CM view when CAISI enabled
        } else {

%>
<c:if test="${empty sessionScope.archiveView or sessionScope.archiveView != true}">
<%!String refresh = oscar.OscarProperties.getInstance().getProperty("refresh.appointmentprovideradminday.jsp", "-1");%>
<%="-1".equals(refresh)?"":"<meta http-equiv=\"refresh\" content=\""+refresh+";\">"%>
</c:if>
<%
	} }
%>

<script type="text/javascript" src="../share/javascript/Oscar.js" ></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
<script type="text/javascript" src="../phr/phr.js"></script>

<script src="<c:out value="../js/jquery.js"/>"></script>
<script>
	jQuery.noConflict();
</script>

<oscar:customInterface section="main"/>

<script type="text/javascript" src="schedulePage.js.jsp"></script>
<script>
function changeGroup(s) {
	var newGroupNo = s.options[s.selectedIndex].value;
	if(newGroupNo.indexOf("_grp_") != -1) {
	  newGroupNo = s.options[s.selectedIndex].value.substring(5);
	}else{
	  newGroupNo = s.options[s.selectedIndex].value;
	}
	<%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){%>
		//Disable schedule view associated with the program
		//Made the default program id "0";
		//var programId = document.getElementById("bedprogram_no").value;
		var programId = 0;
		var programId_forCME = document.getElementById("bedprogram_no").value;

		popupPage(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&caisiBillingPreferenceNotDelete=<%=caisiBillingPreferenceNotDelete%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&default_servicetype=<%=defaultServiceType%>&prescriptionQrCodes=<%=prescriptionQrCodes%>&erx_enable=<%=erx_enable%>&erx_training_mode=<%=erx_training_mode%>&mygroup_no="+newGroupNo+"&programId_oscarView="+programId+"&case_program_id="+programId_forCME + "<%=eformIds.toString()%><%=ectFormNames.toString()%>");
	<%}else {%>
	  var programId=0;
	  popupPage(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&default_servicetype=<%=defaultServiceType%>&prescriptionQrCodes=<%=prescriptionQrCodes%>&erx_enable=<%=erx_enable%>&erx_training_mode=<%=erx_training_mode%>&mygroup_no="+newGroupNo+"&programId_oscarView="+programId + "<%=eformIds.toString()%><%=ectFormNames.toString()%>");
	<%}%>
	}

	function ts1(s) {
	popupPage(360,780,('../appointment/addappointment.jsp?'+s));
	}
	function tsr(s) {
	popupPage(360,780,('../appointment/appointmentcontrol.jsp?displaymode=edit&dboperation=search&'+s));
	}
	function goFilpView(s) {
	self.location.href = "../schedule/scheduleflipview.jsp?originalpage=../provider/providercontrol.jsp&startDate=<%=year+"-"+month+"-"+day%>" + "&provider_no="+s ;
	}
	function goWeekView(s) {
	self.location.href = "providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&view=0&displaymode=day&dboperation=searchappointmentday&viewall=1&provider_no="+s;
	}
	function goZoomView(s, n) {
	self.location.href = "providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider="+s+"&curProviderName="+encodeURIComponent(n)+"&displaymode=day&dboperation=searchappointmentday" ;
	}
	function findProvider(p,m,d) {
	popupPage(300,400, "receptionistfindprovider.jsp?pyear=" +p+ "&pmonth=" +m+ "&pday=" +d+ "&providername="+ encodeURIComponent(document.findprovider.providername.value));
	}
	function goSearchView(s) {
		popupPage(600,650,"../appointment/appointmentsearch.jsp?provider_no="+s);
	}
	function launchGroupProperties(s) {
		popupPage(800,1000,"../appointment/groupProperties.jsp?provider_no="+s + "&date=<%=inform.format(cal.getTime())%>");
	}
	
	function review(key) {
		var searchKey = null; 
	
		if(key == '0') {
			searchKey = '1';
		} else {
			searchKey = '0';
		}
		
		var newUrl = null;
		
		var searchStr = 'viewall=' + searchKey;
		if(self.location.href.search(searchStr) > -1) {
			//first case, viewall=searchKey -> switch to key
			newUrl = self.location.href;
			newUrl = newUrl.replace(searchStr,'viewall=' + key);
		} else {
			//do we already have it
			searchStr = 'viewall=';
			if(self.location.href.search(searchStr) == -1) {
				newUrl = self.location.href + "&viewall=" + key;
			}
		}
		
		  self.location.href = newUrl;
		  
		}

</script>


<%
	if (OscarProperties.getInstance().getBooleanProperty("indivica_hc_read_enabled", "true")) {
%>
<script src="<%=request.getContextPath()%>/hcHandler/hcHandler.js"></script>
<script src="<%=request.getContextPath()%>/hcHandler/hcHandlerAppointment.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath()%>/hcHandler/hcHandler.css" type="text/css" />
<%
	}
%>

</head>
<%
	if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable()){
%>
<body bgcolor="#EEEEFF" onload="load();" topmargin="0" leftmargin="0" rightmargin="0">
<%
	}else{
%>
<body bgcolor="#EEEEFF" onLoad="refreshAllTabAlerts();scrollOnLoad();" topmargin="0" leftmargin="0" rightmargin="0">
<%
	}
%>

<%
	boolean isTeamScheduleOnly = false;
%>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_team_schedule_only" rights="r" reverse="false">
<%
	isTeamScheduleOnly = true;
%>
</security:oscarSec>
<%
	int numProvider=0, numAvailProvider=0;
String [] curProvider_no;
String [] curProviderName;
//initial provider bean for all the application
if(providerBean.isEmpty()) {
	for(Provider p : providerDao.getActiveProviders()) {
		 providerBean.setProperty(p.getProviderNo(),p.getFormattedName());
	}
 }

String viewall = request.getParameter("viewall");
if( viewall == null ) {
    viewall = "0";
}
String _scheduleDate = strYear+"-"+strMonth+"-"+strDay;

List<Map<String,Object>> resultList = null;

if(mygroupno != null && providerBean.get(mygroupno) != null) { //single appointed provider view
     numProvider=1;
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];
     curProvider_no[0]=mygroupno;
     
     curProviderName[0]=providerDao.getProvider(mygroupno).getFullName();
     
} else {
	if(view==0) { //multiple views
	   if (selectedSite!=null) {
		   numProvider = siteDao.site_searchmygroupcount(mygroupno, selectedSite).intValue();
	   }
	   else {
		   numProvider = myGroupDao.getGroupByGroupNo(mygroupno).size();
	   }
	   
	  
       String [] param3 = new String [2];
       param3[0] = mygroupno;
       param3[1] = strDate; //strYear +"-"+ strMonth +"-"+ strDay ;
       numAvailProvider = 0;
       if (selectedSite!=null) {
    	    List<String> siteProviders = providerSiteDao.findByProviderNoBySiteName(selectedSite);
    	  	List<ScheduleDate> results = scheduleDateDao.search_numgrpscheduledate(mygroupno, ConversionUtils.fromDateString(strDate));
    	  	
    	  	for(ScheduleDate result:results) {
    	  		if(siteProviders.contains(result.getProviderNo())) {
    	  			numAvailProvider++;
    	  		}
    	  	}
       }
       else {
    	   	numAvailProvider = scheduleDateDao.search_numgrpscheduledate(mygroupno, ConversionUtils.fromDateString(strDate)).size();
       		
       }
      
     // _team_schedule_only does not support groups
     // As well, the mobile version only shows the schedule of the login provider.
     if(numProvider==0 || isTeamScheduleOnly || isMobileOptimized) {
       numProvider=1; //the login user
       curProvider_no = new String []{curUser_no};  //[numProvider];
       curProviderName = new String []{(userlastname+", "+userfirstname)}; //[numProvider];
     } else {
       if(request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1") ) {
         if(numProvider >= 5) {lenLimitedL = 2; lenLimitedS = 3; }
       } else {
         if(numAvailProvider >= 5) {lenLimitedL = 2; lenLimitedS = 3; }
         if(numAvailProvider == 2) {lenLimitedL = 20; lenLimitedS = 10; len = 20;}
         if(numAvailProvider == 1) {lenLimitedL = 30; lenLimitedS = 30; len = 30; }
       }
      UserProperty uppatientNameLength = userPropertyDao.getProp(curUser_no, UserProperty.PATIENT_NAME_LENGTH);
      int NameLength=0;
      
      if ( uppatientNameLength != null && uppatientNameLength.getValue() != null) {
          try {
             NameLength=Integer.parseInt(uppatientNameLength.getValue());
          } catch (NumberFormatException e) {
             NameLength=0;
          }
      
          if(NameLength>0) {
             len=lenLimitedS= lenLimitedL = NameLength;
          }
                   }
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];

     int iTemp = 0;
     if (selectedSite!=null) {
    	 List<String> siteProviders = providerSiteDao.findByProviderNoBySiteName(selectedSite);
    	 List<MyGroup> results = myGroupDao.getGroupByGroupNo(mygroupno);
    	 for(MyGroup result:results) {
    		 if(siteProviders.contains(result.getId().getProviderNo())) {
    			 curProvider_no[iTemp] = String.valueOf(result.getId().getProviderNo());
    			 
    			 Provider p = providerDao.getProvider(curProvider_no[iTemp]);
    			 if (p!=null) {
    				 curProviderName[iTemp] = p.getFullName();
    			 }
        	     iTemp++;
    		 }
    	 }
     }
     else {
    	 List<MyGroup> results = myGroupDao.getGroupByGroupNo(mygroupno);
    	 Collections.sort(results,MyGroup.MyGroupNoViewOrderComparator);
  	   
    	 for(MyGroup result:results) {
    		 curProvider_no[iTemp] = String.valueOf(result.getId().getProviderNo());
    		 
    		 Provider p = providerDao.getProvider(curProvider_no[iTemp]);
    		 if (p!=null) {
        		 curProviderName[iTemp] = p.getFullName();
    		 }
    	     iTemp++;
    	 }
     }
     
    
    }
   } else { //single view
     numProvider=1;
     curProvider_no = new String [numProvider];
     curProviderName = new String [numProvider];
     curProvider_no[0]=request.getParameter("curProvider");
     curProviderName[0]=request.getParameter("curProviderName");
   }
}
//set timecode bean
String bgcolordef = "#486ebd" ;
String [] param3 = new String[2];
param3[0] = strDate;
for(nProvider=0;nProvider<numProvider;nProvider++) {
     param3[1] = curProvider_no[nProvider];
     List<Object[]> results = scheduleDateDao.search_appttimecode(ConversionUtils.fromDateString(strDate), curProvider_no[nProvider]);
     for(Object[] result:results) {
    	 ScheduleTemplate st = (ScheduleTemplate)result[0];
    	 ScheduleDate sd = (ScheduleDate)result[1];
    	 dateTimeCodeBean.put(sd.getProviderNo(), st.getTimecode());
     }
    
}

	for(ScheduleTemplateCode stc : scheduleTemplateCodeDao.findAll()) {
   
     dateTimeCodeBean.put("description"+stc.getCode(), stc.getDescription());
     dateTimeCodeBean.put("duration"+stc.getCode(), stc.getDuration());
     dateTimeCodeBean.put("color"+stc.getCode(), (stc.getColor()==null || "".equals(stc.getColor()))?bgcolordef:stc.getColor());
     dateTimeCodeBean.put("confirm" + stc.getCode(), stc.getConfirm());
   }

java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
%>


<table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%" id="firstTable" class="noprint">
<tr>
<td align="center" >
<a href="../web/" title="OSCAR EMR"><img src="<%=request.getContextPath()%>/images/oscar_small.png" border="0"></a>
</td>
<td id="firstMenu">
<ul id="navlist">
<logic:notEqual name="infirmaryView_isOscar" value="false">
<% if(request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1") ) { %>
         <li>
         <a href=# onClick = "review('0')" title="<bean:message key="provider.appointmentProviderAdminDay.viewProvAval"/>"><bean:message key="provider.appointmentProviderAdminDay.schedView"/></a>
         </li>
 <% } else {  %>
 <li>
 <a href='providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=0&displaymode=day&dboperation=searchappointmentday&viewall=1'><bean:message key="provider.appointmentProviderAdminDay.schedView"/></a>
 </li>
         
<% } %>
</logic:notEqual>

 <li>
 <a href='providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=0&displaymode=day&dboperation=searchappointmentday&caseload=1&clProv=<%=curUser_no%>'><bean:message key="global.caseload"/></a>
 </li>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
 <security:oscarSec roleName="<%=roleName$%>" objectName="_resource" rights="r">
 <li>
    <a href="#" ONCLICK ="popupPage2('<%=resourcebaseurl%>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.viewResources"/>" onmouseover="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewResources"/>';return true"><bean:message key="oscarEncounter.Index.clinicalResources"/></a>
 </li>
 </security:oscarSec>
</caisi:isModuleLoad>

 <%
 	if (isMobileOptimized) {
 %>
        <!-- Add a menu button for mobile version, which opens menu contents when clicked on -->
        <li id="menu"><a class="leftButton top" onClick="showHideItem('navlistcontents');">
                <bean:message key="global.menu" /></a>
            <ul id="navlistcontents" style="display:none;">
<% } %>


<security:oscarSec roleName="<%=roleName$%>" objectName="_search" rights="r">
 <li id="search">
    <caisi:isModuleLoad moduleName="caisi">
    	<%
    		String caisiSearch = oscarVariables.getProperty("caisi.search.workflow", "true");
    		if("true".equalsIgnoreCase(caisiSearch)) {
    	%>
    	<a HREF="../PMmodule/ClientSearch2.do" TITLE='<bean:message key="global.searchPatientRecords"/>' OnMouseOver="window.status='<bean:message key="global.searchPatientRecords"/>' ; return true"><bean:message key="provider.appointmentProviderAdminDay.search"/></a>
       
    	<%	
    		} else {
    	%>
       	 <a HREF="#" ONCLICK ="popupPage2('../demographic/search.jsp');return false;"  TITLE='<bean:message key="global.searchPatientRecords"/>' OnMouseOver="window.status='<bean:message key="global.searchPatientRecords"/>' ; return true"><bean:message key="provider.appointmentProviderAdminDay.search"/></a>
   	<% } %>
    </caisi:isModuleLoad>
    <caisi:isModuleLoad moduleName="caisi" reverse="true">
       <a HREF="#" ONCLICK ="popupPage2('../demographic/search.jsp');return false;"  TITLE='<bean:message key="global.searchPatientRecords"/>' OnMouseOver="window.status='<bean:message key="global.searchPatientRecords"/>' ; return true"><bean:message key="provider.appointmentProviderAdminDay.search"/></a>
    </caisi:isModuleLoad>
</li>
</security:oscarSec>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="r">
<li>
    <a HREF="#" ONCLICK ="popupPage2('../report/reportindex.jsp','reportPage');return false;"   TITLE='<bean:message key="global.genReport"/>' OnMouseOver="window.status='<bean:message key="global.genReport"/>' ; return true"><bean:message key="global.report"/></a>
</li>
</security:oscarSec>
<oscar:oscarPropertiesCheck property="NOT_FOR_CAISI" value="no" defaultVal="true">

<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r">
<li>
	<a HREF="#" ONCLICK ="popupPage2('../billing/CA/<%=prov%>/billingReportCenter.jsp?displaymode=billreport&providerview=<%=curUser_no%>');return false;" TITLE='<bean:message key="global.genBillReport"/>' onMouseOver="window.status='<bean:message key="global.genBillReport"/>';return true"><bean:message key="global.billing"/></a>
</li>
</security:oscarSec>

<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment.doctorLink" rights="r">
   <li>
       <a HREF="#" ONCLICK ="popupInboxManager('../dms/inboxManage.do?method=prepareForIndexPage&providerNo=<%=curUser_no%>', 'Lab');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'>
	   <span id="oscar_new_lab"><bean:message key="global.lab"/></span>
       </a>
       <oscar:newUnclaimedLab>
       <a class="tabalert" HREF="#" ONCLICK ="popupInboxManager('../dms/inboxManage.do?method=prepareForIndexPage&providerNo=0&searchProviderNo=0&status=N&lname=&fname=&hnum=&pageNum=1&startIndex=0', 'Lab');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewLabReports"/>'>*</a>
       </oscar:newUnclaimedLab>
   </li>
  </security:oscarSec>
</oscar:oscarPropertiesCheck>

 </caisi:isModuleLoad>
 
 <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
 	<security:oscarSec roleName="<%=roleName$%>" objectName="_msg" rights="r">
     <li>
	 <a HREF="#" ONCLICK ="popupOscarRx(600,1024,'../oscarMessenger/DisplayMessages.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')" title="<bean:message key="global.messenger"/>">
	 <span id="oscar_new_msg"><bean:message key="global.msg"/></span></a>
     </li>
   	</security:oscarSec>
 </caisi:isModuleLoad>
<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="r">
<li id="con">
 <a HREF="#" ONCLICK ="popupOscarRx(625,1024,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>')" title="<bean:message key="provider.appointmentProviderAdminDay.viewConReq"/>">
 <span id="oscar_aged_consults"><bean:message key="global.con"/></span></a>
</li>
</security:oscarSec>
</caisi:isModuleLoad>
<security:oscarSec roleName="<%=roleName$%>" objectName="_pref" rights="r">
<li>    <!-- remove this and let providerpreference check -->
    <caisi:isModuleLoad moduleName="ticklerplus">
	<a href=# onClick ="popupPage(715,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>&caisiBillingPreferenceNotDelete=<%=caisiBillingPreferenceNotDelete%>&tklerproviderno=<%=tklerProviderNo%>');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' ; return true"><bean:message key="global.pref"/></a>
    </caisi:isModuleLoad>
    <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
	<a href=# onClick ="popupPage(715,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>');return false;" TITLE='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.msgSettings"/>' ; return true"><bean:message key="global.pref"/></a>
    </caisi:isModuleLoad>
</li>
</security:oscarSec>
 <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<security:oscarSec roleName="<%=roleName$%>" objectName="_edoc" rights="r">
<li>
   <a HREF="#" onclick="popup('700', '1024', '../dms/documentReport.jsp?function=provider&functionid=<%=curUser_no%>&curUser=<%=curUser_no%>', 'edocView');" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewEdoc"/>'><bean:message key="global.edoc"/></a>
</li>
</security:oscarSec>
 </caisi:isModuleLoad>
 <security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="r">
<li>
   <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
    <a HREF="#" ONCLICK ="popupPage2('../tickler/ticklerMain.jsp','<bean:message key="global.tickler"/>');return false;" TITLE='<bean:message key="global.tickler"/>'>
	<span id="oscar_new_tickler"><bean:message key="global.btntickler"/></span></a>
   </caisi:isModuleLoad>
   <caisi:isModuleLoad moduleName="ticklerplus">
    <a HREF="#" ONCLICK ="popupPage2('../Tickler.do?filter.assignee=<%=curUser_no%>&filter.demographic_no=&filter.demographic_webName=','<bean:message key="global.tickler"/>');return false;" TITLE='<bean:message key="global.tickler"/>'+'+'>
	<span id="oscar_new_tickler"><bean:message key="global.btntickler"/></span></a>
   </caisi:isModuleLoad>
</li>
</security:oscarSec>
<oscar:oscarPropertiesCheck property="OSCAR_LEARNING" value="yes">
<li>
    <a HREF="#" ONCLICK ="popupPage2('../oscarLearning/CourseView.jsp','<bean:message key="global.courseview"/>');return false;" TITLE='<bean:message key="global.courseview"/>'>
	<span id="oscar_courseview"><bean:message key="global.btncourseview"/></span></a>
</li>
</oscar:oscarPropertiesCheck>

<oscar:oscarPropertiesCheck property="referral_menu" value="yes">
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r">
<li id="ref">
 <a href="#" onclick="popupPage(550,800,'../admin/ManageBillingReferral.do');return false;"><bean:message key="global.manageReferrals"/></a>
</li>
</security:oscarSec>
</oscar:oscarPropertiesCheck>

<oscar:oscarPropertiesCheck property="WORKFLOW" value="yes">
   <li><a href="javascript: function myFunction() {return false; }" onClick="popup(700,1024,'../oscarWorkflow/WorkFlowList.jsp','<bean:message key="global.workflow"/>')"><bean:message key="global.btnworkflow"/></a></li>
</oscar:oscarPropertiesCheck>

    <myoscar:indivoRegistered provider="<%=curUser_no%>">
		<%
			MyOscarUtils.attemptMyOscarAutoLoginIfNotAlreadyLoggedInAsynchronously(loggedInInfo1, false);
		%>
	    <li>
			<a HREF="#" ONCLICK ="popup('600', '1024','../phr/PhrMessage.do?method=viewMessages','INDIVOMESSENGER2<%=curUser_no%>')" title='<bean:message key="global.phr"/>'>
				<bean:message key="global.btnphr"/>
				<div id="unreadMessagesMenuMarker" style="display:inline-block;vertical-align:top"><!-- place holder for unread message count --></div>
			</a>
			<script type="text/javascript">
				function pollMessageCount()
				{
					jQuery('#unreadMessagesMenuMarker').load('<%=request.getContextPath()%>/phr/msg/unread_message_count.jsp')
				}
				
				window.setInterval(pollMessageCount, 60000);
				window.setTimeout(pollMessageCount, 2000);
			</script>
	    </li>
    </myoscar:indivoRegistered>
<%if(appManager.isK2AEnabled()){ %>
<li>
	<a href="javascript:void(0);" id="K2ALink">K2A<span><sup id="k2a_new_notifications"></sup></span></a>
	<script type="text/javascript">
		function getK2AStatus(){
			jQuery.get( "../ws/rs/resources/notifications/number", function( data ) {
				  if(data === "-"){ //If user is not logged in
					  jQuery("#K2ALink").click(function() {
						var win = window.open('../apps/oauth1.jsp?id=K2A','appAuth','width=700,height=450,scrollbars=1');
						win.focus();
					  });
				   }else{
					  jQuery("#k2a_new_notifications").text(data); 
					  jQuery("#K2ALink").click(function() {
						var win = window.open('../apps/notifications.jsp','appAuth','width=450,height=700,scrollbars=1');
						win.focus();
					  });
				   }
			});
		}
		getK2AStatus();
	</script>
</li>
<%}%>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
	<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc,_admin.fax" rights="r">

<li id="admin2">
 <a href="javascript:void(0)" id="admin-panel" TITLE='Administration Panel' onclick="newWindow('<%=request.getContextPath()%>/administration/','admin')">Administration</a>
</li>

</security:oscarSec>
	</caisi:isModuleLoad>

<security:oscarSec roleName="<%=roleName$%>" objectName="_dashboardDisplay" rights="r">
	<% 
		DashboardManager dashboardManager = SpringUtils.getBean(DashboardManager.class);
		List<Dashboard> dashboards = dashboardManager.getActiveDashboards(loggedInInfo1);
		pageContext.setAttribute("dashboards", dashboards);
	%>

	<li id="dashboardList">
		 <div class="dropdown">
			<a href="#" class="dashboardBtn">Dashboard</a>
			<div class="dashboardDropdown">
				<c:forEach items="${ dashboards }" var="dashboard" >			
					<a href="javascript:void(0)" onclick="newWindow('<%=request.getContextPath()%>/web/dashboard/display/DashboardDisplay.do?method=getDashboard&dashboardId=${ dashboard.id }','dashboard')"> 
						<c:out value="${ dashboard.name }" />
					</a>
				</c:forEach>
				<a href="javascript:void(0)" onclick="newWindow('<%=request.getContextPath()%>/web/dashboard/display/sharedOutcomesDashboard.jsp','shared_dashboard')"> 
						Common Provider Dashboard
					</a>
			</div>
			
		</div>
	</li>		

</security:oscarSec> 
 
  <!-- Added logout link for mobile version -->
  <li id="logoutMobile">
      <a href="../logout.jsp"><bean:message key="global.btnLogout"/></a>
  </li>

	
<!-- plugins menu extension point add -->
<%
	int pluginMenuTagNumber=0;
%>
<plugin:pageContextExtension serviceName="oscarMenuExtension" stemFromPrefix="Oscar"/>
<logic:iterate name="oscarMenuExtension.points" id="pt" scope="page" type="oscar.caisi.OscarMenuExtension">
<%
	if (oscar.util.plugin.IsPropertiesOn.propertiesOn(pt.getName().toLowerCase())) {
	pluginMenuTagNumber++;
%>

       <li><a href='<html:rewrite page="<%=pt.getLink()%>"/>'>
       <%=pt.getName()%></a></li>
<%
	}
%>
</logic:iterate>

<!-- plugin menu extension point add end-->

<%
	int menuTagNumber=0;
%>
<caisi:isModuleLoad moduleName="caisi">
   <li>
     <a href='<html:rewrite page="/PMmodule/ProviderInfo.do"/>'>Program</a>
     <%
     	menuTagNumber++ ;
     %>
   </li>
</caisi:isModuleLoad>

<% if (isMobileOptimized) { %>
    </ul></li> <!-- end menu list for mobile-->
<% } %>

</ul>  <!--- old TABLE -->

</td>


<td align="right" valign="bottom" >
	<a href="javascript: function myFunction() {return false; }" onClick="popup(700,1024,'../scratch/index.jsp','scratch')"><span id="oscar_scratch"></span></a>&nbsp;

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

	| <a href="../logout.jsp"><bean:message key="global.btnLogout"/>&nbsp;</a>

</td>


</tr>
</table>


<script>
	jQuery(document).ready(function(){
		jQuery.get("<%=request.getContextPath()%>/SystemMessage.do?method=view","html",function(data,textStatus){
			jQuery("#system_message").html(data);
		});
		jQuery.get("<%=request.getContextPath()%>/FacilityMessage.do?method=view","html",function(data,textStatus){
			jQuery("#facility_message").html(data);
		});
	});
</script>

<div id="system_message"></div>
<div id="facility_message"></div>
<%
	if (caseload) {
%>
<jsp:include page="caseload.jspf"/>
<%
	} else {
%>

<table BORDER="0" CELLPADDING="1" CELLSPACING="0" WIDTH="100%" BGCOLOR="#C0C0C0">
<tr id="ivoryBar">
<td id="dateAndCalendar" BGCOLOR="ivory" width="33%">
 <a class="redArrow" href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=isWeekView?(day-7):(day-1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+URLEncoder.encode(request.getParameter("curProviderName"),"UTF-8") )%>&displaymode=day&dboperation=searchappointmentday<%=isWeekView?"&provider_no="+provNum:""%>&viewall=<%=viewall%>">
 &nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" class="noprint" ALT="<bean:message key="provider.appointmentProviderAdminDay.viewPrevDay"/>" vspace="2"></a>
 <b><span class="dateAppointment"><%
 	if (isWeekView) {
 %><bean:message key="provider.appointmentProviderAdminDay.week"/> <%=week%><%
 	} else {
 %><%=formatDate%><%
 	}
 %></span></b>
 <a class="redArrow" href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=isWeekView?(day+7):(day+1)%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+URLEncoder.encode(request.getParameter("curProviderName"),"UTF-8") )%>&displaymode=day&dboperation=searchappointmentday<%=isWeekView?"&provider_no="+provNum:""%>&viewall=<%=viewall%>">
 <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" class="noprint" ALT="<bean:message key="provider.appointmentProviderAdminDay.viewNextDay"/>" vspace="2">&nbsp;&nbsp;</a>
<a id="calendarLink" href=# onClick ="popupPage(425,430,'../share/CalendarPopup.jsp?urlfrom=../provider/providercontrol.jsp&year=<%=strYear%>&month=<%=strMonth%>&param=<%=URLEncoder.encode("&view=0&displaymode=day&dboperation=searchappointmentday&viewall="+viewall,"UTF-8")%><%=isWeekView?URLEncoder.encode("&provider_no="+provNum, "UTF-8"):""%>')"><bean:message key="global.calendar"/></a>

<logic:notEqual name="infirmaryView_isOscar" value="false">
| <% if(request.getParameter("viewall")!=null && request.getParameter("viewall").equals("1") ) { %>
 <!-- <span style="color:#333"><bean:message key="provider.appointmentProviderAdminDay.viewAll"/></span> -->
 <u><a href=# onClick = "review('0')" title="<bean:message key="provider.appointmentProviderAdminDay.viewAllProv"/>"><bean:message key="provider.appointmentProviderAdminDay.schedView"/></a></u>
 
<%}else{%>
	<u><a href=# onClick = "review('1')" title="<bean:message key="provider.appointmentProviderAdminDay.viewAllProv"/>"><bean:message key="provider.appointmentProviderAdminDay.viewAll"/></a></u>
<%}%>
</logic:notEqual>

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<security:oscarSec roleName="<%=roleName$%>" objectName="_day" rights="r">
 | <a class="rightButton top" href="providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+URLEncoder.encode(request.getParameter("curProviderName"),"UTF-8") )%>&displaymode=day&dboperation=searchappointmentday" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewDaySched"/>' ; return true"><bean:message key="global.today"/></a>
</security:oscarSec>
<security:oscarSec roleName="<%=roleName$%>" objectName="_month" rights="r">

   | <a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+URLEncoder.encode(request.getParameter("curProviderName"),"UTF-8") )%>&displaymode=month&dboperation=searchappointmentmonth" TITLE='<bean:message key="provider.appointmentProviderAdminDay.viewMonthSched"/>' OnMouseOver="window.status='<bean:message key="provider.appointmentProviderAdminDay.viewMonthSched"/>' ; return true"><bean:message key="global.month"/></a>

 </security:oscarSec>
 
</caisi:isModuleLoad>

<%
	boolean anonymousEnabled = false;
	if (loggedInInfo1.getCurrentFacility() != null) {
		anonymousEnabled = loggedInInfo1.getCurrentFacility().isEnableAnonymous();
	}
	if(anonymousEnabled) {
%>
&nbsp;&nbsp;(<a href="#" onclick="popupPage(710, 1024,'<html:rewrite page="/PMmodule/createAnonymousClient.jsp"/>?programId=<%=(String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID)%>');return false;">New Anon Client</a>)
<%
	}
%>
<%
	boolean epe = false;
	if (loggedInInfo1.getCurrentFacility() != null) {
		epe = loggedInInfo1.getCurrentFacility().isEnablePhoneEncounter();
	}
	if(epe) {
%>
&nbsp;&nbsp;(<a href="#" onclick="popupPage(710, 1024,'<html:rewrite page="/PMmodule/createPEClient.jsp"/>?programId=<%=(String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID)%>');return false;">Phone Encounter</a>)
<%
	}
%>
</td>

<td class="title noprint" ALIGN="center"  BGCOLOR="ivory" width="33%">

<%
	if (isWeekView) {
for(int provIndex=0;provIndex<numProvider;provIndex++) {
if (curProvider_no[provIndex].equals(provNum)) {
%>
<bean:message key="provider.appointmentProviderAdminDay.weekView"/>: <%=curProviderName[provIndex]%>
<%
	} } } else { if (view==1) {
%>
<a href='providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=0&displaymode=day&dboperation=searchappointmentday'><bean:message key="provider.appointmentProviderAdminDay.grpView"/></a>
<% } else { %>
<% if (!isMobileOptimized) { %> <bean:message key="global.hello"/> <% } %>
<% out.println( userfirstname+" "+userlastname); %>
</td>
<% } } %>

<td id="group" ALIGN="RIGHT" BGCOLOR="Ivory">

<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="true">
<form method="post" name="findprovider" onSubmit="findProvider(<%=year%>,<%=month%>,<%=day%>);return false;" target="apptReception" action="receptionistfindprovider.jsp" style="display:inline;margin:0px;padding:0px;padding-right:10px">
<INPUT TYPE="text" NAME="providername" VALUE="" WIDTH="2" HEIGHT="10" border="0" size="10" maxlength="10" class="noprint" title="Find a Provider" placeholder="Enter Lastname">
<INPUT TYPE="SUBMIT" NAME="Go" VALUE='<bean:message key="provider.appointmentprovideradminmonth.btnGo"/>' class="noprint" onClick="findProvider(<%=year%>,<%=month%>,<%=day%>);return false;">
</form>
</caisi:isModuleLoad>

<form name="appointmentForm" style="display:inline;margin:0px;padding:0px;">
<% if (isWeekView) { %>
<bean:message key="provider.appointmentProviderAdminDay.provider"/>:
<select name="provider_select" onChange="goWeekView(this.options[this.selectedIndex].value)">
<%
	for (nProvider=0;nProvider<numProvider;nProvider++) {
%>
<option value="<%=curProvider_no[nProvider]%>"<%=curProvider_no[nProvider].equals(provNum)?" selected":""%>><%=curProviderName[nProvider]%></option>
<%
	}
%>

</select>

<%
	} else {
%>

<!-- caisi infirmary view extension add ffffffffffff-->
<caisi:isModuleLoad moduleName="caisi">
<table><tr><td align="right">
    <caisi:ProgramExclusiveView providerNo="<%=curUser_no%>" value="appointment">
	<%
		session.setAttribute("infirmaryView_isOscar", "true");
	%>
    </caisi:ProgramExclusiveView>
    <caisi:ProgramExclusiveView providerNo="<%=curUser_no%>" value="case-management">
	<%
		session.setAttribute("infirmaryView_isOscar", "false");
	%>
    </caisi:ProgramExclusiveView>
</caisi:isModuleLoad>

<caisi:isModuleLoad moduleName="TORONTO_RFQ">
	<%
		session.setAttribute("infirmaryView_isOscar", "false");
	%>
</caisi:isModuleLoad>

<caisi:isModuleLoad moduleName="oscarClinic">
	<%
		session.setAttribute("infirmaryView_isOscar", "true");
	%>
</caisi:isModuleLoad>
<!-- caisi infirmary view extension add end ffffffffffffff-->


<logic:notEqual name="infirmaryView_isOscar" value="false">

<%
	//session.setAttribute("case_program_id", null);
%>
	<!--  multi-site , add site dropdown list -->
 <%
 	if (bMultisites) {
 %>
	   <script>
			function changeSite(sel) {
				sel.style.backgroundColor=sel.options[sel.selectedIndex].style.backgroundColor;
				var siteName = sel.options[sel.selectedIndex].value;
				var newGroupNo = "<%=(mygroupno == null ? ".default" : mygroupno)%>";
			        <%if (org.oscarehr.common.IsPropertiesOn.isCaisiEnable() && org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable()){%>
				  popupPage(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo+"&site="+siteName);
			        <%}else {%>
			          popupPage(10,10, "providercontrol.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&color_template=deepblue&dboperation=updatepreference&displaymode=updatepreference&mygroup_no="+newGroupNo+"&site="+siteName);
			        <%}%>
			}
      </script>

    	<select id="site" name="site" onchange="changeSite(this)" style="background-color: <%=( selectedSite == null || siteBgColor.get(selectedSite) == null ? "#FFFFFF" : siteBgColor.get(selectedSite))%>">
    		<option value="none" style="background-color:white">---all clinic---</option>
    	<%
    		for (int i=0; i<curUserSites.size(); i++) {
    	%>
    		<option value="<%=curUserSites.get(i).getName()%>" style="background-color:<%=curUserSites.get(i).getBgColor()%>"
    				<%=(curUserSites.get(i).getName().equals(selectedSite)) ? " selected " : ""%> >
    			<%=curUserSites.get(i).getName()%>
    		</option>
    	<%
    		}
    	%>
    	</select>
<%
	}
%>
  <span><bean:message key="global.group"/>:</span>

<%
	List<MyGroupAccessRestriction> restrictions = myGroupAccessRestrictionDao.findByProviderNo(curUser_no);
%>
  <select id="mygroup_no" name="mygroup_no" onChange="changeGroup(this)">
  <option value=".<bean:message key="global.default"/>">.<bean:message key="global.default"/></option>


<%
if(!"true".equals(oscarVariables.getProperty("schedule.groupsFromPrograms","false"))) {
%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_team_schedule_only" rights="r" reverse="false">
<%
	String provider_no = curUser_no;
	for(Provider p : providerDao.getActiveProviders()) {
		boolean skip = checkRestriction(restrictions,p.getProviderNo());
		if(!skip) {
%>
<option value="<%=p.getProviderNo()%>" <%=mygroupno.equals(p.getProviderNo())?"selected":""%>>
		<%=p.getFormattedName()%></option>
<%
	} }
%>

</security:oscarSec>
<security:oscarSec roleName="<%=roleName$%>" objectName="_team_schedule_only" rights="r" reverse="true">
<%
	request.getSession().setAttribute("archiveView","false");
	for(MyGroup g : myGroupDao.searchmygroupno()) {
	
		boolean skip = checkRestriction(restrictions,g.getId().getMyGroupNo());

		if (!skip && (!bMultisites || siteGroups == null || siteGroups.size() == 0 || siteGroups.contains(g.getId().getMyGroupNo()))) {
%>
  <option value="<%="_grp_"+g.getId().getMyGroupNo()%>"
		<%=mygroupno.equals(g.getId().getMyGroupNo())?"selected":""%>><%=g.getId().getMyGroupNo()%></option>
<%
	}
	}

	for(Provider p : providerDao.getActiveProviders()) {
		boolean skip = checkRestriction(restrictions,p.getProviderNo());

		if (!skip && (!bMultisites || siteProviderNos  == null || siteProviderNos.size() == 0 || siteProviderNos.contains(p.getProviderNo()))) {
%>
  <option value="<%=p.getProviderNo()%>" <%=mygroupno.equals(p.getProviderNo())?"selected":""%>>
		<%=p.getFormattedName()%></option>
<%
	}
	}
%>
</security:oscarSec>

<% } else { //schedule.groupsFromPrograms 
	
	List<ProgramProvider> ppList = programManager.getProgramDomain(loggedInInfo1,loggedInInfo1.getLoggedInProviderNo());
	List<Integer> programDomain = new ArrayList<Integer>();
	for(ProgramProvider pp:ppList) {
		programDomain.add(pp.getProgramId().intValue());
	}
	List<String> mGroups = scheduleManager.getMyGroups(loggedInInfo1,programDomain);
	List<Provider> mProviders = providerManager.getActiveProvidersInMyDomain(loggedInInfo1,programDomain);

	for(String mGroup:mGroups) {
		
%>

<option value="<%="_grp_"+mGroup%>"
		<%=mygroupno.equals(mGroup)?"selected":""%>><%=mGroup%></option>
		
<% }
	
	for(Provider mProvider:mProviders) {
		boolean skip = checkRestriction(restrictions,mProvider.getProviderNo());
		if(!skip) {
%>
<option value="<%=mProvider.getProviderNo()%>" <%=mygroupno.equals(mProvider.getProviderNo())?"selected":""%>>
		<%=mProvider.getFormattedName()%></option>
		
<% } } }%>
</select>

</logic:notEqual>

<logic:equal name="infirmaryView_isOscar" value="false">
&nbsp;&nbsp;&nbsp;&nbsp;
</logic:equal>

<%
	}
%>


<!-- caisi infirmary view extension add fffffffffffff-->
<caisi:isModuleLoad moduleName="caisi">

	<jsp:include page="infirmaryviewprogramlist.jspf"/>

</caisi:isModuleLoad>
<!-- caisi infirmary view extension add end fffffffffffff-->

      </td>
      </tr>

      <tr><td colspan="3">
        <table border="0" cellpadding="0" bgcolor="#486ebd" cellspacing="0" width="100%">
        <tr>
<%
	boolean bShowDocLink = false;
boolean bShowEncounterLink = false;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment.doctorLink" rights="r">
<%
	bShowDocLink = true;
%>
</security:oscarSec>
<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r">
<%
	bShowEncounterLink = true;
%>
</security:oscarSec>


<%
	int hourCursor=0, minuteCursor=0, depth=everyMin; //depth is the period, e.g. 10,15,30,60min.
String am_pm=null;
boolean bColor=true, bColorHour=true; //to change color

int iCols=0, iRows=0, iS=0,iE=0,iSm=0,iEm=0; //for each S/E starting/Ending hour, how many events
int ih=0, im=0, iSn=0, iEn=0 ; //hour, minute, nthStartTime, nthEndTime, rowspan
boolean bFirstTimeRs=true;
boolean bFirstFirstR=true;
Object[] paramTickler = new Object[2];
String[] param = new String[2];
String strsearchappointmentday=request.getParameter("dboperation");

boolean userAvail = true;
int me = -1;
for(nProvider=0;nProvider<numProvider;nProvider++) {
	if(curUser_no.equals(curProvider_no[nProvider]) ) {
       //userInGroup = true;
		me = nProvider; break;
	}
}

   // set up the iterator appropriately (today - for each doctor; this week - for each day)
   int iterMax;
   if (isWeekView) {
      iterMax=7;
      // find the nProvider value that corresponds to provNum
      if(numProvider == 1) {
    	  nProvider = 0;
      }
      else {
	      for(int provIndex=0;provIndex<numProvider;provIndex++) {
	         if (curProvider_no[provIndex].equals(provNum)) {
	            nProvider=provIndex;
	         }
	      }
      }
   } else {
      iterMax=numProvider;
   }

   StringBuffer hourmin = null;
   String [] param1 = new String[2];

   java.util.ResourceBundle wdProp = ResourceBundle.getBundle("oscarResources", request.getLocale());

   for(int iterNum=0;iterNum<iterMax;iterNum++) {

     if (isWeekView) {
        // get the appropriate datetime objects for the current day in this week
        year = cal.get(Calendar.YEAR);
        month = (cal.get(Calendar.MONTH)+1);
        day = cal.get(Calendar.DAY_OF_MONTH);

        strDate = year + "-" + month + "-" + day;
        monthDay = String.format("%02d", month) + "-" + String.format("%02d", day);

        inform = new SimpleDateFormat ("yyyy-MM-dd", request.getLocale());
        try {
           formatDate = UtilDateUtilities.DateToString(inform.parse(strDate), wdProp.getString("date.EEEyyyyMMdd"),request.getLocale());
        } catch (Exception e) {
           MiscUtils.getLogger().error("Error", e);
           formatDate = UtilDateUtilities.DateToString(inform.parse(strDate), "EEE, yyyy-MM-dd");
        }
        strYear=""+year;
        strMonth=month>9?(""+month):("0"+month);
        strDay=day>9?(""+day):("0"+day);

        // Reset timecode bean for this day
        param3[0] = strDate; //strYear+"-"+strMonth+"-"+strDay;
        param3[1] = curProvider_no[nProvider];
    	dateTimeCodeBean.put(String.valueOf(provNum), "");
    	
    	List<Object[]> results = scheduleDateDao.search_appttimecode(ConversionUtils.fromDateString(strDate), curProvider_no[nProvider]);
    	for(Object[] result : results) {
    		 ScheduleTemplate st = (ScheduleTemplate)result[0];
        	 ScheduleDate sd = (ScheduleDate)result[1];
        	 dateTimeCodeBean.put(sd.getProviderNo(), st.getTimecode());
    	}
     

     for(ScheduleTemplateCode stc : scheduleTemplateCodeDao.findAll()) {
     
       dateTimeCodeBean.put("description"+stc.getCode(), stc.getDescription());
       dateTimeCodeBean.put("duration"+stc.getCode(), stc.getDuration());
       dateTimeCodeBean.put("color"+stc.getCode(), (stc.getColor()==null || "".equals(stc.getColor()))?bgcolordef:stc.getColor());
       dateTimeCodeBean.put("confirm" + stc.getCode(), stc.getConfirm());
     }

        // move the calendar forward one day
        cal.add(Calendar.DATE, 1);
     } else {
        nProvider = iterNum;
     }

     userAvail = true;
     int timecodeLength = dateTimeCodeBean.get(curProvider_no[nProvider])!=null?((String) dateTimeCodeBean.get(curProvider_no[nProvider]) ).length() : 4*24;

     if (timecodeLength == 0){
        timecodeLength = 4*24;
     }

     depth = bDispTemplatePeriod ? (24*60 / timecodeLength) : everyMin; // add function to display different time slot
     param1[0] = strDate; //strYear+"-"+strMonth+"-"+strDay;
     param1[1] = curProvider_no[nProvider];
     
     ScheduleDate sd = scheduleDateDao.findByProviderNoAndDate(curProvider_no[nProvider],ConversionUtils.fromDateString(strDate));
     
     //viewall function
     if(request.getParameter("viewall")==null || request.getParameter("viewall").equals("0") ) {
         if(sd == null|| "0".equals(String.valueOf(sd.getAvailable())) ) {
             if(nProvider!=me ) continue;
             else userAvail = false;
         }
     }
     bColor=bColor?false:true;
%>
            <td valign="top" width="<%=isWeekView?100/7:100/numProvider%>%"> <!-- for the first provider's schedule -->

        <table border="0" cellpadding="0" bgcolor="#486ebd" cellspacing="0" width="100%"><!-- for the first provider's name -->
          <tr><td class="infirmaryView" NOWRAP ALIGN="center" BGCOLOR="<%=bColor?"#bfefff":"silver"%>">
 <!-- caisi infirmary view extension modify ffffffffffff-->
  <logic:notEqual name="infirmaryView_isOscar" value="false">

      <%
      	if (isWeekView) {
      %>
          <b><a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&view=0&displaymode=day&dboperation=searchappointmentday"><%=formatDate%></a></b>
      <%
      	} else {
      		UserPropertyDAO upDao = SpringUtils.getBean(UserPropertyDAO.class);
      		String groupModule = upDao.getStringValue(curProvider_no[nProvider], "GroupModule");
      		boolean hasGroupModule = groupModule != null && "true".equals(groupModule);
      %>
  <b>
  <%if(hasGroupModule) { %>
  <input type='button' value="G" name='groupProps' onClick="launchGroupProperties('<%=curProvider_no[nProvider]%>')" title="Manage Group Series Attributes" style="color:black" class="noprint">
  <% } %>
  <input type='button' value="<bean:message key="provider.appointmentProviderAdminDay.weekLetter"/>" name='weekview' onClick=goWeekView('<%=curProvider_no[nProvider]%>') title="<bean:message key="provider.appointmentProviderAdminDay.weekView"/>" style="color:black" class="noprint">
	  <input type='button' value="<bean:message key="provider.appointmentProviderAdminDay.searchLetter"/>" name='searchview' onClick=goSearchView('<%=curProvider_no[nProvider]%>') title="<bean:message key="provider.appointmentProviderAdminDay.searchView"/>" style="color:black" class="noprint">
          <b><input type='radio' name='flipview' class="noprint" onClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="Flip view"  >
          <a href=# onClick="goZoomView('<%=curProvider_no[nProvider]%>','<%=StringEscapeUtils.escapeJavaScript(curProviderName[nProvider])%>')" onDblClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>" >
          <!--a href="providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider=<%=curProvider_no[nProvider]%>&curProviderName=<%=curProviderName[nProvider]%>&displaymode=day&dboperation=searchappointmentday" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>"-->
          <%=curProviderName[nProvider]%></a> 
       	<oscar:oscarPropertiesCheck value="yes" property="TOGGLE_REASON_BY_PROVIDER" defaultVal="true">   
				<a id="expandReason" href="#" onclick="return toggleReason('<%=curProvider_no[nProvider]%>');" 
					title="<bean:message key="provider.appointmentProviderAdminDay.expandreason"/>">*</a>
					<%-- Default is to hide inline reasons. --%>
				<c:set value="true" var="hideReason" />
		</oscar:oscarPropertiesCheck>	
</b>
      <% } %>

          <%
          	if (!userAvail) {
          %>
          [<bean:message key="provider.appointmentProviderAdminDay.msgNotOnSched"/>]
          <%
          	}
          %>
</logic:notEqual>
<logic:equal name="infirmaryView_isOscar" value="false">
	<%
		String prID="1";
	%>
	<logic:present name="infirmaryView_programId">
	<%
		prID=(String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID);
	%>
	</logic:present>
	<logic:iterate id="pb" name="infirmaryView_programBeans" type="org.apache.struts.util.LabelValueBean">
	  	<%
	  		if (pb.getValue().equals(prID)) {
	  	%>
  		<b><%=pb.getLabel()%></label></b>
		<%
			}
		%>
  	</logic:iterate>
</logic:equal>
<!-- caisi infirmary view extension modify end ffffffffffffffff-->
</td></tr>
          <tr><td valign="top">

<!-- caisi infirmary view exteion add -->
<!--  fffffffffffffffffffffffffffffffffffffffffff-->
<caisi:isModuleLoad moduleName="caisi">

<%
String eURL =  "../oscarEncounter/IncomingEncounter.do?providerNo="+curUser_no+"&curProviderNo="+curProvider_no[nProvider]+"&userName="+URLEncoder.encode( userfirstname+" "+userlastname)+"&curDate="+curYear+"-"+curMonth+"-"+curDay+"&appointmentDate="+year+"-"+month+"-"+day+"&source=cm";
%>
<jsp:include page="infirmarydemographiclist.jspf">
	<jsp:param value="<%=userAvail %>" name="userAvail"/>
	<jsp:param name="strDate" value="<%=strDate %>"/>
	<jsp:param name="bShowDocLink" value="<%=bShowDocLink %>"/>
	<jsp:param name="bShowEncounterLink" value="<%=bShowEncounterLink %>"/>
	<jsp:param name="eURL" value="<%=eURL%>"/>
</jsp:include>

</caisi:isModuleLoad>


<logic:notEqual name="infirmaryView_isOscar" value="false">
<!-- caisi infirmary view exteion add end ffffffffffffffffff-->
<!-- =============== following block is the original oscar code. -->
        <!-- table for hours of day start -->
        <table id="providerSchedule" border="0" cellpadding="0" bgcolor="<%=userAvail?"#486ebd":"silver"%>" cellspacing="0" width="100%">
<%
		bFirstTimeRs=true;
        bFirstFirstR=true;
        
        String useProgramLocation = OscarProperties.getInstance().getProperty("useProgramLocation");
    	String moduleNames = OscarProperties.getInstance().getProperty("ModuleNames");
    	boolean caisiEnabled = moduleNames != null && org.apache.commons.lang.StringUtils.containsIgnoreCase(moduleNames, "Caisi");
    	boolean locationEnabled = caisiEnabled && (useProgramLocation != null && useProgramLocation.equals("true"));
    	
    	int length = locationEnabled ? 4 : 3;
    	
        String [] param0 = new String[length];

        param0[0]=curProvider_no[nProvider];
        param0[1]=year+"-"+month+"-"+day;//e.g."2001-02-02";
		param0[2]=programId_oscarView;
		/*
		if (locationEnabled) {
			
			
			ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
			ProgramProvider programProvider = programManager2.getCurrentProgramInDomain(loggedInInfo1,loggedInInfo1.getLoggedInProviderNo());
            if(programProvider!=null && programProvider.getProgram() != null) {
            	programProvider.getProgram().getName();
            }
		    param0[3]=request.getParameter("programIdForLocation");
		    strsearchappointmentday = "searchappointmentdaywithlocation";
		}*/
		//load program domain for CAISI, or else just 0
		List<Integer> programIds = new ArrayList<Integer>();
		programIds.add(0);
		for(ProgramProvider pp: programManager.getProgramDomain(loggedInInfo1, loggedInInfo1.getLoggedInProviderNo())) {
			programIds.add(pp.getProgramId().intValue());
		}
		
		List<Appointment> appointments = appointmentDao.searchappointmentday(curProvider_no[nProvider], ConversionUtils.fromDateString(year+"-"+month+"-"+day),programIds);
               	Iterator<Appointment> it = appointments.iterator();
		
                Appointment appointment = null;
            	String router = "";
            	String record = "";
            	String module = "";
            	String newUxUrl = "";
            	String inContextStyle = "";
            	
            	if(request.getParameter("record")!=null){
            		record=request.getParameter("record");
            	}
            	
            	if(request.getParameter("module")!=null){
            		module=request.getParameter("module");
            	}
        List<Object[]> confirmTimeCode = scheduleDateDao.search_appttimecode(ConversionUtils.fromDateString(strDate), curProvider_no[nProvider]);
            	
	    for(ih=startHour*60; ih<=(endHour*60+(60/depth-1)*depth); ih+=depth) { // use minutes as base
            hourCursor = ih/60;
            minuteCursor = ih%60;
            bColorHour=minuteCursor==0?true:false; //every 00 minute, change color

            //templatecode
            if((dateTimeCodeBean.get(curProvider_no[nProvider]) != null)&&(dateTimeCodeBean.get(curProvider_no[nProvider]) != "") && confirmTimeCode.size()!=0) {
	          int nLen = 24*60 / ((String) dateTimeCodeBean.get(curProvider_no[nProvider]) ).length();
	          int ratio = (hourCursor*60+minuteCursor)/nLen;
              hourmin = new StringBuffer(dateTimeCodeBean.get(curProvider_no[nProvider])!=null?((String) dateTimeCodeBean.get(curProvider_no[nProvider])).substring(ratio,ratio+1):" " );
            } else { hourmin = new StringBuffer(); }
%>
          <tr>
            <td align="RIGHT" class="<%=bColorHour?"scheduleTime00":"scheduleTimeNot00"%>" NOWRAP>
             <a href=# onClick="confirmPopupPage(400,780,'../appointment/addappointment.jsp?provider_no=<%=curProvider_no[nProvider]%>&bFirstDisp=<%=true%>&year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&start_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+ (minuteCursor<10?"0":"") +minuteCursor%>&end_time=<%=(hourCursor>9?(""+hourCursor):("0"+hourCursor))+":"+(minuteCursor+depth-1)%>&duration=<%=dateTimeCodeBean.get("duration"+hourmin.toString())%>','<%=dateTimeCodeBean.get("confirm"+hourmin.toString())%>','<%=allowDay%>','<%=allowWeek%>');return false;"
  title='<%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+ (minuteCursor<10?"0":"")+minuteCursor)%> - <%=MyDateFormat.getTimeXX_XXampm(hourCursor +":"+((minuteCursor+depth-1)<10?"0":"")+(minuteCursor+depth-1))%>' class="adhour">
             <%=(hourCursor<10?"0":"") +hourCursor+ ":"%><%=(minuteCursor<10?"0":"")+minuteCursor%>&nbsp;</a></td>
            <td class="hourmin" width='1%' <%=dateTimeCodeBean.get("color"+hourmin.toString())!=null?("bgcolor="+dateTimeCodeBean.get("color"+hourmin.toString()) ):""%> title='<%=dateTimeCodeBean.get("description"+hourmin.toString())%>'><font color='<%=(dateTimeCodeBean.get("color"+hourmin.toString())!=null && !dateTimeCodeBean.get("color"+hourmin.toString()).equals(bgcolordef) )?"black":"white"%>'><%=hourmin.toString()%></font></td>
<%
	while (bFirstTimeRs?it.hasNext():true) { //if it's not the first time to parse the standard time, should pass it by
                  appointment = bFirstTimeRs?it.next():appointment;
                  len = bFirstTimeRs&&!bFirstFirstR?lenLimitedS:lenLimitedL;
                  String strStartTime = ConversionUtils.toTimeString(appointment.getStartTime());
                  String strEndTime = ConversionUtils.toTimeString(appointment.getEndTime());
                  
                  iS=Integer.parseInt(String.valueOf(strStartTime).substring(0,2));
                  iSm=Integer.parseInt(String.valueOf(strStartTime).substring(3,5));
                  iE=Integer.parseInt(String.valueOf(strEndTime).substring(0,2));
              	  iEm=Integer.parseInt(String.valueOf(strEndTime).substring(3,5));

          	  if( (ih < iS*60+iSm) && (ih+depth-1)<iS*60+iSm ) { //iS not in this period (both start&end), get to the next period
          	  	//out.println("<td width='10'>&nbsp;</td>"); //should be comment
          	  	bFirstTimeRs=false;
          	  	break;
          	  }
          	  if( (ih > iE*60+iEm) ) { //appt before this time slot (both start&end), get to the next period
          	  	//out.println("<td width='10'>&nbsp;</td>"); //should be comment
          	  	bFirstTimeRs=true;
          	  	continue;
          	  }
         	    iRows=((iE*60+iEm)-ih)/depth+1; //to see if the period across an hour period
         	    //iRows=(iE-iS)*60/depth+iEm/depth-iSm/depth+1; //to see if the period across an hour period

 
                    int demographic_no = appointment.getDemographicNo();

                  //Pull the appointment name from the demographic information if the appointment is attached to a specific demographic.
                  //Otherwise get the name associated with the appointment from the appointment information
                  StringBuilder nameSb = new StringBuilder();
                  if ((demographic_no != 0)&& (demographicDao != null)) {
                        Demographic demo = demographicDao.getDemographic(String.valueOf(demographic_no));
                        nameSb.append(demo.getLastName())
                              .append(",")
                              .append(demo.getFirstName());
                  }
                  else {
                        nameSb.append(String.valueOf(appointment.getName()));
                  }
                  String name = UtilMisc.toUpperLowerCase(nameSb.toString());

                  paramTickler[0]=String.valueOf(demographic_no);
                  paramTickler[1]=MyDateFormat.getSysDate(strDate); //year+"-"+month+"-"+day;//e.g."2001-02-02";
                  tickler_no = "";
                  tickler_note="";
                  
                 if(securityInfoManager.hasPrivilege(loggedInInfo1, "_tickler", "r", demographic_no)) {
	                  for(Tickler t: ticklerManager.search_tickler(loggedInInfo1, demographic_no,MyDateFormat.getSysDate(strDate))) {
	                	  tickler_no = t.getId().toString();
	                      tickler_note = t.getMessage()==null?tickler_note:tickler_note + "\n" + t.getMessage();
	                  }
                 }
                     
                  //alerts and notes
                  DemographicCust dCust = demographicCustDao.find(demographic_no);
                  
                  
                  ver = "";
                  roster = "";
                  Demographic demographic = demographicDao.getDemographicById(demographic_no);
                  if(demographic != null) {
                   
                    ver = demographic.getVer();
                    roster = demographic.getRosterStatus();

                    int intMob = 0;
                    int intDob = 0;

                    mob = String.valueOf(demographic.getMonthOfBirth());
                    if(mob.length()>0 && !mob.equals("null"))
                    	intMob = Integer.parseInt(mob);

                    dob = String.valueOf(demographic.getDateOfBirth());
                    if(dob.length()>0 && !dob.equals("null"))
                    	intDob = Integer.parseInt(dob);


                    demBday = mob + "-" + dob;

                    if (roster == null ) { 
                        roster = "";
                    }
                  }
                  study_no = new StringBuffer("");
                  study_link = new StringBuffer("");
		  studyDescription = new StringBuffer("");

		  int numStudy = 0;
		  
		  for(DemographicStudy ds:demographicStudyDao.findByDemographicNo(demographic_no)) {
			  Study study = studyDao.find(ds.getId().getStudyNo());
			  if(study != null && study.getCurrent1() == 1) {
				  numStudy++;
				  if(numStudy == 1) {
					  study_no = new StringBuffer(String.valueOf(study.getId()));
	                          study_link = new StringBuffer(String.valueOf(study.getStudyLink()));
	                          studyDescription = new StringBuffer(String.valueOf(study.getDescription()));
				  } else {
					  study_no = new StringBuffer("0");
		                      study_link = new StringBuffer("formstudy.jsp");
				      studyDescription = new StringBuffer("Form Studies");
				  }
			  }
		  }
		  
                  //String reason = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(String.valueOf(appointment.getReason()).trim());
                  //String notes = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(String.valueOf(appointment.getNotes()).trim());
                  String reason = String.valueOf(appointment.getReason()).trim();
                  String notes = String.valueOf(appointment.getNotes()).trim();
                  String status = String.valueOf(appointment.getStatus()).trim();
          	      String sitename = String.valueOf(appointment.getLocation()).trim();
          	      String type = appointment.getType();
          	      String urgency = appointment.getUrgency();
          	      String reasonCodeName = null;
          	      if(appointment.getReasonCode() != null)    {  	   
          	    	LookupListItem lli  = reasonCodesMap.get(appointment.getReasonCode()); 
          	    	if(lli != null) {
          	    		reasonCodeName = lli.getLabel();
          	    	}
          	      }
				if ( "yes".equalsIgnoreCase(OscarProperties.getInstance().getProperty("SHOW_APPT_TYPE_WITH_REASON")) ) {
					reasonCodeName = ( type + " : " + reasonCodeName );
				}
          
          	  bFirstTimeRs=true;
	    as.setApptStatus(status);

	 //multi-site. if a site have been selected, only display appointment in that site
	 if (!bMultisites || (selectedSite == null && CurrentSiteMap.get(sitename) != null) || sitename.equals(selectedSite)) {
%>
            <td class="appt" bgcolor='<%=as.getBgColor()%>' rowspan="<%=iRows%>" <%-- =view==0?(len==lenLimitedL?"nowrap":""):"nowrap"--%> nowrap>
			<%
			   if (BookingSource.MYOSCAR_SELF_BOOKING == appointment.getBookingSource())
				{
					%>
						<bean:message key="provider.appointmentProviderAdminDay.SelfBookedMarker"/>
					<%
				}
			%>
			 <!-- multisites : add colour-coded to the "location" value of that appointment. -->
			 <%if (bMultisites) {%>
			 	<span title="<%= sitename %>" style="background-color:<%=siteBgColor.get(sitename)%>;">&nbsp;</span>|
			 <%} %>

            <%
                String nextStatus = as.getNextStatus();
			    if (nextStatus != null && !nextStatus.equals("")) {
            %>
			<!-- Short letters -->
            <a class="apptStatus" href=# onclick="refreshSameLoc('providercontrol.jsp?appointment_no=<%=appointment.getId()%>&provider_no=<%=curProvider_no[nProvider]%>&status=&statusch=<%=nextStatus%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+URLEncoder.encode(request.getParameter("curProviderName"),"UTF-8") )%>&displaymode=addstatus&dboperation=updateapptstatus&viewall=<%=request.getParameter("viewall")==null?"0":(request.getParameter("viewall"))%><%=isWeekView?"&viewWeek=1":""%>');" title="<%=as.getTitleString(request.getLocale())%> " >
            <%
						}
						if (nextStatus != null) {
							if(OscarProperties.getInstance().getProperty("APPT_SHOW_SHORT_LETTERS", "false") != null 
								&& OscarProperties.getInstance().getProperty("APPT_SHOW_SHORT_LETTERS", "false").equals("true")){
						
								String colour = as.getShortLetterColour();
								if(colour == null){
									colour = "#FFFFFF";
								}			
									
					%>
								<span 
									class='short_letters' 
									style='color:<%= colour%>;border:0;height:10'>
											[<%=UtilMisc.htmlEscape(as.getShortLetters())%>]
									</span>
					<%	
							}else{
				    %>
					
				    			<img src="../images/<%=as.getImageName()%>" border="0" height="10" title="<%=as.getTitleString(request.getLocale())%>">
					
            <%
							}
                } else {
	                out.print("&nbsp;");
                }

			%>
			</a>
			<%
            if(urgency != null && urgency.equals("critical")) {
            %>
            	<img src="../images/warning-icon.png" border="0" width="14" height="14" title="Critical Appointment"/>
            <% } %>
<%--|--%>
        <%
        			if(demographic_no==0) {
        %>
        	<!--  caisi  -->
        	<security:oscarSec roleName="<%=roleName$%>" objectName="_tickler" rights="r">
	        	<% if (tickler_no.compareTo("") != 0) {%>
		        	<caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
	        			<a href="#" onClick="popupPage(700,1024, '../tickler/ticklerDemoMain.jsp?demoview=0');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a>
	    			</caisi:isModuleLoad>
	    			<caisi:isModuleLoad moduleName="ticklerplus">
	    				<a href="../ticklerPlus/index.jsp" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a>
	    			</caisi:isModuleLoad>
	    		<%} %>
    		</security:oscarSec>
    		
    		<!--  alerts -->
    		<% if(OscarProperties.getInstance().getProperty("displayAlertsOnScheduleScreen", "").equals("true")){ %>
    		<% if(dCust != null && dCust.getAlert() != null && !dCust.getAlert().isEmpty()) { %>
    			<a href="#" onClick="return false;" title="<%=StringEscapeUtils.escapeHtml(dCust.getAlert())%>">A</a>		
    		<%} }%>
    		
    		<!--  notes -->
    		<% if(OscarProperties.getInstance().getProperty("displayNotesOnScheduleScreen", "").equals("true")){ %>
    		<% if(dCust != null && dCust.getNotes() != null && !SxmlMisc.getXmlContent(dCust.getNotes(), "<unotes>", "</unotes>").isEmpty()) { %>
    			<a href="#" onClick="return false;" title="<%=StringEscapeUtils.escapeHtml(SxmlMisc.getXmlContent(dCust.getNotes(), "<unotes>", "</unotes>"))%>">N</a>		
    		<%} }%>
    		
    		
<a href=# onClick ="popupPage(535,860,'../appointment/appointmentcontrol.jsp?appointment_no=<%=appointment.getId()%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=0&displaymode=edit&dboperation=search');return false;" title="<%=iS+":"+(iSm>10?"":"0")+iSm%>-<%=iE+":"+iEm%>
<%=name%>
	<%=type != null ? "type: " + type : "" %>
	reason: <%=reasonCodeName!=null?reasonCodeName:""%> <%if(reason!=null && !reason.isEmpty()){%>- <%=UtilMisc.htmlEscape(reason)%>
<%}%>	<bean:message key="provider.appointmentProviderAdminDay.notes"/>: <%=UtilMisc.htmlEscape(notes)%>" >
            .<%=(view==0&&numAvailProvider!=1)?(name.length()>len?name.substring(0,len).toUpperCase():name.toUpperCase()):name.toUpperCase()%>
            </font></a><!--Inline display of reason -->
      <oscar:oscarPropertiesCheck property="SHOW_APPT_REASON" value="yes" defaultVal="true">
      <span class="reason reason_<%=curProvider_no[nProvider]%> ${ hideReason ? "hideReason" : "" }"><bean:message key="provider.appointmentProviderAdminDay.Reason"/>:<%=UtilMisc.htmlEscape(reason)%></span>
      </oscar:oscarPropertiesCheck></td>
        <%
        			} else {
				%>	<% if (tickler_no.compareTo("") != 0) {%>
			        	<caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
                                        <a href="#" onClick="popupPage(700,1024, '../tickler/ticklerDemoMain.jsp?demoview=<%=demographic_no%>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a>
    					</caisi:isModuleLoad>
    					<caisi:isModuleLoad moduleName="ticklerplus">
		    				<!--  <a href="../Tickler.do?method=filter&filter.client=<%=demographic_no %>" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a> -->
    						<a href="#" onClick="popupPage(700,102.4, '../Tickler.do?method=filter&filter.client=<%=demographic_no %>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.ticklerMsg"/>: <%=UtilMisc.htmlEscape(tickler_note)%>"><font color="red">!</font></a>
    					</caisi:isModuleLoad>
					<%} %>
					
					<!--  alerts -->
			<% if(OscarProperties.getInstance().getProperty("displayAlertsOnScheduleScreen", "").equals("true")) {%>
    		<% if(dCust != null && dCust.getAlert() != null && !dCust.getAlert().isEmpty()) { %>
    			<a href="#" onClick="return false;" title="<%=StringEscapeUtils.escapeHtml(dCust.getAlert())%>">A</a>		
    		<%} } %>
    		
    		<!--  notes -->
    		<% if(OscarProperties.getInstance().getProperty("displayNotesOnScheduleScreen", "").equals("true")) {%>
    		<% if(dCust != null && dCust.getNotes() != null && !SxmlMisc.getXmlContent(dCust.getNotes(), "<unotes>", "</unotes>").isEmpty()) { %>
    			<a href="#" onClick="return false;" title="<%=StringEscapeUtils.escapeHtml(SxmlMisc.getXmlContent(dCust.getNotes(), "<unotes>", "</unotes>"))%>">N</a>		
    		<%} }%>

<!-- doctor code block 1 -->
<% if(bShowDocLink) { %>
<!-- security:oscarSec roleName="<%--=roleName$--%>" objectName="_appointment.doctorLink" rights="r" -->
<% if ("".compareTo(study_no.toString()) != 0) {%>	<a href="#" onClick="popupPage(700,1024, '../form/study/forwardstudyname.jsp?study_link=<%=study_link.toString()%>&demographic_no=<%=demographic_no%>&study_no=<%=study_no%>');return false;" title="<bean:message key="provider.appointmentProviderAdminDay.study"/>: <%=UtilMisc.htmlEscape(studyDescription.toString())%>"><%="<font color='"+studyColor+"'>"+studySymbol+"</font>"%></a><%} %>

<% if (ver!=null && ver!="" && "##".compareTo(ver.toString()) == 0){%><a href="#" title="<bean:message key="provider.appointmentProviderAdminDay.versionMsg"/> <%=UtilMisc.htmlEscape(ver)%>"> <font color="red">*</font></a><%}%>

<% if (roster!="" && "FS".equalsIgnoreCase(roster)){%> <a href="#" title="<bean:message key="provider.appointmentProviderAdminDay.rosterMsg"/> <%=UtilMisc.htmlEscape(roster)%>"><font color="red">$</font></a><%}%>

<% if ("NR".equalsIgnoreCase(roster) || "PL".equalsIgnoreCase(roster)){%> <a href="#" title="<bean:message key="provider.appointmentProviderAdminDay.rosterMsg"/> <%=UtilMisc.htmlEscape(roster)%>"><font color="red">#</font></a><%}%>
<!-- /security:oscarSec -->
<% } %>
<!-- doctor code block 2 -->
<%

boolean disableStopSigns = PreventionManager.isDisabled();
boolean propertyExists = PreventionManager.isCreated();
if(disableStopSigns!=true){
if( OscarProperties.getInstance().getProperty("SHOW_PREVENTION_STOP_SIGNS","false").equals("true") || propertyExists==true) {

		String warning = prevMgr.getWarnings(loggedInInfo1, String.valueOf(demographic_no));
		warning = PreventionManager.checkNames(warning);

		String htmlWarning = "";

		if( !warning.equals("")) {
			  htmlWarning = "<img src=\"../images/stop_sign.png\" height=\"14\" width=\"14\" title=\"" + warning +"\">&nbsp;";
		}

		out.print(htmlWarning);

}
}

String start_time = "";
if( iS < 10 ) {
	 	start_time = "0"; 
}
start_time +=  iS + ":";
if( iSm < 10 ) {
	 	start_time += "0";
}

start_time += iSm + ":00";
%>

<a class="apptLink" href=# onClick ="popupPage(535,860,'../appointment/appointmentcontrol.jsp?appointment_no=<%=appointment.getId()%>&provider_no=<%=curProvider_no[nProvider]%>&year=<%=year%>&month=<%=month%>&day=<%=day%>&start_time=<%=iS+":"+iSm%>&demographic_no=<%=demographic_no%>&displaymode=edit&dboperation=search');return false;" 
<oscar:oscarPropertiesCheck property="SHOW_APPT_REASON_TOOLTIP" value="yes" defaultVal="true"> 
	title="<%=name%>
	type: <%=type != null ? type : "" %>
	reason: <%=reasonCodeName!=null? reasonCodeName:""%> <%if(reason!=null && !reason.isEmpty()){%>- <%=UtilMisc.htmlEscape(reason)%><%}%>
	notes: <%=notes%>"
</oscar:oscarPropertiesCheck> ><%=(view==0) ? (name.length()>len?name.substring(0,len) : name) :name%></a>

<% if(len==lenLimitedL || view!=0 || numAvailProvider==1 ) {%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart" rights="r">
<oscar:oscarPropertiesCheck property="eform_in_appointment" value="yes">
	&#124;<b><a href="#" onclick="popupPage(500,1024,'../eform/efmformslistadd.jsp?parentAjaxId=eforms&demographic_no=<%=demographic_no%>&appointment=<%=appointment.getId()%>'); return false;"
		  title="eForms">e</a></b>
</oscar:oscarPropertiesCheck>
</security:oscarSec>

<!-- doctor code block 3 -->
<% if(bShowEncounterLink && !isWeekView) { %>
<% if (oscar.OscarProperties.getInstance().isPropertyActive("SINGLE_PAGE_CHART")) { 
	
	newUxUrl = "../web/#/record/" + demographic_no + "/";
	
	if(String.valueOf(demographic_no).equals(record) && !module.equals("summary")){
		newUxUrl =  newUxUrl + module;
		inContextStyle = "style='color: blue;'";
	}else{
		newUxUrl =  newUxUrl + "summary?appointmentNo=" + appointment.getId() + "&encType=face%20to%20face%20encounter%20with%20client";
		inContextStyle = "";
	}
%>
&#124; <a href="<%=newUxUrl%>" <%=inContextStyle %>><bean:message key="provider.appointmentProviderAdminDay.btnE"/>2</a>
<%}%>

<% String  eURL = "../oscarEncounter/IncomingEncounter.do?providerNo="
	+curUser_no+"&appointmentNo="
	+appointment.getId()
	+"&demographicNo="
	+demographic_no
	+"&curProviderNo="
	+curProvider_no[nProvider]
	+"&reason="
	+URLEncoder.encode(reason, "UTF-8")
	+"&encType="
	+URLEncoder.encode("face to face encounter with client","UTF-8")
	+"&userName="
	+URLEncoder.encode( userfirstname+" "+userlastname, "UTF-8")
	+"&curDate="+curYear+"-"+curMonth+"-"
	+curDay+"&appointmentDate="+year+"-"
	+month+"-"+day+"&startTime=" 
	+ start_time + "&status="+status 
	+ "&apptProvider_no=" 
	+ curProvider_no[nProvider] 
			+ "&providerview=" 
	+ curProvider_no[nProvider];%>
	
<% if (showOldEchartLink) { %>
&#124; <a href=# class="encounterBtn" onClick="popupWithApptNo(710, 1024,'<%=eURL%>','encounter',<%=appointment.getId()%>);return false;" title="<bean:message key="global.encounter"/>">
<bean:message key="provider.appointmentProviderAdminDay.btnE"/></a>
<% }} %>

<%= (bShortcutIntakeForm) ? "| <a href='#' onClick='popupPage(700, 1024, \"formIntake.jsp?demographic_no="+demographic_no+"\")' title='Intake Form'>In</a>" : "" %>
<!--  eyeform open link -->
<% if (oscar.OscarProperties.getInstance().isPropertyActive("new_eyeform_enabled") && !isWeekView) { %>
&#124; <a href="#" onClick='popupPage(800, 1280, "../eyeform/eyeform.jsp?demographic_no=<%=demographic_no %>&appointment_no=<%=appointment.getId()%>");return false;' title="EyeForm">EF</a>
<% } %>

<!-- billing code block -->
<% if (!isWeekView) { %>
	<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r">
	<% 
	if(status.indexOf('B')==-1) 
	{ 
	%>
		&#124; <a href=# onClick='popupPage(755,1200, "../billing.do?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=appointment.getId()%>&demographic_name=<%=URLEncoder.encode(name)%>&status=<%=status%>&demographic_no=<%=demographic_no%>&providerview=<%=curProvider_no[nProvider]%>&user_no=<%=curUser_no%>&apptProvider_no=<%=curProvider_no[nProvider]%>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=start_time%>&bNewForm=1");return false;' title="<bean:message key="global.billingtag"/>"><bean:message key="provider.appointmentProviderAdminDay.btnB"/></a>
	<% 
	}
	else 
	{
		if(caisiBillingPreferenceNotDelete!=null && caisiBillingPreferenceNotDelete.equals("1")) 
		{
	%>
			&#124; <a href=# onClick='onUpdatebill("../billing/CA/ON/billingEditWithApptNo.jsp?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=<%=URLEncoder.encode("")%>&appointment_no=<%=appointment.getId()%>&demographic_name=<%=URLEncoder.encode(name)%>&status=<%=status%>&demographic_no=<%=demographic_no%>&providerview=<%=curProvider_no[nProvider]%>&user_no=<%=curUser_no%>&apptProvider_no=<%=curProvider_no[nProvider]%>&appointment_date=<%=year+"-"+month+"-"+day%>&start_time=<%=iS+":"+iSm%>&bNewForm=1");return false;' title="<bean:message key="global.billingtag"/>">=<bean:message key="provider.appointmentProviderAdminDay.btnB"/></a>
	<% 
		} 
		else 
		{ 
	%>
		&#124; <a href=# onClick='onUnbilled("../billing/CA/<%=prov%>/billingDeleteWithoutNo.jsp?status=<%=status%>&appointment_no=<%=appointment.getId()%>");return false;' title="<bean:message key="global.billingtag"/>">-<bean:message key="provider.appointmentProviderAdminDay.btnB"/></a>
	<% 
		} 
	} 
	%>

<!--/security:oscarSec-->
	  </security:oscarSec>
<% } %>
<!-- billing code block -->
<security:oscarSec roleName="<%=roleName$%>" objectName="_masterLink" rights="r">
   
    &#124; <a class="masterBtn" href="javascript: function myFunction() {return false; }" onClick="popupWithApptNo(700,1024,'../demographic/demographiccontrol.jsp?demographic_no=<%=demographic_no%>&apptProvider=<%=curProvider_no[nProvider]%>&appointment=<%=appointment.getId()%>&displaymode=edit&dboperation=search_detail','master',<%=appointment.getId()%>)"
    title="<bean:message key="provider.appointmentProviderAdminDay.msgMasterFile"/>"><bean:message key="provider.appointmentProviderAdminDay.btnM"/></a>
  
</security:oscarSec>
      <% if (!isWeekView) { %>

<!-- doctor code block 4 -->

<security:oscarSec roleName="<%=roleName$%>" objectName="_appointment.doctorLink" rights="r">
     &#124; <a href=# onClick="popupWithApptNo(700,1027,'../oscarRx/choosePatient.do?providerNo=<%=curUser_no%>&demographicNo=<%=demographic_no%>','rx',<%=appointment.getId()%>)" title="<bean:message key="global.prescriptions"/>"><bean:message key="global.rx"/>
      </a>


<!-- doctor color -->
<oscar:oscarPropertiesCheck property="ENABLE_APPT_DOC_COLOR" value="yes">
        <%
                String providerColor = null;
                if(view == 1 && demographicDao != null && userPropertyDao != null) {
                        String providerNo = (demographicDao.getDemographic(String.valueOf(demographic_no))==null?null:demographicDao.getDemographic(String.valueOf(demographic_no)).getProviderNo());
                        UserProperty property = userPropertyDao.getProp(providerNo, UserPropertyDAO.COLOR_PROPERTY);
                        if(property != null) {
                                providerColor = property.getValue();
                        }
                }
        %>
        <%= (providerColor != null ? "<span style=\"background-color:"+providerColor+";width:5px\">&nbsp;</span>" : "") %>
</oscar:oscarPropertiesCheck>

      <%
	  if("bc".equalsIgnoreCase(prov)){
	  if(patientHasOutstandingPrivateBills(String.valueOf(demographic_no))){
	  %>
	  &#124;<b style="color:#FF0000">$</b>
	  <%}}%>
      <oscar:oscarPropertiesCheck property="SHOW_APPT_REASON" value="yes" defaultVal="true">
     		<span class="reason_<%=curProvider_no[nProvider]%> ${ hideReason ? "hideReason" : "" }">
     			<strong>&#124;<%=reasonCodeName==null?"":"&nbsp;" + reasonCodeName + " -"%><%=reason==null?"":"&nbsp;" + UtilMisc.htmlEscape(reason)%></strong>
     		</span>
      </oscar:oscarPropertiesCheck>
      
	</security:oscarSec>

	  <!-- add one link to caisi Program Management Module -->
	  <caisi:isModuleLoad moduleName="caisi">
                <%-- <a href=# onClick="popupPage(700, 1048,'../PMmodule/ClientManager.do?id=<%=demographic_no%>')" title="Program Management">|P</a>--%>
	  	<a href='../PMmodule/ClientManager.do?id=<%=demographic_no%>' title="Program Management">|P</a>
    </caisi:isModuleLoad>
          <%

      if(isBirthday(monthDay,demBday)){%>
       	&#124; <img src="../images/cake.gif" height="20" alt="Happy Birthday"/>
      <%}%>

      <%String appointment_no=appointment.getId().toString();
      	request.setAttribute("providerPreference", providerPreference);
      %>
      <c:set var="demographic_no" value="<%=demographic_no %>" />
      <c:set var="appointment_no" value="<%=appointment_no %>" />
      
	  <jsp:include page="appointmentFormsLinks.jspf">	  	
	  	<jsp:param value="${demographic_no}" name="demographic_no"/>
	  	<jsp:param value="${appointment_no}" name="appointment_no"/>
	  </jsp:include>

	<oscar:oscarPropertiesCheck property="appt_pregnancy" value="true" defaultVal="false">

		<c:set var="demographicNo" value="<%=demographic_no %>" />
	   <jsp:include page="appointmentPregnancy.jspf" >
	   	<jsp:param value="${demographicNo}" name="demographicNo"/>
	   </jsp:include>

	</oscar:oscarPropertiesCheck>

<% }} %>
        	</font></td>	
        <%
        			}
        		}
        			bFirstFirstR = false;
          	}
            //out.println("<td width='1'>&nbsp;</td></tr>"); give a grid display
            out.println("<td class='noGrid' width='1'></td></tr>"); //no grid display
          }
				%>

          </table> <!-- end table for each provider schedule display -->
<!-- caisi infirmary view extension add fffffffffff-->
</logic:notEqual>
<!-- caisi infirmary view extension add end fffffffffffffff-->

         </td></tr>
          <tr><td class="infirmaryView" ALIGN="center" BGCOLOR="<%=bColor?"#bfefff":"silver"%>">
<!-- caisi infirmary view extension modify fffffffffffffffffff-->
<logic:notEqual name="infirmaryView_isOscar" value="false">

      <% if (isWeekView) { %>
          <b><a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=day%>&view=0&displaymode=day&dboperation=searchappointmentday"><%=formatDate%></a></b>
      <% } else { %>
          <b>
          <input type='button' value="G" name='groupProps' onClick="launchGroupProperties('<%=curProvider_no[nProvider]%>')" title="Manage Group Series Attributes" style="color:black" class="noprint">
          <input type='button' value="<bean:message key="provider.appointmentProviderAdminDay.weekLetter"/>" name='weekview' onClick=goWeekView('<%=curProvider_no[nProvider]%>') title="<bean:message key="provider.appointmentProviderAdminDay.weekView"/>" style="color:black" class="noprint">
          <input type='button' value="<bean:message key="provider.appointmentProviderAdminDay.searchLetter"/>" name='searchview' onClick=goSearchView('<%=curProvider_no[nProvider]%>') title="<bean:message key="provider.appointmentProviderAdminDay.searchView"/>" style="color:black" class="noprint">
          <b><input type='radio' name='flipview' class="noprint" onClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="Flip view"  >
          <a href=# onClick="goZoomView('<%=curProvider_no[nProvider]%>','<%=StringEscapeUtils.escapeJavaScript(curProviderName[nProvider])%>')" onDblClick="goFilpView('<%=curProvider_no[nProvider]%>')" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>" >
          <!--a href="providercontrol.jsp?year=<%=strYear%>&month=<%=strMonth%>&day=<%=strDay%>&view=1&curProvider=<%=curProvider_no[nProvider]%>&curProviderName=<%=curProviderName[nProvider]%>&displaymode=day&dboperation=searchappointmentday" title="<bean:message key="provider.appointmentProviderAdminDay.zoomView"/>"-->
          <%=curProviderName[nProvider]%></a></b>
      <% } %>

          <% if(!userAvail) { %>
          [<bean:message key="provider.appointmentProviderAdminDay.msgNotOnSched"/>]
          <% } %>
</logic:notEqual>
<logic:equal name="infirmaryView_isOscar" value="false">
	<%String prID="1"; %>
	<logic:present name="infirmaryView_programId">
        <%prID=(String)session.getAttribute(SessionConstants.CURRENT_PROGRAM_ID); %>
	</logic:present>
	<logic:iterate id="pb" name="infirmaryView_programBeans" type="org.apache.struts.util.LabelValueBean">
	  	<%if (pb.getValue().equals(prID)) {%>
  		<b><%=pb.getLabel()%></label></b>
		<%} %>
  	</logic:iterate>
</logic:equal>
<!-- caisi infirmary view extension modify end ffffffffffffffffff-->
          </td></tr>

       </table><!-- end table for each provider name -->

            </td>
 <%
   } //end of display team a, etc.

 %>


          </tr>
<% } // end caseload view %>
        </table>        <!-- end table for the whole schedule row display -->




        </td>
      </tr>

      <tr><td colspan="3">
              <table BORDER="0" CELLPADDING="0" CELLSPACING="0" WIDTH="100%" class="noprint">
                  <tr>
                      <td BGCOLOR="ivory" width="60%">
                          <a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=isWeekView ? (day - 7) : (day - 1)%>&view=<%=view == 0 ? "0" : ("1&curProvider=" + request.getParameter("curProvider") + "&curProviderName=" + URLEncoder.encode(request.getParameter("curProviderName"),"UTF-8"))%>&displaymode=day&dboperation=searchappointmentday<%=isWeekView ? "&provider_no=" + provNum : ""%>">
                              &nbsp;&nbsp;<img src="../images/previous.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="<bean:message key="provider.appointmentProviderAdminDay.viewPrevDay"/>" vspace="2"></a>
                          <b><span class="dateAppointment"><% if (isWeekView) {%><bean:message key="provider.appointmentProviderAdminDay.week"/> <%=week%><% } else {%><%=formatDate%><% }%></span></b>
                          <a href="providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=<%=isWeekView ? (day + 7) : (day + 1)%>&view=<%=view == 0 ? "0" : ("1&curProvider=" + request.getParameter("curProvider") + "&curProviderName=" + URLEncoder.encode(request.getParameter("curProviderName"),"UTF-8"))%>&displaymode=day&dboperation=searchappointmentday<%=isWeekView ? "&provider_no=" + provNum : ""%>">
                              <img src="../images/next.gif" WIDTH="10" HEIGHT="9" BORDER="0" ALT="<bean:message key="provider.appointmentProviderAdminDay.viewNextDay"/>" vspace="2">&nbsp;&nbsp;</a>
                          <a id="calendarLinkBottom" href=# onClick ="popupPage(425,430,'../share/CalendarPopup.jsp?urlfrom=../provider/providercontrol.jsp&year=<%=strYear%>&month=<%=strMonth%>&param=<%=URLEncoder.encode("&view=0&displaymode=day&dboperation=searchappointmentday", "UTF-8")%><%=isWeekView ? URLEncoder.encode("&provider_no=" + provNum, "UTF-8") : ""%>')"><bean:message key="global.calendar"/></a></td>
                      <td ALIGN="RIGHT" BGCOLOR="Ivory">
                         | <a href="../logout.jsp"><bean:message key="global.btnLogout"/> &nbsp;</a>
                      </td>
                  </tr>
              </table>
		</td></tr>

	</table>
	</td></tr>
</table>
</body>
<!-- key shortcut hotkey block added by phc -->
<script language="JavaScript">

// popup blocking for the site must be off!
// developed on Windows FF 2, 3 IE 6 Linux FF 1.5
// FF on Mac and Opera on Windows work but will require shift or control with alt and Alpha
// to fire the altKey + Alpha combination - strange

// Modification Notes:
//     event propagation has not been blocked beyond returning false for onkeydown (onkeypress may or may not fire depending)
//     keyevents have not been even remotely standardized so test mods across agents/systems or something will break!
//     use popupOscarRx so that this codeblock can be cut and pasted to appointmentprovideradminmonth.jsp

// Internationalization Notes:
//     underlines should be added to the labels to prompt/remind the user and should correspond to
//     the actual key whose keydown fires, which is also stored in the oscarResources.properties files
//     if you are using the keydown/up event the value stored is the actual key code
//     which, at least with a US keyboard, also is the uppercase utf-8 code, ie A keyCode=65

document.onkeydown=function(e){
	evt = e || window.event;  // window.event is the IE equivalent
	if (evt.altKey) {
		//use (evt.altKey || evt.metaKey) for Mac if you want Apple+A, you will probably want a seperate onkeypress handler in that case to return false to prevent propagation
		switch(evt.keyCode) {
			case <bean:message key="global.adminShortcut"/> : newWindow("../administration/","admin");  return false;  //run code for 'A'dmin
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
			case <bean:message key="global.monthShortcut"/> : window.open("providercontrol.jsp?year=<%=year%>&month=<%=month%>&day=1&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+URLEncoder.encode(request.getParameter("curProviderName"),"UTF-8") )%>&displaymode=month&dboperation=searchappointmentmonth","_self"); return false ;  //run code for Mo'n'th
			case <bean:message key="global.conShortcut"/> : popupOscarRx(625,1024,'../oscarEncounter/IncomingConsultation.do?providerNo=<%=curUser_no%>&userName=<%=URLEncoder.encode(userfirstname+" "+userlastname)%>');  return false;  //run code for c'O'nsultation
			case <bean:message key="global.reportShortcut"/> : popupOscarRx(650,1024,'../report/reportindex.jsp','reportPage');  return false;  //run code for 'R'eports
			case <bean:message key="global.prefShortcut"/> : {
				    <caisi:isModuleLoad moduleName="ticklerplus">
					popupOscarRx(715,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>&caisiBillingPreferenceNotDelete=<%=caisiBillingPreferenceNotDelete%>&new_tickler_warning_window=<%=newticklerwarningwindow%>&default_pmm=<%=default_pmm%>'); //run code for tickler+ 'P'references
					return false;
				    </caisi:isModuleLoad>
			            <caisi:isModuleLoad moduleName="ticklerplus" reverse="true">
					popupOscarRx(715,680,'providerpreference.jsp?provider_no=<%=curUser_no%>&start_hour=<%=startHour%>&end_hour=<%=endHour%>&every_min=<%=everyMin%>&mygroup_no=<%=mygroupno%>'); //run code for 'P'references
					return false;
			            </caisi:isModuleLoad>
			}
			case <bean:message key="global.searchShortcut"/> : popupOscarRx(550,687,'../demographic/search.jsp');  return false;  //run code for 'S'earch
			case <bean:message key="global.dayShortcut"/> : window.open("providercontrol.jsp?year=<%=curYear%>&month=<%=curMonth%>&day=<%=curDay%>&view=<%=view==0?"0":("1&curProvider="+request.getParameter("curProvider")+"&curProviderName="+URLEncoder.encode(request.getParameter("curProviderName"),"UTF-8") )%>&displaymode=day&dboperation=searchappointmentday","_self") ;  return false;  //run code for 'T'oday
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
<!-- end of keycode block -->
<% if (OscarProperties.getInstance().getBooleanProperty("indivica_hc_read_enabled", "true")) { %>
<jsp:include page="/hcHandler/hcHandler.html"/>
<% } %>
</html:html>

<%!public boolean checkRestriction(List<MyGroupAccessRestriction> restrictions, String name) {
                for(MyGroupAccessRestriction restriction:restrictions) {
                        if(restriction.getMyGroupNo().equals(name))
                                return true;
                }
                return false;
        }%>


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

<%@page import="org.apache.commons.lang.time.DateFormatUtils"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%-- @ taglib uri="../WEB-INF/taglibs-log.tld" prefix="log" --%>
<%@page import="org.oscarehr.sharingcenter.SharingCenterUtil"%>
<%@page import="oscar.util.ConversionUtils"%>
<%@page import="org.oscarehr.myoscar.utils.MyOscarLoggedInInfo"%>
<%@page import="org.oscarehr.phr.util.MyOscarUtils"%>
<%@page import="org.oscarehr.util.LoggedInInfo" %>
<%@page import="org.oscarehr.PMmodule.caisi_integrator.ConformanceTestHelper"%>
<%@page import="org.oscarehr.common.dao.DemographicExtDao" %>
<%@page import="org.oscarehr.common.dao.DemographicArchiveDao" %>
<%@page import="org.oscarehr.common.dao.DemographicExtArchiveDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="oscar.OscarProperties" %>
<%@page import="org.oscarehr.common.dao.ScheduleTemplateCodeDao" %>
<%@page import="org.oscarehr.common.model.ScheduleTemplateCode" %>
<%@page import="org.oscarehr.common.dao.WaitingListDao" %>
<%@page import="org.oscarehr.common.dao.WaitingListNameDao" %>
<%@page import="org.oscarehr.common.model.WaitingListName" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@page import="org.oscarehr.common.Gender" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.managers.ProgramManager2" %>
<%@page import="org.oscarehr.PMmodule.model.Program" %>
<%@page import="org.oscarehr.PMmodule.web.GenericIntakeEditAction" %>
<%@page import="org.oscarehr.PMmodule.model.ProgramProvider" %>
<%@page import="org.oscarehr.managers.PatientConsentManager" %>
<%@page import="org.oscarehr.common.model.Consent" %>
<%@page import="org.oscarehr.common.model.ConsentType" %>
<%@page import="org.oscarehr.common.model.EncounterType" %>
<%@page import="org.oscarehr.common.dao.EncounterTypeDao" %>
<%@page import="oscar.util.SuperSiteUtil"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<%

    String demographic$ = request.getParameter("demographic_no") ;
    
    LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
    
    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
    CountryCodeDao ccDAO =  (CountryCodeDao) ctx.getBean("countryCodeDao");
    UserPropertyDAO pref = (UserPropertyDAO) ctx.getBean("UserPropertyDAO");                       
    List<CountryCode> countryList = ccDAO.getAllCountryCodes();

    DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
    DemographicArchiveDao demographicArchiveDao = SpringUtils.getBean(DemographicArchiveDao.class);
    DemographicExtArchiveDao demographicExtArchiveDao = SpringUtils.getBean(DemographicExtArchiveDao.class);
    ScheduleTemplateCodeDao scheduleTemplateCodeDao = SpringUtils.getBean(ScheduleTemplateCodeDao.class);
    WaitingListDao waitingListDao = SpringUtils.getBean(WaitingListDao.class);
    WaitingListNameDao waitingListNameDao = SpringUtils.getBean(WaitingListNameDao.class);
    String privateConsentEnabledProperty = OscarProperties.getInstance().getProperty("privateConsentEnabled");
    boolean privateConsentEnabled = (privateConsentEnabledProperty != null && privateConsentEnabledProperty.equals("true"));
	EncounterTypeDao encounterTypeDao = SpringUtils.getBean(EncounterTypeDao.class);
%>

<security:oscarSec roleName="<%=roleName$%>"
	objectName='<%="_demographic$"+demographic$%>' rights="o"
	reverse="<%=false%>">
<bean:message key="demographic.demographiceditdemographic.accessDenied"/>
<% response.sendRedirect("../acctLocked.html"); 
authed=false;
%>
</security:oscarSec>

<%
if(!authed) {
	return;
}

%>
<%@ page import="java.util.*, java.sql.*, java.net.*,java.text.DecimalFormat, oscar.*, oscar.oscarDemographic.data.ProvinceNames, oscar.oscarWaitingList.WaitingList, oscar.oscarReport.data.DemographicSets,oscar.log.*"%>
<%@ page import="oscar.oscarDemographic.data.*"%>
<%@ page import="oscar.oscarDemographic.pageUtil.Util" %>
<%@ page import="org.springframework.web.context.*,org.springframework.web.context.support.*" %>
<%@ page import="oscar.OscarProperties"%>
<%@ page import="org.oscarehr.common.dao.*,org.oscarehr.common.model.*" %>
<%@ page import="org.oscarehr.common.OtherIdManager" %>
<%@ page import="org.oscarehr.common.web.ContactAction" %>
<%@ page import="org.oscarehr.casemgmt.model.CaseManagementNoteLink" %>
<%@ page import="org.oscarehr.casemgmt.service.CaseManagementManager" %>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.ProfessionalSpecialist" %>
<%@page import="org.oscarehr.common.dao.ProfessionalSpecialistDao" %>
<%@page import="org.oscarehr.common.model.DemographicCust" %>
<%@page import="org.oscarehr.common.dao.DemographicCustDao" %>
<%@page import="org.oscarehr.common.model.Demographic" %>
<%@page import="org.oscarehr.common.dao.DemographicDao" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.managers.DemographicManager" %>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager" %>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao" %>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager" %>
<%@ page import="org.oscarehr.common.dao.ContactSpecialtyDao" %>
<%@ page import="org.oscarehr.common.model.ContactSpecialty" %>
<%
	ProfessionalSpecialistDao professionalSpecialistDao = (ProfessionalSpecialistDao) SpringUtils.getBean("professionalSpecialistDao");
	DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	List<Provider> providers = providerDao.getActiveProviders();
	List<Provider> doctors = providerDao.getActiveProvidersByRole("doctor");
	List<Provider> nurses = providerDao.getActiveProvidersByRole("nurse");
	List<Provider> midwifes = providerDao.getActiveProvidersByRole("midwife");
	
	DemographicManager demographicManager = SpringUtils.getBean(DemographicManager.class);
	ProgramManager2 programManager2 = SpringUtils.getBean(ProgramManager2.class);
    
%>

<jsp:useBean id="providerBean" class="java.util.Properties"	scope="session" />
<% java.util.Properties oscarVariables = OscarProperties.getInstance(); %>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/phr-tag.tld" prefix="phr"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-logic"
	prefix="logic"%>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>

<c:set var="ctx" value="${ pageContext.request.contextPath }" />
<%
	ProgramManager pm = SpringUtils.getBean(ProgramManager.class);
	ProgramDao programDao = (ProgramDao)SpringUtils.getBean("programDao");
    

	String curProvider_no = (String) session.getAttribute("user");
	String demographic_no = request.getParameter("demographic_no") ;
	String apptProvider = request.getParameter("apptProvider");
	String appointment = request.getParameter("appointment");
	String userfirstname = (String) session.getAttribute("userfirstname");
	String userlastname = (String) session.getAttribute("userlastname");

	String str = null;
	int nStrShowLen = 20;
	String prov= (oscarVariables.getProperty("billregion","")).trim().toUpperCase();

	CaseManagementManager cmm = (CaseManagementManager) SpringUtils.getBean("caseManagementManager");
	List<CaseManagementNoteLink> cml = cmm.getLinkByTableId(CaseManagementNoteLink.DEMOGRAPHIC, Long.valueOf(demographic_no));
	boolean hasImportExtra = (cml.size()>0);
	String annotation_display = CaseManagementNoteLink.DISP_DEMO;

	LogAction.addLog((String) session.getAttribute("user"), LogConst.READ, LogConst.CON_DEMOGRAPHIC,  demographic_no , request.getRemoteAddr(),demographic_no);


	OscarProperties oscarProps = OscarProperties.getInstance();

        Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;
	ProvinceNames pNames = ProvinceNames.getInstance();
	Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(Integer.parseInt(demographic_no));

	
	String usSigned = StringUtils.defaultString(apptMainBean.getString(demoExt.get("usSigned")));
    String privacyConsent = StringUtils.defaultString(apptMainBean.getString(demoExt.get("privacyConsent")), "");
	String informedConsent = StringUtils.defaultString(apptMainBean.getString(demoExt.get("informedConsent")), "");
	String IPHISClientNumber = StringUtils.defaultString(apptMainBean.getString(demoExt.get("IPHISClientNumber")),"");	
	String PanoramaClientNumber = StringUtils.defaultString(apptMainBean.getString(demoExt.get("PanoramaClientNumber")),"");	
	String IscisClientNumber = StringUtils.defaultString(apptMainBean.getString(demoExt.get("IscisClientNumber")),"");	
	String OhissClientNumber = StringUtils.defaultString(apptMainBean.getString(demoExt.get("OhissClientNumber")),"");	
	String EpiInfoClientNumber = StringUtils.defaultString(apptMainBean.getString(demoExt.get("EpiInfoClientNumber")),"");	
	String HedgehogClientNumber = StringUtils.defaultString(apptMainBean.getString(demoExt.get("HedgehogClientNumber")),"");	

	
	
	boolean showConsentsThisTime = false;
	boolean showIPHIS = false, showPanorama=false,showIscis=false,showOhiss=false,showEpiInfo=false,showHedgehog=false;
	
    GregorianCalendar now=new GregorianCalendar();
    int curYear = now.get(Calendar.YEAR);
    int curMonth = (now.get(Calendar.MONTH)+1);
    int curDay = now.get(Calendar.DAY_OF_MONTH);
    
	// MARC-HI's Sharing Center
	boolean isSharingCenterEnabled = SharingCenterUtil.isEnabled();

	String programId = (String)session.getAttribute(org.oscarehr.util.SessionConstants.CURRENT_PROGRAM_ID);	
	
	//get all the programs that provider is part of , and patient is part of in order to show the right
	//list of "paper archive" section
	
	AdmissionManager admissionManager = SpringUtils.getBean(AdmissionManager.class);
	List<Program> programDomain = pm.getProgramDomain(curProvider_no);
	List<Program> programList = admissionManager.filterProgramListByCurrentPatientAdmissions(programDomain,Integer.parseInt(demographic_no));
	
	//make a map of programs which require consent
	Map<Integer,Boolean> consentProgramMap = new HashMap<Integer,Boolean>();
	String[] privateConsentPrograms = OscarProperties.getInstance().getProperty("privateConsentPrograms","").split(",");
	for(int x=0;x<privateConsentPrograms.length;x++) {
		if(!StringUtils.isEmpty(privateConsentPrograms[x])) {
			consentProgramMap.put(Integer.parseInt(privateConsentPrograms[x]),true);
		}
	}
	
	// get a list of programs the patient has consented to. 
	if( OscarProperties.getInstance().getBooleanProperty("USE_NEW_PATIENT_CONSENT_MODULE", "true") ) {
	    PatientConsentManager patientConsentManager = SpringUtils.getBean( PatientConsentManager.class );
		pageContext.setAttribute( "consentTypes", patientConsentManager.getConsentTypes() );
		pageContext.setAttribute( "patientConsents", patientConsentManager.getAllConsentsByDemographic( loggedInInfo, Integer.parseInt(demographic_no) ) );
	}
%>



<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.apache.commons.lang.StringUtils"%><html:html locale="true">

<head>
<title><bean:message
	key="demographic.demographiceditdemographic.title" /></title>
<html:base />

<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PATIENT_HEALTH_CARE_TEAM" value="true">
	<link rel="stylesheet" type="text/css" href="${ pageContext.request.contextPath }/css/healthCareTeam.css" />
</oscar:oscarPropertiesCheck>

<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<% if (OscarProperties.getInstance().getBooleanProperty("workflow_enhance", "true")) { %>
<script language="javascript" src="<%=request.getContextPath() %>/hcHandler/hcHandler.js"></script>
<script language="javascript" src="<%=request.getContextPath() %>/hcHandler/hcHandlerUpdateDemographic.js"></script>
<link rel="stylesheet" href="<%=request.getContextPath() %>/hcHandler/hcHandler.css" type="text/css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/demographic/demographiceditdemographic.css" type="text/css" />
<% } %>

<!-- main calendar program -->
<script type="text/javascript" src="../share/calendar/calendar.js"></script>

<!-- language for the calendar -->
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/check_hin.js"></script>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/nhpup_1.1.js"></script>

<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<% if (isMobileOptimized) { %>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
    <link rel="stylesheet" type="text/css" href="../mobile/editdemographicstyle.css">
<% } else { %>
    <link rel="stylesheet" type="text/css" href="../oscarEncounter/encounterStyles.css">
    <link rel="stylesheet" type="text/css" href="../share/css/searchBox.css">
    <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<% } %>
<script language="javascript" type="text/javascript"
	src="../share/javascript/Oscar.js"></script>

<!--popup menu for encounter type -->
<script src="<c:out value="${ctx}"/>/share/javascript/popupmenu.js"
	type="text/javascript"></script>
<script src="<c:out value="${ctx}"/>/share/javascript/menutility.js"
	type="text/javascript"></script>
<script type="text/javascript" src="../share/javascript/prototype.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="master"/>

<script>
var demographicNo='<%=demographic_no%>';
</script>

<script type="text/javascript" src="<%=request.getContextPath() %>/demographic/demographiceditdemographic.js.jsp"></script>

<style>
#pup {
  position:absolute;
  z-index:200; /* aaaalways on top*/
  padding: 3px;
  margin-left: 10px;
  margin-top: 5px;
  width: 350px;
  border: 1px solid black;
  background-color: #777;
  color: white;
  font-size: 0.95em;
}
</style>

</head>
<body onLoad="setfocus(); checkONReferralNo(); formatPhoneNum(); checkRosterStatus2();"
	topmargin="0" leftmargin="0" rightmargin="0" id="demographiceditdemographic">
<%
       Demographic demographic = demographicDao.getDemographic(demographic_no);
       List<DemographicArchive> archives = demographicArchiveDao.findByDemographicNo(Integer.parseInt(demographic_no));
       List<DemographicExtArchive> extArchives = demographicExtArchiveDao.getDemographicExtArchiveByDemoAndKey(Integer.parseInt(demographic_no), "demo_cell");
       
       Admission bedAdmission = admissionManager.getCurrentBedProgramAdmission(demographic.getDemographicNo());
     	Admission communityAdmission = admissionManager.getCurrentCommunityProgramAdmission(demographic.getDemographicNo());
     	List<Admission> serviceAdmissions = admissionManager.getCurrentServiceProgramAdmission(demographic.getDemographicNo());
     	if(serviceAdmissions == null) {
     		serviceAdmissions = new ArrayList<Admission>();
     	}

%>
<table class="MainTable" id="scrollNumber1" name="encounterTable">
	<tr class="MainTableTopRow">
		<td class="MainTableTopRowLeftColumn"><bean:message
			key="demographic.demographiceditdemographic.msgPatientDetailRecord" />
		</td>
		<td class="MainTableTopRowRightColumn">
		<table class="TopStatusBar">
			<tr>
				<td>
				<%
                           java.util.Locale vLocale =(java.util.Locale)session.getAttribute(org.apache.struts.Globals.LOCALE_KEY);
                                //----------------------------REFERRAL DOCTOR------------------------------
                                String rdohip="", rd="", fd="", family_doc = "";

                                String resident="", nurse="", alert="", notes="", midwife="";
                                
                                DemographicCust demographicCust = demographicCustDao.find(Integer.parseInt(demographic_no));
                                if(demographicCust != null) {
                                	resident = demographicCust.getResident() == null ? "" : demographicCust.getResident();
                                	nurse = demographicCust.getNurse() == null ? "" : demographicCust.getNurse();
                                	alert = demographicCust.getAlert() == null ? "" : demographicCust.getAlert();;
                                	midwife = demographicCust.getMidwife() == null ? "" : demographicCust.getMidwife();;
                                	notes = SxmlMisc.getXmlContent(demographicCust.getNotes(),"unotes") ;
                                	
                                	resident = resident==null?"":resident;
                                	nurse = nurse==null?"":nurse;
                                	alert = alert==null?"":alert;	
                                	midwife = midwife==null?"":midwife;
                                	notes = notes==null?"":notes;                               	
                                }

                                // Demographic demographic=demographicDao.getDemographic(demographic_no);

                                String dateString = curYear+"-"+curMonth+"-"+curDay;
                                int age=0, dob_year=0, dob_month=0, dob_date=0;
                                String birthYear="0000", birthMonth="00", birthDate="00";

                                
                                if(demographic==null) {
                                        out.println("failed!!!");
                                } else {
                                        if (true) {
                                                //----------------------------REFERRAL DOCTOR------------------------------
                                                fd=demographic.getFamilyDoctor();
                                                if (fd==null) {
                                                        rd = "";
                                                        rdohip="";
                                                        family_doc = "";
                                                }else{
                                                        rd = SxmlMisc.getXmlContent(StringUtils.trimToEmpty(demographic.getFamilyDoctor()),"rd");
                                                        rd = rd !=null && !rd.equals("null") ? rd : "" ;
                                                        rdohip = SxmlMisc.getXmlContent(StringUtils.trimToEmpty(demographic.getFamilyDoctor()),"rdohip");
                                                        rdohip = rdohip !=null && !rdohip.equals("null") ? rdohip : "" ;
                                                        family_doc = SxmlMisc.getXmlContent(StringUtils.trimToEmpty(demographic.getFamilyDoctor()),"family_doc");
                                                        family_doc = family_doc !=null ? family_doc : "" ;
                                                }
                                                //----------------------------REFERRAL DOCTOR --------------end-----------

                                                if (oscar.util.StringUtils.filled(demographic.getYearOfBirth())) birthYear = StringUtils.trimToEmpty(demographic.getYearOfBirth());
                                                if (oscar.util.StringUtils.filled(demographic.getMonthOfBirth())) birthMonth = StringUtils.trimToEmpty(demographic.getMonthOfBirth());
                                                if (oscar.util.StringUtils.filled(demographic.getDateOfBirth())) birthDate = StringUtils.trimToEmpty(demographic.getDateOfBirth());

                                               	dob_year = Integer.parseInt(birthYear);
                                               	dob_month = Integer.parseInt(birthMonth);
                                               	dob_date = Integer.parseInt(birthDate);
                                                if(dob_year!=0) age=MyDateFormat.getAge(dob_year,dob_month,dob_date);
                        %> <%=demographic.getLastName()%>,
				<%=demographic.getFirstName()%> <%=demographic.getSex()%>
				<%=age%> years &nbsp;
				<oscar:phrverification demographicNo='<%=demographic.getDemographicNo().toString()%>' ><bean:message key="phr.verification.link"/></oscar:phrverification>

				<span style="margin-left: 20px;font-style:italic">
				<bean:message key="demographic.demographiceditdemographic.msgNextAppt"/>: <oscar:nextAppt demographicNo='<%=demographic.getDemographicNo().toString()%>' />
				</span>

				<%
				if (loggedInInfo.getCurrentFacility().isIntegratorEnabled()){%>
        		<jsp:include page="../admin/IntegratorStatus.jspf"/>
        		<%}%>
				
				</td>
			</tr>
		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableLeftColumn" valign="top">
		<table border=0 cellspacing=0 width="100%" id="appt_table">
			<tr class="Header">
				<td style="font-weight: bold"><bean:message key="demographic.demographiceditdemographic.msgAppt"/></td>
			</tr>
			<tr id="appt_hx">
				<td><a
					href='demographiccontrol.jsp?demographic_no=<%=demographic.getDemographicNo()%>&last_name=<%=URLEncoder.encode(demographic.getLastName())%>&first_name=<%=URLEncoder.encode(demographic.getFirstName())%>&orderby=appttime&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=25'><bean:message
					key="demographic.demographiceditdemographic.btnApptHist" /></a>
				</td>
			</tr>

<%
String wLReadonly = "";
WaitingList wL = WaitingList.getInstance();
if(!wL.getFound()){
    wLReadonly = "readonly";
}
if(wLReadonly.equals("")){
%>
			<tr>
				<td><a
					href="../oscarWaitingList/SetupDisplayPatientWaitingList.do?demographic_no=<%=demographic.getDemographicNo()%>">
				<bean:message key="demographic.demographiceditdemographic.msgWaitList"/></a>
				</td>
			</tr>
			</table>
			 <table border=0 cellspacing=0 width="100%">
<%}%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r">
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="admin.admin.billing" /></td>
			</tr>
			<tr>
				<td>
					<% 
					if ("CLINICAID".equals(prov)) 
					{
						%>
							<a href="../billing.do?billRegion=CLINICAID&action=invoice_reports" target="_blank">
							<bean:message key="demographic.demographiceditdemographic.msgInvoiceList"/>
							</a>
						<%
					}
					else if("ON".equals(prov)) 
					{
					%>
						<a href="javascript: function myFunction() {return false; }"
							onClick="popupPage(500,800,'../billing/CA/ON/billinghistory.jsp?demographic_no=<%=demographic.getDemographicNo()%>&last_name=<%=URLEncoder.encode(demographic.getLastName())%>&first_name=<%=URLEncoder.encode(demographic.getFirstName())%>&orderby=appointment_date&displaymode=appt_history&dboperation=appt_history&limit1=0&limit2=10')">
						<bean:message key="demographic.demographiceditdemographic.msgBillHistory"/></a>
					<%
					}
					else
					{
					%>
						<a href="#"
							onclick="popupPage(800,1000,'../billing/CA/BC/billStatus.jsp?lastName=<%=URLEncoder.encode(demographic.getLastName())%>&firstName=<%=URLEncoder.encode(demographic.getFirstName())%>&filterPatient=true&demographicNo=<%=demographic.getDemographicNo()%>');return false;">
						<bean:message key="demographic.demographiceditdemographic.msgInvoiceList"/></a>


						<br/>
						<a  href="javascript: void();" onclick="return !showMenu('2', event);" onmouseover="callEligibilityWebService('../billing/CA/BC/ManageTeleplan.do','returnTeleplanMsg');"><bean:message key="demographic.demographiceditdemographic.btnCheckElig"/></a>
						<div id='menu2' class='menu' onclick='event.cancelBubble = true;' style="width:350px;">
							<span id="search_spinner" ><bean:message key="demographic.demographiceditdemographic.msgLoading"/></span>
							<span id="returnTeleplanMsg"></span>
						</div>
					<%}%>
				</td>
			</tr>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700, 1000, '../billing.do?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("default_view"))%>&hotclick=&appointment_no=0&demographic_name=<%=URLEncoder.encode(demographic.getLastName())%>%2C<%=URLEncoder.encode(demographic.getFirstName())%>&demographic_no=<%=demographic.getDemographicNo()%>&providerview=<%=demographic.getProviderNo()%>&user_no=<%=curProvider_no%>&apptProvider_no=none&appointment_date=<%=dateString%>&start_time=00:00:00&bNewForm=1&status=t');return false;"
					title="<bean:message key="demographic.demographiceditdemographic.msgBillPatient"/>"><bean:message key="demographic.demographiceditdemographic.msgCreateInvoice"/></a></td>
			</tr>
			<%
			if("ON".equals(prov)) {
				String default_view = oscarVariables.getProperty("default_view", "");

				if (!oscarProps.getProperty("clinic_no", "").startsWith("1022")) { // part 2 of quick hack to make Dr. Hunter happy
	%>
				<tr>
					<td><a
						href="javascript: function myFunction() {return false; }"
						onClick="window.open('../billing/CA/ON/specialtyBilling/fluBilling/addFluBilling.jsp?function=demographic&functionid=<%=demographic.getDemographicNo()%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(demographic.getLastName())%>%2C<%=URLEncoder.encode(demographic.getFirstName())%>&hin=<%=URLEncoder.encode(demographic.getHin()!=null?demographic.getHin():"")%><%=URLEncoder.encode(demographic.getVer()!=null?demographic.getVer():"")%>&demo_sex=<%=URLEncoder.encode(demographic.getSex())%>&demo_hctype=<%=URLEncoder.encode(demographic.getHcType()==null?"null":demographic.getHcType())%>&rd=<%=URLEncoder.encode(rd==null?"null":rd)%>&rdohip=<%=URLEncoder.encode(rdohip==null?"null":rdohip)%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(birthYear),Integer.parseInt(birthMonth),Integer.parseInt(birthDate))%>&mrp=<%=demographic.getProviderNo() != null ? demographic.getProviderNo() : ""%>','', 'scrollbars=yes,resizable=yes,width=720,height=500');return false;"
						title='<bean:message key="demographic.demographiceditdemographic.msgAddFluBill"/>'><bean:message key="demographic.demographiceditdemographic.msgFluBilling"/></a></td>
				</tr>
	<%          } %>
				<tr>
					<td><a
						href="javascript: function myFunction() {return false; }"
						onClick="popupS('../billing/CA/ON/billingShortcutPg1.jsp?billRegion=<%=URLEncoder.encode(prov)%>&billForm=<%=URLEncoder.encode(oscarVariables.getProperty("hospital_view", default_view))%>&hotclick=&appointment_no=0&demographic_name=<%=URLEncoder.encode(demographic.getLastName())%>%2C<%=URLEncoder.encode(demographic.getFirstName())%>&demographic_no=<%=demographic.getDemographicNo()%>&providerview=<%=demographic.getProviderNo()%>&user_no=<%=curProvider_no%>&apptProvider_no=none&appointment_date=<%=dateString%>&start_time=00:00:00&bNewForm=1&status=t');return false;"
						title="<bean:message key="demographic.demographiceditdemographic.msgBillPatient"/>"><bean:message key="demographic.demographiceditdemographic.msgHospitalBilling"/></a></td>
				</tr>
				<tr>
					<td><a
						href="javascript: function myFunction() {return false; }"
						onClick="window.open('../billing/CA/ON/addBatchBilling.jsp?demographic_no=<%=demographic.getDemographicNo().toString()%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(demographic.getLastName())%>%2C<%=URLEncoder.encode(demographic.getFirstName())%>&hin=<%=URLEncoder.encode(demographic.getHin()!=null?demographic.getHin():"")%><%=URLEncoder.encode(demographic.getVer()!=null?demographic.getVer():"")%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(birthYear),Integer.parseInt(birthMonth),Integer.parseInt(birthDate))%>','', 'scrollbars=yes,resizable=yes,width=600,height=400');return false;"
						title='<bean:message key="demographic.demographiceditdemographic.msgAddBatchBilling"/>'><bean:message key="demographic.demographiceditdemographic.msgAddBatchBilling"/></a>
					</td>
				</tr>
				<tr>
					<td><a
						href="javascript: function myFunction() {return false; }"
						onClick="window.open('../billing/CA/ON/inr/addINRbilling.jsp?function=demographic&functionid=<%=demographic.getDemographicNo()%>&creator=<%=curProvider_no%>&demographic_name=<%=URLEncoder.encode(demographic.getLastName())%>%2C<%=URLEncoder.encode(demographic.getFirstName())%>&hin=<%=URLEncoder.encode(demographic.getHin()!=null?demographic.getHin():"")%><%=URLEncoder.encode(demographic.getVer()!=null?demographic.getVer():"")%>&dob=<%=MyDateFormat.getStandardDate(Integer.parseInt(birthYear),Integer.parseInt(birthMonth),Integer.parseInt(birthDate))%>','', 'scrollbars=yes,resizable=yes,width=600,height=400');return false;"
						title='<bean:message key="demographic.demographiceditdemographic.msgAddINRBilling"/>'><bean:message key="demographic.demographiceditdemographic.msgAddINR"/></a>
					</td>
				</tr>
				<tr>
					<td><a
						href="javascript: function myFunction() {return false; }"
						onClick="window.open('../billing/CA/ON/inr/reportINR.jsp?provider_no=<%=curProvider_no%>','', 'scrollbars=yes,resizable=yes,width=600,height=600');return false;"
						title='<bean:message key="demographic.demographiceditdemographic.msgINRBilling"/>'><bean:message key="demographic.demographiceditdemographic.msgINRBill"/></a>
					</td>
				</tr>
<%
			}
%>

</security:oscarSec>
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="oscarEncounter.Index.clinicalModules" /></td>
			</tr>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,960,'../oscarEncounter/oscarConsultationRequest/DisplayDemographicConsultationRequests.jsp?de=<%=demographic.getDemographicNo()%>&proNo=<%=demographic.getProviderNo()%>')"><bean:message
					key="demographic.demographiceditdemographic.btnConsultation" /></a></td>
			</tr>

			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupOscarRx(700,1027,'../oscarRx/choosePatient.do?providerNo=<%=curProvider_no%>&demographicNo=<%=demographic_no%>')"><bean:message
					key="global.prescriptions" /></a>
				</td>
			</tr>

			<security:oscarSec roleName="<%=roleName$%>" objectName="_eChart"
				rights="r" reverse="<%=false%>">
                    <special:SpecialEncounterTag moduleName="eyeform" reverse="true">
                    <tr><td>
					<a href="javascript: function myFunction() {return false; }" onClick="popupEChart(710, 1024,encURL);return false;" title="<bean:message key="demographic.demographiceditdemographic.btnEChart"/>">
					<bean:message key="demographic.demographiceditdemographic.btnEChart" /></a>&nbsp;<a style="text-decoration: none;" href="javascript: function myFunction() {return false; }" onmouseover="return !showMenu('1', event);">+</a>
					<div id='menu1' class='menu' onclick='event.cancelBubble = true;'>
					<h3 style='text-align: center'><bean:message key="demographic.demographiceditdemographic.msgEncType"/></h3>
					<br>
					<ul>
						<% for(EncounterType encounterType: encounterTypeDao.findAll()) { %>
							<li><a href="#" 
								onmouseover='this.style.color="black"' 
								onmouseout='this.style.color="white"' 
								onclick="return add2url('<%=encounterType.getValue()%>');">
									<%=encounterType.getValue() %>
							</a><br>
							</li>
						<% } %>
						
						<li><a href="#" onmouseover='this.style.color="black"' onmouseout='this.style.color="white"' onclick="return customReason();"><bean:message key="demographic.demographiceditdemographic.msgCustom"/></a></li>
						<li id="listCustom" style="display: none;"><input id="txtCustom" type="text" size="16" maxlength="32" onkeypress="return grabEnterCustomReason(event);"></li>
						
					</ul>
					</div>
                    </td></tr>
                    </special:SpecialEncounterTag>
                    <special:SpecialEncounterTag moduleName="eyeform">
                    <tr><td>
                            <a href="javascript: function myFunction() {return false; }" onClick="popupEChart(710, 1024,encURL);return false;" title="<bean:message key="demographic.demographiceditdemographic.btnEChart"/>">
                            <bean:message key="demographic.demographiceditdemographic.btnEChart"/></a>
                    </td></tr>
                    </special:SpecialEncounterTag>
				<tr>
					<td><a
						href="javascript: function myFunction() {return false; }"
						onClick="popupPage(700,960,'<c:out value="${ctx}"/>/oscarPrevention/index.jsp?demographic_no=<%=demographic_no%>');return false;">
					<bean:message key="oscarEncounter.LeftNavBar.Prevent" /></a></td>
				</tr>
			</security:oscarSec>
                <plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<%session.setAttribute("encounter_oscar_baseurl",request.getContextPath());
%>
      			<special:SpecialEncounterTag moduleName="eyeform" exactEqual="true">

				<tr><td>
      			<a href="#" style="color: brown;" onclick="popupPage(600,800,'<%=request.getContextPath()%>/mod/specialencounterComp/PatientLog.do?method=editPatientLog&demographicNo=<%=demographic_no%>&providerNo=<%=curProvider_no%>&providerName=<%=URLEncoder.encode( userfirstname+" "+userlastname)%>');return false;">patient log</a>
      			</td>
      			</tr>
      			</special:SpecialEncounterTag>
      			<special:SpecialEncounterTag moduleName="eyeform">
      			<tr><td>
      			<a href="#" style="color: brown;" onclick="popupPage(600,600,'<%=request.getContextPath()%>/mod/specialencounterComp/EyeForm.do?method=eyeFormHistory&demographicNo=<%=demographic_no%>&providerNo=<%=curProvider_no%>&providerName=<%=URLEncoder.encode( userfirstname+" "+userlastname)%>');return false;">eyeForm Hx</a>
      			</td>
      			</tr>
      			<tr>
      			<td>
				<a href="#" style="color: brown;" onclick="popupPage(600,600,'<%=request.getContextPath()%>/mod/specialencounterComp/EyeForm.do?method=chooseField&&demographicNo=<%=demographic_no%>&providerNo=<%=curProvider_no%>&providerName=<%=URLEncoder.encode( userfirstname+" "+userlastname)%>');return false;">Exam Hx</a>
				</td>
				</tr>
				<tr>
				<td>
				<a href="#" style="color: brown;" onclick="popupPage(600,1000,'<%=request.getContextPath()%>/mod/specialencounterComp/ConReportList.do?method=list&&dno=<%=demographic_no%>');return false;">ConReport Hx</a>

      			</td></tr>
      			</special:SpecialEncounterTag>
      		</plugin:hideWhenCompExists>
			<tr>
				<td>
<%if( org.oscarehr.common.IsPropertiesOn.isTicklerPlusEnable() ) {%>
				<a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,1000,'../Tickler.do?filter.demographic_no=<%=demographic_no%>');return false;">
				<bean:message key="global.tickler" /></a>
				<% }else { %>
				<a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,1000,'../tickler/ticklerDemoMain.jsp?demoview=<%=demographic_no%>');return false;">
				<bean:message key="global.tickler" /></a>
				<% } %>
				</td>
			</tr>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popup(700,960,'../oscarMessenger/SendDemoMessage.do?demographic_no=<%=demographic.getDemographicNo()%>','msg')">
				<bean:message key="demographic.demographiceditdemographic.msgSendMsg"/></a></td>
			</tr>
                        <tr>
                            <td> <a href="#" onclick="popup(300,300,'demographicCohort.jsp?demographic_no=<%=demographic.getDemographicNo()%>', 'cohort'); return false;"><bean:message key="demographic.demographiceditdemographic.msgAddPatientSet"/></a>
                            </td>
                        </tr>
                        
           <%
           	if(loggedInInfo.getCurrentFacility().isIntegratorEnabled()) {
           %>             
           <tr>
               <td> <a href="#" onclick="popup(500,500,'../integrator/manage_linked_clients.jsp?demographicId=<%=demographic.getDemographicNo()%>', 'manage_linked_clients'); return false;">Integrator Linking</a>
               </td>
           </tr>
           <% } %>
				<phr:indivoRegistered provider="<%=curProvider_no%>"
					demographic="<%=demographic_no%>">
                                <tr class="Header">
				     <td style="font-weight: bold"><bean:message key="global.personalHealthRecord"/></td>
                                </tr>
					<tr>
						<td>
							<%
								String onclickString="alert('Please login to MyOscar first.')";

								MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
								if (myOscarLoggedInInfo!=null && myOscarLoggedInInfo.isLoggedIn()) onclickString="popupOscarRx(600,900,'../phr/PhrMessage.do?method=createMessage&providerNo="+curProvider_no+"&demographicNo="+demographic_no+"')";
							%>
							<a href="javascript: function myFunction() {return false; }" ONCLICK="<%=onclickString%>"	title="myOscar">
								<bean:message key="demographic.demographiceditdemographic.msgSendMsgPHR"/>
							</a>
						</td>
					</tr>
					<tr>
						<td>
							<a href="" onclick="popup(600, 1000, '<%=request.getContextPath()%>/demographic/viewPhrRecord.do?demographic_no=<%=demographic_no%>', 'viewPatientPHR'); return false;">View PHR Record</a>
						</td>
					</tr>
					<tr>
						<td>
							<%
								if (myOscarLoggedInInfo!=null && myOscarLoggedInInfo.isLoggedIn()) onclickString="popupOscarRx(600,900,'"+request.getContextPath()+"/admin/oscar_myoscar_sync_config_redirect.jsp')";
							%>
							<a href="javascript: function myFunction() {return false; }" ONCLICK="<%=onclickString%>"	title="myOscar">
								<bean:message key="demographic.demographiceditdemographic.MyOscarDataSync"/>
							</a>
						</td>
					</tr>
				</phr:indivoRegistered>
			
<% if (oscarProps.getProperty("clinic_no", "").startsWith("1022")) { // quick hack to make Dr. Hunter happy
%>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,1000,'../form/forwardshortcutname.jsp?formname=AR1&demographic_no=<%=request.getParameter("demographic_no")%>');">AR1</a>
				</td>
			</tr>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(700,1000,'../form/forwardshortcutname.jsp?formname=AR2&demographic_no=<%=request.getParameter("demographic_no")%>');">AR2</a>
				</td>
			</tr>
<% } %>
			<tr class="Header">
				<td style="font-weight: bold"><bean:message
					key="oscarEncounter.Index.clinicalResources" /></td>
			</tr>
                <special:SpecialPlugin moduleName="inboxmnger">
                <tr>
                <td>

                        <a href="#" onClick="window.open('../mod/docmgmtComp/DocList.do?method=list&&demographic_no=<%=demographic_no %>','_blank','resizable=yes,status=yes,scrollbars=yes');return false;">Inbox Manager</a><br>
              	</td>
              	</tr>
                 </special:SpecialPlugin>
                 <special:SpecialPlugin moduleName="inboxmnger" reverse="true">
			<tr><td>
				<a href="javascript: function myFunction() {return false; }"
					onClick="popupPage(710,970,'../dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=demographic.getDemographicNo()%>&curUser=<%=curProvider_no%>')"><bean:message
					key="demographic.demographiceditdemographic.msgDocuments" /></a></td>
			</tr>
                        <%
                        UserProperty upDocumentBrowserLink = pref.getProp(curProvider_no, UserProperty.EDOC_BROWSER_IN_MASTER_FILE);
                        if ( upDocumentBrowserLink != null && upDocumentBrowserLink.getValue() != null && upDocumentBrowserLink.getValue().equals("yes")) {%>
                        <tr><td>
				<a href="javascript: function myFunction() {return false; }"
					onClick="popupPage(710,970,'../dms/documentBrowser.jsp?function=demographic&doctype=lab&functionid=<%=demographic.getDemographicNo()%>&categorykey=Private Documents')"><bean:message
					key="demographic.demographiceditdemographic.msgDocumentBrowser" /></a></td>
			</tr>
                        <%}%>
			<tr>
				<td><a
					href="javascript: function myFunction() {return false; }"
					onClick="popupPage(710,970,'../dms/documentReport.jsp?function=demographic&doctype=lab&functionid=<%=demographic.getDemographicNo()%>&curUser=<%=curProvider_no%>&mode=add')"><bean:message
					key="demographic.demographiceditdemographic.btnAddDocument" /></a></td>
			</tr>
                </special:SpecialPlugin>
                <special:SpecialEncounterTag moduleName="eyeform">
<% String iviewTag=oscarProps.getProperty("iviewTag");

if (iviewTag!=null && !"".equalsIgnoreCase(iviewTag.trim())){
%>
	    		<tr><td>
				<a href='<%=request.getContextPath()%>/mod/specialencounterComp/iviewServlet?method=iview&demoNo=<%=demographic.getDemographicNo()%>&<%=System.currentTimeMillis() %>'>
				<%=iviewTag %></a>
				</td></tr>
<%} %>
		</special:SpecialEncounterTag>
			<tr>
				<td><a
					href="../eform/efmpatientformlist.jsp?demographic_no=<%=demographic_no%>&apptProvider=<%=apptProvider%>&appointment=<%=appointment%>"><bean:message
					key="demographic.demographiceditdemographic.btnEForm" /></a></td>
			</tr>
			<tr>
				<td><a
					href="../eform/efmformslistadd.jsp?demographic_no=<%=demographic_no%>&appointment=<%=appointment%>">
				<bean:message
					key="demographic.demographiceditdemographic.btnAddEForm" /> </a></td>
			</tr>
			
			<% if (isSharingCenterEnabled) { %>
			<!-- Sharing Center Links -->
			<tr>
			  <td><a href="../sharingcenter/networks/sharingnetworks.jsp?demographic_no=<%=demographic_no%>"><bean:message key="sharingcenter.networks.sharingnetworks" /></a></td>
			</tr>
			<tr>
			  <td><a href="../sharingcenter/documents/SharedDocuments.do?demographic_no=<%=demographic_no%>"><bean:message key="sharingcenter.documents.shareddocuments" /></a></td>
			</tr>
			<% } // endif isSharingCenterEnabled %>

		</table>
		</td>
		<td class="MainTableRightColumn" valign="top">
                    <!-- A list used in the mobile version for users to pick which information they'd like to see -->
                    <div id="mobileDetailSections" style="display:<%=(isMobileOptimized)?"block":"none"%>;">
                        <ul class="wideList">
                            <% if (!"".equals(alert)) { %>
                            <li><a style="color:brown" onClick="showHideMobileSections(new Array('alert'))"><bean:message
                                key="demographic.demographiceditdemographic.formAlert" /></a></li>
                            <% } %>
                            <li><a onClick="showHideMobileSections(new Array('demographic'))"><bean:message
                                key="demographic.demographiceditdemographic.msgDemographic"/></a></li>
                            <li><a onClick="showHideMobileSections(new Array('contactInformation'))"><bean:message
                                key="demographic.demographiceditdemographic.msgContactInfo"/></a></li>
                            <li><a onClick="showHideMobileSections(new Array('otherContacts'))"><bean:message
                                key="demographic.demographiceditdemographic.msgOtherContacts"/></a></li>
                            <li><a onClick="showHideMobileSections(new Array('healthInsurance'))"><bean:message
                                key="demographic.demographiceditdemographic.msgHealthIns"/></a></li>
                            <li><a onClick="showHideMobileSections(new Array('patientClinicStatus','clinicStatus'))"><bean:message
                                key="demographic.demographiceditdemographic.msgClinicStatus"/></a></li>
                            <li><a onClick="showHideMobileSections(new Array('notes'))"><bean:message
                                key="demographic.demographiceditdemographic.formNotes" /></a></li>
                        </ul>
                    </div>
		<table border=0 width="100%">
			<tr id="searchTable">
				<td colspan="4"><%-- log:info category="Demographic">Demographic [<%=demographic_no%>] is viewed by User [<%=userfirstname%> <%=userlastname %>]  </log:info --%>
				<jsp:include page="zdemographicfulltitlesearch.jsp"/>
				</td>
			</tr>
			<tr>
				<td>
				<form method="post" name="updatedelete" id="updatedelete"
					action="demographiccontrol.jsp"
					onSubmit="return checkTypeInEdit();"><input type="hidden"
					name="demographic_no"
					value="<%=demographic.getDemographicNo()%>">
				<table width="100%" class="demographicDetail">
					<tr>
						<td class="RowTop">
						<div style="float:left;width:50%;">
						<%
						oscar.oscarDemographic.data.DemographicMerged dmDAO = new oscar.oscarDemographic.data.DemographicMerged();
                            String dboperation = "search_detail";
                            String head = dmDAO.getHead(demographic_no);
                            ArrayList records = dmDAO.getTail(head);
                           
                                    %><a
							href="demographiccontrol.jsp?demographic_no=<%= head %>&displaymode=edit&dboperation=<%= dboperation %>"><%=head%></a>
						<%

                                for (int i=0; i < records.size(); i++){
                                    if (((String) records.get(i)).equals(demographic_no)){
                                        %><%=", "+demographic_no %>
						<%
                                    }else{
                                        %>, <a
							href="demographiccontrol.jsp?demographic_no=<%= records.get(i) %>&displaymode=edit&dboperation=<%= dboperation %>"><%=records.get(i)%></a>
						<%
                                    }
                                }
                            %> ) </span></b>
                            
                            <security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="w">
                            <%
                                                    if( head.equals(demographic_no)) {
                                                    %>
                                                        <a id="editBtn" href="javascript: showHideDetail();"><bean:message key="demographic.demographiceditdemographic.msgEdit"/></a>
                                                        <a id="closeBtn" href="javascript: showHideDetail();" style="display:none;">Close</a>
                                                   <% } %>
                              </security:oscarSec>
                              </div>
                              <div style="float:right;width:50%;text-align:right">
                              <b>Last Edited by</b>: <%=(StringUtils.isNotBlank(demographic.getLastUpdateUser())?providerDao.getProviderName(demographic.getLastUpdateUser()):"N/A" ) %> on <%=(demographic.getLastUpdateDate()!=null)?DateFormatUtils.ISO_DATE_FORMAT.format(demographic.getLastUpdateDate()):"N/A" %>
                              </div>
						</td>
					</tr>
<%
String printEnvelope, printLbl, printAddressLbl, printChartLbl, printSexHealthLbl, printHtmlLbl, printLabLbl;
printEnvelope = printLbl = printAddressLbl = printChartLbl = printSexHealthLbl = printHtmlLbl = printLabLbl = null;

if(oscarProps.getProperty("new_label_print") != null && oscarProps.getProperty("new_label_print").equals("true")) {

	printEnvelope = "printEnvelope.jsp?demos=";
	printLbl = "printDemoLabel.jsp?demographic_no=";
	printAddressLbl = "printAddressLabel.jsp?demographic_no=";
	printChartLbl = "printDemoChartLabel.jsp?demographic_no=";
	printSexHealthLbl = "printDemoChartLabel.jsp?labelName=SexualHealthClinicLabel&demographic_no=";
	printHtmlLbl = "demographiclabelprintsetting.jsp?demographic_no=";
	printLabLbl = "printClientLabLabel.jsp?demographic_no=";

}else{

	printEnvelope = "../report/GenerateEnvelopes.do?demos=";
	printLbl = "printDemoLabelAction.do?demographic_no=";
	printAddressLbl = "printDemoAddressLabelAction.do?demographic_no=";
	printChartLbl = "printDemoChartLabelAction.do?demographic_no=";
	printSexHealthLbl = "printDemoChartLabelAction.do?labelName=SexualHealthClinicLabel&demographic_no=";
	printHtmlLbl = "demographiclabelprintsetting.jsp?demographic_no=";
	printLabLbl = "printClientLabLabelAction.do?demographic_no=";

}

%>
<%if (OscarProperties.getInstance().getProperty("workflow_enhance") != null && OscarProperties.getInstance().getProperty("workflow_enhance").equals("true")) {%>
					
					<tr bgcolor="#CCCCFF">
                        <td colspan="4">
                        <table border="0" width="100%" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="30%" valign="top">
                                                             
                                <input type="hidden" name="displaymode" value="Update Record">
                                
                                <input type="hidden" name="dboperation" value="update_record">
                            
                            <security:oscarSec roleName="<%=roleName$%>" objectName="_demographicExport" rights="r" reverse="<%=false%>">
                                <input type="button" value="<bean:message key="demographic.demographiceditdemographic.msgExport"/>"
                                    onclick="window.open('demographicExport.jsp?demographicNo=<%=demographic.getDemographicNo()%>');">
                             </security:oscarSec>     
                                </td>
                                <td width="30%" align='center' valign="top">
                                <% if (OscarProperties.getInstance().getBooleanProperty("workflow_enhance", "true")) { %>
									<span style="position: relative; float: right; font-style: italic; background: black; color: white; padding: 4px; font-size: 12px; border-radius: 3px;">
										<span class="_hc_status_icon _hc_status_success"></span>Ready for Card Swipe
									</span>
								<% } %>	
                                <% if (!OscarProperties.getInstance().getBooleanProperty("workflow_enhance", "true")) { %>
								<span id="swipeButton" style="display: inline;"> 
                                    <input type="button" name="Button"
                                    value="<bean:message key="demographic.demographiceditdemographic.btnSwipeCard"/>"
                                    onclick="window.open('zdemographicswipe.jsp','', 'scrollbars=yes,resizable=yes,width=600,height=300, top=360, left=0')">
                                </span> <!--input type="button" name="Button" value="<bean:message key="demographic.demographiceditdemographic.btnSwipeCard"/>" onclick="javascript:window.alert('Health Card Number Already Inuse');"-->
                                <% } %>
                                </td>
                                <td width="40%" align='right' valign="top">
								<input type="button" size="110" name="Button"
								    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFEnvelope"/>"
								    onclick="popupPage(400,700,'<%=printEnvelope%><%=demographic.getDemographicNo()%>');return false;">
								<input type="button" size="110" name="Button"
								    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFLabel"/>"
								    onclick="popupPage(400,700,'<%=printLbl%><%=demographic.getDemographicNo()%>');return false;">
								<input type="button" size="110" name="Button"
								    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFAddressLabel"/>"
								    onclick="popupPage(400,700,'<%=printAddressLbl%><%=demographic.getDemographicNo()%>');return false;">
								<input type="button" size="110" name="Button"
								    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFChartLabel"/>"
								    onclick="popupPage(400,700,'<%=printChartLbl%><%=demographic.getDemographicNo()%>');return false;">
								    <%
										if(oscarVariables.getProperty("showSexualHealthLabel", "false").equals("true")) {
									%>
								<input type="button" size="110" name="Button"
								    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePublicHealthLabel"/>"
								    onclick="popupPage(400,700,'<%=printSexHealthLbl%><%=demographic.getDemographicNo()%>');return false;">
								    <% } %>
								<input type="button" name="Button" size="110"
								    value="<bean:message key="demographic.demographiceditdemographic.btnPrintLabel"/>"
								    onclick="popupPage(600,800,'<%=printHtmlLbl%><%=demographic.getDemographicNo()%>');return false;">
								<input type="button" size="110" name="Button"
								    value="<bean:message key="demographic.demographiceditdemographic.btnClientLabLabel"/>"
								    onclick="popupPage(400,700,'<%=printLabLbl%><%=demographic.getDemographicNo()%>');return false;">
                                </td>
                              </tr>
                        </table>
                        </td>
                    </tr>
					
					
					<%} %>
					
					<tr>
						<td class="lightPurple"><!---new-->
						<div style="display: inline;" id="viewDemographics2">
						<div class="demographicWrapper">
						<div class="leftSection">
						<div class="demographicSection" id="demographic">
						<h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.msgDemographic"/></h3>
						<%
							for (String key : demoExt.keySet()) {
							    if (key.endsWith("_id")) {
						%>
						<input type="hidden" name="<%=key%>" value="<%=StringEscapeUtils.escapeHtml(StringUtils.trimToEmpty(demoExt.get(key)))%>"/>
						<%
							    }
							}
						%>
						<ul>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formLastName" />:</span>
                                                        <span class="info"><%=demographic.getLastName()%></span>
                                                    </li>
                                                    <li><span class="label">
							<bean:message
                                                                key="demographic.demographiceditdemographic.formFirstName" />:</span>
                                                        <span class="info"><%=demographic.getFirstName()%></span>
							</li>
                                                    <li><span class="label"><bean:message key="demographic.demographiceditdemographic.msgDemoTitle"/>:</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demographic.getTitle())%></span>
							</li>
                                                    <li><span class="label"><bean:message key="demographic.demographiceditdemographic.formSex" />:</span>
                                                        <span class="info"><%=demographic.getSex()%></span>
                                                    </li>
                                                    <li><span class="label"><bean:message key="demographic.demographiceditdemographic.msgDemoAge"/>:</span>
                                                        <span class="info"><%=age%>&nbsp;(<bean:message
                                                            key="demographic.demographiceditdemographic.formDOB" />: <%=birthYear%>-<%=birthMonth%>-<%=birthDate%>)
                                                        </span>
                                                    </li>
                                                    <li><span class="label"><bean:message key="demographic.demographiceditdemographic.msgDemoLanguage"/>:</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demographic.getOfficialLanguage())%></span>
                                                    </li>
						<% if (demographic.getCountryOfOrigin() != null &&  !demographic.getCountryOfOrigin().equals("") && !demographic.getCountryOfOrigin().equals("-1")){
                                                        CountryCode countryCode = ccDAO.getCountryCode(demographic.getCountryOfOrigin());
                                                        if  (countryCode != null){
                                                    %>
                                                <li><span class="label"><bean:message key="demographic.demographiceditdemographic.msgCountryOfOrigin"/>:</span>
                                                    <span class="info"><%=countryCode.getCountryName() %></span>
                                                </li><%      }
                                                    }
                                                %>
						<% String sp_lang = demographic.getSpokenLanguage();
						   if (sp_lang!=null && sp_lang.length()>0) { %>
                                               <li><span class="label"><bean:message key="demographic.demographiceditdemographic.msgSpokenLang"/>:</span>
                                                   <span class="info"><%=sp_lang%></span>
							</li>
						<% } %>
						
						<% String aboriginal = StringUtils.trimToEmpty(demoExt.get("aboriginal"));
						   if (aboriginal!=null && aboriginal.length()>0) { %>
                                               <li><span class="label"><bean:message key="demographic.demographiceditdemographic.aboriginal"/>:</span>
                                                   <span class="info"><%=aboriginal%></span>
							</li>
						<% }
						  if (oscarProps.getProperty("EXTRA_DEMO_FIELDS") !=null){
                                              String fieldJSP = oscarProps.getProperty("EXTRA_DEMO_FIELDS");
                                              fieldJSP+= "View.jsp";
                                            %>
							<jsp:include page="<%=fieldJSP%>">
								<jsp:param name="demo" value="<%=demographic_no%>" />
							</jsp:include>
							<%}%>

						</ul>
						</div>



<%-- TOGGLE NEW CONTACTS UI --%>
<%if(!OscarProperties.getInstance().isPropertyActive("NEW_CONTACTS_UI")) { %>
					
						

	
						<div class="demographicSection" id="otherContacts">
						<h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.msgOtherContacts"/>: <b><a
							href="javascript: function myFunction() {return false; }"
							onClick="popup(700,960,'AddAlternateContact.jsp?demo=<%=demographic.getDemographicNo()%>','AddRelation')">
						<bean:message key="demographic.demographiceditdemographic.msgAddRelation"/><!--i18n--></a></b></h3>
						<ul>
							<%DemographicRelationship demoRelation = new DemographicRelationship();
                                          List relList = demoRelation.getDemographicRelationshipsWithNamePhone(loggedInInfo, demographic.getDemographicNo().toString(), loggedInInfo.getCurrentFacility().getId());
                                          for (int reCounter = 0; reCounter < relList.size(); reCounter++){
                                             HashMap relHash = (HashMap) relList.get(reCounter);
                                             String dNo = (String)relHash.get("demographicNo");
                                             String workPhone = demographicManager.getDemographicWorkPhoneAndExtension(loggedInInfo, Integer.valueOf(dNo));
                                             
                                             
                                             String formattedWorkPhone = (workPhone != null && workPhone.length()>0 && !workPhone.equals("null") )?"  W:"+workPhone:"";
                                             String sdb = relHash.get("subDecisionMaker") == null?"":((Boolean) relHash.get("subDecisionMaker")).booleanValue()?"<span title=\"SDM\" >/SDM</span>":"";
                                             String ec = relHash.get("emergencyContact") == null?"":((Boolean) relHash.get("emergencyContact")).booleanValue()?"<span title=\"Emergency Contact\">/EC</span>":"";
											 String masterLink = "<a target=\"demographic"+dNo+"\" href=\"" + request.getContextPath() + "/demographic/demographiccontrol.jsp?demographic_no="+dNo+"&displaymode=edit&dboperation=search_detail\">M</a>";
											 String encounterLink = "<a target=\"encounter"+dNo+"\" href=\"javascript: function myFunction() {return false; }\" onClick=\"popupEChart(710,1024,'" + request.getContextPath() + "/oscarEncounter/IncomingEncounter.do?demographicNo="+dNo+"&providerNo="+loggedInInfo.getLoggedInProviderNo()+"&appointmentNo=&curProviderNo=&reason=&appointmentDate=&startTime=&status=&userName="+URLEncoder.encode( userfirstname+" "+userlastname)+"&curDate="+curYear+"-"+curMonth+"-"+curDay+"');return false;\">E</a>";												 
                                          %>
							<li><span class="label"><%=relHash.get("relation")%><%=sdb%><%=ec%>:</span>
                            	<span class="info"><%=relHash.get("lastName")%>, <%=relHash.get("firstName")%>, H:<%=relHash.get("phone")== null?"":relHash.get("phone")%><%=formattedWorkPhone%> <%=masterLink%> <%=encounterLink %></span>
                            </li>
							<%}%>

						</ul>
						</div>

						<% } else { %>

						<div class="demographicSection" id="otherContacts2">
						<h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.msgOtherContacts"/>: <b><a
							href="javascript: function myFunction() {return false; }"
							onClick="popup(700,960,'Contact.do?method=manage&demographic_no=<%=demographic.getDemographicNo()%>','ManageContacts')">
						<bean:message key="demographic.demographiceditdemographic.msgManageContacts"/><!--i18n--></a></b></h3>
						<ul>
						<%
							ContactDao contactDao = (ContactDao)SpringUtils.getBean("contactDao");
							List<DemographicContact> dContacts = demographicManager.getDemographicContacts(loggedInInfo, demographic.getDemographicNo());
							dContacts = ContactAction.fillContactNames(dContacts);
							for(DemographicContact dContact:dContacts) {
								String sdm = (dContact.getSdm()!=null && dContact.getSdm().equals("true"))?"<span title=\"SDM\" >/SDM</span>":"";
								String ec = (dContact.getEc()!=null && dContact.getEc().equals("true"))?"<span title=\"Emergency Contact\" >/EC</span>":"";
								
								String masterLink1 = "";
								if(dContact.getType() == DemographicContact.TYPE_DEMOGRAPHIC) {
									masterLink1 = "<a href=\""+request.getContextPath()+"/demographic/demographiccontrol.jsp?demographic_no="+dContact.getContactId()+"&displaymode=edit&dboperation=search_detail\">M</a>";
								} else if(dContact.getType() == DemographicContact.TYPE_CONTACT) {
									if("professional".equals(dContact.getCategory())) {
										masterLink1 = "<a onClick=\"popupPage(500,800,'"+request.getContextPath()+"/demographic/Contact.do?method=editProContact&pcontact.id="+dContact.getContactId()+"')\" href=\"javascript:void(0)\">C</a>";
									} else {
										masterLink1 = "<a onClick=\"popupPage(500,800,'"+request.getContextPath()+"/demographic/Contact.do?method=editContact&contact.id="+dContact.getContactId()+"')\" href=\"javascript:void(0)\">C</a>";
									}
								}
						%>

								<li><span class="label"><%=dContact.getRole()%>:</span>
                                                            <span class="info"><%=dContact.getContactName() %><%=sdm%><%=ec%> <%=masterLink1%></span>
                                                        </li>

						<%  } %>

						</ul>
						</div>

						<% } %>
						<div class="demographicSection" id="clinicStatus">
						<h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.msgClinicStatus"/> (<a href="#" onclick="popup(1000, 650, 'EnrollmentHistory.jsp?demographicNo=<%=demographic_no%>', 'enrollmentHistory'); return false;"><bean:message key="demographic.demographiceditdemographic.msgEnrollmentHistory"/></a>)</h3>
						<ul>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formRosterStatus" />:</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demographic.getRosterStatus())%></span>
                                                    </li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.DateJoined" />:</span>
                                                        <span class="info"><%=MyDateFormat.getMyStandardDate(demographic.getRosterDate())%></span>
                                                    </li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.RosterTerminationDate" />:</span>
                                                        <span class="info"><%=MyDateFormat.getMyStandardDate(demographic.getRosterTerminationDate())%></span>
                                                    </li>
<%if (null != demographic.getRosterTerminationDate()) { %>
													<li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.RosterTerminationReason" />:</span>
                                                        <span class="info"><%=Util.rosterTermReasonProperties.getReasonByCode(demographic.getRosterTerminationReason()) %></span>
                                                    </li>
<%} %>
                                                    <li><span class="label"><bean:message
								key="demographic.demographiceditdemographic.formPatientStatus" />:</span>
                                                        <span class="info">
							<%
String PatStat = demographic.getPatientStatus();
String Dead = "DE";
String Inactive = "IN";

if ( Dead.equals(PatStat) ) {%>
							<b style="color: #FF0000;"><%=demographic.getPatientStatus()%></b>
							<%} else if (Inactive.equals(PatStat) ){%>
							<b style="color: #0000FF;"><%=demographic.getPatientStatus()%></b>
							<%} else {%>
                                                            <%=demographic.getPatientStatus()%>
							<%}%>
                                                        </span>
							</li>
							 <li><span class="label">
							 	<bean:message key="demographic.demographiceditdemographic.PatientStatusDate" />:</span>
                                <span class="info">
                                <%
                                String tmpDate="";
                                if(demographic.getPatientStatusDate ()!= null) {
                                	tmpDate = org.apache.commons.lang.time.DateFormatUtils.ISO_DATE_FORMAT.format(demographic.getPatientStatusDate());
                                }
                                %>
                                <%=tmpDate%></span>
							</li>
							
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formChartNo" />:</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demographic.getChartNo())%></span>
							</li>
							<% if (oscarProps.isPropertyActive("meditech_id")) { %>
                                                    <li><span class="label">Meditech ID:</span>
                                                        <span class="info"><%=OtherIdManager.getDemoOtherId(demographic_no, "meditech_id")%></span>
							</li>
<% } %>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.cytolNum" />:</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demoExt.get("cytolNum"))%></span></li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formDateJoined1" />:</span>
							<span class="info"><%=MyDateFormat.getMyStandardDate(demographic.getDateJoined())%></span>
                                                    </li><li>
                                                        <span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formEndDate" />:</span>
                                                        <span class="info"><%=MyDateFormat.getMyStandardDate(demographic.getEndDate())%></span>
							</li>
						</ul>
						</div>

						<div class="demographicSection" id="alert">
						<h3>&nbsp;<bean:message
							key="demographic.demographiceditdemographic.formAlert" /></h3>
                                                <b style="color: brown;"><%=alert%></b>
						&nbsp;
						</div>

						<div class="demographicSection" id="rxInteractionWarningLevel">
						<h3>&nbsp;<bean:message
							key="demographic.demographiceditdemographic.rxInteractionWarningLevel" /></h3>
                              <%
                              	String warningLevel = demoExt.get("rxInteractionWarningLevel");
                              	if(warningLevel==null) warningLevel="0";
	          					String warningLevelStr = "Not Specified";
	          					if(warningLevel.equals("1")) {warningLevelStr="Low";}
	          					if(warningLevel.equals("2")) {warningLevelStr="Medium";}
	          					if(warningLevel.equals("3")) {warningLevelStr="High";}
	          					if(warningLevel.equals("4")) {warningLevelStr="None";}
                              %>
						&nbsp;
						
						</div>


<%-- TOGGLE PUBLIC HEALTH IDS --%>
<%if(OscarProperties.getInstance().isPropertyActive("PublicHealthIDsEnabled")) {
%>
			<div class="demographicSection" id="PublicHealthIDs">
				<h3><bean:message key="demographic.demographiceditdemographic.PublicHealthID"/></h3>
<%
	ProgramProvider pp = programManager2.getCurrentProgramInDomain(loggedInInfo,loggedInInfo.getLoggedInProviderNo());
	
	showIPHIS = shouldShowForThisProvider(OscarProperties.getInstance().getProperty("IPHISid","").split(","),pp);
	showPanorama = shouldShowForThisProvider(OscarProperties.getInstance().getProperty("Panorama_id","").split(","),pp);
	showIscis = shouldShowForThisProvider(OscarProperties.getInstance().getProperty("Iscis_id","").split(","),pp);
	showOhiss = shouldShowForThisProvider(OscarProperties.getInstance().getProperty("Ohiss_id","").split(","),pp);
	showEpiInfo = shouldShowForThisProvider(OscarProperties.getInstance().getProperty("EpiInfo_id","").split(","),pp);
	showHedgehog = shouldShowForThisProvider(OscarProperties.getInstance().getProperty("Hedgehog_id","").split(","),pp);
	
	boolean showedAnId = false;
	
	if(showIPHIS) {
		showedAnId=true;
		%><ul><li><span class="label"><bean:message key="demographic.demographiceditdemographic.IPHIS"/>:</span>
		<span class="info"><%=IPHISClientNumber%></span></li></ul><%	
	}
	
	if(showPanorama) {
		showedAnId=true;
		%><ul><li><span class="label"><bean:message key="demographic.demographiceditdemographic.Panorama"/>:</span>
		<span class="info"><%=PanoramaClientNumber%></span></li></ul><%	
	}
	
	if(showIscis) {
		showedAnId=true;
		%><ul><li><span class="label"><bean:message key="demographic.demographiceditdemographic.ISCIS"/>:</span>
		<span class="info"><%=IscisClientNumber%></span></li></ul><%	
	}
	
	if(showOhiss) {
		showedAnId=true;
		%><ul><li><span class="label"><bean:message key="demographic.demographiceditdemographic.OHISS"/>:</span>
		<span class="info"><%=OhissClientNumber%></span></li></ul><%	
	}
	
	if(showEpiInfo) {
		showedAnId=true;
		%><ul><li><span class="label"><bean:message key="demographic.demographiceditdemographic.EpiInfo"/>:</span>
		<span class="info"><%=EpiInfoClientNumber%></span></li></ul><%	
	}
	
	if(showHedgehog) {
		showedAnId=true;
		%><ul><li><span class="label"><bean:message key="demographic.demographiceditdemographic.Hedgehog"/>:</span>
		<span class="info"><%=HedgehogClientNumber%></span></li></ul><%	
	}
	
	
	if(!showedAnId){%><span class="label">N/A</span><%}%>
	
	</div>
<%}%>
<%-- END TOGGLE PUBLIC HEALTH IDS --%>
						
						<div class="demographicSection" id="paperChartIndicator">

						<h3>&nbsp;<bean:message
							key="demographic.demographiceditdemographic.paperChartIndicator" /></h3>
							<%
								String archived = demoExt.get("paper_chart_archived");
								String archivedStr = "", archivedDate = "", archivedProgram = "";
								
								if("YES".equals(archived)) {
									archivedStr="Yes";
								}
								if("NO".equals(archived)) {
									archivedStr="No";
								}
                      			if(demoExt.get("paper_chart_archived_date") != null) {
                      				archivedDate = demoExt.get("paper_chart_archived_date");
                      			}
                      			if(demoExt.get("paper_chart_archived_program") != null) {
                      				archivedProgram = demoExt.get("paper_chart_archived_program");
                      			}
							%>
                           <ul>
	                          <li><span class="label"><bean:message key="demographic.demographiceditdemographic.paperChartIndicator.archived"/>:</span>
	                              <span class="info"><%=archivedStr %></span>
	                          </li>
	                          <li><span class="label"><bean:message key="demographic.demographiceditdemographic.paperChartIndicator.dateArchived"/>:</span>
	                              <span class="info"><%=archivedDate %></span>
	                          </li>
	                          <li><span class="label"><bean:message key="demographic.demographiceditdemographic.paperChartIndicator.programArchived"/>:</span>
	                              <span class="info"><%=archivedProgram %></span>
	                          </li>                     
	                       </ul>
	                       
	                       						
						
						</div>
						
<%-- TOGGLE PRIVACY CONSENTS --%>						
<oscar:oscarPropertiesCheck property="privateConsentEnabled" value="true">

		<div class="demographicSection" id="consent">
				<h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.consent" /></h3>
                             
					<ul>
					
						<%
							ProgramProvider pp2 = programManager2.getCurrentProgramInDomain(loggedInInfo,loggedInInfo.getLoggedInProviderNo());
		
							if(pp2 != null) {
								for(int x=0;x<privateConsentPrograms.length;x++) {
									if(privateConsentPrograms[x].equals(pp2.getProgramId().toString())) {
										showConsentsThisTime=true;
									}
								}
							}
						
						if(showConsentsThisTime) { %>

	                          <li><span class="label"><bean:message key="demographic.demographiceditdemographic.privacyConsent"/>:</span>
	                              <span class="info"><%=privacyConsent %></span>
	                          </li>
	                          <li><span class="label"><bean:message key="demographic.demographiceditdemographic.informedConsent"/>:</span>
	                              <span class="info"><%=informedConsent %></span>
	                          </li>
	                          <li><span class="label"><bean:message key="demographic.demographiceditdemographic.usConsent"/>:</span>
	                              <span class="info"><%=usSigned %></span>
	                          </li>
	                          
						
						<% } %>
    
<%-- ENABLE THE NEW PATIENT CONSENT MODULE --%>
<oscar:oscarPropertiesCheck property="USE_NEW_PATIENT_CONSENT_MODULE" value="true" >
		                          	
                          		<c:forEach items="${ patientConsents }" var="patientConsent" >
                          		<li>
                          			<span class="popup label" onmouseover="nhpup.popup(${ patientConsent.consentType.description },{'width':350} );" >
										<c:out value="${ patientConsent.consentType.name }" />
									</span>
                          			
                          			<c:choose>
										<c:when test="${ patientConsent.optout }">
											<span class="info" style="color:red;"> Opted Out:<c:out value="${ patientConsent.optoutDate }" /></span>
										</c:when>
															
										<c:otherwise>
											<span class="info" style="color:green;">Consented:<c:out value="${ patientConsent.consentDate }" /></span>
										</c:otherwise>				
									</c:choose>		
                          				
                          		</li>	
                          		</c:forEach>	                              	
</oscar:oscarPropertiesCheck>
<%-- END ENABLE NEW PATIENT CONSENT MODULE --%>

	                       </ul>						
						</div>
						
</oscar:oscarPropertiesCheck>	                      
<%-- END TOGGLE ALL PRIVACY CONSENTS --%>

						</div>
						<div class="rightSection">
						<div class="demographicSection" id="contactInformation">
						<h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.msgContactInfo"/></h3>
						<ul>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formPhoneH" />(<span class="popup"  onmouseover="nhpup.popup(homePhoneHistory,{'width':350});" >History</span>):</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demographic.getPhone())%> <%=StringUtils.trimToEmpty(demoExt.get("hPhoneExt"))%></span>
							</li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formPhoneW" />(<span class="popup"  onmouseover="nhpup.popup(workPhoneHistory,{'width':350});" >History</span>):</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demographic.getPhone2())%> <%=StringUtils.trimToEmpty(demoExt.get("wPhoneExt"))%></span>
							</li>
	                        						<li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formPhoneC" />(<span class="popup"  onmouseover="nhpup.popup(cellPhoneHistory,{'width':350});" >History</span>):</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demoExt.get("demo_cell"))%></span></li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographicaddrecordhtm.formPhoneComment" />:</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demoExt.get("phoneComment"))%></span></li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formAddr" />(<span class="popup"  onmouseover="nhpup.popup(addressHistory,{'width':350});" >History</span>):</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demographic.getAddress())%></span>
							</li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formCity" />:</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demographic.getCity())%></span>
                                                    </li>
                                                    <li><span class="label">
							<% if(oscarProps.getProperty("demographicLabelProvince") == null) { %>
							<bean:message
								key="demographic.demographiceditdemographic.formProcvince" /> <% } else {
			                                  out.print(oscarProps.getProperty("demographicLabelProvince"));
                                                                               } %>:</span>
                                                        <span class="info"><%=StringUtils.trimToEmpty(demographic.getProvince())%></span></li>
                                                    <li><span class="label">
							<% if(oscarProps.getProperty("demographicLabelPostal") == null) { %>
							<bean:message
								key="demographic.demographiceditdemographic.formPostal" /> <% } else {
			                                  out.print(oscarProps.getProperty("demographicLabelPostal"));
                                                                               } %>:</span>
                                                       <span class="info"><%=StringUtils.trimToEmpty(demographic.getPostal())%></span></li>

                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formEmail" />:</span>
                                                        <span class="info"><%=demographic.getEmail()!=null? demographic.getEmail() : ""%></span>
							</li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formNewsLetter" />:</span>
                                                        <span class="info"><%=demographic.getNewsletter()!=null? demographic.getNewsletter() : "Unknown"%></span>
							</li>
						</ul>
						</div>

						<div class="demographicSection" id="healthInsurance">
						<h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.msgHealthIns"/></h3>
						<ul>
                                                    <li><span class="label"><bean:message
								key="demographic.demographiceditdemographic.formHin" />:</span>
                                                                <span class="info"><%=StringUtils.trimToEmpty(demographic.getHin())%>
							&nbsp; <%=StringUtils.trimToEmpty(demographic.getVer())%></span>
							</li>
                                                    <li><span class="label"><bean:message
								key="demographic.demographiceditdemographic.formHCType" />:</span>
                                                        <span class="info"><%=demographic.getHcType()==null?"":demographic.getHcType() %></span>
							</li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formEFFDate" />:</span>
                                                        <span class="info"><%=MyDateFormat.getMyStandardDate(demographic.getEffDate())%></span>
                                                    </li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formHCRenewDate" />:</span>
                                                        <span class="info"><%=MyDateFormat.getMyStandardDate(demographic.getHcRenewDate())%></span>
                                                    </li>
						</ul>
						</div>

<%-- TOGGLE WORKFLOW_ENHANCE - SHOWS PATIENTS INTERNAL PROVIDERS AND RELATED SCHEDULE AVAIL --%>

<oscar:oscarPropertiesCheck value="true" property="workflow_enhance">
<%--if (OscarProperties.getInstance().getProperty("workflow_enhance")!=null && OscarProperties.getInstance().getProperty("workflow_enhance").equals("true")) {--%>
						
						<div class="demographicSection">
                        <h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.msgInternalProviders"/></h3>
                        <div style="background-color: #EEEEFF;">
                        <ul>
			<%!	// ===== functions for quick appointment booking =====

				// convert hh:nn:ss format to elapsed minutes (from 00:00:00)
				int timeStrToMins (String timeStr) {
					String[] temp = timeStr.split(":");
					return Integer.parseInt(temp[0])*60+Integer.parseInt(temp[1]);
				}
			%>
			<%	// ===== quick appointment booking =====
				// database access object, data objects for looking things up
				
				
				
				String[] twoLetterDate = {"", "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
						
				// build templateMap, which maps template codes to their associated duration
				Map<String, String> templateMap = new HashMap<String, String>();
				for(ScheduleTemplateCode stc : scheduleTemplateCodeDao.findTemplateCodes()) {
					templateMap.put(String.valueOf(stc.getCode()),stc.getDuration());
				}
				

				// build list of providers associated with this patient 
				Map<String, Map<String, Map<String,String>>> provMap = new HashMap<String, Map<String, Map<String,String>>>();
				if (demographic != null) {
					provMap.put("doctor", new HashMap<String, Map<String,String>>());
					provMap.get("doctor").put("prov_no", new HashMap<String, String>());
					provMap.get("doctor").get("prov_no").put("no", demographic.getProviderNo());
				}
				if (StringUtils.isNotEmpty(providerBean.getProperty(resident,""))) {
					provMap.put("prov1", new HashMap<String, Map<String,String>>());
					provMap.get("prov1").put("prov_no", new HashMap<String, String>());
					provMap.get("prov1").get("prov_no").put("no", resident);
				}
				if (StringUtils.isNotEmpty(providerBean.getProperty(midwife,""))) {
					provMap.put("prov2", new HashMap<String, Map<String,String>>());
					provMap.get("prov2").put("prov_no", new HashMap<String, String>());
					provMap.get("prov2").get("prov_no").put("no", midwife); 
				}
				if (StringUtils.isNotEmpty(providerBean.getProperty(nurse,""))) {
					provMap.put("prov3", new HashMap<String, Map<String,String>>());
					provMap.get("prov3").put("prov_no", new HashMap<String, String>());
					provMap.get("prov3").get("prov_no").put("no", nurse);
				}
				
				// precompute all data for the providers associated with this patient
				for (String thisProv : provMap.keySet()) {
					
					String thisProvNo = provMap.get(thisProv).get("prov_no").get("no");

					// starting tomorrow, look for available appointment slots
					Calendar qApptCal = new GregorianCalendar();
					qApptCal.add(Calendar.DATE, 1);
					int numDays = 0;
					int maxLookahead = 90;

					while ((numDays < 5) && (maxLookahead > 0)) {
						int qApptYear = qApptCal.get(Calendar.YEAR);
						int qApptMonth = (qApptCal.get(Calendar.MONTH)+1);
						int qApptDay = qApptCal.get(Calendar.DAY_OF_MONTH);
						String qApptWkDay = twoLetterDate[qApptCal.get(Calendar.DAY_OF_WEEK)];
                        String qCurDate = qApptYear+"-"+qApptMonth+"-"+qApptDay;
						
						// get timecode string template associated with this day, number of minutes each slot represents
						ScheduleTemplateDao dao = SpringUtils.getBean(ScheduleTemplateDao.class); 
						List<Object> timecodeResult = dao.findTimeCodeByProviderNo2(thisProvNo, ConversionUtils.fromDateString(qCurDate));

						// if theres a template on this day, continue
                        if (timecodeResult != null && !timecodeResult.isEmpty()) {

                       	String timecode = StringUtils.trimToEmpty(String.valueOf(timecodeResult.get(0)));
                       	
                  	    int timecodeInterval = 1440/timecode.length();

						// build schedArr, which has 1s where template slots are
                   		int[] schedArr = new int[timecode.length()];
                   		String schedChar;
                   		for (int i=0; i<timecode.length(); i++) {
                           		schedChar = ""+timecode.charAt(i);
                           		if (!schedChar.equals("_")) {
									if (templateMap.get(""+timecode.charAt(i)) != null) {
                                     	schedArr[i] = 1;
									}
                           		}
                   		}

						// get list of appointments on this day
						int start_index, end_index;
						OscarAppointmentDao apptDao = SpringUtils.getBean(OscarAppointmentDao.class);
						// put 0s in schedArr where appointments are
						for(Appointment appt : apptDao.findByProviderAndDayandNotStatuses(thisProvNo, ConversionUtils.fromDateString(qCurDate), new String[] {"N", "C"})) {
							start_index = timeStrToMins(StringUtils.trimToEmpty(ConversionUtils.toTimeString(appt.getStartTime())))/timecodeInterval;
							end_index = timeStrToMins(StringUtils.trimToEmpty(ConversionUtils.toTimeString(appt.getEndTime())))/timecodeInterval;
							
							// very late appts may push us past the time range we care about 
							// trying to invalidate these times will lead to a ArrayIndexOutOfBoundsException
							// fix this so we stay within the bounds of schedArr
							if (end_index > (timecode.length()-1)) {
								end_index = timecode.length()-1;
							}

							// protect against the dual case as well
							if (start_index < 0) {
								start_index = 0;
							} 
							
							// handle appts of duration longer than template interval
							for (int i=start_index; i<=end_index; i++) {
								schedArr[i] = 0;
							}
						}

						// list slots that can act as start times for appointments of template specified length
						boolean enoughRoom;
						boolean validDay = false;
						int templateDuration, startHour, startMin;
						String startTimeStr, endTimeStr, sortDateStr;
						String timecodeChar;
						for (int i=0; i<timecode.length(); i++) {
							if (schedArr[i] == 1) {
								enoughRoom = true;
								timecodeChar = ""+timecode.charAt(i);
								templateDuration = Integer.parseInt(templateMap.get(timecodeChar));
								for (int n=0; n<templateDuration/timecodeInterval; n++) {
									if (((i+n) < (schedArr.length-1)) && (schedArr[i+n] != 1)) {
										enoughRoom=false;
									}
								}
								if (enoughRoom) {
									validDay = true;
									sortDateStr = qApptYear+"-"+String.format("%02d",qApptMonth)+"-"+String.format("%02d",qApptDay);
									if (!provMap.get(thisProv).containsKey(sortDateStr+","+qApptWkDay+" "+qApptMonth+"-"+qApptDay)) {
										provMap.get(thisProv).put(sortDateStr+","+qApptWkDay+" "+qApptMonth+"-"+qApptDay, new HashMap<String, String>());
									}
									startHour = i*timecodeInterval / 60;
									startMin = i*timecodeInterval % 60;
									startTimeStr = String.format("%02d",startHour)+":"+String.format("%02d",startMin);
									endTimeStr = String.format("%02d",startHour)+":"+String.format("%02d",startMin+timecodeInterval-1);

									provMap.get(thisProv).get(sortDateStr+","+qApptWkDay+" "+qApptMonth+"-"+qApptDay).put(startTimeStr+","+timecodeChar, "../appointment/addappointment.jsp?demographic_no="+demographic.getDemographicNo()+"&name="+URLEncoder.encode(demographic.getLastName()+","+demographic.getFirstName())+"&provider_no="+thisProvNo+"&bFirstDisp=true&year="+qApptYear+"&month="+qApptMonth+"&day="+qApptDay+"&start_time="+startTimeStr+"&end_time="+endTimeStr+"&duration="+templateDuration+"&search=true");
								}
							}
						}
						
						if (validDay) {
							numDays++;
						}
						}
						
						// look at the next day
						qApptCal.add(Calendar.DATE, 1);
						maxLookahead--;
					} 
				}
			%>
                            <% if (demographic.getProviderNo()!=null) { %>
                            <li>
<% if(oscarProps.getProperty("demographicLabelDoctor") != null) { out.print(oscarProps.getProperty("demographicLabelDoctor","")); } else { %>
                            <bean:message
                                key="demographic.demographiceditdemographic.formDoctor" />
                            <% } %>: <b><%=providerBean.getProperty(demographic.getProviderNo(),"")%></b>
                        <% // ===== quick appointment booking for doctor =====
                        if (provMap.get("doctor") != null) {
				%><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%
				boolean firstBar = true;
                                ArrayList<String> sortedDays = new ArrayList(provMap.get("doctor").keySet());
                                Collections.sort(sortedDays);
                                for (String thisDate : sortedDays) {
                                        if (!thisDate.equals("prov_no")) {
                                                if (!firstBar) {%>|<%}; firstBar = false;
	                                        String[] thisDateArr = thisDate.split(",");
						String thisDispDate = thisDateArr[1];
						%>
                                                <a style="text-decoration: none;" href="#" onclick="return !showAppt('_doctor_<%=thisDateArr[0]%>', event);"><b><%=thisDispDate%></b></a>
                                                <div id='menu_doctor_<%=thisDateArr[0]%>' class='menu' onclick='event.cancelBubble = true;' >
                                                <h3 style='text-align: center; color: black;'>Available Appts. (<%=thisDispDate%>)</h3>
						<ul>
                                                <%
                                                ArrayList<String> sortedTimes = new ArrayList(provMap.get("doctor").get(thisDate).keySet());
                                                Collections.sort(sortedTimes);
                                                for (String thisTime : sortedTimes) {
							String[] thisTimeArr = thisTime.split(",");
                                                        %><li>[<%=thisTimeArr[1]%>] <a href="#" onClick="popupPage(400,780,'<%=provMap.get("doctor").get(thisDate).get(thisTime) %>');return false;"><%= thisTimeArr[0] %></a></li><%
                                                }
                                                %></ul></div><%                                        }
                                }
                        }
                        %>
                            </li>
                            <% } if (StringUtils.isNotEmpty(providerBean.getProperty(resident,""))) { %>
                            <li>Alt. Provider 1: <b><%=providerBean.getProperty(resident,"")%></b>
                        <% // ===== quick appointment booking for prov1 =====
                        if (provMap.get("prov1") != null) {
				%><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%
				boolean firstBar = true;
                                ArrayList<String> sortedDays = new ArrayList(provMap.get("prov1").keySet());
                                Collections.sort(sortedDays);
                                for (String thisDate : sortedDays) {
                                        if (!thisDate.equals("prov_no")) {
                                                if (!firstBar) {%>|<%}; firstBar = false;
	                                        String[] thisDateArr = thisDate.split(",");
						String thisDispDate = thisDateArr[1];
						%>
                                                <a style="text-decoration: none;" href="#" onclick="return !showAppt('_prov1_<%=thisDateArr[0]%>', event);"><b><%=thisDispDate%></b></a>
                                                <div id='menu_prov1_<%=thisDateArr[0]%>' class='menu' onclick='event.cancelBubble = true;'>
                                                <h3 style='text-align: center; color: black;'>Available Appts. (<%=thisDispDate%>)</h3> 
                                                <ul>
                                                <%
                                                ArrayList<String> sortedTimes = new ArrayList(provMap.get("prov1").get(thisDate).keySet());
                                                Collections.sort(sortedTimes);
                                                for (String thisTime : sortedTimes) {
							String[] thisTimeArr = thisTime.split(",");
                                                        %><li>[<%=thisTimeArr[1]%>] <a href="#" onClick="popupPage(400,780,'<%=provMap.get("prov1").get(thisDate).get(thisTime) %>');return false;"><%= thisTimeArr[0] %></a></li><%
                                                }
                                                %></ul></div><%
                                        }
                                }
                        }
                        %>
                            </li>
                            <% } if (StringUtils.isNotEmpty(providerBean.getProperty(midwife,""))) { %>
                            <li>Alt. Provider 2: <b><%=providerBean.getProperty(midwife,"")%></b>
                        <% // ===== quick appointment booking for prov2 =====
                        if (provMap.get("prov2") != null) {
							%><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%
							boolean firstBar = true;
                            	ArrayList<String> sortedDays = new ArrayList(provMap.get("prov2").keySet());
                            	Collections.sort(sortedDays);
                            	   for (String thisDate : sortedDays) {
                                        if (!thisDate.equals("prov_no")) {
                                                if (!firstBar) {%>|<%}; firstBar = false;
	                                        String[] thisDateArr = thisDate.split(",");
						String thisDispDate = thisDateArr[1];
						%>
                                                <a style="text-decoration: none;" href="#" onclick="return !showAppt('_prov2_<%=thisDateArr[0]%>', event);"><b><%=thisDispDate%></b></a>
                                                <div id='menu_prov2_<%=thisDateArr[0]%>' class='menu' onclick='event.cancelBubble = true;'>
                                                <h3 style='text-align: center; color: black;'>Available Appts. (<%=thisDispDate%>)</h3> 
                                                <ul>
                                                <%
                                                ArrayList<String> sortedTimes = new ArrayList(provMap.get("prov2").get(thisDate).keySet());
                                                Collections.sort(sortedTimes);
                                                for (String thisTime : sortedTimes) {
							String[] thisTimeArr = thisTime.split(",");
                                                        %><li>[<%=thisTimeArr[1]%>] <a href="#" onClick="popupPage(400,780,'<%=provMap.get("prov2").get(thisDate).get(thisTime) %>');return false;"><%= thisTimeArr[0] %></a></li><%
                                                }
                                                %></ul></div><%
                                        }
                                }
                        }
                        %>
                            </li>
                            <% } if (StringUtils.isNotEmpty(providerBean.getProperty(nurse,""))) { %>
                            <li>Alt. Provider 3: <b><%=providerBean.getProperty(nurse,"")%></b>
                        <% // ===== quick appointment booking for prov3 =====
                        if (provMap.get("prov3") != null) {
							%><br />&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%
							boolean firstBar = true;
                                ArrayList<String> sortedDays = new ArrayList(provMap.get("prov3").keySet());
                                Collections.sort(sortedDays);
                                for (String thisDate : sortedDays) {
                                        if (!thisDate.equals("prov_no")) {
                                                if (!firstBar) {%>|<%}; firstBar = false;
	                                        String[] thisDateArr = thisDate.split(",");
						String thisDispDate = thisDateArr[1];
						%>
                                                <a style="text-decoration: none;" href="#" onclick="return !showAppt('_prov3_<%=thisDateArr[0]%>', event);"><b><%=thisDispDate%></b></a>
                                                <div id='menu_prov3_<%=thisDateArr[0]%>' class='menu' onclick='event.cancelBubble = true;'>
                                                <h3 style='text-align: center; color: black;'>Available Appts. (<%=thisDispDate%>)</h3> 
                                                <ul>
                                                <%
                                                ArrayList<String> sortedTimes = new ArrayList(provMap.get("prov3").get(thisDate).keySet());
                                                Collections.sort(sortedTimes);
                                                for (String thisTime : sortedTimes) {
							String[] thisTimeArr = thisTime.split(",");
                                                        %><li>[<%=thisTimeArr[1]%>] <a href="#" onClick="popupPage(400,780,'<%=provMap.get("prov3").get(thisDate).get(thisTime) %>');return false;"><%= thisTimeArr[0] %></a></li><%
                                                }
                                                %></ul></div><%
                                        }
                                }
                        }
                        %>
                            </li>
                            <% } %> 
                         </ul>
                         </div>
                         </div>
						
						<%--} --%>
</oscar:oscarPropertiesCheck>
<%-- END TOGGLE WORKFLOW_ENHANCE --%>

<%-- AUTHOR DENNIS WARREN O/A COLCAMEX RESOURCES --%>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PATIENT_HEALTH_CARE_TEAM" value="true">
	<jsp:include page="/demographic/displayHealthCareTeam.jsp"> 	    		
		<jsp:param name="demographicNo" value="<%= demographic_no %>" />
	</jsp:include>
</oscar:oscarPropertiesCheck>
	<%-- TOGGLE OFF PATIENT CLINIC STATUS --%>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PATIENT_CLINIC_STATUS" value="true">
						
						<div class="demographicSection" id="patientClinicStatus">
						<h3>&nbsp;<bean:message key="demographic.demographiceditdemographic.msgPatientClinicStatus"/></h3>
						<ul>
                                                    <li><span class="label">
							<% if(oscarProps.getProperty("demographicLabelDoctor") != null) { out.print(oscarProps.getProperty("demographicLabelDoctor","")); } else { %>
							<bean:message
								key="demographic.demographiceditdemographic.formDoctor" />
                                                    <% } %>:</span><span class="info">
                                                    <%if(demographic != null && demographic.getProviderNo() != null){%>	
                                                           <%=providerBean.getProperty(demographic.getProviderNo(),"")%>
                                                    <%}%>
                                                    </span>
							</li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formNurse" />:</span><span class="info"><%=providerBean.getProperty(nurse == null ? "" : nurse,"")%></span>
							</li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formMidwife" />:</span><span class="info"><%=providerBean.getProperty(midwife == null ? "" : midwife,"")%></span>
							</li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formResident" />:</span>
                                                        <span class="info"><%=providerBean.getProperty(resident==null ? "" : resident,"")%></span></li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formRefDoc" />:</span><span class="info"><%=rd%></span>
							</li>
                                                    <li><span class="label"><bean:message
                                                            key="demographic.demographiceditdemographic.formRefDocNo" />:</span><span class="info"><%=rdohip%></span>
							</li>
						</ul>
						</div>
						
</oscar:oscarPropertiesCheck>

	<%-- END TOGGLE OFF PATIENT CLINIC STATUS --%>
	
<%-- END AUTHOR DENNIS WARREN O/A COLCAMEX RESOURCES --%>


						<div class="demographicSection" id="notes">
						<h3>&nbsp;<bean:message
							key="demographic.demographiceditdemographic.formNotes" /></h3>

                                                    <%=notes%>&nbsp;
<%if (hasImportExtra) { %>
		                <a href="javascript:void(0);" title="Extra data from Import" onclick="window.open('../annotation/importExtra.jsp?display=<%=annotation_display %>&amp;table_id=<%=demographic_no %>&amp;demo=<%=demographic_no %>','anwin','width=400,height=250');">
		                    <img src="../images/notes.gif" align="right" alt="Extra data from Import" height="16" width="13" border="0"> </a>
<%} %>


						</div>
						
<%-- TOGGLED OFF PROGRAM ADMISSIONS --%>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PROGRAM_ADMISSIONS" value="true">						
						<div class="demographicSection" id="programs">
						<h3>&nbsp;Programs</h3>
						<ul>
                         <li><span class="label">Bed:</span><span class="info"><%=bedAdmission != null?bedAdmission.getProgramName():"N/A" %></span></li>
                         <%
                         for(Admission adm:serviceAdmissions) {
                        	 %>
                        		 <li><span class="label">Service:</span><span class="info"><%=adm.getProgramName()%></span></li>
                         
                        	 <%
                         }
                         %>
						</ul>
                                                  
						</div>
</oscar:oscarPropertiesCheck>
<%-- TOGGLED OFF PROGRAM ADMISSIONS --%>

						</div>
						</div>

						<% // customized key
						if(oscarVariables.getProperty("demographicExt") != null) {
							String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
						%>
						<div class="demographicSection" id="special">
						<h3>&nbsp;Special</h3>
						<% 	for(int k=0; k<propDemoExt.length; k++) {%> <%=propDemoExt[k]+": <b>" + StringUtils.trimToEmpty(demoExt.get(propDemoExt[k].replace(' ', '_'))) +"</b>"%>
						&nbsp;<%=((k+1)%4==0&&(k+1)<propDemoExt.length)?"<br>":"" %> <% 	} %>
						</div>
						<% } %>
						</div>

						<!--newEnd-->

						<table width="100%" bgcolor="#EEEEFF" border=0
							id="editDemographic" style="display: none;">
							<tr>
								<td align="right"
									title='<%=demographic.getDemographicNo()%>'>
								<b><bean:message
									key="demographic.demographiceditdemographic.formLastName" />: </b></td>
								<td align="left"><input type="text" name="last_name" <%=getDisabled("last_name")%>
									size="30" value="<%=StringEscapeUtils.escapeHtml(demographic.getLastName())%>"
									onBlur="upCaseCtrl(this)"></td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formFirstName" />:
								</b></td>
								<td align="left"><input type="text" name="first_name" <%=getDisabled("first_name")%>
									size="30" value="<%=StringEscapeUtils.escapeHtml(demographic.getFirstName())%>"
									onBlur="upCaseCtrl(this)"></td>
							</tr>
							<tr>
							  <td align="right"><b><bean:message key="demographic.demographiceditdemographic.msgDemoLanguage"/>: </b> </td>
							    <td align="left">
					<% String lang = oscar.util.StringUtils.noNull(demographic.getOfficialLanguage()); %>
								<select name="official_lang" <%=getDisabled("official_lang")%>>
								    <option value="English" <%=lang.equals("English")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgEnglish"/></option>
								    <option value="French" <%=lang.equals("French")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgFrench"/></option>
								    <option value="Other" <%=lang.equals("Other")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.optOther"/></option>
								</select>
								</td>
							  <td align="right"> <b><bean:message key="demographic.demographiceditdemographic.msgDemoTitle"/>: </b></td>
							    <td align="left">
					<%
						String title = demographic.getTitle();
						if(title == null) {
							title="";
						}
					%>
								<select name="title" <%=getDisabled("title")%>>
									<option value="" <%=title.equals("")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgNotSet"/></option>
									<option value="DR" <%=title.equalsIgnoreCase("DR")?"selected":""%> ><bean:message key="demographic.demographicaddrecordhtm.msgDr"/></option>
								    <option value="MS" <%=title.equalsIgnoreCase("MS")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgMs"/></option>
								    <option value="MISS" <%=title.equalsIgnoreCase("MISS")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgMiss"/></option>
								    <option value="MRS" <%=title.equalsIgnoreCase("MRS")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgMrs"/></option>
								    <option value="MR" <%=title.equalsIgnoreCase("MR")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgMr"/></option>
								    <option value="MSSR" <%=title.equalsIgnoreCase("MSSR")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgMssr"/></option>
								    <option value="PROF" <%=title.equalsIgnoreCase("PROF")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgProf"/></option>
								    <option value="REEVE" <%=title.equalsIgnoreCase("REEVE")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgReeve"/></option>
								    <option value="REV" <%=title.equalsIgnoreCase("REV")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgRev"/></option>
								    <option value="RT_HON" <%=title.equalsIgnoreCase("RT_HON")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgRtHon"/></option>
								    <option value="SEN" <%=title.equalsIgnoreCase("SEN")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgSen"/></option>
								    <option value="SGT" <%=title.equalsIgnoreCase("SGT")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgSgt"/></option>
								    <option value="SR" <%=title.equalsIgnoreCase("SR")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgSr"/></option>
								    <option value="DR" <%=title.equalsIgnoreCase("DR")?"selected":""%> ><bean:message key="demographic.demographiceditdemographic.msgDr"/></option>
								</select>
							    </td>
							</tr>
							<tr>
                                <td align="right">
							    <b><bean:message key="demographic.demographiceditdemographic.msgSpoken"/>: </b>
							    </td>
							    <td>
								<%String spokenLang = oscar.util.StringUtils.noNull(demographic.getSpokenLanguage()); %>
									<select name="spoken_lang" <%=getDisabled("spoken_lang")%>>
<%for (String splang : Util.spokenLangProperties.getLangSorted()) { %>
                                        <option value="<%=splang %>" <%=spokenLang.equals(splang)?"selected":"" %>><%=splang %></option>
<%} %>
									</select>
							    </td>
							    <td colspan="2">&nbsp;</td>
							</tr>

							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formAddr" />: </b></td>
								<td align="left"><input type="text" name="address" <%=getDisabled("address")%>
									size="30" value="<%=StringUtils.trimToEmpty(demographic.getAddress())%>">
								</td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formCity" />: </b></td>
								<td align="left"><input type="text" name="city" size="30" <%=getDisabled("city")%>
									value="<%=StringEscapeUtils.escapeHtml(StringUtils.trimToEmpty(demographic.getCity()))%>"></td>
							</tr>

							<tr valign="top">
								<td align="right"><b> <% if(oscarProps.getProperty("demographicLabelProvince") == null) { %>
								<bean:message
									key="demographic.demographiceditdemographic.formProcvince" /> <% } else {
                                  out.print(oscarProps.getProperty("demographicLabelProvince"));
                              	 } %> : </b></td>
								<td align="left">
								
								<% String province = demographic.getProvince(); %> 
								<select name="province" style="width: 200px" <%=getDisabled("province")%>>
									<option value="OT"
										<%=(province==null || province.equals("OT") || province.equals("") || province.length() > 2)?" selected":""%>>Other</option>
									<% if (pNames.isDefined()) {
                                       for (ListIterator li = pNames.listIterator(); li.hasNext(); ) {
                                           String pr2 = (String) li.next(); %>
									<option value="<%=pr2%>"
										<%=pr2.equals(province)?" selected":""%>><%=li.next()%></option>
									<% }//for %>
									<% } else { %>
									<option value="AB" <%="AB".equals(province)?" selected":""%>>AB-Alberta</option>
									<option value="BC" <%="BC".equals(province)?" selected":""%>>BC-British Columbia</option>
									<option value="MB" <%="MB".equals(province)?" selected":""%>>MB-Manitoba</option>
									<option value="NB" <%="NB".equals(province)?" selected":""%>>NB-New Brunswick</option>
									<option value="NL" <%="NL".equals(province)?" selected":""%>>NL-Newfoundland Labrador</option>
									<option value="NT" <%="NT".equals(province)?" selected":""%>>NT-Northwest Territory</option>
									<option value="NS" <%="NS".equals(province)?" selected":""%>>NS-Nova Scotia</option>
									<option value="NU" <%="NU".equals(province)?" selected":""%>>NU-Nunavut</option>
									<option value="ON" <%="ON".equals(province)?" selected":""%>>ON-Ontario</option>
									<option value="PE" <%="PE".equals(province)?" selected":""%>>PE-Prince Edward Island</option>
									<option value="QC" <%="QC".equals(province)?" selected":""%>>QC-Quebec</option>
									<option value="SK" <%="SK".equals(province)?" selected":""%>>SK-Saskatchewan</option>
									<option value="YT" <%="YT".equals(province)?" selected":""%>>YT-Yukon</option>
									<option value="US" <%="US".equals(province)?" selected":""%>>US resident</option>
									<option value="US-AK" <%="US-AK".equals(province)?" selected":""%>>US-AK-Alaska</option>
									<option value="US-AL" <%="US-AL".equals(province)?" selected":""%>>US-AL-Alabama</option>
									<option value="US-AR" <%="US-AR".equals(province)?" selected":""%>>US-AR-Arkansas</option>
									<option value="US-AZ" <%="US-AZ".equals(province)?" selected":""%>>US-AZ-Arizona</option>
									<option value="US-CA" <%="US-CA".equals(province)?" selected":""%>>US-CA-California</option>
									<option value="US-CO" <%="US-CO".equals(province)?" selected":""%>>US-CO-Colorado</option>
									<option value="US-CT" <%="US-CT".equals(province)?" selected":""%>>US-CT-Connecticut</option>
									<option value="US-CZ" <%="US-CZ".equals(province)?" selected":""%>>US-CZ-Canal Zone</option>
									<option value="US-DC" <%="US-DC".equals(province)?" selected":""%>>US-DC-District Of Columbia</option>
									<option value="US-DE" <%="US-DE".equals(province)?" selected":""%>>US-DE-Delaware</option>
									<option value="US-FL" <%="US-FL".equals(province)?" selected":""%>>US-FL-Florida</option>
									<option value="US-GA" <%="US-GA".equals(province)?" selected":""%>>US-GA-Georgia</option>
									<option value="US-GU" <%="US-GU".equals(province)?" selected":""%>>US-GU-Guam</option>
									<option value="US-HI" <%="US-HI".equals(province)?" selected":""%>>US-HI-Hawaii</option>
									<option value="US-IA" <%="US-IA".equals(province)?" selected":""%>>US-IA-Iowa</option>
									<option value="US-ID" <%="US-ID".equals(province)?" selected":""%>>US-ID-Idaho</option>
									<option value="US-IL" <%="US-IL".equals(province)?" selected":""%>>US-IL-Illinois</option>
									<option value="US-IN" <%="US-IN".equals(province)?" selected":""%>>US-IN-Indiana</option>
									<option value="US-KS" <%="US-KS".equals(province)?" selected":""%>>US-KS-Kansas</option>
									<option value="US-KY" <%="US-KY".equals(province)?" selected":""%>>US-KY-Kentucky</option>
									<option value="US-LA" <%="US-LA".equals(province)?" selected":""%>>US-LA-Louisiana</option>
									<option value="US-MA" <%="US-MA".equals(province)?" selected":""%>>US-MA-Massachusetts</option>
									<option value="US-MD" <%="US-MD".equals(province)?" selected":""%>>US-MD-Maryland</option>
									<option value="US-ME" <%="US-ME".equals(province)?" selected":""%>>US-ME-Maine</option>
									<option value="US-MI" <%="US-MI".equals(province)?" selected":""%>>US-MI-Michigan</option>
									<option value="US-MN" <%="US-MN".equals(province)?" selected":""%>>US-MN-Minnesota</option>
									<option value="US-MO" <%="US-MO".equals(province)?" selected":""%>>US-MO-Missouri</option>
									<option value="US-MS" <%="US-MS".equals(province)?" selected":""%>>US-MS-Mississippi</option>
									<option value="US-MT" <%="US-MT".equals(province)?" selected":""%>>US-MT-Montana</option>
									<option value="US-NC" <%="US-NC".equals(province)?" selected":""%>>US-NC-North Carolina</option>
									<option value="US-ND" <%="US-ND".equals(province)?" selected":""%>>US-ND-North Dakota</option>
									<option value="US-NE" <%="US-NE".equals(province)?" selected":""%>>US-NE-Nebraska</option>
									<option value="US-NH" <%="US-NH".equals(province)?" selected":""%>>US-NH-New Hampshire</option>
									<option value="US-NJ" <%="US-NJ".equals(province)?" selected":""%>>US-NJ-New Jersey</option>
									<option value="US-NM" <%="US-NM".equals(province)?" selected":""%>>US-NM-New Mexico</option>
									<option value="US-NU" <%="US-NU".equals(province)?" selected":""%>>US-NU-Nunavut</option>
									<option value="US-NV" <%="US-NV".equals(province)?" selected":""%>>US-NV-Nevada</option>
									<option value="US-NY" <%="US-NY".equals(province)?" selected":""%>>US-NY-New York</option>
									<option value="US-OH" <%="US-OH".equals(province)?" selected":""%>>US-OH-Ohio</option>
									<option value="US-OK" <%="US-OK".equals(province)?" selected":""%>>US-OK-Oklahoma</option>
									<option value="US-OR" <%="US-OR".equals(province)?" selected":""%>>US-OR-Oregon</option>
									<option value="US-PA" <%="US-PA".equals(province)?" selected":""%>>US-PA-Pennsylvania</option>
									<option value="US-PR" <%="US-PR".equals(province)?" selected":""%>>US-PR-Puerto Rico</option>
									<option value="US-RI" <%="US-RI".equals(province)?" selected":""%>>US-RI-Rhode Island</option>
									<option value="US-SC" <%="US-SC".equals(province)?" selected":""%>>US-SC-South Carolina</option>
									<option value="US-SD" <%="US-SD".equals(province)?" selected":""%>>US-SD-South Dakota</option>
									<option value="US-TN" <%="US-TN".equals(province)?" selected":""%>>US-TN-Tennessee</option>
									<option value="US-TX" <%="US-TX".equals(province)?" selected":""%>>US-TX-Texas</option>
									<option value="US-UT" <%="US-UT".equals(province)?" selected":""%>>US-UT-Utah</option>
									<option value="US-VA" <%="US-VA".equals(province)?" selected":""%>>US-VA-Virginia</option>
									<option value="US-VI" <%="US-VI".equals(province)?" selected":""%>>US-VI-Virgin Islands</option>
									<option value="US-VT" <%="US-VT".equals(province)?" selected":""%>>US-VT-Vermont</option>
									<option value="US-WA" <%="US-WA".equals(province)?" selected":""%>>US-WA-Washington</option>
									<option value="US-WI" <%="US-WI".equals(province)?" selected":""%>>US-WI-Wisconsin</option>
									<option value="US-WV" <%="US-WV".equals(province)?" selected":""%>>US-WV-West Virginia</option>
									<option value="US-WY" <%="US-WY".equals(province)?" selected":""%>>US-WY-Wyoming</option>
									<% } %>
								</select>
								</td>
								<td align="right"><b> <% if(oscarProps.getProperty("demographicLabelPostal") == null) { %>
								<bean:message
									key="demographic.demographiceditdemographic.formPostal" /> <% } else {
                                  out.print(oscarProps.getProperty("demographicLabelPostal"));
                              	 } %> : </b></td>
								<td align="left"><input type="text" name="postal" size="30" <%=getDisabled("postal")%>
									value="<%=StringUtils.trimToEmpty(demographic.getPostal())%>"
									onBlur="upCaseCtrl(this)" onChange="isPostalCode()"></td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPhoneH" />: </b></td>
								<td align="left">
								<input type="text" name="phone" onblur="formatPhoneNum();" <%=getDisabled("phone")%>
									style="display: inline; width: auto;"
									value="<%=StringUtils.trimToEmpty(StringUtils.trimToEmpty(demographic.getPhone()))%>"> <bean:message key="demographic.demographiceditdemographic.msgExt"/>:<input
									type="text" name="hPhoneExt" <%=getDisabled("hPhoneExt")%>
									value="<%=StringUtils.trimToEmpty(StringUtils.trimToEmpty(demoExt.get("hPhoneExt")))%>"
									size="4" /> <input type="hidden" name="hPhoneExtOrig"
									value="<%=StringUtils.trimToEmpty(StringUtils.trimToEmpty(demoExt.get("hPhoneExt")))%>" />
								</td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPhoneW" />:</b></td>
								<td align="left"><input type="text" name="phone2" <%=getDisabled("phone2")%>
									onblur="formatPhoneNum();"
									style="display: inline; width: auto;"
									value="<%=StringUtils.trimToEmpty(demographic.getPhone2())%>"> <bean:message key="demographic.demographiceditdemographic.msgExt"/>:<input
									type="text" name="wPhoneExt" <%=getDisabled("wPhoneExt")%>
									value="<%=StringUtils.trimToEmpty(StringUtils.trimToEmpty(demoExt.get("wPhoneExt")))%>"
									style="display: inline" size="4" /> <input type="hidden"
									name="wPhoneExtOrig"
									value="<%=StringUtils.trimToEmpty(StringUtils.trimToEmpty(demoExt.get("wPhoneExt")))%>" />
								</td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPhoneC" />: </b></td>
								<td align="left">
								<input type="text" name="demo_cell" onblur="formatPhoneNum();"
									style="display: inline; width: auto;" <%=getDisabled("demo_cell")%>
									value="<%=StringUtils.trimToEmpty(demoExt.get("demo_cell"))%>">
								<input type="hidden" name="demo_cellOrig"
									value="<%=StringUtils.trimToEmpty(demoExt.get("demo_cell"))%>" />
								</td>
								<td align="right"><b><bean:message
										key="demographic.demographicaddrecordhtm.formPhoneComment" />: </b></td>
								<td align="left" colspan="3">
								<input type="hidden" name="phoneCommentOrig"
									value="<%=StringUtils.trimToEmpty(demoExt.get("phoneComment"))%>" />
										<textarea rows="2" cols="30" name="phoneComment"><%=StringUtils.trimToEmpty(demoExt.get("phoneComment"))%></textarea>
								</td>
							</tr>							
							<tr valign="top">
								<td align="right"><b><bean:message
								    key="demographic.demographiceditdemographic.formNewsLetter" />:
								</b></td>
								<td align="left">
								<% String newsletter = oscar.util.StringUtils.noNull(demographic.getNewsletter()).trim();
								     if( newsletter == null || newsletter.equals("")) {
								        newsletter = "Unknown";
								     }
								  %> <select name="newsletter" <%=getDisabled("newsletter")%>>
								    <option value="Unknown" <%if(newsletter.equals("Unknown")){%>
								        selected <%}%>><bean:message
								        key="demographic.demographicaddrecordhtm.formNewsLetter.optUnknown" /></option>
								    <option value="No" <%if(newsletter.equals("No")){%> selected
								        <%}%>><bean:message
								        key="demographic.demographicaddrecordhtm.formNewsLetter.optNo" /></option>
								    <option value="Paper" <%if(newsletter.equals("Paper")){%>
								        selected <%}%>><bean:message
								        key="demographic.demographicaddrecordhtm.formNewsLetter.optPaper" /></option>
								    <option value="Electronic"
								        <%if(newsletter.equals("Electronic")){%> selected <%}%>><bean:message
								        key="demographic.demographicaddrecordhtm.formNewsLetter.optElectronic" /></option>
								</select></td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.aboriginal" />: </b></td>
								<td align="left">
								
								<select name="aboriginal" <%=getDisabled("aboriginal")%>>
									<option value="" <%if(aboriginal.equals("")){%>
										selected <%}%>>Unknown</option>
									<option value="No" <%if(aboriginal.equals("No")){%> selected
										<%}%>>No</option>
									<option value="Yes" <%if(aboriginal.equals("Yes")){%>
										selected <%}%>>Yes</option>
						
								</select>
								<input type="hidden" name="aboriginalOrig"
									value="<%=StringUtils.trimToEmpty(demoExt.get("aboriginal"))%>" />
								</td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formEmail" />: </b></td>
								<td align="left"><input type="text" name="email" size="30" <%=getDisabled("email")%>
									value="<%=demographic.getEmail()!=null? demographic.getEmail() : ""%>">
								</td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPHRUserName" />: </b></td>
								<td align="left"><input type="text" name="myOscarUserName" size="30" <%=getDisabled("myOscarUserName")%>
									value="<%=demographic.getMyOscarUserName()!=null? demographic.getMyOscarUserName() : ""%>"><br />
								<%if (demographic.getMyOscarUserName()==null ||demographic.getMyOscarUserName().equals("")) {%>

								<%
									String onclickString="popup(900, 800, '../phr/indivo/RegisterIndivo.jsp?demographicNo="+demographic_no+"', 'indivoRegistration');";
									MyOscarLoggedInInfo myOscarLoggedInInfo=MyOscarLoggedInInfo.getLoggedInInfo(session);
									if (myOscarLoggedInInfo==null || !myOscarLoggedInInfo.isLoggedIn()) onclickString="alert('Please login to MyOscar first.')";
								%>
								<a href="javascript:"
									onclick="<%=onclickString%>"><sub
									style="white-space: nowrap;"><bean:message key="demographic.demographiceditdemographic.msgRegisterPHR"/></sub></a> <%}%>
								</td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formDOB" /></b><bean:message
									key="demographic.demographiceditdemographic.formDOBDetais" /><b>:</b>
								</td>
								<td align="left" nowrap><input type="text"
									name="year_of_birth" <%=getDisabled("year_of_birth")%>
									value="<%=birthYear%>"
									size="3" maxlength="4"> 

									<% 
									String sbMonth;
									String sbDay;
									DecimalFormat dFormat = new DecimalFormat("00");
									%>
			                        <select name="month_of_birth" id="month_of_birth">
									<% for(int i=1; i<=12; i++) {
										sbMonth = dFormat.format(i); %>
										<option value="<%=sbMonth%>"<%=birthMonth.equals(sbMonth)?" selected":""%>><%=sbMonth%></option>
									<%} %>
									</select>
									
			                         <select name="date_of_birth" id="date_of_birth">
									<% for(int i=1; i<=31; i++) {
										sbDay = dFormat.format(i); %>
										<option value="<%=sbDay%>"<%=birthDate.equals(sbDay)?" selected":""%>><%=sbDay%></option>
									<%} %>
									</select>			
									
									<b>Age: <input type="text"
									name="age" readonly value="<%=age%>" size="3"> </b></td>
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formSex" />:</b></td>
			                	<td><select  name="sex" id="sex">
			                        <option value=""></option>
			                		<% for(Gender gn : Gender.values()){ %>
			                        <option value=<%=gn.name()%> <%=((demographic.getSex().toUpperCase().equals(gn.name())) ? " selected=\"selected\" " : "") %>><%=gn.getText()%></option>
			                        <% } %>
			                        </select>
			                    </td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formHin" />: </b></td>
								<td align="left" nowrap><input type="text" name="hin" <%=getDisabled("hin")%>
									value="<%=StringUtils.trimToEmpty(demographic.getHin())%>" size="17">
								<b><bean:message
									key="demographic.demographiceditdemographic.formVer" /></b> <input
									type="text" name="ver" <%=getDisabled("ver")%>
									value="<%=StringUtils.trimToEmpty(demographic.getVer())%>" size="3"
									onBlur="upCaseCtrl(this)"></td>
								<td align="right">
									<b><bean:message key="demographic.demographiceditdemographic.formEFFDate" />:</b>
								</td>
								<td align="left">
								<%
								java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
								String effDate=null;
								if(demographic.getEffDate() != null) {
									effDate=StringUtils.trimToNull(sdf.format(demographic.getEffDate()));
								}
                                // Put 0 on the left on dates
                                DecimalFormat decF = new DecimalFormat();
								String effDateYear="";
								String effDateMonth="";
								String effDateDay="";
								if (effDate!=null)
								{
	                                 // Year
	                                 decF.applyPattern("0000");
	                                 effDateYear = decF.format(MyDateFormat.getYearFromStandardDate(effDate));
	                                 // Month and Day
	                                 decF.applyPattern("00");
	                                 effDateMonth = decF.format(MyDateFormat.getMonthFromStandardDate(effDate));
	                                 effDateDay = decF.format(MyDateFormat.getDayFromStandardDate(effDate));
								}
                              %> <input type="text" name="eff_date_year" <%=getDisabled("eff_date_year")%>
									size="4" maxlength="4" value="<%= effDateYear%>"> <input
									type="text" name="eff_date_month" size="2" maxlength="2" <%=getDisabled("eff_date_month")%>
									value="<%= effDateMonth%>"> <input type="text"
									name="eff_date_date" size="2" maxlength="2" <%=getDisabled("eff_date_date")%>
									value="<%= effDateDay%>">
								</td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formHCType" />:</b></td>
								<td align="left">
								
								<% String hctype = demographic.getHcType()==null?"":demographic.getHcType(); %>
								<select name="hc_type" style="width: 200px" <%=getDisabled("hc_type")%>>
									<option value="OT"
										<%=(hctype.equals("OT") || hctype.equals("") || hctype.length() > 2)?" selected":""%>><bean:message key="demographic.demographiceditdemographic.optOther"/></option>
									<% if (pNames.isDefined()) {
                                       for (ListIterator li = pNames.listIterator(); li.hasNext(); ) {
                                           province = (String) li.next(); %>
									<option value="<%=province%>"
										<%=province.equals(hctype)?" selected":""%>><%=li.next()%></option>
									<% } %>
									<% } else { %>
									<option value="AB" <%=hctype.equals("AB")?" selected":""%>>AB-Alberta</option>
									<option value="BC" <%=hctype.equals("BC")?" selected":""%>>BC-British Columbia</option>
									<option value="MB" <%=hctype.equals("MB")?" selected":""%>>MB-Manitoba</option>
									<option value="NB" <%=hctype.equals("NB")?" selected":""%>>NB-New Brunswick</option>
									<option value="NL" <%=hctype.equals("NL")?" selected":""%>>NL-Newfoundland & Labrador</option>
									<option value="NT" <%=hctype.equals("NT")?" selected":""%>>NT-Northwest Territory</option>
									<option value="NS" <%=hctype.equals("NS")?" selected":""%>>NS-Nova Scotia</option>
									<option value="NU" <%=hctype.equals("NU")?" selected":""%>>NU-Nunavut</option>
									<option value="ON" <%=hctype.equals("ON")?" selected":""%>>ON-Ontario</option>
									<option value="PE" <%=hctype.equals("PE")?" selected":""%>>PE-Prince Edward Island</option>
									<option value="QC" <%=hctype.equals("QC")?" selected":""%>>QC-Quebec</option>
									<option value="SK" <%=hctype.equals("SK")?" selected":""%>>SK-Saskatchewan</option>
									<option value="YT" <%=hctype.equals("YT")?" selected":""%>>YT-Yukon</option>
									<option value="US" <%=hctype.equals("US")?" selected":""%>>US resident</option>
									<option value="US-AK" <%=hctype.equals("US-AK")?" selected":""%>>US-AK-Alaska</option>
									<option value="US-AL" <%=hctype.equals("US-AL")?" selected":""%>>US-AL-Alabama</option>
									<option value="US-AR" <%=hctype.equals("US-AR")?" selected":""%>>US-AR-Arkansas</option>
									<option value="US-AZ" <%=hctype.equals("US-AZ")?" selected":""%>>US-AZ-Arizona</option>
									<option value="US-CA" <%=hctype.equals("US-CA")?" selected":""%>>US-CA-California</option>
									<option value="US-CO" <%=hctype.equals("US-CO")?" selected":""%>>US-CO-Colorado</option>
									<option value="US-CT" <%=hctype.equals("US-CT")?" selected":""%>>US-CT-Connecticut</option>
									<option value="US-CZ" <%=hctype.equals("US-CZ")?" selected":""%>>US-CZ-Canal Zone</option>
									<option value="US-DC" <%=hctype.equals("US-DC")?" selected":""%>>US-DC-District Of Columbia</option>
									<option value="US-DE" <%=hctype.equals("US-DE")?" selected":""%>>US-DE-Delaware</option>
									<option value="US-FL" <%=hctype.equals("US-FL")?" selected":""%>>US-FL-Florida</option>
									<option value="US-GA" <%=hctype.equals("US-GA")?" selected":""%>>US-GA-Georgia</option>
									<option value="US-GU" <%=hctype.equals("US-GU")?" selected":""%>>US-GU-Guam</option>
									<option value="US-HI" <%=hctype.equals("US-HI")?" selected":""%>>US-HI-Hawaii</option>
									<option value="US-IA" <%=hctype.equals("US-IA")?" selected":""%>>US-IA-Iowa</option>
									<option value="US-ID" <%=hctype.equals("US-ID")?" selected":""%>>US-ID-Idaho</option>
									<option value="US-IL" <%=hctype.equals("US-IL")?" selected":""%>>US-IL-Illinois</option>
									<option value="US-IN" <%=hctype.equals("US-IN")?" selected":""%>>US-IN-Indiana</option>
									<option value="US-KS" <%=hctype.equals("US-KS")?" selected":""%>>US-KS-Kansas</option>
									<option value="US-KY" <%=hctype.equals("US-KY")?" selected":""%>>US-KY-Kentucky</option>
									<option value="US-LA" <%=hctype.equals("US-LA")?" selected":""%>>US-LA-Louisiana</option>
									<option value="US-MA" <%=hctype.equals("US-MA")?" selected":""%>>US-MA-Massachusetts</option>
									<option value="US-MD" <%=hctype.equals("US-MD")?" selected":""%>>US-MD-Maryland</option>
									<option value="US-ME" <%=hctype.equals("US-ME")?" selected":""%>>US-ME-Maine</option>
									<option value="US-MI" <%=hctype.equals("US-MI")?" selected":""%>>US-MI-Michigan</option>
									<option value="US-MN" <%=hctype.equals("US-MN")?" selected":""%>>US-MN-Minnesota</option>
									<option value="US-MO" <%=hctype.equals("US-MO")?" selected":""%>>US-MO-Missouri</option>
									<option value="US-MS" <%=hctype.equals("US-MS")?" selected":""%>>US-MS-Mississippi</option>
									<option value="US-MT" <%=hctype.equals("US-MT")?" selected":""%>>US-MT-Montana</option>
									<option value="US-NC" <%=hctype.equals("US-NC")?" selected":""%>>US-NC-North Carolina</option>
									<option value="US-ND" <%=hctype.equals("US-ND")?" selected":""%>>US-ND-North Dakota</option>
									<option value="US-NE" <%=hctype.equals("US-NE")?" selected":""%>>US-NE-Nebraska</option>
									<option value="US-NH" <%=hctype.equals("US-NH")?" selected":""%>>US-NH-New Hampshire</option>
									<option value="US-NJ" <%=hctype.equals("US-NJ")?" selected":""%>>US-NJ-New Jersey</option>
									<option value="US-NM" <%=hctype.equals("US-NM")?" selected":""%>>US-NM-New Mexico</option>
									<option value="US-NU" <%=hctype.equals("US-NU")?" selected":""%>>US-NU-Nunavut</option>
									<option value="US-NV" <%=hctype.equals("US-NV")?" selected":""%>>US-NV-Nevada</option>
									<option value="US-NY" <%=hctype.equals("US-NY")?" selected":""%>>US-NY-New York</option>
									<option value="US-OH" <%=hctype.equals("US-OH")?" selected":""%>>US-OH-Ohio</option>
									<option value="US-OK" <%=hctype.equals("US-OK")?" selected":""%>>US-OK-Oklahoma</option>
									<option value="US-OR" <%=hctype.equals("US-OR")?" selected":""%>>US-OR-Oregon</option>
									<option value="US-PA" <%=hctype.equals("US-PA")?" selected":""%>>US-PA-Pennsylvania</option>
									<option value="US-PR" <%=hctype.equals("US-PR")?" selected":""%>>US-PR-Puerto Rico</option>
									<option value="US-RI" <%=hctype.equals("US-RI")?" selected":""%>>US-RI-Rhode Island</option>
									<option value="US-SC" <%=hctype.equals("US-SC")?" selected":""%>>US-SC-South Carolina</option>
									<option value="US-SD" <%=hctype.equals("US-SD")?" selected":""%>>US-SD-South Dakota</option>
									<option value="US-TN" <%=hctype.equals("US-TN")?" selected":""%>>US-TN-Tennessee</option>
									<option value="US-TX" <%=hctype.equals("US-TX")?" selected":""%>>US-TX-Texas</option>
									<option value="US-UT" <%=hctype.equals("US-UT")?" selected":""%>>US-UT-Utah</option>
									<option value="US-VA" <%=hctype.equals("US-VA")?" selected":""%>>US-VA-Virginia</option>
									<option value="US-VI" <%=hctype.equals("US-VI")?" selected":""%>>US-VI-Virgin Islands</option>
									<option value="US-VT" <%=hctype.equals("US-VT")?" selected":""%>>US-VT-Vermont</option>
									<option value="US-WA" <%=hctype.equals("US-WA")?" selected":""%>>US-WA-Washington</option>
									<option value="US-WI" <%=hctype.equals("US-WI")?" selected":""%>>US-WI-Wisconsin</option>
									<option value="US-WV" <%=hctype.equals("US-WV")?" selected":""%>>US-WV-West Virginia</option>
									<option value="US-WY" <%=hctype.equals("US-WY")?" selected":""%>>US-WY-Wyoming</option>
									<% } %>
								</select>
								</td>

								<td align="right"><b><bean:message key="demographic.demographiceditdemographic.formHCRenewDate" />:</b></td>
								<td align="left">
								<%
                                 // Put 0 on the left on dates
                                 // Year
                                 decF.applyPattern("0000");

								 GregorianCalendar hcRenewalCal=new GregorianCalendar();
								 String renewDateYear="";
								 String renewDateMonth="";
								 String renewDateDay="";
								 if (demographic.getHcRenewDate()!=null)
								 {
								    hcRenewalCal.setTime(demographic.getHcRenewDate());
	                                 renewDateYear = decF.format(hcRenewalCal.get(GregorianCalendar.YEAR));
                                 // Month and Day
                                 decF.applyPattern("00");
	                                 renewDateMonth = decF.format(hcRenewalCal.get(GregorianCalendar.MONTH)+1);
	                                 renewDateDay = decF.format(hcRenewalCal.get(GregorianCalendar.DAY_OF_MONTH));
								 }

                              %>
								<input type="text" name="hc_renew_date_year" size="4" maxlength="4" value="<%=renewDateYear%>" <%=getDisabled("hc_renew_date_year")%>>
								<input type="text" name="hc_renew_date_month" size="2" maxlength="2" value="<%=renewDateMonth%>" <%=getDisabled("hc_renew_date_month")%>>
								<input type="text" name="hc_renew_date_date" size="2" maxlength="2" value="<%=renewDateDay%>" <%=getDisabled("hc_renew_date_date")%>>
								</td>
							</tr>
							<tr valign="top">
								<td align="right"><b><bean:message key="demographic.demographiceditdemographic.msgCountryOfOrigin"/>: </b></td>
								<td align="left"><select id="countryOfOrigin" name="countryOfOrigin" <%=getDisabled("countryOfOrigin")%>>
									<option value="-1"><bean:message key="demographic.demographiceditdemographic.msgNotSet"/></option>
									<%for(CountryCode cc : countryList){ %>
									<option value="<%=cc.getCountryId()%>"
										<% if (oscar.util.StringUtils.noNull(demographic.getCountryOfOrigin()).equals(cc.getCountryId())){out.print("SELECTED") ;}%>><%=cc.getCountryName() %></option>
									<%}%>
								</select></td>
								
							</tr>
							<tr valign="top">
								<td align="right"><b>SIN:</b></td>
								<td align="left"><input type="text" name="sin" size="30" <%=getDisabled("sin")%>
									value="<%=(demographic.getSin()==null||demographic.getSin().equals("null"))?"":demographic.getSin()%>"></td>
								<td align="right" nowrap><b> <bean:message
									key="demographic.demographiceditdemographic.cytolNum" />:</b></td>
								<td><input type="text" name="cytolNum" <%=getDisabled("cytolNum")%>
									style="display: inline; width: auto;"
									value="<%=StringUtils.trimToEmpty(demoExt.get("cytolNum"))%>">
								<input type="hidden" name="cytolNumOrig"
									value="<%=StringUtils.trimToEmpty(demoExt.get("cytolNum"))%>" />
								</td>
							</tr>

<%-- TOGGLE OFF PATIENT CLINIC STATUS --%>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PATIENT_CLINIC_STATUS" value="true">

							<tr valign="top">
								<td align="right" nowrap><b>
								<% if(oscarProps.getProperty("demographicLabelDoctor") != null) { out.print(oscarProps.getProperty("demographicLabelDoctor","")); } else { %>
								<bean:message
									key="demographic.demographiceditdemographic.formDoctor" />
								<% } %>: </b></td>
								<td align="left"><select name="provider_no" <%=getDisabled("provider_no")%>
									style="width: 200px">
									<option value=""></option>
									<%
							for(Provider p : doctors) {
                         
                        %>
									<option value="<%=p.getProviderNo()%>"
										<%=p.getProviderNo().equals(demographic.getProviderNo())?"selected":""%>>
									<%=Misc.getShortStr( (p.getLastName()+","+p.getFirstName()),"",nStrShowLen)%></option>
									<% } %>
								</select></td>
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formNurse" />: </b></td>
								<td align="left"><select name="nurse" <%=getDisabled("nurse")%>
									style="width: 200px">
									<option value=""></option>
									<%
                         
                         
									for(Provider p : nurses) {
                        %>
									<option value="<%=p.getProviderNo()%>"
										<%=p.getProviderNo().equals(nurse)?"selected":""%>>
									<%=Misc.getShortStr( (p.getLastName()+","+p.getFirstName()),"",nStrShowLen)%></option>
									<% } %>
								</select></td>
							</tr>
							<tr valign="top">
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formMidwife" />: </b></td>
								<td align="left"><select name="midwife" <%=getDisabled("midwife")%>
									style="width: 200px">
									<option value=""></option>
									<%
									for(Provider p : midwifes) {
                        %>
									<option value="<%=p.getProviderNo()%>"
										<%=p.getProviderNo().equals(midwife)?"selected":""%>>
									<%=Misc.getShortStr( (p.getLastName()+","+p.getFirstName()),"",nStrShowLen)%></option>
									<% } %>
								</select></td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formResident" />:</b></td>
								<td align="left"><select name="resident" style="width: 200px" <%=getDisabled("resident")%>>
									<option value=""></option>
									<%
									for(Provider p : doctors) {
                        %>
									<option value="<%=p.getProviderNo()%>"
										<%=p.getProviderNo().equals(resident)?"selected":""%>>
									<%=Misc.getShortStr( (p.getLastName()+","+p.getFirstName()),"",nStrShowLen)%></option>
									<% } %>
								</select></td>
							</tr>

							<tr valign="top">
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formRefDoc" />: </b></td>
								<td align="left">
								<% if(oscarProps.getProperty("isMRefDocSelectList", "").equals("true") ) {
                                  		// drop down list
									  Properties prop = null;
									  Vector vecRef = new Vector();
									  List<ProfessionalSpecialist> specialists = professionalSpecialistDao.findAll();
                                      for(ProfessionalSpecialist specialist : specialists) {
                                    	  prop = new Properties();
                                    	  if (specialist != null && specialist.getReferralNo() != null && ! specialist.getReferralNo().equals("")) {
	                                          prop.setProperty("referral_no", specialist.getReferralNo());
	                                          prop.setProperty("last_name", specialist.getLastName());
	                                          prop.setProperty("first_name", specialist.getFirstName());
	                                          vecRef.add(prop);
                                    	  }
                                      }

                                  %> <select name="r_doctor" <%=getDisabled("r_doctor")%>
									onChange="changeRefDoc()" style="width: 200px">
									<option value=""></option>
									<% for(int k=0; k<vecRef.size(); k++) {
                                  		prop= (Properties) vecRef.get(k);
                                  	%>
									<option
										value="<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>"
										<%=prop.getProperty("referral_no").equals(rdohip)?"selected":""%>>
									<%=Misc.getShortStr( (prop.getProperty("last_name")+","+prop.getProperty("first_name")),"",nStrShowLen)%></option>
									<% }
 	                      	
 	                       %>
                                  </select> <script type="text/javascript" language="Javascript">
<!--
function changeRefDoc() {
//alert(document.updatedelete.r_doctor.value);
var refName = document.updatedelete.r_doctor.options[document.updatedelete.r_doctor.selectedIndex].value;
var refNo = "";
  	<% for(int k=0; k<vecRef.size(); k++) {
  		prop= (Properties) vecRef.get(k);
  	%>
if(refName=="<%=prop.getProperty("last_name")+","+prop.getProperty("first_name")%>") {
  refNo = '<%=prop.getProperty("referral_no", "")%>';
}
<% } %>
document.updatedelete.r_doctor_ohip.value = refNo;
}
//-->
</script> <% } else {%> <input type="text" name="r_doctor" size="30" maxlength="40" <%=getDisabled("r_doctor")%>
									value="<%=rd%>"> <% } %>
								</td>
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formRefDocNo" />: </b></td>
								<td align="left"><input type="text" name="r_doctor_ohip" <%=getDisabled("r_doctor_ohip")%>
									size="20" maxlength="6" value="<%=rdohip%>"> <% if("ON".equals(prov)) { %>
								<a
									href="javascript:referralScriptAttach2('r_doctor_ohip','r_doctor')"><bean:message key="demographic.demographiceditdemographic.btnSearch"/>
								#</a> <% } %>
								</td>
							</tr>

</oscar:oscarPropertiesCheck>
<%-- END TOGGLE OFF PATIENT CLINIC STATUS --%>

<%-- TOGGLE OFF PATIENT ROSTERING - NOT USED IN ALL PROVINCES. --%>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PATIENT_ROSTERING" value="true">	

							<tr valign="top">
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formRosterStatus" />:
								</b></td>
								<td align="left">
								<%String rosterStatus = demographic.getRosterStatus();
                                  if (rosterStatus == null) {
                                     rosterStatus = "";
                                  }
                                  %>
                                <input type="hidden" name="initial_rosterstatus" value="<%=rosterStatus%>"/>
								<select id="roster_status" name="roster_status" style="width: 120" <%=getDisabled("roster_status")%> onchange="checkRosterStatus2()">
									<option value=""></option>
									<option value="RO"
										<%="RO".equals(rosterStatus)?" selected":""%>>
									<bean:message key="demographic.demographiceditdemographic.optRostered"/></option>
									<option value="NR"
										<%=rosterStatus.equals("NR")?" selected":""%>>
									<bean:message key="demographic.demographiceditdemographic.optNotRostered"/></option>
									<option value="TE"
										<%=rosterStatus.equals("TE")?" selected":""%>>
									<bean:message key="demographic.demographiceditdemographic.optTerminated"/></option>
									<option value="FS"
										<%=rosterStatus.equals("FS")?" selected":""%>>
									<bean:message key="demographic.demographiceditdemographic.optFeeService"/></option>
									<% 
									for(String status: demographicDao.getRosterStatuses()) {
									%>
									<option
										<%=rosterStatus.equals(status)?" selected":""%>><%=status%></option>
									<% }
                                    
                                   // end while %>
								</select> <input type="button" onClick="newStatus1();" value="<bean:message key="demographic.demographiceditdemographic.btnAddNew"/>">
								</td>
                                                                    <%
                                                             // Put 0 on the left on dates
                                                             // Year
                                                             decF.applyPattern("0000");

                                                             GregorianCalendar dateCal=new GregorianCalendar();
                                                             String rosterDateYear="";
                                                             String rosterDateMonth="";
                                                             String rosterDateDay="";
                                                             if (demographic.getRosterDate()!=null){
                                                                dateCal.setTime(demographic.getRosterDate());
                                                                rosterDateYear = decF.format(dateCal.get(GregorianCalendar.YEAR));
                                                                // Month and Day
                                                                decF.applyPattern("00");
                                                                rosterDateMonth = decF.format(dateCal.get(GregorianCalendar.MONTH)+1);
                                                                rosterDateDay   = decF.format(dateCal.get(GregorianCalendar.DAY_OF_MONTH));
                                                             }
                                                             String rosterTerminationDateYear="";
                                                             String rosterTerminationDateMonth="";
                                                             String rosterTerminationDateDay="";
                                                             String rosterTerminationReason="";
                                                             if (demographic.getRosterTerminationDate()!=null){
                                                                dateCal.setTime(demographic.getRosterTerminationDate());
                                                                rosterTerminationDateYear = decF.format(dateCal.get(GregorianCalendar.YEAR));
                                                                // Month and Day
                                                                decF.applyPattern("00");
                                                                rosterTerminationDateMonth = decF.format(dateCal.get(GregorianCalendar.MONTH)+1);
                                                                rosterTerminationDateDay   = decF.format(dateCal.get(GregorianCalendar.DAY_OF_MONTH));
                                                             }
                                                             rosterTerminationReason = demographic.getRosterTerminationReason();

                                                             String patientStatusDateYear="";
                                                             String patientStatusDateMonth="";
                                                             String patientStatusDateDay="";
                                                             if (demographic.getPatientStatusDate()!=null){
                                                                dateCal.setTime(demographic.getPatientStatusDate());
                                                                patientStatusDateYear = decF.format(dateCal.get(GregorianCalendar.YEAR));
                                                                // Month and Day
                                                                decF.applyPattern("00");
                                                                patientStatusDateMonth = decF.format(dateCal.get(GregorianCalendar.MONTH)+1);
                                                                patientStatusDateDay   = decF.format(dateCal.get(GregorianCalendar.DAY_OF_MONTH));
                                                             }
                                                                    %>

								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.DateJoined" />: </b></td>
								<td align="left">
									<input  type="text" name="roster_date_year" size="4" maxlength="4" value="<%=rosterDateYear%>">
									<input  type="text" name="roster_date_month" size="2" maxlength="2" value="<%=rosterDateMonth%>">
									<input  type="text" name="roster_date_day" size="2" maxlength="2" value="<%=rosterDateDay%>">
								</td>
							</tr>
							
							
							<%--
							<tr valign="top">
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.RosterTerminationReason" />: </b></td>
								<td align="left" colspan="3">
									<select  name="roster_termination_reason">
										<option value="">N/A</option>
<%for (String code : Util.rosterTermReasonProperties.getTermReasonCodes()) { %>
										<option value="<%=code %>" <%=code.equals(rosterTerminationReason)?"selected":"" %> ><%=Util.rosterTermReasonProperties.getReasonByCode(code) %></option>
<%} %>
									</select>
								</td>
							</tr>
							--%>

</oscar:oscarPropertiesCheck>														
<%-- END TOGGLE OFF PATIENT ROSTERING --%>


							<tr valign="top">
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formPatientStatus" />:</b>
								<b> </b></td>
								<td align="left">
								<%
                                String patientStatus = demographic.getPatientStatus();
                                 if(patientStatus==null) patientStatus="";%>
                                <input type="hidden" name="initial_patientstatus" value="<%=patientStatus%>">
								<select name="patient_status" style="width: 120" <%=getDisabled("patient_status")%>>
									<option value="AC"
										<%="AC".equals(patientStatus)?" selected":""%>>
									<bean:message key="demographic.demographiceditdemographic.optActive"/></option>
									<option value="IN"
										<%="IN".equals(patientStatus)?" selected":""%>>
									<bean:message key="demographic.demographiceditdemographic.optInActive"/></option>
									<option value="DE"
										<%="DE".equals(patientStatus)?" selected":""%>>
									<bean:message key="demographic.demographiceditdemographic.optDeceased"/></option>
									<option value="MO"
										<%="MO".equals(patientStatus)?" selected":""%>>
									<bean:message key="demographic.demographiceditdemographic.optMoved"/></option>
									<option value="FI"
										<%="FI".equals(patientStatus)?" selected":""%>>
									<bean:message key="demographic.demographiceditdemographic.optFired"/></option>
									<%
									for(String status : demographicDao.search_ptstatus()) {
                                     %>
									<option
										<%=status.equals(patientStatus)?" selected":""%>><%=status%></option>
									<% }
                                 
                                   // end while %>
								</select> <input type="button" onClick="newStatus();" value="<bean:message key="demographic.demographiceditdemographic.btnAddNew"/>">
								
								</td>
								<%--
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.PatientStatusDate" />: </b></td>
								<td align="left">
                                                                    <input  type="text" name="patientstatus_date_year" size="4" maxlength="4" value="<%=patientStatusDateYear%>">
                                                                    <input  type="text" name="patientstatus_date_month" size="2" maxlength="2" value="<%=patientStatusDateMonth%>">
                                                                    <input  type="text" name="patientstatus_date_day" size="2" maxlength="2" value="<%=patientStatusDateDay%>">
								</td>
                                                        </tr>
                                                        <tr>
                                                                <td>&nbsp;</td>
                                                                --%>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formChartNo" />:</b></td>
								<td align="left"><input type="text" name="chart_no"
									size="30" value="<%=StringUtils.trimToEmpty(demographic.getChartNo())%>" <%=getDisabled("chart_no")%>>
								</td>
							</tr>
							
							<tr>
								
						
	                            <td align="right"><b><bean:message key="web.record.details.archivedPaperChart" />: </b></td>
	                            <td align="left">
	                            	<%
	                            		String paperChartIndicator = StringUtils.trimToEmpty(demoExt.get("paper_chart_archived"));
	                            		String paperChartIndicatorDate = StringUtils.trimToEmpty(demoExt.get("paper_chart_archived_date"));
	                            		String paperChartIndicatorProgram = StringUtils.trimToEmpty(demoExt.get("paper_chart_archived_program"));
	                            		
	                            	%>
	                            	<select name="paper_chart_archived" id="paper_chart_archived" <%=getDisabled("paper_chart_archived")%> onChange="updatePaperArchive()">
		                            	<option value="" <%="".equals(paperChartIndicator)?" selected":""%>>
		                            	</option>
										<option value="NO" <%="NO".equals(paperChartIndicator)?" selected":""%>>
											<bean:message key="demographic.demographiceditdemographic.paperChartIndicator.no"/>
										</option>
										<option value="YES"	<%="YES".equals(paperChartIndicator)?" selected":""%>>
											<bean:message key="demographic.demographiceditdemographic.paperChartIndicator.yes"/>
										</option>
									</select>
									
									<input type="text" name="paper_chart_archived_date" id="paper_chart_archived_date" size="11" value="<%=paperChartIndicatorDate%>" >
										<img src="../images/cal.gif" id="archive_date_cal">
											<bean:message key="schedule.scheduletemplateapplying.msgDateFormat"/>
										
											<input type="hidden" name="paper_chart_archived_program" id="paper_chart_archived_program" value="<%=paperChartIndicatorProgram%>"/>	
									
						
								
                                </td>


							<%-- TOGGLE SHOW IPHIS NUMBER --%>
								<%
									if(showIPHIS || showPanorama || showIscis || showOhiss || showEpiInfo || showHedgehog) {
										%><td colspan="2">
											<table border="0">
										<%
									}		
								%>
							
								<% if(showIPHIS) { %>
									<tr>
									<td align="right"><b><bean:message key="demographic.demographiceditdemographic.IPHIS" />:</b></td>
									<td align="left"><input type="text" name="IPHISClientNumber" size="30" value="<%=IPHISClientNumber%>"></td>
									</tr>
								<% } %>
								<% if(showPanorama) { %>
									<tr>
									<td align="right"><b><bean:message key="demographic.demographiceditdemographic.Panorama" />:</b></td>
									<td align="left"><input type="text" name="PanoramaClientNumber" size="30" value="<%=PanoramaClientNumber%>"></td>
									</tr>
								<% } %>
								<% if(showIscis) { %>
									<tr>
									<td align="right"><b><bean:message key="demographic.demographiceditdemographic.ISCIS" />:</b></td>
									<td align="left"><input type="text" name="IscisClientNumber" size="30" value="<%=IscisClientNumber%>"></td>
									</tr>
								<% } %>
								<% if(showOhiss) { %>
								<tr>
									<td align="right"><b><bean:message key="demographic.demographiceditdemographic.OHISS" />:</b></td>
									<td align="left"><input type="text" name="OhissClientNumber" size="30" value="<%=OhissClientNumber%>"></td>
								</tr>
								<% } %>
								<% if(showEpiInfo) { %>
								<tr>
									<td align="right"><b><bean:message key="demographic.demographiceditdemographic.EpiInfo" />:</b></td>
									<td align="left"><input type="text" name="EpiInfoClientNumber" size="30" value="<%=EpiInfoClientNumber%>"></td>
								</tr>
								<% } %>
								<% if(showHedgehog) { %>
								<tr>
									<td align="right"><b><bean:message key="demographic.demographiceditdemographic.Hedgehog" />:</b></td>
									<td align="left"><input type="text" name="HedgehogClientNumber" size="30" value="<%=HedgehogClientNumber%>"></td>
								</tr>
								<% } %>
	
	
								<%
									if(showIPHIS || showPanorama || showIscis || showOhiss || showEpiInfo || showHedgehog) {
										%></table></td><%
									}		
								%>
															
							
							<%-- END TOGGLE SHOW IPHIS NUMBER --%>
							</tr>
							
							
							
				
							<%-- 
						THE "PATIENT JOINED DATE" ROW HAS NOT BEEN ADDED TWICE IN ERROR 
						IT IS PLACED HERE FOR REPOSITIONING WHEN THE WAITING LIST
						MODULE IS ACTIVE. 
						THIS WAY WILL MAKE EVERYONE HAPPY.
					--%>
							<tr valign="top" >
								<td align="right" nowrap><b><bean:message
									key="demographic.demographiceditdemographic.formDateJoined1" />:
								</b></td>
								<td align="left">
								<%

								String date_joined = demographic.getDateJoined() != null  ? sdf.format(demographic.getDateJoined()) : null;
                                 String dateJoinedYear = "";
                                 String dateJoinedMonth = "";
                                 String dateJoinedDay = "";
                                 if( date_joined != null && date_joined.length() == 10 ) {
                                    // Format year
                                    decF.applyPattern("0000");
                                    dateJoinedYear = decF.format(MyDateFormat.getYearFromStandardDate(date_joined));
                                    decF.applyPattern("00");
                                    dateJoinedMonth = decF.format(MyDateFormat.getMonthFromStandardDate(date_joined));
                                    dateJoinedDay = decF.format(MyDateFormat.getDayFromStandardDate(date_joined));
                                 }
                              %> <input type="text"
									name="date_joined_year" size="4" maxlength="4"
									value="<%= dateJoinedYear %>"> <input type="text"
									name="date_joined_month" size="2" maxlength="2"
									value="<%= dateJoinedMonth %>"> <input type="text"
									name="date_joined_date" size="2" maxlength="2"
									value="<%= dateJoinedDay %>"></td>
								<td align="right"><b><bean:message
									key="demographic.demographiceditdemographic.formEndDate" />: </b></td>
								<td align="left">
								<%
								String endDate = null;
								if(demographic.getEndDate() != null) {
									endDate=sdf.format(demographic.getEndDate());
								}
								String endYear="";
								String endMonth="";
								String endDay="";

								if (endDate!=null)
								{
	                                 // Format year
	                                 decF.applyPattern("0000");
	                                 endYear = decF.format(MyDateFormat.getYearFromStandardDate(endDate));
	                                 decF.applyPattern("00");
	                                 endMonth = decF.format(MyDateFormat.getMonthFromStandardDate(endDate));
	                                 endDay = decF.format(MyDateFormat.getDayFromStandardDate(endDate));
								}
                              %> <input type="text" name="end_date_year"
									size="4" maxlength="4" value="<%= endYear %>"> <input
									type="text" name="end_date_month" size="2" maxlength="2"
									value="<%= endMonth %>"> <input type="text"
									name="end_date_date" size="2" maxlength="2"
									value="<%= endDay %>"></td>
							</tr>
				<%-- END MOVE PATIENT JOINED DATE --%>
							

<%-- TOGGLE PATIENT PRIVACY CONSENT --%>							
<oscar:oscarPropertiesCheck property="privateConsentEnabled" value="true">
					
<tr valign="top"><td colspan="4">
	<table id="privacyConsentTable" >	
			<tr id="privacyConsentHeading" style="display:none;">
				<th class="alignLeft" colspan="4" >Privacy Consent</th>
			</tr>
			
			<% if(showConsentsThisTime) { %>
			<tr>
				<td width="30%">
				<input type="hidden" name="usSignedOrig" value="<%=StringUtils.defaultString(apptMainBean.getString(demoExt.get("usSigned")))%>" />	
					<input type="hidden" name="privacyConsentOrig" value="<%=privacyConsent%>" />
					<input type="hidden" name="informedConsentOrig" value="<%=informedConsent%>" />
				
				<input type="checkbox" name="privacyConsent" id="privacyConsent" value="yes" <%=privacyConsent.equals("yes") ? "checked" : ""%>>
				<label style="font-weight:bold;" for="privacyConsent">Privacy Consent (verbal) Obtained</label> 
				</td>
				<td>
					&nbsp;
				</td>
			</tr>
			<tr>
				<td>
				<input type="checkbox" name="informedConsent" id="informedConsent" value="yes" <%=informedConsent.equals("yes") ? "checked" : ""%>>
				<label style="font-weight:bold;" for="informedConsent">Informed Consent (verbal) Obtained</label>
				</td>
			</tr>
			<tr>
			<td >
				<div id="usSigned">
					<input type="radio" name="usSigned" id="usSigned" value="signed" <%=usSigned.equals("signed") ? "checked" : ""%>>
						<label style="font-weight:bold;" for="usSigned">U.S. Resident Consent Form Signed </label>
			
				    <input type="radio" name="usSigned" id="usSigned" value="unsigned" <%=usSigned.equals("unsigned") ? "checked" : ""%>>
				    	<label style="font-weight:bold;" for="usSigned">U.S. Resident Consent Form NOT Signed</label>
			    </div>
			</td>
			</tr>
			<% } %>
			  			
			<%-- This block of code was designed to eventually manage all of the patient consents. --%>
			<oscar:oscarPropertiesCheck property="USE_NEW_PATIENT_CONSENT_MODULE" value="true" >
			
				<c:forEach items="${ consentTypes }" var="consentType" varStatus="count">
					<c:set var="patientConsent" value="" />
					<c:forEach items="${ patientConsents }" var="consent" >
						<c:if test="${ consent.consentType.id eq consentType.id }">
							<c:set var="patientConsent" value="${ consent }" />
						</c:if>													
					</c:forEach>
					<tr class="privacyConsentRow" id="${ count.index }" valign="top">
						<td class="alignLeft" width="30%" >
							<label style="font-weight:bold;" valign="center" for="${ consentType.type }" >
							
								<input type="checkbox" 
									name="${ consentType.type }" 
									id="${ consentType.type }" 
									value="${ consentType.id }" 
									${ not empty patientConsent and patientConsent.patientConsented ? 'checked' : '' } />
								
								<input type="hidden" name="${ consentType.type }_id" 
									value="${ not empty patientConsent and patientConsent.id gt 0 ? patientConsent.id : 0 }" />
									
								<c:out value="${ consentType.name }" />
								
							</label>
						</td>
						
						<td class="alignLeft" colspan="2" width="40%" >
							<c:out value="${ consentType.description }" />
						</td>
						
						<td class="alignLeft" id="consentStatusDate" width="25%" >	
							<c:if test="${ not empty patientConsent }" >
								<c:choose>
									<c:when test="${ patientConsent.optout }">
										<span class="info" style="color:red;">Opted Out:<c:out value="${ patientConsent.optoutDate }" /></span>
									</c:when>					
									<c:otherwise>
										<span class="info" style="color:green;">Consented:<c:out value="${ patientConsent.consentDate }" /></span>
									</c:otherwise>				
								</c:choose>															
							</c:if>																														
						</td>
						
					</tr>
				</c:forEach>
				
			</oscar:oscarPropertiesCheck>

</table></td></tr>
</oscar:oscarPropertiesCheck> 
                                                       
<%-- END TOGGLE OFF PATIENT PRIVACY CONSENT --%> 

<%-- TOGGLE OFF MEDITECH MODULE --%>                                                     
<% if (oscarProps.isPropertyActive("meditech_id")) { %>
												<tr>
													<td align="right"><b>Meditech ID: </b></td>
													<td align="left"><input type="text" name="meditech_id"
														size="30"
														value="<%=OtherIdManager.getDemoOtherId(
									demographic_no, "meditech_id")%>">
														<input type="hidden" name="meditech_idOrig"
														value="<%=OtherIdManager.getDemoOtherId(
									demographic_no, "meditech_id")%>">
													</td>
												</tr>
												<%
													}
												%>
<%-- END TOGGLE OFF MEDITECH MODULE --%>

<%-- TOGGLE OFF EXTRA DEMO FIELDS (NATIVE HEALTH) --%>							
<%
								if (oscarProps.getProperty("EXTRA_DEMO_FIELDS") != null) {
												String fieldJSP = oscarProps
														.getProperty("EXTRA_DEMO_FIELDS");
												fieldJSP += ".jsp";
							%>
	<jsp:include page="<%=fieldJSP%>">
		<jsp:param name="demo" value="<%=demographic_no%>" />
	</jsp:include>
<%}%>

<%-- END TOGGLE OFF EXTRA DEMO FIELDS (NATIVE HEALTH) --%>	

<%-- WAITING LIST MODULE --%>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_WAITING_LIST" value="true">

					
							<tr valign="top">
								<td colspan="4">
								<table border="0" cellspacing="0" cellpadding="0" width="100%" id="waitingListTable">
								
								<tr id="waitingListHeading" style="display:none;">
									<th colspan="4" class="alignLeft" >Waiting List</th>
								</tr>
									<tr>
										<td align="right" width="16%" nowrap><b>
										<bean:message key="demographic.demographiceditdemographic.msgWaitList"/>:</b></td>
										<td align="left" width="31%">
										<%
										
										List<org.oscarehr.common.model.WaitingList> wls = waitingListDao.search_wlstatus(Integer.parseInt(demographic_no));
									
 	                        String wlId="", listID="", wlnote="";
 	                        String wlReferralDate="";
                                if (wls.size()>0){
                                	org.oscarehr.common.model.WaitingList wl = wls.get(0);
                                    wlId = wl.getId().toString();
                                    listID =String.valueOf(wl.getListId());
                                    wlnote =wl.getNote();
                                    wlReferralDate =oscar.util.ConversionUtils.toDateString(wl.getOnListSince());
                                    if(wlReferralDate != null  &&  wlReferralDate.length()>10){
                                        wlReferralDate = wlReferralDate.substring(0, 11);
                                    }
                                }
                               
                               %> <input type="hidden" name="wlId"
											value="<%=wlId%>"> <select name="list_id">
											<%if("".equals(wLReadonly)){%>
											<option value="0"><bean:message key="demographic.demographiceditdemographic.optSelectWaitList"/></option>
											<%}else{%>
											<option value="0">
											<bean:message key="demographic.demographiceditdemographic.optCreateWaitList"/></option>
											<%} %>
											<%
											
									List<WaitingListName> wlns = waitingListNameDao.findCurrentByGroup(((org.oscarehr.common.model.ProviderPreference)session.getAttribute(org.oscarehr.util.SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE)).getMyGroupNo());
                                     for(WaitingListName wln:wlns) {
                                    %>
											<option value="<%=wln.getId()%>"
												<%=wln.getId().toString().equals(listID)?" selected":""%>>
											<%=wln.getName()%></option>
											<%
                                      }
                                     
                                    %>
										</select></td>
										<td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.msgWaitListNote"/>: </b></td>
										<td align="left"><input type="text"
											name="waiting_list_note" value="<%=wlnote%>" size="34"
											<%=wLReadonly%>></td>
									</tr>
									<tr>
										<td colspan="2">&nbsp;</td>
										<td align="right" nowrap><b><bean:message key="demographic.demographiceditdemographic.msgDateOfReq"/>: </b></td>
										<td align="left"><input type="text"
											name="waiting_list_referral_date"
											id="waiting_list_referral_date" size="11"
											value="<%=wlReferralDate%>" <%=wLReadonly%>><img
											src="../images/cal.gif" id="referral_date_cal"><bean:message key="schedule.scheduletemplateapplying.msgDateFormat"/>
										</td>

									</tr>
								</table>
								</td>
							</tr>
</oscar:oscarPropertiesCheck>
<%-- END WAITING LIST MODULE --%>



<%-- AUTHOR DENNIS WARREN O/A COLCAMEX RESOURCES --%>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PATIENT_HEALTH_CARE_TEAM" value="true">
	<tr><td colspan="4" >
		<jsp:include page="manageHealthCareTeam.jsp">
			<jsp:param name="demographicNo" value="<%= demographic_no %>" />
		</jsp:include>
	</td></tr>	
</oscar:oscarPropertiesCheck>
<%-- END AUTHOR DENNIS WARREN O/A COLCAMEX RESOURCES --%>

<%-- TOGGLED OFF PROGRAM ADMISSIONS --%>
<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_PROGRAM_ADMISSIONS" value="true">
							
			<tr valign="top">
			    <td colspan="4">
			        <table border="1" width="100%">
			            <tr bgcolor="#CCCCFF">
			                <td colspan="2" >Program Admissions</td>
			            </tr>
			            <tr>
			                <td>Residential Status<font color="red">:</font></td>
			                <td>Service Programs</td>
			            </tr>
			            <tr>
			                <td>
                                <select id="rsid" name="rps">
                                	<option value=""></option>
                                    <%
                                        GenericIntakeEditAction gieat = new GenericIntakeEditAction();
                                        gieat.setProgramManager(pm);
                                     
                                        
                                        String _pvid =loggedInInfo.getLoggedInProviderNo();
                                        Set<Program> pset = gieat.getActiveProviderProgramsInFacility(loggedInInfo,_pvid,loggedInInfo.getCurrentFacility().getId());
                                        List<Program> bedP = gieat.getBedPrograms(pset,_pvid);
                                        List<Program> commP = gieat.getCommunityPrograms();
                      	                Program oscarp = programDao.getProgramByName("OSCAR");
                      	                
                      	                
                                        for(Program _p:bedP){
                                    %>
                                        <option value="<%=_p.getId()%>" <%=isProgramSelected(bedAdmission, _p.getId()) %>><%=_p.getName()%></option>
                                    <%
                                        }
                                        
                                      %>
                                </select>
                                
			                </td>
			                <td>
			                    <%
			                    	ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);
			                    	List<Program> servP = programManager.getServicePrograms();
			                       
			                        for(Program _p:servP){
			                        	boolean readOnly=false;
			                        	if(!pset.contains(_p)) {
			                        		readOnly=true;
			                        	}
			                        	String selected = isProgramSelected(serviceAdmissions, _p.getId());
			                        	
			                        	if(readOnly && selected.length() == 0) {
			                        		continue;
			                        	}
			                        	
			                    %>
			                        <input type="checkbox" name="sp" value="<%=_p.getId()%>" <%=selected %> <%=(readOnly)?" disabled=\"disabled\" ":"" %> />
			                        <%=_p.getName()%>
			                        <br/>
			                    <%}%>
			                </td>
			            </tr>
			        </table>
			    </td>
			</tr>

</oscar:oscarPropertiesCheck>
<%-- END TOGGLE OFF PROGRAM ADMISSIONS --%>							
							
							<% // customized key
if(oscarVariables.getProperty("demographicExt") != null) {
    boolean bExtForm = oscarVariables.getProperty("demographicExtForm") != null ? true : false;
    String [] propDemoExtForm = bExtForm ? (oscarVariables.getProperty("demographicExtForm","").split("\\|") ) : null;
	String [] propDemoExt = oscarVariables.getProperty("demographicExt","").split("\\|");
	for(int k=0; k<propDemoExt.length; k=k+2) {
%>
							<tr valign="top" bgcolor="#CCCCFF">
								<td align="right" nowrap><b><%=propDemoExt[k]%>: </b></td>
								<td align="left">
								<% if(bExtForm) {
                                  	if(propDemoExtForm[k].indexOf("<select")>=0) {
                                		out.println(propDemoExtForm[k].replaceAll("value=\""+StringUtils.trimToEmpty(demoExt.get(propDemoExt[k].replace(' ', '_')))+"\"" , "value=\""+StringUtils.trimToEmpty(demoExt.get(propDemoExt[k].replace(' ', '_')))+"\"" + " selected") );
                                  	} else {
                              			out.println(propDemoExtForm[k].replaceAll("value=\"\"", "value=\""+StringUtils.trimToEmpty(demoExt.get(propDemoExt[k].replace(' ', '_')))+"\"" ) );
                                  	}
                              	 } else { %> <input type="text"
									name="<%=propDemoExt[k].replace(' ', '_')%>"
									value="<%=StringUtils.trimToEmpty(demoExt.get(propDemoExt[k].replace(' ', '_')))%>" />
								<% }  %> <input type="hidden"
									name="<%=propDemoExt[k].replace(' ', '_')%>Orig"
									value="<%=StringUtils.trimToEmpty(demoExt.get(propDemoExt[k].replace(' ', '_')))%>" />
								</td>
								<% if((k+1)<propDemoExt.length) { %>
								<td align="right" nowrap><b>
								<%out.println(propDemoExt[k+1]+":");%> </b></td>
								<td align="left">
								<% if(bExtForm) {
                                  	if(propDemoExtForm[k+1].indexOf("<select")>=0) {
                                		out.println(propDemoExtForm[k+1].replaceAll("value=\""+StringUtils.trimToEmpty(demoExt.get(propDemoExt[k+1].replace(' ', '_')))+"\"" , "value=\""+StringUtils.trimToEmpty(demoExt.get(propDemoExt[k+1].replace(' ', '_')))+"\"" + " selected") );
                                  	} else {
                              			out.println(propDemoExtForm[k+1].replaceAll("value=\"\"", "value=\""+StringUtils.trimToEmpty(demoExt.get(propDemoExt[k+1].replace(' ', '_')))+"\"" ) );
                                  	}
                              	 } else { %> <input type="text"
									name="<%=propDemoExt[k+1].replace(' ', '_')%>"
									value="<%=StringUtils.trimToEmpty(demoExt.get(propDemoExt[k+1].replace(' ', '_')))%>" />
								<% }  %> <input type="hidden"
									name="<%=propDemoExt[k+1].replace(' ', '_')%>Orig"
									value="<%=StringUtils.trimToEmpty(demoExt.get(propDemoExt[k+1].replace(' ', '_')))%>" />
								</td>
								<% } else {%>
								<td>&nbsp;</td>
								<td>&nbsp;</td>
								<% }  %>
							</tr>
							<% 	}
}
if(oscarVariables.getProperty("demographicExtJScript") != null) { out.println(oscarVariables.getProperty("demographicExtJScript")); }
%>


<tr valign="top">
<td nowrap colspan="4">
<b><bean:message key="demographic.demographiceditdemographic.rxInteractionWarningLevel" /></b>
<input type="hidden" name="rxInteractionWarningLevelOrig"
									value="<%=StringUtils.trimToEmpty(demoExt.get("rxInteractionWarningLevel"))%>" />
					<select id="rxInteractionWarningLevel" name="rxInteractionWarningLevel">
						<option value="0" <%=(warningLevel.equals("0")?"selected=\"selected\"":"") %>>Not Specified</option>
						<option value="1" <%=(warningLevel.equals("1")?"selected=\"selected\"":"") %>>Low</option>
						<option value="2" <%=(warningLevel.equals("2")?"selected=\"selected\"":"") %>>Medium</option>
						<option value="3" <%=(warningLevel.equals("3")?"selected=\"selected\"":"") %>>High</option>
						<option value="4" <%=(warningLevel.equals("4")?"selected=\"selected\"":"") %>>None</option>
					</select>
					<oscar:oscarPropertiesCheck property="INTEGRATOR_LOCAL_STORE" value="yes">
					<b><bean:message key="demographic.demographiceditdemographic.primaryEMR" />:</b>

				    <%
				       	String primaryEMR = demoExt.get("primaryEMR");
				       	if(primaryEMR==null) primaryEMR="0";
				    %>
					<input type="hidden" name="primaryEMROrig" value="<%=StringUtils.trimToEmpty(demoExt.get("primaryEMR"))%>" />
					<select id="primaryEMR" name="primaryEMR">
						<option value="0" <%=(primaryEMR.equals("0")?"selected=\"selected\"":"") %>>No</option>
						<option value="1" <%=(primaryEMR.equals("1")?"selected=\"selected\"":"") %>>Yes</option>
					</select>
					</oscar:oscarPropertiesCheck>

</td>
</tr> 
<%-- PATIENT NOTES MODULE --%>		
							<tr valign="top">
								<td nowrap colspan="4">
								<table width="100%" bgcolor="#EEEEFF" id="demographicPatientNotes">
								<tr id="paitientNotesHeading" style="display:none;">
									<th colspan="2" class="alignLeft" >Patient Notes</th>
								</tr>
								
									<tr>
										<td width="7%" align="right"><font color="#FF0000"><b><bean:message
											key="demographic.demographiceditdemographic.formAlert" />: </b></font></td>
										<td><textarea name="alert" style="width: 100%" cols="80" 
											rows="2" maxlength="255"><%=alert%></textarea></td>
									</tr>
									<tr>
										<td align="right"><b><bean:message
											key="demographic.demographiceditdemographic.formNotes" />: </b></td>
										<td><textarea name="notes" style="width: 100%" cols="60"><%=notes%></textarea>
										</td>
									</tr>
								</table>
								</td>
							</tr>

						</table>

						</td>
					</tr>
<%-- END PATIENT NOTES MODULE --%>	

<%-- Site MODULE --%>
<tr>

			<td>
				<table>
				<tr><td>
				<bean:message key="admin.provider.sitesAssigned" /><font color="red">:</font>
				</td>
				<div class="sites">
				<td>
<% 

if (org.oscarehr.common.IsPropertiesOn.isMultisitesEnable()) { 
	List<Integer> siteIDs = new ArrayList<Integer>();
	boolean isSiteAccessPrivacy=false;	
	isSiteAccessPrivacy=true; 	
	ProviderSiteDao psDao = (ProviderSiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("providerSiteDao");
	List<ProviderSite> psList = psDao.findByProviderNo(curProvider_no);
	
	SuperSiteUtil superSiteUtil = SuperSiteUtil.getInstance(curProvider_no);
	
	for(ProviderSite ps : psList) {
		/*if(!superSiteUtil.isUserAllowedToAdmitDischargeForSite(ps.getId().getSiteId()))
			continue;*/
		siteIDs.add(ps.getId().getSiteId());
	}	

	ProviderDao pDao = (ProviderDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("providerDao");
		
	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");	
	DemographicSiteDao dsiteDao = (DemographicSiteDao)SpringUtils.getBean("demographicSiteDao");
	List<DemographicSite> dsites = dsiteDao.findSitesByDemographicId(Integer.valueOf(demographic_no));
	
	String visibilityStr = "";
	for (int i=0; i<siteIDs.size(); i++) {
		boolean inSite = false;
		
		visibilityStr = "";
		if(!superSiteUtil.isUserAllowedToAdmitDischargeForSite(siteIDs.get(i)))
			visibilityStr = "visibility: hidden;";
			
		for(DemographicSite ds : dsites) {
			if(ds.getSiteId().equals(siteIDs.get(i))) {
				inSite = true;
				break;
			}
		}
		String s = "";
		s = siteDao.getById(siteIDs.get(i)).getName();

%>	<span style="<%=visibilityStr%>">	
	<input type="checkbox" name="sites" value="<%= siteIDs.get(i) %>" <%=  inSite?"checked='checked'":"" %>>
	<%= s %>
	</span>

<%
	}
	
	//find demographicsite in which provider is not mapped .. and add checkboxes for that too.
	//so that it gets updated properly.
	for(DemographicSite ds : dsites) {
		
		//if demographic is mapped with site.. but provided is not mapped.
		//then show the checkbox only

		//hide this.. as provider will not have access to this site.
		visibilityStr = "visibility: hidden;";
		if(!siteIDs.contains(ds.getSiteId()))
		{
			%>
			<span style="<%=visibilityStr%>">	
			<input type="checkbox" name="sites" value="<%= ds.getSiteId() %>" checked="checked">			
			</span>
			<%
		}
	}
}
%>
		</td>
		</div>
		</tr>
		</table>
		
		</td>
		</tr>	
<%-- END Site MODULE --%>

<%-- BOTTOM TOOLBAR  --%>				
					<tr class="darkPurple">
						<td colspan="4">
						<table border="0" width="100%" cellpadding="0" cellspacing="0">
							<tr>
								<td width="30%" valign="top">
								<input type="hidden" name="dboperation" value="update_record"> 
								<oscar:oscarPropertiesCheck property="E2EViewerEnabled" value="true"> 
									<security:oscarSec roleName="<%=roleName$%>" objectName="_demographicExport" rights="r" reverse="<%=false%>">
										<input type="button" value="<bean:message key="demographic.demographiceditdemographic.msgViewAsE2ECDA"/>" onclick="window.open('${pageContext.request.contextPath}/ws/rs/e2eCDA/<%=demographic.getDemographicNo()%>');">
									</security:oscarSec>
								</oscar:oscarPropertiesCheck>
								
								<security:oscarSec roleName="<%=roleName$%>" objectName="_demographicExport" rights="r" reverse="<%=false%>">
								<input type="button" value="<bean:message key="demographic.demographiceditdemographic.msgExport"/>"
									onclick="window.open('demographicExport.jsp?demographicNo=<%=demographic.getDemographicNo()%>');">
								</security:oscarSec>
								
								<br>
								<input type="button" name="Button" id="cancelButton" class="leftButton top" value="Exit Master Record"	onclick="self.close();">
								</td>
								<td width="30%" align='center' valign="top"><input
									type="hidden" name="displaymode" value="Update Record">
								<!-- security code block --> <span id="updateButton"
									style="display: none;"> <security:oscarSec
									roleName="<%=roleName$%>" objectName="_demographic" rights="w">
									<%
										boolean showCbiReminder=oscarProps.getBooleanProperty("CBI_REMIND_ON_UPDATE_DEMOGRAPHIC", "true");
									%>
									<input type="submit" <%=(showCbiReminder?"onclick='showCbiReminder()'":"")%>
										value="<bean:message key="demographic.demographiceditdemographic.btnUpdate"/>">
								</security:oscarSec> </span> <!-- security code block --></td>
								<td width="40%" align='right' valign="top"><span
									id="swipeButton" style="display: none;"> <input
									type="button" name="Button"
									value="<bean:message key="demographic.demographiceditdemographic.btnSwipeCard"/>"
									onclick="window.open('zdemographicswipe.jsp','', 'scrollbars=yes,resizable=yes,width=600,height=300, top=360, left=0')">
								</span> <!--input type="button" name="Button" value="<bean:message key="demographic.demographiceditdemographic.btnSwipeCard"/>" onclick="javascript:window.alert('Health Card Number Already Inuse');"-->
									<input type="button" size="110" name="Button"
									    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFEnvelope"/>"
									    onclick="popupPage(400,700,'<%=printEnvelope%><%=demographic.getDemographicNo()%>');return false;">
									<input type="button" size="110" name="Button"
									    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFLabel"/>"
									    onclick="popupPage(400,700,'<%=printLbl%><%=demographic.getDemographicNo()%>&appointment_no=<%=appointment%>');return false;">
									<input type="button" size="110" name="Button"
									    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFAddressLabel"/>"
									    onclick="popupPage(400,700,'<%=printAddressLbl%><%=demographic.getDemographicNo()%>');return false;">
									<input type="button" size="110" name="Button"
									    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePDFChartLabel"/>"
									    onclick="popupPage(400,700,'<%=printChartLbl%><%=demographic.getDemographicNo()%>');return false;">
									    <%
											if(oscarVariables.getProperty("showSexualHealthLabel", "false").equals("true")) {
										%>
									<input type="button" size="110" name="Button"
									    value="<bean:message key="demographic.demographiceditdemographic.btnCreatePublicHealthLabel"/>"
									    onclick="popupPage(400,700,'<%=printSexHealthLbl%><%=demographic.getDemographicNo()%>');return false;">
									    <% } %>
									<input type="button" name="Button" size="110"
									    value="<bean:message key="demographic.demographiceditdemographic.btnPrintLabel"/>"
									    onclick="popupPage(600,800,'<%=printHtmlLbl%><%=demographic.getDemographicNo()%>');return false;">
									<input type="button" size="110" name="Button"
									    value="<bean:message key="demographic.demographiceditdemographic.btnClientLabLabel"/>"
									    onclick="popupPage(400,700,'<%=printLabLbl%><%=demographic.getDemographicNo()%>');return false;">
								</td>
                                                        </tr>
						</table>
<%-- END BOTTOM TOOLBAR  --%>

						<%
							if (ConformanceTestHelper.enableConformanceOnlyTestFeatures)
							{
								String styleBut = "";
								if(ConformanceTestHelper.hasDifferentRemoteDemographics(loggedInInfo, Integer.parseInt(demographic$))){
                                                                       styleBut = " style=\"background-color:yellow\" ";
                                                                }%>
									<input type="button" value="Compare with Integrator" <%=styleBut%>  onclick="popup(425, 600, 'DiffRemoteDemographics.jsp?demographicId=<%=demographic$%>', 'RemoteDemoWindow')" />
									<input type="button" value="Update latest integrated demographics information" onclick="document.location='<%=request.getContextPath()%>/demographic/copyLinkedDemographicInfoAction.jsp?demographicId=<%=demographic$%>&<%=request.getQueryString()%>'" />
									<input type="button" value="Send note to integrated provider" onclick="document.location='<%=request.getContextPath()%>/demographic/followUpSelection.jsp?demographicId=<%=demographic$%>'" />
								<%
							}
						%>
						</td>
					</tr>
				</table>
				
                                </form>
				<%
                    }
                  }
                %>

		</table>
		</td>
	</tr>
	<tr>
		<td class="MainTableBottomRowLeftColumn"></td>
		<td class="MainTableBottomRowRightColumn"></td>
	</tr>
</table>

<oscar:oscarPropertiesCheck property="DEMOGRAPHIC_WAITING_LIST" value="true">
<script type="text/javascript">
Calendar.setup({ inputField : "waiting_list_referral_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "referral_date_cal", singleClick : true, step : 1 });
</script>
</oscar:oscarPropertiesCheck>

<script type="text/javascript">




Calendar.setup({ inputField : "paper_chart_archived_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "archive_date_cal", singleClick : true, step : 1 });


function callEligibilityWebService(url,id){

       var ran_number=Math.round(Math.random()*1000000);
       var params = "demographic=<%=demographic_no%>&method=checkElig&rand="+ran_number;  //hack to get around ie caching the page
		 var response;
       new Ajax.Request(url+'?'+params, {
           onSuccess: function(response) {
                document.getElementById(id).innerHTML=response.responseText ;
                document.getElementById('search_spinner').innerHTML="";
           }
        } );
 }

<%
if (privateConsentEnabled) {
%>
jQuery(document).ready(function(){
	var countryOfOrigin = jQuery("#countryOfOrigin").val();
	if("US" != countryOfOrigin) {
		jQuery("#usSigned").hide();
	} else {
		jQuery("#usSigned").show();
	}
	
	jQuery("#countryOfOrigin").change(function () {
		var countryOfOrigin = jQuery("#countryOfOrigin").val();
		if("US" == countryOfOrigin){
		   	jQuery("#usSigned").show();
		} else {
			jQuery("#usSigned").hide();
		}
	});
});
<%
}
%>
</script>
</body>
</html:html>


<%!

	public String getDisabled(String fieldName) {
		String val = OscarProperties.getInstance().getProperty("demographic.edit."+fieldName,"");
		if(val != null && val.equals("disabled")) {
			return " disabled=\"disabled\" ";
		}

		return "";
}

%>

<%!
public String isProgramSelected(Admission admission, Integer programId) {
	if(admission != null && admission.getProgramId() != null && admission.getProgramId().equals(programId)) {
		return " selected=\"selected\" ";
	}
	
	return "";
}

	public String isProgramSelected(List<Admission> admissions, Integer programId) {
		for(Admission admission:admissions) {
			if(admission.getProgramId() != null && admission.getProgramId().equals(programId)) {
				return " checked=\"checked\" ";
			}
		}
		return "";
	}

	public boolean shouldShowForThisProvider(String[] programs, ProgramProvider pp) {
		if(pp!=null){
			for(int x=0;x<programs.length;x++){
				if(programs[x].equals(pp.getProgramId().toString())) {
					return true;
				}
			}
		}
		return false;
	}
%>

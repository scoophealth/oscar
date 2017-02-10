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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_con" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_con");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="org.oscarehr.util.WebUtilsOld"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!-- add for special encounter -->
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<!-- end -->
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar"%>


<%@page import="java.util.ArrayList, java.util.Collections, java.util.List, java.util.*, oscar.util.StringUtils, oscar.dms.*, oscar.oscarEncounter.pageUtil.*,oscar.oscarEncounter.data.*, oscar.OscarProperties, oscar.oscarLab.ca.on.*"%>
<%@page import="org.oscarehr.casemgmt.service.CaseManagementManager,org.oscarehr.casemgmt.model.CaseManagementNote,org.oscarehr.casemgmt.model.Issue,org.oscarehr.common.model.UserProperty,org.oscarehr.common.dao.UserPropertyDAO,org.springframework.web.context.support.*,org.springframework.web.context.*"%>

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.util.WebUtils, oscar.SxmlMisc"%>
<%@page import="oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestForm"%>
<%@page import="oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil"%>
<%@page import="oscar.oscarDemographic.data.DemographicData"%>
<%@page import="oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctViewRequestAction"%>
<%@page import="org.oscarehr.util.MiscUtils,oscar.oscarClinic.ClinicData"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils" %>
<%@ page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="org.oscarehr.util.DigitalSignatureUtils"%>
<%@ page import="org.oscarehr.ui.servlet.ImageRenderingServlet"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.util.MiscUtils, org.oscarehr.PMmodule.caisi_integrator.CaisiIntegratorManager, org.oscarehr.caisi_integrator.ws.CachedDemographicNote"%>
<%@page import="org.oscarehr.PMmodule.dao.ProgramDao, org.oscarehr.PMmodule.model.Program" %>
<%@page import="oscar.oscarDemographic.data.DemographicData, oscar.oscarRx.data.RxProviderData, oscar.oscarRx.data.RxProviderData.Provider, oscar.oscarClinic.ClinicData"%>
<%@ page import="org.oscarehr.common.dao.FaxConfigDao, org.oscarehr.common.model.FaxConfig" %>
<%@page import="org.oscarehr.common.dao.ConsultationServiceDao" %>
<%@page import="org.oscarehr.common.model.ConsultationServices" %>
<jsp:useBean id="displayServiceUtil" scope="request" class="oscar.oscarEncounter.oscarConsultationRequest.config.pageUtil.EctConDisplayServiceUtil" />

<html:html locale="true">

<%! boolean bMultisites=org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);

	displayServiceUtil.estSpecialist();	

	//multi-site support
	String appNo = request.getParameter("appNo");
	appNo = (appNo==null ? "" : appNo);

	String defaultSiteName = "";
	int defaultSiteIdx = 0;
	Integer defaultSiteId = 0;
	Vector<String> vecAddressName = new Vector<String>() ;
	Vector<String> bgColor = new Vector<String>() ;
	Vector<Integer> siteIds = new Vector<Integer>();
	String siteIdJS = null;
	String siteNameJS = null;
	String siteAddressJS = null;
	String sitePhoneJS = null;
	String siteFaxJS = null;
	String siteProviderNoJS = null;
	String siteProviderNameJS = null;
	if (bMultisites) {
		SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
		ProviderDao providerDao = (ProviderDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("providerDao");
		
		List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));
		List<org.oscarehr.common.model.Provider> siteProviders = null;
		if (sites != null) {
			siteIdJS = "var siteIdList = new Array();";
			siteNameJS = "var siteNameList = new Array();";
			siteAddressJS = "var siteAddressList = new Array();";
			sitePhoneJS = "var sitePhoneList = new Array();";
			siteFaxJS = "var siteFaxList = new Array();";
		    siteProviderNoJS = "var siteProviderNoList = new Array();";
		    siteProviderNameJS = "var siteProviderNameList = new Array();";
			int i = 0;
			for (Site s:sites) {
				   siteIds.add(s.getSiteId());
		           vecAddressName.add(s.getName());
		           bgColor.add(s.getBgColor());
		           
		           siteIdJS += "siteIdList["+i+"]='"+s.getId() + "';";
		           siteNameJS += "siteNameList["+i+"]='"+s.getName().replaceAll("'", "\\\\'")+"';";
		           siteAddressJS += "siteAddressList["+i+"]='"+s.getAddress().replaceAll("'", "\\\\'")+", "+s.getCity().replaceAll("'", "\\\\'")+", "+s.getProvince().replaceAll("'", "\\\\'")+ " " +s.getPostal().replaceAll("'", "\\\\'") +"';";
		           sitePhoneJS += "sitePhoneList["+i+"]='"+s.getPhone().replaceAll("'", "\\\\'")+"';";
		           siteFaxJS += "siteFaxList["+i+"]='"+s.getFax().replaceAll("'", "\\\\'")+"';";
		           if(s.getId().toString().equals(defaultSiteId) || sites.size()==1) {
		               defaultSiteIdx = i;
		               defaultSiteId = s.getId();
		           }
	               siteProviders = providerDao.getProvidersBySiteLocation(s.getId().toString());
		           int j = 0;
		           siteProviderNoJS += "var sno"+i+"=new Array();";
		           siteProviderNameJS += "var sname"+i+"=new Array();";
		           Collections.sort(siteProviders, new org.oscarehr.common.model.Provider().ComparatorName());
		           for(org.oscarehr.common.model.Provider siteProvider : siteProviders) {
		               siteProviderNoJS += "sno"+i+"["+j+"]='"+siteProvider.getProviderNo()+"';";
		               siteProviderNameJS += "sname"+i+"["+j+"]='"+siteProvider.getFirstName().replaceAll("'", "\\\\'")+" "+siteProvider.getLastName().replaceAll("'", "\\\\'")+"';";
			       j++;
		           }
		           siteProviderNoJS += "siteProviderNoList["+i+"]=sno"+i+";";
		           siteProviderNameJS += "siteProviderNameList["+i+"]=sname"+i+";";
		           i++;
		 	}
		}

		if (appNo != "") {
			defaultSiteName = siteDao.getSiteNameByAppointmentNo(appNo);
		}
	}
	String demo_mrp = null;
	String demo = request.getParameter("de");
		String requestId = request.getParameter("requestId");
		// segmentId is != null when viewing a remote consultation request from an hl7 source
		String segmentId = request.getParameter("segmentId");
		String team = request.getParameter("teamVar");
		String providerNo = (String)session.getAttribute("user");
		String providerNoFromChart = null;
		DemographicData demoData = null;
		org.oscarehr.common.model.Demographic demographic = null;

		RxProviderData rx = new RxProviderData();
		List<Provider> prList = rx.getAllProviders();
		Provider thisProvider = rx.getProvider(providerNo);
		ClinicData clinic = new ClinicData();
		
		EctConsultationFormRequestUtil consultUtil = new EctConsultationFormRequestUtil();
		
		if (requestId != null) consultUtil.estRequestFromId(loggedInInfo, requestId);
		if (demo == null) demo = consultUtil.demoNo;

		ArrayList<String> users = (ArrayList<String>)session.getServletContext().getAttribute("CaseMgmtUsers");
		boolean useNewCmgmt = false;
		WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
		CaseManagementManager cmgmtMgr = null;
		if (users != null && users.size() > 0 && (users.get(0).equalsIgnoreCase("all") || Collections.binarySearch(users, providerNo) >= 0))
		{
			useNewCmgmt = true;
			cmgmtMgr = (CaseManagementManager)ctx.getBean("caseManagementManager");
		}

		UserPropertyDAO userPropertyDAO = (UserPropertyDAO)ctx.getBean("UserPropertyDAO");
		UserProperty fmtProperty = userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_REQ_PASTE_FMT);
		String pasteFmt = fmtProperty != null?fmtProperty.getValue():null;

		if (demo != null)
		{
			demoData = new oscar.oscarDemographic.data.DemographicData();
			demographic = demoData.getDemographic(loggedInInfo, demo);
			
			providerNoFromChart = demographic.getProviderNo();
		}
		else if (requestId == null && segmentId == null)
		{
			MiscUtils.getLogger().debug("Missing both requestId and segmentId.");
		}
		demo_mrp = demographic.getProviderNo();

		if (demo != null) consultUtil.estPatient(loggedInInfo, demo);
		consultUtil.estActiveTeams();

		if (request.getParameter("error") != null)
		{
%>
<SCRIPT LANGUAGE="JavaScript">
        alert("The form could not be printed due to an error. Please refer to the server logs for more details.");
    </SCRIPT>
<%
	}

		java.util.Calendar calender = java.util.Calendar.getInstance();
		String day = Integer.toString(calender.get(java.util.Calendar.DAY_OF_MONTH));
		String mon = Integer.toString(calender.get(java.util.Calendar.MONTH) + 1);
		String year = Integer.toString(calender.get(java.util.Calendar.YEAR));
		String formattedDate = year + "/" + mon + "/" + day;

		OscarProperties props = OscarProperties.getInstance();
		ConsultationServiceDao consultationServiceDao = SpringUtils.getBean(ConsultationServiceDao.class);
		
		// Look up list
		org.oscarehr.managers.LookupListManager lookupListManager = SpringUtils.getBean(org.oscarehr.managers.LookupListManager.class);
		pageContext.setAttribute("appointmentInstructionList", lookupListManager.findLookupListByName( loggedInInfo, "consultApptInst") ); 
%><head>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<script>
	var ctx = '<%=request.getContextPath()%>';
	var requestId = '<%=request.getParameter("requestId")%>';
	var demographicNo = '<%=demo%>';
	var demoNo = '<%=demo%>';
	var appointmentNo = '<%=appNo%>';
	<%=(siteIdJS!=null ? siteIdJS : "")%>;
	<%=(siteNameJS!=null ? siteNameJS : "")%>;
	<%=(siteAddressJS!=null ? siteAddressJS : "")%>;
	<%=(sitePhoneJS!=null ? sitePhoneJS : "")%>;
	<%=(siteFaxJS!=null ? siteFaxJS : "")%>;
	<%=(siteProviderNoJS!=null ? siteProviderNoJS : "")%>;
	<%=(siteProviderNameJS!=null ? siteProviderNameJS : "")%>;
	var user = <%= loggedInInfo.getLoggedInProviderNo() %>;
	var mrp = <%=demo_mrp%>;
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/global.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery_oscar_defaults.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/prototype.js"></script>
<link rel="stylesheet" type="text/css" media="all"
	href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript"
	src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>

   <script src="<c:out value="${ctx}/js/jquery.js"/>"></script>
   <script>
     jQuery.noConflict();
   </script>


	<oscar:customInterface section="conreq"/>

<title><bean:message
	key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.title" />
</title>
<html:base />
<style type="text/css">

/* Used for "import from enctounter" button */
input.btn{
   color:black;
   font-family:'trebuchet ms',helvetica,sans-serif;
   font-size:84%;
   font-weight:bold;
   background-color:#B8B8FF;
   border:1px solid;
   border-top-color:#696;
   border-left-color:#696;
   border-right-color:#363;
   border-bottom-color:#363;
}

.doc {
    color:blue;
}

.lab {
    color: #CC0099;
}

.hrm {
    color: green;
}

td.tite {

background-color: #bbbbFF;
color : black;
font-size: 12pt;

}

td.tite1 {

background-color: #ccccFF;
color : black;
font-size: 12pt;

}

th,td.tite2 {

background-color: #BFBFFF;
color : black;
font-size: 12pt;

}

td.tite3 {

background-color: #B8B8FF;
color : black;
font-size: 12pt;

}

td.tite4 {

background-color: #ddddff;
color : black;
font-size: 12pt;

}

td.stat{
font-size: 10pt;
}

.consultDemographicData input, .consultDemographicData select, .consultDemographicData textarea {
    width: 100% !important;
}

input#referalDate, input#appointmentDate, input#followUpDate {
   background-image: url( ../../images/cal.gif);
   background-position-x: right;
   background-position-y: center;
   background-repeat: no-repeat;
}

input#referalDate {
   background-image: url( ../../images/cal.gif);
   background-position-x: right;
   background-position-y: center;
   background-repeat: no-repeat;
}

#referalDate_cal, #appointmentDate_cal, #followUpDate_cal  {
    display: none !important;
}

* table tr td {
    vertical-align:top !important;
}


textarea {
    width: 100%;
}

</style>
</head>



<link type="text/javascript" src="../consult.js" />

<script language="JavaScript" type="text/javascript">

var servicesName = new Object();   		// used as a cross reference table for name and number
var services = new Array();				// the following are used as a 2D table for makes and models
var specialists = new Array();
var specialistFaxNumber = "";
<%oscar.oscarEncounter.oscarConsultationRequest.config.data.EctConConfigurationJavascriptData configScript;
				configScript = new oscar.oscarEncounter.oscarConsultationRequest.config.data.EctConConfigurationJavascriptData();
				out.println(configScript.getJavascript());%>

/////////////////////////////////////////////////////////////////////
function initMaster() {
	makeSpecialistslist(2);
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
// create car make objects and fill arrays
//==========
function K( serviceNumber, service ){

	//servicesName[service] = new ServicesName(serviceNumber);
        servicesName[service] = serviceNumber;
	services[serviceNumber] = new Service( );
}
//-------------------------------------------------------------------
	
	//-----------------disableDateFields() disables date fields if "Patient Will Book" selected
	var disableFields=false;
</script>

<oscar:oscarPropertiesCheck defaultVal="false" value="true" property="CONSULTATION_PATIENT_WILL_BOOK">
	<script type="text/javascript" >

	
	function disableDateFields(){
		if(document.forms[0].patientWillBook.checked){
			setDisabledDateFields(document.forms[0], true);
		}
		else{
			setDisabledDateFields(document.forms[0], false);
		}
	}
	</script>
</oscar:oscarPropertiesCheck>

<oscar:oscarPropertiesCheck defaultVal="false" value="false" property="CONSULTATION_PATIENT_WILL_BOOK">
	<script type="text/javascript" >
	
	function disableDateFields(){

		setDisabledDateFields(document.forms[0], false);

	}
	</script>
</oscar:oscarPropertiesCheck>


<script type="text/javascript" >
// btnReminders
jQuery(document).ready(function(){
	jQuery(".clinicalData").click(function(){
		var data = new Object();
		var target = "#" + this.id.split("_")[1];		
		data.method = this.id.split("_")[0];
		data.demographicNo = <%= demo %>;
		getClinicalData( data, target )
	});
})

function getClinicalData( data, target ) {
	jQuery.ajax({
		method : "POST",
		url : "${ pageContext.request.contextPath }/oscarConsultationRequest/consultationClinicalData.do",
		data : data,
		dataType : 'JSON',
		success: function(data) {
			var json = JSON.parse(data);
			jQuery(target).val( jQuery(target).val() + "\n" + json.note );			
		}
	});
}

function setDisabledDateFields(form, disabled)
{
	//form.appointmentYear.disabled = disabled;
	//form.appointmentMonth.disabled = disabled;
	//form.appointmentDay.disabled = disabled;
	form.appointmentHour.disabled = disabled;
	form.appointmentMinute.disabled = disabled;
	form.appointmentPm.disabled = disabled;
}

function disableEditing()
{
	if (disableFields)
	{
		form=document.forms[0];

		setDisabledDateFields(form, disableFields);

		form.status[0].disabled = disableFields;
		form.status[1].disabled = disableFields;
		form.status[2].disabled = disableFields;
		form.status[3].disabled = disableFields;

		form.referalDate.disabled = disableFields;
		form.service.disabled = disableFields;
		form.urgency.disabled = disableFields;
		form.phone.disabled = disableFields;
		form.fax.disabled = disableFields;
		form.address.disabled = disableFields;
		form.patientWillBook.disabled = disableFields;
		form.sendTo.disabled = disableFields;

		form.appointmentNotes.disabled = disableFields;
		form.reasonForConsultation.disabled = disableFields;
		form.clinicalInformation.disabled = disableFields;
		form.concurrentProblems.disabled = disableFields;
		form.currentMedications.disabled = disableFields;
		form.allergies.disabled = disableFields;
                form.annotation.disabled = disableFields;

		disableIfExists(form.update, disableFields);
		disableIfExists(form.updateAndPrint, disableFields);
		disableIfExists(form.updateAndSendElectronically, disableFields);
		disableIfExists(form.updateAndFax, disableFields);

		disableIfExists(form.submitSaveOnly, disableFields);
		disableIfExists(form.submitAndPrint, disableFields);
		disableIfExists(form.submitAndSendElectronically, disableFields);
		disableIfExists(form.submitAndFax, disableFields);
	}
}

function disableIfExists(item, disabled)
{
	if (item!=null) item.disabled=disabled;
}

//------------------------------------------------------------------------------------------
/////////////////////////////////////////////////////////////////////
// create car model objects and fill arrays
//=======
function D( servNumber, specNum, phoneNum ,SpecName,SpecFax,SpecAddress){
    var specialistObj = new Specialist(servNumber,specNum,phoneNum, SpecName, SpecFax, SpecAddress);
    services[servNumber].specialists.push(specialistObj);
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function Specialist(makeNumber,specNum,phoneNum,SpecName, SpecFax, SpecAddress){

	this.specId = makeNumber;
	this.specNbr = specNum;
	this.phoneNum = phoneNum;
	this.specName = SpecName;
	this.specFax = SpecFax;
	this.specAddress = SpecAddress;
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
// make name constructor
function ServicesName( makeNumber ){

	this.serviceNumber = makeNumber;
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
// make constructor
function Service(  ){

	this.specialists = new Array();
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
// construct model selection on page
function fillSpecialistSelect( aSelectedService ){


	var selectedIdx = aSelectedService.selectedIndex;
	var makeNbr = (aSelectedService.options[ selectedIdx ]).value;

	document.EctConsultationFormRequestForm.specialist.options.selectedIndex = 0;
	document.EctConsultationFormRequestForm.specialist.options.length = 1;

	document.EctConsultationFormRequestForm.phone.value = ("");
	document.EctConsultationFormRequestForm.fax.value = ("");
	document.EctConsultationFormRequestForm.address.value = ("");

	if ( selectedIdx == 0)
	{
		return;
	}

        var i = 1;
	var specs = (services[makeNbr].specialists);
	for ( var specIndex = 0; specIndex < specs.length; ++specIndex ){
		   aPit = specs[ specIndex ];	   	
           document.EctConsultationFormRequestForm.specialist.options[ i++ ] = new Option( aPit.specName , aPit.specNbr );
	}

}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function fillSpecialistSelect1( makeNbr )
{
	//document.EctConsultationFormRequestForm.specialist.options.length = 1;

	var specs = (services[makeNbr].specialists);
	var i=1;
    var match = false;
        
	for ( var specIndex = 0; specIndex < specs.length; ++specIndex )
	{
		aPit = specs[specIndex];

		if(aPit.specNbr=="<%=consultUtil.specialist%>"){
			//look for matching specialist on spec list and make option selected
			match=true;
			document.EctConsultationFormRequestForm.specialist.options[i] = new Option(aPit.specName, aPit.specNbr,false ,true );
		}else{
			//add specialist on list as normal
			document.EctConsultationFormRequestForm.specialist.options[i] = new Option(aPit.specName, aPit.specNbr );
		}

		i++;
	}

	<%if(requestId!=null){ %>
		if(!match){ 
			//if no match then most likely doctor has been removed from specialty list so just add specialist
			document.EctConsultationFormRequestForm.specialist.options[0] = new Option("<%=consultUtil.getSpecailistsName(consultUtil.specialist)%>", "<%=consultUtil.specialist%>",false ,true );

			//don't display if no consultant was saved
			<%if(!consultUtil.specialist.equals("null")){%>
			document.getElementById("consult-disclaimer").style.display='inline';
			<%}else{%>
			//display so user knows why field is empty
			document.EctConsultationFormRequestForm.specialist.options[0] = new Option("No Consultant Saved", "-1");
			<%}%>
		}
	<%}%>

}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function setSpec(servNbr,specialNbr){
//    //window.alert("get Called");
    specs = (services[servNbr].specialists);
//    //window.alert("got specs");
    var i=1;
    var NotSelected = true;
 
    for ( var specIndex = 0; specIndex < specs.length; ++specIndex ){
//      //  window.alert("loop");
        aPit = specs[specIndex];
        if (aPit.specNbr == specialNbr){
//        //    window.alert("if");
            document.EctConsultationFormRequestForm.specialist.options[i].selected = true;
            NotSelected = false;
        }

        i++;
    }

    if( NotSelected )
        document.EctConsultationFormRequestForm.specialist.options[0].selected = true;
//    window.alert("exiting");

}
//=------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
//insert first option title into specialist drop down list select box
function initSpec() {
	<%if(requestId==null){ %>
	var aSpecialist = services["-1"].specialists[0];
    document.EctConsultationFormRequestForm.specialist.options[0] = new Option(aSpecialist.specNbr, aSpecialist.specId);
    <%}%>
}

/////////////////////////////////////////////////////////////////////
function initService(ser,name,spec,specname,phone,fax,address){
	var i = 0;
	var isSel = 0;
	var strSer = new String(ser);
	var strNa = new String(name);
	var strSpec = new String(spec);
	var strSpecNa = new String(specname);
	var strPhone = new String(phone);
	var strFax = new String(fax);
	var strAddress = new String(address);

	var isSerDel=1;//flagging service if deleted: 1=deleted 0=active

	$H(servicesName).each(function(pair){
	if( pair.value == strSer ) {
	isSerDel = 0;
	}
	});

	if (isSerDel==1 && strSer != "null") {
	K(strSer,strNa);
	D(strSer,strSpec,strPhone,strSpecNa,strFax,strAddress);
    }

        $H(servicesName).each(function(pair){
              var opt = new Option( pair.key, pair.value );
              if( pair.value == strSer ) {
                opt.selected = true;
                fillSpecialistSelect1( pair.value );
              }
              $("service").options.add(opt);

        });

/*	for (aIdx in servicesName){
	   var serNBR = servicesName[aIdx].serviceNumber;
   	   document.EctConsultationFormRequestForm.service.options[ i ] = new Option( aIdx, serNBR );
	   if (serNBR == strSer){
	      document.EctConsultationFormRequestForm.service.options[ i ].selected = true;
	      isSel = 1;
          //window.alert("get here"+serNBR);
	      fillSpecialistSelect1( serNBR );
          //window.alert("and here");
	   }
	   if (isSel != 1){
	      document.EctConsultationFormRequestForm.service.options[ 0 ].selected = true;
	   }
	   i++;
	}*/
	}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function onSelectSpecialist(SelectedSpec)	{
	var selectedIdx = SelectedSpec.selectedIndex;
	var form=document.EctConsultationFormRequestForm;

	if (selectedIdx==null || selectedIdx==-1 || (SelectedSpec.options[ selectedIdx ]).value == "-1") {   		//if its the first item set everything to blank
		form.phone.value = ("");
		form.fax.value = ("");
		form.address.value = ("");

		enableDisableRemoteReferralButton(form, true);

		<%
		if (props.isConsultationFaxEnabled()) {//
		%>
		specialistFaxNumber = "";
		updateFaxButton();
		<% } %>
		
		return;
	}
	var selectedService = document.EctConsultationFormRequestForm.service.value;  				// get the service that is selected now
	var specs = (services[selectedService].specialists); 			// get all the specs the offer this service
    
	// load the text fields with phone fax and address for past consult review even if spec has been removed from service list
	<%if(requestId!=null && !consultUtil.specialist.equals("null")){ %>
	form.phone.value = '<%=StringEscapeUtils.escapeJavaScript(consultUtil.specPhone)%>';
	form.fax.value = '<%=StringEscapeUtils.escapeJavaScript(consultUtil.specFax)%>';					
	form.address.value = '<%=StringEscapeUtils.escapeJavaScript(consultUtil.specAddr) %>';

	//make sure this dislaimer is displayed
	document.getElementById("consult-disclaimer").style.display='inline';
	<%}%>
	
								
        for( var idx = 0; idx < specs.length; ++idx ) {
            aSpeci = specs[idx];									// get the specialist Object for the currently selected spec
            if( aSpeci.specNbr == SelectedSpec.value ) {
            	form.phone.value = (aSpeci.phoneNum.replace(null,""));
            	form.fax.value = (aSpeci.specFax.replace(null,""));					// load the text fields with phone fax and address
            	form.address.value = (aSpeci.specAddress.replace(null,""));
            	
       			//since there is a match make sure the dislaimer is hidden
       			document.getElementById("consult-disclaimer").style.display='none';
        	
            	<%
        		if (props.isConsultationFaxEnabled()) {//
				%>
				specialistFaxNumber = aSpeci.specFax.trim();
				updateFaxButton();
        		<% } %>
            	
            	jQuery.getJSON("getProfessionalSpecialist.json", {id: aSpeci.specNbr},
                    function(xml)
                    {
                		var hasUrl=xml.eDataUrl!=null&&xml.eDataUrl!="";
                		enableDisableRemoteReferralButton(form, !hasUrl);

                                var annotation = document.getElementById("annotation");
                                annotation.value = xml.annotation;
                	}
            	);

            	break;
            }
        }//spec loop
	 
	}

//-----------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function FillThreeBoxes(serNbr)	{

	var selectedService = document.EctConsultationFormRequestForm.service.value;  				// get the service that is selected now
	var specs = (services[selectedService].specialists);					// get all the specs the offer this service

        for( var idx = 0; idx < specs.length; ++idx ) {
            aSpeci = specs[idx];									// get the specialist Object for the currently selected spec
            if( aSpeci.specNbr == serNbr ) {
                document.EctConsultationFormRequestForm.phone.value = (aSpeci.phoneNum);
                document.EctConsultationFormRequestForm.fax.value = (aSpeci.specFax);					// load the text fields with phone fax and address
                document.EctConsultationFormRequestForm.address.value = (aSpeci.specAddress);
                <%
        		if (props.isConsultationFaxEnabled()) {//
				%>
				specialistFaxNumber = aSpeci.specFax.trim();
				updateFaxButton();
				<% } %>
                break;
           }
        }
}
//-----------------------------------------------------------------

function enableDisableRemoteReferralButton(form, disabled)
{
	var button=form.updateAndSendElectronically;
	if (button!=null) button.disabled=disabled;
	button=form.submitAndSendElectronically;
	if (button!=null) button.disabled=disabled;

        var button=form.updateAndSendElectronicallyTop;
	if (button!=null) button.disabled=disabled;
	button=form.submitAndSendElectronicallyTop;
	if (button!=null) button.disabled=disabled;
}

//-->

function BackToOscar() {
       window.close();
}
function rs(n,u,w,h,x){
	args="width="+w+",height="+h+",resizalbe=yes,scrollbars=yes,status=0,top=60,left=30";
        remote=window.open(u,n,args);
        if(remote != null){
	   if (remote.opener == null)
		remote.opener = self;
	}
	if ( x == 1 ) { return remote; }
}

var DocPopup = null;
function popup(location) {
    DocPopup = window.open(location,"_blank","height=380,width=580");

    if (DocPopup != null) {
        if (DocPopup.opener == null) {
            DocPopup.opener = self;
        }
    }
}

function popupAttach( height, width, url, windowName){
  var page = url;
  windowprops = "height="+height+",width="+width+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=0,left=0";
  var popup=window.open(url, windowName, windowprops);
  if (popup != null){
    if (popup.opener == null){
      popup.opener = self;
    }
  }
  popup.focus();
  return false;
}

function popupOscarCal(vheight,vwidth,varpage) { //open a new popup window
  var page = varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=no,menubars=no,toolbars=no,resizable=no,screenX=0,screenY=0,top=20,left=20";
  var popup=window.open(varpage, "<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCal"/>", windowprops);

  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self;
    }
  }
}



function checkForm(submissionVal,formName){
    //if document attach to consultation is still active user needs to close before submitting
    if( DocPopup != null && !DocPopup.closed ) {
        alert("Please close Consultation Documents window before proceeding");
        return false;
    }

   var msg = "<bean:message key="Errors.service.noServiceSelected"/>";
   msg  = msg.replace('<li>','');
   msg  = msg.replace('</li>','');
  if (document.EctConsultationFormRequestForm.service.options.selectedIndex == 0){
     alert(msg);
     document.EctConsultationFormRequestForm.service.focus();
     return false;
  }
  $("saved").value = "true";
  document.forms[formName].submission.value=submissionVal;
  document.forms[formName].submit();
  return true;
}

//enable import from encounter
function importFromEnct(reqInfo,txtArea)
{
    var info = "";
    switch( reqInfo )
    {
        case "MedicalHistory":
            <%String value = "";
				if (demo != null)
				{
					if (useNewCmgmt)
					{
						value = listNotes(cmgmtMgr, "MedHistory", providerNo, demo);
					}
					else
					{
						oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request), demo);
						value = EctInfo.getMedicalHistory();
					}
					if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
					{
						value = StringUtils.lineBreaks(value);
					}
					value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
					out.println("info = '" + value + "'");
				}%>
             break;
          case "ongoingConcerns":
             <%if (demo != null)
				{
					if (useNewCmgmt)
					{
						value = listNotes(cmgmtMgr, "Concerns", providerNo, demo);
					}
					else
					{
						oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demo);
						value = EctInfo.getOngoingConcerns();
					}
					if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
					{
						value = StringUtils.lineBreaks(value);
					}
					value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
					out.println("info = '" + value + "'");
				}%>
             break;
           case "FamilyHistory":
              <%if (demo != null)
				{
					if (OscarProperties.getInstance().getBooleanProperty("caisi", "on"))
					{
						oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demo);
						value = EctInfo.getFamilyHistory();
					}
					else
					{
						if (useNewCmgmt)
						{
							value = listNotes(cmgmtMgr, "FamHistory", providerNo, demo);
						}
						else
						{
							oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demo);
							value = EctInfo.getFamilyHistory();
						}
					}
					if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
					{
						value = StringUtils.lineBreaks(value);
					}
					value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
					out.println("info = '" + value + "'");
				}%>
              break;
            case "OtherMeds":
              <%if (demo != null)
				{
					if (OscarProperties.getInstance().getBooleanProperty("caisi", "on"))
					{
						value = "";
					}
					else
					{
						if (useNewCmgmt)
						{
							value = listNotes(cmgmtMgr, "OMeds", providerNo, demo);
						}
						else
						{
							//family history was used as bucket for Other Meds in old encounter
							oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demo);
							value = EctInfo.getFamilyHistory();
						}
					}
					if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
					{
						value = StringUtils.lineBreaks(value);
					}
					value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
					out.println("info = '" + value + "'");

				}%>
                break;
            case "Reminders":
              <%if (demo != null)
				{
					if (useNewCmgmt)
					{
						value = listNotes(cmgmtMgr, "Reminders", providerNo, demo);
					}
					else
					{
						oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation(LoggedInInfo.getLoggedInInfoFromSession(request),demo);
						value = EctInfo.getReminders();
					}
					//if( !value.equals("") ) {
					if (pasteFmt == null || pasteFmt.equalsIgnoreCase("single"))
					{
						value = StringUtils.lineBreaks(value);
					}

					value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
					out.println("info = '" + value + "'");
					//}
				}%>
    } //end switch

    if( txtArea.value.length > 0 && info.length > 0 )
        txtArea.value += '\n';

    txtArea.value += info;
    txtArea.scrollTop = txtArea.scrollHeight;
    txtArea.focus();

}



function updateAttached() {
    var t = setTimeout('fetchAttached()', 2000);
}

function fetchAttached() {
    var updateElem = 'tdAttachedDocs';
    var params = "demo=<%=demo%>&requestId=<%=requestId%>";
    var url = "<rewrite:reWrite jspPage="displayAttachedFiles.jsp" />";

    var objAjax = new Ajax.Request (
                url,
                {
                    method: 'get',
                    parameters: params,
                    onSuccess: function(request) {
                                    $(updateElem).innerHTML = request.responseText;
                                },
                    onFailure: function(request) {
                                    $(updateElem).innerHTML = "<h3>Error: " + + request.status + "</h3>";
                                }
                }

            );

}

function addCCName(){
        if (document.EctConsultationFormRequestForm.ext_cc.value.length<=0)
                document.EctConsultationFormRequestForm.ext_cc.value=document.EctConsultationFormRequestForm.docName.value;
        else document.EctConsultationFormRequestForm.ext_cc.value+="; "+document.EctConsultationFormRequestForm.docName.value;
}

</script>


<script>

var providerData = new Object(); //{};
<%
for (Provider p : prList) {
	if (!p.getProviderNo().equalsIgnoreCase("-1")) {
		String prov_no = "prov_"+p.getProviderNo();

		%>
	 providerData['<%=prov_no%>'] = new Object(); //{};

	providerData['<%=prov_no%>'].address = "<%=p.getFullAddress() %>";
	providerData['<%=prov_no%>'].phone = "<%=p.getClinicPhone().trim() %>";
	providerData['<%=prov_no%>'].fax = "<%=p.getClinicFax().trim() %>";

<%	}
}

ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
List<Program> programList = programDao.getAllActivePrograms();

if (OscarProperties.getInstance().getBooleanProperty("consultation_program_letterhead_enabled", "true")) {
	if (programList != null) {
		for (Program p : programList) {
			String progNo = "prog_" + p.getId();
%>
		providerData['<%=progNo %>'] = new Object();
		providerData['<%=progNo %>'].address = "<%=(p.getAddress() != null && p.getAddress().trim().length() > 0) ? p.getAddress().trim() : ((clinic.getClinicAddress() + "  " + clinic.getClinicCity() + "   " + clinic.getClinicProvince() + "  " + clinic.getClinicPostal()).trim()) %>";
		providerData['<%=progNo %>'].phone = "<%=(p.getPhone() != null && p.getPhone().trim().length() > 0) ? p.getPhone().trim() : clinic.getClinicPhone().trim() %>";
		providerData['<%=progNo %>'].fax = "<%=(p.getFax() != null && p.getFax().trim().length() > 0) ? p.getFax().trim() : clinic.getClinicFax().trim() %>";
<%
		}
	}
} %>


function switchProvider(value) {
	if (!<%=bMultisites%>) {
		if (value==-1) {
			document.getElementById("letterheadName").value = value;
			document.getElementById("letterheadAddress").value = "<%=(clinic.getClinicAddress() + "  " + clinic.getClinicCity() + "   " + clinic.getClinicProvince() + "  " + clinic.getClinicPostal()).trim() %>";
			document.getElementById("letterheadAddressSpan").innerHTML = "<%=(clinic.getClinicAddress() + "  " + clinic.getClinicCity() + "   " + clinic.getClinicProvince() + "  " + clinic.getClinicPostal()).trim() %>";
			document.getElementById("letterheadPhone").value = "<%=clinic.getClinicPhone().trim() %>";
			document.getElementById("letterheadPhoneSpan").innerHTML = "<%=clinic.getClinicPhone().trim() %>";
			document.getElementById("letterheadFax").value = "<%=clinic.getClinicFax().trim() %>";
			// document.getElementById("letterheadFaxSpan").innerHTML = "<%=clinic.getClinicFax().trim() %>";
		} else {
			if (typeof providerData["prov_" + value] != "undefined")
				value = "prov_" + value;

			document.getElementById("letterheadName").value = value;
			document.getElementById("letterheadAddress").value = providerData[value]['address'];
			document.getElementById("letterheadAddressSpan").innerHTML = providerData[value]['address'].replace(" ", "&nbsp;");
			document.getElementById("letterheadPhone").value = providerData[value]['phone'];
			document.getElementById("letterheadPhoneSpan").innerHTML = providerData[value]['phone'];
			document.getElementById("letterheadFax").value = providerData[value]['fax'];
			//document.getElementById("letterheadFaxSpan").innerHTML = providerData[value]['fax'];
		}
	}
}

function switchSite(value) {
	document.getElementById("letterheadAddress").value = siteAddressList[value];
	document.getElementById("letterheadAddressSpan").innerHTML = siteAddressList[value].replace(" ", "&nbsp;");
	document.getElementById("letterheadPhone").value = sitePhoneList[value];
	document.getElementById("letterheadPhoneSpan").innerHTML = sitePhoneList[value];
	document.getElementById("letterheadFax").value = siteFaxList[value];
	document.getElementById("letterheadFaxSpan").innerHTML = siteFaxList[value];
	
	var hasProvider = false;
	var providerSelect = document.getElementById("letterheadName");
	providerSelect.options.length=siteProviderNoList[value].length;
	for(i=0;i<siteProviderNoList[value].length;i++) {
	    var isSelected = false;		   
	    providerSelect.options[i]=new Option(siteProviderNameList[value][i],siteProviderNoList[value][i],isSelected,false);		
	    if('<%= consultUtil.letterheadName %>' == providerSelect.options[i].value) {
	    	providerSelect.options[i].selected = true;
	    	hasProvider = true;
	    }
	}
	if(!hasProvider) {
	    if(providerSelect.options[i].value == mrp)  
	    	providerSelect.options[i].selected = true;
	    else if(providerSelect.options[i].value == user) 
	    	providerSelect.options[i].selected = true;
	}    
}

</script>
<script type="text/javascript">
<%
String signatureRequestId=DigitalSignatureUtils.generateSignatureRequestId(loggedInInfo.getLoggedInProviderNo());
String imageUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_preview.name()+"&"+DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY+"="+signatureRequestId;
String storedImgUrl=request.getContextPath()+"/imageRenderingServlet?source="+ImageRenderingServlet.Source.signature_stored.name()+"&digitalSignatureId=";
%>
var POLL_TIME=1500;
var counter=0;
function refreshImage()
{
	counter=counter+1;
	document.getElementById('signatureImgTag').src='<%=imageUrl%>&rand='+counter;
	document.getElementById('signatureImg').value='<%=signatureRequestId%>';
}

function showSignatureImage()
{
	if (document.getElementById('signatureImg') != null && document.getElementById('signatureImg').value.length > 0) {
		document.getElementById('signatureImgTag').src = "<%=storedImgUrl %>" + document.getElementById('signatureImg').value;

		<% if (OscarProperties.getInstance().getBooleanProperty("topaz_enabled", "true")) { 
		  //this is empty
		%>

		document.getElementById('clickToSign').style.display = "none";

		<% } else { 
		  //this is empty
		%>

		document.getElementById("signatureFrame").style.display = "none";

		<% } %>


		document.getElementById('signatureShow').style.display = "block";
	}

	return true;
}

<%
String userAgent = request.getHeader("User-Agent");
String browserType = "";
if (userAgent != null) {
	if (userAgent.toLowerCase().indexOf("ipad") > -1) {
		browserType = "IPAD";
	} else {
		browserType = "ALL";
	}
}
%>

function requestSignature()
{


	<% if (OscarProperties.getInstance().getBooleanProperty("topaz_enabled", "true")) { %>
	document.getElementById('newSignature').value = "true";
	document.getElementById('signatureShow').style.display = "block";
	document.getElementById('clickToSign').style.display = "none";
	document.getElementById('signatureShow').style.display = "block";
	setInterval('refreshImage()', POLL_TIME);
	document.location='<%=request.getContextPath()%>/signature_pad/topaz_signature_pad.jnlp.jsp?<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>';

	<% } %>
}

var isSignatureDirty = false;
var isSignatureSaved = <%= consultUtil.signatureImg != null && !"".equals(consultUtil.signatureImg) ? "true" : "false" %>;

function signatureHandler(e) {
	isSignatureDirty = e.isDirty;
	isSignatureSaved = e.isSave;
	<%
	if (props.isConsultationFaxEnabled()) { //
	%>
	updateFaxButton();
	<% } %>
	if (e.isSave) {
		refreshImage();
		document.getElementById('newSignature').value = "true";
	}
	else {
		document.getElementById('newSignature').value = "false";
	}
}

var requestIdKey = "<%=signatureRequestId %>";

function AddOtherFaxProvider() {
	var selected = jQuery("#otherFaxSelect option:selected");
	_AddOtherFax(selected.text(),selected.val());
}
function AddOtherFax() {
	var number = jQuery("#otherFaxInput").val();
	if (checkPhone(number)) {
		_AddOtherFax(number,number);
	}
	else {
		alert("The fax number you entered is invalid.");
	}
}

function _AddOtherFax(name, number) {
	var remove = "<a href='javascript:void(0);' onclick='removeRecipient(this)'>remove</a>";
	var html = "<li>"+name+"<b>, Fax No: </b>"+number+ " " +remove+"<input type='hidden' name='faxRecipients' value='"+number+"'></input></li>";
	jQuery("#faxRecipients").append(jQuery(html));
	updateFaxButton();
}

function checkPhone(str)
{
	var phone =  /^((\+\d{1,3}(-| )?\(?\d\)?(-| )?\d{1,5})|(\(?\d{2,6}\)?))(-| )?(\d{3,4})(-| )?(\d{4})(( x| ext)\d{1,5}){0,1}$/
	if (str.match(phone)) {
   		return true;
 	} else {
 		return false;
 	}
}

function removeRecipient(el) {
	var el = jQuery(el);
	if (el) { el.parent().remove(); updateFaxButton(); }
	else { alert("Unable to remove recipient."); }
}

function hasFaxNumber() {
	return specialistFaxNumber.length > 0 || jQuery("#faxRecipients").children().size() > 0;
}
function updateFaxButton() {
	var disabled = !hasFaxNumber();
	document.getElementById("fax_button").disabled = disabled;
	document.getElementById("fax_button2").disabled = disabled;
}
</script>

<%=WebUtilsOld.popErrorMessagesAsAlert(session)%>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" 
	onload="window.focus();disableDateFields();fetchAttached();disableEditing();showSignatureImage();">
<html:errors />
<html:form action="/oscarEncounter/RequestConsultation"
	onsubmit="alert('HTHT'); return false;">
	<%
		EctConsultationFormRequestForm thisForm = (EctConsultationFormRequestForm)request.getAttribute("EctConsultationFormRequestForm");

		if (requestId != null)
		{
			EctViewRequestAction.fillFormValues(LoggedInInfo.getLoggedInInfoFromSession(request), thisForm, new Integer(requestId));
                thisForm.setSiteName(consultUtil.siteName);
                defaultSiteName = consultUtil.siteName ;

		}
		else if (segmentId != null)
		{
			EctViewRequestAction.fillFormValues(thisForm, segmentId);
                thisForm.setSiteName(consultUtil.siteName);
                defaultSiteName = consultUtil.siteName ;
		}
		else if (request.getAttribute("validateError") == null)
		{
			//  new request
			if (demo != null)
			{
				oscar.oscarDemographic.data.RxInformation RxInfo = new oscar.oscarDemographic.data.RxInformation();
                EctViewRequestAction.fillFormValues(thisForm,consultUtil);
				
                if( "true".equalsIgnoreCase( props.getProperty("CONSULTATION_AUTO_INCLUDE_ALLERGIES", "true") ) ) { 
                	String allergies = RxInfo.getAllergies( loggedInInfo, demo );
					thisForm.setAllergies( allergies );
                }
                
                if( "true".equalsIgnoreCase( props.getProperty( "CONSULTATION_AUTO_INCLUDE_MEDICATIONS", "true" ) ) ) {
					if (props.getProperty("currentMedications", "").equalsIgnoreCase("otherMedications"))
					{
						oscar.oscarDemographic.data.EctInformation EctInfo = new oscar.oscarDemographic.data.EctInformation( loggedInInfo, demo );
						thisForm.setCurrentMedications(EctInfo.getFamilyHistory());
					}
					else
					{
						thisForm.setCurrentMedications(RxInfo.getCurrentMedication(demo));
					}
                }
                
				team = consultUtil.getProviderTeam(consultUtil.mrp);
			}

			thisForm.setStatus("1");

			thisForm.setSendTo(team);

       		if (bMultisites) {
        		thisForm.setSiteName(defaultSiteName);
       		}
		}

		if (thisForm.iseReferral())
		{
			%>
				<SCRIPT LANGUAGE="JavaScript">
					disableFields=true;
				</SCRIPT>
			<%
		}


	%>

	<% if (!props.isConsultationFaxEnabled() || !OscarProperties.getInstance().isPropertyActive("consultation_dynamic_labelling_enabled")) { %>
	<input type="hidden" name="providerNo" value="<%=providerNo%>">
	<% } %>
	<input type="hidden" name="demographicNo" value="<%=demo%>">
	<input type="hidden" name="requestId" value="<%=requestId%>">
	<input type="hidden" name="documents" value="">
	<input type="hidden" name="ext_appNo" value="<%=request.getParameter("appNo") %>">
	<input type="hidden" name="source" value="<%=(requestId!=null)?thisForm.getSource():request.getParameter("source") %>">
	
        <input type="hidden" id="saved" value="false">
	<!--  -->
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn">Consultation</td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td class="Header"
						style="padding-left: 2px; padding-right: 2px; border-right: 2px solid #003399; text-align: left; font-size: 80%; font-weight: bold; width: 100%;"
						NOWRAP>
						<h2>
						<%=thisForm.getPatientName()%> <%=thisForm.getPatientSex()%>	<%=thisForm.getPatientAge()%>
						</h2>
						</td>
				</tr>
			</table>
			</td>
		</tr>
		<tr style="vertical-align: top">
			<td class="MainTableLeftColumn">
			<table>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat" colspan="2"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCreated" />:</td>
						</tr>
						<tr>
							<td class="stat" colspan="2"  nowrap><%=thisForm.getProviderName()%>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgStatus" />
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="status" value="1" />
							</td>
							<td class="stat"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNoth" />:
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="status" value="2" />
							</td>
							<td class="stat"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSpecCall" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="status" value="3" />
							</td>
							<td class="stat"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPatCall" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="status" value="4" />
							</td>
							<td class="stat"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCompleted" /></td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat">&nbsp;</td>
						</tr>
						<tr>
							<td style="text-align: center" class="stat">
							<%
								if (thisForm.iseReferral())
								{
									%>
										<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.attachDoc" />
									<%
								}
								else
								{
									%>
									<% if (OscarProperties.getInstance().isPropertyActive("consultation_indivica_attachment_enabled")) { %>
									<a href="#" onclick="popup('<rewrite:reWrite jspPage="attachConsultation2.jsp"/>?provNo=<%=consultUtil.providerNo%>&demo=<%=demo%>&requestId=<%=requestId%>');return false;">
										<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.attachDoc" />
									</a>
									<% } else { %>
									<a href="#" onclick="popup('<rewrite:reWrite jspPage="attachConsultation.jsp"/>?provNo=<%=consultUtil.providerNo%>&demo=<%=demo%>&requestId=<%=requestId%>');return false;">
										<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.attachDoc" />
									</a>
									<% }
								}
							%>
							</td>
						</tr>
						<tr>
							<td style="text-align: center"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.curAttachDoc"/>:</td>
						</tr>
						<tr>
							<td id="tdAttachedDocs"></td>
						</tr>
						<tr>
							<td style="text-align: center"><bean:message
								key="oscarEncounter.oscarConsultationRequest.AttachDoc.Legend" /><br />
							<span class="doc"><bean:message
								key="oscarEncounter.oscarConsultationRequest.AttachDoc.LegendDocs" /></span><br />
							<span class="lab"><bean:message
								key="oscarEncounter.oscarConsultationRequest.AttachDoc.LegendLabs" /></span><br/>
							<span class="hrm legend"><bean:message
							key="oscarEncounter.oscarConsultationRequest.AttachDoc.LegendHrm" /></span>
							</td>
						</tr>
					</table>
					</td>
				</tr>
			</table>
			</td>
			<td class="MainTableRightColumn">
			<table cellpadding="0" cellspacing="2"
				style="border-collapse: collapse" bordercolor="#111111" width="100%"
				height="100%" border=1>

				<!----Start new rows here-->
				<tr>
					<td class="tite4" colspan=2>
					<% boolean faxEnabled = props.isConsultationFaxEnabled(); %>
					<% if (request.getAttribute("id") != null) { %>
						<input name="update" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdate"/>" onclick="return checkForm('Update Consultation Request','EctConsultationFormRequestForm');" />
						<input name="updateAndPrint" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndPrint"/>" onclick="return checkForm('Update Consultation Request And Print Preview','EctConsultationFormRequestForm');" />
						<input name="printPreview" type="button" value="Print Preview" onclick="return checkForm('And Print Preview','EctConsultationFormRequestForm');" />
												
						<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
							<input name="updateAndSendElectronicallyTop" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndSendElectronicReferral"/>" 
								onclick="return checkForm('Update_esend','EctConsultationFormRequestForm');" />
						</logic:equal>
			
						<oscar:oscarPropertiesCheck value="yes" property="faxEnable">
							<input id="fax_button" name="updateAndFax" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndFax"/>" onclick="return checkForm('Update And Fax','EctConsultationFormRequestForm');" />
						</oscar:oscarPropertiesCheck>

					<% } else { %>
						<input name="submitSaveOnly" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmit"/>" onclick="return checkForm('Submit Consultation Request','EctConsultationFormRequestForm'); " />
						<input name="submitAndPrint" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndPrint"/>" onclick="return checkForm('Submit Consultation Request And Print Preview','EctConsultationFormRequestForm'); " />

						<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
							<input name="submitAndSendElectronicallyTop" type="button" 
							value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndSendElectronicReferral"/>" 
							onclick="return checkForm('Submit_esend','EctConsultationFormRequestForm');" />
						</logic:equal>
				
						<oscar:oscarPropertiesCheck value="yes" property="faxEnable">
							<input id="fax_button" name="submitAndFax" type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndFax"/>" onclick="return checkForm('Submit And Fax','EctConsultationFormRequestForm');" />
						</oscar:oscarPropertiesCheck>

						<input type="button" value="Send eResponse" onclick="$('saved').value='true';document.location='<%=thisForm.getOruR01UrlString(request)%>'" />
					<% } %>
					<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
						<input type="button" value="Send eResponse" onclick="$('saved').value='true';document.location='<%=thisForm.getOruR01UrlString(request)%>'" />
					</logic:equal>
					
					</td>
                    </tr>
                    <tr class="consultDemographicData" >
					<td>

					<table height="100%" width="100%">
						<% if (props.isConsultationFaxEnabled() && OscarProperties.getInstance().isPropertyActive("consultation_dynamic_labelling_enabled")) { %>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.consultationFormPrint.msgAssociated2" />:</td>
							<td  class="tite1">
								<html:select property="providerNo" onchange="switchProvider(this.value)">
									<%
										for (Provider p : prList) {
											if (p.getProviderNo().compareTo("-1") != 0) {
									%>
									<option value="<%=p.getProviderNo() %>" <%=((consultUtil.providerNo != null && consultUtil.providerNo.equalsIgnoreCase(p.getProviderNo())) || (consultUtil.providerNo == null &&  providerNo.equalsIgnoreCase(p.getProviderNo())) ? "selected='selected'" : "") %>>
										<%=p.getFirstName() %> <%=p.getSurname() %>
									</option>
									<% }

								}
								%>
								</html:select>
							</td>
						</tr>
						<% } %>
						<tr>						
						<td class="tite4">
							<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formRefDate" />:
						</td>
								
						<oscar:oscarPropertiesCheck value="false" property="CONSULTATION_LOCK_REFERRAL_DATE">	
	                            <td class="tite3">
	                            <img alt="calendar" id="referalDate_cal" src="../../images/cal.gif" /> 
								<%
								if (request.getAttribute("id") != null)	{
								%> 
								<html:text styleId="referalDate" property="referalDate" ondblclick="this.value='';"/>
								 <%
	 							} else {
	 							%> 
	 							<html:text styleId="referalDate" property="referalDate" ondblclick="this.value='';" value="<%=formattedDate%>"/>
	 							<%
	 							}
		 						%>
								</td>							
						</oscar:oscarPropertiesCheck>
						
						<oscar:oscarPropertiesCheck value="true" property="CONSULTATION_LOCK_REFERRAL_DATE">
						
								<td  class="tite3" >
									<html:text styleId="referalDate" property="referalDate" 
										readonly="true"  value="<%=formattedDate%>" />
								</td>
													
						</oscar:oscarPropertiesCheck>

						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formService" />:
							</td>
							<td  class="tite1">
								<html:select styleId="service" property="service" onchange="fillSpecialistSelect(this);">
							</html:select></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formCons" />:
							</td>
							<td  class="tite2">
							<%
								if (thisForm.iseReferral())
								{
									%>
										<%=thisForm.getProfessionalSpecialistName()%>
									<%
								}
								else
								{
									%>
									
									<span id="consult-disclaimer" title="When consult was saved this was the saved consultant but is no longer on this specialist list." style="display:none;font-size:24px;">*</span> <html:select styleId="specialist" property="specialist" size="1" onchange="onSelectSpecialist(this)">
									
									</html:select>
									
									
									<%
								}
							%>
							</td>
						</tr>
                                                <tr>
                                                    <td class="tite4">
                                                        <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formInstructions" />
                                                    </td>
                                                    <td  class="tite2">
                                                        <textarea id="annotation" style="color: blue;" readonly></textarea>
                                                    </td>
                                                </tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formUrgency" /></td>
							<td  class="tite2">
								<html:select property="urgency">
									<html:option value="2">
										<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNUrgent" />
									</html:option>
									<html:option value="1">
										<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgUrgent" />
									</html:option>
									<html:option value="3">
										<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgReturn" />
									</html:option>
								</html:select>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formPhone" />:
							</td>
							<td  class="tite2"><input type="text" name="phone" class="righty" value="<%=thisForm.getProfessionalSpecialistPhone()%>" /></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formFax" />:
							</td>
							<td  class="tite3"><input type="text" name="fax" class="righty" /></td>
						</tr>

						<tr>
							<td class="tite4">
								<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAddr" />:
							</td>
							<td  class="tite3">
								<textarea name="address" cols=20 ><%=thisForm.getProfessionalSpecialistAddress()%></textarea>
							</td>
						</tr>
	
						<oscar:oscarPropertiesCheck defaultVal="false" value="true" property="CONSULTATION_APPOINTMENT_INSTRUCTIONS_LOOKUP">
							<tr>
								<td class="tite4">
									<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.appointmentInstr" />
								</td>
								<td  class="tite3">														
									<html:select property="appointmentInstructions" styleId="appointmentInstructions" >
										<html:option value="" ></html:option>
										<c:forEach items="${ appointmentInstructionList.items }" var="appointmentInstruction">
											<%-- Ensure that only active items are shown --%>
											<c:if test="${ appointmentInstruction.active }" >
												<html:option value="${ appointmentInstruction.value }" >
													<c:out value="${ appointmentInstruction.label }" />
												</html:option>
											</c:if>
										</c:forEach>								
									</html:select>
								</td>
							</tr>
						</oscar:oscarPropertiesCheck>	
						<oscar:oscarPropertiesCheck defaultVal="false" value="true" property="CONSULTATION_PATIENT_WILL_BOOK">
							<tr>
								<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formPatientBook" />:</td>
								<td  class="tite3"><html:checkbox property="patientWillBook" value="1" onclick="disableDateFields()">
								</html:checkbox></td>
							</tr>
						</oscar:oscarPropertiesCheck>
	
	
						<tr>						
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnAppointmentDate" />:
							</td>
                            <td  class="tite3"><img alt="calendar" id="appointmentDate_cal" src="../../images/cal.gif"> 
 							<html:text styleId="appointmentDate" property="appointmentDate" readonly="true" ondblclick="this.value='';" />
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message	key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAppointmentTime" />:
							</td>
							<td  class="tite3">
							<table>
								<tr>
									<td><html:select property="appointmentHour">
										<html:option value="-1">&nbsp;</html:option>
										<%
											for (int i = 1; i < 13; i = i + 1)
														{
															String hourOfday = Integer.toString(i);
										%>
										<html:option value="<%=hourOfday%>"><%=hourOfday%></html:option>
										<%
											}
										%>
									</html:select></td>
									<td><html:select property="appointmentMinute">
										<html:option value="-1">&nbsp;</html:option>
										<%
											for (int i = 0; i < 60; i = i + 1)
														{
															String minuteOfhour = Integer.toString(i);
															if (i < 10)
															{
																minuteOfhour = "0" + minuteOfhour;
															}
										%>
										<html:option value="<%=String.valueOf(i)%>"><%=minuteOfhour%></html:option>
										<%
											}
										%>
									</html:select></td>
									<td><html:select property="appointmentPm">
										<html:option value="AM">AM</html:option>
										<html:option value="PM">PM</html:option>
									</html:select></td>
								</tr>
							</table>
							</td>
						</tr>
						<%if (bMultisites) { %>
						<tr>
							<td  class="tite4">
								<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.siteName" />:
							</td>
							<td>
								<html:select property="siteName" onchange='this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor'>
						            <%  for (int i =0; i < vecAddressName.size();i++){
						                 String te = vecAddressName.get(i);
						                 String bg = bgColor.get(i);
						                 if (te.equals(defaultSiteName))
						                	 defaultSiteId = siteIds.get(i);
						            %>
						                    <html:option value="<%=te%>" style='<%="background-color: "+bg%>'> <%=te%> </html:option>
						            <%  }%>
							</html:select>
							</td>
						</tr>
						<%} %>
					</table>
					</td>
					<td valign="top" cellspacing="1" class="tite4">
					<table height="100%" width="100%" bgcolor="white">
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPatient" />:
							</td>
                                                        <td class="tite1"><a href="javascript:void();" onClick="popupAttach(600,900,'<%=request.getContextPath()%>/demographic/demographiccontrol.jsp?demographic_no=<%=demo%>&displaymode=edit&dboperation=search_detail')"><%=thisForm.getPatientName()%></a></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgAddress" />:
							</td>
							<td class="tite1"><%=thisForm.getPatientAddress().replace("null", "")%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPhone" />:
							</td>
							<td class="tite2"><%=thisForm.getPatientPhone()%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgWPhone" />:
							</td>
							<td class="tite2"><%=thisForm.getPatientWPhone()%></td>
						</tr>
                                                <tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgEmail" />:
							</td>
							<td class="tite2"><%=thisForm.getPatientEmail()%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgBirthDate" />:
							</td>
							<td class="tite2"><%=thisForm.getPatientDOB()%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSex" />:
							</td>
							<td class="tite3"><%=thisForm.getPatientSex()%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgHealthCard" />:
							</td>
							<td class="tite3"><%=thisForm.getPatientHealthNum()%>&nbsp;<%=thisForm.getPatientHealthCardVersionCode()%>&nbsp;<%=thisForm.getPatientHealthCardType()%>
							</td>
						</tr>
						<tr id="conReqSendTo">
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSendTo" />:
							</td>
							<td class="tite3"><html:select property="sendTo">
								<html:option value="-1">---- <bean:message
										key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgTeams" /> ----</html:option>
								<%
									for (int i = 0; i < consultUtil.teamVec.size(); i++)
												{
													String te = (String)consultUtil.teamVec.elementAt(i);
								%>
								<html:option value="<%=te%>"><%=te%></html:option>
								<%
									}
								%>
							</html:select></td>
						</tr>

<!--add for special encounter-->
<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<special:SpecialEncounterTag moduleName="eyeform">

<%
	String aburl1 = "/EyeForm.do?method=addCC&demographicNo=" + demo;
					if (requestId != null) aburl1 += "&requestId=" + requestId;
%>
<plugin:include componentName="specialencounterComp" absoluteUrl="<%=aburl1 %>"></plugin:include>
</special:SpecialEncounterTag>
</plugin:hideWhenCompExists>
<!-- end -->

						<tr>
							<td colspan="2" class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAppointmentNotes" />:
							</td>
						</tr>
						<tr>
							<td colspan="2" class="tite3"><html:textarea cols="50"
								rows="3" property="appointmentNotes"></html:textarea></td>
						</tr>
                       
						
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formLastFollowup" />:
							</td>
							<td class="tite3">
	                             <img alt="calendar" id="followUpDate_cal" src="../../images/cal.gif" />
	                             <html:text styleId="followUpDate" property="followUpDate" ondblclick="this.value='';" />
                             </td>
						
						</tr>
						
						<%
							if(thisForm.getFdid() != null) {
						%>
						<tr>
							<td class="tite4">EForm:
							</td>
							<td class="tite2">
								<a href="<%=request.getContextPath()%>/eform/efmshowform_data.jsp?fdid=<%=thisForm.getFdid() %>">Click to view</a>
							</td>
						</tr>
						<%
							}
						%>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan=2>
					<table  width="100%">
						<tr>
						<%
						String lhndType = "provider"; //set default as provider
						String providerDefault = providerNo;

						if(consultUtil.letterheadName == null ){
						//nothing saved so find default	
						UserProperty lhndProperty = userPropertyDAO.getProp(providerNo, UserProperty.CONSULTATION_LETTERHEADNAME_DEFAULT);
						String lhnd = lhndProperty != null?lhndProperty.getValue():null;
						//1 or null = provider, 2 = MRP and 3 = clinic
						
							if(lhnd!=null){	
								if(lhnd.equals("2")){
									//mrp
									providerDefault = providerNoFromChart;
								}else if(lhnd.equals("3")){
									//clinic
									lhndType="clinic";
								}
							}	

						}
						%>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.letterheadName" />:
							</td>							
							<td  class="tite3">				
								<select name="letterheadName" id="letterheadName" onchange="switchProvider(this.value)">
									<option value="<%=StringEscapeUtils.escapeHtml(clinic.getClinicName())%>" <%=(consultUtil.letterheadName != null && consultUtil.letterheadName.equalsIgnoreCase(clinic.getClinicName()) ? "selected='selected'" : lhndType.equals("clinic") ? "selected='selected'" : "" )%>><%=clinic.getClinicName() %></option>
								<%
									for (Provider p : prList) {
										if (p.getProviderNo().compareTo("-1") != 0 && (p.getFirstName() != null || p.getSurname() != null)) {
								%>
								<option value="<%=p.getProviderNo() %>" 
								<%=(consultUtil.letterheadName != null && consultUtil.letterheadName.equalsIgnoreCase(p.getProviderNo()) ? "selected='selected'"  : consultUtil.letterheadName == null && p.getProviderNo().equalsIgnoreCase(providerDefault) && lhndType.equals("provider") ? "selected='selected'"  : "") %>>
									<%=p.getSurname() %>, <%=p.getFirstName().replace("Dr.", "") %>
								</option>
								<% }
								}

								if (OscarProperties.getInstance().getBooleanProperty("consultation_program_letterhead_enabled", "true")) {
								for (Program p : programList) {
								%>
									<option value="prog_<%=p.getId() %>" <%=(consultUtil.letterheadName != null && consultUtil.letterheadName.equalsIgnoreCase("prog_" + p.getId()) ? "selected='selected'"  : "") %>>
									<%=p.getName() %>
									</option>
								<% }
								}%>
								</select>
								<%if ( props.isConsultationFaxEnabled() ) {%>
									<div style="font-size:12px"><input type="checkbox" name="ext_letterheadTitle" value="Dr" <%=(consultUtil.letterheadTitle != null && consultUtil.letterheadTitle.equals("Dr") ? "checked"  : "") %>>Include Dr. with name</div>
								<%}%>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.letterheadAddress" />:
							</td>
							<td  class="tite3">
								<% if (consultUtil.letterheadAddress != null) { %>
									<input type="hidden" name="letterheadAddress" id="letterheadAddress" value="<%=StringEscapeUtils.escapeHtml(consultUtil.letterheadAddress) %>" />
									<span id="letterheadAddressSpan">
										<%=consultUtil.letterheadAddress %>
									</span>
								<% } else { %>
									<input type="hidden" name="letterheadAddress" id="letterheadAddress" value="<%=StringEscapeUtils.escapeHtml(clinic.getClinicAddress()) %>  <%=StringEscapeUtils.escapeHtml(clinic.getClinicCity()) %>  <%=StringEscapeUtils.escapeHtml(clinic.getClinicProvince()) %>  <%=StringEscapeUtils.escapeHtml(clinic.getClinicPostal()) %>" />
									<span id="letterheadAddressSpan">
										<%=clinic.getClinicAddress() %>&nbsp;&nbsp;<%=clinic.getClinicCity() %>&nbsp;&nbsp;<%=clinic.getClinicProvince() %>&nbsp;&nbsp;<%=clinic.getClinicPostal() %>
									</span>
								<% } %>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.letterheadPhone" />:
							</td>
							<td  class="tite3">
								<% if (consultUtil.letterheadPhone != null) {
								%>
									<input type="hidden" name="letterheadPhone" id="letterheadPhone" value="<%=StringEscapeUtils.escapeHtml(consultUtil.letterheadPhone) %>" />
								 	<span id="letterheadPhoneSpan">
										<%=consultUtil.letterheadPhone%>
									</span>
								<% } else { %>
									<input type="hidden" name="letterheadPhone" id="letterheadPhone" value="<%=StringEscapeUtils.escapeHtml(clinic.getClinicPhone()) %>" />
									<span id="letterheadPhoneSpan">
										<%=clinic.getClinicPhone()%>
									</span>
								<% } %>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.letterheadFax" />:
							</td>
							<td  class="tite3">
							   <%								
									FaxConfigDao faxConfigDao = SpringUtils.getBean(FaxConfigDao.class);
									List<FaxConfig> faxConfigs = faxConfigDao.findAll(null, null);
								%>
									<span id="letterheadFaxSpan">
										<select name="letterheadFax">
								<%
									for( FaxConfig faxConfig : faxConfigs ) {
								%>
										<option value="<%=faxConfig.getFaxNumber()%>" <%=faxConfig.getFaxNumber().equalsIgnoreCase(consultUtil.letterheadFax) ? "selected" : ""%>><%=faxConfig.getFaxUser()%></option>								
								<%	    
									}								
								%>
									</select>
								</span>
							
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan=2>
					<td>
				</tr>
				<tr>
					<td colspan="2" class="tite4"><bean:message
						key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formReason" />
					</td>
				</tr>
				<tr>
					<td colspan=2><html:textarea property="reasonForConsultation"
						cols="90" rows="6"></html:textarea></td>
				</tr>
				<tr>
					<td colspan=2 class="tite4">
					<table width="100%">
						<tr>
							<td width="30%" rowspan="2" class="tite4">
								<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formClinInf" />:						
							</td>
							<td id="clinicalInfoButtonBar">
								<input type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportFamHistory"/>" onclick="importFromEnct('FamilyHistory',document.forms[0].clinicalInformation);" />&nbsp;
								<input type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportMedHistory"/>" onclick="importFromEnct('MedicalHistory',document.forms[0].clinicalInformation);" />&nbsp;
								<input id="btnOngoingConcerns" type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportConcerns"/>" onclick="importFromEnct('ongoingConcerns',document.forms[0].clinicalInformation);" />&nbsp;
								<input type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportOtherMeds"/>" onclick="importFromEnct('OtherMeds',document.forms[0].clinicalInformation);" />&nbsp;

								<span id="clinicalInfoButtons"></span>
							</td>
						</tr>
						<tr>
														<td>
								<input id="btnReminders" type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportReminders"/>" onclick="importFromEnct('Reminders',document.forms[0].clinicalInformation);" />&nbsp;								
								<input id="fetchRiskFactors_clinicalInformation" type="button" class="btn clinicalData" value="Risk Factors" />&nbsp;
								<input id="fetchMedications_clinicalInformation" type="button" class="btn clinicalData" value="Medications" />&nbsp;
								<input id="fetchLongTermMedications_clinicalInformation" type="button" class="btn clinicalData" value="Long Term Medications" />&nbsp;
								
								
							</td>
						</tr>
					</table>
				</tr>
				<tr>
					<td colspan=2>
					<html:textarea cols="90" rows="10" styleId="clinicalInformation" property="clinicalInformation"></html:textarea></td>
				</tr>
				<tr>
					<td colspan=2 class="tite4">
					<table width="100%">
						<tr>
							<td width="30%" rowspan="2" class="tite4">
							<%
								if (props.getProperty("significantConcurrentProblemsTitle", "").length() > 1)
										{
											out.print(props.getProperty("significantConcurrentProblemsTitle", ""));
										}
										else
										{
							%> <bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formSignificantProblems" />:
							<%
 	}
 %>
							</td>
							<td id="concurrentProblemsButtonBar">
								<input type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportFamHistory"/>" onclick="importFromEnct('FamilyHistory',document.forms[0].concurrentProblems);" />&nbsp;
								<input type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportMedHistory"/>" onclick="importFromEnct('MedicalHistory',document.forms[0].concurrentProblems);" />&nbsp;
								<input id="btnOngoingConcerns2" type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportConcerns"/>" onclick="importFromEnct('ongoingConcerns',document.forms[0].concurrentProblems);" />&nbsp;
								<input type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportOtherMeds"/>" onclick="importFromEnct('OtherMeds',document.forms[0].concurrentProblems);" />&nbsp;

							</td>
						</tr>
						<tr>
							
							<td>
								<input id="btnReminders2" type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportReminders"/>" onclick="importFromEnct('Reminders',document.forms[0].concurrentProblems);" />&nbsp;
								<input id="fetchRiskFactors_concurrentProblems" type="button" class="btn clinicalData" value="Risk Factors" />&nbsp;
								<input id="fetchMedications_concurrentProblems" type="button" class="btn clinicalData" value="Medications" />&nbsp;
								<input id="fetchLongTermMedications_concurrentProblems" type="button" class="btn clinicalData" value="Long Term Medications" />&nbsp;
								
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr id="trConcurrentProblems">
					<td colspan=2>
					
					<html:textarea cols="90" rows="6" styleId="concurrentProblems" property="concurrentProblems">

					</html:textarea></td>
				</tr>
 <!--add for special encounter-->
<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<special:SpecialEncounterTag moduleName="eyeform">
<%
	String aburl2 = "/EyeForm.do?method=specialConRequest&demographicNo=" + demo + "&appNo=" + request.getParameter("appNo");
					if (requestId != null) aburl2 += "&requestId=" + requestId;
if (defaultSiteId!=0) aburl2+="&site="+defaultSiteId;
%>
<html:hidden property="specialencounterFlag" value="true"/>
<plugin:include componentName="specialencounterComp" absoluteUrl="<%=aburl2 %>"></plugin:include>
</special:SpecialEncounterTag>
</plugin:hideWhenCompExists>
				<tr>
					<td colspan="2" class="tite4">
					<table width="100%">
						<tr>
							<td width="30%" class="tite4">
								<% if (props.getProperty("currentMedicationsTitle", "").length() > 1) {
									out.print( props.getProperty("currentMedicationsTitle", "") );
								}else { %> 										
									<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formCurrMedications" />:
								<% }  %>
							</td>
							<td id="medsButtonBar">
								<input type="button" class="btn" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportOtherMeds"/>" 
								onclick="importFromEnct('OtherMeds',document.forms[0].currentMedications);" />
								
								<input id="fetchMedications_currentMedications" type="button" class="btn clinicalData" value="Medications" />&nbsp;
								<input id="fetchLongTermMedications_currentMedications" type="button" class="btn clinicalData" value="Long Term Medications" />&nbsp;
								
								<span id="medsButtons"></span>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan=2>
						<html:textarea cols="90" rows="6" styleId="currentMedications" property="currentMedications"></html:textarea>
					</td>
				</tr>
				<tr>
					<td colspan=2  class="tite4" >
						<table width="100%">
						<tr>
							<td width="30%" class="tite4">
							<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAllergies" />:
							</td>
							<td>
								<input id="fetchAllergies_allergies" type="button" class="btn clinicalData" value="Allergies" />
							</td>
						</tr>
						
						</table>
					</td>
					</tr>
				<tr>
					<td colspan=2>
						<html:textarea cols="90" rows="6" styleId="allergies" property="allergies"></html:textarea></td>
				</tr>

<%
				if (props.isConsultationSignatureEnabled()) {
				%>
				<tr>
					<td colspan=2 class="tite4"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formSignature" />:
					</td>
				</tr>
				<tr>
					<td colspan=2>

						<input type="hidden" name="newSignature" id="newSignature" value="true" />
						<input type="hidden" name="signatureImg" id="signatureImg" value="<%=(consultUtil.signatureImg != null ? consultUtil.signatureImg : "") %>" />
						<input type="hidden" name="newSignatureImg" id="newSignatureImg" value="<%=signatureRequestId %>" />

						<div id="signatureShow" style="display: none;">
							<img id="signatureImgTag" src="" />
						</div>

						<% if (OscarProperties.getInstance().getBooleanProperty("topaz_enabled", "true")) { %>
						<input type="button" id="clickToSign" onclick="requestSignature()" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formClickToSign" />" />
						<% } else { %>
						<iframe style="width:500px; height:132px;"id="signatureFrame" src="<%= request.getContextPath() %>/signature_pad/tabletSignature.jsp?inWindow=true&<%=DigitalSignatureUtils.SIGNATURE_REQUEST_ID_KEY%>=<%=signatureRequestId%>" ></iframe>
						<% } %>

					</td>
				</tr>
				<% }%>
				<%
				if (props.isConsultationFaxEnabled()) {
				%>
				<tr><td colspan=2 class="tite4">Additional Fax Recipients:</td></tr>
				<tr>
					<td colspan=2>
					    <%
					    String rdohip = "";
					    if (demographic!=null) {
					    	String famDoc = demographic.getFamilyDoctor();
					    	if (famDoc != null && famDoc.trim().length() > 0) { rdohip = SxmlMisc.getXmlContent(famDoc,"rdohip"); rdohip = rdohip == null ? "" : rdohip.trim(); }
					    }
					    %>
						<table width="100%">
						<tr>

							<td class="tite4" width="10%">  Providers: </td>
							<td class="tite3" width="20%">
								<select id="otherFaxSelect">
									<option value="">Select additional fax recipients</option>
								<%
								String rdName = "";
								String rdFaxNo = "";
								for (int i=0;i < displayServiceUtil.specIdVec.size(); i++) {
		                                 String  specId     =  displayServiceUtil.specIdVec.elementAt(i);
		                                 String  fName      =  displayServiceUtil.fNameVec.elementAt(i);
		                                 String  lName      =  displayServiceUtil.lNameVec.elementAt(i);
		                                 String  proLetters =  displayServiceUtil.proLettersVec.elementAt(i);
		                                 String  address    =  displayServiceUtil.addressVec.elementAt(i);
		                                 String  phone      =  displayServiceUtil.phoneVec.elementAt(i);
		                                 String  fax        =  displayServiceUtil.faxVec.elementAt(i);
		                                 String  referralNo = ""; // TODO: add referal number to specialists ((String) displayServiceUtil.referralNoList.get(i)).trim();
		                                 if (rdohip != null && !"".equals(rdohip) && rdohip.equals(referralNo)) {
		                                	 rdName = String.format("%s, %s", lName, fName);
		                                	 rdFaxNo = fax;
		                                 }
									if (!"".equals(fax)) {
									%>

									<option value="<%= fax %>"> <%= String.format("%s, %s", lName, fName) %> </option>
									<%
									}
								}
		                        %>
								</select>
							</td>
							<td class="tite3">
								<button onclick="AddOtherFaxProvider(); return false;">Add Provider</button>
							</td>
						</tr>
						<tr>
							<td class="tite4" width="20%"> Other Fax Number: </td>
							<td class="tite3" width="32%">
								<input type="text" id="otherFaxInput"></input>

							<font size="1"> <bean:message key="global.phoneformat1" />  </font></td>
							<td class="tite3">
								<button onclick="AddOtherFax(); return false;">Add Other Fax Recipient</button>
							</td>
						</tr>
						<tr>
							<td colspan=3>
								<ul id="faxRecipients">
								<%
								if (!"".equals(rdName) && !"".equals(rdFaxNo)) {
									%>
								<!--<li>-->
										<!-- <%--= rdName %> <b>Fax No: </b><%= rdFaxNo --%> <a href="javascript:void(0);" onclick="removeRecipient(this)">remove</a>-->
										<input type="hidden" name="faxRecipients" value="<%= rdFaxNo %>" />
								<!--</li>-->
									<%
								}
								%>
								</ul>
							</td>
						</tr>
						</table>
					</td>
				</tr>
				<% } %>



				<tr>

<td colspan=2>
						<input type="hidden" name="submission" value="" />
						
						<%if (request.getAttribute("id") != null) {%>
						
							<input name="update" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdate"/>" 
								onclick="return checkForm('Update Consultation Request','EctConsultationFormRequestForm');" />
							<input name="updateAndPrint" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndPrint"/>" 
								onclick="return checkForm('Update Consultation Request And Print Preview','EctConsultationFormRequestForm');" />
							
							<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
								<input name="updateAndSendElectronically" type="button" 
									value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndSendElectronicReferral"/>" 
									onclick="return checkForm('Update_esend','EctConsultationFormRequestForm');" />
							</logic:equal>
							
							<oscar:oscarPropertiesCheck value="yes" property="faxEnable">
								<input id="fax_button2" name="updateAndFax" type="button" 
									value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndFax"/>" 
									onclick="return checkForm('Update And Fax','EctConsultationFormRequestForm');" />
							</oscar:oscarPropertiesCheck>
							
						<%} else {%>
						
							<input name="submitSaveOnly" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmit"/>" 
								onclick="return checkForm('Submit Consultation Request','EctConsultationFormRequestForm'); " />
							<input name="submitAndPrint" type="button" 
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndPrint"/>" 
								onclick="return checkForm('Submit Consultation Request And Print Preview','EctConsultationFormRequestForm'); " />
								
							<logic:equal value="true" property="eReferral" name="EctConsultationFormRequestForm" >
								<input name="submitAndSendElectronically" type="button" 
									value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndSendElectronicReferral"/>" 
									onclick="return checkForm('Submit_esend','EctConsultationFormRequestForm');" />
							</logic:equal>
							<oscar:oscarPropertiesCheck value="yes" property="faxEnable">
								<input id="fax_button2" name="submitAndFax" type="button" 
									value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndFax"/>" 
									onclick="return checkForm('Submit And Fax','EctConsultationFormRequestForm');" />
							</oscar:oscarPropertiesCheck>
							
						<% }%>
						
						<logic:equal value="true" name="EctConsultationFormRequestForm" property="eReferral">
								<input type="button" value="Send eResponse" onclick="$('saved').value='true';document.location='<%=thisForm.getOruR01UrlString(request)%>'" />
						</logic:equal>
					</td>
				</tr>

				<script type="text/javascript">

	        initMaster();
	        initService('<%=consultUtil.service%>','<%=((consultUtil.service==null)?"":StringEscapeUtils.escapeJavaScript(consultUtil.getServiceName(consultUtil.service.toString())))%>','<%=consultUtil.specialist%>','<%=((consultUtil.specialist==null)?"":StringEscapeUtils.escapeJavaScript(consultUtil.getSpecailistsName(consultUtil.specialist.toString())))%>','<%=StringEscapeUtils.escapeJavaScript(consultUtil.specPhone)%>','<%=StringEscapeUtils.escapeJavaScript(consultUtil.specFax)%>','<%=StringEscapeUtils.escapeJavaScript(consultUtil.specAddr)%>');
            initSpec();
            document.EctConsultationFormRequestForm.phone.value = ("");
        	document.EctConsultationFormRequestForm.fax.value = ("");
        	document.EctConsultationFormRequestForm.address.value = ("");
            <%if (request.getAttribute("id") != null)
					{%>
                setSpec('<%=consultUtil.service%>','<%=consultUtil.specialist%>');
                FillThreeBoxes('<%=consultUtil.specialist%>');
            <%}
					else
					{%>
                document.EctConsultationFormRequestForm.service.options.selectedIndex = 0;
                document.EctConsultationFormRequestForm.specialist.options.selectedIndex = 0;
            <%}%>

            onSelectSpecialist(document.EctConsultationFormRequestForm.specialist);
            
            <%
            	//new with BORN referrals. Allow form to be loaded with service and 
            	//specialist pre-selected
            	String reqService = request.getParameter("service");
            	
            	String reqSpecialist = request.getParameter("specialist");
            	if(reqService != null && reqSpecialist != null) {
            		ConsultationServices consultService = consultationServiceDao.findByDescription(reqService);
            		if(consultService != null) {
            		%>
            		jQuery("#service").val('<%=consultService.getId()%>');
            		fillSpecialistSelect(document.getElementById('service'));
            		jQuery("#specialist").val('<%=reqSpecialist%>');
            		onSelectSpecialist(document.getElementById('specialist'));
            		<%
            	} }
            	
            	String serviceId = request.getParameter("serviceId");
            	if(serviceId != null) {
            		%>
            		jQuery("#service").val('<%=serviceId%>');
            		fillSpecialistSelect(document.getElementById('service'));
            		<%
            	}
            %>
        //-->
        </script>




				<!----End new rows here-->

				<tr height="100%">
					<td></td>
				</tr>
			</table>
			</td>
		</tr>
		<tr>
			<td class="MainTableBottomRowLeftColumn"></td>
			<td class="MainTableBottomRowRightColumn"></td>
		</tr>
	</table>
</html:form>
</body>

<script type="text/javascript" language="javascript">

Calendar.setup( { inputField : "followUpDate", ifFormat : "%Y/%m/%d", showsTime :false, trigger : "followUpDate", singleClick : true, step : 1 } );
Calendar.setup( { inputField : "appointmentDate", ifFormat : "%Y/%m/%d", showsTime :false, trigger : "appointmentDate", singleClick : true, step : 1 } );
<%if("false".equals(OscarProperties.getInstance().getProperty("CONSULTATION_LOCK_REFERRAL_DATE", "true"))) {%>
Calendar.setup( { inputField : "referalDate", ifFormat : "%Y/%m/%d", showsTime :false, trigger : "referalDate", singleClick : true, step : 1 } );
<%}%>
</script>
</html:html>

<%!protected String listNotes(CaseManagementManager cmgmtMgr, String code, String providerNo, String demoNo)
	{
		// filter the notes by the checked issues
		List<Issue> issues = cmgmtMgr.getIssueInfoByCode(providerNo, code);

		String[] issueIds = new String[issues.size()];
		int idx = 0;
		for (Issue issue : issues)
		{
			issueIds[idx] = String.valueOf(issue.getId());
		}

		// need to apply issue filter
		List<CaseManagementNote> notes = cmgmtMgr.getNotes(demoNo, issueIds);
		StringBuffer noteStr = new StringBuffer();
		for (CaseManagementNote n : notes)
		{
			if (!n.isLocked() && !n.isArchived()) noteStr.append(n.getNote() + "\n");
		}

		return noteStr.toString();
	}%>

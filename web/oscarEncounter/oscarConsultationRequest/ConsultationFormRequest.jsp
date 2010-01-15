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

<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/rewrite-tag.tld" prefix="rewrite"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c"%>
<!-- add for special encounter -->
<%@ taglib uri="http://www.caisi.ca/plugin-tag" prefix="plugin" %>
<%@ taglib uri="/WEB-INF/special_tag.tld" prefix="special" %>
<!-- end -->

<%@page
	import="java.util.ArrayList, java.util.Collections, java.util.List, oscar.dms.*, oscar.oscarEncounter.pageUtil.*,oscar.oscarEncounter.data.*, oscar.OscarProperties, oscar.util.StringUtils, oscar.oscarLab.ca.on.*"%>
<%@page
	import="org.oscarehr.casemgmt.service.CaseManagementManager, org.oscarehr.casemgmt.model.CaseManagementNote, org.oscarehr.casemgmt.model.Issue, org.oscarehr.common.model.UserProperty, org.oscarehr.common.dao.UserPropertyDAO, org.springframework.web.context.support.*,org.springframework.web.context.*"%>

<%
    if(session.getAttribute("user") == null) response.sendRedirect("../../logout.jsp");
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy
%>

<html:html locale="true">

<%
String demo = request.getParameter("de");
String requestId = request.getParameter("requestId");
String team = (String) request.getParameter("teamVar");
String providerNo = (String) session.getAttribute("user");
oscar.oscarDemographic.data.DemographicData demoData = null;
oscar.oscarDemographic.data.DemographicData.Demographic demographic = null;

oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil consultUtil;
consultUtil = new  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil();

if( requestId != null ) consultUtil.estRequestFromId(requestId);
if( demo == null ) demo = consultUtil.demoNo;

ArrayList<String> users = (ArrayList<String>)session.getServletContext().getAttribute("CaseMgmtUsers");
boolean useNewCmgmt = false;
WebApplicationContext  ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
CaseManagementManager cmgmtMgr = null;
if( users != null && users.size() > 0 && (users.get(0).equalsIgnoreCase("all") || Collections.binarySearch(users, providerNo)>=0)) {
        useNewCmgmt = true;
        cmgmtMgr = (CaseManagementManager)ctx.getBean("caseManagementManager");
}

UserPropertyDAO userPropertyDAO = (UserPropertyDAO) ctx.getBean("UserPropertyDAO");
UserProperty fmtProperty = userPropertyDAO.getProp(providerNo,UserProperty.CONSULTATION_REQ_PASTE_FMT);
String pasteFmt = fmtProperty != null ? fmtProperty.getValue() : null;

if (demo != null ){
    demoData = new oscar.oscarDemographic.data.DemographicData();
    demographic = demoData.getDemographic(demo);
}
else if(requestId==null){
    response.sendRedirect("../error.jsp");

}
if (demo != null) consultUtil.estPatient(demo);
consultUtil.estActiveTeams();

if (request.getParameter("error") != null){
    %>
<SCRIPT LANGUAGE="JavaScript">
        alert("The form could not be printed due to an error. Please refer to the server logs for more details.");
    </SCRIPT>
<%
}

java.util.Calendar calender = java.util.Calendar.getInstance();
String day =  Integer.toString(calender.get(java.util.Calendar.DAY_OF_MONTH));
String mon =  Integer.toString(calender.get(java.util.Calendar.MONTH)+1);
String year = Integer.toString(calender.get(java.util.Calendar.YEAR));
String formattedDate = year+"/"+mon+"/"+day;

OscarProperties props = OscarProperties.getInstance();

%><head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
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

input.righty{
text-align: right;
}
</style>
</head>



<link type="text/javascript" src="../consult.js" />

<SCRIPT LANGUAGE="JavaScript">

var servicesName = new Array();   		// used as a cross reference table for name and number
var services = new Array();				// the following are used as a 2D table for makes and models
var specialists = new Array();

<%
oscar.oscarEncounter.oscarConsultationRequest.config.data.EctConConfigurationJavascriptData configScript;
configScript = new oscar.oscarEncounter.oscarConsultationRequest.config.data.EctConConfigurationJavascriptData();
out.println(configScript.getJavascript());
%>

/////////////////////////////////////////////////////////////////////
function initMaster() {
	makeSpecialistslist(2);
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
// create car make objects and fill arrays
//==========
function K( serviceNumber, service ){

	servicesName[service] = new ServicesName(serviceNumber);
	services[serviceNumber] = new Service( );
}
//-------------------------------------------------------------------

//-----------------disableDateFields() disables date fields if "Patient Will Book" selected
function disableDateFields(){
	if(document.forms[0].patientWillBook.checked){
		document.forms[0].appointmentYear.disabled = true;
		document.forms[0].appointmentMonth.disabled = true;
		document.forms[0].appointmentDay.disabled = true;
		document.forms[0].appointmentHour.disabled = true;
		document.forms[0].appointmentMinute.disabled = true;
		document.forms[0].appointmentPm.disabled = true;
	}
	else{
		document.forms[0].appointmentYear.disabled = false;
		document.forms[0].appointmentMonth.disabled = false;
		document.forms[0].appointmentDay.disabled = false;
		document.forms[0].appointmentHour.disabled = false;
		document.forms[0].appointmentMinute.disabled = false;
		document.forms[0].appointmentPm.disabled = false;
	}
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

	if ( selectedIdx == 0){ return; }

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
    //window.alert("im in");
	//document.searchForm.mdnm.options.selectedIndex = 0;
	document.EctConsultationFormRequestForm.specialist.options.length = 1;
//    window.alert("before var");
	var specs;
	var t = new String("");
	var tUpper = t.toUpperCase();
	var x = tUpper.split(", ");
	var j = 0;
//    window.alert("after vars"+x);
	specs = (services[makeNbr].specialists);
//    window.alert("after selectedModels "+specNbr);
	var i=0;
	i++;
        var notSet = true;
	for ( var specIndex = 0; specIndex < specs.length; ++specIndex )
	{
		aPit = specs[specIndex];
		document.EctConsultationFormRequestForm.specialist.options[i] = new Option(aPit.specName, aPit.specNbr );
       // window.alert("in da loop");

		/*if (aPit.specName.toUpperCase() == x[j]) {
			document.EctConsultationFormRequestForm.specialist.options[i].selected = true;
			j++;
                        notSet = false;
		}*/
		i++;
	}
    //window.alert("im out");
        if( notSet ) {
            document.EctConsultationFormRequestForm.specialist.options[0].selected = true;
        }
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
    var aSpecialist = services["-1"].specialists[0];
    document.EctConsultationFormRequestForm.specialist.options[0] = new Option(aSpecialist.specNbr, aSpecialist.specId);
}

/////////////////////////////////////////////////////////////////////
function initService(ser){
	var i = 0;
	var isSel = 0;
	var strSer = new String(ser);

	for (aIdx in servicesName){
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
	}
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function GetExtensionOn(SelectedSpec)	{
	var selectedIdx = SelectedSpec.selectedIndex;

	if ((SelectedSpec.options[ selectedIdx ]).value == "-1") {   		//if its the first item set everything to blank
		document.EctConsultationFormRequestForm.phone.value = ("");
		document.EctConsultationFormRequestForm.fax.value = ("");
		document.EctConsultationFormRequestForm.address.value = ("");
		return;
	}
	var selectedService = document.EctConsultationFormRequestForm.service.value;  				// get the service that is selected now
	var specs = (services[selectedService].specialists);					// get all the specs the offer this service
        for( var idx = 0; idx < specs.length; ++idx ) {
            aSpeci = specs[idx];									// get the specialist Object for the currently selected spec
            if( aSpeci.specNbr == SelectedSpec.value ) {
                document.EctConsultationFormRequestForm.phone.value = (aSpeci.phoneNum);
                document.EctConsultationFormRequestForm.fax.value = (aSpeci.specFax);					// load the text fields with phone fax and address
                document.EctConsultationFormRequestForm.address.value = (aSpeci.specAddress);
                break;
            }
        }
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
                break;
           }
        }
}
//-----------------------------------------------------------------


//-->
</SCRIPT>

<script type="text/javascript" language="javascript">


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
            <%

                String value = "";
                if( demo != null )
                {
                     if( useNewCmgmt ) {
                        value = listNotes(cmgmtMgr, "MedHistory", providerNo, demo);
                    }
                    else {
                        value = demographic.EctInfo.getMedicalHistory();
                    }
                    if( pasteFmt == null || pasteFmt.equalsIgnoreCase("single") ) {
                        value = StringUtils.lineBreaks(value);
                    }
                    value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
                    out.println("info = '" + value + "'");
                }
             %>
             break;
          case "ongoingConcerns":
             <%
                 if( demo != null )
                 {
                     if( useNewCmgmt ) {
                        value = listNotes(cmgmtMgr, "Concerns", providerNo, demo);
                    }
                    else {
                        value = demographic.EctInfo.getOngoingConcerns();
                    }
                    if( pasteFmt == null || pasteFmt.equalsIgnoreCase("single") ) {
                        value = StringUtils.lineBreaks(value);
                    }
                    value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
                    out.println("info = '" + value + "'");
                 }
             %>
             break;
           case "FamilyHistory":
              <%
                 if( demo != null )
                 {
                   if(OscarProperties.getInstance().getBooleanProperty("caisi","on")) {
                		 value = demographic.EctInfo.getFamilyHistory();
                   }else{
					if( useNewCmgmt ) {
                        value = listNotes(cmgmtMgr, "SocHistory", providerNo, demo);
                    }
                    else {
                        value = demographic.EctInfo.getSocialHistory();
                    }
				  }
                    if( pasteFmt == null || pasteFmt.equalsIgnoreCase("single") ) {
                        value = StringUtils.lineBreaks(value);
                    }
                    value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
                    out.println("info = '" + value + "'");
                 }
              %>
              break;
            case "OtherMeds":
              <%
                 if( demo != null )
                 {
                  if(OscarProperties.getInstance().getBooleanProperty("caisi","on")) {
                		 value = "";
                  }else{
					if( useNewCmgmt ) {
                        value = listNotes(cmgmtMgr, "OMeds", providerNo, demo);
                    }
                    else {
                    //family history was used as bucket for Other Meds in old encounter
                        value = demographic.EctInfo.getFamilyHistory();
                    }
				  }
                    if( pasteFmt == null || pasteFmt.equalsIgnoreCase("single") ) {
                        value = StringUtils.lineBreaks(value);
                    }
                    value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
                    out.println("info = '" + value + "'");

                }
              %>
                break;
            case "Reminders":
              <%
                 if( demo != null )
                 {
                     if( useNewCmgmt ) {
                        value = listNotes(cmgmtMgr, "Reminders", providerNo, demo);
                    }
                    else {
                        value = demographic.EctInfo.getReminders();
                    }
                    //if( !value.equals("") ) {
                    if( pasteFmt == null || pasteFmt.equalsIgnoreCase("single") ) {
                        value = StringUtils.lineBreaks(value);
                    }

                        value = org.apache.commons.lang.StringEscapeUtils.escapeJavaScript(value);
                        out.println("info = '" + value + "'");
                    //}
                 }
              %>
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

</script>
<link rel="stylesheet" type="text/css" href="../encounterStyles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF"
	onload="window.focus();disableDateFields();fetchAttached();">
<html:errors />

<html:form action="/oscarEncounter/RequestConsultation"
	onsubmit="alert('HTHT'); return false;">
	<%
        oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestForm thisForm;
        thisForm = (oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestForm ) request.getAttribute("EctConsultationFormRequestForm");
        //System.out.println("Requested ID :"+requestId);
        if (requestId !=null ){

                thisForm.setAllergies(consultUtil.allergies);
                thisForm.setReasonForConsultation(consultUtil.reasonForConsultation);
                thisForm.setClinicalInformation(consultUtil.clinicalInformation);
                thisForm.setCurrentMedications(consultUtil.currentMedications);
                thisForm.setReferalDate(consultUtil.referalDate);
                thisForm.setSendTo(consultUtil.sendTo);
                thisForm.setService(consultUtil.service);
                thisForm.setStatus(consultUtil.status);
                thisForm.setAppointmentDay(consultUtil.appointmentDay);
                thisForm.setAppointmentMonth(consultUtil.appointmentMonth);
                thisForm.setAppointmentYear(consultUtil.appointmentYear);
//                thisForm.setAppointmentTime(consultUtil.appointmentTime);
                thisForm.setAppointmentHour(consultUtil.appointmentHour);
                thisForm.setAppointmentMinute(consultUtil.appointmentMinute);
                thisForm.setAppointmentPm(consultUtil.appointmentPm);
                thisForm.setConcurrentProblems(consultUtil.concurrentProblems);
                thisForm.setAppointmentNotes(consultUtil.appointmentNotes);
                thisForm.setUrgency(consultUtil.urgency);
                thisForm.setPatientWillBook(consultUtil.pwb);

                if( !consultUtil.teamVec.contains(consultUtil.sendTo) ) {
                    consultUtil.teamVec.add(consultUtil.sendTo);
                }
                // System.out.println("this is from in the form setter "+ consultUtil.patientName);


        }else if(request.getAttribute("validateError") == null){
            //  new request
            if( demo != null ) {
                        thisForm.setAllergies(demographic.RxInfo.getAllergies());

			if(props.getProperty("currentMedications", "").equalsIgnoreCase("otherMedications")) {
                            thisForm.setCurrentMedications(demographic.EctInfo.getFamilyHistory());
			} else {
                            thisForm.setCurrentMedications(demographic.RxInfo.getCurrentMedication());
			}

                        team = consultUtil.getProviderTeam(consultUtil.mrp);
            }

                thisForm.setStatus("1");
                
                thisForm.setSendTo(team);
                //thisForm.setConcurrentProblems(demographic.EctInfo.getOngoingConcerns());
        	thisForm.setAppointmentYear(year);

	}

        %>

	<input type="hidden" name="providerNo" value="<%=providerNo%>">
	<input type="hidden" name="demographicNo" value="<%=demo%>">
	<input type="hidden" name="requestId" value="<%=requestId%>">
	<input type="hidden" name="documents" value="">
	<!--  -->
	<table class="MainTable" id="scrollNumber1" name="encounterTable">
		<tr class="MainTableTopRow">
			<td class="MainTableTopRowLeftColumn">Consultation</td>
			<td class="MainTableTopRowRightColumn">
			<table class="TopStatusBar">
				<tr>
					<td class="Header"
						style="padding-left: 2px; padding-right: 2px; border-right: 2px solid #003399; text-align: left; font-size: 80%; font-weight: bold; width: 100%;"
						NOWRAP><%=consultUtil.patientName%> <%=consultUtil.patientSex%>
					<%=consultUtil.patientAge%></td>
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
							<td class="stat" colspan="2" align="right" nowrap><%=consultUtil.getProviderName(consultUtil.providerNo) %>
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2"><bean:message
						key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgStatus" />
					</td>
				</tr>
				<tr>
					<td class="tite4" colspan="2">
					<table>
						<tr>
							<td class="stat"><html:radio property="status" value="1" />
							</td>
							<td class="stat"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNoth" />:
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
							<td class="stat"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSpecCall" />
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
							<td class="stat"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPatCall" />
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
							<td class="stat"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCompleted" />
							</td>
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
							<td style="text-align: center" class="stat"><a href="#"
								onclick="popup('<rewrite:reWrite jspPage="attachConsultation.jsp"/>?provNo=<%=consultUtil.providerNo%>&demo=<%=demo%>&requestId=<%=requestId%>');return false;"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.attachDoc" /></a></td>
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
								key="oscarEncounter.oscarConsultationRequest.AttachDoc.LegendLabs" /></span>
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
					<td colspan=2>
					<%if (request.getAttribute("id") != null){ %> <input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdate"/>"
						onclick="return checkForm('Update Consultation Request','EctConsultationFormRequestForm');" />
					<input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndPrint"/>"
						onclick="return checkForm('Update Consultation Request And Print Preview','EctConsultationFormRequestForm');" />
					<%if (props.getProperty("faxEnable", "").equalsIgnoreCase("yes")) { %>
					<input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndFax"/>"
						onclick="return checkForm('Update And Fax','EctConsultationFormRequestForm');" />
					<%}%> <%}else{%> <input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmit"/>"
						onclick="return checkForm('Submit Consultation Request','EctConsultationFormRequestForm'); " />
					<input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndPrint"/>"
						onclick="return checkForm('Submit Consultation Request And Print Preview','EctConsultationFormRequestForm');" />
					<%if (props.getProperty("faxEnable", "").equalsIgnoreCase("yes")) { %>
					<input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndFax"/>"
						onclick="return checkForm('Submit And Fax','EctConsultationFormRequestForm');" />
					<%}%> <%}%>
					</td>

				</tr>
				<tr>
					<td>

					<table border=0 width="100%">
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formRefDate" />:
							</td>
							<td align="right" class="tite1">
							<%if ( request.getAttribute("id") != null) {%> <html:text
								styleClass="righty" property="referalDate" /> <%}else{%> <html:text
								styleClass="righty" property="referalDate"
								value="<%=formattedDate%>" /> <%}%>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formService" />:
							</td>
							<td align="right" class="tite1"><html:select
								property="service" onchange="fillSpecialistSelect(this);">
								<!-- <option value="-1">------ <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formServSelect"/> ------</option>
					<option/>
				    	<option/>
			    		<option/>
		    			<option/> -->
							</html:select></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formCons" />:
							</td>
							<td align="right" class="tite2"><html:select
								property="specialist" size="1" onchange="GetExtensionOn(this)">
								<!-- <option value="-1">--- <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formSelectSpec"/> ---</option>
                                        <option/>
	    				<option/>
		    			<option/>
			    		<option/> -->
							</html:select></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formUrgency" />
							</td>
							<td align="right" class="tite2"><html:select
								property="urgency">
								<html:option value="2">
									<bean:message
										key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNUrgent" />
								</html:option>
								<html:option value="1">
									<bean:message
										key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgUrgent" />
								</html:option>
								<html:option value="3">
									<bean:message
										key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgReturn" />
								</html:option>
							</html:select></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formPhone" />:
							</td>
							<td align="right" class="tite2"><input type="text"
								name="phone" class="righty" /></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formFax" />:
							</td>
							<td align="right" class="tite3"><input type="text"
								name="fax" class="righty" /></td>
						</tr>

						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAddr" />:
							</td>
							<td align="right" class="tite3"><textarea name="address"
								cols=20></textarea></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formPatientBook" />:</td>
							<td align="right" class="tite3"><html:checkbox
								property="patientWillBook" value="1"
								onclick="disableDateFields()">
							</html:checkbox></td>
						</tr>
						<tr>
							<td class="tite4"><a
								href="javascript:popupOscarCal(300,380,'https://<%=request.getServerName() %>:<%=request.getServerPort()%><%=request.getContextPath()%>/oscarEncounter/oscarConsultationRequest/CalendarPopup.jsp?year=<%=year%>&month=<%=mon%>')"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnAppointmentDate" />:</a>

							</td>
							<td align="right" class="tite3">
							<table bgcolor="white">
								<tr>
									<th class="tite2"><bean:message key="global.year" /></th>
									<th class="tite2"><bean:message key="global.month" /></th>
									<th class="tite2"><bean:message key="global.day" /></th>
								</tr>
								<tr>
									<td class="tite3"><html:text size="5" maxlength="4"
										property="appointmentYear" /></td>
									<td class="tite3"><html:select property="appointmentMonth">
										<% for (int i=1; i < 13; i = i + 1){
                                    String month = Integer.toString(i);
                                    if (i < 10){ month = "0"+month; }
                                %>
										<html:option value="<%=month%>"><%=month%></html:option>
										<%}%>
									</html:select></td>
									<td class="tite3"><html:select property="appointmentDay">
										<% for (int i=1; i < 32; i = i + 1){
                                    String dayOfWeek = Integer.toString(i);
                                    if (i < 10){ dayOfWeek = "0"+dayOfWeek;}
                                %>
										<html:option value="<%=dayOfWeek%>"><%=dayOfWeek%></html:option>
										<%}%>

									</html:select></td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAppointmentTime" />:
							</td>
							<td align="right" class="tite3">
							<table>
								<tr>
									<td><html:select property="appointmentHour">
										<% for (int i=1; i < 13; i = i + 1){
                                    String hourOfday = Integer.toString(i);
                                %>
										<html:option value="<%=hourOfday%>"><%=hourOfday%></html:option>
										<%}%>
									</html:select></td>
									<td><html:select property="appointmentMinute">
										<% for (int i=0; i < 60; i = i + 1){
                                    String minuteOfhour = Integer.toString(i);
                                    if (i < 10){ minuteOfhour = "0"+minuteOfhour;}
                                %>
										<html:option value="<%=minuteOfhour%>"><%=minuteOfhour%></html:option>
										<%}%>
									</html:select></td>
									<td><html:select property="appointmentPm">
										<html:option value="AM">AM</html:option>
										<html:option value="PM">PM</html:option>
									</html:select></td>
								</tr>
							</table>
							</td>
						</tr>
					</table>
					</td>
					<td valign="top" cellspacing="1" class="tite4">
					<table border=0 width="100%" bgcolor="white">
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPatient" />:
							</td>
							<td class="tite1"><%=consultUtil.patientName%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgAddress" />:
							</td>
							<td class="tite1"><%=consultUtil.patientAddress%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPhone" />:
							</td>
							<td class="tite2"><%=consultUtil.patientPhone%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgWPhone" />:
							</td>
							<td class="tite2"><%=consultUtil.patientWPhone%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgBirthDate" />:
							</td>
							<td class="tite2"><%=consultUtil.patientDOB%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSex" />:
							</td>
							<td class="tite3"><%=consultUtil.patientSex%></td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgHealthCard" />:
							</td>
							<td class="tite3"><%=consultUtil.patientHealthNum%>&nbsp;<%=consultUtil.patientHealthCardVersionCode%>&nbsp;<%=consultUtil.patientHealthCardType%>
							</td>
						</tr>
						<tr>
							<td class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSendTo" />:
							</td>
							<td class="tite3"><html:select property="sendTo">
								<html:option value="-1">---- <bean:message
										key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgTeams" /> ----</html:option>
								<% for (int i =0; i < consultUtil.teamVec.size();i++){
                            String te = (String) consultUtil.teamVec.elementAt(i);
                        %>
								<html:option value="<%=te%>"><%=te%></html:option>
								<%}%>
							</html:select></td>
						</tr>

<!--add for special encounter-->
<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<special:SpecialEncounterTag moduleName="eyeform">

<%
String aburl1="/EyeForm.do?method=addCC&demographicNo="+demo;
if (requestId!=null) aburl1+="&requestId="+requestId; %>
<plugin:include componentName="specialencounterComp" absoluteUrl="<%=aburl1 %>"></plugin:include>
</special:SpecialEncounterTag>
</plugin:hideWhenCompExists>
<!-- end -->

						<tr>
							<td colspan=2 class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAppointmentNotes" />:
							</td>
							<td style="background-color: #B8B8FF;">&nbsp;</td>
						</tr>
						<tr>
							<td colspan=2 class="tite3"><html:textarea cols="50"
								rows="3" property="appointmentNotes"></html:textarea></td>
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
						cols="90" rows="3"></html:textarea></td>
				</tr>
				<tr>
					<td colspan=2 class="tite4">
					<table width="100%">
						<tr>
							<td width="30%" class="tite4"><bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formClinInf" />:
							</td>
							<td><input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportFamHistory"/>"
								onclick="importFromEnct('FamilyHistory',document.forms[0].clinicalInformation);" />&nbsp;
							<input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportMedHistory"/>"
								onclick="importFromEnct('MedicalHistory',document.forms[0].clinicalInformation);" />&nbsp;
							<input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportConcerns"/>"
								onclick="importFromEnct('ongoingConcerns',document.forms[0].clinicalInformation);" />&nbsp;
							<input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportOtherMeds"/>"
								onclick="importFromEnct('OtherMeds',document.forms[0].clinicalInformation);" />&nbsp;
							<input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportReminders"/>"
								onclick="importFromEnct('Reminders',document.forms[0].clinicalInformation);" />&nbsp;
							</td>
						</tr>
					</table>
				</tr>
				<tr>
					<td colspan=2><html:textarea cols="90" rows="10"
						property="clinicalInformation"></html:textarea></td>
				</tr>
				<tr>
					<td colspan=2 class="tite4">
					<table width="100%">
						<tr>
							<td width="30%" class="tite4">
							<% if(props.getProperty("significantConcurrentProblemsTitle", "").length() > 1) {
                                    out.print(props.getProperty("significantConcurrentProblemsTitle", ""));
                                } else { %> <bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formSignificantProblems" />:
							<% } %>
							</td>
							<td><input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportFamHistory"/>"
								onclick="importFromEnct('FamilyHistory',document.forms[0].concurrentProblems);" />&nbsp;
							<input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportMedHistory"/>"
								onclick="importFromEnct('MedicalHistory',document.forms[0].concurrentProblems);" />&nbsp;
							<input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportConcerns"/>"
								onclick="importFromEnct('ongoingConcerns',document.forms[0].concurrentProblems);" />&nbsp;
							<input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportOtherMeds"/>"
								onclick="importFromEnct('OtherMeds',document.forms[0].concurrentProblems);" />&nbsp;
							<input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportReminders"/>"
								onclick="importFromEnct('Reminders',document.forms[0].concurrentProblems);" />&nbsp;
							</td>
						</tr>
					</table>

					</td>
				</tr>
				<tr>
					<td colspan=2><html:textarea cols="90" rows="3"
						property="concurrentProblems">

					</html:textarea></td>
				</tr>
 <!--add for special encounter-->
<plugin:hideWhenCompExists componentName="specialencounterComp" reverse="true">
<special:SpecialEncounterTag moduleName="eyeform">
<%String aburl2="/EyeForm.do?method=specialConRequest&demographicNo="+demo+"&appNo="+request.getParameter("appNo");
if (requestId!=null) aburl2+="&requestId="+requestId;
System.out.println("requestId:"+requestId);
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
							<% if(props.getProperty("currentMedicationsTitle", "").length() > 1) {
                                    out.print(props.getProperty("currentMedicationsTitle", ""));
                               } else { %> <bean:message
								key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formCurrMedications" />:
							<% } %>
							</td>
							<td><input type="button" class="btn"
								value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnImportOtherMeds"/>"
								onclick="importFromEnct('OtherMeds',document.forms[0].currentMedications);" />
							</td>
						</tr>
					</table>
					</td>
				</tr>
				<tr>
					<td colspan=2><html:textarea cols="90" rows="3"
						property="currentMedications"></html:textarea></td>
				</tr>
				<tr>
					<td colspan=2 class="tite4"><bean:message
						key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAllergies" />:
					</td>
				</tr>
				<tr>
					<td colspan=2><html:textarea cols="90" rows="3"
						property="allergies"></html:textarea></td>
				</tr>

				<tr>
					<td colspan=2><input type="hidden" name="submission" value="">
					<%if (request.getAttribute("id") != null){ %> <input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdate"/>"
						onclick="return checkForm('Update Consultation Request','EctConsultationFormRequestForm');" />
					<input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndPrint"/>"
						onclick="return checkForm('Update Consultation Request And Print Preview','EctConsultationFormRequestForm');" />
					<%if (props.getProperty("faxEnable", "").equalsIgnoreCase("yes")) { %>
					<input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndFax"/>"
						onclick="return checkForm('Update And Fax','EctConsultationFormRequestForm');" />
					<%}%> <%}else{%> <input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmit"/>"
						onclick="return checkForm('Submit Consultation Request','EctConsultationFormRequestForm'); " />
					<input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndPrint"/>"
						onclick="return checkForm('Submit Consultation Request And Print Preview','EctConsultationFormRequestForm'); " />
					<%if (props.getProperty("faxEnable", "").equalsIgnoreCase("yes")) { %>
					<input type="button"
						value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndFax"/>"
						onclick="return checkForm('Submit And Fax','EctConsultationFormRequestForm');" />
					<%}%> <%}%>
					</td>
				</tr>

				<script type="text/javascript">

	        initMaster();
        	initService('<%=consultUtil.service%>');
                initSpec();
            document.EctConsultationFormRequestForm.phone.value = ("");
        	document.EctConsultationFormRequestForm.fax.value = ("");
        	document.EctConsultationFormRequestForm.address.value = ("");
            <%if (request.getAttribute("id") != null){%>
                setSpec('<%=consultUtil.service%>','<%=consultUtil.specialist%>');
                FillThreeBoxes('<%=consultUtil.specialist%>');
            <%}else{%>
                document.EctConsultationFormRequestForm.service.options.selectedIndex = 0;
                document.EctConsultationFormRequestForm.specialist.options.selectedIndex = 0;
            <%}%>
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
<script type="text/javascript" src="../../share/javascript/prototype.js" />

<script type="text/javascript" language="javascript">


</script>
</html:html>

<%!
    protected String listNotes(CaseManagementManager cmgmtMgr, String code, String providerNo, String demoNo) {
         // filter the notes by the checked issues
        List<Issue> issues = cmgmtMgr.getIssueInfoByCode(providerNo, code);

        String[] issueIds = new String[issues.size()];
        int idx = 0;
        for(Issue issue: issues) {
            issueIds[idx] = String.valueOf(issue.getId());
        }

        // need to apply issue filter
        List<CaseManagementNote>notes = cmgmtMgr.getNotes(demoNo, issueIds);
        StringBuffer noteStr = new StringBuffer();
        for(CaseManagementNote n: notes) {
            if( !n.isLocked() )
                noteStr.append(n.getNote() + "\n");
        }

        return noteStr.toString();
    }
%>
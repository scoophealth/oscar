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

<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page import="oscar.oscarEncounter.pageUtil.*"%>
<%
    response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
    response.setHeader("Pragma","no-cache"); //HTTP 1.0
    response.setDateHeader ("Expires", 0); //prevents caching at the proxy 
%>

<html:html locale="true">

<%
String team = null;
EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
if ( bean != null){
    team = bean.getTeam();
}

String requestId = (String) bean.getConsultationRequestId();

oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil consultUtil;
consultUtil = new  oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestUtil();

String demoNo = bean.getDemographicNo();
if (demoNo != null) consultUtil.estPatient(demoNo);

consultUtil.estTeams();

java.util.Calendar calender = java.util.Calendar.getInstance();
String day =  Integer.toString(calender.get(java.util.Calendar.DAY_OF_MONTH));
String mon =  Integer.toString(calender.get(java.util.Calendar.MONTH)+1);
String year = Integer.toString(calender.get(java.util.Calendar.YEAR));
String formattedDate = year+"/"+mon+"/"+day;
%>

<head>
<title>
<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.title"/>
</title>
<html:base/>
<style type="text/css">
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

<link type="text/javascript" src="../consult.js"/>

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

/////////////////////////////////////////////////////////////////////
// create car model objects and fill arrays
//=======
function D( servNumber, specNum, phoneNum ,SpecName,SpecFax,SpecAddress){

	var specialistObj = new Specialist(servNumber,specNum,phoneNum, SpecName, SpecFax, SpecAddress);
	services[servNumber].specialists[specNum] = specialistObj;
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

	var specs = (services[makeNbr].specialists);

	var i=0;
	i++;
	for ( specIndex in specs){
	   aPit = specs[ specIndex ];
	   document.EctConsultationFormRequestForm.specialist.options[ i ] = new Option( aPit.specName , aPit.specNbr );
	   i++;
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
	for ( specIndex in specs )
	{
		aPit = specs[specIndex];
		document.EctConsultationFormRequestForm.specialist.options[i] = new Option(aPit.specName, aPit.specNbr );
       // window.alert("in da loop");
		if (aPit.specName.toUpperCase() == x[j]) {
			document.EctConsultationFormRequestForm.specialist.options[i].selected = true;
			j++;
		}
		i++;
	}
    //window.alert("im out");
}
//-------------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function setSpec(servNbr,specialNbr){
//    //window.alert("get Called");
    specs = (services[servNbr].specialists);
//    //window.alert("got specs");
    var i=1;
    for ( specIndex in specs ){
//      //  window.alert("loop");
        aPit = specs[specIndex];
        if (aPit.specNbr == specialNbr){
//        //    window.alert("if");
            document.EctConsultationFormRequestForm.specialist.options[i].selected = true;
        }

        i++;
    }
//    window.alert("exiting");

}
//=------------------------------------------------------------------

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
	aSpeci = specs[SelectedSpec.value];									// get the specialist Object for the currently selected spec
	document.EctConsultationFormRequestForm.phone.value = (aSpeci.phoneNum);
	document.EctConsultationFormRequestForm.fax.value = (aSpeci.specFax);					// load the text fields with phone fax and address
	document.EctConsultationFormRequestForm.address.value = (aSpeci.specAddress);
}
//-----------------------------------------------------------------

/////////////////////////////////////////////////////////////////////
function FillThreeBoxes(serNbr)	{

	var selectedService = document.EctConsultationFormRequestForm.service.value;  				// get the service that is selected now
	var specs = (services[selectedService].specialists);					// get all the specs the offer this service

	aSpeci = specs[serNbr];									// get the specialist Object for the currently selected spec
	document.EctConsultationFormRequestForm.phone.value = (aSpeci.phoneNum);
	document.EctConsultationFormRequestForm.fax.value = (aSpeci.specFax);					// load the text fields with phone fax and address
	document.EctConsultationFormRequestForm.address.value = (aSpeci.specAddress);
}
//-----------------------------------------------------------------


//-->
</SCRIPT>

<script language="javascript">
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

</script>

<link rel="stylesheet" type="text/css" href="../styles.css">
<body topmargin="0" leftmargin="0" vlink="#0000FF" onload="window.focus();">
<html:errors/>

<html:form action="/oscarEncounter/RequestConsultation">
        <%
        oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestForm thisForm;
        thisForm = (oscar.oscarEncounter.oscarConsultationRequest.pageUtil.EctConsultationFormRequestForm ) request.getAttribute("EctConsultationFormRequestForm");
        System.out.println("Requested ID :"+requestId);
        if (requestId !=null ){
                consultUtil.estRequestFromId(requestId);
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

                System.out.println("this is from in the form setter "+ consultUtil.patientName);


        }else if(request.getAttribute("validateError") == null){
                thisForm.setStatus("1");
                thisForm.setSendTo(team);
                thisForm.setConcurrentProblems(bean.ongoingConcerns);
        	thisForm.setAppointmentYear(year);
	}
        %>





<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="#111111" width="100%" id="AutoNumber1" height="100%">
    <tr>
        <td width="100%" style="padding-left: 3; padding-right: 3; padding-top: 2; padding-bottom: 2" height="0%" colspan="2">
        <p class="HelpAboutLogout"><span class="FakeLink"><a href="Help.htm"><bean:message key="global.help"/></a></span> |
        <span class="FakeLink"><a href="About.htm"><bean:message key="global.about"/></a></span> | <span class="FakeLink">
        <a href="Disclaimer.htm"><bean:message key="global.disclaimer"/></a></span></p>
        </td>
    </tr>
    <tr>
        <td width="10%" height="37" bgcolor="#000000">&nbsp;</td>
        <td width="100%" bgcolor="#000000" style="border-left: 2px solid #A9A9A9; padding-left: 5" height="0%">
        <p class="ScreenTitle"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.title"/></p>
        </td>
    </tr>





    <tr>
        <td valign="top">
            <table>
                <tr >
                    <td class="tite4" colspan="2" >
                    <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgStatus"/>
                    </td>
                </tr>
                <tr >
                    <td class="tite4" colspan="2">
                        <table>
                           <tr>
                              <td class="stat">
                                 <html:radio property="status" value="1"/>
                              </td>
                              <td class="stat">
                                 <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNoth"/>:
                              </td>
                           </tr>
                        </table>
                    </td>
                </tr>
                <tr >
                    <td class="tite4" colspan="2">
                       <table>
                          <tr>
                            <td class="stat">
                                <html:radio property="status" value="2"/>
                            </td>
                            <td class="stat">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSpecCall"/>
                            </td>
                          </tr>
                       </table>
                    </td>
                </tr>
                <tr >
                    <td class="tite4" colspan="2">
                       <table>
                          <tr>
                            <td class="stat">
                                <html:radio property="status" value="3"/>
                            </td>
                            <td class="stat">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPatCall"/>
                            </td>
                          </tr>
                      </table>
                    </td>
                </tr>
                <tr >
                    <td class="tite4" colspan="2">
                       <table>
                          <tr>
                            <td class="stat">
                                <html:radio property="status" value="4"/>
                            </td>
                            <td class="stat">
                                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgCompleted"/>
                            </td>
                          </tr>
                      </table>
                    </td>
                </tr>


            </table>
        </td>
        <td width="100%" style="border-left: 2px solid #A9A9A9; " height="100%" valign="top">
            <table cellpadding="0" cellspacing="2" style="border-collapse: collapse" bordercolor="#111111" width="100%" height="100%" border=1>

            <!----Start new rows here-->
       <tr>
        <td>

            <table border=0 width="100%">
            <tr>
                <td class="tite4">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formRefDate"/>:
                </td>
                <td align="right" class="tite1">
                    <%if ( request.getAttribute("id") != null) {%>
                    <html:text styleClass="righty" property="referalDate"/>
                    <%}else{%>
                    <html:text styleClass="righty" property="referalDate" value="<%=formattedDate%>"/>
                    <%}%>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formService"/>:
                </td>
                <td align="right" class="tite1">
                    <html:select property="service" onchange="fillSpecialistSelect(this);">
    					<option>------ <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formServSelect"/> ------</option>
					    <option/>
				    	<option/>
			    		<option/>
		    			<option/>
				    </html:select>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formCons"/>:
                </td>
                <td align="right" class="tite2">
                    <html:select property="specialist" size="1" onchange="GetExtensionOn(this)">
		    		    <option value="-1">--- <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formSelectSpec"/> ---</option>
    					<option/>
	    				<option/>
		    			<option/>
			    		<option/>
				    </html:select>
                </td>
            </tr>
	    <tr>
	        <td class="tite4">
                   <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formUrgency"/>
                </td>
                <td align="right" class="tite2">
                    <html:select property="urgency">
                         <html:option value="1"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgUrgent"/></html:option>
                         <html:option value="2"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgNUrgent"/></html:option>
                         <html:option value="3"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgReturn"/></html:option>
                    </html:select>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formPhone"/>:
                </td>
                <td align="right" class="tite2">
                    <input type="text" name="phone" class="righty"/>
                </td>
            </tr>
            <tr>
                <td class="tite4" >
                    <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formFax"/>:
                </td>
                <td align="right" class="tite3">
                    <input type="text" name="fax" class="righty"/>
                </td>
            </tr>
            <tr>
                <td class="tite4" >
                    <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAddr"/>:
                </td>
                <td align="right" class="tite3">
                    <textarea name="address" cols=20></textarea>
                </td>
            </tr>
            <tr>
                <td class="tite4">
              <a href="javascript:popupOscarCal(300,380,'https://<%=request.getServerName() %>:<%=request.getServerPort()%><%=request.getContextPath()%>/oscarEncounter/oscarConsultationRequest/CalendarPopup.jsp?year=<%=year%>&month=<%=mon%>')"><bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnAppointmentDate"/>:</a>
   
                </td>
                <td align="right" class="tite3">
                    <table bgcolor="white">
                    <tr>
                        <th class="tite2">
                        <bean:message key="global.year"/>
                        </th>
                        <th class="tite2">
                        <bean:message key="global.month"/>
                        </th>
                        <th class="tite2">
                        <bean:message key="global.day"/>
                        </th>
                    </tr>
                    <tr>
                        <td class="tite3">
                            <html:text size="5" maxlength="4" property="appointmentYear" />
                        </td>
                        <td class="tite3">
                            <html:select property="appointmentMonth">
                                <% for (int i=1; i < 13; i = i + 1){
                                    String month = Integer.toString(i);
                                    if (i < 10){ month = "0"+month; }
                                %>
                                   <html:option value="<%=month%>"><%=month%></html:option>
                                <%}%>
                            </html:select>
                        </td>
                        <td class="tite3">
                            <html:select property="appointmentDay">
                                <% for (int i=1; i < 32; i = i + 1){
                                    String dayOfWeek = Integer.toString(i);
                                    if (i < 10){ dayOfWeek = "0"+dayOfWeek;}
                                %>
                                   <html:option value="<%=dayOfWeek%>"><%=dayOfWeek%></html:option>
                                <%}%>

                            </html:select>
                        </td>
                    </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAppointmentTime"/>:
                </td>
                <td align="right" class="tite3">
                    <table>
                        <tr>
                            <td>
                                <html:select property="appointmentHour">
                                <% for (int i=1; i < 13; i = i + 1){
                                    String hourOfday = Integer.toString(i);
                                %>
                                   <html:option value="<%=hourOfday%>"><%=hourOfday%></html:option>
                                <%}%>
                                </html:select>
                            </td>
                            <td>
                                <html:select property="appointmentMinute">
                                <% for (int i=0; i < 60; i = i + 1){
                                    String minuteOfhour = Integer.toString(i);
                                    if (i < 10){ minuteOfhour = "0"+minuteOfhour;}
                                %>
                                   <html:option value="<%=minuteOfhour%>"><%=minuteOfhour%></html:option>
                                <%}%>
                                </html:select>
                            </td>
                            <td>
                                <html:select property="appointmentPm">
                                  <html:option value="AM">AM</html:option>
                                  <html:option value="PM">PM</html:option>
                                </html:select>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            </table>
        </td>
        <td valign="top"  cellspacing="1" class="tite4">
            <table border=0 width="100%" bgcolor="white">
            <tr>
                <td class="tite4">
                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPatient"/>:
                </td>
                <td class="tite1">
                    <%=consultUtil.patientName%>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgAddress"/>:
                </td>
                <td class="tite1">
                    <%=consultUtil.patientAddress%>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgPhone"/>:
                </td >
                <td class="tite2">
                    <%=consultUtil.patientPhone%>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgBirthDate"/>:
                </td>
                <td class="tite2">
                    <%=consultUtil.patientDOB%>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSex"/>:
                </td>
                <td class="tite3">
                    <%=consultUtil.patientSex%>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgHealthCard"/>:
                </td>
                <td class="tite3">
                    <%=consultUtil.patientHealthNum%>&nbsp;<%=consultUtil.patientHealthCardVersionCode%>&nbsp;<%=consultUtil.patientHealthCardType%>
                </td>
            </tr>
            <tr>
                <td class="tite4">
                &nbsp;
                </td>
                <td class="tite4">
                &nbsp;
                </td>
            </tr>
            <tr>
                <td class="tite4">
                    <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgSendTo"/>:
                </td>
                <td class="tite3">
                    <html:select property="sendTo">
                        <html:option value="-1">---- <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.msgTeams"/> ----</html:option>
                        <% for (int i =0; i < consultUtil.teamVec.size();i++){
                            String te = (String) consultUtil.teamVec.elementAt(i);
                        %>
                            <html:option value="<%=te%>"><%=te%></html:option>
                        <%}%>
                    </html:select>
                </td>
            </tr>

            </table>
        </td>
       </tr>
       <tr>
            <td colspan=2>
                &nbsp;
            <td>
       </tr>
       <tr>
            <td colspan=2 class="tite4">
            <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formReason"/>:
            </td>
       </tr>
       <tr>
            <td colspan=2>
                <html:textarea property="reasonForConsultation"  cols="90" rows="3" ></html:textarea>
            </td>
       </tr>
       <tr>
            <td colspan=2 class="tite4">
            <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formClinInf"/>:
            </td>
       </tr>
       <tr>
            <td colspan=2>
                <html:textarea cols="90" rows="3" property="clinicalInformation"></html:textarea>
            </td>
       </tr>
       <tr>
            <td colspan=2 class="tite4">
            <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formSignificantProblems"/>:
            </td>
       </tr>
       <tr>
            <td colspan=2>
                <html:textarea cols="90" rows="3" property="concurrentProblems"></html:textarea>
            </td>
       </tr>
       <tr>
            <td colspan=2 class="tite4">
            <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formCurrMedications"/>:
            </td>
       </tr>
       <tr>
            <td colspan=2 >
                <html:textarea cols="90" rows="3" property="currentMedications"></html:textarea>
            </td>
       </tr>
       <tr>
            <td colspan=2 class="tite4">
            <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAllergies"/>:
            </td>
       </tr>
       <tr>
            <td colspan=2>
                <html:textarea cols="90" rows="3" property="allergies"></html:textarea>
            </td>
       </tr>
       <tr>
            <td colspan=2 class="tite4">
            <bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.formAppointmentNotes"/>
            </td>
       </tr>
       <tr>
            <td colspan=2>
                <html:textarea cols="90" rows="3" property="appointmentNotes"></html:textarea>
            </td>
       </tr>
       <tr>
            <td colspan=2>
	        <input type="hidden" name="submission" value="">
                <%if (request.getAttribute("id") != null){ %>
      		<input type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdate"/>" onclick="document.forms['EctConsultationFormRequestForm'].submission.value='Update Consultation Request'; document.forms['EctConsultationFormRequestForm'].submit();"/>
                <input type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnUpdateAndPrint"/>"  onclick="document.forms['EctConsultationFormRequestForm'].submission.value='Update Consultation Request And Print Preview'; document.forms['EctConsultationFormRequestForm'].submit();"/>
                <%}else{%>
      		<input type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmit"/>" onclick="document.forms['EctConsultationFormRequestForm'].submission.value='Submit Consultation Request'; document.forms['EctConsultationFormRequestForm'].submit();"/>
                <input type="button" value="<bean:message key="oscarEncounter.oscarConsultationRequest.ConsultationFormRequest.btnSubmitAndPrint"/>"  onclick="document.forms['EctConsultationFormRequestForm'].submission.value='Submit Consultation Request And Print Preview'; document.forms['EctConsultationFormRequestForm'].submit();"/>

                <%}%>
            </td>
       </tr>

       <SCRIPT LANGUAGE=JAVASCRIPT>
        <!--
	        initMaster();
        	initService('<%=consultUtil.service%>');
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
        </SCRIPT>




            <!----End new rows here-->

		        <tr height="100%">
                    <td>
                    </td>
                </tr>
            </table>
        </td>
    </tr>

	<tr>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
    	<td height="0%" style="border-bottom:2px solid #A9A9A9; border-top:2px solid #A9A9A9; "></td>
  	</tr>

  	<tr>
    	<td width="100%" height="0%" colspan="2">&nbsp;</td>
  	</tr>
  	<tr>
    	<td width="100%" height="0%" style="padding: 5" bgcolor="#DCDCDC" colspan="2"></td>
  	</tr>
</table>
</html:form>

</body>
</html:html>


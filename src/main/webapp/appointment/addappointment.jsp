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
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="org.oscarehr.util.SessionConstants"%>
<%@page import="org.oscarehr.common.model.ProviderPreference"%>
<%@page import="oscar.oscarBilling.ca.bc.decisionSupport.BillingGuidelines"%>
<%@page import="org.oscarehr.decisionSupport.model.DSConsequence"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="java.util.Set,java.util.HashSet"%>
<%
 
  String DONOTBOOK = "Do_Not_Book";
  String curProvider_no = request.getParameter("provider_no");
  String curDoctor_no = request.getParameter("doctor_no") != null ? request.getParameter("doctor_no") : "";
  String curUser_no = (String) session.getAttribute("user");
  String userfirstname = (String) session.getAttribute("userfirstname");
  String userlastname = (String) session.getAttribute("userlastname");

  ProviderPreference providerPreference=(ProviderPreference)session.getAttribute(SessionConstants.LOGGED_IN_PROVIDER_PREFERENCE);
  int everyMin=providerPreference.getEveryMin();

  boolean bFirstDisp=true; //this is the first time to display the window
  boolean bFromWL=false; //this is from waiting list page

  if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
  if (request.getParameter("demographic_no")!=null) bFromWL=true;

  String duration = request.getParameter("duration")!=null?(request.getParameter("duration").equals(" ")||request.getParameter("duration").equals("")||request.getParameter("duration").equals("null")?(""+everyMin) :request.getParameter("duration")):(""+everyMin) ;

  //check for management fee code eligibility
  Set<String> billingRecommendations = new HashSet<String>();
  try{
    List<DSConsequence> list = BillingGuidelines.getInstance().evaluateAndGetConsequences(request.getParameter("demographic_no"), curProvider_no);

    for (DSConsequence dscon : list){
        if (dscon.getConsequenceStrength().equals(DSConsequence.ConsequenceStrength.recommendation)) {
            String recommendation = new String(dscon.getText());
            billingRecommendations.add(recommendation);
        }
    }
  }catch(Exception e){
    MiscUtils.getLogger().error("Error", e);
  }
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*, oscar.appt.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.appt.status.service.AppointmentStatusMgr"%>
<%@ page import="oscar.appt.status.service.impl.AppointmentStatusMgrImpl"%>
<%@ page import="org.oscarehr.common.model.AppointmentStatus"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="oscar.oscarEncounter.data.EctFormData"%>
<%@ page import="org.oscarehr.common.model.DemographicCust" %>
<%@ page import="org.oscarehr.common.dao.DemographicCustDao" %>
<%@ page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.common.model.Demographic" %>
<%@ page import="org.oscarehr.common.dao.DemographicDao" %>
<%@ page import="org.oscarehr.common.model.EncounterForm" %>
<%@ page import="org.oscarehr.common.dao.EncounterFormDao" %>
<%@ page import="org.oscarehr.common.model.Appointment" %>
<%@ page import="org.oscarehr.common.dao.OscarAppointmentDao" %>
<%@ page import="oscar.util.ConversionUtils" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="org.oscarehr.PMmodule.model.Program" %>
<%@ page import="org.oscarehr.PMmodule.model.ProgramProvider" %>
<%@ page import="org.oscarehr.common.model.Facility" %>
<%@ page import="org.oscarehr.PMmodule.service.ProviderManager" %>
<%@ page import="org.oscarehr.PMmodule.service.ProgramManager" %>
<%@ page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page import="org.oscarehr.managers.LookupListManager"%>
<%@ page import="org.oscarehr.common.model.LookupList"%>
<%@ page import="org.oscarehr.common.model.LookupListItem"%>

<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>

<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />

<%
	DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	DemographicDao demographicDao = SpringUtils.getBean(DemographicDao.class);
	EncounterFormDao encounterFormDao = SpringUtils.getBean(EncounterFormDao.class);
	OscarAppointmentDao appointmentDao = SpringUtils.getBean(OscarAppointmentDao.class);
	
	ProviderManager providerManager = SpringUtils.getBean(ProviderManager.class);
	ProgramManager programManager = SpringUtils.getBean(ProgramManager.class);
	
	String providerNo = LoggedInInfo.loggedInInfo.get().loggedInProvider.getProviderNo();
	Facility facility = LoggedInInfo.loggedInInfo.get().currentFacility;
	
    List<Program> programs = programManager.getActiveProgramByFacility(providerNo, facility.getId());

	LookupListManager lookupListManager = SpringUtils.getBean(LookupListManager.class);
	LookupList reasonCodes = lookupListManager.findLookupListByName("reasonCode");

    int iPageSize=5;

    ApptData apptObj = ApptUtil.getAppointmentFromSession(request);

    oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
    String strEditable = pros.getProperty("ENABLE_EDIT_APPT_STATUS");
    Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;

    AppointmentStatusMgr apptStatusMgr = new AppointmentStatusMgrImpl();
    List<AppointmentStatus> allStatus = apptStatusMgr.getAllActiveStatus();
    
    String useProgramLocation = OscarProperties.getInstance().getProperty("useProgramLocation");
    String moduleNames = OscarProperties.getInstance().getProperty("ModuleNames");
    boolean caisiEnabled = moduleNames != null && org.apache.commons.lang.StringUtils.containsIgnoreCase(moduleNames, "Caisi");
    boolean locationEnabled = caisiEnabled && (useProgramLocation != null && useProgramLocation.equals("true"));
%>
<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.Site"%>
<html:html locale="true">
<head>
<script type="text/javascript" src="../js/jquery-1.7.1.min.js"></script>
<script src="<%=request.getContextPath()%>/js/jquery-ui-1.8.18.custom.min.js"></script>
<script src="<%=request.getContextPath()%>/js/fg.menu.js"></script>

<link rel="stylesheet" href="<%=request.getContextPath()%>/css/cupertino/jquery-ui-1.8.18.custom.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/css/fg.menu.css">

<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/checkDate.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>
<% if (isMobileOptimized) { %>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
    <link rel="stylesheet" href="../mobile/appointmentstyle.css" type="text/css">
<% } else { %>
    <link rel="stylesheet" href="appointmentstyle.css" type="text/css">
<% }%>
<title><bean:message key="appointment.addappointment.title" /></title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="addappt"/>
<script type="text/javascript">

function onAdd() {
    if (document.ADDAPPT.notes.value.length > 255) {
      window.alert("<bean:message key="appointment.editappointment.msgNotesTooBig"/>");
      return false;
    }
    return calculateEndTime() ;
}


<!--
function setfocus() {
	this.focus();
  document.ADDAPPT.keyword.focus();
  document.ADDAPPT.keyword.select();
}

function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}

function onBlockFieldFocus(obj) {
  obj.blur();
  document.ADDAPPT.keyword.focus();
  document.ADDAPPT.keyword.select();
  window.alert("<bean:message key="Appointment.msgFillNameField"/>");
}

function checkTypeNum(typeIn) {
	var typeInOK = true;
	var i = 0;
	var length = typeIn.length;
	var ch;

	// walk through a string and find a number
	if (length>=1) {
	  while (i <  length) {
		  ch = typeIn.substring(i, i+1);
		  if (ch == ":") { i++; continue; }
		  if ((ch < "0") || (ch > "9") ) {
			  typeInOK = false;
			  break;
		  }
	    i++;
      }
	} else typeInOK = false;
	return typeInOK;
}

function checkTimeTypeIn(obj) {
  var colonIdx;
  if(!checkTypeNum(obj.value) ) {
	  alert ("<bean:message key="Appointment.msgFillTimeField"/>");
  } else {
      colonIdx = obj.value.indexOf(':');
      if(colonIdx==-1) {
        if(obj.value.length < 3) alert("<bean:message key="Appointment.msgFillValidTimeField"/>");
        obj.value = obj.value.substring(0, obj.value.length-2 )+":"+obj.value.substring( obj.value.length-2 );
  }
}
          
  var hours = "";
  var minutes = "";  

  colonIdx = obj.value.indexOf(':');  
  if (colonIdx < 1)
      hours = "00";     
  else if (colonIdx == 1)
      hours = "0" + obj.value.substring(0,1);
  else
      hours = obj.value.substring(0,2);
  
  minutes = obj.value.substring(colonIdx+1,colonIdx+3);
  if (minutes.length == 0)
	    minutes = "00";
  else if (minutes.length == 1)
    minutes = "0" + minutes;
  else if (minutes > 59)
    minutes = "00";

  obj.value = hours + ":" + minutes;    
}

var readOnly=false;
function checkDateTypeIn(obj) {
    if (obj.value == '') {
        alert("Date cannot be empty");
        return false;
    } else { 
        obj.value = obj.value.replace(/\//g,"-");
        if (!check_date(obj.name))
          return false;
    } 
}

function calculateEndTime() {
  var stime = document.ADDAPPT.start_time.value;
  var vlen = stime.indexOf(':')==-1?1:2;
  var shour = stime.substring(0,2) ;
  var smin = stime.substring(stime.length-vlen) ;
  var duration = document.ADDAPPT.duration.value ;
  
  if(isNaN(duration)) {
	  alert("<bean:message key="Appointment.msgFillTimeField"/>");
	  return false;
  }

  if(eval(duration) == 0) { duration =1; }
  if(eval(duration) < 0) { duration = Math.abs(duration) ; }

  var lmin = eval(smin)+eval(duration)-1 ;
  var lhour = parseInt(lmin/60);

  if((lmin) > 59) {
    shour = eval(shour) + eval(lhour);
    shour = shour<10?("0"+shour):shour;
    smin = lmin - 60*lhour;
  } else {
    smin = lmin;
  }

  smin = smin<10?("0"+ smin):smin;
  document.ADDAPPT.end_time.value = shour +":"+ smin;

  if(shour > 23) {
    alert("<bean:message key="Appointment.msgCheckDuration"/>");
    return false;
  }

  //no show
  if(document.ADDAPPT.keyword.value.substring(0,1)=="." && document.ADDAPPT.demographic_no.value=="" ) {
    document.ADDAPPT.status.value = 'N' ;
  }

  return true;
}

function onNotBook() {
	document.forms[0].keyword.value = "<%=DONOTBOOK%>" ;
}

function onButRepeat() {
	document.forms[0].action = "appointmentrepeatbooking.jsp" ;
	if (calculateEndTime()) { document.forms[0].submit(); }
}
<% if(apptObj!=null) { %>
function pasteAppt(multipleSameDayGroupAppt) {

        var warnMsgId = document.getElementById("tooManySameDayGroupApptWarning");

        if (multipleSameDayGroupAppt) {
           warnMsgId.style.display = "block";
           if (document.forms[0].groupButton) {
              document.forms[0].groupButton.style.display = "none";
           }
           if (document.forms[0].addPrintPreviewButton){
              document.forms[0].addPrintPreviewButton.style.display = "none";
           }
           document.forms[0].addButton.style.display = "none";
           document.forms[0].printButton.style.display = "none";

           if (document.forms[0].pasteButton) {
                document.forms[0].pasteButton.style.display = "none";
           }

           if (document.forms[0].apptRepeatButton) {
                document.forms[0].apptRepeatButton.style.display = "none";
           }
        }
        else {
           warnMsgId.style.display = "none";
        }
        //document.forms[0].status.value = "<%=apptObj.getStatus()%>";
        document.forms[0].duration.value = "<%=apptObj.getDuration()%>";
        //document.forms[0].chart_no.value = "<%=apptObj.getChart_no()%>";
        document.forms[0].keyword.value = "<%=apptObj.getName()%>";
        document.forms[0].demographic_no.value = "<%=apptObj.getDemographic_no()%>";
        document.forms[0].reason.value = "<%= StringEscapeUtils.escapeJavaScript(apptObj.getReason()) %>";
        document.forms[0].notes.value = "<%= StringEscapeUtils.escapeJavaScript(apptObj.getNotes()) %>";
        //document.forms[0].location.value = "<%=apptObj.getLocation()%>";
        document.forms[0].resources.value = "<%=apptObj.getResources()%>";
        document.forms[0].type.value = "<%=apptObj.getType()%>";
        if('<%=apptObj.getUrgency()%>' == 'critical') {
                document.forms[0].urgency.checked = "checked";
        }

}
<% } %>


	function openTypePopup () {
		windowprops = "height=170,width=500,location=no,scrollbars=no,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=100,left=100";
		var popup=window.open("appointmentType.jsp?type="+document.forms['ADDAPPT'].type.value, "Appointment Type", windowprops);
		if (popup != null) {
			if (popup.opener == null) {
				popup.opener = self;
			}
			popup.focus();
		}
	}

	function setType(typeSel,reasonSel,locSel,durSel,notesSel,resSel) {
		  document.forms['ADDAPPT'].type.value = typeSel;
		  document.forms['ADDAPPT'].reason.value = reasonSel;
		  document.forms['ADDAPPT'].duration.value = durSel;
		  document.forms['ADDAPPT'].notes.value = notesSel;
		  document.forms['ADDAPPT'].duration.value = durSel;
		  document.forms['ADDAPPT'].resources.value = resSel;
		  var loc = document.forms['ADDAPPT'].location;
		  if(loc.nodeName == 'SELECT') {
		          for(c = 0;c < loc.length;c++) {
		                  if(loc.options[c].innerHTML == locSel) {
		                          loc.selectedIndex = c;
		                          loc.style.backgroundColor=loc.options[loc.selectedIndex].style.backgroundColor;
		                          break;
		                  }
		          }
		  } else if (loc.nodeName == "INPUT") {
			  document.forms['ADDAPPT'].location.value = locSel;
		  }
	}


// stop javascript -->

</script>

<%

  SimpleDateFormat fullform = new SimpleDateFormat ("yyyy-MM-dd HH:mm");
  SimpleDateFormat inform = new SimpleDateFormat ("yyyy-MM-dd");
  SimpleDateFormat outform = new SimpleDateFormat ("EEE");

  java.util.Date apptDate;

  if(request.getParameter("year")==null || request.getParameter("month")==null || request.getParameter("day")==null){
    Calendar cal = Calendar.getInstance();
    String sDay = String.valueOf(cal.get(Calendar.DATE));
    String sMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
    String sYear = String.valueOf(cal.get(Calendar.YEAR));
    String sTime=(request.getParameter("start_time")==null)?"00:00AM":request.getParameter("start_time");
    apptDate = fullform.parse(bFirstDisp?(sYear + "-" + sMonth + "-" + sDay + " "+ sTime):
        (request.getParameter("appointment_date") + " " + sTime)) ;
  }else if(request.getParameter("start_time")==null){
    apptDate = fullform.parse(bFirstDisp?(request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day")+" "+ "00:00 AM"):
        (request.getParameter("appointment_date") + " " + "00:00AM")) ;
  }else
  {
    apptDate = fullform.parse(bFirstDisp?(request.getParameter("year") + "-" + request.getParameter("month") + "-" + request.getParameter("day")+" "+ request.getParameter("start_time")):
        (request.getParameter("appointment_date") + " " + request.getParameter("start_time"))) ;
  }

  String dateString1 = outform.format(apptDate );
  String dateString2 = inform.format(apptDate );

  GregorianCalendar caltime =new GregorianCalendar( );
  caltime.setTime(apptDate);
  caltime.add(GregorianCalendar.MINUTE, Integer.parseInt(duration)-1 );

  java.util.Date startTime = ConversionUtils.fromDateString(request.getParameter("start_time"),"HH:mm");
  java.util.Date endTime = ConversionUtils.fromDateString(caltime.get(Calendar.HOUR_OF_DAY) +":"+ caltime.get(Calendar.MINUTE),"HH:mm");
  
  List<Appointment> appts = appointmentDao.search_appt(apptDate, curProvider_no, startTime, endTime, startTime, endTime, startTime, endTime, Integer.parseInt((String)request.getSession().getAttribute("programId_oscarView")));
  
  long apptnum = appts.size() > 0 ? new Long(appts.size()) : 0;
  
  OscarProperties props = OscarProperties.getInstance();
  
  String timeoutSeconds = props.getProperty("appointment_locking_timeout","0");
  int timeoutSecs = 0; 
  try { 
    timeoutSecs = Integer.parseInt(timeoutSeconds);
  }catch (NumberFormatException e) {/*empty*/}
  
  int hourInt = caltime.get(Calendar.HOUR_OF_DAY); 
  String hour = String.valueOf(hourInt);
  if (hour.length() == 0)
      hour = "00";
   else if (hour.length() == 1)
      hour = "0" + hour;
  
  int minuteInt = caltime.get(Calendar.MINUTE); 
  String minute = String.valueOf(minuteInt);
   if (minute.length() == 0)
      minute = "00";
   else if (minute.length() == 1) 
      minute = "0" + minute;  
  
   if (timeoutSecs > 0) {
%>

<script>
        var timers = new Array();

	$(document).ready(function(){
           $(window).bind('beforeunload',function(){cancelPageLock();
           });
           //cancel any page view/locks held by provider on clicking 'X'
           $("form#addappt").submit(function() {$(window).unbind('beforeunload');
           });

           calculateEndTime();
           var endTime = document.forms[0].end_time.value;
           var startTime = document.forms[0].start_time.value;
           var apptDate = document.forms[0].appointment_date.value; 
           updatePageLock(100,apptDate,startTime,endTime);	   
	});
        
        function checkPageLock() {
           $("#searchBtn").attr("disabled","disabled");
           calculateEndTime();
           var endTime = document.forms[0].end_time.value;
           var startTime = document.forms[0].start_time.value;
           var apptDate = document.forms[0].appointment_date.value;
           updatePageLock(100,apptDate,startTime,endTime);
           
        }
        
        function updatePageLock(timeout, apptDate, startTime, endTime) {

           for (var i = 0; i < timers.length; i++) {
                clearTimeout(timers[i]);               
           }
                      
	   haveLock=false;
           $.ajax({
               type: "POST",
               url: "<%=request.getContextPath()%>/PageMonitoringService.do",
               data: { method: "update", page: "addappointment", pageId: "<%=curProvider_no%>|" + apptDate + "|" + startTime + "|" + endTime, lock: true, timeout: <%=timeoutSeconds%>, cleanupExisting: true},
               dataType: 'json',
               async: false,
               success: function(data,textStatus) {
                       lockData=data;
                            var locked=false;
                            var lockedProviderName='';
                            var providerNames='';
                            haveLock=false;
                       $.each(data, function(key, val) {				
                         if(val.locked) {
                             locked=true;
                             lockedProviderName=val.providerName;
                         }
                         if(val.locked==true && val.self==true) {
                                       haveLock=true;
                               }
                         if(providerNames.length > 0)
                             providerNames += ",";
                         providerNames += val.providerName;

                       });

                       var lockedMsg = locked?'<span style="color:red" title="'+lockedProviderName+'">&nbsp(locked)</span>':'';
                       $("#lock_notification").html(
                            '<span title="'+providerNames+'">Viewers:'+data.length+lockedMsg+'</span>'	   
                       );


                       if(haveLock==true) { //i have the lock
                            $("#addButton").show(); 
                            $("#printButton").show();
                            $("#addPrintPreviewButton").show(); 
                            $("#pasteButton").show(); 
                            $("#apptRepeatButton").show(); 
                       } else if(locked && !haveLock) { //someone else has lock.
                            $("#addButton").hide(); 
                            $("#printButton").hide();
                            $("#addPrintPreviewButton").hide(); 
                            $("#pasteButton").hide(); 
                            $("#apptRepeatButton").hide();                          
                       } else { //no lock
                            $("#addButton").show(); 
                            $("#printButton").show();
                            $("#addPrintPreviewButton").show(); 
                            $("#pasteButton").show(); 
                            $("#apptRepeatButton").show();                                 
                       }
                       $("#searchBtn").removeAttr("disabled");
               }
             }
            );
            
            timers.push(setTimeout(function(){updatePageLock(5000, apptDate, startTime, endTime)},timeout));      
        }
        
        function cancelPageLock() {
           calculateEndTime();
           var endTime = document.forms[0].end_time.value;
           var startTime = document.forms[0].start_time.value;
           var apptDate = document.forms[0].appointment_date.value;
           
           for (var i = 0; i < timers.length; i++) {
               clearTimeout(timers[i]);               
           }
           
           $.ajax({
               type: "POST",
               url: "<%=request.getContextPath()%>/PageMonitoringService.do",
               data: { method: "cancel", page: "addappointment", pageId: "<%=curProvider_no%>|" + apptDate + "|" + startTime + "|" + endTime},
               dataType: 'json',
               async: false,
               success: function(data,textStatus) {}
           });
        }

</script>

<%
 } else {        
%>
<script>
    function checkPageLock() { //don't do anything unless timeout/locking is enabled.
    }
    function cancelPageLock() { //don't do anything unless timeout/locking is enabled.
    }
</script>
<%
  }
  String deepcolor = apptnum==0?"#CCCCFF":"gold", weakcolor = apptnum==0?"#EEEEFF":"ivory";
  if (!isMobileOptimized) {
%>
      <!-- Change the background color of deep/weak sections -->
      <style type="text/css">
          .deep { background-color: <%= deepcolor %>; }
          .weak { background-color: <%= weakcolor %>; }
      </style>
  
<%
  }
  boolean bDnb = false;
  for(Appointment a : appts) {
	  String apptName = a.getName();
	  if (apptName.equalsIgnoreCase(DONOTBOOK)) bDnb = true;
  }
 

  // select provider lastname & firstname
  String pLastname = "";
  String pFirstname = "";
  Provider p = providerDao.getProvider(curProvider_no);
  if(p != null) {
	  pLastname = p.getLastName();
      pFirstname = p.getFirstName();
  }
%>
</head>
<body bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">
 <% if (timeoutSecs >0) { %>
    <div id="lock_notification">
        <span title="">Viewers: N/A</span>
    </div>
 <% } %>
<%
  String patientStatus = "";
  String disabled="";
  String address ="";
  String province = "";
  String city ="";
  String postal ="";
  String phone = "";
  String phone2 = "";
  String email  = "";
  String hin = "";
  String dob = "";
  String sex = "";

  //to show Alert msg

  boolean bMultipleSameDayGroupAppt = false;
  String displayStyle = "display:none";
  String myGroupNo = providerPreference.getMyGroupNo();

  if (props.getProperty("allowMultipleSameDayGroupAppt", "").equalsIgnoreCase("no")) {

        String demographicNo = request.getParameter("demographic_no");

        if (!bFirstDisp && (demographicNo != null) && (!demographicNo.equals(""))) {

        	
        	appts = appointmentDao.search_group_day_appt(myGroupNo, Integer.parseInt(demographicNo), apptDate);
            
            long numSameDayGroupAppts = appts.size() > 0 ? new Long(appts.size()) : 0;
            bMultipleSameDayGroupAppt = (numSameDayGroupAppts > 0);
        }

        if (bMultipleSameDayGroupAppt){
            displayStyle="display:block";
        }
  }
  %>
  <div id="tooManySameDayGroupApptWarning" style="<%=displayStyle%>">
    <table width="98%" BGCOLOR="red" border=1 align='center'>
        <tr>
            <th>
                <font color='white'>
                    <bean:message key='appointment.addappointment.titleMultipleGroupDayBooking'/><br/>
                    <bean:message key='appointment.addappointment.MultipleGroupDayBooking'/>
                </font>
            </th>
        </tr>
    </table>
</div>
<%
  if (!bFirstDisp && request.getParameter("demographic_no") != null && !request.getParameter("demographic_no").equals("")) {
	  Demographic d = demographicDao.getDemographic(request.getParameter("demographic_no"));
	  if(d != null) {
		  patientStatus = d.getPatientStatus();
		  address = d.getAddress();
		  city = d.getCity();
		  province = d.getProvince();
		  postal = d.getPostal();
		  phone = d.getPhone();
		  phone2 = d.getPhone2();
		  email = d.getEmail();
		  String year_of_birth   = d.getYearOfBirth();
	      String month_of_birth  = d.getMonthOfBirth();
	      String date_of_birth   = d.getDateOfBirth();
	      dob = "("+year_of_birth+"-"+month_of_birth+"-"+date_of_birth+")";
	      sex = d.getSex();
	      hin = d.getHin();
	      String ver = d.getVer();
	      hin = hin +" "+ ver;
	        
	      if (patientStatus == null || patientStatus.equalsIgnoreCase("AC")) {
	        patientStatus = "";
	      } else if (patientStatus.equalsIgnoreCase("FI")||patientStatus.equalsIgnoreCase("DE")||patientStatus.equalsIgnoreCase("IN")) {
	      	disabled = "disabled";
	      }

	      String rosterStatus = d.getRosterStatus();
	      if (rosterStatus == null || rosterStatus.equalsIgnoreCase("RO")) {
	      	rosterStatus = "";
	      }

	      if(!patientStatus.equals("") || !rosterStatus.equals("") ) {
	      	String rsbgcolor = "BGCOLOR=\"orange\"" ;
	        String exp = " null-undefined\n IN-inactive ID-deceased OP-out patient\n NR-not signed\n FS-fee for service\n TE-terminated\n SP-self pay\n TP-third party";

%>
<table width="98%" <%=rsbgcolor%> border=0 align='center'>
	<tr>
		<td><font color='blue' title='<%=exp%>'> <b><bean:message key="Appointment.msgPatientStatus" />:&nbsp;
                    <font color='yellow'><%=patientStatus%></font>&nbsp;<bean:message key="Appointment.msgRosterStatus" />:&nbsp;
                    <font color='yellow'><%=rosterStatus%></font></b></font>
                </td>
	</tr>
</table>
<%

        }
	}
	if( request.getParameter("demographic_no")!=null && !"".equals(request.getParameter("demographic_no")) ) {
	DemographicCust demographicCust = demographicCustDao.find(Integer.parseInt(request.getParameter("demographic_no")));

		if (demographicCust != null && demographicCust.getAlert() != null && !demographicCust.getAlert().equals("") ) {

%>
<p>
<table width="98%" BGCOLOR="yellow" border=1 align='center'>
	<tr>
		<td><font color='red'><bean:message key="Appointment.formAlert" />: <b><%=demographicCust.getAlert()%></b></font></td>
	</tr>
</table>
<%

		}
	}
  }


  if(apptnum!=0) {

%>
<table width="98%" class="deep" border=1 align='center'>
	<tr>
		<%--    <TH><font color='red'><%=apptnum>1?"Double ++ ":"Double"%> Booking</font></TH>--%>
		<TH><font color='red'> <% if(apptnum>1) {

         %> <bean:message key='appointment.addappointment.msgBooking' />
		<%

       } else {

          %> <bean:message
			key='appointment.addappointment.msgDoubleBooking' /> <%
			if(bDnb) out.println("<br/>You can NOT book an appointment on this time slot.");
       }

     %> </font></TH>
	</tr>
</table>
<% } %>

<% if (billingRecommendations.size() > 0) { %>
        <table width="98%" align="center" style="border:solid 3px red;padding-left:10px;line-height:150%;font-family:Arial;color: red; background-color: #FFFFFF; font-size: 18px; font-weight: bold; text-align:left;">
            <% for (String recommendation : billingRecommendations) { %>
                <tr>
                    <th><%=recommendation%></th>
                </tr>
            <% } %>
        </table>
<% } %>

<FORM NAME="ADDAPPT" id="addappt" METHOD="post" ACTION="<%=request.getContextPath()%>/appointment/appointmentcontrol.jsp"
	onsubmit="return(onAdd())"><INPUT TYPE="hidden"
	NAME="displaymode" value="">
	<input type="hidden" name="year" value="<%=request.getParameter("year") %>" >
    <input type="hidden" name="month" value="<%=request.getParameter("month") %>" >
    <input type="hidden" name="day" value="<%=request.getParameter("day") %>" >
    <input type="hidden" name="fromAppt" value="1" >
	
<div class="header deep">
    <div class="title">
        <!-- We display a shortened title for the mobile version -->
        <% if (isMobileOptimized) { %><bean:message key="appointment.addappointment.msgMainLabelMobile" />
        <% } else { %><bean:message key="appointment.addappointment.msgMainLabel" />
        <%          out.println("("+pFirstname+" "+pLastname+")"); %>
        <% } %>
    </div>
</div>
<div class="panel">
    <ul>
        <li class="row weak">
            <div class="label"><bean:message key="Appointment.formDate" /><font size='-1' color='brown'>(<%=dateString1%>)</font>:</div>
            <div class="input">
                <INPUT TYPE="TEXT" NAME="appointment_date"
                    VALUE="<%=dateString2%>" WIDTH="25" HEIGHT="20" border="0"
                    hspace="2" onChange="checkDateTypeIn(this);checkPageLock()">
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formStatus" />:</div>
            <div class="input">
				<%
            if (strEditable!=null&&strEditable.equalsIgnoreCase("yes")){
            %> <select name="status" STYLE="width: 154px" HEIGHT="20"
                    border="0">
                    <% for (int i = 0; i < allStatus.size(); i++) { %>
                    <option
                            value="<%=(allStatus.get(i)).getStatus()%>"
                            <%=(allStatus.get(i)).getStatus().equals(request.getParameter("status"))?"SELECTED":""%>><%=(allStatus.get(i)).getDescription()%></option>
                    <% } %>
            </select> <%
            }
            if (strEditable==null || !strEditable.equalsIgnoreCase("yes")){
            %> <INPUT TYPE="TEXT" NAME="status"
					VALUE='<%=bFirstDisp?"t":request.getParameter("status")==null?"":request.getParameter("status").equals("")?"":request.getParameter("status")%>'
					WIDTH="25" HEIGHT="20" border="0" hspace="2"> <%}%>
            </div>
        </li>
        <li class="row weak">
            <div class="label"><bean:message key="Appointment.formStartTime" />:</div>
            <div class="input">
                <INPUT TYPE="TEXT" NAME="start_time"
                    VALUE='<%=request.getParameter("start_time")%>' WIDTH="25"
                    HEIGHT="20" border="0" onChange="checkTimeTypeIn(this);checkPageLock()">
            </div>
            <div class="space">&nbsp;</div>

            <%
				    // multisites start ==================
				    boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();
				    SiteDao siteDao = (SiteDao)SpringUtils.getBean("siteDao");
				    List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));
				    // multisites end ==================

				    boolean bMoreAddr = bMultisites? true : props.getProperty("scheduleSiteID", "").equals("") ? false : true;
				    String tempLoc = "";
				    if(bFirstDisp && bMoreAddr) {
				            tempLoc = (new JdbcApptImpl()).getLocationFromSchedule(dateString2, curProvider_no);
				    }
				    String loc = bFirstDisp?tempLoc:request.getParameter("location");
				    String colo = bMultisites
				                                        ? ApptUtil.getColorFromLocation(sites, loc)
				                                        : bMoreAddr? ApptUtil.getColorFromLocation(props.getProperty("scheduleSiteID", ""), props.getProperty("scheduleSiteColor", ""),loc) : "white";
			%>                                    
					<div class="input" style="text-align: right;"> <INPUT TYPE="button" NAME="typeButton" VALUE="<bean:message key="Appointment.formType"/>" onClick="openTypePopup()"> </div>

            <div class="input">
                <INPUT TYPE="TEXT" NAME="type"
                    VALUE='<%=bFirstDisp?"":request.getParameter("type").equals("")?"":request.getParameter("type")%>'
                    WIDTH="25" HEIGHT="20" border="0" hspace="2">
            </div>
        </li>
        <li class="row weak">
            <div class="label"><bean:message key="Appointment.formDuration" />:</div> <!--font face="arial"> End Time :</font-->
            <div class="input">
                <INPUT TYPE="TEXT" NAME="duration"
                        VALUE="<%=duration%>" WIDTH="25" HEIGHT="20" border="0" hspace="2" onChange="checkPageLock()">
                <INPUT TYPE="hidden" NAME="end_time"
                        VALUE='<%=request.getParameter("end_time")%>' WIDTH="25"
                        HEIGHT="20" border="0" hspace="2" onChange="checkTimeTypeIn(this)">
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formDoctor" />:</div>
            <div class="input">
                <INPUT type="TEXT" readonly
                       value="<%=bFirstDisp ? "" : StringEscapeUtils.escapeHtml(providerBean.getProperty(curDoctor_no,""))%>">
            </div>
        </li>
        <li class="row deep">
            <div class="label"><bean:message key="appointment.addappointment.formSurName" />:</div>
            <div class="input">
            	<% 
            		String name="";
            		name = String.valueOf((bFirstDisp && !bFromWL)?"":request.getParameter("name")==null?session.getAttribute("appointmentname")==null?"":session.getAttribute("appointmentname"):request.getParameter("name"));
            	%>
                <INPUT TYPE="TEXT" NAME="keyword"
                        VALUE="<%=name%>"
                        HEIGHT="20" border="0" hspace="2" width="25" tabindex="1">
            </div>
            <div class="space">
                <a href=# onclick="onNotBook();"><font size='-1' color='brown'>Not book</font></a>
            </div>
            <INPUT TYPE="hidden" NAME="orderby" VALUE="last_name, first_name">
<%
    String searchMode = request.getParameter("search_mode");
    if (searchMode == null || searchMode.isEmpty()) {
        searchMode = OscarProperties.getInstance().getProperty("default_search_mode","search_name");
    }
%> 
            <INPUT TYPE="hidden" NAME="search_mode" VALUE="<%=searchMode%>"> 
            <INPUT TYPE="hidden" NAME="originalpage" VALUE="../appointment/addappointment.jsp"> 
            <INPUT TYPE="hidden" NAME="limit1" VALUE="0"> 
            <INPUT TYPE="hidden" NAME="limit2" VALUE="5"> 
            <INPUT TYPE="hidden" NAME="ptstatus" VALUE="active"> 
			<input type="hidden" name="outofdomain" value="<%=OscarProperties.getInstance().getProperty("pmm.client.search.outside.of.domain.enabled","true")%>"/> 
            <!--input type="hidden" name="displaymode" value="Search " -->
            <div class="label">
                <INPUT TYPE="submit" name="searchBtn" id="searchBtn" style="width:auto;"
                    onclick="document.forms['ADDAPPT'].displaymode.value='Search '"
                    VALUE="<bean:message key="appointment.addappointment.btnSearch"/>">
            </div>
            <div class="input">
                <input type="TEXT" name="demographic_no"
                    ONFOCUS="onBlockFieldFocus(this)" readonly
                    value='<%=(bFirstDisp && !bFromWL)?"":request.getParameter("demographic_no").equals("")?"":request.getParameter("demographic_no")%>'
                    width="25" height="20" border="0" hspace="2">
            </div>
        </li>
        <li class="row deep">
            <div class="label"><bean:message key="Appointment.formReason" />:</div>
            <div class="input">
                <select name="reasonCode">
				    <%
				    if(reasonCodes != null) {
				    	for(LookupListItem reasonCode : reasonCodes.getItems()) {
				    %>
				    <option value="<%=reasonCode.getId()%>"><%=StringEscapeUtils.escapeHtml(reasonCode.getValue())%></option>
				    <%
				    	}
				    } else {
					%>
						<option value="-1">Other</option>
					<%
					}
					%>
				</select>
				</br>
				<textarea id="reason" name="reason" tabindex="2" rows="2" wrap="virtual" cols="18"><%=bFirstDisp?"":request.getParameter("reason").equals("")?"":request.getParameter("reason")%></textarea>
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formNotes" />:</div>
            <div class="input">
                <textarea name="notes" tabindex="3" rows="2" wrap="virtual" cols="18"><%=bFirstDisp?"":request.getParameter("notes").equals("")?"":request.getParameter("notes")%></textarea>
            </div>
        </li>
        <% if (pros.isPropertyActive("mc_number")) { %>
        <li class="row deep">
            <div class="label">M/C number:</div>
            <div class="input">
                <input type="text" name="appt_mc_number" tabindex="4" />
            </div>
            <div class="space">&nbsp;</div>
            <div class="label">&nbsp;</div>
            <div class="input">&nbsp;</div>
        </li>
        <% } %>
        <li class="row weak">
            <div class="label"><bean:message key="Appointment.formLocation" />:</div>

            <div class="input">
		<% // multisites start ==================
		if (bMultisites) { %>
	        <select tabindex="4" name="location" style="background-color: <%=colo%>" onchange='this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor'>
	        <% for (Site s:sites) { %>
	                <option value="<%=s.getName()%>" style="background-color: <%=s.getBgColor()%>" <%=s.getName().equals(loc)?"selected":"" %>><%=s.getName()%></option>
	        <% } %>
	        </select>
		<% } else {
			// multisites end ==================
			if (locationEnabled) {
		%>
			<select name="location" >
                <%
                String sessionLocation = StringUtils.defaultString((String) session.getAttribute("sessionLocation"));
                if (programs != null && !programs.isEmpty()) {
			       	for (Program program : programs) {
			       	    String description = StringUtils.isBlank(program.getLocation()) ? program.getName() : program.getLocation();
			   	%>
			        <option value="<%=program.getId()%>" <%=program.getId().toString().equals(sessionLocation) ? "selected='selected'" : ""%>><%=StringEscapeUtils.escapeHtml(description)%></option>
			    <%	}
                }
			  	%>
            </select>
        	<% } else { %>
        	<input type="TEXT" name="location" tabindex="4" value="<%=loc%>" width="25" height="20" border="0" hspace="2">	
        	<% } %>
		<% } %>
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formResources" />:</div>
            <div class="input">
                <input type="TEXT" name="resources"
                    tabindex="5"
                    value='<%=bFirstDisp?"":request.getParameter("resources").equals("")?"":request.getParameter("resources")%>'
                    width="25" height="20" border="0" hspace="2">
            </div>
        </li>
        <li class="row weak">
            <div class="label"><bean:message key="Appointment.formCreator" />:</div>
            <div class="input">
                <INPUT TYPE="TEXT" NAME="user_id" readonly
                    VALUE='<%=bFirstDisp?(StringEscapeUtils.escapeHtml(userlastname)+", "+StringEscapeUtils.escapeHtml(userfirstname)):request.getParameter("user_id").equals("")?"Unknown":request.getParameter("user_id")%>'
                    WIDTH="25" HEIGHT="20" border="0" hspace="2">
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formDateTime" />:</div>
            <div class="input">
<%
            GregorianCalendar now=new GregorianCalendar();
            GregorianCalendar cal = (GregorianCalendar) now.clone();
            cal.add(GregorianCalendar.DATE, 1);
            String strDateTime=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "
                + now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
%>
                <INPUT TYPE="TEXT" NAME="createdatetime" readonly VALUE="<%=strDateTime%>" WIDTH="25" HEIGHT="20" border="0" hspace="2">
                <INPUT TYPE="hidden" NAME="provider_no" VALUE="<%=curProvider_no%>">
                <INPUT TYPE="hidden" NAME="dboperation" VALUE="search_titlename">
                <INPUT TYPE="hidden" NAME="creator" VALUE='<%=StringEscapeUtils.escapeHtml(userlastname)+", "+StringEscapeUtils.escapeHtml(userfirstname)%>'>
                <INPUT TYPE="hidden" NAME="remarks" VALUE="">
            </div>
        </li>
        <li class="row weak">
            <% String emailReminder = pros.getProperty("emailApptReminder");
               if ((emailReminder != null) && emailReminder.equalsIgnoreCase("yes")) { %>
                    <div class="label"><bean:message key="Appointment.formEmailReminder" />:</div>
                    <div class="input"><input type="checkbox" name="emailPt" value="email reminder"></div>
             <%  }else { %>
                    <div class="label"></div>
                    <div class="input"></div>
	     <%  }%>

            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formCritical" />:</div>
            <div class="input">
            	<input type="checkbox" name="urgency" value="critical"/>
            </div>
        </li>
    </ul>
</div>
<%String demoNo = request.getParameter("demographic_no");%>
<table class="buttonBar deep">
    <tr>
        <% if(!(bDnb || bMultipleSameDayGroupAppt)) { %>
        <TD nowrap>
        <%    if (!props.getProperty("allowMultipleSameDayGroupAppt", "").equalsIgnoreCase("no")) {%>
      <INPUT TYPE="submit" id="groupButton"
            onclick="document.forms['ADDAPPT'].displaymode.value='Group Appt'"
            VALUE="<bean:message key="appointment.addappointment.btnGroupAppt"/>"
            <%=disabled%>>
        <% }

  if(dateString2.equals( inform.format(inform.parse(now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH))) )

    || dateString2.equals( inform.format(inform.parse(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH))) ) ) {

        org.apache.struts.util.MessageResources resources = org.apache.struts.util.MessageResources.getMessageResources("oscarResources");

        %> <INPUT TYPE="submit" id="addPrintPreviewButton"
            onclick="document.forms['ADDAPPT'].displaymode.value='Add Appt & PrintPreview'"
            VALUE="<bean:message key='appointment.addappointment.btnAddApptPrintPreview'/>"
            <%=disabled%>>


<%
  }

%>
        <INPUT TYPE="submit" id="addButton" class="rightButton blueButton top"
            onclick="document.forms['ADDAPPT'].displaymode.value='Add Appointment'"
            tabindex="6"
            VALUE="<% if (isMobileOptimized) { %><bean:message key="appointment.addappointment.btnAddAppointmentMobile" />
                   <% } else { %><bean:message key="appointment.addappointment.btnAddAppointment"/><% } %>"
            <%=disabled%>>
        <INPUT TYPE="submit" id="printButton"
            onclick="document.forms['ADDAPPT'].displaymode.value='Add Appt & PrintCard'"
            VALUE="<bean:message key='global.btnPrint'/>"
            <%=disabled%>>
                </TD>
		<TD></TD>
        <% } %>
    <TD align="right">
        <%
           if(bFirstDisp && apptObj!=null) {

               long numSameDayGroupApptsPaste = 0;

               if (props.getProperty("allowMultipleSameDayGroupAppt", "").equalsIgnoreCase("no")) {
                    String [] sqlParam = new String[3] ;
                    sqlParam[0] = myGroupNo; //schedule group
                    sqlParam[1] = apptObj.getDemographic_no();
                    sqlParam[2] = dateString2;
					appts = appointmentDao.search_group_day_appt(myGroupNo, Integer.parseInt(apptObj.getDemographic_no()), apptDate);
                    numSameDayGroupApptsPaste = appts.size() > 0 ? new Long(appts.size()) : 0;
                }
          %>
          <input type="button" id="pasteButton" value="Paste" onclick="pasteAppt(<%=(numSameDayGroupApptsPaste > 0)%>);">
        <% }%>
          <INPUT TYPE="RESET" id="backButton" class="leftButton top" VALUE="<bean:message key="appointment.addappointment.btnCancel"/>" onClick="cancelPageLock();window.close();">
       <% if (!props.getProperty("allowMultipleSameDayGroupAppt", "").equalsIgnoreCase("no")) {%>
          <input type="button" id="apptRepeatButton" value="<bean:message key="appointment.addappointment.btnRepeat"/>" onclick="onButRepeat()" <%=disabled%>>
      <%  } %>
   </TD>
	</tr>
</table>
</FORM>


<table align="center">
<tr>
    <td valign="top">
        <%if( bFromWL && demoNo != null && demoNo.length() > 0 ) {%>
        <table style="font-size: 9pt;" bgcolor="#c0c0c0" align="center" valign="top" cellpadding="3px">
            <tr bgcolor="#ccccff">
                <th colspan="2">
                    <bean:message key="appointment.addappointment.msgDemgraphics"/>
                    <a title="Master File" onclick="popup(700,1000,'../demographic/demographiccontrol.jsp?demographic_no=<%=demoNo%>&amp;displaymode=edit&amp;dboperation=search_detail','master')" href="javascript: function myFunction() {return false; }"><bean:message key="appointment.addappointment.btnEdit"/></a>

                    <bean:message key="appointment.addappointment.msgSex"/>: <%=sex%> &nbsp; <bean:message key="appointment.addappointment.msgDOB"/>: <%=dob%>
                </th>
            </tr>
             <tr bgcolor="#ccccff">
                <th style="padding-right: 20px" align="left"><bean:message key="appointment.addappointment.msgHin"/>:</th>
                <td><%=hin%> </td>
            </tr>
            <tr bgcolor="#ccccff">
                <th style="padding-right: 20px"align="left"><bean:message key="appointment.addappointment.msgAddress"/>:</th>
                <td><%=address%>, <%=city%>, <%=province%>, <%=postal%></td>
            </tr>
            <tr bgcolor="#ccccff">
                <th style="padding-right: 20px" align="left"><bean:message key="appointment.addappointment.msgPhone"/>:</th>
                <td><b><bean:message key="appointment.addappointment.msgH"/></b>:<%=phone%> <b><bean:message key="appointment.addappointment.msgW"/></b>:<%=phone2%> </td>
            </tr>
            <tr bgcolor="#ccccff" align="left">
                <th style="padding-right: 20px"><bean:message key="appointment.addappointment.msgEmail"/>:</th>
                <td><%=email%></td>
            </tr>

        </table>
        <%}%>
    </td>
    <td valign="top">
    <%
        String formTblProp = props.getProperty("appt_formTbl","");
        String[] formTblNames = formTblProp.split(";");

        int numForms = 0;
        for (String formTblName : formTblNames){
            if ((formTblName != null) && !formTblName.equals("")) {
                //form table name defined
                for(EncounterForm ef:encounterFormDao.findByFormTable(formTblName)) {
                    String formName = ef.getFormName();
                    pageContext.setAttribute("formName", formName);
                    boolean formComplete = false;
                    EctFormData.PatientForm[] ptForms = EctFormData.getPatientFormsFromLocalAndRemote(demoNo, formTblName);

                    if (ptForms.length > 0) {
                        formComplete = true;
                    }
                    numForms++;
                    if (numForms == 1) {
               
    %>
            <table style="font-size: 9pt;" bgcolor="#c0c0c0" align="center" valign="top" cellpadding="3px">
                <tr bgcolor="#ccccff">
                    <th colspan="2">
                        <bean:message key="appointment.addappointment.msgFormsSaved"/>
                    </th>
                </tr>
    <%              }%>

                <tr bgcolor="#c0c0c0" align="left">
                    <th style="padding-right: 20px"><c:out value="${formName}:"/></th>
    <%              if (formComplete){  %>
                        <td><bean:message key="appointment.addappointment.msgFormCompleted"/></td>
    <%              } else {            %>
                        <td><bean:message key="appointment.addappointment.msgFormNotCompleted"/></td>
    <%              } %>
                </tr>
    <%
                }
            }
        }

        if (numForms > 0) {
    %>
         </table>
    <%  }   %>
    </td>
    <td valign="top">
<table style="font-size: 8pt;" bgcolor="#c0c0c0" align="center" valign="top">
	<tr bgcolor="#ccccff">
		<th colspan="4"><bean:message key="appointment.addappointment.msgOverview" /></th>
	</tr>
	<tr bgcolor="#ccccff">
		<th style="padding-right: 25px"><bean:message key="Appointment.formDate" /></th>
 		<th style="padding-right: 25px"><bean:message key="Appointment.formStartTime" /></th>
		<th style="padding-right: 25px"><bean:message key="appointment.addappointment.msgProvider" /></th>
		<th><bean:message key="appointment.addappointment.msgComments" /></th>
	</tr>
	<%

        int iRow=0;
        if( bFromWL && demoNo != null && demoNo.length() > 0 ) {

            Object [] param2 = new Object[3];
            param2[0] = demoNo;
            Calendar cal2 = Calendar.getInstance();
            param2[1] = new java.sql.Date(cal2.getTime().getTime());
            java.util.Date start = cal2.getTime();
            cal2.add(Calendar.YEAR, 1);
            java.util.Date end = cal2.getTime();
            param2[2] = new java.sql.Date(cal2.getTime().getTime());
            
            for(Object[] result : appointmentDao.search_appt_future(Integer.parseInt(demoNo), start, end)) {
            	Appointment a = (Appointment)result[0];
            	p = (Provider)result[1];
           
                iRow ++;
                if (iRow > iPageSize) break;
    %>
	<tr bgcolor="#eeeeff">
		<td style="background-color: #CCFFCC; padding-right: 25px"><%=ConversionUtils.toDateString(a.getAppointmentDate())%></td>
		<td style="background-color: #CCFFCC; padding-right: 25px"><%=ConversionUtils.toTimeString(a.getStartTime())%></td>
		<td style="background-color: #CCFFCC; padding-right: 25px"><%=p.getFormattedName()%></td>
		<td style="background-color: #CCFFCC;"><%=a.getStatus()==null?"":(a.getStatus().equals("N")?"No Show":(a.getStatus().equals("C")?"Cancelled":"") )%></td>
	</tr>
	<%
            }

            iRow=0;
            cal2 = Calendar.getInstance();
            cal2.add(Calendar.YEAR, -1);
            
            for(Object[] result : appointmentDao.search_appt_past(Integer.parseInt(demoNo), start, cal2.getTime())) {
            	Appointment a = (Appointment)result[0];
            	p = (Provider)result[1];
                iRow ++;
                if (iRow > iPageSize) break;
    %>
	<tr bgcolor="#eeeeff">
		<td style="background-color: #CCFFCC; padding-right: 25px"><%=ConversionUtils.toDateString(a.getAppointmentDate())%></td>
		<td style="background-color: #CCFFCC; padding-right: 25px"><%=ConversionUtils.toTimeString(a.getStartTime())%></td>
		<td style="background-color: #CCFFCC; padding-right: 25px"><%=p.getFormattedName()%></td>
		<td style="background-color: #CCFFCC;"><%=a.getStatus()==null?"":(a.getStatus().equals("N")?"No Show":(a.getStatus().equals("C")?"Cancelled":"") )%></td>
	</tr>
	<%
            }
        }
    %>
</table>
</td>
</tr>
</table>

</body>
<script type="text/javascript">
var loc = document.forms['ADDAPPT'].location;
if(loc.nodeName.toUpperCase() == 'SELECT') loc.style.backgroundColor=loc.options[loc.selectedIndex].style.backgroundColor;
</script>
</html:html>

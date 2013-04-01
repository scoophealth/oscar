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
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao"%>
<%
  if (session.getAttribute("user") == null)    response.sendRedirect("../logout.jsp");

  String curProvider_no = request.getParameter("provider_no");
  String appointment_no = request.getParameter("appointment_no");
  String curUser_no = (String) session.getAttribute("user");
  String userfirstname = (String) session.getAttribute("userfirstname");
  String userlastname = (String) session.getAttribute("userlastname");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";
  String origDate = null;

  boolean bFirstDisp = true; //this is the first time to display the window
  if (request.getParameter("bFirstDisp")!=null) bFirstDisp = (request.getParameter("bFirstDisp")).equals("true");
%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<%@page import="oscar.oscarDemographic.data.*, java.util.*, java.sql.*, oscar.appt.*, oscar.*, java.text.*, java.net.*, org.oscarehr.common.OtherIdManager"%>
<%@ page import="oscar.appt.status.service.AppointmentStatusMgr"%>
<%@ page import="oscar.appt.status.model.AppointmentStatus"%>
<%@ page import="org.oscarehr.common.dao.DemographicDao, org.oscarehr.common.model.Demographic, org.oscarehr.util.SpringUtils"%>
<%@ page import="oscar.oscarEncounter.data.EctFormData"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<%@page import="org.oscarehr.common.model.DemographicCust" %>
<%@page import="org.oscarehr.common.dao.DemographicCustDao" %>
<%
	DemographicCustDao demographicCustDao = (DemographicCustDao)SpringUtils.getBean("demographicCustDao");
	org.oscarehr.PMmodule.dao.ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
%>
<%
  ApptData apptObj = ApptUtil.getAppointmentFromSession(request);

  oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
  String strEditable = pros.getProperty("ENABLE_EDIT_APPT_STATUS");

  AppointmentStatusMgr apptStatusMgr = (AppointmentStatusMgr)webApplicationContext.getBean("AppointmentStatusMgr");
  List allStatus = apptStatusMgr.getAllActiveStatus();

  Boolean isMobileOptimized = session.getAttribute("mobileOptimized") != null;

  DemographicDao demographicDao = (DemographicDao)SpringUtils.getBean("demographicDao");
%>
<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.Site"%><html:html locale="true">
<head>
<% if (isMobileOptimized) { %>
    <meta name="viewport" content="initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, width=device-width" />
    <link rel="stylesheet" href="../mobile/appointmentstyle.css" type="text/css">
<% } else { %>
    <link rel="stylesheet" href="appointmentstyle.css" type="text/css">
    <style type="text/css">
        .deep { background-color: <%= deepcolor %>; }
        .weak { background-color: <%= weakcolor %>; }
    </style>
    <!-- Must change styles for browsers that do not understand display:table properties -->
    <!--[if lt IE 8]>
        <style type="text/css">
            body { min-width: 760px; }
            .row { clear: both; }
            li.deep { background-color: <%= weakcolor %>; }
            .label, .space { float:left; width: 100px !important; }
            .panel li div { border:none; }
            .input, .space { text-align: right; float:left; }
            .panel { background-color: #EEEEFF; }
        </style>
    <![endif]-->
    <!-- Min-width doesn't work properly in IE6, so we simulate it using JavaScript.
    It's important to set a min-width since many elements will be floating, and
    resizing may otherwise cause elements to collapse in strange ways
    -->
    <!--[if lt IE 7]>
        <script language="JavaScript">
            window.onresize = function() { setMinWidth(860); }
        </script>
    <![endif]-->
<% } %>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="appointment.editappointment.title" /></title>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
   <script>
     jQuery.noConflict();
   </script>
<oscar:customInterface section="editappt"/>
<script language="javascript">
<!-- // start javascript
function toggleView() {
    showHideItem('editAppointment');
    showHideItem('viewAppointment');
}
function demographicdetail(vheight,vwidth) {
  if(document.forms['EDITAPPT'].demographic_no.value=="") return;
  self.close();
  var page = "../demographic/demographiccontrol.jsp?demographic_no=" + document.forms['EDITAPPT'].demographic_no.value+"&displaymode=edit&dboperation=search_detail";
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
  var popup=window.open(page, "demographic", windowprops);
}
function onButRepeat() {
	if(calculateEndTime()) {
		document.forms[0].action = "appointmenteditrepeatbooking.jsp" ;
		document.forms[0].submit();
	}
}

var saveTemp=0;

function setfocus() {
  this.focus();
  document.EDITAPPT.keyword.focus();
  document.EDITAPPT.keyword.select();
}

function onBlockFieldFocus(obj) {
  obj.blur();
  document.EDITAPPT.keyword.focus();
  document.EDITAPPT.keyword.select();
  window.alert("<bean:message key="Appointment.msgFillNameField"/>");
}
function labelprint(vheight,vwidth,varpage) {
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,screenX=600,screenY=200,top=0,left=0";
  var popup=window.open(page, "encounterhist", windowprops);
}
function onButDelete() {
  saveTemp=1;
}
function onButUpdate() {
  saveTemp=2;
}

function onButCancel(){
   var aptStat = document.EDITAPPT.status.value;
   if (aptStat.indexOf('B') == 0){
       var agree = confirm("<bean:message key="appointment.editappointment.msgCanceledBilledConfirmation"/>") ;
       if (agree){
          window.location='appointmentcontrol.jsp?buttoncancel=Cancel Appt&displaymode=Update Appt&appointment_no=<%=appointment_no%>';
       }
   }else{
      window.location='appointmentcontrol.jsp?buttoncancel=Cancel Appt&displaymode=Update Appt&appointment_no=<%=appointment_no%>';
   }
}
function upCaseCtrl(ctrl) {
	ctrl.value = ctrl.value.toUpperCase();
}
function onSub() {
  if( saveTemp==1 ) {
    var aptStat = document.EDITAPPT.status.value;
    if (aptStat.indexOf('B') == 0){
       return (confirm("<bean:message key="appointment.editappointment.msgDeleteBilledConfirmation"/>")) ;
    }else{
       return (confirm("<bean:message key="appointment.editappointment.msgDeleteConfirmation"/>")) ;
    }
  }
  if( saveTemp==2 ) {
    return calculateEndTime() ;
  } else
      return true;
}



function calculateEndTime() {
  var stime = document.EDITAPPT.start_time.value;
  var vlen = stime.indexOf(':')==-1?1:2;
  if(vlen==1 && stime.length==4 ) {
    document.EDITAPPT.start_time.value = stime.substring(0,2) +":"+ stime.substring(2);
    stime = document.EDITAPPT.start_time.value;
  }
  if(stime.length!=5) {
    alert("<bean:message key="Appointment.msgInvalidDateFormat"/>");
    return false;
  }

  var shour = stime.substring(0,2) ;
  var smin = stime.substring(stime.length-vlen) ;
  var duration = document.EDITAPPT.duration.value ;
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
  document.EDITAPPT.end_time.value = shour +":"+ smin;
  if(shour > 23) {
    alert("<bean:message key="Appointment.msgCheckDuration"/>");
    return false;
  }
  return true;
}

function checkTimeTypeIn(obj) {
  if(!checkTypeNum(obj.value) ) {
	  alert ("<bean:message key="Appointment.msgFillTimeField"/>");
	} else {
	  if(obj.value.indexOf(':')==-1) {
	    if(obj.value.length < 3) alert("<bean:message key="Appointment.msgFillValidTimeField"/>");
	    obj.value = obj.value.substring(0, obj.value.length-2 )+":"+obj.value.substring( obj.value.length-2 );
	  }
	}
}
<% if (apptObj!=null) { %>
function pasteAppt(multipleSameDayGroupAppt) {

        var warnMsgId = document.getElementById("tooManySameDayGroupApptWarning");

        if (multipleSameDayGroupAppt) {
           warnMsgId.style.display = "block";
           if (document.EDITAPPT.updateButton) {
              document.EDITAPPT.updateButton.style.display = "none";
           }
           if (document.EDITAPPT.groupButton) {
              document.EDITAPPT.groupButton.style.display = "none";
           }
           if (document.EDITAPPT.deleteButton){
              document.EDITAPPT.deleteButton.style.display = "none";
           }
           if (document.EDITAPPT.cancelButton){
              document.EDITAPPT.cancelButton.style.display = "none";
           }
           if (document.EDITAPPT.noShowButton){
              document.EDITAPPT.noShowButton.style.display = "none";
           }
           if (document.EDITAPPT.labelButton) {
                document.EDITAPPT.labelButton.style.display = "none";
           }
           if (document.EDITAPPT.repeatButton) {
                document.EDITAPPT.repeatButton.style.display = "none";
           }
        }
        //else {
        //   warnMsgId.style.display = "none";
        //}
	document.EDITAPPT.status.value = "<%=apptObj.getStatus()%>";
	document.EDITAPPT.duration.value = "<%=apptObj.getDuration()%>";
	document.EDITAPPT.chart_no.value = "<%=apptObj.getChart_no()%>";
	document.EDITAPPT.keyword.value = "<%=apptObj.getName()%>";
	document.EDITAPPT.demographic_no.value = "<%=apptObj.getDemographic_no()%>";
	document.EDITAPPT.reason.value = "<%=apptObj.getReason()%>";
	document.EDITAPPT.notes.value = "<%=apptObj.getNotes()%>";
	document.EDITAPPT.location.value = "<%=apptObj.getLocation()%>";
	document.EDITAPPT.resources.value = "<%=apptObj.getResources()%>";
	document.EDITAPPT.type.value = "<%=apptObj.getType()%>";
	if('<%=apptObj.getUrgency()%>' == 'critical') {
		document.EDITAPPT.urgency.checked = "checked";
	}
}
<% } %>
function onCut() {
  document.EDITAPPT.submit();
}


function openTypePopup () {
    windowprops = "height=170,width=500,location=no,scrollbars=no,menubars=no,toolbars=no,resizable=yes,screenX=0,screenY=0,top=100,left=100";
    var popup=window.open("appointmentType.jsp?type="+document.forms['EDITAPPT'].type.value, "Appointment Type", windowprops);
    if (popup != null) {
      if (popup.opener == null) {
        popup.opener = self;
      }
      popup.focus();
    }
}

function setType(typeSel,reasonSel,locSel,durSel,notesSel,resSel) {
  document.forms['EDITAPPT'].type.value = typeSel;
  document.forms['EDITAPPT'].reason.value = reasonSel;
  document.forms['EDITAPPT'].duration.value = durSel;
  document.forms['EDITAPPT'].notes.value = notesSel;
  document.forms['EDITAPPT'].duration.value = durSel;
  document.forms['EDITAPPT'].resources.value = resSel;
  var loc = document.forms['EDITAPPT'].location;
  if(loc.nodeName == 'SELECT') {
          for(c = 0;c < loc.length;c++) {
                  if(loc.options[c].innerHTML == locSel) {
                          loc.selectedIndex = c;
                          loc.style.backgroundColor=loc.options[loc.selectedIndex].style.backgroundColor;
                          break;
                  }
          }
  }
}

// stop javascript -->
</script>
</head>
<body onload="setfocus()" bgproperties="fixed"
      topmargin="0" leftmargin="0" rightmargin="0" bottommargin="0">
<!-- The mobile optimized page is split into two sections: viewing and editing an appointment
     In the mobile version, we only display the edit section first if we are returning from a search -->
<div id="editAppointment" style="display:<%= (isMobileOptimized && bFirstDisp) ? "none":"block"%>;">
<FORM NAME="EDITAPPT" METHOD="post" ACTION="appointmentcontrol.jsp"
	onSubmit="return(onSub())"><INPUT TYPE="hidden"
	NAME="displaymode" value="">
    <div class="header deep">
        <div class="title">
            <!-- We display a shortened title for the mobile version -->
            <% if (isMobileOptimized) { %><bean:message key="appointment.editappointment.msgMainLabelMobile" />
            <% } else { %><bean:message key="appointment.editappointment.msgMainLabel" />
            <% } %>
        </div>
        <a href="javascript:toggleView();" id="viewButton" class="leftButton top">
            <bean:message key="appointment.editappointment.btnView" />
        </a>
    </div>

<%
	Map appt = null;
	String demono="", chartno="", phone="", rosterstatus="", alert="", doctorNo="";
	String strApptDate = bFirstDisp?"":request.getParameter("appointment_date") ;


	if (bFirstDisp) {
		List<Map<String,Object>> resultList = oscarSuperManager.find("appointmentDao",
				request.getParameter("dboperation"), new Object [] {appointment_no});
		if (resultList.size() == 0) {
%>
<bean:message key="appointment.editappointment.msgNoSuchAppointment" />
<%
			return;
		} else {
			appt = resultList.get(0);
		}
	}


	if (bFirstDisp && appt.get("demographic_no")!=null) {
		demono = String.valueOf(appt.get("demographic_no"));
	} else if (request.getParameter("demographic_no")!=null && !request.getParameter("demographic_no").equals("")) {
		demono = request.getParameter("demographic_no");
	}

	//get chart_no from demographic table if it exists
	if (!demono.equals("0") && !demono.equals("")) {
   		List<Map<String,Object>> resultList = oscarSuperManager.find("appointmentDao", "search_detail", new Object [] {demono});
		if (resultList.size() > 0) {
			Map detail = resultList.get(0);
			chartno = (String) detail.get("chart_no");
			phone = (String) detail.get("phone");
			rosterstatus = (String) detail.get("roster_status");
		}
		DemographicCust demographicCust = demographicCustDao.find(Integer.parseInt(demono));
		if(demographicCust != null) {
			alert = demographicCust.getAlert();
		}

	}

        OscarProperties props = OscarProperties.getInstance();
        String displayStyle="display:none";
        String myGroupNo = (String) session.getAttribute("groupno");
        boolean bMultipleSameDayGroupAppt = false;
        if (props.getProperty("allowMultipleSameDayGroupAppt", "").equalsIgnoreCase("no")) {

            if (!bFirstDisp && !demono.equals("0") && !demono.equals("")) {
                String [] sqlParam = new String[3] ;
                sqlParam[0] = myGroupNo; //schedule group
                sqlParam[1] = demono;
                sqlParam[2] = strApptDate;

                List<Map<String,Object>> resultList = oscarSuperManager.find("appointmentDao", "search_group_day_appt", sqlParam);
                long numSameDayGroupAppts = resultList.size() > 0 ? (Long)resultList.get(0).get("numAppts") : 0;
                bMultipleSameDayGroupAppt = (numSameDayGroupAppts > 0);
            }

            if (bMultipleSameDayGroupAppt){
                displayStyle="display:block";
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
        }

    //RJ 07/12/2006
    //If page is loaded first time hit db for patient's family doctor
    //Else if we are coming back from search this has been done for us
    //Else how did we get here?
    if( bFirstDisp ) {
        DemographicData dd = new DemographicData();
        org.oscarehr.common.model.Demographic demo = dd.getDemographic(String.valueOf(appt.get("demographic_no")));
        doctorNo = demo!=null ? (demo.getProviderNo()) : "";
    } else if (!request.getParameter("doctor_no").equals("")) {
        doctorNo = request.getParameter("doctor_no");
    }
%>
<div class="panel">
    <ul>
        <li class="row weak">
            <div class="label">
                <bean:message key="Appointment.formDate" />:
            </div>
            <div class="input">
		<INPUT TYPE="TEXT"
					NAME="appointment_date"
					VALUE="<%=bFirstDisp?appt.get("appointment_date"):strApptDate%>"
                    WIDTH="25" HEIGHT="20" border="0">
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formStatus" />:</div>
            <div class="input">
<%
              String statusCode = request.getParameter("status");
			  String importedStatus = null;
              if (bFirstDisp){
                  statusCode = (String) appt.get("status");
                  importedStatus = (String) appt.get("imported_status");
              }


              String signOrVerify = "";
              if (statusCode.length() >= 2){
                  signOrVerify = statusCode.substring(1,2);
                  statusCode = statusCode.substring(0,1);
              }
              if (strEditable!=null && strEditable.equalsIgnoreCase("yes")) { %>
                <select name="status" STYLE="width: 154px">
					<% for (int i = 0; i < allStatus.size(); i++) { %>
					<option
						value="<%=((AppointmentStatus)allStatus.get(i)).getStatus()+signOrVerify%>"
						<%=((AppointmentStatus)allStatus.get(i)).getStatus().equals(statusCode)?"SELECTED":""%>><%=((AppointmentStatus)allStatus.get(i)).getDescription()%></option>
					<% } %>
				</select> <%
              } else {
              	if (importedStatus==null || importedStatus.trim().equals("")) { %>
              	<INPUT TYPE="TEXT" NAME="status" VALUE="<%=statusCode%>" WIDTH="25"> <%
              	} else { %>
                <INPUT TYPE="TEXT" NAME="status" VALUE="<%=statusCode%>" WIDTH="25">
                <INPUT TYPE="TEXT" TITLE="Imported Status" VALUE="<%=importedStatus%>" WIDTH="25" readonly> <%
              	}
              }
%>
            </div>
        </li>
        <li class="row weak">
            <div class="label"><bean:message key="Appointment.formStartTime" />:</div>
            <div class="input">
                <INPUT TYPE="TEXT"
					NAME="start_time"
					VALUE="<%=bFirstDisp?String.valueOf(appt.get("start_time")).substring(0,5):request.getParameter("start_time")%>"
                    WIDTH="25">
            </div>
            <div class="space">&nbsp;</div>

	    <div class="label">
            <%
                        // multisites start ==================
                boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();

            SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
            List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user"));
            // multisites end ==================

            boolean bMoreAddr = bMultisites? true : props.getProperty("scheduleSiteID", "").equals("") ? false : true;

            String loc = bFirstDisp?((String)appt.get("location")):request.getParameter("location");
            String colo = bMultisites
                                        ? ApptUtil.getColorFromLocation(sites, loc)
                                        : bMoreAddr? ApptUtil.getColorFromLocation(props.getProperty("scheduleSiteID", ""), props.getProperty("scheduleSiteColor", ""),loc) : "white";
            %>
                        <% if (bMultisites) { %>
                                <INPUT TYPE="button" NAME="typeButton" VALUE="<bean:message key="Appointment.formType"/>" onClick="openTypePopup()">
                        <% } else { %>
                                <bean:message key="Appointment.formType"/>
                        <% } %>

            </div>

            <div class="input">
                <INPUT TYPE="TEXT" NAME="type"
					VALUE="<%=bFirstDisp?appt.get("type"):request.getParameter("type")%>"
                    WIDTH="25">
            </div>
        </li>
        <li class="row weak">
            <div class="label"><bean:message key="Appointment.formDuration" />:</div>
            <div class="input">
				<%
  int everyMin = 1;
  StringBuilder nameSb = new StringBuilder();
  if(bFirstDisp) {
    int endtime = (Integer.parseInt(String.valueOf(appt.get("end_time")).substring(0,2) ) )*60 + (Integer.parseInt(String.valueOf(appt.get("end_time")).substring(3,5) ) ) ;
    int starttime = (Integer.parseInt(String.valueOf(appt.get("start_time")).substring(0,2) ) )*60 + (Integer.parseInt(String.valueOf(appt.get("start_time")).substring(3,5) ) ) ;
    everyMin = endtime - starttime +1;

    if (!demono.equals("0") && !demono.equals("") && (demographicDao != null)) {
        Demographic demo = demographicDao.getDemographic(demono);
        nameSb.append(demo.getLastName())
              .append(",")
              .append(demo.getFirstName());
    }
    else {
        nameSb.append(appt.get("name"));
    }
  }
%> <INPUT TYPE="hidden" NAME="end_time"
					VALUE="<%=bFirstDisp?String.valueOf(appt.get("end_time")).substring(0,5):request.getParameter("end_time")%>"
					WIDTH="25" HEIGHT="20" border="0" onChange="checkTimeTypeIn(this)">
				<%--              <INPUT TYPE="hidden" NAME="end_time" VALUE="<%=request.getParameter("end_time")%>" WIDTH="25" HEIGHT="20" border="0" onChange="checkTimeTypeIn(this)">--%>
				<INPUT TYPE="TEXT" NAME="duration"
					VALUE="<%=request.getParameter("duration")!=null?(request.getParameter("duration").equals(" ")||request.getParameter("duration").equals("")||request.getParameter("duration").equals("null")?(""+everyMin) :request.getParameter("duration")):(""+everyMin)%>"
                    WIDTH="25">
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formChartNo" />:</div>
            <div class="input">
                <input type="TEXT" name="chart_no"
                    readonly value="<%=bFirstDisp?chartno:request.getParameter("chart_no")%>"
                    width="25">
            </div>
        </li>
			<% if(providerBean != null && doctorNo != null && providerBean.getProperty(doctorNo)!=null) {%>
        <li class="row weak">
            <div class="label"></div>
            <div class="input"></div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formDoctor" />:</div>
            <div class="input">
                <INPUT type="TEXT" name="doctorNo" readonly
                   value="<%=providerBean.getProperty(doctorNo)%>"
                   width="25">
            </div>
        </li>
			<% } %>
        <li class="row weak">
            <div class="label"><a href="#"onclick="demographicdetail(550,700)">
                    <bean:message key="Appointment.formName" /></a>:
            </div>
            <div class="input">
                <INPUT TYPE="TEXT" NAME="keyword"
					tabindex="1"
					VALUE="<%=bFirstDisp?nameSb.toString():request.getParameter("name")%>"
                    width="25">
            </div>
            <div class="space">&nbsp;</div>
            <div class="label">
		<INPUT TYPE="hidden" NAME="orderby" VALUE="last_name, first_name">
<%
    String searchMode = request.getParameter("search_mode");
    if (searchMode == null || searchMode.isEmpty()) {
        searchMode = OscarProperties.getInstance().getProperty("default_search_mode","search_name");
    }
%>
                <INPUT TYPE="hidden" NAME="search_mode" VALUE="<%=searchMode%>">
                <INPUT TYPE="hidden" NAME="originalpage" VALUE="../appointment/editappointment.jsp">
                <INPUT TYPE="hidden" NAME="limit1" VALUE="0">
                <INPUT TYPE="hidden" NAME="limit2" VALUE="5">
                <INPUT TYPE="hidden" NAME="ptstatus" VALUE="active">
                <!--input type="hidden" name="displaymode" value="Search " -->
                <input type="submit" style="width:auto;"
					onclick="document.forms['EDITAPPT'].displaymode.value='Search '"
                    value="<bean:message key="appointment.editappointment.btnSearch"/>">
            </div>
            <div class="input">
                <input type="TEXT"
					name="demographic_no" onFocus="onBlockFieldFocus(this)" readonly
					value="<%=bFirstDisp?( ((Integer)appt.get("demographic_no"))==0?"":(""+appt.get("demographic_no")) ):request.getParameter("demographic_no")%>"
                    width="25">
            </div>
        </li>
        <li class="row weak">
            <div class="label"><bean:message key="Appointment.formReason" />:</div>
            <div class="input">
				<textarea name="reason" tabindex="2" rows="2" wrap="virtual"
					cols="18"><%=bFirstDisp?appt.get("reason"):request.getParameter("reason")%></textarea>
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formNotes" />:</div>
            <div class="input">
				<textarea name="notes" tabindex="3" rows="2" wrap="virtual"
					cols="18"><%=bFirstDisp?appt.get("notes"):request.getParameter("notes")%></textarea>
            </div>
        </li>
			<% if (pros.isPropertyActive("mc_number")) {
		String mcNumber = OtherIdManager.getApptOtherId(appointment_no, "appt_mc_number");
%>
        <li class="row weak">
            <div class="label">M/C number :</div>
            <div class="input">
                <input type="text" name="appt_mc_number" tabindex="4"
                    value="<%=bFirstDisp?mcNumber:request.getParameter("appt_mc_number")%>" />
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"></div>
            <div class="input"></div>
        </li>
<% } %>
        <li class="row weak">
            <div class="label"><bean:message key="Appointment.formLocation" />:</div>
            <div class="input">

<% // multisites start ==================
boolean isSiteSelected = false;
if (bMultisites) { %>
				<select tabindex="4" name="location" style="background-color: <%=colo%>" onchange='this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor'>
				<%
					StringBuilder sb = new StringBuilder();
					for (Site s:sites) {
						if (s.getName().equals(loc)) isSiteSelected = true; // added by vic
						sb.append("<option value=\"").append(s.getName()).append("\" style=\"background-color: ").append(s.getBgColor()).append("\" ").append(s.getName().equals(loc)?"selected":"").append(">").append(s.getName()).append("</option>");
					}
					if (isSiteSelected) {
						out.println(sb.toString());
					} else {
						out.println("<option value='"+loc+"'>"+loc+"</option>");
					}
				%>

				</select>
<% } else {
	isSiteSelected = true;
	// multisites end ==================
%>
            <INPUT TYPE="TEXT" NAME="location" tabindex="4"
					VALUE="<%=bFirstDisp?appt.get("location"):request.getParameter("location")%>"
					WIDTH="25">
<% } %>
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formResources" />:</div>
            <div class="input">
                <input type="TEXT"
					name="resources" tabindex="5"
					value="<%=bFirstDisp?appt.get("resources"):request.getParameter("resources")%>"
                    width="25">
            </div>
        </li>
        <li class="weak row">
            <div class="label"><bean:message key="Appointment.formLastCreator" />:</div>
            <div class="input">
		<% String lastCreatorNo = appt.get("lastUpdateUser")==null?(String)appt.get("creator"):providerDao.getProvider((String)appt.get("lastUpdateUser")).getFormattedName(); %>
                <INPUT TYPE="TEXT" NAME="user_id" VALUE="<%=lastCreatorNo%>" readonly WIDTH="25">
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formLastTime" />:</div>
            <div class="input">
				<%
                 origDate =  bFirstDisp ? String.valueOf(appt.get("createdatetime")) : request.getParameter("createDate");
                 String lastDateTime = bFirstDisp?String.valueOf(appt.get("updatedatetime")):request.getParameter("updatedatetime");
                 if (lastDateTime == null){ lastDateTime = bFirstDisp?String.valueOf(appt.get("createdatetime")):request.getParameter("createdatetime"); }

				GregorianCalendar now=new GregorianCalendar();
				String strDateTime=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "
					+	now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);

                 String remarks = "";
                 if (bFirstDisp && appt.get("remarks")!=null) {
                     remarks = (String) appt.get("remarks");
                 }

%>
                <INPUT TYPE="TEXT" NAME="lastcreatedatetime" readonly
                    VALUE="<%=bFirstDisp?lastDateTime:request.getParameter("lastcreatedatetime")%>"
                    WIDTH="25">
                <INPUT TYPE="hidden" NAME="createdatetime" VALUE="<%=strDateTime%>">
				<INPUT TYPE="hidden" NAME="provider_no" VALUE="<%=curProvider_no%>">
				<INPUT TYPE="hidden" NAME="dboperation" VALUE="update_apptrecord">
                <INPUT TYPE="hidden" NAME="creator" VALUE="<%=userlastname+", "+userfirstname%>">
                <INPUT TYPE="hidden" NAME="remarks" VALUE="<%=remarks%>">
                <INPUT TYPE="hidden" NAME="appointment_no" VALUE="<%=appointment_no%>">
            </div>
        </li>
        <li class="row weak">
            <div class="label">Create Date:</div>
            <div class="input">
                <INPUT TYPE="TEXT" NAME="createDate" readonly
					VALUE="<%=origDate%>" WIDTH="25">
            </div>
            <div class="space">&nbsp;</div>
            <div class="label"><bean:message key="Appointment.formCritical" />:</div>
            <div class="input">
            	<%
           			String urgencyChecked=new String();
            		if(bFirstDisp) {
            			if(appt.get("urgency") != null && appt.get("urgency").equals("critical")) {
            				urgencyChecked=" checked=\"checked\" ";
            			}
            		} else {
            			if(request.getParameter("urgency") != null) {
            				if(request.getParameter("urgency").equals("critical")) {
            					urgencyChecked=" checked=\"checked\" ";
            				}
            			}
            		}
            	%>
            	<input type="checkbox" name="urgency" value="critical" <%=urgencyChecked%>/>
            </div>
        </li>
        <li class="row weak">
			<div class="label"></div>
            <div class="input"></div>
            <div class="space">&nbsp;</div>
			<div class="label"></div>
            <div class="input"></div>
        </li>
    </ul>

<% if (isSiteSelected) { %>
<table class="buttonBar deep">
	<tr>
            <% if (!bMultipleSameDayGroupAppt) { %>
        <td align="left"><input type="submit" class="rightButton blueButton top" id="updateButton"
			onclick="document.forms['EDITAPPT'].displaymode.value='Update Appt'; onButUpdate();"
			value="<bean:message key="appointment.editappointment.btnUpdateAppointment"/>">
             <% if (!props.getProperty("allowMultipleSameDayGroupAppt", "").equalsIgnoreCase("no")) {%>
		<input type="submit" id="groupButton"
			onclick="document.forms['EDITAPPT'].displaymode.value='Group Action'; onButUpdate();"
			value="<bean:message key="appointment.editappointment.btnGroupAction"/>">
             <% }%>
		<input type="submit" class="redButton button" id="deleteButton"
			onclick="document.forms['EDITAPPT'].displaymode.value='Delete Appt'; onButDelete();"
			value="<bean:message key="appointment.editappointment.btnDeleteAppointment"/>">
		<input type="button" name="buttoncancel" id="cancelButton"
			value="<bean:message key="appointment.editappointment.btnCancelAppointment"/>"
			onClick="onButCancel();"> <input type="button"
			name="buttoncancel" id="noShowButton"
			value="<bean:message key="appointment.editappointment.btnNoShow"/>"
			onClick="window.location='appointmentcontrol.jsp?buttoncancel=No Show&displaymode=Update Appt&appointment_no=<%=appointment_no%>'">
		</td>
		<td align="right" nowrap><input type="button" name="labelprint" id="labelButton"
			value="<bean:message key="appointment.editappointment.btnLabelPrint"/>"
			onClick="window.open('../demographic/demographiclabelprintsetting.jsp?demographic_no='+document.EDITAPPT.demographic_no.value, 'labelprint','height=550,width=700,location=no,scrollbars=yes,menubars=no,toolbars=no' )">
		<!--input type="button" name="Button" value="<bean:message key="global.btnExit"/>" onClick="self.close()"-->
		 <% if (!props.getProperty("allowMultipleSameDayGroupAppt", "").equalsIgnoreCase("no")) {%>
                    <input type="button" id="repeatButton"
			value="<bean:message key="appointment.addappointment.btnRepeat"/>"
			onclick="onButRepeat()"></td>
                 <% }
                }%>
	</tr>
</table>
<% } %>

</div>
<div id="bottomInfo">
<table width="95%" align="center">
	<tr>
		<td><bean:message key="Appointment.msgTelephone" />: <%= phone%><br>
		<bean:message key="Appointment.msgRosterStatus" />: <%=rosterstatus%>
		</td>
		<% if (alert!=null && !alert.equals("")) { %>
		<td bgcolor='yellow'><font color='red'><b><%=alert%></b></font></td>
		<% } %>
	</tr>
</table>
<hr />

<% if (isSiteSelected) { %>
<table width="95%" align="center">
	<tr>
		<td><input type="submit"
			onclick="document.forms['EDITAPPT'].displaymode.value='Cut';"
			value="Cut" /> | <input type="submit"
			onclick="document.forms['EDITAPPT'].displaymode.value='Copy';"
			value="Copy" />
                     <%
                     if(bFirstDisp && apptObj!=null) {

                            long numSameDayGroupApptsPaste = 0;

                            if (props.getProperty("allowMultipleSameDayGroupAppt", "").equalsIgnoreCase("no")) {
                                String [] sqlParam = new String[3] ;
                                sqlParam[0] = myGroupNo; //schedule group
                                sqlParam[1] = apptObj.getDemographic_no();
                                sqlParam[2] = (String) appt.get("appointment_date").toString();

                                List<Map<String,Object>> resultList = oscarSuperManager.find("appointmentDao", "search_group_day_appt", sqlParam);
                                numSameDayGroupApptsPaste = resultList.size() > 0 ? (Long)resultList.get(0).get("numAppts") : 0;
                            }
                  %><a href=#
			onclick="pasteAppt(<%=(numSameDayGroupApptsPaste > 0)%>);">Paste</a>
		<% } %>
		</td>
	</tr>
</table>
<% } %>


</div>
</FORM>
</div> <!-- end of edit appointment screen -->

<% 
    String formTblProp = props.getProperty("appt_formTbl");
    String[] formTblNames = formTblProp.split(";");
               
    int numForms = 0;
    for (String formTblName : formTblNames){
        if ((formTblName != null) && !formTblName.equals("")) {
            //form table name defined
            List<Map<String,Object>> resultList = oscarSuperManager.find("appointmentDao", "search_formtbl", new Object [] {formTblName});
            if (resultList.size() > 0) {
                //form table exists                            
                Map mFormName = resultList.get(0);
                String formName = (String) mFormName.get("form_name");
                pageContext.setAttribute("formName", formName);
                boolean formComplete = false;
                EctFormData.PatientForm[] ptForms = EctFormData.getPatientFormsFromLocalAndRemote(demono, formTblName);

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
<%              } %>
             
            <tr bgcolor="#c0c0c0" align="left">
                <th style="padding-right: 20px"><c:out value="${formName}:"/></th>
<%                 if (formComplete){  %>
                <td><bean:message key="appointment.addappointment.msgFormCompleted"/></td>
<%                 } else {            %>
                <td><bean:message key="appointment.addappointment.msgFormNotCompleted"/></td>
<%                 } %>               
            </tr>
<%                         
            }
        }
    }
               
    if (numForms > 0) {        %>
         </table>
<%  }   %>
         
         
<!-- View Appointment: Screen that summarizes appointment data.
Currently this is only used in the mobile version -->
<div id="viewAppointment" style="display:<%=(bFirstDisp && isMobileOptimized) ? "block":"none"%>;">
    <%
        // Format date to be more readable
        java.text.SimpleDateFormat inform = new java.text.SimpleDateFormat ("yyyy-MM-dd");
        String strDate = bFirstDisp ? appt.get("appointment_date").toString() : request.getParameter("appointment_date");
        java.util.Date d = inform.parse(strDate);
        String formatDate = "";
        try { // attempt to change string format
        java.util.ResourceBundle prop = ResourceBundle.getBundle("oscarResources", request.getLocale());
        formatDate = oscar.util.UtilDateUtilities.DateToString(d, prop.getString("date.EEEyyyyMMdd"));
        } catch (Exception e) {
            org.oscarehr.util.MiscUtils.getLogger().error("Error", e);
            formatDate = oscar.util.UtilDateUtilities.DateToString(inform.parse(strDate), "EEE, yyyy-MM-dd");
        }
    %>
    <div class="header">
        <div class="title" id="appointmentTitle">
            <bean:message key="appointment.editappointment.btnView" />
        </div>
        <a href=# onclick="window.close();" id="backButton" class="leftButton top"><%= strDate%></a>
        <a href="javascript:toggleView();" id="editButton" class="rightButton top">Edit</a>
    </div>
    <div id="info" class="panel">
        <ul>
            <li class="mainInfo"><a href="#" onclick="demographicdetail(550,700)">
                <%
                    String apptName = (bFirstDisp ? appt.get("name") : request.getParameter("name")).toString();
                    //If a comma exists, need to split name into first and last to prevent overflow
                    int comma = apptName.indexOf(",");
                    if (comma != -1)
                        apptName = apptName.substring(0, comma) + ", " + apptName.substring(comma+1);
                %>
                <%=apptName%>
            </a></li>
            <li><div class="label"><bean:message key="Appointment.formDate" />: </div>
                <div class="info"><%=formatDate%></div>
            </li>
            <% // Determine appointment status from code so we can access
   // the description, colour, image, etc.
      AppointmentStatus apptStatus = (AppointmentStatus)allStatus.get(0);
      for (int i = 0; i < allStatus.size(); i++) {
            AppointmentStatus st = (AppointmentStatus) allStatus.get(i);
            if (st.getStatus().equals(statusCode)) {
                apptStatus = st;
                break;
            }
      }
%>
            <li><div class="label"><bean:message key="Appointment.formStatus" />: </div>
                <div class="info">
                <font style="background-color:<%=apptStatus.getColor()%>; font-weight:bold;">
                    <img src="../images/<%=apptStatus.getIcon()%>" />
                    <%=apptStatus.getDescription()%>
                </font>
                </div>
            </li>
            <li><div class="label"><bean:message key="appointment.editappointment.msgTime" />: </div>
                <div class="info">From <%=bFirstDisp ? String.valueOf(appt.get("start_time")).substring(0, 5) : request.getParameter("start_time")%>
                to <%=bFirstDisp ? String.valueOf(appt.get("end_time")).substring(0, 5) : request.getParameter("end_time")%></div>
            </li>
            <li><div class="label"><bean:message key="Appointment.formType" />: </div>
                <div class="info"><%=bFirstDisp ? appt.get("type") : request.getParameter("type")%></div>
            </li>
            <li><div class="label"><bean:message key="Appointment.formReason" />: </div>
                <div class="info"><%=bFirstDisp ? appt.get("reason") : request.getParameter("reason")%></div>
            </li>
            <li><div class="label"><bean:message key="Appointment.formLocation" />: </div>
                <div class="info"><%=bFirstDisp ? appt.get("location") : request.getParameter("location")%></div>
            </li>
            <li><div class="label"><bean:message key="Appointment.formResources" />: </div>
                <div class="info"><%=bFirstDisp ? appt.get("resources") : request.getParameter("resources")%></div>
            </li>
            <li>&nbsp;</li>
            <li class="notes">
                <div class="label"><bean:message key="Appointment.formNotes" />: </div>
                <div class="info"><%=bFirstDisp ? appt.get("notes") : request.getParameter("notes")%></div>
            </li>
        </ul>
    </div>
</div> <!-- end of screen to view appointment -->
</body>
<script type="text/javascript">
var loc = document.forms['EDITAPPT'].location;
if(loc.nodeName.toUpperCase() == 'SELECT') loc.style.backgroundColor=loc.options[loc.selectedIndex].style.backgroundColor;
</script>

</html:html>

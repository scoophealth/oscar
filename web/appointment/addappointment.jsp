<%@page contentType="text/html" pageEncoding="ISO-8859-1"%> <%
  if(session.getAttribute("user") == null)    response.sendRedirect("../logout.jsp");

  String DONOTBOOK = "Do_Not_Book";
  String curProvider_no = request.getParameter("provider_no");
  String curDoctor_no = request.getParameter("doctor_no") != null ? request.getParameter("doctor_no") : "";
  String curUser_no = (String) session.getAttribute("user");
  String userfirstname = (String) session.getAttribute("userfirstname");
  String userlastname = (String) session.getAttribute("userlastname");

  //String curDemoNo = request.getParameter("demographic_no")!=null?request.getParameter("demographic_no"):"";
  //String curDemoName = request.getParameter("demographic_name")!=null?request.getParameter("demographic_name"):"";  

  int everyMin=Integer.parseInt(((String) session.getAttribute("everymin")).trim());

  boolean bFirstDisp=true; //this is the first time to display the window
  boolean bFromWL=false; //this is from waiting list page

  if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
  if (request.getParameter("demographic_no")!=null) bFromWL=true;

  String duration = request.getParameter("duration")!=null?(request.getParameter("duration").equals(" ")||request.getParameter("duration").equals("")||request.getParameter("duration").equals("null")?(""+everyMin) :request.getParameter("duration")):(""+everyMin) ;
%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*, oscar.appt.*"
	errorPage="../appointment/errorpage.jsp"%>
<%@ page import="oscar.appt.status.service.AppointmentStatusMgr"
	errorPage="../appointment/errorpage.jsp"%>
<%@ page import="oscar.appt.status.model.AppointmentStatus"
	errorPage="../appointment/errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%--RJ 07/07/2006 --%>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />

<%      
  int iPageSize=5;

  ApptData apptObj = ApptUtil.getAppointmentFromSession(request);

  oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
  String strEditable = pros.getProperty("ENABLE_EDIT_APPT_STATUS");

  AppointmentStatusMgr apptStatusMgr = (AppointmentStatusMgr)webApplicationContext.getBean("AppointmentStatusMgr");
  List allStatus = apptStatusMgr.getAllActiveStatus();
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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */

-->

<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.oscarehr.common.model.Site"%><html:html locale="true">
<head>
<meta http-equiv="Cache-Control" content="no-cache">
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script type="text/javascript" src="<%= request.getContextPath() %>/share/javascript/Oscar.js"></script>
<title><bean:message key="appointment.addappointment.title" /></title>
<script type="text/javascript">

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
  if(!checkTypeNum(obj.value) ) {
	  alert ("<bean:message key="Appointment.msgFillTimeField"/>");
	} else {
	  if(obj.value.indexOf(':')==-1) {
	    if(obj.value.length < 3) alert("<bean:message key="Appointment.msgFillValidTimeField"/>");
	    obj.value = obj.value.substring(0, obj.value.length-2 )+":"+obj.value.substring( obj.value.length-2 );
	  } 
	}
}

function calculateEndTime() {
  var stime = document.ADDAPPT.start_time.value;
  var vlen = stime.indexOf(':')==-1?1:2;
  var shour = stime.substring(0,2) ;
  var smin = stime.substring(stime.length-vlen) ;
  var duration = document.ADDAPPT.duration.value ;

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
function pasteAppt() {
	//document.forms[0].status.value = "<%=apptObj.getStatus()%>";
	document.forms[0].duration.value = "<%=apptObj.getDuration()%>";
	//document.forms[0].chart_no.value = "<%=apptObj.getChart_no()%>";
	document.forms[0].keyword.value = "<%=apptObj.getName()%>";
	document.forms[0].demographic_no.value = "<%=apptObj.getDemographic_no()%>";
	document.forms[0].reason.value = "<%=apptObj.getReason()%>";
	document.forms[0].notes.value = "<%=apptObj.getNotes()%>";
	//document.forms[0].location.value = "<%=apptObj.getLocation()%>";
	document.forms[0].resources.value = "<%=apptObj.getResources()%>";
}
<% } %>

// stop javascript -->

</script>
</head>
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
  caltime.add(caltime.MINUTE, Integer.parseInt(duration)-1 );

  String [] param = new String[9] ;
  param[0] = dateString2;
  param[1] = curProvider_no;
  param[2] = request.getParameter("start_time");
  param[3] = caltime.get(Calendar.HOUR_OF_DAY) +":"+ caltime.get(Calendar.MINUTE);
  param[4] = param[2];
  param[5] = param[3];
  param[6] = param[2];
  param[7] = param[3];
  param[8] = request.getParameter("programId_oscarView");
  
  List<Map> resultList = oscarSuperManager.find("appointmentDao", "search_appt", param);
  long apptnum = resultList.size() > 0 ? (Long)resultList.get(0).get("n") : 0;

  String deepcolor = apptnum==0?"#CCCCFF":"gold", weakcolor = apptnum==0?"#EEEEFF":"ivory";

  resultList = oscarSuperManager.find("appointmentDao", "search_appt_name", param);
  boolean bDnb = false;
  for (Map apt : resultList) {
    String apptName = (String) apt.get("name");
    if (apptName.equalsIgnoreCase(DONOTBOOK)) bDnb = true;
  }
  
  // select provider lastname & firstname
  String pLastname = "";
  String pFirstname = "";
  resultList = oscarSuperManager.find("appointmentDao", "search_provider_name", new Object [] {curProvider_no});
  if (resultList.size() > 0) {
	  Map name = resultList.get(0);
      pLastname = (String) name.get("last_name");
      pFirstname = (String) name.get("first_name");
  }
%>
<body background="../images/gray_bg.jpg" bgproperties="fixed"
	onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
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

  if (!bFirstDisp && request.getParameter("demographic_no") != null && !request.getParameter("demographic_no").equals("")) {

	resultList = oscarSuperManager.find("appointmentDao", "search_demographic_statusroster", new Object [] {request.getParameter("demographic_no") });
	for (Map status : resultList) {

        patientStatus      = (String) status.get("patient_status");
        address            = (String) status.get("address");                    
        city               = (String) status.get("city");
        province           = (String) status.get("province"); 
        postal             = (String) status.get("postal");
        phone              = (String) status.get("phone"); 
        phone2             = (String) status.get("phone2");
        email              = (String) status.get("email");
        String year_of_birth   = (String) status.get("year_of_birth");
        String month_of_birth  = (String) status.get("month_of_birth"); 
        String date_of_birth   = (String) status.get("date_of_birth");
        dob = "("+year_of_birth+"-"+month_of_birth+"-"+date_of_birth+")";
        sex = (String) status.get("sex");
        hin                    = (String) status.get("hin");
        String ver             = (String) status.get("ver");
        hin = hin +" "+ ver;

        if (patientStatus == null || patientStatus.equalsIgnoreCase("AC")) {
           patientStatus = "";
        } else if (patientStatus.equalsIgnoreCase("FI")||patientStatus.equalsIgnoreCase("DE")||patientStatus.equalsIgnoreCase("IN")) {
           disabled = "disabled";
        }

        String rosterStatus = (String) status.get("roster_status");
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

	resultList = oscarSuperManager.find("appointmentDao", "search_demographiccust_alert", new Object [] {request.getParameter("demographic_no") });
	for (Map alert : resultList) {
		if (alert.get("cust3") != null && !alert.get("cust3").equals("") ) {

%>
<p>
<table width="98%" BGCOLOR="yellow" border=1 align='center'>
	<tr>
		<td><font color='red'><bean:message key="Appointment.formAlert" />: <b><%=alert.get("cust3")%></b></font></td>
	</tr>
</table>
<%    

		}
	}
  }


  if(apptnum!=0) {

%>
<table width="98%" BGCOLOR="<%=deepcolor%>" border=1 align='center'>
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
<FORM NAME="ADDAPPT" METHOD="post" ACTION="appointmentcontrol.jsp"
	onsubmit="return(calculateEndTime())"><INPUT TYPE="hidden"
	NAME="displaymode" value="">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<tr bgcolor="<%=deepcolor%>">
		<th><font face="Helvetica"><bean:message
			key="appointment.addappointment.msgMainLabel" /> (<%=pFirstname%> <%=pLastname%>)</font></th>
	</tr>
</table>
<table border="0" cellpadding="0" cellspacing="0" width="100%">
	<tr>
		<td width="100%">
		<table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%"
			BGCOLOR="#C0C0C0">
			<tr valign="middle" BGCOLOR="#EEEEFF">
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formDate" /><font size='-1' color='brown'>(<%=dateString1%>)</font>:</font></div>
				</td>
				<td width="20%"><INPUT TYPE="TEXT" NAME="appointment_date"
					VALUE="<%=dateString2%>" WIDTH="25" HEIGHT="20" border="0"
					hspace="2"></td>
				<td width="5%"></td>
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formStatus" />:</font></div>
				</td>
				<td width="20%">
				<%
            if (strEditable!=null&&strEditable.equalsIgnoreCase("yes")){
            %> <select name="status" STYLE="width: 154px" HEIGHT="20"
					border="0">
					<% for (int i = 0; i < allStatus.size(); i++) { %>
					<option
						value="<%=((AppointmentStatus)allStatus.get(i)).getStatus()%>"
						<%=((AppointmentStatus)allStatus.get(i)).getStatus().equals("t")?"SELECTED":""%>><%=((AppointmentStatus)allStatus.get(i)).getDescription()%></option>
					<% } %>
				</select> <%
            }
            if (strEditable==null || !strEditable.equalsIgnoreCase("yes")){
            %> <INPUT TYPE="TEXT" NAME="status"
					VALUE='<%=bFirstDisp?"t":request.getParameter("status").equals("")?"":request.getParameter("status")%>'
					WIDTH="25" HEIGHT="20" border="0" hspace="2"> <%}%>
				</td>
			</tr>
			<tr valign="middle" BGCOLOR="#EEEEFF">
				<td width="20%">
				<div align="right"><font face="arial"><font
					face="arial"><bean:message key="Appointment.formStartTime" />:</font></font></div>
				</td>
				<td width="20%"><INPUT TYPE="TEXT" NAME="start_time"
					VALUE='<%=request.getParameter("start_time")%>' WIDTH="25"
					HEIGHT="20" border="0" onChange="checkTimeTypeIn(this)"></td>
				<td width="5%"></td>
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formType" />:</font></div>
				</td>
				<td width="20%"><INPUT TYPE="TEXT" NAME="type"
					VALUE='<%=bFirstDisp?"":request.getParameter("type").equals("")?"":request.getParameter("type")%>'
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle" BGCOLOR="#EEEEFF">
				<td width="20%" align="right"><font face="arial"><bean:message
					key="Appointment.formDuration" />:</font> <!--font face="arial"> End Time :</font-->
				</td>
				<td width="20%"><INPUT TYPE="TEXT" NAME="duration"
					VALUE="<%=duration%>" WIDTH="25" HEIGHT="20" border="0" hspace="2">
				<INPUT TYPE="hidden" NAME="end_time"
					VALUE='<%=request.getParameter("end_time")%>' WIDTH="25"
					HEIGHT="20" border="0" hspace="2" onChange="checkTimeTypeIn(this)">
				</td>
				<td width="5%"></td>
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formDoctor" />:</font></div>
				</td>
				<td width="20%"><%--RJ 07/10/2006 --%><%=bFirstDisp ? "" : providerBean.getProperty(curDoctor_no,"")%></td>
			</tr>
			<tr valign="middle" BGCOLOR="#CCCCFF">
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="appointment.addappointment.formSurName" />:</font></div>
				</td>
				<td width="20%"><INPUT TYPE="TEXT" NAME="keyword"
					VALUE='<%=(bFirstDisp && !bFromWL)?"":request.getParameter("name")==null?session.getAttribute("appointmentname")==null?"":session.getAttribute("appointmentname"):request.getParameter("name")%>'
					HEIGHT="20" border="0" hspace="2" width="25" tabindex="1">
				</td>
				<td width="5%"><font size=-1><a href=#
					onclick="onNotBook();">Not book</a></font></td>
				<td width="20%">
				<div align="right"><font face="arial"> <INPUT
					TYPE="hidden" NAME="orderby" VALUE="last_name, first_name">
				<INPUT TYPE="hidden" NAME="search_mode" VALUE="search_name">
				<INPUT TYPE="hidden" NAME="originalpage"
					VALUE="../appointment/addappointment.jsp"> <INPUT
					TYPE="hidden" NAME="limit1" VALUE="0"> <INPUT
					TYPE="hidden" NAME="limit2" VALUE="5"> <INPUT
					TYPE="hidden" NAME="ptstatus" VALUE="active"> <!--input type="hidden" name="displaymode" value="Search " -->
				<INPUT TYPE="submit"
					onclick="document.forms['ADDAPPT'].displaymode.value='Search '"
					VALUE="<bean:message key="appointment.addappointment.btnSearch"/> 
"></font></div>
				</td>
				<td width="20%"><input type="TEXT" name="demographic_no"
					ONFOCUS="onBlockFieldFocus(this)" readonly
					value='<%=(bFirstDisp && !bFromWL)?"":request.getParameter("demographic_no").equals("")?"":request.getParameter("demographic_no")%>'
					width="25" height="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle" BGCOLOR="#CCCCFF">
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formReason" />:</font></div>
				</td>
				<td width="20%"><font face="Times New Roman"> <textarea
					name="reason" tabindex="2" rows="2" wrap="virtual" cols="18"><%=bFirstDisp?"":request.getParameter("reason").equals("")?"":request.getParameter("reason")%></textarea>
				</font></td>
				<td width="5%"><font face="Times New Roman"> </font></td>
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formNotes" />:</font></div>
				</td>
				<td width="20%"><font face="Times New Roman"> <textarea
					name="notes" tabindex="3" rows="2" wrap="virtual" cols="18"><%=bFirstDisp?"":request.getParameter("notes").equals("")?"":request.getParameter("notes")%></textarea>
				</font></td>
			</tr>
			<% if (pros.isPropertyActive("mc_number")) { %>
			<tr valign="middle" BGCOLOR="#CCCCFF">
				<td align="right">M/C number:</td>
				<td><input type="text" name="appt_mc_number" tabindex="4" /></td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
				<td>&nbsp;</td>
			</tr>
<% } %>
			<tr valign="middle" BGCOLOR="#EEEEFF">
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formLocation" />:</font></div>
				</td>
				<% 
			// multisites start ==================
			boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable();
        	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
          	List<Site> sites = siteDao.getActiveSitesByProviderNo((String) session.getAttribute("user")); 
			// multisites end ==================

            OscarProperties props = OscarProperties.getInstance();
            boolean bMoreAddr = bMultisites? true : props.getProperty("scheduleSiteID", "").equals("") ? false : true;
			String tempLoc = "";
            if(bFirstDisp && bMoreAddr) {
            	//System.out.println(dateString2 + curProvider_no);
            	tempLoc = (new JdbcApptImpl()).getLocationFromSchedule(dateString2, curProvider_no);
            }
            String loc = bFirstDisp?tempLoc:request.getParameter("location");
            String colo = bMultisites
            				? ApptUtil.getColorFromLocation(sites, loc)
            				: bMoreAddr? ApptUtil.getColorFromLocation(props.getProperty("scheduleSiteID", ""), props.getProperty("scheduleSiteColor", ""),loc) : "white";
            %>
				<td width="20%">
<% // multisites start ==================
if (bMultisites) { %>
				<select tabindex="4" name="location" style="background-color: <%=colo%>" onchange='this.style.backgroundColor=this.options[this.selectedIndex].style.backgroundColor'>
				<% for (Site s:sites) { %>
					<option value="<%=s.getName()%>" style="background-color: <%=s.getBgColor()%>" <%=s.getName().equals(loc)?"selected":"" %>><%=s.getName()%></option>
				<% } %>
				</select>
<% } else { 
	// multisites end ==================
%>				
				<input type="TEXT" name="location"
					style="background-color: <%=colo%>" tabindex="4" value="<%=loc%>"
					width="25" height="20" border="0" hspace="2">
<% } %>					
				</td>
				<td width="5%"></td>
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formResources" />:</font></div>
				</td>
				<td width="20%"><input type="TEXT" name="resources"
					tabindex="5"
					value='<%=bFirstDisp?"":request.getParameter("resources").equals("")?"":request.getParameter("resources")%>'
					width="25" height="20" border="0" hspace="2"></td>
			</tr>
			<tr valign="middle" BGCOLOR="#EEEEFF">
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formCreator" />:</font></div>
				</td>
				<td width="20%"><INPUT TYPE="TEXT" NAME="user_id" readonly
					VALUE='<%=bFirstDisp?(userlastname+", "+userfirstname):request.getParameter("user_id").equals("")?"Unknown":request.getParameter("user_id")%>'
					WIDTH="25" HEIGHT="20" border="0" hspace="2"></td>
				<td width="5%"></td>
				<td width="20%">
				<div align="right"><font face="arial"><bean:message
					key="Appointment.formDateTime" />:</font></div>
				</td>
				<td width="20%">
				<%

				GregorianCalendar now=new GregorianCalendar();

                        GregorianCalendar cal = (GregorianCalendar) now.clone();

                        cal.add(cal.DATE, 1);

				String strDateTime=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "

					+	now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);

			%> <INPUT TYPE="TEXT" NAME="createdatetime" readonly
					VALUE="<%=strDateTime%>" WIDTH="25" HEIGHT="20" border="0"
					hspace="2"> <INPUT TYPE="hidden" NAME="provider_no"
					VALUE="<%=curProvider_no%>"> <INPUT TYPE="hidden"
					NAME="dboperation" VALUE="add_apptrecord"> <INPUT
					TYPE="hidden" NAME="creator"
					VALUE='<%=userlastname+", "+userfirstname%>'> <INPUT
					TYPE="hidden" NAME="remarks" VALUE=""></td>
			</tr>
		</table>
		</td>
	</tr>
</table>
<%String demoNo = request.getParameter("demographic_no");%>
<table width="100%" BGCOLOR="<%=deepcolor%>">
	<tr>
		<% if(!bDnb) { %>
		<TD nowrap><INPUT TYPE="submit"
			onclick="document.forms['ADDAPPT'].displaymode.value='Group Appt'"
			VALUE="<bean:message key="appointment.addappointment.btnGroupAppt"/> 
"
			<%=disabled%>> <%

  if(dateString2.equals( inform.format(inform.parse(now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH))) ) 

     || dateString2.equals( inform.format(inform.parse(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH))) ) ) {

    org.apache.struts.util.MessageResources resources = org.apache.struts.util.MessageResources.getMessageResources("oscarResources");

    %> <INPUT TYPE="submit"
			onclick="document.forms['ADDAPPT'].displaymode.value='Add Appt & PrintPreview'"
			VALUE="<bean:message key='appointment.addappointment.btnAddApptPrintPreview'/>"
			<%=disabled%>>


    <%
  }

%>

    
<INPUT TYPE="submit"
			onclick="document.forms['ADDAPPT'].displaymode.value='Add Appointment'"
			tabindex="6"
			VALUE="<bean:message key="appointment.addappointment.btnAddAppointment"/>" <%=disabled%>>
 <INPUT TYPE="submit"
			onclick="document.forms['ADDAPPT'].displaymode.value='Add Appt & PrintCard'"
			VALUE="&<bean:message key='global.btnPrint'/>"
			<%=disabled%>>
                </TD>
		<TD></TD>
		<% } %>
		<TD align="right">
		<% if(apptObj!=null) { %> <input type="button" value="Paste"
			onclick="pasteAppt();"> <% } %> <INPUT TYPE="RESET"
			VALUE="<bean:message key="appointment.addappointment.btnCancel"/>" onClick="window.close();"> <input type="button"
			value="<bean:message key="appointment.addappointment.btnRepeat"/>" onclick="onButRepeat()" <%=disabled%>></TD>
	</tr>
</table>
</FORM>


<table align="center" style="font-family: arial, sans-serif">
<tr>
    <td valign="top">    
        <%if( bFromWL && demoNo != null && demoNo.length() > 0 ) {%>
        <table style="font-size: 9pt;" bgcolor="#c0c0c0" align="center" valign="top" cellpadding="3px">
            <tr bgcolor="#ccccff">
                <th style="font-family: arial, sans-serif" colspan="2">
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
<table style="font-size: 8pt;" bgcolor="#c0c0c0" align="center" valign="top">
	<tr bgcolor="#ccccff">
		<th style="font-family: arial, sans-serif" colspan="4"><bean:message key="appointment.addappointment.msgOverview" /></th>
	</tr>
	<tr style="font-family: arial, sans-serif" bgcolor="#ccccff">
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
            cal2.add(Calendar.YEAR, 1);
            param2[2] = new java.sql.Date(cal2.getTime().getTime());
    		resultList = oscarSuperManager.find("appointmentDao", "search_appt_future", param2);

    		for (Map appt : resultList) {
                iRow ++;
                if (iRow > iPageSize) break;
    %>
	<tr bgcolor="#eeeeff">
		<td style="background-color: #CCFFCC; padding-right: 25px"><%=appt.get("appointment_date")%></td>
		<td style="background-color: #CCFFCC; padding-right: 25px"><%=appt.get("start_time")%></td>
		<td style="background-color: #CCFFCC; padding-right: 25px"><%=appt.get("last_name") + ",&nbsp;" + appt.get("first_name")%></td>
		<td style="background-color: #CCFFCC;"><%=appt.get("status")==null?"":(appt.get("status").equals("N")?"No Show":(appt.get("status").equals("C")?"Cancelled":"") )%></td>
	</tr>
	<%
            }

            iRow=0;
            cal2 = Calendar.getInstance();
            cal2.add(Calendar.YEAR, -1);
            param2[2] = new java.sql.Date(cal2.getTime().getTime());
    		resultList = oscarSuperManager.find("appointmentDao", "search_appt_past", param2);

    		for (Map appt : resultList) {
                iRow ++;
                if (iRow > iPageSize) break;
    %>
	<tr bgcolor="#eeeeff">
		<td style="padding-right: 25px"><%=appt.get("appointment_date")%></td>
		<td style="padding-right: 25px"><%=appt.get("start_time")%></td>
		<td style="padding-right: 25px"><%=appt.get("last_name") + ",&nbsp;" + appt.get("first_name")%></td>
		<td><%=appt.get("status")==null?"":(appt.get("status").equals("N")?"No Show":(appt.get("status").equals("C")?"Cancelled":"") )%></td>
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
</html:html>

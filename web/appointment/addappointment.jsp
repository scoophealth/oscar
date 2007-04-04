<%

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
  
  ApptData apptObj = (new ApptOpt()).getApptObj(request);

%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.text.*, java.lang.*,java.net.*, oscar.appt.*" errorPage="../appointment/errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<jsp:useBean id="addApptBean" class="oscar.AppointmentMainBean" scope="page" /><%@ include file="../admin/dbconnection.jsp" %>
<%--RJ 07/07/2006 --%>
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<% 

String [][] dbQueries=new String[][] { 

    {"search_appt", "select count(appointment_no) AS n from appointment where appointment_date = ? and provider_no = ? and status !='C' and ((start_time>= ? and start_time<= ?) or (end_time>= ? and end_time<= ?) or (start_time<= ? and end_time>= ?) )" }, 
    {"search_appt_name", "select name from appointment where appointment_date = ? and provider_no = ? and status !='C' and ((start_time>= ? and start_time<= ?) or (end_time>= ? and end_time<= ?) or (start_time<= ? and end_time>= ?) )" }, 
    {"search_demographiccust_alert", "select cust3 from demographiccust where demographic_no = ? " }, 

    {"search_demographic_statusroster", "select patient_status,roster_status from demographic where demographic_no = ? " }, 
    {"search_appt_future", "select appt.appointment_date, appt.start_time, appt.status, p.last_name, p.first_name from appointment appt, provider p where appt.provider_no = p.provider_no and appt.demographic_no = ? and appt.appointment_date >= now() and appt.appointment_date < date_add(now(), interval 365 day) order by appointment_date limit 5" },
    {"search_appt_past", "select appt.appointment_date, appt.start_time, appt.status, p.last_name, p.first_name from appointment appt, provider p where appt.provider_no = p.provider_no and appt.demographic_no = ? and appt.appointment_date < now() and appt.appointment_date > date_sub(now(), interval 365 day) order by appointment_date desc limit 5"}

  };

  addApptBean.doConfigure(dbParams,dbQueries);

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

 * McMaster Unviersity 

 * Hamilton 

 * Ontario, Canada 

 */

-->
<head>
<title><bean:message key="appointment.addappointment.title"/></title>
<script type="text/javascript">

<!-- start javascript ---- check to see if it is really empty in database

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
<html:html locale="true">
<meta http-equiv="Cache-Control" content="no-cache" >
<%

  SimpleDateFormat fullform = new SimpleDateFormat ("yyyy-MM-dd HH:mm");

  SimpleDateFormat inform = new SimpleDateFormat ("yyyy-MM-dd");

  SimpleDateFormat outform = new SimpleDateFormat ("EEE");

  java.util.Date apptDate = fullform.parse(bFirstDisp?(request.getParameter("year")+"-"+request.getParameter("month")+"-"+request.getParameter("day")+" "+ request.getParameter("start_time")):(request.getParameter("appointment_date")+" "+ request.getParameter("start_time") )) ;

  String dateString1 = outform.format(apptDate );

  String dateString2 = inform.format(apptDate );



	GregorianCalendar caltime =new GregorianCalendar( );

	caltime.setTime(apptDate);

	caltime.add(caltime.MINUTE, Integer.parseInt(duration)-1 );



  String [] param = new String[8] ;

  param[0] = dateString2;

  param[1] = curProvider_no;

  param[2] = request.getParameter("start_time");

  param[3] = caltime.get(Calendar.HOUR_OF_DAY) +":"+ caltime.get(Calendar.MINUTE);

//	System.out.println(param[3] );

  param[4] = param[2];

  param[5] = param[3];

  param[6] = param[2];

  param[7] = param[3];

	ResultSet rsdemo = addApptBean.queryResults(param, "search_appt");

	int apptnum = rsdemo.next()?rsdemo.getInt("n"):0 ;

  String deepcolor = apptnum==0?"#CCCCFF":"gold", weakcolor = apptnum==0?"#EEEEFF":"ivory";

  rsdemo = addApptBean.queryResults(param, "search_appt_name");
  boolean bDnb = false;
  while(rsdemo.next()) {
      String apptName = rsdemo.getString("name");
      if(apptName.equalsIgnoreCase(DONOTBOOK)) bDnb = true;
  }
  
%>
<body  background="../images/gray_bg.jpg" bgproperties="fixed"  onLoad="setfocus()" topmargin="0"  leftmargin="0" rightmargin="0"> 
<%
String patientStatus = "";
String disabled="";
%>
<%

  //to show Alert msg

  if (!bFirstDisp && !request.getParameter("demographic_no").equals("")) {

	  rsdemo = addApptBean.queryResults(request.getParameter("demographic_no"), "search_demographic_statusroster");

      while (rsdemo.next()) { 

            patientStatus = rsdemo.getString("patient_status");

            if (patientStatus == null || patientStatus.equalsIgnoreCase("AC")) {

               patientStatus = "";


            }
	         else if (patientStatus.equalsIgnoreCase("FI")||patientStatus.equalsIgnoreCase("DE")||patientStatus.equalsIgnoreCase("IN")) {
    
               disabled = "disabled";

            }
	
            String rosterStatus = rsdemo.getString("roster_status");

            if (rosterStatus == null || rosterStatus.equalsIgnoreCase("RO")) {

               rosterStatus = "";

            }

/*          patientStatus = (patientStatus != null && !patientStatus.equalsIgnoreCase("AC")) ? 

			  (" Patient Status:<font color='yellow'>" + patientStatus + "</font>" ) : "";



		  String rosterStatus = rsdemo.getString("roster_status");

          rosterStatus = (rosterStatus != null && !rosterStatus.equalsIgnoreCase("RO")) ?

			  (" Roster Status:<font color='yellow'>" + rosterStatus + "</font> ") : "";

*/



		  if(!patientStatus.equals("") || !rosterStatus.equals("") ) {

              String rsbgcolor = "BGCOLOR=\"orange\"" ;

			  String exp = " null-undefined\n IN-inactive ID-deceased OP-out patient\n NR-not signed\n FS-fee for service\n TE-terminated\n SP-self pay\n TP-third party";

%> 
<table width="98%" <%=rsbgcolor%> border=0 align='center'> 
  <tr> 
    <td><font color='blue' title='<%=exp%>'> <b><bean:message key="Appointment.msgPatientStatus"/>:&nbsp;<font color='yellow'><%=patientStatus%></font>&nbsp;<bean:message key="Appointment.msgRosterStatus"/>:&nbsp;<font color='yellow'><%=rosterStatus%></font></b></td> 
  </tr> 
</table> 
<% 

	      }

      }

	  rsdemo = addApptBean.queryResults(request.getParameter("demographic_no"), "search_demographiccust_alert");

      while (rsdemo.next()) { 

          if(rsdemo.getString("cust3")!=null && !rsdemo.getString("cust3").equals("") ) {

%> 
<p>  
<table width="98%" BGCOLOR="yellow" border=1 align='center'> 
  <tr> 
    <td><font color='red'><bean:message key="Appointment.formAlert"/>: <b><%=rsdemo.getString("cust3")%></b></td> 
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
    <TH><font color='red'> 
      <% if(apptnum>1) {

         %> 
      <bean:message key='appointment.addappointment.msgBooking'/> 
      <%

       } else {

          %> 
      <bean:message key='appointment.addappointment.msgDoubleBooking'/> 
      <%
			if(bDnb) out.println("<br/>You can NOT book an appointment on this time slot.");
       }

     %> 
      </font></TH> 
  </tr> 
</TABLE> 
<% } %> 
<FORM NAME = "ADDAPPT" METHOD="post" ACTION="appointmentcontrol.jsp" onsubmit="return(calculateEndTime())"> 
  <INPUT TYPE="hidden" NAME="displaymode" value=""> 
  <table border=0 cellspacing=0 cellpadding=0 width="100%" > 
    <tr bgcolor="<%=deepcolor%>"> 
      <th><font face="Helvetica"><bean:message key="appointment.addappointment.msgMainLabel"/></font></th> 
    </tr> 
  </table> 
  <table border="0" cellpadding="0" cellspacing="0" width="100%"> 
    <tr> 
      <td width="100%"> <table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%" BGCOLOR="#C0C0C0"> 
          <tr valign="middle" BGCOLOR="#EEEEFF"> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="Appointment.formDate"/><font size='-1' color='brown'>(<%=dateString1%>)</font>:</font></div></td> 
            <td width="20%"> <INPUT TYPE="TEXT" NAME="appointment_date" VALUE="<%=dateString2%>" WIDTH="25" HEIGHT="20" border="0" hspace="2"> </td> 
            <td width="5%" ></td> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="Appointment.formStatus"/>:</font></div></td> 
            <td width="20%"> <INPUT TYPE="TEXT" NAME="status" VALUE="<%=bFirstDisp?"t":request.getParameter("status").equals("")?"":request.getParameter("status")%>"  WIDTH="25" HEIGHT="20" border="0" hspace="2"> </td> 
          </tr> 
          <tr valign="middle" BGCOLOR="#EEEEFF"> 
            <td width="20%"> <div align="right"><font face="arial"><font face="arial"><bean:message key="Appointment.formStartTime"/>:</font></font></div></td> 
            <td width="20%"> <INPUT TYPE="TEXT" NAME="start_time" VALUE="<%=request.getParameter("start_time")%>" WIDTH="25" HEIGHT="20" border="0"  onChange="checkTimeTypeIn(this)"> </td> 
            <td width="5%" ></td> 
            <td width="20%" > <div align="right"><font face="arial"><bean:message key="Appointment.formType"/>:</font></div></td> 
            <td width="20%"> <INPUT TYPE="TEXT" NAME="type" VALUE="<%=bFirstDisp?"":request.getParameter("type").equals("")?"":request.getParameter("type")%>" WIDTH="25" HEIGHT="20" border="0" hspace="2"> </td> 
          </tr> 
          <tr valign="middle" BGCOLOR="#EEEEFF" > 
            <td width="20%"  align="right"> <font face="arial"><bean:message key="Appointment.formDuration"/>:</font> 
              <!--font face="arial"> End Time :</font--> </td> 
            <td width="20%"> <INPUT TYPE="TEXT" NAME="duration" VALUE="<%=duration%>" WIDTH="25" HEIGHT="20" border="0" hspace="2" > 
              <INPUT TYPE="hidden" NAME="end_time" VALUE="<%=request.getParameter("end_time")%>" WIDTH="25" HEIGHT="20" border="0" hspace="2"  onChange="checkTimeTypeIn(this)"> </td> 
            <td width="5%" ></td> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="Appointment.formDoctor"/>:</font></div></td> 
            <td width="20%"><%--RJ 07/10/2006 --%><%=bFirstDisp ? "" : providerBean.getProperty(curDoctor_no,"")%></td> 
          </tr> 
          <tr valign="middle" BGCOLOR="#CCCCFF"> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="appointment.addappointment.formSurName"/>:</font></div></td> 
            <td width="20%"> <INPUT TYPE="TEXT" NAME="keyword" VALUE="<%=(bFirstDisp && !bFromWL)?"":request.getParameter("name").equals("")?session.getAttribute("appointmentname")==null?"":session.getAttribute("appointmentname"):request.getParameter("name")%>" HEIGHT="20" border="0" hspace="2" width="25" tabindex="1"> </td> 
            <td width="5%"><font size=-1><a href=# onclick="onNotBook();">Not book</font></a></td> 
            <td width="20%"> <div align="right"><font face="arial"> 
                <INPUT TYPE="hidden" NAME="orderby" VALUE="last_name, first_name" > 
                <INPUT TYPE="hidden" NAME="search_mode" VALUE="search_name" > 
                <INPUT TYPE="hidden" NAME="originalpage" VALUE="../appointment/addappointment.jsp" > 
                <INPUT TYPE="hidden" NAME="limit1" VALUE="0" > 
                <INPUT TYPE="hidden" NAME="limit2" VALUE="5" > 
                <INPUT TYPE="hidden" NAME="ptstatus" VALUE="active"> 
                <!--input type="hidden" name="displaymode" value="Search " --> 
                <INPUT TYPE="submit" onclick="document.forms['ADDAPPT'].displaymode.value='Search '" VALUE="<bean:message key="appointment.addappointment.btnSearch"/> 
"></font></div></td> 
            <td width="20%" > <input type="TEXT" name="demographic_no" ONFOCUS="onBlockFieldFocus(this)" readonly value="<%=(bFirstDisp && !bFromWL)?"":request.getParameter("demographic_no").equals("")?"":request.getParameter("demographic_no")%>" width="25" height="20" border="0" hspace="2"> </td> 
          </tr> 
          <tr valign="middle" BGCOLOR="#CCCCFF"> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="Appointment.formReason"/>:</font></div></td> 
            <td width="20%"><font face="Times New Roman"> 
              <textarea name="reason"  tabindex="2" rows="2" wrap="virtual" cols="18"><%=bFirstDisp?"":request.getParameter("reason").equals("")?"":request.getParameter("reason")%></textarea> 
              </font> </TD> 
            <td width="5%"><font face="Times New Roman"> </font></td> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="Appointment.formNotes"/>:</font></div></td> 
            <td width="20%"><font face="Times New Roman"> 
              <textarea name="notes"  tabindex="3" rows="2" wrap="virtual" cols="18"><%=bFirstDisp?"":request.getParameter("notes").equals("")?"":request.getParameter("notes")%></textarea> 
              </font> </td> 
          </tr> 
          <tr valign="middle" BGCOLOR="#EEEEFF"> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="Appointment.formLocation"/>:</font></div></td> 
            <% 
            OscarProperties props = OscarProperties.getInstance();
            boolean bMoreAddr = props.getProperty("scheduleSiteID", "").equals("") ? false : true;
			String tempLoc = "";
            if(bFirstDisp && bMoreAddr) {
            	//System.out.println(dateString2 + curProvider_no);
            	tempLoc = (new ApptOpt()).getLocationFromSchedule(dateString2, curProvider_no);
            }
            String loc = bFirstDisp?tempLoc:request.getParameter("location").equals("")?"":request.getParameter("location");
            String colo = bMoreAddr? (new ApptOpt()).getColorFromLocation(props.getProperty("scheduleSiteID", ""), props.getProperty("scheduleSiteColor", ""),loc) : "white";
            %>
            <td width="20%"> <input type="TEXT" name="location" style="background-color: <%=colo%>" tabindex="4" value="<%=loc%>" width="25" height="20" border="0" hspace="2"> </TD> 
            <td width="5%" ></td> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="Appointment.formResources"/>:</font></div></td> 
            <td width="20%"> <input type="TEXT" name="resources"  tabindex="5" value="<%=bFirstDisp?"":request.getParameter("resources").equals("")?"":request.getParameter("resources")%>" width="25" height="20" border="0" hspace="2"> </td> 
          </tr> 
          <tr valign="middle" BGCOLOR="#EEEEFF"> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="Appointment.formCreator"/>:</font></div></td> 
            <td width="20%"> <INPUT TYPE="TEXT" NAME="user_id" readonly VALUE='<%=bFirstDisp?(userlastname+", "+userfirstname):request.getParameter("user_id").equals("")?"Unknown":request.getParameter("user_id")%>' WIDTH="25" HEIGHT="20" border="0" hspace="2"> </td> 
            <td width="5%" ></td> 
            <td width="20%"> <div align="right"><font face="arial"><bean:message key="Appointment.formDateTime"/>:</font></div></td> 
            <td width="20%"> <%

				GregorianCalendar now=new GregorianCalendar();

                        GregorianCalendar cal = (GregorianCalendar) now.clone();

                        cal.add(cal.DATE, 1);

				String strDateTime=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "

					+	now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);

			%> 
              <INPUT TYPE="TEXT" NAME="createdatetime" readonly VALUE="<%=strDateTime%>" WIDTH="25" HEIGHT="20" border="0" hspace="2"> 
              <INPUT TYPE="hidden" NAME="provider_no" VALUE="<%=curProvider_no%>"> 
              <INPUT TYPE="hidden" NAME="dboperation" VALUE="add_apptrecord"> 
              <INPUT TYPE="hidden" NAME="creator" VALUE="<%=userlastname+", "+userfirstname%>"> 
              <INPUT TYPE="hidden" NAME="remarks" VALUE=""> </td> 
          </tr> 
        </table></td> 
    </tr> 
  </table> 
  <table width="100%" BGCOLOR="<%=deepcolor%>"> 
    <tr> 
<% if(!bDnb) { %>    
      <TD nowrap> <INPUT TYPE="submit" onclick="document.forms['ADDAPPT'].displaymode.value='Group Appt'" VALUE="<bean:message key="appointment.addappointment.btnGroupAppt"/> 
" <%=disabled%>> 
        <%

  if(dateString2.equals( inform.format(inform.parse(now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH))) ) 

     || dateString2.equals( inform.format(inform.parse(cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH))) ) ) {

    org.apache.struts.util.MessageResources resources = org.apache.struts.util.MessageResources.getMessageResources("oscarResources");

    %> 
        <INPUT TYPE="submit" onclick="document.forms['ADDAPPT'].displaymode.value='Add Appt & PrintPreview'" VALUE="<bean:message key='appointment.addappointment.btnAddApptPrintPreview'/>" <%=disabled%>> 
        <%

  }

%> 
        <INPUT TYPE="submit" onclick="document.forms['ADDAPPT'].displaymode.value='Add Appointment'" tabindex="6" VALUE="<bean:message key="appointment.addappointment.btnAddAppointment"/> 
" <%=disabled%>> </TD> 
      <TD></TD> 
<% } %>
      <TD align="right">
          <% if(apptObj!=null) { %>
                <input type="button" value="Paste" onclick="pasteAppt();">
          <% } %>
          <INPUT TYPE = "RESET" VALUE = "<bean:message key="appointment.addappointment.btnCancel"/> 
" onClick="window.close();"> 
        <input type="button" value="<bean:message key="appointment.addappointment.btnRepeat"/> 
" onclick="onButRepeat()" <%=disabled%>></TD> 
    </tr> 
  </TABLE> 
</FORM> 

    <table style="font-size:8pt;" bgcolor="#c0c0c0" align="center">
        <tr bgcolor="#ccccff">
            <th style="font-family: arial, sans-serif" colspan="4"><bean:message key="appointment.addappointment.msgOverview" /></th>
        </tr>
        <tr style="font-family: arial, sans-serif" bgcolor="#ccccff">
            <th style="padding-right:25px"><bean:message key="Appointment.formDate" /></th>
            <th style="padding-right:25px"><bean:message key="Appointment.formStartTime" /></th>
            <th style="padding-right:25px"><bean:message key="appointment.addappointment.msgProvider" /></th>
            <th><bean:message key="appointment.addappointment.msgComments" /></th>
        </tr>
      <%                          
        if( bFromWL ) {
            rsdemo = addApptBean.queryResults(request.getParameter("demographic_no"), "search_appt_future");
            ArrayList appts = new ArrayList();
            ApptData appt;
            while(rsdemo.next()) {
                appt = new ApptData();
                appt.setAppointment_date(rsdemo.getString("appointment_date"));
                appt.setStart_time(rsdemo.getString("start_time"));
                appt.setName(rsdemo.getString("last_name") + ", " + rsdemo.getString("first_name")); //provider name
                appt.setStatus(rsdemo.getString("status")==null?"":(rsdemo.getString("status").equals("N")?"No Show":(rsdemo.getString("status").equals("C")?"Cancelled":"") ) );
                appts.add(appt);
            }
            //we have to reverse order to start in future and move towards present
            for( int idx = appts.size()-1; idx >= 0; --idx ) {
                appt = (ApptData)appts.get(idx);
        %>
            <tr bgcolor="#eeeeff">
                <td style="background-color: #CCFFCC; padding-right:25px"><%=appt.getAppointment_date()%></td>
                <td style="background-color: #CCFFCC; padding-right:25px"><%=appt.getStart_time()%></td>
                <td style="background-color: #CCFFCC; padding-right:25px"><%=appt.getName()%></td>
                <td style="background-color: #CCFFCC;" ><%=appt.getStatus()%></td>
            </tr>
        <%
            }
            rsdemo.close();
            rsdemo = addApptBean.queryResults(request.getParameter("demographic_no"), "search_appt_past");
            while(rsdemo.next()) {
        %>
            <tr bgcolor="#eeeeff">
                <td style="padding-right:25px"><%=rsdemo.getString("appointment_date")%></td>
                <td style="padding-right:25px"><%=rsdemo.getString("start_time")%></td>
                <td style="padding-right:25px"><%=rsdemo.getString("last_name") + ",&nbsp;" + rsdemo.getString("first_name")%></td>
                <td><%=rsdemo.getString("status")==null?"":(rsdemo.getString("status").equals("N")?"No Show":(rsdemo.getString("status").equals("C")?"Cancelled":"") )%></td>
            </tr>
        <%
            }
        }
        rsdemo.close();
        addApptBean.closePstmtConn();
        %>
    </table>
</body>
</html:html>

<%
  if(session.getAttribute("user") == null)    response.sendRedirect("../logout.jsp");
  String curProvider_no = request.getParameter("provider_no");
  String curUser_no = (String) session.getAttribute("user");
  String userfirstname = (String) session.getAttribute("userfirstname");
  String userlastname = (String) session.getAttribute("userlastname");
  String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF";
  String origDate = null;

  

  boolean bFirstDisp=true; //this is the first time to display the window
  if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
  
  ApptData apptObj = (new ApptOpt()).getApptObj(request);
%>
<%@ page import="oscar.oscarDemographic.data.*, java.util.*, java.sql.*, oscar.appt.*, oscar.*" errorPage="errorpage.jsp" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" errorPage="errorpage.jsp" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" errorPage="errorpage.jsp" %>
<%@ page import="oscar.appt.status.service.AppointmentStatusMgr" errorPage="errorpage.jsp" %>
<%@ page import="oscar.appt.status.model.AppointmentStatus" errorPage="errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
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
<%      
  oscar.OscarProperties pros = oscar.OscarProperties.getInstance();
  String strEditable = pros.getProperty("ENABLE_EDIT_APPT_STATUS");
  WebApplicationContext wc = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getSession().getServletContext());
  AppointmentStatusMgr apptStatusMgr = (AppointmentStatusMgr)wc.getBean("AppointmentStatusMgr");
  List allStatus = apptStatusMgr.getAllActiveStatus();
%>
<html:html locale="true">
<head><title><bean:message key="appointment.editappointment.title"/></title>

<script language="javascript">
<!-- start javascript ---- 
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
// stop javascript -->
</script>

</head>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script>
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
          window.location='appointmentcontrol.jsp?buttoncancel=Cancel Appt&displaymode=Update Appt&appointment_no=<%=request.getParameter("appointment_no")%>';
       }              
   }else{
      window.location='appointmentcontrol.jsp?buttoncancel=Cancel Appt&displaymode=Update Appt&appointment_no=<%=request.getParameter("appointment_no")%>';
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
<% if(apptObj!=null) { %>
function pasteAppt() {
	document.EDITAPPT.status.value = "<%=apptObj.getStatus()%>";
	document.EDITAPPT.duration.value = "<%=apptObj.getDuration()%>";
	document.EDITAPPT.chart_no.value = "<%=apptObj.getChart_no()%>";
	document.EDITAPPT.keyword.value = "<%=apptObj.getName()%>";
	document.EDITAPPT.demographic_no.value = "<%=apptObj.getDemographic_no()%>";
	document.EDITAPPT.reason.value = "<%=apptObj.getReason()%>";
	document.EDITAPPT.notes.value = "<%=apptObj.getNotes()%>";
	document.EDITAPPT.location.value = "<%=apptObj.getLocation()%>";
	document.EDITAPPT.resources.value = "<%=apptObj.getResources()%>";
}
<% } %>
function onCut() {
  document.EDITAPPT.submit();
}

</script>
<body  onload="setfocus()" background="../images/gray_bg.jpg" bgproperties="fixed" topmargin="0" leftmargin="0" rightmargin="0">
<FORM NAME = "EDITAPPT" METHOD="post" ACTION="appointmentcontrol.jsp"  onSubmit="return(onSub())">
<INPUT TYPE="hidden" NAME="displaymode" value="">
<table border=0 cellspacing=0 cellpadding=0 width="100%" >
  <tr bgcolor="<%=deepcolor%>"><th><font face="Helvetica"><bean:message key="appointment.editappointment.msgMainLabel"/></font></th></tr>
</table>

<%
  String chartno="", phone="", rosterstatus="", alert="";
  String[] param = new String[1];
  param[0]=request.getParameter("appointment_no");
  String strApptDate = bFirstDisp?"":request.getParameter("appointment_date") ;

  ResultSet rs = null, rsdemo = null;
  if(bFirstDisp) rs = apptMainBean.queryResults(param, request.getParameter("dboperation"));
  if(bFirstDisp&&rs==null) { %>
     <bean:message key="appointment.editappointment.msgNoSuchAppointment"/>
  <% } else {
    while (bFirstDisp?rs.next():true) { 
	  //get chart_no from demographic table if it exists.
	  if(bFirstDisp && apptMainBean.getString(rs,"demographic_no")!=null) {
	    String demono=apptMainBean.getString(rs,"demographic_no");
		  if(!demono.equals("0") && !demono.equals("") ) {
    	  rsdemo = apptMainBean.queryResults(demono, "search_detail");
		    if(rsdemo.next()) {
		      chartno = rsdemo.getString("chart_no");
		      phone = rsdemo.getString("phone");
		      rosterstatus = rsdemo.getString("roster_status");
		    }
    	  rsdemo = apptMainBean.queryResults(demono, "search_demographiccust_alert");
		    if(rsdemo.next()) {
		      alert = rsdemo.getString("cust3");
		    }
		    
		  }
	  }  
	  if(!bFirstDisp && !request.getParameter("demographic_no").equals("") ) {
	    String demono=request.getParameter("demographic_no");
		  if(!demono.equals("0") ) {
    	  rsdemo = apptMainBean.queryResults(demono, "search_detail");
		    if(rsdemo.next()) {
		      chartno = rsdemo.getString("chart_no");
		      phone = rsdemo.getString("phone");
		      rosterstatus = rsdemo.getString("roster_status");
		    }
    	  rsdemo = apptMainBean.queryResults(demono, "search_demographiccust_alert");
		    if(rsdemo.next()) {
		      alert = rsdemo.getString("cust3");
		    }
		  }
    }	

    //RJ 07/12/2006
    //If page is loaded first time hit db for patient's family doctor
    //Else if we are coming back from search this has been done for us
    //Else how did we get here?
    String doctorNo = "";
    if( bFirstDisp ) {
        DemographicData dd = new DemographicData();
        DemographicData.Demographic demo = dd.getDemographic(apptMainBean.getString(rs,"demographic_no"));
        doctorNo = demo!=null? (demo.getProviderNo()) : "";
    }
    else if( !request.getParameter("doctor_no").equals(""))
        doctorNo = request.getParameter("doctor_no");
%>
<table border="0" cellpadding="0" cellspacing="0" width="100%" >
  <tr><td width="100%">
  
        <table BORDER="0" CELLPADDING="0" CELLSPACING="1" WIDTH="100%" BGCOLOR="<%=weakcolor%>">
          <tr valign="middle"> 
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"> <bean:message key="Appointment.formDate"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
              <INPUT TYPE="TEXT" NAME="appointment_date" VALUE="<%=bFirstDisp?apptMainBean.getString(rs,"appointment_date"):strApptDate%>" WIDTH="25" HEIGHT="20" border="0" hspace="2">
            </td>
            <td width="5%"  ></td>
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"> <bean:message key="Appointment.formStatus"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT">
            <%
              String statusCode = apptMainBean.getString(rs,"status");
              String signOrVerify = "";
              if (statusCode.length()>=2){
              signOrVerify = statusCode.substring(1,2);
              statusCode = statusCode.substring(0,1);
              }
            %>
            <%
            if (strEditable!=null&&strEditable.equalsIgnoreCase("yes")){
            %>
              <select name="status" STYLE="width: 154px" HEIGHT="20" border="0">
                <% for (int i = 0; i < allStatus.size(); i++) { %>
                <option value="<%=((AppointmentStatus)allStatus.get(i)).getStatus()+signOrVerify%>" <%=((AppointmentStatus)allStatus.get(i)).getStatus().equals(apptMainBean.getString(rs,"status"))?"SELECTED":""%>><%=((AppointmentStatus)allStatus.get(i)).getDescription()%></option>
                <% } %>
              </select>
            <%
            }
            if (strEditable==null || !strEditable.equalsIgnoreCase("yes")){
            %>
              <INPUT TYPE="TEXT" NAME="status" VALUE="<%=bFirstDisp?apptMainBean.getString(rs,"status"):request.getParameter("status")%>"  WIDTH="25" HEIGHT="20" border="0" hspace="2">
            <%}%>
	   </td>
          </tr>
          <tr valign="middle"> 
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"><font face="arial"> <bean:message key="Appointment.formStartTime"/> :</font></font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
              <INPUT TYPE="TEXT" NAME="start_time" VALUE="<%=bFirstDisp?apptMainBean.getString(rs,"start_time").substring(0,5):request.getParameter("start_time")%>" WIDTH="25" HEIGHT="20" border="0" hspace="2">
            </td>
            <td width="5%"  ></td>
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"> <bean:message key="Appointment.formType"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
              <INPUT TYPE="TEXT" NAME="type" VALUE="<%=bFirstDisp?apptMainBean.getString(rs,"type"):request.getParameter("type")%>" WIDTH="25" HEIGHT="20" border="0" hspace="2">
            </td>
          </tr>
          <tr valign="middle"> 
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"> <bean:message key="Appointment.formDuration"/>:</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
<%
  int everyMin = 1;
  if(bFirstDisp) {
    int endtime = (Integer.parseInt(apptMainBean.getString(rs,"end_time").substring(0,2) ) )*60 + (Integer.parseInt(apptMainBean.getString(rs,"end_time").substring(3,5) ) ) ;
    int starttime = (Integer.parseInt(apptMainBean.getString(rs,"start_time").substring(0,2) ) )*60 + (Integer.parseInt(apptMainBean.getString(rs,"start_time").substring(3,5) ) ) ;
    everyMin = endtime - starttime +1;
  }
%>  
                  <INPUT TYPE="hidden" NAME="end_time" VALUE="<%=bFirstDisp?apptMainBean.getString(rs,"end_time").substring(0,5):request.getParameter("end_time")%>" WIDTH="25" HEIGHT="20" border="0" onChange="checkTimeTypeIn(this)">          
<%--              <INPUT TYPE="hidden" NAME="end_time" VALUE="<%=request.getParameter("end_time")%>" WIDTH="25" HEIGHT="20" border="0" onChange="checkTimeTypeIn(this)">--%>
              <INPUT TYPE="TEXT" NAME="duration" VALUE="<%=request.getParameter("duration")!=null?(request.getParameter("duration").equals(" ")||request.getParameter("duration").equals("")||request.getParameter("duration").equals("null")?(""+everyMin) :request.getParameter("duration")):(""+everyMin)%>" WIDTH="25" HEIGHT="20" border="0" hspace="2" >
            </td>
            <td width="5%"  ></td>
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="Arial, Helvetica, sans-serif"><bean:message key="Appointment.formChartNo"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT">
              <input type="TEXT" name="chart_no" readonly value="<%=bFirstDisp?chartno:request.getParameter("chart_no")%>" width="25" height="20" border="0" hspace="2">
            </td>
          </tr>
	<% if(providerBean.getProperty(doctorNo)!=null) {%>
          <tr valign="middle"> 
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right">&nbsp;</div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
              &nbsp; 
            </td>
            <td width="5%"  ></td>
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><bean:message key="Appointment.formDoctor"/>:</div>
            </td>
            <td width="20%"  ALIGN="LEFT">
		<%=providerBean.getProperty(doctorNo)%>
            </td>
          </tr>
          <% } %>
          <tr valign="middle"> 
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"> <a href="#" onclick="demographicdetail(550,700)" ><bean:message key="Appointment.formName"/></a> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
              <INPUT TYPE="TEXT" NAME="keyword"  tabindex="1" VALUE="<%=bFirstDisp?apptMainBean.getString(rs,"name"):request.getParameter("name")%>" HEIGHT="20" border="0" hspace="2" width="25" >
            </td>
            <td width="5%"  ></td>
            <td width="20%"  ALIGN="LEFT"> 
				      <INPUT TYPE="hidden" NAME="orderby" VALUE="last_name, first_name" >
				      <INPUT TYPE="hidden" NAME="search_mode" VALUE="search_name" >
				      <INPUT TYPE="hidden" NAME="originalpage" VALUE="../appointment/editappointment.jsp" >
				      <INPUT TYPE="hidden" NAME="limit1" VALUE="0" >
				      <INPUT TYPE="hidden" NAME="limit2" VALUE="5" >
				      <INPUT TYPE="hidden" NAME="ptstatus" VALUE="active">
              <!--input type="hidden" name="displaymode" value="Search " -->
              <div align="right"><input type="submit" onclick="document.forms['EDITAPPT'].displaymode.value='Search '" value="<bean:message key="appointment.editappointment.btnSearch"/>"></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
              <input type="TEXT" name="demographic_no" onFocus="onBlockFieldFocus(this)" readonly value="<%=bFirstDisp?( rs.getInt("demographic_no")==0?"":(""+rs.getInt("demographic_no")) ):request.getParameter("demographic_no")%>" width="25" height="20" border="0" hspace="2">
            </td>
          </tr>
          <tr valign="middle"> 
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"> <bean:message key="Appointment.formReason"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"><font face="Times New Roman">
              <textarea name="reason"  tabindex="2" rows="2"  wrap="virtual" cols="18"><%=bFirstDisp?apptMainBean.getString(rs,"reason"):request.getParameter("reason")%></textarea>
              </font> </TD>
            <td width="5%"  ><font face="Times New Roman"> </font></td>
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"><bean:message key="Appointment.formNotes"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"><font face="Times New Roman">
              <textarea name="notes" tabindex="3" rows="2" wrap="virtual" cols="18"><%=bFirstDisp?apptMainBean.getString(rs,"notes"):request.getParameter("notes")%></textarea>
              </font> </td>
          </tr>
          <tr valign="middle"> 
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"><bean:message key="Appointment.formLocation"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
            <% 
            OscarProperties props = OscarProperties.getInstance();
            boolean bMoreAddr = props.getProperty("scheduleSiteID", "").equals("") ? false : true;
            String loc = bFirstDisp?apptMainBean.getString(rs,"location"):request.getParameter("location").equals("")?"":request.getParameter("location");
            String colo = bMoreAddr? (new ApptOpt()).getColorFromLocation(props.getProperty("scheduleSiteID", ""), props.getProperty("scheduleSiteColor", ""),loc) : "white";
            %>
              <INPUT TYPE="TEXT" NAME="location"  tabindex="4" style="background-color: <%=colo%>" VALUE="<%=bFirstDisp?apptMainBean.getString(rs,"location"):request.getParameter("location")%>" WIDTH="25" HEIGHT="20" border="0" hspace="2">
            </td>
            <td width="5%"  ></td>
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"><bean:message key="Appointment.formResources"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
              <input type="TEXT" name="resources" tabindex="5"  value="<%=bFirstDisp?apptMainBean.getString(rs,"resources"):request.getParameter("resources")%>" width="25" height="20" border="0" hspace="2">
            </td>
          </tr>
          <tr valign="middle"> 
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"><bean:message key="Appointment.formLastCreator"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
<%
  String lastCreatorNo = bFirstDisp?apptMainBean.getString(rs,"creator"):request.getParameter("user_id");
%>            
              <INPUT TYPE="TEXT" NAME="user_id" VALUE="<%=lastCreatorNo%>" readonly WIDTH="25" HEIGHT="20" border="0" hspace="2">
            </td>
            <td width="5%"  ></td>
            <td width="20%"  ALIGN="LEFT"> 
              <div align="right"><font face="arial"><bean:message key="Appointment.formLastTime"/> :</font></div>
            </td>
            <td width="20%"  ALIGN="LEFT"> 
		<%                 
                 origDate =  rs != null ? rs.getString("createdatetime") : request.getParameter("createDate");
                 String lastDateTime = bFirstDisp?apptMainBean.getString(rs,"updatedatetime"):request.getParameter("updatedatetime");
                 if (lastDateTime == null){ lastDateTime = bFirstDisp?apptMainBean.getString(rs,"createdatetime"):request.getParameter("createdatetime"); }
		%>
<INPUT TYPE="TEXT" NAME="lastcreatedatetime" readonly VALUE="<%=bFirstDisp?lastDateTime:request.getParameter("lastcreatedatetime")%>" WIDTH="25" HEIGHT="20" border="0" hspace="2"> 
<%
      break;
    } //end while
  } //end if
  if(bFirstDisp) apptMainBean.closePstmtConn();
 
    // the cursor of ResultSet only goes through once from top
				GregorianCalendar now=new GregorianCalendar();
				String strDateTime=now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)+" "
					+	now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE)+":"+now.get(Calendar.SECOND);
%> 

              <INPUT TYPE="hidden" NAME="createdatetime" VALUE="<%=strDateTime%>">
              <INPUT TYPE="hidden" NAME="provider_no" VALUE="<%=curProvider_no%>">
              <INPUT TYPE="hidden" NAME="dboperation" VALUE="update_apptrecord">
              <INPUT TYPE="hidden" NAME="creator" VALUE="<%=userlastname+", "+userfirstname%>">
              <INPUT TYPE="hidden" NAME="remarks" VALUE="">
              <INPUT TYPE="hidden" NAME="appointment_no" VALUE="<%=request.getParameter("appointment_no")%>">
            </td>
          </tr>
          <tr>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td>&nbsp;</td>
              <td align="left"><div align="right"><font face="arial">Create Date :</font></div></td>
              <td><INPUT TYPE="TEXT" NAME="createDate" readonly VALUE="<%=origDate%>" WIDTH="25" HEIGHT="20" border="0" hspace="2"> </td>
          </tr>
        </table>
	</td></tr>
</table>

<table width="100%" BGCOLOR="<%=deepcolor%>">
<tr>
	<td align="left">
	<input type="submit" onclick="document.forms['EDITAPPT'].displaymode.value='Update Appt'; onButUpdate();" value="<bean:message key="appointment.editappointment.btnUpdateAppointment"/>">
	<input type="submit" onclick="document.forms['EDITAPPT'].displaymode.value='Group Action'; onButUpdate();" value="<bean:message key="appointment.editappointment.btnGroupAction"/>">
	<input type="submit" onclick="document.forms['EDITAPPT'].displaymode.value='Delete Appt'; onButDelete();" value = "<bean:message key="appointment.editappointment.btnDeleteAppointment"/>">
	<input type="button" name="buttoncancel" value = "<bean:message key="appointment.editappointment.btnCancelAppointment"/>" onClick="onButCancel();">
	<input type="button" name="buttoncancel" value = "<bean:message key="appointment.editappointment.btnNoShow"/>" onClick="window.location='appointmentcontrol.jsp?buttoncancel=No Show&displaymode=Update Appt&appointment_no=<%=request.getParameter("appointment_no")%>'">
	</td>
	<td align="right" nowrap> 
	<input type = "button" name="labelprint" value = "<bean:message key="appointment.editappointment.btnLabelPrint"/>" onClick="window.open('../demographic/demographiclabelprintsetting.jsp?demographic_no='+document.EDITAPPT.demographic_no.value, 'labelprint','height=550,width=700,location=no,scrollbars=yes,menubars=no,toolbars=no' )">
	<!--input type="button" name="Button" value="<bean:message key="global.btnExit"/>" onClick="self.close()"-->
	<input type="button" value="<bean:message key="appointment.addappointment.btnRepeat"/>" onclick="onButRepeat()">	
	</td>
</tr>
</table>

<table width="95%" align="center">
  <tr><td><bean:message key="Appointment.msgTelephone"/>: <%= phone%><br><bean:message key="Appointment.msgRosterStatus"/>: <%=rosterstatus%>
  </td>
<%
  if(alert!=null && !alert.equals("") ) {
%>
  <td bgcolor='yellow'><font color='red'><b><%=alert%></b></font></td>
<% } %>  
  </tr>
</table>
<hr/>
<table width="95%" align="center">
  <tr><td>
  <input type="submit" onclick="document.forms['EDITAPPT'].displaymode.value='Cut';" value="Cut"/> |
  <input type="submit" onclick="document.forms['EDITAPPT'].displaymode.value='Copy';" value="Copy"/>
  <% if(apptObj!=null) { %><a href=# onclick="pasteAppt();">Paste</a><% } %>
  </td>
  </tr>
</table>
</FORM>

</body>
</html:html>

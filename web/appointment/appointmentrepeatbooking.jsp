<% 
if(session.getAttribute("user") == null) response.sendRedirect("../logout.jsp");
String curProvider_no = request.getParameter("provider_no");
String deepcolor = "#CCCCFF", weakcolor = "#EEEEFF", tableTitle = "#99ccff";
boolean bEdit = request.getParameter("appointment_no") != null ? true : false;
%>

<%@ page import="java.util.*, oscar.*, oscar.util.*" errorPage="errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<jsp:useBean id="groupApptBean" class="oscar.AppointmentMainBean" scope="page" />
<jsp:useBean id="providerBean" class="java.util.Properties" scope="session" />
<%@ include file="../admin/dbconnection.jsp" %>
<% 
String [][] dbQueries=new String[][] { 
	{"search_groupprovider", "select p.last_name, p.first_name, p.provider_no from mygroup m, provider p where m.mygroup_no=? and m.provider_no=p.provider_no order by p.last_name"}, 
	{"add_appt", "insert into appointment (provider_no,appointment_date,start_time,end_time,name, notes,reason,location,resources,type, style,billing,status,createdatetime,creator, remarks, demographic_no) values(?,?,?,?,?, ?,?,?,?,?, ?,?,?,?,?, ?,?)"}, 
	{"delete", "delete from appointment where appointment_date=? and start_time=? and end_time=? and name=? and creator=?"}, 
	{"search_scheduledate_single", "select * from scheduledate where sdate=? and provider_no=?" }, 
	{"search_appt", "select * from appointment where appointment_no = ?" }, 
	{"delete_appt", "delete from appointment where appointment_no = ?" }, 
	//{"cancel_appt", "update appointment set status = ?, createdatetime = ?, creator = ? where appointment_no = ?" }, 
	{"cancel_appt", "update appointment set status = ?, updatedatetime = ?, creator = ? where appointment_no = ?" }, 
	//{"update_appt", "update appointment set demographic_no=?,appointment_date=?,start_time=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,createdatetime=?,creator=?,remarks=? where appointment_no=? " }, 
	{"update_appt", "update appointment set demographic_no=?,appointment_date=?,start_time=?,end_time=?,name=?, notes=?,reason =?,location=?, resources=?, type=?,style=?,billing =?,status=?,updatedatetime=?,creator=?,remarks=? where appointment_no=? " }, 
        {"search_otherappt", "select * from appointment where appointment_date=? and ((start_time <= ? and end_time >= ?) or (start_time > ? and start_time < ?) ) order by provider_no, start_time" }, 
};
groupApptBean.doConfigure(dbParams,dbQueries);
%>

<%
  if (request.getParameter("groupappt")!=null) {
    boolean bSucc = false;
    if (request.getParameter("groupappt")!=null && request.getParameter("groupappt").equals("Add Group Appointment") ) {
        String[] param =new String[16];
        int rowsAffected=0, datano=0;
        StringBuffer strbuf=new StringBuffer();
		String createdDateTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(),"yyyy-MM-dd HH:mm:ss");
		String userName =  (String) session.getAttribute("userlastname") + ", " + (String) session.getAttribute("userfirstname");

		String everyNum = request.getParameter("everyNum")!=null? request.getParameter("everyNum") : "0";
		String everyUnit = request.getParameter("everyUnit")!=null? request.getParameter("everyUnit") : "day";
		String endDate = request.getParameter("endDate")!=null? request.getParameter("endDate") : UtilDateUtilities.DateToString(UtilDateUtilities.now(),"dd/MM/yyyy");
		int delta = Integer.parseInt(everyNum);
		if (everyUnit.equals("week") ) {
			delta = delta*7;
			everyUnit = "day";
		}
		GregorianCalendar gCalDate = new GregorianCalendar();
		GregorianCalendar gEndDate = (GregorianCalendar) gCalDate.clone();
		gEndDate.setTime(UtilDateUtilities.StringToDate(endDate, "dd/MM/yyyy"));

		int[] intparam=new int [1];
        //for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	        //strbuf=new StringBuffer(e.nextElement().toString());
            //if (strbuf.toString().indexOf("one")==-1 && strbuf.toString().indexOf("two")==-1) continue;
          
		    //datano=Integer.parseInt(request.getParameter(strbuf.toString()) );
     	    param[0]=request.getParameter("provider_no");
	        param[1]=request.getParameter("appointment_date");
    	    param[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
	        param[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
            param[4] = request.getParameter("keyword");
	        param[5]=request.getParameter("notes");
	        param[6]=request.getParameter("reason");
    	    param[7]=request.getParameter("location");
	        param[8]=request.getParameter("resources");
	        param[9]=request.getParameter("type");
    	    param[10]=request.getParameter("style");
	        param[11]=request.getParameter("billing");
	        param[12]=request.getParameter("status");
     	    param[13]=createdDateTime;   //request.getParameter("createdatetime");
	        param[14]=userName;  //request.getParameter("creator");
    	    param[15]=request.getParameter("remarks");
	        if (!(request.getParameter("demographic_no").equals(""))) {
				intparam[0]= Integer.parseInt(request.getParameter("demographic_no"));
     	    } else intparam[0]=0;

        //}
		// repeat adding
		while (true) {

			rowsAffected = groupApptBean.queryExecuteUpdate(param,intparam,"add_appt");
			gCalDate.setTime(UtilDateUtilities.StringToDate(param[1], "yyyy-MM-dd"));
			if (everyUnit.equals("day")) {
				gCalDate.add(Calendar.DATE, delta);
			} else if (everyUnit.equals("month")) {
				gCalDate.add(Calendar.MONTH, delta);
			} else if (everyUnit.equals("year")) {
				gCalDate.add(Calendar.YEAR, delta);
			} 

			if (gCalDate.after(gEndDate)) break;
			else param[1] = UtilDateUtilities.DateToString(gCalDate.getTime(), "yyyy-MM-dd");

		}
        if (rowsAffected == 1) bSucc = true;

	}


    if (request.getParameter("groupappt")!=null && (request.getParameter("groupappt").equals("Group Update") 
		    || request.getParameter("groupappt").equals("Group Cancel") || request.getParameter("groupappt").equals("Group Delete"))) {
        int rowsAffected=0, datano=0;
        StringBuffer strbuf=new StringBuffer();
		String createdDateTime = UtilDateUtilities.DateToString(UtilDateUtilities.now(),"yyyy-MM-dd HH:mm:ss");
		String userName =  (String) session.getAttribute("userlastname") + ", " + (String) session.getAttribute("userfirstname");

		for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	        strbuf=new StringBuffer(e.nextElement().toString());
            if (strbuf.toString().indexOf("one")==-1 && strbuf.toString().indexOf("two")==-1) continue;
 		    datano=Integer.parseInt(request.getParameter(strbuf.toString()) );

            if (request.getParameter("groupappt").equals("Group Cancel")) {
                String[] paramc =new String[4];
	            paramc[0]="C";
	            paramc[1]=createdDateTime;
     	        paramc[2]=userName;   //request.getParameter("createdatetime");
	            paramc[3]=request.getParameter("appointment_no" + datano);  //request.getParameter("creator");

                rowsAffected = groupApptBean.queryExecuteUpdate(paramc , "cancel_appt");
			}

			//can find and save them to recyclebin first
		    //delete the selected appts
            if (request.getParameter("groupappt").equals("Group Delete")) {
                rowsAffected = groupApptBean.queryExecuteUpdate(request.getParameter("appointment_no" + datano) , "delete_appt");
			}

			if (request.getParameter("groupappt").equals("Group Update")) {
                rowsAffected = groupApptBean.queryExecuteUpdate(request.getParameter("appointment_no" + datano) , "delete_appt");
     	        
                String[] paramu =new String[16];
				paramu[0]=request.getParameter("provider_no"+datano);
				paramu[1]=request.getParameter("appointment_date");
	    	    paramu[2]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("start_time"));
		        paramu[3]=MyDateFormat.getTimeXX_XX_XX(request.getParameter("end_time"));
			    paramu[4] = request.getParameter("keyword");
				paramu[5]=request.getParameter("notes");
		        paramu[6]=request.getParameter("reason");
			    paramu[7]=request.getParameter("location");
			    paramu[8]=request.getParameter("resources");
				paramu[9]=request.getParameter("type");
	    	    paramu[10]=request.getParameter("style");
		        paramu[11]=request.getParameter("billing");
			    paramu[12]=request.getParameter("status");
     			paramu[13]=createdDateTime;   //request.getParameter("createdatetime");
		        paramu[14]=userName;  //request.getParameter("creator");
			    paramu[15]=request.getParameter("remarks");
			    int[] intparam=new int [1];
				if (!(request.getParameter("demographic_no").equals("")) && strbuf.toString().indexOf("one") != -1) {
					intparam[0]= Integer.parseInt(request.getParameter("demographic_no"));
		 	    } else intparam[0]=0;
			    
				rowsAffected = groupApptBean.queryExecuteUpdate(paramu,intparam,"add_appt");
	            if (rowsAffected != 1) break;
			}
		}
        if (rowsAffected == 1) bSucc = true;
	}
%>
<%   
    if (bSucc) {
%>
<h1><bean:message key="appointment.appointmentgrouprecords.msgAddSuccess"/></h1>
<script LANGUAGE="JavaScript">
self.close();
self.opener.refresh();
</script>
<%
        }  else {
%>
  <p><h1><bean:message key="appointment.appointmentgrouprecords.msgAddFailure"/></h1></p>
<%  
    }
    groupApptBean.closePstmtConn();
    return;
  }
%>
<%--TODO: Check to see if HTML output is correct--%>
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
<html:html locale="true">
<head>
<title><bean:message key="appointment.appointmentgrouprecords.title"/></title>
<meta http-equiv="Expires" content="Monday, 8 Aug 88 18:18:18 GMT">
<meta http-equiv="Cache-Control" content="no-cache">
<script language="JavaScript">
<!--
function setfocus() {
    this.focus();
}
function onCheck(a, b) {
    if (a.checked) {
		document.getElementById("everyUnit").value = b;
		//document.groupappt.everyUnit.value = b;
    }
}
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=250,left=150";//360,680
  var popup=window.open(page, "repeat", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}

function onExit() {
    if (confirm("<bean:message key="appointment.appointmentgrouprecords.msgExitConfirmation"/>")) {
        window.close()
	}
}

var saveTemp=0;
function onButDelete() {
  saveTemp=1;
}
function onSub() {
  if( saveTemp==1 ) {
    return (confirm("<bean:message key="appointment.appointmentgrouprecords.msgDeleteConfirmation"/>")) ; 
  } 
}
//-->
</SCRIPT>
  <!-- calendar stylesheet -->
  <link rel="stylesheet" type="text/css" media="all" href="../share/calendar/calendar.css" title="win2k-cold-1" />

  <!-- main calendar program -->
  <script type="text/javascript" src="../share/calendar/calendar.js"></script>

  <!-- language for the calendar -->
  <script type="text/javascript" src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>

  <!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
  <script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
</head>

<body  bgcolor="ivory" onLoad="setfocus()"  topmargin="0" leftmargin="0" rightmargin="0">
<form name="groupappt" method="POST" action="appointmentrepeatbooking.jsp" onSubmit="return ( onSub());">
<INPUT TYPE="hidden" NAME="groupappt" value="">
<table width="100%" BGCOLOR="silver">
  <tr><TD>
<%    if (bEdit) {    %>
  <INPUT TYPE="button" onclick="document.forms['groupappt'].groupappt.value='Group Update'; document.forms['groupappt'].submit();" VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupUpdate"/>" >
  <INPUT TYPE="button" onclick="document.forms['groupappt'].groupappt.value='Group Cancel'; document.forms['groupappt'].submit();" VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupCancel"/>" >
  <INPUT TYPE="button" onclick="document.forms['groupappt'].groupappt.value='Group Delete'; document.forms['groupappt'].submit();" VALUE="<bean:message key="appointment.appointmentgrouprecords.btnGroupDelete"/>" onClick="onButDelete()">
<%    } else {    %>
  <INPUT TYPE="button" onclick="document.forms['groupappt'].groupappt.value='Add Group Appointment'; document.forms['groupappt'].submit();" VALUE="<bean:message key="appointment.appointmentgrouprecords.btnAddGroupAppt"/>" >
<%    }    %>
  </TD>
      <TD align="right"><INPUT TYPE = "button" VALUE = " <bean:message key="global.btnBack"/> " onClick="window.history.go(-1);return false;"> <INPUT TYPE = "button" VALUE = " <bean:message key="global.btnExit"/> " onClick="onExit()"></TD>
  </tr>
</table>

<table border=0 cellspacing=0 cellpadding=0 width="100%" >
  <tr bgcolor="<%=deepcolor%>"><th><font face="Helvetica">Repeat Booking</font></th></tr>
</table>

<table border="0" cellspacing="1" cellpadding="2" width="100%" >
<tr><td width="20%"></td><td nowrap>How often?</td></tr>
<tr><td></td><td nowrap>&nbsp;&nbsp;&nbsp;
    <input type="radio" name="dateUnit" value="day"  <%="checked"%> onclick='onCheck(this, "day")'>
	Day &nbsp;&nbsp;
    <input type="radio" name="dateUnit" value="week"  <%=""%> onclick='onCheck(this, "week")'>
	Week &nbsp;&nbsp;
    <input type="radio" name="dateUnit" value="month"  <%=""%> onclick='onCheck(this, "month")'>
	Month &nbsp;&nbsp;
    <input type="radio" name="dateUnit" value="year"  <%=""%> onclick='onCheck(this, "year")'>
	Year 
</td></tr>
</table>

<table border="0" cellspacing="1" cellpadding="2" width="100%" >
<tr><td width="20%"></td><td width="16%" nowrap>Every </td>
	<td nowrap>
	<select name="everyNum">
<%	
for (int i = 1; i < 12; i++) {
%>
		<option value="<%=i%>"><%=i%></option>
<%
}
%> 
	</select>
	<input type="text" name="everyUnit" id="everyUnit" size="10" value="<%="day"%>" readonly>
	</td>
</tr><tr>
	<td></td>
	<td>End on &nbsp;&nbsp;<button type="button" id="f_trigger_b">...</button><br>
	<font size="-1">(dd/mm/yyyy)</font>
	</td>
	<td nowrap valign="top">
	<input type="text" id="endDate" name="endDate" size="10" value="<%=UtilDateUtilities.DateToString(UtilDateUtilities.now(),"dd/MM/yyyy")%>" readonly>
	</td>
</tr>
</table>
<%
String temp = null;
for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	temp=e.nextElement().toString();
	if(temp.equals("dboperation") ||temp.equals("displaymode") ||temp.equals("search_mode") ||temp.equals("chart_no")) continue;
	out.println("<input type='hidden' name='"+temp+"' value=\"" + UtilMisc.htmlEscape(request.getParameter(temp)) + "\">");
}
%> 
</form>

<script type="text/javascript">
    Calendar.setup({
        inputField     :    "endDate",      // id of the input field
        ifFormat       :    "%d/%m/%Y",       // format of the input field
        showsTime      :    false,            // will display a time selector
        button         :    "f_trigger_b",   // trigger for the calendar (button ID)
        singleClick    :    true,           // double-click mode
        step           :    1                // show all years in drop-down boxes (instead of every other year as default)
    });
</script>



</body>
</html:html>

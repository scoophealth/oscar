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
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
  String user_no;
  user_no = (String) session.getAttribute("user");
  int  nItems=0;
     String strLimit1="0";
    String strLimit2="5";
    if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
 boolean bFirstDisp=true; //this is the first time to display the window
  if (request.getParameter("bFirstDisp")!=null) bFirstDisp= (request.getParameter("bFirstDisp")).equals("true");
 String ChartNo;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*" %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />

<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  
  
  %><% //String providerview=request.getParameter("provider")==null?"":request.getParameter("provider");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay):request.getParameter("xml_appointment_date");
%>
<html>
<head>
<title>oscarTickler</title>
<link rel="stylesheet" href="../billing/billing.css" >
<style type="text/css">
<!--
.bodytext
{
  font-family: Arial, Helvetica, sans-serif;
  font-size: 14px;
  font-style: bold;
  line-height: normal;
  font-weight: normal;
  font-variant: normal;
  text-transform: none;
  color: #FFFFFF;
  text-decoration: none;
}
-->
</style>
      <meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
      <meta http-equiv="Pragma" content="no-cache">
      <script language="JavaScript">
<!--
function popupPage(vheight,vwidth,varpage) { //open a new popup window
  var page = "" + varpage;
  windowprops = "height="+vheight+",width="+vwidth+",location=no,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
  var popup=window.open(page, "attachment", windowprops);
  if (popup != null) {
    if (popup.opener == null) {
      popup.opener = self; 
    }
  }
}
function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function openBrWindow(theURL,winName,features) { 
  window.open(theURL,winName,features);
}
function setfocus() {
  this.focus();
  document.ADDAPPT.keyword.focus();
  document.ADDAPPT.keyword.select();
}

function validate(form){
if (validateDemoNo(form)){
form.action = "dbTicklerAdd.jsp"
form.submit()}

else{}
}
function validateDemoNo() {
  if (document.serviceform.demographic_no.value == "") {
alert("Invalid demographic information, please verify!");
	return false;
 }
 else{  if (document.serviceform.xml_appointment_date.value == "") {
alert("Missing service date, please verify!");
	return false;
 }
 else{
 return true;
}
 }


}
function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}





function DateAdd(startDate, numDays, numMonths, numYears)
{
	var returnDate = new Date(startDate.getTime());
	var yearsToAdd = numYears;
	
	var month = returnDate.getMonth()	+ numMonths;
	if (month > 11)
	{
		yearsToAdd = Math.floor((month+1)/12);
		month -= 12*yearsToAdd;
		yearsToAdd += numYears;
	}
	returnDate.setMonth(month);
	returnDate.setFullYear(returnDate.getFullYear()	+ yearsToAdd);
	
	returnDate.setTime(returnDate.getTime()+60000*60*24*numDays);
	
	return returnDate;

}

function YearAdd(startDate, numYears)
{
		return DateAdd(startDate,0,0,numYears);
}

function MonthAdd(startDate, numMonths)
{
		return DateAdd(startDate,0,numMonths,0);
}

function DayAdd(startDate, numDays)
{
		return DateAdd(startDate,numDays,0,0);
}


function addMonth(no)
{       var gCurrentDate = new Date();
	var newDate = DateAdd(gCurrentDate, 0, no,0 );
var newYear = newDate.getFullYear() 
var newMonth = newDate.getMonth()+1;
var newDay = newDate.getDate();
var newD = newYear + "-" + newMonth + "-" + newDay;
	document.serviceform.xml_appointment_date.value = newD;
}









//-->
</script>


</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0" topmargin="10" onLoad="setfocus()">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr bgcolor="#000000"> 
    <td height="40" width="10%"> </td>
    <td width="90%" align="left"> 
      <p><font face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><font face="Arial, Helvetica, sans-serif" size="4">oscar<font size="3">Tickler</font></font></b></font> 
      </p>
    </td>
  </tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0"bgcolor="#EEEEFF">
 <form name="ADDAPPT" method="post" action="../appointment/appointmentcontrol.jsp">
<tr> 
      <td width="35%"><font color="#003366"><font face="Verdana, Arial, Helvetica, sans-serif" size="2"><b>Demographic 
        Name: </b></font></font></td>
      <td colspan="2" width="65%">
<div align="left"><INPUT TYPE="TEXT" NAME="keyword" size="25" VALUE="<%=bFirstDisp?"":request.getParameter("name").equals("")?session.getAttribute("appointmentname"):request.getParameter("name")%>">
   	 <input type="submit" name="Submit" value="Search">    
  </div>
</td>
    </tr>
  <INPUT TYPE="hidden" NAME="orderby" VALUE="last_name" >
				      <INPUT TYPE="hidden" NAME="search_mode" VALUE="search_name" >
				      <INPUT TYPE="hidden" NAME="originalpage" VALUE="../tickler/ticklerAdd.jsp" >
				      <INPUT TYPE="hidden" NAME="limit1" VALUE="0" >
				      <INPUT TYPE="hidden" NAME="limit2" VALUE="5" >
              <!--input type="hidden" name="displaymode" value="TicklerSearch" -->
              <INPUT TYPE="hidden" NAME="displaymode" VALUE="Search "> 

<% ChartNo = bFirstDisp?"":request.getParameter("chart_no")==null?"":request.getParameter("chart_no"); %>
   <INPUT TYPE="hidden" NAME="appointment_date" VALUE="2002-10-01" WIDTH="25" HEIGHT="20" border="0" hspace="2">
       <INPUT TYPE="hidden" NAME="status" VALUE="t"  WIDTH="25" HEIGHT="20" border="0" hspace="2">
              <INPUT TYPE="hidden" NAME="start_time" VALUE="10:45" WIDTH="25" HEIGHT="20" border="0"  onChange="checkTimeTypeIn(this)">
              <INPUT TYPE="hidden" NAME="type" VALUE="" WIDTH="25" HEIGHT="20" border="0" hspace="2">
              <INPUT TYPE="hidden" NAME="duration" VALUE="15" WIDTH="25" HEIGHT="20" border="0" hspace="2" >
              <INPUT TYPE="hidden" NAME="end_time" VALUE="10:59" WIDTH="25" HEIGHT="20" border="0" hspace="2"  onChange="checkTimeTypeIn(this)">
       

 <input type="hidden" name="demographic_no"  readonly value="" width="25" height="20" border="0" hspace="2">
         <input type="hidden" name="location"  tabindex="4" value="" width="25" height="20" border="0" hspace="2">
              <input type="hidden" name="resources"  tabindex="5" value="" width="25" height="20" border="0" hspace="2">
              <INPUT TYPE="hidden" NAME="user_id" readonly VALUE='oscardoc, doctor' WIDTH="25" HEIGHT="20" border="0" hspace="2">
     	        <INPUT TYPE="hidden" NAME="dboperation" VALUE="add_apptrecord">
              <INPUT TYPE="hidden" NAME="createdatetime" readonly VALUE="2002-10-1 17:53:50" WIDTH="25" HEIGHT="20" border="0" hspace="2">
              <INPUT TYPE="hidden" NAME="provider_no" VALUE="115">
              <INPUT TYPE="hidden" NAME="creator" VALUE="oscardoc, doctor">
              <INPUT TYPE="hidden" NAME="remarks" VALUE="">
 </form>
</table>
<table width="100%" border="0" bgcolor="#EEEEFF">
  <form name="serviceform" method="post" >
     <tr> 
      <td width="35%"> <div align="left"><font color="#003366"><font face="Verdana, Arial, Helvetica, sans-serif" size="2"><strong>Chart 
          No:</strong> </font></font></div></td>
      <td colspan="2"> <div align="left"><INPUT TYPE="hidden" NAME="demographic_no" VALUE="<%=bFirstDisp?"":request.getParameter("demographic_no").equals("")?"":request.getParameter("demographic_no")%>"><%=bFirstDisp?"":request.getParameter("chart_no").equals("")?"":request.getParameter("chart_no")%></div></td>
    </tr>

    <tr> 
      <td><font color="#003366" size="2" face="Verdana, Arial, Helvetica, sans-serif"><strong>Service 
        Date:</strong></font></td>
      <td><input type="text" name="xml_appointment_date" value="<%=xml_appointment_date%>"> 
        <font color="#003366" size="1" face="Verdana, Arial, Helvetica, sans-serif"><a href="#" onClick="openBrWindow('../billing/billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">Calendar 
        Lookup</a> &nbsp; &nbsp; <a href="#" onClick="addMonth(6)">6-month</a>&nbsp; &nbsp;<a href="#" onClick="addMonth(12)">1-year</a></font> </td>
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td height="21" valign="top"><font color="#003366" size="2" face="Verdana, Arial, Helvetica, sans-serif"><strong>Reminder 
        Message:</strong></font></td>
      <td valign="top"> <textarea style="font-face:Verdana, Arial, Helvetica, sans-serif"name="textarea" cols="50" rows="10"></textarea></td>
      <td>&nbsp;</td>
    </tr>
 
     <INPUT TYPE="hidden" NAME="user_no" VALUE="<%=user_no%>">
    <tr> 
      <td><input type="button" name="Button" value="Cancel and EXIT" onClick="window.close()"></td>
      <td><input type="button" name="Button" value="Submit and EXIT" onClick="validate(this.form)"></td>
      <td></td>
	  </tr>
  </form>
</table>
<p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
  <p>&nbsp; </p>
<%@ include file="../demographic/zfooterbackclose.jsp" %> 

</body>
</html>

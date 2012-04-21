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

<% 
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
  String user_no="";
user_no = (String) session.getAttribute("user");
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbBilling.jspf"%>
<% 	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
  String monthCode = "";
  if (curMonth == 1) monthCode = "A";
    if (curMonth == 2) monthCode = "B";
      if (curMonth == 3) monthCode = "C";
        if (curMonth == 4) monthCode = "D";
          if (curMonth == 5) monthCode = "E";
            if (curMonth == 6) monthCode = "F";
              if (curMonth == 7) monthCode = "G";
                if (curMonth == 8) monthCode = "H";
                  if (curMonth == 9) monthCode = "I";
                    if (curMonth == 10) monthCode = "J";
                      if (curMonth == 11) monthCode = "K";
                        if (curMonth == 12) monthCode = "L";
                        
  %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
<script language="JavaScript">
<!--
function openBrWindow(theURL,winName,features) {
  window.open(theURL,winName,features);
}

//-->
</script>
<style type='text/css'>
<!--
.bodytext {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 12px;
	font-style: normal;
	line-height: normal;
	font-weight: normal;
	font-variant: normal;
	text-transform: none;
	color: #003366;
	text-decoration: none;
}
-->
</style>

</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="0" onLoad="setfocus()">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF"> OHIP Report Simulation</font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>

<% String providerview=request.getParameter("provider")==null?"":request.getParameter("provider");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>
<table width="100%" border="0" bgcolor="#E6F0F7">
	<form name="serviceform" method="post" action="genSimulation.jsp">
	<tr>
		<td width="220"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Select provider </font></b></td>
		<td width="254"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"> <select name="provider">
			<option value="000000"
				<%=providerview.equals("000000")?"selected":""%>><b>Select
			Provider</b></option>
			<% String proFirst="";
           String proLast="";
           String proOHIP="";
           String specialty_code; 
String billinggroup_no;
           int Count = 0;
        ResultSet rslocal;
        rslocal = null;
 rslocal = apptMainBean.queryResults("%", "search_provider_dt");
 while(rslocal.next()){
 proFirst = rslocal.getString("first_name");
 proLast = rslocal.getString("last_name");
 proOHIP = rslocal.getString("ohip_no"); 
 billinggroup_no= SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
 specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");


 %>
			<option value="<%=proOHIP%>,<%=specialty_code%>|<%=billinggroup_no%>"
				<%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<% 

 }
//
  %>
		</select> </font></b></td>
		<td width="254"><font color="#003366"><b><font
			face="Arial, Helvetica, sans-serif" size="2">Bill center: <input
			type="hidden" name="billcenter" value="G"> </font></b><font
			face="Arial, Helvetica, sans-serif" size="2">Hamilton </font></font></td>
		<td width="277"><font color="#003366"> <input
			type="hidden" name="monthCode" value="<%=monthCode%>"> <input
			type="hidden" name="verCode" value="V03"> <input
			type="hidden" name="curUser" value="<%=user_no%>"> <input
			type="hidden" name="curDate" value="<%=nowDate%>"> </font></td>
	</tr>
	<tr>
		<td><font color="#003366"><font
			face="Arial, Helvetica, sans-serif" size="2"><b> Service
		Date-Range: </b></font></font></td>
		<td><font size="1" face="Arial, Helvetica, sans-serif"><a
			href="#"
			onClick="openBrWindow('billingCalendarPopup.jsp?type=admission&year=<%=curYear%>&month=<%=curMonth%>','','top=0,left=0,width=300,height=300')">Begin:</a></font>
		<input type="text" name="xml_vdate" value="<%=xml_vdate%>"></td>
		<td><font size="1" face="Arial, Helvetica, sans-serif"><a
			href="#"
			onClick="openBrWindow('billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','top=0,left=0,width=300,height=300')">End:</a>
		</font> <input type="text" name="xml_appointment_date"
			value="<%=xml_appointment_date%>"></td>
		<td><font color="#003366"> <input type="submit"
			name="Submit" value="Create Report"> </font></td>
	</tr>
	</form>
</table>
<%=request.getAttribute("html") == null?"":request.getAttribute("html")%>
</body>
</html>

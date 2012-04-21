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
  String user_no;
  user_no = (String) session.getAttribute("user");
  int  nItems=0;
     String strLimit1="0";
    String strLimit2="5";
    if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
%>
<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*"
	errorPage="errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbBilling.jspf"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
<link rel="stylesheet" href="../web.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!--

function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}

//-->
</script>


</head>

<body bgcolor="#FFFFFF" text="#000000">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">Generate
		Billing Report by Appointment</font></th>
	</tr>
</table>
<table width="100%" border="0" cellspacing="1" cellpadding="1"
	bgcolor="#0066CC" bordercolor="#0066CC">
	<tr>
		<td bgcolor="#003366" class="bodytext">
		<div align="center"><a
			href="billingreport.jsp?displaymode=billreport&providerview=<%=providerview%>">Generate
		Billing Report</a></div>
		</td>
		<td bgcolor="#CCCCCC" class="bodytext">
		<div align="center"><a
			href="billingAppointment.jsp?displaymode=billreport&providerview=<%=providerview%>">Search
		Unbilled Appointment</a></div>
		</td>
	</tr>
</table>


<table width="100%" border="0" bgcolor="#003366">
	<form name="form1" method="post" action="genBillingReport.jsp">
	<tr>
		<td width="30%" align="right"><b><font
			face="Arial, Helvetica, sans-serif" size="2" color="#FFFFFF">Select
		provider </font></b></td>
		<td width="50%"><font face="Arial, Helvetica, sans-serif"
			size="2"> <select name="provider_no">
			<% String proFirst="";
           String proLast="";
           String proOHIP="";
           String specialty_code; 
String billinggroup_no;
           int Count = 0;
        ResultSet rslocal;
        rslocal = null;
 rslocal = apptMainBean.queryResults("%", "search_provider_all_dt");
 while(rslocal.next()){
 proFirst = rslocal.getString("first_name");
 proLast = rslocal.getString("last_name");
 proOHIP = rslocal.getString("provider_no"); 
 billinggroup_no= SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
 specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");

%>
			<option value="<%=proOHIP%>"
				<%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<%
      }      
  
  %>

		</select> </font></td>

		<td width="20%"><input type="hidden" name="verCode" value="V03">
		<input type="submit" name="Submit" value="Create Report"></td>

	</tr>
	</form>
</table>
<p><font face="Arial, Helvetica, sans-serif" size="2"> </font></p>
<p>&nbsp;</p>
<%@ include file="../demographic/zfooterbackclose.jsp"%>

</body>
</html>

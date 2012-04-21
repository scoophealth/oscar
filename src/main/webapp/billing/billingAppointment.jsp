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
    String providerview = request.getParameter("providerview")==null?"":request.getParameter("providerview") ;
%>
<%@ page
	import="java.util.*, java.sql.*, oscar.*, java.net.*, oscar.MyDateFormat"
	errorPage="errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  
  
  %>
<% //String providerview=request.getParameter("provider")==null?"":request.getParameter("provider");
   String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
   String xml_appointment_date = request.getParameter("xml_appointment_date")==null?"":request.getParameter("xml_appointment_date");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
<link rel="stylesheet" href="../web.css">
<script language="JavaScript">
<!--

function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}


function openBrWindow(theURL,winName,features) { 
  window.open(theURL,winName,features);
}

function refresh() {
  var u = self.location.href;
  if(u.lastIndexOf("view=1") > 0) {
    self.location.href = u.substring(0,u.lastIndexOf("view=1")) + "view=0" + u.substring(eval(u.lastIndexOf("view=1")+6));
  } else {
    history.go(0);
  }
}


//-->
</script>
<style type='text/css'>
<!--
.bodytext {
	font-family: Arial, Helvetica, sans-serif;
	font-size: 10px;
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
<% String provider="", proFirst1="", proLast1="";
	provider = request.getParameter("provider");
	int flag = 0, rowCount=0;
   if (provider == null || provider.compareTo("") == 0) {
   flag =0;
   } else {
   flag =1;
   if (providerview.compareTo(provider) != 0) providerview=provider;
    ResultSet rslocal1;
        rslocal1 = null;
 rslocal1 = apptMainBean.queryResults(provider, "search_provider_name");
 while(rslocal1.next()){
 proFirst1 = rslocal1.getString("first_name");
 proLast1 = rslocal1.getString("last_name");
}

   }
   %>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP width="11%">
		<div align="left"><font face="Helvetica" color="#FFFFFF">
		<input type="button" value="Done" onClick="window.close()"
			name="button2"> </font></div>
		</th>
		<th align=CENTER NOWRAP width="81%">
		<div align="center"><font face="Helvetica" color="#FFFFFF">Search
		UNBILLED Appointment </font></div>
		</th>
		<th align=CENTER NOWRAP width="8%">
		<div align="right"><font face="Helvetica" color="#FFFFFF">
		<input type="button" value="Print Report" onClick="window.print()"
			name="button"> </font></div>
		</th>
	</tr>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="1"
	bgcolor="#0066CC" bordercolor="#0066CC">
	<tr>
		<td bgcolor="#CCCCCC" class="bodytext">
		<div align="center"><a
			href="billingreport.jsp?displaymode=billreport&providerview=<%=providerview%>">Generate
		Billing Report</a></div>
		</td>
		<td bgcolor="#003366" class="bodytext">
		<div align="center"><a
			href="billingAppointment.jsp?displaymode=billreport&providerview=<%=providerview%>">Search
		Unbilled Appointment</a></div>
		</td>
	</tr>
</table>



<table width="100%" border="0" bgcolor="#003366">
	<form name="serviceform" method="get">
	<tr>
		<td width="19%" align="right">
		<div align="left"><b><font
			face="Arial, Helvetica, sans-serif" size="2" color="#FFFFFF">Select
		provider </font></b></div>
		</td>
		<td width="41%"><font face="Arial, Helvetica, sans-serif"
			size="2"> <select name="provider">
			<% String proFirst="";
           String proLast="";
           String proOHIP="";
           int Count = 0;
        ResultSet rslocal;
        rslocal = null;
 rslocal = apptMainBean.queryResults("%", "search_provider_all_dt");
 while(rslocal.next()){
 proFirst = rslocal.getString("first_name");
 proLast = rslocal.getString("last_name");
 proOHIP = rslocal.getString("provider_no"); 
  

 %>
			<option value="<%=proOHIP%>"
				<%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<% }

 

  %>
		</select> </font></td>
		<td width="40%"><input type="submit" name="Submit"
			value="Create Report"></td>
	</tr>
	<tr>
		<td width="19%">
		<div align="left"><font color="#003366"><font
			face="Arial, Helvetica, sans-serif" size="2"><b> <font
			color="#FFFFFF">Service Date-Range</font></b></font></font></div>
		</td>
		<td width="41%"><input type="text" name="xml_vdate"
			value="<%=xml_vdate%>"> <font size="1"
			face="Arial, Helvetica, sans-serif"><a href="#"
			onClick="openBrWindow('billingCalendarPopup.jsp?type=admission&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">Begin:</a></font>
		</td>
		<td width="40%"><input type="text" name="xml_appointment_date"
			value="<%=xml_appointment_date%>"> <font size="1"
			face="Arial, Helvetica, sans-serif"><a href="#"
			onClick="openBrWindow('billingCalendarPopup.jsp?type=end&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">End:</a></font>
		</td>
	</tr>
	</form>
</table>
<% if (flag == 1) { %>

<table width="100%" border="2" bgcolor="#ffffff" valign="top">
	<%
 String dateBegin = request.getParameter("xml_vdate");
   String dateEnd = request.getParameter("xml_appointment_date");
   if (dateEnd.compareTo("") == 0) dateEnd = MyDateFormat.getMysqlStandardDate(curYear, curMonth, curDay);
   if (dateBegin.compareTo("") == 0) dateBegin="1950-01-01"; // set to any early time to start search from beginning
 ResultSet rs=null ;
  String[] param =new String[3];
  param[0] = request.getParameter("provider");
 param[1] = dateBegin;
  param[2] = dateEnd;
  rs = apptMainBean.queryResults(param, "search_unbill_history_daterange");

  boolean bodd=false;
   nItems=0;
  String apptNo="", demoNo = "", demoName="", userno="", apptDate="", apptTime="", reason="";
  if(rs==null) {
    out.println("failed!!!"); 
  } else {
  %>
	<tr bgcolor="#339999" class="bodytext">
		<TH align="center" width="20%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">APPOINTMENT DATE</font></b></TH>
		<TH align="center" width="10%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">TIME</font></b></TH>
		<TH align="center" width="10%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">PATIENT</font></b></TH>
		<TH align="center" width="20%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">PROVIDER</font></b></TH>
		<TH align="center" width="10%"><b><font size="2"
			face="Arial, Helvetica, sans-serif">COMMENTS</font></b></TH>
	</tr>
	<%
    while (rs.next()) {
     
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
      apptNo = rs.getString("appointment_no");
      demoNo = rs.getString("demographic_no");
      demoName = rs.getString("name");
      userno=rs.getString("provider_no");
      apptDate = rs.getString("appointment_date");
      apptTime = rs.getString("start_time");
      reason = rs.getString("reason");
      
%>
	<tr bgcolor="<%=bodd?"ivory":"white"%>" class="bodytext">
		<TD align="center" width="20%"><b><font size="2"
			face="Arial, Helvetica, sans-serif"><%=apptDate%></font></b></TD>
		<TD align="center" width="10%"><b><font size="2"
			face="Arial, Helvetica, sans-serif"><%=apptTime%></font></b></TD>
		<TD align="center" width="10%"><b><font size="2"
			face="Arial, Helvetica, sans-serif"><%=demoName%></font></b></TD>
		<TD align="center" width="20%"><b><font size="2"
			face="Arial, Helvetica, sans-serif"><%=proLast1%>, <%=proFirst1%></font></b></TD>
		<TD align="center" width="10%"><b> <font size="2"
			face="Arial, Helvetica, sans-serif"><a href=#
			onClick='popupPage(700,1000, "../billing/billingOB.jsp?billForm=MFP&hotclick=&appointment_no=<%=apptNo%>&demographic_name=<%=URLEncoder.encode(demoName)%>&demographic_no=<%=demoNo%>&user_no=<%=userno%>&apptProvider_no=<%=provider%>&appointment_date=<%=apptDate%>&start_time=<%=apptTime%>&bNewForm=1")'
			title="<%=reason%>">Bill |$|</a></font></b></TD>
	</tr>
	<%  rowCount = rowCount + 1;
    }
    if (rowCount == 0) {
    %>
	<tr bgcolor="<%=bodd?"ivory":"white"%>">
		<TD colspan="5" align="center"><b><font size="2"
			face="Arial, Helvetica, sans-serif">No unbill items</font></b></TD>
	</tr>
	<% }
}%>
</table>
<% }
%>


<%
  %>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%>
<a
	href="../billing/billingAppointment.jsp?provider=<%=provider%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last
Page</a>
|
<%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%>
<a
	href="../billing/billingAppointment.jsp?provider=<%=provider%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
Next Page</a>
<%
}
%>
<p><%@ include file="../demographic/zfooterbackclose.jsp"%>
</center>
</body>
</html>

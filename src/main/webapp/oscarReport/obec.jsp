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

<%@ page
	import="java.math.*, java.util.*, java.sql.*, oscar.*, oscar.util.DateUtils, java.net.*"
	errorPage="errorpage.jsp"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<link rel="stylesheet" type="text/css"
	href="../oscarEncounter/encounterStyles.css">
<%       
  if(session.getValue("user") == null)
    response.sendRedirect("../logout.jsp");
  String user_no; 
  user_no = (String) session.getAttribute("user");

%>
<%
  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  DateUtils dateUtils = new DateUtils();
  String tomorrowDate = dateUtils.NextDay(curDay, curMonth, curYear);
  String clinic="";
  Properties proppies = oscar.OscarProperties.getInstance(); 
  String homepath = proppies.getProperty("DOCUMENT_DIR");
  session.setAttribute("obecdownload", homepath);
  
  
  %>
<% 
    int flag = 0, rowCount=0;
    String obectxt=(String) request.getAttribute("obectxt") == null?"":(String)request.getAttribute("obectxt");
    String xml_vdate=request.getParameter("xml_vdate") == null?tomorrowDate:request.getParameter("xml_vdate");
    String numDays = request.getParameter("numDays")==null?"4":request.getParameter("numDays");
%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>oscarReport - OBEC Report</title>
<link rel="stylesheet" href="oscarReport.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
<script language="JavaScript">
<!-- 

function selectprovider(s) {
  if(self.location.href.lastIndexOf("&providerview=") > 0 ) a = self.location.href.substring(0,self.location.href.lastIndexOf("&providerview="));
  else a = self.location.href;
	self.location.href = a + "&providerview=" +s.options[s.selectedIndex].value ;
}
function MM_openBrWindow(theURL,winName,features) { //v2.0
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


</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="5">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#FFFFFF">
		<div align="right"></div>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"><input type='button' name='print'
			value='Print' onClick='window.print()'></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Report - Overnight Batch Eligibility Checking</font></font></b></font></p>
		</td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<html:errors />
	<form name="serviceform" method="get" action="obec.do">
	<tr>
		<td width="50%" align="left"><font size="1" color="#333333"
			face="Verdana, Arial, Helvetica, sans-serif"></td>
		<td width="40%"></td>
		<td width="10%"><font color="#333333" size="1"
			face="Verdana, Arial, Helvetica, sans-serif"> </font></td>
	</tr>
	<tr>
		<td width="50%">
		<div align="left"><font color="#003366"><font
			face="Verdana, Arial, Helvetica, sans-serif" size="1"><b>

		<font color="#333333">Service Date-Range</font></b></font></font> &nbsp; &nbsp; <font
			size="1" face="Arial, Helvetica, sans-serif"><a href="#"
			onClick="MM_openBrWindow('../billing/billingCalendarPopup.jsp?type=admission&amp;year=<%=curYear%>&amp;month=<%=curMonth%>','','width=300,height=300')">Begin
		Date:</a></font> <input type="text" name="xml_vdate" value="<%=xml_vdate%>">

		</div>
		</td>
		<td colspan='2'>
		<div align="left"><font size="1"
			face="Arial, Helvetica, sans-serif">Number of Days:</font> <input
			type="text" name="numDays" value="<%=numDays%>"> <input
			type="submit" name="Submit" value="Create Report"></div>
		</td>
	</tr>
	</form>
</table>
<pre>
<% if (obectxt.compareTo("0")!=0 && obectxt.compareTo("")!=0 ){ %>

<a
	href="../servlet/OscarDownload?homepath=obecdownload&filename=<%=obectxt%>"
	target="_blank">File Created <%=obectxt%></a>
<%}
    else{%>
        <font size="1" color="#333333"
	face="Verdana, Arial, Helvetica, sans-serif">File not created!</font>
<%}%>
</pre>
<%@ include file="../demographic/zfooterbackclose.jsp"%>

</body>
</html>

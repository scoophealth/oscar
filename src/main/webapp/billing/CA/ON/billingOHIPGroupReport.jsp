<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<% 
  String user_no= (String) session.getAttribute("user");
%>

<%@ page import="java.util.*, java.sql.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="org.oscarehr.common.model.BillCenter" %>
<%@page import="org.oscarehr.common.dao.BillCenterDao" %>
<%@page import="org.oscarehr.billing.CA.model.BillActivity" %>
<%@page import="org.oscarehr.billing.CA.dao.BillActivityDao" %>
<%@page import="oscar.util.ConversionUtils" %>
<%@ include file="../../../admin/dbconnection.jsp"%>

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillCenterDao billCenterDao = SpringUtils.getBean(BillCenterDao.class);
	BillActivityDao billActivityDao = SpringUtils.getBean(BillActivityDao.class);
	
%>


<% 	GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
  
    String[] yearArray = new String[5];
  String thisyear = request.getParameter("year")==null?String.valueOf(curYear):request.getParameter("year").compareTo("")==0?String.valueOf(curYear):request.getParameter("year");
if (thisyear.compareTo("")==0) thisyear = String.valueOf(curYear);
String yearColor = "";  
yearArray[0] = String.valueOf(curYear);
    yearArray[1] = String.valueOf(curYear-1);
      yearArray[2] = String.valueOf(curYear-2);
        yearArray[3] = String.valueOf(curYear-3);
          yearArray[4] = String.valueOf(curYear-4);
          
if (thisyear.compareTo(yearArray[0])==0) yearColor="#B1D3EF";

if (thisyear.compareTo(yearArray[1])==0) yearColor="#BBBBBB";
if (thisyear.compareTo(yearArray[2])==0) yearColor="#CCCCCC";
if (thisyear.compareTo(yearArray[3])==0) yearColor="#DDDDDD";
if (thisyear.compareTo(yearArray[4])==0) yearColor="#EEEEEE";

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
     String ohipdownload = oscarVariables.getProperty("HOME_DIR") ;;
     session.setAttribute("ohipdownload", ohipdownload);      
  %>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
</head>

<body bgcolor="#FFFFFF" text="#000000">
<div id="Layer1"
	style="position: absolute; left: 90px; top: 35px; width: 0px; height: 12px; z-index: 1"></div>
<div id="Layer2"
	style="position: absolute; left: 45px; top: 61px; width: 129px; height: 123px; z-index: 2; background-color: #EEEEFF; layer-background-color: #6666FF; border: 1px none #000000; visibility: hidden;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#DDDDEE">
		<td align='CENTER'><font size="2"
			face="Tahoma, Geneva, Arial, Helvetica, san-serif"><strong>Last
		5 Years</strong></font></td>
	</tr>
	<% for (int i=0; i<5;i++) { %>
	<tr>
		<td align='CENTER'><font size="2"
			face="Tahoma, Geneva, Arial, Helvetica, san-serif"><a
			href="billingOHIPreport.jsp?year=<%=yearArray[i]%>">YEAR <%=yearArray[i]%></a></font></td>
	</tr>
	<% } %>
</table>
</div>
<table border="0" cellspacing="0" cellpadding="0" width="100%"
	onLoad="setfocus()" rightmargin="0" topmargin="0" leftmargin="0">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF"> OHIP Group Report - <%=thisyear%></font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>


<table width="100%" border="0" bgcolor="#E6F0F7">
	<form name="form1" method="post" action="genGroupReport.jsp">
	<tr>
		<td width="220"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><a href="#"
			onClick="showHideLayers('Layer2','','show')">Show Archive</a> </font></b></td>
		<td width="220"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Select provider </font></b></td>
		<td width="254"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"> <select name="provider">
			<option value="all">All Providers</option>
			<% String proFirst="";
           String proLast="";
           String proOHIP="";
           String specialty_code; 
String billinggroup_no;
           int Count = 0;
           
for(Provider p : providerDao.getActiveProviders()) {
	if(p.getOhipNo() == null || !p.getOhipNo().isEmpty()) {
		continue;
	}
	proFirst = p.getFirstName();
	proLast = p.getLastName();
	proOHIP = p.getOhipNo();
	billinggroup_no= SxmlMisc.getXmlContent(p.getComments(),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
	specialty_code = SxmlMisc.getXmlContent(p.getComments(),"<xml_p_specialty_code>","</xml_p_specialty_code>");	

 %>
			<option value="<%=proOHIP%>,<%=specialty_code%>|<%=billinggroup_no%>"><%=proLast%>,
			<%=proFirst%></option>
			<% 

 }
//
  %>
		</select> </font></b></td>
		<td width="181"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Select billing center</font></b></td>
		<td width="254"><font face="Arial, Helvetica, sans-serif"
			size="2"> <select name="billcenter">

			<% String centerCode="";
           String centerDesc="";
           
           int Count1 = 0;
for(BillCenter bc:billCenterDao.findByBillCenterDesc("%")) {
 centerCode = bc.getBillCenterCode();
 centerDesc = bc.getBillCenterDesc();
  
 %>
			<option value="<%=centerCode%>"
				<%=oscarVariables.getProperty("billcenter").compareTo(centerCode)==0?"selected":""%>><%=centerDesc%></option>
			<% } %>
		</select></td>
		<td width="277"><font color="#003366"> <input
			type="submit" name="Submit" value="Create Report"> <input
			type="hidden" name="monthCode" value="<%=monthCode%>"> <input
			type="hidden" name="verCode" value="V03"> <input
			type="hidden" name="curUser" value="<%=user_no%>"> <input
			type="hidden" name="curDate" value="<%=nowDate%>"> </font></td>
	</tr>
	<tr>
		<td colspan="4"><font color="#003366"><b><font
			face="Arial, Helvetica, sans-serif" size="2"> </font></b><font
			face="Arial, Helvetica, sans-serif" size="2"> </font></font><font
			color="#003366"><b><font
			face="Arial, Helvetica, sans-serif" size="2"> </font></b><font
			face="Arial, Helvetica, sans-serif" size="2"> </font></font></td>
	</tr>
	</form>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	bgcolor="<%=yearColor%>">
	<tr>
		<td colspan="6"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"> <b>Activity List </b></font></td>
	</tr>
	<tr>
		<td width="14%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Provider</font></td>
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Group Number</font></td>
		<td width="16%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Creation Date </font></td>
		<td width="15%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Claims/Records</font></td>
		<td width="19%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Ohip Filename</font></td>
		<td width="24%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">HTML Filename</font></td>
	</tr>

	<%
   String pro_ohip="", pro_group="", updatedate="", cr="", oFile="", hFile="";
  
   List<BillActivity> bas = billActivityDao.findCurrentByDateRange(ConversionUtils.fromDateString(thisyear+"-01-01 00:00:00"), ConversionUtils.fromDateString(thisyear + "-12-31 23:59:59"));
   Collections.sort(bas,BillActivity.UpdateDateTimeComparator);
   for(BillActivity ba:bas) {
	  
   pro_ohip = ba.getProviderOhipNo();
   pro_group = ba.getGroupNo();
   updatedate = ConversionUtils.toDateString(ba.getUpdateDateTime());
   cr = ba.getClaimRecord();
   oFile = ba.getOhipFilename();
   hFile = ba.getHtmlFilename();
 
   %>

	<tr>
		<td width="14%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><%=pro_ohip%></font></td>
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><%=pro_group%></font></td>
		<td width="16%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><%=updatedate%></font></td>
		<td width="15%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><%=cr%></font><font
			face="Arial, Helvetica, sans-serif" size="2" color="#003366"></font></td>
		<td width="19%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><a
			href="../servlet/OscarDownload?homepath=ohipdownload&filename=<%=oFile%>"
			target="_blank"><%=oFile%></a></font></td>
		<td width="24%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><a
			href="../servlet/OscarDownload?homepath=ohipdownload&filename=<%=hFile%>"
			target="_blank"><%=hFile%></a></font></td>
	</tr>
	<%  }
  %>
</table>
</body>
</html>

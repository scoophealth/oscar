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

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%
String user_no = (String) session.getAttribute("user");
%>

<%@ page import="java.util.*, java.sql.*, oscar.util.*,oscar.*" errorPage="errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.billing.CA.model.BillActivity" %>
<%@page import="org.oscarehr.common.model.Provider" %>
<%@page import="org.oscarehr.billing.CA.dao.BillActivityDao" %>
<%@page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@page import="oscar.util.ConversionUtils" %>

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillActivityDao billActivityDao = SpringUtils.getBean(BillActivityDao.class);
%>
<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);
String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);

String[] yearArray = new String[5];
String thisyear = (request.getParameter("year")==null || request.getParameter("year").compareTo("")==0) ? String.valueOf(curYear) : request.getParameter("year");

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
<script language="JavaScript">



var checkSubmitFlg = false;
function checkSubmit() {
	if (checkSubmitFlg == true) {
		return false;
	}
	checkSubmitFlg = true;
	document.forms[0].Submit.disabled = true;
	return true;
}

function findObj(n, d) { //v4.0
  var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
    d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
  if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
  for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
  if(!x && document.getElementById) x=document.getElementById(n); return x;
}

function showHideLayers() { //v3.0
    var i,p,v,obj,args=showHideLayers.arguments;
    for (i=0; i<(args.length-2); i+=3){
    if ((obj=findObj(args[i]))!=null) {
        v=args[i+2];
        if (obj.style) {
            obj=obj.style;
            v=(v=='show')?'visible':(v='hide')?'hidden':v;
        }
        obj.visibility=v;
    }
  }
}
//-->
</script>
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
			href="billingTeleplanGroupReport.jsp?year=<%=yearArray[i]%>">YEAR
		<%=yearArray[i]%></a></font></td>
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
			color="#FFFFFF"> Teleplan Group Report - <%=thisyear%></font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>


<table width="100%" border="0" bgcolor="#E6F0F7">
	<form name="form1" method="post" action="genTeleplanGroupReport.jsp"
		onsubmit="return checkSubmit();">
	<tr>
		<td width="220"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"><a href="#"
			onClick="showHideLayers('Layer2','','show');">Show Archive</a> </font></b></td>
		<td width="220"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Select provider </font></b></td>
		<td width="254"><b><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"> <select name="provider">
			<option value="all">All Providers</option>
			<%String proFirst="";
             String proLast="";
             String proOHIP="";
             String specialty_code;
             String billinggroup_no;
             int Count = 0;
             for(Provider p : providerDao.getActiveProviders()) {
            	 if(p.getOhipNo()!= null && !p.getOhipNo().isEmpty()) {
                proFirst = p.getFirstName();
                proLast = p.getLastName();
                proOHIP = p.getOhipNo();
                billinggroup_no= p.getBillingNo();//SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
                specialty_code = SxmlMisc.getXmlContent(p.getComments(),"<xml_p_specialty_code>","</xml_p_specialty_code>");
           %>
			<option value="<%=proOHIP%>,<%=specialty_code%>|<%=billinggroup_no%>"><%=proLast%>,<%=proFirst%></option>
			<%

             } }
//
  %>
		</select> </font></b></td>
		<td width="181"><b><!--<font face="Arial, Helvetica, sans-serif" size="2">Select
        billing center</font>--></b></td>
		<td width="254"><font face="Arial, Helvetica, sans-serif"
			size="2"> <!--   <select name="billcenter">


 </select>--></td>
		<td width="277"><font color="#003366"> <input
			type="submit" name="Submit" value="Create Report"> <input
			type="hidden" name="monthCode" value="<%=monthCode%>"> <input
			type="hidden" name="verCode" value="V03"> <input
			type="hidden" name="curUser" value="<%=user_no%>"> <input
			type="hidden" name="curDate" value="<%=nowDate%>"> </font></td>
	</tr>
	<tr>
		<td colspan="4"><font color="#003366"> <b><font
			face="Arial, Helvetica, sans-serif" size="2"></font></b> <font
			face="Arial, Helvetica, sans-serif" size="2"> </font> </font> <font
			color="#003366"> <b><font
			face="Arial, Helvetica, sans-serif" size="2"></font></b> <font
			face="Arial, Helvetica, sans-serif" size="2"> </font> </font></td>
	</tr>
	</form>
</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr bgcolor="<%=yearColor%>">
		<td colspan="6"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366"> <b>Activity List </b></font></td>
	</tr>
	<tr bgcolor="<%=yearColor%>">
		<th width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Provider</font></th>
		<th width="14%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Group Number</font></th>
		<th width="20%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Creation Date </font></th>
		<th width="15%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="#003366">Claims/Records</font></th>
		<th width="15%"><font face="Arial, Helvetica, sans-serif"
			size="2" color="blue">MSP Filename</font></th>
		<th><font face="Arial, Helvetica, sans-serif" size="2"
			color="#003366">HTML Filename</font></th>
	</tr>

	<%
	String pro_ohip="", pro_group="", updatedate="", cr="", oFile="", hFile="";
	String strToday = UtilDateUtilities.DateToString(new java.util.Date(), "yyyy-MM-dd");


	List<BillActivity> bas = billActivityDao.findCurrentByDateRange(ConversionUtils.fromDateString(thisyear+"-01-01 00:00:00"), ConversionUtils.fromDateString(thisyear+"-12-31 23:59:59"));
	
	for(BillActivity ba:bas){
		pro_ohip = ba.getProviderOhipNo();
		pro_group = ba.getGroupNo();
		updatedate = ConversionUtils.toTimestampString(ba.getUpdateDateTime());
		cr = ba.getClaimRecord();
		oFile = ba.getOhipFilename();
		hFile =ba.getHtmlFilename();
%>

	<tr
		bgcolor="<%=updatedate.startsWith(strToday)? "#E6F0F7" : yearColor %>"
		align="center">
		<td><font face="Arial, Helvetica, sans-serif" size="2"
			color="#003366"><%=pro_ohip%></font></td>
		<td><font face="Arial, Helvetica, sans-serif" size="2"
			color="#003366"><%=pro_group%></font></td>
		<td><font face="Arial, Helvetica, sans-serif" size="2"
			color="#003366"><%=updatedate%></font></td>
		<td><font face="Arial, Helvetica, sans-serif" size="2"
			color="#003366"><%=cr%></font><font
			face="Arial, Helvetica, sans-serif" size="2" color="#003366"></font></td>
		<td><font face="Arial, Helvetica, sans-serif" size="2"
			color="#003366"><a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=oFile%>"
			target="_blank"><%=oFile%></a></font></td>
		<td><font face="Arial, Helvetica, sans-serif" size="2"
			color="#003366"><a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=hFile%>"
			target="_blank"><%=hFile%></a></font></td>
	</tr>
	<%
}
%>
</table>
</body>
</html>

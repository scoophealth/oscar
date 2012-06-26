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
<%  
if(session.getValue("user") == null) response.sendRedirect("../../../logout.jsp");
String user_no = (String) session.getAttribute("user");
OscarProperties props = OscarProperties.getInstance();
if(props.getProperty("isNewONbilling", "").equals("true")) {
%>
<jsp:forward page="billingONMRI.jsp" />
<% } %>
%>

<%@ page
	import="java.util.*, java.sql.*, oscar.*, oscar.util.*, java.net.*"
	errorPage="errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%@ include file="dbBilling.jspf"%>

<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);

String nowDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd HH:mm:ss"); //String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
String thisyear = (request.getParameter("year") == null || request.getParameter("year").equals("")) ? ("" + curYear) : request.getParameter("year") ;

String[] yearArray = new String[5];
for (int i = 0; i < yearArray.length; i++) {
	yearArray[i] = "" + (curYear - i);
}

String yearColor = "";  
String[] yearColorArray = new String[] {"#B1D3EF", "#BBBBBB", "#CCCCCC", "#DDDDDD", "#EEEEEE"};
for (int i = 0; i < yearArray.length; i++) {
	if (yearArray[i].equals(thisyear)) {
		yearColor = yearColorArray[i];
		break;
	}
}

String monthCode = "";
if (curMonth == 1) monthCode = "A";
else if (curMonth == 2) monthCode = "B";
else if (curMonth == 3) monthCode = "C";
else if (curMonth == 4) monthCode = "D";
else if (curMonth == 5) monthCode = "E";
else if (curMonth == 6) monthCode = "F";
else if (curMonth == 7) monthCode = "G";
else if (curMonth == 8) monthCode = "H";
else if (curMonth == 9) monthCode = "I";
else if (curMonth == 10) monthCode = "J";
else if (curMonth == 11) monthCode = "K";
else if (curMonth == 12) monthCode = "L";

String ohipdownload = oscarVariables.getProperty("HOME_DIR") ;;
session.setAttribute("ohipdownload", ohipdownload);      
%>

<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
<script language="JavaScript" type="text/JavaScript">
<!--

var checkSubmitFlg = false;
function checkSubmit() {
	if (checkSubmitFlg == true) {
		return false;      
	}
	checkSubmitFlg = true;
	document.forms[0].Submit.disabled = true;
	return true;   
}

function reloadPage(init) {  //reloads the window if Nav4 resized
if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
document.pgW=innerWidth; document.pgH=innerHeight; onresize=reloadPage; }}
else if (innerWidth!=document.pgW || innerHeight!=document.pgH) location.reload();
}
reloadPage(true);

function findObj(n, d) { 
var p,i,x;  if(!d) d=document; if((p=n.indexOf("?"))>0&&parent.frames.length) {
d=parent.frames[n.substring(p+1)].document; n=n.substring(0,p);}
if(!(x=d[n])&&d.all) x=d.all[n]; for (i=0;!x&&i<d.forms.length;i++) x=d.forms[i][n];
for(i=0;!x&&d.layers&&i<d.layers.length;i++) x=findObj(n,d.layers[i].document);
if(!x && d.getElementById) x=d.getElementById(n); return x;
}

function showHideLayers() {
var i,p,v,obj,args=showHideLayers.arguments;
for (i=0; i<(args.length-2); i+=3) if ((obj=findObj(args[i]))!=null) { v=args[i+2];
if (obj.style) { obj=obj.style; v=(v=='show')?'visible':(v=='hide')?'hidden':v; }
obj.visibility=v; }
}
//-->
</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" onLoad="setfocus()" topmargin="0"
	leftmargin="0" rightmargin="0">
<div id="Layer1"
	style="position: absolute; left: 90px; top: 35px; width: 0px; height: 12px; z-index: 1"></div>
<div id="Layer2"
	style="position: absolute; left: 45px; top: 61px; width: 129px; height: 123px; z-index: 2; background-color: #EEEEFF; layer-background-color: #6666FF; border: 1px none #000000; visibility: hidden;">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#DDDDEE">
		<td align='CENTER'><font size="2"> <strong>Last 5
		Years</strong></font></td>
	</tr>
	<% for (int i=0; i<5;i++) { %>
	<tr>
		<td align='CENTER'><font size="2"> <a
			href="billingOHIPreport.jsp?year=<%=yearArray[i]%>">YEAR <%=yearArray[i]%></a></font></td>
	</tr>
	<% } %>
</table>
</div>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th><font face="Arial, Helvetica, sans-serif" color="#FFFFFF">
		OHIP Report - <%=thisyear%></font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>


<table width="100%" border="0" bgcolor="#E6F0F7">
	<form name="form1" method="post"
		action="<%=oscarVariables.getProperty("group_billing", "").trim().equals("on")?"genGroupReport.jsp" : "genreport.jsp"%>"
		onsubmit="return checkSubmit();">
	<tr>
		<td width="220"><a href="#"
			onClick="showHideLayers('Layer2','','show')">Show Archive</a></td>
		<td width="220">Select provider</td>
		<td width="254"><select name="provider">
			<option value="all">All Providers</option>

			<%
String proFirst="";
String proLast="";
String proOHIP="";
String specialty_code; 
String billinggroup_no;
ResultSet rslocal = apptMainBean.queryResults("%", "search_provider_dt");
while(rslocal.next()){
	proFirst = rslocal.getString("first_name");
	proLast = rslocal.getString("last_name");
	proOHIP = rslocal.getString("ohip_no"); 
	billinggroup_no= SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_billinggroup_no>","</xml_p_billinggroup_no>");
	specialty_code = SxmlMisc.getXmlContent(rslocal.getString("comments"),"<xml_p_specialty_code>","</xml_p_specialty_code>");
%>

			<option value="<%=proOHIP%>,<%=specialty_code%>|<%=billinggroup_no%>"><%=proLast%>,
			<%=proFirst%></option>
			<% 
}
%>
		</select></td>
		<td width="200">Billing center</td>
		<td width="254"><select name="billcenter">

			<% 
String centerCode="";
String centerDesc="";

ResultSet rsCenter = apptMainBean.queryResults("%", "search_bill_center");
while(rsCenter.next()){
	centerCode = rsCenter.getString("billcenter_code");
	centerDesc = rsCenter.getString("billcenter_desc");
%>

			<option value="<%=centerCode%>"
				<%=oscarVariables.getProperty("billcenter").compareTo(centerCode)==0?"selected":""%>><%=centerDesc%></option>
			<% } %>

		</select></td>
		<td><input type="submit" name="Submit" value="Create Report">
		<input type="hidden" name="monthCode" value="<%=monthCode%>">
		<input type="hidden" name="verCode" value="V03"> <input
			type="hidden" name="curUser" value="<%=user_no%>"> <input
			type="hidden" name="curDate" value="<%=nowDate%>"></td>
	</tr>
	</form>
</table>

<table width="100%" border="0" cellspacing="1" cellpadding="0">
	<tr bgcolor="<%=yearColor%>">
		<th width="25%">Provider</th>
		<th width="20%">Creation Date</th>
		<th width="10%">Clm/Rec</th>
		<th width="10%">Total
		</td>
		<th width="15%">Ohip Filename</th>
		<th width="20%">HTML Filename</th>
	</tr>

	<%
Properties proName = new Properties();
ResultSet rspro = null;
rspro = apptMainBean.queryResults("%", "search_provider_ohip_dt");
while(rspro.next()){
	proName.setProperty(rspro.getString("ohip_no"), (rspro.getString("last_name") + ", " + rspro.getString("first_name")));
}

String[] paramYear = new String[2];
paramYear[0] = thisyear+"/01/01";
paramYear[1] = thisyear+"/12/31 23:59:59";
String pro_ohip="", pro_group="", pro_name="", updatedate="", cr="", oFile="", hFile="", total="";

int count = 0;
rslocal = apptMainBean.queryResults(paramYear, "search_billactivity_short");
while(rslocal.next()){
	count++;
	pro_ohip = rslocal.getString("providerohipno"); 
	pro_group = rslocal.getString("groupno");
	updatedate = rslocal.getString("updatedatetime");
	cr = rslocal.getString("claimrecord");
	oFile = rslocal.getString("ohipfilename");
	hFile = rslocal.getString("htmlfilename");
	total = rslocal.getString("total")==null?"0.00":rslocal.getString("total");
	pro_name = proName.getProperty(pro_ohip);
%>

	<tr bgcolor="<%=count%2==0?yearColor:"white"%>">
		<td><font size="2"><%=pro_name%></font></td>
		<td align="center"><font size="2"><%=updatedate%></font></td>
		<td align="center"><font size="2"><%=cr%></td>
		<td align="right"><font size="2"><%=total.substring(0,total.indexOf(".")) + total.substring(total.indexOf("."), total.indexOf(".") + 3)%></font></td>

		<td width="15%"><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=oFile%>"
			target="_blank"><%=oFile%></a></font></td>
		<td width="20%"><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=hFile%>"
			target="_blank"><%=hFile%></a></font></td>
	</tr>

	<%
}
%>

</table>
</body>
</html>

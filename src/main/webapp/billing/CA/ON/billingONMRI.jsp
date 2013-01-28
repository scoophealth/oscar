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
<%@ page import="oscar.login.DBHelp"%>

<%
	if(session.getAttribute("user") == null ) response.sendRedirect("../logout.jsp");
	String curProvider_no = (String) session.getAttribute("user");
    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    
    boolean isTeamBillingOnly=false;
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
%>
<security:oscarSec objectName="_team_billing_only" roleName="<%=roleName$ %>" rights="r" reverse="false">
<% isTeamBillingOnly=true; %>
</security:oscarSec>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isTeamAccessPrivacy=true; %>
</security:oscarSec>


<% 
HashMap<String,String> providerMap = new HashMap<String,String>();
//multisites function
if (isSiteAccessPrivacy || isTeamAccessPrivacy) {
	String sqlStr = "select provider_no from provider ";
	if (isSiteAccessPrivacy) 
		sqlStr = "select distinct p.provider_no from provider p inner join providersite s on s.provider_no = p.provider_no " 
		 + " where s.site_id in (select site_id from providersite where provider_no = " + curProvider_no + ")";
	if (isTeamAccessPrivacy) 
		sqlStr = "select distinct p.provider_no from provider p where team in (select team from provider "
				+ " where team is not null and team <> '' and provider_no = " + curProvider_no + ")";
	DBHelp dbObj = new DBHelp();
	ResultSet rs = dbObj.searchDBRecord(sqlStr);
	while (rs.next()) {
		providerMap.put(rs.getString("provider_no"),"true");
	}
	rs.close();
}
%>

<%@ page import="java.util.*,java.sql.*,oscar.*,oscar.util.*,java.net.*"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />
<%if (session.getAttribute("user") == null) {
				response.sendRedirect("../../../logout.jsp");
			}
			String user_no = (String) session.getAttribute("user");

			%>
<%@ include file="dbBilling.jspf"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
<link rel="stylesheet" type="text/css" href="billingON.css" />

<%
			GregorianCalendar now = new GregorianCalendar();
			int curYear = now.get(Calendar.YEAR);
			int curMonth = (now.get(Calendar.MONTH) + 1);
			int curDay = now.get(Calendar.DAY_OF_MONTH);

			String nowDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd HH:mm:ss"); //String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay)+ " " +now.get(Calendar.HOUR_OF_DAY)+":"+now.get(Calendar.MINUTE) + ":"+now.get(Calendar.SECOND);
			String thisyear = (request.getParameter("year") == null || request.getParameter("year").equals("")) ? ("" + curYear)
					: request.getParameter("year");

			String[] yearArray = new String[5];
			for (int i = 0; i < yearArray.length; i++) {
				yearArray[i] = "" + (curYear - i);
			}

			String yearColor = "";
			String[] yearColorArray = new String[] { "#CCFFCC", "#BBBBBB", "#CCCCCC", "#DDDDDD", "#EEEEEE" };
			for (int i = 0; i < yearArray.length; i++) {
				if (yearArray[i].equals(thisyear)) {
					yearColor = yearColorArray[i];
					break;
				}
			}

			String monthCode = BillingDataHlp.propMonthCode.getProperty("" + curMonth);
			String ohipdownload = oscarVariables.getProperty("HOME_DIR");
			session.setAttribute("ohipdownload", ohipdownload);

			//			 get the current year's billing disk filenames
			BillingReviewPrep prep = new BillingReviewPrep();
			Vector mriList = (Vector) prep.getMRIList(thisyear + "/01/01 00:00:01", thisyear + "/12/31 23:59:59","'U'");
			
			String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
			String xml_appointment_date = request.getParameter("xml_appointment_date")==null? UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy-MM-dd") : request.getParameter("xml_appointment_date");
			%>
<script language="JavaScript" type="text/JavaScript">
<!--
function validateGroupReport() {
	var e = document.getElementsByName('provider')[0];
	var val = e.options[e.selectedIndex].value;
	if(val == 'all') {
		alert("This function will create the billing for the selected provider's group. No applicable to 'All Providers'");
		return false;
	}
	return true;
}

var checkSubmitFlg = false;
function checkSubmit() {
	if (checkSubmitFlg == true) {
		return false;      
	}
	checkSubmitFlg = true;
	document.forms[0].Submit.disabled = true;
	return true;   
}
function recreate(si) {
    ret = confirm("Are you sure you want to do the action?");
	if(ret) {
		ss=document.forms[0].billcenter[document.forms[0].billcenter.selectedIndex].value;
		var su = document.forms[0].useProviderMOH.checked;
		location.href="onregenreport.jsp?diskId="+si+"&billcenter="+ss+"&useProviderMOH="+su;		
	}
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

<!-- calendar stylesheet -->
<link rel="stylesheet" type="text/css" media="all"
	href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<!-- main calendar program -->
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<!-- language for the calendar -->
<script type="text/javascript"
	src="../../../share/calendar/lang/calendar-en.js"></script>
<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
<script type="text/javascript"
	src="../../../share/calendar/calendar-setup.js"></script>	
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
	<%for (int i = 0; i < 5; i++) {

				%>
	<tr>
		<td align='CENTER'><font size="2"> <a
			href="billingONMRI.jsp?year=<%=yearArray[i]%>">YEAR <%=yearArray[i]%></a></font></td>
	</tr>
	<%}

			%>
</table>
</div>

<table border="0" cellspacing="0" cellpadding="0" width="100%"
	class="myDarkGreen">
	<tr>
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th><font face="Arial" color="#FFFFFF"> OHIP Report - <%=thisyear%></font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>


<table width="100%" border="0" class="myGreen">
	<form name="form1" method="post" action="ongenreport.jsp"
		onsubmit="return checkSubmit();">
	<tr>
		<td width="220"><a href="#"
			onClick="showHideLayers('Layer2','','show')">Show Archive</a></td>
		<td width="220">Select Provider</td>
		<td width="254"><select name="provider">
			<%
			List providerStr; 
			
			if (isTeamBillingOnly || isTeamAccessPrivacy) {
				providerStr = prep.getTeamProviderBillingStr(user_no);
			}
			else if (isSiteAccessPrivacy) {
				providerStr = prep.getSiteProviderBillingStr(user_no);
			}
			else {
				providerStr = prep.getProviderBillingStr();
			}
			
			
			if(providerStr.size() == 1) {
				String temp[] = ((String) providerStr.get(0)).split("\\|");
			%>
			<option value="<%=temp[0]%>"><%=temp[1]%>, <%=temp[2]%></option>
			<%
			} else {
			%>
			<option value="all">All Providers</option>

			<%
			for (int i = 0; i < providerStr.size(); i++) {
				String temp[] = ((String) providerStr.get(i)).split("\\|");
			%>
			<option value="<%=temp[0]%>"><%=temp[1]%>, <%=temp[2]%></option>
			<%}
			}
			%>
		</select></td>
		<td width="200">Billing Center</td>
		<td width="254"><select name="billcenter">

			<%for (Enumeration e = BillingDataHlp.propBillingCenter.propertyNames(); e.hasMoreElements();) {
				String centerCode = (String) e.nextElement();
%>

			<option value="<%=centerCode%>"
				<%=oscarVariables.getProperty("billcenter").compareTo(centerCode)==0?"selected":""%>><%=BillingDataHlp.propBillingCenter.getProperty(centerCode)%></option>
			<%}

			%>

		</select></td>
		<td><input type="submit" name="Submit" value="Create Report">
		
		<input type="hidden" name="monthCode" value="<%=monthCode%>">
		<input type="hidden" name="verCode" value="V03"> <input
			type="hidden" name="curUser" value="<%=user_no%>"> <input
			type="hidden" name="curDate" value="<%=nowDate%>"></td>
	</tr>
	<tr>
		<td><font face="Arial, Helvetica, sans-serif" size="2"><b>
		Service Date: </b></font></td>
		<td><font size="1" face="Arial, Helvetica, sans-serif">
		From:</font> <input type="text" id="xml_vdate" name="xml_vdate" size="10"
			maxlength="10" value="<%=xml_vdate%>" readonly> <img
			src="../../../images/cal.gif" id="xml_vdate_cal"></td>
		<td><font size="1" face="Arial, Helvetica, sans-serif">
		To:</font> <input type="text" id="xml_appointment_date"
			name="xml_appointment_date" size="10" maxlength="10"
			value="<%=xml_appointment_date%>" readonly> <img
			src="../../../images/cal.gif" id="xml_appointment_date_cal"></td>
			<td colspan="3">
			<input type="checkbox" name="useProviderMOH" id="useProviderMOH" <%=("true".equals(request.getParameter("useProviderMOH")) ? "checked" : "") %>><label for="useProviderMOH"> Use individual provider's bill center setting (will use above bill center if provider does not have one set.)</label>
		</td>		
	</tr>	
	</form>
</table>
<script type="text/javascript">
Calendar.setup({ inputField : "xml_vdate", ifFormat : "%Y/%m/%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "xml_appointment_date", ifFormat : "%Y/%m/%d", showsTime :false, button : "xml_appointment_date_cal", singleClick : true, step : 1 });
</script>
<table width="100%" border="0" cellspacing="1" cellpadding="1"
	class="myIvory">
	<tr class="myYellow">
		<th width="25%">Provider</th>
		<th width="15%">Creation Date</th>
		<th width="10%">Clm/Rec</th>
		<th width="10%">Total</th>
		<th width="12%" colspan=2>to OHIP</th>
		<th>HTML</th>
	</tr>

	<%Properties proName = (new JdbcBillingPageUtil()).getPropProviderName();
			String pro_no = "", pro_ohip = "", pro_group = "", pro_name = "", updatedate = "", cr = "", oFile = "", hFile = "", total = "";

			int count = 0;
			for(int i=0; i<mriList.size(); i++) {
				BillingDiskNameData obj = (BillingDiskNameData) mriList.get(i);
				oFile = obj.getOhipfilename();
				pro_group = obj.getGroupno();
				updatedate = obj.getUpdatedatetime();
				String createdate = obj.getCreatedatetime();
				Vector vecProviderOhipNo = obj.getProviderohipno();
				Vector vecProviderNo = obj.getProviderno();
				for(int j=0; j<vecProviderNo.size(); j++){
					count++;
					pro_ohip = (String)vecProviderOhipNo.get(j);
					pro_no = (String)vecProviderNo.get(j);
					cr = (String)obj.getVecClaimrecord().get(j);
					hFile = (String)obj.getHtmlfilename().get(j);
					total = (String)obj.getVecTotal().get(j);
					pro_name = proName.getProperty(pro_no);
					String bgColor = count%2==0?yearColor:"ivory";
					if(!updatedate.equals(createdate)) bgColor = "silver";
					
				    //multisites. skip record if not belong to same site/team
				    if (isSiteAccessPrivacy || isTeamAccessPrivacy) {
				    	if(providerMap.get(pro_no)== null)  continue;
				    }

%>

	<tr bgcolor="<%=bgColor%>"
		onMouseOver="this.style.backgroundColor='pink';"
		onMouseout="this.style.backgroundColor='<%=bgColor%>';">
		<td><font size="2"><%=pro_name%></font></td>
		<td align="center"><font size="2"><%=updatedate.substring(0,16)%></font></td>
		<td align="center"><font size="2"><%=cr%></td>
		<td align="right"><font size="2"><%=total%></font></td>

		<td width="15%"><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=oFile%>"
			target="_blank"><%=oFile%></a></font></td>
		<td width="3%"><input type="button" value="R"
			onclick="recreate(<%=obj.getId() %>)" /></td>
		<td><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=hFile%>"
			target="_blank"><%=hFile%></a></font></td>
	</tr>

	<%}}
		%>

	<% // get old data records
proName = new Properties();
ResultSet rspro = null;
rspro = apptMainBean.queryResults("%", "search_provider_ohip_dt");
while(rspro.next()){
	proName.setProperty(rspro.getString("ohip_no"), (rspro.getString("last_name") + ", " + rspro.getString("first_name")));
}

String[] paramYear = new String[2];
paramYear[0] = thisyear+"/01/01";
paramYear[1] = thisyear+"/12/31 23:59:59";
//String pro_ohip="", pro_group="", pro_name="", updatedate="", cr="", oFile="", hFile="", total="";

//int count = 0;
ResultSet rslocal = apptMainBean.queryResults(paramYear, "search_billactivity_short");
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

		<td colspan=2><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=oFile%>"
			target="_blank"><%=oFile%></a></font></td>
		<td><font size="2"> <a
			href="../../../servlet/OscarDownload?homepath=ohipdownload&filename=<%=hFile%>"
			target="_blank"><%=hFile%></a></font></td>
	</tr>

	<%
}
%>

</table>
</body>
</html>

</body>
</html>

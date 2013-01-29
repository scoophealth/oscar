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
<%@ page import="oscar.util.ConversionUtils" %>
<%@ page import="java.util.*,java.sql.*,oscar.*,oscar.util.*,java.net.*" errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>

<%@ page import="org.oscarehr.billing.CA.model.BillActivity" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillActivityDao" %>

<%@ page import="org.oscarehr.common.model.ProviderData"%>
<%@ page import="org.oscarehr.common.dao.ProviderDataDao"%>


<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%@ include file="../../../admin/dbconnection.jsp"%>


<%@page import="org.oscarehr.common.model.ProviderBillCenter" %>
<%@page import="org.oscarehr.common.dao.ProviderBillCenterDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%
	ProviderDao providerDao = SpringUtils.getBean(ProviderDao.class);
	BillActivityDao billActivityDao = SpringUtils.getBean(BillActivityDao.class);
	ProviderDataDao providerDataDao = SpringUtils.getBean(ProviderDataDao.class);

	String curProvider_no = (String) session.getAttribute("user");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    
    boolean isTeamBillingOnly=false;
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
%>

<security:oscarSec objectName="_team_billing_only" roleName="<%=roleName$ %>" rights="r" reverse="false"><% isTeamBillingOnly=true; %></security:oscarSec>
<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"><%isSiteAccessPrivacy=true; %></security:oscarSec>
<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false"><%isTeamAccessPrivacy=true; %></security:oscarSec>

<% 
List<ProviderData> pdList = null;
HashMap<String,String> providerMap = new HashMap<String,String>();

//multisites function
if (isSiteAccessPrivacy || isTeamAccessPrivacy) {

	if (isSiteAccessPrivacy) 
		pdList = providerDataDao.findByProviderSite(curProvider_no);
	
	if (isTeamAccessPrivacy) 
		pdList = providerDataDao.findByProviderTeam(curProvider_no);

	for(ProviderData providerData : pdList) {
		providerMap.put(providerData.getId(), "true");
	}
}
%>



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
			List mriList = prep.getMRIList(thisyear + "-01-01 00:00:01", thisyear + "-12-31 23:59:59","U");
			
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
<link rel="stylesheet" type="text/css" media="all" href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<script type="text/javascript" src="../../../share/calendar/lang/calendar-en.js"></script>
<script type="text/javascript" src="../../../share/calendar/calendar-setup.js"></script>	
<script>
var providerBillCenterMap = new Object();
<%
ProviderBillCenterDao providerBillCenterDao = (ProviderBillCenterDao)SpringUtils.getBean("providerBillCenterDao");

for(Provider p : providerDao.getBillableProviders()) {
	String providerNo = p.getProviderNo();
	ProviderBillCenter pbc = providerBillCenterDao.find(providerNo);
	%>
	providerBillCenterMap['<%=providerNo%>'] = '<%=pbc.getBillCenterCode()%>';
	<%
}
%>

function setBillingCenter( providerNo ) {
	var bcDropdown = document.getElementById("billcenter");
	
	var textToFind = providerBillCenterMap[providerNo];
	
	if (bcDropdown) {
		for (var i = 0; i < bcDropdown.options.length; i++) {
	    if (bcDropdown.options[i].value === textToFind) {
	        bcDropdown.selectedIndex = i;
	        break;
	    }
	}
	}
}
</script>
</head>

<body bgcolor="#FFFFFF" text="#000000" onLoad="setfocus()" topmargin="0" leftmargin="0" rightmargin="0">
<div id="Layer1" style="position: absolute; left: 90px; top: 35px; width: 0px; height: 12px; z-index: 1"></div>
<div id="Layer2" style="position: absolute; left: 45px; top: 61px; width: 129px; height: 123px; z-index: 2; background-color: #EEEEFF; layer-background-color: #6666FF; border: 1px none #000000; visibility: hidden;">
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
		<td width="254"><select name="provider" onchange = "setBillingCenter(this.value);">
			<%
			List providerStr; 
			
			if (isTeamBillingOnly || isTeamAccessPrivacy) {
				providerStr = prep.getTeamProviderBillingStr(curProvider_no);
			}
			else if (isSiteAccessPrivacy) {
				providerStr = prep.getSiteProviderBillingStr(curProvider_no);
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
		<td width="254"><select name="billcenter" id="billcenter">

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
			type="hidden" name="curUser" value="<%=curProvider_no%>"> <input
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
Calendar.setup({ inputField : "xml_vdate", ifFormat : "%Y-%m-%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "xml_appointment_date", ifFormat : "%Y-%m-%d", showsTime :false, button : "xml_appointment_date_cal", singleClick : true, step : 1 });
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
for(Provider p : providerDao.getActiveProviders()) {
	if(p.getOhipNo() != null && !p.getOhipNo().isEmpty()) {
		proName.setProperty(p.getOhipNo(), (p.getLastName() + ", " +p.getFirstName()));
	}
}


String[] paramYear = new String[2];
paramYear[0] = thisyear+"/01/01";
paramYear[1] = thisyear+"/12/31 23:59:59";
//String pro_ohip="", pro_group="", pro_name="", updatedate="", cr="", oFile="", hFile="", total="";

//int count = 0;
List<BillActivity> bas = billActivityDao.findCurrentByDateRange(ConversionUtils.fromDateString(thisyear+"-01-01 00:00:00"), ConversionUtils.fromDateString(thisyear+"-12-31 23:59:59"));
Collections.sort(bas,BillActivity.UpdateDateTimeComparator);
for(BillActivity ba:bas) {
	count++;
	pro_ohip = ba.getProviderOhipNo();
	pro_group = ba.getGroupNo();
	updatedate = ConversionUtils.toDateString(ba.getUpdateDateTime());
	cr = ba.getClaimRecord();
	oFile = ba.getOhipFilename();
	hFile = ba.getHtmlFilename();
	total = ba.getTotal()==null?"0.00":ba.getTotal();
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

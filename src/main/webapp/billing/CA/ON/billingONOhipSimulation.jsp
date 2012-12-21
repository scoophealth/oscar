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
<%@page import="org.oscarehr.util.DateRange"%>
<%! boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%@ page import="java.math.*, java.util.*, oscar.util.*"%>
<%
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
if(session.getAttribute("user") == null) response.sendRedirect("../../../logout.jsp");
String user_no = (String) session.getAttribute("user");
%>


<%@ page
	import="java.util.*, java.sql.*, oscar.*, oscar.util.*, java.net.*"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.pageUtil.*"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<jsp:useBean id="SxmlMisc" class="oscar.SxmlMisc" scope="session" />

<%
GregorianCalendar now=new GregorianCalendar();
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);

String nowDate = UtilDateUtilities.DateToString(UtilDateUtilities.now(), "yyyy/MM/dd"); //"yyyy/MM/dd HH:mm"
String monthCode = BillingDataHlp.propMonthCode.getProperty("" + curMonth);

OscarProperties props = OscarProperties.getInstance();
String billCenter = props.getProperty("billcenter", "").trim();
String healthOffice = BillingDataHlp.propBillingCenter.getProperty(billCenter);

boolean summaryView = "on".equals(request.getParameter("summaryView"));

if(request.getParameter("submit")!=null && request.getParameter("submit").equals("Create Report")) {
	String errorMsg = "";
	int PROVIDER_BILLINGNO_LENGTH = 6;
	int PROVIDER_SPECIALTYCODE_LENGTH = 2;
	int PROVIDER_GROUPNO_LENGTH = 4;
	int errorCount = 0;	
	int bCount = 1;
	String batchCount = "0";
	BigDecimal bigTotal = new BigDecimal((double) 0).setScale(2, BigDecimal.ROUND_HALF_UP);
	int recordCount = 0;
	String pro = request.getParameter("provider");
	BillingProviderData proObj;
	ArrayList<String> providers = new ArrayList<String>();
	String htmlValue="";
	
	if (bMultisites) {
		DateRange dateRange = null;
		String proOHIP=""; 
		String specialty_code; 
		String billinggroup_no;
		String dateBegin = request.getParameter("xml_vdate");
		String dateEnd = request.getParameter("xml_appointment_date");
		if (dateEnd.compareTo("") == 0) dateEnd = request.getParameter("curDate");
		if (dateBegin.compareTo("") == 0){
			dateRange = new DateRange(null, ConversionUtils.fromDateString(dateEnd));
		} else {
			dateRange = new DateRange(ConversionUtils.fromDateString(dateBegin), ConversionUtils.fromDateString(dateEnd));
		}
		proObj = (new JdbcBillingPageUtil()).getProviderObj(pro);
		
		if (proObj.getOhipNo().length() != PROVIDER_BILLINGNO_LENGTH) 
			errorMsg = "The provider's billing code is not correct!<br>";
			
		proOHIP = proObj.getOhipNo(); 
		billinggroup_no= proObj.getBillingGroupNo();
		specialty_code = proObj.getSpecialtyCode();

		if (specialty_code.length() != PROVIDER_SPECIALTYCODE_LENGTH){
			errorMsg += "The provider's specialty code is not correct!<br>";
			specialty_code = "00"; 
		}

		if (billinggroup_no.length() != PROVIDER_GROUPNO_LENGTH){
			errorMsg += "The provider's group no is not correct!<br>";
			billinggroup_no = "0000";
		} 

		JdbcBillingCreateBillingFile dbObj = new JdbcBillingCreateBillingFile();
		dbObj.setEFlag("0");
		dbObj.setDateRange(dateRange);
		dbObj.setProviderNo(pro);
		BillingBatchHeaderData bhObj = new BillingBatchHeaderData();
		//bhObj.setId(bid);
		//bhObj.setDisk_id("" + rs.getInt("disk_id"));
		//bhObj.setTransc_id(rs.getString("transc_id"));
		//bhObj.setRec_id(rs.getString("rec_id"));
		bhObj.setSpec_id("   ");
		bhObj.setMoh_office(" ");
		//bhObj.setBatch_id(rs.getString("batch_id"));
		//bhObj.setOperator(rs.getString("operator"));
		bhObj.setGroup_num(billinggroup_no);
		bhObj.setProvider_reg_num(proOHIP);
		bhObj.setSpecialty(specialty_code);
		//bhObj.setH_count(rs.getString("h_count"));
		//bhObj.setR_count(rs.getString("r_count"));
		//bhObj.setT_count(rs.getString("t_count"));
		//bhObj.setBatch_date(rs.getString("batch_date"));
		
		dbObj.setBatchHeaderObj(bhObj);	
		dbObj.createSiteBillingFileStr("0", new String[] {"O", "W", "I" });
		htmlValue = "<font color='red'>" + errorMsg + "</font>" + dbObj.getHtmlValue();
	}
	else {
		if ("all".equals(pro)) {
			BillingReviewPrep prep = new BillingReviewPrep();
			List<String> providerStr = null;
			if (isTeamBillingOnly || isTeamAccessPrivacy) {			
				providerStr = prep.getTeamProviderBillingStr(user_no);
			}
			else if (isSiteAccessPrivacy) {
				providerStr = prep.getSiteProviderBillingStr(user_no);
			}
			else {
				providerStr = prep.getProviderBillingStr();
			}
			for (int i = 0; i < providerStr.size(); i++) {
				providers.add((providerStr.get(i)).split("\\|")[0]);			
			}
		}
		else {
			providers.add(pro);
		}
		
		for (String provider : providers) {
			errorMsg = "";
			proObj = (new JdbcBillingPageUtil()).getProviderObj(provider);
			
			if (proObj.getOhipNo().length() != PROVIDER_BILLINGNO_LENGTH) 
				errorMsg = "The provider's billing code is not correct!<br>";
			
			String proOHIP=""; 
			String specialty_code; 
			String billinggroup_no;
			DateRange dateRange = null;
			
			String dateBegin = request.getParameter("xml_vdate");
			String dateEnd = request.getParameter("xml_appointment_date");
			if (dateEnd.compareTo("") == 0) dateEnd = request.getParameter("curDate");
			if (dateBegin.compareTo("") == 0){
				dateRange = new DateRange(null, ConversionUtils.fromDateString(dateEnd));
			} else {
				dateRange = new DateRange(ConversionUtils.fromDateString(dateBegin), ConversionUtils.fromDateString(dateEnd));
			}
			
			proOHIP = proObj.getOhipNo(); 
			billinggroup_no= proObj.getBillingGroupNo();
			specialty_code = proObj.getSpecialtyCode();
		
			if (specialty_code.length() != PROVIDER_SPECIALTYCODE_LENGTH){
				errorMsg += "The provider's specialty code is not correct!<br>";
				specialty_code = "00"; 
			}
		
			if (billinggroup_no.length() != PROVIDER_GROUPNO_LENGTH){
				errorMsg += "The provider's group no is not correct!<br>";
				billinggroup_no = "0000";
			} 
		
			JdbcBillingCreateBillingFile dbObj = new JdbcBillingCreateBillingFile();
			dbObj.setEFlag("0");
			dbObj.setDateRange(dateRange);
			dbObj.setProviderNo(provider);
			BillingBatchHeaderData bhObj = new BillingBatchHeaderData();
			bhObj.setSpec_id("   ");
			bhObj.setMoh_office(" ");
			bhObj.setGroup_num(billinggroup_no);
			bhObj.setProvider_reg_num(proOHIP);
			bhObj.setSpecialty(specialty_code);
			dbObj.setBatchHeaderObj(bhObj);
			dbObj.errorMsg += errorMsg;
			
			dbObj.createBillingFileStr("0", new String[] { "O", "W", "I" }, true, null, summaryView);
			if (dbObj.getRecordCount() > 0) {
				recordCount += dbObj.getRecordCount();	
				bigTotal = bigTotal.add(dbObj.getBigTotal());
				htmlValue += dbObj.getHtmlValue();			
			}			
			errorCount += "".equals(dbObj.errorMsg) ? 0 : dbObj.errorMsg.split("<br>").length;
			errorCount += "".equals(dbObj.errorFatalMsg) ? 0 : dbObj.errorFatalMsg.split("<br>").length;
			dbObj.errorMsg = "";
		}
		
		String billingTable = htmlValue;
		htmlValue   = "<style type='text/css'><!-- .myGreen{  font-family: Arial, Helvetica, sans-serif;  font-size: 12px; font-style: normal;  line-height: normal;  font-weight: normal;  font-variant: normal;  text-transform: none;  color: #003366;  text-decoration: none; --></style>";
		htmlValue  += "\n<table width='100%' border='0' cellspacing='0' cellpadding='2'>\n"
					+ "<tr><td colspan='12' class='myGreen'></td></tr>";
		if (summaryView) {
			htmlValue += "\n<tr><td class='myGreen'>OHIP NO</td><td class='myGreen'>NUMBER OF RECORDS</td><td class='myGreen'>TOTAL BILLED</td><td class='myGreen' colspan='9'></td></tr>";
		}
		else {
			htmlValue += "\n<tr><td class='myGreen'>OHIP NO</td><td class='myGreen'>ACCT NO</td>"
					+ "<td width='25%' class='myGreen'>NAME</td><td class='myGreen'>RO</td><td class='myGreen'>DOB</td><td class='myGreen'>Sex</td><td class='myGreen'>HEALTH #</td>"
					+ "<td class='myGreen'>BILLDATE</td><td class='myGreen'>CODE</td>"
					+ "<td align='right' class='myGreen'>BILLED</td>"
					+ "<td align='right' class='myGreen'>DX</td><td align='right' class='myGreen'>Comment</td></tr>";	
		
			
						
		}
		htmlValue  += billingTable;
		htmlValue  += "\n<tr><td colspan='12' class='myIvory'>&nbsp;</td></tr><tr><td colspan='4' class='myIvory'>" 
			+ recordCount
			+ " RECORDS PROCESSED, " 
			+ errorCount 
			+" ERROR"+ (errorCount > 1 ? "S" : "") + "</td><td colspan='8' class='myIvory'>TOTAL: "
			+ bigTotal.toString()
			+ "\n</td></tr>";
		htmlValue  += "</table>";
	}
	request.setAttribute("html",htmlValue);
}
%>



<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Billing Report</title>
<link rel="stylesheet" type="text/css" href="billingON.css" />
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
<script type="text/javascript" src="<%=request.getContextPath()%>/share/javascript/jquery/jquery-1.4.2.js"></script>
<script type="text/javascript" language="JavaScript">
<!--
function openBrWindow(theURL,winName,features) {
	window.open(theURL,winName,features);
}


function checkData() {
	var b = true;
	if(document.forms[0].provider.value=="000000"){
		alert("Please select a provider!");
		b = false;
	}else if(document.forms[0].xml_vdate.value==""){
		alert("Please give a date!");
		b = false;
	}

	return b;
}
//-->
</script>

</head>

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="0" onLoad="setfocus()">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr class="myDarkGreen">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th><font color="#FFFFFF"> OHIP Report Simulation</font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>

<%
String providerview=request.getParameter("provider")==null?user_no:request.getParameter("provider");
String xml_vdate=request.getParameter("xml_vdate") == null?"":request.getParameter("xml_vdate");
String xml_appointment_date = request.getParameter("xml_appointment_date")==null? nowDate : request.getParameter("xml_appointment_date");
%>

<table width="100%" border="0" class="myYellow">
	<form name="serviceform" method="post"
		action="billingONOhipSimulation.jsp" onSubmit="return checkData();">
	<tr>
		<td width="220"><b><font face="Arial" size="2"> Select
		Provider </font></b></td>
		<td width="254"><select name="provider">
			<% if (bMultisites) { %>
			<option value="all">Select Providers</option>
			<% } else { %>
			<option value="all">All Providers</option>
			<% } %>
			<%
		BillingReviewPrep prep = new BillingReviewPrep();
			
			
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
		
		for (int i = 0; i < providerStr.size(); i++) {
			String temp[] = ((String) providerStr.get(i)).split("\\|");
			%>
			<option value="<%=temp[0]%>"
				<%=providerview.equals(temp[0])?"selected":(providerStr.size()==1?"selected":"")%>><%=temp[1]%>,
			<%=temp[2]%></option>
			<%}
		%>
		</select></td>
		<td width="254"><b><font face="Arial" size="2"> Bill
		Center: </font></b> <input type="hidden" name="billcenter"
			value="<%=billCenter%>"> <font face="Arial" size="2"><b><%=healthOffice%></b>
		</font></td>
		<% if (!bMultisites) { %>
		<td width="220">
			<b><font face="Arial" size="2"> 
				<input type="checkbox" name="summaryView" id="summaryView" <%= summaryView ? "checked" : "" %> />		
				<label for="summaryView">Summary View</label> 
			</font></b>
		</td>
		<% } %>
		<td width="277"><font color="#003366"> <input
			type="hidden" name="monthCode" value="<%=monthCode%>"> <input
			type="hidden" name="verCode" value="V03"> <input
			type="hidden" name="curUser" value="<%=user_no%>"> <input
			type="hidden" name="curDate" value="<%=nowDate%>"> </font></td>
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
		<td><input type="submit" name="submit" value="Create Report">
		</td>
	</tr>
	</form>
</table>

<%=request.getAttribute("html") == null?"":request.getAttribute("html")%>

</body>
<script type="text/javascript">
Calendar.setup({ inputField : "xml_vdate", ifFormat : "%Y/%m/%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 });
Calendar.setup({ inputField : "xml_appointment_date", ifFormat : "%Y/%m/%d", showsTime :false, button : "xml_appointment_date_cal", singleClick : true, step : 1 });
</script>
</html>

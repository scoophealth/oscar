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
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%! boolean bMultisites = org.oscarehr.common.IsPropertiesOn.isMultisitesEnable(); %>

<%
	String user_no = (String) session.getAttribute("user");
    boolean isTeamBillingOnly=false; 
%>
<security:oscarSec objectName="_team_billing_only" roleName="<%= roleName$ %>" rights="r" reverse="false">
<% isTeamBillingOnly=true; %>
</security:oscarSec>

<%    

int nItems=0;
String strLimit1="0";
String strLimit2="50";
if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
String providerview = request.getParameter("providerview")==null?"all":request.getParameter("providerview") ;
%>

<%@ page import="java.util.*, java.sql.*, oscar.login.*, oscar.*, java.net.*" errorPage="errorpage.jsp"%>
<%@ include file="../../../admin/dbconnection.jsp"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.ReportProvider" %>
<%@ page import="org.oscarehr.common.dao.ReportProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>


<%
	ReportProviderDao reportProviderDao = SpringUtils.getBean(ReportProviderDao.class);
%>
<%
GregorianCalendar now=new GregorianCalendar(); 
int curYear = now.get(Calendar.YEAR);
int curMonth = (now.get(Calendar.MONTH)+1);
int curDay = now.get(Calendar.DAY_OF_MONTH);

String xml_vdate = request.getParameter("xml_vdate") == null ? "" : request.getParameter("xml_vdate");
String xml_appointment_date = request.getParameter("xml_appointment_date") == null? "" : request.getParameter("xml_appointment_date");
%>

<%
// action
Vector vecHeader = new Vector();
Vector vecValue = new Vector();
Vector vecTotal = new Vector();
Properties prop = null;
DBHelp dbObj = new DBHelp();
ResultSet rs = null;
String sql = null;

String action = request.getParameter("reportAction") == null ? "" : request.getParameter("reportAction");
if("unbilled".equals(action)) {
    vecHeader.add("SERVICE DATE");
    vecHeader.add("TIME");
    vecHeader.add("PATIENT");
    vecHeader.add("DESCRIPTION");
    vecHeader.add("COMMENTS");
    
    sql = "select * from appointment where provider_no='" + providerview + "' and appointment_date >='" + xml_vdate   
            + "' and appointment_date<='" + xml_appointment_date 
            + "' and (BINARY status NOT LIKE 'B%' AND BINARY status NOT LIKE 'C%' AND BINARY status NOT LIKE 'N%' AND BINARY status NOT LIKE 'T%' AND BINARY status NOT LIKE 't%')"
            + " and demographic_no != 0 order by appointment_date , start_time ";
    
    rs = dbObj.searchDBRecord(sql);
    while (rs.next()) {
    	if (bMultisites) {
    		// skip record if location does not match the selected site, blank location always gets displayed for backward-compatibility
    		String location = rs.getString("location");
    		if (StringUtils.isNotBlank(location) && !location.equals(request.getParameter("site"))) 
    			continue; 
    	}

    	prop = new Properties();
        prop.setProperty("SERVICE DATE", rs.getString("appointment_date"));
        prop.setProperty("TIME", rs.getString("start_time").substring(0,5));
        prop.setProperty("PATIENT", rs.getString("name"));
        prop.setProperty("DESCRIPTION", rs.getString("reason"));
        String tempStr = "<a href=# onClick='popupPage(700,1000, \"billingOB.jsp?billForm=" 
                + URLEncoder.encode(oscarVariables.getProperty("default_view")) + "&hotclick=&appointment_no="
                + rs.getString("appointment_no") + "&demographic_name=" + URLEncoder.encode(rs.getString("name"))
				+ "&demographic_no=" + rs.getString("demographic_no") + "&user_no=" + rs.getString("provider_no") 
				+ "&apptProvider_no=" + providerview + "&appointment_date=" + rs.getString("appointment_date") 
				+ "&start_time=" + rs.getString("start_time") + "&bNewForm=1\"); return false;'>Bill ";
        prop.setProperty("COMMENTS", tempStr);
        vecValue.add(prop);
    }

}

if("billed".equals(action)) {
    vecHeader.add("SERVICE DATE");
    vecHeader.add("TIME");
    vecHeader.add("PATIENT");
    vecHeader.add("DESCRIPTION");
    vecHeader.add("ACCOUNT");
    sql = "select * from billing_on_cheader1 where provider_no='" + providerview + "' and billing_date >='" + xml_vdate 
            + "' and billing_date<='" + xml_appointment_date + "' and (status<>'D' and status<>'S' and status<>'B')" 
            + " order by billing_date , billing_time ";
    rs = dbObj.searchDBRecord(sql);
    while (rs.next()) {
    	if (bMultisites) {
    		// skip record if clinic is not match the selected site, blank clinic always gets displayed for backward compatible
    		String clinic = rs.getString("clinic");
    		if (StringUtils.isNotBlank(clinic) && !clinic.equals(request.getParameter("site"))) 
    			continue; 
    	}
    	
        prop = new Properties();
        prop.setProperty("SERVICE DATE", rs.getString("billing_date"));
        prop.setProperty("TIME", rs.getString("billing_time").substring(0,5));
        prop.setProperty("PATIENT", rs.getString("demographic_name"));
        
        String apptDoctorNo = rs.getString("apptProvider_no");
        String userno=rs.getString("provider_no");
        String reason = rs.getString("status");
        String note = "";
        if (apptDoctorNo.compareTo("none") == 0){
        	note = "No Appt / INR";
        } else {
    	    if (apptDoctorNo.compareTo(userno) == 0) {
    	    	note = "With Appt. Doctor";
    	    } else {
    	    	note = "Unmatched Appt. Doctor";
    	    }
        }
    	if (reason.compareTo("N") == 0) reason="Do Not Bill ";
    	else if (reason.compareTo("O") == 0) reason="Bill OHIP ";
    	else if (reason.compareTo("W") == 0) reason="Bill WSIB ";
    	else if (reason.compareTo("H") == 0) reason="Capitated Bill ";
    	else if (reason.compareTo("P") == 0) reason="Bill Patient";

    	prop.setProperty("DESCRIPTION", reason + "(" + note + ")");
        String tempStr = "<a href=# onClick='popupPage(700,720, \"../../../billing/CA/ON/billingCorrection.jsp?billing_no="
                + rs.getString("id") + "&dboperation=search_bill&hotclick=0\"); return false;' title="
                + reason + ">" + rs.getString("id") + "</a>";
  	    prop.setProperty("ACCOUNT", tempStr);
        vecValue.add(prop);
    }
    
    
}

if("paid".equals(action)) {
    vecHeader.add("No");
    vecHeader.add("Billing No");
    vecHeader.add("HIN");
    vecHeader.add("Claim");
    vecHeader.add("Paid");
    vecHeader.add("Billing Date");
    //vecHeader.add("Time");
    float fTotalClaim = 0.00f;
    float fTotalPaid = 0.00f;
        
    // get billing no in the date range
    Vector vecBillingNo = new Vector();
    Properties propTotal = new Properties();
    sql = "select billing_no,total from billing where provider_no='" + providerview 
    + "' and billing_date>='" + xml_vdate + "' and billing_date<='" + xml_appointment_date 
    + "' and status ='S' order by billing_date, billing_time";
    
    // change 'S' to 'O' for testing
    
    rs = dbObj.searchDBRecord(sql);
    while (rs.next()) {
        vecBillingNo.add("" + rs.getInt("billing_no"));
        propTotal.setProperty(""+rs.getInt("billing_no"), rs.getString("total"));
    }
    rs.close();
    
    // get detail ra for the billing no
    String tempStr = "";
    for(int i=0; i<vecBillingNo.size(); i++) {
        tempStr += ("".equals(tempStr) ? "" : ",") + (String)vecBillingNo.get(i);
    }
    tempStr = "".equals(tempStr) ? "-1" : tempStr;
    
    // change tempStr to '75980, 75982, 75990' for testing
    //tempStr = "75980, 75982, 75990,79571,79066";
    
    sql = "select billing_no, amountclaim, amountpay, hin, service_date from radetail where billing_no in ("
            + tempStr + ") and raheader_no !=0 order by billing_no, radetail_no";
    rs = dbObj.searchDBRecord(sql);
    String sAmountclaim = "", sAmountpay = "", hin = "";
    int nNo = 0;
    while (rs.next()) {
        if(!tempStr.equals("" + rs.getInt("billing_no")) ) { // new billing no
            prop = new Properties();
        	// reset something
            tempStr = "" + rs.getInt("billing_no");
            nNo++;
            sAmountclaim = rs.getString("amountclaim");
			sAmountpay = rs.getString("amountpay");
			String strT = "<a href=# onClick='popupPage(700,720, \"../../../billing/CA/BC/billingView.do?billing_no="
		        + rs.getString("billing_no") + "&dboperation=search_bill&hotclick=0\"); return false;' >" 
		        + rs.getString("billing_no") + "</a>";
	        prop.setProperty("No", ""+nNo);
	        prop.setProperty("Billing No", strT);
	        prop.setProperty("HIN", rs.getString("hin"));
	        prop.setProperty("Claim", sAmountclaim);
	        prop.setProperty("Paid", sAmountpay);
	        prop.setProperty("Billing Date", getFormatDateStr(rs.getString("service_date")));
	        vecValue.add(prop);
	        
	        fTotalClaim += Float.parseFloat(rs.getString("amountclaim"));
	        fTotalPaid += Float.parseFloat(rs.getString("amountpay"));
        } else { // old billing no
            prop = new Properties();
            //sAmountclaim = rs.getString("amountclaim");
			//sAmountpay = rs.getString("amountpay");
			float fAmountclaim = Float.parseFloat(sAmountclaim);
			fAmountclaim = fAmountclaim + Float.parseFloat(rs.getString("amountclaim"));
			sAmountclaim = "" + Math.round(fAmountclaim*100)/100.00;
			float fAmountpay = Float.parseFloat(sAmountpay);
			fAmountpay = fAmountpay + Float.parseFloat(rs.getString("amountpay"));
			sAmountpay = "" + Math.round(fAmountpay*100)/100.00;
			//hin = rs.getString("hin");
			String strT = "<a href=# onClick='popupPage(700,720, \"../../../billing/CA/BC/billingView.do?billing_no="
		        + rs.getString("billing_no") + "&dboperation=search_bill&hotclick=0\"); return false;' >" 
		        + rs.getString("billing_no") + "</a>";
	        prop.setProperty("No", ""+nNo);
	        prop.setProperty("Billing No", strT);
	        prop.setProperty("HIN", rs.getString("hin"));
	        // repeated records
	        //prop.setProperty("Claim", sAmountclaim);
	        prop.setProperty("Claim", propTotal.getProperty(tempStr));
	        prop.setProperty("Paid", sAmountpay);
	        prop.setProperty("Billing Date", getFormatDateStr(rs.getString("service_date")));
	        vecValue.remove(vecValue.size()-1);
	        vecValue.add(prop);
	        
	        fTotalClaim += Float.parseFloat(rs.getString("amountclaim"));
	        fTotalPaid += Float.parseFloat(rs.getString("amountpay"));
        }
    }
    rs.close();
    vecTotal.add("Total");  
    vecTotal.add("");  
    vecTotal.add("");  
    vecTotal.add("" + Math.round(fTotalClaim*100)/100.00);  
    vecTotal.add("" + Math.round(fTotalPaid*100)/100.00);  
    vecTotal.add("");  
}

if("unpaid".equals(action)) {
    vecHeader.add("No");
    vecHeader.add("Billing No");
    vecHeader.add("Patient");
    vecHeader.add("Claim");
    vecHeader.add("Description");
    vecHeader.add("Service Date");
    vecHeader.add("Time");
    float fTotalClaim = 0.00f;
    String sAmountclaim = "";
        
    sql = "select * from billing where provider_no='" + providerview + "' and billing_date >='" + xml_vdate 
    + "' and billing_date<='" + xml_appointment_date + "' and (status<>'D' and status<>'S')" 
    + " order by billing_date , billing_time ";
    int nNo = 0;
	rs = dbObj.searchDBRecord(sql);
	while (rs.next()) {
		prop = new Properties();
		nNo++;
        prop.setProperty("No", ""+nNo);
		prop.setProperty("Service Date", rs.getString("billing_date"));
		prop.setProperty("Time", rs.getString("billing_time").substring(0,5));
		prop.setProperty("Patient", rs.getString("demographic_name"));
		
		String apptDoctorNo = rs.getString("apptProvider_no");
		String userno=rs.getString("provider_no");
		String reason = rs.getString("status");
		String note = "";
		if (apptDoctorNo.compareTo("none") == 0){
			note = "No Appt / INR";
		} else {
		    if (apptDoctorNo.compareTo(userno) == 0) {
		    	note = "With Appt. Doctor";
		    } else {
		    	note = "Unmatched Appt. Doctor";
		    }
		}
		if (reason.compareTo("N") == 0) reason="Do Not Bill ";
		else if (reason.compareTo("O") == 0) reason="Bill OHIP ";
		else if (reason.compareTo("W") == 0) reason="Bill WSIB ";
		else if (reason.compareTo("H") == 0) reason="Capitated Bill ";
		else if (reason.compareTo("P") == 0) reason="Bill Patient";
		else if (reason.compareTo("B") == 0) reason="Sent OHIP";
		
		prop.setProperty("Description", reason + "(" + note + ")");
		String tempStr = "<a href=# onClick='popupPage(700,720, \"../../../billing/CA/BC/billingView.do?billing_no="
		        + rs.getString("billing_no") + "&dboperation=search_bill&hotclick=0\"); return false;' title="
		        + reason + ">" + rs.getString("billing_no") + "</a>";
		prop.setProperty("Billing No", tempStr);
        sAmountclaim = rs.getString("total");
		prop.setProperty("Claim", sAmountclaim);
        fTotalClaim += Float.parseFloat(rs.getString("total"));
		
		vecValue.add(prop);
	}
    rs.close();
    vecTotal.add("Total");  
    vecTotal.add("");  
    vecTotal.add("");  
    vecTotal.add("" + Math.round(fTotalClaim*100)/100.00);  
    vecTotal.add("");  
    vecTotal.add("");  
    vecTotal.add("");  
}

%>


<%@page import="org.oscarehr.common.dao.SiteDao"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.oscarehr.common.model.Site"%>
<%@page import="org.oscarehr.common.model.Provider"%>
<%@page import="org.apache.commons.lang.StringUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>ON Billing Report</title>
<link rel="stylesheet" href="../../web.css">
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
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

<body bgcolor="#FFFFFF" text="#000000" leftmargin="0" rightmargin="0"
	topmargin="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#CCCCFF">
		<td width="5%"></td>
		<td width="80%" align="left">
		<p><b><font face="Verdana, Arial" color="#FFFFFF" size="3"><a
			href="billingReportCenter.jsp">OSCARbilling</a></font></b></p>
		</td>
		<td align="right"><a href=#
			onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')">
		<font size="1">Manage Provider List </font></a></td>
	</tr>
</table>

<table width="100%" border="0" bgcolor="#EEEEFF">
	<form name="serviceform" method="post" action="billingONReport.jsp">
	<tr>
		<td width="30%" align="center"><font size="2"> <input
			type="radio" name="reportAction" value="unbilled"
			<%="unbilled".equals(action)? "checked" : "" %>>Unbilled <input
			type="radio" name="reportAction" value="billed"
			<%="billed".equals(action)? "checked" : "" %>>Billed <!--  input type="radio" name="reportAction" value="paid" <%="paid".equals(action)? "checked" : "" %>>Paid 
	<input type="radio" name="reportAction" value="unpaid" <%="unpaid".equals(action)? "checked" : "" %>>Unpaid -->
		</font></td>
		<td width="20%" align="right" nowrap><b>Provider </b></font> 
<% if (bMultisites) 
{ // multisite start ==========================================
        	SiteDao siteDao = (SiteDao)WebApplicationContextUtils.getWebApplicationContext(application).getBean("siteDao");
          	List<Site> sites = siteDao.getActiveSitesByProviderNo(user_no); 
          	// now get all report providers
          	
          	HashSet<String> reporters=new HashSet<String>();
          	for(Object[] res:reportProviderDao.search_reportprovider("billingreport")) {
          		ReportProvider rp = (ReportProvider)res[0];
          		Provider p =  (Provider)res[1];
          		reporters.add(p.getProviderNo());
          	}

      %> 
      <script>
var _providers = [];
<%	for (int i=0; i<sites.size(); i++) { 
	Set<Provider> siteProviders = sites.get(i).getProviders();
	List<Provider>  siteProvidersList = new ArrayList<Provider> (siteProviders);
	Collections.sort(siteProvidersList,(new Provider()).ComparatorName());%>
	_providers["<%= sites.get(i).getName() %>"]="<% Iterator<Provider> iter = siteProvidersList.iterator();
	while (iter.hasNext()) {
		Provider p=iter.next();
		if (reporters.contains(p.getProviderNo())) {
	%><option value='<%= p.getProviderNo() %>'><%= p.getLastName() %>, <%= p.getFirstName() %></option><% }} %>";
<% } %>
function changeSite(sel) {
	sel.form.providerview.innerHTML=sel.value=="none"?"":_providers[sel.value];
	sel.style.backgroundColor=sel.options[sel.selectedIndex].style.backgroundColor;
}
      </script>
      	<select id="site" name="site" onchange="changeSite(this)">
      		<option value="none" style="background-color:white">---select clinic---</option>
      	<%
      	for (int i=0; i<sites.size(); i++) {
      	%>
      		<option value="<%= sites.get(i).getName() %>" style="background-color:<%= sites.get(i).getBgColor() %>"
      				<%=sites.get(i).getName().toString().equals(request.getParameter("site"))?"selected":"" %>><%= sites.get(i).getName() %></option>
      	<% } %>
      	</select>
      	<select id="providerview" name="providerview" style="width:140px"></select>
<% if (request.getParameter("providerview")!=null) { %>
      	<script>
     	changeSite(document.getElementById("site"));
      	document.getElementById("providerview").value='<%=request.getParameter("providerview")%>';     	
      	</script>
<% } // multisite end ==========================================
} else {
%>
		<select
			name="providerview">
			<% 
String proFirst="";
String proLast="";
String proOHIP="";
String specialty_code; 
String billinggroup_no;
int Count = 0;


for(Object[] res:reportProviderDao.search_reportprovider("billingreport")) {
	ReportProvider rp = (ReportProvider)res[0];
	Provider p =  (Provider)res[1];
	proFirst = p.getFirstName();
	proLast = p.getLastName();
	proOHIP = p.getProviderNo();

  
%>
			<option value="<%=proOHIP%>"
				<%=providerview.equals(proOHIP)?"selected":""%>><%=proLast%>,
			<%=proFirst%></option>
			<%
}      
%>
		</select>
<% } %>
		
		
		</td>
		<td align="center" nowrap><font size="1"> From:</font> <input
			type="text" name="xml_vdate" id="xml_vdate" size="10"
			value="<%=xml_vdate%>"> <font size="1"> <img
			src="../../../images/cal.gif" id="xml_vdate_cal"> To:</font> <input
			type="text" name="xml_appointment_date" id="xml_appointment_date"
			onDblClick="calToday(this)" size="10"
			value="<%=xml_appointment_date%>"> <img
			src="../../../images/cal.gif" id="xml_appointment_date_cal"></td>
		<td align="right"><input type="submit" name="Submit"
			value="Create Report"> </font></td>
	</tr>
	<tr>
		</form>
</table>

<table border="1" cellspacing="0" cellpadding="0" width="100%"
	bordercolorlight="#99A005" bordercolordark="#FFFFFF" bgcolor="#FFFFFF">
	<tr bgcolor=<%="#ccffcc" %>>
		<% for (int i=0; i<vecHeader.size(); i++) {%>
		<th><%=vecHeader.get(i) %></th>
		<% } %>
		<% for (int i=0; i<vecValue.size(); i++) {%>
	
	<tr bgcolor="<%=i%2==0? "ivory" : "#EEEEFF" %>">
		<% for (int j=0; j<vecHeader.size(); j++) {
	    prop = (Properties)vecValue.get(i);
	%>
		<td align="center"><%=prop.getProperty((String)vecHeader.get(j), "&nbsp;") %>&nbsp;</td>
		<% } %>
	</tr>
	<% } %>

	<% if(vecTotal.size() > 0) { %>
	<tr bgcolor="silver">
		<% for (int i=0; i < vecTotal.size(); i++) {%>
		<th><%=vecTotal.get(i) %>&nbsp;</th>
		<% } %>
	</tr>
	<% } %>

</table>

<br>

<hr width="100%">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr>
		<td><a href=# onClick="javascript:history.go(-1);return false;">
		<img src="images/leftarrow.gif" border="0" width="25" height="20"
			align="absmiddle"> Back </a></td>
		<td align="right"><a href="" onClick="self.close();">Close
		the Window<img src="images/rightarrow.gif" border="0" width="25"
			height="20" align="absmiddle"></a></td>
	</tr>
</table>

</body>
<script type="text/javascript">
Calendar.setup( { inputField : "xml_vdate", ifFormat : "%Y/%m/%d", showsTime :false, button : "xml_vdate_cal", singleClick : true, step : 1 } );
Calendar.setup( { inputField : "xml_appointment_date", ifFormat : "%Y/%m/%d", showsTime :false, button : "xml_appointment_date_cal", singleClick : true, step : 1 } );
</script>
</html>
<%! 
String getFormatDateStr(String str) {
    String ret = str;
	if(str.length() == 8) {
	    ret = str.substring(0,4) + "/" + str.substring(4,6) + "/" + str.substring(6);
	}
	return ret;
}
%>

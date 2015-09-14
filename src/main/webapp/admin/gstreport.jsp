<!DOCTYPE html>
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
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ page
	import="java.math.*, java.util.*, oscar.util.*, oscar.oscarBilling.ca.on.administration.*, oscar.oscarBilling.ca.on.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%
    String curProvider_no = (String) session.getAttribute("user");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
    
    boolean authed=true;
%>

<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.reporting,_admin.billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.billing&type=_admin.reporting");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<security:oscarSec objectName="_site_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isSiteAccessPrivacy=true; %>
</security:oscarSec>

<security:oscarSec objectName="_team_access_privacy" roleName="<%=roleName$%>" rights="r" reverse="false">
	<%isTeamAccessPrivacy =true; %>
</security:oscarSec>

<%
GstReport gstReport = new GstReport(); 
Properties props = new Properties();
String providerNo = request.getParameter("providerview");
String startDate  = request.getParameter("xml_vdate"); 
String endDate    = request.getParameter("xml_appointment_date");
if ( providerNo == null ) { providerNo = "" ; }
if ( startDate == null ) { startDate = ""; } 
if ( endDate == null ) { endDate = ""; } 
int i;
String bgColour;
BigDecimal gsttotal = new BigDecimal(0);
BigDecimal billedtotal = new BigDecimal(0);
BigDecimal earnedtotal = new BigDecimal(0);
BigDecimal earned;
BigDecimal billed;
BigDecimal gst = new BigDecimal(0);
Vector list = gstReport.getGST(LoggedInInfo.getLoggedInInfoFromSession(request), providerNo, startDate, endDate);

List<String> pList = new ArrayList<String>();
if (isTeamAccessPrivacy) {
	pList=(new JdbcBillingPageUtil()).getCurTeamProviderStr(curProvider_no);
}
else if (isSiteAccessPrivacy) {
	pList= (new JdbcBillingPageUtil()).getCurSiteProviderStr(curProvider_no);
}
else {
	pList= (new JdbcBillingPageUtil()).getCurProviderStr();
}
%>
<html>
<head>
<title><bean:message key="admin.admin.gstReport"/></title>

<script type="text/javascript" src="<%=request.getContextPath() %>/js/jquery-1.9.1.min.js"></script>
<script src="<%=request.getContextPath() %>/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/js/bootstrap-datepicker.js"></script>

<link href="<%=request.getContextPath() %>/css/bootstrap.min.css" rel="stylesheet">
<link href="<%=request.getContextPath() %>/css/datepicker.css" rel="stylesheet" type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/font-awesome.min.css">
</head>
<body>
<FORM name="gstform" action="gstreport.jsp" class="form-inline">

<h3><bean:message key="admin.admin.gstReport"/></h3>

<div class="container-fluid well well-small">
<div class="span2">Date: <%=DateUtils.sumDate("yyyy-MM-dd","0")%></div> <div class="span2 pull-right"><button class="btn" type="button" value="Print" onclick="window.print()" /><i class="icon-print icon-white"></i> Print</button></div>

<div class="span12">
	<div class="span2">		
	Start:
	<div class="input-append">
		<input type="text" name="xml_vdate" id="xml_vdate" value="<%=startDate%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" style="width:90px" />
		<span class="add-on"><i class="icon-calendar"></i></span>
	</div>
	</div>
	<div class="span2">		
	End:
	<div class="input-append">
		<input type="text" name="xml_appointment_date" id="xml_appointment_date" value="<%=endDate%>" pattern="^\d{4}-((0\d)|(1[012]))-(([012]\d)|3[01])$" autocomplete="off" style="width:90px"/>
		<span class="add-on"><i class="icon-calendar"></i></span>
	</div>
</div>

<div class="span6">
Provider
<div>
<select name="providerview">
	<%
                          if(pList.size() == 1) {
                          String temp[] = ((String) pList.get(0)).split("\\|");
                          %>
	<option value="<%=temp[0]%>"><%=temp[1]%>, <%=temp[2]%></option>
	<%
                          } else {
                          %>
	<option value="all">-- Select a Provider --</option>
	<% for (i = 0 ; i < pList.size(); i++) { 
                          String temp[] = ((String) pList.get(i)).split("\\|");
                          %>
	<option value="<%=temp[0]%>"
		<%=providerNo.equals(temp[0])?"selected":""%>><%=temp[1]%>,
	<%=temp[2]%></option>
	<% } 
                          } %>
</select> 
<input class="btn btn-primary" type="submit" value="Search" />
</div>
</div><!--span6-->

</div><!--span10-->


<div class="span12">
<br>

</div><!--span12-->

</div>

<TABLE class="table table-striped  table-condensed">
	<TR style="font-weight:bold;">
		<TD align="center">SERVICE DATE</TD>
		<TD align="center">PATIENT</TD>
		<TD align="center">PATIENT NAME</TD>
		<TD align="center">GST Billed</TD>
		<TD align="center">Revenue</TD>
		<TD align="center">Total with ONLY GST</TD>
	</TR>
	<% for ( i = 0; i < list.size(); i++){
                if ( i % 2 == 1)        // If odd, then have colour, 
                bgColour = "#CCFFCC";
                else                    // Otherwise, white background
                bgColour = "";
                props = (Properties)list.get(i);
                // Calculate billed, and add into billedtotal
                if ( props.getProperty("total", "") != null){
                    billed = new BigDecimal(props.getProperty("total", "")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    billedtotal = billedtotal.add(billed);
                } else {
                    billed = new BigDecimal("0.00");
                }
                // Calculate gst, and add into gsttotal
                if ( props.getProperty("gst", "") != null ){     // If gst is avaliable, then add to total
                    gst = new BigDecimal(props.getProperty("gst", "")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    gsttotal = gsttotal.add(gst);
                } else {
                    gst = new BigDecimal("0.00");
                }
                // Calculate earned, and earnedtotal
                earned = billed.subtract(gst);
                earnedtotal = earnedtotal.add(earned);
                %>
	<% if ( gst.doubleValue() > 0 ){%>
	<TR>
		<TD width="20%" align="center"><%=props.getProperty("date", "")%></TD>
		<TD width="10%" align="center"><%=props.getProperty("demographic_no", "")%></TD>
		<TD width="15%" align="center"><%=props.getProperty("name", "")%></TD>
		<TD width="15%" align="center"><%=gst%></TD>
		<%//Perhaps show 0.00 if no gst...%>
		<TD width="15%" align="center"><%=earned%></TD>
		<TD width="15%" align="center"><%=billed%></TD>
		<%}%>
	</TR>
	<%}%>
	<TR align="center" style="font-weight:bold;">
		<TD width="20%">Totals:</TD>
		<TD></TD>
		<TD></TD>
		<TD width="15%" align="center"><%=gsttotal%></TD>
		<TD width="15%"><%=earnedtotal%></TD>
		<TD width="15%"><%=billedtotal%></TD>
	</TR>
</TABLE>
</FORM>
</body>
<script type="text/javascript">
var startDate = $("#xml_vdate").datepicker({
	format : "yyyy-mm-dd"
});

var endDate = $("#xml_appointment_date").datepicker({
	format : "yyyy-mm-dd"
});
</script>
</html>

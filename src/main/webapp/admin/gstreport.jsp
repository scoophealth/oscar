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
<%@ page
	import="java.math.*, java.util.*, oscar.util.*, oscar.oscarBilling.ca.on.administration.*, oscar.oscarBilling.ca.on.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%
    if(session.getAttribute("user") == null ) response.sendRedirect("../logout.jsp");
    String curProvider_no = (String) session.getAttribute("user");

    if(session.getAttribute("userrole") == null )  response.sendRedirect("../logout.jsp");
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    
    boolean isSiteAccessPrivacy=false;
    boolean isTeamAccessPrivacy=false; 
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
Vector list = gstReport.getGST(providerNo, startDate, endDate);

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
<link rel="stylesheet" type="text/css" media="all"
	href="../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript" src="../share/calendar/calendar-setup.js"></script>
<script type="text/javascript">

</script>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>GST Report</title>
</head>
<body>
<FORM name="gstform" action="gstreport.jsp">
<table width="100%" cellpadding="0" cellspacing="0" style="border: 0px;">
	<TR style="background-color: green;">
		<TD height="40" width="10%"></TD>
		<TD>
		<div style="color: white;">GST Report</div>
		</TD>
		<TD valign="BOTTOM">
		<div align="right"><b><%=DateUtils.sumDate("yyyy-MM-dd","0")%></b><input
			type="button" value="Print" onclick="window.print()" /></div>
		</TD>
	</TR>
</TABLE>
<TABLE width="100%" CELLPADDING="0" CELLSPACING="0" style="border: 0px;">
	<TR>
		<TD width="15%" align="center"><a
			href="javascript: function myFunction() {return false; }"
			style="font-size: 10px;" id="hlSDate">From:</a> <input type="text"
			name="xml_vdate" id="xml_vdate" value="<%=startDate%>" maxlength="10"
			style="width: 80px" /></TD>
		<TD width="15%" align="center"><a
			href="javascript: function myFunction() {return false; }"
			style="font-size: 10px;" id="hlADate">To:</a> <input type="text"
			name="xml_appointment_date" id="xml_appointment_date"
			value="<%=endDate%>" maxlength="10" style="width: 80px" /></TD>
		<TD height="40" align="left" width="70%"
			style="background-color: #FFCC99;"><select name="providerview">
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
		</select> <input type="submit" value="Search" /></TD>
	</TR>
</table>
<TABLE width="100%" cellpadding="0" cellspacing="0" border="1">
	<TR style="background-color: #FFCC99;">
		<TD width="20%" align="center">SERVICE DATE</TD>
		<TD width="10%" align="center">PATIENT</TD>
		<TD width="15%" align="center">PATIENT NAME</TD>
		<TD width="15%" align="center">GST Billed</TD>
		<TD width="15%" align="center">Revenue</TD>
		<TD width="15%" align="center">Total with ONLY GST</TD>
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
	<TR style="background-color: <%=bgColour%>;">
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
	<TR style="background-color: #FFCC99;" align="center">
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

<script language='javascript'>
       Calendar.setup({inputField:"xml_vdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});          
       Calendar.setup({inputField:"xml_appointment_date",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlADate",singleClick:true,step:1});                      
   </script>
</html>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />

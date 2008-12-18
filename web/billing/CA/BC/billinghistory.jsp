
<%
  if (session.getValue("user") == null)
    response.sendRedirect("../../../logout.htm");
  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
  //display the main provider page
  //includeing the provider name and a month calendar
  String strLimit1 = "0";
  String strLimit2 = "50";
  if (request.getParameter("limit1") != null) strLimit1 = request.getParameter("limit1");
  if (request.getParameter("limit2") != null) strLimit2 = request.getParameter("limit2");
%>
<%@page import="java.util.*, java.sql.*, java.net.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@include file="../../../admin/dbconnection.jsp"%>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@include file="dbBilling.jsp"%>
<!--
  /*
  *
  * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
  * This software is published under the GPL GNU General Public License.
  * This program is free software; you can redistribute it and/or
  * modify it under the terms of the GNU General Public License
  * as published by the Free Software Foundation; either version 2
  * of the License, or (at your option) any later version. *
  * This program is distributed in the hope that it will be useful,
  * but WITHOUT ANY WARRANTY; without even the implied warranty of
  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
  * along with this program; if not, write to the Free Software
  * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
  *
  * <OSCAR TEAM>
  *
  * This software was written for the
  * Department of Family Medicine
  * McMaster Unviersity
  * Hamilton
  * Ontario, Canada
  */
-->
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>BILLING HISTORY</title>
<link rel="stylesheet" href="../../../share/css/oscar.css">
<meta http-equiv="expires" content="Mon,12 May 1998 00:36:05 GMT">
<meta http-equiv="Pragma" content="no-cache">
<script language="JavaScript">

<!--



//-->

</SCRIPT>
<!--base target="pt_srch_main"-->
</head>
<body bgproperties="fixed" class="regtext">
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#000000">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF">BILLING
		HISTORY</font></th>
	</tr>
</table>
<!--%@ include file="zcppfulltitlesearch.htm" %-->
<table width="95%" border="0">
	<tr>
		<td align="left"><i>Results for Demographic</i> : <%=request.getParameter("last_name")%>
		, <%=request.getParameter("first_name")%> ( <%=request.getParameter("demographic_no")%>
		)</td>
	</tr>
</table>
<hr>
<CENTER>
<table width="100%" border="2" class="regtext">
	<tr class="SectionHead">
		<th><b>INVOICE#</b></th>
		<th>LINE#</th>
		<th>SVC CODE</th>
		<th><b>AMT</b></th>
		<th><b>DX CODE</b></th>
		<th><b>APP. DATE</b></th>
		<th><b>TYPE</b></th>
		<th><b>STATUS</b></th>
		<th><b>PRACT.</b></th>
		<th><b>COMMENTS</b></th>
	</tr>
	<%
    String proFirst = "";
    String proLast = "";
    String proNo = "";
    ResultSet rslocal;
    rslocal = null;
    ResultSet rs = null;
    rs = apptMainBean.queryResults(Integer.parseInt(request.getParameter("demographic_no")), "search_allbill_history");
    boolean bodd = false;
    int nItems = 0;
    String billType = "", billCode = "";
    java.text.NumberFormat fmt = java.text.NumberFormat.getCurrencyInstance();
    if (rs == null) {
      out.println("failed!!!");
    }
    else {
      String currentBillingNo = "";
      while (rs.next()) {
        String billno = apptMainBean.getString(rs,"billing_no");
        bodd = !currentBillingNo.equals(billno) ? !bodd : bodd; //for the color of rows
        nItems++; //to calculate if it is the end of records
        billCode = apptMainBean.getString(rs,"status");
        if (billCode.compareTo("B") == 0) {
          billType = "Submitted MSP";
        }
        if (billCode.substring(0, 1).compareTo("O") == 0) {
          billType = "Bill MSP";
        }
        if (billCode.substring(0, 1).compareTo("N") == 0) {
          billType = "Do Not Bill";
        }
        if (billCode.substring(0, 1).compareTo("P") == 0) {
          billType = "Bill Patient";
        }
        if (billCode.substring(0, 1).compareTo("W") == 0) {
          billType = "Bill WCB";
        }
        if (billCode.substring(0, 1).compareTo("H") == 0) {
          billType = "Capitated";
        }
        if (billCode.substring(0, 1).compareTo("S") == 0) {
          billType = "Settled";
        }
        if (billCode.substring(0, 1).compareTo("D") == 0) {
          billType = "Deleted";
        }
        if (billCode.substring(0, 1).compareTo("R") == 0) {
          billType = "Rejected";
        }
        if (billCode.substring(0, 1).compareTo("Z") == 0) {
          billType = "Held";
        }
        if (billCode.substring(0, 1).compareTo("C") == 0) {
          billType = "Data Center Changed";
        }
        if (billCode.substring(0, 1).compareTo("E") == 0) {
          billType = "Paid w/ Exception";
        }
        if (billCode.substring(0, 1).compareTo("F") == 0) {
          billType = "Refused";
        }
        if (billCode.substring(0, 1).compareTo("W") == 0) {
          billType = "Billed";
        }
        if (billCode.substring(0, 1).compareTo("T") == 0) {
          billType = "Collection";
        }
        if (billCode.substring(0, 1).compareTo("A") == 0) {
          billType = "Paid Private";
        }
        proNo = apptMainBean.getString(rs,"apptProvider_no") == null ? "" : apptMainBean.getString(rs,"apptProvider_no");
        if (proNo.compareTo("") == 0 || proNo.compareTo("000") == 0 || proNo.compareTo("none") == 0) {
          proFirst = "Not Available";
          proLast = "";
        }
        else {
          rslocal = apptMainBean.queryResults(proNo, "search_provider_all_dt");
          while (rslocal.next()) {
            proFirst = rslocal.getString("first_name");
            proLast = rslocal.getString("last_name") + ",";
          }
        }
        ResultSet rsamt = apptMainBean.queryResults(new String[] {billno}, "search_billamount");
        double billAmt = 0;
        if (rsamt.next()) {
          billAmt = rsamt.getDouble(1);
        }
        rsamt.close();
        ResultSet rsbillingmaster_count = apptMainBean.queryResults(new String[] {billno}, "search_billingmaster_count");
        int billingmaster_count = 0;
        if (rsbillingmaster_count.next()) {
          billingmaster_count = rsbillingmaster_count.getInt(1);
        }
        rsbillingmaster_count.close();
  %>
	<tr align="center" class="<%=bodd?"InnerSectionHead":""%>">
		<%if (!currentBillingNo.equals(billno)) {    %>
		<td rowspan="<%=billingmaster_count%>"><a href=#
			onClick="popupPage(600,800, '../../../billing/CA/BC/billingView.do?billing_no=<%=billno%>&receipt=yes')"><%=billno%>
		</a> <br />
		AMT: <%=fmt.format(billAmt)%> <br />
		</td>
		<%}    %>
		<td><%=apptMainBean.getString(rs,"billingmaster_no")%></td>
		<td><%=apptMainBean.getString(rs,"billing_code")%></td>
		<td><%=fmt.format(rs.getDouble("bill_amount"))%></td>
		<td><%=apptMainBean.getString(rs,"dx_code1")%> &nbsp</td>
		<td><%=apptMainBean.getString(rs,"billing_date")%></td>
		<td><%=apptMainBean.getString(rs,"billingtype")%></td>
		<td><%=billType%></td>
		<td><%=apptMainBean.getString(rs,"last_name")+","+apptMainBean.getString(rs,"first_name")%>
		</td>
		<%if (billCode.substring(0, 1).compareTo("B") == 0 || billCode.substring(0, 1).compareTo("S") == 0) {    %>
		<td>&nbsp;</td>
		<%} else {    %>
		<td><a
			href="billingDeleteNoAppt.jsp?billing_no=<%=apptMainBean.getString(rs,"billing_no")%>&billCode=<%=billCode%>&dboperation=delete_bill&hotclick=0">Unbill</a>
		</td>
		<%}    %>
	</tr>
	<%
    currentBillingNo = billno;
    } //else
        } //while
        apptMainBean.closePstmtConn();
  %>
</table>
<br>
<%
  int nLastPage = 0, nNextPage = 0;
  nNextPage = Integer.parseInt(strLimit2) + Integer.parseInt(strLimit1);
  nLastPage = Integer.parseInt(strLimit1) - Integer.parseInt(strLimit2);
  if (nLastPage >= 0) {
%> <a
	href="billinghistory.jsp?last_name=<%=URLEncoder.encode(request.getParameter("last_name")) %>&first_name=<%=URLEncoder.encode(request.getParameter("first_name")) %>&demographic_no=<%=request.getParameter("demographic_no")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last
Page</a> | <%
  }
      if (nItems == Integer.parseInt(strLimit2)) {
%> <a
	href="billinghistory.jsp?last_name=<%=URLEncoder.encode(request.getParameter("last_name")) %>&first_name=<%=URLEncoder.encode(request.getParameter("first_name")) %>&demographic_no=<%=request.getParameter("demographic_no")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">Next
Page</a> <%
  }
%>
<p><%@include file="../../../demographic/zfooterbackclose.jsp"%>
</center>
</body>
</html>

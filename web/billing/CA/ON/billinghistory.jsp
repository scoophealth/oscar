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
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->

<%
  if(session.getAttribute("user") == null)
    response.sendRedirect("../logout.htm");
  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
  String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user"); 
  //display the main provider page
  //includeing the provider name and a month calendar
  String strLimit1="0";
  String strLimit2="10";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");

  OscarProperties props = OscarProperties.getInstance();
  if(props.getProperty("isNewONbilling", "").equals("true")) {
  %>
<jsp:forward page="billingONHistory.jsp" />
<% } 
%>
<%@ page import="java.util.*, java.sql.*, java.net.*, oscar.*"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean"
	scope="session" />
<%@ include file="dbBilling.jspf"%>
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>BILLING HISTORY</title>
<link rel="stylesheet" href="billingON.css">
<script language="JavaScript">
<!--

//-->
</SCRIPT>
</head>
<body topmargin="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr class="myDarkGreen">
		<th><font color="#FFFFFF">BILLING HISTORY </font></th>
	</tr>
</table>

<table width="95%" border="0">
	<tr>
		<td align="left"><i>Results for Demographic</i> :<%=request.getParameter("last_name")%>,<%=request.getParameter("first_name")%>
		(<%=request.getParameter("demographic_no")%>)</td>
	</tr>
</table>
<CENTER>
<table width="100%" border="2" bgcolor="#ffffff">
	<tr class="myYellow">
		<TH align="center" width="5%"><b>INVOICE#</b></TH>
		<TH align="left" width="25%"><b>APPT DATE</b></TH>
		<TH align="center" width="10%"><b>BILL TYPE</b></TH>
		<TH align="center" width="15%"><b>BILL PROVIDER</b></TH>
		<TH align="center" width="15%"><b>APPT PROVIDER</b></TH>
		<TH align="center" width="10%"><b>COMMENTS</b></TH>
	</tr>
	<% // new billing records
JdbcBillingReviewImpl dbObj = new JdbcBillingReviewImpl();
List aL = dbObj.getBillingHist(request.getParameter("demographic_no"), Integer.parseInt(strLimit2), Integer.parseInt(strLimit1), null);
for(int i=0; i<aL.size(); i=i+2) {
	BillingClaimHeader1Data obj = (BillingClaimHeader1Data) aL.get(i);
%>
	<tr bgcolor="<%=i%2==0?"#CCFF99":"white"%>">
		<td width="5%" align="center" height="25"><a href=#
			onClick="popupPage(600,800, 'billingView.do?billing_no=<%=obj.getId()%>&dboperation=search_bill&hotclick=0')"><%=obj.getId()%></a>
		<security:oscarSec roleName="<%=roleName$%>"
			objectName="_admin,_admin.billing" rights="r">
			<a href=#
				onClick="popupPage(600,800, 'billingONCorrection.jsp?billing_no=<%=obj.getId()%>')">edit</a>
		</security:oscarSec></td>
		<td align="left" width="25%" height="25"><%=obj.getBilling_date()%>
		&nbsp; &nbsp; &nbsp; &nbsp; <%=obj.getBilling_time()%></td>
		<td align="center" width="10%" height="25"><%=BillingDataHlp.propBillingType.getProperty(obj.getStatus(),"")%></td>
		<td align="center" width="15%" height="25"><%=providerBean.getProperty(obj.getProviderNo(), "")%></td>
		<td align="center" width="15%" height="25"><%=providerBean.getProperty(obj.getApptProvider_no(), "")%></td>

		<% if (obj.getStatus().compareTo("B")==0 || obj.getStatus().compareTo("S")==0) { %>
		<td align="center" width="10%" height="25">&nbsp;</td>
		<% } else { %>
		<td align="center" width="10%" height="25"><a
			href="billingDeleteNoAppt.jsp?billing_no=<%=obj.getId()%>&billCode=<%=obj.getStatus()%>&dboperation=delete_bill&hotclick=0">Unbill</a></td>
		<% } %>
	</tr>
	<% 
}
%>
	<%
//OscarProperties props = OscarProperties.getInstance();
if(!props.getProperty("isNewONbilling", "").equals("true")) {
 String proFirst="";
 String proLast="";
 String proNo = "";
 ResultSet rslocal;
 rslocal = null;
   ResultSet rs=null ;
  rs = apptMainBean.queryResults(Integer.parseInt(request.getParameter("demographic_no")), "search_allbill_history");

  boolean bodd=false;
  int nItems=0;
  String billType="", billCode = "";
  if(rs==null) {
    out.println("failed!!!"); 
  } else {
    while (rs.next()) {
      bodd=bodd?false:true; //for the color of rows
      nItems++; //to calculate if it is the end of records
       billCode = rs.getString("status");
      if (billCode.compareTo("B") == 0){ billType = "Submitted OHIP"; }
       if (billCode.substring(0,1).compareTo("O") == 0){ billType = "Bill OHIP"; }
       if (billCode.substring(0,1).compareTo("N") == 0){ billType = "Do Not Bill"; }
       if (billCode.substring(0,1).compareTo("P") == 0){ billType = "Bill Patient"; }
       if (billCode.substring(0,1).compareTo("W") == 0){ billType = "Bill WCB"; }
       if (billCode.substring(0,1).compareTo("H") == 0){ billType = "Capitated"; }
   if (billCode.substring(0,1).compareTo("S") == 0){ billType = "Settled"; }     
  if (billCode.substring(0,1).compareTo("D") == 0){billType = "Deleted";}
  proNo = rs.getString("apptProvider_no")==null?"":rs.getString("apptProvider_no");
  
  if (proNo.compareTo("") ==0 || proNo.compareTo("000") ==0 || proNo.compareTo("none")==0){
  proFirst = "Not Available";
  proLast = "";
  }else{
  
  rslocal = apptMainBean.queryResults(proNo, "search_provider_all_dt");
   while(rslocal.next()){
   proFirst = rslocal.getString("first_name");
   proLast = rslocal.getString("last_name") + ",";

}
}
%>
	<tr bgcolor="<%=bodd?"ivory":"white"%>">
		<td width="5%" align="center" height="25"><a href=#
			onClick="popupPage(600,800, '../../../billing/CA/BC/billingView.do?billing_no=<%=rs.getString("billing_no")%>&dboperation=search_bill&hotclick=0')"><%=rs.getString("billing_no")%></a>
		<security:oscarSec roleName="<%=roleName$%>"
			objectName="_admin,_admin.billing" rights="r">
			<a href=#
				onClick="popupPage(600,800, 'billingCorrection.jsp?billing_no=<%=rs.getString("billing_no")%>')">edit</a>
		</security:oscarSec></td>
		<td align="left" width="25%" height="25"><%=rs.getString("billing_date")%>
		&nbsp; &nbsp; &nbsp; &nbsp; <%=rs.getString("billing_time")%></td>
		<td align="center" width="10%" height="25"><%=billType%></td>
		<td align="center" width="15%" height="25"><%=rs.getString("last_name")+","+rs.getString("first_name")%></td>
		<td align="center" width="15%" height="25"><%=proLast+" "+proFirst%></td>

		<% if (billCode.substring(0,1).compareTo("B")==0 || billCode.substring(0,1).compareTo("S")==0) { %>
		<td align="center" width="10%" height="25">&nbsp;</td>
		<% } else { %>
		<td align="center" width="10%" height="25"><a
			href="billingDeleteNoAppt.jsp?billing_no=<%=rs.getString("billing_no")%>&billCode=<%=billCode%>&dboperation=delete_bill&hotclick=0">Unbill</a></td>
		<% } %>
	</tr>
	<% 
    }
  }
%>

</table>
<br>
<%
  int nLastPage=0,nNextPage=0;
  nNextPage=Integer.parseInt(strLimit2)+Integer.parseInt(strLimit1);
  nLastPage=Integer.parseInt(strLimit1)-Integer.parseInt(strLimit2);
  if(nLastPage>=0) {
%> <a
	href="billinghistory.jsp?last_name=<%=URLEncoder.encode(request.getParameter("last_name")) %>&first_name=<%=URLEncoder.encode(request.getParameter("first_name")) %>&demographic_no=<%=request.getParameter("demographic_no")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nLastPage%>&limit2=<%=strLimit2%>">Last
Page</a> | <%
  }
  if(nItems==Integer.parseInt(strLimit2)) {
%> <a
	href="billinghistory.jsp?last_name=<%=URLEncoder.encode(request.getParameter("last_name")) %>&first_name=<%=URLEncoder.encode(request.getParameter("first_name")) %>&demographic_no=<%=request.getParameter("demographic_no")%>&displaymode=<%=request.getParameter("displaymode")%>&dboperation=<%=request.getParameter("dboperation")%>&orderby=<%=request.getParameter("orderby")%>&limit1=<%=nNextPage%>&limit2=<%=strLimit2%>">
Next Page</a> <%
}

}
%>
<p>
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

</center>
</body>
</html>

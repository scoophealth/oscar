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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@page import="java.math.BigDecimal"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%

  String curProvider_no;
  curProvider_no = (String) session.getAttribute("user");
  //display the main provider page
  //includeing the provider name and a month calendar
  String strLimit1="0";
  String strLimit2="10";
  if(request.getParameter("limit1")!=null) strLimit1 = request.getParameter("limit1");
  if(request.getParameter("limit2")!=null) strLimit2 = request.getParameter("limit2");
  
  LoggedInInfo loggedInInfo = LoggedInInfo.getLoggedInInfoFromSession(request);

%>
<%@ page
	import="java.util.*, java.sql.*, java.net.*, oscar.*, oscar.oscarDB.*"
	errorPage="errorpage.jsp"%>
<%@ page import="oscar.oscarBilling.ca.on.data.*"%>
<%@page import="org.oscarehr.billing.CA.ON.dao.*" %>
<%@page import="org.oscarehr.common.dao.BillingONExtDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>

<%@page import="org.oscarehr.common.dao.BillingONPaymentDao" %>
<%@page import="org.oscarehr.common.model.BillingONPayment" %>
<%@page import="org.oscarehr.common.dao.BillingONCHeader1Dao" %>
<%@page import="org.oscarehr.common.model.BillingONCHeader1" %>
<%
	BillingONPaymentDao billingOnPaymentDao = SpringUtils.getBean(BillingONPaymentDao.class);
	BillingONCHeader1Dao bCh1Dao = SpringUtils.getBean(BillingONCHeader1Dao.class);	
%>

<%@ taglib uri="/WEB-INF/oscar-tag.tld" prefix="oscar" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<jsp:useBean id="providerBean" class="java.util.Properties"
	scope="session" />
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>BILLING HISTORY</title>
<link rel="stylesheet" href="billingON.css">
<script language="JavaScript">
function onUnbilled(url) {
  if(confirm("<bean:message key="provider.appointmentProviderAdminDay.onUnbilled"/>")) {
    popupPage(700,720, url);
  }
}

function popUpClosed() {
    window.location.reload();
}
</SCRIPT>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script>
	jQuery.noConflict();
</script>

<oscar:customInterface section="billingONHistory"/>
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
<table width="100%" border="0" bgcolor="#ffffff">
	<tr class="myYellow">
		<TH width="12%"><b>Invoice No.</b></TH>
		<TH width="12%"><b>Billing Doctor</b></TH>
		<TH width="15%"><b>Appt. Date</b></TH>
		<TH width="10%"><b>Bill Type</b></TH>
		<TH width="35%"><b>Service Code</b></TH>
		<TH width="5%"><b>Dx</b></TH>
		<TH width="8%"><b>Balance</b></TH>
		<TH width="8%"><b>Fee</b></TH>
		<TH><b>COMMENTS</b></TH>
	</tr>
	<% // new billing records
JdbcBillingReviewImpl dbObj = new JdbcBillingReviewImpl();
BillingONExtDao billingOnExtDao = (BillingONExtDao)SpringUtils.getBean(BillingONExtDao.class);
String limit = " limit " + strLimit1 + "," + strLimit2;
List aL = dbObj.getBillingHist(loggedInInfo, request.getParameter("demographic_no"), Integer.parseInt(strLimit2), Integer.parseInt(strLimit1), null);
int nItems=0;
for(int i=0; i<aL.size(); i=i+2) {
	nItems++;
	BillingClaimHeader1Data obj = (BillingClaimHeader1Data) aL.get(i);
	BillingItemData itObj = (BillingItemData) aL.get(i+1);
	String strBillType = obj.getPay_program();
	if(strBillType != null) {
		if(strBillType.matches(BillingDataHlp.BILLINGMATCHSTRING_3RDPARTY)) {
			if(BillingDataHlp.getPropBillingType().getProperty(obj.getStatus(),"").equals("Settled")) {
				strBillType += " Settled";
			}
		} else {
			strBillType = BillingDataHlp.getPropBillingType().getProperty(obj.getStatus(),"");
		}
	} else {
		strBillType = "";
	}
	

	
	//BigDecimal balance = new BigDecimal("0.00");
	BigDecimal balance = new BigDecimal("0.00");
	if("PAT".equals(strBillType)||"PAT Settled".equals(strBillType)){
		int billingNo = Integer.parseInt(obj.getId());
		BillingONCHeader1 bCh1 = bCh1Dao.find(billingNo);
		
		BigDecimal total = bCh1.getTotal();
		BigDecimal sumOfPay = BigDecimal.ZERO;
		BigDecimal sumOfDiscount = BigDecimal.ZERO;
		BigDecimal sumOfRefund = BigDecimal.ZERO;
		BigDecimal sumOfCredit = BigDecimal.ZERO;
		
		for(BillingONPayment payment:billingOnPaymentDao.find3rdPartyPaymentsByBillingNo(billingNo)) {
			sumOfPay = sumOfPay.add(payment.getTotal_payment());
			sumOfDiscount = sumOfDiscount.add(payment.getTotal_discount());
			sumOfRefund = sumOfRefund.add(payment.getTotal_refund());
			sumOfCredit = sumOfCredit.add(payment.getTotal_credit());
		}
		
		balance = total.subtract(sumOfPay).subtract(sumOfDiscount).add(sumOfCredit);
	}
%>

	<tr bgcolor="<%=i%2==0?"#CCFF99":"white"%>">
		<td width="5%" align="center" height="25">
		<a href="javascript:void(0)" onClick="popupPage(600,800, 'billingONDisplay.jsp?billing_no=<%=obj.getId()%>')" title="Billing Display"><%=obj.getId()%></a>
		
		<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w">
		<a href="javascript:void(0)" onClick="popupPage(600,800, 'billingONCorrection.jsp?billing_no=<%=obj.getId()%>')" title="Billing Correction">Edit</a>
		</security:oscarSec>
		
		<a href="javascript:void(0)" onClick="popupPage(600,800, 'billingON3rdInv.jsp?billingNo=<%=obj.getId()%>')">Print</a>
		</td>
		<td align="center"><%=obj.getLast_name()+", "+obj.getFirst_name()%></td>
		<td align="center"><%=obj.getBilling_date()%> <%--=obj.getBilling_time()--%></td>
		<td align="center"><%=strBillType%></td>
		<td align="center"><%=itObj.getService_code()%></td>
		<td align="center"><%=itObj.getDx()%></td>
		<td align="center"><%if("PAT".equals(strBillType)||"PAT Settled".equals(strBillType)){ %>
			<%=balance %>
		<%}else{ %>
			<%="" %>
		<%} %></td>
		<td align="center"><%=obj.getTotal()%></td>

		<% if (obj.getStatus().compareTo("B")==0 || obj.getStatus().compareTo("S")==0) { %>
		<td align="center">&nbsp;</td>
		<% } else if (OscarProperties.getInstance().getBooleanProperty("warnOnDeleteBill","true")){ %>
		<td align="center"><a
			href="#" onClick="onUnbilled('billingDeleteNoAppt.jsp?billing_no=<%=obj.getId()%>&billCode=<%=obj.getStatus()%>&hotclick=0');return false;">Unbill</a>
                </td>
		<% } else { %>
                <td align="center">
			<a href="billingDeleteNoAppt.jsp?billing_no=<%=obj.getId()%>&billCode=<%=obj.getStatus()%>&dboperation=delete_bill&hotclick=0">Unbill</a></td>

                <% }%>
	</tr>
	<% 
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

//}
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

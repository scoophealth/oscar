<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report,_admin.reporting,_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_report&type=_admin.reporting&type=_admin");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat" errorPage="errorpage.jsp" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.TeleplanS21Dao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.TeleplanS21" %>

<%
	TeleplanS21Dao teleplanS21Dao = SpringUtils.getBean(TeleplanS21Dao.class);
%>

<jsp:useBean id="documentBean" class="oscar.DocumentBean" scope="request" />


<%
  GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);
  String amtbilled="", amtpaid="", balancefwd="", chequeamt="", newbalance="", raNo="";
  String paymentdate="" , payable="";
	String payeeNo = "";

%>



<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>

<html:base/>
<link rel="stylesheet" href="../../../billing/billing.css" >
<title>Billing Reconcilliation</title>

<script language="JavaScript">
<!--
    var remote=null;
    function refresh() {
        history.go(0);
    }
    function rs(n,u,w,h,x) {
      args="width="+w+",height="+h+",resizable=yes,scrollbars=yes,status=0,top=60,left=30";
      remote=window.open(u,n,args);
      if (remote != null) {
        if (remote.opener == null)
          remote.opener = self;
      }
      if (x == 1) { return remote; }
    }

    var awnd=null;
    function popPage(url) {
        awnd=rs('',url ,400,200,1);
        awnd.focus();
    }

    function checkReconcile(url){
        if(confirm("You are about to reconcile the file, are you sure?")) {
            location.href=url;
        }else{
            alert("You have cancel the action!");
        }
    }
//-->
</SCRIPT>
</head>

<body bgcolor="#EBF4F5" text="#000000" leftmargin="0" topmargin="0" marginwidth="0" marginheight="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%" >
   <tr bgcolor="#486ebd">
      <th align='LEFT'>
		   <input type='button' name='print' value='Print' onClick='window.print()'>
      </th>
      <th align='CENTER'  >
         <font face="Arial, Helvetica, sans-serif" color="#FFFFFF">Billing Reconcilliation </font>
      </th>
      <th align='RIGHT'>
         <input type='button' name='close' value='Close' onClick='window.close()'>
      </th>
   </tr>
</table>

<table width="100%" border="1" cellspacing="0" cellpadding="0" bgcolor="#EFEFEF">
   <form>
     <tr>
        <td width="5%" height="16">Payment Date </td>
        <td width="10%" height="16" align="right">Payable </td>
        <td width="10%" height="16" align="right">Amount Billed</td>
        <td width="10%" height="16" align="right">Amount Paid</td>
        <td width="10%" height="16" align="right">Balance Fwd</td>
        <td width="10%" height="16" align="right">Cheque Amount</td>
        <td width="10%" height="16" align="right">New Balance</td>
        <td width="20%" height="16">Action</td>
        <td width="5%" height="16">Status</td>
     </tr>

  <%
    
    for(TeleplanS21 result : teleplanS21Dao.search_all_tahd("D")) {
    
        raNo  = result.getId().toString();
        paymentdate = result.getPayment();
        payable = result.getPayeeName();
        amtbilled= result.getAmountBilled();
		payeeNo= result.getPayeeNo();
        amtpaid = result.getAmountPaid();
        balancefwd = result.getBalanceForward();
        chequeamt= result.getCheque();
        newbalance = result.getNewBalance();
   %>

     <tr>
        <td ><%=paymentdate%>  </td>
        <td align="right"><%=payable%> </td>
        <td align="right"><%=moneyFormat(amtbilled)%></td>
        <td align="right"><%=moneyFormat(amtpaid)%></td>
        <td align="right"><%=moneyFormat(balancefwd)%></td>
        <td align="right"><%=moneyFormat(chequeamt)%></td>
        <td align="right"><%=moneyFormat(newbalance)%></td>
        <td >&nbsp;&nbsp;
           Billed( <a href="createBillingReportAction.do?docFormat=pdf&repType=REP_MSPREM&rano=<%=raNo%>&selPayee=<%=payeeNo%>" target="_blank">PDF</a>|<a href="createBillingReportAction.do?docFormat=csv&repType=REP_MSPREM&rano=<%=raNo%>&selPayee=<%=payeeNo%>" target="_blank">CSV</a>) | 
           <a href="genTAS00.jsp?rano=<%=raNo%>&proNo=" target="_blank">Detail</a> |
           <a href="genTAS22.jsp?rano=<%=raNo%>&proNo=" target="_blank">Summary</a> 
           ( <a href="createBillingReportAction.do?docFormat=pdf&repType=REP_MSPREMSUM&rano=<%=raNo%>&proNo=" target="_blank">PDF</a>|<a href="createBillingReportAction.do?docFormat=csv&repType=REP_MSPREMSUM&rano=<%=raNo%>&proNo=" target="_blank">CSV</a>)
        </td>
        <td ><%=result.getStatus()%></td>
     </tr>
     <tr>
        <td colspan="10" bgcolor="#EBF4F5">&nbsp;</td>
     </tr>
 <% }%>

</table>

</body>
</html>
<%!
    String moneyFormat(String str){
        String moneyStr = "0.00";
        try{
            moneyStr = new java.math.BigDecimal(str).movePointLeft(2).toString();
        }catch (Exception moneyException) {
        	MiscUtils.getLogger().error("Error", moneyException);
            moneyStr = str;
        }
    return moneyStr;
    }
%>

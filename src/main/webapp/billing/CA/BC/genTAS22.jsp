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
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>
<%@page import="org.oscarehr.util.MiscUtils"%>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.TeleplanS00Dao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.TeleplanS00" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.TeleplanS22Dao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.TeleplanS22" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.TeleplanS23Dao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.TeleplanS23" %>
<%@page import="org.oscarehr.billing.CA.BC.dao.TeleplanS25Dao" %>
<%@page import="org.oscarehr.billing.CA.BC.model.TeleplanS25" %>
<%
	TeleplanS00Dao teleplanS00Dao = SpringUtils.getBean(TeleplanS00Dao.class);
    TeleplanS22Dao teleplanS22Dao = SpringUtils.getBean(TeleplanS22Dao.class);
	TeleplanS23Dao teleplanS23Dao = SpringUtils.getBean(TeleplanS23Dao.class);
	TeleplanS25Dao teleplanS25Dao = SpringUtils.getBean(TeleplanS25Dao.class);
%>


<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<link rel="stylesheet" href="billing.css">
<title>Teleplan Reconcilliation</title>
</head>

<body bgcolor="#EBF4F5" text="#000000" leftmargin="0" topmargin="0"
	marginwidth="0" marginheight="0">

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align='LEFT'><input type='button' name='print' value='Print'
			onClick='window.print()'></th>
		<th align='CENTER'><font face="Arial, Helvetica, sans-serif"
			color="#FFFFFF">Teleplan Reconcilliation - Summary Report</font></th>
		<th align='RIGHT'><input type='button' name='close' value='Close'
			onClick='window.close()'></th>
	</tr>
</table>
<%
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);
  
  String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);
%>

<% String raNo = "", flag="", plast="", pfirst="", pohipno="", proNo="";
String filepath="", filename = "", header="", headerCount="", total="", paymentdate="", payable="", totalStatus="", deposit=""; //request.getParameter("filename");
String transactiontype="", providerno="", specialty="", account="", patient_last="", patient_first="", provincecode="", hin="", ver="", billtype="", location="";
String servicedate="", serviceno="", servicecode="", amountsubmit="", amountpay="", amountpaysign="", explain="", error="";
String proFirst="", proLast="", demoFirst="", demoLast="", apptDate="", apptTime="", checkAccount="";



      proNo = request.getParameter("proNo");
      raNo = request.getParameter("rano");
      if (raNo.compareTo("") == 0 || raNo == null){
      flag = "0";
      }else{
      
      %>
<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#333333">
		<th align='CENTRE'>
		<form action="genTAS22.jsp"><input type="hidden" name="rano"
			value="<%=raNo%>"> <select name="proNo">
			<option value="all" <%=proNo.equals("all")?"selected":""%>>All
			Providers</option>
			<%   
                        ResultSet rsdemo3 = null;
                        ResultSet rsdemo2 = null;
                        for(Object[] result : teleplanS00Dao.search_taprovider(Integer.parseInt(raNo))) {
                       		
                            pohipno = (String)result[0];
                            plast = (String)result[1];
                            pfirst =(String)result[2];
                         %>
			<option value="<%=pohipno%>" <%=proNo.equals(pohipno)?"selected":""%>><%=plast%>,<%=pfirst%></option>
			<%  }  %>
		</select> <input type=submit name=submit value=Generate></form>
		</th>
	</tr>
</table>


<% if (proNo.compareTo("") == 0 || proNo.compareTo("all") == 0 || proNo == null){  
         proNo = "%"; 
         }%>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	bgcolor="#EFEFEF">
	<tr>
		<td width="10%" height="16">Payment Date</td>
		<td width="10%" height="16">Practitioner #</td>
		<td width="10%" height="16">Practitioner Name</td>
		<td width="10%" height="16" align="right">Billed</td>
		<td width="10%" height="16" align="right">Paid</td>
		<td width="50%" height="16">Status &nbsp;</td>


	</tr>
	<%

          String[] param0 = new String[2];
        
          for(TeleplanS22 result : teleplanS22Dao.search_taS22(Integer.parseInt(raNo), "S01", proNo)) {
         
      %>
	<tr>


		<td width="10%" height="16"><%=result.getPayment()%></td>
		<td width="10%" height="16"><%=result.getPractitionerNo()%></td>
		<td width="10%" height="16"><%=result.getPractitionerName()%></td>
		<td width="10%" height="16" align="right"><%=moneyFormat(result.getAmountBilled())%></td>
		<td width="10%" height="16" align="right"><%=moneyFormat(result.getAmountPaid())%></td>
		<td width="50%" height="16"><%=String.valueOf(result.getLineCode()).compareTo("Y")==0?"Practitioner Totals within Payee":""%></td>

	</tr>


	<%
      }
      %>


</table>
<table width="100%" border="1" cellspacing="0" cellpadding="0"
	bgcolor="#EFEFEF">
	<tr>
		<td width="10%" height="16">Payment Date</td>
		<td width="5%" height="16">Payee</td>
		<td width="5%" height="16">AJ. Code</td>
		<td width="10%" height="16">Pract #</td>
		<td width="10%" height="16">Practitioner</td>
		<td width="10%" height="16">Calc Method</td>
		<td width="5%" height="16">Regular %</td>
		<td width="5%" height="16">One-Time %</td>
		<td width="5%" height="16">Net AMT</td>
		<td width="5%" height="16">Regular AMT</td>
		<td width="5%" height="16">Onetime AMT</td>
		<td width="5%" height="16">Balance FWD.</td>
		<td width="10%" height="16">AJ. Made</td>
		<td width="10%" height="16">AJ. Outstanding</td>


	</tr>
	<%                        
               
                for(TeleplanS23 result : teleplanS23Dao.search_taS23(Integer.parseInt(raNo), "S01", proNo)) {
               
             %>
	<tr>

		<td width="10%" height="16"><%=result.getPayment()%>&nbsp;</td>
		<td width="5%" height="16"><%=result.getPayeeNo()%>&nbsp;</td>
		<td width="5%" height="16"><%=result.getAjc()%>&nbsp;</td>
		<td width="10%" height="16"><%=result.getAji()%>&nbsp;</td>
		<td width="10%" height="16"><%=result.getAjm()%>&nbsp;</td>
		<td width="10%" height="16"><%=result.getCalcMethod()%>&nbsp;</td>
		<td width="5%" height="16" align="right"><%=moneyFormat(result.getrPercent())%>&nbsp;</td>
		<td width="5%" height="16" align="right"><%=moneyFormat(result.getoPercent())%>&nbsp;</td>
		<td width="5%" height="16" align="right"><%=moneyFormat(result.getgAmount())%>&nbsp;</td>
		<td width="5%" height="16" align="right"><%=moneyFormat(result.getrAmount())%>&nbsp;</td>
		<td width="5%" height="16" align="right"><%=moneyFormat(result.getoAmount())%>&nbsp;</td>
		<td width="5%" height="16" align="right"><%=moneyFormat(result.getBalanceForward())%>&nbsp;</td>
		<td width="10%" height="16" align="right"><%=moneyFormat(result.getAdjMade())%>&nbsp;</td>
		<td width="10%" height="16" align="right"><%=moneyFormat(result.getAdjOutstanding())%>&nbsp;</td>
	</tr>


	<%  }  %>
</table>

<table width="100%" border="1" cellspacing="0" cellpadding="0"
	bgcolor="#EFEFEF">
	<tr>
		<td width="10%" height="16">Payment Date</td>
		<td width="10%" height="16">Payee</td>
		<td width="10%" height="16">Practitioner</td>
		<td width="70%" height="16">Message</td>


	</tr>
	<%
	                
	                
	                   String[] param2 = new String[3];
	                   
	                    
	                    for(TeleplanS25 result : teleplanS25Dao.search_taS25(Integer.parseInt(raNo),"S01",proNo)) {
	                   
	                 %>
	<tr>

		<td width="10%" height="16"><%=result.getPayment()%>&nbsp;</td>
		<td width="10%" height="16"><%=result.getPayeeNo()%>&nbsp;</td>
		<td width="10%" height="16"><%=result.getPractitionerNo()%>&nbsp;</td>
		<td width="70%" height="16"><%=result.getMessage()%>&nbsp;</td>
	</tr>


	<%  }  %>
</table>
<%
      
      
      
      }
     %>









</body>
</html>
<%!
    String moneyFormat(String str){       
        String moneyStr = "0.00";
        try{             
            moneyStr = new java.math.BigDecimal(str).movePointLeft(2).toString();
        }catch (Exception moneyException) { MiscUtils.getLogger().error("Error", moneyException); }
    return moneyStr;
    }
%>

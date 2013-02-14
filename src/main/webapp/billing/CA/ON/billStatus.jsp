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

<%@ page
	import="java.math.*,java.util.*, java.sql.*, oscar.*, java.net.*,oscar.oscarBilling.ca.bc.MSP.*,oscar.util.*,oscar.oscarProvider.data.*,oscar.oscarBilling.ca.on.data.*"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%--
The taglib directive below imports the JSTL library. If you uncomment it,
you must also add the JSTL library to the project. The Add Library... action
on Libraries node in Projects view can be used to add the JSTL 1.1 library.
--%>
<%--
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
--%>

<%

OscarProperties props = OscarProperties.getInstance();
if(props.getProperty("isNewONbilling", "").equals("true")) {
%>
<jsp:forward page="billingONStatus.jsp" />
<% } %>
<%
ProviderData pd = new ProviderData();
ArrayList pList = pd.getProviderList();

BillingData billingData = new BillingData();
RAData raData = new RAData();

String statusType = request.getParameter("billTypes");
String providerNo = request.getParameter("providerview");
String startDate  = request.getParameter("xml_vdate"); 
String endDate    = request.getParameter("xml_appointment_date");
String demoNo     = request.getParameter("demographicNo");

if ( statusType == null ) { statusType = "O"; } 
if ( startDate == null ) { startDate = ""; } 
if ( endDate == null ) { endDate = ""; } 
if ( demoNo == null ) { demoNo = ""; } 
if ( providerNo == null ) { providerNo = "" ; } 

ArrayList bList = billingData.getBills( statusType,  providerNo, startDate,  endDate,  demoNo);

BigDecimal total = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP); 
BigDecimal paidTotal = new BigDecimal(0).setScale(2, BigDecimal.ROUND_HALF_UP); 

%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">


<%@page import="org.oscarehr.util.MiscUtils"%><html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title>Bill Status</title>
<script type="text/javascript" src="../../../share/javascript/Oscar.js"></script>
<link rel="stylesheet" type="text/css" media="all"
	href="../../../share/calendar/calendar.css" title="win2k-cold-1" />
<script type="text/javascript" src="../../../share/calendar/calendar.js"></script>
<script type="text/javascript"
	src="../../../share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
<script type="text/javascript"
	src="../../../share/calendar/calendar-setup.js"></script>
<script type="text/javascript">
        function fillEndDate(d){
           document.serviceform.xml_appointment_date.value= d;  
        }
        function setDemographic(demoNo){
           //alert(demoNo);
           document.serviceform.demographicNo.value = demoNo;
        }
        </script>
<link rel="stylesheet" type="text/css" media="all" href="../share/css/extractedFromPages.css"  />
</head>
<body>

<table width="100%" border="1" cellspacing="0" cellpadding="0">
	<tr bgcolor="#FFFFFF">
		<div align="right"><a
			href="javascript: function myFunction() {return false; }"
			onClick="popupPage(700,720,'../../../oscarReport/manageProvider.jsp?action=billingreport')"><font
			face="Arial, Helvetica, sans-serif" size="1">Manage Provider
		List </font></a></div>
	</tr>
</table>
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Billing</font></font></b></font></p>
		</td>
		<td nowrap valign="bottom"><font
			face="Verdana, Arial, Helvetica, sans-serif" color="#FFFFFF"><b><%=DateUtils.sumDate("yyyy-M-d","0")%></b></font>
		</td>
	</tr>
</table>
<form name="serviceform" method="get" action="billStatus.jsp">


<div class="selectionForm" align="center">Select Provider <select
	name="providerview">
	<option value="all">All Providers</option>
	<% for (int i = 0 ; i < pList.size(); i++) { 
       Hashtable h = (Hashtable) pList.get(i);%>
	<option value="<%=h.get("providerNo")%>"
		<%=providerNo.equals(h.get("providerNo"))?"selected":""%>><%=h.get("firstName")%>,
	<%=h.get("lastName")%></option>
	<% } %>
</select> <input type="submit" name="Submit" value="Create Report">
<div>Service Date-Range&nbsp;&nbsp; <font size="1"><a
	href="javascript: function myFunction() {return false; }" id="hlSDate">Begin:</a></font>
<input type="text" name="xml_vdate" id="xml_vdate"
	value="<%=startDate%>"> <font size="1"><a
	href="javascript: function myFunction() {return false; }" id="hlADate">End:</a></font>
<input type="text" name="xml_appointment_date" id="xml_appointment_date"
	value="<%=endDate%>"> <a
	href="javascript: function myFunction() {return false; }"
	onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-M-d","-30")%>')">30</a>&nbsp;
<a href="javascript: function myFunction() {return false; }"
	onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-M-d","-60")%>')">60</a>&nbsp;
<a href="javascript: function myFunction() {return false; }"
	onClick="fillEndDate('<%=DateUtils.sumDate("yyyy-M-d","-90")%>')">90</a>&nbsp;

Demographic:<input type="text" name="demographicNo" size="5"
	value="<%=demoNo%>" /> <input type='button' name='print' value='Print'
	onClick='window.print()'></div>

</div>

<div class="statusTypeList">
<ul>
	<li><input type="radio" name="billTypes" value="H"
		<%=statusType.equals("H")?"checked":""%>>Capitated</input></li>
	<li><input type="radio" name="billTypes" value="O"
		<%=statusType.equals("O")?"checked":""%>>Bill OHIP</input></li>
	<li><input type="radio" name="billTypes" value="P"
		<%=statusType.equals("P")?"checked":""%>>Bill Patient</input></li>
	<li><input type="radio" name="billTypes" value="N"
		<%=statusType.equals("N")?"checked":""%>>Do Not Bill</input></li>
	<li><input type="radio" name="billTypes" value="W"
		<%=statusType.equals("W")?"checked":""%>>WCB</input></li>
	<li><input type="radio" name="billTypes" value="B"
		<%=statusType.equals("B")?"checked":""%>>Submitted OHIP</input></li>
	<li><input type="radio" name="billTypes" value="S"
		<%=statusType.equals("S")?"checked":""%>>Settled/Paid by OHIP</input></li>
	<li><input type="radio" name="billTypes" value="X"
		<%=statusType.equals("X")?"checked":""%>>Bad Debt</input></li>
	<li><input type="radio" name="billTypes" value="D"
		<%=statusType.equals("D")?"checked":""%>>Deleted Bill</input></li>
	<li><input type="radio" name="billTypes" value="%"
		<%=statusType.equals("%")?"checked":""%>>All</input></li>
</ul>
</div>
</form>
<div class="tableListing">
<table>
	<tr>
		<th>SERVICE DATE</th>
		<th>PATIENT</th>
		<th title="Status">STAT</th>
		<th title="Code Billed">CODE</th>
		<th title="Amount Billed">BILLED</th>
		<th title="Amount Paid">PAID</th>
		<th>DX1</th>
		<th>DX2</th>
		<th>DX3</th>
		<th>ACCOUNT</th>
		<th>MESSAGES</th>
	</tr>


	<% 

       for (int i = 0 ; i < bList.size(); i++) { 
       Hashtable h = (Hashtable) bList.get(i);    
       ArrayList raList = raData.getRAData((String)h.get("billing_no"));
       boolean incorrectVal = false;
       
       BigDecimal valueToAdd = new BigDecimal("0.00"); 
       try{
          valueToAdd = new BigDecimal(""+h.get("total")).setScale(2, BigDecimal.ROUND_HALF_UP);  
       }catch(Exception badValueException){ 
          MiscUtils.getLogger().error(" Error calculating value for "+h.get("billing_no")); 
          incorrectVal = true;
       }
       total = total.add(valueToAdd);
       String amountPaid = raData.getAmountPaid(raList);
       paidTotal.add(new BigDecimal(amountPaid).setScale(2,BigDecimal.ROUND_HALF_UP));
       
       %>
	<tr>
		<td><%=h.get("billing_date")%> <%=h.get("billing_time")%></td>
		<!--SERVICE DATE-->
		<td><a
			href="javascript: setDemographic('<%=h.get("demographic_no")%>');"><%=h.get("demographic_name")%></a></td>
		<!--PATIENT-->
		<td><%=h.get("status")%></td>
		<!--STAT-->
		<td>&nbsp;</td>
		<!--CODE-->
		<td><%=h.get("total")%></td>
		<!--BILLED-->
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="javascript:popup(700,700,'billingRAView.jsp?billing_no=<%=h.get("billing_no")%>','RAView<%=h.get("billing_no")%>')">
		<%=amountPaid%> </a></td>
		<!--PAID-->
		<td>&nbsp;</td>
		<!--DX1-->
		<td>&nbsp;</td>
		<!--DX2-->
		<td>&nbsp;</td>
		<!--DX3-->
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="javascript:popup(700,700,'../../../billing/CA/BC/billingView.do?billing_no=<%=h.get("billing_no")%>','BillView<%=h.get("billing_no")%>')"><%=h.get("billing_no")%></a>
		</td>
		<!--ACCOUNT-->
		<td><a href="javascript: function myFunction() {return false; }"
			onclick="javascript:popup(700,700,'billingCorrection.jsp?billing_no=<%=h.get("billing_no")%>','BillCorrection<%=h.get("billing_no")%>')">Edit</a>
		<%=raData.getErrorCodes(raList)%></td>
		<!--MESSAGES-->
	</tr>
	<% } %>

	<tr>
		<td>Count:</td>
		<td><%=bList.size()%></td>
		<td>&nbsp;</td>
		<!--STAT-->
		<td>Total:</td>
		<!--CODE-->
		<td><%=total.toString()%></td>
		<!--BILLED-->
		<td><%=paidTotal.toString()%></td>
		<!--PAID-->
		<td>&nbsp;</td>
		<!--DX1-->
		<td>&nbsp;</td>
		<!--DX2-->
		<td>&nbsp;</td>
		<!--DX3-->
		<td>&nbsp;</td>
		<!--ACCOUNT-->
		<td>&nbsp;</td>
		<!--MESSAGES-->
	</tr>
	<table>
		</div>
		<script language='javascript'>
       Calendar.setup({inputField:"xml_vdate",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlSDate",singleClick:true,step:1});          
       Calendar.setup({inputField:"xml_appointment_date",ifFormat:"%Y-%m-%d",showsTime:false,button:"hlADate",singleClick:true,step:1});                      
   </script>
</body>
</html>

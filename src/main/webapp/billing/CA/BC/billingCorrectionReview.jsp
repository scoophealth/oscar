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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title></title>
</head>
<body bgcolor="#FFFFFF" text="#000000" topmargin="5" leftmargin="0"
	rightmargin="0">
<%@ page import="oscar.*,java.text.*, java.util.*"%>
<jsp:useBean id="billing" scope="session" class="oscar.BillingBean" />
<jsp:useBean id="billingItem" scope="page" class="oscar.BillingItemBean" />
<jsp:useBean id="billingDataBean" class="oscar.BillingDataBean"
	scope="session" />
<jsp:useBean id="billingPatientDataBean"
	class="oscar.BillingPatientDataBean" scope="session" />
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr bgcolor="#000000">
		<td height="40" width="10%"></td>
		<td width="90%" align="left">
		<p><font face="Verdana, Arial, Helvetica, sans-serif"
			color="#FFFFFF"><b><font
			face="Arial, Helvetica, sans-serif" size="4">oscar<font
			size="3">Billing - Correction</font></font></b></font></p>
		</td>
	</tr>
</table>
<%
 
 try {
 
 // BillingDataBean billingDataBean = new BillingDataBean();
 java.lang.String _p0_0 = billingDataBean.getUpdate_date(); //throws an exception if empty
 java.lang.String _p0_1 = billingDataBean.getBilling_no(); //throws an exception if empty
 java.lang.String _p0_2 = billingDataBean.getHin(); //throws an exception if empty
 java.lang.String _p0_3 = billingDataBean.getVisittype(); //throws an exception if empty
 java.lang.String _p0_4 = billingDataBean.getVisitdate(); //throws an exception if empty
 java.lang.String _p0_5 = billingDataBean.getStatus(); //throws an exception if empty
 java.lang.String _p0_6 = billingDataBean.getDob(); //throws an exception if empty
 java.lang.String _p0_7 = billingDataBean.getProviderNo(); //throws an exception if empty
 java.lang.String _p0_8 = billingDataBean.getClinic_ref_code(); //throws an exception if empty
 java.lang.String _p0_9 = billingDataBean.getBilling_date(); //throws an exception if empty
 java.lang.String _p0_10 = billingPatientDataBean.getDemoname(); //throws an exception if empty
 java.lang.String _p0_11 = billingPatientDataBean.getAddress(); //throws an exception if empty
 java.lang.String _p0_12 = billingPatientDataBean.getProvince(); //throws an exception if empty
 java.lang.String _p0_13 = billingPatientDataBean.getCity(); //throws an exception if empty
 java.lang.String _p0_14 = billingPatientDataBean.getPostal(); //throws an exception if empty
 java.lang.String _p0_15 = billingPatientDataBean.getSex(); //throws an exception if empty
  java.lang.String _p0_16 = billingDataBean.getContent(); //throws an exception if empty
  java.lang.String _p0_17 = "";
   java.lang.String _p0_18 = billingDataBean.getTotal(); //throws an exception if empty
   java.lang.String _p0_19 = "";
 
 %>

<table width="600" border="0">
	<tr>
		<td width="293"><b><font face="Arial, Helvetica, sans-serif"><u>Correction
		Review</u></font></b></td>
		<td width="297"><font size="2"
			face="Arial, Helvetica, sans-serif"><b>Last update: <%=_p0_0%></b></font></td>
	</tr>
</table>
<br>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td colspan="2" height="21"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><font size="3">Patient
		Information</font></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Patient Name: <%=_p0_10%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Health# : <%=_p0_2%></font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><font size="2" face="Arial, Helvetica, sans-serif"><b>Sex:
		<%=_p0_15%></b></font></td>
		<td><font size="2"><b><font
			face="Arial, Helvetica, sans-serif">D.O.B. : <%=_p0_6%></font></b></font></td>
	</tr>
	<tr>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Address:
		<%=_p0_11%></font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">City:
		<%=_p0_13%></font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Province:
		<%=_p0_12%></font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Postal
		Code: <%=_p0_14%></font></b></td>
	</tr>
	<tr>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Referral:
		<%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<rd>","</rd>")%></font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif">Referral
		#: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<rdohip>","</rdohip>")%></font></b></td>
	</tr>
</table>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td colspan="2"><font face="Arial, Helvetica, sans-serif"><strong>Additional
		Information</strong></font></td>
	</tr>
	<tr>
		<td width="320"><strong><font size="2"
			face="Arial, Helvetica, sans-serif">HC-Type: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<hctype>","</hctype>")%>
		</font></strong></td>
		<td width="270"><strong><font size="2"
			face="Arial, Helvetica, sans-serif">Manual Review: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<mreview>","</mreview>").equals("checked")?"Yes":"N/A"%>
		</font></strong></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><strong><font size="2"
			face="Arial, Helvetica, sans-serif">Referral Doctor: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<xml_referral>","</xml_referral>").equals("checked")?"Yes":"N/A"%>
		</font></strong></td>
		<td><strong><font size="2"
			face="Arial, Helvetica, sans-serif">Roster Status: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<xml_roster>","</xml_roster>")%>
		</font></strong></td>
	</tr>
</table>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td colspan="2"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><font size="3">Billing
		Information</font></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Billing Type: <%=_p0_5%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Billing Date: <%=_p0_9%></font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Visit Location: <%=_p0_8%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Billing Physician#: <%=_p0_7%> </font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Visit Type: <%=_p0_3%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Visit Date: <%=_p0_4%></font></b></td>
	</tr>
</table>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td width="25%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Service Code</font></b></td>
		<td width="50%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Description</font></b></td>
		<td width="12%"><b><font face="Arial, Helvetica, sans-serif"
			size="2">Quantity</font></b></td>
		<td width="13%">
		<div align="right"><b><font
			face="Arial, Helvetica, sans-serif" size="2">$ Fee</font></b></div>
		</td>
	</tr>
	<%
    ListIterator it	=	billing.getBillingItems().listIterator();
 
	while (it.hasNext()) {
    billingItem = (BillingItemBean)it.next();
    _p0_17=billingItem.getDiag_code();
    _p0_19=billingItem.getService_value();
   %>
	<tr>
		<td width="25%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=billingItem.getService_code()%></font></td>

		<td width="50%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=billingItem.getDesc()%></font></td>
		<td width="12%"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=billingItem.getQuantity()%></font></td>
		<td width="13%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=_p0_19.substring(0,_p0_19.length()-2)+"."+_p0_19.substring(_p0_19.length()-2)%></font></div>
		</td>
	</tr>

	<%
  }
  
  %>
	<tr bgcolor="#CCCCFF">
		<td colspan="4"><font face="Arial, Helvetica, sans-serif"
			size="2"><b>Diagnostic Code</b></font></td>

	</tr>
	<tr>
		<td colspan="4"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=_p0_17%></font></td>

	</tr>
	<tr>
		<td width="25%">&nbsp;</td>
		<td width="50%">&nbsp;</td>
		<td width="12%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2">Total: </font></div>
		</td>
		<td width="13%">
		<div align="right"><font face="Arial, Helvetica, sans-serif"
			size="2"><%=_p0_18.substring(0,_p0_18.length()-2)+"."+_p0_18.substring(_p0_18.length()-2)%></font></div>
		</td>
	</tr>
	<%
  }
   catch (java.lang.ArrayIndexOutOfBoundsException _e0) {
 }%>
</table>
<form action="billingCorrectionSubmit.jsp"><input type="submit"
	name="submit" value="Submit"><input type="button" name="cancel"
	value="Cancel" onclick="history.go(-1);return false;"></form>
</body>
</html>

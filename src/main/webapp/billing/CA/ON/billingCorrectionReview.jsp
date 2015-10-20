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
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<html:html locale="true">
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<title><bean:message key="billing.billingCorrection.title" /></title>
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
			face="Arial, Helvetica, sans-serif" size="4"><bean:message
			key="billing.billingCorrection.msgBillingCorrection" /></font></b></font></p>
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
		<td width="293"><b><font face="Arial, Helvetica, sans-serif"><u><bean:message
			key="billing.billingCorrection.msgCorrectionReview" /></u></font></b></td>
		<td width="297"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><bean:message
			key="billing.billingCorrection.msgLastUpdate" />: <%=_p0_0%></b></font></td>
	</tr>
</table>
<br>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td colspan="2" height="21"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><font size="3"><bean:message
			key="billing.billingCorrection.msgPatientInformation" /></font></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message key="billing.billingCorrection.msgName" />:
		<%=_p0_10%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgHealthNo" /> : <%=_p0_2%></font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><font size="2" face="Arial, Helvetica, sans-serif"><b><bean:message
			key="billing.billingCorrection.msgSex" />: <%=_p0_15%></b></font></td>
		<td><font size="2"><b><font
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgDOB" /> : <%=_p0_6%></font></b></font></td>
	</tr>
	<tr>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgAddress" />: <%=_p0_11%></font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgCity" />: <%=_p0_13%></font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgProvince" />: <%=_p0_12%></font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgPostalCode" />: <%=_p0_14%></font></b></td>
	</tr>
	<tr>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgReferal" />: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<rd>","</rd>")%></font></b></td>
		<td><b><font size="2" face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgReferealNo" />: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<rdohip>","</rdohip>")%></font></b></td>
	</tr>
</table>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td colspan="2"><font face="Arial, Helvetica, sans-serif"><strong><bean:message
			key="billing.billingCorrection.msgAdditionalInf" /></strong></font></td>
	</tr>
	<tr>
		<td width="320"><strong><font size="2"
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgHCType" />: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<hctype>","</hctype>")%>
		</font></strong></td>
		<td width="270"><strong><font size="2"
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgManualReview" />: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<mreview>","</mreview>").equals("checked")?"Yes":"N/A"%>
		</font></strong></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td><strong><font size="2"
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgReferralDoctor" />: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<xml_referral>","</xml_referral>").equals("checked")?"Yes":"N/A"%>
		</font></strong></td>
		<td><strong><font size="2"
			face="Arial, Helvetica, sans-serif"><bean:message
			key="billing.billingCorrection.msgRosterStatus" />: <%=SxmlMisc.getXmlContent(billingDataBean.getContent(), "<xml_roster>","</xml_roster>")%>
		</font></strong></td>
	</tr>
</table>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td colspan="2"><font size="2"
			face="Arial, Helvetica, sans-serif"><b><font size="3"><bean:message
			key="billing.billingCorrection.msgBillingInf" /></font></b></font></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgBillingType" />: <%=_p0_5%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgBillingDate" />: <%=_p0_9%></font></b></td>
	</tr>
	<tr bgcolor="#EEEEFF">
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgVisitLocation" />: <%=_p0_8%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgBillingPhysicianNo" />: <%=_p0_7%>
		</font></b></td>
	</tr>
	<tr>
		<td width="54%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgVisitType" />: <%=_p0_3%></font></b></td>
		<td width="46%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgVisitDate" />: <%=_p0_4%></font></b></td>
	</tr>
</table>
<table width="600" border="0">
	<tr bgcolor="#CCCCFF">
		<td width="25%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgServiceCode" /></font></b></td>
		<td width="50%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgDescription" /></font></b></td>
		<td width="12%"><b><font face="Arial, Helvetica, sans-serif"
			size="2"><bean:message
			key="billing.billingCorrection.msgQuantity" /></font></b></td>
		<td width="13%">
		<div align="right"><b><font
			face="Arial, Helvetica, sans-serif" size="2"><bean:message
			key="billing.billingCorrection.msgFee" /></font></b></div>
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
			size="2"><b><bean:message
			key="billing.billingCorrection.msgDiagCode" /></b></font></td>

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
			size="2"><bean:message
			key="billing.billingCorrection.msgTotal" />: </font></div>
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
	name="submit"
	value="<bean:message key="billing.billingCorrection.btnSubmit"/>"><input
	type="button" name="cancel"
	value="<bean:message key="billing.billingCorrection.btnCancel"/>"
	onclick="history.go(-1);return false;"></form>
</body>
</html:html>

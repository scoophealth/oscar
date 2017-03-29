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
<title><bean:message key="billing.billingCorrectionSubmit.title" /></title>
</head>
<body>
<%@ page import="oscar.*,java.text.*, java.util.*"%>
<jsp:useBean id="billingBean" scope="session" class="oscar.BillingBean" />
<jsp:useBean id="billingItem" scope="page" class="oscar.BillingItemBean" />
<jsp:useBean id="billingDataBean" class="oscar.BillingDataBean"	scope="session" />
<jsp:useBean id="billingPatientDataBean" class="oscar.BillingPatientDataBean" scope="session" />

<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail"%>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao"%>
<%@ page import="org.oscarehr.common.model.RecycleBin" %>
<%@ page import="org.oscarehr.common.dao.RecycleBinDao" %>
<%@ page import="org.oscarehr.common.model.Billing" %>
<%@ page import="org.oscarehr.common.dao.BillingDao" %>
<%@ page import="oscar.util.ConversionUtils" %>
<%
	BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
	RecycleBinDao recycleBinDao = SpringUtils.getBean(RecycleBinDao.class);
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
%>

<table border="0" cellspacing="0" cellpadding="0" width="100%">
	<tr bgcolor="#486ebd">
		<th align=CENTER NOWRAP><font face="Helvetica" color="#FFFFFF"><bean:message
			key="billing.billingCorrectionSubmit.msgSuccessfull" /></font></th>
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

  GregorianCalendar now=new GregorianCalendar();

	RecycleBin recycleBin = new RecycleBin();
	recycleBin.setProviderNo((String) session.getAttribute("user"));
	recycleBin.setTableName("billing");
	recycleBin.setTableContent(_p0_16);
	recycleBin.setUpdateDateTime(new java.util.Date());
	recycleBinDao.persist(recycleBin);

	int rowsAffected2 = 0;
	for(BillingDetail b:billingDetailDao.findByBillingNo(Integer.parseInt(_p0_1))) {
		b.setStatus("D");
		billingDetailDao.merge(b);
		rowsAffected2++;
	}
	
  if(_p0_18 == null) _p0_18 = "000";
	  
	  Billing b = billingDao.find(Integer.parseInt(_p0_1));
	  if(b != null) {
		 b.setHin(_p0_2);
		 b.setDob(_p0_6);
		 b.setVisitType(_p0_3);
		 b.setVisitDate(ConversionUtils.fromDateString(_p0_4));
		 b.setClinicRefCode(_p0_8);
		 b.setProviderNo(_p0_7);
		 b.setStatus(_p0_5);
		 b.setUpdateDate(ConversionUtils.fromDateString(now.get(Calendar.YEAR)+"-"+(now.get(Calendar.MONTH)+1)+"-"+now.get(Calendar.DAY_OF_MONTH)));
		 b.setTotal(new java.math.BigDecimal(_p0_18).movePointLeft(2).toString());
		 b.setContent(_p0_16);
		 billingDao.merge(b);
	  }
	  
 %>

<%
    int recordAffected = 0;
    ListIterator it	=	billingBean.getBillingItems().listIterator();

	while (it.hasNext()) {
  	  billingItem = (BillingItemBean)it.next();

	   BillingDetail bd = new BillingDetail();
	   bd.setBillingNo(Integer.parseInt(_p0_1));
	   bd.setServiceCode(billingItem.getService_code());
	   bd.setServiceDesc(billingItem.getDesc());
	   bd.setBillingAmount(billingItem.getService_value());
	   bd.setDiagnosticCode(billingItem.getDiag_code());
	   bd.setAppointmentDate(MyDateFormat.getSysDate(request.getParameter("_p0_9")));
	   bd.setStatus(request.getParameter("_p0_5"));
	   bd.setBillingUnit(billingItem.getQuantity());
	   billingDetailDao.persist(bd);

   %>




<%
  }
  %>

<%
  }
   catch (java.lang.ArrayIndexOutOfBoundsException _e0) {
 }%>

<form action="billingCorrection.jsp"><input type="hidden"
	name="billing_no" value=""> <input type="submit"
	value="<bean:message key="billing.billingCorrectionSubmit.btnCorrectAnother"/>"
	name="submit"> <input type="button"
	value="<bean:message key="billing.billingCorrectionSubmit.btnClose"/>"
	onClick="window.close()"></form>
</body>
</html:html>

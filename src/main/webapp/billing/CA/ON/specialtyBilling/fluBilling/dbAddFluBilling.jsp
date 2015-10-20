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
<security:oscarSec roleName="<%=roleName$%>" objectName="_billing" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%

boolean billSaved = false;
String total = "0.00";
String content = "", rd="", rdohip="", hctype="";
String demoNo = request.getParameter("functionid");
rd = request.getParameter("rd").equals("null")?"":request.getParameter("rd");
rdohip = request.getParameter("rdohip").equals("null")?"000000":request.getParameter("rdohip");
hctype = request.getParameter("demo_hctype").equals("null")?"ON":request.getParameter("demo_hctype").equals("")?"ON":request.getParameter("demo_hctype");
content = content + "<rdohip>" + rdohip+"</rdohip>" + "<rd>" +  rd + "</rd>";
content = content + "<hctype>" + hctype+"</hctype>" + "<demosex>" + request.getParameter("demo_sex") + "</demosex>";
content = content + "<specialty>flu</specialty>";

String curUser_no,userfirstname,userlastname;
curUser_no = (String) session.getAttribute("user");
userfirstname = (String) session.getAttribute("userfirstname");
userlastname = (String) session.getAttribute("userlastname");
%>

<%@ page import="java.sql.*"%>


<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="org.oscarehr.common.model.Billing" %>
<%@ page import="org.oscarehr.common.dao.BillingDao" %>
<%@ page import="org.oscarehr.common.model.BillingService" %>
<%@ page import="org.oscarehr.common.dao.BillingServiceDao" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail" %>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao" %>
<%@ page import="org.oscarehr.billing.CA.model.BillingDetail"%>
<%@ page import="org.oscarehr.billing.CA.dao.BillingDetailDao"%>
<%
	BillingDao billingDao = SpringUtils.getBean(BillingDao.class);
	BillingDetailDao billingDetailDao = SpringUtils.getBean(BillingDetailDao.class);
	BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class);
%>

<%
String billNo = null, svcDesc = null, svcPrice = null, sPrice = null ;

for(BillingService bs: billingServiceDao.findByServiceCode(request.getParameter("svcCode"))) {
	svcDesc = bs.getDescription();
	svcPrice = bs.getValue();
}


sPrice = svcPrice.substring(0,svcPrice.indexOf(".")) + svcPrice.substring(svcPrice.indexOf(".")+1);

Billing b = new Billing();
b.setClinicNo(Integer.parseInt(request.getParameter("clinicNo")));
b.setDemographicNo(Integer.parseInt(request.getParameter("functionid").trim()));
b.setProviderNo(request.getParameter("provider").substring(7));
b.setAppointmentNo(Integer.parseInt(request.getParameter("appointment_no")));
b.setOrganizationSpecCode("V03G");
b.setDemographicName(request.getParameter("demo_name"));
b.setHin(request.getParameter("demo_hin"));
b.setUpdateDate(oscar.MyDateFormat.getSysDate(request.getParameter("docdate")));
b.setUpdateTime(new java.util.Date());
b.setBillingDate(oscar.MyDateFormat.getSysDate(request.getParameter("apptDate")));
b.setBillingTime(oscar.MyDateFormat.getSysTime(request.getParameter("start_time")));
b.setClinicRefCode(request.getParameter("clinic_ref_code"));
b.setContent(content);
b.setTotal(svcPrice);
b.setStatus(request.getParameter("xml_billtype"));
b.setDob(request.getParameter("demo_dob"));
b.setVisitDate(null);
b.setVisitType(request.getParameter("xml_visittype"));
b.setProviderOhipNo(request.getParameter("provider").substring(0,6));
b.setProviderRmaNo("");
b.setApptProviderNo(request.getParameter("apptProvider"));
b.setAsstProviderNo("0");
b.setCreator(request.getParameter("doccreator"));
billingDao.persist(b);
int rowsAffected=1;

billNo = String.valueOf(billingDao.search_billing_no_by_appt(Integer.parseInt(request.getParameter("functionid")), Integer.parseInt(request.getParameter("appointment_no"))));


int recordAffected=0;

   BillingDetail bd = new BillingDetail();
   bd.setBillingNo(Integer.parseInt(billNo));
   bd.setServiceCode(request.getParameter("svcCode"));
   bd.setServiceDesc(svcDesc);
   bd.setBillingAmount(sPrice);
   bd.setDiagnosticCode(request.getParameter("dxCode"));
   bd.setAppointmentDate(oscar.MyDateFormat.getSysDate(request.getParameter("apptDate")));
   bd.setStatus(request.getParameter("xml_billtype"));
   bd.setBillingUnit("1");
   billingDetailDao.persist(bd);
   rowsAffected=1;

if (rowsAffected ==1) {
	
	if(billingDao.search_billing_no(Integer.parseInt(request.getParameter("functionid"))) != null) {
		billSaved=true;
	}

   if ( request.getParameter("goPrev") != null && request.getParameter("goPrev").equals("goPrev") && billSaved){
      response.sendRedirect("../../../../../oscarPrevention/AddPreventionData.jsp?prevention=Flu&demographic_no="+demoNo);
   }
}
%>



<html>
<head>
<script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
<script LANGUAGE="JavaScript">
    <!--
    function start(){
      this.focus();
    }
    function closeit() {
    	//self.opener.refresh();
      //self.close();
    }
    //-->
</script>
</head>
<body onload="start()">
<center>
<table border="0" cellspacing="0" cellpadding="0" width="90%">
	<tr bgcolor="#486ebd">
		<th align="CENTER"><font face="Helvetica" color="#FFFFFF">
		ADD A BILLING RECORD</font></th>
	</tr>
</table>
<%if (billSaved) { %>
<p>
<h1>Successful Addition of a billing Record.</h1>
</p>
<script LANGUAGE="JavaScript">
              self.close();
              self.opener.refresh();
        </script> <%}  else {%>
<p>
<h1>Sorry, addition has failed.</h1>
</p>
<%}%>
<p></p>
<hr width="90%"></hr>
<form><input type="button" value="Close this window"
	onClick="window.close()"></form>
</center>
</body>
</html>

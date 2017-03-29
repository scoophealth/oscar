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
	<%response.sendRedirect("../../../../securityError.jsp?type=_billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%
String curUser_no = (String) session.getAttribute("user");
%>   

<%@page import="java.sql.*, java.util.*,java.net.*, oscar.MyDateFormat"  errorPage="../../errorpage.jsp"%>

<%@page import="org.oscarehr.billing.CA.model.BillingInr" %>
<%@page import="org.oscarehr.billing.CA.dao.BillingInrDao" %>
<%@page import="org.oscarehr.common.model.BillingService" %>
<%@page import="org.oscarehr.common.dao.BillingServiceDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="oscar.util.ConversionUtils" %>
<%
	BillingInrDao dao = SpringUtils.getBean(BillingInrDao.class);
	BillingServiceDao billingServiceDao = SpringUtils.getBean(BillingServiceDao.class);
%>
<html>
<head>
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
<body  onload="start()">
<center>
    <table border="0" cellspacing="0" cellpadding="0" width="90%" >
      <tr bgcolor="#486ebd"> 
            <th align="CENTER"><font face="Helvetica" color="#FFFFFF">
            UPDATE A BILLING RECORD</font></th>
      </tr>
    </table>
<%
String demo_hin="", demo_dob="", demo_name="", billinginr_no="", errorCode="",service_code="", service_desc="", service_amount="",diag_code="";
billinginr_no = request.getParameter("billinginr_no");
service_code = request.getParameter("service_code").trim();
diag_code = request.getParameter("diag_code").trim();
if (service_code.trim().compareTo("") == 0){
	errorCode = errorCode + "Please input a service code.<br>"; 
}else{
	service_code = service_code.substring(0,5);    
    Calendar cal = GregorianCalendar.getInstance();
    String yyyy = String.valueOf(cal.get(Calendar.YEAR));
    String mm = String.valueOf(cal.get(Calendar.MONTH)+1);
    String dd = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
   
    for(BillingService bs:billingServiceDao.findGst(service_code,ConversionUtils.fromDateString( yyyy + "-" + mm + "-" + dd))) {
    	service_desc = bs.getDescription();
		service_code = bs.getServiceCode();
		service_amount = bs.getValue();
    }
    
}

if (diag_code.trim().compareTo("") == 0){
	errorCode = errorCode + "Please input a diagnostic code.<br>";
}else{
	diag_code = diag_code.substring(0,3);
	String numCode = "";
	for(int i=0;i<diag_code.length();i++){
		String c = diag_code.substring(i,i+1);
		if(c.hashCode()>=48 && c.hashCode()<=58)
			numCode += c;
	}

	if (numCode.length() < 3) {
		// diagnostic_code = "000|Other code";
		diag_code="000";
		errorCode = errorCode + "Please input a diagnostic code.<br>";
	}
} 

if (errorCode.compareTo("") ==0){
	if (request.getParameter("inraction").compareTo("update")==0) {
		demo_hin = request.getParameter("demo_hin");
		demo_dob = request.getParameter("demo_dob");
		
		BillingInr b = dao.find(Integer.parseInt(billinginr_no));
		if(b != null && !b.getStatus().equals("D")) {
			b.setHin(demo_hin);
			b.setDob(demo_dob);
			b.setServiceCode(service_code);
			b.setServiceDesc(service_desc);
			b.setBillingAmount(service_amount);
			b.setDiagnosticCode(diag_code);
			dao.merge(b);
		}
		
%>
<script LANGUAGE="JavaScript">
      self.close();
      self.opener.refresh();
</script>
<%
	}else{
		if (request.getParameter("inraction").compareTo("delete")==0) {
			GregorianCalendar now=new GregorianCalendar();
			int curYear = now.get(Calendar.YEAR);
			int curMonth = (now.get(Calendar.MONTH)+1);
			int curDay = now.get(Calendar.DAY_OF_MONTH);
			String nowDate = String.valueOf(curYear)+"/"+String.valueOf(curMonth) + "/" + String.valueOf(curDay);

			int rowAffect = 0;
			
			BillingInr bi = dao.find(Integer.parseInt(billinginr_no));
			if(bi != null && !bi.getStatus().equals("D")) {
				bi.setStatus("D");
				bi.setCreateDateTime(ConversionUtils.fromDateString(nowDate));
				dao.merge(bi);
				rowAffect++;
			}
      		
%>
<script LANGUAGE="JavaScript">
      self.close();
      self.opener.refresh();
</script>
<%
		}
	}
}else{
%>

<%=errorCode%>
<input type="button" value="Change" onClick="history.go(-1);return false;">
<%
}
%>
  <p><%=request.getParameter("inraction")%> Bill number <%=billinginr_no%></p>
  <hr width="90%"></hr>
<form>
<input type="button" value="Close this window" onClick="window.close()">
</form>
</center>
</body>
</html>

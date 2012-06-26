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
<%
if(session.getAttribute("user") == null) response.sendRedirect("../../../../logout.htm");

String curUser_no,userfirstname,userlastname;
curUser_no = (String) session.getAttribute("user");
userfirstname = (String) session.getAttribute("userfirstname");
userlastname = (String) session.getAttribute("userlastname");
%>   

<%@page import="java.sql.*, java.util.*,java.net.*, oscar.MyDateFormat"  errorPage="../../errorpage.jsp"%>

<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbINR.jspf" %>
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
    String[] params1 = new String[2];
    params1[0] = service_code;
    params1[1] = yyyy + "-" + mm + "-" + dd;
	ResultSet rsother = apptMainBean.queryResults(params1, "search_servicecode_detail");  
	while(rsother.next()){
		service_desc = rsother.getString("description");
		service_code = rsother.getString("service_code");
		service_amount = rsother.getString("value");
		// otherperc1 = rsother.getString("percentage");
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

		String[] param = new String[7];
		param[0] = demo_hin;
		param[1] = demo_dob;
		param[2] = service_code;
		param[3] = service_desc;
		param[4] = service_amount; 
		param[5] = diag_code;
		param[6] = billinginr_no; 

		int rowAffect = apptMainBean.queryExecuteUpdate(param,"update_inrbilling_dt_item");
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

			String[] param1 = new String[3];
			param1[0] = "D";
			param1[1] = nowDate;
			param1[2] = billinginr_no;

			int rowAffect = apptMainBean.queryExecuteUpdate(param1,"update_inrbilling_dt_billno");
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

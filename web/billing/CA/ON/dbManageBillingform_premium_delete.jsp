<%      
if(session.getValue("user") == null) response.sendRedirect("../../../logout.jsp");
%>


<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
<%@ include file="../../../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbBilling.jsp" %>

<%
String temp;
int recordAffected = -100;
for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
	temp=e.nextElement().toString();
	if( temp.indexOf("service")==-1 ) continue; 

	recordAffected = apptMainBean.queryExecuteUpdate(request.getParameter(temp),"delete_ctlpremium");
}

if (recordAffected != -100) apptMainBean.closePstmtConn();
%>

<% response.sendRedirect("manageBillingform.jsp"); %>
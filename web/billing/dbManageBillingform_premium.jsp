<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbBilling.jsp" %>
<%


String[] group = new String[4];
String typeid = "", type="";
GregorianCalendar now=new GregorianCalendar();
  int curYear = now.get(Calendar.YEAR);
  int curMonth = (now.get(Calendar.MONTH)+1);
  int curDay = now.get(Calendar.DAY_OF_MONTH);

%>

<%
for (int i=1; i<11; i++){

if(request.getParameter("service"+i).length() !=0){

 String[] param =new String[4];
	  param[0]="Office";
	  param[1]=request.getParameter("service"+i);
	  param[2]="A";
	  param[3]= curYear + "-" + curMonth + "-" + curDay;
	  int rowsAffected = apptMainBean.queryExecuteUpdate(param,"save_ctlpremium");



}}
%>
             
  <% response.sendRedirect("manageBillingform.jsp"); %>
<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"  %>
<%@ include file="../admin/dbconnection.jsp" %>
<jsp:useBean id="apptMainBean" class="oscar.AppointmentMainBean" scope="session" /> 
<%@ include file="dbBilling.jsp" %>
<%


String group1="",group2="", group3="";
String typeid = "", type="";

    String temp;
	for (Enumeration e = request.getParameterNames() ; e.hasMoreElements() ;) {
		temp=e.nextElement().toString();
		if( temp.indexOf("service")==-1 ) continue; 

                int   recordAffected = apptMainBean.queryExecuteUpdate(request.getParameter(temp),"delete_ctlpremium");
	
             
}

			 	           
	            
	    
	    




%>
  <% response.sendRedirect("manageBillingform.jsp"); %>
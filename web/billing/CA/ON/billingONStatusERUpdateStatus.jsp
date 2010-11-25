
<% 
	if(session.getAttribute("user") == null)
		response.sendRedirect("../logout.jsp");
	//String user_no = (String) session.getAttribute("user");
%>
<%@ page
	import="oscar.oscarBilling.ca.on.data.*, java.sql.*, oscar.*, java.net.*"
	errorPage="../errorpage.jsp"%>
<% 
	String id = request.getParameter("id");
	String val = request.getParameter("val");;
	JdbcBillingErrorRepImpl dbObj = new JdbcBillingErrorRepImpl();
	boolean bChecked = dbObj.updateErrorReportStatus(id, val);
	String ret = "Y".equals(val) ? "checked" : "uncheck";
	out.println(ret);

%>
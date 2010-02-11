
<% 
	if(session.getAttribute("user") == null)
		response.sendRedirect("../logout.jsp");
	//String user_no = (String) session.getAttribute("user");
%>
<%@ page
	import="oscar.oscarBilling.ca.on.data.*, java.sql.*, oscar.*, java.net.*"
	errorPage="../errorpage.jsp"%>
<% 
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

	String id = request.getParameter("id");
	String val = request.getParameter("val");;
	JdbcBillingErrorRepImpl dbObj = new JdbcBillingErrorRepImpl();
	boolean bChecked = dbObj.updateErrorReportStatus(id, val);
	String ret = "Y".equals(val) ? "checked" : "uncheck";
	out.println(ret);

%>
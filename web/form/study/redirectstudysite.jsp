<%
	if(session.getValue("user") == null || !( ((String) session.getValue("userprofession")).equalsIgnoreCase("doctor") ))
		response.sendRedirect("../../logout.jsp");
%>
<%@ page language="java"%>
<%@ page import="java.sql.*, oscar.oscarDB.*" %>

<%
//http://192.168.2.4/PDSsecurity/logindd.asp?DI=PEPPER&UN=yilee18&PW=515750564848564853485353544852485248484851575150

    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    String studyId = request.getParameter("study_no");

	String baseURL = "http://competeii.mcmaster.ca/PDSsecurity/login.asp";
	String username = "yilee18";
	String password = "515750564848564853485353544852485248484851575150";

	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA); 
    String sql = "SELECT * from studylogin where provider_no=" + provNo + " and study_no = " + studyId + " and current=1" ;
	ResultSet rs = db.GetSQL(sql);
	while(rs.next()) {
		baseURL = rs.getString("remote_login_url");
		username = rs.getString("username");
		password = rs.getString("password");
	}

	rs.close();
	db.CloseConn();


	String studyURL = baseURL + "?DI=PEPPER&DIPatID=&UN=" + username + "&PW=" + password ;
	response.sendRedirect(studyURL);
%>
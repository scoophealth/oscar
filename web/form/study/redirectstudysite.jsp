<%--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
--%>
<%
	if(session.getValue("user") == null || !( ((String) session.getValue("userprofession")).equalsIgnoreCase("doctor") ))
		response.sendRedirect("../../logout.jsp");
%>
<%@ page language="java"%>
<%@ page import="java.sql.*, oscar.oscarDB.*"%>

<%
//http://192.168.2.4/PDSsecurity/logindd.asp?DI=PEPPER&UN=yilee18&PW=515750564848564853485353544852485248484851575150

    int provNo = Integer.parseInt((String) session.getAttribute("user"));
    String studyId = request.getParameter("study_no");

	String baseURL = "http://competeii.mcmaster.ca/PDSsecurity/login.asp";
	String username = "yilee18";
	String password = "515750564848564853485353544852485248484851575150";

	DBHandler db = new DBHandler(DBHandler.OSCAR_DATA); 
    String sql = "SELECT * from studylogin where provider_no=" + provNo + " and study_no = " + studyId + " and current1=1" ;
	ResultSet rs = db.GetSQL(sql);
	while(rs.next()) {
		baseURL = db.getString(rs,"remote_login_url");
		username = db.getString(rs,"username");
		password = db.getString(rs,"password");
	}

	rs.close();

	String studyURL = baseURL + "?DI=PEPPER&DIPatID=&UN=" + username + "&PW=" + password ;
	response.sendRedirect(studyURL);
%>
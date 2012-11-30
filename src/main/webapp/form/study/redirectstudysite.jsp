<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@page import="org.oscarehr.common.model.StudyLogin"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="org.oscarehr.common.dao.StudyLoginDao"%>
<%@ page import="java.sql.*, oscar.oscarDB.*"%>

<%
//http://192.168.2.4/PDSsecurity/logindd.asp?DI=PEPPER&UN=yilee18&PW=515750564848564853485353544852485248484851575150

	String providerNo = (String) session.getAttribute("user");
    String studyId = request.getParameter("study_no");

	oscar.OscarProperties op = oscar.OscarProperties.getInstance();

	String baseURL = op.getProperty("redirectstudysite_default_baseURL");
	String username = op.getProperty("redirectstudysite_default_username");
	String password = op.getProperty("redirectstudysite_default_password");

	StudyLoginDao dao = SpringUtils.getBean(StudyLoginDao.class);
	for(StudyLogin login : dao.find(providerNo, studyId)){
		baseURL = login.getRemoteLoginUrl();
		username = login.getUsername();
		password = login.getPassword();
	}
	
	String studyURL = baseURL + "?DI=PEPPER&DIPatID=&UN=" + username + "&PW=" + password ;
	response.sendRedirect(studyURL);
%>

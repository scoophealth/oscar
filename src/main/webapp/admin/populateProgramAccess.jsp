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
<%@page import="org.oscarehr.common.model.CaisiAccessType"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao" %>
<%@page import="org.oscarehr.common.model.SecRole" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.oscarehr.common.dao.CaisiAccessTypeDao" %>
<%@page import="org.oscarehr.util.MiscUtils" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<!DOCTYPE HTML>
<html>
<head>
</head>
<body>

<%

String[] patterns = {"read %s issues","write %s issues","read %s notes","write %s notes","read %s ticklers","read ticklers assigned to a %s"};

if(request.getParameter("method") != null && "run".equals(request.getParameter("method"))) {
	//load all roles, go through each, and check that all permutations present.
	
	SecRoleDao dao = SpringUtils.getBean(SecRoleDao.class);
	
	CaisiAccessTypeDao caisiAccessTypeDao = SpringUtils.getBean(CaisiAccessTypeDao.class); 
	
	for(SecRole role : dao.findAll()) {
		String name = role.getName();
		
		for(String genName : patterns) {
			String tmp = genName.replaceAll("%s", name);
			if(caisiAccessTypeDao.findByName(tmp) == null) {
				CaisiAccessType cat = new CaisiAccessType();
	 			cat.setName(tmp);
	 			cat.setType("access");
	 			caisiAccessTypeDao.saveEntity(cat);
	 			MiscUtils.getLogger().info("adding ACCESS_TYPE=" + tmp);
			}
		}

	}
	
	%>Report was run.<%
	
} else { 
%>
<h1>This tool will add all missing roles based access_type DB entries.</h1>
<form action="populateProgramAccess.jsp">
	<input type="hidden" name="method" value="run"/>
	<input type="submit" />
</form>
</body>
</html>





<%}%>
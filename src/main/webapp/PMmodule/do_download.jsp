<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>
<%@ include file="/taglibs.jsp"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_report" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_report");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>


<%@page import="org.oscarehr.util.MiscUtils"%><%@page import="java.io.FileInputStream"%>
<%@page import="java.io.OutputStream"%>
<%@page import="java.io.File"%>
<%
	String root = getServletContext().getRealPath(File.separator) + File.separator;
	String name = "IntakeCReport1.csv";

	response.setContentType("unknown");
	response.addHeader("Content-Disposition", "filename=\"" + name + "\"");

	try {
		OutputStream os = response.getOutputStream();
		FileInputStream fis = new FileInputStream(root + name);

		byte[] b = new byte[1024];
		int i = 0;

		while ((i = fis.read(b)) > 0) {
			os.write(b, 0, i);
		}

		fis.close();
		os.flush();
		os.close();
	} catch (Exception e) {
		MiscUtils.getLogger().error("Error", e);
	}
%>

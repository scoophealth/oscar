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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
	boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.fax" rights="r" reverse="<%=true%>"> 
	<%authed=false; %>
	<%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.fax");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>

<%@ page import="org.oscarehr.common.dao.FaxJobDao, org.oscarehr.common.model.FaxJob"%>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="java.util.List" %>

<ul style="list-style:none;list-style-type:none;padding:0px;">

<%
	FaxJobDao faxJobDao = SpringUtils.getBean(FaxJobDao.class);
	String Id = request.getParameter("jobId");
	FaxJob faxJob = faxJobDao.find(Integer.parseInt(Id));

	for( int idx = 1; idx <= faxJob.getNumPages(); ++idx ) {
%>
		<li><img src="<%=request.getContextPath() + "/admin/ManageFaxes.do?method=viewFax&jobId=" + faxJob.getId() + "&curPage=" + idx %>" ondblclick="_zoom(this)"/></li>
<%
	}
%>

</ul>
<%--

    Copyright (c) 2006-. OSCARservice, OpenSoft System. All Rights Reserved.
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

--%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
      String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
      boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin.billing,_admin" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_admin&type=_admin.billing");%>
</security:oscarSec>
<%
if(!authed) {
	return;
}
%>


<%@ page import="java.math.*, java.util.*, java.io.*, java.sql.*, oscar.*, java.net.*,oscar.MyDateFormat"%>

<%@page import="org.oscarehr.common.model.CtlBillingType" %>
<%@page import="org.oscarehr.common.dao.CtlBillingTypeDao" %>
<%@page import="org.oscarehr.common.model.CtlBillingService" %>
<%@page import="org.oscarehr.common.dao.CtlBillingServiceDao" %>
<%@page import="org.oscarehr.common.model.CtlDiagCode" %>
<%@page import="org.oscarehr.common.dao.CtlDiagCodeDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>


<%
	CtlBillingTypeDao billingTypeDao = SpringUtils.getBean(CtlBillingTypeDao.class);
	CtlBillingServiceDao billingServiceDao = SpringUtils.getBean(CtlBillingServiceDao.class);
	CtlDiagCodeDao diagCodeDao = SpringUtils.getBean(CtlDiagCodeDao.class);

	String typeid = request.getParameter("servicetype");
	
	for(CtlBillingService b : billingServiceDao.findByServiceType(typeid)) {
		billingServiceDao.remove(b.getId());
	}
	
	for(CtlDiagCode d: diagCodeDao.findByServiceType(typeid)) {
		diagCodeDao.remove(d.getId());
	}
	
	billingTypeDao.remove(typeid);
%>

<script LANGUAGE="JavaScript">
    opener.document.serviceform.submit();
    self.close();
</script>

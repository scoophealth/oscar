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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="w" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.util.WebUtils"%><%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Calendar"%>
<%@page import="oscar.util.DateUtils"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>

<%
	Integer clientId=Integer.parseInt(request.getParameter("clientId"));
	String admissionString=StringUtils.trimToNull(request.getParameter("hospitalAdmission"));
	String dischargeString=StringUtils.trimToNull(request.getParameter("hospitalDischarge"));
	if(admissionString == null && dischargeString==null) {
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
	else {
		Calendar admissionDate=DateUtils.toGregorianCalendarDate(admissionString);
		Calendar dischargeDate=DateUtils.toGregorianCalendarDate(dischargeString);
		CdsForm4.addHospitalisationDay(clientId, admissionDate, dischargeDate);
	
		response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	}
%>

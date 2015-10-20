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
<security:oscarSec roleName="<%=roleName$%>" objectName="_demographic" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_demographic");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="java.util.*"%>
<%@page import="org.oscarehr.common.dao.DemographicExtDao" %>
<%@page import="org.oscarehr.util.SpringUtils" %>
<%@page import="org.apache.commons.lang.StringUtils" %>

<%
String demographic_no = request.getParameter("demo");
DemographicExtDao demographicExtDao = SpringUtils.getBean(DemographicExtDao.class);
Map<String,String> demoExt = demographicExtDao.getAllValuesForDemo(Integer.parseInt(demographic_no));

Hashtable h = new Hashtable();
            h.put("-1","Not Asked");
            h.put("1","Has Given Consent");
            h.put("2","Has Refused Consent");
%>
<li>Consent: <b><%=getText(h,StringUtils.trimToEmpty(demoExt.get("given_consent")) )%></b>
</li>

<%!

String getText(Hashtable h,String s){
    if (s != null && h.get(s) != null ){
        return (String) h.get(s);
    }
    return "";
}
%>

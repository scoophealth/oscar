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
            h.put("-1","Not Set");
            h.put("1","Status On-reserve");
            h.put("2","Status Off-reserve");
            h.put("3","Non-status on-reserve");
            h.put("4","Non-status off-reserve");
            h.put("5","Metis");
            h.put("6","Inuit");
            h.put("7","Asian");
            h.put("8","Caucasian");
            h.put("9","Hispanic");
            h.put("10","Black");
            h.put("11","Other");

Hashtable h2 = new Hashtable();
    h2.put("-1","Not Set");
    h2.put("1", "CHA1");
    h2.put("2", "CHA2");
    h2.put("3", "CHA3");
    h2.put("4", "CHA4");
    h2.put("5", "CHA5");
    h2.put("6", "CHA6");
    h2.put("7", "Richmond");
    h2.put("8", "North or West Vancouver");
    h2.put("9", "Surrey");
    h2.put("10", "On-Reserve");
    h2.put("11", "Homeless");
    h2.put("12", "Out of Country Residents");
    h2.put("13","Other");
    h2.put("14","Off-Reserve");
%>
<li>Area: <b><%=getArea(h2,StringUtils.trimToEmpty(demoExt.get("area")))%></b>
Status #: <b><%=StringUtils.trimToEmpty(demoExt.get("statusNum"))%></b>
Ethinicity: <b><%=getEth(h,StringUtils.trimToEmpty(demoExt.get("ethnicity")) )%></b>
</li>
<li>First Nations Community: <b><%=StringUtils.trimToEmpty(demoExt.get("fNationCom"))%></b></li>

<%!

String getEth(Hashtable h,String s){
    if (s != null && h.get(s) != null ){
        return (String) h.get(s);
    }
    return "";
}


String getArea(Hashtable h2,String s){
    if (s != null && h2.get(s) != null ){
        return (String) h2.get(s);
    }
    return "";
}



 %>

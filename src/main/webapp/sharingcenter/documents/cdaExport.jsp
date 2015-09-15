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
    String roleName2$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName2$%>" objectName="_edoc" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect("../../securityError.jsp?type=_edoc");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request"/>
<%@page import="org.oscarehr.sharingcenter.dao.DemographicExportDao" %>
<%@page import="org.oscarehr.sharingcenter.model.DemographicExport" %>
<%@page import="javax.xml.transform.Transformer"%>
<%@page import="javax.xml.transform.TransformerException"%>
<%@page import="javax.xml.transform.TransformerFactory"%>
<%@page import="javax.xml.transform.stream.StreamResult"%>
<%@page import="javax.xml.transform.stream.StreamSource"%>
<%@page import="java.io.StringReader"%>
<%@page import="java.io.StringWriter"%>
<%@page import="oscar.OscarProperties"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%
    String htmlResult = "No result";
    String demographic_no = request.getParameter("document_no");
    if (demographic_no != null) {
        DemographicExportDao demographicExportDao = SpringUtils.getBean(DemographicExportDao.class);
        DemographicExport export = demographicExportDao.getDocument(Integer.parseInt(demographic_no));

        StringReader xmlDocReader = new StringReader(new String(export.getDocument()));
        StringWriter htmlWriter = new StringWriter();
        try {
            //There's more than meets the eye
            Transformer optimusPrime = TransformerFactory.newInstance().newTransformer(new StreamSource(getClass().getResourceAsStream("/shic/OscarStyleCda.xsl")));
            optimusPrime.transform(new StreamSource(xmlDocReader), new StreamResult(htmlWriter));
            htmlResult = htmlWriter.toString();
        } catch (TransformerException e) {
            htmlResult = "Error while creating the HTML representation of the document.";
        }
    }
%>
<%= htmlResult%>
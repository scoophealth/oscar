<%--

    Copyright (c) 2013-2015. Department of Computer Science, University of Victoria. All Rights Reserved.
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
    Department of Computer Science
    LeadLab
    University of Victoria
    Victoria, Canada

--%>

<%@ page import="org.oscarehr.integration.cdx.CDXSpecialist" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="oscar.OscarProperties" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.consult" rights="w" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../../../securityError.jsp?type=_admin&type=_admin.consult");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>
<%
    OscarProperties props = OscarProperties.getInstance();
    boolean showCdx = "bc".equalsIgnoreCase(props.getProperty("billregion"));
    String cdxSpecId;

    String cdxSpecialistDetails = null;
    if (showCdx) {
        cdxSpecId = request.getParameter("cdxSpecId");
        MiscUtils.getLogger().debug("parameter cdxSpecId:" + cdxSpecId);
        CDXSpecialist cdxSpecialist = new CDXSpecialist();
       Boolean result = cdxSpecialist.saveProfessionalSpecialist(cdxSpecId);
        if (result) {
            cdxSpecialistDetails = cdxSpecialist.providerDescription(cdxSpecId);
            MiscUtils.getLogger().debug("description: " + cdxSpecialistDetails);
            if (cdxSpecialistDetails != null) {
                cdxSpecialistDetails = cdxSpecialistDetails.replaceAll(System.lineSeparator(), "<br>");
            }
        } else {
            cdxSpecialistDetails = "Failed to add cdxSpecId: " + cdxSpecId;
        }


    }
%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<html:html locale="true">
    <head>
        <script type="text/javascript" src="<%= request.getContextPath() %>/js/global.js"></script>
        <title><bean:message
                key="oscarEncounter.oscarConsultationRequest.config.EditSpecialists.title"/>
        </title>
    </head>
    <script>
        function BackToOscar() {
            window.close();
        }
    </script>
    <script>
        function redirect(elem) {
            elem.setAttribute("action", "./ShowAllServices.jsp");
            elem.submit();
        }
    </script>

    <link rel="stylesheet" type="text/css" href="../../encounterStyles.css">
    <body>

    <html:errors/>

    <p>The following information about this specialist was retrieved from CDX:</p>
    <p><%=cdxSpecialistDetails%>
    </p>
    <p>Use the Edit Specialists screen if you wish to add or modify details.</p>
    <form id="main" method="post" name="main" action="" onclick="redirect(this);">
        <button>Continue</button>
    </form>
    </body>
</html:html>

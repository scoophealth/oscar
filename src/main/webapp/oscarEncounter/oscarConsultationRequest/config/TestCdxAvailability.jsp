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

<%@ page import="ca.uvic.leadlab.obibconnector.facades.registry.IProvider" %>
<%@ page import="org.oscarehr.common.dao.ProfessionalSpecialistDao" %>
<%@ page import="org.oscarehr.common.model.ProfessionalSpecialist" %>
<%@ page import="org.oscarehr.integration.cdx.CDXSpecialist" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>
<%@ page import="oscar.OscarProperties" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Map" %>

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
    Map<Integer, String> updates = new HashMap<Integer, String>();
    Map<Integer, String> possibleUpdates = new HashMap<Integer, String>();
    ProfessionalSpecialistDao dao = SpringUtils.getBean(ProfessionalSpecialistDao.class);

    if (showCdx) {
        String lastName;
        String firstName;
        CDXSpecialist cdxSpecialist = new CDXSpecialist();
        List<IProvider> providers;


        for (ProfessionalSpecialist ps : dao.findAll()) {
            firstName = ps.getFirstName();
            lastName = ps.getLastName();
            providers = cdxSpecialist.findCdxSpecialistByName(lastName.trim());
            if (providers != null && !providers.isEmpty()) {
                for (IProvider p : providers) {
                    if (lastName.trim().equalsIgnoreCase(p.getLastName().trim())) {
                        MiscUtils.getLogger().info("For '" + lastName + "," + firstName + "' found lastName in CDX");
                        if (firstName.equalsIgnoreCase(p.getFirstName())) {
                            if ((ps.getCdxId() == null || ps.getCdxId().isEmpty()) && (p.getID() != null && !p.getID().isEmpty())) {
                                updates.put(ps.getId(), p.getID());
                                MiscUtils.getLogger().info("Found firstName as well.  Modified CDX ID for specId: " + ps.getId());
                            } else {
                                MiscUtils.getLogger().info("Found firstName as well and matching CDX ID");
                            }
                        } else {
                            possibleUpdates.put(ps.getId(), p.getFirstName());
                            MiscUtils.getLogger().info("but not firstName. Can't update CDX ID");
                        }
                    } else {
                        if (ps.getCdxCapable() || (ps.getCdxId() != null && !ps.getCdxId().isEmpty())) {
                            updates.put(ps.getId(), "");
                            MiscUtils.getLogger().info("Exact match for last name '" + lastName + "' not found.  Disabling CDX for specId: " + ps.getId());
                        } else {
                            MiscUtils.getLogger().info("No exact match for last name '" + lastName + "'");
                        }
                    }
                }
            } else {
                MiscUtils.getLogger().info("For '" + lastName + "," + firstName + "' last name not found in CDX");
                if (ps.getCdxCapable() || (ps.getCdxId() != null && !ps.getCdxId().isEmpty())) {
                    updates.put(ps.getId(), "");
                    MiscUtils.getLogger().info("Disabled CDX available for specId: " + ps.getId());
                }
            }
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
        <style>
            table {
                font-family: arial, sans-serif;
                border-collapse: collapse;
                width: 100%;
            }

            td, th {
                border: 1px solid #dddddd;
                text-align: left;
                padding: 8px;
            }

            tr:nth-child(even) {
                background-color: #dddddd;
            }
        </style>
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

    <%
        if (!updates.isEmpty()) {
            MiscUtils.getLogger().debug("Size of update map: " + updates.size());

    %>

    <p>These specialists have had their CDX capability updated:</p>
    <table>
        <tr>
            <th>Last Name</th>
            <th>First Name</th>
            <th>CDX ID</th>
        </tr>
        <%
            for (ProfessionalSpecialist ps : dao.findAll()) {
                if (updates.containsKey(ps.getId())) {
                    out.println("<tr><td>" + ps.getLastName() + "</td><td>" + ps.getFirstName() +
                            "</td><td>" + updates.get(ps.getId()) + "</td></tr>");
                    if (updates.get(ps.getId()).isEmpty()) {
                        ps.setCdxCapable(false);
                        ps.setCdxId(null);
                    } else {
                        ps.setCdxCapable(true);
                        ps.setCdxId(updates.get(ps.getId()));
                    }
                    try {
                        dao.merge(ps);
                    } catch (Exception e) {
                        MiscUtils.getLogger().error("Got exception updating professional specialist: " + e.getMessage());
                    }

                }
            }
        %>
    </table>
    <%
        } else {
            out.println("No specialists have had their CDX capability modified");
        }
    %>

    <%
        if (!possibleUpdates.isEmpty()) {
            MiscUtils.getLogger().debug("Size of possibleUpdates map: " + possibleUpdates.size());

    %>

    <p>These CDX capable specialists have the same last name as an existing specialist but their first name doesn't
        match:</p>
    <table>
        <tr>
            <th>Last Name</th>
            <th>First Name</th>
            <th>CDX First Name</th>
        </tr>
        <%
            for (ProfessionalSpecialist ps : dao.findAll()) {
                if (possibleUpdates.containsKey(ps.getId())) {
                    out.println("<tr><td>" + ps.getLastName() + "</td><td>" + ps.getFirstName() +
                            "</td><td>" + possibleUpdates.get(ps.getId()) + "</td></tr>");
                }
            }
        %>
    </table>
    <%
        }
    %>
    
    <p>Use the Edit Specialists screen if you wish to make further modifications.</p>
    <form id="main" method="post" name="main" action="" onclick="redirect(this);">
        <button>Continue</button>
    </form>
    </body>
</html:html>

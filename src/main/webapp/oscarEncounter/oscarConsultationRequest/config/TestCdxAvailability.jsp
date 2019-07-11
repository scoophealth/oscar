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
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.regex.Pattern" %>
<%@ page import="java.util.regex.Matcher" %>
<%@ page import="oscar.Misc" %>
<%@ page import="org.oscarehr.integration.cdx.CDXConfiguration" %>

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
    boolean bcReferralNo = "\\d{5}".equals(props.getProperty("referral_no.pattern"));
    MiscUtils.getLogger().info("bcReferralNo: " + bcReferralNo);
    ProfessionalSpecialistDao dao = SpringUtils.getBean(ProfessionalSpecialistDao.class);
    StringBuilder changeMsg = new StringBuilder();
    StringBuilder errorMsg = new StringBuilder();
    boolean cdxConnected = CDXConfiguration.obibIsConnected();
    if (!cdxConnected) {
        errorMsg.append("OSCAR is not connected to CDX via OBIB.  Can not test for CDX availability.<br>");
    }
    if (showCdx && cdxConnected) {
        String referralNo;
        String cdxId;
        CDXSpecialist cdxSpecialist = new CDXSpecialist();
        List<IProvider> providers;

        for (ProfessionalSpecialist ps : dao.findAll()) {
            referralNo = ps.getReferralNo();
            cdxId = ps.getCdxId();
            // if professionalSpecialists table has stored cdxId check that it can still be found in CDX
            if (cdxId != null && cdxId.trim().length() != 0) {
                providers = cdxSpecialist.findCdxSpecialistById(cdxId);
                if (providers == null || providers.isEmpty()) {
                    ps.setCdxCapable(false);
                    ps.setCdxId(null);
                    try {
                        dao.merge(ps);
                    } catch (Exception e) {
                        MiscUtils.getLogger().error("Got exception updating professional specialist: " + e.getMessage());
                        errorMsg.append("Failed to update professional specialist with stored cdxId: ").append(cdxId);
                    }
                    changeMsg.append("<tr><td>").append(ps.getLastName()).append("</td><td>").append(ps.getFirstName()).append("</td><td>").append(ps.getCdxId()).append("</td></tr>");
                } else {
                    if (providers.size() != 1) {  // cdxId should uniquely identify the provider
                        errorMsg.append("Multiple providers for cdxId: ").append(cdxId);
                        MiscUtils.getLogger().error("Multiple providers for cdxId: " + cdxId);
                    }
                }
            }
            // in BC expect 5 digit MSP billing numbers
            if ((cdxId == null || cdxId.trim().length() == 0) && referralNo != null && referralNo.length() == 5 && referralNo.matches("[0-9]+")) {
                providers = cdxSpecialist.findCdxSpecialistById(referralNo);
                if (providers != null && !providers.isEmpty()) {
                    ps.setCdxCapable(true);
                    ps.setCdxId(referralNo);
                    try {
                        dao.merge(ps);
                    } catch (Exception e) {
                        MiscUtils.getLogger().error("Got exception updating professional specialist: " + e.getMessage());
                        errorMsg.append("Failed to update professional specialist with Referral No.: ").append(referralNo);
                    }
                    changeMsg.append("<tr><td>").append(ps.getLastName()).append("</td><td>").append(ps.getFirstName()).append("</td><td>").append(ps.getCdxId()).append("</td></tr>");
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

    <% if (showCdx && !bcReferralNo) {
        %>
    <b>
        Testing whether specialists are CDX enabled requires that the
        5-digit BC MSP billing number be recorded in the "Referral No." field,
        <i>unless the specialist already has a CDX ID</i>.

        <br><br>This server has not been configured for 5-digit referral numbers.
        Your OSP needs to configure the property "referral_no.pattern=\\d{5}"
        in the oscar properties file.

        <br><br>If the specialist has a CDX ID recorded but is no longer CDX enabled,
            the CDX ID will be removed.
        <br><br>
    </b>
    <% } %>

    <% if (errorMsg.length() > 0) {
        out.println("<b>"+errorMsg+"</b>");
    }

        if (changeMsg.length() > 0) {
     %>

    <p>These specialists have had their CDX capability updated:</p>
    <table>
        <tr>
            <th>Last Name</th>
            <th>First Name</th>
            <th>CDX ID</th>
        </tr>
        <%
            out.println(changeMsg.toString());
        %>
    </table>
    <%
        } else {
            out.println("<br>No specialists have had their CDX capability updated.");
        }
    %>

    <p>Use the Edit Specialists screen if you wish to make further modifications.</p>
    <form id="main" method="post" name="main" action="" onclick="redirect(this);">
        <button>Continue</button>
    </form>
    </body>
</html:html>

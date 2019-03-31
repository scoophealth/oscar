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

<%@ page import="org.oscarehr.common.dao.ClinicDAO" %>
<%@ page import="org.oscarehr.common.dao.UserPropertyDAO" %>
<%@ page import="org.oscarehr.common.model.Clinic" %>
<%@ page import="org.oscarehr.common.model.UserProperty" %>
<%@ page import="org.oscarehr.integration.cdx.dao.CdxConfigDao" %>
<%@ page import="org.oscarehr.integration.cdx.model.CdxConfig" %>
<%@ page import="org.oscarehr.util.MiscUtils" %>
<%@ page import="org.oscarehr.util.SpringUtils" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed = true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_admin,_admin.misc" rights="r" reverse="<%=true%>">
    <%authed = false; %>
    <%response.sendRedirect("../securityError.jsp?type=_admin&type=_admin.misc");%>
</security:oscarSec>
<%
    if (!authed) {
        return;
    }
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%
    CdxConfigDao cdxConfigDao = (CdxConfigDao) SpringUtils.getBean(CdxConfigDao.class);
    ClinicDAO clinicDAO = (ClinicDAO) SpringUtils.getBean(ClinicDAO.class);
    UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

    String defaultProvider = "";
    String cdxOid = "";
    String pollInterval = "30";
    String privateKey = "";
    String decryptionKey = "";


    CdxConfig cdxConfig = cdxConfigDao.getCdxConfig(1);
    if (cdxConfig != null && cdxConfig.getDefaultProvider() != null) defaultProvider = cdxConfig.getDefaultProvider();

    Clinic clinic = clinicDAO.getClinic();
    if (clinic != null && clinic.getCdxOid() != null) cdxOid = clinic.getCdxOid();

    UserProperty up = null;
    up = userPropertyDao.getProp("cdx_poll_interval");
    if (up != null) pollInterval = up.getValue();

    up = userPropertyDao.getProp("cdx_privateKey");
    if (up != null) privateKey = up.getValue();


    up = userPropertyDao.getProp("cdx_decryptionKey");
    if (up != null) decryptionKey = up.getValue();

    MiscUtils.getLogger().info("defaultProvider: " + defaultProvider);
    MiscUtils.getLogger().info("cdxOid: " + cdxOid);
    MiscUtils.getLogger().info("pollInterval: " + pollInterval);
%>
<html>
<head>
    <title>CDX Preferences</title>
</head>
<body>
<h4>CDX Preferences</h4>
<form action="<%=request.getContextPath()%>/cdx/CDXPreferences.do" method="post">
    <fieldset>
        <div class="control-group">
            <label class="control-label">Default Provider:</label>
            <div class="controls">
                <input type="text" name="defaultProvider" value="<%=defaultProvider%>" />
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">Clinic OID:</label>
            <div class="controls">
                <input type="text" name="cdxOid" value="<%=cdxOid%>"/>
            </div>
        </div>
<%--        <div class="control-group">--%>
<%--            <label class="control-label">Private Key:</label>--%>
<%--            <div class="controls">--%>
<%--                <a href="../<%=privateKey%>" id="pkeyLink">View Private Key</a>--%>
<%--&lt;%&ndash;                <input type="button" class="btn" name="privateKey" value="Upload Private Key" onClick='popupPage(600,900,&quot;<html:rewrite page="/hospitalReportManager/hrmKeyUploader.jsp"/>&quot;);return false;' />&ndash;%&gt;--%>
<%--&lt;%&ndash;            </div>&ndash;%&gt;--%>
<%--        </div>--%>
<%--        <div class="control-group">--%>
<%--            <label class="control-label">Decryption Key:</label>--%>
<%--            <div class="controls">--%>
<%--                <a href="../<%=decryptionKey%>" id="dkeyLink">View Decryption Key</a>--%>
<%--&lt;%&ndash;                <input type="button" class="btn" name="decryptionKey" value="Upload Decryption Key" onClick='popupPage(600,900,&quot;<html:rewrite page="/hospitalReportManager/hrmKeyUploader.jsp"/>&quot;);return false;' />&ndash;%&gt;--%>
<%--            </div>--%>
<%--        </div>--%>
        <div class="control-group">
            <label class="control-label">Auto Polling Interval:</label>
            <div class="controls">
                <input type="text" name="cdx_poll_interval" value="<%=pollInterval %>" />
            </div>
        </div>
        <div class="control-group">
            <input type="submit" class="btn btn-primary" value="Submit" />
        </div>
        </table>
</form>
</body>
</html>
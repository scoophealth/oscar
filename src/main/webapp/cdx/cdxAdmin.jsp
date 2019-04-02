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
<%@ page import="org.oscarehr.PMmodule.dao.ProviderDao" %>
<%@ page import="org.oscarehr.common.model.Provider" %>
<%@ page import="java.util.List" %>
<%@ page import="org.oscarehr.integration.cdx.CDXDownloadJob" %>
<%@ page import="org.oscarehr.integration.cdx.CDXAdminAction" %>
<%@ page import="org.oscarehr.integration.cdx.CDXConfiguration" %>

<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>
<%
    String roleName$ = session.getAttribute("userrole") + "," + session.getAttribute("user");
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
    CdxConfigDao cdxConfigDao = SpringUtils.getBean(CdxConfigDao.class);
    ClinicDAO clinicDAO = SpringUtils.getBean(ClinicDAO.class);
    ProviderDao providerDao = (ProviderDao) SpringUtils.getBean("providerDao");
    UserPropertyDAO userPropertyDao = (UserPropertyDAO) SpringUtils.getBean("UserPropertyDAO");

    List<Provider> providers = providerDao.getActiveProviders();

    String defaultProvider = "";
    String cdxOid = "";
    String pollEnabled;
    String pollDisabled;
    String pollInterval;
    String cdxUrl = "127.0.0.1";
    String privateKey = "";
    String decryptionKey = "";


//    CdxConfig cdxConfig = cdxConfigDao.getCdxConfig(1);
//    if (cdxConfig != null && cdxConfig.getDefaultProvider() != null) defaultProvider = cdxConfig.getDefaultProvider();

    Clinic clinic = clinicDAO.getClinic();
    if (clinic != null && clinic.getCdxOid() != null) cdxOid = clinic.getCdxOid();

    CDXConfiguration cdxConfiguration = new CDXConfiguration();
    if (cdxConfiguration.isPollingEnabled()) {
        pollEnabled = "checked";
        pollDisabled = "unchecked";
    } else {
        pollEnabled = "unchecked";
        pollDisabled = "checked";
    }
    pollInterval = cdxConfiguration.getPollingInterval();

    UserProperty up;

    up = userPropertyDao.getProp("cdx_url");
    if (up != null) cdxUrl = up.getValue();

    up = userPropertyDao.getProp("cdx_default_provider");
    if (up != null) defaultProvider = up.getValue();

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
    <title>CDX Configuration</title>
    <script type="text/javascript">
        function runFetch() {
            window.location = "<%=request.getContextPath() %>/cdx/cdxImportNewDocs.jsp";
        }
    </script>
</head>
<body>
<h4>CDX Configuration</h4>
<form action="<%=request.getContextPath()%>/cdx/CDXAdmin.do" method="post">
        <div class="control-group">
            <label class="control-label">CDX URL:</label>
            <div class="controls">
                <input type="text" name="cdx_url" value="<%=cdxUrl%>"/>
            </div>
        </div>
    <div class="control-group">
        <label class="control-label">Default Provider:</label>
        <div class="controls">
            <select name="defaultProvider">
                <%
                    for (Provider provider : providers) {
                        String selected = "";
                        if (!defaultProvider.equals("")
                                && defaultProvider.equals(provider.getProviderNo())) {
                            selected = " selected=\"selected\" ";
                        }
                %>
                <option value="<%=provider.getProviderNo()%>" <%=selected%>><%=provider.getFormattedName()%>
                </option>
                <%
                    }
                %>
            </select>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">Clinic OID:</label>
        <div class="controls">
            <input type="text" name="cdxOid" value="<%=cdxOid%>"/>
        </div>
    </div>
    <div class="control-group">
        <label class="control-label">Automated Polling:</label>
            <label class="radio-inline"><input type="radio" name="cdx_polling_enabled" value="true" <%=pollEnabled%>
                                               class="form-control">Enabled</label>
            <label class="radio-inline"><input type="radio" name="cdx_polling_enabled" <%=pollDisabled%> value="false"
                                               class="form-control">Disabled</label>
    </div>
    <div class="control-group">
        <label class="control-label">Polling Interval (minutes):</label>
        <div class="controls">
            <input type="text" name="cdx_polling_interval" value="<%=pollInterval %>"/>
        </div>
    </div>
    <div class="control-group">
        <input type="submit" class="btn btn-primary" value="Submit"/>
    </div>

    <hr>
    <div>
        <input type="button" class="btn" onClick="runFetch()" value="Fetch New Data from CDX"/>
    </div>
</form>
</body>
</html>
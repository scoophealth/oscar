<!--
/*
*
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License.
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or (at your option) any later version. *
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
*
* <OSCAR TEAM>
*
* This software was written for
* Centre for Research on Inner City Health, St. Michael's Hospital,
* Toronto, Ontario, Canada
*/
-->
<%@ include file="/taglibs.jsp" %>
<%@ page import="java.util.*" %>
<%@ page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils" %>
<%@ page import="org.springframework.web.context.WebApplicationContext" %>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils" %>
<%@ page import="org.caisi.service.Version" %>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi" %>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security" %>

<%
    long loadPage = System.currentTimeMillis();
    if (session.getAttribute("userrole") == null) response.sendRedirect("../logout.jsp");
    String roleName$ = (String) session.getAttribute("userrole") + "," + (String) session.getAttribute("user");

    boolean userHasExternalOrErClerkRole = UserRoleUtils.hasRole(request, UserRoleUtils.Roles.external);
    userHasExternalOrErClerkRole = userHasExternalOrErClerkRole || UserRoleUtils.hasRole(request, UserRoleUtils.Roles.er_clerk);
%>

<%
    String yearStr = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    String mthStr = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
    String dayStr = String.valueOf(Calendar.getInstance().get(Calendar.DATE));

    if (mthStr.length() == 1)
        mthStr = "0" + mthStr;

    if (dayStr.length() == 1)
        dayStr = "0" + dayStr;

    String dateStr = yearStr + "-" + mthStr + "-" + dayStr;
%>

<script type="text/javascript">
    function getIntakeReport(type) {
        var oneWeekAgo = new Date();
        oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);

        var startDate = prompt("Please enter a start date in this format (e.g. 2000-01-01)", dojo.date.format(oneWeekAgo, {selector:'dateOnly', datePattern:'yyyy-MM-dd'}));
        var endDate = prompt("Please enter the end date in this format (e.g. 2000-12-01)", dojo.date.format(new Date(), {selector:'dateOnly', datePattern:'yyyy-MM-dd'}));

        if (!dojo.validate.isValidDate(startDate, 'YYYY-MM-DD')) {
            alert("'" + startDate + "' is not a valid start date");
            return false;
        }

        if (!dojo.validate.isValidDate(endDate, 'YYYY-MM-DD')) {
            alert("'" + endDate + "' is not a valid end date");
            return false;
        }

        alert("Generating report from " + startDate + " to " + endDate + "." + " " + "Please note: it is normal for the generation process to take up to a few minutes to complete, be patient.");

        location.href = '<html:rewrite action="/PMmodule/GenericIntake/Report"/>?' + 'method=report' + '&type=' + type + '&startDate=' + startDate + '&endDate=' + endDate;

        return false;
    }

    function createIntakeCReport1()
    {
        var startDate = prompt("Please enter the date in this format (e.g. 2006-01-01)", "<%=dateStr%>");

        while (startDate.length != 10 || startDate.substring(4, 5) != "-" || startDate.substring(7, 8) != "-")
        {
            startDate = prompt("Please enter the date in this format (e.g. 2006-01-01)", "<%=dateStr%>");
        }

        alert('creating report until ' + startDate);

        location.href = '<html:rewrite action="/PMmodule/IntakeCMentalHealthReportAction.do"/>?startDate=' + startDate;
    }

    function popupPage2(varpage, windowname) {
        var page = "" + varpage;
        windowprops = "height=700,width=1000,location=no,"
                + "scrollbars=yes,menubars=no,toolbars=no,resizable=yes,top=10,left=0";
        var popup = window.open(page, windowname, windowprops);
        if (popup != null) {
            if (popup.opener == null) {
                popup.opener = self;
            }
            popup.focus();
        }
    }
</script>

<div id="navcolumn">
    <table border="0" cellspacing="0" cellpadding="4">
        <tr>
            <td align="left">
                <table border="0" cellpadding="0" cellspacing="2">
                    <tr>
                        <td nowrap="nowrap" width="120">
                            <div align="center">
                                <img src="<html:rewrite page="/images/caisi_1.jpg" />" alt="Caisi" id="caisilogo"
                                     border="0"/>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td nowrap="nowrap" width="120">
                            <div align="center">
                                <%
                                    WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
                                    Version version = (Version) ctx.getBean("version");
                                %>
                                <span style="font-weight:bold"><%=version.getVersion()%></span>
                            </div>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>

<div id="projecttools" class="toolgroup">
<div class="label">
    <strong>Navigator</strong>
</div>
<div class="body">
    <div>
        <span><html:link action="/PMmodule/ProviderInfo.do">Home</html:link></span>
    </div>

    <div>
        <span>Client Management</span>

        <div>
            <html:link action="/PMmodule/ClientSearch2.do">Client Search</html:link>
        </div>
        <%
            if (!userHasExternalOrErClerkRole) {
        %>
        <div>
            <html:link action="/PMmodule/GenericIntake/Search.do">New Client</html:link>
        </div>
        <div>
            <a HREF="#" ONCLICK="popupPage2('../admin/demographicmergerecord.jsp', 'Merge');return false;">Merge
                Records</a>
        </div>
        <%
            }
        %>
    </div>

    <c:if test="${sessionScope.userrole ne 'er_clerk' and sessionScope.userrole ne 'Vaccine Provider'}">
        <div>
            <span>Reporting Tools</span>

            <div>
                <a href="javascript:void(0)" onclick="javascript:getIntakeReport('quick')">Registration Intake
                    Report</a>
            </div>
            <div>
                <a href="javascript:void(0)" onclick="javascript:getIntakeReport('indepth')">Follow-up Intake Report</a>
            </div>
            <caisi:isModuleLoad moduleName="intakec.enabled">
                <div>
                    <a href="javascript:void(0)" onclick="javascript:createIntakeCReport1();return false;">Street Health
                        Mental Health Report</a>
                </div>
            </caisi:isModuleLoad>
            <div>
                <html:link action="/PMmodule/Reports/ProgramActivityReport.do">Activity Report</html:link>
            </div>
                <%--
                <div>
                    <html:link action="/PMmodule/Reports/ClientListsReport">Client Lists Report</html:link>
                </div>
                --%>
            <div>
                <a href="<%=request.getContextPath()%>/PMmodule/reports/PopulationReportForm.jsp">Population Report</a>
            </div>
        </div>
    </c:if>


    <div>
        <span><a href='<%=request.getContextPath()%>/PMmodule/EditIntake.do'>Registration Editor</a></span>
    </div>
    <c:if test="${sessionScope.userrole ne 'er_clerk' and sessionScope.userrole ne 'Vaccine Provider'}">
        <div>
            <span><a href='<c:out value="${ctx}"/>/provider/providercontrol.jsp'>Oscar Medical</a></span>
        </div>
    </c:if>
    <div>
        <span><a href='<%=request.getContextPath()%>/logout.jsp'>Logout</a></span>
    </div>
</div>
<%
    if (session.getAttribute("userrole") != null && ((String) session.getAttribute("userrole")).indexOf("admin") != -1) {
%>
<div id="admintools" class="toolgroup">
    <div class="label">
        <strong>Administration</strong>
    </div>
    <div class="body">

        <div>
            <span>Agency Management</span>

            <div>
                <html:link action="/PMmodule/AgencyManager.do">Agency Summary</html:link>
            </div>
            <div>
                <html:link action="/PMmodule/AgencyManager.do?method=edit">Update Agency Information</html:link>
            </div>
            <div>
                <html:link action="/PMmodule/FacilityManager.do?method=list">Manage Facilities</html:link>
            </div>
        </div>
        <div>
            <span>Staff Management</span>

            <div>
                <html:link action="/PMmodule/StaffManager.do">Staff List</html:link>
            </div>
        </div>
        <div>
            <span>Program Management</span>

            <div>
                <html:link action="/PMmodule/ProgramManager.do">Program List</html:link>
            </div>
            <div>
                <html:link action="/PMmodule/ProgramManager.do?method=add">Add Program</html:link>
            </div>
            <div>
                <html:link action="/PMmodule/Admin/DefaultRoleAccess.do">Global Role Access</html:link>
            </div>
        </div>
        <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
            <security:oscarSec roleName="<%=roleName$%>"
                               objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc"
                               rights="r">
                <div>
                    <span>System Administration</span>

                    <div>
                        <a HREF="#" ONCLICK="popupPage2('../admin/admin.jsp', 'Admin');return false;">Admin Page</a>
                    </div>
                </div>
            </security:oscarSec>
        </caisi:isModuleLoad>
        <div>
            <span><a href="javascript.void(0);"
                     onclick="window.open('<html:rewrite action="/CaisiRole.do"/>','caisi_role','width=500,height=500');return false;">Caisi
                Roles</a></span>
        </div>


    </div>
</div>
</div>
<%
    }
%>
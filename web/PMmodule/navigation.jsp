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

<%-- Updated by Eugene Petruhin on 05 feb 2009 while fixing #2493970 --%>

<%@ include file="/taglibs.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

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
	WebApplicationContext ctx=null;
%>

<script type="text/javascript">
    function getIntakeReport(type) {
        var oneWeekAgo = new Date();
        oneWeekAgo.setDate(oneWeekAgo.getDate() - 7);

        var startDate = prompt("Please enter a start date in this format (e.g. 2000-01-01)", dojo.date.format(oneWeekAgo, {selector:'dateOnly', datePattern:'yyyy-MM-dd'}));
        if (startDate == null) {
            return;
        }
        if (!dojo.validate.isValidDate(startDate, 'YYYY-MM-DD')) {
            alert("'" + startDate + "' is not a valid start date");
            return;
        }

        var endDate = prompt("Please enter the end date in this format (e.g. 2000-12-01)", dojo.date.format(new Date(), {selector:'dateOnly', datePattern:'yyyy-MM-dd'}));
        if (endDate == null) {
            return;
        }
        if (!dojo.validate.isValidDate(endDate, 'YYYY-MM-DD')) {
            alert("'" + endDate + "' is not a valid end date");
            return;
        }

        var includePast = confirm("Do you want to include past intake forms in your report? ([OK] for yes / [Cancel] for no)");

        alert("Generating report from " + startDate + " to " + endDate + ". Please note: it is normal for the generation process to take up to a few minutes to complete, be patient.");

        var url = '<html:rewrite action="/PMmodule/GenericIntake/Report"/>?' + 'method=report' + '&type=' + type + '&startDate=' + startDate + '&endDate=' + endDate + '&includePast=' + includePast;
        
        popupPage2(url, "IntakeReport" + type);
    }

    function createIntakeCReport1()
    {
        var startDate = "";

        while (startDate.length != 10 || startDate.substring(4, 5) != "-" || startDate.substring(7, 8) != "-")
        {
            startDate = prompt("Please enter the date in this format (e.g. 2006-01-01)", "<%=dateStr%>");
            if (startDate == null) {
                return false;
            }
        }

        alert('Generating report for date ' + startDate);

        popupPage2('<html:rewrite action="/PMmodule/IntakeCMentalHealthReportAction.do"/>?startDate=' + startDate, "IntakeCReport");
    }

    function createStreetHealthReport()
    {
        var startDate = "";

        while (startDate.length != 10 || startDate.substring(4, 5) != "-" || startDate.substring(7, 8) != "-")
        {
            startDate = prompt("Please enter start date (e.g. 2006-01-01)", "<%=dateStr%>");
            if (startDate == null) {
                return false;
            }
        }

        alert('Generating report for date ' + startDate);

        popupPage2('<html:rewrite action="/PMmodule/StreetHealthIntakeReportAction.do"/>?startDate=' + startDate, "StreetHealthReport");
    }

    function popupPage2(varpage, windowname) {
        var page = "" + varpage;
        var windowprops = "height=700,width=1000,top=10,left=0,location=yes,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
        var popup = window.open(page, windowname, windowprops);
        if (popup != null) {
            if (popup.opener == null) {
                popup.opener = self;
            }
            popup.focus();
        }
    }
</script>

<!--
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
                                    ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
                                %>
                                <span style="font-weight:bold"></span>
                            </div>
                        </td>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
-->
<div id="projecttools" class="toolgroup">
<div class="label"><strong>Navigator</strong></div>
<div class="body"><!-- <div>
        <span><html:link action="/PMmodule/ProviderInfo.do">Home</html:link></span>
    </div>
-->
<div><span>Client Management</span> <security:oscarSec
	roleName="<%=roleName$%>" objectName="_pmm.clientSearch" rights="r">
	<div><html:link action="/PMmodule/ClientSearch2.do">Search Client</html:link>
	</div>
</security:oscarSec> <%
            if (!userHasExternalOrErClerkRole) {
        %> <security:oscarSec roleName="<%=roleName$%>"
	objectName="_pmm.newClient" rights="r">
	<div><html:link action="/PMmodule/GenericIntake/Search.do">New Client</html:link>
	</div>
</security:oscarSec> <security:oscarSec roleName="<%=roleName$%>"
	objectName="_pmm.mergeRecords" rights="r">
	<div><a HREF="#"
		ONCLICK="popupPage2('<c:out value="${ctx}"/>/admin/demographicmergerecord.jsp', 'Merge');return false;">Merge
	Records</a></div>
</security:oscarSec> <%
            }
        %>
</div>

<c:if
	test="${sessionScope.userrole ne 'er_clerk' and sessionScope.userrole ne 'Vaccine Provider'}">
	<div><span>Reporting Tools</span> <caisi:isModuleLoad
		moduleName="TORONTO_RFQ" reverse="true">
		<div><a href="javascript:void(0);" onclick="javascript:getIntakeReport('quick');return false;">Registration
		Intake Report</a></div>
		<div><a href="javascript:void(0);" onclick="javascript:getIntakeReport('indepth');return false;">Follow-up
		Intake Report</a></div>
		<caisi:isModuleLoad moduleName="intakec.enabled">
			<div><a href="javascript:void(0);" onclick="javascript:createIntakeCReport1();return false;">Street
			Health Mental Health Report</a></div>
		</caisi:isModuleLoad>
		<div><html:link action="/PMmodule/Reports/ProgramActivityReport.do">Activity Report</html:link>
		</div>
		<%--
                <div>
                    <html:link action="/PMmodule/Reports/ClientListsReport">Client Lists Report</html:link>
                </div>
                --%>
		<div><html:link action="/SurveyManager.do?method=reportForm">User Created Form Report</html:link>
		</div>
		</caisi:isModuleLoad> <caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
			<div><html:link action="QuatroReport/ReportList.do">Quatro Report Runner</html:link>
			</div>
		</caisi:isModuleLoad> <caisi:isModuleLoad moduleName="streethealth">
		<div><a href="javascript:void(0);" onclick="javascript:createStreetHealthReport();return false;">Street
		Health Mental Health Report</a></div>
	</caisi:isModuleLoad></div>
</c:if> <c:if
	test="${sessionScope.userrole ne 'er_clerk' and sessionScope.userrole ne 'Vaccine Provider'}">
	<security:oscarSec roleName="<%=roleName$%>"
		objectName="_pmm.caseManagement" rights="r">
		<div><span>Case Management</span>
		<div><span><a
			href='<c:out value="${ctx}"/>/provider/providercontrol.jsp'>Case
		Management</a></span></div>
		</div>
	</security:oscarSec>
</c:if> <!--    <div>
        <span><a href='<%=request.getContextPath()%>/logout.jsp'>Logout</a></span>
    </div>
--></div>

<div id="admintools" class="toolgroup">
<%
    if (session.getAttribute("userrole") != null && ((String) session.getAttribute("userrole")).indexOf("admin") != -1) {
	%>
<div class="label"><strong>Administration</strong></div>
<%} %>
<div class="body">

<div><security:oscarSec roleName="<%=roleName$%>"
	objectName="_pmm.manageFacilities" rights="r">
	<span>Facilities</span>

	<div><html:link action="/PMmodule/FacilityManager.do?method=list">Manage Facilities</html:link>
	</div>
</security:oscarSec> <security:oscarSec roleName="<%=roleName$%>" objectName="_pmm.editor"
	rights="r">
	<span>Editor</span>
	<div><span><a href="javascript:void(0)"
		onclick="window.open('<%=request.getContextPath()%>/PMmodule/GenericIntake/EditIntake.jsp?pub=<c:out value="${sessionScope.provider.formattedName}" />');">Intake
	Form Editor</a></span></div>
</security:oscarSec></div>

<div><span>Staff</span> <security:oscarSec
	roleName="<%=roleName$%>" objectName="_pmm.staffList" rights="r">
	<div><html:link action="/PMmodule/StaffManager.do">Staff List</html:link>
	</div>
</security:oscarSec></div>

<div><span>Program</span> <security:oscarSec
	roleName="<%=roleName$%>" objectName="_pmm.programList" rights="r">
	<div><html:link action="/PMmodule/ProgramManager.do">Program List</html:link>
	</div>
</security:oscarSec> <security:oscarSec roleName="<%=roleName$%>"
	objectName="_pmm.addProgram" rights="r">
	<div><html:link action="/PMmodule/ProgramManager.do?method=add">Add Program</html:link>
	</div>
</security:oscarSec> <security:oscarSec roleName="<%=roleName$%>"
	objectName="_pmm.globalRoleAccess" rights="r">
	<div><html:link action="/PMmodule/Admin/DefaultRoleAccess.do">Global Role Access</html:link>
	</div>
</security:oscarSec></div>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc,_admin.torontoRfq"
	rights="r">
	<div><span>System Administration</span>

	<div><a HREF="#" ONCLICK="popupPage2('<%=request.getContextPath()%>/admin/admin.jsp', 'Admin');return false;">Admin
	Page</a></div>
	<!--
                   	<caisi:isModuleLoad moduleName="TORONTO_RFQ" reverse="false">
                   	<div>	
                   		<html:link action="/Lookup/LookupTableList.do">Lookup Field Editor</html:link>
                  	</div>
                    </caisi:isModuleLoad>
--></div>
</security:oscarSec> <!--         
         <security:oscarSec roleName="<%=roleName$%>"
                               objectName="_pmm.caisiRoles"
                               rights="r">
        <div><div>
            <span><a href="javascript.void(0);"
                     onclick="window.open('<html:rewrite action="/CaisiRole.do"/>','caisi_role','width=500,height=500');return false;">Caisi
                Roles</a></span>
        </div></div>
		</security:oscarSec>
--></div>
</div>
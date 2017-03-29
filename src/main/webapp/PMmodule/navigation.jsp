
<%--


    Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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

    This software was written for
    Centre for Research on Inner City Health, St. Michael's Hospital,
    Toronto, Ontario, Canada

--%>



<%-- Updated by Eugene Petruhin on 05 feb 2009 while fixing #2493970 --%>

<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@ include file="/taglibs.jsp"%>
<%@ page import="java.util.*"%>
<%@ page import="org.oscarehr.PMmodule.web.utils.UserRoleUtils"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@ page import="org.oscarehr.PMmodule.model.Program"%>
<%@ page import="org.oscarehr.util.SpringUtils"%>
<%@ taglib uri="/WEB-INF/caisi-tag.tld" prefix="caisi"%>
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>

<%
	LoggedInInfo loggedInInfo987=LoggedInInfo.getLoggedInInfoFromSession(request);

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
	
	//get current program, to check for OCAN
	boolean programEnableOcan=false;
	String currentProgram = (String)session.getAttribute(org.oscarehr.util.SessionConstants.CURRENT_PROGRAM_ID);
	if(currentProgram != null) {
		ProgramManager pm = SpringUtils.getBean(ProgramManager.class);
		Program program = pm.getProgram(currentProgram);
		programEnableOcan =program.isEnableOCAN();
	}
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


	function getGeneralFormsReport()
	{

		popupPage2('<html:rewrite action="/PMmodule/ClientManager.do"/>?method=getGeneralFormsReport',"generalFormsReport");
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
            if (!dojo.validate.isValidDate(startDate, 'YYYY-MM-DD')) {
                alert("'" + startDate + "' is not a valid start date");
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

    function popupOcanWorkloadPage(url) {
        var windowprops = "height=700,width=1400,top=10,left=0,location=yes,scrollbars=yes,menubars=no,toolbars=no,resizable=yes";
        var popup = window.open(url, 'OcanWorkload', windowprops);
        if (popup != null) {
            if (popup.opener == null) {
                popup.opener = self;
            }
            popup.focus();
        }
    }

</script>

<div id="projecttools" class="toolgroup">
<div class="label"><strong>Navigator</strong></div>
<div class="body">
<div><span>Client Management</span> <security:oscarSec
	roleName="<%=roleName$%>" objectName="_pmm.clientSearch" rights="r">
	<div><html:link action="/PMmodule/ClientSearch2.do">Search Client</html:link>
	</div>
</security:oscarSec> <%
            if (!userHasExternalOrErClerkRole) {
        %> <security:oscarSec roleName="<%=roleName$%>"
	objectName="_pmm.newClient" rights="r">
	<div><html:link action="/PMmodule/ClientSearch2.do">New Client</html:link>
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
	<security:oscarSec roleName="<%=roleName$%>"
		objectName="_pmm.caseManagement" rights="r">
		<div><span>Case Management</span>
		<div><span>
		<caisi:isModuleLoad moduleName="oscarClinic">
		<a
			href='<c:out value="${ctx}"/>/provider/providercontrol.jsp?infirmaryView_isOscar=true&GoToCaisiViewFromOscarView=false'>Case
		Management</a>
		</caisi:isModuleLoad>
		<caisi:isModuleLoad moduleName="oscarClinic" reverse="true">
		<a
			href='<c:out value="${ctx}"/>/provider/providercontrol.jsp?infirmaryView_isOscar=false&GoToCaisiViewFromOscarView=true'>Case
		Management</a>
		</caisi:isModuleLoad>
		</span></div>
                <%                
                if (loggedInInfo987.getCurrentFacility()!=null && loggedInInfo987.getCurrentFacility().isEnableOcanForms() && programEnableOcan)
                {
                %>
                        <div>
                                <a href="javascript:void(0);"onclick="popupOcanWorkloadPage('<%=request.getContextPath()%>/PMmodule/OcanWorkload.do'); return false;">OCAN Workload</a>
                        </div>
                <%
                }
                %>
		</div>
	</security:oscarSec>
</c:if> 

<div>
	
		<span>Wait-list Management</span>
		<!--  
		<div><span><a target='_blank' href='<c:out value="${ctx}"/>/PMmodule/incVacancyMatches.jsp'>New Vacancies</a></span></div>
		-->
		<div><html:link action="/PMmodule/AllVacancies.do">All Vacancies</html:link></div>
</div>

<div>
	<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm_agencyList" rights="r">
		<span>Information</span>
		<div><span><a target='_blank'
			href='http://www.oscarcanada.org/caisi/social_support_services/hospitals-agencies-and-shelters/participating-agencies-1/participating-agencies'>List of Integrating CAISI Agencies</a></span></div>
	</security:oscarSec>
</div>

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
<div>
	<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm.functionalCentre" rights="r">
	<span>Functional Centre</span>
	<div><html:link action="PMmodule/FunctionalCentreManager.do?method=list">Functional Centre</html:link></div>
	</security:oscarSec>
</div>
<div>
	<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm.staffList" rights="r">
	<span>Staff</span>
	<div><html:link action="/PMmodule/StaffManager.do">Staff List</html:link></div>
	</security:oscarSec>
</div>

<div>
	<security:oscarSec roleName="<%=roleName$%>" objectName="_pmm.programList" rights="r">
	<span>Program</span>
	<div><html:link action="/PMmodule/ProgramManager.do">Program List</html:link>
	</div>
    </security:oscarSec>

    <security:oscarSec roleName="<%=roleName$%>" objectName="_pmm.addProgram" rights="r">
	<div><html:link action="/PMmodule/ProgramManager.do?method=add">Add Program</html:link>
	</div>
	</security:oscarSec>

	<security:oscarSec roleName="<%=roleName$%>"
	objectName="_pmm.globalRoleAccess" rights="r">
	<div><html:link action="/PMmodule/Admin/DefaultRoleAccess.do">Global Role Access</html:link>
	</div>
	</security:oscarSec>
</div>
<security:oscarSec roleName="<%=roleName$%>"
	objectName="_admin,_admin.userAdmin,_admin.schedule,_admin.billing,_admin.resource,_admin.reporting,_admin.backup,_admin.messenger,_admin.eform,_admin.encounter,_admin.misc,_admin.torontoRfq"
	rights="r">
	<div><span>System Administration</span>

	<div><a HREF="#" ONCLICK="popupPage2('<%=request.getContextPath()%>/admin/admin.jsp', 'Admin');return false;">Admin
	Page</a></div>
	</div>
</security:oscarSec> 
</div>
</div>

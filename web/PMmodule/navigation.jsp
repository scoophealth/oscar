<%@ include file="/taglibs.jsp"%>

<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.caisi.service.Version"%>

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
    
    location.href='<html:rewrite action="/PMmodule/GenericIntake/Report"/>?' + 'method=report' + '&type=' + type + '&startDate=' + startDate + '&endDate=' + endDate;
    
    return false;
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
								<img src="<html:rewrite page="/images/caisi_1.jpg" />" alt="Caisi" id="caisilogo" border="0" />
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
            <div>
    			<html:link action="/PMmodule/GenericIntake/Search.do">New Client</html:link>
            </div>
		</div>

        <c:if test="${sessionScope.userrole eq 'ER Clerk' or sessionScope.userrole eq 'Vaccine Provider'}">
            <%
            String oscarContextPath = (String) session.getAttribute("oscar_context_path");
            %>
            <div>
                <a href="<%=oscarContextPath%>/logout.jsp">Logout</a>
            </div>
        </c:if>
        
        <c:if test="${sessionScope.userrole ne 'ER Clerk' and sessionScope.userrole ne 'Vaccine Provider'}">
        <div>
            <span>Reporting Tools</span>
            <div>
                <a href="javascript:void(0)" onclick="javascript:getIntakeReport('quick')">Registration Intake Report</a>
            </div>
            <div>
                <a href="javascript:void(0)" onclick="javascript:getIntakeReport('indepth')">Follow-up Intake Report</a>
            </div>
            <div>
                <html:link action="/PMmodule/Reports/ProgramActivityReport.do">Activity Report</html:link>
            </div>
            <div>
                <html:link action="/PMmodule/Reports/ClientListsReport">Client Lists Report</html:link>
            </div>
        </div>
        </c:if>
        
        <%
        if (session.getAttribute("userrole") != null && ((String) session.getAttribute("userrole")).indexOf("admin") != -1) {
        %>
		<div>
			<span>Agency Management</span>
			<div>
				<html:link action="/PMmodule/AgencyManager.do">Summary</html:link>
			</div>
			<div>
				<html:link action="/PMmodule/AgencyManager.do?method=edit">Update Info</html:link>
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
        <div>
            <span><a href="javascript.void(0);" onclick="window.open('<html:rewrite action="/CaisiRole.do"/>','caisi_role','width=500,height=500');return false;">Caisi Roles</a></span>
        </div>
        <%
        }
        %>
        
        <c:if test="${sessionScope.userrole ne 'ER Clerk' and sessionScope.userrole ne 'Vaccine Provider'}">
		<div>
			<span><a href='<c:out value="${ctx}"/>/provider/providercontrol.jsp'>Oscar Medical</a></span>
		</div>
		</c:if>
		<div>
			<span><a href='<%=request.getContextPath()%>/logout.jsp'>Logout</a></span>
		</div>
    </div>
</div>
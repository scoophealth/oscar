<%@ include file="/taglibs.jsp"%>

<%@ page import="java.util.*"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ page import="org.caisi.service.Version"%>

<%
	WebApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(getServletContext());
	Version version = (Version) ctx.getBean("version");
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
function createIntakeAReport1()
{
	var dateObj = new Date();
	var year;
	var yearStr;
	
	var year = dateObj.getYear();
	
	if (year < 2000)
		yearStr = dateObj.getYear() + 1900;
	else
		yearStr = year;
		
	var mthStr = (dateObj.getMonth() + 1) + "";
	var dayStr = "" + dateObj.getDate();
	
	if (mthStr.length == 1)
	{
		mthStr = "0" + mthStr;
	}
	
	if(dayStr.length == 1)
	{
		dayStr = "0" + dayStr;
	}
	
	var dateStr = yearStr + "-" + mthStr + "-" + dayStr;
	var startDate = prompt("Please enter the start date in this format (e.g. 2005-01-10)", "0001-01-01");
	var endDate = prompt("Please enter the end date in this format (e.g. 2005-11-10)", dateStr);
	
	while(startDate.length != 10  ||  startDate.substring(4,5) != "-"  ||  startDate.substring(7,8) != "-")
	{
		startDate = prompt("Please enter the start date in this format (e.g. 2005-01-10)", "0001-01-01");
	}
	
	while(endDate.length != 10  ||  endDate.substring(4,5) != "-"  ||  endDate.substring(7,8) != "-")
	{
		endDate = prompt("Please enter the end date in this format (e.g. 2005-11-10)", dateStr);
	}
	
	alert('creating report from ' + startDate + ' to ' + endDate);
	
	location.href='<html:rewrite action="/PMmodule/IntakeAReport1Action"/>?startDate=' + startDate + '&endDate=' + endDate;
}

function createIntakeCReport1()
{
	var startDate = prompt("Please enter the date in this format (e.g. 2006-01-01)", "<%=dateStr%>");

	while(startDate.length != 10  ||  startDate.substring(4,5) != "-"  ||  startDate.substring(7,8) != "-")
	{
		startDate = prompt("Please enter the date in this format (e.g. 2006-01-01)", "<%=dateStr%>");
	}

	alert('creating report until ' + startDate);

	location.href='<html:rewrite action="/PMmodule/IntakeCReport1Action"/>?startDate=' + startDate;
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
			<span>
				<html:link action="/PMmodule/ProviderInfo.do">Home</html:link>
			</span>
		</div>
		<div>
			<span>Client Management</span>
		<div>
			<html:link action="/PMmodule/ClientSearch2.do">Client Search</html:link>
			<br />
			<html:link action="/PMmodule/GenericIntake/Search.do">New Client</html:link></div>
		</div>
		<c:if test="${sessionScope.userrole ne 'ER Clerk' and sessionScope.userrole ne 'Vaccine Provider'}">
		<div>
			<span>Reporting Tools</span>
			<div>
				<a href="javascript:void(0)" onclick="javascript:createIntakeAReport1();return false;">IntakeA</a>
				<br />
				<html:link action="/PMmodule/Reports/ProgramActivityReport.do">Activity Report</html:link>
				<br />
				<a href="javascript:void(0)" onclick="javascript:createIntakeCReport1();return false;">Street Health Mental Health Report</a>
			</div>
		</div>
		</c:if>
		
		<c:if test="${sessionScope.userrole eq 'ER Clerk' or sessionScope.userrole eq 'Vaccine Provider'}">		
		<%
			String oscarContextPath=(String)session.getAttribute("oscar_context_path");
		%>
			<div><a href="<%=oscarContextPath%>/logout.jsp">Logout</a></div>
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
</div>

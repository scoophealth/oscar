<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.PMmodule.service.ProgramManager"%>
<%@page import="org.oscarehr.PMmodule.service.AdmissionManager"%>
<%
	ProgramManager programManager = (ProgramManager)SpringUtils.getBean("programManager");
	AdmissionManager admissionManager = (AdmissionManager)SpringUtils.getBean("admissionManager");

	List<Admission> admissions = admissionManager.getCurrentAdmissionsByProgramId(request.getParameter("programId"));
	
	String demographicNo = request.getParameter("demographicNo");
%>
<html>

	<head>
		<title>Group Note - Select Clients</title>
	</head>
	
	<body>
	<form action="groupNoteSelectAction.jsp">
		<input type="hidden" name="demographicNo" value="<%=demographicNo%>"/>
	<table>
	
<%
	for(Admission admission:admissions) {
		Demographic demographic = admission.getClient();
		if(demographic.getDemographicNo().intValue() == Integer.parseInt(demographicNo)) {
			continue;
		}
%>
	<tr>
		<td><input type="checkbox" name="group_client_id" value="<%=demographic.getDemographicNo()%>"/></td>
		<td><%=demographic.getFormattedName()%></td>
	</tr>
<%
	}
%>
	</table>
	<br/>
	<input type="text" name="num_anonymous" value="0" size="3"/>&nbsp;
	Anonymous Clients
	
	<br/><br/>
	
	<input type="button" value="cancel" onclick="window.close();"/> &nbsp;&nbsp; <input type="submit" value="Enter note into selected clients"/>
	<input type="hidden" name="programId" value="<%=request.getParameter("programId")%>"/>
	</form>
	</body>

</html>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	String ocanType = request.getParameter("ocanType");
	int centerNumber = Integer.parseInt(request.getParameter("center_num"));
	String LHIN_code = request.getParameter("LHIN_code");
	String orgName = request.getParameter("orgName");
	String programName = request.getParameter("programName");
	
	OcanStaffForm ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId, prepopulationLevel,ocanType);		
	
%>

<div id="center_programNumber<%=centerNumber%>">
	<table>
		<tr>
			<td class="genericTableHeader">Program Number</td>
			<td class="genericTableData">			
				<%=OcanForm.renderAsOrgProgramNumberTextField(ocanStaffForm.getId(),"serviceUseRecord_programNumber"+centerNumber,OcanForm.getOcanConnexProgramNumber(LHIN_code, orgName, programName), 10, prepopulationLevel)%>
			</td>
		</tr>
		
		
	</table>
</div>
<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	
	int centerNumber = Integer.parseInt(request.getParameter("center_num"));
	String LHIN_code = request.getParameter("LHIN_code");
	String orgName = request.getParameter("orgName");
	
	OcanStaffForm ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId, prepopulationLevel);		
	
%>

<script type="text/javascript">
$('document').ready(function() {
	//load mental health centres orgnaization name
	var demographicId='<%=currentDemographicId%>';
	var cenCount = $("#center_count").val();
	var LHIN_code = $("#serviceUseRecord_orgLHIN<%=centerNumber%>").val(); 
	var orgName = $("#serviceUseRecord_orgName<%=centerNumber%>").val();
	var programName = $("#serviceUseRecord_programName<%=centerNumber%>").val();
	var item = $("#center_block_orgName<%=centerNumber%>");
	if(programName!=null && programName!="") {
		$.get('ocan_form_getProgramNumber.jsp?demographicId='+demographicId+'&center_num=<%=centerNumber%>'+'&LHIN_code='+LHIN_code+'&orgName='+orgName+'&programName='+programName, function(data) {
				  item.append(data);					 
				});	
	}
});
</script>



<div id="center_programName<%=centerNumber%>">
	<table>
		<tr>
			<td class="genericTableHeader">Organization Number **</td>
			<td class="genericTableData">			
				<%=OcanForm.renderAsOrgProgramNumberTextField(ocanStaffForm.getId(),"serviceUseRecord_orgNumber"+centerNumber,OcanForm.getOcanConnexOrgNumber(LHIN_code, orgName), 10, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Program Name ** </td>
			<td class="genericTableData">
			
			<select name="serviceUseRecord_programName<%=centerNumber %>" id="serviceUseRecord_programName<%=centerNumber %>" onchange="changeProgramName(this);" class="{validate: {required:true}}">					
				<%=OcanForm.renderAsConnexProgramNameSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_programName"+centerNumber, OcanForm.getOcanConnexProgramOptions(LHIN_code,orgName), prepopulationLevel)%>
			</select>	
			</td>
		</tr>
		
	</table>
</div>
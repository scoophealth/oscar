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
<%@ taglib uri="/WEB-INF/security.tld" prefix="security"%>
<%
    String roleName$ = (String)session.getAttribute("userrole") + "," + (String) session.getAttribute("user");
    boolean authed=true;
%>
<security:oscarSec roleName="<%=roleName$%>" objectName="_form" rights="r" reverse="<%=true%>">
	<%authed=false; %>
	<%response.sendRedirect(request.getContextPath() + "/securityError.jsp?type=_form");%>
</security:oscarSec>
<%
	if(!authed) {
		return;
	}
%>

<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.common.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	LoggedInInfo loggedInInfo=LoggedInInfo.getLoggedInInfoFromSession(request);
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	String ocanType = request.getParameter("ocanType");
	int ocanStaffFormId =0;
	if(request.getParameter("ocanStaffFormId")!=null && request.getParameter("ocanStaffFormId")!="") {
		ocanStaffFormId = Integer.parseInt(request.getParameter("ocanStaffFormId"));
	}
	int centerNumber = Integer.parseInt(request.getParameter("center_num"));
	String LHIN_code = request.getParameter("LHIN_code");
	String orgName = request.getParameter("orgName");
	
	int prepopulate = 0;
	prepopulate = Integer.parseInt(request.getParameter("prepopulate")==null?"0":request.getParameter("prepopulate"));
	
	OcanStaffForm ocanStaffForm = null;
	if(ocanStaffFormId != 0) {
		ocanStaffForm=OcanForm.getOcanStaffForm(Integer.valueOf(request.getParameter("ocanStaffFormId")));
	}else {
		ocanStaffForm=OcanForm.getOcanStaffForm(loggedInInfo.getCurrentFacility().getId(),currentDemographicId,prepopulationLevel,ocanType);	
		
		if(ocanStaffForm.getAssessmentId()==null) {
			
			OcanStaffForm lastCompletedForm = OcanForm.getLastCompletedOcanFormByOcanType(loggedInInfo.getCurrentFacility().getId(),currentDemographicId,ocanType);
			if(lastCompletedForm!=null) {
				
				// prepopulate the form from last completed assessment
				if(prepopulate==1) {			
						
					lastCompletedForm.setAssessmentId(null);
					lastCompletedForm.setAssessmentStatus("In Progress");
						
					ocanStaffForm = lastCompletedForm;
					
				}
			}
		}
		if(ocanStaffForm!=null) {
			ocanStaffFormId = ocanStaffForm.getId()==null?0:ocanStaffForm.getId().intValue();
		}
	}
%>

<script type="text/javascript">
$('document').ready(function() {
	//load mental health centres orgnaization name
	var demographicId='<%=currentDemographicId%>';
	var ocanType='<%=ocanType%>';
	var ocanStaffFormId = '<%=ocanStaffFormId%>';
	var cenCount = $("#center_count").val();
	var LHIN_code = $("#serviceUseRecord_orgLHIN<%=centerNumber%>").val(); 
	var orgName = $("#serviceUseRecord_orgName<%=centerNumber%>").val();
	var programName = $("#serviceUseRecord_programName<%=centerNumber%>").val();
	var item = $("#center_block_orgName<%=centerNumber%>");
	//if(programName!=null && programName!="") {
	//	$.get('ocan_form_getProgramNumber.jsp?ocanStaffFormId='+ocanStaffFormId+'&ocanType='+ocanType+'&demographicId='+demographicId+'&center_num=<%=centerNumber%>'+'&LHIN_code='+LHIN_code+'&orgName='+orgName+'&programName='+programName, function(data) {
	//			  item.append(data);					 
	//			});	
	//}
});



function changeProgramName(selectProgramBox) {
	var newCount = $("#center_count").val(); 
		
	var selectProgramBoxId = selectProgramBox.id;
	var program_priority = selectProgramBoxId.charAt(selectProgramBoxId.length-1);
	var selectProgramValue = selectProgramBox.options[selectProgramBox.selectedIndex].value;
	
	var LHIN_code = $("#serviceUseRecord_orgLHIN"+program_priority).val();
	var orgName = $("#serviceUseRecord_orgName"+program_priority).val();	
  	var programName = $("#serviceUseRecord_programName"+program_priority).val();
  	
	var demographicId='<%=currentDemographicId%>';

	if(document.getElementById("serviceUseRecord_programNumber" + program_priority) == null) {
			$.get('ocan_form_getProgramNumber.jsp?demographicId='+demographicId+'&center_num='+program_priority+'&LHIN_code='+LHIN_code+'&orgName='+orgName+'&programName='+programName, function(data) {
				  $("#center_block_orgName"+program_priority).append(data);					 
				});														
		}
		if(document.getElementById("serviceUseRecord_programNumber" + program_priority) != null) {
			$("#center_programNumber"+program_priority).remove();
			$.get('ocan_form_getProgramNumber.jsp?demographicId='+demographicId+'&center_num='+program_priority+'&LHIN_code='+LHIN_code+'&orgName='+orgName+'&programName='+programName, function(data) {
				  $("#center_block_orgName"+program_priority).append(data);					 
				});	
		}
}



</script>



<div id="center_programName<%=centerNumber%>">
	<table>
		<tr>
			<td class="genericTableHeader">Organization Number </td>
			<td class="genericTableData">			
				<%=OcanForm.renderAsOrgProgramNumberTextField(ocanStaffForm.getId(),"serviceUseRecord_orgNumber"+centerNumber,OcanForm.getOcanConnexOrgNumber(LHIN_code, orgName), 10, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Program Name </td>
			<td class="genericTableData">			
				<%=OcanForm.renderAsOrgProgramNumberTextField(ocanStaffForm.getId(), "serviceUseRecord_programName"+centerNumber, OcanForm.getOcanConnexProgramName(LHIN_code,orgName), 100, prepopulationLevel)%>
			</td>
		</tr>
		
		
		
		
		<tr>
			<td class="genericTableHeader">Program Number</td>
			<td class="genericTableData">			
				<%=OcanForm.renderAsOrgProgramNumberTextField(ocanStaffForm.getId(),"serviceUseRecord_programNumber"+centerNumber,OcanForm.getOcanConnexProgramNumber(LHIN_code, orgName), 10, prepopulationLevel)%>
			</td>
		</tr>
	</table>
</div>

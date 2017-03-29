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
	int centerNumber = Integer.parseInt(request.getParameter("center_num"));
%>

<script type="text/javascript">
$('document').ready(function() {
	//load mental health centres orgnaization name
	var demographicId='<%=currentDemographicId%>';
	var ocanType='<%=ocanType%>';
	var ocanStaffFormId = '<%=ocanStaffFormId%>';
	var cenCount = $("#center_count").val();
	var LHIN_code = $("#serviceUseRecord_orgLHIN<%=centerNumber%>").val(); 
	var item=$("#center_block_orgName<%=centerNumber%>");
	var prepopulate = '<%=prepopulate%>';
	if(LHIN_code != null && LHIN_code!="") {
		$.get('ocan_form_getOrgName.jsp?prepopulate='+prepopulate+'&ocanStaffFormId='+ocanStaffFormId+'&ocanType='+ocanType+'&demographicId='+demographicId+'&center_num=<%=centerNumber%>'+'&LHIN_code='+LHIN_code, function(data) {
			item.append(data);					 
		});				
	} 
});
</script>



<div id="center_<%=centerNumber%>">
	<table>
	<tr>
			<td colspan="2">Mental Health Functional Centre <%=centerNumber %></td>
		</tr>
		<tr>
			<td class="genericTableHeader">OCAN Lead</td>
			<td class="genericTableData">
				<select name="serviceUseRecord_OCANLead<%=centerNumber %>" id="serviceUseRecord_OCANLead<%=centerNumber %>" class="{validate: {required:true}}">					
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_OCANLead"+centerNumber, OcanForm.getOcanFormOptions("OCAN Lead Assessment"), prepopulationLevel)%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Staff Worker Name </td>
			<td class="genericTableData">
				<% String input = "";
				input = OcanForm.renderAsTextField(ocanStaffForm.getId(),"serviceUseRecord_staffWorker"+centerNumber,64, prepopulationLevel);
				input = input.substring(0,input.length()-2);
				input = input.concat(" class=\"{validate: {required:true}}\"/>");
				%>
				<%=input %>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Staff Worker Phone Number </td>
			<td class="{validate: {required:true}}">
				<% input = "";
				input = OcanForm.renderAsTextField(ocanStaffForm.getId(),"serviceUseRecord_phoneNumber"+centerNumber,32, prepopulationLevel);
				input = input.substring(0,input.length()-2);
				input = input.concat(" class=\"{validate: {required:true}}\"/>");
				%>
				<%=input %>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Ext </td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"serviceUseRecord_ext"+centerNumber,16, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Organizations LHIN</td>
			<td class="genericTableData">
				<select name="serviceUseRecord_orgLHIN<%=centerNumber %>" id="serviceUseRecord_orgLHIN<%=centerNumber %>" onchange="changeOrgLHIN(this);" class="{validate: {required:true}}">					
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_orgLHIN"+centerNumber, OcanForm.getOcanFormOptions("LHIN code"), prepopulationLevel)%>
				</select>					
			</td>
		</tr>	
		
		<tr>
			<td colspan="2">
				
				<div id="center_block_orgName<%=centerNumber %>">
					<!-- results from adding/removing organization name will go into this block -->
				</div>
			</td>
		</tr>	
		
		
		
		<tr>
			<td class="genericTableHeader">Organization Name - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"serviceUseRecord_orgNameOther"+centerNumber,128, prepopulationLevel)%>
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Organization Number - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"serviceUseRecord_orgNumberOther"+centerNumber,4, prepopulationLevel)%>
			</td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">Program Name - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"serviceUseRecord_programNameOther"+centerNumber,128, prepopulationLevel)%>
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Program Number - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"serviceUseRecord_programNumberOther"+centerNumber,4, prepopulationLevel)%>
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Functional Center Name</td>
			<td class="genericTableData">
				<select name="serviceUseRecord_functionName<%=centerNumber %>" class="{validate: {required:true}}">					
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_functionName"+centerNumber, OcanForm.getOcanFormOptions("MIS Functional Centre List"), prepopulationLevel)%>
				</select>					
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Functional Center Name - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"serviceUseRecord_functionNameOther"+centerNumber,128, prepopulationLevel)%>
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Functional Center Number - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextField(ocanStaffForm.getId(),"serviceUseRecord_functionNumberOther"+centerNumber,16, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Service Delivery LHIN</td>
			<td class="genericTableData">
				<select name="serviceUseRecord_service_delivery_lhin<%=centerNumber %>" class="{validate: {required:true}}">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_service_delivery_lhin"+centerNumber, OcanForm.getOcanFormOptions("LHIN code"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Referral Source</td>
			<td class="genericTableData">
				<select name="serviceUseRecord_source_of_referral<%=centerNumber %>" class="{validate: {required:true}}">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_source_of_referral"+centerNumber, OcanForm.getOcanFormOptions("Referral Source"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Request for Service Date (YYYY-MM-DD)</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "serviceUseRecord_requestForServiceDate"+centerNumber,false,prepopulationLevel)%>				
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Service Decision Date (YYYY-MM-DD)</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "serviceUseRecord_serviceDecisionDate"+centerNumber,false,prepopulationLevel)%>				
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Accepted</td>
			<td class="genericTableData">
				<select name="serviceUseRecord_accepted<%=centerNumber %>" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_accepted"+centerNumber, OcanForm.getOcanFormOptions("Yes No"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Service Initiation Date (YYYY-MM-DD)</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "serviceUseRecord_serviceInitiationDate"+centerNumber,false,prepopulationLevel)%>				
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Exit Date (YYYY-MM-DD)</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "serviceUseRecord_exitDate"+centerNumber,false,prepopulationLevel)%>				
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Exit Disposition</td>
			<td class="genericTableData">
				<select name="serviceUseRecord_exitDisposition<%=centerNumber %>" id="serviceUseRecord_exitDisposition<%=centerNumber %>" >
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_exitDisposition"+centerNumber, OcanForm.getOcanFormOptions("Exit Disposition"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>	
	</table>
</div>

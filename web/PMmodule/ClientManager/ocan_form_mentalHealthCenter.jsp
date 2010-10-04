<%@page import="org.oscarehr.common.model.OcanStaffForm"%>
<%@page import="org.oscarehr.PMmodule.model.Admission"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	
	OcanStaffForm ocanStaffForm=OcanForm.getOcanStaffForm(currentDemographicId, prepopulationLevel);		

	int centerNumber = Integer.parseInt(request.getParameter("center_num"));
%>
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
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_staffWorker"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Staff Worker Phone Number </td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_phoneNumber"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Ext </td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_ext"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Organizations LHIN</td>
			<td class="genericTableData">
				<select name="serviceUseRecord_orgLHIN<%=centerNumber %>" class="{validate: {required:true}}">					
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_orgLHIN"+centerNumber, OcanForm.getOcanFormOptions("LHIN code"), prepopulationLevel)%>
				</select>					
			</td>
		</tr>	
		
		<tr>
			<td class="genericTableHeader">Organization Name</td>
			<td class="genericTableData">
			<!--  
				<input type="text" id="serviceUseRecord_orgName<%=centerNumber %>" name="serviceUseRecord_orgName<%=centerNumber %>"value="<%=LoggedInInfo.loggedInInfo.get().currentFacility.getName() %>" readonly=readonly onfocus="this.blur();"/>
			-->
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_orgName"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Organization Name - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_orgNameOther"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Organization Number</td>
			<td class="genericTableData">
			<!--  
				<input type="text" id="serviceUseRecord_orgNumber<%=centerNumber %>" name="serviceUseRecord_orgNumber<%=centerNumber %>" value="<%=LoggedInInfo.loggedInInfo.get().currentFacility.getOcanServiceOrgNumber() %>" readonly=readonly onfocus="this.blur();"/>
			-->
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_orgNumber"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Organization Number - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_orgNumberOther"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<!--    
		<tr>
			<td class="genericTableHeader">Program Name</td>
			<td class="genericTableData">
				<select name="admissionId<%=centerNumber %>">
					<%
						for (Admission admission : OcanForm.getAdmissions(currentDemographicId))
						{
							String selected="";
							
							if (ocanStaffForm.getAdmissionId()!=null && ocanStaffForm.getAdmissionId().intValue()==admission.getId().intValue()) selected="selected=\"selected\"";
							
							%>
								<option <%=selected%> value="<%=admission.getId()%>"><%=OcanForm.getEscapedAdmissionSelectionDisplay(admission)%></option>
							<%
						}
					%>
				</select>
			</td>
		</tr>
		-->
		<tr>
			<td class="genericTableHeader">Program Name</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_programName"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Program Name - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_programNameOther"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Program Number</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_programNumber"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Program Number - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_programNumberOther"+centerNumber,1,30, prepopulationLevel)%>
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
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_functionNameOther"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Functional Center Number - Other</td>
			<td class="genericTableData">
				<%=OcanForm.renderAsTextArea(ocanStaffForm.getId(),"serviceUseRecord_functionNumberOther"+centerNumber,1,30, prepopulationLevel)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Service Delivery LHIN</td>
			<td class="genericTableData">
				<select name="service_delivery_lhin<%=centerNumber %>" class="{validate: {required:true}}">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "service_delivery_lhin"+centerNumber, OcanForm.getOcanFormOptions("LHIN code"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>						
		<tr>
			<td class="genericTableHeader">Referral Source</td>
			<td class="genericTableData">
				<select name="source_of_referral<%=centerNumber %>" class="{validate: {required:true}}">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "source_of_referral"+centerNumber, OcanForm.getOcanFormOptions("Referral Source"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Request for Service Date (YYYY-MM-DD)</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "requestForServiceDate"+centerNumber,false,prepopulationLevel)%>				
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Service Decision Date (YYYY-MM-DD)</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "serviceDecisionDate"+centerNumber,false,prepopulationLevel)%>				
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Accepted</td>
			<td class="genericTableData">
				<select name="serviceUseRecord_accepted<%=centerNumber %>" class="{validate: {required:true}}">
					<%=OcanForm.renderAsSelectOptions(ocanStaffForm.getId(), "serviceUseRecord_accepted"+centerNumber, OcanForm.getOcanFormOptions("Yes No"),prepopulationLevel)%>
				</select>					
			</td>
		</tr>	
		<tr>
			<td class="genericTableHeader">Service Initiation Date (YYYY-MM-DD)</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "serviceInitiationDate"+centerNumber,false,prepopulationLevel)%>				
			</td>
		</tr>		
		<tr>
			<td class="genericTableHeader">Exit Date (YYYY-MM-DD)</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<%=OcanForm.renderAsDate(ocanStaffForm.getId(), "exitDate"+centerNumber,false,prepopulationLevel)%>				
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
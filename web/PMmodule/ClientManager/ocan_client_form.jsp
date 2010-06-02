<%@page import="org.oscarehr.common.model.OcanClientForm"%>
<%@page import="org.oscarehr.common.model.Demographic"%>
<%@page import="org.oscarehr.PMmodule.web.OcanForm"%>
<%@page import="org.oscarehr.util.LoggedInInfo"%>

<%@include file="/layouts/caisi_html_top-jquery.jspf"%>


<%
	int currentDemographicId=Integer.parseInt(request.getParameter("demographicId"));	
	int prepopulationLevel = OcanForm.PRE_POPULATION_LEVEL_ALL;
	
	OcanClientForm ocanClientForm=OcanForm.getOcanClientForm(currentDemographicId,prepopulationLevel);		
	boolean printOnly=request.getParameter("print")!=null;
	if (printOnly) request.setAttribute("noMenus", true);
%>

<!-- 
<script type="text/javascript" src="<%=request.getContextPath()%>/PMmodule/ClientManager/ocan_staff_form_validation.js"></script>
-->	

<script>
//setup validation plugin
$("document").ready(function() {	
	$("#ocan_client_form").validate({meta: "validate"});	
});


function submitOcanClientForm() {
	var status = document.getElementById('assessment_status').value;
	if(status == 'Active') {
		$('#ocan_client_form').unbind('submit').submit();		
		return true;
	}
	if(!$("#ocan_client_form").valid()) {
		alert('Validation failed. Please check all required fields highlighted');
		return false;
	}
	return true;
}

</script>


<style>
.error {color:red;}
</style>


<form id="ocan_client_form" name="ocan_client_form" action="ocan_client_form_action.jsp" onsubmit="return submitOcanClientForm()">
	<input type="hidden" id="assessment_status" name="assessment_status" value=""/>
	
	<h3>OCAN Consumer Self-Assessment (v2.0)</h3>

	<br />
	
	<table style="margin-left:auto;margin-right:auto;background-color:#f0f0f0;border-collapse:collapse">

		<tr>
			<td class="genericTableHeader">Start Date</td>
			<td class="genericTableData">
				<input id="startDate" name="startDate" onfocus="this.blur()" readonly="readonly" class="{validate: {required:true}}" type="text" value="<%=ocanClientForm.getFormattedStartDate()%>"> <img title="Calendar" id="cal_startDate" src="../../images/cal.gif" alt="Calendar" border="0"><script type="text/javascript">Calendar.setup({inputField:'startDate',ifFormat :'%Y-%m-%d',button :'cal_startDate',align :'cr',singleClick :true,firstDay :1});</script>		
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Last Name</td>
			<td class="genericTableData">
				<input type="text" name="lastName" id="lastName" value="<%=ocanClientForm.getLastName()%>" class="{validate: {required:true}}"/>
			</td>			
		</tr>
		<tr>
			<td class="genericTableHeader">First Name</td>
			<td class="genericTableData">
				<input type="text" name="firstName" id="firstName" value="<%=ocanClientForm.getFirstName()%>" class="{validate: {required:true}}"/>
			</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">Date of Birth</td>
			<td class="genericTableData" class="{validate: {required:true}}">
				<%=OcanForm.renderAsDate(ocanClientForm.getId(), "date_of_birth",true,ocanClientForm.getDateOfBirth(),prepopulationLevel)%>										
			</td>
		</tr>

		<tr>
			<td colspan="2">1. Accommodation</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person lack a current place to stay?</td>
			<td class="genericTableData">
				<select name="1_1" id="1_1">
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "1_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"1_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
		

		<tr>
			<td colspan="2">2. Food</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty in getting enough to eat?</td>
			<td class="genericTableData">
				<select name="2_1" id="2_1">
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "2_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"2_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

	
		<tr>
			<td colspan="2">3. Looking after the home</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty looking after the home?</td>
			<td class="genericTableData">
				<select name="3_1" id="3_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "3_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"3_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
		

		<tr>
			<td colspan="2">4. Self-care</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty with self-care?</td>
			<td class="genericTableData">
				<select name="4_1" id="4_1">
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "4_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"4_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

	
		<tr>
			<td colspan="2">5. Daytime activities</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty with regular, appropriate daytime activities?</td>
			<td class="genericTableData">
				<select name="5_1" id="5_1">
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "5_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"5_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
		

	
		<tr>
			<td colspan="2">6. Physical health</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have any physical disability or any physical illness?</td>
			<td class="genericTableData">
				<select name="6_1" id="6_1">
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "6_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"6_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">7. Psychotic Symptoms</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have any psychotic symptoms?</td>
			<td class="genericTableData">
				<select name="7_1"  id="7_1">
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "7_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

					
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"7_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

	

		<tr>
			<td colspan="2">8. Information on condition and treatment</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Has the person had clear verbal or written information about condition and treatment?</td>
			<td class="genericTableData">
				<select name="8_1" id="8_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "8_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"8_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">9. Psychological Distress</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person suffer from current psychological distress?</td>
			<td class="genericTableData">
				<select name="9_1"  id="9_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "9_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"9_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">10. Safefy to self</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Is the person a danger to him- or herself?</td>
			<td class="genericTableData">
				<select name="10_1" id="10_1">
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "10_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"10_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

					
		<tr>
			<td colspan="2">11. Safefy to others</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Is the person a current or potential risk to other's people safety?</td>
			<td class="genericTableData">
				<select name="11_1" id="11_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "11_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
			
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"11_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
	
					
		<tr>
			<td colspan="2">12. Alcohol</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Does the person drink excessively, or have a problem controlling his or her drinking?</td>
			<td class="genericTableData">
				<select name="12_1" id="12_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "12_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"12_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
					
		<tr>
			<td colspan="2">13. Drugs</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have problems with drug misuse?</td>
			<td class="genericTableData">
				<select name="13_1" id="13_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "13_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"13_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">14. Other Addictions</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have problems with addictions?</td>
			<td class="genericTableData">
				<select name="14_1" id="14_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "14_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"14_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">15. Company</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Does the person need help with social contact?</td>
			<td class="genericTableData">
				<select name="15_1" id="15_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "15_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
					
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"15_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">16. Intimate relationships</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Does the person have any difficulty in finding a partner or in maintaining a close relationship?</td>
			<td class="genericTableData">
				<select name="16_1" id="16_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "16_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"16_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">17. Sexual Expression</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have problems with his or her sex life?</td>
			<td class="genericTableData">
				<select name="17_1" id="17_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "17_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
	
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"17_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">18. Child care</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty looking after his or her children?</td>
			<td class="genericTableData">
				<select name="18_1" id="18_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "18_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
					
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"18_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			



		<tr>
			<td colspan="2">19. Other dependents</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have difficulty looking after other dependents?</td>
			<td class="genericTableData">
				<select name="19_1" id="19_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "19_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"19_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2">20. Basic education</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person lack basic skills in numeracy and literacy?</td>
			<td class="genericTableData">
				<select name="20_1" id="20_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "20_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"20_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">21. Telephone</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person lack basic skills in getting access to or using a telephone?</td>
			<td class="genericTableData">
				<select name="21_1" id="21_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "21_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"21_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			
	
							
		<tr>
			<td colspan="2">22. Transport</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have any problems using public transport?</td>
			<td class="genericTableData">
				<select name="22_1" id="22_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "22_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
	
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"22_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">23. Money</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1. Does the person have problems budgeting his or her money?</td>
			<td class="genericTableData">
				<select name="23_1" id="23_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "23_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"23_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		
		<tr>
			<td colspan="2">24. Benefits</td>
		</tr>			

		<tr>
			<td class="genericTableHeader">1.  Is the person definitely receiving all the benefits that he or she is entitled to?</td>
			<td class="genericTableData">
				<select name="24_1" id="24_1" >
					<%=OcanForm.renderAsSelectOptions(ocanClientForm.getId(), "24_1", OcanForm.getOcanFormOptions("Camberwell Need"),prepopulationLevel,true)%>
				</select>					
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Comments</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"24_comments",5,30,prepopulationLevel,true)%>
			</td>
		</tr>			

		<tr>
			<td colspan="2" vheight="4"></td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">What are your hopes for the future?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"hopes_future",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">What do you think you need in order to get there?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"hope_future_need",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">How do you view your mental health?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"view_mental_health",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Is spirituality an important part of your life?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"sprituality",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Is culture (heritage) an important part of your life?</td>
			<td class="genericTableData">
						<%=OcanForm.renderAsTextArea(ocanClientForm.getId(),"culture_heritage",5,30,prepopulationLevel,true)%>
			</td>
		</tr>
	
		<tr>
			<td colspan="2" vheight="4"></td>
		</tr>

		<tr>
			<td class="genericTableHeader">Completion Date</td>
			<td class="genericTableData">
					<input id="completionDate" name="completionDate" onfocus="this.blur()" readonly="readonly" class="{validate: {required:true}}" type="text" value="<%=ocanClientForm.getFormattedCompletionDate()%>"> <img title="Calendar" id="cal_completionDate" src="../../images/cal.gif" alt="Calendar" border="0"><script type="text/javascript">Calendar.setup({inputField:'completionDate',ifFormat :'%Y-%m-%d',button :'cal_completionDate',align :'cr',singleClick :true,firstDay :1});</script>									
			</td>
		</tr>

		<tr style="background-color:white">
			<td colspan="2">
				<br />
				<input type="hidden" name="clientId" value="<%=currentDemographicId%>" />
				<%
					if (!printOnly)
					{
						%>
				<input type="submit" name="submit" value="Submit" onclick="document.getElementById('assessment_status').value='Complete';"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<input type="submit" name="submit" value="Save Draft"  onclick="document.getElementById('assessment_status').value='Active'; document.getElementById('completionDate').value=''"/>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<%
					}
				%>					
				<input type="button" name="cancel" value="Cancel" onclick="history.go(-1)" />
				<%
					if (printOnly)
					{
						%>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<input type="button" name="print" value="Print" onclick="window.print()">
						<%
					}
				%>
			</td>
		</tr>		
	</table>
	<%
		if (printOnly)
		{
			%>
				<script>
					setEnabledAll(document.ocan_client_form, false);

					document.getElementsByName('cancel')[0].disabled=false;
					document.getElementsByName('print')[0].disabled=false;
				</script>
			<%
		}
	%>

</form>


<%@include file="/layouts/caisi_html_bottom2.jspf"%>

<%-- 
/*
* Copyright (c) 2007-2009. CAISI, Toronto. All Rights Reserved.
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. 
* 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. 
* 
* You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.  
* 
* This software was written for 
* CAISI, 
* Toronto, Ontario, Canada 
*/
--%>
<%@page import="java.util.*"%>
<%@page import="org.caisi.dao.*"%>
<%@page import="org.caisi.model.*"%>
<%@page import="org.oscarehr.common.dao.SecRoleDao"%>
<%@page import="org.oscarehr.common.model.SecRole"%>
<%@page import="org.oscarehr.PMmodule.model.*"%>
<%@page import="org.oscarehr.PMmodule.dao.*"%>
<%@page import="org.oscarehr.util.SpringUtils"%>
<%@page import="java.text.DateFormatSymbols"%>
<%@page import="org.oscarehr.web.Cds4FunctionCode"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="org.oscarehr.PMmodule.web.CdsForm4"%>
<%@page import="org.oscarehr.common.model.CdsFormOption"%>

<%
	ProgramDao programDao = (ProgramDao) SpringUtils.getBean("programDao");
	
	List<Program> allPrograms=programDao.getAllActivePrograms();
%>

<%@include file="/layouts/caisi_html_top.jspf"%>



<%@page import="org.oscarehr.web.CdsManualLineEntry"%><h1>CDS Reports</h1>

<script type="text/javascript">
	function validate(form)
	{
		var fields = form.elements;

		if (fields.serviceDeliveryLhin.value==null||fields.serviceDeliveryLhin.value=="")
		{
			alert('10b. Service Delivery LHIN, is required.');
			return(false);
		}
	}
</script>
				
<form method="post" action="cds_4_report_export.jsp" onsubmit="return(validate(this))">
	<table class="borderedTableAndCells">
		<tr>
			<td>CDS version</td>
			<td>CDS-MH 4.x</td>
		</tr>
		<tr>
			<td>Caisi programs to include</td>
			<td>
				<select name="caisiProgramIds" multiple="multiple" style="width:20em;height:8em">
					<%
						for (Program program : allPrograms)
						{
							if (program.isBed() || program.isService())
							{
								%>
									<option value="<%=program.getId() %>"><%=program.getName()%></option>
								<%
							}
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Date Range Start</td>
			<td>
				<select name="startYear">
				<%
					GregorianCalendar cal=new GregorianCalendar();
					int year=cal.get(GregorianCalendar.YEAR);
					for (int i=0; i<10; i++)
					{
						%>
							<option value="<%=year-i%>"><%=year-i%></option>
						<%
					}
				%>
				</select>
				-
				<select name="startMonth">
				<%
					DateFormatSymbols dateFormatSymbols=DateFormatSymbols.getInstance();
					String[] months=dateFormatSymbols.getShortMonths();
					
					for (int i=1; i<13; i++)
					{
						%>
							<option value="<%=i%>" title="<%=months[i-1]%>"><%=i%></option>
						<%
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Date Range End (inclusive)</td>
			<td>
				<select name="endYear">
				<%
					for (int i=0; i<10; i++)
					{
						%>
							<option value="<%=year-i%>"><%=year-i%></option>
						<%
					}
				%>
				</select>
				-
				<select name="endMonth">
				<%
					for (int i=1; i<13; i++)
					{
						%>
							<option value="<%=i%>" title="<%=months[i-1]%>"><%=i%></option>
						<%
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
			<td>Ministries Organisation Number</td>
			<td><input type="text" name="ministryOrganisationNumber" /></td>
		</tr>
		<tr>
			<td>4. Ministries Program Number</td>
			<td><input type="text" name="ministryProgramNumber" /></td>
		</tr>
		<tr>
			<td>5. Ministries Function</td>
			<td>
				<select name="ministryFunctionCode">
					<%
						// oops I guess this breaks the theory of making the form multi version, oh well we'll sort it when V5 of the form comes out.
						for (Cds4FunctionCode code : Cds4FunctionCode.values())
						{
							%>
								<option value="<%=code.getFunctionCode()%>"><%=StringEscapeUtils.escapeHtml(code.getFunctionName())%></option>
							<%
						}
					%>
				</select>
			</td>
		</tr>
		<tr>
			<td>6. Service languages for the above programs</td>
			<td>
				<input type="checkbox" name="serviceLanguages" value="en" /> English<br />
				<input type="checkbox" name="serviceLanguages" value="fr" /> French<br />
				<input type="checkbox" name="serviceLanguages" value="other" /> Other
			</td>
		</tr>
		<tr>
			<td>7-02. Unique individuals - pre-admission</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("007-02")%>
			</td>
		</tr>
		<tr>
			<td>7-04. Multiple admissions for uniquely identified individuals</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("007-04")%>
			</td>
		</tr>
		<tr>
			<td>7a-01. Individuals Waiting for Initial Assessment</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("07a-01")%>
			</td>
		</tr>
		<tr>
			<td>7a-02. Days Waited for Initial Assessment</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("07a-02")%>
			</td>
		</tr>
		<tr>
			<td>7a-03. Individuals Waiting for Service Initiation</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("07a-03")%>
			</td>
		</tr>
		<tr>
			<td>7a-04. Days Waited for Service Initiation</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("07a-04")%>
			</td>
		</tr>
		<tr>
			<td>10b. Service Delivery LHIN</td>
			<td>
				<select multiple="multiple" name="serviceDeliveryLhin" style="height:8em">
				<%
					for (CdsFormOption option : CdsForm4.getCdsFormOptions("10b"))
					{
						String htmlEscapedName=StringEscapeUtils.escapeHtml(option.getCdsDataCategoryName());
						String lengthLimitedEscapedName=CdsForm4.limitLengthAndEscape(option.getCdsDataCategoryName());
	
						%>
							<option value="<%=StringEscapeUtils.escapeHtml(option.getCdsDataCategory())%>" title="<%=htmlEscapedName%>"><%=lengthLimitedEscapedName%></option>
						<%
					}
				%>
				</select>
			</td>
		</tr>
		<tr>
			<td>21-03. Total Number of Hospitalization Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-03")%>
			</td>
		</tr>
		<tr>
			<td>21-04. Unknown or Service Recipient Declined</td>
			<td>  
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-04")%>
			</td>
		</tr>
		<tr>
			<td>21-05. Year 1 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-05")%>
			</td>
		</tr>
		<tr>
			<td>21-06. Year 2 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-06")%>
			</td>
		</tr>
		<tr>
			<td>21-07. Year 3 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-07")%>
			</td>
		</tr>
		<tr>
			<td>21-08. Year 4 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-08")%>
			</td>
		</tr>
		<tr>
			<td>21-09. Year 5 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-09")%>
			</td>
		</tr>
		<tr>
			<td>21-10. Year 6 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-10")%>
			</td>
		</tr>
		<tr>
			<td>21-11. Year 7 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-11")%>
			</td>
		</tr>
		<tr>
			<td>21-12. Year 8 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-12")%>
			</td>
		</tr>
		<tr>
			<td>21-13. Year 9 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-13")%>
			</td>
		</tr>
		<tr>
			<td>21-14. Year 10 Hospital Days</td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("021-14")%>
			</td>
		</tr>
		<tr>
			<td>32. Formal Service Evaluation Process (check for 'yes')</td>
			<td>
				Does the function formally measure service recipient satisfaction? <input type="checkbox" name="measureServiceRecipientSatisfaction" /><br />
				Does the function formally measure service recipient family satisfaction? <input type="checkbox" name="measureServiceRecipientFamiltySatisfaction" /><br />
				Is the function involved in formal quality improvement strategies? <input type="checkbox" name="qualityImprovementStrategies" /><br />
				Does the function participate in accreditation? <input type="checkbox" name="participateInAccreditation" /><br />
			</td>
		</tr>
		<tr>
			<td>37-01. </td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("036-01")%>
			</td>
		</tr>
		<tr>
			<td>37-02. </td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("036-02")%>
			</td>
		</tr>
		<tr>
			<td>37-03. </td>
			<td>
				<%=CdsManualLineEntry.outputCdsManualLineEntryTable("036-03")%>
			</td>
		</tr>
		<tr>
			<td></td>
			<td><input type="submit" value="Download Report" /></td>
		</tr>
	</table>	
</form>


<%@include file="/layouts/caisi_html_bottom.jspf"%>

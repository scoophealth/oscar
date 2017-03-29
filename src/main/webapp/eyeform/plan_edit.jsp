<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<%@ include file="/taglibs.jsp"%>

<%@page import="org.oscarehr.eyeform.model.EyeformFollowUp"%>
<%@page import="org.oscarehr.eyeform.model.EyeformProcedureBook"%>
<%@page import="org.oscarehr.eyeform.model.EyeformTestBook"%>
<%@page import="org.oscarehr.eyeform.web.PlanAction"%>




<html>
<head>
	<title></title>
	<link rel="stylesheet" type="text/css" href='<html:rewrite page="/jsCalendar/skins/aqua/theme.css" />' />

		<link rel="stylesheet" type="text/css" media="all" href="<%=request.getContextPath()%>/share/calendar/calendar.css" title="win2k-cold-1" />
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/lang/<bean:message key="global.javascript.calendar"/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/share/calendar/calendar-setup.js"></script>
		
</head>
<body>

<center><b>Impression Plan</b></center>

<form action="<%=request.getContextPath()%>/eyeform/EyeformPlan.do" method="post">
<input type="hidden" name="method" value="save_edit"/>

<c:if test="${empty followUps}">
	<span>No Follow ups/Consults currently in plan.</span>
</c:if>
<c:if test="${not empty followUps}">
	<c:forEach var="f" items="${followUps}">
		<%
			EyeformFollowUp followUp = (EyeformFollowUp)pageContext.getAttribute("f");
		%>
		<input type="hidden" name="followup.id" value="<%=followUp.getId()%>"/>
		<!-- table it -->
		<table border="0">
           <tbody>
           <tr>
	            <td width="25%">
		            <select name="followup<%=followUp.getId()%>.type">
		            	<%=PlanAction.printFollowUpTypeOptions(followUp)%>		            	
		            </select>
	            </td>
				<td width="30%">				           
		             <input type="text" name="followup<%=followUp.getId()%>.timespan" size="4" maxlength="6" style="width: 25px;" class="special" value="<c:out value="${f.timespan}"/>"/>		             
		             <select name="followup<%=followUp.getId()%>.timeframe">
		             	<%=PlanAction.printFollowUpTimeFrameOptions(followUp)%>
		            </select>*
	            </td> 		            
	            <td width="25%">	             
		            <select name="followup<%=followUp.getId()%>.followupProvider">
		            	<%=PlanAction.printFollowUpProvidersOptions(followUp)%>       	
					</select>
				</td>
         		<td width="20%">
		            <select name="followup<%=followUp.getId()%>.urgency">
		             	<%=PlanAction.printFollowUpUrgency(followUp)%>   	
		            </select>
	           </td>				
			</tr>
           
           <tr>
	           <td colspan="5">
	           	Comment:<br/><textarea rows="5" cols="60" name="followup<%=followUp.getId()%>.comment"><c:out value="${f.comment}"/></textarea>		
	           </td>
           </tr>
            
           </tbody>
			</table>		
					
	</c:forEach>
</c:if>

<br/>

<c:if test="${empty procedures}">
	<span>No procedures currently in plan.</span>
</c:if>
<c:if test="${not empty procedures}">
	<c:forEach var="proc" items="${procedures}">
		<%
			EyeformProcedureBook proc = (EyeformProcedureBook)pageContext.getAttribute("proc");
		%>
		<input type="hidden" name="procedures.id" value="<%=proc.getId()%>"/>		
	<table>
		<tr>
			<td class="genericTableHeader">Procedure name</td>
			<td class="genericTableData">
				<input type="text" name="proc<%=proc.getId()%>.procedureName" value="<c:out value="${proc.procedureName}"/>" size="35"/>*
			</td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">Eye</td>
			<td class="genericTableData">
			<select name="proc<%=proc.getId()%>.eye">
					<%=PlanAction.printProcedureEye(proc)%>
			</select>
			</td>
		</tr>
		
		<tr>
			<td class="genericTableHeader">Urgency</td>
			<td class="genericTableData">
			<select name="proc<%=proc.getId()%>.urgency">
					<%=PlanAction.printProcedureUrgency(proc)%>
			</select>
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Location</td>
			<td class="genericTableData">
					<input type="text" name="proc<%=proc.getId()%>.location" size="35" value="<c:out value="${proc.location}"/>"/>		
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Comment</td>
			<td class="genericTableData">
					<textarea rows="5" cols="40" name="proc<%=proc.getId()%>.comment"><c:out value="${proc.comment}"/></textarea>		
			</td>
		</tr>
		</table>	
					
	</c:forEach>	
</c:if>

<br/>

<c:if test="${empty tests}">
	<span>No tests currently in plan.</span>
</c:if>
<c:if test="${not empty tests}">
	<c:forEach var="test" items="${tests}">
		<%
			EyeformTestBook test = (EyeformTestBook)pageContext.getAttribute("test");
		%>		
		<input type="hidden" name="tests.id" value="<%=test.getId()%>"/>		
	<table>
		<tr>
			<td class="genericTableHeader">Test name</td>
			<td class="genericTableData">
				<input type="text" name="test<%=test.getId()%>.testname" size="35" value="<c:out value="${test.testname}"/>"/>*
			</td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">Eye</td>
			<td class="genericTableData">
				<select name="test<%=test.getId()%>.eye">
					<%=PlanAction.printTestEye(test) %>
				</select>
			</td>
		</tr>
						
		<tr>
			<td class="genericTableHeader">Comment</td>
			<td class="genericTableData">
					<textarea rows="5" cols="40" name="test<%=test.getId()%>.comment"><c:out value="${test.comment}"/></textarea>		
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Urgency</td>
			<td class="genericTableData">
				<select name="test<%=test.getId()%>.urgency">
					<%=PlanAction.printTestUrgency(test) %>
				</select>
			</td>
		</tr>		
	</table>
					
	</c:forEach>
</c:if>

<br/>
	<input type="submit" value="Submit" />
	&nbsp;&nbsp;
	<input type="button" name="cancel" value="Cancel" onclick="window.close()" />	
</form>
<span>*Delete by setting timespan to 0 for follow ups/consults</span>
<span>*Delete by setting procedure name to 0 for for booking procedures</span>
<span>*Delete by setting test name to 0 for booking tests</span>
</body>
</html>

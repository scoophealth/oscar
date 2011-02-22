<!--  
/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
-->
<%@page import="org.oscarehr.eyeform.model.SpecsHistory"%>

<%@ include file="/taglibs.jsp"%>


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
<br/>
<b>Follow Up/Consult:</b>
<br />

<html:form action="/eyeform/EyeformPlan.do">
 
		<input type="hidden" name="method" value="save"/>		
		
		<html:hidden property="followup.demographicNo"/>		
		<html:hidden property="followup.appointmentNo"/>		
		
		<table border="0">
           <tbody>
           <tr>
	            <td width="25%">
		            <html:select property="followup.type">
		            	<html:option value="followup">Follow up</html:option>
		            	<html:option value="consult">Consult</html:option>
		            </html:select>
	            </td>
				<td width="30%">				           
		             <html:text property="followup.timespan" size="4"  styleId="width: 25px;" styleClass="special"/>		             
		             <html:select property="followup.timeframe" styleId="width: 50px;" styleClass="special">
		             	<html:option value="days">days</html:option>
		            	<html:option value="weeks">weeks</html:option>
		            	<html:option value="months">months</html:option>
		            </html:select>
	            </td> 		            
	            <td width="25%">	             
		            <html:select property="followup.followupProvider">
		            	<c:forEach var="item" items="${providers}">
		            		<option value="<c:out value="${item.providerNo}"/>"><c:out value="${item.formattedName}"/></option>
		            	</c:forEach>            	
					</html:select>
				</td>
         		<td width="20%">
		            <html:select property="followup.urgency" styleId="width: 50px;" styleClass="special">
		             	<html:option value="routine">routine</html:option>
		            	<html:option value="asap">ASAP</html:option>            	
		            </html:select>
	           </td>				
			</tr>
           
           <tr>
	           <td colspan="5">
	           	Comment:<br/><html:textarea rows="5" cols="60" property="followup.comment"></html:textarea>		
	           </td>
           </tr>
            
           </tbody>
			</table>				
<br/>
<b>Book Procedure:</b>
<br />
	<table>
		<tr>
			<td class="genericTableHeader">Procedure name</td>
			<td class="genericTableData">
				<html:text property="proc.procedureName" size="35"/>
			</td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">Eye</td>
			<td class="genericTableData">
				<html:select property="proc.eye">
					<html:option value="OU">OU</html:option>
					<html:option value="OD">OD</html:option>
					<html:option value="OS">OS</html:option>
					<html:option value="OD then OS">OD then OS</html:option>
					<html:option value="OS then OD">OS then OD</html:option>
				</html:select>
			</td>
		</tr>
				
		<tr>
			<td class="genericTableHeader">Location</td>
			<td class="genericTableData">
					<html:text property="proc.location" size="35"/>		
			</td>
		</tr>
		<tr>
			<td class="genericTableHeader">Comment</td>
			<td class="genericTableData">
					<html:textarea rows="5" cols="40" property="proc.comment"></html:textarea>		
			</td>
		</tr>
		</table>
<br/>

<b>Book Test:</b>
<br />

	<table>
		<tr>
			<td class="genericTableHeader">Test name</td>
			<td class="genericTableData">
				<html:text property="test.testname" size="35"/>
			</td>
		</tr>
		
		
		<tr>
			<td class="genericTableHeader">Eye</td>
			<td class="genericTableData">
				<html:select property="test.eye">
					<html:option value="OU">OU</html:option>
					<html:option value="OD">OD</html:option>
					<html:option value="OS">OS</html:option>
					<html:option value="n/a">n/a</html:option>			
				</html:select>
			</td>
		</tr>
						
		<tr>
			<td class="genericTableHeader">Comment</td>
			<td class="genericTableData">
					<html:textarea rows="5" cols="40" property="test.comment"></html:textarea>		
			</td>
		</tr>

		<tr>
			<td class="genericTableHeader">Urgency</td>
			<td class="genericTableData">
				<html:select property="test.urgency">
					<html:option value="routine">routine</html:option>
					<html:option value="ASAP">ASAP</html:option>
					<html:option value="PTNV">PTNV</html:option>					
				</html:select>
			</td>
		</tr>		
	</table>

<br/>
	<html:submit value="Submit" />
	&nbsp;&nbsp;
	<input type="button" name="cancel" value="Cancel" onclick="window.close()" />

			</td>
		</tr>
</html:form>


</body>
</html>

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

<%@page import="org.oscarehr.eyeform.model.EyeformSpecsHistory"%>


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
Follow Up/Consult
<br /><br /><br />

<html:form action="/eyeform/FollowUp.do">
 

		<input type="hidden" name="method" value="save"/>
		
		<html:hidden property="followup.demographicNo"/>
		
		<html:hidden property="followup.appointmentNo"/>
		
		
		<table border="0">
           <tbody><tr>
            <td width="25%">
            <html:select property="followup.type">
            	<html:option value="followup">Follow up</html:option>
            	<html:option value="consult">Consult</html:option>
            </html:select>
            </td>
            <td width="25%">
             Doctor:
            <html:select property="followup.followupProvider">
            	<c:forEach var="item" items="${providers}">
            		<option value="<c:out value="${item.providerNo}"/>"><c:out value="${item.formattedName}"/></option>
            	</c:forEach>            	
			</html:select>
			</td>
			</tr>
			<tr>
			<td colspan="2">
			
           
             <html:text property="followup.timespan" size="4"  styleId="width: 25px;" styleClass="special"/>
             
             <html:select property="followup.timeframe" styleId="width: 50px;" styleClass="special">
             	<html:option value="days">days</html:option>
            	<html:option value="weeks">weeks</html:option>
            	<html:option value="months">months</html:option>
            </html:select>
            </td> 			                       
            </tr>
           <tr>
           <td>
            <html:select property="followup.urgency" styleId="width: 50px;" styleClass="special">
             	<html:option value="routine">routine</html:option>
            	<html:option value="asap">ASAP</html:option>            	
            </html:select>
           </td>
           </tr>
           <tr>
           <td>
           	Comment:<html:textarea rows="5" cols="40" property="followup.comment"></html:textarea>		
           </td>
           </tr>
            <tr>
            <td><br/><html:submit value="submit"/></td>
            </tr>
           </tbody>
			</table>
		
			

</html:form>

</body>
</html>
